package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import edu.yale.its.tp.cas.client.CASReceipt;

public class CASAuthenticationAction extends BaseAuthenticationAction {

    @Override
    protected IUserView doAuthentication(ActionForm form, HttpServletRequest request, String remoteHostName)
	    throws FenixFilterException, FenixServiceException {

	if (!useCASAuthentication) {
	    throw new ExcepcaoAutenticacao("errors.noAuthorization");
	}

	IUserView userView = getCurrentUserView(request);

	if (userView == null) {
	    final String casTicket = (String) request.getParameter("ticket");
	    final String requestURL = request.getRequestURL().toString();
	    final CASReceipt receipt = Authenticate.getCASReceipt(casTicket, requestURL);
	    final Object authenticationArgs[] = { receipt, requestURL, remoteHostName };

	    userView = (IUserView) ServiceManagerServiceFactory.executeService(PropertiesManager
		    .getProperty("authenticationService"), authenticationArgs);

	}

	return userView;
    }

    @Override
    protected ActionForward getAuthenticationFailedForward(final ActionMapping mapping, final HttpServletRequest request,
	    final String actionKey, final String messageKey) {
	final ActionErrors actionErrors = new ActionErrors();
	actionErrors.add(actionKey, new ActionError(messageKey));
	saveErrors(request, actionErrors);
	return mapping.findForward("error");
    }

    private IUserView getCurrentUserView(HttpServletRequest request) {
	return UserView.getUser();
    }
}