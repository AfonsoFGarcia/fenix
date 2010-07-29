package net.sourceforge.fenixedu.domain.accounting.report;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.PercentageGratuityExemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.StandaloneEnrolmentGratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.ValueGratuityExemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class GratuityReportQueueJob extends GratuityReportQueueJob_Base {

    private GratuityReportQueueJob() {
	super();
    }

    GratuityReportQueueJob(final ExecutionYear executionYear) {
	this();

	if (executionYear == null) {
	    throw new DomainException("error.gratuity.report.execution.year.cannot.be.null");
	}

	setExecutionYear(executionYear);
    }

    @Override
    public QueueJobResult execute() throws Exception {
	ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

	buildReport().exportToCSV(byteArrayOS, ";");

	final QueueJobResult queueJobResult = new QueueJobResult();
	queueJobResult.setContentType("text/csv");
	queueJobResult.setContent(byteArrayOS.toByteArray());

	System.out.println("Job " + getFilename() + " completed");

	return queueJobResult;
    }

    private static final DateTime BEGIN_YEAR = new DateTime(2009, 01, 01, 0, 0, 0, 0);
    private static final DateTime END_YEAR = new DateTime(2009, 12, 31, 23, 59, 59, 0);

    private Spreadsheet buildReport() {
	final Spreadsheet spreadsheet = new Spreadsheet(getFilename());

	spreadsheet.setHeaders(new String[] { "Aluno", "Nome", "N� Contribuinte", "Ano", "Curso", "Tipo de Curso",
		"Valor Em D�vida", "Valor Total", "Valor Pago", "Valor Reembols�vel", "Tipo de Isen��o", "Valor de Isen��o",
		"Percentagem de Isen��o", "Motivo", "Data de entrada do pagamento", "Montante inicial", "Montante ajustado",
		"Modo de pagamento", "Data de entrada do ajuste", "Montante do ajuste", "Justifica��o" });

	int i = 0;
	for (final GratuityEvent gratuityEvent : getGratuityEvents()) {
	    GratuityEventEntry entry = new GratuityEventEntry(gratuityEvent);

	    for (TransactionEntryDetail transaction : entry.getNonAdjustingTransactions()) {
		if (transaction.getWhenRegisteredDateTime().isBefore(BEGIN_YEAR)) {
		    continue;
		}

		if (transaction.getWhenRegisteredDateTime().isAfter(END_YEAR)) {
		    continue;
		}

		Row row = spreadsheet.addRow();

		fillWithGratuityEventInformation(row, entry);
		fillWithTransactionsInformation(row, transaction);
	    }

	    if (++i % 100 == 0) {
		System.out.println(String.format("Lido %d propinas", i));

	    }
	}

	return spreadsheet;
    }

    private void fillWithTransactionsInformation(Row row, TransactionEntryDetail transaction) {
	row.setCell(transaction.getWhenRegistered());
	row.setCell(transaction.getOriginalAmount().toPlainString());
	row.setCell(transaction.getAmountWithAdjustment().toPlainString());
	row.setCell(transaction.getPaymentModeName());

	for (TransactionEntryDetail adjustment : transaction.getBoundAdjustingTransactions()) {
	    row.setCell(adjustment.getWhenRegistered());
	    row.setCell(adjustment.getAmountWithAdjustment().toPlainString());
	    row.setCell(adjustment.getJustification());
	}

	if (transaction.getBoundAdjustingTransactions().isEmpty()) {
	    row.setCell("");
	    row.setCell("");
	    row.setCell("");
	}

	for (String name : transaction.getReceiptContributorsNameForOtherParties()) {
	    row.setCell(name);
	}
    }

    private void fillWithGratuityEventInformation(Row row, GratuityEventEntry entry) {
	row.setCell(entry.getStudentNumber());
	row.setCell(entry.getStudentName());
	row.setCell(entry.getSocialSecurityNumber());
	row.setCell(entry.getGratuityExecutionYearName());
	row.setCell(entry.getDegreeName());
	row.setCell(entry.getDegreeTypeName());
	row.setCell(entry.getDebtAmount().toPlainString());
	row.setCell(entry.getTotalAmount().toPlainString());
	row.setCell(entry.getPaidAmount().toPlainString());
	row.setCell(entry.getReimbuisedAmount().toPlainString());

	if (entry.hasExemption()) {
	    row.setCell(entry.getExemptionJustificationName());
	    row.setCell(entry.getExemptionValue().toPlainString());
	    row.setCell(entry.getExemptionPercentage());
	    row.setCell(entry.getExemptionReason());
	} else {
	    row.setCell("");
	    row.setCell("");
	    row.setCell("");
	    row.setCell("");
	}
    }

    @Override
    public String getFilename() {
	return String.format("PROPINAS_%s.csv", getRequestDate().toString("dd_MM_yyyy_hh_mm"));
    }

    private static class GratuityEventEntry {
	GratuityEvent event;

	public GratuityEventEntry(GratuityEvent event) {
	    this.event = event;
	}

	public String getStudentNumber() {
	    return event.getPerson().getStudent().getNumber().toString();
	}

	public String getStudentName() {
	    return event.getPerson().getName();
	}

	public String getSocialSecurityNumber() {
	    return event.getPerson().getSocialSecurityNumber();
	}

	public String getGratuityExecutionYearName() {
	    return event.getExecutionYear().getName();
	}

	public String getDegreeName() {
	    return event.getDegree().getNameI18N().getContent();
	}

	public String getDegreeTypeName() {
	    return event.getDegree().getDegreeType().getLocalizedName();
	}

	public Money getOriginalTotalAmount() {
	    // TODO

	    return Money.ZERO;
	}

	public Money getTotalAmount() {
	    return event.getTotalAmountToPay();
	}

	public Money getDebtAmount() {
	    return event.getAmountToPay();
	}

	public Money getPaidAmount() {
	    return event.getPayedAmount();
	}

	public Money getReimbuisedAmount() {
	    return event.getReimbursableAmount();
	}

	public boolean hasExemption() {
	    return event.hasGratuityExemption();
	}

	public String getExemptionJustificationName() {
	    return hasExemption() ? event.getGratuityExemption().getJustificationType().getLocalizedName() : "";
	}

	public Money getExemptionValue() {
	    if (!hasExemption()) {
		return Money.ZERO;
	    }

	    if (!event.getGratuityExemption().isValueExemption()) {
		return Money.ZERO;
	    }

	    return ((ValueGratuityExemption) event.getGratuityExemption()).getValue();
	}

	public BigDecimal getExemptionPercentage() {
	    if (!hasExemption()) {
		return new BigDecimal(0d);
	    }

	    if (!event.getGratuityExemption().isPercentageExemption()) {
		return new BigDecimal(0d);
	    }

	    return ((PercentageGratuityExemption) event.getGratuityExemption()).getPercentage();
	}

	public String getExemptionReason() {
	    if (!hasExemption()) {
		return "";
	    }

	    return event.getGratuityExemption().getReason();
	}

	public List<TransactionEntryDetail> getNonAdjustingTransactions() {
	    List<TransactionEntryDetail> transactionList = new ArrayList<TransactionEntryDetail>();

	    for (AccountingTransaction transaction : event.getNonAdjustingTransactions()) {
		transactionList.add(new TransactionEntryDetail(transaction));
	    }

	    return transactionList;
	}
    }

    private static class TransactionEntryDetail {
	AccountingTransaction transaction;

	public TransactionEntryDetail(final AccountingTransaction transaction) {
	    this.transaction = transaction;
	}

	public String getWhenRegistered() {
	    return transaction.getWhenRegistered().toString("dd-MM-yyyy");
	}

	public DateTime getWhenRegisteredDateTime() {
	    return transaction.getWhenRegistered();
	}

	public String getPaymentModeName() {
	    return transaction.getPaymentMode().getLocalizedName();
	}

	public Money getOriginalAmount() {
	    return transaction.getOriginalAmount();
	}

	public Money getAmountWithAdjustment() {
	    return transaction.getAmountWithAdjustment();
	}

	public List<TransactionEntryDetail> getBoundAdjustingTransactions() {
	    List<TransactionEntryDetail> adjustmentTransactions = new ArrayList<TransactionEntryDetail>();

	    for (AccountingTransaction adjustment : transaction.getAdjustmentTransactions()) {
		adjustmentTransactions.add(new TransactionEntryDetail(adjustment));
	    }

	    return adjustmentTransactions;
	}

	public String getJustification() {
	    return transaction.getComments();
	}

	public boolean hasReceipts() {
	    return transaction.getToAccountEntry().getReceiptsCount() > 0;
	}

	public String[] getReceiptContributorsNameForOtherParties() {
	    if (!hasReceipts()) {
		return new ArrayList<String>().toArray(new String[0]);
	    }

	    List<String> contributorsNames = new ArrayList<String>();
	    for (Receipt receipt : transaction.getEntryFor(transaction.getFromAccount()).getReceipts()) {
		if (!net.sourceforge.fenixedu.util.StringUtils.isEmpty(receipt.getContributorName())) {
		    contributorsNames.add(receipt.getContributorName());
		    continue;
		}

		if (receipt.getContributorParty() != transaction.getEvent().getPerson()) {
		    contributorsNames.add(receipt.getContributorParty().getSocialSecurityNumber());
		}

	    }

	    return contributorsNames.toArray(new String[0]);
	}
    }

    protected Set<GratuityEvent> getGratuityEvents() {
	final Set<GratuityEvent> result = new HashSet<GratuityEvent>();

	int i = 0;
	List<ExecutionYear> executionYearList = new ArrayList<ExecutionYear>();
	executionYearList.addAll(RootDomainObject.readAllDomainObjects(ExecutionYear.class));

	for (ExecutionYear executionYear : executionYearList) {
	    for (AnnualEvent event : executionYear.getAnnualEvents()) {
		if (event.isCancelled()) {
		    continue;
		}

		if (event instanceof GratuityEvent) {
		    if (!isToDiscard((GratuityEvent) event)) {
			result.add((GratuityEvent) event);
		    }
		}

		if ((++i % 1000) == 0) {
		    System.out.println(String.format("Read %s events", i));
		}
	    }
	}

	return result;
    }

    private boolean isToDiscard(GratuityEvent gratuityEvent) {
	if (!(gratuityEvent instanceof StandaloneEnrolmentGratuityEvent)) {
	    return false;
	}

	final Collection<Enrolment> enrolmentsToCalculateGratuity = getEnrolmentsToCalculateGratuity(gratuityEvent);

	if (enrolmentsToCalculateGratuity.isEmpty()) {
	    return true;
	}

	for (final Enrolment enrolment : enrolmentsToCalculateGratuity) {
	    if (enrolment.getDegreeCurricularPlanOfDegreeModule().getDegree().isDEA()) {
		return true;
	    }
	}

	return false;

    }

    private Collection<Enrolment> getEnrolmentsToCalculateGratuity(GratuityEvent gratuityEvent) {
	if (!gratuityEvent.getDegree().isEmpty()) {
	    if (!gratuityEvent.getStudentCurricularPlan().hasStandaloneCurriculumGroup()) {
		return Collections.emptySet();
	    }

	    return gratuityEvent.getStudentCurricularPlan().getStandaloneCurriculumGroup().getEnrolmentsBy(
		    gratuityEvent.getExecutionYear());
	} else {
	    return gratuityEvent.getStudentCurricularPlan().getRoot().getEnrolmentsBy(gratuityEvent.getExecutionYear());
	}
    }

    public static List<GratuityReportQueueJob> retrieveAllGeneratedReports(final ExecutionYear executionYear) {
	List<GratuityReportQueueJob> reports = new ArrayList<GratuityReportQueueJob>();

	CollectionUtils.select(executionYear.getGratuityReportQueueJobs(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		GratuityReportQueueJob gratuityQueueJob = (GratuityReportQueueJob) arg0;

		return gratuityQueueJob.getDone();
	    }

	}, reports);

	return reports;
    }

    public static List<GratuityReportQueueJob> retrieveNotGeneratedReports(final ExecutionYear executionYear) {
	List<GratuityReportQueueJob> reports = new ArrayList<GratuityReportQueueJob>();

	CollectionUtils.select(executionYear.getGratuityReportQueueJobs(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		GratuityReportQueueJob gratuityQueueJob = (GratuityReportQueueJob) arg0;

		return !gratuityQueueJob.getDone();
	    }

	}, reports);

	return reports;
    }

    public static boolean canRequestReportGeneration() {
	return QueueJob.getUndoneJobsForClass(GratuityReportQueueJob.class).isEmpty();
    }
}
