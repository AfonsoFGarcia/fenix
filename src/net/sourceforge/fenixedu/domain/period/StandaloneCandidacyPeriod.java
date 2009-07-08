package net.sourceforge.fenixedu.domain.period;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.candidacyProcess.standalone.StandaloneCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class StandaloneCandidacyPeriod extends StandaloneCandidacyPeriod_Base {

    private StandaloneCandidacyPeriod() {
	super();
    }

    public StandaloneCandidacyPeriod(final StandaloneCandidacyProcess candidacyProcess, final ExecutionSemester executionSemester,
	    final DateTime start, final DateTime end) {
	this();
	init(candidacyProcess, executionSemester, start, end);
    }

    private void init(final StandaloneCandidacyProcess candidacyProcess, final ExecutionInterval executionInterval,
	    final DateTime start, final DateTime end) {
	checkParameters(candidacyProcess);
	checkIfCanCreate(executionInterval, start, end);
	super.init(executionInterval, start, end);
	addCandidacyProcesses(candidacyProcess);
    }

    private void checkParameters(final StandaloneCandidacyProcess candidacyProcess) {
	if (candidacyProcess == null) {
	    throw new DomainException("error.StandaloneCandidacyProcess.invalid.candidacy.process");
	}
    }

    private void checkIfCanCreate(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	for (final StandaloneCandidacyPeriod candidacyPeriod : executionInterval.getStandaloneCandidacyPeriods()) {
	    if (candidacyPeriod.intercept(start, end)) {
		throw new DomainException("error.StandaloneCandidacyPeriod.interception", executionInterval.getName(), start
			.toString("dd/MM/yyyy HH:mm"), end.toString("dd/MM/yyyy HH:mm"));
	    }
	}
    }

    public boolean intercept(final DateTime start, final DateTime end) {
	if (start.isAfter(end)) {
	    throw new DomainException("error.CandidacyPeriod.begin.cannot.be.after.end");
	}
	return contains(start) || contains(end);
    }

    public StandaloneCandidacyProcess getStandaloneCandidacyProcess() {
	return (StandaloneCandidacyProcess) (hasAnyCandidacyProcesses() ? getCandidacyProcesses().get(0) : null);
    }

    @Override
    public ExecutionSemester getExecutionInterval() {
	return (ExecutionSemester) super.getExecutionInterval();
    }

    public String getPresentationName() {
	return getStart().toString("dd/MM/yyyy") + " - " + getEnd().toString("dd/MM/yyyy");
    }

    @Override
    public void edit(final DateTime start, final DateTime end) {
	checkDates(start, end);
	checkIfCandEdit(start, end);
	super.setStart(start);
	super.setEnd(end);
    }

    private void checkIfCandEdit(DateTime start, DateTime end) {
	for (final StandaloneCandidacyPeriod StandaloneCandidacyPeriod : getExecutionInterval().getStandaloneCandidacyPeriods()) {
	    if (StandaloneCandidacyPeriod != this && StandaloneCandidacyPeriod.intercept(start, end)) {
		throw new DomainException("error.StandaloneCandidacyPeriod.interception", getExecutionInterval().getName(), start
			.toString("dd/MM/yyyy HH:mm"), end.toString("dd/MM/yyyy HH:mm"));
	    }
	}
    }

}
