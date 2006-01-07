package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPersonAccount;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class PersonAccountVO extends VersionedObjectsBase implements IPersistentPersonAccount {

    public PersonAccount readByPerson(final Integer personOID) throws ExcepcaoPersistencia {
		final Person person = (Person) readByOID(Person.class, personOID);
		return person.getAssociatedPersonAccount();
    }

}