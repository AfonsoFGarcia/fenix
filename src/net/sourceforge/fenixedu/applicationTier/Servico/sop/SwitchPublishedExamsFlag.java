/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * 
 * @author Luis Cruz
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class SwitchPublishedExamsFlag implements IService {

    public void run(final Integer executionPeriodOID) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport
                .getIPersistentExecutionPeriod();
        final IPersistentExecutionDegree persistentExecutionDegree = persistentSupport
                .getIPersistentExecutionDegree();

        final ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentExecutionPeriod.readByOID(
                ExecutionPeriod.class, executionPeriodOID);
        final ExecutionYear executionYear = executionPeriod.getExecutionYear();
        final List<ExecutionDegree> executionDegrees = persistentExecutionDegree.readByExecutionYearOID(executionYear.getIdInternal());

        if (!executionDegrees.isEmpty()) {
            final Boolean examsPublicationState = new Boolean(!executionDegrees.get(0)
                    .getTemporaryExamMap().booleanValue());

            for (final ExecutionDegree executionDegree : executionDegrees) {
                executionDegree.setTemporaryExamMap(examsPublicationState);
            }
        }
    }
}