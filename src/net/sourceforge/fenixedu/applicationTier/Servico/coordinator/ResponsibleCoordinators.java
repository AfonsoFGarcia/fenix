package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ResponsibleCoordinators extends Service {

    public void run(Integer executionDegreeId, List<Integer> coordinatorsToBeResponsibleIDs)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ExecutionDegree executionDegree = rootDomainObject
                .readExecutionDegreeByOID(executionDegreeId);
        if (executionDegree == null) {
            throw new FenixServiceException("error.noExecutionDegree");
        }

        for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
            if (coordinatorsToBeResponsibleIDs.contains(coordinator.getIdInternal())) {
                coordinator.setResponsible(Boolean.TRUE);
            } else {
                coordinator.setResponsible(Boolean.FALSE);
            }
        }
    }
}