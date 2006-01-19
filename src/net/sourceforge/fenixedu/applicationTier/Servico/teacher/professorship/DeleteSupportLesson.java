/**
 * Nov 23, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteSupportLesson extends Service {

    public void run(Integer supportLessonID) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        SupportLesson supportLesson = (SupportLesson) persistentSupport.getIPersistentSupportLesson()
                .readByOID(SupportLesson.class, supportLessonID);
        supportLesson.delete();
    }

}
