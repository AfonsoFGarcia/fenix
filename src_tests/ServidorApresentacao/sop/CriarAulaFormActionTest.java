package ServidorApresentacao.sop;
import java.util.ArrayList;
import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.TestCasePresentation;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.DiaSemana;
import Util.TipoAula;
/**
 * @author tfc130
 */
public class CriarAulaFormActionTest extends TestCasePresentation {
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CriarAulaFormActionTest.class);

		return suite;
	}
	public void setUp() throws Exception {
		super.setUp();
		// define ficheiro de configuracao Struts a utilizar
		setServletConfigFile("/WEB-INF/tests/web-sop.xml");

	}

	public CriarAulaFormActionTest(String testName) {
		super(testName);
	}

	public void testSuccessfulCriarAula() {
		// define mapping de origem
		setRequestPathInfo("", "/criarAulaForm");
		// Preenche campos do formulario
		addRequestParameter(
			"diaSemana",
			new Integer(DiaSemana.SEGUNDA_FEIRA).toString());
		addRequestParameter("horaInicio", new Integer(18).toString());
		addRequestParameter("minutosInicio", new Integer(0).toString());
		addRequestParameter("horaFim", new Integer(19).toString());
		addRequestParameter("minutosFim", new Integer(30).toString());
		addRequestParameter(
			"tipoAula",
			(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter("courseInitials", "TFCI");
		addRequestParameter("nomeSala", "Ga1");
		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("CriarAula");
		privilegios.add(
			"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);

		try {
			InfoDegree iL =
				new InfoDegree(
					"LEIC",
					"Licenciatura de Engenharia Informatica e de Computadores");
			InfoExecutionDegree iLE =
				new InfoExecutionDegree(
					new InfoDegreeCurricularPlan("plano1", iL),
					new InfoExecutionYear("2002/2003"));
			Object argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular[] =
				{
					 new CurricularYearAndSemesterAndInfoExecutionDegree(
						new Integer(5),
						new Integer(1),
						iLE)};
			getSession().setAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_KEY,
				iLE);

			InfoExecutionPeriod executionPeriod =
				new InfoExecutionPeriod(
					"2� Semestre",
					new InfoExecutionYear("2002/2003"));
			getSession().setAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY,
				executionPeriod);

			getSession().setAttribute(
				SessionConstants.CURRICULAR_YEAR_KEY,
				new Integer(2));

			GestorServicos gestor = GestorServicos.manager();
			ArrayList iDE =
				(ArrayList) gestor.executar(
					userView,
					"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular",
					argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular);
			getSession().setAttribute(
				SessionConstants.EXECUTION_COURSE_LIST_KEY,
				iDE);
		} catch (Exception ex) {
			System.out.println("Erro na invocacao do servico " + ex);
		}

		// invoca acaoo
		actionPerform();

		//verifica ausencia de erros
		verifyNoActionErrors();

		// verifica reencaminhamento
		verifyForward("Sucesso");
	}

	public void testUnsuccessfulCriarAula() {
		setRequestPathInfo("", "/criarAulaForm");
		addRequestParameter(
			"diaSemana",
			new Integer(DiaSemana.SEGUNDA_FEIRA).toString());
		addRequestParameter("horaInicio", new Integer(8).toString());
		addRequestParameter("minutosInicio", new Integer(0).toString());
		addRequestParameter("horaFim", new Integer(9).toString());
		addRequestParameter("minutosFim", new Integer(30).toString());
		addRequestParameter(
			"tipoAula",
			(new Integer(TipoAula.TEORICA)).toString());
		addRequestParameter("courseInitials", "TFCI");
		addRequestParameter("nomeSala", "Ga1");
		// coloca credenciais na sessao
		HashSet privilegios = new HashSet();
		privilegios.add("CriarAula");
		privilegios.add(
			"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular");
		IUserView userView = new UserView("user", privilegios);
		getSession().setAttribute("UserView", userView);
		try {
			InfoDegree iL =
				new InfoDegree(
					"LEIC",
					"Licenciatura de Engenharia Informatica e de Computadores");
			InfoExecutionDegree iLE =
				new InfoExecutionDegree(
					new InfoDegreeCurricularPlan("plano1", iL),
					new InfoExecutionYear("2002/2003"));
			Object argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular[] =
				{
					 new CurricularYearAndSemesterAndInfoExecutionDegree(
						new Integer(5),
						new Integer(1),
						iLE)};
			getSession().setAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_KEY,
				iLE);

			InfoExecutionPeriod executionPeriod =
							new InfoExecutionPeriod(
								"2� Semestre",
								new InfoExecutionYear("2002/2003"));
						getSession().setAttribute(
							SessionConstants.INFO_EXECUTION_PERIOD_KEY,
							executionPeriod);

						getSession().setAttribute(
							SessionConstants.CURRICULAR_YEAR_KEY,
							new Integer(2));

			GestorServicos gestor = GestorServicos.manager();
			ArrayList iDE =
				(ArrayList) gestor.executar(
					userView,
					"LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular",
					argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular);
			getSession().setAttribute("infoDisciplinasExecucao", iDE);
		} catch (Exception ex) {
			System.out.println("Erro na invocacao do servico " + ex);
		}
		actionPerform();
		verifyForwardPath("/naoExecutado.do");

		verifyActionErrors(
			new String[] { "ServidorAplicacao.NotExecutedException" });
	}

}