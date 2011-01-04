package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class InterruptedState extends InterruptedState_Base {

    protected InterruptedState(Registration registration, Person person, DateTime dateTime) {
	super();
	init(registration, person, dateTime);
    }

    @Override
    public RegistrationStateType getStateType() {
	return RegistrationStateType.INTERRUPTED;
    }

    @Override
    public Set<String> getValidNextStates() {
	Set<String> states = new HashSet<String>();
	states.add(RegistrationStateType.CANCELED.name());
	states.add(RegistrationStateType.REGISTERED.name());
	states.add(RegistrationStateType.INTERNAL_ABANDON.name());
	states.add(RegistrationStateType.EXTERNAL_ABANDON.name());
	states.add(RegistrationStateType.MOBILITY.name());
	states.add(RegistrationStateType.SCHOOLPARTCONCLUDED.name());
	return states;
    }

}
