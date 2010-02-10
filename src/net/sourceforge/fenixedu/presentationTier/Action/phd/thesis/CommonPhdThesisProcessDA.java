package net.sourceforge.fenixedu.presentationTier.Action.phd.thesis;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.JuryReporterFeedbackUpload;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdDocumentsZip;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

abstract public class CommonPhdThesisProcessDA extends PhdProcessDA {

    @Override
    protected PhdThesisProcess getProcess(HttpServletRequest request) {
	return (PhdThesisProcess) super.getProcess(request);
    }

    public ActionForward viewIndividualProgramProcess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return viewIndividualProgramProcess(request, getProcess(request));
    }

    protected ActionForward viewIndividualProgramProcess(HttpServletRequest request, final PhdThesisProcess process) {
	return redirect(String.format("/phdIndividualProgramProcess.do?method=viewProcess&processId=%s", process
		.getIndividualProgramProcess().getExternalId()), request);
    }

    // jury report feedback operations

    public ActionForward prepareJuryReportFeedbackUpload(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final PhdThesisProcessBean bean = new PhdThesisProcessBean();
	bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.JURY_REPORT_FEEDBACK));
	bean.setJuryElement(getProcess(request).getThesisJuryElement(AccessControl.getPerson()));

	request.setAttribute("thesisProcessBean", bean);
	request.setAttribute("thesisDocuments", getProcess(request).getThesisDocumentsToFeedback());

	return mapping.findForward("juryReporterFeedbackUpload");
    }

    public ActionForward juryReportFeedbackUploadInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("thesisProcessBean", getThesisProcessBean());
	request.setAttribute("thesisDocuments", getProcess(request).getThesisDocumentsToFeedback());
	return mapping.findForward("juryReporterFeedbackUpload");
    }

    public ActionForward juryReportFeedbackUpload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	try {

	    final IViewState viewState = RenderUtils.getViewState("thesisProcessBean.edit.documents");
	    if (!viewState.isValid()) {
		return juryReportFeedbackUploadInvalid(mapping, actionForm, request, response);
	    }
	    ExecuteProcessActivity.run(getProcess(request), JuryReporterFeedbackUpload.class, getThesisProcessBean());
	    addSuccessMessage(request, "message.thesis.jury.report.feedback.uploaded.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return juryReportFeedbackUploadInvalid(mapping, actionForm, request, response);
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward downloadThesisDocumentsToFeedback(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {

	writeFile(response, getThesisDocumentsToFeedbackFilename(request), PhdDocumentsZip.ZIP_MIME_TYPE, PhdDocumentsZip
		.zip(getProcess(request).getThesisDocumentsToFeedback()));

	return null;
    }

    protected String getThesisDocumentsToFeedbackFilename(HttpServletRequest request) {
	return getProcess(request).getProcessNumber().replace("/", "-") + "-Documents.zip";
    }

    protected PhdThesisProcessBean getThesisProcessBean() {
	return (PhdThesisProcessBean) getRenderedObject("thesisProcessBean");
    }

    // end of jury report feedback operations

}
