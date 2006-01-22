package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonRole;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class ChangeStudentInfoAuthorization extends AuthorizationByRoleFilter {

    public final static ChangeStudentInfoAuthorization instance = new ChangeStudentInfoAuthorization();

    public static Filtro getInstance() {
        return instance;
    }

    protected RoleType getRoleType() {
        return RoleType.DEGREE_ADMINISTRATIVE_OFFICE;
    }
    
    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        // Temporaraly unavailable due to syncronization with external systems.
        // When this service is to be activated just delete these three lines.
        if (true) {
            throw new NotAuthorizedFilterException();
        }

        IUserView userView = (IUserView) request.getRequester();
        if (!AuthorizationUtils.containsRole(userView.getRoles(), RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
            super.execute(request, response);
        }
        
        InfoPerson infoPerson = (InfoPerson) request.getServiceParameters().parametersArray()[0];
        
        Person person = (Person) persistentObject.readByOID(Person.class, infoPerson.getIdInternal());
        Role teacherRole = (Role) persistentSupport.getIPersistentRole().readByRoleType(RoleType.TEACHER);
        Role employeeRole = (Role) persistentSupport.getIPersistentRole().readByRoleType(RoleType.EMPLOYEE);
        
        for (PersonRole personRole : person.getAssociatedPersonRoles()) {
            if (personRole.getRole().equals(teacherRole) || personRole.getRole().equals(employeeRole)) {
                throw new NotAuthorizedFilterException();
            }
        }
        
    }

}
