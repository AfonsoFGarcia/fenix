/*
 * Created on 2004/04/15
 *  
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ExecutionDegree;
import Dominio.IExecutionDegree;
import Dominio.IStudent;
import Dominio.finalDegreeWork.Group;
import Dominio.finalDegreeWork.GroupStudent;
import Dominio.finalDegreeWork.IGroup;
import Dominio.finalDegreeWork.IGroupStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 *  
 */
public class EstablishFinalDegreeWorkStudentGroup implements IService {

    public EstablishFinalDegreeWorkStudentGroup() {
        super();
    }

    public boolean run(String username, Integer executionDegreeOID) throws ExcepcaoPersistencia,
            FenixServiceException {
        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();
        IPersistentExecutionDegree cursoExecucaoPersistente = persistentSupport
                .getIPersistentExecutionDegree();
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();

        IGroup group = persistentFinalDegreeWork.readFinalDegreeWorkGroupByUsername(username);
        if (group == null) {
            group = new Group();
            persistentFinalDegreeWork.simpleLockWrite(group);
            group.setGroupStudents(new ArrayList());
            IStudent student = persistentStudent.readByUsername(username);
            if (student != null) {
                IGroupStudent groupStudent = new GroupStudent();
                persistentFinalDegreeWork.simpleLockWrite(groupStudent);
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

            persistentFinalDegreeWork.simpleLockWrite(group);
        }

        if (group.getExecutionDegree() == null
                || !group.getExecutionDegree().getIdInternal().equals(executionDegreeOID)) {
            IExecutionDegree executionDegree = (IExecutionDegree) cursoExecucaoPersistente.readByOID(
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