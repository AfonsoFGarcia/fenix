package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.CreateNewProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessStateBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.RatifyCandidacyBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.RegistrationFormalizationBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.AddNotification;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.DeleteDocument;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.RatifyCandidacy;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.RegistrationFormalization;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.RequestCandidacyReview;
import net.sourceforge.fenixedu.domain.phd.notification.PhdNotification;
import net.sourceforge.fenixedu.domain.phd.notification.PhdNotificationBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.CommonPhdCandidacyDA;
import net.sourceforge.fenixedu.presentationTier.docs.phd.notification.PhdCandidacyDeclarationDocument;
import net.sourceforge.fenixedu.presentationTier.docs.phd.notification.PhdNotificationDocument;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/phdProgramCandidacyProcess", module = "academicAdminOffice")
@Forwards( {

@Forward(name = "searchPerson", path = "/phd/candidacy/academicAdminOffice/searchPerson.jsp"),

@Forward(name = "createCandidacy", path = "/phd/candidacy/academicAdminOffice/createCandidacy.jsp"),

@Forward(name = "manageProcesses", path = "/phdIndividualProgramProcess.do?method=manageProcesses"),

@Forward(name = "manageCandidacyDocuments", path = "/phd/candidacy/academicAdminOffice/manageCandidacyDocuments.jsp"),

@Forward(name = "requestCandidacyReview", path = "/phd/candidacy/academicAdminOffice/requestCandidacyReview.jsp"),

@Forward(name = "manageCandidacyReview", path = "/phd/candidacy/academicAdminOffice/manageCandidacyReview.jsp"),

@Forward(name = "rejectCandidacyProcess", path = "/phd/candidacy/academicAdminOffice/rejectCandidacyProcess.jsp"),

@Forward(name = "ratifyCandidacy", path = "/phd/candidacy/academicAdminOffice/ratifyCandidacy.jsp"),

@Forward(name = "viewProcess", path = "/phdIndividualProgramProcess.do?method=viewProcess"),

@Forward(name = "manageNotifications", path = "/phd/candidacy/academicAdminOffice/manageNotifications.jsp"),

@Forward(name = "createNotification", path = "/phd/candidacy/academicAdminOffice/createNotification.jsp"),

@Forward(name = "registrationFormalization", path = "/phd/candidacy/academicAdminOffice/registrationFormalization.jsp")

})
public class PhdProgramCandidacyProcessDA extends CommonPhdCandidacyDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final Process process = getProcess(request);
	if (process != null) {
	    request.setAttribute("processId", process.getExternalId());
	    request.setAttribute("process", process);
	}

	return super.execute(mapping, actionForm, request, response);
    }

    // Create Candidacy Steps

    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean();
	bean.setState(PhdProgramCandidacyProcessState.STAND_BY_WITH_MISSING_INFORMATION);
	bean.setPersonBean(new PersonBean());
	bean.setChoosePersonBean(new ChoosePersonBean());

	request.setAttribute("createCandidacyBean", bean);
	request.setAttribute("persons", Collections.emptyList());

	return mapping.findForward("searchPerson");
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final PhdProgramCandidacyProcessBean bean = getCreateCandidacyProcessBean();
	request.setAttribute("createCandidacyBean", bean);

	final ChoosePersonBean choosePersonBean = getCreateCandidacyProcessBean().getChoosePersonBean();
	if (!choosePersonBean.hasPerson()) {
	    if (choosePersonBean.isFirstTimeSearch()) {
		final Collection<Person> persons = Person.findPersonByDocumentID(choosePersonBean.getIdentificationNumber());
		choosePersonBean.setFirstTimeSearch(false);
		if (showSimilarPersons(choosePersonBean, persons)) {
		    RenderUtils.invalidateViewState();
		    return mapping.findForward("searchPerson");
		}
	    }
	    bean.setPersonBean(new PersonBean(choosePersonBean.getName(), choosePersonBean.getIdentificationNumber(),
		    choosePersonBean.getDocumentType(), choosePersonBean.getDateOfBirth()));

	    return mapping.findForward("createCandidacy");

	} else {
	    bean.setPersonBean(new PersonBean(bean.getChoosePersonBean().getPerson()));
	    setIsEmployeeAttributeAndMessage(request, bean.getChoosePersonBean().getPerson());
	    return mapping.findForward("createCandidacy");
	}

    }

    protected boolean showSimilarPersons(final ChoosePersonBean choosePersonBean, final Collection<Person> persons) {
	if (!persons.isEmpty()) {
	    return true;
	}
	return !Person.findByDateOfBirth(choosePersonBean.getDateOfBirth(),
		Person.findInternalPersonMatchingFirstAndLastName(choosePersonBean.getName())).isEmpty();
    }

    public ActionForward createCandidacyInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createCandidacyBean", getCreateCandidacyProcessBean());
	setIsEmployeeAttributeAndMessage(request, getCreateCandidacyProcessBean().getChoosePersonBean().getPerson());

	return mapping.findForward("createCandidacy");
    }

    public ActionForward createCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	try {

	    if (!validateAreaCodeAndAreaOfAreaCode(request, getCreateCandidacyProcessBean().getChoosePersonBean().getPerson(),
		    getCreateCandidacyProcessBean().getPersonBean().getCountryOfResidence(), getCreateCandidacyProcessBean()
			    .getPersonBean().getAreaCode(), getCreateCandidacyProcessBean().getPersonBean().getAreaOfAreaCode())) {

		setIsEmployeeAttributeAndMessage(request, getCreateCandidacyProcessBean().getChoosePersonBean().getPerson());
		request.setAttribute("createCandidacyBean", getCreateCandidacyProcessBean());
		return mapping.findForward("createCandidacy");

	    }

	    CreateNewProcess.run(PhdIndividualProgramProcess.class, getCreateCandidacyProcessBean());

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    setIsEmployeeAttributeAndMessage(request, getCreateCandidacyProcessBean().getChoosePersonBean().getPerson());
	    request.setAttribute("createCandidacyBean", getCreateCandidacyProcessBean());
	    return mapping.findForward("createCandidacy");
	}

	return mapping.findForward("manageProcesses");

    }

    private PhdProgramCandidacyProcessBean getCreateCandidacyProcessBean() {
	return (PhdProgramCandidacyProcessBean) getRenderedObject("createCandidacyBean");
    }

    // End of Create Candidacy Steps

    public ActionForward cancelCreateCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return mapping.findForward("manageProcesses");
    }

    @Override
    public ActionForward manageCandidacyDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	prepareDocumentsToUpload(request);
	return mapping.findForward("manageCandidacyDocuments");
    }

    private void prepareDocumentsToUpload(HttpServletRequest request) {
	request.setAttribute("documentsToUpload", Arrays.asList(new PhdProgramDocumentUploadBean(),
		new PhdProgramDocumentUploadBean(), new PhdProgramDocumentUploadBean(), new PhdProgramDocumentUploadBean(),
		new PhdProgramDocumentUploadBean()));
    }

    public ActionForward uploadDocumentsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("documentsToUpload", getDocumentsToUpload());
	return mapping.findForward("manageCandidacyDocuments");
    }

    public ActionForward uploadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	if (!hasAnyDocumentToUpload()) {
	    request.setAttribute("documentsToUpload", getDocumentsToUpload());

	    addErrorMessage(request, "message.no.documents.to.upload");

	    return mapping.findForward("manageCandidacyDocuments");
	}

	final ActionForward result = executeActivity(PhdProgramCandidacyProcess.UploadDocuments.class, getDocumentsToUpload(),
		request, mapping, "manageCandidacyDocuments", "manageCandidacyDocuments",
		"message.documents.uploaded.with.success");

	RenderUtils.invalidateViewState("documentsToUpload");

	prepareDocumentsToUpload(request);

	return result;

    }

    protected boolean hasAnyDocumentToUpload() {
	for (final PhdProgramDocumentUploadBean each : getDocumentsToUpload()) {
	    if (each.hasAnyInformation()) {
		return true;
	    }
	}
	return false;
    }

    protected List<PhdProgramDocumentUploadBean> getDocumentsToUpload() {
	return (List<PhdProgramDocumentUploadBean>) getObjectFromViewState("documentsToUpload");
    }

    public ActionForward prepareRequestCandidacyReview(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramCandidacyProcessStateBean bean = new PhdProgramCandidacyProcessStateBean();
	bean.setState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION);
	request.setAttribute("stateBean", bean);
	return mapping.findForward("requestCandidacyReview");
    }

    public ActionForward requestCandidacyReview(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    final PhdProgramCandidacyProcess process = getProcess(request);
	    ExecuteProcessActivity.run(process, RequestCandidacyReview.class.getSimpleName(), getRenderedObject("stateBean"));
	    return viewIndividualProgramProcess(request, process);

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("stateBean", getRenderedObject("stateBean"));
	    return mapping.findForward("requestCandidacyReview");
	}
    }

    public ActionForward deleteDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	prepareDocumentsToUpload(request);

	return executeActivity(DeleteDocument.class, getDocument(request), request, mapping, "manageCandidacyDocuments",
		"manageCandidacyDocuments", "message.document.deleted.successfuly");
    }

    public ActionForward prepareRatifyCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("ratifyCandidacyBean", new RatifyCandidacyBean(getProcess(request)));

	return mapping.findForward("ratifyCandidacy");
    }

    public ActionForward prepareRatifyCandidacyInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("ratifyCandidacyBean", getRenderedObject("ratifyCandidacyBean"));

	return mapping.findForward("ratifyCandidacy");
    }

    public ActionForward ratifyCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final RatifyCandidacyBean bean = (RatifyCandidacyBean) getRenderedObject("ratifyCandidacyBean");
	try {
	    ExecuteProcessActivity.run(getProcess(request), RatifyCandidacy.class, bean);
	    addSuccessMessage(request, "message.candidacy.ratified.successfuly");

	    request.setAttribute("processId", getProcess(request).getIndividualProgramProcess().getExternalId());

	    return mapping.findForward("viewProcess");

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());

	    request.setAttribute("ratifyCandidacyBean", bean);

	    return mapping.findForward("ratifyCandidacy");
	}
    }

    // Notification Management

    public ActionForward manageNotifications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return mapping.findForward("manageNotifications");
    }

    public ActionForward prepareCreateNotification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("notificationBean", new PhdNotificationBean(getProcess(request)));

	return mapping.findForward("createNotification");
    }

    public ActionForward prepareCreateNotificationInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("notificationBean", getRenderedObject("notificationBean"));

	return mapping.findForward("createNotification");
    }

    public ActionForward createNotification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdNotificationBean bean = (PhdNotificationBean) getRenderedObject("notificationBean");

	final ActionForward result = executeActivity(AddNotification.class, bean, request, mapping, "createNotification",
		"manageNotifications", "message.notification.created.with.success");

	request.setAttribute("notificationBean", bean);

	return result;
    }

    private PhdNotification getNotification(HttpServletRequest request) {
	return getDomainObject(request, "notificationId");
    }

    public ActionForward printNotification(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JRException, IOException {

	final PhdNotificationDocument report = new PhdNotificationDocument(getNotification(request));
	writeFile(response, report.getReportFileName() + ".pdf", "application/pdf", ReportsUtils
		.exportToProcessedPdfAsByteArray(report));

	return null;

    }

    public ActionForward markNotificationAsSent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	getNotification(request).markAsSent();

	return manageNotifications(mapping, form, request, response);

    }

    public ActionForward printCandidacyDeclarationPt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JRException {

	final PhdCandidacyDeclarationDocument report = new PhdCandidacyDeclarationDocument(getProcess(request), Language.pt);
	writeFile(response, report.getReportFileName() + ".pdf", "application/pdf", ReportsUtils
		.exportToProcessedPdfAsByteArray(report));

	return null;

    }

    public ActionForward printCandidacyDeclarationEn(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JRException {

	final PhdCandidacyDeclarationDocument report = new PhdCandidacyDeclarationDocument(getProcess(request), Language.en);
	writeFile(response, report.getReportFileName() + ".pdf", "application/pdf", ReportsUtils
		.exportToProcessedPdfAsByteArray(report));

	return null;

    }

    // End of Notification Management

    // Begin RegistrationFormalization

    public ActionForward prepareRegistrationFormalization(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	checkCandidacyDocuments(request);
	final RegistrationFormalizationBean bean = new RegistrationFormalizationBean();
	request.setAttribute("registrationFormalizationBean", bean);
	return mapping.findForward("registrationFormalization");
    }

    private void checkCandidacyDocuments(final HttpServletRequest request) {
	final PhdProgramCandidacyProcess process = getProcess(request);
	final Person person = process.getPerson();

	request.setAttribute("idDocument", process.hasAnyDocuments(PhdIndividualProgramDocumentType.ID_DOCUMENT));
	request.setAttribute("personalPhoto", process.getPerson().hasPersonalPhoto());
	request.setAttribute("healthBulletin", process.hasAnyDocuments(PhdIndividualProgramDocumentType.HEALTH_BULLETIN));
	request.setAttribute("habilitationsCertificates", person.getAssociatedQualificationsCount() == process
		.getDocumentsCount(PhdIndividualProgramDocumentType.HABILITATION_CERTIFICATE_DOCUMENT));
    }

    public ActionForward registrationFormalizationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	checkCandidacyDocuments(request);
	request.setAttribute("registrationFormalizationBean", getRenderedObject("registrationFormalizationBean"));
	return mapping.findForward("registrationFormalization");
    }

    public ActionForward registrationFormalization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), RegistrationFormalization.class,
		    getRenderedObject("registrationFormalizationBean"));

	    // TODO: message and warning due to insurance, enrolment debts, etc
	    // etc
	    // addSuccessMessage(request,
	    // "message.candidacy.ratified.successfuly");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    return registrationFormalizationInvalid(mapping, actionForm, request, response);
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }
    // End of RegistrationFormalization
}
