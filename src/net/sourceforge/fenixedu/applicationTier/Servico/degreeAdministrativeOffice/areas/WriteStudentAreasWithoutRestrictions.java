package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.areas;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author David Santos in Apr 14, 2004
 */
public class WriteStudentAreasWithoutRestrictions extends Service {
    public WriteStudentAreasWithoutRestrictions() {
    }

    // The first 2 arguments are only used by the filter applyed to this
    // service.
    public void run(InfoStudent infoStudent, DegreeType degreeType, Integer studentCurricularPlanID,
            Integer specializationAreaID, Integer secundaryAreaID) throws FenixServiceException, ExcepcaoPersistencia {

        StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanID);

        if (studentCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        Branch specializationArea = rootDomainObject.readBranchByOID(specializationAreaID);

        Branch secundaryArea = null;
        if (secundaryAreaID != null) {
            secundaryArea = rootDomainObject.readBranchByOID(secundaryAreaID);
        }

        try {
            studentCurricularPlan.setStudentAreasWithoutRestrictions(specializationArea, secundaryArea);
        } catch (DomainException e) {
            throw new BothAreasAreTheSameServiceException();
        }
    }

}