package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class WorkScheduleType extends WorkScheduleType_Base {

    public static Duration maximumContinuousWorkPeriod = new Duration(18000000); // 5 hours

    public void update(String className, YearMonthDay beginValidDate, YearMonthDay endValidDate,
            String acronym, TimeOfDay dayTime, Duration dayTimeDuration, TimeOfDay clockingTime,
            Duration clockingTimeDuration, WorkPeriod normalWorkPeriod, WorkPeriod fixedWorkPeriod,
            Meal meal, DateTime lastModifiedDate, Employee modifiedBy) {
        setOjbConcreteClass(className);
        setAcronym(acronym);
        setWorkTime(dayTime);
        setWorkTimeDuration(dayTimeDuration);
        setClockingTime(clockingTime);
        setClockingTimeDuration(clockingTimeDuration);
        setNormalWorkPeriod(normalWorkPeriod);
        setFixedWorkPeriod(fixedWorkPeriod);
        setMeal(meal);
        setBeginValidDate(beginValidDate);
        setEndValidDate(endValidDate);
        setLastModifiedDate(lastModifiedDate);
        setModifiedBy(modifiedBy);
    }

    public boolean definedFixedPeriod() {
        return (getFixedWorkPeriod() != null);
    }

    public boolean definedMeal() {
        return (getMeal() != null);
    }

    public Duration calculateFixedPeriodDuration(Timeline timeline) {
        if (definedFixedPeriod()) {
            Duration workedfixedPeriod = timeline.calculateFixedPeriod(AttributeType.FIXED_PERIOD_1);
            if ((getFixedWorkPeriod()).getSecondPeriodInterval() != null) {
                workedfixedPeriod = workedfixedPeriod.plus(timeline
                        .calculateFixedPeriod(AttributeType.FIXED_PERIOD_2));
            }
            return getFixedWorkPeriod().getWorkPeriodDuration().minus(workedfixedPeriod);
        }
        return Duration.ZERO;
    }

    public Duration checkMealDurationAccordingToRules(TimeInterval lunchBreak, boolean justification,
            Timeline timeline, TimePoint firstClocking) {
        if (definedMeal()) {
            if (!justification
                    && lunchBreak.getDuration().isShorterThan(getMeal().getMinimumMealBreakInterval())) {
                return null;
            } else {
                Duration discount = calculateNotWorkedMealDuration(timeline, lunchBreak.getStartTime(),
                        lunchBreak.getEndTime(), firstClocking);
                if (discount.isLongerThan(getMeal().getMandatoryMealDiscount())
                        || discount.isEqual(getMeal().getMandatoryMealDiscount())) {
                    return Duration.ZERO;
                }
                // /////////remove in 2007
                if (getMeal().getMinimumMealBreakInterval() != Duration.ZERO) {
                    if (getMeal().getMealBreak().overlap(lunchBreak) == null) {
                        return Duration.ZERO;
                    }
                    Duration mealDiscount = getMeal().calculateMealDiscount(
                            getMeal().getMealBreak().overlap(lunchBreak).toDuration());
                    if (discount.isLongerThan(mealDiscount)) {
                        return Duration.ZERO;
                    }
                    return mealDiscount.minus(discount);
                }
                // /////////////////////////
                Duration mealDiscount = getMeal().calculateMealDiscount(lunchBreak.getDuration());
                if (discount.isLongerThan(mealDiscount)) {
                    return Duration.ZERO;
                }
                return mealDiscount.minus(discount);
            }
        }
        return Duration.ZERO;
    }

    private Duration calculateNotWorkedMealDuration(Timeline timeline, TimeOfDay beginLunch,
            TimeOfDay endLunch, TimePoint firstClocking) {
        Duration totalDuration = Duration.ZERO;
        Interval scheduleMealBreak = getMeal().getMealBreak().toInterval(
                new YearMonthDay().toDateTimeAtMidnight());
        for (TimePoint timePoint : timeline.getTimePoints()) {
            DateTime timePointDateTime = timePoint.getDateTime(new YearMonthDay());
            if (scheduleMealBreak.contains(timePointDateTime)
                    && (!timePoint.getTime().isEqual(beginLunch))
                    && (timeline.isOpeningAndNotClosingWorkedPeriod(timePoint)
                            || timeline.isClosingAnyWorkedPeriod(timePoint) || timePoint
                            .isAtSameTime(firstClocking))) {
                if (!timePoint.getTime().isAfter(beginLunch)) {
                    totalDuration = totalDuration.plus(new Duration(scheduleMealBreak.getStart(),
                            timePointDateTime));
                } else if (!timePoint.getTime().isBefore(endLunch)) {
                    totalDuration = totalDuration.plus(new Duration(timePointDateTime, scheduleMealBreak
                            .getEnd()));
                }
            }
        }
        return totalDuration;
    }

    public void delete() {
        if (canBeDeleted()) {
            removeRootDomainObject();
            Meal meal = getMeal();
            if (meal != null) {
                removeMeal();
                meal.delete();
            }
            WorkPeriod normalWorkPeriod = getNormalWorkPeriod();
            removeNormalWorkPeriod();
            normalWorkPeriod.delete();
            WorkPeriod fixedWorkPeriod = getFixedWorkPeriod();
            if (fixedWorkPeriod != null) {
                removeFixedWorkPeriod();
                fixedWorkPeriod.delete();
            }
            removeModifiedBy();
            deleteDomainObject();
        }
    }

    public boolean canBeDeleted() {
        return !hasAnyWorkSchedules();
    }

    public TimeOfDay getWorkEndTime() {
        return getWorkTime().plus(getWorkTimeDuration().toPeriod());
    }

    public TimeOfDay getClockingEndTime() {
        return getClockingTime().plus(getClockingTimeDuration().toPeriod());
    }

    public boolean isWorkTimeNextDay() {
        DateTime now = TimeOfDay.MIDNIGHT.toDateTimeToday();
        Duration maxDuration = new Duration(getWorkTime().toDateTime(now).getMillis(), now.plusDays(1)
                .getMillis());
        return (getWorkTimeDuration().compareTo(maxDuration) >= 0);
    }

    public boolean isClokingTimeNextDay() {
        DateTime now = TimeOfDay.MIDNIGHT.toDateTimeToday();
        Duration maxDuration = new Duration(getClockingTime().toDateTime(now).getMillis(), now.plusDays(
                1).getMillis());
        return (getClockingTimeDuration().compareTo(maxDuration) >= 0);
    }

    public static Interval getDefaultWorkTime(YearMonthDay workDay) {
        return new Interval(workDay.toDateTime(new TimeOfDay(3, 0, 0, 0)), workDay.toDateTime(
                new TimeOfDay(6, 0, 0, 0)).plusDays(1));
    }

    public Duration getMaximumContinuousWorkPeriod() {
        // if (getMeal() == null || getMeal().getMinimumMealBreakInterval().equals(Duration.ZERO)) {
        // return null;
        // }
        // return maximumContinuousWorkPeriod;
        return null;
    }
}
