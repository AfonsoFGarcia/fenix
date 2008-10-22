package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

public class ResponsibleCoordinators extends FenixService {

    public void run(Integer executionDegreeId, List<Integer> coordinatorsToBeResponsibleIDs) throws FenixServiceException {

	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);
	if (executionDegree == null) {
	    throw new FenixServiceException("error.noExecutionDegree");
	}

	for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
	    coordinator.setResponsible(coordinatorsToBeResponsibleIDs.contains(coordinator.getIdInternal()));
	}
    }
}