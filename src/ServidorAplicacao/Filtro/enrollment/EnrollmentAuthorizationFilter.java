/*
 * Created on Jul 5, 2004
 *
 */
package ServidorAplicacao.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import DataBeans.InfoRole;
import Dominio.ICoordinator;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.ITutor;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationByManyRolesFilter;
import ServidorAplicacao.Servico.enrollment.ShowAvailableCurricularCoursesWithoutEnrollmentPeriod;
import ServidorAplicacao.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPersistentTutor;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.TipoCurso;

/**
 * @author Jo�o Mota
 *  
 */
public class EnrollmentAuthorizationFilter extends
        AuthorizationByManyRolesFilter {

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
            List roles = getRoleList((List) id.getRoles());

            ISuportePersistente sp = null;
            sp = SuportePersistenteOJB.getInstance();

            if (roles.contains(RoleType.STUDENT)) {
                IStudent student = readStudent(id, sp);
                if (student == null) {
                    return "noAuthorization";
                }
                if (!curriculumOwner(student, id)) {
                    return "noAuthorization";
                }
                ITutor tutor = verifyStudentWithTutor(student, sp);
                if (tutor != null) {
                    return new String("error.enrollment.student.withTutor+"
                            + tutor.getTeacher().getTeacherNumber().toString()
                            + "+" + tutor.getTeacher().getPerson().getNome());
                }
            } else {

                if (roles.contains(RoleType.COORDINATOR)
                        && arguments[0] != null) {
                    ITeacher teacher = readTeacher(id, sp);
                    if (teacher == null) {
                        return "noAuthorization";
                    }

                    if (!verifyCoordinator(teacher, arguments, sp)) {
                        return "noAuthorization";
                    }
                } else if (roles.contains(RoleType.TEACHER)) {
                    ITeacher teacher = readTeacher(id, sp);
                    if (teacher == null) {
                        return "noAuthorization";
                    }

                    IStudent student = readStudent(arguments, sp);
                    if (student == null) {
                        return "noAuthorization";
                    }

                    if (!verifyStudentTutor(teacher, student, sp)) {
                        return new String("error.enrollment.notStudentTutor+"
                                + student.getNumber().toString());
                    }

                } else if (roles
                        .contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                        || roles
                                .contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
                    IStudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(
                            arguments, sp);

                    if (studentCurricularPlan.getStudent() == null) {
                        return "noAuthorization";
                    }
                    if (insideEnrollmentPeriod(studentCurricularPlan, sp)) {
                        ITutor tutor = verifyStudentWithTutor(
                                studentCurricularPlan.getStudent(), sp);
                        if (tutor != null) {
                            return new String(
                                    "error.enrollment.student.withTutor+"
                                            + tutor.getTeacher()
                                                    .getTeacherNumber()
                                                    .toString()
                                            + "+"
                                            + tutor.getTeacher().getPerson()
                                                    .getNome());
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
    protected boolean insideEnrollmentPeriod(
            IStudentCurricularPlan studentCurricularPlan, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        try {
            ShowAvailableCurricularCoursesWithoutEnrollmentPeriod
                    .getEnrolmentPeriod(studentCurricularPlan);
        } catch (OutOfCurricularCourseEnrolmentPeriod e) {
            return false;
        }
        return true;
    }

    protected IStudentCurricularPlan readStudentCurricularPlan(
            Object[] arguments, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IStudentCurricularPlanPersistente persistentStudentCurricularPlan = sp
                .getIStudentCurricularPlanPersistente();

        IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
        if (arguments[1] != null) {

            studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan
                    .readByOID(StudentCurricularPlan.class,
                            (Integer) arguments[1]);
        } else {
            studentCurricularPlan = persistentStudentCurricularPlan
                    .readActiveByStudentNumberAndDegreeType(
                            (Integer) arguments[2], TipoCurso.LICENCIATURA_OBJ);
        }
        return studentCurricularPlan;
    }

    protected IStudent readStudent(IUserView id, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();

        return persistentStudent.readByUsername(id.getUtilizador());
    }

    protected IStudent readStudent(Object[] arguments, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IStudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(
                arguments, sp);
        if (studentCurricularPlan == null) {
            return null;
        }

        return studentCurricularPlan.getStudent();
    }

    protected boolean curriculumOwner(IStudent student, IUserView id) {
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
    protected ITutor verifyStudentWithTutor(IStudent student,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentTutor persistentTutor = sp.getIPersistentTutor();

        ITutor tutor = persistentTutor.readTeachersByStudent(student);

        return tutor;
    }

    protected ITeacher readTeacher(IUserView id, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

        return persistentTeacher.readTeacherByUsername(id.getUtilizador());
    }

    /**
     * @param teacher
     * @param arguments
     * @param sp
     * @return
     */
    protected boolean verifyStudentTutor(ITeacher teacher, IStudent student,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentTutor persistentTutor = sp.getIPersistentTutor();

        ITutor tutor = persistentTutor.readTutorByTeacherAndStudent(teacher,
                student);

        return (tutor != null);
    }

    protected boolean verifyCoordinator(ITeacher teacher, Object[] arguments,
            ISuportePersistente sp) throws ExcepcaoPersistencia {

        IPersistentCoordinator persistentCoordinator = sp
                .getIPersistentCoordinator();
        ICoordinator coordinator = persistentCoordinator
                .readCoordinatorByTeacherAndExecutionDegreeId(teacher,
                        (Integer) arguments[0]);
        if (coordinator == null) {
            return false;
        }
        IStudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(
                arguments, sp);
        if (studentCurricularPlan == null) {
            return false;
        }
        return studentCurricularPlan.getDegreeCurricularPlan().equals(
                coordinator.getExecutionDegree().getCurricularPlan());

    }
}