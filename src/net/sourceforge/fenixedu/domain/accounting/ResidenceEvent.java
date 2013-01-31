package net.sourceforge.fenixedu.domain.accounting;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class ResidenceEvent extends ResidenceEvent_Base {

	protected ResidenceEvent() {
		super();
	}

	public ResidenceEvent(ResidenceMonth month, Person person, Money roomValue, String room) {
		init(EventType.RESIDENCE_PAYMENT, person, month, roomValue, room);
	}

	protected void init(EventType eventType, Person person, ResidenceMonth month, Money roomValue, String room) {
		super.init(eventType, person);
		if (month == null) {
			throw new DomainException("error.accounting.events.ResidenceEvent.ResidenceMonth.cannot.be.null");
		}
		setResidenceMonth(month);
		setRoomValue(roomValue);
		setRoom(room);
	}

	@Override
	public LabelFormatter getDescription() {
		return getDescriptionForEntryType(EntryType.RESIDENCE_FEE);
	}

	@Override
	public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
		final LabelFormatter labelFormatter = new LabelFormatter();

		labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
		labelFormatter.appendLabel(" - ");
		labelFormatter.appendLabel(getResidenceMonth().getMonth().getName(), "enum");
		labelFormatter.appendLabel("-");
		labelFormatter.appendLabel(getResidenceMonth().getYear().getYear().toString());
		return labelFormatter;
	}

	@Override
	protected Account getFromAccount() {
		return getPerson().getAccountBy(AccountType.EXTERNAL);
	}

	@Override
	public PostingRule getPostingRule() {
		return getManagementUnit().getUnitServiceAgreementTemplate().findPostingRuleBy(getEventType(), getWhenOccured(), null);
	}

	@Override
	public Account getToAccount() {
		return getManagementUnit().getAccountBy(AccountType.INTERNAL);
	}

	public ResidenceManagementUnit getManagementUnit() {
		return getResidenceMonth().getManagementUnit();
	}

	public DateTime getPaymentStartDate() {
		return getResidenceMonth().getPaymentStartDate();
	}

	public DateTime getPaymentLimitDate() {
		return getResidenceMonth().getPaymentLimitDateTime();
	}

	@Override
	@Checked("EventsPredicates.MANAGER_OR_RESIDENCE_UNIT_EMPLOYEE")
	public void cancel(Person responsible) {
		super.cancel(responsible);
	}

	public DateTime getPaymentDate() {
		return getNonAdjustingTransactions().isEmpty() ? null : getNonAdjustingTransactions().get(0).getTransactionDetail()
				.getWhenRegistered();
	}

	public PaymentMode getPaymentMode() {
		return getNonAdjustingTransactions().isEmpty() ? null : getNonAdjustingTransactions().get(0).getTransactionDetail()
				.getPaymentMode();
	}

	@Override
	public Money getAmountToPay() {
		return calculateAmountToPay(new DateTime());
	}

	@Override
	protected List<AccountingEventPaymentCode> createPaymentCodes() {
		final EntryDTO entryDTO = calculateEntries(new DateTime()).get(0);

		return Collections.singletonList(AccountingEventPaymentCode.create(PaymentCodeType.RESIDENCE_FEE, new YearMonthDay(),
				getPaymentLimitDate().toYearMonthDay(), this, entryDTO.getAmountToPay(), entryDTO.getAmountToPay(), getPerson()));
	}

	@Override
	protected List<AccountingEventPaymentCode> updatePaymentCodes() {
		final EntryDTO entryDTO = calculateEntries(new DateTime()).get(0);
		getNonProcessedPaymentCodes().get(0).update(new YearMonthDay(), getPaymentLimitDate().toYearMonthDay(),
				entryDTO.getAmountToPay(), entryDTO.getAmountToPay());

		return getNonProcessedPaymentCodes();
	}

	@Override
	protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode, Money amountToPay,
			SibsTransactionDetailDTO transactionDetail) {
		return internalProcess(responsibleUser,
				Collections.singletonList(new EntryDTO(EntryType.RESIDENCE_FEE, this, amountToPay)), transactionDetail);
	}

	public boolean isFor(int year) {
		return getResidenceMonth().isFor(year);
	}

	@Override
	public boolean isResidenceEvent() {
		return true;
	}

	@Override
	public Unit getOwnerUnit() {
		return getManagementUnit();
	}

}
