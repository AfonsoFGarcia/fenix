package ServidorApresentacao.TagLib.sop.v3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author jpvl
 */
public class TimeTableRenderer {
	private Integer endHour;
	private Integer startHour;
	private Integer slotSize;
	private TimeTable timeTable;
	private LessonSlotContentRenderer lessonSlotContentRenderer;

	/**
	 * Constructor TimeTableRenderer.
	 * @param timeTable
	 * @param lessonSlotContentRenderer
	 * @param integer
	 * @param integer1
	 * @param integer2
	 */
	public TimeTableRenderer(
		TimeTable timeTable,
		LessonSlotContentRenderer lessonSlotContentRenderer,
		Integer slotSize,
		Integer startHour,
		Integer endHour) {
		this.timeTable = timeTable;
		this.lessonSlotContentRenderer = lessonSlotContentRenderer;

		this.endHour = endHour;
		this.startHour = startHour;
		this.slotSize = slotSize;
	}

	public StringBuffer render() {
		StringBuffer strBuffer = new StringBuffer("");

		TimeTableSlot[][] grid = timeTable.getTimeTableGrid();

		strBuffer.append(
			"<table cellspacing='0' cellpadding='0' width='100%'>");

		renderHeader(strBuffer);

		for (int hourIndex = 0;
			hourIndex < timeTable.getNumberOfHours().intValue();
			hourIndex++) {

			strBuffer.append("<tr class='timeTable_line'>\r\n");
			strBuffer.append("<td class='horariosHoras");
			if (hourIndex == 0)
				strBuffer.append("_first");

			strBuffer.append("'>").append(
				getHourLabelByIndex(hourIndex)).append(
				"</td>\r\n");

			/* iterate over days */
			for (int dayIndex = 0;
				dayIndex < timeTable.getNumberOfDays().intValue();
				dayIndex++) {
				DayColumn dayColumn = timeTable.getDayColumn(dayIndex);

				TimeTableSlot timeTableSlot = grid[dayIndex][hourIndex];

				if (timeTableSlot != null) {

					List colisionList = timeTableSlot.getLessonSlotList();
					InfoLessonWrapper[] lessonSlotListResolved =
						resolveColisions(
							colisionList,
							timeTable.getDayColumn(dayIndex));

					int emptyLessonSlotNumber =
						getEmptyLessonSlotNumber(lessonSlotListResolved);

					for (int slotIndex = 0;
						slotIndex < lessonSlotListResolved.length;
						slotIndex++) {
						InfoLessonWrapper infoLessonWrapper =
							lessonSlotListResolved[slotIndex];

						strBuffer.append("<td class='");

						strBuffer.append(
							getSlotCssClass(
								infoLessonWrapper,
								hourIndex,
								dayIndex,
								grid,
								lessonSlotListResolved,
								slotIndex));
						strBuffer.append("' >");

						if (infoLessonWrapper != null) {
							if (infoLessonWrapper
								.getLessonSlot()
								.getStartIndex()
								== hourIndex) {
								strBuffer.append(
									this.lessonSlotContentRenderer.render(
										infoLessonWrapper.getLessonSlot()));

							} else {
								strBuffer.append("&nbsp;");
							}

						} else {
							strBuffer.append("&nbsp;");
						}
						strBuffer.append("</td>\r\n");
					}
				} else {
					for (int slot = 0;
						slot < dayColumn.getMaxColisionSize().intValue();
						slot++) {
						strBuffer
							.append("<td ")
							.append(" class='")
							.append(
								getSlotCssClass(
									null,
									hourIndex,
									dayIndex,
									grid,
									null,
									slot))
							.append("'>")
							.append("&nbsp;")
							.append("</td>\r\n");
					}
				}

			}
			strBuffer.append("</tr>\r\n");
		}

		strBuffer.append("</table>");
		return strBuffer;
	}
	/**
	 * Method getEmptyLessonSlotNumber.
	 * @param lessonSlotListResolved
	 * @return int
	 */
	private int getEmptyLessonSlotNumber(InfoLessonWrapper[] lessonSlotList) {
		int emptyLessonSlot = 0;
		for (int index = 0; index < lessonSlotList.length; index++) {
			if (lessonSlotList[index] == null)
				emptyLessonSlot++;
		}
		return emptyLessonSlot;
	}
	/**
	 * 
	 * @param infoLessonWrapper may be null
	 * @param hourIndex
	 * @param dayIndex
	 * @param timeTableGrid 
	 * @param slotColisions may be null
	 * @param slotIndex
	 * @return String
	 */
	private String getSlotCssClass(
		InfoLessonWrapper infoLessonWrapper,
		int hourIndex,
		int dayIndex,
		TimeTableSlot[][] timeTableGrid,
		InfoLessonWrapper[] slotColisions,
		int slotIndex) {

		StringBuffer strBuffer = new StringBuffer("slot");

		/* get type of slot */
		if (infoLessonWrapper == null) {
			strBuffer.append("_empty").toString();
		} else {
			LessonSlot lessonSlot = infoLessonWrapper.getLessonSlot();
			if (lessonSlot.getStartIndex() == hourIndex) {
				strBuffer.append("_start");
			}
			if (lessonSlot.getEndIndex() == hourIndex) {
				strBuffer.append("_end");
			}
		}

		/* get border style */
		if (hourIndex == 0) {
			strBuffer.append("_top");
		}

		if (slotIndex == 0
			|| ((slotColisions != null)
				&& (slotIndex == slotColisions.length - 1))
			|| (infoLessonWrapper != null)) {
			strBuffer.append("_right");
		}

		if ((infoLessonWrapper == null)
			&& (hourIndex + 1 < timeTable.getNumberOfHours().intValue())) {
			TimeTableSlot nextSlot = timeTableGrid[dayIndex][hourIndex + 1];
			if ((nextSlot != null)
				&& ((nextSlot.getLessonSlotList() != null)
					&& (!nextSlot.getLessonSlotList().isEmpty()))) {
				List nextLessonSlotList = nextSlot.getLessonSlotList();
				if (nextLessonSlotList.size() - 1 >= slotIndex) {
					Iterator lessonSlotListIterator =
						nextLessonSlotList.iterator();
					while (lessonSlotListIterator.hasNext()) {
						LessonSlot lessonSlot =
							(LessonSlot) lessonSlotListIterator.next();
						if (lessonSlot.getStartIndex() == hourIndex + 1) {
							strBuffer.append("_bottom");
							break;
						}
					}
				}

			}
		}

		if (hourIndex + 1 == timeTable.getNumberOfHours().intValue()) {
			strBuffer.append("_bottom");
		}

		return strBuffer.toString();

	}

	/**
	 * Method renderHeader.
	 * @param strBuffer
	 */
	private void renderHeader(StringBuffer strBuffer) {
		
		strBuffer.append("<td width='15%'> &nbsp; </td>\r\n");

		for (int index = 0;
			index < this.timeTable.getNumberOfDays().intValue();
			index++) {

			StringBuffer classCSS = new StringBuffer("horarioHeader");

			if (index == 0)
				classCSS.append("_first");

			strBuffer
				.append("<td class='")
				.append(classCSS)
				.append("' colspan='")
				.append(timeTable.getDayColumn(index).getMaxColisionSize())
				.append("' width='")
				.append((100 - 15) / timeTable.getNumberOfDays().intValue())
				.append("%'>\r\n")
				.append(timeTable.getDayColumn(index).getLabel())
				.append("</td>\r\n");
		}
	}

	protected InfoLessonWrapper[] resolveColisions(
		List lessonSlotList,
		DayColumn dayColumn) {
		InfoLessonWrapper[] list =
			new InfoLessonWrapper[dayColumn.getMaxColisionSize().intValue()];

		Iterator lessonSlotListIterator = lessonSlotList.iterator();

		List lessonSlotNotLocked = new ArrayList();

		while (lessonSlotListIterator.hasNext()) {
			LessonSlot lessonSlot = (LessonSlot) lessonSlotListIterator.next();

			InfoLessonWrapper infoLessonWrapper =
				lessonSlot.getInfoLessonWrapper();

			if (infoLessonWrapper.isLocked()) {
//				System.out.println(
//					"***************** Tenho uma disciplina locked at "
//						+ infoLessonWrapper.getSlotIndex());
				list[infoLessonWrapper.getSlotIndex()] = infoLessonWrapper;
			} else {
				lessonSlotNotLocked.add(infoLessonWrapper);
			}
		}

		/* Ordena pelo tamanho */
		Collections.sort(lessonSlotNotLocked);

		Iterator lessonSlotNotLockedIterator = lessonSlotNotLocked.iterator();
		while (lessonSlotNotLockedIterator.hasNext()) {
			InfoLessonWrapper infoLessonWrapper =
				(InfoLessonWrapper) lessonSlotNotLockedIterator.next();

			for (int index = 0; index < list.length; index++) {
				if (list[index] == null) {
					list[index] = infoLessonWrapper;
//					System.out.println("Lockei em " + index);
					infoLessonWrapper.setLocked(true, index);
					break;
				}
			}
		}
		return list;

	}

	private StringBuffer getHourLabelByIndex(int hourIndex) {
		StringBuffer label = new StringBuffer("");


		double startLabelHour =
			startHour.doubleValue()
				+ new Integer(hourIndex).doubleValue()
					/ (60.0 / slotSize.doubleValue());
		double startMinutes = startLabelHour - Math.floor(startLabelHour);
		
		String startMinutesLabel = String.valueOf((int) (startMinutes * 60));
		if (startMinutesLabel.length() == 1) {
			startMinutesLabel = "0" + startMinutesLabel;
		}

		double endLabelHour =
			startHour.doubleValue()
				+ new Integer(hourIndex + 1).doubleValue()
					/ (60.0 / slotSize.doubleValue());

		double endMinutes = endLabelHour - Math.floor(endLabelHour);

		String endMinutesLabel = String.valueOf((int)(endMinutes * 60));
		if (endMinutesLabel.length() == 1) {
			endMinutesLabel = "0" + endMinutesLabel;
		}

		return label
			.append((int)startLabelHour)
			.append(":")
			.append(startMinutesLabel)
			.append("-")
			.append((int)endLabelHour)
			.append(":")
			.append(endMinutesLabel);

	}
}
