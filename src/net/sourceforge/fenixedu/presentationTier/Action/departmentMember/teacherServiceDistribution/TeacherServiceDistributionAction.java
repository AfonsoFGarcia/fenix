package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class TeacherServiceDistributionAction extends FenixDispatchAction {
	private static final Integer NOT_SELECTED_EXECUTION_PERIOD = -1;

	public ActionForward prepareTeacherServiceDistribution(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("showMain");
	}

	public ActionForward prepareForTeacherServiceDistributionCreation(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("showTeacherServiceDistributionCreationOptions");
	}

	public ActionForward prepareForEmptyTeacherServiceDistributionCreation(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		IUserView userView = SessionUtils.getUserView(request);

		DynaActionForm dynaForm = (DynaActionForm) form;

		List<ExecutionYear> executionYearList = ExecutionYear.readNotClosedExecutionYears();
		Collections.sort(executionYearList, new BeanComparator("year"));
		
		ExecutionYear selectedExecutionYear = getSelectedExecutionYear(userView, dynaForm, executionYearList);
		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>(selectedExecutionYear.getExecutionPeriods());
		setCurrentExecutionYearInDynamicForm(userView, dynaForm, selectedExecutionYear);

		Collections.sort(executionPeriodList, new BeanComparator("semester"));

		request.setAttribute("departmentName", userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace().getRealName());
		request.setAttribute("executionYearList", executionYearList);
		request.setAttribute("executionPeriodsList", executionPeriodList);

		return mapping.findForward("showTeacherServiceDistributionCreationForm");
	}

	public ActionForward createTeacherServiceDistribution(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		IUserView userView = SessionUtils.getUserView(request);

		DynaActionForm dynaForm = (DynaActionForm) form;

		List<Integer> selectedExecutionPeriodIdList = new ArrayList<Integer>();

		Integer selectedExecutionPeriodId = (Integer) dynaForm.get("executionPeriod");
		if (selectedExecutionPeriodId.equals(NOT_SELECTED_EXECUTION_PERIOD)) {
			ExecutionYear selectedExecutionYear = getSelectedExecutionYear(userView, dynaForm, null);

			for (ExecutionPeriod executionPeriod : selectedExecutionYear.getExecutionPeriods()) {
				selectedExecutionPeriodIdList.add(executionPeriod.getIdInternal());
			}
		} else {
			selectedExecutionPeriodIdList.add(selectedExecutionPeriodId);
		}

		Integer selectedDepartmentId = userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace().getIdInternal();
		String name = (String) dynaForm.get("name");

		Object[] parameters = new Object[] {
				selectedExecutionPeriodIdList,
				selectedDepartmentId,
				userView.getPerson().getIdInternal(),
				name };

		TeacherServiceDistribution teacherServiceDistribution = (TeacherServiceDistribution) ServiceUtils.executeService(
				userView,
				"CreateTeacherServiceDistribution",
				parameters);

		return loadTeacherServiceDistributionServices(mapping, request, teacherServiceDistribution.getIdInternal(), userView);	
	}

	public ActionForward prepareForTeacherServiceDistributionEdition(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		IUserView userView = SessionUtils.getUserView(request);

		DynaActionForm dynaForm = (DynaActionForm) form;

		List<ExecutionYear> executionYearList = ExecutionYear.readNotClosedExecutionYears();
		Collections.sort(executionYearList, new BeanComparator("year"));

		ExecutionYear selectedExecutionYear = getSelectedExecutionYear(userView, dynaForm, null);

		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>();
		if (selectedExecutionYear != null) {
			executionPeriodList.addAll(selectedExecutionYear.getExecutionPeriods());
		}

		Department selectedDepartment = userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace();

		Collections.sort(executionPeriodList, new BeanComparator("semester"));

		List<TeacherServiceDistribution> teacherServiceDistributionList;

		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm);

		if (selectedExecutionPeriod != null) {
			teacherServiceDistributionList = selectedDepartment.getTeacherServiceDistributionsByExecutionPeriod(selectedExecutionPeriod);
		} else if (selectedExecutionYear != null) {
			teacherServiceDistributionList = selectedDepartment.getTeacherServiceDistributionsByExecutionYear(selectedExecutionYear);
		} else {
			teacherServiceDistributionList = new ArrayList<TeacherServiceDistribution>(selectedDepartment.getTeacherServiceDistributions());
		}
		
		final Person person = userView.getPerson();
		teacherServiceDistributionList = (List<TeacherServiceDistribution>) CollectionUtils.select(teacherServiceDistributionList, new Predicate() {

			public boolean evaluate(Object arg0) {
				TeacherServiceDistribution tsd = (TeacherServiceDistribution) arg0;
				return tsd.hasAnyPermission(person);
			}
		});
		
		Collections.sort(teacherServiceDistributionList, new BeanComparator("name"));

		request.setAttribute("departmentName", userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace().getRealName());
		request.setAttribute("executionYearList", executionYearList);
		request.setAttribute("executionPeriodsList", executionPeriodList);
		request.setAttribute("teacherServiceDistributionList", teacherServiceDistributionList);

		return mapping.findForward("showTeacherServiceDistributions");
	}

	public ActionForward showTeacherServiceDistributionServices(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Integer teacherServiceDistributionId = new Integer(request.getParameter("teacherServiceDistribution"));
		IUserView userView = SessionUtils.getUserView(request);

		

		return loadTeacherServiceDistributionServices(mapping, request, teacherServiceDistributionId, userView);
	}

	private ActionForward loadTeacherServiceDistributionServices(ActionMapping mapping, HttpServletRequest request, Integer teacherServiceDistributionId, IUserView userView) {
		setPermissionsOnRequest(
				request,
				rootDomainObject.readTeacherServiceDistributionByOID(teacherServiceDistributionId),
				userView.getPerson());

		request.setAttribute("teacherServiceDistribution", rootDomainObject.readTeacherServiceDistributionByOID(teacherServiceDistributionId));
		return mapping.findForward("showTeacherServiceDistributionServices");
	}

	private void setPermissionsOnRequest(
			HttpServletRequest request,
			TeacherServiceDistribution teacherServiceDistribution,
			Person userViewPerson) {
		Boolean permissionToCoursesAndTeachersValuation = teacherServiceDistribution.hasPermissionToCoursesAndTeachersValuation(userViewPerson)
				|| teacherServiceDistribution.getHasSuperUserPermission(userViewPerson);
		
		Boolean permissionToCoursesAndTeachersManagement = teacherServiceDistribution.hasPermissionToCoursesAndTeachersManagement(userViewPerson)
				|| teacherServiceDistribution.getHasSuperUserPermission(userViewPerson);
		
		Boolean phaseManagementPermission = teacherServiceDistribution.getIsMemberOfPhasesManagementGroup(userViewPerson);
		
		Boolean automaticValuationPermission = teacherServiceDistribution.getIsMemberOfAutomaticValuationGroup(userViewPerson);
		
		Boolean omissionConfigurationPermission = teacherServiceDistribution.getIsMemberOfOmissionConfigurationGroup(userViewPerson);
		
		Boolean valuationCompetenceCoursesAndTeachersManagementPermission = teacherServiceDistribution.getIsMemberOfValuationCompetenceCoursesAndTeachersManagementGroup(userViewPerson);
		
		request.setAttribute("coursesAndTeachersValuationPermission", permissionToCoursesAndTeachersValuation);

		request.setAttribute("coursesAndTeachersManagementPermission", permissionToCoursesAndTeachersManagement);

		request.setAttribute(
				"phaseManagementPermission",
				phaseManagementPermission);

		request.setAttribute(
				"automaticValuationPermission",
				automaticValuationPermission);

		request.setAttribute(
				"omissionConfigurationPermission",
				omissionConfigurationPermission);

		request.setAttribute(
				"valuationCompetenceCoursesAndTeachersManagementPermission",
				valuationCompetenceCoursesAndTeachersManagementPermission);

		request.setAttribute("permissionsGrantPermission", teacherServiceDistribution.getHavePermissionSettings(userViewPerson));
		
		request.setAttribute("viewTeacherServiceDistributionValuationPermission", permissionToCoursesAndTeachersValuation || permissionToCoursesAndTeachersManagement || phaseManagementPermission || automaticValuationPermission || omissionConfigurationPermission || valuationCompetenceCoursesAndTeachersManagementPermission);
	}


	public ActionForward prepareForTeacherServiceDistributionCopy(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		List<ExecutionYear> executionYearList = ExecutionYear.readNotClosedExecutionYears();
		Collections.sort(executionYearList, new BeanComparator("year"));
	
		ExecutionYear selectedExecutionYear = getSelectedExecutionYear(userView, dynaForm, null);

		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>();
		if (selectedExecutionYear != null) {
			executionPeriodList.addAll(selectedExecutionYear.getExecutionPeriods());
		}

		ExecutionYear selectedExecutionYearForCopy = getSelectedExecutionYearForCopy(
				userView,
				dynaForm,
				executionYearList);

		List<ExecutionPeriod> executionPeriodListForCopy = new ArrayList<ExecutionPeriod>();
		if (selectedExecutionYearForCopy != null) {
			executionPeriodListForCopy.addAll(selectedExecutionYearForCopy.getExecutionPeriods());
		}

		Department selectedDepartment = userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace();

		Collections.sort(executionPeriodList, new BeanComparator("semester"));
		Collections.sort(executionPeriodListForCopy, new BeanComparator("semester"));

		List<TeacherServiceDistribution> teacherServiceDistributionList;

		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm);

		if (selectedExecutionPeriod != null) {
			teacherServiceDistributionList = selectedDepartment.getTeacherServiceDistributionsByExecutionPeriod(selectedExecutionPeriod);
		} else if (selectedExecutionYear != null) {
			teacherServiceDistributionList = selectedDepartment.getTeacherServiceDistributionsByExecutionYear(selectedExecutionYear);
		} else {
			teacherServiceDistributionList =  new ArrayList<TeacherServiceDistribution>(selectedDepartment.getTeacherServiceDistributions());
		}

		if (teacherServiceDistributionList.size() > 0) {
			dynaForm.set("teacherServiceDistribution", teacherServiceDistributionList.get(0).getIdInternal());
			Collections.sort(teacherServiceDistributionList, new BeanComparator("name"));
		}

		request.setAttribute("departmentName", userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace().getRealName());
		dynaForm.set("executionYearForCopy", selectedExecutionYearForCopy.getIdInternal());
		request.setAttribute("executionYearList", executionYearList);
		request.setAttribute("executionPeriodsList", executionPeriodList);
		request.setAttribute("executionPeriodsListForCopy", executionPeriodListForCopy);
		request.setAttribute("teacherServiceDistributionList", teacherServiceDistributionList);

		return mapping.findForward("showTeacherServiceDistributionsForCopy");
	}

	public ActionForward copyTeacherServiceDistribution(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		ExecutionYear selectedExecutionYear = getSelectedExecutionYearForCopy(userView, dynaForm, null);
		ExecutionPeriod selectedExecutionPeriodForCopy = getSelectedExecutionPeriodForCopy(userView, dynaForm);
		String name = (String) dynaForm.get("name");
		TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(
				userView,
				dynaForm);

		List<Integer> selectedExecutionPeriodListForCopyId = new ArrayList<Integer>();
		if (selectedExecutionPeriodForCopy != null) {
			selectedExecutionPeriodListForCopyId.add(selectedExecutionPeriodForCopy.getIdInternal());
		} else {
			for (ExecutionPeriod executionPeriod : selectedExecutionYear.getExecutionPeriods())
				selectedExecutionPeriodListForCopyId.add(executionPeriod.getIdInternal());
		}

		Object[] parameters = new Object[] {
				selectedExecutionPeriodListForCopyId,
				selectedTeacherServiceDistribution.getIdInternal(),
				userView.getPerson().getIdInternal(),
				name };

		TeacherServiceDistribution teacherServiceDistribution = (TeacherServiceDistribution) ServiceUtils.executeService(
				userView,
				"CopyTeacherServiceDistribution",
				parameters);

		request.setAttribute("teacherServiceDistribution", teacherServiceDistribution);
		return loadTeacherServiceDistributionServices(mapping, request, teacherServiceDistribution.getIdInternal(), userView);
	}

	private ExecutionYear getSelectedExecutionYear(
			IUserView userView,
			DynaActionForm dynaForm,
			List<ExecutionYear> executionYearList) throws FenixServiceException, FenixFilterException {
		Integer selectedExecutionYearId = (Integer) dynaForm.get("executionYear");

		ExecutionYear selectedExecutionYear = rootDomainObject.readExecutionYearByOID(selectedExecutionYearId);

		if (selectedExecutionYear == null) {
			if (executionYearList != null && !executionYearList.isEmpty()) {
				return getCurrentExecutionYear(executionYearList);
			} else {
				return null;
			}
		}

		return selectedExecutionYear;
	}

	private ExecutionPeriod getSelectedExecutionPeriod(IUserView userView, DynaActionForm dynaForm) throws FenixServiceException, FenixFilterException {
		Integer selectedExeuctionPeriodId = (Integer) dynaForm.get("executionPeriod");

		if (selectedExeuctionPeriodId == NOT_SELECTED_EXECUTION_PERIOD) {
			return null;
		}

		ExecutionPeriod selectedExecutionPeriod = rootDomainObject.readExecutionPeriodByOID(selectedExeuctionPeriodId);

		return selectedExecutionPeriod;
	}

	private void setCurrentExecutionYearInDynamicForm(
			IUserView userView,
			DynaActionForm dynaForm,
			ExecutionYear executionYear) throws FenixServiceException, FenixFilterException {
		dynaForm.set("executionYear", executionYear.getIdInternal());
	}
	
	private ExecutionYear getCurrentExecutionYear(List<ExecutionYear> executionYearList) {
		return (ExecutionYear) CollectionUtils.find(executionYearList, new Predicate() {
			public boolean evaluate(Object arg0) {
				ExecutionYear executionYear = (ExecutionYear) arg0;
				return executionYear.getState().equals(PeriodState.CURRENT);
			}
		});
	}

	private ExecutionYear getSelectedExecutionYearForCopy(
			IUserView userView,
			DynaActionForm dynaForm,
			List<ExecutionYear> executionYearList) throws FenixFilterException, FenixServiceException {
		Integer selectedExecutionYearId = (Integer) dynaForm.get("executionYearForCopy");

		ExecutionYear selectedExecutionYear = rootDomainObject.readExecutionYearByOID(selectedExecutionYearId);

		if (selectedExecutionYear == null) {
			if (executionYearList != null && !executionYearList.isEmpty()) {
				return getCurrentExecutionYear(executionYearList);
			} else {
				return null;
			}
		}

		return selectedExecutionYear;
	}

	private ExecutionPeriod getSelectedExecutionPeriodForCopy(IUserView userView, DynaActionForm dynaForm) throws FenixFilterException, FenixServiceException {
		Integer selectedExecutionPeriodId = (Integer) dynaForm.get("executionPeriodForCopy");

		ExecutionPeriod selectedExecutionPeriod = rootDomainObject.readExecutionPeriodByOID(selectedExecutionPeriodId);

		return selectedExecutionPeriod;
	}

	private TeacherServiceDistribution getSelectedTeacherServiceDistribution(IUserView userView, DynaActionForm dynaForm) throws FenixServiceException, FenixFilterException {
		Integer teacherServiceDistributionId = (Integer) dynaForm.get("teacherServiceDistribution");
		TeacherServiceDistribution teacherServiceDistribution = rootDomainObject.readTeacherServiceDistributionByOID(teacherServiceDistributionId);

		return teacherServiceDistribution;
	}
}
