package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.DegreeCandidacyForGraduatedPersonCandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.Over23CandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.SecondCycleCandidacyPeriod;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.util.PeriodState;

import org.joda.time.YearMonthDay;

abstract public class ExecutionInterval extends ExecutionInterval_Base {

    protected ExecutionInterval() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setState(PeriodState.NOT_OPEN);
    }

    @Override
    public void setAcademicInterval(AcademicInterval academicInterval) {
	if (academicInterval == null) {
	    throw new DomainException("error.executionInterval.empty.executionInterval");
	}
	super.setAcademicInterval(academicInterval);
    }

    @Override
    public void setState(PeriodState state) {
	if (state == null) {
	    throw new DomainException("error.executionInterval.empty.state");
	}
	super.setState(state);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
	final YearMonthDay start = getBeginDateYearMonthDay();
	final YearMonthDay end = getEndDateYearMonthDay();
	return start != null && end != null && start.isBefore(end);
    }

    public List<? extends CandidacyPeriod> getCandidacyPeriods(final Class<? extends CandidacyPeriod> clazz) {
	final List<CandidacyPeriod> result = new ArrayList<CandidacyPeriod>();
	for (final CandidacyPeriod candidacyPeriod : getCandidacyPeriods()) {
	    if (candidacyPeriod.getClass().equals(clazz)) {
		result.add(candidacyPeriod);
	    }
	}
	return result;
    }

    public boolean hasCandidacyPeriods(final Class<? extends CandidacyPeriod> clazz) {
	for (final CandidacyPeriod candidacyPeriod : getCandidacyPeriods()) {
	    if (candidacyPeriod.getClass().equals(clazz)) {
		return true;
	    }
	}
	return false;
    }

    public Over23CandidacyPeriod getOver23CandidacyPeriod() {
	final List<Over23CandidacyPeriod> candidacyPeriods = (List<Over23CandidacyPeriod>) getCandidacyPeriods(Over23CandidacyPeriod.class);
	return candidacyPeriods.isEmpty() ? null : candidacyPeriods.get(0);
    }

    public boolean hasOver23CandidacyPeriod() {
	return getOver23CandidacyPeriod() != null;
    }

    public SecondCycleCandidacyPeriod getSecondCycleCandidacyPeriod() {
	final List<SecondCycleCandidacyPeriod> candidacyPeriods = (List<SecondCycleCandidacyPeriod>) getCandidacyPeriods(SecondCycleCandidacyPeriod.class);
	return candidacyPeriods.isEmpty() ? null : candidacyPeriods.get(0);
    }

    public boolean hasSecondCycleCandidacyPeriod() {
	return getSecondCycleCandidacyPeriod() != null;
    }

    public DegreeCandidacyForGraduatedPersonCandidacyPeriod getDegreeCandidacyForGraduatedPersonCandidacyPeriod() {
	final List<DegreeCandidacyForGraduatedPersonCandidacyPeriod> candidacyPeriods = (List<DegreeCandidacyForGraduatedPersonCandidacyPeriod>) getCandidacyPeriods(DegreeCandidacyForGraduatedPersonCandidacyPeriod.class);
	return candidacyPeriods.isEmpty() ? null : candidacyPeriods.get(0);
    }

    public boolean hasDegreeCandidacyForGraduatedPersonCandidacyPeriod() {
	return getDegreeCandidacyForGraduatedPersonCandidacyPeriod() != null;
    }

    static public List<ExecutionInterval> readExecutionIntervalsWithCandidacyPeriod(final Class<? extends CandidacyPeriod> clazz) {
	final List<ExecutionInterval> result = new ArrayList<ExecutionInterval>();
	for (final ExecutionInterval executionInterval : RootDomainObject.getInstance().getExecutionIntervals()) {
	    if (executionInterval.hasCandidacyPeriods(clazz)) {
		result.add(executionInterval);
	    }
	}
	return result;
    }
}
