package net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.erasmus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusVacancy;
import net.sourceforge.fenixedu.domain.candidacyProcess.exceptions.HashCodeForEmailAndProcessAlreadyBounded;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.DegreeCourseInformationBean;
import net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.RefactoredIndividualCandidacyProcessPublicDA;
import net.sourceforge.fenixedu.presentationTier.docs.candidacy.erasmus.LearningAgreementDocument;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.util.StringUtils;
import net.sourceforge.fenixedu.util.report.ReportsUtils;
import net.sourceforge.fenixedu.util.stork.Attribute;
import net.sourceforge.fenixedu.util.stork.SPUtil;
import net.spy.memcached.MemcachedClient;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.I18NFilter;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/candidacies/caseHandlingErasmusCandidacyIndividualProcess", module = "publico", formBeanClass = FenixActionForm.class)
@Forwards( {
	@Forward(name = "candidacy-process-intro", path = "erasmus.candidacy.process.intro"),
	@Forward(name = "candidacy-submission-type", path = "erasmus.candidacy.submission.type"),
	@Forward(name = "show-pre-creation-candidacy-form", path = "erasmus.show.pre.creation.candidacy.form"),
	@Forward(name = "show-email-message-sent", path = "erasmus.show.email.message.sent"),
	@Forward(name = "show-application-submission-conditions", path = "erasmus.show.application.submission.conditions"),
	@Forward(name = "open-candidacy-processes-not-found", path = "individual.candidacy.not.found"),
	@Forward(name = "show-candidacy-creation-page", path = "erasmus.candidacy.creation.page"),
	@Forward(name = "candidacy-continue-creation", path = "erasmus.candidacy.continue.creation"),
	@Forward(name = "fill-degree-and-courses-information", path = "erasmus.fill.degree.and.courses.information"),
	@Forward(name = "accept-honour-declaration", path = "erasmus.fill.accept.honour.declaration"),
	@Forward(name = "inform-submited-candidacy", path = "erasmus.inform.submited.candidacy"),
	@Forward(name = "show-candidacy-details", path = "erasmus.show.candidacy.details"),
	@Forward(name = "edit-candidacy", path = "erasmus.edit.candidacy"),
	@Forward(name = "edit-candidacy-information", path = "erasmus.edit.candidacy.information"),
	@Forward(name = "edit-candidacy-degree-and-courses", path = "erasmus.edit.candidacy.degree.and.courses"),
	@Forward(name = "edit-candidacy-documents", path = "erasmus.edit.candidacy.documents"),
	@Forward(name = "edit-candidacy-degree-and-courses", path = "erasmus.edit.candidacy.degree.and.courses"),
	@Forward(name = "redirect-to-peps", path = "erasmus.redirect.to.peps"),
	@Forward(name = "show-application-submission-conditions-for-stork", path = "erasmus.show.application.submission.conditions.for.stork"),
	@Forward(name = "show-stork-error-page", path = "erasmus.show.stork.error.page"),
	@Forward(name = "stork-candidacy-already-bounded", path = "erasmus.stork.candidacy.already.bounded"),
	@Forward(name = "redirect-to-peps-to-access-application", path = "erasmus.redirect.to.peps.to.access.application"),
	@Forward(name = "stork-error-authentication-failed", path = "erasmus.stork.authentication.failed") })
public class ErasmusIndividualCandidacyProcessPublicDA extends RefactoredIndividualCandidacyProcessPublicDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	I18NFilter.setLocale(request, request.getSession(true), Locale.ENGLISH);
	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected String getCandidacyInformationLinkDefaultLanguage() {
	return "link.candidacy.information.default.erasmus";
    }

    @Override
    protected String getCandidacyInformationLinkEnglish() {
	return "link.candidacy.information.english.erasmus";
    }

    @Override
    protected String getCandidacyNameKey() {
	return "title.application.name.erasmus";
    }

    @Override
    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ErasmusIndividualCandidacyProcess individualCandidacyProcess = (ErasmusIndividualCandidacyProcess) request
		.getAttribute("individualCandidacyProcess");
	ErasmusIndividualCandidacyProcessBean bean = new ErasmusIndividualCandidacyProcessBean(individualCandidacyProcess);

	bean.setPersonBean(new PersonBean(individualCandidacyProcess.getPersonalDetails()));

	request.setAttribute("individualCandidacyProcessBean", bean);

	return mapping.findForward("show-candidacy-details");
    }

    @Override
    protected Class getParentProcessType() {
	return ErasmusCandidacyProcess.class;
    }

    @Override
    protected Class getProcessType() {
	return ErasmusIndividualCandidacyProcess.class;
    }

    public ActionForward intro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("candidacy-process-intro");
    }

    public ActionForward chooseSubmissionType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("candidacy-submission-type");
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

	ErasmusIndividualCandidacyProcessBean bean = new ErasmusIndividualCandidacyProcessBean(candidacyProcess);
	bean.setPersonBean(new PersonBean());
	bean.getPersonBean().setIdDocumentType(IDDocumentType.FOREIGNER_IDENTITY_CARD);
	bean.setPublicCandidacyHashCode(candidacyHashCode);

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	bean.getPersonBean().setEmail(candidacyHashCode.getEmail());
	return mapping.findForward("show-candidacy-creation-page");
    }

    @Override
    public ActionForward fillExternalPrecedentInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	return mapping.findForward("candidacy-continue-creation");
    }

    public ActionForward fillDegreeInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	if (bean.getErasmusStudentDataBean().getDateOfDeparture().isBefore(bean.getErasmusStudentDataBean().getDateOfArrival())) {
	    addActionMessage("error", request, "erasmus.error.date.of.departure.before.date.of.arrival");
	    return mapping.findForward("candidacy-continue-creation");
	}

	request.setAttribute("degreeCourseInformationBean", new DegreeCourseInformationBean(
		(ExecutionYear) getCurrentOpenParentProcess().getCandidacyExecutionInterval()));

	return mapping.findForward("fill-degree-and-courses-information");
    }

    private DegreeCourseInformationBean readDegreeCourseInformationBean(HttpServletRequest request) {
	return (DegreeCourseInformationBean) getRenderedObject("degree.course.information.bean");
    }

    public ActionForward chooseDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

	RenderUtils.invalidateViewState();

	if ("editCandidacy".equals(request.getParameter("userAction"))) {
	    return mapping.findForward("edit-candidacy-degree-and-courses");
	}

	return mapping.findForward("fill-degree-and-courses-information");
    }

    public ActionForward addCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	DegreeCourseInformationBean degreeCourseBean = readDegreeCourseInformationBean(request);

	if (degreeCourseBean.getChosenCourse() != null) {
	    bean.addCurricularCourse(degreeCourseBean.getChosenCourse());
	}

	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

	RenderUtils.invalidateViewState();

	if ("editCandidacy".equals(request.getParameter("userAction"))) {
	    return mapping.findForward("edit-candidacy-degree-and-courses");
	}

	return mapping.findForward("fill-degree-and-courses-information");
    }

    public ActionForward removeCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	DegreeCourseInformationBean degreeCourseBean = readDegreeCourseInformationBean(request);

	CurricularCourse courseToRemove = getDomainObject(request, "removeCourseId");
	bean.removeCurricularCourse(courseToRemove);

	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

	RenderUtils.invalidateViewState();

	if ("editCandidacy".equals(request.getParameter("userAction"))) {
	    return mapping.findForward("edit-candidacy-degree-and-courses");
	}

	return mapping.findForward("fill-degree-and-courses-information");
    }

    public ActionForward acceptHonourDeclaration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);

	try {
	    ErasmusVacancy vacancy = bean.calculateErasmusVacancy();
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getMessage());
	    request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));
	    RenderUtils.invalidateViewState();

	    return mapping.findForward("fill-degree-and-courses-information");
	}

	return mapping.findForward("accept-honour-declaration");
    }

    public ActionForward submitCandidacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, FenixFilterException, FenixServiceException {
	try {
	    ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
	    if (actionForwardError != null)
		return actionForwardError;

	    ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	    bean.setInternalPersonCandidacy(Boolean.TRUE);

	    boolean isValid = hasInvalidViewState();
	    if (!isValid) {
		invalidateDocumentFileRelatedViewStates();
		request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
		return mapping.findForward("accept-honour-declaration");
	    }

	    if (candidacyIndividualProcessExistsForThisEmail(bean.getPersonBean().getEmail())) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
	    }

	    if (!bean.getHonorAgreement()) {
		addActionMessage("error", request, "error.must.agree.on.declaration.of.honor");
		invalidateDocumentFileRelatedViewStates();
		request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
		return mapping.findForward("accept-honour-declaration");
	    }

	    if (bean.isToAccessFenix() && bean.getPublicCandidacyHashCode() == null) {
		DegreeOfficePublicCandidacyHashCode candidacyHashCode = null;
		try {
		    candidacyHashCode = DegreeOfficePublicCandidacyHashCode
			    .getUnusedOrCreateNewHashCodeAndSendEmailForApplicationSubmissionToCandidate(getProcessType(),
				    getCurrentOpenParentProcess(), bean.getPersonBean().getEmail());
		    bean.setPublicCandidacyHashCode(candidacyHashCode);
		} catch (HashCodeForEmailAndProcessAlreadyBounded e) {
		    addActionMessage(request, "error.candidacy.hash.code.already.bounded");
		    return mapping.findForward("show-pre-creation-candidacy-form");
		}
	    }

	    ErasmusIndividualCandidacyProcess process = (ErasmusIndividualCandidacyProcess) createNewPublicProcess(bean);

	    request.setAttribute("process", process);
	    request.setAttribute("mappingPath", mapping.getPath());
	    request.setAttribute("individualCandidacyProcess", process);
	    request.setAttribute("endSubmissionDate", getFormattedApplicationSubmissionEndDate());

	    return mapping.findForward("inform-submited-candidacy");
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    e.printStackTrace();
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("accept-honour-declaration");
	}
    }

    public ActionForward editCandidacyProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	try {
	    ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
	    if (actionForwardError != null)
		return actionForwardError;

	    if (!isApplicationSubmissionPeriodValid()) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
	    }

	    executeActivity(bean.getIndividualCandidacyProcess(), "EditPublicCandidacyPersonalInformation",
		    getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    if (e.getMessage().equals("error.IndividualCandidacyEvent.invalid.payment.code")) {
		throw e;
	    }

	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy");
	}

	request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
	return backToViewCandidacyInternal(mapping, form, request, response);
    }

    public ActionForward submitWithNationalCitizenCard(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("redirect-to-peps");
    }

    public ActionForward returnFromPeps(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	if (request.getParameter("returnCode") == null || !STORK_RETURN_CODE_OK.equals(request.getParameter("returnCode"))) {
	    return mapping.findForward("stork-error-authentication-failed");
	}

	String memcachedCode = request.getParameter("key");
	MemcachedClient c = new MemcachedClient(new InetSocketAddress(SPUtil.getInstance().getMemcachedHostname(), SPUtil
		.getInstance().getMemcachedPort()));

	String attrList = (String) c.get(memcachedCode);
	Map<String, Attribute> attributes = buildStorkAttributes(attrList);

	String name = getStorkName(attributes);
	YearMonthDay birthDate = getBirthDate(attributes);
	String email = getEmail(attributes);
	Country nationality = getNationality(attributes);
	String eIdentifier = getEIdentifier(attributes);
	Gender gender = getGender(attributes);
	String address = getTextAddress(attributes);

	DegreeOfficePublicCandidacyHashCode candidacyHashCode = null;
	try {
	    if (!StringUtils.isEmpty(email)) {
		candidacyHashCode = DegreeOfficePublicCandidacyHashCode
			.getUnusedOrCreateNewHashCodeAndSendEmailForApplicationSubmissionToCandidate(getProcessType(),
				getCurrentOpenParentProcess(), email);
	    }
	} catch (HashCodeForEmailAndProcessAlreadyBounded e) {
	    addActionMessage(request, "error.candidacy.hash.code.already.bounded");
	    return mapping.findForward("show-pre-creation-candidacy-form");
	}

	ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
	if (actionForwardError != null)
	    return actionForwardError;

	ErasmusCandidacyProcess candidacyProcess = (ErasmusCandidacyProcess) getCurrentOpenParentProcess();

	if (candidacyHashCode != null && candidacyHashCode.getIndividualCandidacyProcess() != null
		&& candidacyHashCode.getIndividualCandidacyProcess().getCandidacyProcess() == candidacyProcess) {
	    request.setAttribute("individualCandidacyProcess", candidacyHashCode.getIndividualCandidacyProcess());
	    return viewCandidacy(mapping, form, request, response);
	}

	if (!StringUtils.isEmpty(eIdentifier) && candidacyProcess.getProcessByEIdentifier(eIdentifier) != null) {
	    return mapping.findForward("stork-candidacy-already-bounded");
	}

	ErasmusIndividualCandidacyProcessBean bean = new ErasmusIndividualCandidacyProcessBean(candidacyProcess);
	bean.setPersonBean(new PersonBean());
	bean.getPersonBean().setIdDocumentType(IDDocumentType.FOREIGNER_IDENTITY_CARD);
	bean.setPublicCandidacyHashCode(candidacyHashCode);

	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	bean.getPersonBean().setEmail(email);
	bean.getPersonBean().setName(name);
	bean.getPersonBean().setDateOfBirth(birthDate);
	bean.getPersonBean().setNationality(nationality);
	bean.getPersonBean().setAddress(address);
	bean.getPersonBean().setGender(gender);
	bean.setEIdentifier(eIdentifier);

	bean.willAccessFenix();

	return mapping.findForward("show-application-submission-conditions-for-stork");
    }

    public ActionForward accessApplicationWithNationalCitizenCard(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("redirect-to-peps-to-access-application");
    }

    public ActionForward returnFromPepsToAccessApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	if (request.getParameter("returnCode") == null || !STORK_RETURN_CODE_OK.equals(request.getParameter("returnCode"))) {
	    return mapping.findForward("stork-error-authentication-failed");
	}

	String memcachedCode = request.getParameter("key");
	MemcachedClient c = new MemcachedClient(new InetSocketAddress(SPUtil.getInstance().getMemcachedHostname(), SPUtil
		.getInstance().getMemcachedPort()));

	String attrList = (String) c.get(memcachedCode);
	Map<String, Attribute> attributes = buildStorkAttributes(attrList);

	String eIdentifier = getEIdentifier(attributes);
	ErasmusIndividualCandidacyProcess process = ((ErasmusCandidacyProcess) getCurrentOpenParentProcess())
		.getProcessByEIdentifier(eIdentifier);

	request.setAttribute("individualCandidacyProcess", process);
	return viewCandidacy(mapping, form, request, response);
    }

    private String getTextAddress(Map<String, Attribute> attributes) {
	Attribute attribute = attributes.get(STORK_TEXT_RESIDENCE_ADDRESS);

	if (attribute == null) {
	    return null;
	}

	return attribute.getSemanticValue();
    }

    private Gender getGender(Map<String, Attribute> attributes) {
	Attribute attribute = attributes.get(STORK_GENDER);

	if (attribute == null) {
	    return null;
	}

	if (attribute.getSemanticValue() == null) {
	    return null;
	}

	if ("M".equals(attribute.getSemanticValue())) {
	    return Gender.MALE;
	} else if ("F".equals(attribute.getSemanticValue())) {
	    return Gender.FEMALE;
	}

	return null;
    }

    private String getEIdentifier(Map<String, Attribute> attributes) {
	Attribute attribute = attributes.get(STORK_EIDENTIFIER);

	if (attribute == null) {
	    return null;
	}

	return attribute.getSemanticValue();
    }

    private String getEmail(Map<String, Attribute> attributes) {
	Attribute attribute = attributes.get(STORK_EMAIL);

	if (attribute == null) {
	    return null;
	}

	return attribute.getSemanticValue();
    }

    private YearMonthDay getBirthDate(Map<String, Attribute> attributes) {
	Attribute attr = attributes.get(STORK_BIRTHDATE);
	String birthDateText = attr != null ? attr.getSemanticValue() : null;

	if (birthDateText == null || !birthDateText.matches("\\d{2}-\\d{2}-\\d{4}")) {
	    return null;
	}

	String[] compounds = birthDateText.split("-");
	return new YearMonthDay(Integer.valueOf(compounds[2]), Integer.valueOf(compounds[1]), Integer.valueOf(compounds[0]));
    }

    private String getStorkName(Map<String, Attribute> attributes) {
	Attribute attr = attributes.get(STORK_NAME);
	return attr != null ? attr.getSemanticValue() : null;
    }

    private Country getNationality(Map<String, Attribute> attributes) {
	Attribute attr = attributes.get(STORK_NATIONALITY);
	String nationalityCode = attr != null ? attr.getSemanticValue() : null;

	if (nationalityCode == null || !nationalityCode.matches("\\w{2,3}")) {
	    return null;
	}

	return nationalityCode.length() == 2 ? Country.readByTwoLetterCode(nationalityCode) : Country
		.readByThreeLetterCode(nationalityCode);
    }

    public ActionForward prepareCandidacyCreationForStork(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	return mapping.findForward("show-candidacy-creation-page");

    }

    public ActionForward prepareEditCandidacyInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward editCandidacyInformationInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy-information");
    }

    public ActionForward editCandidacyInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	try {
	    ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
	    if (actionForwardError != null)
		return actionForwardError;

	    if (!isApplicationSubmissionPeriodValid()) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
	    }

	    executeActivity(bean.getIndividualCandidacyProcess(), "EditPublicCandidacyInformation",
		    getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    if (e.getMessage().equals("error.IndividualCandidacyEvent.invalid.payment.code")) {
		throw e;
	    }

	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy-information");
	}

	request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
	return backToViewCandidacyInternal(mapping, form, request, response);
    }

    public ActionForward prepareEditDegreeAndCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	request.setAttribute("degreeCourseInformationBean", new DegreeCourseInformationBean(
		(ExecutionYear) getCurrentOpenParentProcess().getCandidacyExecutionInterval()));

	return mapping.findForward("edit-candidacy-degree-and-courses");
    }

    public ActionForward prepareEditDegreeAndCoursesInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	return mapping.findForward("edit-candidacy-degree-and-courses");
    }

    public ActionForward editDegreeAndCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	try {
	    ActionForward actionForwardError = verifySubmissionPreconditions(mapping);
	    if (actionForwardError != null)
		return actionForwardError;

	    if (!isApplicationSubmissionPeriodValid()) {
		return beginCandidacyProcessIntro(mapping, form, request, response);
	    }

	    executeActivity(bean.getIndividualCandidacyProcess(), "EditPublicDegreeAndCoursesInformation",
		    getIndividualCandidacyProcessBean());
	} catch (final DomainException e) {
	    if (e.getMessage().equals("error.IndividualCandidacyEvent.invalid.payment.code")) {
		throw e;
	    }

	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());
	    return mapping.findForward("edit-candidacy-degree-and-courses");
	}

	request.setAttribute("individualCandidacyProcess", bean.getIndividualCandidacyProcess());
	return backToViewCandidacyInternal(mapping, form, request, response);
    }

    private Map<String, Attribute> buildStorkAttributes(String attrList) {
	StringTokenizer st = new StringTokenizer(attrList, ";");
	Map<String, Attribute> attributes = new HashMap<String, Attribute>();

	int i = 1;
	while (st.hasMoreTokens()) {
	    String[] params = st.nextToken().split("=");
	    String attrName = params[0];
	    String value = params[1].equals("null") ? null : params[1].substring(1, params[1].length() - 1);

	    attributes.put(attrName, new Attribute(i++, attrName, false, value));
	}

	return attributes;
    }

    private static final String STORK_NAME = "givenName";
    private static final String STORK_BIRTHDATE = "dateOfBirth";
    private static final String STORK_NATIONALITY = "nationalityCode";
    private static final String STORK_EIDENTIFIER = "eIdentifier";
    private static final String STORK_SURNAME = "surname";
    private static final String STORK_GENDER = "gender";
    private static final String STORK_TEXT_RESIDENCE_ADDRESS = "textResidenceAddress";
    private static final String STORK_CANONICAL_RESIDENCE_ADDRESS = "canonicalResidenceAddress";
    private static final String STORK_EMAIL = "eMail";

    private static final String STORK_RETURN_CODE_ERROR = "ERROR";
    private static final String STORK_RETURN_CODE_OK = "OK";

    private static final String f(String format, Object... args) {
	return String.format(format, args);
    }

    public ActionForward chooseCountry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

	RenderUtils.invalidateViewState();

	if ("editCandidacy".equals(request.getParameter("userAction"))) {
	    bean.getErasmusStudentDataBean().setSelectedUniversity(null);

	    return mapping.findForward("edit-candidacy-degree-and-courses");
	}

	return mapping.findForward("candidacy-continue-creation");
    }

    public ActionForward chooseUniversity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
	request.setAttribute("degreeCourseInformationBean", readDegreeCourseInformationBean(request));

	RenderUtils.invalidateViewState();

	return mapping.findForward("edit-candidacy-degree-and-courses");
    }

    public ActionForward retrieveLearningAgreement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ErasmusIndividualCandidacyProcess process = (ErasmusIndividualCandidacyProcess) getProcess(request);

	final LearningAgreementDocument document = new LearningAgreementDocument((ErasmusIndividualCandidacyProcess) process);
	byte[] data = ReportsUtils.exportMultipleToPdfAsByteArray(document);

	response.setContentLength(data.length);
	response.setContentType("application/pdf");
	response.addHeader("Content-Disposition", "attachment; filename=" + document.getReportFileName() + ".pdf");

	final ServletOutputStream writer = response.getOutputStream();
	writer.write(data);
	writer.flush();
	writer.close();

	response.flushBuffer();
	return mapping.findForward("");
    }

    public ActionForward continueCandidacyCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	ErasmusIndividualCandidacyProcessBean bean = (ErasmusIndividualCandidacyProcessBean) getIndividualCandidacyProcessBean();

	final PersonBean personBean = bean.getPersonBean();

	if (existsIndividualCandidacyProcessForDocumentId(request, personBean.getIdDocumentType(), personBean
		.getDocumentIdNumber())) {
	    addActionMessage("individualCandidacyMessages", request, "erasmus.error.candidacy.for.person.already.exists");
	    return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
	}

	final List<Person> persons = new ArrayList<Person>(Person.readByDocumentIdNumber(personBean.getDocumentIdNumber()));

	if (persons.size() > 0) {
	    addActionMessage("individualCandidacyMessages", request, "erasmus.error.person.with.same.identifier.exists");
	    return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
	}

	IndividualCandidacyDocumentFile photoDocumentFile = createIndividualCandidacyDocumentFile(bean.getPhotoDocument(), bean
		.getPersonBean().getDocumentIdNumber());
	bean.getPhotoDocument().setDocumentFile(photoDocumentFile);
	request.setAttribute(getIndividualCandidacyProcessBeanName(), getIndividualCandidacyProcessBean());

	if (bean.isToAccessFenix() && !(personBean.getEmail().equals(personBean.getEmailConfirmation()))) {
	    addActionMessage("individualCandidacyMessages", request, "erasmus.error.emails.are.not.equals");
	    return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
	}

	if (bean.isToAccessFenix() && bean.getPublicCandidacyHashCode() == null) {
	    DegreeOfficePublicCandidacyHashCode candidacyHashCode = null;
	    candidacyHashCode = DegreeOfficePublicCandidacyHashCode.getPublicCandidacyHashCodeByEmailAndCandidacyProcessType(bean
		    .getPersonBean().getEmail(), getProcessType(), getCurrentOpenParentProcess());

	    if (candidacyHashCode != null && candidacyHashCode.getIndividualCandidacyProcess() != null) {
		addActionMessage("individualCandidacyMessages", request, "erasmus.error.email.is.bounded.to.candidacy");
		return executeCreateCandidacyPersonalInformationInvalid(mapping, form, request, response);
	    }

	}

	return mapping.findForward("candidacy-continue-creation");
    }

}
