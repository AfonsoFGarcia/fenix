package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class StudentListByCurricularCourseAuthorizationFilter extends Filtro {

    /**
     *  
     */
    public StudentListByCurricularCourseAuthorizationFilter() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = (IUserView) request.getRequester();
        if ((id != null && id.getRoles() != null && !containsRole(id.getRoles()))
                || (id != null && id.getRoles() != null && !hasPrivilege(id, request.getServiceParameters().parametersArray()))
                || (id == null) || (id.getRoles() == null)) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection getNeededRoles() {
        List roles = new ArrayList();

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.COORDINATOR);
        roles.add(infoRole);

        return roles;
    }

    private List getRoleList(List roles) {
        List result = new ArrayList();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            result.add(((InfoRole) iterator.next()).getRoleType());
        }

        return result;
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean hasPrivilege(IUserView id, Object[] arguments) {

        List roles = getRoleList((List) id.getRoles());
        CollectionUtils.intersection(roles, getNeededRoles());

        Integer curricularCourseID = (Integer) arguments[1];

        ICurricularCourse curricularCourse = null;

        // Read The DegreeCurricularPlan
        try {
            IPersistentCurricularCourse persistentCurricularCourse = PersistenceSupportFactory.getDefaultPersistenceSupport()
                    .getIPersistentCurricularCourse();

            curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                    CurricularCourse.class, curricularCourseID);
        } catch (Exception e) {
            return false;
        }

        if (curricularCourse == null) {
            return false;
        }

        List roleTemp = new ArrayList();
        roleTemp.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            if (curricularCourse.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                    TipoCurso.MESTRADO_OBJ)) {
                return true;
            }
            return false;

        }

        roleTemp = new ArrayList();
        roleTemp.add(RoleType.COORDINATOR);
        if (CollectionUtils.containsAny(roles, roleTemp)) {

            // Read The ExecutionDegree
            try {
                IPersistentExecutionDegree persistentExecutionDegree = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentExecutionDegree();

                List executionDegrees = persistentExecutionDegree
                        .readByDegreeCurricularPlan(curricularCourse.getDegreeCurricularPlan());
                if (executionDegrees == null) {
                    return false;
                }
                // IMPORTANT: It's assumed that the coordinator for a Degree is
                // ALWAYS the same
                //modified by T�nia Pous�o
                List coodinatorsList = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentCoordinator()
                        .readCoordinatorsByExecutionDegree(((IExecutionDegree) executionDegrees.get(0)));
                if (coodinatorsList == null) {
                    return false;
                }
                ListIterator listIterator = coodinatorsList.listIterator();
                while (listIterator.hasNext()) {
                    ICoordinator coordinator = (ICoordinator) listIterator.next();

                    if (id.getUtilizador().equals(coordinator.getTeacher().getPerson().getUsername())) {
                        return true;
                    }
                }

                //                teacher = ((IExecutionDegree)
                // executionDegrees.get(0)).getCoordinator();
                //
                //                if (teacher == null)
                //                {
                //                    return false;
                //                }
                //
                //                if
                // (id.getUtilizador().equals(teacher.getPerson().getUsername()))
                //                {
                //                    return true;
                //                }
                //                else
                //                {
                //                    return false;
                //                }
            } catch (Exception e) {
                return false;
            }
            return false;
        }
        return false;
    }
}