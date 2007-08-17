package net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.commons.student.StudentNumberBean;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.commons.student.enrollment.bolonha.AbstractBolonhaStudentEnrollmentDA;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class BolonhaEnrolmentsManagementDA extends AbstractBolonhaStudentEnrollmentDA {

    public ActionForward prepareSearchStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Object renderedObject = getRenderedObject("student-number-bean");
	if (renderedObject == null) {
	    request.setAttribute("studentNumberBean", new StudentNumberBean());
	} else {
	    final StudentNumberBean numberBean = (StudentNumberBean) renderedObject;
	    final Student student = getStudent(numberBean);
	    if (student != null) {
		RenderUtils.invalidateViewState();
		request.setAttribute("studentCurricularPlans", getAllStudentCurricularPlans(student));
		request.setAttribute("studentNumberBean", numberBean);
	    }
	}

	return mapping.findForward("chooseStudentInformation");
    }

    public ActionForward showAllStudentCurricularPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	
	final StudentNumberBean studentNumberBean = new StudentNumberBean();
	final Student student = getStudent(request);
	studentNumberBean.setNumber(student.getNumber());

	request.setAttribute("studentNumberBean", studentNumberBean);
	request.setAttribute("studentCurricularPlans", getAllStudentCurricularPlans(student));

	return mapping.findForward("chooseStudentInformation");

    }

    private Student getStudent(final HttpServletRequest request) {
	return rootDomainObject.readStudentByOID(getIntegerFromRequest(request, "studentId"));
    }

    private List<StudentCurricularPlan> getAllStudentCurricularPlans(final Student student) {
	final List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
	for (final Registration registration : student.getRegistrations()) {
	    result.addAll(registration.getStudentCurricularPlans());
	}
	return result;
    }

    private Student getStudent(final StudentNumberBean numberBean) {
	return numberBean.getNumber() != null ? Student.readStudentByNumber(numberBean.getNumber()) : null;
    }

    private StudentCurricularPlan getStudentCurricularPlan(final HttpServletRequest request) {
	return rootDomainObject.readStudentCurricularPlanByOID(getIntegerFromRequest(request, "scpId"));
    }

    public ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("bolonhaStudentEnrollmentBean", new BolonhaStudentEnrollmentBean(getStudentCurricularPlan(request),
		ExecutionPeriod.readActualExecutionPeriod(), getCurricularYearForCurricularCourses(),
		getCurricularRuleLevel(form)));
	return mapping.findForward("showDegreeModulesToEnrol");
    }
    
    public ActionForward backToAllStudentCurricularPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final BolonhaStudentEnrollmentBean bean = (BolonhaStudentEnrollmentBean) getRenderedObject("bolonhaStudentEnrolments");
	request.setAttribute("studentId", bean.getStudentCurricularPlan().getRegistration().getStudent().getIdInternal());
	return showAllStudentCurricularPlans(mapping, form, request, response);
    }
    
    public ActionForward viewStudentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("studentCurricularPlan", getStudentCurricularPlan(request));
	return mapping.findForward("viewStudentCurriculum");
    }

    @Override
    public ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, BolonhaStudentEnrollmentBean bean) {

	request.setAttribute("bolonhaStudentEnrollmentBean", bean);
	return mapping.findForward("showDegreeModulesToEnrol");
    }

    @Override
    protected int[] getCurricularYearForCurricularCourses() {
	return null; // all years
    }

    @Override
    protected CurricularRuleLevel getCurricularRuleLevel(ActionForm form) {
	return CurricularRuleLevel.ENROLMENT_NO_RULES;
    }

    @Override
    protected String getAction() {
	// unnecessary method
	return null;
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	// unnecessary method
	return null;
    }

}
