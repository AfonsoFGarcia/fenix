/*
 * Created on 23/Jul/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author Jo�o Mota
 *
 * 23/Jul/2003
 * fenix-head
 * DataBeans
 * 
 */
public class InfoSiteSCDegrees extends DataTranferObject implements ISiteComponent{

private List degrees;


/**
 * @return
 */
public List getDegrees() {
	return degrees;
}

/**
 * @param degrees
 */
public void setDegrees(List degrees) {
	this.degrees = degrees;
}

	/**
	 * 
	 */
	public InfoSiteSCDegrees() {
	}

}
