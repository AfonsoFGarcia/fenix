/*
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences - Java - Code Generation -
 * Code and Comments
 */
package Dominio.gesdis;

import java.util.Date;

import Dominio.ICurricularCourse;
import Dominio.IDomainObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IStudentCourseReport extends IDomainObject {

    public String getStudentReport();

    public void setStudentReport(String delegateReport);

    public String getStrongPoints();

    public void setStrongPoints(String strongPoints);

    public String getWeakPoints();

    public void setWeakPoints(String weakPoints);

    public ICurricularCourse getCurricularCourse();

    public void setCurricularCourse(ICurricularCourse curricularCourse);

    public Date getLastModificationDate();

    public void setLastModificationDate(Date lastModificationDate);
}