package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class ReadCurrentExecutionPeriod extends Service {
    public InfoExecutionPeriod run() {
	return InfoExecutionPeriod.newInfoFromDomain(ExecutionSemester.readActualExecutionSemester());
    }
}