/**
 * Nov 23, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.credits;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherInstitutionWorkingTimeDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Ricardo Rodrigues
 * 
 */

@Mapping(module = "departmentAdmOffice", path = "/institutionWorkingTimeManagement", input = "/institutionWorkingTimeManagement.do?method=prepareEdit&page=0", attribute = "teacherInstitutionWorkingTimeForm", formBean = "teacherInstitutionWorkingTimeForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "successfull-delete", path = "/institutionWorkingTimeManagement.do?method=showTeacherWorkingTimePeriods&page=0"),
		@Forward(name = "successfull-edit", path = "/institutionWorkingTimeManagement.do?method=showTeacherWorkingTimePeriods&page=0"),
		@Forward(name = "list-teacher-institution-working-time", path = "list-institution-working-time"),
		@Forward(name = "teacher-not-found", path = "/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume&page=0"),
		@Forward(name = "edit-institution-work-time", path = "edit-institution-work-time") })
@Exceptions(value = {
		@ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherInstitutionWorkingTimeDispatchAction.InvalidPeriodException.class, key = "message.invalidPeriod", handler = org.apache.struts.action.ExceptionHandler.class, path = "/institutionWorkingTimeManagement.do?method=prepareEdit&page=0", scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class, handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class, scope = "request") })
public class DepartmentAdmOfficeManageTeacherInstitutionWorkingTimeDispatchAction extends
	ManageTeacherInstitutionWorkingTimeDispatchAction {

    public ActionForward showTeacherWorkingTimePeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	DynaActionForm institutionWorkingTimeForm = (DynaActionForm) form;
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(Integer
		.valueOf((String) institutionWorkingTimeForm.get("executionPeriodId")));

	Teacher teacher = DomainObject.fromExternalId(institutionWorkingTimeForm.getString("teacherId"));

	if (!isTeacherOfManageableDepartments(teacher, executionSemester, request)) {
	    request.setAttribute("teacherNotFound", "teacherNotFound");
	    return mapping.findForward("teacher-not-found");
	}

	getInstitutionWokTimeList(request, institutionWorkingTimeForm, executionSemester, teacher);
	return mapping.findForward("list-teacher-institution-working-time");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	DynaActionForm institutionWorkingTimeForm = (DynaActionForm) form;
	Integer institutionWorkingTimeID = (Integer) institutionWorkingTimeForm.get("institutionWorkTimeID");
	Integer executionPeriodID = Integer.valueOf(institutionWorkingTimeForm.getString("executionPeriodId"));

	Teacher teacher = DomainObject.fromExternalId(institutionWorkingTimeForm.getString("teacherId"));
	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);

	if (teacher == null || !isTeacherOfManageableDepartments(teacher, executionSemester, request)) {
	    return mapping.findForward("teacher-not-found");
	}

	InstitutionWorkTime institutionWorkTime = null;
	if (institutionWorkingTimeID != null && institutionWorkingTimeID != 0) {
	    institutionWorkTime = (InstitutionWorkTime) rootDomainObject.readTeacherServiceItemByOID(institutionWorkingTimeID);
	    if (!teacher.getTeacherServiceByExecutionPeriod(executionSemester).getInstitutionWorkTimes()
		    .contains(institutionWorkTime)) {
		return mapping.findForward("teacher-not-found");
	    }
	}

	prepareToEdit(institutionWorkTime, teacher, executionSemester, request, institutionWorkingTimeForm);
	return mapping.findForward("edit-institution-work-time");
    }

    private boolean isTeacherOfManageableDepartments(Teacher teacher, ExecutionSemester executionSemester,
	    HttpServletRequest request) {

	IUserView userView = UserView.getUser();
	List<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();

	if (teacher != null) {
	    Department teacherWorkingDepartment = teacher.getCurrentWorkingDepartment();
	    return teacherWorkingDepartment != null && manageableDepartments.contains(teacherWorkingDepartment);
	}
	return false;
    }

    public ActionForward editInstitutionWorkingTime(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException,
	    InvalidPeriodException {

	editInstitutionWorkingTime(form, request, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
	return mapping.findForward("successfull-edit");

    }

    public ActionForward deleteInstitutionWorkingTime(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	deleteInstitutionWorkingTime(form, request, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
	return mapping.findForward("successfull-delete");
    }
}