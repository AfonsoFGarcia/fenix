/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package Dominio.teacher.workTime;

import java.util.Date;

import Dominio.DomainObject;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Util.DiaSemana;

/**
 * @author jpvl
 */
public class TeacherInstitutionWorkTime extends DomainObject implements ITeacherInstitutionWorkTime
{
    private Date endTime;
    private IExecutionPeriod executionPeriod;
	private Integer keyExecutionPeriod;
    private Integer keyTeacher;
    private Date startTime;
    private ITeacher teacher;
    private DiaSemana weekDay;

    /**
     * 
     */
    public TeacherInstitutionWorkTime()
    {
        super();
    }

    /**
     * @param idInternal
     */
    public TeacherInstitutionWorkTime(Integer idInternal)
    {
        super(idInternal);
    }

    
    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    public boolean equals(Object obj)
    {
        if (obj instanceof TeacherInstitutionWorkTime)
        {
            TeacherInstitutionWorkTime teacherInstitutionWorkTime = (TeacherInstitutionWorkTime) obj;
            return this.getStartTime().equals(teacherInstitutionWorkTime.getStartTime())
                && this.getTeacher().equals(teacherInstitutionWorkTime.getTeacher())
                && this.getWeekDay().equals(teacherInstitutionWorkTime.getWeekDay());
        }
        return false;
    }

    /**
	 * @return Returns the endTime.
	 */
    public Date getEndTime()
    {
        return this.endTime;
    }

    /**
     * @return Returns the executionPeriod.
     */
    public IExecutionPeriod getExecutionPeriod()
    {
        return this.executionPeriod;
    }

    /**
     * @return Returns the keyExecutionPeriod.
     */
    public Integer getKeyExecutionPeriod()
    {
        return this.keyExecutionPeriod;
    }

    /**
	 * @return Returns the keyTeacher.
	 */
    public Integer getKeyTeacher()
    {
        return this.keyTeacher;
    }

    /**
	 * @return Returns the startTime.
	 */
    public Date getStartTime()
    {
        return this.startTime;
    }

    /**
	 * @return Returns the teacher.
	 */
    public ITeacher getTeacher()
    {
        return this.teacher;
    }

    /**
	 * @return Returns the weekDay.
	 */
    public DiaSemana getWeekDay()
    {
        return this.weekDay;
    }

    /**
	 * @param endTime
	 *                   The endTime to set.
	 */
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    /**
     * @param executionPeriod The executionPeriod to set.
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod)
    {
        this.executionPeriod = executionPeriod;
    }

    /**
     * @param keyExecutionPeriod The keyExecutionPeriod to set.
     */
    public void setKeyExecutionPeriod(Integer keyExecutionPeriod)
    {
        this.keyExecutionPeriod = keyExecutionPeriod;
    }

    /**
	 * @param keyTeacher
	 *                   The keyTeacher to set.
	 */
    public void setKeyTeacher(Integer keyTeacher)
    {
        this.keyTeacher = keyTeacher;
    }

    /**
	 * @param startTime
	 *                   The startTime to set.
	 */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    /**
	 * @param teacher
	 *                   The teacher to set.
	 */
    public void setTeacher(ITeacher teacher)
    {
        this.teacher = teacher;
    }

    /**
	 * @param weekDay
	 *                   The weekDay to set.
	 */
    public void setWeekDay(DiaSemana weekDay)
    {
        this.weekDay = weekDay;
    }

}
