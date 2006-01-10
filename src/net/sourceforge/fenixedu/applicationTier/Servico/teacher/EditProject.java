/*
 * Created on Nov 8, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditProject implements IService {

    public void run(Integer executionCourseID, Integer projectID, String name, Date begin, Date end,
            String description) throws ExcepcaoPersistencia, FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final Project project = (Project) persistentSupport.getIPersistentEvaluation().readByOID(
                Project.class, projectID);
        if (project == null) {
            throw new FenixServiceException("error.noEvaluation");
        }
        project.edit(name, begin, end, description);
    }

}
