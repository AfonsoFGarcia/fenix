/*
 * Created on 5/Mai/2003
 *
 * 
 */
package DataBeans;

/**
 * @author Jo�o Mota
 * @author Fernanda Quit�rio
 * 
 */
public class SiteView {
	
	private ISiteComponent bodyComponent;
	
	

	/**
	 * 
	 */
	public SiteView() {
	}

	public SiteView(ISiteComponent bodyComponent) {
		setComponent(bodyComponent);
		}
	/**
	 * @return
	 */
	public ISiteComponent getComponent() {
		return bodyComponent;
	}

	/**
	 * @param component
	 */
	public void setComponent(ISiteComponent component) {
		this.bodyComponent = component;
	}

}
