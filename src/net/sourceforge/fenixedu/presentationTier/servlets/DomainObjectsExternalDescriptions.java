package net.sourceforge.fenixedu.presentationTier.servlets;

/**
 * 
 * @author Jo�o Cachopo
 *
 */
public class DomainObjectsExternalDescriptions {

    public static String getShortDescription(net.sourceforge.fenixedu.domain.messaging.Announcement ann) {
        return ann.getSubject().getContent();
    }

}
