package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;

public class AddIncompatiblePerson extends FenixService {

    public void run(Vigilant vigilant, Person person) {

	if (vigilant.hasIncompatiblePerson()) {
	    vigilant.removeIncompatiblePerson();
	}
	if (person.getCurrentVigilant().hasIncompatiblePerson()) {
	    person.getCurrentVigilant().removeIncompatiblePerson();
	}
	vigilant.setIncompatiblePerson(person);
    }

}