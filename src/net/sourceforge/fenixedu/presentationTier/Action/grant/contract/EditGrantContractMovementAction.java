/*
 * Created on 3/Jul/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractMovement;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantContractMovementAction extends FenixDispatchAction {
    /*
     * Fills the form with the correspondent data
     */
    public ActionForward prepareEditGrantContractMovementForm(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	IUserView userView = UserView.getUser();
	DynaValidatorForm grantContractMovementForm = (DynaValidatorForm) form;

	Integer idGrantMovement = null;
	Integer loaddb = null;
	if (verifyParameterInRequest(request, "loaddb")) {
	    loaddb = new Integer(request.getParameter("loaddb"));
	}
	if (verifyParameterInRequest(request, "idGrantMovement")) {
	    idGrantMovement = new Integer(request.getParameter("idGrantMovement"));
	}

	if (loaddb != null && loaddb.equals(new Integer(1))) //load contents
	// from database
	{
	    if (idGrantMovement != null) //Editing a grant contract movement
	    {
		try {
		    //Read the grant contract movement
		    Object[] args = { idGrantMovement };
		    InfoGrantContractMovement infoGrantContractMovement = (InfoGrantContractMovement) ServiceUtils
			    .executeService( "ReadGrantContractMovement", args);

		    //Populate the form
		    setFormGrantContractMovement(grantContractMovementForm, infoGrantContractMovement);
		    request.setAttribute("idContract", infoGrantContractMovement.getInfoGrantContract()
			    .getIdInternal());
		} catch (FenixServiceException e) {
		    return setError(request, mapping, "errors.grant.contract.movement.read",
			    "manage-grant-contract-movement", null);
		}
	    } else //New grant contract movement
	    {
		Integer idContract = new Integer(request.getParameter("idContract"));
		grantContractMovementForm.set("grantContractId", idContract);
		request.setAttribute("idContract", idContract);
	    }
	} else //Probabily a validation error
	{
	    request.setAttribute("idContract", request.getParameter("grantContractId"));
	}

	return mapping.findForward("edit-grant-contract-movement");
    }

    /*
     * Edit a Grant Contract Movement
     */
    public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	DynaValidatorForm editGrantContractMovementForm = (DynaValidatorForm) form;
	InfoGrantContractMovement infoGrantContractMovement = null;
	IUserView userView = UserView.getUser();
	try {
	    infoGrantContractMovement = populateInfoFromForm(editGrantContractMovementForm);

	    if (infoGrantContractMovement.getDepartureDate() != null
		    && infoGrantContractMovement.getArrivalDate() != null
		    && infoGrantContractMovement.getDepartureDate().after(
			    infoGrantContractMovement.getArrivalDate())) {
		return setError(request, mapping, "errors.grant.contract.movement.beginDateBeforeEnd",
			null, null);
	    }

	    Object[] args = { infoGrantContractMovement };
	    ServiceUtils.executeService("EditGrantContractMovement", args);

	    request.setAttribute("idContract", editGrantContractMovementForm.get("grantContractId"));
	} catch (FenixServiceException e) {
	    return setError(request, mapping, "errors.grant.contract.movement.edit", null, null);
	} 
	return mapping.findForward("manage-grant-contract-movement");
    }

    /*
     * Delete a grant contract movement
     */
    public ActionForward doDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	try {
	    Integer idGrantMovement = new Integer(request.getParameter("idGrantMovement"));
	    Integer idContract = new Integer(request.getParameter("idContract"));

	    Object[] args = { idGrantMovement };
	    IUserView userView = UserView.getUser();
	    ServiceUtils.executeService("DeleteGrantContractMovement", args);

	    request.setAttribute("idContract", idContract);
	} catch (FenixServiceException e) {
	    return setError(request, mapping, "errors.grant.contract.movement.delete", null, null);
	}
	return mapping.findForward("manage-grant-contract-movement");
    }

    /*
     * Populates form from InfoGrantContractMovement
     */
    private void setFormGrantContractMovement(DynaValidatorForm form,
	    InfoGrantContractMovement infoGrantContractMovement) throws Exception {
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	form.set("location", infoGrantContractMovement.getLocation());
	if (infoGrantContractMovement.getDepartureDate() != null)
	    form.set("departureDate", sdf.format(infoGrantContractMovement.getDepartureDate()));
	if (infoGrantContractMovement.getArrivalDate() != null)
	    form.set("arrivalDate", sdf.format(infoGrantContractMovement.getArrivalDate()));

	form.set("idInternal", infoGrantContractMovement.getIdInternal());
	form.set("grantContractId", infoGrantContractMovement.getInfoGrantContract().getIdInternal());
    }

    /*
     * Populates Info from Form
     */
    private InfoGrantContractMovement populateInfoFromForm(
	    DynaValidatorForm editGrantContractMovementForm) throws Exception {
	InfoGrantContractMovement infoGrantContractMovement = new InfoGrantContractMovement();

	infoGrantContractMovement.setLocation((String) editGrantContractMovementForm.get("location"));

	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	if (verifyStringParameterInForm(editGrantContractMovementForm, "departureDate"))
	    infoGrantContractMovement.setDepartureDate(sdf.parse((String) editGrantContractMovementForm
		    .get("departureDate")));
	if (verifyStringParameterInForm(editGrantContractMovementForm, "arrivalDate"))
	    infoGrantContractMovement.setArrivalDate(sdf.parse((String) editGrantContractMovementForm
		    .get("arrivalDate")));

	if (verifyStringParameterInForm(editGrantContractMovementForm, "idInternal")) {
	    infoGrantContractMovement.setIdInternal((Integer) editGrantContractMovementForm
		    .get("idInternal"));
	}

	InfoGrantContract infoGrantContract = new InfoGrantContract();
	infoGrantContract.setIdInternal((Integer) editGrantContractMovementForm.get("grantContractId"));
	infoGrantContractMovement.setInfoGrantContract(infoGrantContract);

	return infoGrantContractMovement;
    }

}