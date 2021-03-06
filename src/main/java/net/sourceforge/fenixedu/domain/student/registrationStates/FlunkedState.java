/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
public class FlunkedState extends FlunkedState_Base {

    protected FlunkedState(Registration registration, Person person, DateTime dateTime) {
        super();
        init(registration, person, dateTime);
    }

    @Override
    public Set<String> getValidNextStates() {
        Set<String> states = new HashSet<String>();
        states.add(RegistrationStateType.CANCELED.name());
        states.add(RegistrationStateType.REGISTERED.name());
        states.add(RegistrationStateType.INTERNAL_ABANDON.name());
        states.add(RegistrationStateType.EXTERNAL_ABANDON.name());
        states.add(RegistrationStateType.MOBILITY.name());
        return states;
    }

    @Override
    public RegistrationStateType getStateType() {
        return RegistrationStateType.FLUNKED;
    }

}
