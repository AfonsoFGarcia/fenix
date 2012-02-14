package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.util.BundleUtil;

public class WebSiteManagersGroup extends DomainBackedGroup<UnitSite> {

    /**
     * Default serial id.
     */
    private static final long serialVersionUID = 1L;

    public WebSiteManagersGroup(UnitSite site) {
	super(site);
    }

    @Override
    public Set<Person> getElements() {
	final UnitSite unitSite = getObject();
	return unitSite == null ? Collections.EMPTY_SET : unitSite.getManagersSet();
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getObject()) };
    }

    @Override
    public String getName() {
	return BundleUtil.getStringFromResourceBundle("resources.GroupNameResources",
		"label.name." + getClass().getSimpleName(), getObject().getUnit().getName());
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    UnitSite site = (UnitSite) arguments[0];
	    return new WebSiteManagersGroup(site);
	}

	public int getMaxArguments() {
	    return 1;
	}

	public int getMinArguments() {
	    return 1;
	}
    }
}
