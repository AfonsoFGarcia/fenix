/*
 * Created on 14/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.CandidateSituation;
import Dominio.ICandidateSituation;
import Dominio.ICursoExecucao;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.MasterDegreeCandidate;
import Dominio.PersonRole;
import Dominio.Pessoa;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.person.GenerateUsername;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.RoleType;
import Util.SituationName;
import Util.Specialization;
import Util.State;
/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class CreateMasterDegreeCandidate implements IServico {


	private static CreateMasterDegreeCandidate servico = new CreateMasterDegreeCandidate();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static CreateMasterDegreeCandidate getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CreateMasterDegreeCandidate() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "CreateMasterDegreeCandidate";
	}

	public InfoMasterDegreeCandidate run(InfoMasterDegreeCandidate newMasterDegreeCandidate)
		throws Exception{

		IMasterDegreeCandidate masterDegreeCandidate = new MasterDegreeCandidate();
		
		ISuportePersistente sp = null;

		IRole role = null;
		IPessoa person = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			
			// Check if the person Exists
			
			person = sp.getIPessoaPersistente().lerPessoaPorNumDocIdETipoDocId(newMasterDegreeCandidate.getInfoPerson().getNumeroDocumentoIdentificacao(), 
							 newMasterDegreeCandidate.getInfoPerson().getTipoDocumentoIdentificacao());
			
			// Read the Execution of this degree in the current execution Year
			
			ICursoExecucao executionDegree = sp.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYear(
						 newMasterDegreeCandidate.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getInfoDegree().getNome(),
						 Cloner.copyInfoExecutionYear2IExecutionYear(newMasterDegreeCandidate.getInfoExecutionDegree().getInfoExecutionYear()));
			
			// Create the Candidate
			
			// Set the Candidate's Situation
			Set situations = new HashSet();
			ICandidateSituation candidateSituation = new CandidateSituation();
			candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
			candidateSituation.setRemarks("Pr�-Candidatura. Pagamento da candidatura por efectuar.");
			candidateSituation.setSituation(new SituationName(SituationName.PRE_CANDIDATO));
			candidateSituation.setValidation(new State(State.ACTIVE));
			
			Calendar actualDate = Calendar.getInstance();
			candidateSituation.setDate(actualDate.getTime());
			
			situations.add(candidateSituation);
			
			masterDegreeCandidate.setSituations(situations);
			masterDegreeCandidate.setPerson(person);
			masterDegreeCandidate.setSpecialization(new Specialization(newMasterDegreeCandidate.getSpecialization()));
			masterDegreeCandidate.setExecutionDegree(executionDegree);

			// Generate the Candidate's number	
			Integer number = sp.getIPersistentMasterDegreeCandidate().generateCandidateNumber(
								masterDegreeCandidate.getExecutionDegree().getExecutionYear().getYear(),
								masterDegreeCandidate.getExecutionDegree().getCurricularPlan().getDegree().getNome(), 
								masterDegreeCandidate.getSpecialization());

			masterDegreeCandidate.setCandidateNumber(number);
			
			if (person == null) {
				// Create the new Person
				
				person = new Pessoa();
				person.setNome(newMasterDegreeCandidate.getInfoPerson().getNome());
				person.setNumeroDocumentoIdentificacao(newMasterDegreeCandidate.getInfoPerson().getNumeroDocumentoIdentificacao());
				person.setTipoDocumentoIdentificacao(newMasterDegreeCandidate.getInfoPerson().getTipoDocumentoIdentificacao());
				
				// Generate Person Username
				
				String username = GenerateUsername.getCandidateUsername(masterDegreeCandidate);
				person.setUsername(username);
				
				sp.getIPessoaPersistente().escreverPessoa(person);
				
				// Give the Person Role
				role = sp.getIPersistentRole().readByRoleType(RoleType.PERSON);
				IPersonRole personRole = new PersonRole();
				personRole.setPerson(person);
				personRole.setRole(role);
				
				sp.getIPersistentPersonRole().write(personRole);
			}
			
			
			masterDegreeCandidate.setPerson(person);
			
			// Write the new Candidate
			sp.getIPersistentMasterDegreeCandidate().writeMasterDegreeCandidate(masterDegreeCandidate);		
			
		} catch (ExistingPersistentException ex) {
			throw new ExistingServiceException(ex);
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 

		try {
			// Give the Master Degree Candidate Role
			IPersonRole personRole = new PersonRole();
			role = sp.getIPersistentRole().readByRoleType(RoleType.MASTER_DEGREE_CANDIDATE);
			personRole.setPerson(person);
			personRole.setRole(role);
				
			sp.getIPersistentPersonRole().write(personRole);

		} catch (ExistingPersistentException ex) {
			// This person is already a Candidate. No need to give the role again
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 
		
		// Return the new Candidate
		return Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
		}
}
