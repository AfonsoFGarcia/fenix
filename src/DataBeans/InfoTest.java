/*
 * Created on 28/Jul/2003
 *
 */
package DataBeans;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Susana Fernandes
 */
public class InfoTest extends InfoObject {
	private String title;
	private String information;
	private Integer numberOfQuestions;
	private Date creationDate;
	private Date lastModifiedDate;
	private InfoExecutionCourse infoExecutionCourse;
	private Boolean visible;

	public InfoTest() {
	}

	public InfoExecutionCourse getInfoExecutionCourse() {
		return infoExecutionCourse;
	}

	public Integer getNumberOfQuestions() {
		return numberOfQuestions;
	}

	public String getTitle() {
		return title;
	}

	public void setInfoExecutionCourse(InfoExecutionCourse course) {
		infoExecutionCourse = course;
	}

	public void setNumberOfQuestions(Integer integer) {
		numberOfQuestions = integer;
	}

	public void setTitle(String string) {
		title = string;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setCreationDate(Date date) {
		creationDate = date;
	}

	public void setLastModifiedDate(Date date) {
		lastModifiedDate = date;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String string) {
		information = string;
	}

	public String getLastModifiedDateFormatted() {
		String result = "";
		Date date = getLastModifiedDate();

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		result += calendar.get(Calendar.DAY_OF_MONTH);
		result += "/";
		result += calendar.get(Calendar.MONTH) + 1;
		result += "/";
		result += calendar.get(Calendar.YEAR);
		result += " ";
		result += calendar.get(Calendar.HOUR_OF_DAY);
		result += ":";
		if (calendar.get(Calendar.MINUTE) < 10) {
			result += "0";
		}
		result += calendar.get(Calendar.MINUTE);

		return result;
	}
	public String getCreationDateFormatted() {
		String result = "";
		Date date = getCreationDate();

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		result += calendar.get(Calendar.DAY_OF_MONTH);
		result += "/";
		result += calendar.get(Calendar.MONTH) + 1;
		result += "/";
		result += calendar.get(Calendar.YEAR);
		result += " ";
		result += calendar.get(Calendar.HOUR_OF_DAY);
		result += ":";
		if (calendar.get(Calendar.MINUTE) < 10) {
			result += "0";
		}
		result += calendar.get(Calendar.MINUTE);

		return result;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean boolean1) {
		visible = boolean1;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoTest) {
			InfoTest infoTest = (InfoTest) obj;
			result = getIdInternal().equals(infoTest.getIdInternal());
			result =
				result
					|| (getInfoExecutionCourse()
						.equals(infoTest.getInfoExecutionCourse()))
					&& (getTitle().equals(infoTest.getTitle()))
					&& (getInformation().equals(infoTest.getInformation()))
					&& (getNumberOfQuestions()
						.equals(infoTest.getNumberOfQuestions()))
					&& (getCreationDate().equals(infoTest.getCreationDate()))
					&& (getLastModifiedDate()
						.equals(infoTest.getLastModifiedDate()));
		}
		return result;
	}

}