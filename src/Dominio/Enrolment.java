package Dominio;

import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class Enrolment implements IEnrolment {

	private IStudentCurricularPlan studentCurricularPlan;
	private ICurricularCourse curricularCourse;
	private IExecutionPeriod executionPeriod;
	private EnrolmentState state;

	private Integer internalID;
	private Integer studentCurricularPlanKey;
	private Integer curricularCourseKey;
	private Integer keyExecutionPeriod;

	private String ojbConcreteClass;

	public Enrolment() {
		this.ojbConcreteClass = this.getClass().getName();
	}

	public Enrolment(IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse, EnrolmentState state) {
		this();
		setCurricularCourse(curricularCourse);
		setStudentCurricularPlan(studentCurricularPlan);
		setState(state);
	}

	public Enrolment(
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourse curricularCourse,
		EnrolmentState state,
		IExecutionPeriod executionPeriod) {
		this();
		setCurricularCourse(curricularCourse);
		setStudentCurricularPlan(studentCurricularPlan);
		setState(state);
		setExecutionPeriod(executionPeriod);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof IEnrolment) {
			IEnrolment enrolment = (IEnrolment) obj;

			resultado =
				this.getStudentCurricularPlan().equals(enrolment.getStudentCurricularPlan())
					&& this.getCurricularCourse().equals(enrolment.getCurricularCourse())
					&& getExecutionPeriod().equals(enrolment.getExecutionPeriod());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "studentCurricularPlan = " + this.studentCurricularPlan + "; ";
		result += "state = " + this.state + "; ";
		result += "execution Period = " + this.executionPeriod + "; ";
		result += "curricularCourse = " + this.curricularCourse + "]\n";
		return result;
	}

	/**
	 * Returns the curricularCourse.
	 * @return ICurricularCourse
	 */
	public ICurricularCourse getCurricularCourse() {
		return curricularCourse;
	}

	/**
	 * Returns the curricularCourseKey.
	 * @return Integer
	 */
	public Integer getCurricularCourseKey() {
		return curricularCourseKey;
	}

	/**
	 * Returns the internalID.
	 * @return Integer
	 */
	public Integer getInternalID() {
		return internalID;
	}

	/**
	 * Returns the studentCurricularPlan.
	 * @return IStudentCurricularPlan
	 */
	public IStudentCurricularPlan getStudentCurricularPlan() {
		return studentCurricularPlan;
	}

	/**
	 * Returns the studentCurricularPlanKey.
	 * @return Integer
	 */
	public Integer getStudentCurricularPlanKey() {
		return studentCurricularPlanKey;
	}

	/**
	 * Sets the curricularCourse.
	 * @param curricularCourse The curricularCourse to set
	 */
	public void setCurricularCourse(ICurricularCourse curricularCourse) {
		this.curricularCourse = curricularCourse;
	}

	/**
	 * Sets the curricularCourseKey.
	 * @param curricularCourseKey The curricularCourseKey to set
	 */
	public void setCurricularCourseKey(Integer curricularCourseKey) {
		this.curricularCourseKey = curricularCourseKey;
	}

	/**
	 * Sets the internalID.
	 * @param internalID The internalID to set
	 */
	public void setInternalID(Integer internalID) {
		this.internalID = internalID;
	}

	/**
	 * Sets the studentCurricularPlan.
	 * @param studentCurricularPlan The studentCurricularPlan to set
	 */
	public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) {
		this.studentCurricularPlan = studentCurricularPlan;
	}

	/**
	 * Sets the studentCurricularPlanKey.
	 * @param studentCurricularPlanKey The studentCurricularPlanKey to set
	 */
	public void setStudentCurricularPlanKey(Integer studentCurricularPlanKey) {
		this.studentCurricularPlanKey = studentCurricularPlanKey;
	}

	/**
	 * @return EnrolmentState
	 */
	public EnrolmentState getState() {
		return state;
	}

	/**
	 * Sets the state.
	 * @param state The state to set
	 */
	public void setState(EnrolmentState state) {
		this.state = state;
	}

	/**
	 * @return
	 */
	public IExecutionPeriod getExecutionPeriod() {
		return executionPeriod;
	}

	/**
	 * @return
	 */
	public Integer getKeyExecutionPeriod() {
		return keyExecutionPeriod;
	}

	/**
	 * @param period
	 */
	public void setExecutionPeriod(IExecutionPeriod period) {
		executionPeriod = period;
	}

	/**
	 * @param integer
	 */
	public void setKeyExecutionPeriod(Integer integer) {
		keyExecutionPeriod = integer;
	}

	/**
	 * @return String
	 */
	public String getOjbConcreteClass() {
		return ojbConcreteClass;
	}

	/**
	 * Sets the ojbConcreteClass.
	 * @param ojbConcreteClass The ojbConcreteClass to set
	 */
	public void setOjbConcreteClass(String ojbConcreteClass) {
		this.ojbConcreteClass = ojbConcreteClass;
	}

	/* (non-Javadoc)
	 * @see Dominio.IEnrolment#getRealCurricularCourse()
	 */
	public ICurricularCourse getRealCurricularCourse() {
		return this.getCurricularCourse();	
	}
}
