package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.LeaveBean;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Holiday;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecordMonthIndex;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.util.DayType;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.DateMidnight;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ExportClosedExtraWorkMonth extends Service {
    private static final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd");

    private DecimalFormat monthFormat = new DecimalFormat("00");

    private DecimalFormat employeeNumberFormat = new DecimalFormat("000000");

    private DecimalFormat justificationCodeFormat = new DecimalFormat("000");

    private String fieldSeparator = ("\t");

    private List<LocalDate> unjustifiedDays;

    private JustificationMotive a66JustificationMotive;

    private JustificationMotive unjustifiedJustificationMotive;

    private JustificationMotive maternityJustificationMotive;

    private List<Assiduousness> maternityJustificationList;

    private static String maternityMovementCode = "508";

    private static String nightExtraWorkMovementCode = "130";

    private static String extraWorkVacationDaysMovementCode = "209";

    public static String extraWorkSundayMovementCode = "207";

    public static String extraWorkSaturdayMovementCode = "210";

    public static String extraWorkHolidayMovementCode = "212";

    public static String extraWorkWeekDayFirstLevelMovementCode = "200";

    public static String extraWorkWeekDaySecondLevelMovementCode = "201";

    public String run(ClosedMonth closedMonth) {
	return run(closedMonth, true, true);
    }

    public String run(ClosedMonth closedMonth, Boolean getWorkAbsences, Boolean getExtraWorkMovements) {
	LocalDate beginDate = new LocalDate().withField(DateTimeFieldType.year(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.year())).withField(DateTimeFieldType.monthOfYear(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear())).withField(DateTimeFieldType.dayOfMonth(),
		1);
	LocalDate endDate = new LocalDate().withField(DateTimeFieldType.year(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.year())).withField(DateTimeFieldType.monthOfYear(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear())).withField(DateTimeFieldType.dayOfMonth(),
		beginDate.dayOfMonth().getMaximumValue());
	LocalDate beginUnpaidLicenseDate = new LocalDate().withField(DateTimeFieldType.year(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.year())).withField(DateTimeFieldType.monthOfYear(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) + 1).withField(
		DateTimeFieldType.dayOfMonth(), 1);
	LocalDate endUnpaidLicenseDate = new LocalDate().withField(DateTimeFieldType.year(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.year())).withField(DateTimeFieldType.monthOfYear(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) + 1).withField(
		DateTimeFieldType.dayOfMonth(), beginUnpaidLicenseDate.dayOfMonth().getMaximumValue());

	Set<AssiduousnessRecord> allAssiduousnessRecord = AssiduousnessRecordMonthIndex.getAssiduousnessRecordBetweenDates(
		beginDate.toDateTimeAtStartOfDay(), endUnpaidLicenseDate.plusDays(1).toDateTimeAtStartOfDay());

	StringBuilder result = new StringBuilder();
	HashMap<Assiduousness, List<LeaveBean>> allLeaves = getLeaves(allAssiduousnessRecord, beginDate, endDate, false);

	HashMap<Assiduousness, List<LeaveBean>> allUnpaidLicenseLeaves = getLeaves(allAssiduousnessRecord,
		beginUnpaidLicenseDate, endUnpaidLicenseDate, true);
	a66JustificationMotive = getA66JustificationMotive();
	unjustifiedJustificationMotive = getUnjustifiedJustificationMotive();
	maternityJustificationMotive = getMaternityJustificationMotive();
	maternityJustificationList = new ArrayList<Assiduousness>();

	StringBuilder extraWorkResult = new StringBuilder();
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : closedMonth.getAssiduousnessClosedMonths()) {
	    unjustifiedDays = new ArrayList<LocalDate>();
	    if (!isADISTEmployee(assiduousnessClosedMonth)) {
		String lineResult = getAssiduousnessMonthBalance(assiduousnessClosedMonth, allLeaves.get(assiduousnessClosedMonth
			.getAssiduousnessStatusHistory().getAssiduousness()));
		if (getWorkAbsences) {
		    result.append(lineResult);
		    List<LeaveBean> leaveBeanList = allUnpaidLicenseLeaves.get(assiduousnessClosedMonth
			    .getAssiduousnessStatusHistory().getAssiduousness());
		    if (leaveBeanList != null) {
			for (LeaveBean leaveBean : leaveBeanList) {
			    result.append(getLeaveLine(assiduousnessClosedMonth, leaveBean, beginUnpaidLicenseDate,
				    endUnpaidLicenseDate, closedMonth.getClosedMonthFirstDay().plusMonths(1)));
			}
		    }
		}
		if (getExtraWorkMovements) {
		    extraWorkResult.append(getAssiduousnessExtraWork(assiduousnessClosedMonth));
		}
	    }
	}
	if (endDate.getDayOfMonth() != 30 && getExtraWorkMovements) {
	    StringBuilder maternityJustificationResult = new StringBuilder();
	    Integer daysNumber = endDate.getDayOfMonth() - 30;
	    for (Assiduousness assiduousness : maternityJustificationList) {
		maternityJustificationResult.append(getExtraWorkMovement(assiduousness, beginDate, endDate,
			maternityMovementCode, daysNumber));
	    }
	    extraWorkResult.append(maternityJustificationResult);
	}
	return result.append(extraWorkResult).toString();
    }

    private String getExtraWorkMovement(Assiduousness assiduousness, LocalDate beginDate, LocalDate endDate, String movementCode,
	    Integer daysNumber) {
	return getExtraWorkMovement(assiduousness, beginDate.plusMonths(1).getYear(), beginDate.plusMonths(1).getMonthOfYear(),
		beginDate, endDate, movementCode, daysNumber);
    }

    private String getExtraWorkMovement(Assiduousness assiduousness, int year, int month, LocalDate beginDate, LocalDate endDate,
	    String movementCode, Integer daysNumber) {
	StringBuilder result = new StringBuilder();
	result.append(year).append(fieldSeparator);
	result.append(monthFormat.format(month)).append(fieldSeparator);
	result.append(employeeNumberFormat.format(assiduousness.getEmployee().getEmployeeNumber())).append(fieldSeparator);
	result.append("M").append(fieldSeparator).append(movementCode).append(fieldSeparator);
	result.append(dateFormat.print(beginDate)).append(fieldSeparator);
	result.append(dateFormat.print(endDate)).append(fieldSeparator);
	result.append(employeeNumberFormat.format(daysNumber)).append("00").append(fieldSeparator);
	result.append(employeeNumberFormat.format(daysNumber)).append("00\r\n");
	return result.toString();
    }

    private String getAssiduousnessExtraWork(AssiduousnessClosedMonth assiduousnessClosedMonth) {
	StringBuilder result = new StringBuilder();
	Map<WorkScheduleType, Duration> nightWorkByWorkScheduleType = assiduousnessClosedMonth.getNightWorkByWorkScheduleType();
	for (WorkScheduleType workScheduleType : nightWorkByWorkScheduleType.keySet()) {
	    Duration duration = nightWorkByWorkScheduleType.get(workScheduleType);
	    if (workScheduleType.isNocturnal() && duration.isLongerThan(Duration.ZERO)) {
		long hours = Math.round((double) duration.toPeriod(PeriodType.minutes()).getMinutes() / (double) 60);
		result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness(),
			assiduousnessClosedMonth.getBeginDate(), assiduousnessClosedMonth.getEndDate(),
			nightExtraWorkMovementCode, (int) hours));
	    }
	}

	List<ExtraWorkRequest> extraWorkRequests = assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
		.getExtraWorkRequests(assiduousnessClosedMonth.getBeginDate());

	for (ExtraWorkRequest extraWorkRequest : extraWorkRequests) {
	    if (extraWorkRequest != null) {
		LocalDate begin = new LocalDate(extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.year()),
			extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.monthOfYear()), 1);
		LocalDate end = new LocalDate(extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.year()),
			extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.monthOfYear()), begin.dayOfMonth()
				.getMaximumValue());

		if (extraWorkRequest.getSundayHours() != null && extraWorkRequest.getSundayHours() != 0.0) {
		    result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			    .getAssiduousness(), assiduousnessClosedMonth.getBeginDate().plusMonths(1).getYear(),
			    assiduousnessClosedMonth.getBeginDate().plusMonths(1).getMonthOfYear(), begin, end,
			    extraWorkSundayMovementCode, extraWorkRequest.getSundayHours()));
		}
		Integer totalVacationDays = 0;
		if (extraWorkRequest.getNightVacationsDays() != null) {
		    totalVacationDays = totalVacationDays + extraWorkRequest.getNightVacationsDays();
		}
		if (extraWorkRequest.getNormalVacationsDays() != null) {
		    totalVacationDays = totalVacationDays + extraWorkRequest.getNormalVacationsDays();
		}

		if (totalVacationDays != 0) {
		    result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			    .getAssiduousness(), assiduousnessClosedMonth.getBeginDate().plusMonths(1).getYear(),
			    assiduousnessClosedMonth.getBeginDate().plusMonths(1).getMonthOfYear(), begin, end,
			    extraWorkVacationDaysMovementCode, extraWorkRequest.getNightVacationsDays()
				    + extraWorkRequest.getNormalVacationsDays()));
		}
		if (extraWorkRequest.getSaturdayHours() != null && extraWorkRequest.getSaturdayHours() != 0.0) {
		    result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			    .getAssiduousness(), assiduousnessClosedMonth.getBeginDate().plusMonths(1).getYear(),
			    assiduousnessClosedMonth.getBeginDate().plusMonths(1).getMonthOfYear(), begin, end,
			    extraWorkSaturdayMovementCode, extraWorkRequest.getSaturdayHours()));
		}
		if (extraWorkRequest.getHolidayHours() != null && extraWorkRequest.getHolidayHours() != 0.0) {
		    result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			    .getAssiduousness(), assiduousnessClosedMonth.getBeginDate().plusMonths(1).getYear(),
			    assiduousnessClosedMonth.getBeginDate().plusMonths(1).getMonthOfYear(), begin, end,
			    extraWorkHolidayMovementCode, extraWorkRequest.getHolidayHours()));
		}

		if (extraWorkRequest.getWorkdayFirstLevelHours() != null && extraWorkRequest.getWorkdayFirstLevelHours() != 0.0
			&& (!extraWorkRequest.getAddToVacations())) {
		    result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			    .getAssiduousness(), assiduousnessClosedMonth.getBeginDate().plusMonths(1).getYear(),
			    assiduousnessClosedMonth.getBeginDate().plusMonths(1).getMonthOfYear(), begin, end,
			    extraWorkWeekDayFirstLevelMovementCode, extraWorkRequest.getWorkdayFirstLevelHours()));
		}

		if (extraWorkRequest.getWorkdaySecondLevelHours() != null && extraWorkRequest.getWorkdaySecondLevelHours() != 0.0
			&& (!extraWorkRequest.getAddToVacations())) {
		    result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			    .getAssiduousness(), assiduousnessClosedMonth.getBeginDate().plusMonths(1).getYear(),
			    assiduousnessClosedMonth.getBeginDate().plusMonths(1).getMonthOfYear(), begin, end,
			    extraWorkWeekDaySecondLevelMovementCode, extraWorkRequest.getWorkdaySecondLevelHours()));
		}
	    }
	}
	return result.toString();
    }

    private String getAssiduousnessMonthBalance(AssiduousnessClosedMonth assiduousnessClosedMonth, List<LeaveBean> leavesBeans) {
	StringBuilder result = new StringBuilder();
	if (leavesBeans != null && !leavesBeans.isEmpty()) {
	    Collections.sort(leavesBeans, LeaveBean.COMPARATOR_BY_DATE);
	    for (LeaveBean leaveBean : leavesBeans) {
		if (leaveBean.getLeave().getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE)
			|| leaveBean.getLeave().getJustificationMotive().getJustificationType().equals(
				JustificationType.MULTIPLE_MONTH_BALANCE)) {
		    result.append(getLeaveLine(assiduousnessClosedMonth, leaveBean));
		} else if (leaveBean.getLeave().getJustificationMotive().getJustificationType().equals(
			JustificationType.HALF_OCCURRENCE_TIME)
			|| leaveBean.getLeave().getJustificationMotive().getJustificationType().equals(
				JustificationType.HALF_OCCURRENCE)
			|| leaveBean.getLeave().getJustificationMotive().getJustificationType().equals(
				JustificationType.HALF_MULTIPLE_MONTH_BALANCE)
			|| (leaveBean.getLeave().getJustificationMotive().getJustificationType().equals(JustificationType.TIME) && !leaveBean
				.getLeave().getJustificationMotive().getAccumulate())) {
		    result.append(getHalfLeaveLine(assiduousnessClosedMonth, leaveBean.getLeave()));
		}
	    }
	}
	HashMap<Integer, Duration> pastJustificationsDurations = assiduousnessClosedMonth.getPastJustificationsDurations();
	HashMap<Integer, Duration> closedMonthJustificationsMap = assiduousnessClosedMonth.getClosedMonthJustificationsMap();
	for (Integer giafCode : closedMonthJustificationsMap.keySet()) {
	    Duration pastDurationToDiscount = Duration.ZERO;
	    Duration pastDuration = pastJustificationsDurations.get(giafCode);
	    int scheduleHours = assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
		    .getAverageWorkTimeDuration(assiduousnessClosedMonth.getBeginDate(), assiduousnessClosedMonth.getEndDate())
		    .toPeriod(PeriodType.dayTime()).getHours();
	    if (pastDuration != null) {
		Period pastToDiscount = Period.hours(pastDuration.toPeriod().getHours() % scheduleHours).withMinutes(
			pastDuration.toPeriod().getMinutes());

		pastDurationToDiscount = pastToDiscount.toDurationFrom(new DateMidnight());
	    }
	    pastDurationToDiscount = pastDurationToDiscount.plus(closedMonthJustificationsMap.get(giafCode));
	    int justificationDays = pastDurationToDiscount.toPeriod().getHours() / scheduleHours;
	    if (justificationDays != 0) {
		JustificationMotive justificationMotive = JustificationMotive.getJustificationMotiveByGiafCode(giafCode,
			assiduousnessClosedMonth.getAssiduousnessStatusHistory());
		if (justificationMotive != null) {
		    result.append(getLeaveLine(assiduousnessClosedMonth, justificationMotive, justificationDays, leavesBeans));
		}
	    }
	}

	AssiduousnessClosedMonth previousAssiduousnessClosedMonth = assiduousnessClosedMonth
		.getPreviousAssiduousnessClosedMonth();
	double previousA66 = 0;
	double previousUnjustified = 0;
	double previousNotCompleteA66 = 0;
	double previousNotCompleteUnjustified = 0;
	if (previousAssiduousnessClosedMonth != null) {
	    previousA66 = previousAssiduousnessClosedMonth.getAccumulatedArticle66();
	    previousNotCompleteA66 = previousA66 - (int) previousA66;
	    previousUnjustified = previousAssiduousnessClosedMonth.getAccumulatedUnjustified();
	    previousNotCompleteUnjustified = previousUnjustified - (int) previousUnjustified;
	}
	int A66ToDiscount = (int) ((assiduousnessClosedMonth.getAccumulatedArticle66() - previousA66) + previousNotCompleteA66);
	int unjustifiedToDiscount = (int) ((assiduousnessClosedMonth.getAccumulatedUnjustified() - previousUnjustified) + previousNotCompleteUnjustified);

	if (A66ToDiscount != 0) {
	    result.append(getLeaveLine(assiduousnessClosedMonth, a66JustificationMotive, A66ToDiscount, leavesBeans));
	}

	if (unjustifiedToDiscount != 0) {
	    result.append(getLeaveLine(assiduousnessClosedMonth, unjustifiedJustificationMotive, unjustifiedToDiscount,
		    leavesBeans));
	}

	return result.toString();
    }

    private StringBuilder getLeaveLine(AssiduousnessClosedMonth assiduousnessClosedMonth,
	    JustificationMotive justificationMotive, int justificationDays, List<LeaveBean> leavesBeans) {
	List<LocalDate> daysToUnjustify = getJustificationDays(assiduousnessClosedMonth, justificationMotive, justificationDays,
		leavesBeans);
	StringBuilder line = new StringBuilder();
	for (LocalDate day : daysToUnjustify) {
	    LocalDate nextMontDate = day.plusMonths(1);
	    Integer code = justificationMotive.getGiafCode(assiduousnessClosedMonth.getAssiduousnessStatusHistory());
	    if (code != 0) {
		line.append(nextMontDate.getYear()).append(fieldSeparator);
		line.append(monthFormat.format(nextMontDate.getMonthOfYear())).append(fieldSeparator);
		line.append(
			employeeNumberFormat.format(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
				.getEmployee().getEmployeeNumber())).append(fieldSeparator);
		line.append("F").append(fieldSeparator);
		line.append(justificationCodeFormat.format(code)).append(fieldSeparator);
		line.append(dateFormat.print(day)).append(fieldSeparator).append(dateFormat.print(day)).append(fieldSeparator)
			.append("100").append(fieldSeparator).append("100\r\n");
	    }
	}
	return line;
    }

    private List<LocalDate> getJustificationDays(AssiduousnessClosedMonth assiduousnessClosedMonth,
	    JustificationMotive justificationMotive, int daysNumber, List<LeaveBean> leavesBeans) {
	List<LocalDate> days = new ArrayList<LocalDate>();
	YearMonth yearMonth = new YearMonth(assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth());
	yearMonth.addMonth();
	LocalDate day = new LocalDate(yearMonth.getYear(), yearMonth.getMonth().getNumberOfMonth(), 1).minusDays(1);
	while (daysNumber != 0) {
	    day = getPreviousWorkingDay(justificationMotive, assiduousnessClosedMonth.getAssiduousnessStatusHistory()
		    .getAssiduousness(), day, true);
	    if (!unjustifiedDays.contains(day) && !existAnyLeaveForThisDay(leavesBeans, day)) {
		days.add(day);
		daysNumber--;
	    }
	    day = day.minusDays(1);
	}
	unjustifiedDays.addAll(days);
	return days;
    }

    private boolean existAnyLeaveForThisDay(List<LeaveBean> leavesBeans, LocalDate day) {
	if (leavesBeans != null) {
	    for (LeaveBean leaveBean : leavesBeans) {
		if (leaveBean.getLeave().getJustificationMotive().getJustificationType().equals(
			JustificationType.MULTIPLE_MONTH_BALANCE)
			|| leaveBean.getLeave().getJustificationMotive().getJustificationType().equals(
				JustificationType.OCCURRENCE)) {
		    if (leaveBean.getLeave().getTotalInterval().contains(day.toDateTimeAtStartOfDay())) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    private StringBuilder getLeaveLine(AssiduousnessClosedMonth assiduousnessClosedMonth, LeaveBean leaveBean) {
	return getLeaveLine(assiduousnessClosedMonth, leaveBean, assiduousnessClosedMonth.getBeginDate(),
		assiduousnessClosedMonth.getEndDate(), null);
    }

    private StringBuilder getLeaveLine(AssiduousnessClosedMonth assiduousnessClosedMonth, LeaveBean leaveBean,
	    LocalDate beginDate, LocalDate endDate, LocalDate paymentMonth) {
	StringBuilder line = new StringBuilder();
	Interval interval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay().plusDays(1));
	if (leaveBean.getLeave().getTotalInterval().overlaps(interval)) {
	    LocalDate start = getNextWorkingDay(leaveBean.getLeave(), beginDate, false);
	    LocalDate end = endDate;
	    if (leaveBean.getDate().toLocalDate().isAfter(beginDate)) {
		start = getNextWorkingDay(leaveBean.getLeave(), leaveBean.getDate().toLocalDate(), false);
	    }
	    if (leaveBean.getEndLocalDate() == null) {
		end = getPreviousWorkingDay(leaveBean.getLeave().getJustificationMotive(), leaveBean.getLeave()
			.getAssiduousness(), leaveBean.getDate().toLocalDate(), false);
	    } else if (leaveBean.getEndLocalDate().isBefore(endDate)) {
		end = getPreviousWorkingDay(leaveBean.getLeave().getJustificationMotive(), leaveBean.getLeave()
			.getAssiduousness(), leaveBean.getEndLocalDate(), false);
	    }
	    Integer code = leaveBean.getLeave().getJustificationMotive().getGiafCode(
		    assiduousnessClosedMonth.getAssiduousnessStatusHistory());

	    if (code != 0) {
		if (leaveBean.getLeave().getJustificationMotive() == maternityJustificationMotive
			&& endDate.getDayOfMonth() != 30 && Days.daysBetween(start, end).getDays() + 1 == endDate.getDayOfMonth()) {
		    if (!maternityJustificationList.contains(leaveBean.getLeave().getAssiduousness())
			    && !isContractedEmployee(leaveBean.getLeave().getAssiduousness(), start, end)) {
			maternityJustificationList.add(leaveBean.getLeave().getAssiduousness());
		    }
		}
		if (paymentMonth == null) {
		    paymentMonth = start.plusMonths(1);
		}
		line.append(paymentMonth.getYear()).append(fieldSeparator);
		line.append(monthFormat.format(paymentMonth.getMonthOfYear())).append(fieldSeparator);
		line.append(
			employeeNumberFormat.format(leaveBean.getLeave().getAssiduousness().getEmployee().getEmployeeNumber()))
			.append(fieldSeparator);
		line.append("F").append(fieldSeparator);
		line.append(justificationCodeFormat.format(code)).append(fieldSeparator);
		line.append(dateFormat.print(start)).append(fieldSeparator);
		line.append(dateFormat.print(end)).append(fieldSeparator);
		int days = Days.daysBetween(start, end).getDays() + 1;
		line.append(days).append("00").append(fieldSeparator);
		interval = new Interval(start.toDateTimeAtStartOfDay().getMillis(), end.toDateTimeAtStartOfDay().getMillis());
		line.append(leaveBean.getLeave().getUtilDaysBetween(interval)).append("00\r\n");
	    }
	}
	return line;
    }

    private LocalDate getNextWorkingDay(Leave leave, LocalDate day, boolean putOnlyWorkingDays) {
	if (leave.getJustificationMotive().getDayType().equals(DayType.WORKDAY) || putOnlyWorkingDays) {
	    List<Campus> campus = leave.getAssiduousness().getCampusForInterval(day, day);
	    WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(day.toDateTimeAtStartOfDay());
	    while (((campus.size() != 0 && Holiday.isHoliday(day, campus.get(0))) || (campus.size() == 0 && Holiday
		    .isHoliday(day)))
		    || dayOfWeek.equals(WeekDay.SATURDAY) || dayOfWeek.equals(WeekDay.SUNDAY)) {
		day = day.plusDays(1);
		dayOfWeek = WeekDay.fromJodaTimeToWeekDay(day.toDateTimeAtStartOfDay());
	    }
	}
	return day;
    }

    private LocalDate getPreviousWorkingDay(JustificationMotive justificationMotive, Assiduousness assiduousness, LocalDate day,
	    boolean putOnlyWorkingDays) {
	if (justificationMotive.getDayType().equals(DayType.WORKDAY) || putOnlyWorkingDays) {
	    List<Campus> campus = assiduousness.getCampusForInterval(day, day);
	    WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(day.toDateTimeAtStartOfDay());
	    while (((campus.size() != 0 && Holiday.isHoliday(day, campus.get(0))) || (campus.size() == 0 && Holiday
		    .isHoliday(day)))
		    || dayOfWeek.equals(WeekDay.SATURDAY) || dayOfWeek.equals(WeekDay.SUNDAY)) {
		day = day.minusDays(1);
		dayOfWeek = WeekDay.fromJodaTimeToWeekDay(day.toDateTimeAtStartOfDay());
	    }
	}
	return day;
    }

    private StringBuilder getHalfLeaveLine(AssiduousnessClosedMonth assiduousnessClosedMonth, Leave leave) {
	Integer code = leave.getJustificationMotive().getGiafCode(assiduousnessClosedMonth.getAssiduousnessStatusHistory());
	StringBuilder line = new StringBuilder();
	if (code != 0) {
	    LocalDate nextMontDate = assiduousnessClosedMonth.getBeginDate().plusMonths(1);
	    line.append(nextMontDate.getYear()).append(fieldSeparator);
	    line.append(monthFormat.format(nextMontDate.getMonthOfYear())).append(fieldSeparator);
	    line.append(employeeNumberFormat.format(leave.getAssiduousness().getEmployee().getEmployeeNumber())).append(
		    fieldSeparator);
	    line.append("F").append(fieldSeparator);
	    line.append(justificationCodeFormat.format(code)).append(fieldSeparator);
	    line.append(dateFormat.print(leave.getDate().toLocalDate())).append(fieldSeparator);
	    line.append(dateFormat.print(leave.getDate().toLocalDate())).append(fieldSeparator);
	    line.append("050").append(fieldSeparator).append("050\r\n");
	}
	return line;
    }

    private HashMap<Assiduousness, List<LeaveBean>> getLeaves(Set<AssiduousnessRecord> assiduousnessRecords, LocalDate beginDate,
	    LocalDate endDate, Boolean unpaidLicenceLeaves) {
	HashMap<Assiduousness, List<Leave>> assiduousnessLeaves = new HashMap<Assiduousness, List<Leave>>();
	Interval interval = new Interval(beginDate.toDateTimeAtStartOfDay(), Assiduousness.defaultEndWorkDay.toDateTime(endDate
		.toDateMidnight()));
	for (AssiduousnessRecord assiduousnessRecord : assiduousnessRecords) {
	    if (assiduousnessRecord.isLeave() && !assiduousnessRecord.isAnulated()) {
		Leave leave = ((Leave) assiduousnessRecord);
		if ((!unpaidLicenceLeaves && (leave.getJustificationMotive().getJustificationGroup() == null || leave
			.getJustificationMotive().getJustificationGroup().equals(JustificationGroup.UNPAID_LICENCES) == unpaidLicenceLeaves))
			|| (unpaidLicenceLeaves && (leave.getJustificationMotive().getJustificationGroup() != null && leave
				.getJustificationMotive().getJustificationGroup().equals(JustificationGroup.UNPAID_LICENCES) == unpaidLicenceLeaves))) {
		    Interval leaveInterval = new Interval(assiduousnessRecord.getDate(), ((Leave) assiduousnessRecord)
			    .getEndDate().plusSeconds(1));
		    if (leaveInterval.overlaps(interval)) {

			List<Leave> leavesList = assiduousnessLeaves.get(assiduousnessRecord.getAssiduousness());
			if (leavesList == null) {
			    leavesList = new ArrayList<Leave>();
			}
			leavesList.add(leave);
			assiduousnessLeaves.put(assiduousnessRecord.getAssiduousness(), leavesList);
		    }
		}
	    }
	}

	HashMap<Assiduousness, List<LeaveBean>> finalAssiduousnessLeaves = new HashMap<Assiduousness, List<LeaveBean>>();
	for (Assiduousness assiduousness : assiduousnessLeaves.keySet()) {
	    finalAssiduousnessLeaves.put(assiduousness, joinLeaves(assiduousnessLeaves.get(assiduousness)));

	}
	return finalAssiduousnessLeaves;
    }

    private List<LeaveBean> joinLeaves(List<Leave> leaves) {
	if (leaves != null) {
	    List<LeaveBean> leaveBeanList = new ArrayList<LeaveBean>();
	    Map<JustificationMotive, List<Leave>> leavesByMotive = new HashMap<JustificationMotive, List<Leave>>();
	    for (Leave leave : leaves) {
		if (leave.getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE)
			|| leave.getJustificationMotive().getJustificationType().equals(JustificationType.MULTIPLE_MONTH_BALANCE)) {
		    if (leavesByMotive.get(leave.getJustificationMotive()) == null) {
			List<Leave> newList = new ArrayList<Leave>();
			newList.add(leave);
			leavesByMotive.put(leave.getJustificationMotive(), newList);
		    } else {
			leavesByMotive.get(leave.getJustificationMotive()).add(leave);
		    }
		}
	    }
	    for (JustificationMotive justificationMotive : leavesByMotive.keySet()) {
		List<Leave> leavesMotiveList = leavesByMotive.get(justificationMotive);
		Collections.sort(leavesMotiveList, AssiduousnessRecord.COMPARATOR_BY_DATE);
		Iterator<Leave> iterator = leavesMotiveList.iterator();
		Leave nextLeave = null;
		LeaveBean leaveBean = null;
		while (iterator.hasNext()) {
		    Leave leave = null;
		    if (nextLeave != null) {
			leave = nextLeave;
		    } else {
			leave = (Leave) iterator.next();
		    }
		    if (iterator.hasNext()) {
			nextLeave = (Leave) iterator.next();
		    }
		    if (nextLeave != null) {
			if (leave.getEndLocalDate().plusDays(1).isEqual(nextLeave.getDate().toLocalDate())) {
			    if (leaveBean == null) {
				leaveBean = new LeaveBean(leave, nextLeave);
			    } else {
				leaveBean.setEndLocalDate(nextLeave.getEndLocalDate());
			    }
			    leaves.remove(nextLeave);
			} else {
			    if (leaveBean == null) {
				leaveBeanList.add(new LeaveBean(leave));
			    } else {
				leaveBeanList.add(leaveBean);
				leaveBean = null;
			    }
			}
		    } else {
			if (leaveBean == null) {
			    leaveBeanList.add(new LeaveBean(leave));
			} else {
			    leaveBeanList.add(leaveBean);
			    leaveBean = null;
			}
		    }
		    leaves.remove(leave);
		}
		if (leaveBean != null) {
		    leaveBeanList.add(leaveBean);
		}
	    }
	    for (Leave leave : leaves) {
		leaveBeanList.add(new LeaveBean(leave));
	    }

	    return leaveBeanList;
	}
	return null;
    }

    private Boolean isADISTEmployee(AssiduousnessClosedMonth assiduousnessClosedMonth) {
	return (assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousnessStatus().getDescription()
		.equals("Contratado pela ADIST"));
    }

    private Boolean isContractedEmployee(Assiduousness assiduousness, LocalDate start, LocalDate end) {
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousness.getStatusBetween(start, end)) {
	    if (assiduousnessStatusHistory.getAssiduousnessStatus().getDescription().equals("Contrato a termo certo")) {
		return true;
	    }
	}
	return false;
    }

    private JustificationMotive getUnjustifiedJustificationMotive() {
	for (JustificationMotive justificationMotive : rootDomainObject.getJustificationMotives()) {
	    if (justificationMotive.getAcronym().equalsIgnoreCase("FINJUST")) {
		return justificationMotive;
	    }
	}
	return null;
    }

    private JustificationMotive getA66JustificationMotive() {
	for (JustificationMotive justificationMotive : rootDomainObject.getJustificationMotives()) {
	    if (justificationMotive.getAcronym().equalsIgnoreCase("A66")) {
		return justificationMotive;
	    }
	}
	return null;
    }

    public JustificationMotive getMaternityJustificationMotive() {
	for (JustificationMotive justificationMotive : rootDomainObject.getJustificationMotives()) {
	    if (justificationMotive.getAcronym().equalsIgnoreCase("LP25%")) {
		return justificationMotive;
	    }
	}
	return null;
    }
}