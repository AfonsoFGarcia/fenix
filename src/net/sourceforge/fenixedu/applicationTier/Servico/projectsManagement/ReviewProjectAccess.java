/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonRole;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.projectsManagement.IPersistentProjectAccess;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Susana Fernandes
 */
public class ReviewProjectAccess extends Service {

    public ReviewProjectAccess() {
    }

    public void run(String username, String costCenter, String userNumber) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentProjectAccess persistentProjectAccess = sp.getIPersistentProjectAccess();
        Person person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
        Role role = sp.getIPersistentRole().readByRoleType(RoleType.PROJECTS_MANAGER);
        if (persistentProjectAccess.countByPersonAndCC(person, false) == 0) {
            Teacher teacher = sp.getIPersistentTeacher().readTeacherByUsername(person.getUsername());
            if (teacher == null) {
                Employee employee = sp.getIPersistentEmployee().readByPerson(person);
                if (employee != null) {
                    IPersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getInstance();
                    if ((persistentSuportOracle.getIPersistentProject().countUserProject(employee.getEmployeeNumber()) == 0)) {
                        persistentProjectAccess.deleteByPersonAndCC(person, false);
                        PersonRole personRole = sp.getIPersistentPersonRole().readByPersonAndRole(person, role);
                        if (personRole != null)
                            sp.getIPersistentPersonRole().deleteByOID(PersonRole.class, personRole.getIdInternal());
                    }
                } else
                    throw new FenixServiceException();
            }
        }

        role = sp.getIPersistentRole().readByRoleType(RoleType.INSTITUCIONAL_PROJECTS_MANAGER);
        if (persistentProjectAccess.countByPersonAndCC(person, true) == 0) {
            Teacher teacher = sp.getIPersistentTeacher().readTeacherByUsername(person.getUsername());
            if (teacher == null) {
                Employee employee = sp.getIPersistentEmployee().readByPerson(person);
                if (employee != null) {
                    IPersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getInstance();
                    if ((persistentSuportOracle.getIPersistentProject().countUserProject(employee.getEmployeeNumber()) == 0)) {
                        persistentProjectAccess.deleteByPersonAndCC(person, true);
                        PersonRole personRole = sp.getIPersistentPersonRole().readByPersonAndRole(person, role);
                        if (personRole != null)
                            sp.getIPersistentPersonRole().deleteByOID(PersonRole.class, personRole.getIdInternal());
                    }
                } else
                    throw new FenixServiceException();
            }
        }
    }
}