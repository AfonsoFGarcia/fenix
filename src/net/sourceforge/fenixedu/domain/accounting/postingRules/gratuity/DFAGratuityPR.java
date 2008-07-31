package net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class DFAGratuityPR extends DFAGratuityPR_Base {

    public static class DFAGratuityPREditor implements FactoryExecutor, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5454487291500203873L;

	private DateTime beginDate;

	private Money dfaTotalAmount;

	private Money dfaAmountPerEctsCredit;

	private BigDecimal dfaPartialAcceptedPercentage;

	private DomainReference<DFAGratuityPR> dfaGratuityPR;

	private DFAGratuityPREditor() {
	}

	public DateTime getBeginDate() {
	    return beginDate;
	}

	public void setBeginDate(DateTime beginDate) {
	    this.beginDate = beginDate;
	}

	public Money getDfaTotalAmount() {
	    return dfaTotalAmount;
	}

	public void setDfaTotalAmount(Money dfaTotalAmount) {
	    this.dfaTotalAmount = dfaTotalAmount;
	}

	public Money getDfaAmountPerEctsCredit() {
	    return dfaAmountPerEctsCredit;
	}

	public void setDfaAmountPerEctsCredit(Money dfaAmountPerEctsCredit) {
	    this.dfaAmountPerEctsCredit = dfaAmountPerEctsCredit;
	}

	public BigDecimal getDfaPartialAcceptedPercentage() {
	    return dfaPartialAcceptedPercentage;
	}

	public void setDfaPartialAcceptedPercentage(BigDecimal dfaPartialAcceptedPercentage) {
	    this.dfaPartialAcceptedPercentage = dfaPartialAcceptedPercentage;
	}

	public DFAGratuityPR getDfaGratuityPR() {
	    return (this.dfaGratuityPR != null) ? this.dfaGratuityPR.getObject() : null;
	}

	public void setDfaGratuityPR(DFAGratuityPR dfaGratuityPR) {
	    this.dfaGratuityPR = (dfaGratuityPR != null) ? new DomainReference<DFAGratuityPR>(dfaGratuityPR) : null;
	}

	@Override
	public Object execute() {
	    return getDfaGratuityPR().edit(getBeginDate(), getDfaTotalAmount(), getDfaAmountPerEctsCredit(),
		    getDfaPartialAcceptedPercentage());

	}

	public static DFAGratuityPREditor buildFrom(final DFAGratuityPR dfaGratuityPR) {
	    final DFAGratuityPREditor result = new DFAGratuityPREditor();
	    result.setDfaGratuityPR(dfaGratuityPR);
	    result.setDfaAmountPerEctsCredit(dfaGratuityPR.getDfaAmountPerEctsCredit());
	    result.setDfaPartialAcceptedPercentage(dfaGratuityPR.getDfaPartialAcceptedPercentage());
	    result.setDfaTotalAmount(dfaGratuityPR.getDfaTotalAmount());

	    return result;
	}

    }

    protected DFAGratuityPR() {
	super();
    }

    public DFAGratuityPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
	    Money dfaTotalAmount, BigDecimal partialAcceptedPercentage, Money dfaAmountPerEctsCredit) {
	super();
	init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
		partialAcceptedPercentage, dfaAmountPerEctsCredit);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money dfaTotalAmount, BigDecimal dfaPartialAcceptedPercentage,
	    Money dfaAmountPerEctsCredit) {

	super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate);

	checkParameters(dfaTotalAmount, dfaPartialAcceptedPercentage, dfaAmountPerEctsCredit);

	super.setDfaTotalAmount(dfaTotalAmount);
	super.setDfaPartialAcceptedPercentage(dfaPartialAcceptedPercentage);
	super.setDfaAmountPerEctsCredit(dfaAmountPerEctsCredit);

    }

    private void checkParameters(Money dfaTotalAmount, BigDecimal dfaPartialAcceptedPercentage, Money dfaAmountPerEctsCredit) {
	if (dfaTotalAmount == null) {
	    throw new DomainException("error.accounting.postingRules.gratuity.DFAGratuityPR.dfaTotalAmount.cannot.be.null");
	}

	if (dfaPartialAcceptedPercentage == null) {
	    throw new DomainException(
		    "error.accounting.postingRules.gratuity.DFAGratuityPR.dfaPartialAcceptedPercentage.cannot.be.null");
	}

	if (dfaAmountPerEctsCredit == null) {
	    throw new DomainException(
		    "error.accounting.postingRules.gratuity.DFAGratuityPR.dfaAmountPerEctsCredit.cannot.be.null");
	}

    }

    @Override
    public void setDfaTotalAmount(Money dfaTotalAmount) {
	throw new DomainException("error.accounting.postingRules.gratuity.DFAGratuityPR.cannot.modify.dfaTotalAmount");
    }

    @Override
    public void setDfaPartialAcceptedPercentage(BigDecimal dfaPartialAcceptedPercentage) {
	throw new DomainException(
		"error.accounting.postingRules.gratuity.DFAGratuityPR.cannot.modify.dfaPartialAcceptedPercentage");
    }

    @Override
    public void setDfaAmountPerEctsCredit(Money dfaAmountPerEctsCredit) {
	throw new DomainException("error.accounting.postingRules.gratuity.DFAGratuityPR.cannot.modify.dfaAmountPerEctsCredit");
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs, Event event, Account fromAccount,
	    Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

	if (entryDTOs.size() != 1) {
	    throw new DomainException("error.accounting.postingRules.gratuity.DFAGratuityPR.invalid.number.of.entryDTOs");
	}

	checkIfCanAddAmount(entryDTOs.get(0).getAmountToPay(), event, transactionDetail.getWhenRegistered());

	return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, getEntryType(), entryDTOs
		.get(0).getAmountToPay(), transactionDetail));
    }

    private void checkIfCanAddAmount(Money amountToAdd, Event event, DateTime when) {
	if (((GratuityEvent) event).isCustomEnrolmentModel()) {
	    checkIfCanAddAmountForCustomEnrolmentModel(event, when, amountToAdd);
	} else {
	    checkIfCanAddAmountForCompleteEnrolmentModel(amountToAdd, event, when);
	}
    }

    private void checkIfCanAddAmountForCustomEnrolmentModel(Event event, DateTime when, Money amountToAdd) {
	if (event.calculateAmountToPay(when).greaterThan(amountToAdd)) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.gratuity.DFAGratuityPR.amount.being.payed.must.be.equal.to.amout.in.debt",
		    event.getDescriptionForEntryType(getEntryType()));
	}
    }

    private void checkIfCanAddAmountForCompleteEnrolmentModel(final Money amountToAdd, final Event event, final DateTime when) {

	if (hasAlreadyPayedAnyAmount(event, when)) {
	    final Money totalFinalAmount = event.getPayedAmount().add(amountToAdd);
	    if (!(totalFinalAmount.greaterOrEqualThan(calculateTotalAmountToPay(event, when)) || totalFinalAmount
		    .equals(getPartialPaymentAmount(event, when)))) {
		throw new DomainExceptionWithLabelFormatter(
			"error.accounting.postingRules.gratuity.DFAGratuityPR.amount.being.payed.must.be.equal.to.amout.in.debt",
			event.getDescriptionForEntryType(getEntryType()));
	    }
	} else {
	    if (!isPayingTotalAmount(event, when, amountToAdd) && !isPayingPartialAmount(event, when, amountToAdd)) {
		final LabelFormatter percentageLabelFormatter = new LabelFormatter();
		percentageLabelFormatter.appendLabel(getDfaPartialAcceptedPercentage().multiply(BigDecimal.valueOf(100))
			.toString());

		throw new DomainExceptionWithLabelFormatter(
			"error.accounting.postingRules.gratuity.DFAGratuityPR.invalid.partial.payment.value", event
				.getDescriptionForEntryType(getEntryType()), percentageLabelFormatter);
	    }
	}
    }

    private boolean isPayingTotalAmount(final Event event, final DateTime when, Money amountToAdd) {
	return amountToAdd.greaterOrEqualThan(event.calculateAmountToPay(when));
    }

    private boolean isPayingPartialAmount(final Event event, final DateTime when, final Money amountToAdd) {
	return amountToAdd.equals(getPartialPaymentAmount(event, when));
    }

    private boolean hasAlreadyPayedAnyAmount(final Event event, final DateTime when) {
	return !calculateTotalAmountToPay(event, when).equals(event.calculateAmountToPay(when));
    }

    private Money getPartialPaymentAmount(final Event event, final DateTime when) {
	return calculateTotalAmountToPay(event, when).multiply(getDfaPartialAcceptedPercentage());
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
	return Collections.singletonList(new EntryDTO(getEntryType(), event, calculateTotalAmountToPay(event, when), event
		.getPayedAmount(), event.calculateAmountToPay(when), event.getDescriptionForEntryType(getEntryType()), event
		.calculateAmountToPay(when)));
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	final Money result;
	if (((GratuityEvent) event).isCustomEnrolmentModel()) {
	    final double enrolmentsEctsForRegistration = ((GratuityEvent) event).getEnrolmentsEctsForRegistration();
	    result = getDfaAmountPerEctsCredit().multiply(new BigDecimal(enrolmentsEctsForRegistration));
	} else {
	    result = getDfaTotalAmount();
	}

	final BigDecimal discountPercentage = applyDiscount ? getDiscountPercentage(event, result) : BigDecimal.ZERO;

	return result.multiply(BigDecimal.ONE.subtract(discountPercentage));
    }

    private BigDecimal getDiscountPercentage(final Event event, final Money amount) {
	return ((DfaGratuityEvent) event).calculateDiscountPercentage(amount);
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public DFAGratuityPR edit(Money dfaTotalAmount, Money dfaAmountPerEctsCredit, BigDecimal partialAcceptedPercentage) {
	return edit(new DateTime(), dfaTotalAmount, dfaAmountPerEctsCredit, partialAcceptedPercentage);
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public DFAGratuityPR edit(DateTime startDate, Money dfaTotalAmount, Money dfaAmountPerEctsCredit,
	    BigDecimal partialAcceptedPercentage) {

	deactivate(startDate);

	return new DFAGratuityPR(startDate, null, getServiceAgreementTemplate(), dfaTotalAmount, partialAcceptedPercentage,
		dfaAmountPerEctsCredit);

    }

}
