package net.sourceforge.fenixedu.applicationTier.Servico.commons.degree;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadExecutionDegreeByCandidateID extends FenixService {

    public InfoExecutionDegree run(Integer candidateID) throws NonExistingServiceException {

	MasterDegreeCandidate masterDegreeCandidate = rootDomainObject.readMasterDegreeCandidateByOID(candidateID);

	ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(masterDegreeCandidate.getExecutionDegree()
		.getIdInternal());

	if (executionDegree == null) {
	    throw new NonExistingServiceException();
	}

	return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}