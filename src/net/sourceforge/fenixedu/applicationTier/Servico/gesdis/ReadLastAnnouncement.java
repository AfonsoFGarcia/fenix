package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ivo Brand�o
 */
public class ReadLastAnnouncement extends Service {

    public InfoAnnouncement run(InfoSite infoSite) throws FenixServiceException, ExcepcaoPersistencia {
    	ExecutionCourse executionCourse = (ExecutionCourse)
    			persistentObject.readByOID(ExecutionCourse.class, infoSite.getInfoExecutionCourse().getIdInternal());
        Site site = executionCourse.getSite();
        Announcement announcement = site.getLastAnnouncement();

        InfoAnnouncement infoAnnouncement = null;
        if (announcement != null)
            infoAnnouncement = InfoAnnouncement.newInfoFromDomain(announcement);

        return infoAnnouncement;
    }
}
