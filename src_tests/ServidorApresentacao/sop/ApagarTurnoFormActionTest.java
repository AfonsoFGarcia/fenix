/*
 * ApagarTurnoFormAction2.java
 * Mar 6, 2003
 */
package ServidorApresentacao.sop;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoShift;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.TestCasePresentationSopPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoAula;

/**
 * @author Ivo Brand�o
 */
public class ApagarTurnoFormActionTest extends TestCasePresentationSopPortal {

	/**
	 * @param testName
	 */
	public ApagarTurnoFormActionTest(String testName) {
		super(testName);
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/web.xml";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		return "/sop";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoNameAction()
	 */
	protected String getRequestPathInfoNameAction() {
		return "/apagarTurno";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "Sucesso";
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#testSuccessfulExecutionOfAction()
	 */
	public void testSuccessfulExecutionOfAction() {
		//place form manipularAulasForm in session
		getSession().setAttribute(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);
		setRequestPathInfo("/sop", "/manipularTurnosForm");
		addRequestParameter("indexTurno", new Integer(0).toString());
		actionPerform();

		//run the test
		doTest(null, getItemsToPutInSessionForActionToBeTestedSuccessfuly(), getSuccessfulForward(), null, null, null, null);
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#testUnsuccessfulExecutionOfAction()
	 * 
	 * dummy method
	 */
	public void testUnsuccessfulExecutionOfAction() {
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		Map result = new Hashtable();

		result.put(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);
		
		try {
			GestorServicos manager = GestorServicos.manager();
			InfoExecutionCourse iDE = 
			new InfoExecutionCourse("Trabalho Final de Curso I","TFCI",
						"programa1",
						new Double(0),
						new Double(0),
						new Double(0),
						new Double(0),
						new InfoExecutionPeriod("2� Semestre",new InfoExecutionYear("2002/2003")));

			result.put(SessionConstants.EXECUTION_COURSE_KEY, iDE);
    	
			Object argsReadShifts[] = new Object[1];
			argsReadShifts[0] = iDE;
			ArrayList infoShifts = (ArrayList) manager.executar(getAuthorizedUser(), "LerTurnosDeDisciplinaExecucao", argsReadShifts);
			result.put("infoTurnosDeDisciplinaExecucao", infoShifts);

			InfoShift infoShift1 = new InfoShift("turno1", new TipoAula(2),new Integer(100), iDE);
			InfoShift infoShift = (InfoShift) infoShifts.get(infoShifts.indexOf((InfoShift) infoShift1));
			result.put("infoTurno", infoShift);

		} catch (Exception exception) {
			exception.printStackTrace(System.out);
			fail("Using services at getItemsToPutInSessionForActionToBeTestedSuccessfuly()!");

		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInSessionForActionToBeTestedUnsuccessfuly()
	 */
	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getItemsToPutInRequestForActionToBeTestedSuccessfuly()
	 */
	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {
		
		return null;
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
		
		return null;
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

}
