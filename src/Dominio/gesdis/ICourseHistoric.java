/*
 * Created on 17/Fev/2004
 *  
 */
package Dominio.gesdis;

import Dominio.ICurricularCourse;
import Dominio.IDomainObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface ICourseHistoric extends IDomainObject {

    public Integer getApproved();

    public ICurricularCourse getCurricularCourse();

    public String getCurricularYear();

    public Integer getEnrolled();

    public Integer getEvaluated();

    public Integer getSemester();

    public void setApproved(Integer approved);

    public void setCurricularCourse(ICurricularCourse curricularCourse);

    public void setCurricularYear(String curricularYear);

    public void setEnrolled(Integer enrolled);

    public void setEvaluated(Integer evaluated);

    public void setSemester(Integer semester);
}