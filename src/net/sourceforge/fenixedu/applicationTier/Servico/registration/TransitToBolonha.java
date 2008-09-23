package net.sourceforge.fenixedu.applicationTier.Servico.registration;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;

public class TransitToBolonha extends FenixService {

    public void run(final Person person, final Registration sourceRegistrationForTransition, final DateTime dateTime) {
	sourceRegistrationForTransition.transitToBolonha(person, dateTime);
    }

}
