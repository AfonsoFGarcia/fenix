/*
 * Created on 14/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoCandidateSituation;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.ICandidateSituation;
import Dominio.IExecutionYear;
import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.SituationName;
import Util.Specialization;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidateList implements IServico {

	private static ReadCandidateList servico = new ReadCandidateList();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadCandidateList getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadCandidateList() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadCandidateList";
	}

	public List run(String degreeName, Specialization degreeType, SituationName candidateSituation, Integer candidateNumber) throws FenixServiceException {
		
		ISuportePersistente sp = null;
		List result = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			
			// Get the Actual Execution Year
			IExecutionYear executionYear = null;

			executionYear = sp.getIPersistentExecutionYear().readActualExecutionYear();
			
			// Read the candidates
			
			result = sp.getIPersistentMasterDegreeCandidate().readCandidateList(degreeName, degreeType, candidateSituation, candidateNumber, executionYear);
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 

		List candidateList = new ArrayList();
		Iterator iterator = result.iterator();
		while(iterator.hasNext()){
			IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) iterator.next(); 
			InfoMasterDegreeCandidate infoMasterDegreeCandidate = Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
			Iterator situationIterator = masterDegreeCandidate.getSituations().iterator();
			List situations = new ArrayList();
			while (situationIterator.hasNext()){ 
				InfoCandidateSituation infoCandidateSituation = Cloner.copyICandidateSituation2InfoCandidateSituation((ICandidateSituation) situationIterator.next()); 
				situations.add(infoCandidateSituation);
				
				// Check if this is the Active Situation
				if 	(infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
					infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
			}			
			infoMasterDegreeCandidate.setSituationList(situations);
			candidateList.add(infoMasterDegreeCandidate);
			
		}
		return candidateList;
		
	}
}
