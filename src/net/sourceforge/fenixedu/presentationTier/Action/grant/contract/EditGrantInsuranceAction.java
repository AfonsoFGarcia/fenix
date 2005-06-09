/*
 * Created on Jun 26, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantCostCenter;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsurance;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPaymentEntity;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantProject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantInsuranceAction extends FenixDispatchAction {

    /*
     * Fills the form with the correspondent data
     */
    public ActionForward prepareEditGrantInsuranceForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        DynaValidatorForm grantInsuranceForm = (DynaValidatorForm) form;

        Integer idContract = null;
        try
        //Probably a validation error
        {
            idContract = new Integer(request.getParameter("idContract"));
        } catch (Exception e) {
            request.setAttribute("idGrantOwner", new Integer(request.getParameter("idGrantOwner")));
            request.setAttribute("contractNumber", request.getParameter("contractNumber"));

            return mapping.findForward("edit-grant-insurance");
        }

        try {
            //Read the insurance
            Object[] args = { idContract };
            InfoGrantContract infoGrantContract = null;
            InfoGrantInsurance infoGrantInsurance = (InfoGrantInsurance) ServiceUtils.executeService(
                    userView, "ReadGrantInsuranceByGrantContract", args);

            if (infoGrantInsurance == null) { //Is a new Insurance
                //Read the contract
                Object[] argsContract = { idContract };
                infoGrantContract = (InfoGrantContract) ServiceUtils.executeService(userView,
                        "ReadGrantContract", argsContract);
                if (infoGrantContract != null) {
                    request.setAttribute("contractNumber", infoGrantContract.getContractNumber());
                }

            } else {

                infoGrantContract = infoGrantInsurance.getInfoGrantContract();
                //Populate the form
                setFormGrantInsurance(grantInsuranceForm, infoGrantInsurance);
                request.setAttribute("idInsurance", infoGrantInsurance.getIdInternal());
                request.setAttribute("contractNumber", infoGrantInsurance.getInfoGrantContract()
                        .getContractNumber());
            }
            grantInsuranceForm.set("idContract", idContract);
            request.setAttribute("idContract", idContract);
            grantInsuranceForm
                    .set("idGrantOwner", infoGrantContract.getGrantOwnerInfo().getIdInternal());
            request.setAttribute("idGrantOwner", infoGrantContract.getGrantOwnerInfo().getIdInternal());
        } catch (FenixServiceException e) {
            return setError(request, mapping, "errors.grant.insurance.read", "edit-grant-insurance",
                    null);
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", "edit-grant-insurance", null);
        }
        return mapping.findForward("edit-grant-insurance");
    }

    public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            DynaValidatorForm editGrantInsuranceForm = (DynaValidatorForm) form;

            //Verificar se foi escolhida UMA E SO UMA entidade pagadora
            if (!verifyStringParameterInForm(editGrantInsuranceForm, "project")
                    && !verifyStringParameterInForm(editGrantInsuranceForm, "costcenter")) {
                return setError(request, mapping, "errors.grant.insurance.mustHaveOnePaymentEntity",
                        null, null);
            }
            if (verifyStringParameterInForm(editGrantInsuranceForm, "project")
                    && verifyStringParameterInForm(editGrantInsuranceForm, "costcenter")) {
                return setError(request, mapping, "errors.grant.insurance.mustBeOnePaymentEntity", null,
                        null);
            }

            InfoGrantInsurance infoGrantInsurance = populateInfoFromForm(editGrantInsuranceForm);
            IUserView userView = SessionUtils.getUserView(request);

            //Let's read the payment entity
            if (infoGrantInsurance.getInfoGrantPaymentEntity().getNumber() != null) {
                Object[] args = { infoGrantInsurance.getInfoGrantPaymentEntity().getNumber(),
                        infoGrantInsurance.getInfoGrantPaymentEntity().getOjbConcreteClass() };
                InfoGrantPaymentEntity infoGrantPaymentEntity = (InfoGrantPaymentEntity) ServiceUtils
                        .executeService(userView, "ReadPaymentEntityByNumberAndClass", args);

                if (infoGrantPaymentEntity == null) {
                    if (verifyStringParameterInForm(editGrantInsuranceForm, "project")) {
                        return setError(request, mapping, "errors.grant.paymententity.unknownProject",
                                null, infoGrantInsurance.getInfoGrantPaymentEntity().getNumber());
                    } else if (verifyStringParameterInForm(editGrantInsuranceForm, "costcenter")) {
                        return setError(request, mapping,
                                "errors.grant.paymententity.unknownCostCenter", null, infoGrantInsurance
                                        .getInfoGrantPaymentEntity().getNumber());
                    } else {
                        return setError(request, mapping, "errors.grant.part.invalidPaymentEntity",
                                null, null);
                    }
                }
                infoGrantInsurance.setInfoGrantPaymentEntity(infoGrantPaymentEntity);
            }
            //Save the insurance
            Object[] args = { infoGrantInsurance };
            ServiceUtils.executeService(userView, "EditGrantInsurance", args);
            request.setAttribute("idInternal", editGrantInsuranceForm.get("idGrantOwner"));

        } catch (FenixServiceException e) {
            return setError(request, mapping, "errors.grant.insurance.edit", null, null);
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", null, null);
        }
        return mapping.findForward("manage-grant-contract");
    }

    /*
     * Populates form from InfoSubsidy
     */
    private void setFormGrantInsurance(DynaValidatorForm form, InfoGrantInsurance infoGrantInsurance)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        form.set("idGrantInsurance", infoGrantInsurance.getIdInternal());
        if (infoGrantInsurance.getTotalValue() != null)
            form.set("totalValue", infoGrantInsurance.getTotalValue());
        if (infoGrantInsurance.getInfoGrantPaymentEntity() != null) {
            if (infoGrantInsurance.getInfoGrantPaymentEntity() instanceof InfoGrantProject && infoGrantInsurance.getInfoGrantPaymentEntity().getGrantProjectOjbConcreteClass().equals(GrantProject.class.getName()) )
                form.set("project", infoGrantInsurance.getInfoGrantPaymentEntity().getNumber());
            else if((infoGrantInsurance.getInfoGrantPaymentEntity() instanceof InfoGrantCostCenter && infoGrantInsurance.getInfoGrantPaymentEntity().getGrantCostCenterOjbConcreteClass().equals(GrantCostCenter.class.getName()) ))
                    form.set("costcenter", infoGrantInsurance.getInfoGrantPaymentEntity().getNumber());
        }
        if (infoGrantInsurance.getDateBeginInsurance() != null)
            form.set("dateBeginInsurance", sdf.format(infoGrantInsurance.getDateBeginInsurance()));
        if (infoGrantInsurance.getDateEndInsurance() != null)
            form.set("dateEndInsurance", sdf.format(infoGrantInsurance.getDateEndInsurance()));
    }

    private InfoGrantInsurance populateInfoFromForm(DynaValidatorForm editGrantInsuranceForm)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        InfoGrantInsurance infoGrantInsurance = new InfoGrantInsurance();
        if (verifyStringParameterInForm(editGrantInsuranceForm, "idGrantInsurance"))
            infoGrantInsurance.setIdInternal((Integer) editGrantInsuranceForm.get("idGrantInsurance"));
        if (verifyStringParameterInForm(editGrantInsuranceForm, "dateBeginInsurance"))
            infoGrantInsurance.setDateBeginInsurance(sdf.parse((String) editGrantInsuranceForm
                    .get("dateBeginInsurance")));
        if (verifyStringParameterInForm(editGrantInsuranceForm, "dateEndInsurance"))
            infoGrantInsurance.setDateEndInsurance(sdf.parse((String) editGrantInsuranceForm
                    .get("dateEndInsurance")));
        if (verifyStringParameterInForm(editGrantInsuranceForm, "totalValue"))
            infoGrantInsurance.setTotalValue((Double) editGrantInsuranceForm.get("totalValue"));

        InfoGrantContract infoGrantContract = new InfoGrantContract();
        infoGrantContract.setIdInternal((Integer) editGrantInsuranceForm.get("idContract"));
        infoGrantInsurance.setInfoGrantContract(infoGrantContract);

        InfoGrantPaymentEntity infoGrantPaymentEntity = null;
        if (verifyStringParameterInForm(editGrantInsuranceForm, "project")) {
            infoGrantPaymentEntity = new InfoGrantProject();
            infoGrantPaymentEntity.setNumber((String) editGrantInsuranceForm.get("project"));
            infoGrantPaymentEntity.setOjbConcreteClass(InfoGrantPaymentEntity
                    .getGrantProjectOjbConcreteClass());

        } else if (verifyStringParameterInForm(editGrantInsuranceForm, "costcenter")) {
            infoGrantPaymentEntity = new InfoGrantCostCenter();
            infoGrantPaymentEntity.setNumber((String) editGrantInsuranceForm.get("costcenter"));
            infoGrantPaymentEntity.setOjbConcreteClass(InfoGrantPaymentEntity
                    .getGrantCostCenterOjbConcreteClass());
        }
        infoGrantInsurance.setInfoGrantPaymentEntity(infoGrantPaymentEntity);

        return infoGrantInsurance;
    }
}