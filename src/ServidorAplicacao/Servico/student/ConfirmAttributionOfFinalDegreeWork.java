/*
 * Created on 2004/04/25
 */
package ServidorAplicacao.Servico.student;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.finalDegreeWork.GroupProposal;
import Dominio.finalDegreeWork.IGroup;
import Dominio.finalDegreeWork.IGroupProposal;
import Dominio.finalDegreeWork.IGroupStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */
public class ConfirmAttributionOfFinalDegreeWork implements IService {

    public ConfirmAttributionOfFinalDegreeWork() {
        super();
    }

    public boolean run(String username, Integer selectedGroupProposalOID)
            throws ExcepcaoPersistencia, FenixServiceException {
        ISuportePersistente persistentSupport = SuportePersistenteOJB
                .getInstance();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();

        IGroupProposal groupProposal = (IGroupProposal) persistentFinalDegreeWork
                .readByOID(GroupProposal.class, selectedGroupProposalOID);

        if (groupProposal != null) {
            IGroup groupAttributed = groupProposal.getFinalDegreeWorkProposal()
                    .getGroupAttributed();

            if (groupAttributed == null) { throw new NoAttributionToConfirmException(); }

            IGroup group = groupProposal.getFinalDegreeDegreeWorkGroup();
            if (group != null) {
                if (!group.getIdInternal().equals(
                        groupAttributed.getIdInternal())) { throw new NoAttributionToConfirmException(); }

                List groupStudents = group.getGroupStudents();
                if (groupStudents != null && !groupStudents.isEmpty()) {
                    for (int i = 0; i < groupStudents.size(); i++) {
                        IGroupStudent groupStudent = (IGroupStudent) groupStudents
                                .get(i);
                        if (groupStudent != null
                                && groupStudent.getStudent().getPerson()
                                        .getUsername().equals(username)) {
                            persistentFinalDegreeWork
                                    .simpleLockWrite(groupStudent);
                            groupStudent
                                    .setFinalDegreeWorkProposalConfirmation(groupProposal
                                            .getFinalDegreeWorkProposal());
                        }
                    }
                }
            }
        }

        return true;
    }

    public class NoAttributionToConfirmException extends FenixServiceException {

        public NoAttributionToConfirmException() {
            super();
        }

        public NoAttributionToConfirmException(int errorType) {
            super(errorType);
        }

        public NoAttributionToConfirmException(String s) {
            super(s);
        }

        public NoAttributionToConfirmException(Throwable cause) {
            super(cause);
        }

        public NoAttributionToConfirmException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}