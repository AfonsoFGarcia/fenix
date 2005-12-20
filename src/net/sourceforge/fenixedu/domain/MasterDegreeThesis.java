/*
 * Created on 9/Out/2003
 *
 *
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class MasterDegreeThesis extends MasterDegreeThesis_Base {

    public String toString() {
        String result = "[" + this.getClass().getName() + ": \n";
        result += "idInternal = " + getIdInternal() + "; \n";
        result += "studentCurricularPlan = " + getStudentCurricularPlan().getIdInternal() + "; \n";
        result += "masterDegreeProofVersions = " + getMasterDegreeProofVersions().toString() + "; \n";
        result += "masterDegreeThesisDataVersions = " + getMasterDegreeThesisDataVersions().toString()
                + "; \n";
        result += "] \n";

        return result;
    }

    public IMasterDegreeProofVersion getActiveMasterDegreeProofVersion() {
        IMasterDegreeProofVersion activeMasterDegreeProofVersion = null;

        for (IMasterDegreeProofVersion candidateMasterDegreeProofVersion : this
                .getMasterDegreeProofVersions()) {
            if (candidateMasterDegreeProofVersion.getCurrentState().getState() == State.ACTIVE) {
                activeMasterDegreeProofVersion = candidateMasterDegreeProofVersion;
                break;
            }
        }

        return activeMasterDegreeProofVersion;
    }

}
