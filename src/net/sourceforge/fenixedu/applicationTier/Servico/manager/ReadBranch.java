package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.Branch;

public class ReadBranch extends FenixService {

    public InfoBranch run(Integer idInternal) throws FenixServiceException {
	Branch branch = rootDomainObject.readBranchByOID(idInternal);
	if (branch == null) {
	    throw new NonExistingServiceException();
	}

	return InfoBranch.newInfoFromDomain(branch);
    }

}
