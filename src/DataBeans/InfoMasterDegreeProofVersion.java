/*
 * Created on Oct 14, 2003
 *
 */
package DataBeans;

import java.sql.Timestamp;
import java.util.Date;

import Util.MasterDegreeClassification;
import Util.State;

/**
 * @author :
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */
public class InfoMasterDegreeProofVersion extends InfoObject {

	private InfoMasterDegreeThesis infoMasterDegreeThesis;
	private InfoEmployee infoResponsibleEmployee;
	private Timestamp lastModification;
	private Date proofDate;
	private Date thesisDeliveryDate;
	private MasterDegreeClassification finalResult;
	private Integer attachedCopiesNumber;
	private State currentState;

	public String toString() {
		String result = "[" + this.getClass().getName() + ": \n";
		result += "idInternal = " + getIdInternal() + "; \n";
		result += "infoMasterDegreeThesis = " + this.infoMasterDegreeThesis.getIdInternal() + "; \n";
		result += "infoResponsibleEmployee = " + this.infoResponsibleEmployee.getIdInternal() + "; \n";
		result += "lastModification = " + this.lastModification.toString() + "; \n";
		result += "proofDate = " + this.proofDate.toString() + "; \n";
		result += "thesisDeliveryDate = " + this.thesisDeliveryDate.toString() + "; \n";
		result += "finalResult = " + this.finalResult.toString() + "; \n";
		result += "attachedCopiesNumber = " + this.attachedCopiesNumber.toString() + "; \n";
		result += "currentState = " + this.currentState.toString() + "; \n";
		result += "] \n";

		return result;
	}
	
	public boolean equals(Object obj) {
		boolean result = false;

		if (obj instanceof InfoMasterDegreeProofVersion) {
			InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = (InfoMasterDegreeProofVersion) obj;
			result =
				this.getInfoMasterDegreeThesis().equals(infoMasterDegreeProofVersion.getInfoMasterDegreeThesis())
					&& this.getLastModification().equals(infoMasterDegreeProofVersion.getLastModification());
		}
		return result;
	}

	public void setAttachedCopiesNumber(Integer attachedCopiesNumber) {
		this.attachedCopiesNumber = attachedCopiesNumber;
	}

	public Integer getAttachedCopiesNumber() {
		return attachedCopiesNumber;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setFinalResult(MasterDegreeClassification finalResult) {
		this.finalResult = finalResult;
	}

	public MasterDegreeClassification getFinalResult() {
		return finalResult;
	}

	public void setInfoMasterDegreeThesis(InfoMasterDegreeThesis infoMasterDegreeThesis) {
		this.infoMasterDegreeThesis = infoMasterDegreeThesis;
	}

	public InfoMasterDegreeThesis getInfoMasterDegreeThesis() {
		return infoMasterDegreeThesis;
	}

	public void setInfoResponsibleEmployee(InfoEmployee infoResponsibleEmployee) {
		this.infoResponsibleEmployee = infoResponsibleEmployee;
	}

	public InfoEmployee getInfoResponsibleEmployee() {
		return infoResponsibleEmployee;
	}

	public void setLastModification(Timestamp lastModification) {
		this.lastModification = lastModification;
	}

	public Timestamp getLastModification() {
		return lastModification;
	}

	public void setProofDate(Date proofDate) {
		this.proofDate = proofDate;
	}

	public Date getProofDate() {
		return proofDate;
	}

	public void setThesisDeliveryDate(Date thesisDeliveryDate) {
		this.thesisDeliveryDate = thesisDeliveryDate;
	}

	public Date getThesisDeliveryDate() {
		return thesisDeliveryDate;
	}

}
