package ServidorApresentacao.sop;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.TestCasePresentationSopPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author tfc130
 *
 */
public class EscolherContextoFormActionTest extends TestCasePresentationSopPortal {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EscolherContextoFormActionTest.class);

		return suite;
	}

	public void setUp() {
		super.setUp();
		// define ficheiro de configura��o Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-sop.xml");
	}

	public EscolherContextoFormActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulEscolherContexto() {
		
		
		// define mapping de origem
		setRequestPathInfo("", "/escolherContextoForm");

		// Preenche campos do formul�rio
		addRequestParameter("sigla", "LEIC");
		addRequestParameter("anoCurricular", "1");
		addRequestParameter("index", "0");

		// coloca credenciais na sess�o
setAuthorizedUser();
		//puts executionPeriod in session
		InfoExecutionPeriod executionPeriod =
			new InfoExecutionPeriod(
				"2� Semestre",
				new InfoExecutionYear("2002/2003"));
		getSession().setAttribute(
			SessionConstants.INFO_EXECUTION_PERIOD_KEY,
			executionPeriod);

		//	puts INFO_EXECUTION_DEGREE_LIST_KEY in session
		try {
			GestorServicos gestor = GestorServicos.manager();
			Object argsReadExecutionDegrees[] =
				{ new InfoExecutionYear("2002/2003")};
			ArrayList executionCourses =
				(ArrayList) gestor.executar(
					getAuthorizedUser(),
					"ReadExecutionDegreesByExecutionYear",
					argsReadExecutionDegrees);

			getSession().setAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY,
				executionCourses);
		} catch (Exception e) {
			System.out.println("Erro na invocacao do servico " + e);
		}

		// invoca action
		actionPerform();

		//verifica ausencia de erros
		verifyNoActionErrors();

		// verifica reencaminhamento
		verifyForward("Sucesso");

	}

	

}