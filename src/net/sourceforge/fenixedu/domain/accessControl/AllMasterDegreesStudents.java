/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 13, 2006,4:41:12 PM
 */
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jul 13, 2006,4:41:12 PM
 * 
 */
public class AllMasterDegreesStudents extends Group {

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();

        final Role role = Role.getRoleByRoleType(RoleType.STUDENT);
        for (final Person person : role.getAssociatedPersons()) {
            Registration registration = person.getStudentByType(DegreeType.MASTER_DEGREE);
            if (registration != null)
                elements.add(person);
        }
        return elements;
    }
    
    @Override
    public boolean isMember(Person person) {
	final Student student = person.getStudent();
	if (student != null) {
	    for (final Registration registration : student.getRegistrationsSet()) { 
		if (registration.isMasterDegreeOrBolonhaMasterDegree()){
		    return true;
		}
	    }
	}
	return false;
    }

}
