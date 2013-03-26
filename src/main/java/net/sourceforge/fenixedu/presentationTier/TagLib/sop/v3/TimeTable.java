package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.apache.struts.Globals;
import org.apache.struts.util.RequestUtils;

/**
 * @author jpvl
 */
public class TimeTable {
    private List days;

    private TimeTableSlot[][] timeTableGrid;

    private int minimumHourInMinutes;

    private Integer slotSize;

    private Integer numberOfDays, numberOfHours;

    private HashMap infoLessonWrapperMap;

    /**
     * Constructor TimeTable.
     * 
     * @param numberOfHours
     * @param numberOfDays
     */
    public TimeTable(Integer numberOfHours, Integer numberOfDays, Calendar minimumHour, Integer slotSize, Locale locale,
            PageContext pageContext) {
        this.minimumHourInMinutes = getMinutes(minimumHour);
        this.slotSize = slotSize;

        this.numberOfDays = numberOfDays;
        this.numberOfHours = numberOfHours;

        this.infoLessonWrapperMap = new HashMap();

        days = new ArrayList(numberOfDays.intValue());
        for (int day = 0; day < numberOfDays.intValue(); day++) {
            DayColumn column = new DayColumn(day, getDiaSemanaLabel(day, locale, pageContext));
            days.add(day, column);
        }
        timeTableGrid = new TimeTableSlot[numberOfDays.intValue()][numberOfHours.intValue()];
    }

    /**
     * Method getDiaSemanaLabel.
     * 
     * @param day
     */
    private String getDiaSemanaLabel(int day, Locale locale, PageContext pageContext) {
        switch (day) {
        case 0:
            return getMessageResource(pageContext, "public.degree.information.label.monday", locale);
        case 1:
            return getMessageResource(pageContext, "public.degree.information.label.tusday", locale);
        case 2:
            return getMessageResource(pageContext, "public.degree.information.label.wednesday", locale);
        case 3:
            return getMessageResource(pageContext, "public.degree.information.label.thursday", locale);
        case 4:
            return getMessageResource(pageContext, "public.degree.information.label.friday", locale);
        case 5:
            return getMessageResource(pageContext, "public.degree.information.label.saturday", locale);
        default:
            return getMessageResource(pageContext, "public.degree.information.label.invalid", locale) + day;

        }
    }

    private String getMessageResource(PageContext pageContext, String key, Locale locale) {
        try {
            return RequestUtils.message(pageContext, "PUBLIC_DEGREE_INFORMATION", Globals.LOCALE_KEY, key);
        } catch (JspException e) {
            return "???" + key + "???";
        }
    }

    /**
     * This method will break infoLesson in minute slots.
     * 
     * @param infoLesson
     */
    public void addLesson(InfoShowOccupation infoShowOccupation) {

        int dayIndex = getDayIndex(infoShowOccupation.getDiaSemana());

        DayColumn dayColumn = (DayColumn) this.days.get(dayIndex);

        int startIndex = getHourIndex(infoShowOccupation.getInicio(), this.minimumHourInMinutes, this.slotSize.intValue());
        int endIndex = getHourIndex(infoShowOccupation.getFim(), this.minimumHourInMinutes, this.slotSize.intValue());

        /* break lesson in slots */
        for (int hourIndex = startIndex; hourIndex < endIndex; hourIndex++) {
            LessonSlot lessonSlot = new LessonSlot(getInfoLessonWrapper(infoShowOccupation), startIndex, endIndex - 1);
            TimeTableSlot timeTableSlot = getTimeTableSlot(dayColumn, hourIndex);
            timeTableSlot.addLessonSlot(lessonSlot);
        }
    }

    /**
     * Method getInfoLessonWrapper.
     * 
     * @param infoLesson
     */
    private InfoLessonWrapper getInfoLessonWrapper(InfoShowOccupation infoShowOccupation) {
        InfoLessonWrapper infoLessonWrapper = (InfoLessonWrapper) this.infoLessonWrapperMap.get(infoShowOccupation);
        if (infoLessonWrapper == null) {
            infoLessonWrapper = new InfoLessonWrapper(infoShowOccupation);
            this.infoLessonWrapperMap.put(infoShowOccupation, infoLessonWrapper);
        }
        return infoLessonWrapper;
    }

    /**
     * Method getTimeTableSlot.
     * 
     * @param dayIndex
     * @param hourIndex
     * @return TimeTableSlot
     */
    private TimeTableSlot getTimeTableSlot(DayColumn day, int hourIndex) {
        TimeTableSlot timeTableSlot = timeTableGrid[day.getIndex()][hourIndex];

        if (timeTableSlot == null) {
            timeTableSlot = new TimeTableSlot(day, new Integer(hourIndex));
            timeTableGrid[day.getIndex()][hourIndex] = timeTableSlot;
        }
        return timeTableSlot;
    }

    private int getDayIndex(DiaSemana day) {
        int dayIndex = -1;
        if (day != null) {
            switch (day.getDiaSemana().intValue()) {
            case DiaSemana.SEGUNDA_FEIRA:
                dayIndex = 0;
                break;
            case DiaSemana.TERCA_FEIRA:
                dayIndex = 1;
                break;
            case DiaSemana.QUARTA_FEIRA:
                dayIndex = 2;
                break;
            case DiaSemana.QUINTA_FEIRA:
                dayIndex = 3;
                break;
            case DiaSemana.SEXTA_FEIRA:
                dayIndex = 4;
                break;
            case DiaSemana.SABADO:
                dayIndex = 5;
                break;
            default:
                dayIndex = -1;
                break;
            }
        }
        return dayIndex;
    }

    public static int getHourIndex(Calendar time, int minimumHourInMinutes, int slotSize) {
        int hourInMinutes = getMinutes(time);
        return (hourInMinutes - minimumHourInMinutes) / slotSize;
    }

    /**
     * Method getMinutes.
     * 
     * @param calendar
     * @return int
     */
    public static int getMinutes(Calendar time) {
        return time.get(Calendar.HOUR_OF_DAY) * 60 + time.get(Calendar.MINUTE);
    }

    /**
     * Returns the timeTableGrid.
     * 
     * @return TimeTableSlot[][]
     */
    public TimeTableSlot[][] getTimeTableGrid() {
        return timeTableGrid;
    }

    /**
     * Returns the numberOfDays.
     * 
     * @return Integer
     */
    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    /**
     * Returns the numberOfHours.
     * 
     * @return Integer
     */
    public Integer getNumberOfHours() {
        return numberOfHours;
    }

    /**
     * Method getDayColumn.
     * 
     * @param dayIndex
     * @return DayColumn
     */
    public DayColumn getDayColumn(int dayIndex) {
        return (DayColumn) this.days.get(dayIndex);
    }

    /**
     * Returns the minimumHourInMinutes.
     * 
     * @return int
     */
    public int getMinimumHourInMinutes() {
        return minimumHourInMinutes;
    }

    /**
     * Returns the slotSize.
     * 
     * @return Integer
     */
    public Integer getSlotSize() {
        return slotSize;
    }

}