/*
 * Created on 26/Fev/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author dcs-rjao
 *
 * 26/Fev/2003
 */

public abstract class TestCaseActionExecution extends TestCasePresentation {

	protected GestorServicos gestor = null;

	public TestCaseActionExecution(String testName) {
		super(testName);
	}

	public void setUp() {

		super.setUp();

	    //defines struts config file to use
		setServletConfigFile(getServletConfigFile());
		setAuthorizedUser();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// -------------------------------------------------------------------------------------------------------

	public void testSuccessfulExecutionOfAction() {

		doTest(
			getItemsToPutInRequestForActionToBeTestedSuccessfuly(),
			getItemsToPutInSessionForActionToBeTestedSuccessfuly(),
			getSuccessfulForward(),
			getSuccessfulForwardPath(),
			getExistingAttributesListToVerifyInSuccessfulExecution(),
			getNonExistingAttributesListToVerifyInSuccessfulExecution(),
			null);
	}

	public void testUnsuccessfulExecutionOfAction() {

		doTest(
			(Map) getItemsToPutInRequestForActionToBeTestedUnsuccessfuly(),
			(Map) getItemsToPutInSessionForActionToBeTestedUnsuccessfuly(),
			getUnsuccessfulForward(),
			getUnsuccessfulForwardPath(),
			(Map) getExistingAttributesListToVerifyInUnsuccessfulExecution(),
			(Map) getNonExistingAttributesListToVerifyInUnsuccessfulExecution(),
			getActionErrors());
	}

	/**
	 * @param itemsToPutInRequest
	 * @param itemsToPutInSession
	 * @param forward
	 * @param existingAttributesList
	 * @param nonExistingAttributesList
	 */
	protected void doTest(
		Map itemsToPutInRequest,
		Map itemsToPutInSession,
		String forward,
		String forwardPath,
		Map existingAttributesList,
		Map nonExistingAttributesList,
		String[] actionErrors) {

		String pathOfAction = getRequestPathInfoPathAction();
		String nameOfAction = getRequestPathInfoNameAction();

		if ((pathOfAction != null) && (nameOfAction != null)) {
			setRequestPathInfo(
				getRequestPathInfoPathAction(),
				getRequestPathInfoNameAction());
		}

		if (itemsToPutInRequest != null) {

			Set keys = itemsToPutInRequest.keySet();
			Iterator keysIterator = keys.iterator();

			while (keysIterator.hasNext()) {
				String key = (String) keysIterator.next();
				String item = (String) itemsToPutInRequest.get(key);
				addRequestParameter(key, item);
			}
		}

		if (itemsToPutInSession != null) {

			Set keys = itemsToPutInSession.keySet();
			Iterator keysIterator = keys.iterator();

			while (keysIterator.hasNext()) {
				String key = (String) keysIterator.next();
				Object item = itemsToPutInSession.get(key);
				getSession().setAttribute(key, item);
			}
		}

		if (((pathOfAction != null) && (nameOfAction != null))
			&& ((forward != null) || (forwardPath != null))) {
			//			perform
			actionPerform();

			//			checks for errors
			if (actionErrors == null)
				verifyNoActionErrors();
			else
				verifyActionErrors(actionErrors);

			//			checks forward			
			if (forward != null)
				verifyForward(forward);

			//			checks forward path			
			if (forwardPath != null)
				verifyForwardPath(forwardPath);

			//			CurricularYearAndSemesterAndInfoExecutionDegree ctx = SessionUtils.getContext(getRequest());
			//			assertNotNull("Context is null!", ctx);

			Set keys = null;
			Iterator keysIterator = null;
			
			// Test existing attributes  			
			if (existingAttributesList != null) {
				keys = existingAttributesList.keySet();
				if (keys != null) {
					keysIterator = keys.iterator();
					while (keysIterator.hasNext()) {
						Integer key = (Integer) keysIterator.next();
						verifyScopeAttributes(
							key.intValue(),
							(List) existingAttributesList.get(key),
							null);
					}
				}
			}
//			Test non existing attributes 
			if (nonExistingAttributesList != null) {
				keys = nonExistingAttributesList.keySet();
				if (keys != null) {
					keysIterator = keys.iterator();
					while (keysIterator.hasNext()) {
						Integer key = (Integer) keysIterator.next();
						verifyScopeAttributes(
							key.intValue(),
							null,
							(List) nonExistingAttributesList.get(key));
					}
				}
				
			}
		}

	}

	/**
	 * @param scope
	 * @param existingAttributesList
	 * @param nonExistingAttributesList
	 */
	private void verifyScopeAttributes(
		int scope,
		List existingAttributesList,
		List nonExistingAttributesList) {

		Enumeration attNames = null;
		String scopeStr = "";
		switch (scope) {
			case ScopeConstants.SESSION :
				attNames = getSession().getAttributeNames();
				scopeStr = "session";
				break;
			case ScopeConstants.REQUEST :
				attNames = getRequest().getAttributeNames();
				scopeStr = "request";
				break;
			case ScopeConstants.APP_CONTEXT :
				attNames =
					getActionServlet().getServletContext().getAttributeNames();
				scopeStr = "context";
				break;
			default :
				throw new IllegalArgumentException(
					"Unknown scope! Use " + ScopeConstants.class.getName()+" constants!");
		}
		
		
		verifyAttributes(scopeStr, attNames,existingAttributesList , true);
		verifyAttributes(scopeStr, attNames, nonExistingAttributesList, false);
	}

	/**
	 * @param attNames
	 * @param list
	 * @param exists
	 */
	private void verifyAttributes(
		String scopeStr,
		Enumeration attNames,
		List list,
		boolean contains) {
		if ((list != null) && (attNames != null)) {
			Collection scopeCollection = new ArrayList();
			CollectionUtils.addAll(scopeCollection, attNames);		
			Iterator listIterator = list.iterator();

			while (listIterator.hasNext()) {
				String attName = (String) listIterator.next();
				String message = null;
				if (scopeCollection.contains(attName) && !contains){
					message = "Scope "+scopeStr+" contains attribute ";
					fail(message + attName + ".");
				}
				if (!scopeCollection.contains(attName) && contains){
					message = "Scope "+scopeStr+" doesn't contains attribute ";
					fail(message + attName + ".");
				}
				
			}			
		}
	}

	// -------------------------------------------------------------------------------------------------------

	/**
	 * This method must return a Map with all the items that should be in session to execute
	 * the action successfuly.
	 * The Map must be an Map and it's keys must be the SessionUtils string constants
	 * correspondent to each object to put in session.
	 * This method must return null if not to be used.
	 */
	protected abstract Map getItemsToPutInSessionForActionToBeTestedSuccessfuly();

	/**
	 * This method must return a Map with all the items that should be in session to execute
	 * the action unsuccessfuly.
	 * The Map must be an Map and it's keys must be the SessionUtils string constants
	 * correspondent to each object to put in session.
	 * This method must return null if not to be used.
	 */
	protected abstract Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() ;

	/**
	 * This method must return a Map with all the items that should be in request (form) to execute
	 * the action successfuly.
	 * The Map must be an Map and it's keys must be the form field names
	 * correspondent to each property to get out of the request.
	 * This method must return null if not to be used.
	 */
	protected abstract Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() ;

	/**
	 * This method must return a Map with all the items that should be in request (form) to execute
	 * the action unsuccessfuly.
	 * The Map must be an Map and it's keys must be the form field names
	 * correspondent to each property to get out of the request.
	 * This method must return null if not to be used.
	 */
	protected abstract Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() ;

	/**
	 * This method must return a List with the attributes that are supose to be present in a specified scope
	 * when the action executes successfuly.
	 * The scope is specified by the method getScope().
	 * This method must return null if not to be used.
	 */
	protected abstract Map getExistingAttributesListToVerifyInSuccessfulExecution() ;

	/**
	 * This method must return a List with the attributes that are not supose to be present in a specified scope
	 * when the action executes successfuly.
	 * The scope is specified by the method getScope().
	 * This method must return null if not to be used.
	 */
	protected abstract Map getNonExistingAttributesListToVerifyInSuccessfulExecution() ;

	/**
	 * This method must return a List with the attributes that are supose to be present in a specified scope
	 * when the action executes unsuccessfuly.
	 * The scope is specified by the method getScope().
	 * This method must return null if not to be used.
	 */
	protected abstract Map getExistingAttributesListToVerifyInUnsuccessfulExecution();

	/**
	 * This method must return a List with the attributes that are not supose to be present in a specified scope
	 * when the action executes unsuccessfuly.
	 * The scope is specified by the method getScope().
	 * This method must return null if not to be used.
	 */
	protected abstract Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() ;

	/**
	 * This method must return one of these 3 constants: ScopeConstants.SESSION;
	 * ScopeConstants.REQUEST; ScopeConstants.APP_CONTEXT.
	 */
	protected int getScope() {
		return ScopeConstants.SESSION;
	}

	/**
	 * This method must return a string identifying the servlet configuration file.
	 */
	protected abstract String getServletConfigFile();

	/**
	 * This method must return a string identifying the action path of the request path.
	 */
	protected abstract String getRequestPathInfoPathAction();

	/**
	 * This method must return a string identifying the action name of the request path.
	 */
	protected abstract String getRequestPathInfoNameAction();

	/**
	 * This method must return a string identifying the forward when the action executes successfuly.
	 */
	protected String getSuccessfulForward() {
		return null;
	}

	/**
	 * This method must return a string identifying the forward when the action executes unsuccessfuly.
	 */
	protected String getUnsuccessfulForward() {
		return null;
	}

	/**
	 * This method must return a string identifying the forward path when the action executes successfuly.
	 */
	protected String getSuccessfulForwardPath() {
		return null;
	}

	/**
	 * This method must return a string identifying the forward path when the action executes unsuccessfuly.
	 */
	protected String getUnsuccessfulForwardPath() {
		return null;
	}

	/**
	 * This method must return a array of strings identifying the ActionErrors for
	 * use with testUnsuccessfulExecutionOfAction.
	 */
	protected String[] getActionErrors() {
		return null;
	}

	public void setNotAuthorizedUser() {
		UserView userView = new UserView("user", null);
		Collection roles = new ArrayList();
		userView.setRoles(roles);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);
	}
	
	abstract public Collection getAuthorizedRolesCollection();
	
	/**
	 * @return Object
	 */
	protected void setAuthorizedUser() {
		getSession().setAttribute(SessionConstants.U_VIEW, getAuthorizedUser());
	}
	protected IUserView getAuthorizedUser(){
		UserView userView = new UserView("user", null);
		Collection roles = getAuthorizedRolesCollection();
		userView.setRoles(roles);
		return userView; 
	}
}
