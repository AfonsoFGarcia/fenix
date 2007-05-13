package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Holiday;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonthJustification;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.util.DayType;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.DateMidnight;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;

public class ExportClosedExtraWorkMonth extends Service {

    private DecimalFormat monthFormat = new DecimalFormat("00");

    private DecimalFormat employeeNumberFormat = new DecimalFormat("000000");

    private DecimalFormat justificationCodeFormat = new DecimalFormat("000");

    private List<YearMonthDay> unjustifiedDays;

    private JustificationMotive a66JustificationMotive;

    private JustificationMotive unjustifiedJustificationMotive;

    public String run(ClosedMonth closedMonth) {
	YearMonthDay beginDate = new YearMonthDay().withField(DateTimeFieldType.year(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.year())).withField(
		DateTimeFieldType.monthOfYear(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear())).withField(
		DateTimeFieldType.dayOfMonth(), 1);
	YearMonthDay endDate = new YearMonthDay().withField(DateTimeFieldType.year(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.year())).withField(
		DateTimeFieldType.monthOfYear(),
		closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear())).withField(
		DateTimeFieldType.dayOfMonth(), beginDate.dayOfMonth().getMaximumValue());
	StringBuilder result = new StringBuilder();
	HashMap<Assiduousness, List<Leave>> allLeaves = getLeaves(beginDate, endDate);
	HashMap<Assiduousness, AssiduousnessClosedMonth> allAssiduousnessClosedMonths = getAssiduousnessClosedMonths(closedMonth);
	a66JustificationMotive = getA66JustificationMotive();
	unjustifiedJustificationMotive = getUnjustifiedJustificationMotive();
	for (Assiduousness assiduousness : rootDomainObject.getAssiduousnesss()) {
	    unjustifiedDays = new ArrayList<YearMonthDay>();
	    if (assiduousness.isStatusActive(beginDate, endDate)
		    && !isADISTEmployee(assiduousness, beginDate, endDate)) {
		result
			.append(getAssiduousnessMonthBalance(assiduousness,
				allLeaves.get(assiduousness), allAssiduousnessClosedMonths
					.get(assiduousness), closedMonth, beginDate, endDate));
	    }
	}

	// result.append(getExtraWorkMonthBalance(assiduousness,
	// allLeaves.get(assiduousness),
	// beginDate, endDate));
	//        
	return result.toString();
    }

    private String getAssiduousnessMonthBalance(Assiduousness assiduousness, List<Leave> leaves,
	    AssiduousnessClosedMonth assiduousnessClosedMonth, ClosedMonth closedMonth,
	    YearMonthDay beginDate, YearMonthDay endDate) {
	StringBuilder result = new StringBuilder();
	if (leaves != null && !leaves.isEmpty()) {
	    Collections.sort(leaves, AssiduousnessRecord.COMPARATOR_BY_DATE);
	    for (Leave leave : leaves) {
		if (leave.getJustificationMotive().getJustificationType().equals(
			JustificationType.OCCURRENCE)
			|| leave.getJustificationMotive().getJustificationType().equals(
				JustificationType.MULTIPLE_MONTH_BALANCE)) {
		    result.append(getLeaveLine(leave, beginDate, endDate));
		} else if (leave.getJustificationMotive().getJustificationType().equals(
			JustificationType.HALF_OCCURRENCE)
			|| leave.getJustificationMotive().getJustificationType().equals(
				JustificationType.HALF_MULTIPLE_MONTH_BALANCE)
			|| (leave.getJustificationMotive().getJustificationType().equals(
				JustificationType.TIME) && !leave.getJustificationMotive()
				.getAccumulate())) {
		    result.append(getHalfLeaveLine(leave, beginDate));
		}
	    }
	}
	HashMap<Integer, Duration> pastJustificationsDurations = assiduousnessClosedMonth
		.getPastJustificationsDurations();
	for (ClosedMonthJustification closedMonthJustification : assiduousnessClosedMonth
		.getClosedMonthJustifications()) {
	    if (closedMonthJustification.getJustificationMotive().getAccumulate()) {
		Duration pastDurationToDiscount = Duration.ZERO;
		Duration pastDuration = pastJustificationsDurations.get(closedMonthJustification
			.getJustificationMotive().getGiafCode(assiduousness, beginDate));
		int scheduleHours = assiduousness.getAverageWorkTimeDuration(beginDate, endDate)
			.toPeriod(PeriodType.dayTime()).getHours();
		if (pastDuration != null) {
		    Period pastToDiscount = Period.hours(
			    pastDuration.toPeriod().getHours() % scheduleHours).withMinutes(
			    pastDuration.toPeriod().getMinutes());

		    pastDurationToDiscount = pastToDiscount.toDurationFrom(new DateMidnight());
		}
		pastDurationToDiscount = pastDurationToDiscount.plus(closedMonthJustification
			.getJustificationDuration());
		int justificationDays = pastDurationToDiscount.toPeriod().getHours() / scheduleHours;
		if (justificationDays != 0) {
		    result.append(getLeaveLine(assiduousness, closedMonthJustification
			    .getJustificationMotive(), closedMonth, justificationDays, leaves));
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
	    result.append(getLeaveLine(assiduousness, a66JustificationMotive, closedMonth,
		    A66ToDiscount, leaves));
	}

	if (unjustifiedToDiscount != 0) {
	    result.append(getLeaveLine(assiduousness, unjustifiedJustificationMotive, closedMonth,
		    unjustifiedToDiscount, leaves));
	}

	return result.toString();
    }

    private StringBuilder getLeaveLine(Assiduousness assiduousness,
	    JustificationMotive justificationMotive, ClosedMonth closedMonth, int justificationDays,
	    List<Leave> leaves) {
	List<YearMonthDay> daysToUnjustify = getJustificationDays(assiduousness, justificationMotive,
		closedMonth, justificationDays, leaves);
	StringBuilder line = new StringBuilder();
	for (YearMonthDay day : daysToUnjustify) {
	    Integer code = justificationMotive.getGiafCode(assiduousness, day);
	    if (code != 0) {
		line.append(day.getYear()).append(" ");
		line.append(monthFormat.format(day.getMonthOfYear() + 1)).append(" ");
		line
			.append(
				employeeNumberFormat.format(assiduousness.getEmployee()
					.getEmployeeNumber())).append(" ");
		line.append("F").append(" ");
		line.append(justificationCodeFormat.format(code)).append(" ");
		line.append(day).append(" ").append(day).append(" 100 100\r\n");
	    }
	}
	return line;
    }

    private List<YearMonthDay> getJustificationDays(Assiduousness assiduousness,
	    JustificationMotive justificationMotive, ClosedMonth closedMonth, int daysNumber,
	    List<Leave> leaves) {
	List<YearMonthDay> days = new ArrayList<YearMonthDay>();
	YearMonthDay day = new YearMonthDay(closedMonth.getClosedYearMonth().get(
		DateTimeFieldType.year()), closedMonth.getClosedYearMonth().get(
		DateTimeFieldType.monthOfYear()) + 1, 1).minusDays(1);
	while (daysNumber != 0) {
	    day = getPreviousWorkingDay(justificationMotive, assiduousness, day);
	    if (!unjustifiedDays.contains(day) && !existAnyLeaveForThisDay(leaves, day)) {
		days.add(day);
		daysNumber--;
	    }
	    day = day.minusDays(1);
	}
	unjustifiedDays.addAll(days);
	return days;
    }

    private boolean existAnyLeaveForThisDay(List<Leave> leaves, YearMonthDay day) {
	// TODO Auto-generated method stub
	if (leaves != null) {
	    for (Leave leave : leaves) {
		if (leave.getJustificationMotive().getJustificationType().equals(
			JustificationType.MULTIPLE_MONTH_BALANCE)
			|| leave.getJustificationMotive().getJustificationType().equals(
				JustificationType.OCCURRENCE)) {
		    if (leave.getTotalInterval().contains(day.toDateTimeAtMidnight())) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    private StringBuilder getLeaveLine(Leave leave, YearMonthDay beginDate, YearMonthDay endDate) {
	YearMonthDay start = beginDate;
	YearMonthDay end = endDate;
	if (leave.getDate().toYearMonthDay().isAfter(beginDate)) {
	    start = getNextWorkingDay(leave, leave.getDate().toYearMonthDay());
	}
	if (leave.getEndYearMonthDay() == null) {
	    end = getPreviousWorkingDay(leave.getJustificationMotive(), leave.getAssiduousness(), leave
		    .getDate().toYearMonthDay());
	} else if (leave.getEndYearMonthDay().isBefore(endDate)) {
	    end = getPreviousWorkingDay(leave.getJustificationMotive(), leave.getAssiduousness(), leave
		    .getEndYearMonthDay());
	}
	Integer code = leave.getJustificationMotive().getGiafCode(leave.getAssiduousness(), start);
	StringBuilder line = new StringBuilder();
	if (code != 0) {
	    line.append(start.getYear()).append(" ");
	    line.append(monthFormat.format(start.getMonthOfYear() + 1)).append(" ");
	    line.append(
		    employeeNumberFormat.format(leave.getAssiduousness().getEmployee()
			    .getEmployeeNumber())).append(" ");
	    line.append("F").append(" ");
	    line.append(justificationCodeFormat.format(code)).append(" ");
	    line.append(start).append(" ");
	    line.append(end).append(" ");
	    int days = Days.daysBetween(start, end).getDays() + 1;
	    line.append(days).append("00 ");
	    Interval interval = new Interval(start.toDateTimeAtMidnight().getMillis(), end
		    .toDateTimeAtMidnight().getMillis());
	    line.append(leave.getUtilDaysBetween(interval)).append("00\r\n");
	}
	return line;
    }

    private YearMonthDay getNextWorkingDay(Leave leave, YearMonthDay day) {
	if (leave.getJustificationMotive().getDayType().equals(DayType.WORKDAY)) {
	    List<Campus> campus = leave.getAssiduousness().getCampusForInterval(day, day);
	    WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(day.toDateTimeAtMidnight());
	    while (((campus.size() != 0 && Holiday.isHoliday(day, campus.get(0))) || (campus.size() == 0 && Holiday
		    .isHoliday(day)))
		    || dayOfWeek.equals(WeekDay.SATURDAY) || dayOfWeek.equals(WeekDay.SUNDAY)) {
		day = day.plusDays(1);
		dayOfWeek = WeekDay.fromJodaTimeToWeekDay(day.toDateTimeAtMidnight());
	    }
	}
	return day;
    }

    private YearMonthDay getPreviousWorkingDay(JustificationMotive justificationMotive,
	    Assiduousness assiduousness, YearMonthDay day) {
	if (justificationMotive.getDayType().equals(DayType.WORKDAY)) {
	    List<Campus> campus = assiduousness.getCampusForInterval(day, day);
	    WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(day.toDateTimeAtMidnight());
	    while (((campus.size() != 0 && Holiday.isHoliday(day, campus.get(0))) || (campus.size() == 0 && Holiday
		    .isHoliday(day)))
		    || dayOfWeek.equals(WeekDay.SATURDAY) || dayOfWeek.equals(WeekDay.SUNDAY)) {
		day = day.minusDays(1);
		dayOfWeek = WeekDay.fromJodaTimeToWeekDay(day.toDateTimeAtMidnight());
	    }
	}
	return day;
    }

    private StringBuilder getHalfLeaveLine(Leave leave, YearMonthDay beginDate) {
	Integer code = leave.getJustificationMotive().getGiafCode(leave.getAssiduousness(), beginDate);
	StringBuilder line = new StringBuilder();
	if (code != 0) {
	    line.append(beginDate.getYear()).append(" ");
	    line.append(monthFormat.format(beginDate.getMonthOfYear() + 1)).append(" ");
	    line.append(
		    employeeNumberFormat.format(leave.getAssiduousness().getEmployee()
			    .getEmployeeNumber())).append(" ");
	    line.append("F").append(" ");
	    line.append(justificationCodeFormat.format(code)).append(" ");
	    line.append(leave.getDate().toYearMonthDay()).append(" ");
	    line.append(leave.getDate().toYearMonthDay()).append(" ");
	    line.append("050 ").append("050\r\n");
	}
	return line;
    }

    private HashMap<Assiduousness, List<Leave>> getLeaves(YearMonthDay beginDate, YearMonthDay endDate) {
	HashMap<Assiduousness, List<Leave>> assiduousnessLeaves = new HashMap<Assiduousness, List<Leave>>();
	Interval interval = new Interval(beginDate.toDateTimeAtMidnight(),
		Assiduousness.defaultEndWorkDay.toDateTime(endDate.toDateMidnight()));
	for (AssiduousnessRecord assiduousnessRecord : rootDomainObject.getAssiduousnessRecords()) {
	    if (assiduousnessRecord.isLeave() && !assiduousnessRecord.isAnulated()) {
		Interval leaveInterval = new Interval(assiduousnessRecord.getDate(),
			((Leave) assiduousnessRecord).getEndDate().plusSeconds(1));
		if (leaveInterval.overlaps(interval)) {

		    List<Leave> leavesList = assiduousnessLeaves.get(assiduousnessRecord
			    .getAssiduousness());
		    if (leavesList == null) {
			leavesList = new ArrayList<Leave>();
		    }
		    leavesList.add((Leave) assiduousnessRecord);
		    assiduousnessLeaves.put(assiduousnessRecord.getAssiduousness(), leavesList);
		}
	    }
	}
	return assiduousnessLeaves;
    }

    private HashMap<Assiduousness, AssiduousnessClosedMonth> getAssiduousnessClosedMonths(
	    ClosedMonth closedMonth) {
	HashMap<Assiduousness, AssiduousnessClosedMonth> allAssiduousnessClosedMonths = new HashMap<Assiduousness, AssiduousnessClosedMonth>();
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : closedMonth
		.getAssiduousnessClosedMonths()) {
	    allAssiduousnessClosedMonths.put(assiduousnessClosedMonth.getAssiduousness(),
		    assiduousnessClosedMonth);
	}
	return allAssiduousnessClosedMonths;
    }

    private Boolean isADISTEmployee(Assiduousness assiduousness, YearMonthDay start, YearMonthDay end) {
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousness.getStatusBetween(
		start, end)) {
	    if (assiduousnessStatusHistory.getAssiduousnessStatus().getDescription().equals(
		    "Contratado pela ADIST")) {
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

}