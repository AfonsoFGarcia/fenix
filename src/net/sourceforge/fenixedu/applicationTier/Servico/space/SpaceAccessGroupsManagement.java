package net.sourceforge.fenixedu.applicationTier.Servico.space;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.Space.SpaceAccessGroupType;

public class SpaceAccessGroupsManagement extends Service {

    public void run(Space space, SpaceAccessGroupType accessGroupType, Person person, boolean toAdd) throws FenixServiceException {
	if (person != null) {
	    if (accessGroupType.equals(SpaceAccessGroupType.PERSON_OCCUPATION_ACCESS_GROUP)) {
		if (space.getPersonOccupationsAccessGroup() == null) {
		    space.setPersonOccupationsAccessGroup(new FixedSetGroup());
		}
		Set<Person> elements = space.getPersonOccupationsAccessGroup().getElements();
		Set<Person> newElements = manageAccessGroup(person, toAdd, elements);
		space.setPersonOccupationsAccessGroup(new FixedSetGroup(newElements));

	    } else if (accessGroupType.equals(SpaceAccessGroupType.EXTENSION_OCCUPATION_ACCESS_GROUP)) {
		if (space.getExtensionOccupationsAccessGroup() == null) {
		    space.setExtensionOccupationsAccessGroup(new FixedSetGroup());
		}
		Set<Person> elements = space.getExtensionOccupationsAccessGroup().getElements();
		Set<Person> newElements = manageAccessGroup(person, toAdd, elements);
		space.setExtensionOccupationsAccessGroup(new FixedSetGroup(newElements));
	    }
	} else {
	    throw new FenixServiceException("error.space.access.groups.management.no.person");
	}
    }

    private Set<Person> manageAccessGroup(Person person, boolean toAdd, Set<Person> elements) {
	Set<Person> newElements;
	if (toAdd) {
	    newElements = addPersonToAccessGroup(person, elements);
	} else {
	    newElements = removePersonFromAccessGroup(person, elements);
	}
	return newElements;
    }

    private Set<Person> addPersonToAccessGroup(Person person, Set<Person> elements) {
	Set<Person> newList = new TreeSet<Person>(Person.COMPARATOR_BY_NAME);
	newList.addAll(elements);
	newList.add(person);
	return newList;
    }

    private Set<Person> removePersonFromAccessGroup(Person person, Set<Person> elements) {
	Set<Person> newList = new TreeSet<Person>(Person.COMPARATOR_BY_NAME);
	newList.addAll(elements);
	newList.remove(person);
	return newList;
    }
}
