package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.thesis;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExternalPerson;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoTeacher;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * 
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class CreateMasterDegreeThesisTest extends ServiceTestCase {

	private GestorServicos serviceManager = GestorServicos.manager();
	private IUserView userView = null;
	private String dataSetFilePath;

	/**
	 * @param testName
	 */
	public CreateMasterDegreeThesisTest(String testName) {
		super(testName);
		if (testName.equals("testCreateMasterDegreeThesisWithExistingMasterDegreeThesis")
			|| testName.equals("testCreateMasterDegreeThesisWithExistingDissertationTitle")) {
			this.dataSetFilePath =
				"etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testCreateMasterDegreeThesisDataSetExistingMasterDegreeThesis.xml";
		} else {
			this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testCreateMasterDegreeThesisDataSet.xml";
		}
	}

	protected void setUp() {
		super.setUp();
		this.userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
	}

	/**
	 * @param strings
	 * @return
	 */
	private IUserView authenticateUser(String[] args) {
		SuportePersistenteOJB.resetInstance();

		try {
			return (IUserView) gestor.executar(null, "Autenticacao", args);
		} catch (Exception ex) {
			fail("Authenticating User!" + ex);
			return null;
		}
	}

	protected String getNameOfServiceToBeTested() {
		return "CreateMasterDegreeThesis";
	}

	protected String getDataSetFilePath() {
		return this.dataSetFilePath;
	}

	protected String[] getAuthenticatedAndAuthorizedUser() {
		String[] args = { "f3667", "pass", getApplication()};
		return args;
	}

	protected String getApplication() {
		return Autenticacao.INTRANET;
	}

	public void testSuccessfulCreateMasterDegreeThesis() {
		try {
			Object[] argsReadStudentCurricularPlan = { new Integer(209), new TipoCurso(TipoCurso.MESTRADO)};
			InfoStudentCurricularPlan infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) serviceManager.executar(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsReadStudentCurricularPlan);

			Object[] argsSearch = { "Xxx" };
			List infoExternalPersons = (List) serviceManager.executar(userView, "SearchExternalPersonsByName", argsSearch);

			InfoTeacher infoTeacherGuider = new InfoTeacher();
			infoTeacherGuider.setIdInternal(new Integer(954));
			InfoTeacher infoTeacherAssistent = new InfoTeacher();
			infoTeacherAssistent.setIdInternal(new Integer(955));
			InfoExternalPerson infoExternalPerson = new InfoExternalPerson();
			infoExternalPerson.setIdInternal(new Integer(1));

			List guiders = new ArrayList();
			List assistentGuiders = new ArrayList();
			List externalAssistentGuiders = new ArrayList();

			guiders.add(infoTeacherGuider);
			assistentGuiders.add(infoTeacherAssistent);
			externalAssistentGuiders.add(infoExternalPerson);

			Object[] argsCreateMasterDegreeThesis =
				{ userView, infoStudentCurricularPlan, "some title", guiders, assistentGuiders, externalAssistentGuiders };

			serviceManager.executar(this.userView, getNameOfServiceToBeTested(), argsCreateMasterDegreeThesis);
			compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testExpectedCreateMasterDegreeThesisDataSet.xml");
			//ok

		} catch (Exception ex) {
			ex.printStackTrace();
			fail("testSuccessfulCreateMasterDegreeThesis" + ex.getMessage());
		}
	}

	public void testCreateMasterDegreeThesisWithExistingMasterDegreeThesis() {
		try {
			Object[] argsReadStudentCurricularPlan = { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
			InfoStudentCurricularPlan infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) serviceManager.executar(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsReadStudentCurricularPlan);

			InfoTeacher infoTeacherGuider = new InfoTeacher();
			infoTeacherGuider.setIdInternal(new Integer(954));
			InfoTeacher infoTeacherAssistent = new InfoTeacher();
			infoTeacherAssistent.setIdInternal(new Integer(955));
			InfoExternalPerson infoExternalPerson = new InfoExternalPerson();
			infoExternalPerson.setIdInternal(new Integer(1));

			List guiders = new ArrayList();
			List assistentGuiders = new ArrayList();
			List externalAssistentGuiders = new ArrayList();

			guiders.add(infoTeacherGuider);
			assistentGuiders.add(infoTeacherAssistent);
			externalAssistentGuiders.add(infoExternalPerson);

			Object[] argsCreateMasterDegreeThesis =
				{ userView, infoStudentCurricularPlan, "Existing Title", guiders, assistentGuiders, externalAssistentGuiders };

			serviceManager.executar(this.userView, getNameOfServiceToBeTested(), argsCreateMasterDegreeThesis);
			fail("testCreateMasterDegreeThesisWithExistingMasterDegreeThesis: Service did not throw ExistingServiceException");

		} catch (ExistingServiceException ex) {
			//test passed
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("testCreateMasterDegreeThesisWithExistingMasterDegreeThesis" + ex.getMessage());
		}
	}

	public void testCreateMasterDegreeThesisWithExistingDissertationTitle() {
		try {
			Object[] argsReadStudentCurricularPlan = { new Integer(209), new TipoCurso(TipoCurso.MESTRADO)};
			InfoStudentCurricularPlan infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) serviceManager.executar(
					userView,
					"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
					argsReadStudentCurricularPlan);

			Object[] argsSearch = { "Xxx" };
			List infoExternalPersons = (List) serviceManager.executar(userView, "SearchExternalPersonsByName", argsSearch);

			InfoTeacher infoTeacherGuider = new InfoTeacher();
			infoTeacherGuider.setIdInternal(new Integer(954));
			InfoTeacher infoTeacherAssistent = new InfoTeacher();
			infoTeacherAssistent.setIdInternal(new Integer(955));
			InfoExternalPerson infoExternalPerson = new InfoExternalPerson();
			infoExternalPerson.setIdInternal(new Integer(1));

			List guiders = new ArrayList();
			List assistentGuiders = new ArrayList();
			List externalAssistentGuiders = new ArrayList();

			guiders.add(infoTeacherGuider);
			assistentGuiders.add(infoTeacherAssistent);
			externalAssistentGuiders.add(infoExternalPerson);

			Object[] argsCreateMasterDegreeThesis =
				{ userView, infoStudentCurricularPlan, "Existing Title", guiders, assistentGuiders, externalAssistentGuiders };
			serviceManager.executar(this.userView, getNameOfServiceToBeTested(), argsCreateMasterDegreeThesis);

			fail("testCreateMasterDegreeThesisWithExistingDissertationTitle did not throw ExistingServiceException");

		} catch (ExistingServiceException ex) {
			//test passed
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("testCreateMasterDegreeThesisWithExistingDissertationTitle" + ex.getMessage());
		}
	}

}
