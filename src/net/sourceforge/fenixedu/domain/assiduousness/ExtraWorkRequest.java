package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.DateTime;
import org.joda.time.Partial;

public class ExtraWorkRequest extends ExtraWorkRequest_Base {

    public ExtraWorkRequest(Partial partialPayingDate, Unit unit, Employee employee, Integer nightHours,
            Integer extraNightHours, Integer extraNightDays, Integer holidayHours,
            Integer saturdayHours, Integer sundayHours, Integer workdayHours, Boolean addToVacations,
            Boolean addToWeekRestTime, Employee modifiedBy, Double totalValue, boolean approved,
            Partial hoursDoneIn, Integer normalVacationsDays, Double normalVacationsAmount,
            Double accumulatedNormalVacationsAmount, Integer nightVacationsDays,
            Double nightVacationsAmount, Double accumulatedNightVacationsAmount) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setPartialPayingDate(partialPayingDate);
        setUnit(unit);
        setAssiduousness(employee.getAssiduousness());
        setNightHours(nightHours);
        setExtraNightHours(extraNightHours);
        setExtraNightDays(extraNightDays);
        setHolidayHours(holidayHours);
        setSaturdayHours(saturdayHours);
        setSundayHours(sundayHours);
        setWorkdayHours(workdayHours);
        setAddToVacations(addToVacations);
        setAddToWeekRestTime(addToWeekRestTime);
        setLastModifiedDate(new DateTime());
        setModifiedBy(modifiedBy);
        setAmount(totalValue);
        setApproved(approved);
        setHoursDoneInPartialDate(hoursDoneIn);
        setNormalVacationsDays(normalVacationsDays);
        setNormalVacationsAmount(normalVacationsAmount);
        setAccumulatedNormalVacationsAmount(accumulatedNormalVacationsAmount);
        setNightVacationsDays(nightVacationsDays);
        setNightVacationsAmount(nightVacationsAmount);
        setAccumulatedNightVacationsAmount(accumulatedNightVacationsAmount);
    }

    public void edit(Integer nightHours, Integer extraNightHours, Integer extraNightDays,
            Integer holidayHours, Integer saturdayHours, Integer sundayHours, Integer workdayHours,
            Boolean addToVacations, Boolean addToWeekRestTime, Employee modifiedBy,
            Partial hoursDoneInPartialDate, Double amount, Integer normalVacationsDays,
            Double normalVacationsAmount, Double accumulatedNormalVacationsAmount,
            Integer nightVacationsDays, Double nightVacationsAmount,
            Double accumulatedNightVacationsAmount) {
        setNightHours(nightHours);
        setExtraNightHours(extraNightHours);
        setExtraNightDays(extraNightDays);
        setHolidayHours(holidayHours);
        setSaturdayHours(saturdayHours);
        setSundayHours(sundayHours);
        setWorkdayHours(workdayHours);
        setAddToVacations(addToVacations);
        setAddToWeekRestTime(addToWeekRestTime);
        setLastModifiedDate(new DateTime());
        setModifiedBy(modifiedBy);
        setHoursDoneInPartialDate(hoursDoneInPartialDate);
        setAmount(amount);
        setNormalVacationsDays(normalVacationsDays);
        setNormalVacationsAmount(normalVacationsAmount);
        setAccumulatedNormalVacationsAmount(accumulatedNormalVacationsAmount);
        setNightVacationsDays(nightVacationsDays);
        setNightVacationsAmount(nightVacationsAmount);
        setAccumulatedNightVacationsAmount(accumulatedNightVacationsAmount);
    }

    public int getTotalHours() {
        int result = 0;
        if (getNightHours() != null) {
            result = result + getNightHours();
        }
        if (getExtraNightHours() != null) {
            result = result + getExtraNightHours();
        }
        if (getHolidayHours() != null) {
            result = result + getHolidayHours();
        }
        if (getSaturdayHours() != null) {
            result = result + getSaturdayHours();
        }
        if (getSundayHours() != null) {
            result = result + getSundayHours();
        }
        if (getWorkdayHours() != null) {
            result = result + getWorkdayHours();
        }
        return result;
    }

    public void delete() {
        if (canBeDeleted()) {
            removeRootDomainObject();
            removeAssiduousness();
            removeModifiedBy();
            removeUnit();
            deleteDomainObject();
        }
    }

    public boolean canBeDeleted() {
        ClosedMonth payingMonth = ClosedMonth.getClosedMonth(getPartialPayingDate());
        return payingMonth == null || (payingMonth != null && !payingMonth.getClosedForExtraWork());
    }
}
