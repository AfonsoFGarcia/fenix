package ServidorAplicacao.Servicos.teacher;

import java.util.List;

import DataBeans.InfoSiteSummaries;
import DataBeans.SiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import Util.TipoAula;

/**
 * @author Leonor Almeida
 * @author S�rgio Montelobo
 */
public class ReadSummariesTest extends ServiceNeedsAuthenticationTestCase {

	/**
	 * @param testName
	 */
	public ReadSummariesTest(String testName) {
		super(testName);
	}

	protected String getDataSetFilePath() {
		return "etc/testReadSummariesDataSet.xml";
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadSummaries";
	}

	protected String[] getAuthorizedUser() {

		String[] args = { "user", "pass", getApplication() };
		return args;
	}

	protected String[] getUnauthorizedUser() {

		String[] args = { "julia", "pass", getApplication() };
		return args;
	}

	protected String[] getNonTeacherUser() {

		String[] args = { "jccm", "pass", getApplication() };
		return args;
	}

	protected Object[] getAuthorizeArguments() {

		Integer executionCourseId = new Integer(24);
		int tipoAula = TipoAula.TEORICA;
		TipoAula summaryType = new TipoAula(tipoAula);

		Object[] args = { executionCourseId, summaryType };
		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	public void testSuccessfullCourseWithSummaries() {

		try {
			SiteView result = null;
			Integer executionCourseId = new Integer(24);
			int tipoAula = TipoAula.TEORICA;
			TipoAula summaryType = new TipoAula(tipoAula);

			String[] args1 = getAuthorizedUser();
			IUserView userView = authenticateUser(args1);

			Object[] args2 = { executionCourseId, summaryType };

			result =
				(SiteView) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					args2);

			InfoSiteSummaries infoSiteSummaries =
				(InfoSiteSummaries) result.getComponent();
			assertEquals(infoSiteSummaries.getSummaryType(), summaryType);
			List infoSummaries = infoSiteSummaries.getInfoSummaries();
			assertEquals(infoSummaries.size(), 2);
			// verifica se a base de dados nao foi alterada
			compareDataSet(getDataSetFilePath());
		} catch (FenixServiceException ex) {
			fail("Reading the Summaries of a Site" + ex);
		} catch (Exception ex) {
			fail("Reading the Summaries of a Site" + ex);
		}
	}

	public void testSuccessfullCourseWithoutSummaries() {

		try {
			SiteView result = null;
			Integer executionCourseId = new Integer(24);
			int tipoAula = TipoAula.DUVIDAS;
			TipoAula summaryType = new TipoAula(tipoAula);

			String[] args1 = getAuthorizedUser();
			IUserView userView = authenticateUser(args1);

			Object[] args2 = { executionCourseId, summaryType };

			result =
				(SiteView) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					args2);

			InfoSiteSummaries infoSiteSummaries =
				(InfoSiteSummaries) result.getComponent();
			assertEquals(infoSiteSummaries.getSummaryType(), summaryType);
			List infoSummaries = infoSiteSummaries.getInfoSummaries();
			assertEquals(infoSummaries.size(), 0);
			// verifica se a base de dados nao foi alterada
			compareDataSet(getDataSetFilePath());
		} catch (FenixServiceException ex) {
			fail("Reading the Summaries of a Site" + ex);
		} catch (Exception ex) {
			fail("Reading the Summaries of a Site" + ex);
		}
	}
}