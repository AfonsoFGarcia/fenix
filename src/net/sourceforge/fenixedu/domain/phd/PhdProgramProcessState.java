package net.sourceforge.fenixedu.domain.phd;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.CancelledCandidacySituation;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituation;
import net.sourceforge.fenixedu.domain.candidacy.NotAdmittedCandidacySituation;
import net.sourceforge.fenixedu.domain.candidacy.RegisteredCandidacySituation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.candidacy.PHDProgramCandidacy;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;

import org.joda.time.DateTime;

public class PhdProgramProcessState extends PhdProgramProcessState_Base {

    private PhdProgramProcessState() {
	super();
    }

    public PhdProgramProcessState(final PhdIndividualProgramProcess process, final PhdIndividualProgramProcessState type,
	    final Person person, final String remarks, final DateTime stateDate) {
	this();
	init(process, type, person, remarks, stateDate);
	
	updateSituationOnPHDCandidacy();
    }

    public void updateSituationOnPHDCandidacy() {
	final PhdIndividualProgramProcess process = getProcess();

	if (this.getStateDate() == null) {
	    throw new DomainException("state.date.null");
	}

	PHDProgramCandidacy candidacy = process.getCandidacyProcess().getCandidacy();
	CandidacySituation situation = null;

	switch (this.getType()) {
	case WORK_DEVELOPMENT:
	    situation = new RegisteredCandidacySituation(candidacy);
	    break;
	case NOT_ADMITTED:
	    situation = new NotAdmittedCandidacySituation(candidacy);
	    break;
	case CANCELLED:
	    situation = new CancelledCandidacySituation(candidacy);
	    break;
	default:
	}

	if (situation != null) {
	    situation.setSituationDate(this.getStateDate());
	}
    }

    protected void init(final Person person, final String remarks, DateTime stateDate) {
	throw new RuntimeException("invoke other init");
    }

    private void init(PhdIndividualProgramProcess process, PhdIndividualProgramProcessState type, Person person, String remarks,
	    final DateTime stateDate) {
	check(process, type);
	setProcess(process);

	super.init(person, remarks, stateDate);
	setType(type);
    }

    private void check(PhdIndividualProgramProcess process, PhdIndividualProgramProcessState type) {
	check(process, "error.PhdProgramProcessState.invalid.process");
	check(type, "error.PhdProgramProcessState.invalid.type");
	checkType(process, type);
    }

    private void checkType(final PhdIndividualProgramProcess process, final PhdIndividualProgramProcessState type) {
	final PhdIndividualProgramProcessState currentType = process.getActiveState();
	if (currentType != null && currentType.equals(type)) {
	    throw new DomainException("error.PhdProgramProcessState.equals.previous.state");
	}
    }

    @Override
    protected void disconnect() {
	removeProcess();
	super.disconnect();
    }

    public boolean isFlunked() {
	return PhdIndividualProgramProcessState.FLUNKED.equals(getType());
    }

    public boolean isSuspended() {
	return PhdIndividualProgramProcessState.SUSPENDED.equals(getType());
    }

    public boolean isCanceled() {
	return PhdIndividualProgramProcessState.CANCELLED.equals(getType());
    }

    public boolean isConcluded() {
	return PhdIndividualProgramProcessState.CONCLUDED.equals(getType());
    }

    public boolean isTransfered() {
	return PhdIndividualProgramProcessState.TRANSFERRED.equals(getType());
    }

    public boolean isNotAdmited() {
	return PhdIndividualProgramProcessState.NOT_ADMITTED.equals(getType());
    }
    
    public boolean isCandidacy() {
	return PhdIndividualProgramProcessState.CANDIDACY.equals(getType());
    }

    @Override
    public void delete() {
	removeProcess();
	super.delete();
    }

    @Override
    public boolean isLast() {
	return getProcess().getMostRecentState() == this;
    }

    public static PhdProgramProcessState createWithInferredStateDate(final PhdIndividualProgramProcess process,
	    final PhdIndividualProgramProcessState type, final Person person, final String remarks) {
	DateTime stateDate = null;
	PhdProgramProcessState mostRecentState = process.getMostRecentState();
	
	
	switch (type) {
	case CANDIDACY:
	    if (process.getCandidacyProcess().getCandidacyDate() == null) {
		throw new PhdDomainOperationException("error.phd.PhdProgramProcessState.candidacyDate.required");
	    }

	    stateDate = process.getCandidacyProcess().getCandidacyDate().toDateTimeAtStartOfDay();
	    break;
	case WORK_DEVELOPMENT:

	    if (process.getMostRecentStateByType(PhdIndividualProgramProcessState.WORK_DEVELOPMENT) != null) {
		stateDate = mostRecentState.getStateDate().plusMinutes(1);
		break;
	    }

	    if (process.getWhenStartedStudies() == null) {
		throw new PhdDomainOperationException("error.phd.PhdProgramProcessState.whenStartedStudies.required");
	    }

	    stateDate = process.getWhenStartedStudies().toDateTimeAtStartOfDay();
	    break;
	case THESIS_DISCUSSION:

	    if (process.getMostRecentStateByType(PhdIndividualProgramProcessState.THESIS_DISCUSSION) != null) {
		stateDate = mostRecentState.getStateDate().plusMinutes(1);
		break;
	    }

	    if (process.getThesisProcess().getWhenThesisDiscussionRequired() == null) {
		throw new PhdDomainOperationException("error.phd.PhdProgramProcessState.whenThesisDiscussionRequired.required");
	    }

	    stateDate = process.getThesisProcess().getWhenThesisDiscussionRequired().toDateTimeAtStartOfDay();
	    break;
	case TRANSFERRED:
	    stateDate = mostRecentState.getStateDate().plusMinutes(1);
	    break;
	case CONCLUDED:
	    if (process.getLastConclusionProcess() == null) {
		throw new PhdDomainOperationException("error.phd.PhdProgramProcessState.conclusionProcess.required");
	    }

	    stateDate = process.getLastConclusionProcess().getWhenCreated();
	    break;
	case CANCELLED:
	case FLUNKED:
	case NOT_ADMITTED:
	case SUSPENDED:
	    stateDate = mostRecentState.getStateDate().plusMinutes(1);
	    break;
	default:
	    throw new DomainException("Cant handle this");
	}

	return createWithGivenStateDate(process, type, person, remarks, stateDate);
    }

    public static PhdProgramProcessState createWithGivenStateDate(final PhdIndividualProgramProcess process,
	    final PhdIndividualProgramProcessState type, final Person person, final String remarks, final DateTime stateDate) {
	List<PhdIndividualProgramProcessState> stateList = PhdIndividualProgramProcessState.getPossibleNextStates(process);

	if (!stateList.contains(type)) {
	    String expectedStateDescription = buildExpectedStatesDescription(stateList);

	    throw new PhdDomainOperationException("error.phd.PhdIndividualProgramProcess.invalid.next.state",
		    type.getLocalizedName(),
		    expectedStateDescription);
	}

	return new PhdProgramProcessState(process, type, person, remarks, stateDate);
    }
}
