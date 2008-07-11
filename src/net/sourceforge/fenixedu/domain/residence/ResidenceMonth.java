package net.sourceforge.fenixedu.domain.residence;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit;
import net.sourceforge.fenixedu.util.Month;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class ResidenceMonth extends ResidenceMonth_Base {

    protected ResidenceMonth() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public ResidenceMonth(Month month, ResidenceYear residenceYear) {
	this();
	setMonth(month);
	setYear(residenceYear);
    }

    public ResidenceManagementUnit getManagementUnit() {
	return getYear().getUnit();
    }

    public boolean isEventPresent(Person person) {
	for (ResidenceEvent event : getEvents()) {
	    if (event.getPerson() == person) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public void setPaymentLimitDay(Integer paymentLimitDay) {
	if (!isAbleToEditPaymentLimitDate()) {
	    throw new DomainException("label.error.unable.to.change.paymentLimitDay.when.there.are.events");
	}
	super.setPaymentLimitDay(paymentLimitDay);
    }

    public DateTime getPaymentStartDate() {
	LocalDate date = new LocalDate(getYear().getYear(), getMonth().getNumberOfMonth(), 1);
	return date.toDateTimeAtStartOfDay();
    }

    public DateTime getPaymentLimitDateTime() {
	LocalDate date = new LocalDate(getYear().getYear(), getMonth().getNumberOfMonth(), getPaymentLimitDay());
	return date.toDateTimeAtStartOfDay();
    }

    public boolean isAbleToEditPaymentLimitDate() {
	return getEventsCount() == 0;
    }
}
