/*
 * Created on 14/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

import net.sourceforge.fenixedu.util.CalendarUtil;

/**
 * @author Ana e Ricardo
 * 
 */
public class Period extends Period_Base {

    public Period() {
    }

    public Period(Calendar startDate, Calendar endDate) {
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }

    /**
     * @return
     */
    public Calendar getStartDate() {
        if (this.getStart() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getStart());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setEndDate(Calendar calendar) {
        if (calendar != null) {
            this.setEnd(calendar.getTime());
        } else {
            this.setEnd(null);
        }
    }

    /**
     * @return
     */
    public Calendar getEndDate() {
        if (this.getEnd() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnd());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setStartDate(Calendar calendar) {
        if (calendar != null) {
            this.setStart(calendar.getTime());
        } else {
            this.setStart(null);
        }
    }

    public Calendar getEndDateOfComposite() {
        Calendar end = this.getEndDate();
        IPeriod period = this.getNextPeriod();
        while (period != null) {
            end = period.getEndDate();
            period = period.getNextPeriod();
        }
        return end;
    }

    public boolean intersectPeriods(final Calendar start, final Calendar end) {
        return CalendarUtil.intersectDates(start, end, getStartDate(), getEndDate());
    }

    public boolean intersectPeriods(IPeriod period) {
        return intersectPeriods(period.getStartDate(), period.getEndDate());
    }

    public boolean containsDay(Calendar day) {
        return !(this.getStartDate().after(day) || this.getEndDate().before(day));
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
                && getExecutionDegreesForLessonsSecondSemester().isEmpty();
    }

    private void delete() {
        final IPeriod previous = getPreviousPeriod();
        final IPeriod next = getNextPeriod();
        if (previous != null && next != null) { 
            previous.setNextPeriod(next);
        } else {
            removeNextPeriod();
            removePreviousPeriod();
        }

        deleteDomainObject();
    }
}
