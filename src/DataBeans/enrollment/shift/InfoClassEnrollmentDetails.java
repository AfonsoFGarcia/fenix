/*
 * Created on 11/Fev/2004
 */
package DataBeans.enrollment.shift;

import java.util.List;
import java.util.Map;

import DataBeans.InfoStudent;

/**
 * @author jpvl
 */
public class InfoClassEnrollmentDetails
{
    /**
     * Map (key - classId; value - List of @see ExecutionCourseShiftEnrollmentDetails
     */
    private Map classExecutionCourseShiftEnrollmentDetailsMap;

    /**
     * Classes that have shifts for enrollment.
     */
    private List infoClassList;

    /**
     * Shifts already enrolled by student.
     */
    private List infoShiftEnrolledList;

    /**
     * Student enrolling
     */
    private InfoStudent infoStudent;

    /**
     * @return Returns the classExecutionCourseShiftEnrollmentDetails.
     */
    public Map getClassExecutionCourseShiftEnrollmentDetailsMap()
    {
        return classExecutionCourseShiftEnrollmentDetailsMap;
    }

    /**
     * @param classExecutionCourseShiftEnrollmentDetails
     *            The classExecutionCourseShiftEnrollmentDetails to set.
     */
    public void setClassExecutionCourseShiftEnrollmentDetailsMap(Map classExecutionCourseShiftEnrollmentDetails)
    {
        this.classExecutionCourseShiftEnrollmentDetailsMap = classExecutionCourseShiftEnrollmentDetails;
    }

    /**
     * @return Returns the infoStudent.
     */
    public InfoStudent getInfoStudent()
    {
        return infoStudent;
    }

    /**
     * @param infoStudent
     *            The infoStudent to set.
     */
    public void setInfoStudent(InfoStudent infoStudent)
    {
        this.infoStudent = infoStudent;
    }

    /**
     * @return Returns the infoStudentShiftList.
     */
    public List getInfoShiftEnrolledList()
    {
        return infoShiftEnrolledList;
    }

    /**
     * @param infoStudentShiftList
     *            The infoStudentShiftList to set.
     */
    public void setInfoShiftEnrolledList(List infoStudentShiftList)
    {
        this.infoShiftEnrolledList = infoStudentShiftList;
    }

    /**
     * @return Returns the infoClassList.
     */
    public List getInfoClassList()
    {
        return infoClassList;
    }

    /**
     * @param infoClassList
     *            The infoClassList to set.
     */
    public void setInfoClassList(List infoClassList)
    {
        this.infoClassList = infoClassList;
    }
}
