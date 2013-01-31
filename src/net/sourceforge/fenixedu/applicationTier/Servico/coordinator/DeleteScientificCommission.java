package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DeleteScientificCommission extends FenixService {

	public void run(Integer executionDegreeId, ScientificCommission commission) {
		ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeId);

		if (!executionDegree.getScientificCommissionMembers().contains(commission)) {
			throw new DomainException("scientificCommission.delete.incorrectExecutionDegree");
		}

		commission.delete();
	}

}
