/*
 * Created on 13/Nov/2003
 *
 */
package DataBeans.teacher;

import Util.CareerType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *
 */
public class InfoTeachingCareer extends InfoCareer
{
	private InfoCategory infoCategory;
	private String courseOrPosition;
    
    /**
     * 
     */
    public InfoTeachingCareer()
    {
        setCareerType(new CareerType(CareerType.PROFESSIONAL));
    }

    /**
     * @return Returns the courseOrPosition.
     */
    public String getCourseOrPosition()
    {
        return courseOrPosition;
    }

    /**
     * @param courseOrPosition The courseOrPosition to set.
     */
    public void setCourseOrPosition(String courseOrPosition)
    {
        this.courseOrPosition = courseOrPosition;
    }

    /**
     * @return Returns the infoCategory.
     */
    public InfoCategory getInfoCategory()
    {
        return infoCategory;
    }

    /**
     * @param infoCategory The infoCategory to set.
     */
    public void setInfoCategory(InfoCategory infoCategory)
    {
        this.infoCategory = infoCategory;
    }

}
