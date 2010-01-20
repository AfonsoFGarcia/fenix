package net.sourceforge.fenixedu.domain.phd.permissions;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.phd.PhdProcessesManager;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class PhdPermission extends PhdPermission_Base {

    static public final Comparator<PhdPermission> COMPARATOR_BY_TYPE = new Comparator<PhdPermission>() {
	@Override
	public int compare(PhdPermission o1, PhdPermission o2) {
	    return o1.getType().compareTo(o2.getType());
	}
    };

    private PhdPermission() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setWhenCreated(new DateTime());
    }

    public PhdPermission(PhdProcessesManager manager, PhdPermissionType type) {
	this();

	check(manager, "error.PhdPermission.manager.cannot.be.null");
	check(type, "error.PhdPermission.type.cannot.be.null");

	setPhdProcessesManager(manager);
	setType(type);
	setMembers(new FixedSetGroup());
    }

    public void delete() {
	removePhdProcessesManager();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public Set<Person> getElements() {
	return getMembers().getElements();
    }

    public boolean isMember(Person person) {
	return getMembers().isMember(person);
    }

    @Service
    public void saveMembers(final Collection<Person> persons) {
	check(persons, "error.PhdPermission.invalid.persons");
	setMembers(new FixedSetGroup(persons));
    }
}
