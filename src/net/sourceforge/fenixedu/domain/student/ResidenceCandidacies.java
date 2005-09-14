/*
 * Created on Aug 3, 2004
 *
 */
package net.sourceforge.fenixedu.domain.student;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ResidenceCandidacies extends ResidenceCandidacies_Base {

    public String toString() {
        String result = "ResidenceCandidancy :\n";
        result += "\n  - InternalId : " + getIdInternal();
        result += "\n  - Student : " + getStudent();
        result += "\n  - Creation Date : " + getCreationDate();
        result += "\n  - Observations : " + getObservations();
        result += "\n  - Candidate : " + getCandidate();
        return result;
    }

}
