package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersionWithGuidersAndResp;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ReadNonActivesMasterDegreeThesisDataVersionsByStudentCurricularPlan extends FenixService {

    public List run(InfoStudentCurricularPlan infoStudentCurricularPlan) {

	StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(infoStudentCurricularPlan
		.getIdInternal());

	List masterDegreeThesisDataVersions = studentCurricularPlan.readNotActiveMasterDegreeThesisDataVersions();

	List infoMasterDegreeThesisDataVersions = new ArrayList(masterDegreeThesisDataVersions.size());
	for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : (List<MasterDegreeThesisDataVersion>) masterDegreeThesisDataVersions) {
	    infoMasterDegreeThesisDataVersions.add(InfoMasterDegreeThesisDataVersionWithGuidersAndResp
		    .newInfoFromDomain(masterDegreeThesisDataVersion));
	}

	return infoMasterDegreeThesisDataVersions;
    }
}