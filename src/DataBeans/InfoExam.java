/*
 * InfoExam.java
 *
 * Created on 2003/03/19
 */

package DataBeans;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
import java.util.Calendar;
import java.util.List;

import Util.Season;

public class InfoExam extends InfoObject{
	protected Calendar day;
	protected Calendar beginning;
	protected Calendar end;
	protected Season season;
	protected Calendar enrollmentBeginDay;
	protected Calendar enrollmentEndDay;
	protected Calendar enrollmentBeginTime;
	protected Calendar enrollmentEndTime;
	protected List associatedRooms;

	

	/**
	 * The following variable serves the purpose of indicating the
	 * execution course associated with this exam through which 
	 * the exam was obtained. It should serve only for view purposes!!!
	 * It was created to be used and set by the ExamsMap Utilities.
	 * It has no meaning in the business logic.
	 */
	private InfoExecutionCourse infoExecutionCourse;

	public InfoExam() {
	}

	public InfoExam(
		Calendar day,
		Calendar beginning,
		Calendar end,
		Season season) {
		this.setDay(day);
		this.setBeginning(beginning);
		this.setEnd(end);
		this.setSeason(season);
	}

	public String toString() {
		return "[INFOEXAM:"
			+ " day= '"
			+ this.getDay()
			+ "'"
			+ " beginning= '"
			+ this.getBeginning()
			+ "'"
			+ " end= '"
			+ this.getEnd()
			+ "'"
			+ " season= '"
			+ this.getSeason()
			+ "'"
			+ "";
	}

	/**
	 * @return Calendar
	 */
	public Calendar getBeginning() {
		return beginning;
	}

	/**
	 * @return Date
	 */
	public Calendar getDay() {
		return day;
	}

	/**
	 * @return Calendar
	 */
	public Calendar getEnd() {
		return end;
	}

	/**
	 * Sets the beginning.
	 * @param beginning The beginning to set
	 */
	public void setBeginning(Calendar beginning) {
		this.beginning = beginning;
	}

	/**
	 * Sets the day.
	 * @param day The day to set
	 */
	public void setDay(Calendar day) {
		this.day = day;
	}

	/**
	 * Sets the end.
	 * @param end The end to set
	 */
	public void setEnd(Calendar end) {
		this.end = end;
	}

	/**
	 * @return
	 */
	public Season getSeason() {
		return season;
	}

	/**
	 * @param season
	 */
	public void setSeason(Season season) {
		this.season = season;
	}

	/**
	 * @return
	 */
	public InfoExecutionCourse getInfoExecutionCourse() {
		return infoExecutionCourse;
	}

	/**
	 * @param course
	 */
	public void setInfoExecutionCourse(InfoExecutionCourse course) {
		infoExecutionCourse = course;
	}

	/**
	 * @return
	 */
	public List getAssociatedRooms() {
		return associatedRooms;
	}

	/**
	 * @param rooms
	 */
	public void setAssociatedRooms(List rooms) {
		associatedRooms = rooms;
	}

	public String getDate() {
		if (getDay() == null) {
			return "0/0/0";
		}
		String result = String.valueOf(getDay().get(Calendar.DAY_OF_MONTH));
		result += "/";
		result += String.valueOf(getDay().get(Calendar.MONTH) + 1);
		result += "/";
		result += String.valueOf(getDay().get(Calendar.YEAR));
		return result;
	}

	public String getBeginningHour() {
		if (getBeginning() == null) {
			return "00:00";
		}
		String result =
			format(String.valueOf(getBeginning().get(Calendar.HOUR_OF_DAY)));
		result += ":";
		result += format(String.valueOf(getBeginning().get(Calendar.MINUTE)));
		return result;
	}

	/**
	 * @param string
	 * @return
	 */
	private String format(String string) {
		if (string.length() == 1) {
			string = "0" + string;
		}
		return string;
	}

	/**
	 * @return
	 */
	public Calendar getEnrollmentBeginDay() {
		return enrollmentBeginDay;
	}

	/**
	 * @return
	 */
	public Calendar getEnrollmentEndDay() {
		return enrollmentEndDay;
	}

	/**
	 * @param calendar
	 */
	public void setEnrollmentBeginDay(Calendar calendar) {
		enrollmentBeginDay = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setEnrollmentEndDay(Calendar calendar) {
		enrollmentEndDay = calendar;
	}

	/**
	 * @return
	 */
	public Calendar getEnrollmentBeginTime() {
		return enrollmentBeginTime;
	}

	/**
	 * @return
	 */
	public Calendar getEnrollmentEndTime() {
		return enrollmentEndTime;
	}

	/**
	 * @param calendar
	 */
	public void setEnrollmentBeginTime(Calendar calendar) {
		enrollmentBeginTime = calendar;
	}

	/**
	 * @param calendar
	 */
	public void setEnrollmentEndTime(Calendar calendar) {
		enrollmentEndTime = calendar;
	}

	public String dateFormatter(Calendar calendar) {
		String result = "";
		if (calendar != null) {

			result += calendar.get(Calendar.DAY_OF_MONTH);
			result += "/";
			result += calendar.get(Calendar.MONTH) + 1;
			result += "/";
			result += calendar.get(Calendar.YEAR);
		}
		return result;
	}

	public String timeFormatter(Calendar calendar) {
		String result = "";
		if (calendar != null) {

			result += calendar.get(Calendar.HOUR_OF_DAY);
			result += ":";
			if (calendar.get(Calendar.MINUTE)<10){
				result += "0";
				result += calendar.get(Calendar.MINUTE);
			}else{
				result += calendar.get(Calendar.MINUTE);
			}
		}
		return result;
	}

	public String getEnrollmentBeginDayFormatted() {
		return dateFormatter(getEnrollmentBeginDay());
	}
	public String getEnrollmentEndDayFormatted() {
		return dateFormatter(getEnrollmentEndDay());
	}
	public String getEnrollmentBeginTimeFormatted() {
		return timeFormatter(getEnrollmentBeginTime());
	}
	public String getEnrollmentEndTimeFormatted() {
		return timeFormatter(getEnrollmentEndTime());
	}
}