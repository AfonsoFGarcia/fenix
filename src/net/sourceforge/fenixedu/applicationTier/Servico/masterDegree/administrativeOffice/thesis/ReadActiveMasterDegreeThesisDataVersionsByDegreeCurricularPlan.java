package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveMasterDegreeThesisDataVersionsByDegreeCurricularPlan extends FenixService {

    @Checked("RolePredicates.COORDINATOR_PREDICATE")
    @Service
    public static List run(Integer degreeCurricularPlanID) throws FenixServiceException {

	DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);

	List masterDegreeThesisDataVersions = degreeCurricularPlan.readActiveMasterDegreeThesisDataVersions();

	if (masterDegreeThesisDataVersions == null || masterDegreeThesisDataVersions.isEmpty()) {
	    throw new NonExistingServiceException("error.exception.masterDegree.nonExistingMasterDegreeThesis");
	}

	CollectionUtils.transform(masterDegreeThesisDataVersions, new Transformer() {

	    public Object transform(Object arg0) {
		MasterDegreeThesisDataVersion masterDegreeThesisDataVersion = (MasterDegreeThesisDataVersion) arg0;
		return InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis
			.newInfoFromDomain(masterDegreeThesisDataVersion);
	    }

	});

	return masterDegreeThesisDataVersions;
    }
}