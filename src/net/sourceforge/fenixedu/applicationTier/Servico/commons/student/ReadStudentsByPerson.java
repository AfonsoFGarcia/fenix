package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author David Santos in Mar 5, 2004
 */

public class ReadStudentsByPerson extends Service {

    public List run(InfoPerson infoPerson) throws ExcepcaoPersistencia {
        Person person = (Person) persistentObject.readByOID(Person.class, infoPerson.getIdInternal());

        final List students = person.getStudents();
        final List infoStudents = new ArrayList(students.size());
        for (final Iterator iterator = students.iterator(); iterator.hasNext();) {
            final InfoStudent infoStudent = InfoStudentWithInfoPerson
                    .newInfoFromDomain((Student) iterator.next());
            infoStudents.add(infoStudent);
        }
        return infoStudents;
    }
}