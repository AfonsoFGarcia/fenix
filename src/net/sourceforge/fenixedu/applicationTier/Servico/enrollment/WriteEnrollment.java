package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.EnrolmentInExtraCurricularCourse;
import net.sourceforge.fenixedu.domain.EnrolmentInOptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.util.enrollment.EnrollmentCondition;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author David Santos Jan 26, 2004
 */
public class WriteEnrollment implements IService {
    private static Map createdAttends = null;

    public static int runCount = 0;

    public WriteEnrollment() {
        createdAttends = new HashMap();
    }

    // some of these arguments may be null. they are only needed for filter
    public Integer run(Integer executionDegreeId, Integer studentCurricularPlanID,
            Integer curricularCourseID, Integer executionPeriodID,
            CurricularCourseEnrollmentType enrollmentType, Integer enrollmentClass, IUserView userView)
            throws ExcepcaoPersistencia {

        runCount++;

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentEnrollment enrollmentDAO = persistentSuport.getIPersistentEnrolment();
        IPersistentStudentCurricularPlan studentCurricularPlanDAO = persistentSuport
                .getIStudentCurricularPlanPersistente();
        IPersistentExecutionPeriod executionPeriodDAO = persistentSuport.getIPersistentExecutionPeriod();
        IPersistentCurricularCourse curricularCourseDAO = persistentSuport
                .getIPersistentCurricularCourse();

        IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) studentCurricularPlanDAO
                .readByOID(StudentCurricularPlan.class, studentCurricularPlanID);
        ICurricularCourse curricularCourse = (ICurricularCourse) curricularCourseDAO.readByOID(
                CurricularCourse.class, curricularCourseID);

        IExecutionPeriod executionPeriod = null;
        if (executionPeriodID == null) {
            executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
        } else {
            executionPeriod = (IExecutionPeriod) executionPeriodDAO.readByOID(ExecutionPeriod.class,
                    executionPeriodID);
        }

        IEnrollment enrollment = enrollmentDAO
                .readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(studentCurricularPlan,
                        curricularCourse, executionPeriod);

        if (enrollment == null) {

            IEnrollment enrollmentToWrite;
            if (enrollmentClass == null || enrollmentClass.equals(new Integer(1))
                    || enrollmentClass.equals(new Integer(0))) {

                enrollmentToWrite = new Enrolment();
            } else if (enrollmentClass.equals(new Integer(2))) {
                enrollmentToWrite = new EnrolmentInOptionalCurricularCourse();
            } else {
                enrollmentToWrite = new EnrolmentInExtraCurricularCourse();
            }
            enrollmentDAO.simpleLockWrite(enrollmentToWrite);
            enrollmentToWrite.setCurricularCourse(curricularCourse);
            enrollmentToWrite.setEnrollmentState(EnrollmentState.ENROLLED);
            enrollmentToWrite.setExecutionPeriod(executionPeriod);
            enrollmentToWrite.setStudentCurricularPlan(studentCurricularPlan);
            enrollmentToWrite.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
            enrollmentToWrite.setCreationDate(new Date());
            enrollmentToWrite.setCondition(getEnrollmentCondition(enrollmentType));
            enrollmentToWrite.setCreatedBy(userView.getUtilizador());

            createEnrollmentEvaluation(enrollmentToWrite);

            createAttend(studentCurricularPlan.getStudent(), curricularCourse, executionPeriod,
                    enrollmentToWrite);
        } else {
            if (enrollment.getCondition().equals(EnrollmentCondition.INVISIBLE)) {
                enrollmentDAO.simpleLockWrite(enrollment);
                enrollment.setCondition(getEnrollmentCondition(enrollmentType));
            }
            if (enrollment.getEnrollmentState().equals(EnrollmentState.ANNULED)) {
                enrollmentDAO.simpleLockWrite(enrollment);
                enrollment.setEnrollmentState(EnrollmentState.ENROLLED);
            }
        }

        resetAttends();

        if (enrollment != null) {
            return enrollment.getIdInternal();
        }
        return null;
    }

    /**
     * @param studentCurricularPlan
     * @param curricularCourse
     * @param executionPeriod
     * @param enrolmentToWrite
     * @throws ExcepcaoPersistencia
     */
    public static void createAttend(IStudent student, ICurricularCourse curricularCourse,
            IExecutionPeriod executionPeriod, IEnrollment enrolmentToWrite) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionCourse executionCourseDAO = persistentSuport.getIPersistentExecutionCourse();
        IFrequentaPersistente attendDAO = persistentSuport.getIFrequentaPersistente();

        if (createdAttends == null) {
            createdAttends = new HashMap();
        }

        List executionCourses = executionCourseDAO.readbyCurricularCourseAndExecutionPeriod(
                curricularCourse, executionPeriod);
        IExecutionCourse executionCourse = null;
        if (executionCourses.size() > 1) {
            Iterator iterator = executionCourses.iterator();
            while (iterator.hasNext()) {
                IExecutionCourse executionCourse2 = (IExecutionCourse) iterator.next();
                if (executionCourse2.getExecutionCourseProperties() == null
                        || executionCourse2.getExecutionCourseProperties().isEmpty()) {
                    executionCourse = executionCourse2;
                }
            }
        } else if (executionCourses.size() == 1) {
            executionCourse = (IExecutionCourse) executionCourses.get(0);
        }

        if (executionCourse != null) {
            IAttends attend = attendDAO.readByAlunoAndDisciplinaExecucao(student, executionCourse);
            String key = student.getIdInternal().toString() + "-"
                    + executionCourse.getIdInternal().toString();

            if (attend == null) {
                attend = (IAttends) createdAttends.get(key);
            }

            if (attend != null) {
                attendDAO.simpleLockWrite(attend);
                attend.setEnrolment(enrolmentToWrite);
            } else {
                IAttends attendToWrite = new Attends();
                attendDAO.simpleLockWrite(attendToWrite);
                attendToWrite.setAluno(student);
                attendToWrite.setDisciplinaExecucao(executionCourse);
                attendToWrite.setEnrolment(enrolmentToWrite);
                createdAttends.put(key, attendToWrite);
            }
        }
    }

    public static void createAttend(IEnrollment enrolment) throws ExcepcaoPersistencia {
        createAttend(enrolment.getStudentCurricularPlan().getStudent(), enrolment.getCurricularCourse(),
                enrolment.getExecutionPeriod(), enrolment);
    }

    private void createEnrollmentEvaluation(IEnrollment enrolment) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentEnrolmentEvaluation enrollmentEvaluationDAO = persistentSuport
                .getIPersistentEnrolmentEvaluation();

        IEnrolmentEvaluation enrolmentEvaluation = enrollmentEvaluationDAO
                .readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(enrolment,
                        EnrolmentEvaluationType.NORMAL_OBJ, null);

        if (enrolmentEvaluation == null) {
            enrolmentEvaluation = new EnrolmentEvaluation();
            enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);
            enrolmentEvaluation.setEnrolment(enrolment);
            enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
            enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
            enrolmentEvaluation.setCheckSum(null);
            enrolmentEvaluation.setEmployee(null);
            enrolmentEvaluation.setExamDate(null);
            enrolmentEvaluation.setGrade(null);
            enrolmentEvaluation.setGradeAvailableDate(null);
            enrolmentEvaluation.setObservation(null);
            enrolmentEvaluation.setPersonResponsibleForGrade(null);
            enrolmentEvaluation.setWhen(null);
            // enrolmentEvaluation.setAckOptLock(new Integer(1));
        }
    }

    public static void resetAttends() {
        if (createdAttends != null && !createdAttends.isEmpty()) {
            createdAttends.clear();
        }
    }

    protected EnrollmentCondition getEnrollmentCondition(CurricularCourseEnrollmentType enrollmentType) {
        switch (enrollmentType) {
        case TEMPORARY:
            return EnrollmentCondition.getEnum(2);
        case DEFINITIVE:
            return EnrollmentCondition.getEnum(1);
        case NOT_ALLOWED:
            return EnrollmentCondition.getEnum(3);
        case VALIDATED:
            return EnrollmentCondition.getEnum(4);
        default:
            return null;
        }
    }

}