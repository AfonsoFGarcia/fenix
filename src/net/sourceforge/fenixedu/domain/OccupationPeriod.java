/*
 * Created on 14/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.joda.time.YearMonthDay;

/**
 * @author Ana e Ricardo
 * 
 */
public class OccupationPeriod extends OccupationPeriod_Base {

    public OccupationPeriod() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    }

    public OccupationPeriod(Date startDate, Date endDate) {
    	this();
        checkDates(startDate, endDate);
        this.setStart(startDate);
        this.setEnd(endDate);
    }

    private void checkDates(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            throw new DomainException("error.occupationPeriod.invalid.dates");
        }
    }

    public Calendar getStartDate() {
        if (this.getStart() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getStart());
            return result;
        }
        return null;
    }

    public void setEndDate(Calendar calendar) {
        if (calendar != null) {
            this.setEnd(calendar.getTime());
        } else {
            this.setEnd(null);
        }
    }

    public Calendar getEndDate() {
        if (this.getEnd() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnd());
            return result;
        }
        return null;
    }

    public void setStartDate(Calendar calendar) {
        if (calendar != null) {
            this.setStart(calendar.getTime());
        } else {
            this.setStart(null);
        }
    }

    public Calendar getEndDateOfComposite() {
        Calendar end = this.getEndDate();
        OccupationPeriod period = this.getNextPeriod();
        while (period != null) {
            end = period.getEndDate();
            period = period.getNextPeriod();
        }
        return end;
    }

    public boolean intersectPeriods(final Calendar start, final Calendar end) {
        return CalendarUtil.intersectDates(start, end, getStartDate(), getEndDate());
    }

    public boolean intersectPeriods(OccupationPeriod period) {
        return intersectPeriods(period.getStartDate(), period.getEndDate());
    }

    public boolean containsDay(Calendar day) {
        return !(this.getStartDate().after(day) || this.getEndDate().before(day));
    }

    public boolean containsDay(Date day) {
        return !(this.getStart().after(day) || this.getEnd().before(day));
    }
    
    public boolean containsDay(YearMonthDay yearMonthDay) {
        return !(this.getStartYearMonthDay().isAfter(yearMonthDay) || this.getEndYearMonthDay().isBefore(yearMonthDay));
    }
    
    public void deleteIfEmpty() {
        if (empty()) {
            delete();
        }
    }

    private boolean empty() {
        return getRoomOccupations().isEmpty()
                && getExecutionDegreesForExamsFirstSemester().isEmpty()
                && getExecutionDegreesForExamsSecondSemester().isEmpty()
                && getExecutionDegreesForLessonsFirstSemester().isEmpty()
                && getExecutionDegreesForLessonsSecondSemester().isEmpty()
                && getExecutionDegreesForGradeSubmissionNormalSeasonFirstSemester().isEmpty()
                && getExecutionDegreesForGradeSubmissionNormalSeasonSecondSemester().isEmpty()
                && getExecutionDegreesForGradeSubmissionSpecialSeason().isEmpty();
    }

    private void delete() {
        final OccupationPeriod previous = getPreviousPeriod();
        final OccupationPeriod next = getNextPeriod();
        if (previous != null && next != null) { 
            previous.setNextPeriod(next);
        } else {
            removeNextPeriod();
            removePreviousPeriod();
        }
        removeRootDomainObject();
        deleteDomainObject();
    }
    
    public static OccupationPeriod readByDatesAndNextOccupationPeriod(Date startDate, Date endDate, OccupationPeriod nextOccupationPeriod) {
        for (OccupationPeriod occupationPeriod : RootDomainObject.getInstance().getOccupationPeriods()) {
            if (DateFormatUtil.equalDates("yyyy-MM-dd", occupationPeriod.getStart(), startDate)
                    && DateFormatUtil.equalDates("yyyy-MM-dd", occupationPeriod.getEnd(), endDate) 
                    && (nextOccupationPeriod == null || (nextOccupationPeriod != null && occupationPeriod.getNextPeriod().equals(nextOccupationPeriod)))) {
                        return occupationPeriod;
            }
        }
        return null;
    }
    
    public static OccupationPeriod readFor(YearMonthDay start, YearMonthDay end) {
        for (final OccupationPeriod occupationPeriod : RootDomainObject.getInstance().getOccupationPeriodsSet()) {
            if (occupationPeriod.getStartYearMonthDay().equals(start) && occupationPeriod.getEndYearMonthDay().equals(end)) {
                return occupationPeriod;
            }
        }
        return null;
    }
    
}
