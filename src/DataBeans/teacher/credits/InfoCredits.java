package DataBeans.teacher.credits;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoObject;
import DataBeans.InfoTeacher;

/**
 * @author T�nia Pous�o
 *  
 */
public class InfoCredits extends InfoObject
{
    private Double degreeFinalProjectStudents;
    private InfoExecutionPeriod infoExecutionPeriod;
    private InfoTeacher infoTeacher;
    private Double institutionWorkTime;
    private Double lessons;
    private Double supportLessons;

    /**
     * @return Returns the degreeFinalProjectStudents.
     */
    public Double getDegreeFinalProjectStudents()
    {
        return degreeFinalProjectStudents;
    }

    /**
     * @return Returns the infoExecutionPeriod.
     */
    public InfoExecutionPeriod getInfoExecutionPeriod()
    {
        return infoExecutionPeriod;
    }

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher()
    {
        return infoTeacher;
    }

    /**
     * @return Returns the institutionWorkTime.
     */
    public Double getInstitutionWorkTime()
    {
        return institutionWorkTime;
    }

    /**
     * @return Returns the lessons.
     */
    public Double getLessons()
    {
        return lessons;
    }

    /**
     * @return Returns the supportLessons.
     */
    public Double getSupportLessons()
    {
        return supportLessons;
    }

    /**
     * @param degreeFinalProjectStudents
     *            The degreeFinalProjectStudents to set.
     */
    public void setDegreeFinalProjectStudents(Double degreeFinalProjectStudents)
    {
        this.degreeFinalProjectStudents = degreeFinalProjectStudents;
    }

    /**
     * @param infoExecutionPeriod
     *            The infoExecutionPeriod to set.
     */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod)
    {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    /**
     * @param infoTeacher
     *            The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher)
    {
        this.infoTeacher = infoTeacher;
    }

    /**
     * @param institutionWorkTime
     *            The institutionWorkTime to set.
     */
    public void setInstitutionWorkTime(Double institutionWorkTime)
    {
        this.institutionWorkTime = institutionWorkTime;
    }

    /**
     * @param lessons
     *            The lessons to set.
     */
    public void setLessons(Double lessons)
    {
        this.lessons = lessons;
    }

    /**
     * @param supportLessons
     *            The supportLessons to set.
     */
    public void setSupportLessons(Double supportLessons)
    {
        this.supportLessons = supportLessons;
    }

    public String getSupportLessonsFormatted()
    {
        return format(this.getSupportLessons());
    }
    public String getLessonsFormatted()
    {
        return format(this.getLessons());
    }
    public String getDegreeFinalProjectStudentsFormatted()
    {
        return format(this.getDegreeFinalProjectStudents());
    }
    public String getInstitutionWorkTimeFormatted()
    {
        return format(this.getInstitutionWorkTime());
    }

    /**
     * @param double1
     * @return
     */
    private String format(Double number)
    {
        if (number == null || number.doubleValue() == 0)
        {
            return "0";
        }
        return number.toString();
    }

}
