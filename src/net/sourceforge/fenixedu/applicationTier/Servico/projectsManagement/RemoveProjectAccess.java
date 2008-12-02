/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProject;

/**
 * @author Susana Fernandes
 */
public class RemoveProjectAccess extends FenixService {

    public void run(String username, String costCenter, String personUsername, Integer projectCode, Boolean it, String userNumber)
	    throws ExcepcaoPersistencia {
	Person person = Person.readPersonByUsername(personUsername);

	RoleType roleType = RoleType.PROJECTS_MANAGER;
	Boolean isCostCenter = false;
	if (costCenter != null && !costCenter.equals("")) {
	    roleType = RoleType.INSTITUCIONAL_PROJECTS_MANAGER;
	    isCostCenter = true;
	}

	List<ProjectAccess> projectAccesses = ProjectAccess.getAllByPersonAndCostCenter(person, isCostCenter, true, it);

	if (projectAccesses.size() == 1) {
	    if (new PersistentProject().countUserProject(getUserNumber(person), it) == 0) {
		Iterator iter = person.getPersonRolesIterator();
		while (iter.hasNext()) {
		    Role role = (Role) iter.next();
		    if (role.getRoleType().equals(roleType)) {
			iter.remove();
		    }
		}
	    }
	}

	ProjectAccess projectAccess = ProjectAccess.getByPersonAndProject(person, projectCode, it);
	projectAccess.delete();
    }

    private Integer getUserNumber(Person person) {
	Integer userNumber = null;
	Teacher teacher = person.getTeacher();
	if (teacher != null)
	    userNumber = teacher.getTeacherNumber();
	else {
	    Employee employee = person.getEmployee();
	    if (employee != null)
		userNumber = employee.getEmployeeNumber();
	}
	return userNumber;
    }
}
