package DataBeans;

import java.io.Serializable;

import Util.EnrolmentState;


/**
 * @author dcs-rjao
 *
 * 22/Abr/2003
 */
public class InfoEnrolment implements Serializable {
	private InfoStudentCurricularPlan infoStudentCurricularPlan;
	private InfoCurricularCourse infoCurricularCourse;
	private InfoExecutionPeriod infoExecutionPeriod;
	private EnrolmentState state;


	public InfoEnrolment() {
	}

	public InfoEnrolment(InfoStudentCurricularPlan infoStudentCurricularPlan, InfoCurricularCourse infoCurricularCourse, EnrolmentState state, InfoExecutionPeriod infoExecutionPeriod) {
		this();
		setInfoCurricularCourse(infoCurricularCourse);
		setInfoStudentCurricularPlan(infoStudentCurricularPlan);
		setState(state);
		setInfoExecutionPeriod(infoExecutionPeriod);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof InfoEnrolment) {
			InfoEnrolment enrolment = (InfoEnrolment) obj;

			resultado =
				this.getInfoStudentCurricularPlan().equals(enrolment.getInfoStudentCurricularPlan())
					&& this.getInfoCurricularCourse().equals(enrolment.getInfoCurricularCourse())
					&& getInfoExecutionPeriod().equals(enrolment.getInfoExecutionPeriod());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "infoStudentCurricularPlan = " + this.infoStudentCurricularPlan + "; ";
		result += "infoExecutionPeriod = " + this.infoExecutionPeriod + "; ";
		result += "state = " + this.state + "; ";		
		result += "infoCurricularCourse = " + this.infoCurricularCourse + "]\n";
		return result;
	}

	/**
	 * @return InfoCurricularCourse
	 */
	public InfoCurricularCourse getInfoCurricularCourse() {
		return infoCurricularCourse;
	}

	/**
	 * @return InfoExecutionPeriod
	 */
	public InfoExecutionPeriod getInfoExecutionPeriod() {
		return infoExecutionPeriod;
	}

	/**
	 * @return InfoStudentCurricularPlan
	 */
	public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
		return infoStudentCurricularPlan;
	}

	/**
	 * @return EnrolmentState
	 */
	public EnrolmentState getState() {
		return state;
	}

	/**
	 * Sets the infoCurricularCourse.
	 * @param infoCurricularCourse The infoCurricularCourse to set
	 */
	public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
		this.infoCurricularCourse = infoCurricularCourse;
	}

	/**
	 * Sets the infoExecutionPeriod.
	 * @param infoExecutionPeriod The infoExecutionPeriod to set
	 */
	public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
		this.infoExecutionPeriod = infoExecutionPeriod;
	}

	/**
	 * Sets the infoStudentCurricularPlan.
	 * @param infoStudentCurricularPlan The infoStudentCurricularPlan to set
	 */
	public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan infoStudentCurricularPlan) {
		this.infoStudentCurricularPlan = infoStudentCurricularPlan;
	}

	/**
	 * Sets the state.
	 * @param state The state to set
	 */
	public void setState(EnrolmentState state) {
		this.state = state;
	}

	}
