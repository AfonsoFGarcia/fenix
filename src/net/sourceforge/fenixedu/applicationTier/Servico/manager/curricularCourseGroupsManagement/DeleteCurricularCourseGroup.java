package net.sourceforge.fenixedu.applicationTier.Servico.manager.curricularCourseGroupsManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;


public class DeleteCurricularCourseGroup extends Service {

    public void run(Integer groupId) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCurricularCourseGroup persistentCurricularCourseGroup = persistentSuport
                .getIPersistentCurricularCourseGroup();
        CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) persistentCurricularCourseGroup
                .readByOID(CurricularCourseGroup.class, groupId);
		if (curricularCourseGroup != null) {
			try {
				curricularCourseGroup.delete();
			} catch (DomainException e) {
				throw new InvalidArgumentsServiceException();
			}
		}
		// inexistent CurricularCourseGroup
    }

}