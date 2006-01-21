package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;

public class ReadAllExecutionPeriods extends Service {

	public List run() throws ExcepcaoPersistencia {
		final IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();

		final Collection<ExecutionPeriod> executionPeriods = persistentExecutionPeriod.readAll(ExecutionPeriod.class);
		final List<InfoExecutionPeriod> infoExecutionPeriods = new ArrayList<InfoExecutionPeriod>(executionPeriods.size());
		for (final ExecutionPeriod executionPeriod : executionPeriods) {
			infoExecutionPeriods.add(InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod));
		}

		return infoExecutionPeriods;
	}

}