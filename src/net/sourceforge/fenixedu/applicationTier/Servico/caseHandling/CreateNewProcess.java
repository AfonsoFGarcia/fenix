package net.sourceforge.fenixedu.applicationTier.Servico.caseHandling;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class CreateNewProcess extends FenixService {

    public Process run(String processName, Object object) {
	return Process.createNewProcess(AccessControl.getUserView(), processName, object);
    }
}
