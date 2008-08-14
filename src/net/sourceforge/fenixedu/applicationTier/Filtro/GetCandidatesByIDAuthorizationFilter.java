package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class GetCandidatesByIDAuthorizationFilter extends Filtro {

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
	IUserView id = getRemoteUser(request);
	Object[] arguments = getServiceCallArguments(request);
	if ((id != null && id.getRoleTypes() != null && !containsRoleType(id.getRoleTypes()))
		|| (id != null && id.getRoleTypes() != null && !hasPrivilege(id, arguments)) || (id == null)
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
	if (id.hasRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
	    return true;
	}

	if (id.hasRoleType(RoleType.COORDINATOR)) {
	    // Read The ExecutionDegree
	    Integer candidateID = (Integer) arguments[0];

	    final Person person = id.getPerson();

	    MasterDegreeCandidate masterDegreeCandidate = rootDomainObject.readMasterDegreeCandidateByOID(candidateID);
	    if (masterDegreeCandidate == null) {
		return false;
	    }

	    // modified by T�nia Pous�o
	    Coordinator coordinator = masterDegreeCandidate.getExecutionDegree().getCoordinatorByTeacher(person);
	    if (coordinator == null) {
		return false;
	    }

	    return true;
	}
	return true;
    }

}