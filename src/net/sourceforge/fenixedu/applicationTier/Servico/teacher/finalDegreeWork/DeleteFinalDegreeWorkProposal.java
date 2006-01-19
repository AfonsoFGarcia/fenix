/*
 * Created on 2004/03/14
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Luis Cruz
 */
public class DeleteFinalDegreeWorkProposal implements IService {

	public void run(Integer finalDegreeWorkProposalOID) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

		IPersistentFinalDegreeWork persistentFinalWork = persistentSupport
				.getIPersistentFinalDegreeWork();

		persistentFinalWork.deleteByOID(Proposal.class, finalDegreeWorkProposalOID);
	}
}