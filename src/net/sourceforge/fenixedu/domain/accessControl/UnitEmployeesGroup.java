/*
 * Author : Goncalo Luiz
 * Creation Date: Jun 27, 2006,12:38:18 PM
 */
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jun 27, 2006,12:38:18 PM
 * 
 */
public class UnitEmployeesGroup extends DomainBackedGroup<Unit> {

    private static final long serialVersionUID = 1L;

    public UnitEmployeesGroup(Unit unit) {
	super(unit);
    }

    @Override
    public Set<Person> getElements() {
	final Set<Person> elements = super.buildSet();
	for (final Employee employee : this.getObject().getAllCurrentActiveWorkingEmployees()) {
	    elements.add(employee.getPerson());
	}
	return elements;
    }

    @Override
    public boolean isMember(Person person) {
	return (person != null && person.hasEmployee() && person.getEmployee().getCurrentWorkingPlace() == getObject());
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    try {
		return new UnitEmployeesGroup((Unit) arguments[0]);
	    } catch (ClassCastException e) {
		throw new GroupDynamicExpressionException("accessControl.group.builder.unitEmployees.notUnit", arguments[0]
			.toString());
	    }
	}

	public int getMinArguments() {
	    return 1;
	}

	public int getMaxArguments() {
	    return 1;
	}

    }
}
