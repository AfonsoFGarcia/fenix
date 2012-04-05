package net.sourceforge.fenixedu.domain.accounting.report.events;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import jvstm.TransactionalCommand;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.IndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DFACandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyEvent;
import net.sourceforge.fenixedu.domain.phd.debts.PhdEvent;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;
import pt.utl.ist.fenix.tools.resources.DefaultResourceBundleProvider;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class EventReportQueueJob extends EventReportQueueJob_Base {

    private static final List<String> EVENTS = Arrays.asList(new String[] {});

    private static final List<Class<? extends Event>> CANDIDACY_EVENT_TYPES = new ArrayList<Class<? extends Event>>();
    private static final List<Class<? extends AnnualEvent>> ADMIN_OFFICE_AND_INSURANCE_TYPES = new ArrayList<Class<? extends AnnualEvent>>();

    static {
	CANDIDACY_EVENT_TYPES.add(DFACandidacyEvent.class);
	CANDIDACY_EVENT_TYPES.add(PhdProgramCandidacyEvent.class);
	CANDIDACY_EVENT_TYPES.add(IndividualCandidacyEvent.class);

	ADMIN_OFFICE_AND_INSURANCE_TYPES.add(AdministrativeOfficeFeeAndInsuranceEvent.class);
	ADMIN_OFFICE_AND_INSURANCE_TYPES.add(InsuranceEvent.class);
    }

    private static boolean IS_CANDIDACY_EVENT(final Event event) {
	for (Class<? extends Event> type : CANDIDACY_EVENT_TYPES) {
	    if (type.isAssignableFrom(event.getClass())) {
		return true;
	    }
	}

	return false;
    }

    private static boolean ALL_OTHER(final Event event) {
	if (event.isGratuity()) {
	    return false;
	}

	if (event.isAcademicServiceRequestEvent()) {
	    return false;
	}

	return !IS_CANDIDACY_EVENT(event) && !IS_ADMIN_OFFICE_OR_INSURANCE_EVENT(event);
    }

    private static boolean IS_ADMIN_OFFICE_OR_INSURANCE_EVENT(final Event event) {
	for (Class<? extends Event> type : ADMIN_OFFICE_AND_INSURANCE_TYPES) {
	    if (type.isAssignableFrom(event.getClass())) {
		return true;
	    }
	}

	return false;
    }

    private List<Event> eventsToProcess = null;

    private EventReportQueueJob() {
	super();
    }

    private EventReportQueueJob(final EventReportQueueJobBean bean) {
	this();
	checkPermissions(bean);

	setExportGratuityEvents(bean.getExportGratuityEvents());
	setExportAcademicServiceRequestEvents(bean.getExportAcademicServiceRequestEvents());
	setExportIndividualCandidacyEvents(bean.getExportIndividualCandidacyEvents());
	setExportPhdEvents(bean.getExportPhdEvents());
	setExportResidenceEvents(bean.getExportResidenceEvents());
	setExportOthers(bean.getExportOthers());
	setForDegreeAdministrativeOffice(bean.getForDegreeAdministrativeOffice());
	setForMasterDegreeAdministrativeOffice(bean.getForMasterDegreeAdministrativeOffice());
	setBeginDate(bean.getBeginDate());
	setEndDate(bean.getEndDate());

	setForExecutionYear(bean.getExecutionYear());
	setForAdministrativeOffice(readAdministrativeOffice());
    }

    private AdministrativeOffice readAdministrativeOffice() {
	Person loggedPerson = AccessControl.getPerson();

	if (!loggedPerson.hasEmployee()) {
	    return null;
	}

	return loggedPerson.getEmployee().getAdministrativeOffice();
    }

    private void checkPermissions(EventReportQueueJobBean bean) {
	Person loggedPerson = AccessControl.getPerson();

	if (loggedPerson == null) {
	    throw new DomainException("error.EventReportQueueJob.permission.denied");
	}

	if (loggedPerson.hasRole(RoleType.MANAGER)) {
	    return;
	}

	if (!loggedPerson.hasEmployee()) {
	    throw new DomainException("error.EventReportQueueJob.permission.denied");
	}

	AdministrativeOffice administrativeOffice = loggedPerson.getEmployee().getAdministrativeOffice();
	if (administrativeOffice == null) {
	    throw new DomainException("error.EventReportQueueJob.permission.denied");
	}

	if (administrativeOffice.isMasterDegree() && bean.getForDegreeAdministrativeOffice()) {
	    throw new DomainException("error.EventReportQueueJob.permission.denied");
	}

	if (administrativeOffice.isDegree() && bean.getForMasterDegreeAdministrativeOffice()) {
	    throw new DomainException("error.EventReportQueueJob.permission.denied");
	}
    }

    @Override
    public String getFilename() {
	return "dividas" + getRequestDate().toString("ddMMyyyyhms") + ".xlsx";
    }

    @Override
    public QueueJobResult execute() throws Exception {
	ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

	SpreadsheetBuilder buildReport = buildReport();

	buildReport.build(WorkbookExportFormat.DOCX, byteArrayOS);

	byteArrayOS.close();

	final QueueJobResult queueJobResult = new QueueJobResult();
	queueJobResult.setContentType("text/csv");
	queueJobResult.setContent(byteArrayOS.toByteArray());

	System.out.println("Job " + getFilename() + " completed");

	return queueJobResult;
    }

    private SpreadsheetBuilder buildReport() {
	ByteArrayOutputStream byteArrayOutputStream = null;
	PrintStream stringStream = null;
	PrintStream defaultErrorStream = System.err;
	try {
	    byteArrayOutputStream = new ByteArrayOutputStream();
	    stringStream = new PrintStream(byteArrayOutputStream, true);

	    System.setErr(stringStream);

	    final SheetData<EventBean> eventsSheet = allEvents();
	    final SheetData<ExemptionBean> exemptionsSheet = allExemptions();
	    final SheetData<AccountingTransactionBean> transactionsSheet = allTransactions();

	    SpreadsheetBuilder spreadsheetBuilder = new SpreadsheetBuilder();

	    spreadsheetBuilder.addSheet("Dividas", eventsSheet);
	    spreadsheetBuilder.addSheet("Isen�oes", exemptionsSheet);
	    spreadsheetBuilder.addSheet("Transac��es", transactionsSheet);

	    return spreadsheetBuilder;
	} finally {
	    stringStream.close();
	    this.setErrors(new String(byteArrayOutputStream.toByteArray()));
	    System.setErr(defaultErrorStream);
	}
    }

    private List<String> getAllEventsExternalIds() {
	try {
	    Connection connection = Transaction.currentFenixTransaction().getOJBBroker().serviceConnectionManager()
		    .getConnection();

	    PreparedStatement prepareStatement = connection.prepareStatement("SELECT OID FROM EVENT");
	    ResultSet executeQuery = prepareStatement.executeQuery();

	    List<String> result = new ArrayList<String>();
	    while (executeQuery.next()) {
		result.add(executeQuery.getString(1));
	    }

	    return result;
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    private static final Integer BLOCK = 5000;

    /* ALL EVENTS */
    private SheetData<EventBean> allEvents() {
	final Spreadsheet spreadsheet = new Spreadsheet("dividas");

	List<String> allEventsExternalIds = getAllEventsExternalIds();
	System.out.println(String.format("%s events to process", allEventsExternalIds.size()));

	Integer blockRead = 0;

	final List<EventBean> result = Collections.synchronizedList(new ArrayList<EventBean>());

	while (blockRead < allEventsExternalIds.size()) {
	    Integer inc = BLOCK;

	    if (blockRead + inc >= allEventsExternalIds.size()) {
		inc = allEventsExternalIds.size() - blockRead;
	    }

	    final List<String> block = allEventsExternalIds.subList(blockRead, blockRead + inc);
	    blockRead += inc;

	    Thread thread = new Thread() {

		@Override
		public void run() {
		    Transaction.withTransaction(true, new TransactionalCommand() {

			@Override
			public void doIt() {
			    for (String oid : block) {
				Event event = Event.fromExternalId(oid);

				try {
				    if (!isAccountingEventForReport(event)) {
					return;
				    }

				    result.add(writeEvent(event));
				} catch (Exception e) {
				    e.printStackTrace(System.err);
				    System.err.println("Error on event -> " + event.getExternalId());
				}

			    }
			}
		    });
		}
	    };

	    thread.start();

	    try {
		thread.join();
	    } catch (InterruptedException e) {
	    }

	    System.out.println(String.format("Read %s events", blockRead));
	}

	return new SheetData<EventBean>(result) {

	    @Override
	    protected void makeLine(EventBean bean) {
		addCell("Identificador", bean.externalId);
		addCell("Aluno", bean.studentNumber);
		addCell("Nome", bean.studentName);
		addCell("Data inscri��o", bean.registrationStartDate);
		addCell("Ano lectivo", bean.executionYear);
		addCell("Tipo de matricula", bean.studiesType);
		addCell("Nome do Curso", bean.degreeName);
		addCell("Tipo de curso", bean.degreeType);
		addCell("Programa doutoral", bean.phdProgramName);
		addCell("ECTS inscritos", bean.enrolledECTS);
		addCell("Regime", bean.regime);
		addCell("Modelo de inscri��o", bean.enrolmentModel);
		addCell("Resid�ncia - Ano", bean.residenceYear);
		addCell("Resid�ncia - M�s", bean.residenceMonth);
		addCell("Tipo de divida", bean.description);
		addCell("Data de cria��o", bean.whenOccured);
		addCell("Valor Total", bean.totalAmount);
		addCell("Valor Pago", bean.payedAmount);
		addCell("Valor em divida", bean.amountToPay);
		addCell("Valor Reembols�vel", bean.reimbursableAmount);
		addCell("Desconto", bean.totalDiscount);
	    }

	};
    }

    private boolean isAccountingEventForReport(final Event event) {

	int count = 0;

	if (event.isCancelled()) {
	    return false;
	}

	Wrapper wrapper = buildWrapper(event);

	if (wrapper == null) {
	    return false;
	}

	if (hasForExecutionYear() && getForExecutionYear() != wrapper.getForExecutionYear()) {
	    return false;
	}

	if (hasForAdministrativeOffice() && getForAdministrativeOffice().isDegree()) {
	    if (!isForDegreeAdministrativeOffice(wrapper)) {
		return false;
	    }
	}

	if (hasForAdministrativeOffice() && getForAdministrativeOffice().isMasterDegree()) {
	    if (!isForMasterDegreeAdministrativeOffice(wrapper)) {
		return false;
	    }
	}

	if (event.getWhenOccured().isBefore(getEndDate().toDateTimeAtStartOfDay().plusDays(1).minusSeconds(1))
		&& event.getWhenOccured().isAfter(getBeginDate().toDateTimeAtStartOfDay())) {
	    return true;
	}

	for (AccountingTransaction transaction : event.getNonAdjustingTransactions()) {
	    if (transaction.getWhenRegistered() != null) {
		if (transaction.getWhenRegistered().isBefore(getEndDate().toDateTimeAtStartOfDay().plusDays(1).minusSeconds(1))
			&& transaction.getWhenRegistered().isAfter(getBeginDate().toDateTimeAtStartOfDay())) {
		    return true;
		}
	    }

	    if (transaction.getWhenProcessed() != null) {
		if (transaction.getWhenProcessed().isBefore(getEndDate().toDateTimeAtStartOfDay().plusDays(1).minusSeconds(1))
			&& transaction.getWhenProcessed().isAfter(getBeginDate().toDateTimeAtStartOfDay())) {
		    return true;
		}
	    }

	    for (AccountingTransaction adjustment : transaction.getAdjustmentTransactions()) {
		if (adjustment.getWhenRegistered() != null) {
		    if (adjustment.getWhenRegistered()
			    .isBefore(getEndDate().toDateTimeAtStartOfDay().plusDays(1).minusSeconds(1))
			    && adjustment.getWhenRegistered().isAfter(getBeginDate().toDateTimeAtStartOfDay())) {
			return true;
		    }
		}

		if (adjustment.getWhenProcessed() != null) {
		    if (adjustment.getWhenProcessed().isBefore(getEndDate().toDateTimeAtStartOfDay().plusDays(1).minusSeconds(1))
			    && adjustment.getWhenProcessed().isAfter(getBeginDate().toDateTimeAtStartOfDay())) {
			return true;
		    }
		}
	    }
	}

	return false;
    }

    private boolean isForMasterDegreeAdministrativeOffice(Wrapper wrapper) {
	if (!hasForAdministrativeOffice()) {
	    return true;
	}

	if (wrapper.getRelatedAcademicOfficeType() == null) {
	    return true;
	}

	return getForAdministrativeOffice().isMasterDegree()
		&& wrapper.getRelatedAcademicOfficeType() == AdministrativeOfficeType.MASTER_DEGREE
		&& getForMasterDegreeAdministrativeOffice();
    }

    private boolean isForDegreeAdministrativeOffice(Wrapper wrapper) {
	if (!hasForAdministrativeOffice()) {
	    return true;
	}

	if (wrapper.getRelatedAcademicOfficeType() == null) {
	    return true;
	}

	return getForAdministrativeOffice().isDegree()
		&& wrapper.getRelatedAcademicOfficeType() == AdministrativeOfficeType.DEGREE
		&& getForDegreeAdministrativeOffice();
    }

    private EventBean writeEvent(final Event event) {
	Properties formatterProperties = new Properties();

	formatterProperties.put(LabelFormatter.ENUMERATION_RESOURCES, "resources.EnumerationResources");
	formatterProperties.put(LabelFormatter.APPLICATION_RESOURCES, "resources.ApplicationResources");

	Wrapper wrapper = buildWrapper(event);

	EventBean bean = new EventBean();

	bean.externalId = event.getExternalId();
	bean.studentNumber = wrapper.getStudentNumber();
	bean.studentName = wrapper.getStudentName();
	bean.registrationStartDate = wrapper.getRegistrationStartDate();
	bean.executionYear = wrapper.getExecutionYear();
	bean.studiesType = wrapper.getStudiesType();
	bean.degreeName = wrapper.getDegreeName();
	bean.degreeType = wrapper.getDegreeType();
	bean.phdProgramName = wrapper.getPhdProgramName();
	bean.enrolledECTS = wrapper.getEnrolledECTS();
	bean.regime = wrapper.getRegime();
	bean.enrolmentModel = wrapper.getEnrolmentModel();
	bean.residenceYear = wrapper.getResidenceYear();
	bean.residenceMonth = wrapper.getResidenceMonth();

	bean.description = event.getDescription().toString(new DefaultResourceBundleProvider(formatterProperties));
	bean.whenOccured = event.getWhenOccured().toString("dd/MM/yyyy");
	bean.totalAmount = event.getTotalAmountToPay().toPlainString();
	bean.payedAmount = event.getPayedAmount().toPlainString();
	bean.amountToPay = event.getAmountToPay().toPlainString();
	bean.reimbursableAmount = event.getReimbursableAmount().toPlainString();
	bean.totalDiscount = event.getTotalDiscount().toPlainString();

	return bean;
    }

    private static class EventBean {
	public String externalId;
	public String studentNumber;
	public String studentName;
	public String registrationStartDate;
	public String executionYear;
	public String studiesType;
	public String degreeName;
	public String degreeType;
	public String phdProgramName;
	public String enrolledECTS;
	public String regime;
	public String enrolmentModel;
	public String residenceYear;
	public String residenceMonth;
	public String description;
	public String whenOccured;
	public String totalAmount;
	public String payedAmount;
	public String amountToPay;
	public String reimbursableAmount;
	public String totalDiscount;
    }

    private static final int NUM_THREADS = 3;

    /* ALL EXEMPTIONS */
    private SheetData<ExemptionBean> allExemptions() {
	List<String> allEventsExternalIds = getAllEventsExternalIds();
	System.out.println(String.format("%s events to process", allEventsExternalIds.size()));

	Integer blockRead = 0;

	final List<ExemptionBean> result = Collections.synchronizedList(new ArrayList<ExemptionBean>());

	while (blockRead < allEventsExternalIds.size()) {
	    Integer inc = BLOCK;

	    if (blockRead + inc >= allEventsExternalIds.size()) {
		inc = allEventsExternalIds.size() - blockRead;
	    }

	    final List<String> block = allEventsExternalIds.subList(blockRead, blockRead + inc);
	    blockRead += inc;

	    Thread thread = new Thread() {

		@Override
		public void run() {
		    Transaction.withTransaction(true, new TransactionalCommand() {

			@Override
			public void doIt() {
			    for (String oid : block) {
				Event event = Event.fromExternalId(oid);

				try {
				    if (!isAccountingEventForReport(event)) {
					return;
				    }

				    result.addAll(writeExemptionInformation(event));
				} catch (Exception e) {
				    e.printStackTrace(System.err);
				    System.err.println("Error on event -> " + event.getExternalId());
				}

			    }
			}
		    });
		}
	    };

	    thread.start();

	    try {
		thread.join();
	    } catch (InterruptedException e) {
	    }

	    System.out.println(String.format("Read %s events", blockRead));
	}

	return new SheetData<ExemptionBean>(result) {

	    @Override
	    protected void makeLine(ExemptionBean bean) {

		addCell("Identificador", bean.eventExternalId);
		addCell("Tipo da Isen��o", bean.exemptionTypeDescription);
		addCell("Valor da Isen��o", bean.exemptionValue);
		addCell("Percentagem da Isen��o", bean.percentage);
		addCell("Motivo da Isen��o", bean.justification);

	    }

	};
    }

    // write Exemption Information
    private List<ExemptionBean> writeExemptionInformation(Event event) {
	Set<Exemption> exemptionsSet = event.getExemptionsSet();

	List<ExemptionBean> result = new ArrayList<ExemptionBean>();

	for (Exemption exemption : exemptionsSet) {

	    ExemptionWrapper wrapper = new ExemptionWrapper(exemption);

	    Properties formatterProperties = new Properties();

	    ExemptionBean bean = new ExemptionBean();

	    formatterProperties.put(LabelFormatter.ENUMERATION_RESOURCES, "resources.EnumerationResources");
	    formatterProperties.put(LabelFormatter.APPLICATION_RESOURCES, "resources.ApplicationResources");

	    bean.eventExternalId = event.getExternalId();
	    bean.exemptionTypeDescription = wrapper.getExemptionTypeDescription();
	    bean.exemptionValue = wrapper.getExemptionValue();
	    bean.percentage = wrapper.getPercentage();
	    bean.justification = wrapper.getJustification();

	    result.add(bean);
	}

	return result;
    }

    private class ExemptionBean {
	public String eventExternalId;
	public String exemptionTypeDescription;
	public String exemptionValue;
	public String percentage;
	public String justification;
    }

    /* ALL TRANSACTIONS */

    private SheetData<AccountingTransactionBean> allTransactions() {
	final Spreadsheet spreadsheet = new Spreadsheet("Transac��es");

	List<String> allEventsExternalIds = getAllEventsExternalIds();
	System.out.println(String.format("%s events to process", allEventsExternalIds.size()));

	Integer blockRead = 0;

	final List<AccountingTransactionBean> result = Collections.synchronizedList(new ArrayList<AccountingTransactionBean>());

	while (blockRead < allEventsExternalIds.size()) {
	    Integer inc = BLOCK;

	    if (blockRead + inc >= allEventsExternalIds.size()) {
		inc = allEventsExternalIds.size() - blockRead;
	    }

	    final List<String> block = allEventsExternalIds.subList(blockRead, blockRead + inc);
	    blockRead += inc;

	    Thread thread = new Thread() {

		@Override
		public void run() {
		    Transaction.withTransaction(true, new TransactionalCommand() {

			@Override
			public void doIt() {
			    for (String oid : block) {
				Event event = Event.fromExternalId(oid);

				try {
				    if (!isAccountingEventForReport(event)) {
					return;
				    }

				    result.addAll(writeTransactionInformation(event));
				} catch (Exception e) {
				    e.printStackTrace(System.err);
				    System.err.println("Error on event -> " + event.getExternalId());
				}

			    }
			}
		    });
		}
	    };

	    thread.start();

	    try {
		thread.join();
	    } catch (InterruptedException e) {
	    }

	    System.out.println(String.format("Read %s events", blockRead));
	}

	return new SheetData<AccountingTransactionBean>(result) {

	    @Override
	    protected void makeLine(AccountingTransactionBean bean) {

		addCell("Identificador", bean.eventExternalId);
		addCell("Data do pagamento", bean.whenRegistered);
		addCell("Data de entrada do pagamento", bean.whenProcessed);
		addCell("Nome da entidade devedora", bean.debtPartyName);
		addCell("Contribuinte da entidade devedora", bean.debtSocialSecurityNumber);
		addCell("Nome da entidade credora", bean.credPartyName);
		addCell("Contribuinte da entidade credora", bean.credSocialSecurityNumber);
		addCell("Montante inicial", bean.originalAmount);
		addCell("Montante ajustado", bean.amountWithAdjustment);
		addCell("Modo de pagamento", bean.paymentMode);
		addCell("Data do ajuste", bean.whenAdjustmentRegistered);
		addCell("Data de entrada do ajuste", bean.whenAdjustmentProcessed);
		addCell("Montante do ajuste", bean.adjustmentAmount);
		addCell("Justifica��o", bean.comments);

	    }

	};
    }

    private List<AccountingTransactionBean> writeTransactionInformation(Event event) {
	List<AccountingTransactionBean> result = new ArrayList<AccountingTransactionBean>();

	for (AccountingTransaction transaction : event.getNonAdjustingTransactions()) {

	    for (AccountingTransaction adjustment : transaction.getAdjustmentTransactions()) {
		Entry internalEntry = obtainInternalAccountEntry(adjustment);
		Entry externalEntry = obtainExternalAccountEntry(adjustment);

		AccountingTransactionBean bean = new AccountingTransactionBean();

		bean.eventExternalId = valueOrNull(event.getExternalId());
		bean.whenRegistered = valueOrNull(transaction.getWhenRegistered());
		bean.whenProcessed = valueOrNull(transaction.getWhenProcessed());
		bean.debtPartyName = internalEntry != null ? valueOrNull(internalEntry.getAccount().getParty().getPartyName()
			.getContent()) : "-";
		bean.debtSocialSecurityNumber = internalEntry != null ? valueOrNull(internalEntry.getAccount().getParty()
			.getSocialSecurityNumber()) : "-";
		bean.credPartyName = externalEntry != null ? valueOrNull(externalEntry.getAccount().getParty().getPartyName()
			.getContent()) : "-";
		bean.credSocialSecurityNumber = externalEntry != null ? valueOrNull(externalEntry.getAccount().getParty()
			.getSocialSecurityNumber()) : "-";
		bean.originalAmount = valueOrNull(transaction.getOriginalAmount().toPlainString());
		bean.amountWithAdjustment = valueOrNull(transaction.getAmountWithAdjustment().toPlainString());
		bean.paymentMode = valueOrNull(transaction.getPaymentMode().getLocalizedName());
		bean.whenAdjustmentRegistered = valueOrNull(adjustment.getWhenRegistered());
		bean.whenAdjustmentProcessed = valueOrNull(adjustment.getWhenProcessed());
		bean.amountWithAdjustment = valueOrNull(adjustment.getAmountWithAdjustment().toPlainString());
		bean.comments = valueOrNull(adjustment.getComments());

		result.add(bean);
	    }

	    if (transaction.getAdjustmentTransactions().isEmpty()) {
		Entry internalEntry = obtainInternalAccountEntry(transaction);
		Entry externalEntry = obtainExternalAccountEntry(transaction);

		AccountingTransactionBean bean = new AccountingTransactionBean();

		bean.eventExternalId = event.getExternalId();
		bean.whenRegistered = valueOrNull(transaction.getWhenRegistered());
		bean.whenProcessed = valueOrNull(transaction.getWhenProcessed());
		bean.debtPartyName = internalEntry != null ? valueOrNull(internalEntry.getAccount().getParty().getPartyName()
			.getContent()) : "-";
		bean.debtSocialSecurityNumber = internalEntry != null ? valueOrNull(internalEntry.getAccount().getParty()
			.getSocialSecurityNumber()) : "-";
		bean.credPartyName = externalEntry != null ? valueOrNull(externalEntry.getAccount().getParty().getPartyName()
			.getContent()) : "-";
		bean.credSocialSecurityNumber = externalEntry != null ? valueOrNull(externalEntry.getAccount().getParty()
			.getSocialSecurityNumber()) : "-";
		bean.originalAmount = valueOrNull(transaction.getOriginalAmount().toPlainString());
		bean.amountWithAdjustment = valueOrNull(transaction.getAmountWithAdjustment().toPlainString());
		bean.paymentMode = valueOrNull(transaction.getPaymentMode().getLocalizedName());
		bean.whenAdjustmentRegistered = "-";
		bean.amountWithAdjustment = "-";
		bean.whenAdjustmentProcessed = "-";
		bean.comments = "-";

		result.add(bean);
	    }
	}

	return result;
    }

    private class AccountingTransactionBean {

	public String eventExternalId;
	public String whenRegistered;
	public String whenProcessed;
	public String debtPartyName;
	public String debtSocialSecurityNumber;
	public String credPartyName;
	public String credSocialSecurityNumber;
	public String originalAmount;
	public String amountWithAdjustment;
	public String paymentMode;
	public String whenAdjustmentRegistered;
	public String whenAdjustmentProcessed;
	public String adjustmentAmount;
	public String comments;
    }

    private Entry obtainInternalAccountEntry(final AccountingTransaction transaction) {
	return transaction.getFromAccountEntry();
    }

    private Entry obtainExternalAccountEntry(final AccountingTransaction transaction) {
	return transaction.getToAccountEntry();
    }

    // Residence

    private Wrapper buildWrapper(Event event) {

	if (event.isGratuity()) {
	    return getExportGratuityEvents() ? new GratuityEventWrapper((GratuityEvent) event) : null;
	} else if (event.isAcademicServiceRequestEvent()) {
	    return getExportAcademicServiceRequestEvents() ? new AcademicServiceRequestEventWrapper(
		    (AcademicServiceRequestEvent) event) : null;
	} else if (event.isIndividualCandidacyEvent()) {
	    return getExportIndividualCandidacyEvents() ? new IndividualCandidacyEventWrapper((IndividualCandidacyEvent) event)
		    : null;
	} else if (event.isPhdEvent()) {
	    return getExportPhdEvents() ? new PhdEventWrapper((PhdEvent) event) : null;
	} else if (event.isResidenceEvent()) {
	    return getExportResidenceEvents() ? new ResidenceEventWrapper((ResidenceEvent) event) : null;
	} else if (event.isFctScholarshipPhdGratuityContribuitionEvent()) {
	    return getExportPhdEvents() ? new ExternalScholarshipPhdGratuityContribuitionEventWrapper(event) : null;
	} else if (getExportOthers()) {
	    return new EventWrapper(event);
	}

	return null;
    }

    private void copyRowCells(Row from, Row to) {
	List<Object> cells = from.getCells();

	for (Object cell : cells) {
	    to.setCell((String) cell);
	}
    }

    public static List<EventReportQueueJob> retrieveAllGeneratedReports() {
	List<EventReportQueueJob> reports = new ArrayList<EventReportQueueJob>();

	CollectionUtils.select(RootDomainObject.getInstance().getQueueJobSet(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		return arg0 instanceof EventReportQueueJob;
	    }

	}, reports);

	return reports;
    }

    public static List<EventReportQueueJob> retrieveDoneGeneratedReports() {
	List<EventReportQueueJob> reports = new ArrayList<EventReportQueueJob>();

	CollectionUtils.select(RootDomainObject.getInstance().getQueueJobSet(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		if (!(arg0 instanceof EventReportQueueJob)) {
		    return false;
		}

		EventReportQueueJob eventReportQueueJob = (EventReportQueueJob) arg0;

		return eventReportQueueJob.getDone();
	    }

	}, reports);

	return reports;
    }

    public static List<EventReportQueueJob> retrievePendingOrCancelledGeneratedReports() {
	List<EventReportQueueJob> all = retrieveAllGeneratedReports();
	List<EventReportQueueJob> done = retrieveDoneGeneratedReports();

	all.removeAll(done);

	return all;
    }

    @Service
    public static EventReportQueueJob createRequest(EventReportQueueJobBean bean) {
	return new EventReportQueueJob(bean);
    }

    public static List<EventReportQueueJob> readPendingOrCancelledJobs(final AdministrativeOfficeType type) {
	List<EventReportQueueJob> list = retrievePendingOrCancelledGeneratedReports();

	return filterByAdministrativeOfficeType(list, type);
    }

    public static List<EventReportQueueJob> readDoneReports(AdministrativeOfficeType type) {
	List<EventReportQueueJob> list = retrieveDoneGeneratedReports();

	return filterByAdministrativeOfficeType(list, type);
    }

    private static List<EventReportQueueJob> filterByAdministrativeOfficeType(final List<EventReportQueueJob> list,
	    final AdministrativeOfficeType type) {

	if (type == null) {
	    return list;
	}

	List<EventReportQueueJob> result = new ArrayList<EventReportQueueJob>();

	for (EventReportQueueJob eventReportQueueJob : list) {
	    if (!eventReportQueueJob.hasForAdministrativeOffice()) {
		continue;
	    }

	    if (eventReportQueueJob.getForAdministrativeOffice().getAdministrativeOfficeType() == type) {
		result.add(eventReportQueueJob);
	    }
	}

	return result;
    }

    private static String valueOrNull(Object obj) {
	if (obj == null) {
	    return "-";
	}

	if (obj instanceof DateTime) {
	    return ((DateTime) obj).toString("dd-MM-yyyy HH:MM");
	}

	if (obj instanceof LocalDate) {
	    return ((LocalDate) obj).toString("dd-MM-yyyy");
	}

	return obj.toString();
    }
}
