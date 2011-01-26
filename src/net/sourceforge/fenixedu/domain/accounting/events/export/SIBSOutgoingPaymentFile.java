package net.sourceforge.fenixedu.domain.accounting.events.export;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.StandaloneEnrolmentGratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.IndividualCandidacyPaymentCode;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.sibs.SibsOutgoingPaymentFile;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class SIBSOutgoingPaymentFile extends SIBSOutgoingPaymentFile_Base {

    private static final String ROOT_DIR_DESCRIPTION = "SIBS Payment Codes File";
    private static final String ROOT_DIR = "SIBSOutgoingPaymentFile";

    private static final Comparator<SIBSOutgoingPaymentFile> SUCCESSFUL_SENT_DATE_TIME_COMPARATOR = new Comparator<SIBSOutgoingPaymentFile>() {

	@Override
	public int compare(SIBSOutgoingPaymentFile o1, SIBSOutgoingPaymentFile o2) {
	    return o1.getSuccessfulSentDate().compareTo(o2.getSuccessfulSentDate());
	}
    };

    private static final Comparator<SIBSOutgoingPaymentFile> CREATION_DATE_TIME_COMPARATOR = new Comparator<SIBSOutgoingPaymentFile>() {

	@Override
	public int compare(SIBSOutgoingPaymentFile o1, SIBSOutgoingPaymentFile o2) {
	    return o1.getUploadTime().compareTo(o2.getUploadTime());
	}
    };

    public SIBSOutgoingPaymentFile(DateTime lastSuccessfulSentDateTime) {
	super();
	setExecutionYear(subjectExecutionYear());

	try {
	    StringBuilder errorsBuilder = new StringBuilder();
	    byte[] paymentFileContents = createPaymentFile(lastSuccessfulSentDateTime, errorsBuilder).getBytes("ASCII");
	    setErrors(errorsBuilder.toString());
	    init(getVirtualPath(), outgoingFilename(), outgoingFilename(), null, paymentFileContents, null);
	} catch (UnsupportedEncodingException e) {
	    throw new DomainException(e.getMessage(), e);
	}
    }

    @Override
    protected List<Class> getAcceptedEventClasses() {
	return Arrays.asList(new Class[] { AdministrativeOfficeFeeAndInsuranceEvent.class, GratuityEventWithPaymentPlan.class,
		DfaGratuityEvent.class, InsuranceEvent.class, ResidenceEvent.class, StandaloneEnrolmentGratuityEvent.class });
    }

    protected String createPaymentFile(DateTime lastSuccessfulSentDateTime, StringBuilder errorsBuilder) {
	final ExecutionYear executionYear = subjectExecutionYear();
	final SibsOutgoingPaymentFile sibsOutgoingPaymentFile = new SibsOutgoingPaymentFile(SOURCE_INSTITUTION_ID,
		DESTINATION_INSTITUTION_ID, ENTITY_CODE, lastSuccessfulSentDateTime);

	for (final Entry<Person, List<Event>> entry : getNotPayedEventsGroupedByPerson(executionYear, errorsBuilder).entrySet()) {
	    for (final Event event : entry.getValue()) {
		addCalculatedPaymentCodesFromEvent(sibsOutgoingPaymentFile, event, errorsBuilder);
	    }
	}

	exportIndividualCandidacyPaymentCodes(sibsOutgoingPaymentFile, errorsBuilder);
	return sibsOutgoingPaymentFile.render();
    }

    protected void exportIndividualCandidacyPaymentCodes(SibsOutgoingPaymentFile sibsFile, StringBuilder errorsBuilder) {
	Set<? extends IndividualCandidacyPaymentCode> individualCandidacyPaymentCodeList = RootDomainObject
		.readAllDomainObjects(IndividualCandidacyPaymentCode.class);

	LocalDate date = new LocalDate();

	for (IndividualCandidacyPaymentCode paymentCode : individualCandidacyPaymentCodeList) {
	    if (!paymentCode.getStartDate().isAfter(date) && !paymentCode.getEndDate().isBefore(date)
		    && paymentCode.getPerson() == null) {
		addPaymentCode(sibsFile, paymentCode, errorsBuilder);
	    }
	}
    }

    protected void addPaymentCode(final SibsOutgoingPaymentFile file, final PaymentCode paymentCode, StringBuilder errorsBuilder) {
	try {
	    file.addLine(paymentCode.getCode(), paymentCode.getMinAmount(), paymentCode.getMaxAmount(),
		    paymentCode.getStartDate(), paymentCode.getEndDate());
	} catch (Throwable e) {
	    appendToErrors(errorsBuilder, paymentCode.getExternalId(), e);
	}
    }

    protected void addCalculatedPaymentCodesFromEvent(final SibsOutgoingPaymentFile file, final Event event,
	    StringBuilder errorsBuilder) {
	try {
	    CalculatePaymentCodes thread = new CalculatePaymentCodes(event.getExternalId(), errorsBuilder, file);
	    thread.start();
	    thread.join();
	} catch (Throwable e) {
	    appendToErrors(errorsBuilder, event.getExternalId(), e);
	}

    }

    protected VirtualPath getVirtualPath() {
	final VirtualPath filePath = new VirtualPath();
	filePath.addNode(new VirtualPathNode(ROOT_DIR, ROOT_DIR_DESCRIPTION));

	filePath.addNode(new VirtualPathNode(subjectExecutionYear().getName(), subjectExecutionYear().getName()));
	filePath.addNode(new VirtualPathNode(outgoingFilename(), outgoingFilename()));

	return filePath;
    }

    private static ExecutionYear subjectExecutionYear() {
	return ExecutionYear.readCurrentExecutionYear();
    }

    private String outgoingFilename() {
	return String.format("SIBS-%s.txt", new DateTime().toString("dd-MM-yyyy_H_m_s"));
    }

    public static List<SIBSOutgoingPaymentFile> readSuccessfulSentPaymentFiles() {
	List<SIBSOutgoingPaymentFile> files = new ArrayList<SIBSOutgoingPaymentFile>();

	CollectionUtils.select(readGeneratedPaymentFiles(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		return ((SIBSOutgoingPaymentFile) arg0).getSuccessfulSentDate() != null;
	    }
	}, files);

	return files;
    }

    public static SIBSOutgoingPaymentFile readLastSuccessfulSentPaymentFile() {
	List<SIBSOutgoingPaymentFile> files = readSuccessfulSentPaymentFiles();

	if (files.isEmpty()) {
	    return null;
	}

	Collections.sort(files, Collections.reverseOrder(SUCCESSFUL_SENT_DATE_TIME_COMPARATOR));
	return files.get(0);
    }

    public static SIBSOutgoingPaymentFile readLastGeneratedPaymentFile() {
	List<SIBSOutgoingPaymentFile> files = readGeneratedPaymentFiles();
	Collections.sort(files, Collections.reverseOrder(CREATION_DATE_TIME_COMPARATOR));

	return files.get(0);
    }

    public static List<SIBSOutgoingPaymentFile> readGeneratedPaymentFiles() {
	return new ArrayList<SIBSOutgoingPaymentFile>(subjectExecutionYear().getSIBSOutgoingPaymentFilesSet());
    }

    @Service
    public void markAsSuccessfulSent(DateTime dateTime) {
	setSuccessfulSentDate(dateTime);
    }

    private class CalculatePaymentCodes extends Thread {
	private String eventExternalId;
	private StringBuilder errorsBuilder;
	private SibsOutgoingPaymentFile sibsFile;

	public CalculatePaymentCodes(String eventExternalId, StringBuilder errorsBuilder, SibsOutgoingPaymentFile sibsFile) {
	    this.eventExternalId = eventExternalId;
	    this.errorsBuilder = errorsBuilder;
	    this.sibsFile = sibsFile;
	}

	@Override
	public void run() {
	    try {
		pt.ist.fenixWebFramework.services.ServiceManager.enterAnnotationService();

		pt.ist.fenixframework.pstm.Transaction.withTransaction(new jvstm.TransactionalCommand() {

		    @Override
		    public void doIt() {
			Event event = Event.fromExternalId(eventExternalId);

			for (final AccountingEventPaymentCode paymentCode : event.calculatePaymentCodes()) {
			    sibsFile.addLine(paymentCode.getCode(), paymentCode.getMinAmount(), paymentCode.getMaxAmount(),
				    paymentCode.getStartDate(), paymentCode.getEndDate());
			}
		    }
		});
	    } catch (Throwable e) {
		appendToErrors(errorsBuilder, eventExternalId, e);
	    } finally {
		pt.ist.fenixWebFramework.services.ServiceManager.exitAnnotationService();
	    }
	}
    }
}
