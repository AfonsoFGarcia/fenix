package DataBeans.gesdis;

import java.util.Date;

/**
 * @author EP 15
 * @author jmota
 * @author Ivo Brand�o
 */

public class InfoAnnouncement {

	private String title;
	private Date creationDate;
	private Date lastModifiedDate;
	private String information;
//	private InfoSite site;	
	private InfoSite infoSite;

	public InfoAnnouncement() {
	}

	public InfoAnnouncement(String title, Date creationDate, Date lastModifiedDate, String information, InfoSite infoSite) {
		this.title = title;
		this.creationDate = creationDate;
		this.lastModifiedDate = lastModifiedDate;
		this.information = information;
		this.infoSite = infoSite;
	}

	public boolean equals(Object obj) {

		boolean resultado = false;
		if (obj != null && obj instanceof InfoAnnouncement) {
			resultado =
				getTitle().equals(((InfoAnnouncement) obj).getTitle())
					&& getCreationDate().equals(((InfoAnnouncement) obj).getCreationDate())
					&& getLastModifiedDate().equals(((InfoAnnouncement) obj).getLastModifiedDate())
					&& getInformation().equals(((InfoAnnouncement) obj).getInformation())
					&& getInfoSite().equals(((InfoAnnouncement) obj).getInfoSite());
		}
		return resultado;
	}


	 public String toString() {
	    String result = "[INFOANNONCEMENT";
	    result += ", title=" + getTitle();
	    result += ", information=" + getInformation();
		result += ", creationDate=" + getCreationDate();
		result += ", lastModifiedDate=" + getLastModifiedDate();
		result += ", infoSite=" + getInfoSite();
	    result += "]";
	    return result;
	}

	/**
	 * @return Date
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @return String
	 */
	public String getInformation() {
		return information;
	}
	/**
	 * @return InfoSite
	 */
	public InfoSite getInfoSite() {
		return infoSite;
	}
	/**
	 * @return Date
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	/**
	 * @return String
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Sets the creationDate.
	 * @param creationDate The creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * Sets the information.
	 * @param information The information to set
	 */
	public void setInformation(String information) {
		this.information = information;
	}
	/**
	 * Sets the infoSite.
	 * @param infoSite The infoSite to set
	 */
	public void setInfoSite(InfoSite infoSite) {
		this.infoSite = infoSite;
	}
	/**
	 * Sets the lastModifiedDate.
	 * @param lastModifiedDate The lastModifiedDate to set
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	/**
	 * Sets the title.
	 * @param title The title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
