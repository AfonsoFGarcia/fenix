package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlan {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static List run(InfoStudentCurricularPlan infoStudentCurricularPlan) throws FenixServiceException {

        StudentCurricularPlan studentCurricularPlan =
                RootDomainObject.getInstance().readStudentCurricularPlanByOID(infoStudentCurricularPlan.getIdInternal());

        List masterDegreeProofVersions = studentCurricularPlan.readNotActiveMasterDegreeProofVersions();

        CollectionUtils.transform(masterDegreeProofVersions, new Transformer() {

            @Override
            public Object transform(Object arg0) {
                MasterDegreeProofVersion masterDegreeProofVersion = (MasterDegreeProofVersion) arg0;
                return InfoMasterDegreeProofVersion.newInfoFromDomain(masterDegreeProofVersion);
            }

        });

        return masterDegreeProofVersions;
    }
}