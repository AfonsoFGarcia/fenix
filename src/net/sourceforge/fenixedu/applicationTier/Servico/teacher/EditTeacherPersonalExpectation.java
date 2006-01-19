package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentTeacherPersonalExpectation;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author naat
 */
public class EditTeacherPersonalExpectation extends Service {

    public void run(InfoTeacherPersonalExpectation infoTeacherPersonalExpectation) throws ExcepcaoPersistencia,
            FenixServiceException {

        ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        IPersistentTeacherPersonalExpectation persistentTeacherPersonalExpectation = persistenceSupport
                .getIPersistentTeacherPersonalExpectation();
        TeacherPersonalExpectation teacherPersonalExpectation = (TeacherPersonalExpectation) persistentTeacherPersonalExpectation
                .readByOID(TeacherPersonalExpectation.class, infoTeacherPersonalExpectation.getIdInternal());

        teacherPersonalExpectation.edit(infoTeacherPersonalExpectation);

    }
}