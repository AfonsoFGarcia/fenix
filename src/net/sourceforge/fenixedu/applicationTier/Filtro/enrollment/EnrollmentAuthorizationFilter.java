/*
 * Created on Jul 5, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByManyRolesFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.ShowAvailableCurricularCoursesWithoutEnrollmentPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.SecretaryEnrolmentStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTutor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Jo�o Mota
 * 
 */
public class EnrollmentAuthorizationFilter extends AuthorizationByManyRolesFilter {

    private static final int LEIC_OLD_DCP = 10;

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByManyRolesFilter#getNeededRoles()
     */
    protected Collection getNeededRoles() {
        List roles = new ArrayList();

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.COORDINATOR);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.TEACHER);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.STUDENT);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        roles.add(infoRole);
        return roles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByManyRolesFilter#hasPrevilege(ServidorAplicacao.IUserView,
     *      java.lang.Object[])
     */
    protected String hasPrevilege(IUserView id, Object[] arguments) {
        try {
            List roles = getRoleList(id.getRoles());

            if (roles.contains(RoleType.STUDENT)) {
                Student student = readStudent(id);
                if (student == null) {
                    return "noAuthorization";
                }
                if (student.getPayedTuition() == null || student.getPayedTuition().equals(Boolean.FALSE)) {
                    if(student.getInterruptedStudies().equals(Boolean.FALSE))
                    	return "error.message.tuitionNotPayed";
                }
                if (student.getFlunked() == null || student.getFlunked().equals(Boolean.TRUE)) {
                    return "error.message.flunked";
                }
                if (student.getRequestedChangeDegree() == null || student.getRequestedChangeDegree().equals(Boolean.TRUE)) {
                    return "error.message.requested.change.degree";
                }
                if (!curriculumOwner(student, id)) {
                    return "noAuthorization";
                }
                Tutor tutor = verifyStudentWithTutor(student);
                if (tutor != null) {
                    return new String("error.enrollment.student.withTutor+"
                            + tutor.getTeacher().getTeacherNumber().toString() + "+"
                            + tutor.getTeacher().getPerson().getNome());
                }

                // check if the student is in the list of secretary enrolments
                // students
                SecretaryEnrolmentStudent secretaryEnrolmentStudent = persistentSupport
                        .getIPersistentSecretaryEnrolmentStudent().readByStudentNumber(
                                student.getNumber());
                if (secretaryEnrolmentStudent != null) {
                    return "error.message.secretaryEnrolmentStudent";
                }

                // check if the student is from old Leic Curricular Plan
                List studentCurricularPlans = (List) CollectionUtils.select(student.getStudentCurricularPlans(), new Predicate(){
                    public boolean evaluate(Object arg0) {
                        StudentCurricularPlan scp = (StudentCurricularPlan) arg0;
                        return scp.getCurrentState().equals(StudentCurricularPlanState.ACTIVE);
                    }});
               
                boolean oldLeicStudent = CollectionUtils.exists(studentCurricularPlans, new Predicate() {
                    public boolean evaluate(Object arg0) {

                        StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) arg0;
                        return (studentCurricularPlan.getDegreeCurricularPlan().getIdInternal()
                                .intValue() == LEIC_OLD_DCP);

                    }
                });
                if (oldLeicStudent) {
                    return "error.message.oldLeicStudent";
                }

                // TEMPORARY!!!
                // if(student.getSpecialSeason() == null ||
                // !student.getSpecialSeason().booleanValue()) {
                // return "noAuthorization";
                // }

            } else {

                if (roles.contains(RoleType.COORDINATOR) && arguments[0] != null) {
                    Teacher teacher = readTeacher(id);
                    if (teacher == null) {
                        return "noAuthorization";
                    }

                    if (!verifyCoordinator(teacher, arguments)) {
                        return "noAuthorization";
                    }
                } else if (roles.contains(RoleType.TEACHER)) {
                    Teacher teacher = readTeacher(id);
                    if (teacher == null) {
                        return "noAuthorization";
                    }

                    Student student = readStudent(arguments);
                    if (student == null) {
                        return "noAuthorization";
                    }

                    if (!verifyStudentTutor(teacher, student)) {
                        return new String("error.enrollment.notStudentTutor+"
                                + student.getNumber().toString());
                    }

                } else if (roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                        || roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
                    StudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(arguments);

                    if (studentCurricularPlan.getStudent() == null) {
                        return "noAuthorization";
                    }
                    if (insideEnrollmentPeriod(studentCurricularPlan)) {
                        Tutor tutor = verifyStudentWithTutor(studentCurricularPlan.getStudent());
                        if (tutor != null) {
                            return new String("error.enrollment.student.withTutor+"
                                    + tutor.getTeacher().getTeacherNumber().toString() + "+"
                                    + tutor.getTeacher().getPerson().getNome());
                        }
                    }
                    return null;
                } else {
                    return "noAuthorization";
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return "noAuthorization";
        }
        return null;
    }

    /**
     * @param arguments
     * @param sp
     * @return
     */
    protected boolean insideEnrollmentPeriod(StudentCurricularPlan studentCurricularPlan)
    		throws ExcepcaoPersistencia {
        try {
            ShowAvailableCurricularCoursesWithoutEnrollmentPeriod
                    .getEnrolmentPeriod(studentCurricularPlan);
        } catch (OutOfCurricularCourseEnrolmentPeriod e) {
            return false;
        }
        return true;
    }

    protected StudentCurricularPlan readStudentCurricularPlan(Object[] arguments)
            throws ExcepcaoPersistencia {
        IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();

        StudentCurricularPlan studentCurricularPlan = null;
        if (arguments[1] != null) {

            studentCurricularPlan = (StudentCurricularPlan) persistentObject.readByOID(
                    StudentCurricularPlan.class, (Integer) arguments[1]);
        } else {
            studentCurricularPlan = persistentStudentCurricularPlan
                    .readActiveByStudentNumberAndDegreeType((Integer) arguments[2],
                            DegreeType.DEGREE);
        }
        return studentCurricularPlan;
    }

    protected Student readStudent(IUserView id) throws ExcepcaoPersistencia {
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
        return persistentStudent.readByUsername(id.getUtilizador());
    }

    protected Student readStudent(Object[] arguments)
            throws ExcepcaoPersistencia {
        StudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(arguments);
        if (studentCurricularPlan == null) {
            return null;
        }

        return studentCurricularPlan.getStudent();
    }

    protected boolean curriculumOwner(Student student, IUserView id) {
        if (!student.getPerson().getUsername().equals(id.getUtilizador())) {
            return false;
        }
        return true;
    }

    /**
     * @param integer
     * @param sp
     * @return
     */
    protected Tutor verifyStudentWithTutor(Student student)
            throws ExcepcaoPersistencia {
        IPersistentTutor persistentTutor = persistentSupport.getIPersistentTutor();
        return persistentTutor.readTeachersByStudent(student);
    }

    protected Teacher readTeacher(IUserView id) throws ExcepcaoPersistencia {
        IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();

        return persistentTeacher.readTeacherByUsername(id.getUtilizador());
    }

    /**
     * @param teacher
     * @param arguments
     * @param sp
     * @return
     */
    protected boolean verifyStudentTutor(Teacher teacher, Student student)
            throws ExcepcaoPersistencia {
        IPersistentTutor persistentTutor = persistentSupport.getIPersistentTutor();

        Tutor tutor = persistentTutor.readTutorByTeacherAndStudent(teacher, student);

        return (tutor != null);
    }

    protected boolean verifyCoordinator(Teacher teacher, Object[] arguments)
            throws ExcepcaoPersistencia {
    	
        IPersistentCoordinator persistentCoordinator = persistentSupport.getIPersistentCoordinator();
        Coordinator coordinator = persistentCoordinator.readCoordinatorByTeacherIdAndExecutionDegreeId(
                teacher.getIdInternal(), (Integer) arguments[0]);
        if (coordinator == null) {
            return false;
        }
        
    	//check if is LEEC coordinator
    	if(!coordinator.getExecutionDegree().getDegreeCurricularPlan().getName().equals("LEEC 2003")) {
    		return false;
    	}

        StudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(arguments);
        if (studentCurricularPlan == null) {
            return false;
        }
        return studentCurricularPlan.getDegreeCurricularPlan().equals(
                coordinator.getExecutionDegree().getDegreeCurricularPlan());

    }
}
