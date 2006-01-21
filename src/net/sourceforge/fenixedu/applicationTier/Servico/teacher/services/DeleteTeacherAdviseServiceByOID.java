/**
 * Nov 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacher.TeacherAdviseService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteTeacherAdviseServiceByOID extends Service {

    public void run(Integer teacherAdviseServiceID) throws ExcepcaoPersistencia {
        TeacherAdviseService teacherAdviseService = (TeacherAdviseService) persistentSupport
                .getIPersistentObject().readByOID(TeacherAdviseService.class, teacherAdviseServiceID);
        teacherAdviseService.delete();
    }
}
