/*
 * Created on 13/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author Jo�o Mota
 * @author Fernanda Quit�rio
 * 
 *
 */
public class InfoSiteExam implements ISiteComponent {
	
	private List infoExams;
	
	

	/**
	 * @return
	 */
	public List getInfoExams() {
		return infoExams;
	}

	/**
	 * @param list
	 */
	public void setInfoExams(List list) {
		infoExams = list;
	}

}
