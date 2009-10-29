package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkScheduleDaySheet;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

public class WorkSchedule extends WorkSchedule_Base {

    public WorkSchedule(WorkScheduleType workScheduleType, WorkWeek workWeek, Periodicity periodicity) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setWorkWeek(workWeek);
	setWorkScheduleType(workScheduleType);
	setPeriodicity(periodicity);
    }

    public boolean isDefinedInDate(LocalDate date, int weekNumber, int maxWorkWeek) {
	return (getWorkWeek().contains(date.toDateTimeAtStartOfDay()) && getPeriodicity().occur(weekNumber, maxWorkWeek));
    }

    public WorkDaySheet calculateWorkingPeriods(WorkDaySheet workDaySheet, List<Leave> timeLeaves) {
	Duration firstWorkPeriod = Duration.ZERO;
	Duration lastWorkPeriod = Duration.ZERO;
	TimeInterval mealInterval = null;
	WorkScheduleType wsType = getWorkScheduleType();
	TimePoint firstWorkTimePoint = workDaySheet.getTimeline().getFirstWorkTimePoint();
	TimePoint lastWorkTimePoint = workDaySheet.getTimeline().getLastWorkTimePoint();
	LocalTime firstClockingDate = firstWorkTimePoint.getTime();
	LocalTime lastClockingDate = lastWorkTimePoint.getTime();
	if (wsType.definedMeal()) {
	    mealInterval = workDaySheet.getTimeline().calculateMealBreakInterval(wsType.getMeal().getMealBreak());
	    if (mealInterval != null) {
		Duration lunchDiscount = wsType.checkMealDurationAccordingToRules(mealInterval,
			justificationInMealBreak(timeLeaves), workDaySheet.getTimeline(), firstWorkTimePoint, lastWorkTimePoint);
		if (lunchDiscount != null) {
		    firstWorkPeriod = workDaySheet.getTimeline()
			    .calculateWorkPeriodDuration(
				    new TimePoint(mealInterval.getStartTime(), false, null),
				    null,
				    new TimePoint(getWorkScheduleType().getClockingTime(), AttributeType.NULL),
				    new TimePoint(getWorkScheduleType().getClockingEndTime(), getWorkScheduleType()
					    .isClokingTimeNextDay(), AttributeType.NULL),
				    wsType.getMaximumContinuousWorkPeriod(), wsType);

		    if ((wsType.getNormalWorkPeriod()).isSecondWorkPeriodDefined()) {
			lastWorkPeriod = workDaySheet.getTimeline().calculateWorkPeriodDuration(
				null,
				new TimePoint(mealInterval.getEndTime(), false, null),
				new TimePoint(getWorkScheduleType().getClockingTime(), AttributeType.NULL),
				new TimePoint(getWorkScheduleType().getClockingEndTime(), getWorkScheduleType()
					.isClokingTimeNextDay(), AttributeType.NULL), wsType.getMaximumContinuousWorkPeriod(),
				wsType);
		    }
		    workDaySheet.setUnjustifiedTime(wsType.calculateFixedPeriodDuration(workDaySheet.getTimeline()));
		    workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(firstWorkPeriod.plus(lastWorkPeriod).minus(
			    lunchDiscount), getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration()));
		} else {
		    workDaySheet.setIrregularDay(true);
		    workDaySheet.setBalanceTime(Duration.ZERO.minus(wsType.getNormalWorkPeriod().getWorkPeriodDuration()));
		    if (wsType.getFixedWorkPeriod() != null) {
			workDaySheet.setUnjustifiedTime(wsType.getFixedWorkPeriod().getWorkPeriodDuration());
		    }
		}
	    } else { // o funcionario nao foi almocar so fez 1 periodo de
		// trabalho
		Duration workPeriod = workDaySheet.getTimeline().calculateWorkPeriodDuration(
			null,
			workDaySheet.getTimeline().getTimePoints().iterator().next(),
			new TimePoint(getWorkScheduleType().getClockingTime(), AttributeType.NULL),
			new TimePoint(getWorkScheduleType().getClockingEndTime(), getWorkScheduleType().isClokingTimeNextDay(),
				AttributeType.NULL), wsType.getMaximumContinuousWorkPeriod(), wsType);

		if (workPeriod.equals(Duration.ZERO)) {
		    workDaySheet.setUnjustifiedTime(wsType.calculateFixedPeriodDuration(workDaySheet.getTimeline()));
		    workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(workPeriod, getWorkScheduleType()
			    .getNormalWorkPeriod().getWorkPeriodDuration()));
		} else if (wsType.getMeal().getMinimumMealBreakInterval().isEqual(Duration.ZERO)) {
		    // caso dos horarios de isencao de horario
		    if (firstClockingDate != null && wsType.getMeal().getMealBreak().contains(firstClockingDate, false)) {
			// funcionario entrou no intervalo de almoco
			// considera-se o periodo desde o inicio do intervalo de
			// almoco + desconto
			// obrigatorio de almoco
			// se funcionario entrar nesse periodo de tempo e'-lhe
			// descontado desde a entrada
			// ate' (inicio do intervalo de almoco + desconto
			// obrigatorio de almoco)
			LocalTime lunchEnd = wsType.getMeal().getLunchEnd();
			TimeInterval lunchTime = new TimeInterval(wsType.getMeal().getBeginMealBreak(), lunchEnd, false);

			Duration lunch = Duration.ZERO;
			if (lunchTime.contains(firstClockingDate, false) && !workPeriod.isEqual(Duration.ZERO)) {

			    lunch = new TimeInterval(firstClockingDate, lunchEnd, false).getDuration();
			}

			// pode ter de descontar tb no final
			if (lastClockingDate != null && wsType.getMeal().getMealBreak().contains(lastClockingDate, false)) {
			    lunchTime = wsType.getMeal().getEndOfMealBreakMinusDiscountInterval();
			    if (wsType.getMeal().getEndOfMealBreakMinusDiscountInterval().contains(lastClockingDate,
				    workDaySheet.getTimeline().getLastWorkTimePoint().isNextDay())) {
				lunch = lunch.plus(new TimeInterval(wsType.getMeal().getEndOfMealBreakMinusMealDiscount(),
					lastClockingDate, false).getDuration());
			    } else {
				// j� fez mais de 1 hora
				lunch = Duration.ZERO;
			    }

			}
			if (!wsType.getMeal().getMandatoryMealDiscount().isShorterThan(lunch)) {
			    workPeriod = workPeriod.minus(lunch);
			}

		    } else if (firstClockingDate != null && firstClockingDate.isBefore(wsType.getMeal().getBeginMealBreak())) {
			if (lastClockingDate != null
				&& (workDaySheet.getTimeline().getLastWorkTimePoint().isNextDay()
					|| lastClockingDate.isAfter(wsType.getMeal().getBeginMealBreak()) || lastClockingDate
					.isEqual(wsType.getMeal().getBeginMealBreak()))) {
			    if (wsType.getMeal().getEndOfMealBreakMinusDiscountInterval().contains(lastClockingDate,
				    workDaySheet.getTimeline().getLastWorkTimePoint().isNextDay())) {
				// descontar periodo de tempo desde fim da
				// refeicao menos periodo
				// obrigatorio de refeicao ate' 'a ultima
				// marcacao do funcionario
				workPeriod = workPeriod.minus((new TimeInterval(wsType.getMeal()
					.getEndOfMealBreakMinusMealDiscount(), lastClockingDate, false)).getDurationMillis());
			    } else if (workDaySheet.getTimeline().getLastWorkTimePoint().isNextDay()
				    || lastClockingDate.isAfter(wsType.getMeal().getEndMealBreak())) {
				workPeriod = workPeriod.minus(wsType.getMeal().getMandatoryMealDiscount());
			    }
			}
		    }
		    workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(workPeriod, getWorkScheduleType()
			    .getNormalWorkPeriod().getWorkPeriodDuration()));
		} else { // caso dos horarios em q o minimo de intervalo de
		    // almoco e' tipicamente de
		    // 15minutos
		    if (firstClockingDate != null) {
			if (wsType.getMeal().getMealBreak().contains(firstClockingDate, false)) {
			    // funcionario entrou no intervalo de almoco
			    Duration lunchDuration = new Duration(wsType.getMeal().getBeginMealBreak().toDateTimeToday(),
				    firstClockingDate.toDateTimeToday());
			    if (lastClockingDate != null && wsType.getMeal().getMealBreak().contains(lastClockingDate, false)) {
				lunchDuration = lunchDuration.plus(new Duration(lastClockingDate.toDateTimeToday(), wsType
					.getMeal().getEndMealBreak().toDateTimeToday()));
			    }

			    if (lunchDuration.isShorterThan(wsType.getMeal().getMandatoryMealDiscount())) {
				workPeriod = workPeriod.minus(wsType.getMeal().getMandatoryMealDiscount().minus(lunchDuration));
			    }
			    workDaySheet.setUnjustifiedTime(wsType.calculateFixedPeriodDuration(workDaySheet.getTimeline()));
			    workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(workPeriod, getWorkScheduleType()
				    .getNormalWorkPeriod().getWorkPeriodDuration()));

			    // primeiro clocking e' antes do periodo de almoco;
			    // ver se o ultimo clocking e' depois do periodo de
			    // almoco
			} else if (lastClockingDate != null
				&& (!wsType.getMeal().getMealBreak().contains(lastClockingDate, false))) {
			    if (justificationInMealBreak(timeLeaves)) {
				workPeriod = workPeriod.minus(wsType.getMeal().getMandatoryMealDiscount());
				workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(workPeriod, getWorkScheduleType()
					.getNormalWorkPeriod().getWorkPeriodDuration()));
				workDaySheet.setUnjustifiedTime(wsType.calculateFixedPeriodDuration(workDaySheet.getTimeline()));
			    } else {
				if (firstClockingDate.isBefore(wsType.getMeal().getBeginMealBreak())
					&& new TimePoint(wsType.getMeal().getEndMealBreak(), (Attributes) null)
						.isBefore(lastWorkTimePoint)) {
				    workDaySheet.setIrregularDay(true);
				    workDaySheet.setBalanceTime(Duration.ZERO.minus(wsType.getNormalWorkPeriod()
					    .getWorkPeriodDuration()));
				    if (wsType.getFixedWorkPeriod() != null) {
					workDaySheet.setUnjustifiedTime(wsType.getFixedWorkPeriod().getWorkPeriodDuration());
				    }
				} else {
				    workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(workPeriod, getWorkScheduleType()
					    .getNormalWorkPeriod().getWorkPeriodDuration()));
				    if (wsType.getFixedWorkPeriod() != null) {
					workDaySheet.setUnjustifiedTime(wsType.calculateFixedPeriodDuration(workDaySheet
						.getTimeline()));
				    }
				}
			    }
			} else {
			    // ver se e' dentro do final do intervalo de almoco
			    // menos o desconto
			    // obrigatorio
			    if (wsType.getMeal().getEndOfMealBreakMinusDiscountInterval().contains(lastClockingDate, false)) {
				// descontar periodo de tempo desde fim da
				// refeicao menos periodo
				// obrigatorio de refeicao ate' 'a ultima
				// marcacao do funcionario
				workPeriod = workPeriod.minus((new TimeInterval(wsType.getMeal()
					.getEndOfMealBreakMinusMealDiscount(), lastClockingDate, false)).getDurationMillis());
			    }
			    workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(workPeriod, getWorkScheduleType()
				    .getNormalWorkPeriod().getWorkPeriodDuration()));
			    workDaySheet.setUnjustifiedTime(wsType.calculateFixedPeriodDuration(workDaySheet.getTimeline()));
			}
		    }
		}
	    }
	} else { // meal nao esta definida - so ha 1 periodo de trabalho
	    Duration worked = workDaySheet.getTimeline().calculateWorkPeriodDuration(
		    null,
		    workDaySheet.getTimeline().getTimePoints().iterator().next(),
		    new TimePoint(getWorkScheduleType().getClockingTime(), AttributeType.NULL),
		    new TimePoint(getWorkScheduleType().getClockingEndTime(), getWorkScheduleType().isClokingTimeNextDay(),
			    AttributeType.NULL), wsType.getMaximumContinuousWorkPeriod(), wsType);
	    workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(worked, getWorkScheduleType().getNormalWorkPeriod()
		    .getWorkPeriodDuration()));
	    workDaySheet.setUnjustifiedTime(wsType.calculateFixedPeriodDuration(workDaySheet.getTimeline()));

	}

	Duration balance = workDaySheet.getBalanceTime();
	if (workDaySheet.getWorkSchedule().getWorkScheduleType().getScheduleClockingType().equals(
		ScheduleClockingType.RIGID_CLOCKING)) {
	    if (workDaySheet.getUnjustifiedTime() != null && !workDaySheet.getUnjustifiedTime().equals(Duration.ZERO)
		    && balance.isShorterThan(Duration.ZERO)) {
		balance = balance.plus(workDaySheet.getUnjustifiedTime());
		if (balance.isLongerThan(Duration.ZERO)) {
		    workDaySheet.setBalanceTime(Duration.ZERO);
		} else {
		    workDaySheet.setBalanceTime(balance);
		}
	    }
	}
	return workDaySheet;
    }

    public Duration subtractDurationsWithoutSeconds(Duration firstDuration, Duration secondDuration) {
	Period normalWorkedPeriod = firstDuration.toPeriod();
	normalWorkedPeriod = normalWorkedPeriod.minusSeconds(normalWorkedPeriod.getSeconds());
	return normalWorkedPeriod.toDurationFrom(new DateMidnight()).minus(secondDuration);
    }

    private boolean justificationInMealBreak(List<Leave> timeLeaves) {
	for (Leave leave : timeLeaves) {
	    Interval leaveInterval = new Interval(leave.getDate().toLocalTime().toDateTimeToday(), leave.getEndDate()
		    .toLocalTime().toDateTimeToday());
	    Interval mealInterval = new Interval(getWorkScheduleType().getMeal().getBeginMealBreak().toDateTimeToday(),
		    getWorkScheduleType().getMeal().getEndMealBreak().toDateTimeToday());
	    if (leaveInterval.overlaps(mealInterval) || leaveInterval.abuts(mealInterval)) {
		return true;
	    }
	}
	return false;
    }

    public void setWorkScheduleDays(HashMap<String, WorkScheduleDaySheet> workScheduleDays, ResourceBundle bundle) {
	for (WeekDay weekDay : getWorkWeek().getDays()) {
	    WorkScheduleDaySheet workScheduleDaySheet = new WorkScheduleDaySheet();
	    workScheduleDaySheet.setSchedule(getWorkScheduleType().getAcronym());
	    workScheduleDaySheet.setWeekDay(bundle.getString(weekDay.toString() + "_ACRONYM"));
	    workScheduleDaySheet.setWorkSchedule(this);
	    workScheduleDays.put(weekDay.toString(), workScheduleDaySheet);
	}
    }

    public void delete() {
	if (canBeDeleted()) {
	    removeRootDomainObject();
	    getPeriodicity().delete();
	    removePeriodicity();
	    getWorkScheduleType().delete();
	    removeWorkScheduleType();
	    deleteDomainObject();
	}
    }

    public boolean canBeDeleted() {
	return !hasAnySchedules();
    }

    // if returns 1 the clocking belongs to the clocking date
    // if returns 0 it may belong to the clocking date or the day before
    // if returns -1 it may belong to the clocking date or the day before
    public static int overlapsSchedule(DateTime clocking, HashMap<LocalDate, WorkSchedule> workScheduleMap) {
	final LocalDate clockingLocalDate = clocking.toLocalDate();

	WorkSchedule thisDaySchedule = workScheduleMap.get(clockingLocalDate);
	WorkSchedule dayBeforeSchedule = workScheduleMap.get(clockingLocalDate.minusDays(1));

	final Interval thisDayWorkTimeInterval;
	if (thisDaySchedule != null) {
	    DateTime beginThisDayWorkTime = clockingLocalDate.toDateTime(thisDaySchedule.getWorkScheduleType().getWorkTime());
	    DateTime endThisDayWorkTime = clockingLocalDate.toDateTime(thisDaySchedule.getWorkScheduleType().getWorkEndTime());
	    if (thisDaySchedule.getWorkScheduleType().isWorkTimeNextDay()) {
		endThisDayWorkTime = endThisDayWorkTime.plusDays(1);
	    }
	    thisDayWorkTimeInterval = new Interval(beginThisDayWorkTime, endThisDayWorkTime);
	} else {
	    thisDayWorkTimeInterval = WorkScheduleType.getDefaultWorkTime(clockingLocalDate);
	}
	final Interval dayBeforeWorkTimeInterval;
	if (dayBeforeSchedule != null) {
	    DateTime beginDayBeforeWorkTime = clockingLocalDate.toDateTime(dayBeforeSchedule.getWorkScheduleType().getWorkTime())
		    .minusDays(1);
	    DateTime endDayBeforeWorkTime = clockingLocalDate
		    .toDateTime(dayBeforeSchedule.getWorkScheduleType().getWorkEndTime()).minusDays(1);
	    if (dayBeforeSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
		endDayBeforeWorkTime = endDayBeforeWorkTime.plusDays(1);
	    }

	    dayBeforeWorkTimeInterval = new Interval(beginDayBeforeWorkTime, endDayBeforeWorkTime);
	} else {
	    dayBeforeWorkTimeInterval = WorkScheduleType.getDefaultWorkTime(clockingLocalDate.minusDays(1));
	}
	Interval overlapResult = thisDayWorkTimeInterval.overlap(dayBeforeWorkTimeInterval);
	if (overlapResult == null) {
	    Interval gapResult = dayBeforeWorkTimeInterval.gap(thisDayWorkTimeInterval);
	    if (gapResult != null) {
		if (!gapResult.contains(clocking)) {
		    return dayBeforeWorkTimeInterval.contains(clocking) ? 0 : 1;
		}
	    } else {
		return dayBeforeWorkTimeInterval.contains(clocking) ? 0 : 1;
	    }
	} else if (!overlapResult.contains(clocking) && clocking.isAfter(overlapResult.getStart())) {
	    return 1;
	} else if (!overlapResult.contains(clocking) && clocking.isBefore(overlapResult.getStart())) {
	    return -1;
	}
	return 0;
    }

}
