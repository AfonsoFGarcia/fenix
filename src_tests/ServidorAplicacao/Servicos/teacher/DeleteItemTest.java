package ServidorAplicacao.Servicos.teacher;

import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;

/**
 * @author  Luis Egidio, lmre@mega.ist.utl.pt
 * 			Nuno Ochoa,  nmgo@mega.ist.utl.pt
 *
 */
public class DeleteItemTest extends ItemBelongsExecutionCourseTest {

	/**
	 * @param testName
	 */
	public DeleteItemTest(String testName) {
		super(testName);
	}

	protected String getNameOfServiceToBeTested() {
		return "DeleteItem";
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/testDeleteItemDataSet.xml";
	}

	protected String[] getAuthorizedUser() {
		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getUnauthorizedUser() {
		String[] args = { "3", "pass", getApplication()};
		return args;
	}

	protected String[] getNonTeacherUser() {
		String[] args = { "13", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {
		Object[] args = { new Integer(27), new Integer(1)};
		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	/*
	 * Refers to the test in the upper class, ItemBelongExecutionCourseTest
	 */
	protected Object[] getTestItemSuccessfullArguments() {
		Object[] args = { new Integer(27), new Integer(1)};
		return args;
	}

	/*
	 * Refers to the test in the upper class, ItemBelongExecutionCourseTest
	 */
	protected Object[] getTestItemUnsuccessfullArguments() {
		Object[] args = { new Integer(27), new Integer(2)};
		return args;
	}

	public void testDeleteExistingItem() {
		Object[] args = getTestItemSuccessfullArguments();
		Object result = null;

		try {
			result =
				gestor.executar(userView, getNameOfServiceToBeTested(), args);

			compareDataSet("etc/datasets/testExpectedDeleteItemDataSet.xml");
			System.out.println(
				"testDeleteExistingItem was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testDeleteExistingItem was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testDeleteExistingItem");
		}
	}

	public void testDeleteNonExistingItem() {
		Object[] args = { new Integer(27), new Integer(100)};

		try {
			gestor.executar(userView, getNameOfServiceToBeTested(), args);
			System.out.println(
				"testDeleteNonExistingItem was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testDeleteNonExistingItem");

		} catch (NotAuthorizedException e) {
			System.out.println(
				"testDeleteNonExistingItem was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testDeleteNonExistingItem was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testDeleteNonExistingItem");
		}
	}

}
