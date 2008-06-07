/*
 * Created on May 5, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetRectifyBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class MarkSheetCreateDispatchAction extends MarkSheetDispatchAction {

    public ActionForward prepareCreateMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	MarkSheetManagementCreateBean markSheetManagementCreateBean = new MarkSheetManagementCreateBean();
	markSheetManagementCreateBean.setExecutionPeriod(ExecutionSemester.readActualExecutionSemester());
	markSheetManagementCreateBean.setUrl("");

	request.setAttribute("edit", markSheetManagementCreateBean);

	return mapping.findForward("createMarkSheetStep1");
    }

    public ActionForward prepareCreateMarkSheetFilled(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	MarkSheetManagementCreateBean markSheetManagementCreateBean = new MarkSheetManagementCreateBean();
	fillMarkSheetBean(actionForm, request, markSheetManagementCreateBean);
	markSheetManagementCreateBean.setUrl(buildUrl((DynaActionForm) actionForm));

	request.setAttribute("edit", markSheetManagementCreateBean);

	return mapping.findForward("createMarkSheetStep1");
    }

    public ActionForward createMarkSheetStepOne(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	MarkSheetManagementCreateBean createBean = (MarkSheetManagementCreateBean) RenderUtils.getViewState().getMetaObject()
		.getObject();
	request.setAttribute("edit", createBean);

	Teacher teacher = Teacher.readByNumber(createBean.getTeacherNumber());
	createBean.setTeacher(teacher);

	ActionMessages actionMessages = createActionMessages();
	checkIfTeacherIsResponsibleOrCoordinator(createBean.getCurricularCourse(), createBean.getExecutionPeriod(), createBean
		.getTeacherNumber(), teacher, request, createBean.getMarkSheetType(), actionMessages);
	if (!actionMessages.isEmpty()) {
	    createBean.setTeacherNumber(null);
	}
	checkIfEvaluationDateIsInExamsPeriod(createBean.getDegreeCurricularPlan(), createBean.getExecutionPeriod(), createBean
		.getEvaluationDate(), createBean.getMarkSheetType(), request, actionMessages);

	prepareCreateEnrolmentEvaluationsForMarkSheet(createBean, request, actionMessages);

	if (!actionMessages.isEmpty()) {
	    return mapping.findForward("createMarkSheetStep1");
	} else {
	    return mapping.findForward("createMarkSheetStep2");
	}
    }

    private void prepareCreateEnrolmentEvaluationsForMarkSheet(MarkSheetManagementCreateBean createBean,
	    HttpServletRequest request, ActionMessages actionMessages) {

	final Collection<Enrolment> enrolments = createBean.getCurricularCourse().getEnrolmentsNotInAnyMarkSheet(
		createBean.getMarkSheetType(), createBean.getExecutionPeriod());

	if (enrolments.isEmpty()) {
	    addMessage(request, actionMessages, "error.allStudentsAreInMarkSheets");
	} else {
	    final Set<MarkSheetEnrolmentEvaluationBean> impossibleEnrolmentEvaluationBeans = new HashSet<MarkSheetEnrolmentEvaluationBean>();
	    final Set<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeans = new HashSet<MarkSheetEnrolmentEvaluationBean>();

	    for (final Enrolment enrolment : enrolments) {
		final MarkSheetEnrolmentEvaluationBean markSheetEnrolmentEvaluationBean = new MarkSheetEnrolmentEvaluationBean();
		markSheetEnrolmentEvaluationBean.setEnrolment(enrolment);
		markSheetEnrolmentEvaluationBean.setEvaluationDate(createBean.getEvaluationDate());

		if (enrolment.isImpossible()) {
		    impossibleEnrolmentEvaluationBeans.add(markSheetEnrolmentEvaluationBean);
		} else {
		    enrolmentEvaluationBeans.add(markSheetEnrolmentEvaluationBean);
		}
	    }
	    createBean.setEnrolmentEvaluationBeans(enrolmentEvaluationBeans);
	    createBean.setImpossibleEnrolmentEvaluationBeans(impossibleEnrolmentEvaluationBeans);
	}
    }

    public ActionForward createMarkSheetStepTwo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	MarkSheetManagementCreateBean createBean = (MarkSheetManagementCreateBean) RenderUtils.getViewState("edit-invisible")
		.getMetaObject().getObject();
	createBean.setTeacher(Teacher.readByNumber(createBean.getTeacherNumber()));

	ActionMessages actionMessages = createActionMessages();
	IUserView userView = getUserView(request);
	try {
	    MarkSheet markSheet = (MarkSheet) ServiceUtils.executeService("CreateMarkSheet", new Object[] { createBean,
		    userView.getPerson().getEmployee() });
	    ((DynaActionForm) actionForm).set("msID", markSheet.getIdInternal());
	    return viewMarkSheet(mapping, actionForm, request, response);

	} catch (NotAuthorizedFilterException e) {
	    addMessage(request, actionMessages, "error.notAuthorized");
	} catch (DomainException e) {
	    addMessage(request, actionMessages, e.getMessage(), e.getArgs());
	}

	request.setAttribute("edit", createBean);
	return mapping.findForward("createMarkSheetStep2");
    }

    public ActionForward createMarkSheetStepTwoInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	/*
         * - This method is used when a validation error occurs. Instead of
         * creating a new bean we use the existing one. - If we dont't use this
         * method, the createMarkSheetStep1 is called (input method) and a new
         * create bean is created.
         */
	request.setAttribute("edit", RenderUtils.getViewState("edit-invisible").getMetaObject().getObject());
	return mapping.findForward("createMarkSheetStep2");
    }

    public ActionForward prepareRectifyMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	DynaActionForm form = (DynaActionForm) actionForm;
	MarkSheet markSheet = rootDomainObject.readMarkSheetByOID((Integer) form.get("msID"));

	MarkSheetRectifyBean rectifyBean = new MarkSheetRectifyBean();
	fillMarkSheetBean(actionForm, request, rectifyBean);
	rectifyBean.setUrl(buildUrl(form));

	rectifyBean.setMarkSheet(markSheet);
	request.setAttribute("rectifyBean", rectifyBean);
	request.setAttribute("msID", (Integer) form.get("msID"));

	List<EnrolmentEvaluation> enrolmentEvaluations = new ArrayList<EnrolmentEvaluation>(markSheet.getEnrolmentEvaluations());
	Collections.sort(enrolmentEvaluations, EnrolmentEvaluation.SORT_BY_STUDENT_NUMBER);
	request.setAttribute("enrolmentEvaluations", enrolmentEvaluations);
	return mapping.findForward("rectifyMarkSheetStep1");
    }

    public ActionForward rectifyMarkSheetStepOneByEvaluation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	DynaActionForm form = (DynaActionForm) actionForm;
	Integer evaluationID = (Integer) form.get("evaluationID");
	EnrolmentEvaluation enrolmentEvaluation = rootDomainObject.readEnrolmentEvaluationByOID(evaluationID);
	MarkSheet markSheet = enrolmentEvaluation.getMarkSheet();
	MarkSheetRectifyBean rectifyBean = new MarkSheetRectifyBean();
	rectifyBean.setMarkSheet(markSheet);
	rectifyBean.setEnrolmentEvaluation(enrolmentEvaluation);
	return rectifyMarkSheetStepOne(mapping, actionForm, request, response, rectifyBean, enrolmentEvaluation);
    }

    public ActionForward rectifyMarkSheetStepOneByStudentNumber(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	MarkSheetRectifyBean rectifyBean = (MarkSheetRectifyBean) RenderUtils.getViewState().getMetaObject().getObject();

	Integer studentNumber = rectifyBean.getStudentNumber();
	Student student = Student.readStudentByNumber(studentNumber);
	// Registration registration =
        // Registration.readStudentByNumberAndDegreeType(studentNumber,
        // DegreeType.DEGREE);
	if (student == null) {
	    ActionMessages actionMessages = new ActionMessages();
	    addMessage(request, actionMessages, "error.no.student", studentNumber.toString());
	    return prepareRectifyMarkSheet(mapping, actionForm, request, response);
	}
	MarkSheet markSheet = rectifyBean.getMarkSheet();
	EnrolmentEvaluation enrolmentEvaluation = markSheet.getEnrolmentEvaluationByStudent(student);

	if (enrolmentEvaluation == null) {
	    ActionMessages actionMessages = new ActionMessages();
	    addMessage(request, actionMessages, "error.no.student.in.markSheet", studentNumber.toString());
	    return prepareRectifyMarkSheet(mapping, actionForm, request, response);
	}
	if (!enrolmentEvaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.FINAL_OBJ)
		&& !enrolmentEvaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFICATION_OBJ)) {
	    ActionMessages actionMessages = new ActionMessages();
	    addMessage(request, actionMessages, "error.markSheet.student.alreadyRectified", studentNumber.toString());
	    return prepareRectifyMarkSheet(mapping, actionForm, request, response);
	}
	return rectifyMarkSheetStepOne(mapping, actionForm, request, response, rectifyBean, enrolmentEvaluation);
    }

    private ActionForward rectifyMarkSheetStepOne(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response, MarkSheetRectifyBean rectifyBean, EnrolmentEvaluation enrolmentEvaluation) {
	rectifyBean.setEnrolmentEvaluation(enrolmentEvaluation);
	request.setAttribute("rectifyBean", rectifyBean);

	return mapping.findForward("rectifyMarkSheetStep2");
    }

    public ActionForward rectifyMarkSheetStepTwo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	MarkSheetRectifyBean rectifyBean = (MarkSheetRectifyBean) RenderUtils.getViewState().getMetaObject().getObject();

	ActionMessages actionMessages = new ActionMessages();
	IUserView userView = getUserView(request);
	try {
	    ServiceUtils.executeService("CreateRectificationMarkSheet", new Object[] { rectifyBean.getMarkSheet(),
		    rectifyBean.getEnrolmentEvaluation(), rectifyBean.getRectifiedGrade(), rectifyBean.getEvaluationDate(),
		    rectifyBean.getReason(), userView.getPerson().getEmployee() });
	    return mapping.findForward("searchMarkSheetFilled");
	} catch (NotAuthorizedFilterException e) {
	    addMessage(request, actionMessages, "error.notAuthorized");
	    return mapping.findForward("searchMarkSheet");
	} catch (DomainException e) {
	    addMessage(request, actionMessages, e.getMessage(), e.getArgs());
	    return rectifyMarkSheetStepOne(mapping, actionForm, request, response, rectifyBean, rectifyBean
		    .getEnrolmentEvaluation());
	}
    }

    public ActionForward validationError(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	String[] pages = request.getParameterValues("page");
	String page = pages[1];
	if (page.equals("1")) {
	    return prepareRectifyMarkSheet(mapping, actionForm, request, response);
	} else {
	    return rectifyMarkSheetStepOneByEvaluation(mapping, actionForm, request, response);
	}
    }

    public ActionForward prepareSearchMarkSheetFilled(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("searchMarkSheetFilled");
    }

    public ActionForward showRectificationHistoric(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	DynaActionForm form = (DynaActionForm) actionForm;
	Integer evaluationID = (Integer) form.get("evaluationID");

	EnrolmentEvaluation enrolmentEvaluation = rootDomainObject.readEnrolmentEvaluationByOID(evaluationID);
	Enrolment enrolment = enrolmentEvaluation.getEnrolment();

	List<EnrolmentEvaluation> rectifiedAndRectificationEvaluations = enrolment.getConfirmedEvaluations(enrolmentEvaluation
		.getMarkSheet().getMarkSheetType());
	if (!rectifiedAndRectificationEvaluations.isEmpty()) {
	    request.setAttribute("enrolmentEvaluation", rectifiedAndRectificationEvaluations.remove(0));
	    request.setAttribute("rectificationEvaluations", rectifiedAndRectificationEvaluations);
	}

	return mapping.findForward("showRectificationHistoric");
    }
}
