
package ServidorApresentacao.Action.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IMasterDegreeCandidate;
import ServidorApresentacao.ScopeConstants;
import ServidorApresentacao.TestCasePresentationMDAdministrativeOffice;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.Specialization;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

public class ListCandidateDispatchActionChangePasswordMethodTest
	extends TestCasePresentationMDAdministrativeOffice{
	/**
	 * Main method 
	 * @param args
	 */
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	/**
	 * 
	 * @return Test to be done
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(ListCandidateDispatchActionChangePasswordMethodTest.class);
		return suite;
	}

	/**
	 * @param testName
	 */
	public ListCandidateDispatchActionChangePasswordMethodTest(String testName) {
		super(testName);
	}

	/**
	 * Only needs SessionConstants.U_VIEW that is already in session 
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		HashMap sessionParameters = new HashMap();
		InfoMasterDegreeCandidate infoMasterDegreeCandidate = this.getCandidate();
		sessionParameters.put(SessionConstants.MASTER_DEGREE_CANDIDATE, infoMasterDegreeCandidate);
		
		// These parameters will be put at null because the aren't used in the Action, they are only cleaned
		sessionParameters.put(SessionConstants.NATIONALITY_LIST_KEY, null);
		sessionParameters.put(SessionConstants.MARITAL_STATUS_LIST_KEY, null);
		sessionParameters.put(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY, null);
		sessionParameters.put(SessionConstants.SEX_LIST_KEY, null);
		sessionParameters.put(SessionConstants.MONTH_DAYS_KEY, null);
		sessionParameters.put(SessionConstants.MONTH_LIST_KEY, null);
		sessionParameters.put(SessionConstants.YEARS_KEY, null);
		sessionParameters.put(SessionConstants.EXPIRATION_YEARS_KEY, null);
		sessionParameters.put(SessionConstants.CANDIDATE_SITUATION_LIST, null);
		return sessionParameters;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		return null;
	}


	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly() 
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		HashMap requestParameters = new HashMap();
		requestParameters.put("method","changePassword");
		return requestParameters;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		HashMap attributes = new HashMap();
		List requestAttributes = new ArrayList();
		requestAttributes.add(SessionConstants.MASTER_DEGREE_CANDIDATE);

		attributes.put(new Integer(ScopeConstants.SESSION),requestAttributes);
		return attributes;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInSuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getNonExistingAttributesListToVerifyInUnsuccessfulExecution()
	 */
	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/editCandidate";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "ChangePasswordSuccess";
	}

	private InfoMasterDegreeCandidate getCandidate(){
		SuportePersistenteOJB sp = null;
		IMasterDegreeCandidate masterDegreeCandidate = null;
		
		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			
			IExecutionYear executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			
			ICursoExecucao executionDegree = sp.getICursoExecucaoPersistente().readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
							"MEEC", "plano2", executionYear);
			assertNotNull(executionDegree);

			
			masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate().readByNumberAndExecutionDegreeAndSpecialization(
						new Integer(1), executionDegree, new Specialization(Specialization.MESTRADO));
			assertNotNull(masterDegreeCandidate);
			
			
			sp.confirmarTransaccao();
			
		} catch(Exception e) {
			fail("Error reading from DataBase ! " + e);
			
		}
		return Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
		
	}

}
