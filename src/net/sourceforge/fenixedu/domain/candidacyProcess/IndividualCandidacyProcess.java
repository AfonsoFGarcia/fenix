package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.Comparator;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

abstract public class IndividualCandidacyProcess extends IndividualCandidacyProcess_Base {

    static final public Comparator<IndividualCandidacyProcess> COMPARATOR_BY_CANDIDACY_PERSON = new Comparator<IndividualCandidacyProcess>() {
	public int compare(IndividualCandidacyProcess o1, IndividualCandidacyProcess o2) {
	    return IndividualCandidacyPersonalDetails.COMPARATOR_BY_NAME_AND_ID.compare(o1.getPersonalDetails(), o2
		    .getPersonalDetails());
	}
    };

    protected IndividualCandidacyProcess() {
	super();
    }

    public ExecutionInterval getCandidacyExecutionInterval() {
	return hasCandidacyProcess() ? getCandidacyProcess().getCandidacyExecutionInterval() : null;
    }

    public boolean isFor(final ExecutionInterval executionInterval) {
	return getCandidacyExecutionInterval() == executionInterval;
    }

    public DateTime getCandidacyStart() {
	return hasCandidacyProcess() ? getCandidacyProcess().getCandidacyStart() : null;
    }

    public DateTime getCandidacyEnd() {
	return hasCandidacyProcess() ? getCandidacyProcess().getCandidacyEnd() : null;
    }

    public LocalDate getCandidacyDate() {
	return getCandidacy().getCandidacyDate();
    }

    public boolean hasOpenCandidacyPeriod() {
	return hasCandidacyProcess() && getCandidacyProcess().hasOpenCandidacyPeriod();
    }

    public boolean hasOpenCandidacyPeriod(final DateTime date) {
	return hasCandidacyProcess() && getCandidacyProcess().hasOpenCandidacyPeriod(date);
    }

    public CandidacyProcessState getState() {
	return hasCandidacyProcess() ? getCandidacyProcess().getState() : null;
    }

    public boolean isInStandBy() {
	return hasCandidacyProcess() && getCandidacyProcess().isInStandBy();
    }

    public boolean isSentToJury() {
	return hasCandidacyProcess() && getCandidacyProcess().isSentToJury();
    }

    public boolean isSentToCoordinator() {
	return hasCandidacyProcess() && getCandidacyProcess().isSentToCoordinator();
    }

    public boolean isSentToScientificCouncil() {
	return hasCandidacyProcess() && getCandidacyProcess().isSentToScientificCouncil();
    }

    public boolean isPublished() {
	return hasCandidacyProcess() && getCandidacyProcess().isPublished();
    }

    protected boolean hasAnyPaymentForCandidacy() {
	return getCandidacy().hasAnyPayment();
    }

    protected void cancelCandidacy(final Person person) {
	getCandidacy().cancel(person);
    }

    public IndividualCandidacyState getCandidacyState() {
	return getCandidacy().getState();
    }

    public boolean isCandidacyValid() {
	return !isCandidacyCancelled() && isCandidacyDebtPayed();
    }

    public boolean isCandidacyInStandBy() {
	return getCandidacy().isInStandBy();
    }

    public boolean isCandidacyAccepted() {
	return getCandidacy().isAccepted();
    }

    public boolean isCandidacyRejected() {
	return getCandidacy().isRejected();
    }

    public boolean isCandidacyCancelled() {
	return getCandidacy().isCancelled();
    }

    public boolean isCandidacyDebtPayed() {
	return getCandidacy().isDebtPayed();
    }

    public IndividualCandidacyPersonalDetails getPersonalDetails() {
	return getCandidacy().getPersonalDetails();
    }

    public boolean hasCandidacyPerson() {
	return getPersonalDetails() != null;
    }

    public Student getCandidacyStudent() {
	return getPersonalDetails().getStudent();
    }

    public boolean hasCandidacyStudent() {
	return getCandidacyStudent() != null;
    }

    public Registration getCandidacyRegistration() {
	return getCandidacy().getRegistration();
    }

    public boolean hasRegistrationForCandidacy() {
	return getCandidacy().hasRegistration();
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/CaseHandlingResources").getString(getClass().getSimpleName());
    }

    protected void editPersonalCandidacyInformation(final PersonBean personBean) {
	getCandidacy().editPersonalCandidacyInformation(personBean);
    }

    public CandidacyInformationBean getCandidacyInformationBean() {
	return hasCandidacy() ? getCandidacy().getCandidacyInformationBean() : null;
    }

    protected void editCommonCandidacyInformation(final CandidacyInformationBean bean) {
	getCandidacy().editCandidacyInformation(bean);
    }

}
