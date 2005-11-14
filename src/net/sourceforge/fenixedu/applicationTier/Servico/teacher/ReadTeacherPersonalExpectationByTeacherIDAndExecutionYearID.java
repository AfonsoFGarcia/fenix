package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.ITeacherPersonalExpectation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author naat
 */
public class ReadTeacherPersonalExpectationByTeacherIDAndExecutionYearID implements IService {

    public InfoTeacherPersonalExpectation run(Integer teacherID,
            Integer executionYearID) throws ExcepcaoPersistencia, FenixServiceException {
        ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        IPersistentTeacher persistentTeacher = persistenceSupport.getIPersistentTeacher();
        IPersistentExecutionYear persistentExecutionYear = persistenceSupport
                .getIPersistentExecutionYear();

        ITeacher teacher = (ITeacher) persistentTeacher.readByOID(Teacher.class, teacherID);
        IExecutionYear executionYear = (IExecutionYear) persistentExecutionYear.readByOID(
                ExecutionYear.class, executionYearID);

        ITeacherPersonalExpectation teacherPersonalExpectation = teacher
                .getTeacherPersonalExpectationByExecutionYear(executionYear);

        return InfoTeacherPersonalExpectation
                .newInfoFromDomain(teacherPersonalExpectation);

    }
}