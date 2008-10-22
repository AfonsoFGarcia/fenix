package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;

public class ReadFinalDegreeWorkProposalHeadersForDegreeCurricularPlan extends FenixService {

    public List run(Integer executionDegreeOID) {
	final List<FinalDegreeWorkProposalHeader> result = new ArrayList<FinalDegreeWorkProposalHeader>();

	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeOID);

	if (executionDegree.hasScheduling()) {
	    final List<Proposal> finalDegreeWorkProposals = executionDegree.getScheduling().getProposals();

	    for (final Proposal proposal : finalDegreeWorkProposals) {
		result.add(FinalDegreeWorkProposalHeader.newInfoFromDomain(proposal, executionDegree));
	    }
	}

	return result;
    }

}
