package net.sourceforge.fenixedu.presentationTier.Action.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSessionActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author joao
 */
public abstract class FenixDispatchAction extends DispatchAction {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        return super.execute(mapping, actionForm, request, response);
    }
    
    protected Person getLoggedPerson(HttpServletRequest request) throws FenixFilterException, FenixServiceException
    {
    	IUserView userView = SessionUtils.getUserView(request);
		Person person  = (Person) ServiceManagerServiceFactory.executeService(userView,"ReadDomainPersonByUsername",new Object[] {userView.getUtilizador()});
		return person;
    }

    protected HttpSession getSession(HttpServletRequest request) throws InvalidSessionActionException {
        HttpSession result = request.getSession(false);
        if (result == null)
            throw new InvalidSessionActionException();

        return result;
    }

    /*
     * Sets an error to display later in the Browser and sets the mapping
     * forward.
     */
    protected ActionForward setError(HttpServletRequest request, ActionMapping mapping,
            String errorMessage, String forwardPage, Object actionArg) {
        ActionErrors errors = new ActionErrors();
        String notMessageKey = errorMessage;
        ActionError error = new ActionError(notMessageKey, actionArg);
        errors.add(notMessageKey, error);
        saveErrors(request, errors);

        if (forwardPage != null) {
            return mapping.findForward(forwardPage);
        }

        return mapping.getInputForward();

    }

    /*
     * Verifies if a property of type String in a FormBean is not empty. Returns
     * true if the field is present and not empty. False otherwhise.
     */
    protected boolean verifyStringParameterInForm(DynaValidatorForm dynaForm, String field) {
        if (dynaForm.get(field) != null && !dynaForm.get(field).equals("")) {
            return true;
        }

        return false;

    }

    /*
     * Verifies if a parameter in a Http Request is not empty. Return true if
     * the field is not empty. False otherwise.
     */
    protected boolean verifyParameterInRequest(HttpServletRequest request, String field) {
        if (request.getParameter(field) != null && !request.getParameter(field).equals("")) {
            return true;
        }

        return false;

    }
}