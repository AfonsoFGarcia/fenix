package net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.academicAdminOffice;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisJuryElementBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.AddJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.AddPresidentJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.AddState;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.DeleteJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.EditJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.EditPhdThesisProcessInformation;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.MoveJuryElementOrder;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RatifyFinalThesis;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RejectJuryElements;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RejectJuryElementsDocuments;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RemindJuryReviewToReporters;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RemoveLastState;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.ReplaceDocument;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RequestJuryElements;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RequestJuryReviews;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.ScheduleThesisDiscussion;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SetFinalGrade;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SubmitJuryElementsDocuments;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SubmitThesis;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SwapJuryElementsOrder;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.ValidateJury;
import net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.CommonPhdThesisProcessDA;
import net.sourceforge.fenixedu.presentationTier.docs.phd.thesis.PhdThesisJuryElementsDocument;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.Pair;

@Mapping(path = "/phdThesisProcess", module = "academicAdminOffice")
@Forwards( {

@Forward(name = "requestJuryElements", path = "/phd/thesis/academicAdminOffice/requestJuryElements.jsp"),

@Forward(name = "submitJuryElementsDocument", path = "/phd/thesis/academicAdminOffice/submitJuryElementsDocument.jsp"),

@Forward(name = "manageThesisJuryElements", path = "/phd/thesis/academicAdminOffice/manageThesisJuryElements.jsp"),

@Forward(name = "rejectJuryElements", path = "/phd/thesis/academicAdminOffice/rejectJuryElements.jsp"),

@Forward(name = "addJuryElement", path = "/phd/thesis/academicAdminOffice/addJuryElement.jsp"),

@Forward(name = "editJuryElement", path = "/phd/thesis/academicAdminOffice/editJuryElement.jsp"),

@Forward(name = "validateJury", path = "/phd/thesis/academicAdminOffice/validateJury.jsp"),

@Forward(name = "submitThesis", path = "/phd/thesis/academicAdminOffice/submitThesis.jsp"),

@Forward(name = "requestJuryReviews", path = "/phd/thesis/academicAdminOffice/requestJuryReviews.jsp"),

@Forward(name = "remindJuryReviews", path = "/phd/thesis/academicAdminOffice/remindJuryReviews.jsp"),

@Forward(name = "addPresidentJuryElement", path = "/phd/thesis/academicAdminOffice/addPresidentJuryElement.jsp"),

@Forward(name = "manageThesisDocuments", path = "/phd/thesis/academicAdminOffice/manageThesisDocuments.jsp"),

@Forward(name = "scheduleThesisDiscussion", path = "/phd/thesis/academicAdminOffice/scheduleThesisDiscussion.jsp"),

@Forward(name = "ratifyFinalThesis", path = "/phd/thesis/academicAdminOffice/ratifyFinalThesis.jsp"),

@Forward(name = "setFinalGrade", path = "/phd/thesis/academicAdminOffice/setFinalGrade.jsp"),

@Forward(name = "rejectJuryElementsDocuments", path = "/phd/thesis/academicAdminOffice/rejectJuryElementsDocuments.jsp"),

@Forward(name = "viewMeetingSchedulingProcess", path = "/phd/thesis/academicAdminOffice/viewMeetingSchedulingProcess.jsp"),

@Forward(name = "juryReporterFeedbackUpload", path = "/phd/thesis/academicAdminOffice/juryReporterFeedbackUpload.jsp"),

@Forward(name = "replaceDocument", path = "/phd/thesis/academicAdminOffice/replaceDocument.jsp"),

@Forward(name = "manageStates", path = "/phd/thesis/academicAdminOffice/manageStates.jsp"),

@Forward(name = "editPhdThesisProcessInformation", path = "/phd/thesis/academicAdminOffice/editPhdThesisProcessInformation.jsp")

})
public class PhdThesisProcessDA extends CommonPhdThesisProcessDA {

    // Begin thesis jury elements management

    public ActionForward prepareRequestJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdThesisProcessBean bean = new PhdThesisProcessBean();
	request.setAttribute("thesisProcessBean", bean);
	return mapping.findForward("requestJuryElements");
    }

    public ActionForward requestJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	try {

	    ExecuteProcessActivity.run(getProcess(request), RequestJuryElements.class, getRenderedObject("thesisProcessBean"));
	    addSuccessMessage(request, "message.thesis.jury.elements.requested.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("thesisProcessBean", getRenderedObject("thesisProcessBean"));
	    return mapping.findForward("requestJuryElements");
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward prepareSubmitJuryElementsDocument(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final PhdThesisProcessBean bean = new PhdThesisProcessBean(getProcess(request).getIndividualProgramProcess());
	bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.JURY_ELEMENTS));
	bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.JURY_PRESIDENT_ELEMENT));
	request.setAttribute("thesisProcessBean", bean);

	return mapping.findForward("submitJuryElementsDocument");
    }

    public ActionForward submitJuryElementsDocumentInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("thesisProcessBean", getRenderedObject("thesisProcessBean"));
	return mapping.findForward("submitJuryElementsDocument");
    }

    public ActionForward submitJuryElementsDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	try {

	    final IViewState viewState = RenderUtils.getViewState("thesisProcessBean.edit.documents");
	    if (!viewState.isValid()) {
		RenderUtils.invalidateViewState("thesisProcessBean.edit.documents");
		return submitJuryElementsDocumentInvalid(mapping, actionForm, request, response);
	    }
	    ExecuteProcessActivity.run(getProcess(request), SubmitJuryElementsDocuments.class,
		    getRenderedObject("thesisProcessBean"));
	    addSuccessMessage(request, "message.thesis.jury.elements.added.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    RenderUtils.invalidateViewState("thesisProcessBean.edit.documents");
	    return submitJuryElementsDocumentInvalid(mapping, actionForm, request, response);
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward manageThesisJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("manageThesisJuryElements");
    }

    public ActionForward prepareAddJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisJuryElementBean", new PhdThesisJuryElementBean(getProcess(request)));
	return mapping.findForward("addJuryElement");
    }

    public ActionForward prepareAddJuryElementInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisJuryElementBean", getRenderedObject("thesisJuryElementBean"));
	return mapping.findForward("addJuryElement");
    }

    public ActionForward prepareAddJuryElementPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisJuryElementBean", getRenderedObject("thesisJuryElementBean"));
	RenderUtils.invalidateViewState();
	return mapping.findForward("addJuryElement");
    }

    public ActionForward addJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), AddJuryElement.class, getRenderedObject("thesisJuryElementBean"));
	    addSuccessMessage(request, "message.thesis.added.jury.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return prepareAddJuryElementInvalid(mapping, actionForm, request, response);
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    static public class ExistingPhdParticipantsEvenInPhdThesisProcess implements DataProvider {

	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
	    final PhdThesisJuryElementBean bean = (PhdThesisJuryElementBean) source;
	    return bean.getExistingParticipants();
	}
    }

    public ActionForward prepareEditJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("thesisJuryElementBean", new PhdThesisJuryElementBean(getProcess(request), getJuryElement(request)));
	return mapping.findForward("editJuryElement");
    }

    public ActionForward editJuryElementInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("thesisJuryElementBean", getRenderedObject("thesisJuryElementBean"));
	return mapping.findForward("editJuryElement");
    }

    public ActionForward editJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), EditJuryElement.class, getRenderedObject("thesisJuryElementBean"));

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return editJuryElementInvalid(mapping, actionForm, request, response);
	}

	return redirect(String.format("/phdThesisProcess.do?method=manageThesisJuryElements&processId=%s", getProcess(request)
		.getExternalId()), request);
    }

    public ActionForward deleteJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), DeleteJuryElement.class, getJuryElement(request));
	    addSuccessMessage(request, "message.thesis.jury.removed");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	}

	return redirect(String.format("/phdThesisProcess.do?method=manageThesisJuryElements&processId=%s", getProcess(request)
		.getExternalId()), request);
    }

    private ThesisJuryElement getJuryElement(HttpServletRequest request) {
	return getDomainObject(request, "juryElementId");
    }

    private void swapJuryElements(HttpServletRequest request, ThesisJuryElement e1, ThesisJuryElement e2) {
	try {
	    ExecuteProcessActivity.run(getProcess(request), SwapJuryElementsOrder.class,
		    new Pair<ThesisJuryElement, ThesisJuryElement>(e1, e2));
	    addSuccessMessage(request, "message.thesis.jury.element.swapped");
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	}
    }

    private void moveElement(HttpServletRequest request, ThesisJuryElement element, Integer position) {
	try {
	    ExecuteProcessActivity.run(getProcess(request), MoveJuryElementOrder.class, new Pair<ThesisJuryElement, Integer>(
		    element, position));
	    addSuccessMessage(request, "message.thesis.jury.element.swapped");
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	}
    }

    public ActionForward moveUp(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdThesisProcess process = getProcess(request);
	final ThesisJuryElement juryElement = getJuryElement(request);
	final ThesisJuryElement lower = process.getOrderedThesisJuryElements().lower(juryElement);

	if (lower != null) {
	    swapJuryElements(request, juryElement, lower);
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward moveDown(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdThesisProcess process = getProcess(request);
	final ThesisJuryElement juryElement = getJuryElement(request);
	final ThesisJuryElement higher = process.getOrderedThesisJuryElements().higher(juryElement);

	if (higher != null) {
	    swapJuryElements(request, juryElement, higher);
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward moveTop(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdThesisProcess process = getProcess(request);
	final ThesisJuryElement juryElement = getJuryElement(request);
	final ThesisJuryElement first = process.getOrderedThesisJuryElements().first();

	if (juryElement != first) {
	    moveElement(request, juryElement, first.getElementOrder() - 1);
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward moveBottom(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdThesisProcess process = getProcess(request);
	final ThesisJuryElement juryElement = getJuryElement(request);
	final ThesisJuryElement last = process.getOrderedThesisJuryElements().last();

	if (juryElement != last) {
	    moveElement(request, juryElement, last.getElementOrder() - 1);
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward prepareAddPresidentJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisJuryElementBean", new PhdThesisJuryElementBean(getProcess(request)));
	return mapping.findForward("addPresidentJuryElement");
    }

    public ActionForward prepareAddPresidentJuryElementInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("thesisJuryElementBean", getRenderedObject("thesisJuryElementBean"));
	return mapping.findForward("addPresidentJuryElement");
    }

    public ActionForward prepareAddPresidentJuryElementPostback(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("thesisJuryElementBean", getRenderedObject("thesisJuryElementBean"));
	RenderUtils.invalidateViewState();
	return mapping.findForward("addPresidentJuryElement");
    }

    public ActionForward addPresidentJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), AddPresidentJuryElement.class,
		    getRenderedObject("thesisJuryElementBean"));
	    addSuccessMessage(request, "message.thesis.added.jury.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return prepareAddPresidentJuryElementInvalid(mapping, actionForm, request, response);
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward prepareValidateJury(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdThesisProcessBean bean = new PhdThesisProcessBean();
	bean.setWhenJuryValidated(new LocalDate());

	request.setAttribute("thesisBean", bean);
	return mapping.findForward("validateJury");
    }

    public ActionForward prepareValidateJuryInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisBean", getRenderedObject("thesisBean"));
	return mapping.findForward("validateJury");
    }

    public ActionForward validateJury(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), ValidateJury.class, getRenderedObject("thesisBean"));

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return prepareValidateJuryInvalid(mapping, actionForm, request, response);
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward printJuryElementsDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JRException {

	final PhdThesisJuryElementsDocument report = new PhdThesisJuryElementsDocument(getProcess(request));

	writeFile(response, report.getReportFileName() + ".pdf", "application/pdf", ReportsUtils
		.exportToProcessedPdfAsByteArray(report));

	return null;
    }

    public ActionForward prepareRejectJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisBean", new PhdThesisProcessBean());
	return mapping.findForward("rejectJuryElements");
    }

    public ActionForward rejectJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), RejectJuryElements.class, getRenderedObject("thesisBean"));

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("thesisBean", getRenderedObject("thesisBean"));
	    return mapping.findForward("rejectJuryElements");
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    // end thesis jury elements management

    // Request Jury Reviews
    public ActionForward prepareRequestJuryReviews(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("requestJuryReviewsBean", new PhdThesisProcessBean());

	return mapping.findForward("requestJuryReviews");
    }

    public ActionForward prepareRequestJuryReviewsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("requestJuryReviewsBean", getRenderedObject("requestJuryReviewsBean"));

	return mapping.findForward("requestJuryReviews");
    }

    public ActionForward requestJuryReviews(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	try {
	    ExecuteProcessActivity
		    .run(getProcess(request), RequestJuryReviews.class, getRenderedObject("requestJuryReviewsBean"));
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return prepareRequestJuryReviewsInvalid(mapping, form, request, response);
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }

    // End of Request Jury Reviews

    // Remind Jury Reviews
    public ActionForward prepareRemindJuryReviews(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("remindJuryReviewsBean", new PhdThesisProcessBean());

	return mapping.findForward("remindJuryReviews");
    }

    public ActionForward prepareRemindJuryReviewsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("remindJuryReviewsBean", getRenderedObject("remindJuryReviewsBean"));

	return mapping.findForward("remindJuryReviews");
    }

    public ActionForward remindJuryReviews(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	try {
	    ExecuteProcessActivity.run(getProcess(request), RemindJuryReviewToReporters.class,
		    getRenderedObject("remindJuryReviewsBean"));
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return prepareRemindJuryReviewsInvalid(mapping, form, request, response);
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }

    // end remind jury reviews

    // Schedule thesis discussion

    public ActionForward prepareScheduleThesisDiscussion(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final PhdThesisProcessBean bean = new PhdThesisProcessBean();
	final PhdThesisProcess thesisProcess = getProcess(request);

	bean.setThesisProcess(thesisProcess);
	setDefaultDiscussionMailInformation(bean, thesisProcess);

	request.setAttribute("thesisProcessBean", bean);
	return mapping.findForward("scheduleThesisDiscussion");
    }

    private void setDefaultDiscussionMailInformation(final PhdThesisProcessBean bean, final PhdThesisProcess thesisProcess) {
	final PhdIndividualProgramProcess process = thesisProcess.getIndividualProgramProcess();
	bean.setMailSubject(AlertService.getSubjectPrefixed(process,
		"message.phd.thesis.schedule.thesis.discussion.default.subject"));
	bean.setMailBody(AlertService.getBodyText(process, "message.phd.thesis.schedule.thesis.discussion.default.body"));
    }

    public ActionForward scheduleThesisDiscussionInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("thesisProcessBean", getThesisProcessBean());
	return mapping.findForward("scheduleThesisDiscussion");
    }

    public ActionForward scheduleThesisDiscussionPostback(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("thesisProcessBean", getThesisProcessBean());
	RenderUtils.invalidateViewState();
	return mapping.findForward("scheduleThesisDiscussion");
    }

    public ActionForward scheduleThesisDiscussion(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdThesisProcess thesisProcess = getProcess(request);

	try {
	    ExecuteProcessActivity.run(thesisProcess, ScheduleThesisDiscussion.class, getThesisProcessBean());

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    return scheduleThesisDiscussionPostback(mapping, actionForm, request, response);
	}

	return viewIndividualProgramProcess(request, thesisProcess);
    }

    // End of schedule thesis discussion

    // Submit thesis
    public ActionForward prepareSubmitThesis(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdThesisProcessBean bean = new PhdThesisProcessBean();
	bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.FINAL_THESIS));

	request.setAttribute("submitThesisBean", bean);

	return mapping.findForward("submitThesis");
    }

    public ActionForward prepareSubmitThesisInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("submitThesisBean", getRenderedObject("submitThesisBean"));
	RenderUtils.invalidateViewState("submitThesisBean.edit.documents");

	return mapping.findForward("submitThesis");
    }

    public ActionForward submitThesis(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	try {

	    if (!RenderUtils.getViewState("submitThesisBean.edit.documents").isValid()) {
		addErrorMessage(request, "error.phd.invalid.documents");
		return prepareSubmitThesisInvalid(mapping, form, request, response);
	    }

	    ExecuteProcessActivity.run(getProcess(request), SubmitThesis.class, getRenderedObject("submitThesisBean"));

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return prepareSubmitThesisInvalid(mapping, form, request, response);
	}

	return viewIndividualProgramProcess(request, getProcess(request));

    }

    // End of submit thesis

    // Ratify final thesis
    public ActionForward prepareRatifyFinalThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdThesisProcessBean bean = new PhdThesisProcessBean();
	bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.FINAL_THESIS_RATIFICATION_DOCUMENT));

	request.setAttribute("thesisProcessBean", bean);
	return mapping.findForward("ratifyFinalThesis");
    }

    public ActionForward ratifyFinalThesisInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisProcessBean", getThesisProcessBean());
	return mapping.findForward("ratifyFinalThesis");
    }

    public ActionForward ratifyFinalThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {

	    if (!RenderUtils.getViewState("thesisProcessBean.edit").isValid()) {
		return ratifyFinalThesisInvalid(mapping, actionForm, request, response);
	    }

	    if (!RenderUtils.getViewState("thesisProcessBean.edit.documents").isValid()) {
		addErrorMessage(request, "error.phd.invalid.documents");
		return ratifyFinalThesisInvalid(mapping, actionForm, request, response);
	    }

	    ExecuteProcessActivity.run(getProcess(request), RatifyFinalThesis.class, getThesisProcessBean());

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return ratifyFinalThesisInvalid(mapping, actionForm, request, response);
	}

	return viewIndividualProgramProcess(request, getProcess(request));

    }

    // End of ratify final thesis

    // Set final grade

    public ActionForward prepareSetFinalGrade(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdThesisProcessBean bean = new PhdThesisProcessBean();
	bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.CONCLUSION_DOCUMENT));

	request.setAttribute("thesisProcessBean", bean);
	return mapping.findForward("setFinalGrade");
    }

    public ActionForward setFinalGradeInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisProcessBean", getThesisProcessBean());
	return mapping.findForward("setFinalGrade");
    }

    public ActionForward setFinalGrade(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {

	    if (!RenderUtils.getViewState("thesisProcessBean.edit").isValid()) {
		return setFinalGradeInvalid(mapping, actionForm, request, response);
	    }

	    if (!RenderUtils.getViewState("thesisProcessBean.edit.documents").isValid()) {
		addErrorMessage(request, "error.phd.invalid.documents");
		return setFinalGradeInvalid(mapping, actionForm, request, response);
	    }

	    ExecuteProcessActivity.run(getProcess(request), SetFinalGrade.class, getThesisProcessBean());

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return setFinalGradeInvalid(mapping, actionForm, request, response);
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }

    // End of set final grade

    // Reject jury elements documents

    public ActionForward prepareRejectJuryElementsDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdThesisProcessBean bean = new PhdThesisProcessBean();
	request.setAttribute("thesisProcessBean", bean);

	return mapping.findForward("rejectJuryElementsDocuments");
    }

    public ActionForward rejectJuryElementsDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	try {

	    ExecuteProcessActivity.run(getProcess(request), RejectJuryElementsDocuments.class,
		    getRenderedObject("thesisProcessBean"));
	    addSuccessMessage(request, "message.thesis.jury.elements.documents.rejected.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("thesisProcessBean", getRenderedObject("thesisProcessBean"));
	    return mapping.findForward("rejectJuryElementsDocuments");
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }

    // End of reject jury elements document

    // Manage phd thesis process meetings
    public ActionForward viewMeetingSchedulingProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdThesisProcessBean bean = new PhdThesisProcessBean();
	request.setAttribute("thesisProcessBean", bean);

	return mapping.findForward("viewMeetingSchedulingProcess");
    }

    // End of manage phd thesis process meetings

    public ActionForward prepareReplaceDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgramDocumentUploadBean bean = new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType
		.valueOf(request.getParameter("type")));

	request.setAttribute("documentBean", bean);

	return mapping.findForward("replaceDocument");
    }

    public ActionForward replaceDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	try {

	    final IViewState viewState = RenderUtils.getViewState("documentBean");
	    if (!viewState.isValid()) {
		return juryReportFeedbackUploadInvalid(mapping, form, request, response);
	    }

	    ExecuteProcessActivity.run(getProcess(request), ReplaceDocument.class, getRenderedObject("documentBean"));
	    addSuccessMessage(request, "message.replace.document.done.with.success");
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return replaceDocumentInvalid(mapping, form, request, response);
	}

	return manageThesisDocuments(mapping, form, request, response);
    }

    public ActionForward replaceDocumentInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisProcessBean", getThesisProcessBean());
	return mapping.findForward("replaceDocument");
    }

    // jury report feedback operations

    @Override
    public ActionForward prepareJuryReportFeedbackUpload(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final PhdThesisProcessBean bean = new PhdThesisProcessBean(getProcess(request).getIndividualProgramProcess());
	bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.JURY_REPORT_FEEDBACK));

	request.setAttribute("thesisProcessBean", bean);
	request.setAttribute("thesisDocuments", getProcess(request).getThesisDocumentsToFeedback());

	return mapping.findForward("juryReporterFeedbackUpload");
    }

    public ActionForward juryReportFeedbackUploadPostback(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("thesisProcessBean", getThesisProcessBean());
	request.setAttribute("thesisDocuments", getProcess(request).getThesisDocumentsToFeedback());
	return mapping.findForward("juryReporterFeedbackUpload");
    }

    public ActionForward manageStates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdThesisProcessBean bean = new PhdThesisProcessBean(getProcess(request).getIndividualProgramProcess());
	request.setAttribute("thesisProcessBean", bean);

	return mapping.findForward("manageStates");
    }

    public ActionForward addState(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	PhdThesisProcessBean bean = getRenderedObject("thesisProcessBean");

	ExecuteProcessActivity.run(getProcess(request), AddState.class, bean);

	return viewIndividualProgramProcess(mapping, form, request, response);
    }

    public ActionForward addStateInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdThesisProcessBean bean = getRenderedObject("thesisProcessBean");

	request.setAttribute("thesisProcessBean", bean);

	return mapping.findForward("manageStates");
    }

    public ActionForward removeLastState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	try {
	    ExecuteProcessActivity.run(getProcess(request), RemoveLastState.class, null);
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	}
	return manageStates(mapping, form, request, response);
    }

    // Phd Thesis process information edit
    public ActionForward prepareEditPhdThesisProcessInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("phdThesisProcessBean", new PhdThesisProcessBean(getProcess(request).getIndividualProgramProcess()));
	return mapping.findForward("editPhdThesisProcessInformation");
    }

    public ActionForward editPhdThesisProcessInformationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("phdThesisProcessBean", getRenderedObject("phdThesisProcessBean"));
	return mapping.findForward("editPhdThesisProcessInformation");
    }

    public ActionForward editPhdThesisProcessInformationPostback(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("phdThesisProcessBean", getRenderedObject("phdThesisProcessBean"));
	RenderUtils.invalidateViewState();
	return mapping.findForward("editPhdThesisProcessInformation");
    }

    public ActionForward editPhdThesisProcessInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final PhdThesisProcessBean bean = getRenderedObject("phdThesisProcessBean");
	request.setAttribute("phdThesisProcessBean", bean);

	try {
	    ExecuteProcessActivity.run(getProcess(request), EditPhdThesisProcessInformation.class.getSimpleName(), bean);
	    addSuccessMessage(request, "message.phdThesisProcessInformation.edit.success");
	    return viewIndividualProgramProcess(request, getProcess(request));

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    return mapping.findForward("editPhdThesisProcessInformation");
	}
    }

    // End of Phd Thesis process information edit

}
