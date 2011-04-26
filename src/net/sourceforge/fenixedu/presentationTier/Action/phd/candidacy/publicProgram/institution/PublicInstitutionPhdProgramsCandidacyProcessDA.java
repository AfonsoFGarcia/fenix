package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram.institution;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.CreateNewProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.applicationTier.Servico.person.qualification.DeleteQualification;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddCandidacyReferees;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddQualification;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.EditIndividualProcessInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.EditPersonalInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.PublicPhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.UploadDocuments;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.InstitutionPhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetterBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram.PublicPhdProgramCandidacyProcessDA;
import net.sourceforge.fenixedu.util.phd.InstitutionPhdCandidacyProcessProperties;
import net.sourceforge.fenixedu.util.phd.PhdProperties;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/applications/phd/phdProgramApplicationProcess", module = "publico")
@Forwards(tileProperties = @Tile(extend = "definition.candidacy.process"), value = {
	@Forward(name = "outOfCandidacyPeriod", path = "/phd/candidacy/publicProgram/institution/outOfCandidacyPeriod.jsp"),
	@Forward(name = "createIdentification", path = "/phd/candidacy/publicProgram/institution/createIdentification.jsp"),
	@Forward(name = "createIdentificationSuccess", path = "/phd/candidacy/publicProgram/institution/createIdentificationSuccess.jsp"),
	@Forward(name = "applicationSubmissionGuide", path = "/phd/candidacy/publicProgram/institution/applicationSubmissionGuide.jsp"),
	@Forward(name = "fillPersonalData", path = "/phd/candidacy/publicProgram/institution/fillPersonalData.jsp"),
	@Forward(name = "fillPhdProgramData", path = "/phd/candidacy/publicProgram/institution/fillPhdProgramData.jsp"),
	@Forward(name = "applicationCreationReport", path = "/phd/candidacy/publicProgram/institution/applicationCreationReport.jsp"),
	@Forward(name = "view", path = "/phd/candidacy/publicProgram/institution/view.jsp"),
	@Forward(name = "editPersonalData", path = "/phd/candidacy/publicProgram/institution/editPersonalData.jsp"),
	@Forward(name = "editPhdInformationData", path = "/phd/candidacy/publicProgram/institution/editPhdInformationData.jsp"),
	@Forward(name = "editQualifications", path = "/phd/candidacy/publicProgram/institution/editQualifications.jsp"),
	@Forward(name = "uploadDocuments", path = "/phd/candidacy/publicProgram/institution/uploadDocuments.jsp"),
	@Forward(name = "editReferees", path = "/phd/candidacy/publicProgram/institution/editReferees.jsp"),
	@Forward(name = "createRefereeLetter", path = "/phd/candidacy/publicProgram/institution/createRefereeLetter.jsp"),
	@Forward(name = "createRefereeLetterSuccess", path = "/phd/candidacy/publicProgram/institution/createRefereeLetterSuccess.jsp") })
public class PublicInstitutionPhdProgramsCandidacyProcessDA extends PublicPhdProgramCandidacyProcessDA {

    static private final List<String> DO_NOT_VALIDATE_CANDIDACY_PERIOD_IN_METHODS = Arrays.asList(

    "viewCandidacy",

    "backToViewCandidacy",

    "prepareCreateRefereeLetter",

    "createRefereeLetterInvalid",

    "createRefereeLetter");

    protected ActionForward filterDispatchMethod(final PhdProgramCandidacyProcessBean bean, ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	final PhdProgramPublicCandidacyHashCode hashCode = (bean != null ? bean.getCandidacyHashCode() : null);
	final String methodName = getMethodName(mapping, actionForm, request, response, mapping.getParameter());

	if (methodName == null || !DO_NOT_VALIDATE_CANDIDACY_PERIOD_IN_METHODS.contains(methodName)) {
	    if (isOutOfCandidacyPeriod(hashCode)) {
		request.setAttribute("candidacyPeriod", getPhdCandidacyPeriod(hashCode));
		return mapping.findForward("outOfCandidacyPeriod");
	    }
	}

	return null;
    }

    private boolean isOutOfCandidacyPeriod(final PhdProgramPublicCandidacyHashCode hashCode) {
	PhdCandidacyPeriod phdCandidacyPeriod = getPhdCandidacyPeriod(hashCode);
	return phdCandidacyPeriod == null || !phdCandidacyPeriod.contains(new DateTime());
    }

    private PhdCandidacyPeriod getPhdCandidacyPeriod(final PhdProgramPublicCandidacyHashCode hashCode) {
	final LocalDate localDate = (hashCode != null && hashCode.hasCandidacyProcess()) ? hashCode
		.getPhdProgramCandidacyProcess().getCandidacyDate() : new LocalDate();

	return InstitutionPhdCandidacyPeriod.readInstitutionPhdCandidacyPeriodForDate(localDate.toDateMidnight().toDateTime());
    }

    /*
     * Create application identification for submission with email
     */
    public ActionForward prepareCreateIdentification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final String hash = request.getParameter("hash");
	final PhdProgramPublicCandidacyHashCode hashCode = (PhdProgramPublicCandidacyHashCode) PublicCandidacyHashCode
		.getPublicCandidacyCodeByHash(hash);
	if (hashCode != null) {
	    return viewCandidacy(mapping, form, request, response, hashCode);
	}

	request.setAttribute("candidacyBean", new PhdProgramCandidacyProcessBean());

	return mapping.findForward("createIdentification");
    }

    public ActionForward createIdentificationInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyBean", getRenderedObject("candidacyBean"));
	return mapping.findForward("createIdentification");
    }

    public ActionForward createIdentification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	final PhdProgramPublicCandidacyHashCode hashCode = PhdProgramPublicCandidacyHashCode
		.getOrCreatePhdProgramCandidacyHashCode(bean.getEmail());

	if (hashCode.hasCandidacyProcess()) {
	    addErrorMessage(request, "error.PhdProgramPublicCandidacyHashCode.already.has.candidacy");
	    return prepareCreateIdentification(mapping, form, request, response);
	}

	sendSubmissionEmailForCandidacy(hashCode, request);

	return mapping.findForward("createIdentificationSuccess");
    }

    private void sendSubmissionEmailForCandidacy(final PublicCandidacyHashCode hashCode, final HttpServletRequest request) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
	final String subject = bundle.getString("message.phd.email.subject.send.link.to.submission");
	final String body = bundle.getString("message.phd.institution.email.body.send.link.to.submission");
	hashCode.sendEmail(
		subject,
		String.format(body, InstitutionPhdCandidacyProcessProperties.getPublicCandidacySubmissionLink(),
			hashCode.getValue()));
    }

    /*
     * Identification recovery
     */
    public ActionForward prepareIdentificationRecovery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyBean", new PhdProgramCandidacyProcessBean());
	return mapping.findForward("identificationRecovery");
    }

    public ActionForward identificationRecoveryInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyBean", getCandidacyBean());

	return mapping.findForward("identificationRecovery");
    }

    public ActionForward identificationRecovery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	final PhdProgramPublicCandidacyHashCode hashCode = PhdProgramPublicCandidacyHashCode.getPhdProgramCandidacyHashCode(bean
		.getEmail());

	if (hashCode != null) {
	    if (hashCode.hasCandidacyProcess()) {
		sendRecoveryEmailForCandidate(hashCode, request);
	    } else {
		sendSubmissionEmailForCandidacy(hashCode, request);
	    }
	}

	return mapping.findForward("emailSentForIdentificationRecovery");
    }

    private void sendRecoveryEmailForCandidate(PhdProgramPublicCandidacyHashCode candidacyHashCode, HttpServletRequest request) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
	final String subject = bundle.getString("message.phd.email.subject.recovery.access");
	final String body = bundle.getString("message.phd.email.body.recovery.access");
	candidacyHashCode.sendEmail(subject,
		String.format(body, PhdProperties.getPublicCandidacyAccessLink(), candidacyHashCode.getValue()));
    }

    /*
     * Submission forms
     */

    public ActionForward beginSubmission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return mapping.findForward("applicationSubmissionGuide");
    }

    public ActionForward prepareFillPersonalData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramPublicCandidacyHashCode hashCode = (PhdProgramPublicCandidacyHashCode) PublicCandidacyHashCode
		.getPublicCandidacyCodeByHash(request.getParameter("hash"));

	if (hashCode == null) {
	    return prepareCreateIdentification(mapping, form, request, response);
	}

	if (hashCode.hasCandidacyProcess()) {
	    return viewCandidacy(mapping, form, request, response, hashCode);
	}

	final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean();
	bean.setPersonBean(new PersonBean());
	bean.getPersonBean().setPhotoAvailable(true);
	bean.getPersonBean().setEmail(hashCode.getEmail());
	bean.getPersonBean().setCreateLoginIdentificationAndUserIfNecessary(false);
	bean.setCandidacyHashCode(hashCode);
	bean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	bean.setState(PhdProgramCandidacyProcessState.PRE_CANDIDATE);
	bean.setMigratedProcess(Boolean.FALSE);
	bean.setPhdCandidacyPeriod(getPhdCandidacyPeriod(hashCode));

	request.setAttribute("candidacyBean", bean);

	return mapping.findForward("fillPersonalData");
    }

    public ActionForward fillPersonalDataInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyBean", getCandidacyBean());

	return mapping.findForward("fillPersonalData");
    }

    public ActionForward returnToPersonalData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyBean", getCandidacyBean());

	return mapping.findForward("fillPersonalData");
    }

    public ActionForward checkPersonalData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();

	final PersonBean personBean = bean.getPersonBean();
	final Person person = Person.readByDocumentIdNumberAndIdDocumentType(personBean.getDocumentIdNumber(),
		personBean.getIdDocumentType());

	// check if person already exists
	if (person != null) {
	    if (person.getDateOfBirthYearMonthDay().equals(personBean.getDateOfBirth())) {
		if (person.hasIstUsername() && person.getIstUsername().equals(bean.getInstitutionId())) {
		    personBean.setPerson(person);
		} else if (!person.hasIstUsername() && !bean.hasInstitutionId()) {
		    personBean.setPerson(person);
		} else {
		    addErrorMessage(request, "error.phd.public.candidacy.fill.personal.information.and.institution.id");
		    return fillPersonalDataInvalid(mapping, form, request, response);
		}
	    } else {
		addErrorMessage(request, "error.phd.public.candidacy.fill.personal.information.and.institution.id");
		return fillPersonalDataInvalid(mapping, form, request, response);
	    }
	}

	return prepareFillPhdProgramData(mapping, form, request, response);
    }

    protected ActionForward prepareFillPhdProgramData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyBean", getCandidacyBean());

	return mapping.findForward("fillPhdProgramData");
    }

    public ActionForward fillPhdProgramDataInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyBean", getCandidacyBean());
	RenderUtils.invalidateViewState();

	return mapping.findForward("fillPhdProgramData");
    }

    public ActionForward fillPhdProgramDataPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyBean", getCandidacyBean());
	return mapping.findForward("fillPhdProgramData");
    }

    public ActionForward createApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	CreateNewProcess.run(PublicPhdIndividualProgramProcess.class, bean);
	sendApplicationSuccessfullySubmitedEmail(bean.getCandidacyHashCode(), request);

	request.setAttribute("candidacyHashCode", bean.getCandidacyHashCode());
	return mapping.findForward("applicationCreationReport");
    }

    private void sendApplicationSuccessfullySubmitedEmail(final PhdProgramPublicCandidacyHashCode hashCode,
	    final HttpServletRequest request) {

	// TODO: if candidacy period exists, then change body message to send
	// candidacy limit end date

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
	final String subject = bundle.getString("message.phd.institution.email.subject.application.submited");
	final String body = bundle.getString("message.phd.institution.email.body.application.submited");
	hashCode.sendEmail(subject, String.format(body, hashCode.getPhdProgramCandidacyProcess().getProcessNumber(),
		InstitutionPhdCandidacyProcessProperties.getPublicCandidacyAccessLink(), hashCode.getValue()));
    }

    /*
     * View application
     */

    public ActionForward viewApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return viewCandidacy(mapping, form, request, response,
		(PhdProgramPublicCandidacyHashCode) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(request
			.getParameter("hash")));
    }

    private ActionForward viewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, PhdProgramPublicCandidacyHashCode hashCode) {

	if (hashCode == null || !hashCode.hasCandidacyProcess()) {
	    return prepareFillPersonalData(mapping, form, request, response);
	}

	PhdIndividualProgramProcess individualProgramProcess = hashCode.getIndividualProgramProcess();
	request.setAttribute("process", individualProgramProcess.getCandidacyProcess());

	canEditCandidacy(request, hashCode);
	canEditPersonalInformation(request, hashCode.getPerson());

	validateProcess(request, individualProgramProcess);

	return mapping.findForward("view");
    }

    private boolean validateProcess(final HttpServletRequest request, final PhdIndividualProgramProcess process) {
	boolean result = true;

	if (!process.hasPhdProgramFocusArea()) {
	    addValidationMessage(request, "message.validation.missing.focus.area");
	    result &= false;
	}

	return validateProcessDocuments(request, process) && result;
    }

    private boolean validateProcessDocuments(final HttpServletRequest request, final PhdIndividualProgramProcess process) {
	boolean result = true;

	if (!process.hasCandidacyProcessDocument(PhdIndividualProgramDocumentType.CV)) {
	    addValidationMessage(request, "message.validation.missing.cv");
	    result &= false;
	}
	if (!process.hasCandidacyProcessDocument(PhdIndividualProgramDocumentType.ID_DOCUMENT)) {
	    addValidationMessage(request, "message.validation.missing.id.document");
	    result &= false;
	}
	if (!process.hasCandidacyProcessDocument(PhdIndividualProgramDocumentType.MOTIVATION_LETTER)) {
	    addValidationMessage(request, "message.validation.missing.motivation.letter");
	    result &= false;
	}
	if (process.getCandidacyProcessDocumentsCount(PhdIndividualProgramDocumentType.HABILITATION_CERTIFICATE_DOCUMENT) < process
		.getQualifications().size()) {
	    addValidationMessage(request, "message.validation.missing.qualification.documents",
		    String.valueOf(process.getQualifications().size()));
	    result &= false;
	}

	return result;
    }

    /*
     * Edit personal data
     */

    public ActionForward prepareEditPersonalData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramCandidacyProcess process = getProcess(request);
	final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean(process);
	bean.setPersonBean(new PersonBean(process.getPerson()));

	canEditCandidacy(request, process.getCandidacyHashCode());
	canEditPersonalInformation(request, process.getPerson());

	request.setAttribute("candidacyBean", bean);
	return mapping.findForward("editPersonalData");
    }

    public ActionForward editPersonalDataInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramCandidacyProcess process = getProcess(request);
	PhdProgramCandidacyProcessBean candidacyBean = getCandidacyBean();
	request.setAttribute("candidacyBean", candidacyBean);

	canEditCandidacy(request, process.getCandidacyHashCode());
	canEditPersonalInformation(request, process.getPerson());

	return mapping.findForward("editPersonalData");
    }

    public ActionForward editPersonalData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	PhdProgramCandidacyProcess process = getProcess(request);

	canEditCandidacy(request, process.getCandidacyHashCode());
	canEditPersonalInformation(request, process.getPerson());

	try {
	    ExecuteProcessActivity.run(createMockUserView(process.getPerson()), process.getIndividualProgramProcess(),
		    EditPersonalInformation.class, bean.getPersonBean());
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("candidacyBean", bean);
	    return mapping.findForward("editPersonalData");
	}

	return viewCandidacy(mapping, form, request, response, process.getCandidacyHashCode());
    }

    /*
     * Edit phd information data
     */
    public ActionForward prepareEditPhdInformationData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramCandidacyProcess process = getProcess(request);
	final PhdProgramCandidacyProcessBean candidacyBean = new PhdProgramCandidacyProcessBean(process);

	request.setAttribute("candidacyBean", candidacyBean);
	request.setAttribute("individualProcessBean", new PhdIndividualProgramProcessBean(process.getIndividualProgramProcess()));

	canEditCandidacy(request, process.getCandidacyHashCode());

	return mapping.findForward("editPhdInformationData");
    }

    public ActionForward prepareEditPhdInformationDataFocusAreaPostback(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	final PhdProgramCandidacyProcess process = getProcess(request);

	PhdProgramCandidacyProcessBean candidacyBean = getCandidacyBean();
	final PhdIndividualProgramProcessBean bean = getIndividualProcessBean();

	request.setAttribute("candidacyBean", candidacyBean);
	request.setAttribute("individualProcessBean", bean);

	canEditCandidacy(request, process.getCandidacyHashCode());

	return mapping.findForward("editPhdInformationData");
    }

    public ActionForward editPhdInformationDataInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramCandidacyProcess process = getProcess(request);
	PhdProgramCandidacyProcessBean candidacyBean = getCandidacyBean();
	final PhdIndividualProgramProcessBean bean = getIndividualProcessBean();

	request.setAttribute("candidacyBean", candidacyBean);
	request.setAttribute("individualProcessBean", bean);

	canEditCandidacy(request, process.getCandidacyHashCode());

	return mapping.findForward("editPhdInformationData");
    }

    public ActionForward editPhdInformationData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdIndividualProgramProcessBean bean = getIndividualProcessBean();
	PhdProgramCandidacyProcess process = getProcess(request);

	canEditCandidacy(request, process.getCandidacyHashCode());

	try {
	    ExecuteProcessActivity.run(createMockUserView(process.getPerson()), process.getIndividualProgramProcess(),
		    EditIndividualProcessInformation.class, bean);
	    addSuccessMessage(request, "message.phdIndividualProgramProcessInformation.edit.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("candidacyBean", getCandidacyBean());
	    request.setAttribute("individualProcessBean", bean);

	    return mapping.findForward("editPhdInformationData");
	}

	return viewCandidacy(mapping, form, request, response, process.getCandidacyHashCode());
    }

    /*
     * Qualifications
     */

    public ActionForward prepareEditQualifications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdProgramCandidacyProcess process = getProcess(request);
	canEditCandidacy(request, process.getCandidacyHashCode());

	PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean(process);
	QualificationBean qualificationBean = new QualificationBean();

	request.setAttribute("candidacyBean", bean);
	request.setAttribute("qualificationBean", qualificationBean);

	return mapping.findForward("editQualifications");
    }

    public ActionForward editQualificationsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	QualificationBean qualificationBean = getQualificationBean();

	request.setAttribute("candidacyBean", bean);
	request.setAttribute("qualificationBean", qualificationBean);

	return prepareEditQualifications(mapping, form, request, response);
    }

    public ActionForward addQualification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdProgramCandidacyProcess process = getProcess(request);
	canEditCandidacy(request, process.getCandidacyHashCode());

	try {
	    ExecuteProcessActivity.run(createMockUserView(process.getPerson()), process.getIndividualProgramProcess(),
		    AddQualification.class, getQualificationBean());
	    addSuccessMessage(request, "message.qualification.information.create.success");
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    return editQualificationsInvalid(mapping, form, request, response);
	}

	RenderUtils.invalidateViewState();
	return prepareEditQualifications(mapping, form, request, response);
    }

    public ActionForward removeQualification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdProgramCandidacyProcess process = getProcess(request);
	canEditCandidacy(request, process.getCandidacyHashCode());

	try {
	    ExecuteProcessActivity.run(createMockUserView(process.getPerson()), process.getIndividualProgramProcess(),
		    DeleteQualification.class, (Qualification) getDomainObject(request, "qualificationId"));
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    return editQualificationsInvalid(mapping, form, request, response);
	}

	RenderUtils.invalidateViewState();
	return prepareEditQualifications(mapping, form, request, response);
    }

    /*
     * Upload documents
     */

    public ActionForward prepareUploadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcess process = getProcess(request);
	final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean(process);

	canEditCandidacy(request, process.getCandidacyHashCode());

	RenderUtils.invalidateViewState();

	request.setAttribute("candidacyBean", bean);
	request.setAttribute("candidacyProcessDocuments", process.getIndividualProgramProcess().getCandidacyProcessDocuments());

	final PhdProgramDocumentUploadBean uploadBean = new PhdProgramDocumentUploadBean();
	uploadBean.setIndividualProgramProcess(process.getIndividualProgramProcess());
	request.setAttribute("documentByType", uploadBean);

	validateProcessDocuments(request, process.getIndividualProgramProcess());

	return mapping.findForward("uploadDocuments");

    }

    public ActionForward uploadDocumentsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramCandidacyProcess process = getProcess(request);
	PhdProgramCandidacyProcessBean bean = getCandidacyBean();
	PhdProgramDocumentUploadBean uploadBean = getUploadBean();

	request.setAttribute("candidacyProcessDocuments", process.getIndividualProgramProcess().getCandidacyProcessDocuments());

	request.setAttribute("candidacyBean", bean);
	request.setAttribute("documentByType", uploadBean);

	return mapping.findForward("uploadDocuments");
    }

    public ActionForward uploadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramCandidacyProcess process = getProcess(request);

	if (!RenderUtils.getViewState("documentByType").isValid()) {
	    return uploadDocumentsInvalid(mapping, form, request, response);
	}

	final PhdProgramDocumentUploadBean uploadBean = getUploadBean();

	if (!uploadBean.hasAnyInformation()) {
	    addErrorMessage(request, "message.no.documents.to.upload");
	    return uploadDocumentsInvalid(mapping, form, request, response);

	}
	try {
	    ExecuteProcessActivity.run(createMockUserView(process.getPerson()), process.getIndividualProgramProcess(),
		    UploadDocuments.class, Collections.singletonList(uploadBean));
	    addSuccessMessage(request, "message.documents.uploaded.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, "message.no.documents.to.upload");
	    return uploadDocumentsInvalid(mapping, form, request, response);
	}

	return prepareUploadDocuments(mapping, form, request, response);
    }

    /*
     * Edit phd referees
     */

    public ActionForward prepareEditReferees(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramCandidacyProcess process = getProcess(request);
	final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean(process);

	canEditCandidacy(request, process.getCandidacyHashCode());

	request.setAttribute("candidacyBean", bean);
	request.setAttribute("refereeBean", new PhdCandidacyRefereeBean());

	return mapping.findForward("editReferees");
    }

    public ActionForward addReferee(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramCandidacyProcess process = getProcess(request);

	try {
	    ExecuteProcessActivity.run(createMockUserView(process.getPerson()), process.getIndividualProgramProcess(),
		    AddCandidacyReferees.class, Collections.singletonList(getRenderedObject("refereeBean")));

	    addSuccessMessage(request, "message.qualification.information.create.success");
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    return editRefereesInvalid(mapping, form, request, response);
	}

	RenderUtils.invalidateViewState();

	return prepareEditReferees(mapping, form, request, response);
    }

    public ActionForward editRefereesInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramCandidacyProcess process = getProcess(request);

	request.setAttribute("candidacyBean", getCandidacyBean());
	request.setAttribute("refereeBean", getPhdCandidacyReferee());

	canEditCandidacy(request, process.getCandidacyHashCode());

	return mapping.findForward("editReferees");
    }

    public ActionForward sendCandidacyRefereeEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdCandidacyReferee referee = getDomainObject(request, "candidacyRefereeId");
	referee.sendEmail();
	addSuccessMessage(request, "message.candidacy.referee.email.sent.with.success", referee.getName());

	return prepareEditReferees(mapping, form, request, response);
    }

    public ActionForward prepareCreateRefereeLetter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdCandidacyReferee hashCode = (PhdCandidacyReferee) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(request
		.getParameter("hash"));

	request.setAttribute("refereeLetterHash", hashCode);

	if (hashCode == null) {
	    request.setAttribute("no-information", Boolean.TRUE);
	    return mapping.findForward("createRefereeLetterSuccess");
	}

	if (hashCode.hasLetter()) {
	    request.setAttribute("has-letter", Boolean.TRUE);
	    request.setAttribute("letter", hashCode.getLetter());
	    return mapping.findForward("createRefereeLetterSuccess");
	}

	final PhdCandidacyRefereeLetterBean bean = new PhdCandidacyRefereeLetterBean();
	bean.setCandidacyReferee(hashCode);
	bean.setRefereeName(hashCode.getName());
	request.setAttribute("createRefereeLetterBean", bean);
	return mapping.findForward("createRefereeLetter");
    }
    
    public ActionForward createRefereeLetterInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdCandidacyReferee hashCode = (PhdCandidacyReferee) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(request
		.getParameter("hash"));

	request.setAttribute("refereeLetterHash", hashCode);

	final PhdCandidacyRefereeLetterBean bean = new PhdCandidacyRefereeLetterBean();

	bean.setCandidacyReferee(hashCode);
	bean.setRefereeName(hashCode.getName());
	request.setAttribute("createRefereeLetterBean", bean);
	return mapping.findForward("createRefereeLetter");
    }

    public ActionForward createRefereeLetter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdCandidacyReferee hashCode = (PhdCandidacyReferee) PublicCandidacyHashCode.getPublicCandidacyCodeByHash(request
		.getParameter("hash"));

	request.setAttribute("refereeLetterHash", hashCode);

	final PhdCandidacyRefereeLetterBean bean = getRenderedObject("createRefereeLetterBean");

	if (hasAnyRefereeLetterViewStateInvalid()) {
	    return createRefereeLetterInvalid(mapping, actionForm, request, response);
	}

	try {
	    PhdCandidacyRefereeLetter.create(bean);

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("createRefereeLetterBean", bean);
	    return mapping.findForward("createRefereeLetter");
	}

	request.setAttribute("created-with-success", Boolean.TRUE);
	return mapping.findForward("createRefereeLetterSuccess");
    }

    private boolean hasAnyRefereeLetterViewStateInvalid() {
	for (final IViewState viewState : getViewStatesWithPrefixId("createRefereeLetterBean.")) {
	    if (!viewState.isValid()) {
		return true;
	    }
	}
	return false;
    }

    /*
     * Phd Guidings
     */


    /*
     * Validate application
     */

    private QualificationBean getQualificationBean() {
	return getRenderedObject("qualificationBean");
    }

    private PhdIndividualProgramProcessBean getIndividualProcessBean() {
	return getRenderedObject("individualProcessBean");
    }

    private PhdProgramDocumentUploadBean getUploadBean() {
	return getRenderedObject("documentByType");

    }

    private PhdCandidacyRefereeBean getPhdCandidacyReferee() {
	return getRenderedObject("refereeBean");
    }
}
