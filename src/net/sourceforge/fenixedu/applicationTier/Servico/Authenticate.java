package net.sourceforge.fenixedu.applicationTier.Servico;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import jvstm.TransactionalCommand;
import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.kerberos.KerberosException;
import net.sourceforge.fenixedu.util.kerberos.Script;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixframework.pstm.Transaction;
import pt.utl.ist.fenix.tools.util.FileUtils;
import edu.yale.its.tp.cas.client.CASAuthenticationException;
import edu.yale.its.tp.cas.client.CASReceipt;
import edu.yale.its.tp.cas.client.ProxyTicketValidator;

/**
 * @author Luis Cruz
 * 
 */
public class Authenticate extends FenixService implements Serializable {

    private static final String URL_ENCODING = "UTF-8";

    protected static final Logger logger = Logger.getLogger(Authenticate.class);

    protected static final Map allowedRolesByHostname = new HashMap();

    protected static final boolean validateExpirationDate;

    private static String buildVersion = null;

    static {
	validateExpirationDate = PropertiesManager.getBooleanProperty("validateExpirationDate");
	final String propertiesFilename = "/.authenticationServiceHostnamesFiltering.properties";
	try {
	    final Properties properties = new Properties();
	    PropertiesManager.loadProperties(properties, propertiesFilename);
	    for (final Iterator iterator = properties.entrySet().iterator(); iterator.hasNext();) {
		final Entry entry = (Entry) iterator.next();
		final String hostnameKey = (String) entry.getKey();
		final String rolesList = (String) entry.getValue();

		final String hostname = hostnameKey.substring(16);
		final String[] roles = rolesList.split(",");

		final Set rolesSet = new HashSet(roles.length);
		for (int i = 0; i < roles.length; i++) {
		    final RoleType roleType = RoleType.valueOf(roles[i].trim());
		    if (LogLevel.INFO) {
			logger.info("Host: " + hostname + " provides role: " + roleType.toString() + '.');
		    }
		    rolesSet.add(roleType);
		}
		allowedRolesByHostname.put(hostname, rolesSet);
	    }

	    final InputStream inputStream = Authenticate.class.getResourceAsStream("/.build.version");
	    buildVersion = FileUtils.readFile(inputStream);
	} catch (IOException e) {
	    throw new RuntimeException("Unable to load " + propertiesFilename + ". User authentication is therefor not possible.");
	}
    }

    protected class UserView implements IUserView {

	final private DomainReference<Person> personRef;

	final private Collection<RoleType> roleTypes;

	private DateTime expirationDate;

	private transient Collection<Role> roles;

	private transient String privateConstantForDigestCalculation;

	private final DateTime userCreationDateTime = new DateTime();

	private UserView(final Person person, final Set allowedRoles) {
	    this.personRef = new DomainReference<Person>((Person) person);

	    final Collection<Role> roles = getInfoRoles(person, allowedRoles);
	    if (roles != null) {
		final SortedSet<RoleType> rolesSet = new TreeSet<RoleType>();
		for (final Role role : roles) {
		    rolesSet.add(role.getRoleType());
		}
		this.roleTypes = Collections.unmodifiableSortedSet(rolesSet);
	    } else {
		this.roleTypes = null;
	    }
	}

	private UserView(final Person person, final Set allowedRoles, final DateTime expirationDate) {
	    this(person, allowedRoles);
	    setExpirationDate(expirationDate);
	}

	public boolean hasRoleType(final RoleType roleType) {
	    return roleTypes == null ? false : roleTypes.contains(roleType);
	}

	public Person getPerson() {
	    return personRef.getObject();
	}

	public String getUtilizador() {
	    return getPerson().getUsername();
	}

	public Collection<RoleType> getRoleTypes() {
	    return roleTypes;
	}

	public String getFullName() {
	    return getPerson().getName();
	}

	private void setExpirationDate(DateTime expirationDate) {
	    this.expirationDate = expirationDate;
	}

	public DateTime getExpirationDate() {
	    return expirationDate;
	}

	@Override
	public boolean equals(Object obj) {
	    if (!(obj instanceof UserView)) {
		return false;
	    }

	    UserView other = (UserView) obj;
	    return this.personRef.equals(other.personRef) && this.roleTypes.equals(other.roleTypes);
	}

	@Override
	public int hashCode() {
	    return this.personRef.hashCode() + this.roleTypes.hashCode();
	}

	public String getPrivateConstantForDigestCalculation() {
	    if (privateConstantForDigestCalculation == null) {
		final Person person = getPerson();
		final User user = person.getUser();
		final Login login = user.readUserLoginIdentification();
		privateConstantForDigestCalculation = user.getUserUId() + login.getPassword() + buildVersion;
	    }
	    return privateConstantForDigestCalculation;
	}

	@Override
	public String getUsername() {
	    return getUtilizador();
	}

	@Override
	public boolean hasRole(String role) {
	    return hasRoleType(RoleType.valueOf(role));
	}

	@Override
	public DateTime getLastLogoutDateTime() {
	    final Person person = getPerson();
	    final User user = person.getUser();
	    return user == null ? null : user.getLogoutDateTime();
	}

	@Override
	public DateTime getUserCreationDateTime() {
	    return userCreationDateTime;
	}
    }

    public static final boolean isValidUserView(IUserView userView) {
	return userView instanceof UserView;
    }

    public IUserView run(final String username, final String password, final String requestURL, final String remoteHost)
	    throws ExcepcaoAutenticacao, FenixServiceException, ExcepcaoPersistencia {

	Person person = Person.readPersonByUsernameWithOpenedLogin(username);
	if (person == null || !PasswordEncryptor.areEquals(person.getPassword(), password)) {
	    throw new ExcepcaoAutenticacao("bad.authentication");
	}

	setLoginHostNameAndDateTime(remoteHost, person);

	return getUserView(person, requestURL);
    }

    protected IUserView getUserView(final Person person, final String requestURL) {
	return getUserView(person, requestURL, null);
    }

    protected IUserView getUserView(final Person person, final String requestURL, final DateTime expirationDate) {
	final Set allowedRoles = getAllowedRolesByHostname(requestURL);
	return new UserView(person, allowedRoles, expirationDate);
    }

    public static class NonExistingUserException extends ExcepcaoAutenticacao {

	public NonExistingUserException(final String message) {
	    super(message);
	}
    }

    public IUserView run(final CASReceipt receipt, final String requestURL, final String remoteHost) throws ExcepcaoAutenticacao,
	    ExcepcaoPersistencia {
	final String username = receipt.getUserName();

	Person person = Person.readPersonByUsernameWithOpenedLogin(username);
	if (person == null) {
	    System.out.println("Attempted login of non-existent user: " + username);
	    throw new NonExistingUserException("error.Exception");
	}

	setLoginHostNameAndDateTime(remoteHost, person);

	if (validateExpirationDate) {
	    try {
		final DateTime expirationDate = Script.returnExpirationDate(person.getIstUsername());
		return getUserView(person, requestURL, expirationDate);
	    } catch (KerberosException e) {
		return getUserView(person, requestURL);
	    }
	} else {
	    return getUserView(person, requestURL);
	}
    }

    public static class RegisterUserLoginThread extends Thread implements TransactionalCommand {

	private final Integer userID;
	private final String remoteHost;

	public RegisterUserLoginThread(final User user, final String remoteHost) {
	    userID = user.getIdInternal();
	    this.remoteHost = remoteHost;
	}

	public void run() {
	    Transaction.withTransaction(this);
	}

	public void doIt() {
	    final User user = rootDomainObject.readUserByOID(userID);
	    user.setLastLoginHost(user.getCurrentLoginHost());
	    user.setLastLoginDateTimeDateTime(user.getCurrentLoginDateTimeDateTime());
	    user.setCurrentLoginDateTimeDateTime(new DateTime());
	    user.setCurrentLoginHost(remoteHost);
	}

	protected static void runThread(final User user, final String remoteHost) {
	    final RegisterUserLoginThread registerUserLoginThread = new RegisterUserLoginThread(user, remoteHost);
	    registerUserLoginThread.start();
	}
    }

    private void setLoginHostNameAndDateTime(final String remoteHost, Person person) {
	// final User user = person.getUser();
	// RegisterUserLoginThread.runThread(user, remoteHost);
    }

    public static CASReceipt getCASReceipt(final String serverName, final String casTicket, final String requestURL)
	    throws UnsupportedEncodingException, CASAuthenticationException {
	final String casValidateUrl = FenixWebFramework.getConfig().getCasConfig(serverName).getCasValidateUrl();
	final String casServiceUrl = URLEncoder.encode(requestURL.replace("http://", "https://").replace(":8080", ""), "UTF-8");

	ProxyTicketValidator pv = new ProxyTicketValidator();
	pv.setCasValidateUrl(casValidateUrl);
	pv.setServiceTicket(casTicket);
	pv.setService(casServiceUrl);
	pv.setRenew(false);

	return CASReceipt.getReceipt(pv);
    }

    protected Collection<Role> getInfoRoles(Person person, final Set allowedRoles) {
	final String username = person.getUsername();
	final Set<Role> personRoles = person.getPersonRolesSet();

	final Map<RoleType, Role> infoRoles = new HashMap<RoleType, Role>(personRoles.size());
	for (final Role role : personRoles) {
	    final RoleType roleType = role.getRoleType();
	    if (allowedRoles.contains(roleType)) {
		infoRoles.put(roleType, role);
	    }
	}

	filterRoles(infoRoles, person);
	return infoRoles.values();
    }

    protected Set getAllowedRolesByHostname(final String requestURL) {
	for (final Iterator iterator = allowedRolesByHostname.keySet().iterator(); iterator.hasNext();) {
	    final String hostname = (String) iterator.next();
	    if (StringUtils.substringAfter(requestURL, "://").startsWith(hostname)) {
		return (Set) allowedRolesByHostname.get(hostname);
	    }
	}
	return new HashSet(0);
    }

    protected void filterRoles(final Map<RoleType, Role> infoRoles, Person person) {
	filterEmployeeRoleForTeachers(infoRoles, person);
    }

    protected void filterEmployeeRoleForTeachers(Map<RoleType, Role> infoRoles, Person person) {
	if (!personHasAssiduousness(person)) {
	    infoRoles.remove(RoleType.EMPLOYEE);
	}
    }

    private boolean personHasAssiduousness(Person person) {
	if (person.hasEmployee()) {
	    LocalDate currentDate = new LocalDate();
	    if (person.getEmployee().hasAssiduousness()) {
		return person.getEmployee().getAssiduousness().isStatusActive(currentDate, currentDate);
	    }
	}
	return false;
    }
}
