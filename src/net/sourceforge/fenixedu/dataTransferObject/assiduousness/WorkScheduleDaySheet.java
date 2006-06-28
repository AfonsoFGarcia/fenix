package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;

import org.joda.time.Chronology;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class WorkScheduleDaySheet implements Serializable {

    WorkSchedule workSchedule;

    String weekDaySchedule;

    public String getWeekDaySchedule() {
        return weekDaySchedule;
    }

    public void setWeekDaySchedule(String weekDaySchedule) {
        this.weekDaySchedule = weekDaySchedule;
    }

    public String getNormalWorkPeriod() {
        if (getWorkSchedule() == null
                || getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod() == null) {
            return "";
        }
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        String firstPeriod = fmt.print(getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod()
                .getFirstPeriod());
        String firstPeriodEnd = fmt.print(getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod()
                .getEndFirstPeriod());
        StringBuilder result = new StringBuilder();
        result.append(firstPeriod).append(" - ").append(firstPeriodEnd);
        if (getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod().getSecondPeriod() != null) {
            String secondPeriod = fmt.print(getWorkSchedule().getWorkScheduleType()
                    .getNormalWorkPeriod().getSecondPeriod());
            String secondPeriodEnd = fmt.print(getWorkSchedule().getWorkScheduleType()
                    .getNormalWorkPeriod().getEndSecondPeriod());
            result.append(" <br/> ").append(secondPeriod).append(" - ").append(secondPeriodEnd);
        }
        return result.toString();
    }

    public String getFixedWorkPeriod() {
        if (getWorkSchedule() == null
                || getWorkSchedule().getWorkScheduleType().getFixedWorkPeriod() == null) {
            return "";
        }
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        String firstPeriod = fmt.print(getWorkSchedule().getWorkScheduleType().getFixedWorkPeriod()
                .getFirstPeriod());
        String firstPeriodEnd = fmt.print(getWorkSchedule().getWorkScheduleType().getFixedWorkPeriod()
                .getEndFirstPeriod());
        StringBuilder result = new StringBuilder();
        result.append(firstPeriod).append(" - ").append(firstPeriodEnd);
        if (getWorkSchedule().getWorkScheduleType().getFixedWorkPeriod().getSecondPeriod() != null) {
            String secondPeriod = fmt.print(getWorkSchedule().getWorkScheduleType().getFixedWorkPeriod()
                    .getSecondPeriod());
            String secondPeriodEnd = fmt.print(getWorkSchedule().getWorkScheduleType()
                    .getFixedWorkPeriod().getEndSecondPeriod());
            result.append(" <br/> ").append(secondPeriod).append(" - ").append(secondPeriodEnd);
        }
        return result.toString();
    }

    public String getMealPeriod() {
        if (getWorkSchedule() == null || getWorkSchedule().getWorkScheduleType().getMeal() == null) {
            return "";
        }
        Chronology chronology = GregorianChronology.getInstanceUTC();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        String beginMealBreak = fmt.print(getWorkSchedule().getWorkScheduleType().getMeal()
                .getBeginMealBreak());
        String endMealBreak = fmt.print(getWorkSchedule().getWorkScheduleType().getMeal()
                .getEndMealBreak());
        TimeOfDay time = new TimeOfDay(getWorkSchedule().getWorkScheduleType().getMeal()
                .getMinimumMealBreakInterval().getMillis(), chronology);
        String minimum = fmt.print(time);
        time = new TimeOfDay(getWorkSchedule().getWorkScheduleType().getMeal()
                .getMandatoryMealDiscount().getMillis(), chronology);
        String mandatory = fmt.print(time);

        StringBuilder result = new StringBuilder();
        result.append(beginMealBreak).append(" - ").append(endMealBreak).append(" <br/> Min:").append(
                minimum).append(" <br/> Obr:").append(mandatory);

        return result.toString();
    }

    public WorkSchedule getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(WorkSchedule workSchedule) {
        this.workSchedule = workSchedule;
    }

}
