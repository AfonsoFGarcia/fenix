package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.areas;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author David Santos in Apr 14, 2004
 */
public class WriteStudentAreasWithoutRestrictions implements IService {
    public WriteStudentAreasWithoutRestrictions() {
    }

    // The first 2 arguments are only used by the filter applyed to this
    // service.
    public void run(InfoStudent infoStudent, DegreeType degreeType, Integer studentCurricularPlanID,
            Integer specializationAreaID, Integer secundaryAreaID) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentBranch persistentBranch = persistentSuport.getIPersistentBranch();
        IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSuport
                .getIStudentCurricularPlanPersistente();

        StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) persistentStudentCurricularPlan
                .readByOID(StudentCurricularPlan.class, studentCurricularPlanID);

        if (studentCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        Branch specializationArea = (Branch) persistentBranch.readByOID(Branch.class,
                specializationAreaID);

        Branch secundaryArea = null;
        if (secundaryAreaID != null) {
            secundaryArea = (Branch) persistentBranch.readByOID(Branch.class, secundaryAreaID);
        }

        try {
            studentCurricularPlan.setStudentAreasWithoutRestrictions(specializationArea, secundaryArea);
        } catch (DomainException e) {
            throw new BothAreasAreTheSameServiceException();
        }
    }

}