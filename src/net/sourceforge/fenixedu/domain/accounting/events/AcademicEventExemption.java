package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.AcademicEvent;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicEventJustificationType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicEventExemptionJustification;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;
import dml.runtime.RelationAdapter;

public class AcademicEventExemption extends AcademicEventExemption_Base {

    static {
	ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {
	    @Override
	    public void beforeAdd(Exemption exemption, Event event) {
		if (exemption != null && event != null) {
		    if (exemption instanceof AcademicEventExemption) {
			final AcademicEvent academicEvent = (AcademicEvent) event;
			if (academicEvent.hasAcademicEventExemption()) {
			    throw new DomainException(
				    "error.accounting.events.AcademicEventExemption.event.already.has.exemption");
			}
		    }
		}
	    }
	});
    }

    private AcademicEventExemption() {
	super();
    }

    public AcademicEventExemption(final Employee employee, final AcademicEvent event, final Money value,
	    final AcademicEventJustificationType justificationType, final LocalDate dispatchDate, final String reason) {

	this();
	super.init(employee, event, createJustification(justificationType, dispatchDate, reason));

	check(value, "error.AcademicEventExemption.invalid.amount");
	setValue(value);

	event.recalculateState(new DateTime());
    }

    private ExemptionJustification createJustification(AcademicEventJustificationType justificationType,
	    final LocalDate dispatchDate, String reason) {
	return new AcademicEventExemptionJustification(this, justificationType, dispatchDate, reason);
    }

    @Override
    public void delete() {
	checkRulesToDelete();
	super.delete();
    }

    private void checkRulesToDelete() {
	if (getEvent().hasAnyPayments()) {
	    throw new DomainException(
		    "error.accounting.events.candidacy.AcademicEventExemption.cannot.delete.event.has.payments");
	}
    }

    public LocalDate getDispatchDate() {
	return ((AcademicEventExemptionJustification) getExemptionJustification()).getDispatchDate();
    }

    @Service
    static public AcademicEventExemption create(final Employee employee, final AcademicEvent event,
	    final Money value, final AcademicEventJustificationType justificationType, final LocalDate dispatchDate,
	    final String reason) {
	return new AcademicEventExemption(employee, event, value, justificationType, dispatchDate, reason);
    }

}
