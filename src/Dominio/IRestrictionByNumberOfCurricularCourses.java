package Dominio;

/**
 * @author David Santos in Jan 27, 2004
 */

public interface IRestrictionByNumberOfCurricularCourses extends IRestriction
{
	public abstract Integer getNumberOfCurricularCourses();
	public abstract void setNumberOfCurricularCourses(Integer numberOfCurricularCourses);
}