package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Fernanda Quit�rio
 * 
 */

public class EditAnnouncementService extends Service {

    public boolean run(Integer announcementCode, String newAnnouncementTitle,
            String newAnnouncementInformation) throws ExcepcaoPersistencia, FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final Announcement announcement = (Announcement) persistentSupport.getIPersistentObject().readByOID(
                Announcement.class, announcementCode);
        if (announcement == null) {
            throw new InvalidArgumentsServiceException();
        }
        announcement.edit(newAnnouncementTitle, newAnnouncementInformation);

        return true;
    }
}