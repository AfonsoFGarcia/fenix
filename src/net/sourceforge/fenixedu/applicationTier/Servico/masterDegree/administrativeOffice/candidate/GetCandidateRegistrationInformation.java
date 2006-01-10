package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateRegistration;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentAndDegree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetCandidateRegistrationInformation implements IService {

	public InfoCandidateRegistration run(Integer candidateID) throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente sp = null;

		InfoCandidateRegistration infoCandidateRegistration = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		MasterDegreeCandidate masterDegreeCandidate = (MasterDegreeCandidate) sp
				.getIPersistentMasterDegreeCandidate().readByOID(MasterDegreeCandidate.class,
						candidateID);

		Student student = sp.getIPersistentStudent().readByPersonAndDegreeType(
				masterDegreeCandidate.getPerson().getIdInternal(), DegreeType.MASTER_DEGREE);

		StudentCurricularPlan studentCurricularPlan = sp.getIStudentCurricularPlanPersistente()
				.readActiveStudentCurricularPlan(student.getNumber(), DegreeType.MASTER_DEGREE);

		infoCandidateRegistration = new InfoCandidateRegistration();

		infoCandidateRegistration.setInfoMasterDegreeCandidate(InfoMasterDegreeCandidateWithInfoPerson
				.newInfoFromDomain(masterDegreeCandidate));
		infoCandidateRegistration
				.setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudentAndDegree
						.newInfoFromDomain(studentCurricularPlan));

		if (studentCurricularPlan.getEnrolments().size() == 0) {
			infoCandidateRegistration.setEnrolments(null);
		} else {
			infoCandidateRegistration.setEnrolments(new ArrayList());
			Iterator iterator = studentCurricularPlan.getEnrolments().iterator();
			while (iterator.hasNext()) {
				Enrolment enrolment = (Enrolment) iterator.next();
				InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
						.newInfoFromDomain(enrolment);
				infoCandidateRegistration.getEnrolments().add(infoEnrolment);
			}
		}

		return infoCandidateRegistration;
	}
}