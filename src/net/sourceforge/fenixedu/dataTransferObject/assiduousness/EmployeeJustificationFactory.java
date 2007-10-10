package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.Anulation;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatus;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ContinuousSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.HalfTimeSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.Justification;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.MissingClocking;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkWeek;
import net.sourceforge.fenixedu.domain.assiduousness.util.AnulationState;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionMessage;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.Partial;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.chrono.GregorianChronology;

public abstract class EmployeeJustificationFactory implements Serializable, FactoryExecutor {

    public static Duration dayDuration = new Duration(86400000); // 24

    // hours

    public enum CorrectionType {
	JUSTIFICATION, REGULARIZATION;
    }

    public enum JustificationDayType {
	DAY, HALF_DAY, HOURS;
    }

    public enum WorkPeriodType {
	FIRST_PERIOD, SECOND_PERIOD;
    }

    private CorrectionType correctionType;

    private JustificationDayType justificationDayType;

    private YearMonthDay date;

    private DomainReference<Employee> employee;

    private JustificationType justificationType;

    private DomainReference<JustificationMotive> justificationMotive;

    private WorkWeek aplicableWeekDays;

    private YearMonthDay beginDate;

    private YearMonthDay endDate;

    private TimeOfDay beginTime;

    private TimeOfDay endTime;

    private String notes;

    private DomainReference<Employee> modifiedBy;

    private String year;

    private String month;

    private YearMonth yearMonth;

    private WorkPeriodType workPeriodType;

    public static class EmployeeJustificationFactoryCreator extends EmployeeJustificationFactory {

	public EmployeeJustificationFactoryCreator(Employee employee, YearMonthDay date,
		CorrectionType correctionType, YearMonth yearMonth) {
	    setEmployee(employee);
	    setDate(date);
	    setBeginDate(date);
	    setCorrectionType(correctionType);
	    setYearMonth(yearMonth);
	}

	public Object execute() {
	    ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
		    LanguageUtils.getLocale());
	    if (getCorrectionType() != null && getCorrectionType().equals(CorrectionType.JUSTIFICATION)) {
		if (getJustificationMotive() != null
			&& getJustificationMotive().getJustificationType() != null) {
		    final GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
		    if (getJustificationMotive().getJustificationType().equals(JustificationType.TIME)) {
			if (getBeginDate() == null) {
			    return new ActionMessage("errors.required", bundle.getString("label.date"));
			}
			if (ClosedMonth.isMonthClosed(getBeginDate())) {
			    return new ActionMessage("errors.datesInClosedMonth");
			}
			if (getBeginTime() == null) {
			    return new ActionMessage("errors.required", bundle
				    .getString("label.beginHour"));
			}
			if (getEndTime() == null) {
			    return new ActionMessage("errors.required", bundle
				    .getString("label.endHour"));
			}
			if (!getBeginTime().isBefore(getEndTime())) {
			    return new ActionMessage("error.invalidTimeInterval");
			}
			String actionMessage = canInsertTimeJustification(getEmployee()
				.getAssiduousness());
			if (actionMessage != null) {
			    return new ActionMessage(actionMessage);
			}
			new Leave(getEmployee().getAssiduousness(), getBeginDate().toDateTime(
				getBeginTime()), getDuration(getBeginTime(), getEndTime()),
				getJustificationMotive(), null, getNotes(), new DateTime(),
				getModifiedBy());
		    } else if (getJustificationMotive().getJustificationType().equals(
			    JustificationType.OCCURRENCE)
			    || getJustificationMotive().getJustificationType().equals(
				    JustificationType.MULTIPLE_MONTH_BALANCE)) {
			if (getBeginDate() == null) {
			    return new ActionMessage("errors.required", bundle
				    .getString("label.beginDate"));
			}

			Duration duration = Duration.ZERO;
			if (getEndDate() != null) {
			    if (getBeginDate().isAfter(getEndDate())) {
				return new ActionMessage("error.invalidDateInterval");
			    }
			    duration = getDuration(getBeginDate(), getEndDate());
			}
			if (isDateIntervalInClosedMonth(getBeginDate(), duration)) {
			    return new ActionMessage("errors.datesInClosedMonth");
			}

			if (!hasScheduleAndActive(getEmployee().getAssiduousness(), getBeginDate(),
				duration)) {
			    return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
			}
			if (isOverlapingOtherJustification(duration, null)) {
			    return new ActionMessage("errors.overlapingOtherJustification");
			}
			new Leave(getEmployee().getAssiduousness(), getBeginDate().toDateTime(
				new TimeOfDay(0, 0, 0, gregorianChronology)), duration,
				getJustificationMotive(), null, getNotes(), new DateTime(),
				getModifiedBy());
		    } else if (getJustificationMotive().getJustificationType().equals(
			    JustificationType.BALANCE)) {
			if (getBeginDate() == null) {
			    return new ActionMessage("errors.required", bundle.getString("label.date"));
			}
			if (ClosedMonth.isMonthClosed(getBeginDate())) {
			    return new ActionMessage("errors.datesInClosedMonth");
			}
			if (!hasScheduleAndActive(getEmployee().getAssiduousness(), getBeginDate(),
				dayDuration)) {
			    return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
			}
			Duration numberOfHours = getDuration(TimeOfDay.MIDNIGHT, getEndTime());
			if (numberOfHours == null) {
			    return new ActionMessage("errors.required", bundle
				    .getString("label.hoursNumber"));
			}
			if (!numberOfHours.isShorterThan(dayDuration)) {
			    return new ActionMessage("errors.numberOfHoursLongerThanDay");
			}
			if (isOverlapingAnotherBalanceLeave(getEmployee().getAssiduousness(),
				numberOfHours)) {
			    return new ActionMessage("errors.overlapingOtherJustification");
			}
			new Leave(getEmployee().getAssiduousness(), getBeginDate().toDateTime(
				new TimeOfDay(0, 0, 0, gregorianChronology)), numberOfHours,
				getJustificationMotive(), null, getNotes(), new DateTime(),
				getModifiedBy());
		    } else if (getJustificationMotive().getJustificationType().equals(
			    JustificationType.HALF_OCCURRENCE_TIME)
			    || getJustificationMotive().getJustificationType().equals(
				    JustificationType.HALF_MULTIPLE_MONTH_BALANCE)) {
			if (getBeginDate() == null) {
			    return new ActionMessage("errors.required", bundle.getString("label.date"));
			}
			if (ClosedMonth.isMonthClosed(getBeginDate())) {
			    return new ActionMessage("errors.datesInClosedMonth");
			}
			if (!hasScheduleAndActive(getEmployee().getAssiduousness(), getBeginDate(),
				dayDuration)) {
			    return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
			}
			if (hasMoreThanOneOfTheKind(getEmployee().getAssiduousness(), getBeginDate())) {
			    return new ActionMessage("errors.hasMoreThanOneOfTheKind");
			}

			Schedule schedule = getEmployee().getAssiduousness().getSchedule(getBeginDate());
			WorkSchedule workSchedule = schedule.workScheduleWithDate(getBeginDate());
			if (workSchedule == null) {
			    return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
			}
			DateTime beginDateTime = getBeginDate().toDateTime(
				workSchedule.getWorkScheduleType().getNormalWorkPeriod()
					.getFirstPeriod());
			Duration halfWorkScheduleDuration = workSchedule.getWorkScheduleType()
				.getNormalWorkPeriod().getFirstPeriodDuration();
			if (getWorkPeriodType().equals(WorkPeriodType.SECOND_PERIOD)) {
			    if (workSchedule.getWorkScheduleType().getNormalWorkPeriod()
				    .getSecondPeriod() == null) {
				return new ActionMessage("errors.workScheduleWithoutSecondPeriod");
			    }
			    beginDateTime = getBeginDate().toDateTime(
				    workSchedule.getWorkScheduleType().getNormalWorkPeriod()
					    .getSecondPeriod());
			    halfWorkScheduleDuration = workSchedule.getWorkScheduleType()
				    .getNormalWorkPeriod().getSecondPeriodDuration();

			}

			new Leave(getEmployee().getAssiduousness(), beginDateTime,
				halfWorkScheduleDuration, getJustificationMotive(), null, getNotes(),
				new DateTime(), getModifiedBy());
		    } else if (getJustificationMotive().getJustificationType().equals(
			    JustificationType.HALF_OCCURRENCE)) {
			if (getBeginDate() == null) {
			    return new ActionMessage("errors.required", bundle.getString("label.date"));
			}
			if (ClosedMonth.isMonthClosed(getBeginDate())) {
			    return new ActionMessage("errors.datesInClosedMonth");
			}
			if (!hasScheduleAndActive(getEmployee().getAssiduousness(), getBeginDate(),
				dayDuration)) {
			    return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
			}
			if (hasMoreThanOneOfTheKind(getEmployee().getAssiduousness(), getBeginDate())) {
			    return new ActionMessage("errors.hasMoreThanOneOfTheKind");
			}

			Schedule schedule = getEmployee().getAssiduousness().getSchedule(getBeginDate());
			WorkSchedule workSchedule = schedule.workScheduleWithDate(getBeginDate());
			if (workSchedule == null) {
			    return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
			}

			new Leave(getEmployee().getAssiduousness(), getBeginDate()
				.toDateTimeAtMidnight(), null, getJustificationMotive(), null,
				getNotes(), new DateTime(), getModifiedBy());
		    } else {
			// error - regul
			return new ActionMessage("errors.error");
		    }
		} else {
		    return new ActionMessage("errors.required", bundle.getString("label.justification"));
		}
	    } else if (getCorrectionType() != null
		    && getCorrectionType().equals(CorrectionType.REGULARIZATION)) {
		if (getJustificationMotive() != null) {
		    if (getJustificationMotive().getJustificationType() == null) {
			if (getBeginDate() == null) {
			    return new ActionMessage("errors.required", bundle.getString("label.date"));
			}
			if (ClosedMonth.isMonthClosed(getBeginDate())) {
			    return new ActionMessage("errors.datesInClosedMonth");
			}
			if (getBeginTime() == null) {
			    return new ActionMessage("errors.required", bundle.getString("label.hour"));
			}
			if (isOverlapingAnotherAssiduousnessRecord()) {
			    return new ActionMessage(
				    "errors.regularizationOverlapingAnotherAssiduousnessRecord");
			}
			new MissingClocking(getEmployee().getAssiduousness(), getBeginDate().toDateTime(
				getBeginTime()), getJustificationMotive(), getModifiedBy());
		    } else {
			// erro - just
			return new ActionMessage("errors.error");
		    }
		} else {
		    return new ActionMessage("errors.required", bundle.getString("label.regularization"));
		}
	    }
	    return null;
	}
    }

    public static class EmployeeJustificationFactoryEditor extends EmployeeJustificationFactory {
	private DomainReference<Justification> justification;

	public EmployeeJustificationFactoryEditor(Justification justification, YearMonth yearMonth) {
	    setYearMonth(yearMonth);
	    setJustification(justification);
	    setEmployee(justification.getAssiduousness().getEmployee());
	    setBeginDate(justification.getDate().toYearMonthDay());
	    setJustificationMotive(justification.getJustificationMotive());
	    setNotes(justification.getNotes());
	    if (justification.isLeave()) {
		setCorrectionType(CorrectionType.JUSTIFICATION);
		setJustificationType(justification.getJustificationMotive().getJustificationType());
		setBeginTime(((Leave) justification).getDate().toTimeOfDay());
		setEndDate(((Leave) justification).getEndYearMonthDay());
		setEndTime(((Leave) justification).getEndTimeOfDay());

		if (justification.getJustificationMotive().getJustificationType().equals(
			JustificationType.HALF_OCCURRENCE_TIME)
			|| getJustificationMotive().getJustificationType().equals(
				JustificationType.HALF_MULTIPLE_MONTH_BALANCE)) {
		    setJustificationDayType(JustificationDayType.HALF_DAY);
		    Schedule schedule = getEmployee().getAssiduousness().getSchedule(getBeginDate());
		    WorkSchedule workSchedule = schedule.workScheduleWithDate(getBeginDate());
		    if (getBeginTime().equals(
			    workSchedule.getWorkScheduleType().getNormalWorkPeriod().getFirstPeriod())) {
			setWorkPeriodType(WorkPeriodType.FIRST_PERIOD);
		    } else {
			setWorkPeriodType(WorkPeriodType.SECOND_PERIOD);
		    }
		}

		if (JustificationType.getDayJustificationTypes().contains(
			justification.getJustificationMotive().getJustificationType())) {
		    setJustificationDayType(JustificationDayType.DAY);
		} else if (JustificationType.getHalfDayJustificationTypes().contains(
			justification.getJustificationMotive().getJustificationType())) {
		    setJustificationDayType(JustificationDayType.HALF_DAY);
		} else if (JustificationType.getHoursJustificationTypes().contains(
			justification.getJustificationMotive().getJustificationType())) {
		    setJustificationDayType(JustificationDayType.HOURS);
		}
	    } else {
		setCorrectionType(CorrectionType.REGULARIZATION);
		setBeginTime(justification.getDate().toTimeOfDay());
	    }
	}

	public Object execute() {
	    ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
		    LanguageUtils.getLocale());
	    if (getCorrectionType() != null && getCorrectionType().equals(CorrectionType.JUSTIFICATION)) {
		if (getJustificationMotive() != null) {
		    final GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
		    if (getJustificationMotive().getJustificationType().equals(JustificationType.TIME)) {
			if (getBeginDate() == null) {
			    return new ActionMessage("errors.required", bundle.getString("label.date"));
			}
			if (ClosedMonth.isMonthClosed(getBeginDate())) {
			    return new ActionMessage("errors.datesInClosedMonth");
			}
			if (getBeginTime() == null) {
			    return new ActionMessage("errors.required", bundle
				    .getString("label.beginHour"));
			}
			if (getEndTime() == null) {
			    return new ActionMessage("errors.required", bundle
				    .getString("label.endHour"));
			}
			if (!getBeginTime().isBefore(getEndTime())) {
			    return new ActionMessage("error.invalidTimeInterval");
			}
			String actionMessage = canInsertTimeJustification(getEmployee()
				.getAssiduousness());
			if (actionMessage != null) {
			    return new ActionMessage(actionMessage);
			}

			((Leave) getJustification()).modify(getBeginDate().toDateTime(getBeginTime()),
				getDuration(getBeginTime(), getEndTime()), getJustificationMotive(),
				null, getNotes(), getModifiedBy());
		    } else if (getJustificationMotive().getJustificationType().equals(
			    JustificationType.OCCURRENCE)
			    || getJustificationMotive().getJustificationType().equals(
				    JustificationType.MULTIPLE_MONTH_BALANCE)) {
			if (getBeginDate() == null) {
			    return new ActionMessage("errors.required", bundle
				    .getString("label.beginDate"));
			}
			Duration duration = Duration.ZERO;
			if (getEndDate() != null) {
			    if (getBeginDate().isAfter(getEndDate())) {
				return new ActionMessage("error.invalidDateInterval");
			    }
			    duration = getDuration(getBeginDate(), getEndDate());
			}
			// if (isDateIntervalInClosedMonth(getBeginDate(),
			// duration)) {
			// return new
			// ActionMessage("errors.datesInClosedMonth");
			// }
			if ((!getBeginDate().isEqual(getJustification().getDate().toYearMonthDay()))
				&& (ClosedMonth.isMonthClosed(getBeginDate()) || ClosedMonth
					.isMonthClosed(getJustification().getDate().toYearMonthDay()))) {
			    return new ActionMessage("errors.datesInClosedMonth");
			}

			if (getEndDate() != null && ClosedMonth.isMonthClosed(getEndDate())
				&& !ClosedMonth.getLastClosedYearMonthDay().equals(getEndDate())) {
			    return new ActionMessage("errors.datesInClosedMonth");
			}

			if (!hasScheduleAndActive(getEmployee().getAssiduousness(), getBeginDate(),
				duration)) {
			    return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
			}
			if (isOverlapingOtherJustification(duration, getJustification())) {
			    return new ActionMessage("errors.overlapingOtherJustification");
			}
			((Leave) getJustification()).modify(getBeginDate().toDateTime(
				new TimeOfDay(0, 0, 0, gregorianChronology)), duration,
				getJustificationMotive(), null, getNotes(), getModifiedBy());
		    } else if (getJustificationMotive().getJustificationType().equals(
			    JustificationType.BALANCE)) {
			if (getBeginDate() == null) {
			    return new ActionMessage("errors.required", bundle.getString("label.date"));
			}
			if (ClosedMonth.isMonthClosed(getBeginDate())) {
			    return new ActionMessage("errors.datesInClosedMonth");
			}
			if (!hasScheduleAndActive(getEmployee().getAssiduousness(), getBeginDate(),
				dayDuration)) {
			    return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
			}
			Duration numberOfHours = getDuration(TimeOfDay.MIDNIGHT, getEndTime());
			if (numberOfHours == null) {
			    return new ActionMessage("errors.required", bundle
				    .getString("label.hoursNumber"));
			}
			if (!numberOfHours.isShorterThan(dayDuration)) {
			    return new ActionMessage("errors.numberOfHoursLongerThanDay");
			}
			if (isOverlapingAnotherBalanceLeave(getEmployee().getAssiduousness(),
				numberOfHours, getJustification())) {
			    return new ActionMessage("errors.overlapingOtherJustification");
			}
			((Leave) getJustification()).modify(getBeginDate().toDateTime(
				new TimeOfDay(0, 0, 0, gregorianChronology)), numberOfHours,
				getJustificationMotive(), null, getNotes(), getModifiedBy());
		    } else if (getJustificationMotive().getJustificationType().equals(
			    JustificationType.HALF_OCCURRENCE_TIME)
			    || getJustificationMotive().getJustificationType().equals(
				    JustificationType.HALF_MULTIPLE_MONTH_BALANCE)) {
			if (getBeginDate() == null) {
			    return new ActionMessage("errors.required", bundle.getString("label.date"));
			}
			if (ClosedMonth.isMonthClosed(getBeginDate())) {
			    return new ActionMessage("errors.datesInClosedMonth");
			}
			if (!hasScheduleAndActive(getEmployee().getAssiduousness(), getBeginDate(),
				dayDuration)) {
			    return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
			}

			Schedule schedule = getEmployee().getAssiduousness().getSchedule(getBeginDate());
			WorkSchedule workSchedule = schedule.workScheduleWithDate(getBeginDate());
			if (workSchedule == null) {
			    return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
			}
			DateTime beginDateTime = getBeginDate().toDateTime(
				workSchedule.getWorkScheduleType().getNormalWorkPeriod()
					.getFirstPeriod());
			Duration halfWorkScheduleDuration = workSchedule.getWorkScheduleType()
				.getNormalWorkPeriod().getFirstPeriodDuration();
			if (getWorkPeriodType().equals(WorkPeriodType.SECOND_PERIOD)) {
			    if (workSchedule.getWorkScheduleType().getNormalWorkPeriod()
				    .getSecondPeriod() == null) {
				return new ActionMessage("errors.workScheduleWithoutSecondPeriod");
			    }
			    beginDateTime = getBeginDate().toDateTime(
				    workSchedule.getWorkScheduleType().getNormalWorkPeriod()
					    .getSecondPeriod());
			    halfWorkScheduleDuration = workSchedule.getWorkScheduleType()
				    .getNormalWorkPeriod().getSecondPeriodDuration();

			}

			((Leave) getJustification()).modify(beginDateTime, halfWorkScheduleDuration,
				getJustificationMotive(), null, getNotes(), getModifiedBy());
		    } else if (getJustificationMotive().getJustificationType().equals(
			    JustificationType.HALF_OCCURRENCE)) {
			if (getBeginDate() == null) {
			    return new ActionMessage("errors.required", bundle.getString("label.date"));
			}
			if (ClosedMonth.isMonthClosed(getBeginDate())) {
			    return new ActionMessage("errors.datesInClosedMonth");
			}
			if (!hasScheduleAndActive(getEmployee().getAssiduousness(), getBeginDate(),
				dayDuration)) {
			    return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
			}
			if (hasMoreThanOneOfTheKind(getEmployee().getAssiduousness(), getBeginDate())) {
			    return new ActionMessage("errors.hasMoreThanOneOfTheKind");
			}

			Schedule schedule = getEmployee().getAssiduousness().getSchedule(getBeginDate());
			WorkSchedule workSchedule = schedule.workScheduleWithDate(getBeginDate());
			if (workSchedule == null) {
			    return new ActionMessage("errors.employeeHasNoScheduleOrInactive");
			}

			((Leave) getJustification()).modify(getBeginDate().toDateTimeAtMidnight(), null,
				getJustificationMotive(), null, getNotes(), getModifiedBy());
		    } else {
			// error - regul
			return new ActionMessage("errors.error");
		    }
		} else {
		    return new ActionMessage("errors.required", bundle.getString("label.justification"));
		}
	    } else if (getCorrectionType() != null
		    && getCorrectionType().equals(CorrectionType.REGULARIZATION)) {
		if (getJustificationMotive() != null) {
		    if (getJustificationMotive().getJustificationType() == null) {
			if (getBeginDate() == null) {
			    return new ActionMessage("errors.required", bundle.getString("label.date"));
			}
			if (ClosedMonth.isMonthClosed(getBeginDate())) {
			    return new ActionMessage("errors.datesInClosedMonth");
			}
			if (getBeginTime() == null) {
			    return new ActionMessage("errors.required", bundle.getString("label.hour"));
			}

			if (isOverlapingAnotherAssiduousnessRecord()) {
			    return new ActionMessage(
				    "errors.regularizationOverlapingAnotherAssiduousnessRecord");
			}
			((MissingClocking) getJustification()).modify(getBeginDate().toDateTime(
				getBeginTime()), getJustificationMotive(), getModifiedBy());
		    } else {
			// erro - just
			return new ActionMessage("errors.error");
		    }
		} else {
		    return new ActionMessage("errors.required", bundle.getString("label.regularization"));
		}

	    }
	    return null;
	}

	public Justification getJustification() {
	    return justification == null ? null : justification.getObject();
	}

	public void setJustification(Justification justification) {
	    if (justification != null) {
		this.justification = new DomainReference<Justification>(justification);
	    }
	}
    }

    public static class EmployeeAnulateJustificationFactory extends EmployeeJustificationFactory {
	private DomainReference<Justification> justification;

	public EmployeeAnulateJustificationFactory(Justification justification, YearMonth yearMonth,
		Employee employee) {
	    setJustification(justification);
	    setModifiedBy(employee);
	    setYearMonth(yearMonth);
	}

	public Object execute() {
	    if (getJustification().getJustificationMotive().getJustificationType() != null
		    && (getJustification().getJustificationMotive().getJustificationType().equals(
			    JustificationType.OCCURRENCE) || getJustification().getJustificationMotive()
			    .getJustificationType().equals(JustificationType.MULTIPLE_MONTH_BALANCE))) {
		if (isDateIntervalInClosedMonth(getJustification().getDate().toYearMonthDay(),
			((Leave) getJustification()).getDuration())) {
		    return new ActionMessage("errors.cantDelete.datesInClosedMonth");
		}
	    } else {
		if (ClosedMonth.isMonthClosed(getJustification().getDate().toYearMonthDay())) {
		    return new ActionMessage("errors.cantDelete.datesInClosedMonth");
		}
	    }
	    if (getJustification().getAnulation() != null) {
		getJustification().getAnulation().setState(AnulationState.VALID);
	    } else {
		new Anulation(getJustification(), getModifiedBy());
	    }
	    return null;
	}

	public Justification getJustification() {
	    return justification == null ? null : justification.getObject();
	}

	public void setJustification(Justification justification) {
	    if (justification != null) {
		this.justification = new DomainReference<Justification>(justification);
	    }
	}
    }

    public static class SeveralEmployeeJustificationFactoryCreator extends EmployeeJustificationFactory {
	private DomainListReference<AssiduousnessStatus> assiduousnessStatus;

	public SeveralEmployeeJustificationFactoryCreator() {
	    setCorrectionType(CorrectionType.JUSTIFICATION);
	}

	public Object execute() {
	    ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources",
		    LanguageUtils.getLocale());
	    if (getJustificationMotive() != null
		    && getJustificationMotive().getJustificationType() != null) {
		final GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
		if (getJustificationMotive().getJustificationType().equals(JustificationType.TIME)) {
		    if (getBeginDate() == null) {
			return new ActionMessage("errors.required", bundle.getString("label.date"));
		    }
		    if (ClosedMonth.isMonthClosed(getBeginDate())) {
			return new ActionMessage("errors.datesInClosedMonth");
		    }
		    if (getBeginTime() == null) {
			return new ActionMessage("errors.required", bundle.getString("label.beginHour"));
		    }
		    if (getEndTime() == null) {
			return new ActionMessage("errors.required", bundle.getString("label.endHour"));
		    }
		    if (!getBeginTime().isBefore(getEndTime())) {
			return new ActionMessage("error.invalidTimeInterval");
		    }
		    String actionMessage = canInsertTimeJustification(getEmployee().getAssiduousness());
		    if (actionMessage != null) {
			return new ActionMessage(actionMessage);
		    }
		    createJustification(getBeginDate().toDateTime(getBeginTime()), getDuration(
			    getBeginTime(), getEndTime()), bundle);
		} else if (getJustificationMotive().getJustificationType().equals(
			JustificationType.OCCURRENCE)
			|| getJustificationMotive().getJustificationType().equals(
				JustificationType.MULTIPLE_MONTH_BALANCE)) {
		    if (getBeginDate() == null) {
			return new ActionMessage("errors.required", bundle.getString("label.beginDate"));
		    }

		    Duration duration = Duration.ZERO;
		    if (getEndDate() != null) {
			if (getBeginDate().isAfter(getEndDate())) {
			    return new ActionMessage("error.invalidDateInterval");
			}
			duration = getDuration(getBeginDate(), getEndDate());
		    }
		    if (isDateIntervalInClosedMonth(getBeginDate(), duration)) {
			return new ActionMessage("errors.datesInClosedMonth");
		    }

		    String result = createJustification(getBeginDate().toDateTime(
			    new TimeOfDay(0, 0, 0, gregorianChronology)), duration, bundle);
		    if (result != null) {
			return new ActionMessage("errors", result);
		    }
		} else if (getJustificationMotive().getJustificationType().equals(
			JustificationType.BALANCE)) {
		    if (getBeginDate() == null) {
			return new ActionMessage("errors.required", bundle.getString("label.date"));
		    }
		    if (ClosedMonth.isMonthClosed(getBeginDate())) {
			return new ActionMessage("errors.datesInClosedMonth");
		    }
		    Duration numberOfHours = getDuration(TimeOfDay.MIDNIGHT, getEndTime());
		    if (numberOfHours == null) {
			return new ActionMessage("errors.required", bundle
				.getString("label.hoursNumber"));
		    }
		    if (!numberOfHours.isShorterThan(dayDuration)) {
			return new ActionMessage("errors.numberOfHoursLongerThanDay");
		    }
		    String result = createJustification(getBeginDate().toDateTime(
			    new TimeOfDay(0, 0, 0, gregorianChronology)), numberOfHours, bundle);
		    if (result != null) {
			return new ActionMessage("errors", result);
		    }
		} else if (getJustificationMotive().getJustificationType().equals(
			JustificationType.HALF_OCCURRENCE_TIME)
			|| getJustificationMotive().getJustificationType().equals(
				JustificationType.HALF_MULTIPLE_MONTH_BALANCE)
			|| getJustificationMotive().getJustificationType().equals(
				JustificationType.HALF_OCCURRENCE)) {
		    if (getBeginDate() == null) {
			return new ActionMessage("errors.required", bundle.getString("label.date"));
		    }
		    if (ClosedMonth.isMonthClosed(getBeginDate())) {
			return new ActionMessage("errors.datesInClosedMonth");
		    }

		    String result = createJustification(getBeginDate().toDateTimeAtMidnight(),
			    dayDuration, bundle);
		    if (result != null) {
			return new ActionMessage("errors", result);
		    }
		} else {
		    // error - regul
		    return new ActionMessage("errors.error");
		}
	    } else {
		return new ActionMessage("errors.required", bundle.getString("label.justification"));
	    }
	    return null;
	}

	private String createJustification(DateTime dateTime, Duration duration, ResourceBundle bundle) {
	    List<Assiduousness> assisuousnesssList = new ArrayList<Assiduousness>();
	    if (getJustificationMotive().getJustificationType().equals(
		    JustificationType.HALF_OCCURRENCE_TIME)
		    || getJustificationMotive().getJustificationType().equals(
			    JustificationType.HALF_MULTIPLE_MONTH_BALANCE)
		    || getJustificationMotive().getJustificationType().equals(
			    JustificationType.HALF_OCCURRENCE)) {

		for (AssiduousnessRecord assiduousnessRecord : RootDomainObject.getInstance()
			.getAssiduousnessRecords()) {
		    if (assiduousnessRecord.isLeave()
			    && !assiduousnessRecord.isAnulated()
			    && ((Leave) assiduousnessRecord).getJustificationMotive()
				    .getJustificationType().equals(getJustificationType())
			    && ((Leave) assiduousnessRecord).getDate().toYearMonthDay().equals(
				    getBeginDate())) {
			assisuousnesssList.add(assiduousnessRecord.getAssiduousness());
		    }
		}
	    }
	    StringBuilder result = new StringBuilder();
	    YearMonthDay end = dateTime.plus(duration).toYearMonthDay();
	    List<Assiduousness> assiduousnessOrderedList = new ArrayList<Assiduousness>(RootDomainObject
		    .getInstance().getAssiduousnesss());
	    Collections.sort(assiduousnessOrderedList, new BeanComparator("employee.employeeNumber"));
	    for (Assiduousness assiduousness : assiduousnessOrderedList) {
		if (satisfiedStatus(assiduousness, dateTime.toYearMonthDay(), end)) {
		    if (getJustificationMotive().getJustificationType().equals(
			    JustificationType.OCCURRENCE)
			    || getJustificationMotive().getJustificationType().equals(
				    JustificationType.MULTIPLE_MONTH_BALANCE)) {
			if (!isActive(assiduousness, getBeginDate(), duration)) {
			    continue;
			}
			if (!hasScheduleAndActive(assiduousness, getBeginDate(), duration)) {
			    result.append(assiduousness.getEmployee().getEmployeeNumber());
			    result.append(" - ");
			    result.append(bundle.getString("errors.employeeHasNoSchedule"));
			    result.append("<br/>");
			    continue;
			}
			if (isOverlapingOtherJustification(duration, null)) {
			    result.append(assiduousness.getEmployee().getEmployeeNumber());
			    result.append(" - ");
			    result.append(bundle.getString("errors.overlapingOtherJustification"));
			    result.append("<br/>");
			    continue;
			}
		    } else if (getJustificationMotive().getJustificationType().equals(
			    JustificationType.TIME)
			    || getJustificationMotive().getJustificationType().equals(
				    JustificationType.BALANCE)
			    || getJustificationMotive().getJustificationType().equals(
				    JustificationType.HALF_OCCURRENCE_TIME)
			    || getJustificationMotive().getJustificationType().equals(
				    JustificationType.HALF_MULTIPLE_MONTH_BALANCE)
			    || getJustificationMotive().getJustificationType().equals(
				    JustificationType.HALF_OCCURRENCE)) {
			if (!isActive(assiduousness, getBeginDate(), duration)) {
			    continue;
			}
			if (!hasScheduleAndActive(assiduousness, getBeginDate(), duration)) {
			    result.append(assiduousness.getEmployee().getEmployeeNumber());
			    result.append(" - ");
			    result.append(bundle.getString("errors.employeeHasNoSchedule"));
			    result.append("<br/>");
			    continue;
			}
		    }
		    if (getJustificationMotive().getJustificationType().equals(JustificationType.TIME)) {

			String error = canInsertTimeJustification(assiduousness);
			if (error != null) {
			    result.append(assiduousness.getEmployee().getEmployeeNumber());
			    result.append(" - ");
			    result.append(bundle.getString(error));
			    result.append("<br/>");
			    continue;
			}
		    }
		    if (getJustificationMotive().getJustificationType().equals(
			    JustificationType.HALF_OCCURRENCE_TIME)
			    || getJustificationMotive().getJustificationType().equals(
				    JustificationType.HALF_MULTIPLE_MONTH_BALANCE)) {

			if (assisuousnesssList.contains(assiduousness)) {
			    result.append(assiduousness.getEmployee().getEmployeeNumber());
			    result.append(" - ");
			    result.append(bundle.getString("errors.hasMoreThanOneOfTheKind"));
			    result.append("<br/>");
			    continue;
			}

			Schedule schedule = assiduousness.getSchedule(getBeginDate());
			if (schedule == null) {
			    result.append(assiduousness.getEmployee().getEmployeeNumber());
			    result.append(" - ");
			    result.append(bundle.getString("errors.employeeHasNoScheduleOrInactive"));
			    result.append("<br/>");
			    continue;
			}
			WorkSchedule workSchedule = schedule.workScheduleWithDate(getBeginDate());
			if (workSchedule == null) {
			    result.append(assiduousness.getEmployee().getEmployeeNumber());
			    result.append(" - ");
			    result.append(bundle.getString("errors.employeeHasNoScheduleOrInactive"));
			    result.append("<br/>");
			    continue;
			}

			dateTime = getBeginDate().toDateTime(
				workSchedule.getWorkScheduleType().getNormalWorkPeriod()
					.getFirstPeriod());
			if ((workSchedule.getWorkScheduleType() instanceof HalfTimeSchedule)) {

			    if (((getWorkPeriodType().equals(WorkPeriodType.FIRST_PERIOD) && ((HalfTimeSchedule) workSchedule
				    .getWorkScheduleType()).isMorningSchedule()) || (getWorkPeriodType()
				    .equals(WorkPeriodType.SECOND_PERIOD) && !((HalfTimeSchedule) workSchedule
				    .getWorkScheduleType()).isMorningSchedule()))) {
				duration = workSchedule.getWorkScheduleType().getNormalWorkPeriod()
					.getWorkPeriodDuration();
			    } else {
				result.append(assiduousness.getEmployee().getEmployeeNumber());
				result.append(" - ");
				result.append(bundle
					.getString("errors.employeeHasPartTimeScheduleInOtherDayTime"));
				result.append("<br/>");
				continue;
			    }
			} else {
			    duration = workSchedule.getWorkScheduleType().getNormalWorkPeriod()
				    .getFirstPeriodDuration();
			    dateTime = getBeginDate().toDateTime(
				    workSchedule.getWorkScheduleType().getNormalWorkPeriod()
					    .getFirstPeriod());
			    if ((workSchedule.getWorkScheduleType() instanceof ContinuousSchedule)) {
				duration = new Duration(workSchedule.getWorkScheduleType()
					.getNormalWorkPeriod().getWorkPeriodDuration().getMillis() / 2);
			    }
			    if (getWorkPeriodType().equals(WorkPeriodType.SECOND_PERIOD)
				    && !(workSchedule.getWorkScheduleType() instanceof ContinuousSchedule)) {
				if (workSchedule.getWorkScheduleType().getNormalWorkPeriod()
					.getSecondPeriod() == null) {
				    result.append(assiduousness.getEmployee().getEmployeeNumber());
				    result.append(" - ");
				    result.append(bundle
					    .getString("errors.workScheduleWithoutSecondPeriod"));
				    result.append("<br/>");
				    continue;
				}
				dateTime = getBeginDate().toDateTime(
					workSchedule.getWorkScheduleType().getNormalWorkPeriod()
						.getSecondPeriod());
				duration = workSchedule.getWorkScheduleType().getNormalWorkPeriod()
					.getSecondPeriodDuration();
			    }
			}
		    } else if (getJustificationMotive().getJustificationType().equals(
			    JustificationType.HALF_OCCURRENCE)) {

			if (assisuousnesssList.contains(assiduousness)) {
			    result.append(assiduousness.getEmployee().getEmployeeNumber());
			    result.append(" - ");
			    result.append(bundle.getString("errors.hasMoreThanOneOfTheKind"));
			    result.append("<br/>");
			    continue;
			}

			Schedule schedule = assiduousness.getSchedule(getBeginDate());
			if (schedule == null) {
			    result.append(assiduousness.getEmployee().getEmployeeNumber());
			    result.append(" - ");
			    result.append(bundle.getString("errors.employeeHasNoScheduleOrInactive"));
			    result.append("<br/>");
			    continue;
			}
			duration = null;
		    }
		    if (getJustificationMotive().getJustificationType()
			    .equals(JustificationType.BALANCE)
			    && isOverlapingAnotherBalanceLeave(assiduousness, duration)) {
			result.append(assiduousness.getEmployee().getEmployeeNumber());
			result.append(" - ");
			result.append(bundle.getString("errors.overlapingOtherJustification"));
			result.append("<br/>");
		    }
		    new Leave(assiduousness, dateTime, duration, getJustificationMotive(), null,
			    getNotes(), new DateTime(), getModifiedBy());
		}
	    }
	    return result.toString();
	}

	private boolean satisfiedStatus(Assiduousness assiduousness, YearMonthDay begin, YearMonthDay end) {
	    if (getAssiduousnessStatus() != null && getAssiduousnessStatus().size() != 0) {
		List<AssiduousnessStatusHistory> assiduousnessStatusList = assiduousness
			.getStatusBetween(begin, end);
		for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusList) {
		    if (getAssiduousnessStatus().contains(
			    assiduousnessStatusHistory.getAssiduousnessStatus())) {
			return true;
		    }
		}
		return false;
	    }
	    return true;
	}

	public List<AssiduousnessStatus> getAssiduousnessStatus() {
	    return assiduousnessStatus;
	}

	public void setAssiduousnessStatus(List<AssiduousnessStatus> assiduousnessStatus) {
	    this.assiduousnessStatus = new DomainListReference<AssiduousnessStatus>(assiduousnessStatus);
	}

    }

    protected Duration getDuration(YearMonthDay begin, YearMonthDay end) {
	if (begin == null || end == null) {
	    return null;
	}
	return new Duration(begin.toDateMidnight(), end.toDateMidnight());
    }

    protected boolean isDateIntervalInClosedMonth(YearMonthDay day, Duration duration) {
	YearMonthDay endDate = (day.plus(duration.toPeriod()));
	Partial end = new Partial().with(DateTimeFieldType.monthOfYear(), endDate.getMonthOfYear())
		.with(DateTimeFieldType.year(), endDate.getYear());
	for (Partial yearMonth = new Partial().with(DateTimeFieldType.monthOfYear(),
		day.getMonthOfYear()).with(DateTimeFieldType.year(), day.getYear()); !yearMonth
		.isAfter(end); yearMonth = yearMonth.withFieldAdded(DurationFieldType.months(), 1)) {
	    if (ClosedMonth.isMonthClosed(yearMonth)) {
		return true;
	    }
	}
	return false;
    }

    protected boolean isOverlapingAnotherAssiduousnessRecord() {
	final List<Leave> leaves = getEmployee().getAssiduousness().getLeaves(getBeginDate(),
		getBeginDate());
	DateTime date = getBeginDate().toDateTime(getBeginTime());
	final List<AssiduousnessRecord> clockings = getEmployee().getAssiduousness()
		.getClockingsAndMissingClockings(date, date.plusMinutes(1));
	if (!leaves.isEmpty()) {
	    for (Leave leave : leaves) {
		if (leave.getJustificationMotive().getJustificationType().equals(
			JustificationType.OCCURRENCE)
			|| leave.getJustificationMotive().getJustificationType().equals(
				JustificationType.MULTIPLE_MONTH_BALANCE)) {
		    return true;
		} else if (leave.getJustificationMotive().getJustificationType().equals(
			JustificationType.TIME)
			&& leave.getTotalInterval().contains(getBeginDate().toDateTime(getBeginTime()))) {
		    return true;
		}
	    }
	}

	if (!clockings.isEmpty()) {
	    return true;
	}
	return false;
    }

    protected boolean isOverlapingAnotherBalanceLeave(Assiduousness assiduousness, Duration duration,
	    Justification justification) {
	final List<Leave> leaves = assiduousness.getLeaves(getBeginDate(), getBeginDate());
	if (!leaves.isEmpty()) {
	    for (Leave leave : leaves) {
		if ((justification == null || !justification.getIdInternal().equals(
			leave.getIdInternal()))
			&& leave.getJustificationMotive().getJustificationType().equals(
				JustificationType.BALANCE) && leave.getDuration().equals(duration)) {
		    return true;
		}
	    }
	}
	return false;
    }

    protected boolean isOverlapingAnotherBalanceLeave(Assiduousness assiduousness, Duration duration) {
	return isOverlapingAnotherBalanceLeave(assiduousness, duration, null);
    }

    protected String canInsertTimeJustification(Assiduousness assiduousness) {
	if (getBeginDate().equals(new YearMonthDay())) {
	    return "errors.cannotInsertTimeJustificationsInCurrentDay";
	}
	YearMonthDay lowerBeginDate = getBeginDate().minusDays(8);
	HashMap<YearMonthDay, WorkSchedule> workScheduleMap = new HashMap<YearMonthDay, WorkSchedule>();
	for (YearMonthDay thisDay = lowerBeginDate; thisDay.isBefore(getBeginDate().plusDays(1)); thisDay = thisDay
		.plusDays(1)) {
	    final Schedule schedule = assiduousness.getSchedule(thisDay);
	    if (schedule != null) {
		workScheduleMap.put(thisDay, schedule.workScheduleWithDate(thisDay));
	    } else {
		workScheduleMap.put(thisDay, null);
	    }
	}

	if (workScheduleMap.get(getBeginDate()) == null
		|| !assiduousness.isStatusActive(getBeginDate(), getBeginDate())) {
	    return "errors.employeeHasNoScheduleOrInactive";
	}

	DateTime init = lowerBeginDate.toDateTime(Assiduousness.defaultStartWorkDay);
	WorkSchedule beginWorkSchedule = workScheduleMap.get(lowerBeginDate);
	if (beginWorkSchedule != null) {
	    init = lowerBeginDate.toDateTime(beginWorkSchedule.getWorkScheduleType().getWorkTime());
	}

	DateTime end = getBeginDate().toDateTime(Assiduousness.defaultEndWorkDay);
	WorkSchedule endWorkSchedule = workScheduleMap.get(getBeginDate());
	if (endWorkSchedule != null) {
	    end = getBeginDate().toDateTime(endWorkSchedule.getWorkScheduleType().getWorkTime()).plus(
		    endWorkSchedule.getWorkScheduleType().getWorkTimeDuration());
	    if (endWorkSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
		end = end.plusDays(2);
	    }
	}

	final List<AssiduousnessRecord> clockings = assiduousness.getClockingsAndMissingClockings(init
		.minusDays(1), end);
	Collections.sort(clockings, AssiduousnessRecord.COMPARATOR_BY_DATE);
	HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap = new HashMap<YearMonthDay, List<AssiduousnessRecord>>();
	for (AssiduousnessRecord record : clockings) {
	    YearMonthDay clockDay = record.getDate().toYearMonthDay();
	    if (WorkSchedule.overlapsSchedule(record.getDate(), workScheduleMap) == 0) {
		if (clockingsMap.get(clockDay.minusDays(1)) != null
			&& clockingsMap.get(clockDay.minusDays(1)).size() % 2 != 0) {
		    clockDay = clockDay.minusDays(1);
		}
	    } else if (WorkSchedule.overlapsSchedule(record.getDate(), workScheduleMap) < 0) {
		clockDay = clockDay.minusDays(1);
	    }

	    List<AssiduousnessRecord> clocks = clockingsMap.get(clockDay);
	    if (clocks == null) {
		clocks = new ArrayList<AssiduousnessRecord>();
	    }
	    clocks.add(record);
	    clockingsMap.put(clockDay, clocks);
	}

	List<AssiduousnessRecord> clocks = clockingsMap.get(getBeginDate());
	if (clocks != null && clocks.size() % 2 != 0) {
	    return "errors.irregularClockings";
	}
	if (clocks != null) {
	    Collections.sort(clocks, AssiduousnessRecord.COMPARATOR_BY_DATE);
	    int before = 0;
	    int middle = 0;
	    for (AssiduousnessRecord assiduousnessRecord : clocks) {
		if (!assiduousnessRecord.getDateWithoutSeconds().toTimeOfDay().isAfter(getBeginTime())) {
		    before++;
		} else if (assiduousnessRecord.getDateWithoutSeconds().toTimeOfDay().isBefore(
			getEndTime())) {
		    middle++;
		} else {
		    break;
		}
	    }
	    if (before % 2 != 0 || middle != 0) {
		return "errors.timeJustificationOverlapsWorkPeriod";
	    }
	}
	return null;
    }

    protected boolean hasScheduleAndActive(Assiduousness assiduousness, YearMonthDay day,
	    Duration duration) {
	Period durationPeriod = duration.toPeriod(PeriodType.dayTime());
	YearMonthDay endDay = day.toDateTimeAtMidnight().plusDays(durationPeriod.getDays()).plusHours(
		durationPeriod.getHours()).plusMinutes(durationPeriod.getMinutes()).toYearMonthDay();

	if (!assiduousness.isStatusActive(day, endDay)) {
	    return false;
	}
	for (YearMonthDay thisday = day; !thisday.isAfter(endDay); thisday = thisday.plusDays(1)) {
	    Schedule schedule = assiduousness.getSchedule(thisday);
	    if (schedule != null) {
		if (schedule.workScheduleWithDate(thisday) != null) {
		    return true;
		}
	    }
	}
	return false;
    }

    protected boolean isActive(Assiduousness assiduousness, YearMonthDay day, Duration duration) {
	Period durationPeriod = duration.toPeriod(PeriodType.dayTime());
	YearMonthDay endDay = day.toDateTimeAtMidnight().plusDays(durationPeriod.getDays()).plusHours(
		durationPeriod.getHours()).plusMinutes(durationPeriod.getMinutes()).toYearMonthDay();
	return (assiduousness.isStatusActive(day, endDay));
    }

    protected boolean isOverlapingOtherJustification(Duration duration, Justification justitication) {
	List<Leave> leaves = getEmployee().getAssiduousness().getLeaves(getBeginDate(),
		getBeginDate().toDateTimeAtMidnight().plus(duration).toYearMonthDay());
	return !leaves.isEmpty()
		&& (justitication == null || !leaves.contains(justitication) || leaves.size() != 1);
    }

    protected Duration getDuration(TimeOfDay begin, TimeOfDay end) {
	if (begin == null || end == null) {
	    return null;
	}
	YearMonthDay now = new YearMonthDay();
	return new Duration(now.toDateTime(begin), now.toDateTime(end));
    }

    protected Duration getDuration(TimeOfDay time) {
	if (time == null) {
	    return null;
	}
	return new Duration(time.toDateTimeToday().getMillis());
    }

    public boolean isComplete() {
	if (getJustificationType() != null && getJustificationMotive() != null && getBeginDate() != null) {
	    return true;
	}
	return false;
    }

    public YearMonthDay getBeginDate() {
	return beginDate;
    }

    public void setBeginDate(YearMonthDay beginDate) {
	this.beginDate = beginDate;
    }

    public TimeOfDay getBeginTime() {
	return beginTime;
    }

    public void setBeginTime(TimeOfDay beginTime) {
	this.beginTime = beginTime;
    }

    public YearMonthDay getEndDate() {
	return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
	this.endDate = endDate;
    }

    public TimeOfDay getEndTime() {
	return endTime;
    }

    public void setEndTime(TimeOfDay endTime) {
	this.endTime = endTime;
    }

    public JustificationMotive getJustificationMotive() {
	return justificationMotive == null ? null : justificationMotive.getObject();
    }

    public void setJustificationMotive(JustificationMotive justificationMotive) {
	if (justificationMotive != null) {
	    this.justificationMotive = new DomainReference<JustificationMotive>(justificationMotive);
	}
    }

    public JustificationType getJustificationType() {
	return justificationType;
    }

    public void setJustificationType(JustificationType justificationType) {
	this.justificationType = justificationType;
    }

    public YearMonthDay getDate() {
	return date;
    }

    public void setDate(YearMonthDay date) {
	this.date = date;
    }

    public Employee getEmployee() {
	return employee == null ? null : employee.getObject();
    }

    public void setEmployee(Employee employee) {
	if (employee != null) {
	    this.employee = new DomainReference<Employee>(employee);
	}
    }

    public Employee getModifiedBy() {
	return modifiedBy == null ? null : modifiedBy.getObject();
    }

    public void setModifiedBy(Employee modifiedBy) {
	if (modifiedBy != null) {
	    this.modifiedBy = new DomainReference<Employee>(modifiedBy);
	}
    }

    public CorrectionType getCorrectionType() {
	return correctionType;
    }

    public void setCorrectionType(CorrectionType correctionType) {
	this.correctionType = correctionType;
    }

    public WorkWeek getAplicableWeekDays() {
	return aplicableWeekDays;
    }

    public void setAplicableWeekDays(WorkWeek aplicableWeekDays) {
	this.aplicableWeekDays = aplicableWeekDays;
    }

    public String getNotes() {
	return notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }

    public String getMonth() {
	return month;
    }

    public void setMonth(String month) {
	this.month = month;
    }

    public String getYear() {
	return year;
    }

    public void setYear(String year) {
	this.year = year;
    }

    public YearMonth getYearMonth() {
	if (yearMonth == null && getYear() != null && getMonth() != null) {
	    setYearMonth(new YearMonth(new Integer(getYear()), Month.valueOf(getMonth())));
	}
	return yearMonth;
    }

    public void setYearMonth(YearMonth yearMonth) {
	setYear(yearMonth.getYear().toString());
	setMonth(yearMonth.getMonth().getName());
	this.yearMonth = yearMonth;
    }

    public WorkPeriodType getWorkPeriodType() {
	return workPeriodType;
    }

    public void setWorkPeriodType(WorkPeriodType workPeriodType) {
	this.workPeriodType = workPeriodType;
    }

    public JustificationDayType getJustificationDayType() {
	return justificationDayType;
    }

    public void setJustificationDayType(JustificationDayType justificationDayType) {
	this.justificationDayType = justificationDayType;
    }

    protected boolean hasMoreThanOneOfTheKind(Assiduousness assiduousness, YearMonthDay date) {
	return !(assiduousness.getLeavesByType(date, date, getJustificationType())).isEmpty();
    }

}