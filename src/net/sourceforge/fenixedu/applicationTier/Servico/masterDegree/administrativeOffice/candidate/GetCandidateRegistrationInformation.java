package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateRegistration;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetCandidateRegistrationInformation implements IService {

    public InfoCandidateRegistration run(Integer candidateID) throws FenixServiceException {

        ISuportePersistente sp = null;

        InfoCandidateRegistration infoCandidateRegistration = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp
                    .getIPersistentMasterDegreeCandidate().readByOID(MasterDegreeCandidate.class,
                            candidateID);

            IStudent student = sp.getIPersistentStudent().readByPersonAndDegreeType(
                    masterDegreeCandidate.getPerson(), TipoCurso.MESTRADO_OBJ);

            IStudentCurricularPlan studentCurricularPlan = sp.getIStudentCurricularPlanPersistente()
                    .readActiveStudentCurricularPlan(student.getNumber(), TipoCurso.MESTRADO_OBJ);

            infoCandidateRegistration = new InfoCandidateRegistration();

            infoCandidateRegistration.setInfoMasterDegreeCandidate(Cloner
                    .copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate));
            infoCandidateRegistration.setInfoStudentCurricularPlan(Cloner
                    .copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan));

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
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);

        }

        return infoCandidateRegistration;
    }
}