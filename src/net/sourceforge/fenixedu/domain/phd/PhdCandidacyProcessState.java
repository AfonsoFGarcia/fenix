package net.sourceforge.fenixedu.domain.phd;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class PhdCandidacyProcessState extends PhdCandidacyProcessState_Base {

    private PhdCandidacyProcessState() {
	super();
    }

    protected PhdCandidacyProcessState(final PhdProgramCandidacyProcess process, final PhdProgramCandidacyProcessState type,
	    final Person person, final String remarks, final DateTime stateDate) {
	this();
	init(process, type, person, remarks, stateDate);
    }

    protected void init(final Person person, final String remarks, DateTime stateDate) {
	throw new RuntimeException("invoke other init");
    }

    protected void init(PhdProgramCandidacyProcess process, PhdProgramCandidacyProcessState type, Person person, String remarks,
	    final DateTime stateDate) {
	check(process, type);
	setProcess(process);
	super.init(person, remarks, stateDate);

	setType(type);
    }

    private void check(PhdProgramCandidacyProcess process, PhdProgramCandidacyProcessState type) {
	check(process, "error.PhdCandidacyProcessState.invalid.process");
	check(type, "error.PhdCandidacyProcessState.invalid.type");
	checkType(process, type);
    }

    private void checkType(final PhdProgramCandidacyProcess process, final PhdProgramCandidacyProcessState type) {
	final PhdProgramCandidacyProcessState currentType = process.getActiveState();
	if (currentType != null && currentType.equals(type)) {
	    throw new DomainException("error.PhdCandidacyProcessState.equals.previous.state");
	}
    }

    @Override
    protected void disconnect() {
	removeProcess();
	super.disconnect();
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    static public PhdCandidacyProcessState create(PhdProgramCandidacyProcess process, PhdProgramCandidacyProcessState type) {
	final PhdCandidacyProcessState result = new PhdCandidacyProcessState();

	result.check(process, type);
	result.setProcess(process);
	result.setType(type);

	return result;
    }

    @Override
    public boolean isLast() {
	return getProcess().getMostRecentState() == this;
    }

    public static PhdCandidacyProcessState createStateWithInferredStateDate(final PhdProgramCandidacyProcess process,
	    final PhdProgramCandidacyProcessState type, final Person person, final String remarks) {

	DateTime stateDate = null;

	PhdCandidacyProcessState mostRecentState = process.getMostRecentState();

	switch (type) {
	case PRE_CANDIDATE:
	case STAND_BY_WITH_MISSING_INFORMATION:
	case STAND_BY_WITH_COMPLETE_INFORMATION:
	case PENDING_FOR_COORDINATOR_OPINION:
	case WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION:
	    if (mostRecentState != null) {
		stateDate = mostRecentState.getStateDate().plusMinutes(1);
	    } else {
		stateDate = process.getCandidacyDate().toDateTimeAtStartOfDay();
	    }

	    break;
	case RATIFIED_BY_SCIENTIFIC_COUNCIL:
	    stateDate = process.getWhenRatified().toDateTimeAtStartOfDay();
	    break;
	case CONCLUDED:
	    stateDate = process.getWhenStartedStudies().toDateTimeAtStartOfDay();
	    break;
	case REJECTED:
	    stateDate = mostRecentState.getStateDate().plusMinutes(1);
	default:
	    throw new DomainException("I cant handle this");
	}

	return createStateWithGivenStateDate(process, type, person, remarks, stateDate);
    }

    public static PhdCandidacyProcessState createStateWithGivenStateDate(final PhdProgramCandidacyProcess process,
	    final PhdProgramCandidacyProcessState type, final Person person, final String remarks, final DateTime stateDate) {
	List<PhdProgramCandidacyProcessState> nextPossibleStates = PhdProgramCandidacyProcessState.getPossibleNextStates(process);

	if (!nextPossibleStates.contains(type)) {
	    throw new DomainException("error.phd.candidacy.PhdProgramCandidacyProcess.invalid.state");
	}

	return new PhdCandidacyProcessState(process, type, person, remarks, stateDate);
    }

    public static PhdCandidacyProcessState createStateForFixOnCandidacies(final DateTime whenCreated,
	    final PhdProgramCandidacyProcess process, final PhdProgramCandidacyProcessState type, final Person person,
	    final String remarks, final DateTime stateDate) {
	List<PhdProgramCandidacyProcessState> nextPossibleStates = PhdProgramCandidacyProcessState.getPossibleNextStates(process);

	PhdCandidacyProcessState phdCandidacyProcessState = new PhdCandidacyProcessState(process, type, person, remarks,
		stateDate);

	phdCandidacyProcessState.setWhenCreated(whenCreated);
	return phdCandidacyProcessState;
    }

}
