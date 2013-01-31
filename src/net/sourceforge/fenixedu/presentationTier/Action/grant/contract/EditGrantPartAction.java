/*
 * Created on 29/Jan/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.InvalidPartResponsibleTeacherException;
import net.sourceforge.fenixedu.applicationTier.Servico.grant.contract.EditGrantPart;
import net.sourceforge.fenixedu.applicationTier.Servico.grant.contract.ReadPaymentEntityByNumberAndClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantCostCenter;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPart;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPaymentEntity;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantProject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantProject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanUtils;
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
 * @author Barbosa
 * @author Pica
 */
@Mapping(
		module = "facultyAdmOffice",
		path = "/editGrantPart",
		input = "/editGrantPart.do?page=0&method=prepareEditGrantPartForm",
		attribute = "editGrantPartForm",
		formBean = "editGrantPartForm",
		scope = "request",
		parameter = "method")
@Forwards(value = {
		@Forward(name = "edit-grant-part", path = "/facultyAdmOffice/grant/contract/editGrantPart.jsp", tileProperties = @Tile(
				title = "private.teachingstaffandresearcher.miscellaneousmanagement.costcenter")),
		@Forward(name = "manage-grant-part", path = "/manageGrantPart.do?method=prepareManageGrantPart") })
public class EditGrantPartAction extends FenixDispatchAction {
	/*
	 * Fills the form with the correspondent data
	 */
	public ActionForward prepareEditGrantPartForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Integer idGrantPart = null;
		Integer loaddb = null;
		if (verifyParameterInRequest(request, "loaddb")) {
			loaddb = new Integer(request.getParameter("loaddb"));
		}
		if (verifyParameterInRequest(request, "idGrantPart")) {
			idGrantPart = new Integer(request.getParameter("idGrantPart"));
		}

		IUserView userView = UserView.getUser();
		DynaValidatorForm grantPartForm = (DynaValidatorForm) form;

		if (loaddb != null && loaddb.equals(new Integer(1))) {
			// load contents from database
			if (idGrantPart != null) { // Editing a grant part
				try {
					// Read the grant part
					Object[] args = { idGrantPart };
					InfoGrantPart infoGrantPart = (InfoGrantPart) ServiceUtils.executeService("ReadGrantPart", args);

					// Populate the form
					setFormGrantPart(grantPartForm, infoGrantPart);
					request.setAttribute("idSubsidy", infoGrantPart.getInfoGrantSubsidy().getIdInternal());
				} catch (FenixServiceException e) {
					return setError(request, mapping, "errors.grant.part.read", "manage-grant-part", null);
				}
			} else { // New grant part
				Integer idSubsidy = new Integer(request.getParameter("idSubsidy"));
				grantPartForm.set("grantSubsidyId", idSubsidy);
				request.setAttribute("idSubsidy", idSubsidy);
			}
		} else { // Probabily a validation error
			request.setAttribute("idSubsidy", request.getParameter("grantSubsidyId"));
		}
		return mapping.findForward("edit-grant-part");
	}

	/*
	 * Edit a Grant Part
	 */
	public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm editGrantPartForm = (DynaValidatorForm) form;
		InfoGrantPart infoGrantPart = null;
		IUserView userView = UserView.getUser();
		try {
			// Verificar se foi escolhida UMA E SO UMA entidade pagadora
			if (!verifyStringParameterInForm(editGrantPartForm, "project")
					&& !verifyStringParameterInForm(editGrantPartForm, "costCenter")) {
				return setError(request, mapping, "errors.grant.part.mustBeOnePaymentEntity", null, null);
			}
			if (verifyStringParameterInForm(editGrantPartForm, "project")
					&& verifyStringParameterInForm(editGrantPartForm, "costCenter")) {
				return setError(request, mapping, "errors.grant.part.mustBeOnePaymentEntity", null, null);
			}

			infoGrantPart = populateInfoFromForm(editGrantPartForm);

			// Let's read the payment entity
			if (infoGrantPart.getInfoGrantPaymentEntity().getNumber() != null) {
				String paymentEntityClass = null;
				if (verifyStringParameterInForm(editGrantPartForm, "project")) {
					paymentEntityClass = GrantProject.class.getName();
				} else if (verifyStringParameterInForm(editGrantPartForm, "costCenter")) {
					paymentEntityClass = GrantCostCenter.class.getName();
				}

				InfoGrantPaymentEntity infoGrantPaymentEntity =
						ReadPaymentEntityByNumberAndClass.run(infoGrantPart.getInfoGrantPaymentEntity().getNumber(),
								paymentEntityClass);

				if (infoGrantPaymentEntity == null) {
					if (verifyStringParameterInForm(editGrantPartForm, "project")) {
						return setError(request, mapping, "errors.grant.paymententity.unknownProject", null, infoGrantPart
								.getInfoGrantPaymentEntity().getNumber());
					} else if (verifyStringParameterInForm(editGrantPartForm, "costCenter")) {
						return setError(request, mapping, "errors.grant.paymententity.unknownCostCenter", null, infoGrantPart
								.getInfoGrantPaymentEntity().getNumber());
					} else {
						return setError(request, mapping, "errors.grant.part.invalidPaymentEntity", null, null);
					}
				}
				infoGrantPart.setInfoGrantPaymentEntity(infoGrantPaymentEntity);
			}

			if (infoGrantPart.getInfoResponsibleTeacher() == null) {
				// NO part responsible teacher set YET.
				// The part responsbile teacher will be set to be the payment
				// entity responsible teacher
				Object[] args = { infoGrantPart.getInfoGrantPaymentEntity().getIdInternal() };
				InfoGrantPaymentEntity infoGrantPaymentEntity =
						(InfoGrantPaymentEntity) ServiceUtils.executeService("ReadGrantPaymentEntity", args);
				infoGrantPart.setInfoResponsibleTeacher(infoGrantPaymentEntity.getInfoResponsibleTeacher());
			}

			EditGrantPart.run(infoGrantPart);

			request.setAttribute("idSubsidy", editGrantPartForm.get("grantSubsidyId"));

		} catch (InvalidPartResponsibleTeacherException e) {
			return setError(request, mapping, "errors.grant.part.invalidPartTeacher", null, infoGrantPart
					.getInfoResponsibleTeacher().getTeacherId());
		} catch (ExistingServiceException e) {
			return setError(request, mapping, "errors.grant.part.duplicateEntry", null, null);
		} catch (FenixServiceException e) {
			return setError(request, mapping, "errors.grant.part.edit", null, null);
		}
		return mapping.findForward("manage-grant-part");
	}

	/*
	 * Delete a grant part
	 */
	public ActionForward doDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			Integer idGrantPart = new Integer(request.getParameter("idGrantPart"));
			Integer idSubsidy = new Integer(request.getParameter("idSubsidy"));

			Object[] args = { idGrantPart };
			IUserView userView = UserView.getUser();
			ServiceUtils.executeService("DeleteGrantPart", args);

			request.setAttribute("idSubsidy", idSubsidy);
			return mapping.findForward("manage-grant-part");

		} catch (FenixServiceException e) {
			return setError(request, mapping, "errors.grant.part.delete", null, null);
		}
	}

	/*
	 * Populates form from InfoSubsidy
	 */
	private void setFormGrantPart(DynaValidatorForm form, InfoGrantPart infoGrantPart) throws Exception {
		BeanUtils.copyProperties(form, infoGrantPart);
		form.set("grantSubsidyId", infoGrantPart.getInfoGrantSubsidy().getIdInternal());

		if (infoGrantPart.getInfoResponsibleTeacher() != null && infoGrantPart.getInfoResponsibleTeacher().getTeacherId() != null) {
			form.set("responsibleTeacherIstId", infoGrantPart.getInfoResponsibleTeacher().getTeacherId());
		}

		if (infoGrantPart.getInfoGrantPaymentEntity() instanceof InfoGrantCostCenter) {
			form.set("costCenter", infoGrantPart.getInfoGrantPaymentEntity().getNumber());
		} else if (infoGrantPart.getInfoGrantPaymentEntity() instanceof InfoGrantProject) {
			form.set("project", infoGrantPart.getInfoGrantPaymentEntity().getNumber());
		}
	}

	/*
	 * Populates Info from Form
	 */
	private InfoGrantPart populateInfoFromForm(DynaValidatorForm editGrantPartForm) throws Exception {
		InfoGrantPart infoGrantPart = new InfoGrantPart();

		// Percentage
		BeanUtils.copyProperties(infoGrantPart, editGrantPartForm);

		if (verifyStringParameterInForm(editGrantPartForm, "idInternal")) {
			infoGrantPart.setIdInternal((Integer) editGrantPartForm.get("idInternal"));
		}

		InfoGrantSubsidy infoGrantSubsidy = new InfoGrantSubsidy();
		infoGrantSubsidy.setIdInternal((Integer) editGrantPartForm.get("grantSubsidyId"));
		infoGrantPart.setInfoGrantSubsidy(infoGrantSubsidy);

		// The part responsible teacher is only set HERE if the user has chosen
		// one in the form
		// Otherwise, the part responsible teacher will be the payment entity
		// responsible teacher
		if (verifyStringParameterInForm(editGrantPartForm, "responsibleTeacherIstId")) {
			InfoTeacher infoTeacher =
					new InfoTeacher(Teacher.readByIstId((String) editGrantPartForm.get("responsibleTeacherIstId")));
			infoGrantPart.setInfoResponsibleTeacher(infoTeacher);
		}

		// Set the Payment Entity
		InfoGrantPaymentEntity infoPaymentEntity = null;
		if (verifyStringParameterInForm(editGrantPartForm, "project")) {
			infoPaymentEntity = new InfoGrantProject();
			infoPaymentEntity.setNumber((String) editGrantPartForm.get("project"));
		} else if (verifyStringParameterInForm(editGrantPartForm, "costCenter")) {
			infoPaymentEntity = new InfoGrantCostCenter();
			infoPaymentEntity.setNumber((String) editGrantPartForm.get("costCenter"));
		}
		infoGrantPart.setInfoGrantPaymentEntity(infoPaymentEntity);

		return infoGrantPart;
	}

}