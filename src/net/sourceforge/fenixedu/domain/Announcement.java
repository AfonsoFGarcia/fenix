/*
 * Announcement.java
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Ivo Brand�o
 */
public class Announcement extends Announcement_Base {

    public Announcement() {
    }

    public Announcement(String title, Date creationDate, Date lastModifiedDate, String information,
            ISite site) {
        setTitle(title);
        setCreationDate(creationDate);
        setLastModifiedDate(lastModifiedDate);
        setInformation(information);
        setSite(site);
    }

    public String toString() {
        String result = "[ANNOUNCEMENT";

        result += ", creationDate=" + getCreationDate();
        result += ", lastModifiedDate=" + getLastModifiedDate();
        result += ", information=" + getInformation();
        result += ", site=" + getSite();
        result += "]";
        return result;
    }

    public void edit(final String newAnnouncementTitle, final String newAnnouncementInformation) {
        if (newAnnouncementTitle == null || newAnnouncementInformation == null) {
            throw new NullPointerException();
        }
        setTitle(newAnnouncementTitle);
        setInformation(newAnnouncementInformation);
        setLastModifiedDate(Calendar.getInstance().getTime());
    }

    public void delete() {
        setSite(null);
    }

}
