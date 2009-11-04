package net.sourceforge.fenixedu.domain.phd.candidacy;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class PhdProgramCandidacyPR extends PhdProgramCandidacyPR_Base {

    private PhdProgramCandidacyPR() {
	super();
    }

    public PhdProgramCandidacyPR(final ServiceAgreementTemplate serviceAgreementTemplate, final DateTime startDate,
	    final DateTime endDate, final Money amount) {
	this();
	init(EntryType.CANDIDACY_ENROLMENT_FEE, EventType.CANDIDACY_ENROLMENT, startDate, endDate, serviceAgreementTemplate,
		amount);
    }

    @Checked("PostingRulePredicates.editPredicate")
    public PhdProgramCandidacyPR edit(final Money amount) {
	deactivate();
	return new PhdProgramCandidacyPR(getServiceAgreementTemplate(), new DateTime().minus(1000), null, amount);
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	Money amountToPay = super.calculateTotalAmountToPay(event, when, applyDiscount);

	final PhdProgramCandidacyEvent candidacyEvent = (PhdProgramCandidacyEvent) event;
	if (candidacyEvent.hasPhdEventExemption()) {
	    amountToPay = amountToPay.subtract(candidacyEvent.getPhdEventExemption().getValue());
	}

	return amountToPay.isPositive() ? amountToPay : Money.ZERO;
    }
}
