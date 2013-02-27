package net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityProgram;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;

import org.joda.time.DateTime;

public class OutboundMobilityContextBean implements Serializable {

    private ExecutionYear executionYear;
    private SortedSet<OutboundMobilityCandidacyPeriod> candidacyPeriods = new TreeSet<OutboundMobilityCandidacyPeriod>();
    private SortedSet<MobilityProgram> mobilityPrograms = new TreeSet<MobilityProgram>();
    private SortedSet<OutboundMobilityCandidacyContestGroup> mobilityGroups = new TreeSet<OutboundMobilityCandidacyContestGroup>();

    private DateTime startDateTime;
    private DateTime endDateTime;

    private MobilityProgram mobilityProgram;
    private ExecutionDegree executionDegree;
    private UniversityUnit unit;
    private Integer vacancies;
    private Person person;

    public OutboundMobilityContextBean() {
        setExecutionYear(ExecutionYear.readCurrentExecutionYear());

        OutboundMobilityCandidacyPeriod last = null;
        for (final CandidacyPeriod candidacyPeriod : executionYear.getCandidacyPeriodsSet()) {
            if (candidacyPeriod instanceof OutboundMobilityCandidacyPeriod) {
                final OutboundMobilityCandidacyPeriod outboundMobilityCandidacyPeriod =
                        (OutboundMobilityCandidacyPeriod) candidacyPeriod;
                if (last == null || last.getStart().isBefore(outboundMobilityCandidacyPeriod.getStart())) {
                    last = outboundMobilityCandidacyPeriod;
                }
            }
        }
        if (last != null) {
            candidacyPeriods.add(last);
            // mobilityGroups.addAll(last.getOutboundMobilityCandidacyContestGroupSet());
        }
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(final ExecutionYear executionYear) {
        if (executionYear != this.executionYear) {
            this.executionYear = executionYear;
            candidacyPeriods.clear();
            getPossibleCandidacyPeriods(candidacyPeriods);
        }
    }

    public void getPossibleCandidacyPeriods(final SortedSet<OutboundMobilityCandidacyPeriod> candidacyPeriods) {
        if (executionYear != null) {
            for (final CandidacyPeriod candidacyPeriod : executionYear.getCandidacyPeriodsSet()) {
                if (candidacyPeriod instanceof OutboundMobilityCandidacyPeriod) {
                    final OutboundMobilityCandidacyPeriod outboundMobilityCandidacyPeriod =
                            (OutboundMobilityCandidacyPeriod) candidacyPeriod;
                    candidacyPeriods.add(outboundMobilityCandidacyPeriod);
                }
            }
        }
    }

    public SortedSet<OutboundMobilityCandidacyPeriod> getCandidacyPeriods() {
        return candidacyPeriods;
    }

    public List<OutboundMobilityCandidacyPeriod> getCandidacyPeriodsAsList() {
        return new ArrayList<OutboundMobilityCandidacyPeriod>(candidacyPeriods);
    }

    public void setCandidacyPeriodsAsList(List<OutboundMobilityCandidacyPeriod> candidacyPeriodsAsList) {
        this.candidacyPeriods.retainAll(candidacyPeriodsAsList);
        this.candidacyPeriods.addAll(candidacyPeriodsAsList);
    }

    public SortedSet<OutboundMobilityCandidacyContestGroup> getMobilityGroups() {
        return mobilityGroups;
    }

    public List<OutboundMobilityCandidacyContestGroup> getMobilityGroupsAsList() {
        return new ArrayList<OutboundMobilityCandidacyContestGroup>(mobilityGroups);
    }

    public void setMobilityGroupsAsList(List<OutboundMobilityCandidacyContestGroup> mobilityGroupsAsList) {
        this.mobilityGroups.retainAll(mobilityGroupsAsList);
        this.mobilityGroups.addAll(mobilityGroupsAsList);
    }

    public SortedSet<MobilityProgram> getMobilityPrograms() {
        return mobilityPrograms;
    }

    public List<MobilityProgram> getMobilityProgramsAsList() {
        return new ArrayList<MobilityProgram>(mobilityPrograms);
    }

    public void setMobilityProgramsAsList(List<MobilityProgram> mobilityProgramsAsList) {
        this.mobilityPrograms.retainAll(mobilityProgramsAsList);
        this.mobilityPrograms.addAll(mobilityProgramsAsList);
    }

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(DateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(DateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void createNewOutboundMobilityCandidacyPeriod() {
        final OutboundMobilityCandidacyPeriod candidacyPeriod =
                OutboundMobilityCandidacyPeriod.create(getExecutionYear(), getStartDateTime(), getEndDateTime());
        candidacyPeriods.add(candidacyPeriod);
    }

    public void createNewOutboundMobilityCandidacyContest() {
        for (final OutboundMobilityCandidacyPeriod candidacyPeriod : candidacyPeriods) {
            if (executionDegree == null) {
                for (final OutboundMobilityCandidacyContestGroup mobilityGroup : mobilityGroups) {
                    candidacyPeriod.createOutboundMobilityCandidacyContest(mobilityGroup, mobilityProgram, unit, vacancies);
                }
            } else {
                candidacyPeriod.createOutboundMobilityCandidacyContest(executionDegree, mobilityProgram, unit, vacancies);
            }
        }
        executionDegree = null;
        unit = null;
        vacancies = null;
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    public UniversityUnit getUnit() {
        return unit;
    }

    public void setUnit(UniversityUnit unit) {
        this.unit = unit;
    }

    public Integer getVacancies() {
        return vacancies;
    }

    public void setVacancies(Integer vacancies) {
        this.vacancies = vacancies;
    }

    public MobilityProgram getMobilityProgram() {
        return mobilityProgram;
    }

    public void setMobilityProgram(MobilityProgram mobilityProgram) {
        this.mobilityProgram = mobilityProgram;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public SortedSet<OutboundMobilityCandidacyContest> getOutboundMobilityCandidacyContest() {
        final SortedSet<OutboundMobilityCandidacyContest> result = new TreeSet<OutboundMobilityCandidacyContest>();
        for (final OutboundMobilityCandidacyPeriod candidacyPeriod : candidacyPeriods) {
            for (final OutboundMobilityCandidacyContest contest : candidacyPeriod.getOutboundMobilityCandidacyContestSet()) {
                if (mobilityPrograms.contains(contest.getMobilityAgreement().getMobilityProgram())) {
                    final OutboundMobilityCandidacyContestGroup mobilityGroup = contest.getOutboundMobilityCandidacyContestGroup();
                    if (mobilityGroups.contains(mobilityGroup)) {
                        result.add(contest);
                    }
                }
            }
        }
        return result;
    }

    public void addDegreeToGroup() {
        for (final OutboundMobilityCandidacyContestGroup mobilityGroup : mobilityGroups) {
            mobilityGroup.addExecutionDegreeService(executionDegree);
        }
    }

    public void removeDegreeFromGroup() {
        for (final OutboundMobilityCandidacyContestGroup mobilityGroup : mobilityGroups) {
            mobilityGroup.removeExecutionDegreeService(executionDegree);
        }
    }

    public void addMobilityCoordinator() {
        if (person != null) {
            for (final OutboundMobilityCandidacyContestGroup mobilityGroup : mobilityGroups) {
                mobilityGroup.addMobilityCoordinatorService(person);
            }
        }
    }

}
