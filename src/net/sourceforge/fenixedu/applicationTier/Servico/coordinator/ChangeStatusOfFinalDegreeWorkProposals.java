/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ChangeStatusOfFinalDegreeWorkProposals implements IService {

    public void run(Integer executionDegreeOID, List selectedProposalOIDs,
            FinalDegreeWorkProposalStatus status) throws FenixServiceException, ExcepcaoPersistencia {
        if (executionDegreeOID != null && selectedProposalOIDs != null) {

            ISuportePersistente persistentSupport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();
            IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                    .getIPersistentFinalDegreeWork();
            for (int i = 0; i < selectedProposalOIDs.size(); i++) {
                IProposal proposal = (IProposal) persistentFinalDegreeWork.readByOID(Proposal.class,
                        (Integer) selectedProposalOIDs.get(i));
                persistentFinalDegreeWork.simpleLockWrite(proposal);
                proposal.setStatus(status);
            }

        }
    }

}