
package ServidorAplicacao.Servico.masterDegree.commons.candidate;

import java.util.Calendar;

import Dominio.CandidateSituation;
import Dominio.ICandidateSituation;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.SituationName;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ApproveCandidates implements IServico {

	private static ApproveCandidates servico = new ApproveCandidates();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static ApproveCandidates getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ApproveCandidates() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ApproveCandidates";
	}

	
	public void run(String[] situations, String[] ids, String[] remarks, String[] substitutes) throws FenixServiceException {
		
		ISuportePersistente sp = null;
		try {
			sp = SuportePersistenteOJB.getInstance();

			for (int i = 0 ; i < situations.length ; i++) {
				IMasterDegreeCandidate masterDegreeCandidateTemp = new MasterDegreeCandidate();
				masterDegreeCandidateTemp.setIdInternal(new Integer(ids[i]));
				
				IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp.getIPersistentMasterDegreeCandidate().readByOId(masterDegreeCandidateTemp, false);
				ICandidateSituation candidateSituationOld = masterDegreeCandidate.getActiveCandidateSituation();
				sp.getIPersistentCandidateSituation().writeCandidateSituation(candidateSituationOld);
				candidateSituationOld.setValidation(new State(State.INACTIVE));
			
				masterDegreeCandidate.getSituations().add(candidateSituationOld);			
				
				if ((substitutes[i] != null) && (substitutes[i].length() > 0)){
					masterDegreeCandidate.setSubstituteOrder(new Integer(substitutes[i]));	
				}
				
				// Create the new Candidate Situation
				
				ICandidateSituation candidateSituation = new CandidateSituation();
				sp.getIPersistentCandidateSituation().writeCandidateSituation(candidateSituation);
				candidateSituation.setDate(Calendar.getInstance().getTime());
				candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
				candidateSituation.setRemarks(remarks[i]);
				candidateSituation.setSituation(new SituationName(situations[i]));
				candidateSituation.setValidation(new State(State.ACTIVE));
			}
			
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 
	}
}
