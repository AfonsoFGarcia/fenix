package ServidorApresentacao.sop;
  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.TestCaseActionExecution;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoAula;
import Util.TipoSala;

/**
 * AdicionarAulasFormActionTest.java
 * 
 * @author Ivo Brand�o
 */
public class AdicionarAulasFormActionTest extends TestCaseActionExecution {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(AdicionarAulasFormActionTest.class);

		return suite;
	}
	
	public void setUp() {
		super.setUp();
	}

	public AdicionarAulasFormActionTest(String testName) {
		super(testName);
	}

	private void prepareRequest() {
		//required to put form manipularTurnosForm in request
		getSession().setAttribute(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);
		setRequestPathInfo("/sop", "/manipularTurnosForm");
		addRequestParameter("indexTurno", new Integer(0).toString());
		actionPerform();		
	}
	
	public void testSuccessfulExecutionOfAction() {

		prepareRequest();
		doTest(null, getItemsToPutInSessionForActionToBeTestedSuccessfuly(), getSuccessfulForward(), null, null, null, null);

	}

	/** :FIXME dummy method to prevent super.testUnsuccessfulExecutionOfAction() 
	 * from executing 
	 */
	public void testUnsuccessfulExecutionOfAction() {
	}

	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {

		Map items = new HashMap();
		
		items.put(SessionConstants.SESSION_IS_VALID, SessionConstants.SESSION_IS_VALID);
		
		try {
			GestorServicos gestor = GestorServicos.manager();

			InfoExecutionCourse infoExecutionCourse =
				new InfoExecutionCourse(
					"Trabalho Final de Curso II",
					"TFCII",
					"programa1",
					new Double(1.5),
					new Double(2),
					new Double(1.5),
					new Double(2),
					new InfoExecutionPeriod(
						"2� Semestre",
						new InfoExecutionYear("2002/2003")));
			
			InfoRoom infoRoom = new InfoRoom("Ga2", 
				"Pavilhao Central", 
				new Integer(0),
				new TipoSala(TipoSala.ANFITEATRO),
				new Integer(100),
				new Integer(50));
			
			//create infoShift
			InfoShift infoShift = new InfoShift("turno_adicionar_aula", 
				new TipoAula(TipoAula.TEORICA), new Integer(100), infoExecutionCourse); 

			//create lessons			
			Object argsLerAulas[] = new Object[1];
			argsLerAulas[0] = infoExecutionCourse;
			ArrayList infoLessons = (ArrayList) gestor.executar(userView, "LerAulasDeDisciplinaExecucao", argsLerAulas);
			
			items.put("infoAulasDeDisciplinaExecucao", infoLessons);

			//create shifts
			Object argsLerTurnos[] = new Object[1];
			argsLerTurnos[0] = infoExecutionCourse;
			ArrayList infoShifts = (ArrayList) gestor.executar(userView, "LerTurnosDeDisciplinaExecucao", argsLerTurnos);
			
			items.put("infoTurnosDeDisciplinaExecucao", infoShifts);			

		} catch (Exception exception) {
			System.out.println("Error while performing the service " + exception);
		}

		return items;
		
	}

	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/tests/web-sop.xml";
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
		return "/adicionarAulasForm";
	}
	
	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getSuccessfulForward()
	 */
	protected String getSuccessfulForward() {
		return "Sucesso";
	}
	
	/**
	 * @see ServidorApresentacao.TestCaseActionExecution#getUnsuccessfulForward()
	 */
	protected String getUnsuccessfulForwardPath() {
		return "/adicionarAulas.jsp";
	}
	
}
