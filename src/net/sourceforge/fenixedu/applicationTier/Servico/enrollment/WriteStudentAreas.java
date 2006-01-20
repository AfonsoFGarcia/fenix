package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ChosenAreasAreIncompatibleServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;

/**
 * @author David Santos Jan 26, 2004
 */
public class WriteStudentAreas extends Service {
	public WriteStudentAreas() {
	}

	// some of these arguments may be null. they are only needed for filter
	public void run(Integer executionDegreeId, Integer studentCurricularPlanID,
			Integer persistentSupportecializationAreaID, Integer secundaryAreaID)
			throws FenixServiceException, ExcepcaoPersistencia {

		IPersistentBranch persistentBranch = persistentSupport
				.getIPersistentBranch();
		IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
				.getIStudentCurricularPlanPersistente();

		StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) persistentStudentCurricularPlan
				.readByOID(StudentCurricularPlan.class, studentCurricularPlanID);

		if (studentCurricularPlan == null) {
			throw new NonExistingServiceException();
		}

		Branch specializationArea = (Branch) persistentBranch.readByOID(
				Branch.class, persistentSupportecializationAreaID);

		Branch secundaryArea = null;
		if (secundaryAreaID != null) {
			secundaryArea = (Branch) persistentBranch.readByOID(Branch.class,
					secundaryAreaID);
		}

		try {
			studentCurricularPlan.setStudentAreas(specializationArea,
					secundaryArea);
		} catch (DomainException e) {
			throw new ChosenAreasAreIncompatibleServiceException();
		}
	}

}