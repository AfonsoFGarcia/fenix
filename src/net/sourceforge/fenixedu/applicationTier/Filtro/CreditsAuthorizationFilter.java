/*
 * Created on 13/Mar/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author jpvl
 */
public class CreditsAuthorizationFilter extends Filtro {

    // the singleton of this class
    public CreditsAuthorizationFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView requester = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        Collection roles = requester.getRoles();
        boolean authorizedRequester = false;
        // ATTENTION: ifs order matters...
        if (AuthorizationUtils.containsRole(roles, RoleType.CREDITS_MANAGER)) {
            authorizedRequester = true;
        } else if (AuthorizationUtils.containsRole(roles, RoleType.DEPARTMENT_CREDITS_MANAGER)) {
            Teacher teacherToEdit = readTeacher(arguments[0]);

            IPessoaPersistente personDAO = persistentSupport.getIPessoaPersistente();
            Person requesterPerson = personDAO.lerPessoaPorUsername(requester.getUtilizador());

            List departmentsWithAccessGranted = requesterPerson.getManageableDepartmentCredits();            
            Department department = teacherToEdit.getCurrentWorkingDepartment();
            authorizedRequester = departmentsWithAccessGranted.contains(department);

        } else if (AuthorizationUtils.containsRole(roles, RoleType.TEACHER)) {
            Teacher teacherToEdit = readTeacher(arguments[0]);
            authorizedRequester = teacherToEdit.getPerson().getUsername().equals(
                    requester.getUtilizador());

        }

        if (!authorizedRequester) {
            throw new NotAuthorizedFilterException(" -----------> User = " + requester.getUtilizador()
                    + "ACCESS NOT GRANTED!");
        }

    }

    /**
     * @param object
     * @return @throws
     *         ExcepcaoPersistencia
     */
    private Teacher readTeacher(Object object) throws ExcepcaoPersistencia {
        Integer teacherOID = null;
        if (object instanceof InfoTeacher) {
            teacherOID = ((InfoTeacher) object).getIdInternal();
        } else if (object instanceof Integer) {
            teacherOID = (Integer) object;
        }
        return (Teacher) persistentObject.readByOID(Teacher.class, teacherOID);
    }
}