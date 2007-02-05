/*
 * Created on 9/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain.space;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

/**
 * @author Ana e Ricardo
 * 
 */
public class RoomOccupation extends RoomOccupation_Base {
   
    public static final Comparator<RoomOccupation> COMPARATOR_BY_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("period.startDate"));	
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE).addComparator(new BeanComparator("idInternal"));
    }
   
    public static final int DIARIA = 1;

    public static final int SEMANAL = 2;

    public static final int QUINZENAL = 3;
   
    private transient Locale locale = LanguageUtils.getLocale();
    
    
    public RoomOccupation() {
    	super();    	
        setRootDomainObject(RootDomainObject.getInstance());       
    }

    public RoomOccupation(OldRoom room, Calendar startTime, Calendar endTime, DiaSemana dayOfWeek, Integer frequency) {	
        
	this();
        setRoom(room);
        setStartTime(startTime);
        setEndTime(endTime);
        setDayOfWeek(dayOfWeek);
        setFrequency(frequency);
    }   
    
    public RoomOccupation(OldRoom room, Calendar startTime, Calendar endTime, 
	    DiaSemana dayOfWeek, FrequencyType frequency, GenericEvent genericEvent, OccupationPeriod occupationPeriod) {	
	
        this();
        setRoom(room);
        setGenericEvent(genericEvent);
        setStartTime(startTime);
        setEndTime(endTime);
        setDayOfWeek(dayOfWeek);                       
        setPeriod(occupationPeriod);
        setFrequency(frequency);
    }   
           
    public boolean roomOccupationForDateAndTime(RoomOccupation roomOccupation) {
        return roomOccupationForDateAndTime(roomOccupation.getPeriod(), roomOccupation.getStartTime(),
                roomOccupation.getEndTime(), roomOccupation.getDayOfWeek(), roomOccupation
                        .getFrequency(), roomOccupation.getWeekOfQuinzenalStart(), roomOccupation
                        .getRoom());
    }
    
    public boolean roomOccupationForDateAndTime(OccupationPeriod period, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, Integer frequency, Integer week) {
     
	return roomOccupationForDateAndTime(period.getStartDate(), period.getEndDate(), startTime,
                endTime, dayOfWeek, frequency, week);
    }

    public boolean roomOccupationForDateAndTime(OccupationPeriod period, Calendar startTime,
	    Calendar endTime, DiaSemana dayOfWeek, Integer frequency, Integer week, OldRoom room) {

	if (!room.equals(this.getRoom())) {
	    return false;
	}

	return roomOccupationForDateAndTime(period.getStartDate(), period.getEndDate(), startTime,
		endTime, dayOfWeek, frequency, week);
    }

    public boolean roomOccupationForDateAndTime(Calendar startDate, Calendar endDate, Calendar startTime, Calendar endTime, 
	    DiaSemana dayOfWeek, Integer frequency, Integer week) {
      
	startTime.set(Calendar.SECOND, 0);
	startTime.set(Calendar.MILLISECOND, 0);
	endTime.set(Calendar.SECOND, 0);
	endTime.set(Calendar.MILLISECOND, 0);
	
	if (this.getPeriod().intersectPeriods(startDate, endDate)) {                       	   	    	    			   
	    
	    List<Interval> thisOccupationIntervals = getRoomOccupationIntervals();
	    List<Interval> passedOccupationIntervals = getRoomOccupationIntervals(new YearMonthDay(startDate), new YearMonthDay(endDate),  
		    new HourMinuteSecond(startTime.getTime()), new HourMinuteSecond(endTime.getTime()), frequency, week, dayOfWeek);		
	    			    	  
	    for (Interval interval : thisOccupationIntervals) {		    
                for (Interval passedInterval : passedOccupationIntervals) {
                    if(interval.getStart().isBefore(passedInterval.getEnd()) && 
                	    interval.getEnd().isAfter(passedInterval.getStart())) {
                        return true;
                    }
                }		    												    
            }
        }
	
        return false;
    }

    public Integer getFrequency() {
        if (getLesson() != null) {
            return getLesson().getFrequency();
        }        
        if (getGenericEvent() != null && getGenericEvent().getFrequency() != null) {                       
            return getGenericEvent().getFrequency().ordinal() + 1;
        }
        return null;
    }
    
    public void setFrequency(Integer frequency) {
        if(getLesson() != null) {
            getLesson().setFrequency(frequency); 
        }      
    }
    
    public void setFrequency(FrequencyType type) {
	if (getGenericEvent() != null) {
	    getGenericEvent().setFrequency(type);
	}
    }
    
    public Integer getWeekOfQuinzenalStart() {
        return getLesson() != null ? getLesson().getWeekOfQuinzenalStart() : null;
    }
    
    public void setWeekOfQuinzenalStart(Integer weekOfQuinzenalStart) {
        if(getLesson() != null) {
            getLesson().setWeekOfQuinzenalStart(weekOfQuinzenalStart); 
        }        
    }              
   
    public void delete() {
        final OccupationPeriod period = getPeriod();

        removeLesson();
        removeWrittenEvaluation();
        removeGenericEvent();
        removeRoom();
        removePeriod();

        if (period != null) {
            period.deleteIfEmpty();
        }

        removeRootDomainObject();
        super.deleteDomainObject();
    }
       
    public DateTime getFirstInstant() {		
	List<Interval> roomOccupationIntervals = getRoomOccupationIntervals();
	return (roomOccupationIntervals != null && !roomOccupationIntervals.isEmpty()) ?
		roomOccupationIntervals.get(0).getStart() : null;	
    }
    
    public DateTime getLastInstant() {	
	List<Interval> roomOccupationIntervals = getRoomOccupationIntervals();
	return (roomOccupationIntervals != null && !roomOccupationIntervals.isEmpty()) ?
		roomOccupationIntervals.get(roomOccupationIntervals.size() - 1).getEnd() : null;	
    }                    
    
    public List<Interval> getRoomOccupationIntervals(){		 
	return getRoomOccupationIntervals(getPeriod().getStartYearMonthDay(), getPeriod().getEndYearMonthDay(), 
		    getStartTimeDateHourMinuteSecond(), getEndTimeDateHourMinuteSecond(), getFrequency(), getWeekOfQuinzenalStart(),
		    getDayOfWeek());	
    }	
          
    public static List<Interval> getRoomOccupationIntervals(YearMonthDay begin, YearMonthDay end,
	    HourMinuteSecond beginTime, HourMinuteSecond endTime, Integer frequency, Integer startWeek, DiaSemana diaSemana) {

	List<Interval> result = new ArrayList<Interval>();		
	if (startWeek != null && startWeek.intValue() == 2) {
	    begin = begin.plusDays(7);	    
	}	
	if(diaSemana != null) {
	    begin = begin.toDateTimeAtMidnight().withDayOfWeek(diaSemana.getDiaSemanaInDayOfWeekJodaFormat()).toYearMonthDay();
	}

	if(frequency == null) {            
	    if (!begin.isAfter(end)) {
                result.add(createNewInterval(begin, end, beginTime, endTime));
                return result;
            }            
	} else {
            int numberOfDaysToSum = (frequency.intValue() == 1) ? 1 : (frequency.intValue() - 1) * 7;
            while (true) {
                if (begin.isAfter(end)) {
                    break;
                }
                result.add(createNewInterval(begin, begin, beginTime, endTime));
                begin = begin.plusDays(numberOfDaysToSum);
            }
	}
	return result; 
    }
             
    private static Interval createNewInterval(YearMonthDay begin, YearMonthDay end, HourMinuteSecond beginTime, HourMinuteSecond endTime) {	
	return new Interval(
		begin.toDateTime(new TimeOfDay(beginTime.getHour(), beginTime.getMinuteOfHour(), 0)),			
		end.toDateTime(new TimeOfDay(endTime.getHour(), endTime.getMinuteOfHour(), 0)));
    }
        
    
    public static Set<RoomOccupation> getActivePunctualRoomOccupations(){	
	Set<RoomOccupation> result = new TreeSet<RoomOccupation>(COMPARATOR_BY_BEGIN_DATE);
	for (RoomOccupation occupation : RootDomainObject.getInstance().getRoomOccupationsSet()) {
	    if(occupation.getGenericEvent() != null && !occupation.getLastInstant().isBeforeNow()) {
		result.add(occupation);
	    }	
	}	
	return result;	
    }
    
    public Calendar getStartTime() {
        if (this.getStartTimeDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getStartTimeDate());
            return result;
        }
        return null;
    }

    public void setStartTime(Calendar calendar) {
        if (calendar != null) {
            this.setStartTimeDate(calendar.getTime());
        } else {
            this.setStartTimeDate(null);
        }
    }

    public Calendar getEndTime() {
        if (this.getEndTimeDate() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEndTimeDate());
            return result;
        }
        return null;
    }

    public void setEndTime(Calendar calendar) {
        if (calendar != null) {
            this.setEndTimeDate(calendar.getTime());
        } else {
            this.setEndTimeDate(null);
        }
    }

    @Override
    public void setStartTimeDateHourMinuteSecond(HourMinuteSecond startTimeDateHourMinuteSecond) {
        final HourMinuteSecond hourMinuteSecond = eliminateSeconds(startTimeDateHourMinuteSecond);
        super.setStartTimeDateHourMinuteSecond(hourMinuteSecond);
    }

    @Override
    public void setEndTimeDateHourMinuteSecond(HourMinuteSecond endTimeDateHourMinuteSecond) {
        final HourMinuteSecond hourMinuteSecond = eliminateSeconds(endTimeDateHourMinuteSecond);
        super.setEndTimeDateHourMinuteSecond(hourMinuteSecond);
    }

    private HourMinuteSecond eliminateSeconds(final HourMinuteSecond hourMinuteSecond) {
        return hourMinuteSecond == null ? null : new HourMinuteSecond(hourMinuteSecond.getHour(), hourMinuteSecond.getMinuteOfHour(), 0);
    }
    
    public String getPrettyPrint() {
	StringBuilder builder = new StringBuilder();
	if(getFrequency() == null) {
	    builder.append(getPeriod().getStartYearMonthDay().toString("dd/MM/yyyy")).append(" ").append(getPresentationBeginTime());
	    builder.append(" - ").append(getPeriod().getEndYearMonthDay().toString("dd/MM/yyyy")).append(" ").append(getPresentationEndTime());	   	 
	}
	else {
	    builder.append(getPeriod().getStartYearMonthDay().toString("dd/MM/yyyy")).append(" - ").append(getPeriod().getEndYearMonthDay().toString("dd/MM/yyyy"));	    
	    builder.append(" (").append(getPresentationBeginTime()).append(" - ").append(getPresentationEndTime()).append(")");
	}
	return builder.toString();
    }
    
    public String getPresentationBeginTime() {
	return getStartTimeDateHourMinuteSecond().toString("HH:mm");
    }
    
    public String getPresentationEndTime() {
	return getEndTimeDateHourMinuteSecond().toString("HH:mm");
    }
    
    public String getPresentationBeginDate() {
	return getPeriod().getStartYearMonthDay().toString("dd MMMM yyyy", locale) + " (" + getPeriod().getStartYearMonthDay().toDateTimeAtMidnight().toString("E", locale) + ")";
    }
    
    public String getPresentationEndDate() {
	return getPeriod().getEndYearMonthDay().toString("dd MMMM yyyy", locale) + " (" + getPeriod().getEndYearMonthDay().toDateTimeAtMidnight().toString("E", locale) + ")";
    }
    
    public static boolean periodQuinzenalContainsWeekPeriod(Calendar startDate, Calendar endDate,
            int startWeek, DiaSemana weekDay, Calendar day, Calendar endDay, OccupationPeriod nextPeriod) {
      
	ArrayList listWeekly = weeklyDatesInPeriod(day, endDay, weekDay, nextPeriod);
        ArrayList listQuinzenal = quinzenalDatesInPeriod(startDate, endDate, startWeek, weekDay);
        for (int i = 0; i < listQuinzenal.size(); i++) {
            Calendar quinzenalDate = (Calendar) listQuinzenal.get(i);
            for (int j = 0; j < listWeekly.size(); j++) {
                Calendar date = (Calendar) listWeekly.get(j);
                if (CalendarUtil.equalDates(quinzenalDate, date)) {
                    return true;
                }
                if (date.after(quinzenalDate)) {
                    break;
                }
            }
        }
        return false;
    }    

    public static ArrayList quinzenalDatesInPeriod(Calendar startDate, Calendar endDate, int startWeek, DiaSemana weekDay) {
       
	ArrayList<Calendar> list = new ArrayList<Calendar>();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(startDate.getTimeInMillis());
        date.add(Calendar.DATE, weekDay.getDiaSemana().intValue() - date.get(Calendar.DAY_OF_WEEK));
        if (startWeek == 2) {
            date.add(Calendar.DATE, 7);
        }

        if (!date.after(endDate)) {
            list.add(date);
        }
        return list;
    }

    public static ArrayList weeklyDatesInPeriod(Calendar day, Calendar endDay, DiaSemana weekDay, OccupationPeriod nextPeriod) {
	
        ArrayList<Calendar> list = new ArrayList<Calendar>();
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(day.getTimeInMillis());
        date.add(Calendar.DATE, weekDay.getDiaSemana().intValue() - date.get(Calendar.DAY_OF_WEEK));
        while (true) {
            if (date.after(endDay)) {
                if (nextPeriod == null) {
                    break;
                }
                int interval = nextPeriod.getStartDate().get(Calendar.DAY_OF_YEAR)
                        - endDay.get(Calendar.DAY_OF_YEAR) - 1;
                if (interval < 0) {
                    interval = nextPeriod.getStartDate().get(Calendar.DAY_OF_YEAR) - 1;
                    interval += (endDay.getActualMaximum(Calendar.DAY_OF_YEAR) - endDay
                            .get(Calendar.DAY_OF_YEAR));
                }

                day = nextPeriod.getStartDate();
                endDay = nextPeriod.getEndDate();
                nextPeriod = nextPeriod.getNextPeriod();

                int weeksToJump = interval / 7;
                date.add(Calendar.DATE, 7 * weeksToJump);
                if (date.before(day)) {
                    date.add(Calendar.DATE, 7);
                }
            } else {
                Calendar dateToAdd = Calendar.getInstance();
                dateToAdd.setTimeInMillis(date.getTimeInMillis());
                list.add(dateToAdd);
                date.add(Calendar.DATE, 7);
            }

        }
        return list;
    }
}
