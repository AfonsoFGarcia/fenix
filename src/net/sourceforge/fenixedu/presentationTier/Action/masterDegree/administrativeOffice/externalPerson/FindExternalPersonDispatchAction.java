package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.externalPerson;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.SearchExternalPersonsByName;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.PresentationConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */

public class FindExternalPersonDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	return mapping.findForward("start");

    }

    public ActionForward find(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	IUserView userView = UserView.getUser();
	ActionErrors actionErrors = new ActionErrors();

	DynaActionForm findExternalPersonForm = (DynaActionForm) form;
	String externalPersonName = (String) findExternalPersonForm.get("name");

	List infoExternalPersonsList = null;

	try {
	    if (!externalPersonName.equals(""))
		infoExternalPersonsList = (ArrayList) SearchExternalPersonsByName.run(externalPersonName);
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	if ((infoExternalPersonsList != null) && (infoExternalPersonsList.isEmpty() == false)) {
	    request.setAttribute(PresentationConstants.EXTERNAL_PERSONS_LIST, infoExternalPersonsList);

	    return mapping.findForward("success");
	}
	actionErrors.add("label.masterDegree.administrativeOffice.searchResultsEmpty", new ActionError(
		"label.masterDegree.administrativeOffice.searchResultsEmpty"));

	saveErrors(request, actionErrors);
	return mapping.findForward("error");

    }

}