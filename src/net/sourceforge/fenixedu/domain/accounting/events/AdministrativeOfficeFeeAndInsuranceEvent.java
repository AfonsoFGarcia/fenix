package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeState;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.postingRules.AdministrativeOfficeFeeAndInsurancePR;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import dml.runtime.RelationAdapter;

public class AdministrativeOfficeFeeAndInsuranceEvent extends AdministrativeOfficeFeeAndInsuranceEvent_Base {

    static {
	PersonAccountingEvent.addListener(new RelationAdapter<Event, Person>() {
	    @Override
	    public void beforeAdd(Event event, Person person) {
		if (event instanceof AdministrativeOfficeFeeAndInsuranceEvent && person != null) {
		    final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent = (AdministrativeOfficeFeeAndInsuranceEvent) event;
		    if (person.hasAdministrativeOfficeFeeInsuranceEventFor(administrativeOfficeFeeAndInsuranceEvent
			    .getExecutionYear())) {
			throw new DomainException(
				"error.net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent.event.is.already.defined.for.execution.year");

		    }

		}
	    }
	});
    }

    @Checked("RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    protected AdministrativeOfficeFeeAndInsuranceEvent() {
	super();
    }

    public AdministrativeOfficeFeeAndInsuranceEvent(AdministrativeOffice administrativeOffice, Person person,
	    ExecutionYear executionYear) {
	this();
	init(administrativeOffice, EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE, person, executionYear);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" - ").appendLabel(getExecutionYear().getYear());

	return labelFormatter;
    }

    @Override
    protected AdministrativeOfficeServiceAgreementTemplate getServiceAgreementTemplate() {
	return getAdministrativeOffice().getServiceAgreementTemplate();
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getExternalAccount();
    }

    @Override
    public Account getToAccount() {
	return getAdministrativeOffice().getUnit().getInternalAccount();
    }

    public boolean hasToPayInsurance() {
	if (getPerson().hasInsuranceEventFor(getExecutionYear())) {
	    return false;
	}
	return getInsurancePayedAmount().lessThan(getInsuranceAmount());
    }

    public boolean hasToPayAdministrativeOfficeFee() {
	return getAdministrativeOfficeFeePayedAmount().lessThan(getAdministrativeOfficeFeeAmount());
    }

    private AdministrativeOfficeFeeAndInsurancePR getAdministrativeOfficeFeeAndInsurancePR() {
	return (AdministrativeOfficeFeeAndInsurancePR) getPostingRule();
    }

    public Money getAdministrativeOfficeFeeAmount() {
	return getAdministrativeOfficeFeeAndInsurancePR().getAdministrativeOfficeFeeAmount(getStartDate(), getEndDate());
    }

    public YearMonthDay getAdministrativeOfficeFeePaymentLimitDate() {
	return getPaymentEndDate() != null ? getPaymentEndDate() : getAdministrativeOfficeFeeAndInsurancePR()
		.getAdministrativeOfficeFeePaymentLimitDate(getStartDate(), getEndDate());
    }

    public Money getAdministrativeOfficeFeePenaltyAmount() {
	return getAdministrativeOfficeFeeAndInsurancePR().getAdministrativeOfficeFeePenaltyAmount(getStartDate(), getEndDate());
    }

    public Money getInsuranceAmount() {
	return getAdministrativeOfficeFeeAndInsurancePR().getInsuranceAmount(getStartDate(), getEndDate());
    }

    @Override
    protected List<AccountingEventPaymentCode> createPaymentCodes() {
	final Money totalAmount = calculateTotalAmount();
	return Collections.singletonList(AccountingEventPaymentCode.create(
		PaymentCodeType.ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE, new YearMonthDay(), calculatePaymentCodeEndDate(), this,
		totalAmount, totalAmount, getPerson().getStudent()));
    }

    @Override
    protected List<AccountingEventPaymentCode> updatePaymentCodes() {
	final Money totalAmount = calculateTotalAmount();
	final AccountingEventPaymentCode nonProcessedPaymentCode = getNonProcessedPaymentCode();

	if (nonProcessedPaymentCode != null) {
	    nonProcessedPaymentCode.update(new YearMonthDay(), calculatePaymentCodeEndDate(), totalAmount, totalAmount);
	} else {
	    final AccountingEventPaymentCode paymentCode = getCancelledPaymentCode();
	    if (paymentCode != null) {
		paymentCode.update(new YearMonthDay(), calculatePaymentCodeEndDate(), totalAmount, totalAmount);
		paymentCode.setState(PaymentCodeState.NEW);
	    }
	}

	return getNonProcessedPaymentCodes();
    }

    private AccountingEventPaymentCode getCancelledPaymentCode() {
	return (getCancelledPaymentCodes().isEmpty() ? null : getCancelledPaymentCodes().get(0));
    }

    private AccountingEventPaymentCode getNonProcessedPaymentCode() {
	return (getNonProcessedPaymentCodes().isEmpty() ? null : getNonProcessedPaymentCodes().get(0));
    }

    private YearMonthDay calculatePaymentCodeEndDate() {
	final YearMonthDay today = new YearMonthDay();
	final YearMonthDay administrativeOfficeFeePaymentLimitDate = getAdministrativeOfficeFeePaymentLimitDate();
	return today.isBefore(administrativeOfficeFeePaymentLimitDate) ? administrativeOfficeFeePaymentLimitDate
		: calculateNextEndDate(today);
    }

    private Money calculateTotalAmount() {
	Money totalAmount = Money.ZERO;
	for (final EntryDTO entryDTO : calculateEntries()) {
	    totalAmount = totalAmount.add(entryDTO.getAmountToPay());
	}
	return totalAmount;
    }

    public AccountingEventPaymentCode calculatePaymentCode() {
	return calculatePaymentCodes().iterator().next();
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode, Money amountToPay,
	    SibsTransactionDetailDTO transactionDetail) {
	return internalProcess(responsibleUser, buildEntryDTOsFrom(amountToPay), transactionDetail);
    }

    @Override
    public boolean isInDebt() {
	return isOpen() && (getPaymentEndDate() == null || getPaymentEndDate().isBefore(new YearMonthDay()));
    }

    private List<EntryDTO> buildEntryDTOsFrom(final Money amountToPay) {
	final List<EntryDTO> result = new ArrayList<EntryDTO>(2);
	Money insuranceAmountToDiscount = Money.ZERO;
	if (hasToPayInsurance()) {
	    insuranceAmountToDiscount = getInsuranceAmount();
	    result.add(buildInsuranceEntryDTO(insuranceAmountToDiscount));
	}

	final Money remainingAmount = amountToPay.subtract(insuranceAmountToDiscount);
	if (remainingAmount.isPositive()) {
	    result.add(buildAdministrativeOfficeFeeEntryDTO(remainingAmount));
	}

	return result;
    }

    private EntryDTO buildAdministrativeOfficeFeeEntryDTO(Money administrativeOfficeFeeAmountToDiscount) {
	return new EntryDTO(EntryType.ADMINISTRATIVE_OFFICE_FEE, this, administrativeOfficeFeeAmountToDiscount);
    }

    private EntryDTO buildInsuranceEntryDTO(Money insuranceAmountToDiscount) {
	return new EntryDTO(EntryType.INSURANCE_FEE, this, insuranceAmountToDiscount);
    }

    public void changePaymentCodeState(DateTime whenRegistered, PaymentMode paymentMode) {
	if (canCloseEvent(whenRegistered) && hasNonProcessedPaymentCode()) {
	    getNonProcessedPaymentCode().setState(getPaymentCodeStateFor(paymentMode));
	}
    }

    private boolean hasNonProcessedPaymentCode() {
	return (getNonProcessedPaymentCode() != null);
    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter labelFormatter = super.getDescription();
	labelFormatter.appendLabel(" ").appendLabel(getExecutionYear().getYear());
	return labelFormatter;
    }

    @Override
    public boolean isExemptionAppliable() {
	return true;
    }

    public boolean hasAdministrativeOfficeFeeAndInsuranceExemption() {
	return getAdministrativeOfficeFeeAndInsurancePenaltyExemption() != null;
    }

    public AdministrativeOfficeFeeAndInsurancePenaltyExemption getAdministrativeOfficeFeeAndInsurancePenaltyExemption() {
	for (final Exemption exemption : getExemptionsSet()) {
	    if (exemption instanceof AdministrativeOfficeFeeAndInsurancePenaltyExemption) {
		return (AdministrativeOfficeFeeAndInsurancePenaltyExemption) exemption;
	    }
	}

	return null;
    }

    @Override
    public void setPaymentEndDate(YearMonthDay paymentEndDate) {
	if (!isOpen()) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent.payment.end.date.can.only.be.modified.on.open.events");
	}

	super.setPaymentEndDate(paymentEndDate);
    }

    public Money getInsurancePayedAmount() {
	Money result = Money.ZERO;

	for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
	    if (transaction.getToAccountEntry().getEntryType() == EntryType.INSURANCE_FEE) {
		result = result.add(transaction.getToAccountEntry().getAmountWithAdjustment());
	    }
	}

	return result;

    }

    public Money getInsurancePayedAmountFor(int civilYear) {
	Money result = Money.ZERO;

	for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
	    if (transaction.getToAccountEntry().getEntryType() == EntryType.INSURANCE_FEE && transaction.isPayed(civilYear)) {
		result = result.add(transaction.getToAccountEntry().getAmountWithAdjustment());
	    }
	}

	return result;
    }

    public Money getAdministrativeOfficeFeePayedAmount() {
	Money result = Money.ZERO;

	for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
	    if (transaction.getToAccountEntry().getEntryType() == EntryType.ADMINISTRATIVE_OFFICE_FEE) {
		result = result.add(transaction.getToAccountEntry().getAmountWithAdjustment());
	    }
	}

	return result;
    }

    public Money getAdministrativeOfficeFeePayedAmountFor(int civilYear) {
	Money result = Money.ZERO;

	for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
	    if (transaction.getToAccountEntry().getEntryType() == EntryType.ADMINISTRATIVE_OFFICE_FEE
		    && transaction.isPayed(civilYear)) {
		result = result.add(transaction.getToAccountEntry().getAmountWithAdjustment());
	    }
	}

	return result;
    }

    @Override
    public Money calculateAmountToPay(DateTime whenRegistered) {
	Money result = super.calculateAmountToPay(whenRegistered);
	if (result.isZero()) {
	    return result;
	}

	result = result.subtract(getPerson().hasInsuranceEventFor(getExecutionYear()) ? getInsuranceAmount() : Money.ZERO);

	return result.isPositive() ? result : Money.ZERO;
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
	final Set<EntryType> result = new HashSet<EntryType>();
	result.add(EntryType.ADMINISTRATIVE_OFFICE_FEE);
	result.add(EntryType.INSURANCE_FEE);

	return result;
    }
}
