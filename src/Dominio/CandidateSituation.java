/*
 * CandidateSituation.java
 *
 * Created on 1 de Novembro de 2002, 15:25
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package Dominio;

import java.util.Date;

import Util.CandidateSituationValidation;
import Util.SituationName;

public class CandidateSituation implements ICandidateSituation {
 
    private Date date = null;                        // Candidate Situation Date
    private String remarks = null;              // Candidate Situation Remarks
    private CandidateSituationValidation validation = null;               // Candidate Situation Validation
	private SituationName situation = null;    // Candidate Situation  

    private IMasterDegreeCandidate masterDegreeCandidate = null;    // Instance from MasterDegreeCandidate
    
    // Internal Keys in Tables
    private Integer internalCode;      // Internal Code for CandidateSituation
    private Integer candidateKey;      // Internal Code for MasterDegreeCandidate
    private Integer situationKey;      // Internal Code for Situation
    
    public CandidateSituation() {
        masterDegreeCandidate = null;
        situation = null;
        date = null;
        remarks = null;
        validation = null; 
    } 
    
    public CandidateSituation(Date date, String remarks, CandidateSituationValidation validation,
           IMasterDegreeCandidate masterDegreeCandidate, SituationName situation) {
        setMasterDegreeCandidate(masterDegreeCandidate);
        setSituation(situation);
        setDate(date);
        setRemarks(remarks); 
        setValidation(validation);
    }

    public boolean equals(Object o) {
        return
        ((o instanceof CandidateSituation) &&
        (date.getTime() == (((CandidateSituation)o).getDate().getTime())) &&
        (remarks.equals((((CandidateSituation)o).getRemarks()))) &&
        (validation.equals((((CandidateSituation)o).getValidation()))));

    }

    public String toString() {
        String result = "Candidate Situation:\n";
        result += "\n  - Internal Code : " + internalCode;
        result += "\n  - Date : " + date;
        result += "\n  - Remarks : " + remarks;
        result += "\n  - Validation : " + validation;
        result += "\n  - Master Degree Candidate : " + masterDegreeCandidate;
        result += "\n  - Situation : " + situation;
        
        return result;
    }
    


	/**
	 * Returns the candidateKey.
	 * @return Integer
	 */
	public Integer getCandidateKey() {
		return candidateKey;
	}

	/**
	 * Returns the date.
	 * @return Date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Returns the internalCode.
	 * @return Integer
	 */
	public Integer getInternalCode() {
		return internalCode;
	}

	/**
	 * Returns the masterDegreeCandidate.
	 * @return IMasterDegreeCandidate
	 */
	public IMasterDegreeCandidate getMasterDegreeCandidate() {
		return masterDegreeCandidate;
	}

	/**
	 * Returns the remarks.
	 * @return String
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * Returns the situation.
	 * @return SituationName
	 */
	public SituationName getSituation() {
		return situation;
	}

	/**
	 * Returns the situationKey.
	 * @return Integer
	 */
	public Integer getSituationKey() {
		return situationKey;
	}

	/**
	 * Returns the validation.
	 * @return CandidateSituationValidation
	 */
	public CandidateSituationValidation getValidation() {
		return validation;
	}

	/**
	 * Sets the candidateKey.
	 * @param candidateKey The candidateKey to set
	 */
	public void setCandidateKey(Integer candidateKey) {
		this.candidateKey = candidateKey;
	}

	/**
	 * Sets the date.
	 * @param date The date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Sets the internalCode.
	 * @param internalCode The internalCode to set
	 */
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}

	/**
	 * Sets the masterDegreeCandidate.
	 * @param masterDegreeCandidate The masterDegreeCandidate to set
	 */
	public void setMasterDegreeCandidate(IMasterDegreeCandidate masterDegreeCandidate) {
		this.masterDegreeCandidate = masterDegreeCandidate;
	}

	/**
	 * Sets the remarks.
	 * @param remarks The remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * Sets the situation.
	 * @param situation The situation to set
	 */
	public void setSituation(SituationName situation) {
		this.situation = situation;
	}

	/**
	 * Sets the situationKey.
	 * @param situationKey The situationKey to set
	 */
	public void setSituationKey(Integer situationKey) {
		this.situationKey = situationKey;
	}

	/**
	 * Sets the validation.
	 * @param validation The validation to set
	 */
	public void setValidation(CandidateSituationValidation validation) {
		this.validation = validation;
	}

} // End of class definition
