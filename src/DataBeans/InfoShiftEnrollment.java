/*
 * Created on 10/Fev/2004
 *
 */
package DataBeans;

import java.util.List;

/**
 * @author T�nia Pous�o
 *
 * This class keeps all information usefull for shift enrollment use case
 * 
 */
public class InfoShiftEnrollment extends InfoObject
{
	InfoStudent infoStudent;
	List infoExecutionDegreesList;
	List infoExecutionDegreesLabelsList;
	InfoExecutionDegree infoExecutionDegree;
	List infoExecutionCoursesList;
	List infoAttendingCourses;
	
	/**
	 * @return Returns the infoStudent.
	 */
	public InfoStudent getInfoStudent()
	{
		return infoStudent;
	}

	/**
	 * @param infoStudent The infoStudent to set.
	 */
	public void setInfoStudent(InfoStudent infoStudent)
	{
		this.infoStudent = infoStudent;
	}

	/**
	 * @return Returns the infoExecutionDegreesLabelsList.
	 */
	public List getInfoExecutionDegreesLabelsList()
	{
		return infoExecutionDegreesLabelsList;
	}

	/**
	 * @param infoExecutionDegreesLabelsList The infoExecutionDegreesLabelsList to set.
	 */
	public void setInfoExecutionDegreesLabelsList(List infoExecutionDegreesLabelsList)
	{
		this.infoExecutionDegreesLabelsList = infoExecutionDegreesLabelsList;
	}

	/**
	 * @return Returns the infoAttendingCourses.
	 */
	public List getInfoAttendingCourses()
	{
		return infoAttendingCourses;
	}

	/**
	 * @param infoAttendingCourses The infoAttendingCourses to set.
	 */
	public void setInfoAttendingCourses(List infoAttendingCourses)
	{
		this.infoAttendingCourses = infoAttendingCourses;
	}

	/**
	 * @return Returns the infoExecutionCoursesList.
	 */
	public List getInfoExecutionCoursesList()
	{
		return infoExecutionCoursesList;
	}

	/**
	 * @param infoExecutionCoursesList The infoExecutionCoursesList to set.
	 */
	public void setInfoExecutionCoursesList(List infoExecutionCoursesList)
	{
		this.infoExecutionCoursesList = infoExecutionCoursesList;
	}

	/**
	 * @return Returns the infoExecutionDegree.
	 */
	public InfoExecutionDegree getInfoExecutionDegree()
	{
		return infoExecutionDegree;
	}

	/**
	 * @param infoExecutionDegree The infoExecutionDegree to set.
	 */
	public void setInfoExecutionDegree(InfoExecutionDegree infoExecutionDegree)
	{
		this.infoExecutionDegree = infoExecutionDegree;
	}

	/**
	 * @return Returns the infoExecutionDegreesList.
	 */
	public List getInfoExecutionDegreesList()
	{
		return infoExecutionDegreesList;
	}

	/**
	 * @param infoExecutionDegreesList The infoExecutionDegreesList to set.
	 */
	public void setInfoExecutionDegreesList(List infoExecutionDegreesList)
	{
		this.infoExecutionDegreesList = infoExecutionDegreesList;
	}

}
