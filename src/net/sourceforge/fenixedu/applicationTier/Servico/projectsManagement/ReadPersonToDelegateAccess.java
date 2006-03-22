package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadPersonToDelegateAccess extends Service {

    public InfoPerson run(String userView, String costCenter, String username, String userNumber) throws FenixServiceException, ExcepcaoPersistencia {
        Person person = Person.readPersonByUsername(username);
        if (person == null) {
            throw new ExcepcaoInexistente();
        } else if (!isTeacherOrEmployee(person)) {
            throw new InvalidArgumentsServiceException();
        }
        return InfoPerson.newInfoFromDomain(person);
    }

    private boolean isTeacherOrEmployee(Person person) throws ExcepcaoPersistencia {
        if (Teacher.readTeacherByUsername(person.getUsername()) == null) {
            if (person.getEmployee() == null) {
                return false;
            }
        }
        return true;
    }

}
