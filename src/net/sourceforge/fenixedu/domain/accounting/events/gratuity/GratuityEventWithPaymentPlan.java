package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryWithInstallmentDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeState;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.InstallmentPenaltyExemption;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.InstallmentPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.CustomGratuityPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.GratuityPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.GratuityWithPaymentPlanPR;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreements.DegreeCurricularPlanServiceAgreement;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class GratuityEventWithPaymentPlan extends GratuityEventWithPaymentPlan_Base {

    protected GratuityEventWithPaymentPlan() {
	super();
    }

    @Checked("RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    public GratuityEventWithPaymentPlan(AdministrativeOffice administrativeOffice, Person person,
	    StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
	this();
	init(administrativeOffice, person, studentCurricularPlan, executionYear);

    }

    @Override
    protected void init(AdministrativeOffice administrativeOffice, Person person, StudentCurricularPlan studentCurricularPlan,
	    ExecutionYear executionYear) {
	super.init(administrativeOffice, person, studentCurricularPlan, executionYear);

	configuratePaymentPlan();
    }

    @Override
    public void setGratuityPaymentPlan(final PaymentPlan gratuityPaymentPlan) {
	throw new DomainException("error.GratuityEventWithPaymentPlan.do.not.use.this.method");
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void setGratuityPaymentPlan(final GratuityPaymentPlan paymentPlan) {
	if (paymentPlan != null) {
	    ensureServiceAgreement();
	    super.setGratuityPaymentPlan(paymentPlan);
	}
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void setGratuityPaymentPlan(final CustomGratuityPaymentPlan paymentPlan) {
	if (paymentPlan != null) {
	    ensureServiceAgreement();
	    super.setGratuityPaymentPlan(paymentPlan);
	}
    }

    public void configuratePaymentPlan() {
	ensureServiceAgreement();

	if (!hasGratuityPaymentPlan()) {
	    super.setGratuityPaymentPlan(getDegreeCurricularPlanServiceAgreement().getGratuityPaymentPlanFor(
		    getStudentCurricularPlan(), getExecutionYear()));
	}
    }

    private void ensureServiceAgreement() {
	if (getDegreeCurricularPlanServiceAgreement() == null) {
	    new DegreeCurricularPlanServiceAgreement(getPerson(), DegreeCurricularPlanServiceAgreementTemplate
		    .readByDegreeCurricularPlan(getStudentCurricularPlan().getDegreeCurricularPlan()));
	}
    }

    public void configureCustomPaymentPlan() {
	if (!hasCustomGratuityPaymentPlan()) {
	    ensureServiceAgreement();
	    super.setGratuityPaymentPlan(new CustomGratuityPaymentPlan(getExecutionYear(),
		    getDegreeCurricularPlanServiceAgreement()));
	}
    }

    @Override
    protected List<AccountingEventPaymentCode> createPaymentCodes() {
	return createMissingPaymentCodes();
    }

    private List<AccountingEventPaymentCode> createMissingPaymentCodes() {
	final List<AccountingEventPaymentCode> result = new ArrayList<AccountingEventPaymentCode>();
	for (final EntryDTO entryDTO : calculateEntries()) {

	    if (!hasAnyNonProcessedPaymentCodeFor(entryDTO)) {
		if (entryDTO instanceof EntryWithInstallmentDTO) {
		    result.add(createInstallmentPaymentCode((EntryWithInstallmentDTO) entryDTO, getPerson().getStudent()));
		} else {
		    result.add(createAccountingEventPaymentCode(entryDTO, getPerson().getStudent()));
		}
	    }

	}
	return result;
    }

    private boolean hasAnyNonProcessedPaymentCodeFor(final EntryDTO entryDTO) {
	for (final AccountingEventPaymentCode paymentCode : getNonProcessedPaymentCodes()) {
	    if (paymentCode instanceof InstallmentPaymentCode) {
		if (entryDTO instanceof EntryWithInstallmentDTO) {
		    final InstallmentPaymentCode installmentPaymentCode = (InstallmentPaymentCode) paymentCode;

		    if (installmentPaymentCode.getInstallment() == ((EntryWithInstallmentDTO) entryDTO).getInstallment()) {
			return true;
		    }
		}
	    } else {
		if (!(entryDTO instanceof EntryWithInstallmentDTO)) {
		    return true;
		}
	    }
	}

	return false;
    }

    @Override
    protected List<AccountingEventPaymentCode> updatePaymentCodes() {
	createMissingPaymentCodes();

	final List<EntryDTO> entryDTOs = calculateEntries();
	final List<AccountingEventPaymentCode> result = new ArrayList<AccountingEventPaymentCode>();

	for (final AccountingEventPaymentCode paymentCode : getNonProcessedPaymentCodes()) {
	    final EntryDTO entryDTO = findEntryDTOForPaymentCode(entryDTOs, paymentCode);
	    if (entryDTO == null) {
		paymentCode.cancel();
		continue;
	    }

	    if (paymentCode instanceof InstallmentPaymentCode) {
		final InstallmentPaymentCode installmentPaymentCode = (InstallmentPaymentCode) paymentCode;
		paymentCode.update(new YearMonthDay(), calculateInstallmentPaymentCodeEndDate(installmentPaymentCode
			.getInstallment()), entryDTO.getAmountToPay(), entryDTO.getAmountToPay());
		result.add(paymentCode);
	    } else {
		paymentCode.update(new YearMonthDay(), calculateFullPaymentCodeEndDate(), entryDTO.getAmountToPay(), entryDTO
			.getAmountToPay());
		result.add(paymentCode);
	    }

	}

	return result;
    }

    private YearMonthDay calculateInstallmentPaymentCodeEndDate(final Installment installment) {
	final YearMonthDay today = new YearMonthDay();
	final YearMonthDay installmentEndDate = installment.getEndDate();
	return today.isBefore(installmentEndDate) ? installmentEndDate : calculateNextEndDate(today);
    }

    private YearMonthDay calculateFullPaymentCodeEndDate() {
	final YearMonthDay today = new YearMonthDay();
	final YearMonthDay totalEndDate = getFirstInstallment().getEndDate();
	return today.isBefore(totalEndDate) ? totalEndDate : calculateNextEndDate(today);
    }

    private EntryDTO findEntryDTOForPaymentCode(List<EntryDTO> entryDTOs, AccountingEventPaymentCode paymentCode) {

	if (paymentCode instanceof InstallmentPaymentCode) {
	    for (final EntryDTO entryDTO : entryDTOs) {
		if (entryDTO instanceof EntryWithInstallmentDTO) {
		    if (((InstallmentPaymentCode) paymentCode).getInstallment() == ((EntryWithInstallmentDTO) entryDTO)
			    .getInstallment()) {
			return entryDTO;
		    }
		}
	    }

	} else {
	    for (final EntryDTO entryDTO : entryDTOs) {
		if (!(entryDTO instanceof EntryWithInstallmentDTO)) {
		    return entryDTO;
		}
	    }
	}

	return null;

	// throw new DomainException(
	// "error.accounting.events.gratuity.GratuityEventWithPaymentPlan.paymentCode.does.not.have.corresponding.entryDTO.because.data.is.corrupted"
	// );

    }

    public void changeGratuityTotalPaymentCodeState(final PaymentCodeState paymentCodeState) {
	for (final AccountingEventPaymentCode accountingEventPaymentCode : getNonProcessedPaymentCodes()) {
	    if (!(accountingEventPaymentCode instanceof InstallmentPaymentCode)) {
		accountingEventPaymentCode.setState(paymentCodeState);
	    }
	}
    }

    public void changeInstallmentPaymentCodeState(final Installment installment, final PaymentCodeState paymentCodeState) {
	for (final AccountingEventPaymentCode paymentCode : getNonProcessedPaymentCodes()) {
	    if (paymentCode instanceof InstallmentPaymentCode
		    && ((InstallmentPaymentCode) paymentCode).getInstallment() == installment) {
		paymentCode.setState(paymentCodeState);
	    }
	}

	// If at least one installment is payed, we assume that the payment
	// will be based on installments
	changeGratuityTotalPaymentCodeState(PaymentCodeState.CANCELLED);
    }

    private AccountingEventPaymentCode createAccountingEventPaymentCode(final EntryDTO entryDTO, final Student student) {
	return AccountingEventPaymentCode.create(PaymentCodeType.TOTAL_GRATUITY, new YearMonthDay(),
		calculateFullPaymentCodeEndDate(), this, entryDTO.getAmountToPay(), entryDTO.getAmountToPay(), student);
    }

    private InstallmentPaymentCode createInstallmentPaymentCode(final EntryWithInstallmentDTO entry, final Student student) {
	return InstallmentPaymentCode.create(getPaymentCodeTypeFor(entry.getInstallment()), new YearMonthDay(),
		calculateInstallmentPaymentCodeEndDate(entry.getInstallment()), this, entry.getInstallment(), entry
			.getAmountToPay(), entry.getAmountToPay(), student);
    }

    private PaymentCodeType getPaymentCodeTypeFor(Installment installment) {
	if (installment.getOrder() == 1) {
	    return PaymentCodeType.GRATUITY_FIRST_INSTALLMENT;
	} else if (installment.getOrder() == 2) {
	    return PaymentCodeType.GRATUITY_SECOND_INSTALLMENT;
	} else {
	    throw new DomainException("error.accounting.events.gratuity.GratuityEventWithPaymentPlan.invalid.installment.order");
	}

    }

    private Installment getFirstInstallment() {
	return getGratuityPaymentPlan().getFirstInstallment();
    }

    private Installment getLastInstallment() {
	return getGratuityPaymentPlan().getLastInstallment();
    }

    public DegreeCurricularPlanServiceAgreement getDegreeCurricularPlanServiceAgreement() {
	return (DegreeCurricularPlanServiceAgreement) getPerson().getServiceAgreementFor(getServiceAgreementTemplate());

    }

    @Override
    public boolean hasInstallments() {
	return true;
    }

    public InstallmentPaymentCode getInstallmentPaymentCodeFor(final Installment installment) {
	for (final AccountingEventPaymentCode paymentCode : calculatePaymentCodes()) {
	    if (paymentCode instanceof InstallmentPaymentCode
		    && ((InstallmentPaymentCode) paymentCode).getInstallment() == installment) {
		return (InstallmentPaymentCode) paymentCode;
	    }
	}

	return null;
    }

    public AccountingEventPaymentCode getTotalPaymentCode() {
	for (final AccountingEventPaymentCode paymentCode : calculatePaymentCodes()) {
	    if (!(paymentCode instanceof InstallmentPaymentCode)) {
		return paymentCode;
	    }
	}

	return null;
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode, Money amountToPay,
	    SibsTransactionDetailDTO transactionDetail) {
	return internalProcess(responsibleUser, Collections.singletonList(buildEntryDTOFrom(paymentCode, amountToPay)),
		transactionDetail);
    }

    private EntryDTO buildEntryDTOFrom(final AccountingEventPaymentCode paymentCode, final Money amountToPay) {
	if (paymentCode instanceof InstallmentPaymentCode) {
	    return new EntryWithInstallmentDTO(EntryType.GRATUITY_FEE, this, amountToPay, ((InstallmentPaymentCode) paymentCode)
		    .getInstallment());
	} else {
	    return new EntryDTO(EntryType.GRATUITY_FEE, this, amountToPay);
	}
    }

    private boolean installmentIsInDebtToday(Installment installment) {
	return installmentIsInDebt(installment) && new YearMonthDay().isAfter(installment.getEndDate());
    }

    private boolean installmentIsInDebt(Installment installment) {
	return getGratuityPaymentPlan().isInstallmentInDebt(installment, this, new DateTime(),
		calculateDiscountPercentage(getGratuityPaymentPlan().calculateOriginalTotalAmount()));
    }

    @Override
    public boolean isInDebt() {
	return isOpen() && (installmentIsInDebtToday(getFirstInstallment()) || installmentIsInDebtToday(getLastInstallment()));
    }

    public InstallmentPenaltyExemption getInstallmentPenaltyExemptionFor(final Installment installment) {
	for (final Exemption exemption : getExemptionsSet()) {
	    if (exemption instanceof InstallmentPenaltyExemption) {
		final InstallmentPenaltyExemption installmentPenaltyExemption = (InstallmentPenaltyExemption) exemption;
		if (installmentPenaltyExemption.getInstallment() == installment) {
		    return installmentPenaltyExemption;
		}
	    }
	}

	return null;
    }

    public boolean hasPenaltyExemptionFor(final Installment installment) {
	for (final Exemption exemption : getExemptionsSet()) {
	    if (exemption instanceof InstallmentPenaltyExemption) {
		if (((InstallmentPenaltyExemption) exemption).getInstallment() == installment) {
		    return true;
		}

	    }
	}

	return false;
    }

    public List<Installment> getInstallments() {
	return getGratuityPaymentPlan().getInstallmentsSortedByEndDate();
    }

    @Override
    public boolean isExemptionAppliable() {
	return true;
    }

    public List<InstallmentPenaltyExemption> getInstallmentPenaltyExemptions() {
	final List<InstallmentPenaltyExemption> result = new ArrayList<InstallmentPenaltyExemption>();
	for (final Exemption exemption : getExemptionsSet()) {
	    if (exemption instanceof InstallmentPenaltyExemption) {
		result.add((InstallmentPenaltyExemption) exemption);
	    }
	}

	return result;
    }

    @Override
    public boolean isOtherPartiesPaymentsSupported() {
	return true;
    }

    @Override
    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void delete() {
	if (hasCustomGratuityPaymentPlan()) {
	    ((CustomGratuityPaymentPlan) getGratuityPaymentPlan()).delete();
	}
	super.setGratuityPaymentPlan(null);
	super.delete();
    }

    @Override
    public boolean isGratuityEventWithPaymentPlan() {
	return true;
    }

    public boolean hasCustomGratuityPaymentPlan() {
	return hasGratuityPaymentPlan() && getGratuityPaymentPlan().isCustomGratuityPaymentPlan();
    }

    public Money getPayedAmountLessPenalty() {
	if (isCancelled()) {
	    throw new DomainException("error.accounting.Event.cannot.calculatePayedAmount.on.invalid.events");
	}

	final DateTime now = new DateTime();
	Money result = Money.ZERO;

	for (final Installment installment : getGratuityPaymentPlan().getInstallments()) {
	    if (!getGratuityPaymentPlan().isInstallmentInDebt(installment, this, now, BigDecimal.ZERO)) {
		result = result.add(installment.getAmount());
	    }
	}
	return result;
    }

    @Override
    public GratuityWithPaymentPlanPR getPostingRule() {
	return (GratuityWithPaymentPlanPR) super.getPostingRule();
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
	return Collections.singleton(EntryType.GRATUITY_FEE);
    }

}
