package Dominio;

import java.util.List;

import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class Enrolment extends DomainObject implements IEnrolment
{
	private IStudentCurricularPlan studentCurricularPlan;
	private ICurricularCourse curricularCourse;
	private IExecutionPeriod executionPeriod;
	private EnrolmentState enrolmentState;
	private EnrolmentEvaluationType enrolmentEvaluationType;
	

	private Integer studentCurricularPlanKey;
	private Integer curricularCourseKey;
	private Integer curricularCourseScopeKey;
	private Integer keyExecutionPeriod;

	private List evaluations;
	private String ojbConcreteClass;

	public Enrolment()
	{
		this.ojbConcreteClass = this.getClass().getName();
	}

	/**
	 * @return Returns the curricularCourse.
	 */
	public ICurricularCourse getCurricularCourse()
	{
		return curricularCourse;
	}

	/**
	 * @param curricularCourse The curricularCourse to set.
	 */
	public void setCurricularCourse(ICurricularCourse curricularCourse)
	{
		this.curricularCourse = curricularCourse;
	}

	/**
	 * @return Returns the curricularCourseKey.
	 */
	public Integer getCurricularCourseKey()
	{
		return curricularCourseKey;
	}

	/**
	 * @param curricularCourseKey The curricularCourseKey to set.
	 */
	public void setCurricularCourseKey(Integer curricularCourseKey)
	{
		this.curricularCourseKey = curricularCourseKey;
	}

	

	/**
	 * @return Returns the curricularCourseScopeKey.
	 */
	public Integer getCurricularCourseScopeKey()
	{
		return curricularCourseScopeKey;
	}

	/**
	 * @param curricularCourseScopeKey The curricularCourseScopeKey to set.
	 */
	public void setCurricularCourseScopeKey(Integer curricularCourseScopeKey)
	{
		this.curricularCourseScopeKey = curricularCourseScopeKey;
	}

	/**
	 * @return Returns the enrolmentEvaluationType.
	 */
	public EnrolmentEvaluationType getEnrolmentEvaluationType()
	{
		return enrolmentEvaluationType;
	}

	/**
	 * @param enrolmentEvaluationType The enrolmentEvaluationType to set.
	 */
	public void setEnrolmentEvaluationType(EnrolmentEvaluationType enrolmentEvaluationType)
	{
		this.enrolmentEvaluationType = enrolmentEvaluationType;
	}

	/**
	 * @return Returns the enrolmentState.
	 */
	public EnrolmentState getEnrolmentState()
	{
		return enrolmentState;
	}

	/**
	 * @param enrolmentState The enrolmentState to set.
	 */
	public void setEnrolmentState(EnrolmentState enrolmentState)
	{
		this.enrolmentState = enrolmentState;
	}

	/**
	 * @return Returns the evaluations.
	 */
	public List getEvaluations()
	{
		return evaluations;
	}

	/**
	 * @param evaluations The evaluations to set.
	 */
	public void setEvaluations(List evaluations)
	{
		this.evaluations = evaluations;
	}

	/**
	 * @return Returns the executionPeriod.
	 */
	public IExecutionPeriod getExecutionPeriod()
	{
		return executionPeriod;
	}

	/**
	 * @param executionPeriod The executionPeriod to set.
	 */
	public void setExecutionPeriod(IExecutionPeriod executionPeriod)
	{
		this.executionPeriod = executionPeriod;
	}

	/**
	 * @return Returns the keyExecutionPeriod.
	 */
	public Integer getKeyExecutionPeriod()
	{
		return keyExecutionPeriod;
	}

	/**
	 * @param keyExecutionPeriod The keyExecutionPeriod to set.
	 */
	public void setKeyExecutionPeriod(Integer keyExecutionPeriod)
	{
		this.keyExecutionPeriod = keyExecutionPeriod;
	}

	/**
	 * @return Returns the ojbConcreteClass.
	 */
	public String getOjbConcreteClass()
	{
		return ojbConcreteClass;
	}

	/**
	 * @param ojbConcreteClass The ojbConcreteClass to set.
	 */
	public void setOjbConcreteClass(String ojbConcreteClass)
	{
		this.ojbConcreteClass = ojbConcreteClass;
	}

	/**
	 * @return Returns the studentCurricularPlan.
	 */
	public IStudentCurricularPlan getStudentCurricularPlan()
	{
		return studentCurricularPlan;
	}

	/**
	 * @param studentCurricularPlan The studentCurricularPlan to set.
	 */
	public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
	{
		this.studentCurricularPlan = studentCurricularPlan;
	}

	/**
	 * @return Returns the studentCurricularPlanKey.
	 */
	public Integer getStudentCurricularPlanKey()
	{
		return studentCurricularPlanKey;
	}

	/**
	 * @param studentCurricularPlanKey The studentCurricularPlanKey to set.
	 */
	public void setStudentCurricularPlanKey(Integer studentCurricularPlanKey)
	{
		this.studentCurricularPlanKey = studentCurricularPlanKey;
	}

	public boolean equals(Object obj)
	{
		boolean result = false;

		if (obj instanceof IEnrolment) {
			IEnrolment enrolment = (IEnrolment) obj;
			
			result =
				this.getStudentCurricularPlan().equals(enrolment.getStudentCurricularPlan()) &&
				this.getCurricularCourse().equals(enrolment.getCurricularCourse()) &&
				//this.getCurricularCourseScope().equals(enrolment.getCurricularCourseScope()) &&
				this.getExecutionPeriod().equals(enrolment.getExecutionPeriod());
		}
		return result;
	}

	public String toString()
	{
		String result = "[" + this.getClass().getName() + "; ";
		result += "idInternal = " + super.getIdInternal() + "; ";
		result += "studentCurricularPlan = " + this.getStudentCurricularPlan() + "; ";
		result += "enrolmentState = " + this.getEnrolmentState() + "; ";
		result += "execution Period = " + this.getExecutionPeriod() + "; ";
		result += "curricularCourse = " + this.getCurricularCourse() + "]\n";
		return result;
	}
}