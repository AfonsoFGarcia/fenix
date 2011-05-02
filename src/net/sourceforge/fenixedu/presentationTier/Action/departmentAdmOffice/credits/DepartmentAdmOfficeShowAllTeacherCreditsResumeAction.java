package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.credits;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ShowAllTeacherCreditsResumeAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class DepartmentAdmOfficeShowAllTeacherCreditsResumeAction extends ShowAllTeacherCreditsResumeAction {

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	DynaActionForm dynaForm = (DynaActionForm) form;
	dynaForm.set("method", "showTeacherCreditsResume");
	return mapping.findForward("search-teacher-form");
    }

    public ActionForward showTeacherCreditsResume(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm dynaActionForm = (DynaActionForm) form;
	ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
	Teacher teacher = Teacher.readByIstId(dynaActionForm.getString("teacherId"));

	if (teacher == null || !isTeacherOfManageableDepartments(teacher, executionSemester, request)) {
	    request.setAttribute("teacherNotFound", "teacherNotFound");
	    dynaActionForm.set("method", "showTeacherCreditsResume");
	    return mapping.findForward("teacher-not-found");
	}

	readAllTeacherCredits(request, teacher);
	return mapping.findForward("show-all-credits-resume");
    }

    private boolean isTeacherOfManageableDepartments(Teacher teacher, ExecutionSemester executionSemester,
	    HttpServletRequest request) {
	IUserView userView = UserView.getUser();
	List<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();
	List<Unit> workingPlacesByPeriod = teacher.getWorkingPlacesByPeriod(executionSemester.getBeginDateYearMonthDay(),
		executionSemester.getEndDateYearMonthDay());
	for (Unit unit : workingPlacesByPeriod) {
	    Department teacherDepartment = unit.isDepartmentUnit() ? unit.getDepartment() : unit.getDepartmentUnit()
		    .getDepartment();
	    if (manageableDepartments.contains(teacherDepartment)) {
		return true;
	    }
	}
	return false;
    }
}
