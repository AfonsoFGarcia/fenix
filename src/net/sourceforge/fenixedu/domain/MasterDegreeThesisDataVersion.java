/*
 * Created on Oct 10, 2003
 *
 *
 */
package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.List;

import net.sourceforge.fenixedu.util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class MasterDegreeThesisDataVersion extends MasterDegreeThesisDataVersion_Base {
    private List externalAssistentGuiders;
    private Timestamp lastModification;

    public MasterDegreeThesisDataVersion() {
    }

    public MasterDegreeThesisDataVersion(IMasterDegreeThesis masterDegreeThesis,
            IEmployee responsibleEmployee, String dissertationTitle, Timestamp lastModification,
            State currentState) {
        this.setMasterDegreeThesis(masterDegreeThesis);
        this.setResponsibleEmployee(responsibleEmployee);
        this.setDissertationTitle(dissertationTitle);
        this.setLastModification(lastModification);
        this.setCurrentState(currentState);
    }

    /**
     * @param masterDegreeThesis
     * @param externalAssistentGuiders
     * @param assistentGuiders
     * @param guiders
     * @param responsibleEmployee
     * @param dissertationTitle
     * @param lastModification
     * @param currentState
     */
    public MasterDegreeThesisDataVersion(IMasterDegreeThesis masterDegreeThesis,
            List externalAssistentGuiders, List assistentGuiders, List guiders,
            IEmployee responsibleEmployee, String dissertationTitle, Timestamp lastModification,
            State currentState) {
        this.setMasterDegreeThesis(masterDegreeThesis);
        this.setExternalAssistentGuiders(externalAssistentGuiders);
        this.setAssistentGuiders(assistentGuiders);
        this.setGuiders(guiders);
        this.setResponsibleEmployee(responsibleEmployee);
        this.setDissertationTitle(dissertationTitle);
        this.setLastModification(lastModification);
        this.setCurrentState(currentState);
    }

    public void setExternalAssistentGuiders(List externalAssistentGuiders) {
        this.externalAssistentGuiders = externalAssistentGuiders;
    }

    public List getExternalAssistentGuiders() {
        return externalAssistentGuiders;
    }

    public void setLastModification(Timestamp lastModification) {
        this.lastModification = lastModification;
    }

    public Timestamp getLastModification() {
        return lastModification;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": \n";
        result += "idInternal = " + this.getIdInternal() + "; \n";
        result += "masterDegreeThesis = " + this.getMasterDegreeThesis().getIdInternal() + "; \n";
        result += "externalAssistentGuiders = " + this.getExternalAssistentGuiders().toString() + "; \n";
        result += "assistentGuiders = " + this.getAssistentGuiders().toString() + "; \n";
        result += "guiders = " + this.getGuiders().toString() + "; \n";
        result += "responsibleEmployee = " + this.getResponsibleEmployee().getIdInternal() + "; \n";
        result += "dissertationTitle = " + this.getDissertationTitle().toString() + "; \n";
        result += "lastModification = " + this.getLastModification().toString() + "; \n";
        result += "currentState = " + this.getCurrentState().toString() + "; \n";
        result += "] \n";

        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IMasterDegreeThesisDataVersion) {
            IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion = (IMasterDegreeThesisDataVersion) obj;
            return masterDegreeThesisDataVersion.getIdInternal().equals(getIdInternal());
        }

        return false;
    }

}