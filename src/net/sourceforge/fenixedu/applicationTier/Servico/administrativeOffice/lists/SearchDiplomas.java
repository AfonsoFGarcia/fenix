package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchDiplomasBySituationParametersBean;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - �ngela Almeida (argelina@ist.utl.pt)
 * 
 */
public class SearchDiplomas extends FenixService {

    @Service
    public static Collection<DiplomaRequest> run(final SearchDiplomasBySituationParametersBean bean) {
	final Collection<DiplomaRequest> result = new HashSet<DiplomaRequest>();

	for (final AcademicServiceRequest request : bean.searchAcademicServiceRequests()) {
	    if (request.isRequestForRegistration() && request.isDocumentRequest() && ((DocumentRequest) request).isDiploma()) {
		result.add((DiplomaRequest) request);
	    }
	}

	return result;
    }

}
