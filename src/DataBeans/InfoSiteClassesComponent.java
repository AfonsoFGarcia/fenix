/*
 * Created on 30/Jun/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author Jo�o Mota
 *
 * 30/Jun/2003
 * fenix-branch
 * DataBeans
 * 
 */
public class InfoSiteClassesComponent implements ISiteComponent {

private List infoClasses;




/**
 * @return
 */
public List getInfoClasses() {
	return infoClasses;
}

/**
 * @param infoClasses
 */
public void setInfoClasses(List infoClasses) {
	this.infoClasses = infoClasses;
}

	/**
	 * 
	 */
	public InfoSiteClassesComponent() {
	}
	public InfoSiteClassesComponent(List infoClasses) {
		setInfoClasses(infoClasses);
		}
}
