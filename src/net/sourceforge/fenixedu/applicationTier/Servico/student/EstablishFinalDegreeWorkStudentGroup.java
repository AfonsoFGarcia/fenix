/*
 * Created on 2004/04/15
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 *  
 */
public class EstablishFinalDegreeWorkStudentGroup implements IService {

    public boolean run(String username, Integer executionDegreeOID) throws ExcepcaoPersistencia,
            FenixServiceException {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();
        IPersistentExecutionDegree cursoExecucaoPersistente = persistentSupport
                .getIPersistentExecutionDegree();
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();

        Group group = persistentFinalDegreeWork.readFinalDegreeWorkGroupByUsername(username);
        if (group == null) {
            group = DomainFactory.makeGroup();
            Student student = persistentStudent.readByUsername(username);
            if (student != null) {
                GroupStudent groupStudent = DomainFactory.makeGroupStudent();
                groupStudent.setStudent(student);
                groupStudent.setFinalDegreeDegreeWorkGroup(group);
            } else {
                throw new FenixServiceException(
                        "Error reading student to place in final degree work group.");
            }
        } else {
            if (!group.getGroupProposals().isEmpty()) {
                throw new GroupProposalCandidaciesExistException();
            }
            if (group.getGroupStudents().size() > 1) {
                throw new GroupStudentCandidaciesExistException();
            }
        }

        if (group.getExecutionDegree() == null
                || !group.getExecutionDegree().getIdInternal().equals(executionDegreeOID)) {
            ExecutionDegree executionDegree = (ExecutionDegree) cursoExecucaoPersistente.readByOID(
                    ExecutionDegree.class, executionDegreeOID);
            if (executionDegree != null) {
                group.setExecutionDegree(executionDegree);
            }
        }

        return true;
    }

    public class GroupStudentCandidaciesExistException extends FenixServiceException {

        public GroupStudentCandidaciesExistException() {
            super();
        }

        public GroupStudentCandidaciesExistException(int errorType) {
            super(errorType);
        }

        public GroupStudentCandidaciesExistException(String s) {
            super(s);
        }

        public GroupStudentCandidaciesExistException(Throwable cause) {
            super(cause);
        }

        public GroupStudentCandidaciesExistException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public class GroupProposalCandidaciesExistException extends FenixServiceException {

        public GroupProposalCandidaciesExistException() {
            super();
        }

        public GroupProposalCandidaciesExistException(int errorType) {
            super(errorType);
        }

        public GroupProposalCandidaciesExistException(String s) {
            super(s);
        }

        public GroupProposalCandidaciesExistException(Throwable cause) {
            super(cause);
        }

        public GroupProposalCandidaciesExistException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
