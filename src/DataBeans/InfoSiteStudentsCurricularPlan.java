package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author Fernanda & T�nia Pous�o e �ngela
 *
 * 
 */
public class InfoSiteStudentsCurricularPlan implements ISiteComponent {

	private InfoCurricularCourseScope infoCurricularCourseScope;
	private List students;

	public boolean equals(Object objectToCompare) {
		boolean result = false;
		if (objectToCompare instanceof InfoSiteStudentsCurricularPlan
			&& (((((InfoSiteStudentsCurricularPlan) objectToCompare).getInfoCurricularCourseScope() != null
				&& this.getInfoCurricularCourseScope() != null
				&& ((InfoSiteStudentsCurricularPlan) objectToCompare).getInfoCurricularCourseScope().equals(this.getInfoCurricularCourseScope()))
				|| ((InfoSiteStudentsCurricularPlan) objectToCompare).getInfoCurricularCourseScope() == null
				&& this.getInfoCurricularCourseScope() == null))) {
			result = true;
		}

		if (((InfoSiteStudentsCurricularPlan) objectToCompare).getStudents() == null && this.getStudents() == null) {
			return true;
		}
		if (((InfoSiteStudentsCurricularPlan) objectToCompare).getStudents() == null
			|| this.getStudents() == null
			|| ((InfoSiteStudentsCurricularPlan) objectToCompare).getStudents().size() != this.getStudents().size()) {
			return false;
		}
		ListIterator iter1 = ((InfoSiteStudentsCurricularPlan) objectToCompare).getStudents().listIterator();
		ListIterator iter2 = this.getStudents().listIterator();
		while (result && iter1.hasNext()) {
			InfoStudent infoStudent1 = (InfoStudent) iter1.next();
			InfoStudent infoStudent2 = (InfoStudent) iter2.next();
			if (!infoStudent1.equals(infoStudent2)) {
				result = false;
			}
		}

		return result;
	}

	/**
	 * @return
	 */
	public List getStudents() {
		return students;
	}

	/**
	 * @param list
	 */
	public void setStudents(List list) {
		students = list;
	}

	/**
	 * @return
	 */
	public InfoCurricularCourseScope getInfoCurricularCourseScope() {
		return infoCurricularCourseScope;
	}

	/**
	 * @param scope
	 */
	public void setInfoCurricularCourseScope(InfoCurricularCourseScope scope) {
		infoCurricularCourseScope = scope;
	}

}
