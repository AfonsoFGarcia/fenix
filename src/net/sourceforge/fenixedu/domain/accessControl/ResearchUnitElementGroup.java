package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.util.BundleUtil;

public class ResearchUnitElementGroup extends DomainBackedGroup<ResearchUnit> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ResearchUnitElementGroup(ResearchUnit unit) {
	super(unit);
    }

    @Override
    public String getName() {
	return BundleUtil.getMessageFromModuleOrApplication("Researcher", "label.researchUnitElement");
    }

    @Override
    public Set<Person> getElements() {
	return new HashSet<Person>(getObject().getPossibleGroupMembers());
    }

    @Override
    public boolean isMember(Person person) {
	return person.getWorkingResearchUnitsAndParents().contains(getObject());
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    ResearchUnit unit = (ResearchUnit) arguments[0];
	    if (unit == null) {
		throw new VariableNotDefinedException("unit");
	    }
	    return new ResearchUnitElementGroup(unit);

	}

	public int getMaxArguments() {
	    return 1;
	}

	public int getMinArguments() {
	    return 1;
	}

    }
}
