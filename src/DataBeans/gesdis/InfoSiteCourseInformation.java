/*
 * Created on 12/Nov/2003
 *  
 */
package DataBeans.gesdis;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.ISiteComponent;
import DataBeans.InfoCurriculum;
import DataBeans.InfoEvaluationMethod;
import DataBeans.InfoExecutionCourse;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoSiteCourseInformation implements ISiteComponent
{

    private InfoExecutionCourse infoExecutionCourse;
    private InfoEvaluationMethod infoEvaluationMethod;
    private List infoCurricularCourses;
    private List infoResponsibleTeachers;
    private List infoCurriculums;
    private List infoLecturingTeachers;
    private List infoLessons;
    private List infoBibliographicReferences;
    private InfoCourseReport infoCourseReport;

    public InfoSiteCourseInformation()
    {}

    /**
	 * @return Returns the infoCourseReport.
	 */
    public InfoCourseReport getInfoCourseReport()
    {
        return infoCourseReport;
    }

    /**
	 * @param infoCourseReport
	 *            The infoCourseReport to set.
	 */
    public void setInfoCourseReport(InfoCourseReport infoCourseReport)
    {
        this.infoCourseReport = infoCourseReport;
    }

    /**
	 * @return Returns the infoExecutionCourse.
	 */
    public InfoExecutionCourse getInfoExecutionCourse()
    {
        return infoExecutionCourse;
    }

    /**
	 * @param infoExecutionCourse
	 *            The infoExecutionCourse to set.
	 */
    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse)
    {
        this.infoExecutionCourse = infoExecutionCourse;
    }

    /**
	 * @return Returns the infoBibliographicReferences.
	 */
    public List getInfoBibliographicReferences()
    {
        return infoBibliographicReferences;
    }

    /**
	 * @param infoBibliographicReferences
	 *            The infoBibliographicReferences to set.
	 */
    public void setInfoBibliographicReferences(List infoBibliographicReferences)
    {
        this.infoBibliographicReferences = infoBibliographicReferences;
    }

    /**
	 * @return Returns the infoCurricularCourses.
	 */
    public List getInfoCurricularCourses()
    {
        return infoCurricularCourses;
    }

    /**
	 * @param infoCurricularCourses
	 *            The infoCurricularCourses to set.
	 */
    public void setInfoCurricularCourses(List infoCurricularCourses)
    {
        this.infoCurricularCourses = infoCurricularCourses;
    }

    /**
	 * @return Returns the infoCurriculums.
	 */
    public List getInfoCurriculums()
    {
        return infoCurriculums;
    }

    /**
	 * @param infoCurriculums
	 *            The infoCurriculums to set.
	 */
    public void setInfoCurriculums(List infoCurriculums)
    {
        this.infoCurriculums = infoCurriculums;
    }

    /**
	 * @return Returns the infoLecturingTeacher.
	 */
    public List getInfoLecturingTeachers()
    {
        return infoLecturingTeachers;
    }

    /**
	 * @param infoLecturingTeacher
	 *            The infoLecturingTeacher to set.
	 */
    public void setInfoLecturingTeachers(List infoLecturingTeachers)
    {
        this.infoLecturingTeachers = infoLecturingTeachers;
    }

    /**
	 * @return Returns the infoResponsibleTeachers.
	 */
    public List getInfoResponsibleTeachers()
    {
        return infoResponsibleTeachers;
    }

    /**
	 * @param infoResponsibleTeachers
	 *            The infoResponsibleTeachers to set.
	 */
    public void setInfoResponsibleTeachers(List infoResponsibleTeachers)
    {
        this.infoResponsibleTeachers = infoResponsibleTeachers;
    }

    /**
	 * @return Returns the infoLessons.
	 */
    public List getInfoLessons()
    {
        return infoLessons;
    }

    /**
	 * @param infoLessons
	 *            The infoLessons to set.
	 */
    public void setInfoLessons(List infoLessons)
    {
        this.infoLessons = infoLessons;
    }

    /**
	 * @return Returns the infoEvaluationMethod.
	 */
    public InfoEvaluationMethod getInfoEvaluationMethod()
    {
        return infoEvaluationMethod;
    }

    /**
	 * @param infoEvaluationMethod
	 *            The infoEvaluationMethod to set.
	 */
    public void setInfoEvaluationMethod(InfoEvaluationMethod infoEvaluationMethod)
    {
        this.infoEvaluationMethod = infoEvaluationMethod;
    }

    public Date getLastModificationDate()
    {
        List dates = new ArrayList();
        dates.add(infoCourseReport.getLastModificationDate());
        dates.addAll(CollectionUtils.collect(infoCurriculums, new Transformer()
        {
            public Object transform(Object arg0)
            {
                InfoCurriculum infoCurriculum = (InfoCurriculum) arg0;
                return infoCurriculum.getLastModificationDate();
            }
        }));
        return getMostRecentDate(dates);
    }

    /**
	 * @param dates
	 * @return
	 */
    private Date getMostRecentDate(List dates)
    {
        Date maxDate = new Date(Long.MAX_VALUE);
        Date minDate = maxDate;
        Iterator iter = dates.iterator();
        while (iter.hasNext())
        {
            Date date = (Date) iter.next();

            if (date == null)
                continue;

            if (date.getTime() < minDate.getTime())
                minDate = date;
        }

        // if the minDate is equal to maxDate then the information wasn't filled
        if (minDate == maxDate)
            minDate = null;

        return minDate;
    }
}
