package ServidorAplicacao.Servicos.coordinator;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegreeCurricularPlan;
import Dominio.DegreeCurricularPlan;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quit�rio 
 * 10/Nov/2003
 */
public class ReadDegreeCurricularPlanHistoryByExecutionDegreeCodeTest extends ServiceTestCase {

	/**
	 * @param testName
	 */

	public ReadDegreeCurricularPlanHistoryByExecutionDegreeCodeTest(String testName) {
		super(testName);
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadDegreeCurricularPlanHistoryByExecutionDegreeCode";
	}

	protected String getDataSetFilePath() {
		return "etc/testDataSetReadDegreeCurricularPlanByExecutionDegreeCode.xml";
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
		String[] args = { "fiado", "pass", getApplication()};
		return args;
	}
	protected Object[] getAuthorizeArguments() {
		Integer infoExecutionDegreeCode = new Integer(10);

		Object[] args = { infoExecutionDegreeCode };
		return args;
	}
	protected Object[] getExecutionDegreeUnsuccessfullArguments() {
		Integer infoExecutionDegreeCode = new Integer(15);

		Object[] args = { infoExecutionDegreeCode };
		return args;
	}

	public void testReadDegreeCurricularPlanHistoryByExecutionDegreeCodeSuccessfull() {
		try {
			//Argumentos do servi�o
			Integer infoExecutionDegreeCode = new Integer(10);
			Integer degreeCurricularPlanCode = new Integer(10);

			Object[] args = { infoExecutionDegreeCode };

			//Utilizador V�lido
			String[] argsUser = getAuthenticatedAndAuthorizedUser();
			IUserView id = (IUserView) gestor.executar(null, "Autenticacao", argsUser);

			//Execu��o do servi�o
			InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
			infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) gestor.executar(id, getNameOfServiceToBeTested(), args);

			//Leu alguma coisa?
			if (infoDegreeCurricularPlan == null) {
				fail("Reading a Degree Curricular Plan.");
			}

			//Verificar se o que foi lido pelo servi�o est� correcto
			try {

				IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan(degreeCurricularPlanCode);
				IDegreeCurricularPlan degreeCurricularPlan2 = null;
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				sp.iniciarTransaccao();

				degreeCurricularPlan2 = (IDegreeCurricularPlan) sp.getIPersistentDegreeCurricularPlan().readByOId(degreeCurricularPlan, false);

				sp.confirmarTransaccao();

				if (degreeCurricularPlan2 == null) {
					fail("Reading a degree Curricular Plan from database.");
				}

				assertEquals(degreeCurricularPlan2.getName(), infoDegreeCurricularPlan.getName());
				assertEquals(degreeCurricularPlan2.getEndDate(), infoDegreeCurricularPlan.getEndDate());
				assertEquals(degreeCurricularPlan2.getInitialDate(), infoDegreeCurricularPlan.getInitialDate());
				assertEquals(degreeCurricularPlan2.getMarkType(), infoDegreeCurricularPlan.getMarkType());
				assertEquals(
					degreeCurricularPlan2.getMinimalYearForOptionalCourses(),
					infoDegreeCurricularPlan.getMinimalYearForOptionalCourses());
				assertEquals(degreeCurricularPlan2.getNeededCredits(), infoDegreeCurricularPlan.getNeededCredits());
				assertEquals(degreeCurricularPlan2.getNumerusClausus(), infoDegreeCurricularPlan.getNumerusClausus());
				assertEquals(degreeCurricularPlan2.getState(), infoDegreeCurricularPlan.getState());

				assertEquals(new Integer(infoDegreeCurricularPlan.getCurricularCourses().size()), new Integer(1));

				InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) infoDegreeCurricularPlan.getCurricularCourses().get(0);
				assertEquals(new Integer(infoCurricularCourse.getInfoScopes().size()), new Integer(2));

			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				fail("Reading a degree Curricular Plan from database2 " + e);
			}

			System.out.println(
				"testReadDegreeCurricularPlanHistoryByExecutionDegreeCodeSuccessfull was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Reading a degree Curricular Plan from database3 " + e);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree Curricular Plan from database4 " + e);
		}
	}

	public void testReadDegreeCurricularPlanHistoryByExecutionDegreeCodeUserUnsuccessfull() {
		try {
			Integer infoExecutionDegreeCode = new Integer(10);

			Object[] args = { infoExecutionDegreeCode };

			//Invalid user
			String[] argsUser = getAuthenticatedAndUnauthorizedUser();
			IUserView id = (IUserView) gestor.executar(null, "Autenticacao", argsUser);

			gestor.executar(id, getNameOfServiceToBeTested(), args);

			fail("Reading a degree curricular plan with invalid user");
		} catch (NotAuthorizedException e) {
			System.out.println(
				"testReadDegreeCurricularPlanHistoryByExecutionDegreeCodeUserUnsuccessfull was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());
		} catch (FenixServiceException e) {
			fail("Reading a degree curricular plan with invalid user " + e);
		} catch (Exception e) {
			fail("Reading a degree curricular plan with invalid user " + e);
		}
	}

	public void testReadDegreeCurricularPlanHistoryByExecutionDegreeCodeNoDegreeUnsuccessfull3() {
		try {
			//				Non existing execution degree
			Integer infoExecutionDegreeCode = new Integer(55);

			Object[] args = { infoExecutionDegreeCode };

			//Valid user
			String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
			IUserView id2 = (IUserView) gestor.executar(null, "Autenticacao", argsUser2);

			gestor.executar(id2, getNameOfServiceToBeTested(), args);

			fail("Reading a degree Curricular Plan with non existent execution degree");
		} catch (NonExistingServiceException e2) {
			System.out.println(
				"testReadDegreeCurricularPlanHistoryByExecutionDegreeCodeNoDegreeUnsuccessfull3 was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());
		} catch (FenixServiceException e3) {
			fail("Reading a degree Curricular Plan with non existent execution degree " + e3);
		} catch (Exception e) {
			fail("Reading a degree Curricular Plan with non existent execution degree " + e);
		}
	}

	public void testReadActiveDegreeCurricularPlanByExecutionDegreeCodeNullDCPUnsuccessfull() {
		try {
			//	existing execution degree
			Integer infoExecutionDegreeCode = new Integer(17);

			Object[] args = { infoExecutionDegreeCode };

			//Valid user
			String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
			IUserView id2 = (IUserView) gestor.executar(null, "Autenticacao", argsUser2);

			InfoDegreeCurricularPlan infoDegreeCurricularPlan =
				(InfoDegreeCurricularPlan) gestor.executar(id2, getNameOfServiceToBeTested(), args);

			if (infoDegreeCurricularPlan == null) {
				System.out.println(
					"testReadDegreeCurricularPlanHistoryByExecutionDegreeCodeNullDCPUnsuccessfull was SUCCESSFULY runned by service: "
						+ getNameOfServiceToBeTested());
			} else {
				fail("Reading a degree Curricular Plan with non existing degree curricular plan");
			}
		} catch (FenixServiceException e7) {
			fail("Reading a degree Curricular Plan with non existing degree curricular plan " + e7);
		} catch (Exception e) {
			fail("Reading a degree Curricular Plan with non existing degree curricular plan " + e);
		}
	}
	public void testReadDegreeCurricularPlanHistoryByExecutionDegreeCodeNullDegreeUnsuccessfull() {
		try {
			// null argument
			Object[] args = { null };

			//Valid user
			String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
			IUserView id2 = (IUserView) gestor.executar(null, "Autenticacao", argsUser2);

			gestor.executar(id2, getNameOfServiceToBeTested(), args);

			fail("Reading a degree curricular plan with null execution degree code");
		} catch (IllegalArgumentException e1) {
			System.out.println(
				"testReadDegreeCurricularPlanHistoryByExecutionDegreeCodeNullDegreeUnsuccessfull was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());
		} catch (FenixServiceException e4) {
			fail("Reading a degree curricular plan with null execution degree code " + e4);
		} catch (Exception e) {
			fail("Reading a degree curricular plan with null execution degree code " + e);
		}
	}
}
