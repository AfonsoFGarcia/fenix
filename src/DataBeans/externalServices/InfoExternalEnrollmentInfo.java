/*
 * Created on 3:59:51 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package DataBeans.externalServices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IEvaluation;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 3:59:51 PM, Mar 11, 2005
 */
public class InfoExternalEnrollmentInfo
{
    private Collection evaluations;
    private InfoExternalCurricularCourseInfo course;
    /**
     * @return Returns the grade.
     */
  

    /**
     * @return Returns the course.
     */
    public InfoExternalCurricularCourseInfo getCourse()
    {
        return this.course;
    }
    /**
     * @param course The course to set.
     */
    public void setCourse(InfoExternalCurricularCourseInfo course)
    {
        this.course = course;
    }
    public static InfoExternalEnrollmentInfo newFromEnrollment(IEnrollment enrollment)
    {
        InfoExternalEnrollmentInfo info = new InfoExternalEnrollmentInfo();
        info.setCourse(InfoExternalCurricularCourseInfo.newFromDomain(enrollment.getCurricularCourse()));
        info.setEvaluations(InfoExternalEnrollmentInfo.buildExternalEvaluationsInfo(enrollment.getEvaluations()));
        return info;
    }
    /**
     * @param evaluations2
     * @return
     */
    private static Collection buildExternalEvaluationsInfo(List evaluations)
    {
        Collection result = new ArrayList();
        for (Iterator iter = evaluations.iterator(); iter.hasNext();)
        {
            IEnrolmentEvaluation evaluation = (IEnrolmentEvaluation) iter.next();
            InfoExternalEvaluationInfo info = InfoExternalEvaluationInfo.newFromEvaluation(evaluation);
            result.add(info);
            
        }
        
        return result;
    }
    /**
     * @return Returns the evaluations.
     */
    public Collection getEvaluations()
    {
        return this.evaluations;
    }
    /**
     * @param evaluations The evaluations to set.
     */
    public void setEvaluations(Collection evaluations)
    {
        this.evaluations = evaluations;
    }
}
