package ServidorApresentacao.publico;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Jo�o Mota
 */
public class ViewShiftsFormActionTest extends TestCasePresentation {
	
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ViewShiftsFormActionTest.class);

		return suite;
	}

	public void setUp() {
		super.setUp();
		// define ficheiro de configura��o Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-publico.xml");
		
	}

	public ViewShiftsFormActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulViewShiftsFormAction() {
		// define mapping de origem
		setRequestPathInfo("publico", "/viewShifts");

		// preenche o form
		addRequestParameter("courseInitials", "TFCI");

		//puts execution Period to session
		InfoExecutionPeriod infoExecutionPeriod= new InfoExecutionPeriod("2� Semestre",new InfoExecutionYear("2002/2003"));
		getSession().setAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY,infoExecutionPeriod);

		// invoca ac��o
		actionPerform();

		// verifica reencaminhamento
		verifyForward("Sucess");

		//verifica ausencia de erros
		verifyNoActionErrors();
	}

}