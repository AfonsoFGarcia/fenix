/*
 * Created on 23/Jul/2003
 */
package ServidorApresentacao.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ServidorApresentacao.ScopeConstants;
import ServidorApresentacao.TestCasePresentationManagerPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */
public class ReadDegreeActionTest extends TestCasePresentationManagerPortal{

	
	/**
	 * @param testName
	 */
	public ReadDegreeActionTest(String testName) {
		super(testName);

	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/readDegree";
	}

	/**
	 * This method must return a array of strings identifying the ActionErrors for
	 * use with testUnsuccessfulExecutionOfAction.
	 */
	protected String[] getActionErrors() {
		return null;
	}

	/**
	* This method must return a string identifying the forward path when the action executes unsuccessfuly.
	*/
	protected String getUnsuccessfulForwardPath() {
		return null;
	}

	/**
	 * This method must return a string identifying the forward path when the action executes successfuly.
	 */
	protected String getSuccessfulForwardPath() {
		return null;
	}

	/**
	 * This method must return a string identifying the forward when the action executes successfuly.
	 */
	protected String getSuccessfulForward() {
		return "viewDegree";
	}

	/**
	 * This method must return a string identifying the forward when the action executes unsuccessfuly.
	 */
	protected String getUnsuccessfulForward() {
		return null;
	}
	
	protected int getScope() {
		return ScopeConstants.REQUEST;
	}

	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		return null;
	}

	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		
		return null;
	}

	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
//		Map result = new HashMap();
//		ISuportePersistente persistentSupport = null;
//		Integer persistentId = null;
//		try {
//					
//				persistentSupport = SuportePersistenteOJB.getInstance();
//				persistentSupport.iniciarTransaccao();			
//				persistentId = persistentSupport.getICursoPersistente().;
//				persistentSupport.confirmarTransaccao();
//
//			}catch (ExcepcaoPersistencia exception) {
//						  exception.printStackTrace(System.out);
//						  fail("Using services at getItemsToPutInSessionForActionToBeTestedSuccessfuly()!");
//						}	
//		result.put(new Integer(ScopeConstants.REQUEST), requestAttributtes);
//	
//		return result;
        return null;
	}
	
	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
	return null;
	}

	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		Map result = new HashMap();

		List requestAttributtes = new ArrayList(1);
		requestAttributtes.add(SessionConstants.INFO_DEGREE);
		result.put(new Integer(ScopeConstants.REQUEST), requestAttributtes);
	
		return result;
	}

	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

}

