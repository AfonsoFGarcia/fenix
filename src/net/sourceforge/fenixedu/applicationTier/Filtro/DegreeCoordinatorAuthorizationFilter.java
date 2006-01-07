/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Jo�o Mota
 *  
 */
public class DegreeCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

    public DegreeCoordinatorAuthorizationFilter() {
    }

    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);

        try {
            if ((id == null) || (id.getRoles() == null)
                    || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                    || !isCoordinatorOfExecutionDegree(id, argumentos)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean isCoordinatorOfExecutionDegree(IUserView id, Object[] argumentos) {

        ISuportePersistente sp;
        boolean result = false;
        if (argumentos == null) {
            return result;
        }
        if (argumentos[0] == null) {
            return result;
        }
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            Teacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();

            Coordinator coordinator = persistentCoordinator
                    .readCoordinatorByTeacherIdAndExecutionDegreeId(teacher.getIdInternal(), (Integer) argumentos[0]);

            result = coordinator != null;

        } catch (Exception e) {
            return false;
        }

        return result;
    }

}