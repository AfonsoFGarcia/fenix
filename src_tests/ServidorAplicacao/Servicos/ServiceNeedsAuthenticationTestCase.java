/*
 * Created on 7/Out/2003
 *  
 */
package ServidorAplicacao.Servicos;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 *  
 */
public abstract class ServiceNeedsAuthenticationTestCase extends ServiceTestCase
{

	protected IUserView userView = null;
	protected IUserView userView2 = null;
	protected IUserView userView3 = null;

	protected ServiceNeedsAuthenticationTestCase(String name)
	{
		super(name);
	}

	protected void setUp()
	{
		super.setUp();
		userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
		userView2 = authenticateUser(getAuthenticatedAndUnauthorizedUser());
		userView3 = authenticateUser(getNotAuthenticatedUser());

	}

	public void testAuthorizedUser()
	{
		Object serviceArguments[] = getAuthorizeArguments();

		try
		{
			Object result = gestor.executar(userView, getNameOfServiceToBeTested(), serviceArguments);
			assertAuthorizedResult(result);
		} catch (Throwable ex)
		{
			ex.printStackTrace(System.out);
			fail(getNameOfServiceToBeTested() + ": fail testAuthorizedUser");
		}
	}

	public void testUnauthorizedUser()
	{
		Object serviceArguments[] = getAuthorizeArguments();
		Object result = null;
		try
		{
			result = gestor.executar(userView2, getNameOfServiceToBeTested(), serviceArguments);
			fail(this.getClass().getName() + ": Service " + getNameOfServiceToBeTested() + ": fail testUnauthorizedUser");
		} catch (NotAuthorizedException ex)
		{
			ex.printStackTrace(System.out);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			fail("Test class:" + this.getClass().getName() + "Unable to run service: " + getNameOfServiceToBeTested());
		}
	}

	/**
	 * This method by default does nothing. This should be used to assert the
	 * result from an authorized service call.
	 * 
	 * @param result
	 *                   Represents the result from service execution.
	 */
	protected void assertAuthorizedResult(Object result)
	{}

	public void testNonAuthenticatedUser()
	{
		Object serviceArguments[] = getAuthorizeArguments();

		try
		{
			gestor.executar(userView3, getNameOfServiceToBeTested(), serviceArguments);
			fail(this.getClass().getName() + ": Service " + getNameOfServiceToBeTested() + "fail testNonAuthenticatedUser");
		} catch (NotAuthorizedException ex)
		{
			ex.printStackTrace();
			System.out.println("testNonAuthenticatedUser was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());

		} catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println("testNonAuthenticatedUser was UNSUCCESSFULY runned by service: " + getNameOfServiceToBeTested());
			fail("Unable to run service: " + getNameOfServiceToBeTested());
		}
	}

	protected IUserView authenticateUser(String[] arguments)
	{
		SuportePersistenteOJB.resetInstance();
		String args[] = arguments;

		try
		{
			return (IUserView) gestor.executar(null, "Autenticacao", args);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			fail("Authenticating User!" + ex);
			return null;
		}
	}

	protected abstract String getNameOfServiceToBeTested();
	protected abstract String getDataSetFilePath();

	protected abstract String[] getAuthenticatedAndAuthorizedUser();
	protected abstract String[] getAuthenticatedAndUnauthorizedUser();
	protected abstract String[] getNotAuthenticatedUser();

	protected abstract Object[] getAuthorizeArguments();
	protected abstract String getApplication();
}
