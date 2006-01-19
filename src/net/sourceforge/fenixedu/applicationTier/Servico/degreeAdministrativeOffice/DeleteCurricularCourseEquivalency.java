package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class DeleteCurricularCourseEquivalency extends Service {

    public void run(final Integer curricularCourseEquivalencyID) throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentCurricularCourseEquivalence persistentCurricularCourseEquivalence = persistentSupport.getIPersistentCurricularCourseEquivalence();

        final CurricularCourseEquivalence curricularCourseEquivalence = (CurricularCourseEquivalence) persistentCurricularCourseEquivalence.readByOID(CurricularCourseEquivalence.class, curricularCourseEquivalencyID);
        curricularCourseEquivalence.delete();
    }

}
