package Dominio;

import java.util.List;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class CurricularCourse implements ICurricularCourse {

	private Integer internalCode;
	private Integer departmentCourseKey;
	private Integer degreeCurricularPlanKey;
	private Double credits;
	private Double theoreticalHours;
	private Double praticalHours;
	private Double theoPratHours;
	private Double labHours;
	private String name;
	private String code;
	private IDisciplinaDepartamento departmentCourse;
	private IDegreeCurricularPlan degreeCurricularPlan;

	private List associatedExecutionCourses;
	private List associatedBranches;
	private List associatedCurricularSemesters;

	// Para sair:
//	private Integer curricularYear;
//	private Integer semester;

	public CurricularCourse() {

		setInternalCode(null);

		setCode(null);
		setCredits(null);
		setDegreeCurricularPlan(null);
		setDegreeCurricularPlanKey(null);
		setDepartmentCourse(null);
		setDepartmentCourseKey(null);
		setLabHours(null);
		setName(null);
		setPraticalHours(null);
		setTheoPratHours(null);
		setTheoreticalHours(null);

		setAssociatedExecutionCourses(null);
		setAssociatedBranches(null);
		setAssociatedCurricularSemesters(null);
	}

	/**
	 * @deprecated
	 */
	public CurricularCourse(
		Double credits,
		Double theoreticalHours,
		Double praticalHours,
		Double theoPratHours,
		Double labHours,
		Integer curricularYear,
		Integer semester,
		String name,
		String code,
		IDisciplinaDepartamento departmentCourse,
		IDegreeCurricularPlan degreeCurricularPlan) {

		this();
		setCode(code);
		setCredits(credits);
		setDegreeCurricularPlan(degreeCurricularPlan);
		setDepartmentCourse(departmentCourse);
		setLabHours(labHours);
		setName(name);
		setPraticalHours(praticalHours);
		setTheoPratHours(theoPratHours);
		setTheoreticalHours(theoreticalHours);

//		setCurricularYear(curricularYear);
//		setSemester(semester);

	}

	public CurricularCourse(
		Double credits,
		Double theoreticalHours,
		Double praticalHours,
		Double theoPratHours,
		Double labHours,
		String name,
		String code,
		IDisciplinaDepartamento departmentCourse,
		IDegreeCurricularPlan degreeCurricularPlan) {

		this();
		setCode(code);
		setCredits(credits);
		setDegreeCurricularPlan(degreeCurricularPlan);
		setDepartmentCourse(departmentCourse);
		setLabHours(labHours);
		setName(name);
		setPraticalHours(praticalHours);
		setTheoPratHours(theoPratHours);
		setTheoreticalHours(theoreticalHours);
	}

//	public boolean equals(Object obj) {
//		boolean resultado = false;
//		if (obj instanceof ICurricularCourse) {
//			ICurricularCourse dc = (ICurricularCourse) obj;
//			resultado = getDegreeCurricularPlan().equals(dc.getDegreeCurricularPlan()) &&
//									getName().equals(dc.getName()) &&
//									getCode().equals(dc.getCode()) &&
//									getCurricularYear().equals(dc.getCurricularYear()) &&
//									getSemester().equals(dc.getSemester());
//		}
//		return resultado;
//	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof ICurricularCourse) {
			ICurricularCourse dc = (ICurricularCourse) obj;
			resultado = getDegreeCurricularPlan().equals(dc.getDegreeCurricularPlan()) &&
									getName().equals(dc.getName()) &&
									getCode().equals(dc.getCode());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "idInternal = " + this.internalCode + "; ";
		result += "name = " + this.name + "; ";
		result += "code = " + this.code + "; ";
		result += "credits=" + credits + "; ";
		result += "theoreticalHours = " + theoreticalHours + "; ";
		result += "praticalHours = " + praticalHours + "; ";
		result += "theoPratHours = " + theoPratHours + "; ";
		result += "labHours = " + labHours + "; ";
		result += "labHours = " + labHours + "]\n";

//		result += "associatedExecutionCourses = " + associatedExecutionCourses + "; ";
//		result += "associatedBranches = " + associatedBranches + "; ";
//		result += "associatedCurricularSemesters = " + associatedCurricularSemesters + "; ";

		return result;
	}

	/**
	 * Returns the code.
	 * @return String
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Returns the credits.
	 * @return double
	 */
	public Double getCredits() {
		return credits;
	}

	/**
	 * Returns the curricularYear.
	 * @return int
	 */
//	public Integer getCurricularYear() {
//		if( (this.associatedCurricularSemesters != null) && (!this.associatedCurricularSemesters.isEmpty()) ) {
//			return ((ICurricularSemester) this.associatedCurricularSemesters.get(0)).getCurricularYear().getYear();
//		}
//		return null;
//	}

	/**
	 * Returns the degreeCurricularPlan.
	 * @return IDegreeCurricularPlan
	 */
	public IDegreeCurricularPlan getDegreeCurricularPlan() {
		return degreeCurricularPlan;
	}

	/**
	 * Returns the degreeCurricularPlanKey.
	 * @return int
	 */
	public Integer getDegreeCurricularPlanKey() {
		return degreeCurricularPlanKey;
	}

	/**
	 * Returns the departmentCourse.
	 * @return IDisciplinaDepartamento
	 */
	public IDisciplinaDepartamento getDepartmentCourse() {
		return departmentCourse;
	}

	/**
	 * Returns the departmentCourseKey.
	 * @return int
	 */
	public Integer getDepartmentCourseKey() {
		return departmentCourseKey;
	}

	/**
	 * Returns the internalCode.
	 * @return Integer
	 */
	public Integer getInternalCode() {
		return internalCode;
	}

	/**
	 * Returns the labHours.
	 * @return double
	 */
	public Double getLabHours() {
		return labHours;
	}

	/**
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the praticalHours.
	 * @return double
	 */
	public Double getPraticalHours() {
		return praticalHours;
	}

	/**
	 * Returns the semester.
	 * @return int
	 */
//	public Integer getSemester() {
//		if( (this.associatedCurricularSemesters != null) && (!this.associatedCurricularSemesters.isEmpty()) ) {
//			return ((ICurricularSemester) this.associatedCurricularSemesters.get(0)).getSemester();
//		}
//		return null;
//	}

	/**
	 * Returns the theoPratHours.
	 * @return double
	 */
	public Double getTheoPratHours() {
		return theoPratHours;
	}

	/**
	 * Returns the theoreticalHours.
	 * @return double
	 */
	public Double getTheoreticalHours() {
		return theoreticalHours;
	}

	/**
	 * Sets the code.
	 * @param code The code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Sets the curricularYear.
	 * @param curricularYear The curricularYear to set
	 */
//	public void setCurricularYear(Integer curricularYear) {
//		if( (this.associatedCurricularSemesters != null) && (!this.associatedCurricularSemesters.isEmpty()) ) {
//			((ICurricularSemester) this.associatedCurricularSemesters.get(0)).getCurricularYear().setYear(curricularYear);
//		}
//	}

	/**
	 * Sets the degreeCurricularPlan.
	 * @param degreeCurricularPlan The degreeCurricularPlan to set
	 */
	public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {
		this.degreeCurricularPlan = degreeCurricularPlan;
	}

	/**
	 * Sets the degreeCurricularPlanKey.
	 * @param degreeCurricularPlanKey The degreeCurricularPlanKey to set
	 */
	public void setDegreeCurricularPlanKey(Integer degreeCurricularPlanKey) {
		this.degreeCurricularPlanKey = degreeCurricularPlanKey;
	}

	/**
	 * Sets the departmentCourse.
	 * @param departmentCourse The departmentCourse to set
	 */
	public void setDepartmentCourse(IDisciplinaDepartamento departmentCourse) {
		this.departmentCourse = departmentCourse;
	}

	/**
	 * Sets the departmentCourseKey.
	 * @param departmentCourseKey The departmentCourseKey to set
	 */
	public void setDepartmentCourseKey(Integer departmentCourseKey) {
		this.departmentCourseKey = departmentCourseKey;
	}

	/**
	 * Sets the internalCode.
	 * @param internalCode The internalCode to set
	 */
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the semester.
	 * @param semester The semester to set
	 */
//	public void setSemester(Integer semester) {
//		if( (this.associatedCurricularSemesters != null) && (!this.associatedCurricularSemesters.isEmpty()) ) {
//			((ICurricularSemester) this.associatedCurricularSemesters.get(0)).setSemester(semester);
//		}
//	}

	/**
	 * Returns the associatedExecutionCourses.
	 * @return List
	 */
	public List getAssociatedExecutionCourses() {
		return associatedExecutionCourses;
	}

	/**
	 * Sets the associatedExecutionCourses.
	 * @param associatedExecutionCourses The associatedExecutionCourses to set
	 */
	public void setAssociatedExecutionCourses(List associatedExecutionCourses) {
		this.associatedExecutionCourses = associatedExecutionCourses;
	}

	/**
	 * Sets the credits.
	 * @param credits The credits to set
	 */
	public void setCredits(Double credits) {
		this.credits = credits;
	}

	/**
	 * Sets the labHours.
	 * @param labHours The labHours to set
	 */
	public void setLabHours(Double labHours) {
		this.labHours = labHours;
	}

	/**
	 * Sets the praticalHours.
	 * @param praticalHours The praticalHours to set
	 */
	public void setPraticalHours(Double praticalHours) {
		this.praticalHours = praticalHours;
	}

	/**
	 * Sets the theoPratHours.
	 * @param theoPratHours The theoPratHours to set
	 */
	public void setTheoPratHours(Double theoPratHours) {
		this.theoPratHours = theoPratHours;
	}

	/**
	 * Sets the theoreticalHours.
	 * @param theoreticalHours The theoreticalHours to set
	 */
	public void setTheoreticalHours(Double theoreticalHours) {
		this.theoreticalHours = theoreticalHours;
	}

	/**
	 * @return List
	 */
	public List getAssociatedBranches() {
		return associatedBranches;
	}

	/**
	 * @return List
	 */
	public List getAssociatedCurricularSemesters() {
		return associatedCurricularSemesters;
	}

	/**
	 * Sets the associatedBranches.
	 * @param associatedBranches The associatedBranches to set
	 */
	public void setAssociatedBranches(List associatedBranches) {
		this.associatedBranches = associatedBranches;
	}

	/**
	 * Sets the associatedCurricularSemesters.
	 * @param associatedCurricularSemesters The associatedCurricularSemesters to set
	 */
	public void setAssociatedCurricularSemesters(List associatedCurricularSemesters) {
		this.associatedCurricularSemesters = associatedCurricularSemesters;
	}

}