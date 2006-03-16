package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

//modified by gedl AT rnl dot IST dot uTl dot pT , September the 16th, 2003
//added the auth to a lecturing teacher

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */
public class ReadShiftsByExecutionCourseIDAuthorizationFilter extends Filtro {

    public ReadShiftsByExecutionCourseIDAuthorizationFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);
        if ((((id != null && id.getRoles() != null && !containsRole(id.getRoles()))
                || (id != null && id.getRoles() != null && !hasPrivilege(id, argumentos))
                || (id == null) || (id.getRoles() == null)))
                && (!lecturesExecutionCourse(id, argumentos))) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection getNeededRoles() {
        List roles = new ArrayList();

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.TIME_TABLE_MANAGER);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.COORDINATOR);
        roles.add(infoRole);

        return roles;
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean hasPrivilege(IUserView id, Object[] arguments) throws ExcepcaoPersistencia {

        List roles = getRoleList(id.getRoles());
        CollectionUtils.intersection(roles, getNeededRoles());

        List roleTemp = new ArrayList();
        roleTemp.add(RoleType.TIME_TABLE_MANAGER);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            return true;
        }

        roleTemp = new ArrayList();
        roleTemp.add(RoleType.COORDINATOR);
        if (CollectionUtils.containsAny(roles, roleTemp)) {

            Teacher teacher = null;
            // Read The ExecutionDegree
            try {

                Integer executionCourseID = (Integer) arguments[0];

                teacher = Teacher.readTeacherByUsername(id.getUtilizador());

                ExecutionCourse executionCourse = (ExecutionCourse) persistentObject
                        .readByOID(ExecutionCourse.class, executionCourseID);

                // For all Associated Curricular Courses
                Iterator curricularCourseIterator = executionCourse.getAssociatedCurricularCourses()
                        .iterator();
                while (curricularCourseIterator.hasNext()) {
                    CurricularCourse curricularCourse = (CurricularCourse) curricularCourseIterator
                            .next();

                    // Read All Execution Degrees for this Degree Curricular
                    // Plan

                    List executionDegrees = persistentSupport.getIPersistentExecutionDegree()
                            .readByDegreeCurricularPlan(
                                    curricularCourse.getDegreeCurricularPlan().getIdInternal());

                    // Check if the Coordinator is the logged one
                    Iterator executionDegreesIterator = executionDegrees.iterator();
                    while (executionDegreesIterator.hasNext()) {
                        ExecutionDegree executionDegree = (ExecutionDegree) executionDegreesIterator
                                .next();

                        // modified by T�nia Pous�o
                        Coordinator coordinator = persistentSupport.getIPersistentCoordinator()
                                .readCoordinatorByTeacherIdAndExecutionDegreeId(teacher.getIdInternal(),
                                        executionDegree.getIdInternal());
                        if (coordinator != null) {
                            return true;
                        }

                        // if ((executionDegree.getCoordinator() != null)
                        // &&
                        // (executionDegree.getCoordinator().equals(teacher)))
                        // {
                        // return true;
                        // }
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private List getRoleList(Collection roles) {
        List result = new ArrayList();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            result.add(((InfoRole) iterator.next()).getRoleType());
        }

        return result;
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
            
            Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());
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
}