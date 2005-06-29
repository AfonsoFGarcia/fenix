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

    public Announcement(String title, Date date, ISite site) {
        setTitle(title);
        setCreationDate(date);
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

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IAnnouncement) {
            IAnnouncement announcement = (IAnnouncement) obj;
            resultado = this.getTitle().equals(announcement.getTitle())
                    && this.getInformation().equals(announcement.getInformation());

        }
        return resultado;
    }

    public void editAnnouncement(final String newAnnouncementTitle,
            final String newAnnouncementInformation) {

        if (newAnnouncementTitle == null || newAnnouncementInformation == null) {
            throw new NullPointerException();
        }

        final Date currentDate = Calendar.getInstance().getTime();
        setTitle(newAnnouncementTitle);
        setInformation(newAnnouncementInformation);
        setLastModifiedDate(currentDate);
    }

    public void deleteAnnouncement() {
        getSite().getAssociatedAnnouncements().remove(this);
        setSite(null);
    }
}