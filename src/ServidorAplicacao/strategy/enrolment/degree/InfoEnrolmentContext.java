package ServidorAplicacao.strategy.enrolment.degree;

import java.util.List;
import java.util.Map;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class InfoEnrolmentContext {

	private InfoStudentCurricularPlan infoStudentActiveCurricularPlan;
	private InfoStudent infoStudent;
	private Integer semester;
	private InfoExecutionPeriod infoExecutionPeriod;
	private EnrolmentValidationResult enrolmentValidationResult;
	private InfoDegree chosenOptionalInfoDegree;
	private InfoCurricularCourseScope infoChosenOptionalCurricularCourseScope;

	private Map acumulatedEnrolments;

	private List actualEnrolment;
	private List infoFinalCurricularCoursesScopesSpanToBeEnrolled;
	private List infoCurricularCoursesScopesEnroledByStudent;
	private List infoDegreesForOptionalCurricularCourses;
	private List optionalInfoCurricularCoursesToChooseFromDegree;
	private List infoCurricularCoursesDoneByStudent;
	private List infoOptionalCurricularCoursesEnrolments;

	public InfoEnrolmentContext() {
		setAcumulatedEnrolments(null);
		setInfoStudentActiveCurricularPlan(null);
		setActualEnrolment(null);
		setInfoFinalCurricularCoursesScopesSpanToBeEnrolled(null);
		setInfoStudent(null);
		setSemester(null);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoEnrolmentContext) {
			InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) obj;
			resultado = this.infoFinalCurricularCoursesScopesSpanToBeEnrolled.equals(infoEnrolmentContext.infoFinalCurricularCoursesScopesSpanToBeEnrolled) &&
									(this.infoStudentActiveCurricularPlan.equals(infoEnrolmentContext.infoStudentActiveCurricularPlan));
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "acumulatedEnrolments = " + this.acumulatedEnrolments + "\n";
		result += "StudentActiveCurricularPlan = " + this.infoStudentActiveCurricularPlan + "\n";
		result += "actualEnrolment = " + this.actualEnrolment + "\n";
		result += "infoFinalCurricularCoursesScopesSpanToBeEnrolled = " + this.infoFinalCurricularCoursesScopesSpanToBeEnrolled + "\n";
		result += "enrolmentValidationResult = " + this.enrolmentValidationResult + "\n";
		result += "student = " + this.infoStudent + "\n";
		result += "chosenOptionalInfoDegree = " + this.chosenOptionalInfoDegree + "\n";
		result += "InfodegreesForOptionalCurricularCourses = " + this.infoDegreesForOptionalCurricularCourses + "\n";
		result += "optionalInfoCurricularCoursesToChooseFromDegree = " + this.optionalInfoCurricularCoursesToChooseFromDegree + "\n";
		result += "semester= " + this.semester + "]";

		return result;
	}

	/**
	 * @return List
	 */
	public List getActualEnrolment() {
		return actualEnrolment;
	}

	/**
	 * @return Map
	 */
	public Map getAcumulatedEnrolments() {
		return acumulatedEnrolments;
	}

	/**
	 * @return List
	 */
	public List getInfoFinalCurricularCoursesScopesSpanToBeEnrolled() {
		return infoFinalCurricularCoursesScopesSpanToBeEnrolled;
	}

	/**
	 * @return InfoStudentCurricularPlan
	 */
	public InfoStudentCurricularPlan getInfoStudentActiveCurricularPlan() {
		return infoStudentActiveCurricularPlan;
	}

	/**
	 * Sets the actualEnrolment.
	 * @param actualEnrolment The actualEnrolment to set
	 */
	public void setActualEnrolment(List actualEnrolment) {
		this.actualEnrolment = actualEnrolment;
	}

	/**
	 * Sets the acumulatedEnrolments.
	 * @param acumulatedEnrolments The acumulatedEnrolments to set
	 */
	public void setAcumulatedEnrolments(Map acumulatedEnrolments) {
		this.acumulatedEnrolments = acumulatedEnrolments;
	}

	/**
	 * Sets the infoFinalCurricularCoursesScopesSpanToBeEnrolled.
	 * @param infoFinalCurricularCoursesScopesSpanToBeEnrolled The infoFinalCurricularCoursesScopesSpanToBeEnrolled to set
	 */
	public void setInfoFinalCurricularCoursesScopesSpanToBeEnrolled(List finalCurricularCoursesScopesSpanToBeEnrolled) {
		this.infoFinalCurricularCoursesScopesSpanToBeEnrolled = finalCurricularCoursesScopesSpanToBeEnrolled;
	}

	/**
	 * Sets the infoStudentActiveCurricularPlan.
	 * @param infoStudentActiveCurricularPlan The infoStudentActiveCurricularPlan to set
	 */
	public void setInfoStudentActiveCurricularPlan(InfoStudentCurricularPlan infoStudentActiveCurricularPlan) {
		this.infoStudentActiveCurricularPlan = infoStudentActiveCurricularPlan;
	}

	/**
	 * @return InfoStudent
	 */
	public InfoStudent getInfoStudent() {
		return infoStudent;
	}

	/**
	 * Sets the infoStudent.
	 * @param infoStudent The infoStudent to set
	 */
	public void setInfoStudent(InfoStudent infoStudent) {
		this.infoStudent = infoStudent;
	}

	/**
	 * @return Integer
	 */
	public Integer getSemester() {
		return semester;
	}

	/**
	 * Sets the semester.
	 * @param semester The semester to set
	 */
	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	/**
	 * @return EnrolmentValidationResult
	 */
	public EnrolmentValidationResult getEnrolmentValidationResult() {
		return enrolmentValidationResult;
	}

	/**
	 * Sets the enrolmentValidationResult.
	 * @param enrolmentValidationResult The enrolmentValidationResult to set
	 */
	public void setEnrolmentValidationResult(EnrolmentValidationResult enrolmentValidationError) {
		this.enrolmentValidationResult = enrolmentValidationError;
	}

	/**
	 * @return List
	 */
	public List getInfoCurricularCoursesScopesEnroledByStudent() {
		return infoCurricularCoursesScopesEnroledByStudent;
	}

	/**
	 * Sets the infoCurricularCoursesScopesEnroledByStudent.
	 * @param infoCurricularCoursesScopesEnroledByStudent The infoCurricularCoursesScopesEnroledByStudent to set
	 */
	public void setInfoCurricularCoursesScopesEnroledByStudent(List infoCurricularCoursesScopesEnroledByStudent) {
		this.infoCurricularCoursesScopesEnroledByStudent = infoCurricularCoursesScopesEnroledByStudent;
	}

	/**
	 * @return InfoDegree
	 */
	public InfoDegree getChosenOptionalInfoDegree() {
		return chosenOptionalInfoDegree;
	}

	/**
	 * @return List
	 */
	public List getInfoDegreesForOptionalCurricularCourses() {
		return infoDegreesForOptionalCurricularCourses;
	}

	/**
	 * @return List
	 */
	public List getOptionalInfoCurricularCoursesToChooseFromDegree() {
		return optionalInfoCurricularCoursesToChooseFromDegree;
	}

	/**
	 * Sets the chosenOptionalInfoDegree.
	 * @param chosenOptionalInfoDegree The chosenOptionalInfoDegree to set
	 */
	public void setChosenOptionalInfoDegree(InfoDegree chosenOptionalInfoDegree) {
		this.chosenOptionalInfoDegree = chosenOptionalInfoDegree;
	}

	/**
	 * Sets the infoDegreesForOptionalCurricularCourses.
	 * @param infoDegreesForOptionalCurricularCourses The infoDegreesForOptionalCurricularCourses to set
	 */
	public void setInfoDegreesForOptionalCurricularCourses(List infoDegreesForOptionalCurricularCourses) {
		this.infoDegreesForOptionalCurricularCourses = infoDegreesForOptionalCurricularCourses;
	}

	/**
	 * Sets the optionalInfoCurricularCoursesToChooseFromDegree.
	 * @param optionalInfoCurricularCoursesToChooseFromDegree The optionalInfoCurricularCoursesToChooseFromDegree to set
	 */
	public void setOptionalInfoCurricularCoursesToChooseFromDegree(List optionalInfoCurricularCoursesToChooseFromDegree) {
		this.optionalInfoCurricularCoursesToChooseFromDegree = optionalInfoCurricularCoursesToChooseFromDegree;
	}

	/**
	 * @return List
	 */
	public List getInfoCurricularCoursesDoneByStudent() {
		return infoCurricularCoursesDoneByStudent;
	}

	/**
	 * Sets the infoCurricularCoursesDoneByStudent.
	 * @param infoCurricularCoursesDoneByStudent The infoCurricularCoursesDoneByStudent to set
	 */
	public void setInfoCurricularCoursesDoneByStudent(List infoCurricularCoursesDoneByStudent) {
		this.infoCurricularCoursesDoneByStudent = infoCurricularCoursesDoneByStudent;
	}

	/**
	 * @return List
	 */
	public List getInfoOptionalCurricularCoursesEnrolments() {
		return infoOptionalCurricularCoursesEnrolments;
	}

	/**
	 * Sets the infoOptionalCurricularCoursesEnrolments.
	 * @param infoOptionalCurricularCoursesEnrolments The infoOptionalCurricularCoursesEnrolments to set
	 */
	public void setInfoOptionalCurricularCoursesEnrolments(List infoOptionalCurricularCoursesEquivalences) {
		this.infoOptionalCurricularCoursesEnrolments = infoOptionalCurricularCoursesEquivalences;
	}

	/**
	 * @return InfoCurricularCourse
	 */
	public InfoCurricularCourseScope getInfoChosenOptionalCurricularCourseScope() {
		return infoChosenOptionalCurricularCourseScope;
	}

	/**
	 * Sets the infoChosenOptionalCurricularCourseScope.
	 * @param infoChosenOptionalCurricularCourseScope The infoChosenOptionalCurricularCourseScope to set
	 */
	public void setInfoChosenOptionalCurricularCourseScope(InfoCurricularCourseScope infoChosenOptionalCurricularCourse) {
		this.infoChosenOptionalCurricularCourseScope = infoChosenOptionalCurricularCourse;
	}

	/**
	 * @return InfoExecutionPeriod
	 */
	public InfoExecutionPeriod getInfoExecutionPeriod() {
		return infoExecutionPeriod;
	}

	/**
	 * Sets the infoExecutionPeriod.
	 * @param infoExecutionPeriod The infoExecutionPeriod to set
	 */
	public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
		this.infoExecutionPeriod = infoExecutionPeriod;
	}

}