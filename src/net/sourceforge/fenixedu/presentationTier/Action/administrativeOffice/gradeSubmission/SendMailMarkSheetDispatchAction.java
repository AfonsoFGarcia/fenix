package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.GradesToSubmitExecutionCourseSendMailBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetSendMailBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetToConfirmSendMailBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.TeachersWithGradesToSubmit;
import net.sourceforge.fenixedu.domain.accessControl.TeachersWithMarkSheetsToConfirm;
import net.sourceforge.fenixedu.domain.organizationalStructure.AdministrativeOfficeUnit;
import net.sourceforge.fenixedu.domain.util.email.EmailBean;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.UnitBasedSender;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class SendMailMarkSheetDispatchAction extends MarkSheetDispatchAction {

    public ActionForward prepareSearchSendMail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	MarkSheetSendMailBean bean = new MarkSheetSendMailBean();
	request.setAttribute("bean", bean);
	return mapping.findForward("searchSendMail");
    }

    public ActionForward prepareSearchSendMailPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	Object object = RenderUtils.getViewState().getMetaObject().getObject();
	RenderUtils.invalidateViewState();
	request.setAttribute("bean", object);

	return mapping.getInputForward();
    }

    public ActionForward prepareSearchSendMailInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("bean", RenderUtils.getViewState().getMetaObject().getObject());
	return mapping.getInputForward();
    }

    public ActionForward searchSendMail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	MarkSheetSendMailBean bean = (MarkSheetSendMailBean) RenderUtils.getViewState().getMetaObject().getObject();
	Collection<MarkSheet> markSheets = bean.getExecutionPeriod().getMarkSheetsToConfirm(bean.getDegreeCurricularPlan());
	Collection<ExecutionCourse> executionCourses = bean.getExecutionPeriod().getExecutionCoursesWithDegreeGradesToSubmit(
		bean.getDegreeCurricularPlan());
	if (!markSheets.isEmpty()) {
	    Map<CurricularCourse, MarkSheetToConfirmSendMailBean> map = new HashMap<CurricularCourse, MarkSheetToConfirmSendMailBean>();
	    for (MarkSheet markSheet : markSheets) {
		if (map.get(markSheet.getCurricularCourse()) == null) {
		    map.put(markSheet.getCurricularCourse(), new MarkSheetToConfirmSendMailBean(markSheet, true));
		}
	    }
	    bean.setMarkSheetToConfirmSendMailBean(new ArrayList<MarkSheetToConfirmSendMailBean>(map.values()));
	}
	if (!executionCourses.isEmpty()) {
	    Collection<GradesToSubmitExecutionCourseSendMailBean> executionCoursesBean = new ArrayList<GradesToSubmitExecutionCourseSendMailBean>();
	    for (ExecutionCourse course : executionCourses) {
		executionCoursesBean.add(new GradesToSubmitExecutionCourseSendMailBean(course, true));
	    }
	    bean.setGradesToSubmitExecutionCourseSendMailBean(executionCoursesBean);
	}
	request.setAttribute("bean", bean);
	return mapping.findForward("searchSendMail");
    }

    public ActionForward prepareMarkSheetsToConfirmSendMail(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	MarkSheetSendMailBean bean = (MarkSheetSendMailBean) RenderUtils.getViewState("sendMailBean").getMetaObject().getObject();
	Group teachersGroup = new TeachersWithMarkSheetsToConfirm(bean.getExecutionPeriod(), bean.getDegree(), bean
		.getDegreeCurricularPlan());
	String message = getResources(request, "ACADEMIC_OFFICE_RESOURCES").getMessage("label.markSheets.to.confirm.send.mail");
	Recipient recipient = Recipient.createNewRecipient(message, teachersGroup);
	EmailBean emailBean = new EmailBean();
	// Nucleo de Graduacao
	UnitBasedSender graduationUnitSender = AdministrativeOfficeUnit.getGraduationUnit().getUnitBasedSenderIterator().next();
	// emailBean.setReplyTos(Collections.singletonList());
	emailBean.setRecipients(Collections.singletonList(recipient));
	emailBean.setSender(graduationUnitSender);
	request.setAttribute("emailBean", emailBean);
	return mapping.findForward("sendEmail");
    }

    public ActionForward prepareGradesToSubmitSendMail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	MarkSheetSendMailBean bean = (MarkSheetSendMailBean) RenderUtils.getViewState("sendMailBean").getMetaObject().getObject();
	Group teachersGroup = new TeachersWithGradesToSubmit(bean.getExecutionPeriod(), bean.getDegree(), bean
		.getDegreeCurricularPlan());
	String message = getResources(request, "ACADEMIC_OFFICE_RESOURCES").getMessage("label.grades.to.submit.send.mail");
	Recipient recipient = Recipient.createNewRecipient(message, teachersGroup);
	EmailBean emailBean = new EmailBean();
	UnitBasedSender graduationUnitSender = AdministrativeOfficeUnit.getGraduationUnit().getUnitBasedSenderIterator().next();
	emailBean.setRecipients(Collections.singletonList(recipient));
	emailBean.setSender(graduationUnitSender);
	request.setAttribute("emailBean", emailBean);
	return mapping.findForward("sendEmail");
    }
}