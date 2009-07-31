package net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.secondCycle;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.RefactoredIndividualCandidacyProcessPublicDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/candidacies/caseHandlingSecondCycleCandidacyIndividualProcess", module = "publico", formBeanClass = FenixActionForm.class)
@Forwards( { @Forward(name = "begin-candidacy-process-intro", path = "second.cycle.candidacy.process.intro"),
	@Forward(name = "begin-candidacy-process-intro-en", path = "second.cycle.candidacy.process.intro.en"),
	@Forward(name = "open-candidacy-process-closed", path = "candidacy.process.closed"),
	@Forward(name = "show-pre-creation-candidacy-form", path = "show.pre.creation.candidacy.form"),
	@Forward(name = "show-email-message-sent", path = "show.email.message.sent"),
	@Forward(name = "show-application-submission-conditions", path = "show.application.submission.conditions"),
	@Forward(name = "open-candidacy-processes-not-found", path = "individual.candidacy.not.found"),
	@Forward(name = "show-candidacy-creation-page", path = "second.cycle.candidacy.creation.page"),
	@Forward(name = "candidacy-continue-creation", path = "second.cycle.candidacy.continue.creation"),
	@Forward(name = "inform-submited-candidacy", path = "inform.submited.candidacy"),
	@Forward(name = "show-candidacy-details", path = "second.cycle.show.candidacy.details"),
	@Forward(name = "edit-candidacy", path = "second.cycle.edit.candidacy"),
	@Forward(name = "edit-candidacy-habilitations", path = "second.cycle.edit.candidacy.habilitations"),
	@Forward(name = "edit-candidacy-documents", path = "second.cycle.edit.candidacy.documents") })
public class SecondCycleIndividualCandidacyProcessRefactoredDA extends RefactoredIndividualCandidacyProcessPublicDA {

    @Override
    protected Class<? extends CandidacyProcess> getParentProcessType() {
	return SecondCycleCandidacyProcess.class;
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub

    }

    @Override
    protected Class getProcessType() {
	return SecondCycleIndividualCandidacyProcess.class;
    }

    @Override
    protected String getCandidacyNameKey() {
	return "title.application.name.secondCycle";
    }

    @Override
    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SecondCycleIndividualCandidacyProcess individualCandidacyProcess = (SecondCycleIndividualCandidacyProcess) request
		.getAttribute("individualCandidacyProcess");
	SecondCycleIndividualCandidacyProcessBean bean = new SecondCycleIndividualCandidacyProcessBean(individualCandidacyProcess);

	bean.setPersonBean(new PersonBean(individualCandidacyProcess.getPersonalDetails()));
	bean.setCandidacyInformationBean(new CandidacyInformationBean(individualCandidacyProcess.getCandidacy()));

	request.setAttribute("individualCandidacyProcessBean", bean);

	return mapping.findForward("show-candidacy-details");
    }

    public ActionForward prepareCandidacyCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
	if (actionForwardError != null)
	    return actionForwardError;

	CandidacyProcess candidacyProcess = getCurrentOpenParentProcess();

	String hash = request.getParameter("hash");
	DegreeOfficePublicCandidacyHashCode candidacyHashCode = (DegreeOfficePublicCandidacyHashCode) PublicCandidacyHashCode
		.getPublicCandidacyCodeByHash(hash);

	if (candidacyHashCode == null) {
	    return mapping.findForward("open-candidacy-processes-not-found");
	}

	if (candidacyHashCode.getIndividualCandidacyProcess() != null
		&& candidacyHashCode.getIndividualCandidacyProcess().getCandidacyProcess() == candidacyProcess) {
	    request.setAttribute("individualCandidacyProcess", candidacyHashCode.getIndividualCandidacyProcess());
	    return viewCandidacy(mapping, form, request, response);
	} else if (candidacyHashCode.getIndividualCandidacyProcess() != null
		&& candidacyHashCode.getIndividualCandidacyProcess().getCandidacyProcess() != candidacyProcess) {
	    return mapping.findForward("open-candidacy-processes-not-found");
	}

	SecondCycleIndividualCandidacyProcessBean bean = new SecondCycleIndividualCandidacyProcessBean();
	bean.setPrecedentDegreeInformation(new CandidacyPrecedentDegreeInformationBean());
	bean.setPersonBean(new PersonBean());
	bean.setCandidacyProcess(candidacyProcess);
	bean.setCandidacyInformationBean(new CandidacyInformationBean());
	bean.setPublicCandidacyHashCode(candidacyHashCode);

	bean.getPersonBean().setName("Anil Mamede Ali Kassamali");
	bean.getPersonBean().setAddress("Rua Vasco Da Gama n�4");
	bean.getPersonBean().setArea("Odivelas");
	bean.getPersonBean().setAreaCode("2675-460");
	bean.getPersonBean().setGender(Gender.MALE);
	bean.getPersonBean().setPhone("939584538");

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	bean.getPersonBean().setEmail(candidacyHashCode.getEmail());
	return mapping.findForward("show-candidacy-creation-page");

    }

    private ActionForward forwardTo(ActionMapping mapping, HttpServletRequest request) {
	if (getFromRequest(request, "userAction").equals("createCandidacy")) {
	    return mapping.findForward("candidacy-continue-creation");
	} else if (getFromRequest(request, "userAction").equals("editCandidacyQualifications")) {
	    return mapping.findForward("edit-candidacy-habilitations");
	}

	return null;
    }

    public ActionForward addConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	bean.addConcludedFormationBean();

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward removeConcludedHabilitationsEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	Integer index = getIntegerFromRequest(request, "removeIndex");
	bean.removeFormationConcludedBean(index);

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	invalidateDocumentFileRelatedViewStates();

	return forwardTo(mapping, request);
    }

    public ActionForward submitCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, FenixFilterException, FenixServiceException {
	try {
	    ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
	    if (actionForwardError != null)
		return actionForwardError;

	    SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	    bean.setInternalPersonCandidacy(Boolean.TRUE);

	    boolean isValid = hasInvalidViewState();
	    if (!isValid) {
		invalidateDocumentFileRelatedViewStates();
		request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
		return mapping.findForward("candidacy-continue-creation");
	    }

	    if (candidacyIndividualProcessExistsForThisEmail(bean.getPersonBean().getEmail())) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
	    }

	    if (!bean.getHonorAgreement()) {
		addActionMessage("error", request, "error.must.agree.on.declaration.of.honor");
		invalidateDocumentFileRelatedViewStates();
		request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
		return mapping.findForward("candidacy-continue-creation");
	    }

	    copyPrecedentBeanToCandidacyInformationBean(bean.getPrecedentDegreeInformation(), bean.getCandidacyInformationBean());

	    SecondCycleIndividualCandidacyProcess process = (SecondCycleIndividualCandidacyProcess) createNewPublicProcess(bean);

	    request.setAttribute("process", process);
	    request.setAttribute("mappingPath", mapping.getPath());
	    request.setAttribute("individualCandidacyProcess", process);
	    request.setAttribute("endSubmissionDate", getFormattedApplicationSubmissionEndDate());

	    return mapping.findForward("inform-submited-candidacy");
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    e.printStackTrace();
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("candidacy-continue-creation");
	}
    }

    public ActionForward editCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	try {
	    ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
	    if (actionForwardError != null)
		return actionForwardError;

	    copyPrecedentBeanToCandidacyInformationBean(bean.getPrecedentDegreeInformation(), bean.getCandidacyInformationBean());

	    if (!isApplicationSubmissionPeriodValid()) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
	    }

	    executeActivity(bean.getIndividualCandidacyProcess(), "EditPublicCandidacyPersonalInformation",
		    getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy");
	}

	request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
	return backToViewCandidacyInternal(mapping, form, request, response);
    }

    public ActionForward editCandidacyQualifications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
	if (actionForwardError != null)
	    return actionForwardError;

	SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	try {
	    boolean isValid = hasInvalidViewState();
	    if (!isValid) {
		request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
		return mapping.findForward("edit-candidacy-habilitations");
	    }

	    copyPrecedentBeanToCandidacyInformationBean(bean.getPrecedentDegreeInformation(), bean.getCandidacyInformationBean());

	    executeActivity(bean.getIndividualCandidacyProcess(), "EditPublicCandidacyHabilitations",
		    getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy-habilitations");
	}

	request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
	return backToViewCandidacyInternal(mapping, form, request, response);
    }

    @Override
    protected String getCandidacyInformationLinkDefaultLanguage() {
	return "link.candidacy.information.default.secondCycle";
    }

    @Override
    protected String getCandidacyInformationLinkEnglish() {
	return "link.candidacy.information.english.secondCycle";
    }

}
