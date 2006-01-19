package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Fernanda Quit�rio
 * 
 */
public class CreateAnnouncement extends Service {

    public boolean run(Integer infoExecutionCourseCode, String announcementTitle,
            String announcementInformation) throws ExcepcaoPersistencia, InvalidArgumentsServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentSite persistentSite = persistentSupport.getIPersistentSite();

        final Site site = persistentSite.readByExecutionCourse(infoExecutionCourseCode);
        if (site == null)
            throw new InvalidArgumentsServiceException();

        site.createAnnouncement(announcementTitle, announcementInformation);
        
        return true;
    }
}