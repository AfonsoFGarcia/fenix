/*
 * Created on 26/Mar/2003
 * 
 *  
 */
package Dominio;

import java.util.List;

/**
 * @author Jo�o Mota
 * @author jpvl
 */
public interface IProfessorship extends IDomainObject {
    public ITeacher getTeacher();

    public IExecutionCourse getExecutionCourse();

    public Double getHours();

    public void setHours(Double credits);

    public void setTeacher(ITeacher teacher);

    public void setExecutionCourse(IExecutionCourse executionCourse);

    public List getAssociatedShiftProfessorship();

    public void setAssociatedShiftProfessorship(List associatedTeacherShiftPercentage);

    public List getSupportLessons();

    public void setSupportLessons(List supportLessons);

}