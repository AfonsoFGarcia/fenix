package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.DateInterval;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class Schedule extends Schedule_Base {

    public Schedule(Assiduousness assiduousness, YearMonthDay beginDate, YearMonthDay endDate,
            DateTime lastModifiedDate, Employee modifiedBy) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setAssiduousness(assiduousness);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setException(false);
        setLastModifiedDate(lastModifiedDate);
        setModifiedBy(modifiedBy);
    }

    public Schedule(Assiduousness assiduousness, YearMonthDay beginDate, YearMonthDay endDate,
            Boolean exception, DateTime lastModifiedDate, Employee modifiedBy) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setAssiduousness(assiduousness);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setException(exception);
        setLastModifiedDate(lastModifiedDate);
        setModifiedBy(modifiedBy);
    }

    public DateInterval getValidInterval() {
        return new DateInterval(getBeginDate(), getEndDate());
    }

    // Return true if the Schedule is valid in the interval
    public boolean isDefinedInInterval(DateInterval interval) {
        return getValidInterval().containsInterval(interval);
    }

    // Return true if the Schedule valid interval constains date
    public boolean isDefinedInDate(YearMonthDay date) {
        return getValidInterval().containsDate(date);
    }

    // Returns the valid interval week number of a given YearMonthDay date
    public int getValidIntervalWeekNumberOfDate(YearMonthDay date) {
        return (new DateInterval(getBeginDate(), date)).numberOfWeeks() + 1;
    }

    // Returns the Employee's work schedule for a particular date
    public WorkSchedule workScheduleWithDate(YearMonthDay date) {
        int weekNumber = getValidIntervalWeekNumberOfDate(date);
        int maxWorkWeek = getMaximumWorkWeekNumber();
        for (WorkSchedule workSchedule : getWorkSchedules()) {
            if (workSchedule.isDefinedInDate(date, weekNumber, maxWorkWeek)) {
                return workSchedule;
            }
        }
        return null;
    }

    public int getMaximumWorkWeekNumber() {
        int maxWorkWeek = 0;
        for (WorkSchedule workSchedule : getWorkSchedules()) {
            if (workSchedule.getPeriodicity().getWorkWeekNumber().intValue() > maxWorkWeek) {
                maxWorkWeek = workSchedule.getPeriodicity().getWorkWeekNumber().intValue();
            }
        }
        return maxWorkWeek;
    }

    public void delete() {
        removeRootDomainObject();
        removeAssiduousness();
        removeModifiedBy();
        List<WorkSchedule> workSchedules = new ArrayList<WorkSchedule>(getWorkSchedules());
        for (WorkSchedule workSchedule : workSchedules) {
            getWorkSchedules().remove(workSchedule);
            workSchedule.delete();
        }
        deleteDomainObject();
    }
}
