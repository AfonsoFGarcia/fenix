package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PhdGratuityEvent extends PhdGratuityEvent_Base {
    public PhdGratuityEvent(PhdIndividualProgramProcess process, int year, DateTime phdGratuityDate) {
	super();
	if (process.hasPhdGratuityEventForYear(year)) {
	    throw new DomainException("error.PhdRegistrationFee.process.already.has.registration.fee.for.this.year");
	}
	init(EventType.PHD_GRATUITY, process.getPerson(), year, process, phdGratuityDate);
    }

    protected void init(EventType eventType, Person person, int year, PhdIndividualProgramProcess process, DateTime phdGratuityDate) {
	super.init(eventType, person);
	if (year < 2006){
	    throw new DomainException("invalid.year");
	}
	
	if (process == null){
	    throw new DomainException("proces.may.not.be.null");
	}
	
	if (phdGratuityDate == null){
	    throw new DomainException("phdGratuityDate.may.not.be.null");
	}

	setYear(year);
	setPhdIndividualProgramProcess(process);
	setPhdGratuityDate(phdGratuityDate);
    }

    @Override
    protected PhdProgram getPhdProgram() {
	return getPhdIndividualProgramProcess().getPhdProgram();
    }

    @Service
    static public PhdGratuityEvent create(PhdIndividualProgramProcess phdIndividualProgramProcess, int year,
	    DateTime phdGratuityDate) {
	return new PhdGratuityEvent(phdIndividualProgramProcess, year, phdGratuityDate);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(final EntryType entryType) {
	return new LabelFormatter().appendLabel(entryType.name(), "enum").appendLabel(" - ").appendLabel("" + getYear())
		.appendLabel(" (").appendLabel(getPhdProgram().getName().getContent()).appendLabel(")");
    }

    @Override
    public boolean isExemptionAppliable() {
	return true;
    }

    public DateTime getLimitDateToPay() {
	LocalDate whenFormalizedRegistration = getPhdIndividualProgramProcess().getWhenFormalizedRegistration();

	PhdGratuityPaymentPeriod phdGratuityPeriod = ((PhdGratuityPR) getPostingRule())
		.getPhdGratuityPeriod(whenFormalizedRegistration);

	return new LocalDate(getYear(), phdGratuityPeriod.getLastPayment().get(DateTimeFieldType.monthOfYear()), phdGratuityPeriod.getLastPayment().get(DateTimeFieldType.dayOfMonth()))
		.toDateMidnight().toDateTime();
    }

}
