/*
 * Created on Nov 12, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 * 
 */
public class ExecutionCourseAndExamLecturingTeacherAuthorizationFilter extends AuthorizationByRoleFilter {

    public ExecutionCourseAndExamLecturingTeacherAuthorizationFilter() {
    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        try {
            if ((id == null) || (id.getRoles() == null)
                    || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                    || !lecturesExecutionCourse(id, arguments)
                    || !examBelongsExecutionCourse(id, arguments)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }

    }

    private boolean lecturesExecutionCourse(IUserView id, Object[] argumentos) {

        Integer executionCourseID = null;

        if (argumentos == null) {
            return false;
        }
        try {
            if (argumentos[0] instanceof InfoExecutionCourse) {
                InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourseID = infoExecutionCourse.getIdInternal();
            } else {
                executionCourseID = (Integer) argumentos[0];
            }

            IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
            Teacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            Professorship professorship = null;
            if (teacher != null) {
                IPersistentProfessorship persistentProfessorship = persistentSupport.getIPersistentProfessorship();
                professorship = persistentProfessorship.readByTeacherAndExecutionCourse(teacher
                        .getIdInternal(), executionCourseID);
            }
            return professorship != null;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean examBelongsExecutionCourse(IUserView id, Object[] argumentos) {
        InfoExecutionCourse infoExecutionCourse = null;
        ExecutionCourse executionCourse = null;

        if (argumentos == null) {
            return false;
        }
        try {
            IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();

            if (argumentos[0] instanceof InfoExecutionCourse) {
                infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourse = (ExecutionCourse) persistentObject.readByOID(
                        ExecutionCourse.class, infoExecutionCourse.getIdInternal());
            } else {
                executionCourse = (ExecutionCourse) persistentObject.readByOID(
                        ExecutionCourse.class, (Integer) argumentos[0]);
            }

            Integer examId;
            if (argumentos[1] instanceof InfoExam) {
                InfoExam infoExam = (InfoExam) argumentos[1];
                examId = infoExam.getIdInternal();
            } else {
                examId = (Integer) argumentos[1];
            }

            if (executionCourse != null && examId != null) {
                for (Evaluation associatedEvaluation : executionCourse.getAssociatedEvaluations()) {
                    if (associatedEvaluation.getIdInternal().equals(examId)) {
                        return true;
                    }
                }
                return false;
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }

}
