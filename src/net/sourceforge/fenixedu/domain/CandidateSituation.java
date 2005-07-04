/*
 * CandidateSituation.java
 *
 * Created on 1 de Novembro de 2002, 15:25
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

public class CandidateSituation extends CandidateSituation_Base {

	public CandidateSituation() {
	}

	public CandidateSituation(Date date, String remarks, State validation,
			IMasterDegreeCandidate masterDegreeCandidate,
			SituationName situation) {
		setMasterDegreeCandidate(masterDegreeCandidate);
		setSituation(situation);
		setDate(date);
		setRemarks(remarks);
		setValidation(validation);
	}

	public String toString() {
		String result = "Candidate Situation:\n";
		result += "\n  - Internal Code : " + getIdInternal();
		result += "\n  - Date : " + getDate();
		result += "\n  - Remarks : " + getRemarks();
		result += "\n  - Validation : " + getValidation();
		result += "\n  - Master Degree Candidate : " + getMasterDegreeCandidate();
		result += "\n  - Situation : " + getSituation();

		return result;
	}

	public boolean equals(Object o) {
		if (o != null && o instanceof ICandidateSituation) {
			return this.getIdInternal().equals(
					((ICandidateSituation) o).getIdInternal());
		}
		return false;
	}
	
}
