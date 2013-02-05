package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.beanutils.MethodUtils;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PeopleForUnitGroups implements DataProvider {

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        Unit unit;
        if (source instanceof Unit) {
            unit = (Unit) source;
        } else {
            try {
                unit = (Unit) MethodUtils.invokeMethod(source, "getUnit", null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return getPeopleForUnit(unit);
    }

    private Collection<Person> getPeopleForUnit(Unit unit) {
        Set<Person> people = new HashSet<Person>();
        people.addAll(unit.getPossibleGroupMembers());
        for (Unit subUnit : unit.getAllSubUnits()) {
            if (subUnit.isResearchUnit()) {
                people.addAll(subUnit.getPossibleGroupMembers());
            }
        }
        for (PersistentGroupMembers persistentGroupMembers : unit.getPersistentGroups()) {
            for (Person person : persistentGroupMembers.getPersons()) {
                if (!people.contains(person)) {
                    people.add(person);
                }
            }
        }
        return people;
    }
}
