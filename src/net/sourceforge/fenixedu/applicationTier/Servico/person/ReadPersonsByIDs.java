package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class ReadPersonsByIDs implements IService {

    public List<InfoPerson> run(List<Integer> personsInternalIds) throws ExcepcaoPersistencia {
        List<InfoPerson> persons = new ArrayList<InfoPerson>();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();

        for (Integer personId : personsInternalIds) {
            Person person = (Person) persistentPerson.readByOID(Person.class, personId);
            persons.add(InfoPerson.newInfoFromDomain(person));
        }

        return persons;
    }

}