/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationUtils;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * Base class for authorization issues on credits information edition done by
 * department members.
 * 
 * @author jpvl
 */
public abstract class AbstractTeacherDepartmentAuthorization extends Filtro {

    public void execute(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
        IUserView requester = (IUserView) serviceRequest.getRequester();
        if ((requester == null)
                || !AuthorizationUtils.containsRole(requester.getRoles(),
                        RoleType.DEPARTMENT_CREDITS_MANAGER)) {
            throw new NotAuthorizedException();
        }

        Integer teacherId = getTeacherId(serviceRequest.getServiceParameters().parametersArray());
        if (teacherId != null) {
            
            Person requesterPerson = Person.readPersonByUsername(requester.getUtilizador());

            Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);

            Department teacherDepartment = teacher.getCurrentWorkingDepartment();

            List departmentsWithAccessGranted = requesterPerson.getManageableDepartmentCredits();

            if (!departmentsWithAccessGranted.contains(teacherDepartment)) {
                throw new NotAuthorizedException();
            }
        }

    }

    protected abstract Integer getTeacherId(Object[] arguments)
    	throws FenixServiceException, ExcepcaoPersistencia;
}