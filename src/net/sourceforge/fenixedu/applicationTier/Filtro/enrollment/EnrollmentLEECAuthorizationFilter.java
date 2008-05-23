package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class EnrollmentLEECAuthorizationFilter extends EnrollmentAuthorizationFilter {
    private static String DEGREE_LEEC_CODE = "LEEC";

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        final List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.COORDINATOR);
        roles.add(RoleType.TEACHER);
        roles.add(RoleType.STUDENT);
        roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        return roles;
    }

    protected String hasPrevilege(IUserView userView, Object[] arguments) {
        if (userView.hasRoleType(RoleType.STUDENT)) {
            Registration registration = readStudent(userView);
            if (registration == null) {
                return "noAuthorization";
            }

            if (!verifyStudentLEEC(arguments)) {
                return "error.student.degreeCurricularPlan.LEEC";
            }

            final Tutorship tutor = registration.getActiveTutorship();
            if (tutor != null) {
                return "error.enrollment.student.withTutor+"
                        + tutor.getTeacher().getTeacherNumber().toString() + "+"
                        + tutor.getTeacher().getPerson().getName();
            }
        } else {
            // verify if the student to enroll is a LEEC degree student
            if (!verifyStudentLEEC(arguments)) {
                return "error.student.degreeCurricularPlan.LEEC";
            }

            // verify if the coodinator is of the LEEC degree
            if (userView.hasRoleType(RoleType.COORDINATOR) && arguments[0] != null) {
                Teacher teacher = readTeacher(userView);
                if (teacher == null) {
                    return "noAuthorization";
                }

                if (!verifyCoordinatorLEEC(userView.getPerson(), arguments)) {
                    return "noAuthorization";
                }
            } else if (userView.hasRoleType(RoleType.TEACHER)) {
                return checkTeacherInformation(userView, arguments);

            } else if (userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                    || userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {

                return checkDegreeAdministrativeOfficeInformation(arguments);

            } else {
                return "noAuthorization";
            }
        }

        return null;
    }

    private boolean verifyStudentLEEC(Object[] args) {

        final StudentCurricularPlan studentCurricularPlan = readStudent(args)
                .getActiveStudentCurricularPlan();
        if (studentCurricularPlan == null) {
            return false;
        }

        String degreeCode = null;
        if (studentCurricularPlan.getDegreeCurricularPlan() != null
                && studentCurricularPlan.getDegreeCurricularPlan().getDegree() != null) {
            degreeCode = studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla();
        }

        return degreeCode.startsWith(DEGREE_LEEC_CODE);
    }

    private boolean verifyCoordinatorLEEC(Person person, Object[] arguments) {

        ExecutionDegree executionDegree = rootDomainObject
                .readExecutionDegreeByOID((Integer) arguments[0]);
        if (executionDegree == null) {
            return false;
        }
        Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);
        if (coordinator == null) {
            return false;
        }

        String degreeCode = null;
        if (coordinator.getExecutionDegree() != null
                && coordinator.getExecutionDegree().getDegreeCurricularPlan() != null
                && coordinator.getExecutionDegree().getDegreeCurricularPlan().getDegree() != null) {
            degreeCode = coordinator.getExecutionDegree().getDegreeCurricularPlan().getDegree()
                    .getSigla();
        }

        return degreeCode.startsWith(DEGREE_LEEC_CODE);
    }

}