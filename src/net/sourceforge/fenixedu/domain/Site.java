/*
 * Site.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Ivo Brand�o
 */
public class Site extends Site_Base {

    public String toString() {
        String result = "[SITE";
        result += ", codInt=" + getIdInternal();
        result += ", executionCourse=" + getExecutionCourse();
        result += ", initialStatement=" + getInitialStatement();
        result += ", introduction=" + getIntroduction();
        result += ", mail =" + getMail();
        result += ", alternativeSite=" + getAlternativeSite();
        result += "]";
        return result;
    }

    public void edit(final String initialStatement, final String introduction, final String mail,
            final String alternativeSite) {
        if (initialStatement == null || introduction == null || mail == null || alternativeSite == null) {
            throw new NullPointerException();
        }
        setInitialStatement(initialStatement);
        setIntroduction(introduction);
        setMail(mail);
        setAlternativeSite(alternativeSite);
    }

    public void createAnnouncement(final String announcementTitle, final String announcementInformation) {

        if (announcementTitle == null || announcementInformation == null) {
            throw new NullPointerException();
        }
        final Date currentDate = Calendar.getInstance().getTime();
        final IAnnouncement announcement = DomainFactory.makeAnnouncement();
        announcement.setTitle(announcementTitle);
        announcement.setInformation(announcementInformation);
        announcement.setCreationDate(currentDate);
        announcement.setLastModifiedDate(currentDate);
        announcement.setSite(this);
    }

    public ISection createSection(String sectionName, ISection parentSection, Integer sectionOrder) {
        if (sectionName == null || sectionOrder == null)
            throw new NullPointerException();

        final ISection section = new Section();
        section.setName(sectionName);
        section.setLastModifiedDate(Calendar.getInstance().getTime());

        Integer newSectionOrder = organizeExistingSectionsOrder(parentSection, sectionOrder);
        section.setSectionOrder(newSectionOrder);
        
        section.setSite(this);
        section.setSuperiorSection(parentSection);
        
        return section;
    }

    private Integer organizeExistingSectionsOrder(final ISection parentSection, int sectionOrder) {

        List<ISection> associatedSections = Section.getSections(parentSection, this);
        
        if (associatedSections != null) {
            if (sectionOrder == -1) {
                sectionOrder = associatedSections.size();
            }
            for (final ISection section : associatedSections) {
                int oldSectionOrder = section.getSectionOrder();
                if (oldSectionOrder >= sectionOrder) {
                    section.setSectionOrder(oldSectionOrder + 1);
                }
            }
        }
        return sectionOrder;
    }
}
