package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestEvent;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class DiplomaRequestPR extends DiplomaRequestPR_Base {

    protected DiplomaRequestPR() {
	super();
    }

    public DiplomaRequestPR(final EntryType entryType, final EventType eventType, final DateTime startDate,
	    final DateTime endDate, final ServiceAgreementTemplate serviceAgreementTemplate, final Money fixedAmount) {
	super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, fixedAmount);
    }

    @Override
    @Checked("PostingRulePredicates.editPredicate")
    final public DiplomaRequestPR edit(final Money fixedAmount) {
	deactivate();
	return new DiplomaRequestPR(getEntryType(), getEventType(), new DateTime().minus(1000), null,
		getServiceAgreementTemplate(), fixedAmount);
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when) {
	Money amountToPay = super.calculateTotalAmountToPay(event, when);

	final AcademicServiceRequestEvent requestEvent = (AcademicServiceRequestEvent) event;
	if (requestEvent.hasAcademicEventExemption()) {
	    amountToPay = amountToPay.subtract(requestEvent.getAcademicEventExemption().getValue());
	}

	return amountToPay.isPositive() ? amountToPay : Money.ZERO;
    }
}
