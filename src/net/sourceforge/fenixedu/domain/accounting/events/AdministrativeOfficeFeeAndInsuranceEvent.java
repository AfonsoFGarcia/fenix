package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.postingRules.AdministrativeOfficeFeeAndInsurancePR;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class AdministrativeOfficeFeeAndInsuranceEvent extends
	AdministrativeOfficeFeeAndInsuranceEvent_Base {

    protected AdministrativeOfficeFeeAndInsuranceEvent() {
	super();
    }

    public AdministrativeOfficeFeeAndInsuranceEvent(AdministrativeOffice administrativeOffice,
	    Person person, ExecutionYear executionYear) {
	this();
	init(administrativeOffice, EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE, person, executionYear);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" - ").appendLabel(
		getExecutionYear().getYear());

	return labelFormatter;
    }

    @Override
    protected PostingRule getPostingRule(DateTime whenRegistered) {
	return getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(),
		whenRegistered);
    }

    private AdministrativeOfficeServiceAgreementTemplate getServiceAgreementTemplate() {
	return getAdministrativeOffice().getServiceAgreementTemplate();
    }

    @Override
    public Account getToAccount() {
	return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);

    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    public boolean needsToPayInsurance() {
	for (final AccountingTransaction accountingTransaction : getAccountingTransactionsSet()) {
	    if (accountingTransaction.getToAccountEntry().getEntryType() == EntryType.INSURANCE_FEE) {
		return false;
	    }
	}

	return !getPerson().hasInsuranceEventFor(getExecutionYear());
    }

    public boolean hasPayedAdministrativeOfficeFee() {
	for (final AccountingTransaction accountingTransaction : getAccountingTransactionsSet()) {
	    if (accountingTransaction.getToAccountEntry().getEntryType() == EntryType.ADMINISTRATIVE_OFFICE_FEE) {
		return true;
	    }
	}

	return false;
    }

    private AdministrativeOfficeFeeAndInsurancePR getAdministrativeOfficeFeeAndInsurancePR() {
	return (AdministrativeOfficeFeeAndInsurancePR) getPostingRule(new DateTime());
    }

    public Money getAdministrativeOfficeFeeAmount() {
	return getAdministrativeOfficeFeeAndInsurancePR().getAdministrativeOfficeFeeAmount();
    }

    public YearMonthDay getAdministrativeOfficeFeePaymentLimitDate() {
	return getAdministrativeOfficeFeeAndInsurancePR().getAdministrativeOfficeFeePaymentLimitDate();
    }

    public Money getAdministrativeOfficeFeePenaltyAmount() {
	return getAdministrativeOfficeFeeAndInsurancePR().getAdministrativeOfficeFeePenaltyAmount();
    }

    public Money getInsuranceAmount() {
	return getAdministrativeOfficeFeeAndInsurancePR().getInsuranceAmount();
    }

    @Override
    protected List<AccountingEventPaymentCode> createPaymentCodes() {
	final Money totalAmount = calculateTotalAmount();
	return Collections.singletonList(AccountingEventPaymentCode.create(
		PaymentCodeType.ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE, new YearMonthDay(),
		getAdministrativeOfficeFeePaymentLimitDate(), this, totalAmount, totalAmount,
		getPerson().getStudent()));
    }

    @Override
    protected List<AccountingEventPaymentCode> updatePaymentCodes() {
	final Money totalAmount = calculateTotalAmount();
	getNonProcessedPaymentCode().update(new YearMonthDay(), calculatePaymentCodeEndDate(),
		totalAmount, totalAmount);

	return getNonProcessedPaymentCodes();
    }

    private AccountingEventPaymentCode getNonProcessedPaymentCode() {
	return getNonProcessedPaymentCodes().iterator().next();
    }

    private YearMonthDay calculatePaymentCodeEndDate() {
	final YearMonthDay today = new YearMonthDay();
	final YearMonthDay administrativeOfficeFeePaymentLimitDate = getAdministrativeOfficeFeePaymentLimitDate();
	return today.isBefore(administrativeOfficeFeePaymentLimitDate) ? administrativeOfficeFeePaymentLimitDate
		: today.plusMonths(1);
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
    protected void beforeCloseEvent() {
	getNonProcessedPaymentCode().cancel();
    }

}
