package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidWorkdayIntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.WorkdayOutOfLegalBoundsException;
import net.sourceforge.fenixedu.domain.exceptions.misc.InvalidIntervalLimitsException;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;
import net.sourceforge.fenixedu.domain.assiduousness.util.WeekDays;
import net.sourceforge.fenixedu.presentationTier.util.DTO;
import net.sourceforge.fenixedu.presentationTier.util.PresentationConstants;

/**
 *
 * @author velouria@velouria.org
 * 
 */
public class WorkSchedule extends WorkSchedule_Base {
    
    public WorkSchedule() {
        super();
    }
 
    public boolean isTemplate() {
        return getTemplateSchedule();
    }
    
    public void turnTemplateSchedule() {
        setTemplateSchedule(true);
    }

    public void turnWorkSchedule() {
        setTemplateSchedule(false);        
    }
    
    public static WorkSchedule createWorkSchedule(DTO presentationDTO) throws FenixDomainException {
        ScheduleType type = (ScheduleType)presentationDTO.get(PresentationConstants.SCHEDULE_TYPE);
        System.out.println("LOG: work schedule type" + type);
        switch (type) {
        case FLEXIBLE:
            return FlexibleSchedule.createFlexibleSchedule(presentationDTO);
        case CONTINUOUS:
            return ContinuousSchedule.createContinuousSchedule(presentationDTO);
        case EXEMPTION:
            return ScheduleExemption.createScheduleExemption(presentationDTO);
        case ONE_HOUR_EXEMPTION:
            return OneHourExemptionSchedule.createOneHourExemptionSchedule(presentationDTO);
        case TWO_HOURS_EXEMPTION:
            return TwoHoursExemptionSchedule.createTwoHoursExemptionSchedule(presentationDTO);
        case HALF_TIME:
            return HalfTimeSchedule.createHalfTimeSchedule(presentationDTO);
        case CUSTOM_FLEXIBLE:    
            return CustomFlexibleSchedule.createCustomFlexibleSchedule(presentationDTO);
        default:
            return null;
        }
    }

    // Returns the schedule Attributes
    public Attributes getAttributes() {
        Attributes attributes = new Attributes(AttributeType.NORMAL_WORK_PERIOD_1);
        if (((NormalWorkPeriod)getNormalWorkPeriod()).definedNormalWorkPeriod2()) {
            attributes.addAttribute(AttributeType.NORMAL_WORK_PERIOD_2);
        }
        if (definedFixedPeriod()) {
            attributes.addAttribute(AttributeType.FIXED_PERIOD_1);
            if (((FixedPeriod)getFixedPeriod()).definedFixedPeriod2()) {
                attributes.addAttribute(AttributeType.FIXED_PERIOD_2);
            }
        }
        if (((Meal)getMeal()).definedMealBreak()) {
            attributes.addAttribute(AttributeType.MEAL);
        }
        return attributes;
    }

    
    // Checks if the Fixed Period is defined. There are schedules that don't have a fixed period
    public boolean definedFixedPeriod() {
        return (getFixedPeriod() != null);
    }

    // Checks if the Fixed Period is defined. Some schedules that don't have a meal period
    public boolean definedMeal() {
        return (getMeal() != null);
    }
    
    // returns true if the actual date is before the schedule's end date
    public boolean isCurrent() {
        return getValidFromTo().contains(new DateTime());
    }

    // Returns the duration of normal period times the number of days per week the Employee has that schedule
	public Duration calculateWeekDuration() {
	    return new Duration(((NormalWorkPeriod)getNormalWorkPeriod()).getTotalNormalWorkPeriodDuration().getMillis() * getWorkWeek().workDaysPerWeek());
	}
    
    // Returns true if the schedule is defined in the week day weekDay
    public boolean isDefinedInWeekDay(WeekDays weekDay) {
        return getWorkWeek().worksAt(weekDay);
    }
   
/* TODO: PASSAR PARA FUNCIONARIO */ 
//    /* Adds a List of Regimes to the employee Schedule */
//    public void addRegimesToWorkSchedule(List<AssiduousnessRegime> regimes) {
//        for (AssiduousnessRegime regime: regimes) {
//            addRegimes(regime);
//        }
//    }
    
//    public boolean hasRegime(RegimeType regimeType) {
//        Iterator<Regime> itRegime = this.getRegimesIterator();
//        while (itRegime.hasNext()) {
//            if (itRegime.equals(regime)) {
//                return true;
//            }
//        }
//        return false;
//    }
    
    
    
    // Plots the schedule in the timeline
    public void plotInTimeline(Timeline timeline) {
        List<TimePoint> pointList = new ArrayList<TimePoint>();
        pointList.addAll(((NormalWorkPeriod)getNormalWorkPeriod()).toTimePoints());
        if (definedFixedPeriod()) {
            pointList.addAll(((FixedPeriod)getFixedPeriod()).toTimePoints());
        }
        if (definedMeal()) {
           pointList.addAll(((Meal)getMeal()).toTimePoints()); 
        }
        timeline.plotList(pointList);
    }
    
    
    
    // TODO test this
    // TODO ver se o funcionario trabalhou dentro dos periodos
    public DailyBalance calculateDailyBalance(YearMonthDay day, List<ClockingInterval> dailyClockings) {
        Timeline timeline = new Timeline();
        plotInTimeline(timeline);
        timeline.plotList(ClockingInterval.toTimePoint(dailyClockings));
        timeline.print();

        DailyBalance dailyBalance = new DailyBalance(this, day);
        Duration firstWorkPeriod = Duration.ZERO;
        Duration lastWorkPeriod = Duration.ZERO;
        TimeInterval mealInterval = null;
        // se meal break estiver definido
        if (definedMeal()) {
            mealInterval = timeline.calculateMealBreakInterval(getMeal().getMealBreak()); // calcular o intervalo de refeicao dentro do periodo de refeicao definido
            System.out.println("intervalo refeicao: " + mealInterval);
            if (mealInterval != null) {
                System.out.println("nao e' nulll");
                dailyBalance.setLunchBreak(mealInterval.getDuration()); // actualiza almoco no dailyBalance

                // calcula morning period
                firstWorkPeriod = timeline.calculateDurationAllIntervalsByAttributesToTime(mealInterval.getStartTime(), DomainConstants.WORKED_ATTRIBUTES);
                System.out.println("trabalhou de manha:" + firstWorkPeriod.toPeriod().toString());
                dailyBalance.setNormalWorkPeriod1Balance(checkNormalWorkPeriodAccordingToRules(firstWorkPeriod).minus(((NormalWorkPeriod)getNormalWorkPeriod()).
                        getNormalWorkPeriod1Duration()));
                // NWP2
                if (((NormalWorkPeriod)getNormalWorkPeriod()).definedNormalWorkPeriod2()) {
                    if (mealInterval != null) {
                        lastWorkPeriod = timeline.calculateDurationAllIntervalsByAttributesFromTime(mealInterval.getEndTime(), DomainConstants.WORKED_ATTRIBUTES);
                        System.out.println("tarde: " +lastWorkPeriod.toDuration().toPeriod().toString());
                        dailyBalance.setNormalWorkPeriod2Balance(lastWorkPeriod.minus(((NormalWorkPeriod)getNormalWorkPeriod()).getNormalWorkPeriod2Duration()));
                    }
                }            
            } else { // mealInterval e' null a pessoa nao foi almocar
                System.out.println("a pessoa nao almocou!");
            }
        } else {
            // se nao tem meal
            // TODO acabar parte das jornadas continuase horarios sem intervalo de refeicao
            // TODO testar
            Duration worked = timeline.calculateDurationAllIntervalsByAttributes(DomainConstants.WORKED_ATTRIBUTES);
            dailyBalance.setNormalWorkPeriod1Balance(worked.minus(((NormalWorkPeriod)getNormalWorkPeriod()).getNormalWorkPeriod1Duration()));
        }

        System.out.println("total worked1 ->" +dailyBalance.getNormalWorkPeriod1Balance().toPeriod().toString());
        System.out.println("total worked2 ->" +dailyBalance.getNormalWorkPeriod2Balance().toPeriod().toString());
        System.out.println(dailyBalance.getNormalWorkPeriod1Balance().plus(dailyBalance.getNormalWorkPeriod2Balance()).minus(((NormalWorkPeriod)getNormalWorkPeriod()).
                getTotalNormalWorkPeriodDuration()).toPeriod().toString());
        System.out.println("devia ter trabalhado ->" + ((NormalWorkPeriod)getNormalWorkPeriod()).getTotalNormalWorkPeriodDuration().toPeriod().toString());
        // Fixed Periods if defined
        this.calculateFixedPeriodDuration(dailyBalance, timeline);
        
        if (definedMeal()) {
            System.out.println("actualizar o meal");
            checkMealDurationAccordingToRules(dailyBalance);
        }
//        System.out.println("total worked ->" + dailyBalance.getNormalWorkPeriod1Balance().plus(dailyBalance.getNormalWorkPeriod2Balance()).minus(this.getNormalWorkPeriod().
//                getTotalNormalWorkPeriodDuration()).toPeriod().toString());
        return dailyBalance;
    }
        
    public void calculateFixedPeriodDuration(DailyBalance dailyBalance, Timeline timeline) {
      if (definedFixedPeriod()) {
            dailyBalance.setFixedPeriodAbsence(timeline.calculateFixedPeriod(AttributeType.FIXED_PERIOD_1));
            if (((FixedPeriod)getFixedPeriod()).definedFixedPeriod2()) {
                Duration fixedPeriod2Duration = timeline.calculateFixedPeriod(AttributeType.FIXED_PERIOD_2);
                dailyBalance.setFixedPeriodAbsence(dailyBalance.getFixedPeriodAbsence().plus(fixedPeriod2Duration));
            }
            dailyBalance.setFixedPeriodAbsence(((FixedPeriod)getFixedPeriod()).getTotalFixedPeriodDuration().minus(dailyBalance.getFixedPeriodAbsence()));
        }
    }
    
    public void checkMealDurationAccordingToRules(DailyBalance dailyBalance) {
        // According to Nota Informativa 55/03
        // if mealDuration is shorter than 15 minutes the whole day is subtracted from the normal work period
        if (dailyBalance.getLunchBreak().isShorterThan(getMeal().getMinimumMealBreakInterval())) {
            // NormalWorkPeriodBalance will be - totalNormalWorkPeriodDuration
            
            dailyBalance.setNormalWorkPeriod1Balance(Duration.ZERO.minus(((NormalWorkPeriod)getNormalWorkPeriod()).getNormalWorkPeriod1Duration()));
            dailyBalance.setNormalWorkPeriod2Balance(Duration.ZERO.minus(((NormalWorkPeriod)getNormalWorkPeriod()).getNormalWorkPeriod2Duration()));
            if (definedFixedPeriod()) {
                // TODO check this with assiduousness ppl
                // FixedPeriod absence will be the whole fixed period duration
                System.out.println("fixed period com tudo!");
//                System.out.println(this.getFixedPeriod().getTotalFixedPeriodDuration().toPeriod().toString());
                dailyBalance.setFixedPeriodAbsence(((FixedPeriod)getFixedPeriod()).getTotalFixedPeriodDuration());
                System.out.println(dailyBalance.getFixedPeriodAbsence().toPeriod().toString());
            }
        } else {
            System.out.println("almoco:" + dailyBalance.getNormalWorkPeriod2Balance().toPeriod().toString());
            System.out.println(dailyBalance.getLunchBreak().toPeriod().toString());
            dailyBalance.setNormalWorkPeriod2Balance(dailyBalance.getNormalWorkPeriod2Balance().minus(((Meal)this.getMeal()).calculateMealDiscount(dailyBalance.getLunchBreak())));
            System.out.println("almoco:" + dailyBalance.getNormalWorkPeriod2Balance().toPeriod().toString());
        }
    }
    
    // So' sao contabilizadas 5 horas no periodo da manha, mesmo que o funcionario trabalhe mais, excepto para o caso do Continuous Schedule
    public Duration checkNormalWorkPeriodAccordingToRules(Duration normalWorkPeriodWorked) {
            System.out.println("normalworkperiod worked" +normalWorkPeriodWorked.toPeriod().toString());
        if (normalWorkPeriodWorked.isLongerThan(DomainConstants.MAX_MORNING_WORK)) {
            return DomainConstants.MAX_MORNING_WORK;
        } else {
            return normalWorkPeriodWorked;
        }
    }    
    
    
	/*
     * ATTRIBUTE BUILDERS WITH DATA FROM THE PRESENTATION
     * TODO check if this makes any sense in the new arch 
	 */
    
//    // builds an Interval from start date and time and end date and time.
//    public static Interval createIntervalFromTimes(Integer startYear, Integer startMonth, Integer startDay, Integer startHours, Integer startMinutes, Integer endHours, 
//	        Integer endMinutes, boolean nextDay) throws InvalidIntervalLimitsException {
//	    DateTime startTime = new DateTime(startYear.intValue(), startMonth.intValue(), startDay.intValue(), startHours.intValue(), startMinutes.intValue(), 0, 0);
//	    DateTime endTime = new DateTime(startYear.intValue(), startMonth.intValue(), startDay.intValue(), endHours.intValue(), endMinutes.intValue(), 0, 0);
//	    if (startTime.isBefore(endTime)) {
//		    // next day
//		    if (nextDay) {
//		        endTime = endTime.plus(Period.days(1));
//		    }
//		    return new Interval(startTime, endTime);
//	    } else {
//	        // TODO throw exception the startTime and endTime are inconsistent...
//	        throw new InvalidIntervalLimitsException("startTime and endTime are inconsistent...");
//	    }
//	}
    
//    // builds an Interval from start date and time and end date and time.
//    public static TimeInterval createTimeIntervalFromTimes(Integer startYear, Integer startMonth, Integer startDay, Integer startHours, Integer startMinutes, Integer endHours, 
//            Integer endMinutes, boolean nextDay) throws InvalidIntervalLimitsException {
//        
//        DateTime startTime = new DateTime(startYear.intValue(), startMonth.intValue(), startDay.intValue(), startHours.intValue(), startMinutes.intValue(), 0, 0);
//        DateTime endTime = new DateTime(startYear.intValue(), startMonth.intValue(), startDay.intValue(), endHours.intValue(), endMinutes.intValue(), 0, 0);
//        if (startTime.isBefore(endTime)) {
//            // next day
//            if (nextDay) {
//                endTime = endTime.plus(Period.days(1));
//            }
//            return new Interval(startTime, endTime);
//        } else {
//            // TODO throw exception the startTime and endTime are inconsistent...
//            throw new InvalidIntervalLimitsException("startTime and endTime are inconsistent...");
//        }
//    }

    
    
//	 // builds regular schedule/fixed period/meal break intervals from the data got from the presentation
//	public static Interval createTimeIntervalPeriod(Integer startYear, Integer startMonth, Integer startDay, Integer startHours, Integer startMinutes, Integer endHours, 
//	        Integer endMinutes, boolean nextDay, FenixDomainException specificException) throws FenixDomainException {
//	    Interval interval = null;
//	    try {
//	        interval = createIntervalFromTimes(startYear, startMonth, startDay, startHours, startMinutes, endHours, endMinutes, nextDay);
//	    } catch (InvalidIntervalLimitsException e) {
//	        throw specificException;
//        }
//        return interval;
//	}

     // builds regular schedule/fixed period/meal break intervals from the data got from the presentation
    public static TimeInterval createTimeInterval(Integer startHours, Integer startMinutes, Integer endHours, Integer endMinutes, boolean nextDay, 
            FenixDomainException specificException) throws FenixDomainException {
        TimeInterval timeInterval = null;
        TimeOfDay startTime = new TimeOfDay(startHours, startMinutes);
        TimeOfDay endTime = new TimeOfDay(endHours, endMinutes);
        if (startTime.isBefore(endTime)) {
            timeInterval = new TimeInterval(startTime, endTime, nextDay);
        } else {
            throw specificException;
        }
        return timeInterval;
    }

    
    // builds and validates the workday
	public static TimeInterval createWorkDay(Integer startHours, Integer startMinutes, Integer endHours, Integer endMinutes, boolean nextDay) throws FenixDomainException {
	    // expediente
        TimeInterval workDay = null;
        try {
            workDay = createTimeInterval(startHours, startMinutes, endHours, endMinutes, nextDay, null);
	        // Se duracao do expediente for menor que a duracao do expediente minimo
	        // ou duracao do expediente for maior que a duracao do expediente maximo da' erro!
            if (workDay.getDuration().isShorterThan(DomainConstants.DURATION_MINIMUM_WORKDAY) && workDay.getDuration().isLongerThan(DomainConstants.DURATION_MAXIMUM_WORKDAY)) {
                System.out.println("erro: workday ta fora dos limites.");
                // TODO mudar msg
                throw new WorkdayOutOfLegalBoundsException("workdayOutofBounds exception");
            }
	    } catch (InvalidIntervalLimitsException e) {
	        throw new InvalidWorkdayIntervalException("Invalid WorkDay Interval Exception");
        }
	    return workDay;
	}
	
    
	// validates and creates the Valid From To Interval
    // TODO find a decent name for this... god it sucks!
    public static Interval createValidFromToInterval(Integer startYear, Integer startMonth, Integer startDay, Integer endYear, Integer endMonth, Integer endDay) {
        // 	data validade - usada como data nos TimeIntervals do horario
        DateTime startScheduleDate = new DateTime(startYear.intValue(), startMonth.intValue(), startDay.intValue(), 0, 0, 0, 0);
        // end day might not be defined
        DateTime endScheduleDate = null;
        if ((endYear != null) && (endMonth != null) && (endDay != null)) {
            System.out.println("dafa final def");
            endScheduleDate = new DateTime(endYear.intValue(), endMonth.intValue(), endDay.intValue(), 0, 0, 0, 0);
        } else {
            System.out.println("dafa final nao def");
            endScheduleDate = DomainConstants.FAR_FUTURE;
        }
        return new Interval(startScheduleDate, endScheduleDate);
    }
        
    
    // creates a Set with the days the employee works from the stuff got from the presentation
    public static EnumSet<WeekDays> createWorkDays(boolean everyDay, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri) {
        if (everyDay) {
            return EnumSet.allOf(WeekDays.class);
        } else {
            EnumSet<WeekDays> days = EnumSet.noneOf(WeekDays.class);
            if (mon) {
               days.add(WeekDays.MONDAY);
            }
            if (tue) {
                days.add(WeekDays.TUESDAY);
            }
            if (wed) {
                days.add(WeekDays.WEDNESDAY);
            }
            if (thu) {
                days.add(WeekDays.THURSDAY);
            }
            if (fri) {
                days.add(WeekDays.FRIDAY);
            }
            return days;
        }
    }    
      
    public String getName() {
        return "Personalizado";
    }

}

