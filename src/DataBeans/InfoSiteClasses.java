/*
 * Created on 5/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author Jo�o Mota
 *
 * 
 */
public class InfoSiteClasses  extends DataTranferObject implements ISiteComponent {

private List classes;
	/**
	 * 
	 */
	public InfoSiteClasses() {
	}

/**
 * @return
 */
public List getClasses() {
	return classes;
}

/**
 * @param list
 */
public void setClasses(List list) {
	classes = list;
}

}
