
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoCandidateRegistration;
import DataBeans.util.Cloner;
import Dominio.Branch;
import Dominio.CandidateSituation;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.Gratuity;
import Dominio.IBranch;
import Dominio.ICandidateEnrolment;
import Dominio.ICandidateSituation;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IGratuity;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPessoa;
import Dominio.IQualification;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.IStudentKind;
import Dominio.MasterDegreeCandidate;
import Dominio.Pessoa;
import Dominio.Qualification;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ActiveStudentCurricularPlanAlreadyExistsServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.GratuityState;
import Util.SituationName;
import Util.State;
import Util.StudentCurricularPlanState;
import Util.StudentState;
import Util.StudentType;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class RegisterCandidate implements IServico {

	private static RegisterCandidate servico = new RegisterCandidate();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static RegisterCandidate getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private RegisterCandidate() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "RegisterCandidate";
	}

	public InfoCandidateRegistration run(Integer candidateID, Integer branchID) throws FenixServiceException {

		ISuportePersistente sp = null;

		IStudentCurricularPlan studentCurricularPlanResult = null;
		IMasterDegreeCandidate masterDegreeCandidate = null; 
		try {
			sp = SuportePersistenteOJB.getInstance();
			
			IMasterDegreeCandidate mdcTemp = new MasterDegreeCandidate();
			mdcTemp.setIdInternal(candidateID);
			
			masterDegreeCandidate = (IMasterDegreeCandidate) sp.getIPersistentMasterDegreeCandidate().readByOId(mdcTemp, false);
			
			
			if (!validSituation(masterDegreeCandidate.getActiveCandidateSituation())) {
				throw new InvalidChangeServiceException();
			} 

			// Check if a Master Degree Student Already Exists
			IStudent student = sp.getIPersistentStudent().readByPersonAndDegreeType(masterDegreeCandidate.getPerson(), TipoCurso.MESTRADO_OBJ);
			
			if (student == null){
				student = new Student();
				sp.getIPersistentStudent().simpleLockWrite(student);
				student.setDegreeType(TipoCurso.MESTRADO_OBJ);
				student.setPerson(masterDegreeCandidate.getPerson());
				student.setState(new StudentState(StudentState.INSCRITO));
				student.setNumber(sp.getIPersistentStudent().generateStudentNumber(TipoCurso.MESTRADO_OBJ));
				
				
				IStudentKind studentKind = sp.getIPersistentStudentKind().readByStudentType(new StudentType(StudentType.NORMAL));
				student.setStudentKind(studentKind);
				
				
			}

			IStudentCurricularPlan studentCurricularPlanOld = sp.getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(student.getNumber(), TipoCurso.MESTRADO_OBJ); 

			if (studentCurricularPlanOld != null){
				sp.getIStudentCurricularPlanPersistente().lockWrite(studentCurricularPlanOld);

				System.out.println("------------- MASTER DEGREE STUDENT WITH ACTIVE CURRICULAR PLAN ----------------");
				System.out.println(" -- STUDENT NUMBER: " + studentCurricularPlanOld.getStudent().getNumber() + "[id: " + studentCurricularPlanOld.getStudent().getIdInternal() + "]");
				System.out.println(" -- STUDENT CURRICULAR PLAN ID : " + studentCurricularPlanOld.getIdInternal());
				System.out.println("--------------------------------------------------------------------------------");

				throw new ActiveStudentCurricularPlanAlreadyExistsServiceException();
			}

			IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
			sp.getIStudentCurricularPlanPersistente().simpleLockWrite(studentCurricularPlan);	
			IBranch branchTemp = new Branch();
			branchTemp.setIdInternal(branchID);
			IBranch branch = (IBranch) sp.getIPersistentBranch().readByOId(branchTemp, false);
				
			studentCurricularPlan.setBranch(branch);
			studentCurricularPlan.setCurrentState(StudentCurricularPlanState.ACTIVE_OBJ);
			studentCurricularPlan.setDegreeCurricularPlan(masterDegreeCandidate.getExecutionDegree().getCurricularPlan());
			studentCurricularPlan.setGivenCredits(masterDegreeCandidate.getGivenCredits());
			studentCurricularPlan.setSpecialization(masterDegreeCandidate.getSpecialization());
			studentCurricularPlan.setStartDate(Calendar.getInstance().getTime());
			studentCurricularPlan.setStudent(student);


			// Get the Candidate Enrolments

			List candidateEnrolments = sp.getIPersistentCandidateEnrolment().readByMDCandidate(masterDegreeCandidate);

			List enrolments = new ArrayList();
			Iterator iterator = candidateEnrolments.iterator();
			while(iterator.hasNext()){
				ICandidateEnrolment candidateEnrolment = (ICandidateEnrolment) iterator.next();
				
				IEnrolment enrolment = new Enrolment();
				sp.getIPersistentEnrolment().simpleLockWrite(enrolment);
				enrolment.setCurricularCourseScope(candidateEnrolment.getCurricularCourseScope()); 
				enrolment.setEnrolmentEvaluationType(new EnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL));
				enrolment.setEnrolmentState(EnrolmentState.ENROLED);
				enrolment.setExecutionPeriod(sp.getIPersistentExecutionPeriod().readActualExecutionPeriod());
				enrolment.setStudentCurricularPlan(studentCurricularPlan);
				enrolment.setEvaluations(new ArrayList());
				
				IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
				sp.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluation);
				enrolmentEvaluation.setEnrolment(enrolment);
				enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
				enrolmentEvaluation.setEnrolmentEvaluationType(new EnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL));
			
				
				enrolment.getEvaluations().add(enrolmentEvaluation);
				
				
			}
			
			// Change the Candidate Situation
			ICandidateSituation candidateSituationTemp = new CandidateSituation();
			candidateSituationTemp.setIdInternal(masterDegreeCandidate.getActiveCandidateSituation().getIdInternal());
			
			ICandidateSituation oldCandidateSituation = (ICandidateSituation) sp.getIPersistentCandidateSituation().readByOId(candidateSituationTemp, true);
			oldCandidateSituation.setValidation(new State(State.INACTIVE));
			
			ICandidateSituation candidateSituation = new CandidateSituation();
			sp.getIPersistentCandidateSituation().simpleLockWrite(candidateSituation);
			candidateSituation.setDate(Calendar.getInstance().getTime());
			candidateSituation.setMasterDegreeCandidate(masterDegreeCandidate);
			candidateSituation.setValidation(new State(State.ACTIVE));
			candidateSituation.setSituation(SituationName.ENROLLED_OBJ);
			 

			// Inicial Gratuity State
			IGratuity gratuity = new Gratuity();
			sp.getIPersistentGratuity().simpleLockWrite(gratuity);
			gratuity.setDate(Calendar.getInstance().getTime());
			gratuity.setGratuityState(GratuityState.NOT_PAYED);
			gratuity.setState(new State(State.ACTIVE));
			gratuity.setStudentCurricularPlan(studentCurricularPlan);
			
						
			// Copy Qualifications
			
			IQualification qualification = new Qualification();
			sp.getIPersistentQualification().simpleLockWrite(qualification);
			qualification.setMark(masterDegreeCandidate.getAverage().toString());
			qualification.setPerson(masterDegreeCandidate.getPerson());
			qualification.setSchool(masterDegreeCandidate.getMajorDegreeSchool());
			qualification.setTitle(masterDegreeCandidate.getMajorDegree());
			qualification.setYear(masterDegreeCandidate.getMajorDegreeYear());
			
			
			
			sp.confirmarTransaccao();
			sp.iniciarTransaccao();			 

			// Change the Username If neccessary
			
			changeUsernameIfNeccessary(studentCurricularPlan.getStudent());

	
			studentCurricularPlanResult = sp.getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(student.getNumber(), TipoCurso.MESTRADO_OBJ);

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		} 

		InfoCandidateRegistration infoCandidateRegistration = new InfoCandidateRegistration();
		
		infoCandidateRegistration.setInfoMasterDegreeCandidate(Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate));
		infoCandidateRegistration.setInfoStudentCurricularPlan(Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlanResult));
		infoCandidateRegistration.setEnrolments(new ArrayList());
		Iterator iterator = studentCurricularPlanResult.getEnrolments().iterator();
		while(iterator.hasNext()){
			Enrolment enrolment = (Enrolment) iterator.next();
			infoCandidateRegistration.getEnrolments().add(Cloner.copyIEnrolment2InfoEnrolment(enrolment));
		}

		return infoCandidateRegistration;
	}

	private void changeUsernameIfNeccessary(IStudent student) throws ExcepcaoPersistencia {
		try {
			SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
			
			if ((student.getPerson().getUsername().indexOf("Mes") != -1) ||
				(student.getPerson().getUsername().indexOf("Esp") != -1) ||
				(student.getPerson().getUsername().indexOf("Int") != -1)){
			
					IPessoa personTemp = new Pessoa();
					personTemp.setIdInternal(student.getPerson().getIdInternal());
					IPessoa person = (IPessoa) sp.getIPessoaPersistente().readByOId(personTemp, true);
					person.setUsername("M" + student.getNumber());
			}
			
		} catch (ExcepcaoPersistencia e) {
			throw new ExcepcaoPersistencia();
		}
	}

	/**
	 * @param situation
	 * @return 
	 */
	private boolean validSituation(ICandidateSituation situation) {
		boolean result = false;
		if (situation.getSituation().equals(SituationName.ADMITIDO_OBJ) ||
			situation.getSituation().equals(SituationName.ADMITED_CONDICIONAL_CURRICULAR_OBJ) ||
			situation.getSituation().equals(SituationName.ADMITED_CONDICIONAL_FINALIST_OBJ) ||
			situation.getSituation().equals(SituationName.ADMITED_CONDICIONAL_OTHER_OBJ) ||
			situation.getSituation().equals(SituationName.ADMITED_SPECIALIZATION_OBJ)) {
				result = true;
			}
		return result;
	}
}
