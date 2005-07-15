package net.sourceforge.fenixedu.presentationTier.servlets;

/**
 * 
 * @author Jo�o Cachopo
 *
 */
public class DomainObjectsExternalDescriptions {

    public static String getShortDescription(net.sourceforge.fenixedu.domain.Announcement ann) {
        return ann.getTitle() + " (site de " + ann.getSite().getExecutionCourse().getNome() + ")";
    }

}
