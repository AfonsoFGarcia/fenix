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

public class InfoExam {
	protected Calendar day;
	protected Calendar beginning;
	protected Calendar end;
	protected Season season;
	
	protected List associatedRooms;
	
	private Integer idInternal;

	// The following variable serves the purpose of indicating the
	// execution course associated with this exam through which
	// the exam was obtained. It should serve only for view purposes!!!
	// It was created to be used and set by the ExamsMap Utilities.
	// It has no meaning in the buisness logic.
	private InfoExecutionCourse infoExecutionCourse;

	public InfoExam() {
	}

	public InfoExam(Calendar day, Calendar beginning, Calendar end, Season season) {
		this.setDay(day);
		this.setBeginning(beginning);
		this.setEnd(end);
		this.setSeason(season);
	}


	public String toString() {
		return "[INFOEXAM:"
			+ " day= '"             + this.getDay()             + "'"
			+ " beginning= '"       + this.getBeginning()       + "'"
			+ " end= '"				+ this.getEnd()       + "'"
			+ " season= '"          + this.getSeason()          + "'"
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

	/**
	 * @return
	 */
	public Integer getIdInternal() {
		return idInternal;
	}

	/**
	 * @param internal
	 */
	public void setIdInternal(Integer internal) {
		idInternal = internal;
	}


	public String getDate() {
		String result = String.valueOf(getDay().get(Calendar.DAY_OF_MONTH));
		result += "/";
		result += String.valueOf(getDay().get(Calendar.MONTH)+1);
		result += "/";
		result += String.valueOf(getDay().get(Calendar.YEAR));
		return result;
	}
	
	public String getBeginningHour(){
		String result = format(String.valueOf(getBeginning().get(Calendar.HOUR_OF_DAY)));
				result += ":";
				result += format(String.valueOf(getBeginning().get(Calendar.MINUTE)));
		return result;
	}
	
	/**
	 * @param string
	 * @return
	 */
	private String format(String string) {
		if (string.length()==1){
			string= "0"+string;
		}
		return string;
	}

	public String getEndHour(){
			String result = format(String.valueOf(getEnd().get(Calendar.HOUR_OF_DAY)));
					result += ":";
					result += format(String.valueOf(getEnd().get(Calendar.MINUTE)));
			return result;
		}

}