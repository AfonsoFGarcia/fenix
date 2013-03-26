package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;

public class Over23IndividualCandidacyResultBean implements Serializable {

    private Over23IndividualCandidacyProcess candidacyProcess;

    private IndividualCandidacyState state;

    private Degree acceptedDegree;

    public Over23IndividualCandidacyResultBean(final Over23IndividualCandidacyProcess candidacyProcess) {
        setCandidacyProcess(candidacyProcess);
        if (candidacyProcess.isCandidacyAccepted()) {
            setState(candidacyProcess.getCandidacyState());
            setAcceptedDegree(candidacyProcess.getAcceptedDegree());
        } else if (candidacyProcess.isCandidacyRejected()) {
            setState(candidacyProcess.getCandidacyState());
        }
    }

    public Over23IndividualCandidacyProcess getCandidacyProcess() {
        return this.candidacyProcess;
    }

    public void setCandidacyProcess(final Over23IndividualCandidacyProcess candidacyProcess) {
        this.candidacyProcess = candidacyProcess;
    }

    public IndividualCandidacyState getState() {
        return state;
    }

    public void setState(IndividualCandidacyState state) {
        this.state = state;
    }

    public Degree getAcceptedDegree() {
        return this.acceptedDegree;
    }

    public void setAcceptedDegree(final Degree acceptedDegree) {
        this.acceptedDegree = acceptedDegree;
    }

    public boolean isValid() {
        return getState() != null;
    }
}