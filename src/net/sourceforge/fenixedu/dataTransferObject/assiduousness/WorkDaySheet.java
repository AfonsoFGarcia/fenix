package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class WorkDaySheet implements Serializable {
    YearMonthDay date;

    String workScheduleAcronym;

    Period balanceTime;

    Duration unjustifiedTime;

    String notes;

    List<TimeOfDay> clockings = new ArrayList<TimeOfDay>();

    public Period getBalanceTime() {
        return balanceTime;
    }

    public void setBalanceTime(Period balanceTime) {
        this.balanceTime = balanceTime;
    }

    public List<TimeOfDay> getClockings() {
        return clockings;
    }

    public void setClockings(List<TimeOfDay> clockings) {
        this.clockings = clockings;
    }

    public void addClockings(TimeOfDay clocking) {
        this.clockings.add(clocking);
    }

    public YearMonthDay getDate() {
        return date;
    }

    public void setDate(YearMonthDay date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        if(getDate() == null) {
            return "";
        }
        return getDate().toString("dd/MM/yyyy");
    }

    public String getBalanceTimeFormatted() {
        Period balancePeriod = getBalanceTime();
        StringBuffer result = new StringBuffer();
        result.append(balancePeriod.getHours());
        result.append(":");
        if (balancePeriod.getMinutes() > -10 && balancePeriod.getMinutes() < 10) {
            result.append("0");
        }
        if (balancePeriod.getMinutes() < 0) {
            result.append((-balancePeriod.getMinutes()));
            if (!result.toString().startsWith("-")) {
                result = new StringBuffer("-").append(result);
            }
        } else {
            result.append(balancePeriod.getMinutes());
        }
        return result.toString();
    }    
    
    public String getWeekDay() {
        if(getDate() == null) {
            return "";
        }
        ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources");
        return  bundle.getString(WeekDay.fromJodaTimeToWeekDay(getDate().toDateTimeAtMidnight())
                        .toString()+"_ACRONYM");
    }
}
