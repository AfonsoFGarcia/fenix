package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoSiteSummary;
import DataBeans.InfoSummary;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ISummary;
import Dominio.Summary;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Leonor Almeida
 * @author S�rgio Montelobo
 */
public class ReadSummaryTest extends SummaryBelongsExecutionCourseTestCase {

	/**
	 * @param testName
	 */
	public ReadSummaryTest(String testName) {
		super(testName);
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/testReadSummaryDataSet.xml";
	}

	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/testExpectedReadSummaryDataSet.xml";
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadSummary";
	}

	protected String[] getAuthenticatedAndAuthorizedUser() {

		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getAuthenticatedAndUnauthorizedUser() {

		String[] args = { "julia", "pass", getApplication()};
		return args;
	}

	protected String[] getNotAuthenticatedUser() {

		String[] args = { "jccm", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {

		Integer executionCourseId = new Integer(24);
		Integer summaryId = new Integer(261);

		Object[] args = { executionCourseId, summaryId };
		return args;
	}

	protected Object[] getTestSummaryUnsuccessfullArguments() {

		Integer executionCourseId = new Integer(25);
		Integer summaryId = new Integer(261);

		Object[] args = { executionCourseId, summaryId };
		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	public void testSuccessfull() {

		try {

			SiteView result = null;

			String[] args = getAuthenticatedAndAuthorizedUser();
			IUserView userView = authenticateUser(args);

			ISummary newSummary = (ISummary) new Summary(new Integer(261));
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IPersistentSummary persistentSummary = sp.getIPersistentSummary();
			newSummary =
				(ISummary) persistentSummary.readByOId(newSummary, false);
			sp.confirmarTransaccao();

			result =
				(SiteView) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					getAuthorizeArguments());

			InfoSiteSummary infoSiteSummary =
				(InfoSiteSummary) result.getComponent();
			InfoSummary infoSummary = infoSiteSummary.getInfoSummary();
			ISummary oldSummary = Cloner.copyInfoSummary2ISummary(infoSummary);

			assertTrue(newSummary.compareTo(oldSummary));
			// verifica se a base de dados nao foi alterada
			compareDataSet(getExpectedDataSetFilePath());
		}
		catch (FenixServiceException ex) {
			fail("Reading the Summary of a Site" + ex);
		}
		catch (Exception ex) {
			fail("Reading the Summary of a Site" + ex);
		}
	}
}