package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;


import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.EnrollmentAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentInOptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author David Santos Jan 26, 2004
 */
public class WriteEnrollment {

    // some of these arguments may be null. they are only needed for filter
    protected Integer run(Integer executionDegreeId, Registration registration, Integer curricularCourseID,
            Integer executionPeriodID, CurricularCourseEnrollmentType enrollmentType, Integer enrollmentClass, IUserView userView) {

        ServiceMonitoring.logService(this.getClass(), executionDegreeId, registration, curricularCourseID, executionPeriodID,
                enrollmentType, enrollmentClass, userView);

        final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();

        final CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseID);

        ExecutionSemester executionSemester = null;
        if (executionPeriodID == null) {
            executionSemester = ExecutionSemester.readActualExecutionSemester();
        } else {
            executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodID);
        }

        final Enrolment enrollment =
                studentCurricularPlan.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse, executionSemester);

        if (enrollment == null) {

            if (enrollmentClass == null || enrollmentClass.intValue() == 1 || enrollmentClass.intValue() == 0) {
                new Enrolment(studentCurricularPlan, curricularCourse, executionSemester, getEnrollmentCondition(enrollmentType),
                        userView.getUtilizador());
            } else if (enrollmentClass.intValue() == 2) {
                new EnrolmentInOptionalCurricularCourse(studentCurricularPlan, curricularCourse, executionSemester,
                        getEnrollmentCondition(enrollmentType), userView.getUtilizador());
            } else {
                new Enrolment(studentCurricularPlan, studentCurricularPlan.getRoot(), curricularCourse, executionSemester,
                        getEnrollmentCondition(enrollmentType), userView.getUtilizador()).markAsExtraCurricular();
            }

        } else {
            if (enrollment.getEnrolmentCondition() == EnrollmentCondition.INVISIBLE) {
                enrollment.setEnrolmentCondition(getEnrollmentCondition(enrollmentType));
            }
            if (enrollment.isAnnulled()) {
                enrollment.setEnrollmentState(EnrollmentState.ENROLLED);
            }
        }
        return (enrollment != null) ? enrollment.getIdInternal() : null;
    }

    protected EnrollmentCondition getEnrollmentCondition(CurricularCourseEnrollmentType enrollmentType) {
        switch (enrollmentType) {
        case TEMPORARY:
            return EnrollmentCondition.TEMPORARY;
        case DEFINITIVE:
            return EnrollmentCondition.FINAL;
        case NOT_ALLOWED:
            return EnrollmentCondition.IMPOSSIBLE;
        case VALIDATED:
            return EnrollmentCondition.VALIDATED;
        default:
            return null;
        }
    }

    // Service Invokers migrated from Berserk

    private static final WriteEnrollment serviceInstance = new WriteEnrollment();

    @Service
    public static Integer runWriteEnrollment(Integer executionDegreeId, Registration registration, Integer curricularCourseID,
            Integer executionPeriodID, CurricularCourseEnrollmentType enrollmentType, Integer enrollmentClass, IUserView userView)
            throws NotAuthorizedException {
        EnrollmentAuthorizationFilter.instance.execute(executionDegreeId, registration);
        return serviceInstance.run(executionDegreeId, registration, curricularCourseID, executionPeriodID, enrollmentType,
                enrollmentClass, userView);
    }

}