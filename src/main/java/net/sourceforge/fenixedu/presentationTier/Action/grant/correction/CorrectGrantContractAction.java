/*
 * Created on May 17, 2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.correction;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.grant.contract.ReadAllContractsByGrantOwner;
import net.sourceforge.fenixedu.applicationTier.Servico.grant.owner.SearchGrantOwner;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Pica
 * @author Barbosa
 */
@Mapping(module = "facultyAdmOffice", path = "/correctGrantContract",
        input = "/correctGrantContract.do?page=0&method=prepareForm", attribute = "correctGrantContract",
        formBean = "correctGrantContract", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "correct-grant-contract-move", path = "/facultyAdmOffice/grant/correction/grantContractMove.jsp",
                tileProperties = @Tile(title = "private.teachingstaffandresearcher.corrections.movecontract")),
        @Forward(name = "correct-grant-contract-change-number",
                path = "/facultyAdmOffice/grant/correction/grantContractChangeNumber.jsp", tileProperties = @Tile(
                        title = "private.teachingstaffandresearcher.corrections.contractnumberchange")),
        @Forward(name = "correct-grant-contract-delete", path = "/facultyAdmOffice/grant/correction/grantContractDeletion.jsp",
                tileProperties = @Tile(title = "private.teachingstaffandresearcher.corrections.deletecontract")) })
public class CorrectGrantContractAction extends FenixDispatchAction {

    public ActionForward prepareForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String action = null;
        if (verifyParameterInRequest(request, "action")) {
            action = request.getParameter("action");
        }

        if (action.equals("deleteContract")) {
            return mapping.findForward("correct-grant-contract-delete");
        } else if (action.equals("changeNumberContract")) {
            return mapping.findForward("correct-grant-contract-change-number");
        } else if (action.equals("moveContract")) {
            return mapping.findForward("correct-grant-contract-move");
        } else {
            throw new Exception();
        }
    }

    public ActionForward deleteContract(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // Read the values from the Form
        DynaValidatorForm correctGrantContractForm = (DynaValidatorForm) form;

        Integer grantOwnerNumber = null;
        Integer grantContractNumber = null;

        try {
            grantOwnerNumber = new Integer((String) correctGrantContractForm.get("grantOwnerNumber"));
            grantContractNumber = new Integer((String) correctGrantContractForm.get("grantContractNumber"));
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.correction.fillAllFields", null, null);
        }

        IUserView userView = UserView.getUser();
        // Read the grant owner

        List infoGrantOwnerList = SearchGrantOwner.run(null, null, null, grantOwnerNumber, new Boolean(false), null);
        if (infoGrantOwnerList.isEmpty() || infoGrantOwnerList.size() > 1) {
            return setError(request, mapping, "errors.grant.correction.unknownGrantOwner", null, null);
        }

        InfoGrantOwner infoGrantOwner = (InfoGrantOwner) infoGrantOwnerList.get(0);

        // Read the contracts

        List infoGrantContractList = ReadAllContractsByGrantOwner.run(infoGrantOwner.getIdInternal());
        InfoGrantContract infoGrantContract = null;
        if (!infoGrantContractList.isEmpty()) {
            // Find the contract
            for (int i = 0; i < infoGrantContractList.size(); i++) {
                InfoGrantContract temp = (InfoGrantContract) infoGrantContractList.get(i);
                if (temp.getContractNumber().equals(grantContractNumber)) {
                    infoGrantContract = temp;
                    break;
                }
            }
        }
        if (infoGrantContract == null) {
            return setError(request, mapping, "errors.grant.correction.unknownContract", null, null);
        }
        // Delete the contract
        Object[] argsDeleteGrantContract = { infoGrantContract.getIdInternal() };
        ServiceUtils.executeService("DeleteGrantContract", argsDeleteGrantContract);
        // Set of the request variables and return
        request.setAttribute("correctionNumber2", "yes");
        return mapping.findForward("correct-grant-contract-delete");
    }

    public ActionForward changeNumberContract(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // Read the values from the Form
        DynaValidatorForm correctGrantContractForm = (DynaValidatorForm) form;

        Integer grantOwnerNumber = null;
        Integer grantContractNumber = null;
        Integer newGrantContractNumber = null;

        try {
            grantOwnerNumber = new Integer((String) correctGrantContractForm.get("grantOwnerNumber"));
            grantContractNumber = new Integer((String) correctGrantContractForm.get("grantContractNumber"));
            newGrantContractNumber = new Integer((String) correctGrantContractForm.get("newGrantContractNumber"));
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.correction.fillAllFields", null, null);
        }

        IUserView userView = UserView.getUser();
        // Read the grant owner

        List infoGrantOwnerList = SearchGrantOwner.run(null, null, null, grantOwnerNumber, new Boolean(false), null);
        if (infoGrantOwnerList.isEmpty() || infoGrantOwnerList.size() > 1) {
            return setError(request, mapping, "errors.grant.correction.unknownGrantOwner", null, null);
        }
        InfoGrantOwner infoGrantOwner = (InfoGrantOwner) infoGrantOwnerList.get(0);
        // Read the contracts

        List infoGrantContractList = ReadAllContractsByGrantOwner.run(infoGrantOwner.getIdInternal());
        InfoGrantContract infoGrantContract = null;
        if (!infoGrantContractList.isEmpty()) {
            // Find the contract
            for (int i = 0; i < infoGrantContractList.size(); i++) {
                InfoGrantContract temp = (InfoGrantContract) infoGrantContractList.get(i);
                if (temp.getContractNumber().equals(newGrantContractNumber)) {
                    // There is already a contract with the future number
                    return setError(request, mapping, "errors.grant.correction.contractWithSameNumberExists", null, null);
                }
                if (temp.getContractNumber().equals(grantContractNumber)) {
                    infoGrantContract = temp;
                }
            }
        }
        if (infoGrantContract == null) {
            return setError(request, mapping, "errors.grant.correction.unknownContract", null, null);
        }
        // Change the number, save the contract
        infoGrantContract.setContractNumber(newGrantContractNumber);
        Object[] argsNewGrantContract = { infoGrantContract };
        ServiceUtils.executeService("EditGrantContract", argsNewGrantContract);
        // Set of the request variables and return
        request.setAttribute("correctionNumber3", "yes");
        return mapping.findForward("correct-grant-contract-change-number");
    }

    public ActionForward moveContract(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // Read the values from the Form
        DynaValidatorForm correctGrantContractForm = (DynaValidatorForm) form;

        Integer grantOwnerNumber = null;
        Integer grantContractNumber = null;
        Integer newGrantOwnerNumber = null;

        try {
            grantOwnerNumber = new Integer((String) correctGrantContractForm.get("grantOwnerNumber"));
            grantContractNumber = new Integer((String) correctGrantContractForm.get("grantContractNumber"));
            newGrantOwnerNumber = new Integer((String) correctGrantContractForm.get("newGrantOwnerNumber"));
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.correction.fillAllFields", null, null);
        }

        IUserView userView = UserView.getUser();
        // Read the original grant owner

        List infoGrantOwnerList = SearchGrantOwner.run(null, null, null, grantOwnerNumber, new Boolean(false), null);
        if (infoGrantOwnerList.isEmpty() || infoGrantOwnerList.size() > 1) {
            return setError(request, mapping, "errors.grant.correction.unknownGrantOwner", null, null);
        }

        InfoGrantOwner originalGrantOwner = (InfoGrantOwner) infoGrantOwnerList.get(0);

        // Read the new grant owner

        infoGrantOwnerList = SearchGrantOwner.run(null, null, null, newGrantOwnerNumber, new Boolean(false), null);
        if (infoGrantOwnerList.isEmpty() || infoGrantOwnerList.size() > 1) {
            return setError(request, mapping, "errors.grant.correction.unknownGrantOwner", null, null);
        }

        InfoGrantOwner newGrantOwner = (InfoGrantOwner) infoGrantOwnerList.get(0);

        // Read the contracts of the original grant owner

        List originalGrantContractList = ReadAllContractsByGrantOwner.run(originalGrantOwner.getIdInternal());

        // Read the contracts of the original grant owner

        List newGrantContractList = ReadAllContractsByGrantOwner.run(newGrantOwner.getIdInternal());

        // Find the contract to move
        InfoGrantContract infoGrantContractToMove = null;
        if (!originalGrantContractList.isEmpty()) {
            for (int i = 0; i < originalGrantContractList.size(); i++) {
                InfoGrantContract temp = (InfoGrantContract) originalGrantContractList.get(i);
                if (temp.getContractNumber().equals(grantContractNumber)) {
                    infoGrantContractToMove = temp;
                }
            }
        }
        if (infoGrantContractToMove == null) {
            return setError(request, mapping, "errors.grant.correction.unknownContract", null, null);
        }

        // Find biggest number of contract in new Grant Owner
        // so that there aren't conflicts moving the contract
        int numeroMaxContrato = 0;
        if (!newGrantContractList.isEmpty()) {
            for (int i = 0; i < newGrantContractList.size(); i++) {

                InfoGrantContract temp = (InfoGrantContract) newGrantContractList.get(i);
                if (numeroMaxContrato < temp.getContractNumber().intValue()) {
                    numeroMaxContrato = temp.getContractNumber().intValue();
                }
            }
        }

        // Change the number and the grant owner, save the contract
        infoGrantContractToMove.setContractNumber(new Integer(++numeroMaxContrato));
        infoGrantContractToMove.setGrantOwnerInfo(newGrantOwner);

        Object[] argsNewGrantContract = { infoGrantContractToMove };
        ServiceUtils.executeService("EditGrantContract", argsNewGrantContract);

        // Set of the request variables and return
        request.setAttribute("correctionNumber4", "yes");
        return mapping.findForward("correct-grant-contract-move");
    }
}