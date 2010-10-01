package net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.presentationTier.Action.commons.tutorship.StudentsPerformanceGridDispatchAction;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher.TutorshipEntryExecutionYearProvider;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher.TutorshipMonitoringExecutionYearProvider;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ViewStudentsPerformanceGridDispatchAction extends StudentsPerformanceGridDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Person person = getLoggedPerson(request);
	generateStudentsPerformanceBean(request, person);
	request.setAttribute("tutor", person);
	RenderUtils.invalidateViewState();
	return prepareStudentsPerformanceGrid(mapping, actionForm, request, response, person);
    }

    protected void generateStudentsPerformanceBean(HttpServletRequest request, Person person) {
	StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) getRenderedObject("performanceGridFiltersBean");
	if ((bean != null) && (bean.getPerson() == person)) {
	    request.setAttribute("performanceGridFiltersBean", bean);
	    return;
	}

	if (!person.getTeacher().hasAnyTutorships()) {
	    return;
	}

	bean = StudentsPerformanceInfoBean.create(person);
	bean.setDegree(getFilteredDegree(bean));
	bean.setStudentsEntryYear(TutorshipEntryExecutionYearProvider.getExecutionYears(bean).get(0));
	bean.setCurrentMonitoringYear(TutorshipMonitoringExecutionYearProvider.getExecutionYears(bean).get(0));
	request.setAttribute("performanceGridFiltersBean", bean);
    }

    public ActionForward changeDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) getRenderedObject("performanceGridFiltersBean");
	if (bean != null && bean.getDegree() != null) {
	    bean.setStudentsEntryYear(TutorshipEntryExecutionYearProvider.getExecutionYears(bean).get(0));
	    bean.setCurrentMonitoringYear(TutorshipMonitoringExecutionYearProvider.getExecutionYears(bean).get(0));
	    request.setAttribute("performanceGridFiltersBean", bean);
	}
	return prepare(mapping, actionForm, request, response);
    }

    protected StudentsPerformanceInfoBean generateStudentsPerformanceBeanFromRequest(HttpServletRequest request, Person person) {
	StudentsPerformanceInfoBean bean = StudentsPerformanceInfoBean.create(person);
	bean.setDegree(rootDomainObject.readDegreeByOID(getIntegerFromRequest(request, "degreeOID")));
	bean.setStudentsEntryYear(rootDomainObject.readExecutionYearByOID(getIntegerFromRequest(request, "entryYearOID")));
	bean.setCurrentMonitoringYear(rootDomainObject
		.readExecutionYearByOID(getIntegerFromRequest(request, "monitoringYearOID")));
	request.setAttribute("performanceGridFiltersBean", bean);
	return bean;
    }

    protected ActionForward prepareStudentsPerformanceGrid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response, Person person) throws Exception {
	StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) request.getAttribute("performanceGridFiltersBean");

	if (bean != null) {
	    final List<Tutorship> tutorships;
	    if (bean.getActiveTutorships()) {

		tutorships = person.getTeacher().getActiveTutorshipsByStudentsEntryYearAndDegree(bean.getStudentsEntryYear(),
			bean.getDegree());
	    } else {
		tutorships = person.getTeacher().getPastTutorshipsByStudentsEntryYearAndDegree(bean.getStudentsEntryYear(),
			bean.getDegree());
	    }

	    if (tutorships != null && !tutorships.isEmpty()) {

		Collections.sort(tutorships, Tutorship.TUTORSHIP_COMPARATOR_BY_STUDENT_NUMBER);

		PerformanceGridTableDTO performanceGridTable = createPerformanceGridTable(request, tutorships, bean
			.getStudentsEntryYear(), bean.getCurrentMonitoringYear());
		getStatisticsAndPutInTheRequest(request, performanceGridTable);

		request.setAttribute("performanceGridFiltersBean", bean);
		request.setAttribute("performanceGridTable", performanceGridTable);
		request.setAttribute("totalStudents", tutorships.size());
	    }

	    request.setAttribute("monitoringYear", bean.getCurrentMonitoringYear());
	}
	request.setAttribute("tutor", person);
	return mapping.findForward("viewStudentsPerformanceGrid");
    }

    public ActionForward prepareAllStudentsStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	StudentsPerformanceInfoBean bean = generateStudentsPerformanceBeanFromRequest(request, getLoggedPerson(request));
	if (!bean.getTutorships().isEmpty()) {

	    List<DegreeCurricularPlan> plans = new ArrayList<DegreeCurricularPlan>(bean.getDegree().getDegreeCurricularPlans());
	    Collections.sort(plans,
		    DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

	    List<StudentCurricularPlan> students = plans.get(0).getStudentsCurricularPlanGivenEntryYear(
		    bean.getStudentsEntryYear());

	    putAllStudentsStatisticsInTheRequest(request, students, bean.getCurrentMonitoringYear());

	    request.setAttribute("entryYear", bean.getStudentsEntryYear());
	    request.setAttribute("totalEntryStudents", students.size());
	}
	return prepareStudentsPerformanceGrid(mapping, actionForm, request, response, getLoggedPerson(request));
    }

    private Degree getFilteredDegree(StudentsPerformanceInfoBean bean) {
	Set<Degree> degrees = new HashSet<Degree>();
	for (Tutorship tutorship : bean.getTutorships()) {
	    StudentCurricularPlan studentCurricularPlan = tutorship.getStudentCurricularPlan();
	    degrees.add(studentCurricularPlan.getRegistration().getDegree());
	}
	return degrees.iterator().next();
    }
}
