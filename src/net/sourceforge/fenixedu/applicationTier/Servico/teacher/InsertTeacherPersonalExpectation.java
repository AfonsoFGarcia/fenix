package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author naat
 */
public class InsertTeacherPersonalExpectation implements IService {

    public void run(InfoTeacherPersonalExpectation infoTeacherPersonalExpectation, Integer teacherID,
            Integer executionYearID) throws ExcepcaoPersistencia, FenixServiceException {

        ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        IPersistentTeacher persistentTeacher = persistenceSupport.getIPersistentTeacher();
        IPersistentExecutionYear persistentExecutionYear = persistenceSupport
                .getIPersistentExecutionYear();

        Teacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherID);
        ExecutionYear executionYear = (ExecutionYear) persistentExecutionYear.readByOID(
                ExecutionYear.class, executionYearID);
        
        teacher.createTeacherPersonalExpectation(infoTeacherPersonalExpectation, executionYear);

    }
}