/*
 * ChangeMasterDegreeCandidate.java
 *
 * O Servico ChangeMasterDegreeCandidate altera a informacao de um candidato de Mestrado
 * Nota : E suposto os campos (numeroCandidato, anoCandidatura, chaveCursoMestrado, username)
 *        nao se puderem alterar
 *
 * Created on 02 de Dezembro de 2002, 16:25
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servico.masterDegree.candidate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoPerson;
import DataBeans.util.Cloner;
import Dominio.CandidateSituation;
import Dominio.ICandidateSituation;
import Dominio.ICursoExecucao;
import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.ICandidateView;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.CandidateView;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.person.ChangePersonalInfo;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCandidateSituation;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.SituationName;
import Util.Specialization;

public class ChangeApplicationInfo implements IServico {

	private static ChangeApplicationInfo servico = new ChangeApplicationInfo();

	/**
	 * The singleton access method of this class.
	 **/
	public static ChangeApplicationInfo getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ChangeApplicationInfo() {
	}

	/**
	 * Returns the service name
	 **/

	public final String getNome() {
		return "ChangeApplicationInfo";
	}

	public InfoMasterDegreeCandidate run(InfoMasterDegreeCandidate newMasterDegreeCandidate, InfoPerson infoPerson, UserView userView) throws FenixServiceException, ExcepcaoPersistencia, IllegalAccessException, InvocationTargetException {

		ISuportePersistente sp = null;
		IMasterDegreeCandidate existingMasterDegreeCandidate = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			ICursoExecucao executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(newMasterDegreeCandidate.getInfoExecutionDegree());
			existingMasterDegreeCandidate =
				sp.getIPersistentMasterDegreeCandidate().readByIdentificationDocNumberAndTypeAndExecutionDegreeAndSpecialization(
					newMasterDegreeCandidate.getInfoPerson().getNumeroDocumentoIdentificacao(),
					newMasterDegreeCandidate.getInfoPerson().getTipoDocumentoIdentificacao().getTipo(),
					executionDegree,
					new Specialization(newMasterDegreeCandidate.getSpecialization()));
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		if (existingMasterDegreeCandidate == null) {
			throw new ExcepcaoInexistente("Unknown Candidate !!");
		}

		try {
			ChangePersonalInfo.getService().run(infoPerson, userView);
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		// Change the Information

		sp.getIPersistentMasterDegreeCandidate().writeMasterDegreeCandidate(existingMasterDegreeCandidate);

		existingMasterDegreeCandidate.setAverage(newMasterDegreeCandidate.getAverage());
		existingMasterDegreeCandidate.setMajorDegree(newMasterDegreeCandidate.getMajorDegree());
		existingMasterDegreeCandidate.setMajorDegreeSchool(newMasterDegreeCandidate.getMajorDegreeSchool());
		existingMasterDegreeCandidate.setMajorDegreeYear(newMasterDegreeCandidate.getMajorDegreeYear());
		existingMasterDegreeCandidate.setSpecializationArea(newMasterDegreeCandidate.getSpecializationArea());

		// Change the Candidate Situation
		InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;

		try {
			IPersistentCandidateSituation candidateSituationDAO = sp.getIPersistentCandidateSituation();

			infoMasterDegreeCandidate = Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(existingMasterDegreeCandidate);

			List situationsFromBD = existingMasterDegreeCandidate.getSituations();

			Iterator situationIterator = situationsFromBD.iterator();
			List situations = new ArrayList();
			ICandidateView candidateView = null;

			while (situationIterator.hasNext()) {
				ICandidateSituation candidateSituation = (ICandidateSituation) situationIterator.next();

				// Check if this is the Active Situation
				if (candidateSituation.getValidation().equals(new Util.State(Util.State.ACTIVE))) {

					candidateSituationDAO.writeCandidateSituation(candidateSituation);

					candidateSituation.setValidation(new Util.State(Util.State.INACTIVE));

					// FIXME: the two lines aboce should be removed. below we should commit and then read the candidate 
					ICandidateSituation candidateSituationTemp = new CandidateSituation();
					candidateSituationTemp.setIdInternal(candidateSituation.getIdInternal());
					ICandidateSituation candidateSituationFromBD = (ICandidateSituation) sp.getIPersistentCandidateSituation().readByOId(candidateSituationTemp, true);
					candidateSituationFromBD.setValidation(new Util.State(Util.State.INACTIVE));

					break;
				}
			}

			//Create the New Candidate Situation
			ICandidateSituation activeCandidateSituation = createActiveSituation(existingMasterDegreeCandidate, candidateSituationDAO);


			sp.confirmarTransaccao();
			sp.iniciarTransaccao();

			situations.add(Cloner.copyICandidateSituation2InfoCandidateSituation(activeCandidateSituation));
			candidateView = new CandidateView(situations);
			userView.setCandidateView(candidateView);

			infoMasterDegreeCandidate.setInfoCandidateSituation(Cloner.copyICandidateSituation2InfoCandidateSituation(activeCandidateSituation));
			infoMasterDegreeCandidate.setInfoPerson(infoPerson);

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return infoMasterDegreeCandidate;
	}

	private ICandidateSituation createActiveSituation(IMasterDegreeCandidate existingMasterDegreeCandidate, IPersistentCandidateSituation candidateSituationDAO) throws ExcepcaoPersistencia {
		ICandidateSituation activeCandidateSituation = new CandidateSituation();
		candidateSituationDAO.writeCandidateSituation(activeCandidateSituation);

		Calendar calendar = Calendar.getInstance();
		activeCandidateSituation.setDate(calendar.getTime());
		activeCandidateSituation.setSituation(new SituationName(SituationName.PENDENT_COM_DADOS));
		activeCandidateSituation.setValidation(new Util.State(Util.State.ACTIVE));

		activeCandidateSituation.setMasterDegreeCandidate(existingMasterDegreeCandidate);
		return activeCandidateSituation;
	}
}