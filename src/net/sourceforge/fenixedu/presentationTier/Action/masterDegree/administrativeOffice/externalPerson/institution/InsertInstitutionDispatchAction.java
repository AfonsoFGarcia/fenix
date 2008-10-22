package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.externalPerson.institution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.institution.InsertInstitution;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 * 
 */

public class InsertInstitutionDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	return mapping.findForward("start");
    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	IUserView userView = UserView.getUser();

	DynaActionForm insertInstitutionForm = (DynaActionForm) form;

	String institutionName = (String) insertInstitutionForm.get("name");

	try {
	    InsertInstitution.run(institutionName);
	} catch (ExistingServiceException e) {
	    throw new ExistingActionException(e.getMessage(), mapping.findForward("error"));
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
	}

	return mapping.findForward("success");
    }

}