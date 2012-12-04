package net.sourceforge.fenixedu.presentationTier.Action.employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ReadAssiduousnessWorkSheet;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.ClockingsDaySheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeScheduleFactory;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.Clocking;
import net.sourceforge.fenixedu.domain.assiduousness.Justification;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.renderers.components.state.ViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(module = "employee", path = "/assiduousnessRecords", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "show-clockings", path = "/employee/showClockings.jsp", tileProperties = @Tile(title = "private.employee.assiduity.markings")),
	@Forward(name = "show-work-sheet", path = "/employee/showWorkSheet.jsp", tileProperties = @Tile(title = "private.employee.assiduity.entry")),
	@Forward(name = "show-employee-info", path = "/employee/showEmployeeInfo.jsp", tileProperties = @Tile(title = "private.employee.assiduity.hours")),
	@Forward(name = "show-justifications", path = "/employee/showJustifications.jsp", tileProperties = @Tile(title = "private.employee.assiduity.justifications")) })
public class AssiduousnessDispatchAction extends FenixDispatchAction {

    private final LocalDate firstMonth = new LocalDate(2006, 9, 1);
    private final LocalDate lastMonth = new LocalDate(2012, 01, 1);

    public ActionForward showEmployeeInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	final IUserView userView = UserView.getUser();
	Employee employee = userView.getPerson().getEmployee();
	if (employee == null) {
	    ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("message", new ActionMessage("error.invalidEmployee"));
	    saveMessages(request, actionMessages);
	    return mapping.getInputForward();
	}
	EmployeeScheduleFactory employeeScheduleFactory = new EmployeeScheduleFactory(employee, null, employee.getAssiduousness()
		.getCurrentSchedule());
	request.setAttribute("employeeScheduleBean", employeeScheduleFactory);
	request.setAttribute("employee", employee);
	return mapping.findForward("show-employee-info");
    }

    public ActionForward showClockings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	final IUserView userView = UserView.getUser();
	Employee employee = userView.getPerson().getEmployee();
	if (employee == null) {
	    ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("message", new ActionMessage("error.invalidEmployee"));
	    saveMessages(request, actionMessages);
	    return mapping.getInputForward();
	}
	YearMonth yearMonth = null;
	ViewState viewState = (ViewState) RenderUtils.getViewState();
	if (viewState != null) {
	    yearMonth = (YearMonth) viewState.getMetaObject().getObject();
	}
	ActionForward actionForward = verifyYearMonth("show-clockings", request, mapping, yearMonth);
	if (actionForward != null) {
	    request.setAttribute("employee", employee);
	    return actionForward;
	}

	LocalDate beginDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	LocalDate endDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, beginDate.dayOfMonth()
		.getMaximumValue());
	if (employee.getAssiduousness() != null) {
	    List<Clocking> clockings = employee.getAssiduousness().getClockings(beginDate, endDate);
	    Collections.sort(clockings, AssiduousnessRecord.COMPARATOR_BY_DATE);
	    HashMap<LocalDate, ClockingsDaySheet> clockingsDaySheetList = new HashMap<LocalDate, ClockingsDaySheet>();
	    for (Clocking clocking : clockings) {
		if (clockingsDaySheetList.containsKey(clocking.getDate().toLocalDate())) {
		    ClockingsDaySheet clockingsDaySheet = clockingsDaySheetList.get(clocking.getDate().toLocalDate());
		    clockingsDaySheet.addClocking(clocking);
		} else {
		    ClockingsDaySheet clockingsDaySheet = new ClockingsDaySheet();
		    clockingsDaySheet.setDate(clocking.getDate().toLocalDate());
		    clockingsDaySheet.addClocking(clocking);
		    clockingsDaySheetList.put(clocking.getDate().toLocalDate(), clockingsDaySheet);
		}
	    }

	    List<ClockingsDaySheet> orderedClockings = new ArrayList<ClockingsDaySheet>(clockingsDaySheetList.values());
	    Collections.sort(orderedClockings, new BeanComparator("date"));
	    request.setAttribute("clockings", orderedClockings);
	}
	request.setAttribute("yearMonth", yearMonth);
	request.setAttribute("employee", employee);
	return mapping.findForward("show-clockings");
    }

    public ActionForward showJustifications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	final IUserView userView = UserView.getUser();
	Employee employee = userView.getPerson().getEmployee();
	if (employee == null) {
	    ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("message", new ActionMessage("error.invalidEmployee"));
	    saveMessages(request, actionMessages);
	    return mapping.getInputForward();
	}
	YearMonth yearMonth = null;
	ViewState viewState = (ViewState) RenderUtils.getViewState();
	if (viewState != null) {
	    yearMonth = (YearMonth) viewState.getMetaObject().getObject();
	}
	ActionForward actionForward = verifyYearMonth("show-justifications", request, mapping, yearMonth);
	if (actionForward != null) {
	    request.setAttribute("employee", employee);
	    return actionForward;
	}

	if (employee.getAssiduousness() != null) {
	    LocalDate beginDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	    LocalDate endDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, beginDate.dayOfMonth()
		    .getMaximumValue());
	    List<Justification> justifications = new ArrayList<Justification>();
	    justifications.addAll(employee.getAssiduousness().getLeaves(beginDate, endDate));
	    justifications.addAll(employee.getAssiduousness().getMissingClockings(beginDate, endDate));
	    List<Justification> orderedJustifications = new ArrayList<Justification>(justifications);
	    Collections.sort(orderedJustifications, AssiduousnessRecord.COMPARATOR_BY_DATE);
	    request.setAttribute("justifications", orderedJustifications);
	}
	request.setAttribute("yearMonth", yearMonth);
	request.setAttribute("employee", employee);
	return mapping.findForward("show-justifications");
    }

    public ActionForward showWorkSheet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	final IUserView userView = UserView.getUser();
	Employee employee = userView.getPerson().getEmployee();
	if (employee == null) {
	    ActionMessages actionMessages = new ActionMessages();
	    actionMessages.add("message", new ActionMessage("error.invalidEmployee"));
	    saveMessages(request, actionMessages);
	    return mapping.getInputForward();
	}
	YearMonth yearMonth = null;
	ViewState viewState = (ViewState) RenderUtils.getViewState();
	if (viewState != null) {
	    yearMonth = (YearMonth) viewState.getMetaObject().getObject();
	}
	ActionForward actionForward = verifyYearMonth("show-work-sheet", request, mapping, yearMonth);
	if (actionForward != null) {
	    request.setAttribute("employeeWorkSheet", new EmployeeWorkSheet(employee));
	    return actionForward;
	}

	LocalDate beginDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	int endDay = beginDate.dayOfMonth().getMaximumValue();
	if (yearMonth.getYear() == new LocalDate().getYear()
		&& yearMonth.getMonth().ordinal() + 1 == new LocalDate().getMonthOfYear()) {
	    endDay = new LocalDate().getDayOfMonth();
	    request.setAttribute("displayCurrentDayNote", "true");
	}
	LocalDate endDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, endDay);

	EmployeeWorkSheet employeeWorkSheet = ReadAssiduousnessWorkSheet.run(employee.getAssiduousness(), beginDate, endDate);
	request.setAttribute("employeeWorkSheet", employeeWorkSheet);
	request.setAttribute("yearMonth", yearMonth);
	return mapping.findForward("show-work-sheet");
    }

    private ActionForward verifyYearMonth(String returnPath, HttpServletRequest request, ActionMapping mapping,
	    YearMonth yearMonth) {
	if (yearMonth == null) {
	    yearMonth = new YearMonth();
	    yearMonth.setYear(new LocalDate().getYear());
	    yearMonth.setMonth(Month.values()[new LocalDate().getMonthOfYear() - 1]);
	}

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	if (yearMonth.getYear() >= lastMonth.getYear()) {
	    saveErrors(request, yearMonth, "error.invalidDateAfter",
		    new Object[] { bundle.getString(Month.values()[lastMonth.getMonthOfYear() - 1].toString()),
			    new Integer(lastMonth.getYear()).toString() });
	    return mapping.findForward(returnPath);
	} else {

	    if ((yearMonth.getYear() < firstMonth.getYear())
		    || (yearMonth.getYear() == firstMonth.getYear() && yearMonth.getMonth().getNumberOfMonth() < firstMonth
			    .getMonthOfYear())) {
		saveErrors(request, yearMonth, "error.invalidDateBefore",
			new Object[] { bundle.getString(Month.values()[firstMonth.getMonthOfYear() - 1].toString()),
				new Integer(firstMonth.getYear()).toString() });
		return mapping.findForward(returnPath);
	    }
	}
	return null;
    }

    private void saveErrors(HttpServletRequest request, YearMonth yearMonth, String errorMsg, Object[] args) {
	ActionMessages actionMessages = getMessages(request);
	actionMessages.add("message", new ActionMessage(errorMsg, args));
	saveMessages(request, actionMessages);
	request.setAttribute("yearMonth", yearMonth);
    }

}