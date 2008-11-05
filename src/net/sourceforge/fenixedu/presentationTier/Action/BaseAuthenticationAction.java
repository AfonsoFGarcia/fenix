package net.sourceforge.fenixedu.presentationTier.Action;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.util.HostAccessControl;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.Days;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.servlets.filters.I18NFilter;
import pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter;

public abstract class BaseAuthenticationAction extends FenixAction {

    protected static final boolean useCASAuthentication;

    static {
	useCASAuthentication = PropertiesManager.getBooleanProperty("cas.enabled");
    }

    public final ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	try {

	    String remoteHostName = getRemoteHostName(request);
	    final IUserView userView = doAuthentication(form, request, remoteHostName);

	    if (userView.getRoleTypes().isEmpty()) {
		return getAuthenticationFailedForward(mapping, request, "errors.noAuthorization", "errors.noAuthorization");
	    }

	    final HttpSession session = request.getSession(false);

	    UserView.setUser(userView);

	    if (isStudentAndHasInquiriesToRespond(userView)) {
		return handleSessionCreationAndForwardToInquiriesResponseQuestion(request, userView, session);
	    } else if (isTeacherAndHasInquiriesToRespond(userView)) {
		return handleSessionCreationAndForwardToTeachingInquiriesResponseQuestion(request, userView, session);
	    } else if (isStudentAndHasGratuityDebtsToPay(userView)) {
		return handleSessionCreationAndForwardToGratuityPaymentsReminder(request, userView, session);
	    } else if (session != null && session.getAttribute("ORIGINAL_REQUEST") != null) {
		return handleSessionRestoreAndGetForward(request, userView, session);
	    } else {
		return handleSessionCreationAndGetForward(mapping, request, userView, session);
	    }
	} catch (ExcepcaoAutenticacao e) {
	    return getAuthenticationFailedForward(mapping, request, "invalidAuthentication", "errors.invalidAuthentication");
	}
    }

    private ActionForward handleSessionCreationAndForwardToGratuityPaymentsReminder(HttpServletRequest request,
	    IUserView userView, HttpSession session) {
	createNewSession(request, session, userView);
	return new ActionForward("/gratuityPaymentsReminder.do?method=showReminder");
    }

    private boolean isStudentAndHasGratuityDebtsToPay(final IUserView userView) {
	return userView.hasRoleType(RoleType.STUDENT)
		&& userView.getPerson().hasGratuityOrAdministrativeOfficeFeeAndInsuranceDebtsFor(
			ExecutionYear.readCurrentExecutionYear());
    }

    private boolean isTeacherAndHasInquiriesToRespond(IUserView userView) {
	if (userView.hasRoleType(RoleType.TEACHER)) {
	    return userView.getPerson().getTeacher().hasTeachingInquiriesToAnswer();
	}
	return false;
    }

    private boolean isStudentAndHasInquiriesToRespond(final IUserView userView) {
	if (userView.hasRoleType(RoleType.STUDENT)) {
	    return userView.getPerson().getStudent().hasInquiriesToRespond();
	}
	return false;
    }

    protected abstract IUserView doAuthentication(ActionForm form, HttpServletRequest request, String remoteHostName)
	    throws FenixFilterException, FenixServiceException;

    protected abstract ActionForward getAuthenticationFailedForward(final ActionMapping mapping,
	    final HttpServletRequest request, final String actionKey, final String messageKey);

    private ActionForward handleSessionCreationAndGetForward(ActionMapping mapping, HttpServletRequest request,
	    IUserView userView, final HttpSession session) {
	createNewSession(request, session, userView);

	ActionForward actionForward = mapping.findForward("sucess");

	return checkExpirationDate(mapping, request, userView, actionForward);
    }

    private ActionForward handleSessionCreationAndForwardToInquiriesResponseQuestion(HttpServletRequest request,
	    IUserView userView, HttpSession session) {
	createNewSession(request, session, userView);
	return new ActionForward("/respondToInquiriesQuestion.do?method=showQuestion");
    }

    private ActionForward handleSessionCreationAndForwardToTeachingInquiriesResponseQuestion(HttpServletRequest request,
	    IUserView userView, HttpSession session) {
	createNewSession(request, session, userView);
	return new ActionForward("/respondToTeachingInquiriesQuestion.do?method=showQuestion");
    }

    private ActionForward checkExpirationDate(ActionMapping mapping, HttpServletRequest request, IUserView userView,
	    ActionForward actionForward) {
	if (userView.getExpirationDate() == null) {
	    return actionForward;
	}

	Days days = Days.daysBetween(new DateTime(), userView.getExpirationDate());
	if (days.getDays() <= 30) {
	    request.setAttribute("path", actionForward.getPath());
	    request.setAttribute("days", days.getDays());
	    request.setAttribute("dayString", userView.getExpirationDate().toString("dd/MM/yyyy"));
	    return mapping.findForward("expirationWarning");
	} else {
	    return actionForward;
	}
    }

    private ActionForward handleSessionRestoreAndGetForward(HttpServletRequest request, IUserView userView,
	    final HttpSession session) {
	final ActionForward actionForward = new ActionForward();
	actionForward.setContextRelative(false);
	actionForward.setRedirect(true);

	final String originalURI = (String) session.getAttribute("ORIGINAL_URI");

	// Set request attributes
	final Map<String, Object> attributeMap = (Map<String, Object>) session.getAttribute("ORIGINAL_ATTRIBUTE_MAP");
	final Map<String, Object> parameterMap = (Map<String, Object>) session.getAttribute("ORIGINAL_PARAMETER_MAP");

	actionForward.setPath("/redirect.do");

	final HttpSession newSession = createNewSession(request, session, userView);

	newSession.setAttribute("ORIGINAL_URI", originalURI);
	newSession.setAttribute("ORIGINAL_PARAMETER_MAP", parameterMap);
	newSession.setAttribute("ORIGINAL_ATTRIBUTE_MAP", attributeMap);

	return actionForward;
    }

    private HttpSession createNewSession(final HttpServletRequest request, final HttpSession session, final IUserView userView) {
	if (session != null) {
	    session.invalidate();
	}

	final HttpSession newSession = request.getSession(true);

	// Store the UserView into the session and return
	UserView.setUser(userView);
	newSession.setAttribute(SetUserViewFilter.USER_SESSION_ATTRIBUTE, userView);
	newSession.setAttribute(SessionConstants.SESSION_IS_VALID, Boolean.TRUE);

	I18NFilter.setDefaultLocale(request, newSession);

	return newSession;
    }

    /**
     * @param userRoles
     * @return
     */
    private int getNumberOfSubApplications(final Collection<RoleType> roleTypes) {
	final Set<String> subApplications = new HashSet<String>();
	for (final RoleType roleType : roleTypes) {
	    final Role role = Role.getRoleByRoleType(roleType);
	    final String subApplication = role.getPortalSubApplication();
	    if (!subApplications.contains(subApplication) && !subApplication.equals("/teacher")) {
		subApplications.add(subApplication);
	    }
	}
	return subApplications.size();
    }

    /**
     * @param infoRole
     * @return
     */
    private ActionForward buildRoleForward(Role infoRole) {
	ActionForward actionForward = new ActionForward();
	actionForward.setContextRelative(false);
	actionForward.setRedirect(false);
	actionForward.setPath("/dotIstPortal.do?prefix=" + infoRole.getPortalSubApplication() + "&page=" + infoRole.getPage());
	return actionForward;
    }

    public static String getRemoteHostName(HttpServletRequest request) {
	String remoteHostName;
	final String remoteAddress = HostAccessControl.getRemoteAddress(request);
	try {
	    remoteHostName = InetAddress.getByName(remoteAddress).getHostName();
	} catch (UnknownHostException e) {
	    remoteHostName = remoteAddress;
	}
	return remoteHostName;
    }
}