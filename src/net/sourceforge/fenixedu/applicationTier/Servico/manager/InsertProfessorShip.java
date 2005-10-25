/*
 * Created on 22/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class InsertProfessorShip implements IService {
    public InsertProfessorShip() {
    }

    public void run(InfoProfessorship infoProfessorShip)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        
        final IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, infoProfessorShip.getInfoExecutionCourse().getIdInternal());
        if (executionCourse == null) {
            throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
        }

        final IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        final ITeacher teacher = persistentTeacher.readByNumber(infoProfessorShip.getInfoTeacher().getTeacherNumber());
        if (teacher == null) {
            throw new NonExistingServiceException("message.non.existing.teacher", null);
        }

        Professorship.create(infoProfessorShip.getResponsibleFor(), executionCourse, teacher, infoProfessorShip.getHours());
    }
}