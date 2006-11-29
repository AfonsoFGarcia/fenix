package net.sourceforge.fenixedu.domain;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.UsernameUtils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class Login extends Login_Base {

    public Login(User user) {
	super();
	checkIfUserAlreadyHaveLogin(user);
	setUser(user);
    }

    public void delete() {
	for (; !getAlias().isEmpty(); getAlias().get(0).delete())
	    ;
	for (; !getLoginPeriods().isEmpty(); getLoginPeriods().get(0).delete())
	    ;
	super.delete();
    }

    public LoginPeriod readLoginPeriodByTimeInterval(YearMonthDay begin, YearMonthDay end) {
	for (LoginPeriod loginPeriod : getLoginPeriodsSet()) {
	    if (loginPeriod.getBeginDate().equals(begin)
		    && ((loginPeriod.getEndDate() == null && end == null) || (loginPeriod.getEndDate()
			    .equals(end)))) {
		return loginPeriod;
	    }
	}
	return null;
    }

    public boolean isOpened() {
	YearMonthDay currentDate = new YearMonthDay();
	for (LoginPeriod loginPeriod : getLoginPeriodsSet()) {
	    if ((loginPeriod.getEndDate() == null || !loginPeriod.getEndDate().isBefore(currentDate))
		    && !loginPeriod.getBeginDate().isAfter(currentDate)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isLogin() {
	return true;
    }

    public String getUsername() {
	String userUId = getUserUId();
	if (userUId == null) {
	    Role mostImportantRole = UsernameUtils.getMostImportantRole(getUser().getPerson()
		    .getPersonRoles());
	    if (mostImportantRole != null) {
		List<LoginAlias> loginAlias = getRoleLoginAlias(mostImportantRole.getRoleType());
		if (!loginAlias.isEmpty()) {
		    return loginAlias.get(0).getAlias();
		}
	    }
	    return (getAlias().isEmpty()) ? null : getAlias().get(0).getAlias();
	}
	return userUId;
    }

    public String getUserUId() {
	LoginAlias loginAliasByType = getInstitutionalLoginAlias();
	return (loginAliasByType != null) ? loginAliasByType.getAlias() : null;
    }

    public void setUsername(RoleType roleType) {
	String newUsername = UsernameUtils.generateNewUsername(roleType, getUser().getPerson());
	removeAliasWithoutCloseLogin(roleType);
	if (!StringUtils.isEmpty(newUsername)) {
	    LoginAlias.createNewRoleLoginAlias(this, newUsername, roleType);
	    openLoginIfNecessary(roleType);
	}
    }

    public void setUserUID() {
	final LoginAlias loginAlias = getInstitutionalLoginAlias();
	if (loginAlias == null) {
	    final String userUId = UsernameUtils.updateIstUsername(getUser().getPerson());
	    if (!StringUtils.isEmpty(userUId)) {
		LoginAlias.createNewInstitutionalLoginAlias(this, userUId);
		getUser().setUserUId(userUId);
	    }
	}
    }

    public LoginAlias readLoginAliasByAlias(String alias) {
	for (LoginAlias loginAlias : getAlias()) {
	    if (loginAlias.getAlias().equalsIgnoreCase(alias)) {
		return loginAlias;
	    }
	}
	return null;
    }

    public LoginAlias getInstitutionalLoginAlias() {
	for (LoginAlias loginAlias : getAlias()) {
	    if (loginAlias.getType().equals(LoginAliasType.INSTITUTION_ALIAS)) {
		return loginAlias;
	    }
	}
	return null;
    }

    public List<LoginAlias> getRoleLoginAlias(RoleType roleType) {
	List<LoginAlias> result = new ArrayList<LoginAlias>();
	for (LoginAlias loginAlias : getAlias()) {
	    if (loginAlias.getType().equals(LoginAliasType.ROLE_TYPE_ALIAS)
		    && loginAlias.getRoleType().equals(roleType)) {
		result.add(loginAlias);
	    }
	}
	return result;
    }

    public Set<LoginAlias> getLoginAliasOrderByImportance() {
	Set<LoginAlias> result = new TreeSet<LoginAlias>(
		LoginAlias.COMPARATOR_BY_TYPE_AND_ROLE_TYPE_AND_ALIAS);
	result.addAll(getAlias());
	return result;
    }

    public void removeAlias(RoleType roleType) {
	removeAliasWithoutCloseLogin(roleType);
	closeLoginIfNecessary();
    }

    public boolean hasUsername(String username) {
	return readLoginAliasByAlias(username) != null;
    }

    public static Login readLoginByUsername(String username) {
	if (username != null) {
	    for (final LoginAlias loginAlias : RootDomainObject.getInstance().getLoginAlias()) {
		if (loginAlias.getAlias().equalsIgnoreCase(username)) {
		    return loginAlias.getLogin();
		}
	    }
	}
	return null;
    }

    /**
         * This map is a temporary solution until DML provides indexed
         * relations.
         * 
         */
    private static final Map<String, SoftReference<Login>> loginMap = new Hashtable<String, SoftReference<Login>>();

    public static Login readOpenedLoginByUsername(String username) {

	// Temporary solution until DML provides indexed relations.
	final String lowerCaseUsername = username.toLowerCase();
	final SoftReference<Login> loginReference = loginMap.get(lowerCaseUsername);
	
	if (loginReference != null) {
	    final Login login = loginReference.get();
	    if (login != null && login.getRootDomainObject() == RootDomainObject.getInstance()
		    && login.isOpened() && login.hasUsername(lowerCaseUsername)) {
		return login;
	    } else {
		loginMap.remove(lowerCaseUsername);
	    }
	}
	// *** end of hack

	for (final LoginAlias loginAlias : RootDomainObject.getInstance().getLoginAlias()) {
	    
	    // Temporary solution until DML provides indexed relations.	    
	    if (loginAlias.getLogin().isOpened()) {		
		final String lowerCaseLoginUsername = loginAlias.getAlias().toLowerCase();
		if (!loginMap.containsKey(lowerCaseLoginUsername)) {
		    loginMap.put(lowerCaseLoginUsername, new SoftReference<Login>(loginAlias.getLogin()));
		}
		// *** end of hack

		if (lowerCaseLoginUsername.equalsIgnoreCase(lowerCaseUsername)) {
		    return loginAlias.getLogin();
		}
	    }
	}
	return null;
    }

    public void closeLoginIfNecessary() {
	Person person = getUser().getPerson();
	if (!person.hasRole(RoleType.TEACHER) && !person.hasRole(RoleType.EMPLOYEE)
		&& !person.hasRole(RoleType.STUDENT) && !person.hasRole(RoleType.ALUMNI)
		&& !person.hasRole(RoleType.CANDIDATE)
		&& !person.hasRole(RoleType.INSTITUCIONAL_PROJECTS_MANAGER)
		&& !person.hasRole(RoleType.PROJECTS_MANAGER)
		&& !person.hasRole(RoleType.MANAGER)) {

	    // minusDays(1) -> This is for person dont make login today
	    YearMonthDay currentDate = new YearMonthDay().minusDays(1);
	    for (LoginPeriod loginPeriod : getLoginPeriodsSet()) {
		if (loginPeriod.getEndDate() == null) {
		    loginPeriod.setEndDate(currentDate);
		}
	    }
	}
    }

    public void openLoginIfNecessary(RoleType roleType) {
	switch (roleType) {	
	case MANAGER:
	case TEACHER:
	case EMPLOYEE:
	case STUDENT:
	case ALUMNI:
	case CANDIDATE:
	case INSTITUCIONAL_PROJECTS_MANAGER:
	case PROJECTS_MANAGER:	
	    for (LoginPeriod loginPeriod : getLoginPeriodsSet()) {
		if (loginPeriod.getEndDate() == null) {
		    return;
		}
	    }
	    // minusDays(2) -> This is for prevent errors in closeLoginIfNecessary (setEndDate line).
	    addLoginPeriods(new LoginPeriod(new YearMonthDay().minusDays(2), this));
	    break;
	    
	default:
	    break;
	}
    }

    private void removeAliasWithoutCloseLogin(RoleType roleType) {
	if (roleType != null) {
	    for (final Iterator<LoginAlias> iter = getAlias().iterator(); iter.hasNext();) {
		final LoginAlias loginAlias = iter.next();
		if (loginAlias.getRoleType() == roleType) {
		    iter.remove();
		    loginAlias.delete();
		}
	    }
	}
    }
    
    private void checkIfUserAlreadyHaveLogin(User user) {
	if (user != null) {
	    Login login = user.readUserLoginIdentification();
	    if(login != null && !login.equals(this)) {
		throw new DomainException("error.user.login.already.exists");
	    }
	}
    }
}
