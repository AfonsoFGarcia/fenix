package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class WorkDaySheet implements Serializable {
    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");

    LocalDate date;

    WorkSchedule workSchedule;

    String workScheduleAcronym;

    Period balanceTime;

    Duration unjustifiedTime;

    Duration unjustifiedTimeWithoutBalanceDiscount;

    Duration complementaryWeeklyRest;

    Duration holidayRest;

    Duration weeklyRest;

    String notes;

    List<AssiduousnessRecord> assiduousnessRecords;

    String clockings;

    String clockingsToManagement;

    List<Leave> leaves;

    Timeline timeline;

    Boolean irregular;

    Duration balanceToCompensate;

    public WorkDaySheet() {
	setBalanceTime(Duration.ZERO.toPeriod());
	setUnjustifiedTime(Duration.ZERO);
	setBalanceToCompensate(Duration.ZERO);
    }

    public WorkDaySheet(LocalDate day, WorkSchedule workSchedule, List<AssiduousnessRecord> clockings, List<Leave> list) {
	setBalanceTime(Duration.ZERO.toPeriod());
	setUnjustifiedTime(Duration.ZERO);
	setBalanceToCompensate(Duration.ZERO);
	setDate(day);
	setWorkSchedule(workSchedule);
	setLeaves(list);
	setAssiduousnessRecords(clockings);
    }

    public Timeline getTimeline() {
	return timeline;
    }

    public void setTimeline(Timeline timeline) {
	this.timeline = timeline;
    }

    public Period getBalanceTime() {
	return balanceTime;
    }

    public void setBalanceTime(Period balanceTime) {
	this.balanceTime = balanceTime;
    }

    public LocalDate getDate() {
	return date;
    }

    public void setDate(LocalDate date) {
	this.date = date;
    }

    public String getNotes() {
	return notes == null ? "" : notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }

    public void addNote(String note) {
	if (notes != null && notes.length() != 0) {
	    this.notes = notes.concat(" / ");
	} else if (notes == null) {
	    notes = new String();
	}
	this.notes = notes.concat(note);
    }

    public Duration getUnjustifiedTime() {
	return unjustifiedTime;
    }

    public void setUnjustifiedTime(Duration unjustifiedTime) {
	this.unjustifiedTime = unjustifiedTime;
    }

    public String getWorkScheduleAcronym() {
	return workScheduleAcronym;
    }

    public void setWorkScheduleAcronym(String workScheduleAcronym) {
	this.workScheduleAcronym = workScheduleAcronym;
    }

    public String getDateFormatted() {
	if (getDate() == null) {
	    return "";
	}
	return getDate().toString("dd/MM/yyyy");
    }

    public Duration getBalanceToCompensate() {
	return balanceToCompensate;
    }

    public void setBalanceToCompensate(Duration balanceToCompensate) {
	this.balanceToCompensate = balanceToCompensate;
    }

    public String getBalanceTimeFormatted() {
	Period balancePeriod = getBalanceTime();
	StringBuilder result = new StringBuilder();
	result.append(balancePeriod.getHours());
	result.append(":");
	if (balancePeriod.getMinutes() > -10 && balancePeriod.getMinutes() < 10) {
	    result.append("0");
	}
	if (balancePeriod.getMinutes() < 0) {
	    result.append((-balancePeriod.getMinutes()));
	    if (!result.toString().startsWith("-")) {
		result = new StringBuilder("-").append(result);
	    }
	} else {
	    result.append(balancePeriod.getMinutes());
	}
	return result.toString();
    }

    public String getUnjustifiedTimeFormatted() {
	Period unjustifiedPeriod = getUnjustifiedTime().toPeriod();
	StringBuilder result = new StringBuilder();
	result.append(unjustifiedPeriod.getHours());
	result.append(":");
	if (unjustifiedPeriod.getMinutes() > -10 && unjustifiedPeriod.getMinutes() < 10) {
	    result.append("0");
	}

	result.append(unjustifiedPeriod.getMinutes());

	return result.toString();
    }

    public String getClockingsFormatted() {
	return clockings;
    }

    public String getClockingsFormattedToManagement() {
	return clockingsToManagement;
    }

    public String getWeekDay() {
	if (getDate() == null) {
	    return "";
	}
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale());
	return bundle.getString(WeekDay.fromJodaTimeToWeekDay(getDate().toDateTimeAtStartOfDay()).toString() + "_ACRONYM");
    }

    public void setAssiduousnessRecords(final List<AssiduousnessRecord> assiduousnessRecords) {
	this.assiduousnessRecords = assiduousnessRecords;
	final StringBuilder result = new StringBuilder();
	final StringBuilder resultToManagement = new StringBuilder();
	boolean isPreviousMissingClocking = false;
	if (assiduousnessRecords != null) {
	    for (final AssiduousnessRecord assiduousnessRecord : assiduousnessRecords) {
		final TimeOfDay timeOfDay = assiduousnessRecord.getDate().toTimeOfDay();
		if (assiduousnessRecord.isMissingClocking()) {
		    if (result.length() != 0) {
			result.append(", ");
			if (isPreviousMissingClocking) {
			    resultToManagement.append("</font></b></i>, <i><b><font color='#889900'>");
			} else {
			    resultToManagement.append(",<i><b><font color='#889900'> ");
			}
		    } else {
			resultToManagement.append("<i><b><font color='#889900'>");
		    }
		    resultToManagement.append(fmt.print(timeOfDay));
		    isPreviousMissingClocking = true;
		} else {
		    if (result.length() != 0) {
			result.append(", ");
			if (isPreviousMissingClocking) {
			    resultToManagement.append("</font></b></i>, ");
			} else {
			    resultToManagement.append(", ");
			}
		    }
		    resultToManagement.append(fmt.print(timeOfDay));
		    isPreviousMissingClocking = false;
		}
		result.append(fmt.print(timeOfDay));
	    }
	}
	if (isPreviousMissingClocking) {
	    resultToManagement.append("</font></b></i>");
	}
	clockings = " " + result.toString();
	clockingsToManagement = " " + resultToManagement.toString();
    }

    public List<AssiduousnessRecord> getAssiduousnessRecords() {
	return assiduousnessRecords;
    }

    public List<Leave> getLeaves() {
	if (leaves == null) {
	    setLeaves(new ArrayList<Leave>());
	}
	return leaves;
    }

    public void setLeaves(List<Leave> leaves) {
	this.leaves = leaves;
    }

    public void addLeaves(List<Leave> list) {
	getLeaves().addAll(list);
    }

    public WorkSchedule getWorkSchedule() {
	return workSchedule;
    }

    public void setWorkSchedule(WorkSchedule workSchedule) {
	this.workSchedule = workSchedule;
    }

    public Duration getComplementaryWeeklyRest() {
	if (complementaryWeeklyRest == null) {
	    return Duration.ZERO;
	}
	return complementaryWeeklyRest;
    }

    public void setComplementaryWeeklyRest(Duration complementaryWeeklyRest) {
	this.complementaryWeeklyRest = complementaryWeeklyRest;
    }

    public Duration getWeeklyRest() {
	if (weeklyRest == null) {
	    return Duration.ZERO;
	}
	return weeklyRest;
    }

    public void setWeeklyRest(Duration weeklyRest) {
	this.weeklyRest = weeklyRest;
    }

    public void discountBalanceLeaveInFixedPeriod(List<Leave> balanceLeaveList) {
	setUnjustifiedTimeWithoutBalanceDiscount(getUnjustifiedTime());
	Duration balance = Duration.ZERO;
	for (Leave balanceLeave : balanceLeaveList) {
	    balance = balance.plus(balanceLeave.getDuration());
	    if (balanceLeave.getJustificationMotive().getJustificationType() == JustificationType.HALF_MULTIPLE_MONTH_BALANCE) {
		balanceToCompensate = balanceToCompensate.plus(balanceLeave.getDuration());
	    }
	}
	Duration newFixedPeriodAbsence = getUnjustifiedTime().minus(balance);
	if (newFixedPeriodAbsence.isShorterThan(Duration.ZERO)) {
	    setUnjustifiedTime(Duration.ZERO);
	} else {
	    setUnjustifiedTime(newFixedPeriodAbsence);
	}
    }

    public void discountBalanceOcurrenceLeaveInFixedPeriod(List<Leave> balanceOcurrenceLeaveList) {
	Duration balance = Duration.ZERO;
	if (!balanceOcurrenceLeaveList.isEmpty()) {
	    balance = balance.plus(getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration());
	    balanceToCompensate = balanceToCompensate.plus(balance);
	}
	Duration newFixedPeriodAbsence = getUnjustifiedTime().minus(balance);
	if (newFixedPeriodAbsence.isShorterThan(Duration.ZERO)) {
	    setUnjustifiedTime(Duration.ZERO);
	} else {
	    setUnjustifiedTime(newFixedPeriodAbsence);
	}
    }

    public void discountBalance(List<Leave> halfOccurrenceLeaves) {
	Duration balance = Duration.ZERO;
	for (Leave halfOccurrenceLeave : halfOccurrenceLeaves) {
	    balance = balance.plus(halfOccurrenceLeave.getDuration());
	}
	Duration newFixedPeriodAbsence = getUnjustifiedTime().minus(balance);
	if (newFixedPeriodAbsence.isShorterThan(Duration.ZERO)) {
	    setUnjustifiedTime(Duration.ZERO);
	} else {
	    setUnjustifiedTime(newFixedPeriodAbsence);
	}
	Duration currentBalance = getBalanceTime().toDurationFrom(new LocalDate().toDateTimeAtStartOfDay());
	setBalanceTime(currentBalance.plus(balance).toPeriod());
    }

    public Duration getHolidayRest() {
	if (holidayRest == null) {
	    return Duration.ZERO;
	}
	return holidayRest;
    }

    public void setHolidayRest(Duration holidayRest) {
	this.holidayRest = holidayRest;
    }

    public Duration getUnjustifiedTimeWithoutBalanceDiscount() {
	return unjustifiedTimeWithoutBalanceDiscount;
    }

    public void setUnjustifiedTimeWithoutBalanceDiscount(Duration unjustifiedTimeWithoutBalanceDiscount) {
	this.unjustifiedTimeWithoutBalanceDiscount = unjustifiedTimeWithoutBalanceDiscount;
    }

    public Boolean getIrregular() {
	return irregular == null ? false : irregular;
    }

    public void setIrregular(Boolean irregular) {
	this.irregular = irregular;
    }

    public void setIrregularDay(Boolean irregular) {
	this.irregular = irregular;
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale());
	addNote(bundle.getString("label.irregular"));
    }

    public Duration getLeaveDuration(final LocalDate thisDay, final WorkSchedule workSchedule, final Leave leave) {
	Duration leaveDuration = Duration.ZERO;
	if (!getIrregular()) {
	    Duration overlapsDuration = Duration.ZERO;

	    Interval interval = workSchedule.getWorkScheduleType().getNormalWorkPeriod().getNotWorkingPeriod(thisDay);
	    if (interval != null
		    && (getTimeline() == null || ((!interval.contains(leave.getDate()) || getTimeline()
			    .hasWorkingPointBeforeLeave(leave)) && (!interval.contains(leave.getEndDate()) || getTimeline()
			    .hasWorkingPointAfterLeave(leave)))

		    )) {
		Interval overlaps = interval.overlap(leave.getTotalInterval());
		if (overlaps != null) {
		    overlapsDuration = overlaps.toDuration();
		}
	    }

	    if ((leave.getDuration().minus(overlapsDuration)).isLongerThan(workSchedule.getWorkScheduleType()
		    .getNormalWorkPeriod().getWorkPeriodDuration())) {
		leaveDuration = leaveDuration.plus(workSchedule.getWorkScheduleType().getNormalWorkPeriod()
			.getWorkPeriodDuration());
	    } else {
		leaveDuration = leaveDuration.plus(leave.getDuration().minus(overlapsDuration));
	    }
	}
	return leaveDuration;
    }

    public boolean hasLeaveType(JustificationType justificationType) {
	for (Leave leave : getLeaves()) {
	    if (leave.getJustificationMotive().getJustificationType().equals(justificationType)) {
		return true;
	    }
	}
	return false;
    }

    public void discountHalfOccurrence() {
	if (workSchedule.getWorkScheduleType().hasFixedWorkPeriod()) {
	    Duration newFixedPeriodAbsence = getUnjustifiedTime().minus(
		    workSchedule.getWorkScheduleType().getFixedWorkPeriod().getHalfWorkPeriodDuration());
	    if (newFixedPeriodAbsence.isShorterThan(Duration.ZERO)) {
		setUnjustifiedTime(Duration.ZERO);
	    } else {
		setUnjustifiedTime(newFixedPeriodAbsence);
	    }
	}
	Duration currentBalance = getBalanceTime().toDurationFrom(new YearMonthDay().toDateTimeAtMidnight());
	setBalanceTime(currentBalance.plus(workSchedule.getWorkScheduleType().getNormalWorkPeriod().getHalfWorkPeriodDuration())
		.toPeriod());
    }

}
