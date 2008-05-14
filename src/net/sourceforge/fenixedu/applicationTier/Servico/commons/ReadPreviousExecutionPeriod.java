package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadPreviousExecutionPeriod extends Service {

    public InfoExecutionPeriod run(final Integer oid) throws ExcepcaoPersistencia {
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(oid);
	return (executionSemester != null) ? InfoExecutionPeriod
		.newInfoFromDomain(executionSemester.getPreviousExecutionPeriod()) : null;
    }

}
