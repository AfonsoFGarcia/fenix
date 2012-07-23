package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import java.util.List;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.AcademicEventExemption;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.EnrolmentCertificateRequest;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class EnrolmentCertificateRequestPR extends EnrolmentCertificateRequestPR_Base {

    protected EnrolmentCertificateRequestPR() {
	super();
    }

    public EnrolmentCertificateRequestPR(final DateTime startDate, final DateTime endDate,
	    final ServiceAgreementTemplate serviceAgreementTemplate, final Money baseAmount, final Money amountPerUnit,
	    final Money amountPerPage, final Money maximumAmount) {
	this();
	init(EntryType.ENROLMENT_CERTIFICATE_REQUEST_FEE, EventType.ENROLMENT_CERTIFICATE_REQUEST, startDate, endDate,
		serviceAgreementTemplate, baseAmount, amountPerUnit, amountPerPage, maximumAmount);
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	final CertificateRequestEvent requestEvent = (CertificateRequestEvent) event;
	Money totalAmountToPay = calculateAmountToPayWithUnits(requestEvent, true)
		.add(calculateAmountToPayForPages(requestEvent));

	if (event.hasAnyExemptions()) {
	    List<Exemption> exemptions = event.getExemptions();

	    for (Exemption exemption : exemptions) {
		AcademicEventExemption academicEventExemption = (AcademicEventExemption) exemption;

		totalAmountToPay = totalAmountToPay.subtract(academicEventExemption.getValue());
	    }
	}

	if (totalAmountToPay.isNegative()) {
	    return Money.ZERO;
	}

	return totalAmountToPay;
    }

    private Money calculateAmountToPayWithUnits(final CertificateRequestEvent requestEvent, final boolean checkUrgency) {
	Money total = checkUrgency && isUrgent(requestEvent) ? getBaseAmount().multiply(2) : getBaseAmount();

	final EnrolmentCertificateRequest request = (EnrolmentCertificateRequest) requestEvent.getAcademicServiceRequest();
	if (request.getDetailed() != null && request.getDetailed().booleanValue()) {
	    total = total.add(getAmountForUnits(requestEvent));
	}
	return total;
    }

    @Checked("PostingRulePredicates.editPredicate")
    public EnrolmentCertificateRequestPR edit(final Money baseAmount, final Money amountPerUnit, final Money amountPerPage,
	    final Money maximumAmount) {
	deactivate();
	return new EnrolmentCertificateRequestPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), baseAmount,
		amountPerUnit, amountPerPage, maximumAmount);
    }
}
