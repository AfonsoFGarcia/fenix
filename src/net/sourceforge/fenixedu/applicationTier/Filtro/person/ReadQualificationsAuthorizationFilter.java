/*
 * Created on 10/Nov/2003
 */

package net.sourceforge.fenixedu.applicationTier.Filtro.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationUtils;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Barbosa
 * @author Pica
 */
public class ReadQualificationsAuthorizationFilter extends Filtro {

    public ReadQualificationsAuthorizationFilter() {
    }

    //Role Type of teacher
    protected RoleType getRoleTypeTeacher() {
        return RoleType.TEACHER;
    }

    //Role Type of Grant Owner Manager
    protected RoleType getRoleTypeGrantOwnerManager() {
        return RoleType.GRANT_OWNER_MANAGER;
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);
        try {
            //Verify if needed fields are null
            if ((id == null) || (id.getRoles() == null)) {
                throw new NotAuthorizedException();
            }

            //Verify if:
            // 1: The user ir a Grant Owner Manager and the qualification
            // belongs to a Grant Owner
            // 2: The user ir a Teacher and the qualification is his own
            boolean valid = false;

            if ((AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeGrantOwnerManager()))
                    && isGrantOwner((String) arguments[0])) {
                valid = true;
            }

            if (AuthorizationUtils.containsRole(id.getRoles(), getRoleTypeTeacher())) {
                valid = true;
            }

            if (!valid)
                throw new NotAuthorizedException();
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    /**
     * Verifies if the qualification user ir a grant owner
     * 
     * @param arguments
     * @return true or false
     */
    private boolean isGrantOwner(String user) {
        try {
            IPessoaPersistente persistentPerson = persistentSupport.getIPessoaPersistente();
            IPersistentGrantOwner persistentGrantOwner = persistentSupport.getIPersistentGrantOwner();

            Person person = Person.readPersonByUsername(user);
            //Try to read the grant owner from de database
            GrantOwner grantOwner = persistentGrantOwner.readGrantOwnerByPerson(person.getIdInternal());

            return grantOwner != null;
        } catch (ExcepcaoPersistencia e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}