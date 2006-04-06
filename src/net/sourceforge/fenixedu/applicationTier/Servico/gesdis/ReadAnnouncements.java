/*
 * Created on 14/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author lmac1
 */

public class ReadAnnouncements extends Service {

    public List run(InfoSite infoSite) throws FenixServiceException, ExcepcaoPersistencia {
    	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoSite.getInfoExecutionCourse().getIdInternal());
        final Site site = executionCourse.getSite();
        final List<Announcement> announcementsList = site.getAssociatedAnnouncements();

        final List<InfoAnnouncement> infoAnnouncementsList = new ArrayList<InfoAnnouncement>();

        if (announcementsList != null) {
            for (final Announcement announcement : announcementsList) {
                infoAnnouncementsList.add(InfoAnnouncement.newInfoFromDomain(announcement));
            }
        }
        return infoAnnouncementsList;
    }

}
