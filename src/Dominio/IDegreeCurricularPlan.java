package Dominio;

import java.util.Date;
import java.util.List;

import Util.AreaType;
import Util.DegreeCurricularPlanState;
import Util.MarkType;
import Util.enrollment.EnrollmentRuleType;

/**
 * @author David Santos in Jun 25, 2004
 */

public interface IDegreeCurricularPlan extends IDomainObject
{
	public IStudentCurricularPlan getNewStudentCurricularPlan();
	public List getAreas();
	public List getCurricularCourses();
	public ICurso getDegree();
	public Integer getDegreeDuration();
	public String getDescription();
	public String getDescriptionEn();
	public Date getEndDate();
	public Date getInitialDate();
	public MarkType getMarkType();
	public Integer getMinimalYearForOptionalCourses();
	public String getName();
	public Double getNeededCredits();
	public Integer getNumerusClausus();
	public DegreeCurricularPlanState getState();
	public String getConcreteClassForStudentCurricularPlans();
	
	public void setAreas(List areas);
	public void setCurricularCourses(List curricularCourses);
	public void setDegree(ICurso degree);
	public void setDegreeDuration(Integer degreeDuration);
	public void setDescription(String description);
	public void setDescriptionEn(String descriptionEn);
	public void setEndDate(Date endDate);
	public void setInitialDate(Date initialDate);
	public void setMarkType(MarkType markType);
	public void setMinimalYearForOptionalCourses(Integer minimalYearForOptionalCourses);
	public void setName(String name);
	public void setNeededCredits(Double neededCredits);
	public void setNumerusClausus(Integer numerusClausus);
	public void setState(DegreeCurricularPlanState state);
	public void setConcreteClassForStudentCurricularPlans(String concreteClassForStudentCurricularPlans);

	// -------------------------------------------------------------
	// BEGIN: Only for enrollment purposes
	// -------------------------------------------------------------

	public List getListOfEnrollmentRules(IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod,
		EnrollmentRuleType enrollmentRuleType);
	public List getCurricularCoursesFromArea(IBranch area, AreaType areaType);
	public List getCommonAreas();
	
	// -------------------------------------------------------------
	// END: Only for enrollment purposes
	// -------------------------------------------------------------
}