package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class StudentListByCurricularCourseScopeAuthorizationFilter extends Filtro {

    public StudentListByCurricularCourseScopeAuthorizationFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk
     * .ServiceRequest, pt.utl.ist.berserk.ServiceResponse)
     */
    @Override
    public void execute(ServiceRequest request) throws Exception {
        IUserView id = AccessControl.getUserView();
        Object[] argumentos = request.getServiceParameters().parametersArray();
        if ((id != null && id.getRoleTypes() != null && !containsRoleType(id.getRoleTypes()))
                || (id != null && id.getRoleTypes() != null && !hasPrivilege(id, argumentos)) || (id == null)
                || (id.getRoleTypes() == null)) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @return The Needed Roles to Execute The Service
     */
    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(RoleType.COORDINATOR);
        return roles;
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean hasPrivilege(IUserView id, Object[] arguments) {
        Integer curricularCourseScopeID = (Integer) arguments[1];

        CurricularCourseScope curricularCourseScope = RootDomainObject.getInstance().readCurricularCourseScopeByOID(curricularCourseScopeID);

        if (curricularCourseScope == null) {
            return false;
        }

        if (id.hasRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
            DegreeType degreeType =
                    curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan().getDegree().getDegreeType();
            if (degreeType.equals(DegreeType.MASTER_DEGREE) || degreeType.equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)) {
                return true;
            }
            return false;

        }

        if (id.hasRoleType(RoleType.COORDINATOR)) {
            List executionDegrees = curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan().getExecutionDegrees();
            if (executionDegrees == null) {
                return false;
            }
            // IMPORTANT: It's assumed that the coordinator for a Degree is
            // ALWAYS the same
            // modified by Tânia Pousão
            List<Coordinator> coodinatorsList = ((ExecutionDegree) executionDegrees.get(0)).getCoordinatorsList();
            if (coodinatorsList == null) {
                return false;
            }
            ListIterator listIterator = coodinatorsList.listIterator();
            while (listIterator.hasNext()) {
                Coordinator coordinator = (Coordinator) listIterator.next();

                if (coordinator.getPerson() == id.getPerson()) {
                    return true;
                }
            }

        }
        return false;
    }

}