package ServidorApresentacao.TagLib.sop.v3;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author jpvl
 */
public class TimeTableRenderer {

    //	private Integer endHour;
    private Integer startHour;

    private Integer slotSize;

    private TimeTable timeTable;

    private LessonSlotContentRenderer lessonSlotContentRenderer;

    private ColorPicker colorPicker;

    /**
     * Constructor TimeTableRenderer.
     * 
     * @param timeTable
     * @param lessonSlotContentRenderer
     * @param integer
     * @param integer1
     * @param integer2
     */
    public TimeTableRenderer(TimeTable timeTable, LessonSlotContentRenderer lessonSlotContentRenderer,
            Integer slotSize, Integer startHour, Integer endHour, ColorPicker colorPicker) {
        if (colorPicker == null)
            throw new IllegalArgumentException(this.getClass().getName()
                    + ":Color picker must be not null!");
        this.timeTable = timeTable;
        this.lessonSlotContentRenderer = lessonSlotContentRenderer;

        //		this.endHour = endHour;
        this.startHour = startHour;
        this.slotSize = slotSize;
        this.colorPicker = colorPicker;
    }

    public StringBuffer render() {
        StringBuffer strBuffer = new StringBuffer("");

        TimeTableSlot[][] grid = timeTable.getTimeTableGrid();

        strBuffer.append("<table class='timetable' cellspacing='0' cellpadding='0' width='90%'>");

        renderHeader(strBuffer);

        for (int hourIndex = 0; hourIndex < timeTable.getNumberOfHours().intValue(); hourIndex++) {

            strBuffer.append("<tr>\r\n");
            strBuffer.append("<td width='15%' class='period-hours'>");
            strBuffer.append(getHourLabelByIndex(hourIndex)).append("</td>\r\n");

            /* iterate over days */
            for (int dayIndex = 0; dayIndex < timeTable.getNumberOfDays().intValue(); dayIndex++) {
                DayColumn dayColumn = timeTable.getDayColumn(dayIndex);

                TimeTableSlot timeTableSlot = grid[dayIndex][hourIndex];

                if (timeTableSlot != null) {

                    List colisionList = timeTableSlot.getLessonSlotList();
                    InfoLessonWrapper[] lessonSlotListResolved = resolveColisions(colisionList,
                            timeTable.getDayColumn(dayIndex));

                    for (int slotIndex = 0; slotIndex < lessonSlotListResolved.length; slotIndex++) {
                        InfoLessonWrapper infoLessonWrapper = lessonSlotListResolved[slotIndex];

                        strBuffer.append("<td ");

                        if (infoLessonWrapper != null) {
                            strBuffer.append("style='background-color: ").append(
                                    colorPicker.getBackgroundColor(infoLessonWrapper)).append("' ");
                        }

                        int slotColspan = calculateColspan(infoLessonWrapper, lessonSlotListResolved,
                                slotIndex, dayColumn);

                        if (slotColspan > 1) {
                            strBuffer.append(" colspan ='").append(slotColspan).append("'");
                            slotIndex += slotColspan - 1;
                        }

                        strBuffer.append(" class='");
                        strBuffer.append(getSlotCssClass(infoLessonWrapper, hourIndex));
                        strBuffer.append("' ");
                        if (infoLessonWrapper != null) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("k:mm");
                            Calendar start = infoLessonWrapper.getLessonSlot().getInfoLessonWrapper()
                                    .getInfoShowOccupation().getInicio();
                            Calendar end = infoLessonWrapper.getLessonSlot().getInfoLessonWrapper()
                                    .getInfoShowOccupation().getFim();
                            String startLabel = dateFormat.format(start.getTime());
                            String endLabel = dateFormat.format(end.getTime());
                            strBuffer.append(" title='").append(startLabel).append("-").append(endLabel)
                                    .append("'");
                        }
                        strBuffer.append(">");

                        if ((infoLessonWrapper != null)
                                && (infoLessonWrapper.getLessonSlot().getStartIndex() == hourIndex)) {
                            strBuffer.append(this.lessonSlotContentRenderer.render(infoLessonWrapper
                                    .getLessonSlot()));
                        } else {
                            strBuffer.append("&nbsp;");
                        }
                        strBuffer.append("</td>\r\n");
                    }
                } else {
                    /** no lessons */
                    int colspan = dayColumn.getMaxColisionSize().intValue();
                    String cssClass = getSlotCssClass(null, hourIndex);
                    strBuffer.append("<td ").append(" class='").append(cssClass).append("' ");
                    if (colspan > 1){
                        strBuffer.append("colspan='").append(colspan).append("'");
                    }
                    strBuffer.append(">").append("&nbsp;").append("</td>\r\n");
                }

            }
            strBuffer.append("</tr>\r\n");
        }

        strBuffer.append("</table>");
        return strBuffer;
    }

    /**
     * @param infoLessonWrapper
     * @param lessonSlotListResolved
     * @param dayColumn
     * @param slotIndex
     * @return
     */
    private int calculateColspan(InfoLessonWrapper infoLessonWrapper,
            InfoLessonWrapper[] lessonSlotListResolved, int currentSlotIndex, DayColumn dayColumn) {
        int colspan = 1;
        if (infoLessonWrapper != null && (infoLessonWrapper.getNumberOfCollisions().intValue() == 0)) {
            colspan = dayColumn.getMaxColisionSize().intValue();
        } else if (infoLessonWrapper == null) {
            for (int i = currentSlotIndex + 1; i < lessonSlotListResolved.length; i++) {
                InfoLessonWrapper next = lessonSlotListResolved[i];
                if (next == null) {
                    colspan++;
                } else {
                    break;
                }
            }
        }
        return colspan;
    }

    /**
     * 
     * @param infoLessonWrapper
     *            may be null
     * @param hourIndex
     * @param dayIndex
     * @param timeTableGrid
     * @param slotColisions
     *            may be null
     * @param slotIndex
     * @return String
     */
    private String getSlotCssClass(InfoLessonWrapper infoLessonWrapper, int hourIndex) {
        String css = "period-empty-slot";
        if (infoLessonWrapper != null) {

            LessonSlot lessonSlot = infoLessonWrapper.getLessonSlot();
            if (lessonSlot.getStartIndex() == hourIndex) {
                css = "period-first-slot";
            } else if (lessonSlot.getEndIndex() == hourIndex) {
                css = "period-last-slot";
            } else {
                css = "period-middle-slot";
            }
        }
        return css;
    }

    /**
     * Method renderHeader.
     * 
     * @param strBuffer
     */
    private void renderHeader(StringBuffer strBuffer) {

        //        strBuffer.append("<col />");
        //        for (int index = 0; index <
        // this.timeTable.getNumberOfDays().intValue(); index++) {
        //            strBuffer.append("<col
        // span='").append(timeTable.getDayColumn(index).getMaxColisionSize())
        //                    .append("'/>\r\n");
        //        }

        strBuffer.append("<th width='15%'>horas/dias</th>\r\n");
        int cellWidth = (100 - 15) / timeTable.getNumberOfDays().intValue();
        for (int index = 0; index < this.timeTable.getNumberOfDays().intValue(); index++) {
            strBuffer.append("<th colspan='").append(timeTable.getDayColumn(index).getMaxColisionSize())
                    .append("' width='").append(cellWidth).append("%'>\r\n").append(
                            timeTable.getDayColumn(index).getLabel()).append("</th>\r\n");
        }
    }

    protected InfoLessonWrapper[] resolveColisions(List lessonSlotList, DayColumn dayColumn) {
        InfoLessonWrapper[] list = new InfoLessonWrapper[dayColumn.getMaxColisionSize().intValue()];

        Iterator lessonSlotListIterator = lessonSlotList.iterator();

        List lessonSlotNotLocked = new ArrayList();

        while (lessonSlotListIterator.hasNext()) {
            LessonSlot lessonSlot = (LessonSlot) lessonSlotListIterator.next();

            InfoLessonWrapper infoLessonWrapper = lessonSlot.getInfoLessonWrapper();

            if (infoLessonWrapper.isLocked()) {
                list[infoLessonWrapper.getSlotIndex()] = infoLessonWrapper;
            } else {
                lessonSlotNotLocked.add(infoLessonWrapper);
            }
        }

        Collections.sort(lessonSlotNotLocked);

        Iterator lessonSlotNotLockedIterator = lessonSlotNotLocked.iterator();
        while (lessonSlotNotLockedIterator.hasNext()) {
            InfoLessonWrapper infoLessonWrapper = (InfoLessonWrapper) lessonSlotNotLockedIterator.next();

            for (int index = 0; index < list.length; index++) {
                if (list[index] == null) {
                    list[index] = infoLessonWrapper;
                    infoLessonWrapper.setLocked(true, index);
                    break;
                }
            }
        }
        return list;

    }

    private StringBuffer getHourLabelByIndex(int hourIndex) {
        StringBuffer label = new StringBuffer("");

        double startLabelHour = startHour.doubleValue() + new Integer(hourIndex).doubleValue()
                / (60.0 / slotSize.doubleValue());
        double startMinutes = startLabelHour - Math.floor(startLabelHour);

        String startMinutesLabel = String.valueOf((int) (startMinutes * 60));
        if (startMinutesLabel.length() == 1) {
            startMinutesLabel = "0" + startMinutesLabel;
        }

        double endLabelHour = startHour.doubleValue() + new Integer(hourIndex + 1).doubleValue()
                / (60.0 / slotSize.doubleValue());

        double endMinutes = endLabelHour - Math.floor(endLabelHour);

        String endMinutesLabel = String.valueOf((int) (endMinutes * 60));
        if (endMinutesLabel.length() == 1) {
            endMinutesLabel = "0" + endMinutesLabel;
        }

        return label.append((int) startLabelHour).append(":").append(startMinutesLabel).append("-")
                .append((int) endLabelHour).append(":").append(endMinutesLabel);

    }
}