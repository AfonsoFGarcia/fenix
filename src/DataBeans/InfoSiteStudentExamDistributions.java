/*
 * Created on 9/Jun/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author Jo�o Mota
 *
 */
public class InfoSiteStudentExamDistributions implements ISiteComponent {

	private List examDistributions;
	/**
	 * 
	 */
	public InfoSiteStudentExamDistributions() {
	}

	public InfoSiteStudentExamDistributions(List examDistributions) {
		setExamDistributions(examDistributions);
	}

	/**
	 * @return
	 */
	public List getExamDistributions() {
		return examDistributions;
	}

	/**
	 * @param list
	 */
	public void setExamDistributions(List list) {
		examDistributions = list;
	}

}
