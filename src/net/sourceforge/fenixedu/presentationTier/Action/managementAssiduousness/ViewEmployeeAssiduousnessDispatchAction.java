package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.ClockingsDaySheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeScheduleFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.Clocking;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.Justification;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.DateTimeFieldType;
import org.joda.time.YearMonthDay;

public class ViewEmployeeAssiduousnessDispatchAction extends FenixDispatchAction {
    public ActionForward chooseEmployee(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	String action = request.getParameter("action");
	request.setAttribute("action", action);
	request.setAttribute("yearMonth", getYearMonth(request, null));
	return mapping.findForward("choose-employee");
    }

    public ActionForward showWorkSheet(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	final IUserView userView = SessionUtils.getUserView(request);
	final Employee employee = getEmployee(request, (DynaActionForm) form);
	ActionForward actionForward = validateEmployee(mapping, request, employee);
	if (actionForward != null) {
	    return actionForward;
	}
	YearMonth yearMonth = getYearMonth(request, employee);
	if (yearMonth == null) {
	    return mapping.findForward("show-employee-work-sheet");
	}
	YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(),
		yearMonth.getMonth().ordinal() + 1, 01);
	int endDay = beginDate.dayOfMonth().getMaximumValue();
	if (yearMonth.getYear() == new YearMonthDay().getYear()
		&& yearMonth.getMonth().ordinal() + 1 == new YearMonthDay().getMonthOfYear()) {
	    // endDay = new YearMonthDay().getDayOfMonth();
	    request.setAttribute("displayCurrentDayNote", "true");
	}
	YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1,
		endDay);

	Object[] args = { employee.getAssiduousness(), beginDate, endDate };
	EmployeeWorkSheet employeeWorkSheet = (EmployeeWorkSheet) ServiceUtils.executeService(userView,
		"ReadEmployeeWorkSheet", args);

	request.setAttribute("employeeWorkSheet", employeeWorkSheet);
	setEmployeeStatus(request, employee, beginDate, endDate);
	request.setAttribute("yearMonth", yearMonth);
	return mapping.findForward("show-employee-work-sheet");
    }

    public ActionForward showSchedule(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	final Employee employee = getEmployee(request, (DynaActionForm) form);
	ActionForward actionForward = validateEmployee(mapping, request, employee);
	if (actionForward != null) {
	    return actionForward;
	}
	YearMonth yearMonth = getYearMonth(request, employee);
	request.setAttribute("yearMonth", yearMonth);

	List<Schedule> schedules = new ArrayList<Schedule>(employee.getAssiduousness().getSchedules());
	if (!schedules.isEmpty()) {
	    Collections.sort(schedules, new BeanComparator("beginDate"));
	}

	Schedule choosenSchedule = null;
	Integer scheduleID = getIntegerFromRequest(request, "scheduleID");
	if (scheduleID != null) {
	    for (Schedule schedule : schedules) {
		if (schedule.getIdInternal().equals(scheduleID)) {
		    choosenSchedule = schedule;
		    break;
		}
	    }
	}
	if (choosenSchedule == null) {
	    choosenSchedule = employee.getAssiduousness() != null ? employee.getAssiduousness()
		    .getCurrentSchedule() : null;
	}
	request.setAttribute("scheduleList", schedules);
	EmployeeScheduleFactory employeeScheduleFactory = new EmployeeScheduleFactory(employee, null,
		choosenSchedule);
	request.setAttribute("employeeScheduleBean", employeeScheduleFactory);

	return mapping.findForward("show-schedule");
    }

    public ActionForward showClockings(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	final Employee employee = getEmployee(request, (DynaActionForm) form);
	ActionForward actionForward = validateEmployee(mapping, request, employee);
	if (actionForward != null) {
	    return actionForward;
	}
	YearMonth yearMonth = getYearMonth(request, employee);
	if (yearMonth == null) {
	    return mapping.findForward("show-employee-work-sheet");
	}

	YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(),
		yearMonth.getMonth().ordinal() + 1, 01);
	YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1,
		beginDate.dayOfMonth().getMaximumValue());
	if (employee.getAssiduousness() != null) {
	    List<Clocking> clockings = employee.getAssiduousness().getClockingsAndAnulatedClockings(
		    beginDate, endDate);
	    Collections.sort(clockings, AssiduousnessRecord.COMPARATOR_BY_DATE);
	    HashMap<YearMonthDay, ClockingsDaySheet> clockingsDaySheetList = new HashMap<YearMonthDay, ClockingsDaySheet>();
	    for (Clocking clocking : clockings) {
		if (clockingsDaySheetList.containsKey(clocking.getDate().toYearMonthDay())) {
		    ClockingsDaySheet clockingsDaySheet = clockingsDaySheetList.get(clocking.getDate()
			    .toYearMonthDay());
		    clockingsDaySheet.addClocking(clocking);
		} else {
		    ClockingsDaySheet clockingsDaySheet = new ClockingsDaySheet();
		    clockingsDaySheet.setDate(clocking.getDate().toYearMonthDay());
		    clockingsDaySheet.addClocking(clocking);
		    clockingsDaySheetList.put(clocking.getDate().toYearMonthDay(), clockingsDaySheet);
		}
	    }

	    List<ClockingsDaySheet> orderedClockings = new ArrayList<ClockingsDaySheet>(
		    clockingsDaySheetList.values());
	    Collections.sort(orderedClockings, new BeanComparator("date"));
	    request.setAttribute("clockings", orderedClockings);
	}
	setEmployeeStatus(request, employee, beginDate, endDate);
	request.setAttribute("yearMonth", yearMonth);
	request.setAttribute("employee", employee);
	return mapping.findForward("show-clockings");
    }

    public ActionForward showJustifications(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	final Employee employee = getEmployee(request, (DynaActionForm) form);
	ActionForward actionForward = validateEmployee(mapping, request, employee);
	if (actionForward != null) {
	    return actionForward;
	}
	YearMonth yearMonth = getYearMonth(request, employee);
	if (yearMonth == null) {
	    return mapping.findForward("show-employee-work-sheet");
	}

	if (employee.getAssiduousness() != null) {
	    YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth()
		    .ordinal() + 1, 01);
	    YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(),
		    yearMonth.getMonth().ordinal() + 1, beginDate.dayOfMonth().getMaximumValue());
	    List<Justification> justifications = new ArrayList<Justification>();
	    justifications.addAll(employee.getAssiduousness().getLeaves(beginDate, endDate));
	    justifications.addAll(employee.getAssiduousness().getMissingClockings(beginDate, endDate));
	    List<Justification> orderedJustifications = new ArrayList<Justification>(justifications);
	    Collections.sort(orderedJustifications, AssiduousnessRecord.COMPARATOR_BY_DATE);
	    request.setAttribute("justifications", orderedJustifications);
	    setEmployeeStatus(request, employee, beginDate, endDate);
	}
	request.setAttribute("yearMonth", yearMonth);
	request.setAttribute("employee", employee);
	return mapping.findForward("show-justifications");
    }

    private ActionForward validateEmployee(ActionMapping mapping, HttpServletRequest request,
	    final Employee employee) {
	if (employee == null || employee.getAssiduousness() == null) {
	    ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("message", new ActionMessage("error.invalidEmployee"));
	    saveMessages(request, actionMessages);
	    return mapping.getInputForward();
	}
	return null;
    }

    private Employee getEmployee(HttpServletRequest request, DynaActionForm form) {
	Integer employeeNumber = null;
	String employeeNumberString = form.getString("employeeNumber");
	if (StringUtils.isEmpty(employeeNumberString)) {
	    Object number = getFromRequest(request, "employeeNumber");
	    employeeNumber = number instanceof String ? new Integer((String) number) : (Integer) number;
	} else {
	    employeeNumber = new Integer(employeeNumberString);
	}
	return Employee.readByNumber(employeeNumber);
    }

    private YearMonth getYearMonth(HttpServletRequest request, Employee employee) {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	if (yearMonth == null) {
	    yearMonth = (YearMonth) request.getAttribute("yearMonth");
	}
	if (yearMonth == null) {
	    yearMonth = new YearMonth();
	    String year = request.getParameter("year");
	    String month = request.getParameter("month");

	    if (StringUtils.isEmpty(year) || StringUtils.isEmpty(month)) {
		ClosedMonth lastClosedMonth = ClosedMonth.getLastMonthClosed();
		if (lastClosedMonth != null) {
		    yearMonth = new YearMonth(lastClosedMonth.getClosedYearMonth().get(
			    DateTimeFieldType.year()), lastClosedMonth.getClosedYearMonth().get(
			    DateTimeFieldType.monthOfYear()));
		    yearMonth.addMonth();
		} else {
		    yearMonth = new YearMonth(new YearMonthDay());
		}
	    } else {
		yearMonth.setYear(new Integer(year));
		yearMonth.setMonth(Month.valueOf(month));
	    }
	}
	if (yearMonth.getYear() < 2006) {
	    saveErrors(request, employee, yearMonth, "error.invalidPastDateNoData");
	    return null;
	}
	return yearMonth;
    }

    private void saveErrors(HttpServletRequest request, Employee employee, YearMonth yearMonth,
	    String message) {
	ActionMessages actionMessages = new ActionMessages();
	actionMessages.add("message", new ActionMessage(message));
	saveMessages(request, actionMessages);
	request.setAttribute("yearMonth", yearMonth);
	EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet();
	employeeWorkSheet.setEmployee(employee);
	request.setAttribute("employeeWorkSheet", employeeWorkSheet);
    }

    private void setEmployeeStatus(HttpServletRequest request, final Employee employee,
	    YearMonthDay beginDate, YearMonthDay endDate) {
	List<AssiduousnessStatusHistory> employeeStatusList = employee.getAssiduousness()
		.getStatusBetween(beginDate, endDate);
	Collections.sort(employeeStatusList, new BeanComparator("beginDate"));
	request.setAttribute("employeeStatusList", employeeStatusList);
    }
}