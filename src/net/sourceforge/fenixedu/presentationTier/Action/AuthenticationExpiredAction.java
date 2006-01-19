package net.sourceforge.fenixedu.presentationTier.Action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.mapping.ActionMappingForAuthentication;

public class AuthenticationExpiredAction extends FenixDispatchAction {
	
    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	DynaActionForm actionForm = (DynaActionForm) form;
    	actionForm.set("username", request.getParameter("username"));
    	actionForm.set("page", 0);
    	saveErrors(request, null);
        return mapping.findForward("changePass");
    }
    
    public ActionForward changePass(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
        	
        	final IUserView userView = authenticateUser(form, request);
        	
            if (userView == null || userView.getRoles().isEmpty()) {
                return authenticationFailedForward(mapping, request, "errors.noAuthorization");
            }

            final HttpSession session = request.getSession(true);

            // Store the UserView into the session and return
            session.setAttribute(SessionConstants.U_VIEW, userView);
            session.setAttribute(SessionConstants.SESSION_IS_VALID, Boolean.TRUE);

            Collection userRoles = userView.getRoles();

            InfoRole firstTimeStudentInfoRole = new InfoRole();
            firstTimeStudentInfoRole.setRoleType(RoleType.FIRST_TIME_STUDENT);

            if (userRoles.contains(firstTimeStudentInfoRole)) {
                // TODO impose a period time limit
                InfoRole infoRole = getRole(RoleType.FIRST_TIME_STUDENT, userRoles);
                return buildRoleForward(infoRole);
            } else {
                InfoRole personInfoRole = new InfoRole();
                personInfoRole.setRoleType(RoleType.PERSON);
                int numberOfSubApplications = getNumberOfSubApplications(userRoles);
                if (numberOfSubApplications == 1 || !userRoles.contains(personInfoRole)) {
                    final InfoRole firstInfoRole = ((userRoles.isEmpty()) ? null : (InfoRole) userRoles.iterator().next());
                    return buildRoleForward(firstInfoRole);
                } else {
                    return mapping.findForward("sucess");
                }
            }

        } catch (ExcepcaoAutenticacao e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("invalidAuthentication", new ActionError("errors.invalidAuthentication"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        } catch (InvalidPasswordServiceException e) {
        	ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(e.getMessage(), new ActionError(e.getMessage()));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
		} catch (FenixServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.person.impossible.change", new ActionError("error.person.impossible.change"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
		}
        
    }
    
    /**
     * @param userRoles
     * @return
     */
    private int getNumberOfSubApplications(Collection userRoles) {
        List subApplications = new ArrayList();
        Iterator iterator = userRoles.iterator();
        while (iterator.hasNext()) {
            InfoRole infoRole = (InfoRole) iterator.next();
            String subApplication = infoRole.getPortalSubApplication();
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
    private ActionForward buildRoleForward(InfoRole infoRole) {
        ActionForward actionForward = new ActionForward();
        actionForward.setContextRelative(false);
        actionForward.setRedirect(false);
        actionForward.setPath("/dotIstPortal.do?prefix=" + infoRole.getPortalSubApplication() + "&page="
                + infoRole.getPage());
        return actionForward;
    }

    private InfoRole getRole(RoleType roleType, Collection rolesList) {

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(roleType);

        Iterator iterator = rolesList.iterator();
        while (iterator.hasNext()) {

            InfoRole role = (InfoRole) iterator.next();
            if (role.equals(infoRole))
                return role;

        }
        return null;
    }
    
    private IUserView authenticateUser(final ActionForm form, final HttpServletRequest request)  throws FenixServiceException, FenixFilterException {
        DynaActionForm authenticationForm = (DynaActionForm) form;
		final String username = (String) authenticationForm.get("username");
		final String password = (String) authenticationForm.get("password");
		final String newPassword = (String) authenticationForm.get("newPassword");
		final String requestURL = request.getRequestURL().toString();
        Object argsAutenticacao[] = { username, password, newPassword, requestURL };

        return (IUserView) ServiceManagerServiceFactory.executeService(null, "AuthenticationExpired",
                argsAutenticacao);
    }
    
    protected ActionForward authenticationFailedForward(final ActionMapping mapping,
            final HttpServletRequest request, final String messageKey) {
        final ActionErrors actionErrors = new ActionErrors();
        actionErrors.add(messageKey, new ActionError(messageKey));
        saveErrors(request, actionErrors);
        return mapping.getInputForward();
    }
}
