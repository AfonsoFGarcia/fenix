/*
 * Created on 6/Nov/2003
 *
 */
package ServidorAplicacao.Servicos.coordinator;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 *fenix-head
 *ServidorAplicacao.Servicos.coordinator
 * @author Jo�o Mota
 *6/Nov/2003
 *
 */
public abstract class CoordinatorBelongsToExecutionDegree
	extends ServiceNeedsAuthenticationTestCase {

	/**
	 * @param name
	 */
	protected CoordinatorBelongsToExecutionDegree(String name) {
		super(name);
	}

	protected abstract String[] getAuthenticatedAndAuthorizedUser();
	protected abstract String[] getAuthenticatedAndUnauthorizedUser();
	protected abstract String[] getNotAuthenticatedUser();
	protected abstract String getNameOfServiceToBeTested();
	protected abstract Object[] getAuthorizeArguments();
	protected abstract Object[] getNonAuthorizeArguments();
	protected abstract String getDataSetFilePath();
	protected abstract String getApplication();

	protected void setUp() {
		super.setUp();
	}

	protected IUserView authenticateUser(String[] arguments) {
		SuportePersistenteOJB.resetInstance();
		String args[] = arguments;
		try {
			return (IUserView) gestor.executar(null, "Autenticacao", args);
		} catch (Exception ex) {
			fail("Authenticating User!" + ex);
			return null;
		}
	}

	public void testNonCoordinatorUser() {
		Object serviceArguments[] = getAuthorizeArguments();
		try {
			gestor.executar(userView3, getNameOfServiceToBeTested(), serviceArguments);
			System.out.println(
				"testNonCoordinatorUser was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail(getNameOfServiceToBeTested() + "fail testNonCoordinatorUser");

		} catch (NotAuthorizedException ex) {
			System.out.println(
				"testNonCoordinatorUser was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		} catch (Exception ex) {
			System.out.println(
				"testNonCoordinatorUser was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail("Unable to run service: " + getNameOfServiceToBeTested());
		}
	}

	public void testUnauthorizedUser() {
		Object serviceArguments[] = getAuthorizeArguments();
		try {
			gestor.executar(userView2, getNameOfServiceToBeTested(), serviceArguments);
			System.out.println(
				"testUnauthorizedUser was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail(getNameOfServiceToBeTested() + "fail testUnauthorizedUser");
		} catch (NotAuthorizedException ex) {
			System.out.println(
				"testUnauthorizedUser was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		} catch (Exception ex) {
			System.out.println(
				"testUnauthorizedUser was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail("Unable to run service: " + getNameOfServiceToBeTested());

		}
	}

	public void testAuthorizedUser() {
		Object serviceArguments[] = getAuthorizeArguments();
		try {
			gestor.executar(userView, getNameOfServiceToBeTested(), serviceArguments);
			System.out.println(
				"testAuthorizedUser was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		} catch (NotAuthorizedException ex) {
			System.out.println(
				"testAuthorizedUser was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail(getNameOfServiceToBeTested() + "fail testAuthorizedUser");
		} catch (Exception ex) {
			System.out.println(
				"testAuthorizedUser was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail("Unable to run service: " + getNameOfServiceToBeTested());

		}
	}

	public void testCoordinatorNotBelongsToExecutionDegree() {
		Object serviceArguments[] = getNonAuthorizeArguments();
		try {
			gestor.executar(userView, getNameOfServiceToBeTested(), serviceArguments);
			System.out.println(
				"testCoordinatorNotBelongsToExecutionDegree was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail(getNameOfServiceToBeTested() + "fail testCoordinatorNotBelongsToExecutionDegree");
		} catch (NotAuthorizedException ex) {
			System.out.println(
				"testCoordinatorNotBelongsToExecutionDegree was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		} catch (Exception ex) {
			System.out.println(
				"testCoordinatorNotBelongsToExecutionDegree was UNSUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
			fail("Unable to run service: " + getNameOfServiceToBeTested());

		}
	}
}
