package net.sourceforge.fenixedu.applicationTier.Servico.research.project;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.project.Project;

public class DeleteResearchProject extends FenixService {

    public void run(Integer oid) throws FenixServiceException {
	Project project = rootDomainObject.readProjectByOID(oid);
	if (project == null) {
	    throw new FenixServiceException();
	}
	project.delete();
    }
}
