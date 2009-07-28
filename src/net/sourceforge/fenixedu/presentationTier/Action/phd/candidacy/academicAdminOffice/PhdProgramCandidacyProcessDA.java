package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessDocument;
import net.sourceforge.fenixedu.domain.phd.candidacy.RatifyCandidacyBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.DeleteDocument;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.RatifyCandidacy;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.UploadDocuments;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdProgramCandidacyProcess", module = "academicAdminOffice")
@Forwards( {

@Forward(name = "searchPerson", path = "/phd/candidacy/academicAdminOffice/searchPerson.jsp"),

@Forward(name = "createCandidacy", path = "/phd/candidacy/academicAdminOffice/createCandidacy.jsp"),

@Forward(name = "manageProcesses", path = "/phdIndividualProgramProcess.do?method=manageProcesses"),

@Forward(name = "manageCandidacyDocuments", path = "/phd/candidacy/academicAdminOffice/manageCandidacyDocuments.jsp"),

@Forward(name = "manageCandidacyReview", path = "/phd/candidacy/academicAdminOffice/manageCandidacyReview.jsp"),

@Forward(name = "ratifyCandidacy", path = "/phd/candidacy/academicAdminOffice/ratifyCandidacy.jsp"),

@Forward(name = "viewProcess", path = "/phdIndividualProgramProcess.do?method=viewProcess")

})
public class PhdProgramCandidacyProcessDA extends PhdProcessDA {

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

    @Override
    protected PhdProgramCandidacyProcess getProcess(HttpServletRequest request) {
	return (PhdProgramCandidacyProcess) super.getProcess(request);
    }

    // Create Candidacy Steps

    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramCandidacyProcessBean bean = new PhdProgramCandidacyProcessBean();
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
	    getCreateCandidacyProcessBean().getPersonBean().setPerson(null);
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

    public ActionForward manageCandidacyDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	prepareDocumentsToUpload(request);
	return mapping.findForward("manageCandidacyDocuments");
    }

    private void prepareDocumentsToUpload(HttpServletRequest request) {
	request.setAttribute("documentsToUpload", Arrays.asList(new PhdCandidacyDocumentUploadBean(),
		new PhdCandidacyDocumentUploadBean(), new PhdCandidacyDocumentUploadBean(), new PhdCandidacyDocumentUploadBean(),
		new PhdCandidacyDocumentUploadBean()));
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
	for (final PhdCandidacyDocumentUploadBean each : getDocumentsToUpload()) {
	    if (each.hasAnyInformation()) {
		return true;
	    }
	}
	return false;
    }

    protected List<PhdCandidacyDocumentUploadBean> getDocumentsToUpload() {
	return (List<PhdCandidacyDocumentUploadBean>) getObjectFromViewState("documentsToUpload");
    }

    public ActionForward manageCandidacyReview(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdCandidacyDocumentUploadBean bean = new PhdCandidacyDocumentUploadBean();
	bean.setType(PhdIndividualProgramDocumentType.CANDIDACY_REVIEW);
	request.setAttribute("documentToUpload", bean);
	return mapping.findForward("manageCandidacyReview");
    }

    public ActionForward uploadCandidacyReviewInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("documentToUpload", getRenderedObject("documentToUpload"));
	RenderUtils.invalidateViewState();
	return mapping.findForward("manageCandidacyReview");
    }

    public ActionForward uploadCandidacyReview(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdCandidacyDocumentUploadBean bean = (PhdCandidacyDocumentUploadBean) getRenderedObject("documentToUpload");
	RenderUtils.invalidateViewState();

	try {
	    ExecuteProcessActivity.run(getProcess(request), UploadDocuments.class, Collections.singletonList(bean));
	    addSuccessMessage(request, "message.document.uploaded.with.success");

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    bean.removeFile();
	    request.setAttribute("documentToUpload", bean);
	    return mapping.findForward("manageCandidacyReview");
	}

	return manageCandidacyReview(mapping, actionForm, request, response);
    }

    public ActionForward deleteDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	prepareDocumentsToUpload(request);

	return executeActivity(DeleteDocument.class, getDocument(request), request, mapping, "manageCandidacyDocuments",
		"manageCandidacyDocuments", "message.document.deleted.successfuly");
    }

    public ActionForward deleteCandidacyReview(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), DeleteDocument.class, getDocument(request));
	    addSuccessMessage(request, "message.document.uploaded.with.success");

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	}

	return manageCandidacyReview(mapping, actionForm, request, response);
    }

    private PhdProgramCandidacyProcessDocument getDocument(HttpServletRequest request) {
	return getDomainObject(request, "documentId");
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

}
