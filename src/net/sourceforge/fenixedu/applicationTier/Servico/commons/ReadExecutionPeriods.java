package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class ReadExecutionPeriods implements IService {

    public List run() throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

        final Collection<ExecutionPeriod> executionPeriods = executionPeriodDAO.readAll(ExecutionPeriod.class);
        final List result = new ArrayList(executionPeriods.size());
        for (final ExecutionPeriod executionPeriod : executionPeriods) {
            result.add(InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod));
        }
        return result;
    }

    public List run(DegreeType degreeType) throws ExcepcaoPersistencia {
        // the degree type is used in the pos-filtration
        return this.run();
    }

}