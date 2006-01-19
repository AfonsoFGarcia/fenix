/*
 * Created on 14/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidateForRegistration implements IService {

	public List run(Integer executionDegreeCode) throws FenixServiceException,
			ExcepcaoPersistencia {

		ISuportePersistente sp = null;
		List result = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		// Get the Actual Execution Year

		result = sp.getIPersistentCandidateSituation()
				.readCandidateListforRegistration(executionDegreeCode);

		if (result == null) {
			throw new NonExistingServiceException();
		}

		List candidateList = new ArrayList();
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			CandidateSituation candidateSituation = (CandidateSituation) iterator
					.next();
			InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
					.newInfoFromDomain(candidateSituation
							.getMasterDegreeCandidate());
			infoMasterDegreeCandidate
					.setInfoCandidateSituation(InfoCandidateSituation.newInfoFromDomain(candidateSituation));
			candidateList.add(infoMasterDegreeCandidate);
		}

		return candidateList;

	}
}