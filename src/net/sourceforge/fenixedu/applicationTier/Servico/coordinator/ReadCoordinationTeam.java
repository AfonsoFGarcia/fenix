/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinator;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinatorWithInfoPerson;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * 
 * @author Jo�o Mota 17/Set/2003
 * 
 */
public class ReadCoordinationTeam extends Service {

    public List run(Integer executionDegreeId) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
        IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();

        ExecutionDegree executionDegree = (ExecutionDegree) persistentExecutionDegree.readByOID(
                ExecutionDegree.class, executionDegreeId);
        if (executionDegree == null) {
            throw new FenixServiceException("errors.invalid.execution.degree");
        }
        List coordinators = persistentCoordinator.readCoordinatorsByExecutionDegree(executionDegree
                .getIdInternal());
        Iterator iterator = coordinators.iterator();
        List infoCoordinators = new ArrayList();
        while (iterator.hasNext()) {
            Coordinator coordinator = (Coordinator) iterator.next();
            InfoCoordinator infoCoordinator = InfoCoordinatorWithInfoPerson
                    .newInfoFromDomain(coordinator);
            infoCoordinators.add(infoCoordinator);
        }
        return infoCoordinators;
    }
}