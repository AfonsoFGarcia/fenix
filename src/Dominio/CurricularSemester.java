package Dominio;

import java.util.List;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class CurricularSemester implements ICurricularSemester {

	private Integer internalID;
	private Integer curricularYearKey;
	private Integer semester;
	
	private ICurricularYear curricularYear;
		
	private List associatedCurricularCourses;
	
	public CurricularSemester() {
		setSemester(null);
		setInternalID(null);
		setAssociatedCurricularCourses(null);
		setCurricularYearKey(null);
		setCurricularYear(null);
	}

	public CurricularSemester(Integer semester, ICurricularYear curricularYear) {
		setSemester(semester);
		setInternalID(null);
		setAssociatedCurricularCourses(null);
		setCurricularYear(curricularYear);
		setCurricularYearKey(null);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof CurricularSemester) {
			CurricularSemester curricularSemester = (CurricularSemester) obj;
			resultado = (this.getSemester().equals(curricularSemester.getSemester()) &&
									(this.getCurricularYearKey().equals(curricularSemester.getCurricularYearKey())));
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "idInternal = " + this.internalID + "; ";
		result += "semester = " + this.getSemester() + "; ";
		result += "curricularYear = " + this.getCurricularYear() + "]";
//		result += "associatedCurricularCourses = " + this.associatedCurricularCourses + "; ";
		return result;
	}

	/**
	 * @return Integer
	 */
	public Integer getInternalID() {
		return internalID;
	}

	/**
	 * Sets the internalID.
	 * @param internalID The internalID to set
	 */
	public void setInternalID(Integer internalID) {
		this.internalID = internalID;
	}

	/**
	 * @return List
	 */
	public List getAssociatedCurricularCourses() {
		return associatedCurricularCourses;
	}

	/**
	 * Sets the associatedCurricularCourses.
	 * @param associatedCurricularCourses The associatedCurricularCourses to set
	 */
	public void setAssociatedCurricularCourses(List associatedCurricularCourses) {
		this.associatedCurricularCourses = associatedCurricularCourses;
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
	 * @return ICurricularYear
	 */
	public ICurricularYear getCurricularYear() {
		return curricularYear;
	}

	/**
	 * @return Integer
	 */
	public Integer getCurricularYearKey() {
		return curricularYearKey;
	}

	/**
	 * Sets the curricularYear.
	 * @param curricularYear The curricularYear to set
	 */
	public void setCurricularYear(ICurricularYear curricularYear) {
		this.curricularYear = curricularYear;
	}

	/**
	 * Sets the curricularYearKey.
	 * @param curricularYearKey The curricularYearKey to set
	 */
	public void setCurricularYearKey(Integer curricularYearKey) {
		this.curricularYearKey = curricularYearKey;
	}

}