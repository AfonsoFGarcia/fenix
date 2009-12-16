package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class EnrolmentCertificateRequestWithCeilingInTotalAmountForUnitsPR extends
	EnrolmentCertificateRequestWithCeilingInTotalAmountForUnitsPR_Base {

    private EnrolmentCertificateRequestWithCeilingInTotalAmountForUnitsPR() {
	super();
    }

    public EnrolmentCertificateRequestWithCeilingInTotalAmountForUnitsPR(DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money baseAmount, Money amountPerUnit, Money amountPerPage,
	    Money maximumAmount) {
	this();

	init(EntryType.ENROLMENT_CERTIFICATE_REQUEST_FEE, EventType.ENROLMENT_CERTIFICATE_REQUEST, startDate, endDate,
		serviceAgreementTemplate, baseAmount, amountPerUnit, amountPerPage, maximumAmount);
    }

    @Override
    public Money getAmountForUnits(Event event) {
	if (getNumberOfUnits(event) <= 1) {
	    return Money.ZERO;
	}

	Money totalAmountOfUnits = getAmountPerUnit().multiply(new BigDecimal(getNumberOfUnits(event) - 1));

	if (this.getMaximumAmount().greaterThan(Money.ZERO)) {
	    if (totalAmountOfUnits.greaterThan(this.getMaximumAmount())) {
		totalAmountOfUnits = this.getMaximumAmount();
	    }
	}

	return totalAmountOfUnits;
    }

    @Checked("PostingRulePredicates.editPredicate")
    public EnrolmentCertificateRequestWithCeilingInTotalAmountForUnitsPR edit(final Money baseAmount, final Money amountPerUnit,
	    final Money amountPerPage, final Money maximumAmount) {
	deactivate();
	return new EnrolmentCertificateRequestWithCeilingInTotalAmountForUnitsPR(new DateTime().minus(1000), null,
		getServiceAgreementTemplate(), baseAmount, amountPerUnit, amountPerPage, maximumAmount);
    }

}
