package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.externalPerson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.ReadExternalPersonByID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */

public class ViewExternalPersonDispatchAction extends FenixDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	IUserView userView = UserView.getUser();

	Integer externalPersonId = new Integer(this.getFromRequest("id", request));

	InfoExternalPerson infoExternalPerson = null;

	try {
	    infoExternalPerson = (InfoExternalPerson) ReadExternalPersonByID.run(externalPersonId);
	} catch (NonExistingServiceException e) {
	    throw new FenixActionException(e);
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	request.setAttribute(PresentationConstants.EXTERNAL_PERSON, infoExternalPerson);

	return mapping.findForward("start");

    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
	String parameterString = request.getParameter(parameter);
	if (parameterString == null) {
	    parameterString = (String) request.getAttribute(parameter);
	}
	return parameterString;
    }

}