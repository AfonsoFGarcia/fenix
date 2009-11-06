/*
 * Created on 04/Dec/2003
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.owner;

import net.sourceforge.fenixedu.applicationTier.Servico.grant.owner.EditGrantOwner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.general.ReadAllCountries;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ReadPersonByID;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountryEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Barbosa
 * @author Pica
 */

public class EditGrantOwnerAction extends FenixDispatchAction {
	/*
	 * Fills the form with the correspondent data
	 */
	public ActionForward prepareEditGrantOwnerForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Integer loaddb = null;
		Integer personId = null;
		Integer idInternal = null;

		if (verifyParameterInRequest(request, "loaddb")) {
			loaddb = new Integer(request.getParameter("loaddb"));
		}
		if (verifyParameterInRequest(request, "personId")) {
			personId = new Integer(request.getParameter("personId"));
		}
		if (verifyParameterInRequest(request, "idGrantOwner")) {
			idInternal = new Integer(request.getParameter("idGrantOwner"));
		}

		/*
		 * loaddb=1 --> first time on page, load contents from db personId not
		 * null --> create grant owner from searchGrantOwnerResult idGrantOwner
		 * not null --> edit grant owner from manageGrantOwner
		 */
		InfoGrantOwner infoGrantOwner = new InfoGrantOwner();
		if (loaddb != null && loaddb.equals(new Integer(1))) // Load contents
		// from database
		{
			if (idInternal != null) // Comes from manageGrantOwner
			{
				// Read the grant owner
				Object[] args = { idInternal };
				infoGrantOwner = (InfoGrantOwner) executeService("ReadGrantOwner", args);
				request.setAttribute("toShow", "toShow");
			} else if (personId != null) {
				// Read the person (grant owner doesn't exist)

				InfoPerson infoPerson = null;
				infoPerson = (InfoPerson) ReadPersonByID.run(personId);
				infoGrantOwner.setPersonInfo(infoPerson);
				request.setAttribute("toShow", "toShow");
			}
		}

		DynaValidatorForm grantOwnerInformationForm = (DynaValidatorForm) form;
		if (request.getParameter("searchPerson") != null) {
			ActionMessages errorMessages = validateDocumentIDInfo(grantOwnerInformationForm, request);
			if (!errorMessages.isEmpty()) {
				saveErrors(request, errorMessages);
				return mapping.findForward("edit-grant-owner-form");
			}
			String documentIdNumber = grantOwnerInformationForm.getString("idNumber");
			IDDocumentType idDocumentType = IDDocumentType.valueOf((String) grantOwnerInformationForm.get("idType"));
			Person person = Person.readByDocumentIdNumberAndIdDocumentType(documentIdNumber, idDocumentType);
			if (person != null) {
				infoGrantOwner.setPersonInfo(new InfoPerson(person));
				ActionMessages actionMessages = getMessages(request);
				actionMessages.add("message", new ActionMessage("message.grant.personAlreadyExists"));
				saveMessages(request, actionMessages);
			} else {
				ActionMessages actionMessages = getMessages(request);
				actionMessages.add("message", new ActionMessage("message.grant.personDoesNotExist"));
				saveMessages(request, actionMessages);
			}
			request.setAttribute("toShow", "toShow");
		}

		List countryList = (List) ReadAllCountries.run();
		Collections.sort(countryList, new BeanComparator("name"));
		request.setAttribute("countryList", countryList);
		
		// Fill the form (grant owner e person information)
		if (infoGrantOwner.getIdInternal() != null) {
			setFormGrantOwnerInformation(grantOwnerInformationForm, infoGrantOwner);
		}
		if (infoGrantOwner.getPersonInfo() != null && infoGrantOwner.getPersonInfo().getIdInternal() != null
				&& !infoGrantOwner.getPersonInfo().getIdInternal().equals(new Integer(0))) {
			setFormPersonalInformation(grantOwnerInformationForm, infoGrantOwner);
		}

		if (infoGrantOwner.getIdInternal() != null) {
			request.setAttribute("idInternal", infoGrantOwner.getIdInternal().toString());
		}

		List maritalStatusList = Arrays.asList(MaritalStatus.values());
		request.setAttribute("maritalStatusList", maritalStatusList);
		
		return mapping.findForward("edit-grant-owner-form");
	}

	private ActionMessages validateDocumentIDInfo(DynaValidatorForm grantOwnerInformationForm, HttpServletRequest request) {
		ActionMessages actionMessages = getActionMessages(request);
		ResourceBundle bundle = ResourceBundle.getBundle("resources.FacultyAdmOfficeResources");

		if (StringUtils.isEmpty(grantOwnerInformationForm.getString("idNumber"))) {
			actionMessages.add("error", new ActionMessage("errors.required", bundle
					.getString("label.grant.owner.infoperson.documentId")));
		}
		if (StringUtils.isEmpty(grantOwnerInformationForm.getString("idType"))) {
			actionMessages.add("error", new ActionMessage("errors.required", bundle
					.getString("label.grant.owner.infoperson.documentIdType")));
		}
		return actionMessages;
	}

	public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm editGrantOwnerForm = (DynaValidatorForm) form;
		InfoGrantOwner infoGrantOwner = null;
		Integer grantOwnerId = null;

		try {
			infoGrantOwner = populateInfoFromForm(editGrantOwnerForm);

			if (infoGrantOwner.getInfoPersonEditor().getTipoDocumentoIdentificacao() == null) {
				return setError(request, mapping, "errors.grant.owner.idtype", null, null);
			}

			// Edit Grant Owner

			grantOwnerId = (Integer) EditGrantOwner.run(infoGrantOwner);

		} catch (DomainException e) {
			return setError(request, mapping, "errors.grant.owner.personexists", null, null);
		} catch (ExistingServiceException e) {
			return setError(request, mapping, "errors.grant.owner.personexists", null, null);
		} catch (FenixServiceException e) {
			return setError(request, mapping, "errors.grant.owner.exists", null, null);
		}

		try {
			// Read the grant owner by person
			Object[] args2 = { grantOwnerId };
			infoGrantOwner = (InfoGrantOwner) executeService("ReadGrantOwner", args2);
		} catch (FenixServiceException e) {
			return setError(request, mapping, "errors.grant.owner.bd.read", null, null);
		}

		if (infoGrantOwner == null) {
			return setError(request, mapping, "errors.grant.owner.readafterwrite", null, null);
		}

		request.setAttribute("idInternal", infoGrantOwner.getIdInternal());
		return mapping.findForward("manage-grant-owner");
	}

	/*
	 * Populates form from InfoPerson
	 */
	private void setFormPersonalInformation(DynaValidatorForm form, InfoGrantOwner infoGrantOwner) throws Exception {
		InfoPerson infoPerson = infoGrantOwner.getPersonInfo();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		form.set("idInternalPerson", infoPerson.getIdInternal().toString());
		form.set("idNumber", infoPerson.getNumeroDocumentoIdentificacao());
		if (infoPerson.getLocalEmissaoDocumentoIdentificacao() != null)
			form.set("idLocation", infoPerson.getLocalEmissaoDocumentoIdentificacao());
		if (infoPerson.getDataEmissaoDocumentoIdentificacao() != null)
			form.set("idDate", sdf.format(infoPerson.getDataEmissaoDocumentoIdentificacao()));
		if (infoPerson.getDataValidadeDocumentoIdentificacao() != null)
			form.set("idValidDate", sdf.format(infoPerson.getDataValidadeDocumentoIdentificacao()));
		if (infoPerson.getNome() != null)
			form.set("name", infoPerson.getNome());
		if (infoPerson.getNascimento() != null)
			form.set("birthdate", sdf.format(infoPerson.getNascimento()));
		if (infoPerson.getNomePai() != null)
			form.set("fatherName", infoPerson.getNomePai());
		if (infoPerson.getNomeMae() != null)
			form.set("motherName", infoPerson.getNomeMae());
		if (infoPerson.getNacionalidade() != null)
			form.set("nationality", infoPerson.getNacionalidade());
		if (infoPerson.getFreguesiaNaturalidade() != null)
			form.set("parishOfBirth", infoPerson.getFreguesiaNaturalidade());
		if (infoPerson.getConcelhoNaturalidade() != null)
			form.set("districtSubBirth", infoPerson.getConcelhoNaturalidade());
		if (infoPerson.getDistritoNaturalidade() != null)
			form.set("districtBirth", infoPerson.getDistritoNaturalidade());
		if (infoPerson.getMorada() != null)
			form.set("address", infoPerson.getMorada());
		if (infoPerson.getLocalidade() != null)
			form.set("area", infoPerson.getLocalidade());
		if (infoPerson.getCodigoPostal() != null)
			form.set("areaCode", infoPerson.getCodigoPostal());
		if (infoPerson.getLocalidadeCodigoPostal() != null)
			form.set("areaOfAreaCode", infoPerson.getLocalidadeCodigoPostal());
		if (infoPerson.getFreguesiaMorada() != null)
			form.set("addressParish", infoPerson.getFreguesiaMorada());
		if (infoPerson.getConcelhoMorada() != null)
			form.set("addressDistrictSub", infoPerson.getConcelhoMorada());
		if (infoPerson.getDistritoMorada() != null)
			form.set("addressDistrict", infoPerson.getDistritoMorada());
		if (infoPerson.getTelefone() != null)
			form.set("phone", infoPerson.getTelefone());
		if (infoPerson.getWorkPhone() != null)
			form.set("workphone", infoPerson.getWorkPhone());
		if (infoPerson.getTelemovel() != null)
			form.set("cellphone", infoPerson.getTelemovel());
		if (infoPerson.getEmail() != null)
			form.set("email", infoPerson.getEmail());
		if (infoPerson.getEnderecoWeb() != null)
			form.set("homepage", infoPerson.getEnderecoWeb());
		if (infoPerson.getNumContribuinte() != null)
			form.set("socialSecurityNumber", infoPerson.getNumContribuinte());
		if (infoPerson.getProfissao() != null)
			form.set("profession", infoPerson.getProfissao());
		if (infoPerson.getUsername() != null)
			form.set("personUsername", infoPerson.getUsername());
		if (infoPerson.getPassword() != null)
			form.set("password", infoPerson.getPassword());
		if (infoPerson.getCodigoFiscal() != null)
			form.set("fiscalCode", infoPerson.getCodigoFiscal());
		form.set("idType", infoPerson.getTipoDocumentoIdentificacao().toString());
		if (infoPerson.getSexo() != null)
			form.set("sex", infoPerson.getSexo().toString());
		if (infoPerson.getMaritalStatus() != null)
			form.set("maritalStatus", infoPerson.getMaritalStatus().toString());
		if (infoPerson.getInfoPais() != null)
			form.set("country", infoPerson.getInfoPais().getIdInternal());
	}

	/*
	 * Populates form from InfoGrantOwner
	 */
	private void setFormGrantOwnerInformation(DynaValidatorForm form, InfoGrantOwner infoGrantOwner) throws Exception {
		form.set("idGrantOwner", infoGrantOwner.getIdInternal().toString());
		form.set("grantOwnerNumber", infoGrantOwner.getGrantOwnerNumber().toString());
		if (infoGrantOwner.getCardCopyNumber() != null)
			form.set("cardCopyNumber", infoGrantOwner.getCardCopyNumber().toString());
		if (infoGrantOwner.getDateSendCGD() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			form.set("dateSendCGD", sdf.format(infoGrantOwner.getDateSendCGD()));
		}
	}

	/*
	 * Populate infoGrantOwner from form
	 */
	private InfoGrantOwner populateInfoFromForm(DynaValidatorForm editGrantOwnerForm) throws FenixServiceException {

		InfoGrantOwner infoGrantOwner = new InfoGrantOwner();
		InfoPersonEditor infoPerson = new InfoPersonEditor();

		// Format of date in the form
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		try {
			if (editGrantOwnerForm.get("idDate") != null && !editGrantOwnerForm.get("idDate").equals(""))
				infoPerson.setDataEmissaoDocumentoIdentificacao(sdf.parse((String) editGrantOwnerForm.get("idDate")));
			if (editGrantOwnerForm.get("idValidDate") != null && !editGrantOwnerForm.get("idValidDate").equals(""))
				infoPerson.setDataValidadeDocumentoIdentificacao(sdf.parse((String) editGrantOwnerForm.get("idValidDate")));
			if (editGrantOwnerForm.get("birthdate") != null && !editGrantOwnerForm.get("birthdate").equals(""))
				infoPerson.setNascimento(sdf.parse((String) editGrantOwnerForm.get("birthdate")));
			if (editGrantOwnerForm.get("dateSendCGD") != null && !editGrantOwnerForm.get("dateSendCGD").equals(""))
				infoGrantOwner.setDateSendCGD(sdf.parse((String) editGrantOwnerForm.get("dateSendCGD")));
		} catch (ParseException e) {
			throw new FenixServiceException();
		}

		// GrantOwner
		if (!((String) editGrantOwnerForm.get("idGrantOwner")).equals(""))
			infoGrantOwner.setIdInternal(new Integer((String) editGrantOwnerForm.get("idGrantOwner")));

		if (!((String) editGrantOwnerForm.get("grantOwnerNumber")).equals(""))
			infoGrantOwner.setGrantOwnerNumber(new Integer((String) editGrantOwnerForm.get("grantOwnerNumber")));
		if (!((String) editGrantOwnerForm.get("cardCopyNumber")).equals(""))
			infoGrantOwner.setCardCopyNumber(new Integer((String) editGrantOwnerForm.get("cardCopyNumber")));

		// Person
		if (!((String) editGrantOwnerForm.get("idInternalPerson")).equals(""))
			infoPerson.setIdInternal(new Integer((String) editGrantOwnerForm.get("idInternalPerson")));
		infoPerson.setNumeroDocumentoIdentificacao((String) editGrantOwnerForm.get("idNumber"));
		infoPerson.setLocalEmissaoDocumentoIdentificacao((String) editGrantOwnerForm.get("idLocation"));
		infoPerson.setNome((String) editGrantOwnerForm.get("name"));
		infoPerson.setNomePai((String) editGrantOwnerForm.get("fatherName"));
		infoPerson.setNomeMae((String) editGrantOwnerForm.get("motherName"));
		infoPerson.setFreguesiaNaturalidade((String) editGrantOwnerForm.get("parishOfBirth"));
		infoPerson.setConcelhoNaturalidade((String) editGrantOwnerForm.get("districtSubBirth"));
		infoPerson.setDistritoNaturalidade((String) editGrantOwnerForm.get("districtBirth"));
		infoPerson.setMorada((String) editGrantOwnerForm.get("address"));
		infoPerson.setLocalidade((String) editGrantOwnerForm.get("area"));
		infoPerson.setCodigoPostal((String) editGrantOwnerForm.get("areaCode"));
		infoPerson.setLocalidadeCodigoPostal((String) editGrantOwnerForm.get("areaOfAreaCode"));
		infoPerson.setFreguesiaMorada((String) editGrantOwnerForm.get("addressParish"));
		infoPerson.setConcelhoMorada((String) editGrantOwnerForm.get("addressDistrictSub"));
		infoPerson.setDistritoMorada((String) editGrantOwnerForm.get("addressDistrict"));
		infoPerson.setTelefone((String) editGrantOwnerForm.get("phone"));
		infoPerson.setWorkPhone((String) editGrantOwnerForm.get("workphone"));
		infoPerson.setTelemovel((String) editGrantOwnerForm.get("cellphone"));
		infoPerson.setEmail((String) editGrantOwnerForm.get("email"));
		infoPerson.setEnderecoWeb((String) editGrantOwnerForm.get("homepage"));
		infoPerson.setNumContribuinte((String) editGrantOwnerForm.get("socialSecurityNumber"));
		infoPerson.setProfissao((String) editGrantOwnerForm.get("profession"));
		infoPerson.setUsername((String) editGrantOwnerForm.get("personUsername"));
		infoPerson.setPassword((String) editGrantOwnerForm.get("password"));
		infoPerson.setCodigoFiscal((String) editGrantOwnerForm.get("fiscalCode"));

		infoPerson.setAvailableEmail(Boolean.FALSE);
		infoPerson.setAvailablePhoto(Boolean.FALSE);
		infoPerson.setAvailableWebSite(Boolean.FALSE);

		if ((editGrantOwnerForm.get("idType")).equals("") || ((String) editGrantOwnerForm.get("idType")).equals(null)) {
			infoPerson.setTipoDocumentoIdentificacao(null);
		} else {
			infoPerson.setTipoDocumentoIdentificacao(IDDocumentType.valueOf((String) editGrantOwnerForm.get("idType")));
		}
		Gender sexo = Gender.valueOf((String) editGrantOwnerForm.get("sex"));
		infoPerson.setSexo(sexo);
		MaritalStatus estadoCivil;
		if (((String) editGrantOwnerForm.get("maritalStatus")).equals("")
				|| ((String) editGrantOwnerForm.get("maritalStatus")).equals("null"))
			estadoCivil = MaritalStatus.SINGLE;
		else
			estadoCivil = MaritalStatus.valueOf((String) editGrantOwnerForm.get("maritalStatus"));
		infoPerson.setMaritalStatus(estadoCivil);

		InfoCountryEditor infoCountry = new InfoCountryEditor();
		if (((Integer) editGrantOwnerForm.get("country")).equals(new Integer(0))) {
			infoCountry.setIdInternal(null);
		} else {
			infoCountry.setIdInternal((Integer) editGrantOwnerForm.get("country"));
		}
		infoPerson.setInfoPais(infoCountry);
		infoGrantOwner.setInfoPersonEditor(infoPerson);
		return infoGrantOwner;
	}
}