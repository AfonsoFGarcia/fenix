package net.sourceforge.fenixedu.domain.phd.debts;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.ValueGratuityExemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class PhdGratuityPR extends PhdGratuityPR_Base {
    public static double STANDARD_FINE_RATE = 0.01;
    public static Money STANDARD_GRATUITY = new Money("3000");


    public PhdGratuityPR(DateTime start, DateTime end, ServiceAgreementTemplate serviceAgreementTemplate){
	this(start, end, serviceAgreementTemplate, STANDARD_GRATUITY, STANDARD_FINE_RATE);
    }
    
    public PhdGratuityPR(DateTime start, DateTime end, ServiceAgreementTemplate serviceAgreementTemplate, Money gratuity,
	    double fineRate) {
	super();
	super.init(EventType.PHD_GRATUITY, start, end, serviceAgreementTemplate);
	setGratuity(gratuity);
	setFineRate(fineRate);
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	PhdGratuityEvent phdGratuityEvent = (PhdGratuityEvent) event;
	LocalDate programStartDate = phdGratuityEvent.getPhdIndividualProgramProcess().getWhenFormalizedRegistration();
	
	Money gratuity = getGratuity();
	BigDecimal percentage  = new BigDecimal(0);
	for (Exemption exemption : event.getExemptions()){
	    if (exemption.isGratuityExemption()){
		percentage = percentage.add(((GratuityExemption) exemption).calculateDiscountPercentage(gratuity));
	    }
	}
	
	gratuity = gratuity.subtract(gratuity.multiply(percentage));
	if(gratuity.lessOrEqualThan(Money.ZERO)) {
	    return Money.ZERO;
	}
	
	if(phdGratuityEvent.getLimitDateToPay().isAfter(when)) {
	    return gratuity;
	}
	
	Money payedAmount = phdGratuityEvent.getPayedAmount(phdGratuityEvent.getLimitDateToPay());
	
	if(payedAmount.greaterOrEqualThan(gratuity)) {
	    return gratuity;
	}
	
	DateTime lastPaymentDate = phdGratuityEvent.getLastPaymentDate();
	payedAmount = phdGratuityEvent.getPayedAmount(lastPaymentDate);
	
	
	Money gratuityWithFine = gratuity.add(getFine(programStartDate, lastPaymentDate));
	
	if(payedAmount.greaterOrEqualThan(gratuityWithFine)) {
	    return gratuityWithFine;
	}
	
	return gratuity.add(getFine(programStartDate, when));
    }

    public PhdGratuityPaymentPeriod getPhdGratuityPeriod(LocalDate programStartDate) {
	for (PhdGratuityPaymentPeriod period : getPhdGratuityPaymentPeriods()) {
	    if (period.contains(programStartDate)) {
		return period;
	    }
	}
	
	throw new DomainException("error.phd.debts.PhdGratuityPR.cannot.find.period");
    }
    
    private Money getFine(LocalDate programStartDate, DateTime when) {
	PhdGratuityPaymentPeriod phdGratuityPeriod = getPhdGratuityPeriod(programStartDate);
	
	return phdGratuityPeriod.fine(getFineRate(), getGratuity(), when);
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
	return Collections.singletonList(new EntryDTO(EntryType.PHD_GRATUITY_FEE, event, calculateTotalAmountToPay(event, when),
		event.getPayedAmount(), event.calculateAmountToPay(when), event
			.getDescriptionForEntryType(EntryType.PHD_GRATUITY_FEE), event.calculateAmountToPay(when)));
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs, Event event, Account fromAccount,
	    Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

	if (entryDTOs.size() != 1) {
	    throw new DomainException("error.accounting.postingRules.gratuity.PhdGratuityPR.invalid.number.of.entryDTOs");
	}

	return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, EntryType.PHD_GRATUITY_FEE,
		entryDTOs.get(0).getAmountToPay(), transactionDetail));
    }
    
    @Override
    public void removeOtherRelations(){
	for (PhdGratuityPaymentPeriod period : getPhdGratuityPaymentPeriods()){
	    period.delete();
	}
	getPhdGratuityPaymentPeriods().clear();
    }
}
