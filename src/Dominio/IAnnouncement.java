/*
 * IAdvertisement.java
 * Mar 10, 2003
 */
package Dominio;

import java.sql.Timestamp;

/**
 * @author Ivo Brand�o
 */
public interface IAnnouncement {

	String getTitle();
	Timestamp getCreationDate();
	Timestamp getLastModifiedDate();
	String getInformation();
	ISite getSite();

	void setTitle(String title);
	void setCreationDate(Timestamp date);
	void setLastModifiedDate(Timestamp lastModifiedDate);
	void setInformation(String information);
	void setSite(ISite site);
}
