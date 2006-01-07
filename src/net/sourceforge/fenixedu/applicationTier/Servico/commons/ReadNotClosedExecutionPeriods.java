package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadNotClosedExecutionPeriods implements IService {

    public List run() throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

        final List<ExecutionPeriod> executionPeriods = executionPeriodDAO.readNotClosedExecutionPeriods();
        final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>(executionPeriods.size());

        for (final ExecutionPeriod executionPeriod : executionPeriods) {
        	result.add(InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod));
        }

        return result;
    }

}