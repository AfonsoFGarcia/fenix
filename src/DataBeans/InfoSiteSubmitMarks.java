package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author T�nia Pous�o
 *
 * 
 */
public class InfoSiteSubmitMarks implements ISiteComponent {
	private InfoEvaluation infoEvaluation;
	private List marksList;

	//errors		
	private List errorsNotEnrolmented = null;
	private List errorsMarkNotPublished = null;
	private boolean noMarks = false;
	private boolean allMarksNotPublished = false;
	

	public boolean equals(Object objectToCompare) {
		boolean result = false;

		if (objectToCompare instanceof InfoSiteStudents
			&& (((((InfoSiteMarks) objectToCompare).getInfoEvaluation() != null
				&& this.getInfoEvaluation() != null
				&& ((InfoSiteMarks) objectToCompare).getInfoEvaluation().equals(this.getInfoEvaluation()))
				|| ((InfoSiteMarks) objectToCompare).getInfoEvaluation() == null
				&& this.getInfoEvaluation() == null))) {
			result = true;
		}

		if (((InfoSiteMarks) objectToCompare).getMarksList() == null && this.getMarksList() == null && result == true) {
			return true;
		}

		if (((InfoSiteMarks) objectToCompare).getMarksList() == null
			|| this.getMarksList() == null
			|| ((InfoSiteMarks) objectToCompare).getMarksList().size() != this.getMarksList().size()) {
			return false;
		}

		ListIterator iter1 = ((InfoSiteMarks) objectToCompare).getMarksList().listIterator();
		ListIterator iter2 = this.getMarksList().listIterator();
		while (result && iter1.hasNext()) {
			InfoMark infoMark1 = (InfoMark) iter1.next();
			InfoMark infoMark2 = (InfoMark) iter2.next();
			if (!infoMark1.equals(infoMark2)) {
				result = false;
			}
		}

		return result;
	}

	/**
	 * @return
	 */
	public List getMarksList() {
		return marksList;
	}

	/**
	 * @param list
	 */
	public void setMarksList(List list) {
		marksList = list;
	}

	/**
	 * @return
	 */
	public InfoEvaluation getInfoEvaluation() {
		return infoEvaluation;
	}

	/**
	 * @param exam
	 */
	public void setInfoEvaluation(InfoEvaluation evaluation) {
		infoEvaluation = evaluation;
	}


	/**
	 * @return
	 */
	public boolean getAllMarksNotPublished() {
		return allMarksNotPublished;
	}

	/**
	 * @return
	 */
	public List getErrorsMarkNotPublished() {
		return errorsMarkNotPublished;
	}

	/**
	 * @return
	 */
	public List getErrorsNotEnrolmented() {
		return errorsNotEnrolmented;
	}

	/**
	 * @param boolean1
	 */
	public void setAllMarksNotPublished(boolean boolean1) {
		allMarksNotPublished = boolean1;
	}

	/**
	 * @param list
	 */
	public void setErrorsMarkNotPublished(List list) {
		errorsMarkNotPublished = list;
	}

	/**
	 * @param list
	 */
	public void setErrorsNotEnrolmented(List list) {
		errorsNotEnrolmented = list;
	}

	/**
	 * @return
	 */
	public boolean getNoMarks() {
		return noMarks;
	}

	/**
	 * @param boolean1
	 */
	public void setNoMarks(boolean boolean1) {
		noMarks = boolean1;
	}

}
