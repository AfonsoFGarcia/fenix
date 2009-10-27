package net.sourceforge.fenixedu.presentationTier.Action.coordinator.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentInquiriesCourseResultBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeachingInquiryDTO;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.ViewInquiriesResultPageDTO;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.YearDelegateCourseInquiryDTO;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CoordinatorExecutionDegreeCoursesReport;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesCourseResult;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesTeachingResult;
import net.sourceforge.fenixedu.domain.inquiries.teacher.TeachingInquiry;
import net.sourceforge.fenixedu.domain.student.YearDelegateCourseInquiry;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

abstract public class ViewInquiriesResultsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("executionPeriods", getExecutionSemesters(request, actionForm));
	request.setAttribute("otherExecutionCourses", Collections.EMPTY_LIST);
	request.setAttribute("excelentExecutionCourses", Collections.EMPTY_LIST);
	request.setAttribute("executionCoursesToImproove", Collections.EMPTY_LIST);
	((ViewInquiriesResultPageDTO) actionForm).setDegreeCurricularPlanID(getIntegerFromRequest(request,
		"degreeCurricularPlanID"));

	return actionMapping.findForward("curricularUnitSelection");
    }

    protected List<ExecutionSemester> getExecutionSemesters(HttpServletRequest request, ActionForm actionForm) {
	Integer degreeCurricularPlanID = getIntegerFromRequest(request, "degreeCurricularPlanID");
	if (degreeCurricularPlanID == null || degreeCurricularPlanID == 0) {
	    degreeCurricularPlanID = ((ViewInquiriesResultPageDTO) actionForm).getDegreeCurricularPlanID();
	}
	final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(getIntegerFromRequest(
		request, "degreeCurricularPlanID"));
	final List<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>();
	final Person loggedPerson = AccessControl.getPerson();
	for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
	    if (executionDegree.getCoordinatorByTeacher(loggedPerson) != null) {
		final ExecutionYear executionYear = executionDegree.getExecutionYear();
		executionSemesters.addAll(executionYear.getExecutionPeriodsSet());
	    }
	}
	Collections.sort(executionSemesters);
	Collections.reverse(executionSemesters);
	return executionSemesters;
    }

    public ActionForward selectexecutionSemester(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ViewInquiriesResultPageDTO resultPageDTO = (ViewInquiriesResultPageDTO) actionForm;
	final ExecutionSemester executionSemester = resultPageDTO.getExecutionSemester();
	if (executionSemester == null) {
	    return prepare(actionMapping, actionForm, request, response);
	}
	final ExecutionYear executionYear = executionSemester.getExecutionYear();
	final DegreeCurricularPlan degreeCurricularPlan = resultPageDTO.getDegreeCurricularPlan();
	final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
	if (executionDegree == null) {
	    return prepare(actionMapping, actionForm, request, response);
	}

	resultPageDTO.setExecutionDegreeID(executionDegree.getOid());

	Collection<StudentInquiriesCourseResult> otherExecutionCourses = new ArrayList<StudentInquiriesCourseResult>();
	Collection<StudentInquiriesCourseResult> executionCoursesToImproove = new ArrayList<StudentInquiriesCourseResult>();
	Collection<StudentInquiriesCourseResult> excelentExecutionCourses = new ArrayList<StudentInquiriesCourseResult>();

	for (StudentInquiriesCourseResult studentInquiriesCourseResult : executionDegree.getStudentInquiriesCourseResults()) {
	    final ExecutionCourse executionCourse = studentInquiriesCourseResult.getExecutionCourse();
	    if (executionCourse != null && executionCourse.getExecutionPeriod() == executionSemester) {

		if (studentInquiriesCourseResult.isUnsatisfactory()
			|| hasTeachingResultsToImproove(executionDegree, executionCourse)) {
		    executionCoursesToImproove.add(getStudentInquiriesCourseResult(executionCourse, executionDegree));
		} else if (studentInquiriesCourseResult.isExcellent()
			|| hasExcellentTeachingResults(executionDegree, executionCourse)) {
		    excelentExecutionCourses.add(getStudentInquiriesCourseResult(executionCourse, executionDegree));
		} else {
		    otherExecutionCourses.add(getStudentInquiriesCourseResult(executionCourse, executionDegree));
		}

	    }
	}

	Collections.sort((List<StudentInquiriesCourseResult>) otherExecutionCourses,
		StudentInquiriesCourseResult.EXECUTION_COURSE_NAME_COMPARATOR);
	Collections.sort((List<StudentInquiriesCourseResult>) executionCoursesToImproove,
		StudentInquiriesCourseResult.EXECUTION_COURSE_NAME_COMPARATOR);
	Collections.sort((List<StudentInquiriesCourseResult>) excelentExecutionCourses,
		StudentInquiriesCourseResult.EXECUTION_COURSE_NAME_COMPARATOR);

	if (getFromRequest(request, "courseResultsCoordinatorCommentEdit") != null) {
	    request.setAttribute("courseResultsCoordinatorCommentEdit", true);
	}

	request
		.setAttribute("executionDegreeCoursesReport",
			getExecutionDegreeCoursesReports(executionSemester, executionDegree));
	request.setAttribute("executionSemester", executionSemester);
	request.setAttribute("canComment", coordinatorCanComment(executionDegree, executionSemester));
	request.setAttribute("executionPeriods", getExecutionSemesters(request, actionForm));
	request.setAttribute("otherExecutionCourses", otherExecutionCourses);
	request.setAttribute("excelentExecutionCourses", excelentExecutionCourses);
	request.setAttribute("executionCoursesToImproove", executionCoursesToImproove);
	request.setAttribute("executionDegreeID", resultPageDTO.getExecutionDegreeID());

	return actionMapping.findForward("curricularUnitSelection");
    }

    private CoordinatorExecutionDegreeCoursesReport getExecutionDegreeCoursesReports(final ExecutionSemester executionSemester,
	    final ExecutionDegree executionDegree) {
	if (executionDegree.getDegreeType().isThirdCycle()) {
	    return null;
	}

	final CoordinatorExecutionDegreeCoursesReport executionDegreeCoursesReports = executionDegree
		.getExecutionDegreeCoursesReports(executionSemester);
	try {
	    return executionDegreeCoursesReports == null && coordinatorCanComment(executionDegree, executionSemester) ? CoordinatorExecutionDegreeCoursesReport
		    .makeNew(executionDegree, executionSemester)
		    : executionDegreeCoursesReports;
	} catch (DomainException e) {
	    return null;
	}
    }

    private boolean hasTeachingResultsToImproove(final ExecutionDegree executionDegree, final ExecutionCourse executionCourse) {
	for (Professorship otherTeacherProfessorship : executionCourse.getProfessorships()) {
	    for (StudentInquiriesTeachingResult studentInquiriesTeachingResult : otherTeacherProfessorship
		    .getStudentInquiriesTeachingResults()) {
		if (studentInquiriesTeachingResult.getExecutionDegree() == executionDegree
			&& studentInquiriesTeachingResult.isUnsatisfactory()) {
		    return true;
		}
	    }
	}
	return false;
    }

    private boolean hasExcellentTeachingResults(final ExecutionDegree executionDegree, final ExecutionCourse executionCourse) {
	for (Professorship otherTeacherProfessorship : executionCourse.getProfessorships()) {
	    for (StudentInquiriesTeachingResult studentInquiriesTeachingResult : otherTeacherProfessorship
		    .getStudentInquiriesTeachingResults()) {
		if (studentInquiriesTeachingResult.getExecutionDegree() == executionDegree
			&& studentInquiriesTeachingResult.isExcellent()) {
		    return true;
		}
	    }
	}
	return false;
    }

    public ActionForward selectExecutionCourse(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	StudentInquiriesCourseResult courseResult = (StudentInquiriesCourseResult) getRenderedObject();

	final ExecutionCourse executionCourse = courseResult == null ? (ExecutionCourse) DomainObject.fromOID(getLongFromRequest(
		request, "executionCourseID")) : courseResult.getExecutionCourse();
	final ExecutionDegree executionDegree = courseResult == null ? (ExecutionDegree) DomainObject.fromOID(getLongFromRequest(
		request, "executionDegreeID")) : courseResult.getExecutionDegree();

	request.setAttribute("canComment", coordinatorCanComment(executionDegree, executionCourse.getExecutionPeriod()));
	request.setAttribute("executionCourse", executionCourse);

	if (executionCourse != null) {

	    if (courseResult == null && getFromRequest(request, "courseResultsCoordinatorCommentEdit") != null) {
		request.setAttribute("courseResultsCoordinatorCommentEdit", true);
	    }

	    ((ViewInquiriesResultPageDTO) actionForm).setExecutionCourseID(executionCourse.getOid());
	    ((ViewInquiriesResultPageDTO) actionForm).setExecutionDegreeID(executionDegree.getOid());
//	    ((ViewInquiriesResultPageDTO) actionForm).setDegreeCurricularPlanID(executionDegree.getDegreeCurricularPlan().getIdInternal());

	    final StudentInquiriesCourseResultBean courseResultBean = populateStudentInquiriesCourseResults(executionCourse,
		    executionDegree);
	    request.setAttribute("isToImproove", courseResultBean.getStudentInquiriesCourseResult().isUnsatisfactory()
		    || hasTeachingResultsToImproove(executionDegree, executionCourse));
	    request.setAttribute("studentInquiriesCourseResult", courseResultBean);

	    return actionMapping.findForward("inquiryResults");
	}
	return selectexecutionSemester(actionMapping, actionForm, request, response);
    }

    abstract protected boolean coordinatorCanComment(final ExecutionDegree executionDegree,
	    final ExecutionSemester executionPeriod);

    private StudentInquiriesCourseResultBean populateStudentInquiriesCourseResults(final ExecutionCourse executionCourse,
	    final ExecutionDegree executionDegree) {

	StudentInquiriesCourseResultBean resultBean = new StudentInquiriesCourseResultBean(getStudentInquiriesCourseResult(
		executionCourse, executionDegree));

	for (Professorship otherTeacherProfessorship : executionCourse.getProfessorships()) {
	    for (StudentInquiriesTeachingResult studentInquiriesTeachingResult : otherTeacherProfessorship
		    .getStudentInquiriesTeachingResults()) {
		if (studentInquiriesTeachingResult.getExecutionDegree() == executionDegree
			&& studentInquiriesTeachingResult.getInternalDegreeDisclosure()) {
		    resultBean.addStudentInquiriesTeachingResult(studentInquiriesTeachingResult);
		}
	    }
	}

	return resultBean;
    }

    private StudentInquiriesCourseResult getStudentInquiriesCourseResult(final ExecutionCourse executionCourse,
	    final ExecutionDegree executionDegree) {
	for (StudentInquiriesCourseResult studentInquiriesCourseResult : executionCourse.getStudentInquiriesCourseResults()) {
	    if (studentInquiriesCourseResult.getExecutionDegree() == executionDegree) {
		return studentInquiriesCourseResult;
	    }
	}
	return null;
    }

    public ActionForward showInquiryCourseResult(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("inquiryResult", RootDomainObject.getInstance().readStudentInquiriesCourseResultByOID(
		getIntegerFromRequest(request, "resultId")));
	return actionMapping.findForward("showCourseInquiryResult");
    }

    public ActionForward showInquiryTeachingResult(ActionMapping actionMapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("inquiryResult", RootDomainObject.getInstance().readStudentInquiriesTeachingResultByOID(
		getIntegerFromRequest(request, "resultId")));
	return actionMapping.findForward("showTeachingInquiryResult");
    }

    public ActionForward showFilledTeachingInquiry(ActionMapping actionMapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final TeachingInquiry teachingInquiry = RootDomainObject.getInstance().readTeachingInquiryByOID(
		getIntegerFromRequest(request, "filledTeachingInquiryId"));
	request.setAttribute("teachingInquiry", teachingInquiry);

	final ExecutionSemester executionPeriod = teachingInquiry.getProfessorship().getExecutionCourse().getExecutionPeriod();
	if (executionPeriod.getSemester() == 2 && executionPeriod.getYear().equals("2007/2008")) {
	    return actionMapping.findForward("showFilledTeachingInquiry");
	}

	request.setAttribute("teachingInquiryDTO", new TeachingInquiryDTO(teachingInquiry.getProfessorship()));
	return actionMapping.findForward("showFilledTeachingInquiry_v2");

    }

    public ActionForward showFilledYearDelegateInquiry(ActionMapping actionMapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final YearDelegateCourseInquiry delegateCourseInquiry = RootDomainObject.getInstance()
		.readYearDelegateCourseInquiryByOID(getIntegerFromRequest(request, "filledYearDelegateInquiryId"));

	request.setAttribute("delegateInquiryDTO", new YearDelegateCourseInquiryDTO(delegateCourseInquiry));
	return actionMapping.findForward("showFilledDelegateInquiry");

    }

}
