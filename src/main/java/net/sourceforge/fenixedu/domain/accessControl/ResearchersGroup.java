package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;

public class ResearchersGroup extends DomainBackedGroup<ResearchUnit> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ResearchersGroup(ResearchUnit object) {
        super(object);
    }

    @Override
    public Set<Person> getElements() {
        return Collections.emptySet();
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            ResearchUnit unit = (ResearchUnit) arguments[0];
            return new ResearchersGroup(unit);
        }

        @Override
        public int getMaxArguments() {
            return 1;
        }

        @Override
        public int getMinArguments() {
            return 1;
        }

    }
}
