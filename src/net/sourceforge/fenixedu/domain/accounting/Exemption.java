package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public abstract class Exemption extends Exemption_Base {

    static {
	ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {

	    @Override
	    public void beforeAdd(Exemption exemption, Event event) {
		if (event != null && !event.isExemptionAppliable()) {
		    throw new DomainException(
			    "error.accounting.Exemption.event.does.not.support.exemption");
		}
	    }
	});

    }

    protected Exemption() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(getClass().getName());
	super.setWhenCreated(new DateTime());
    }

    protected void init(final Employee employee, final Event event) {
	checkParameters(employee, event);

	super.setEmployee(employee);
	super.setEvent(event);
    }

    private void checkParameters(Employee employee, Event event) {
	if (employee == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.GratuityExemption.field_name.cannot.be.null");
	}

	if (event == null) {
	    throw new DomainException("error.accounting.Exemption.event.cannot.be.null");
	}
    }

    @Override
    public void setEvent(Event event) {
	throw new DomainException("error.domain.accounting.Exemption.cannot.modify.event");
    }

    @Override
    public void setEmployee(Employee employee) {
	throw new DomainException("error.accounting.Exemption.cannot.modify.employee");
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
	throw new DomainException("error.accounting.Exemption.cannot.modify.whenCreated");
    }

    public void delete() {
	removeRootDomainObject();
	removeEmployee();
	removeEvent();

	super.deleteDomainObject();
    }

    @Override
    public void removeEmployee() {
	super.setEmployee(null);
    }

    @Override
    public void removeEvent() {
	super.setEvent(null);
    }

}
