/*
 * Created on 12/Nov/2003
 */

package ServidorAplicacao.Servicos.person.qualification;

import DataBeans.InfoPerson;
import DataBeans.person.InfoQualification;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;

/**
 * @author Barbosa
 * @author Pica
 */

public class ReadQualificationTest extends QualificationServiceNeedsAuthenticationTestCase
{

	public ReadQualificationTest(java.lang.String testName)
	{
		super(testName);
	}

	/*
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getApplication()
	 */
	protected String getApplication()
	{
		return Autenticacao.EXTRANET;
	}
	/*
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested()
	{
		return "ReadQualification";
	}
	/*
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
	 */
	protected String getDataSetFilePath()
	{
		return "etc/datasets/servicos/person/qualification/testReadQualificationDataSet.xml";
	}
	/*
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizedUser_GrantOwnerManager()
	 */
	protected String[] getAuthorizedUserGrantOwnerManager()
	{
		String[] args = { "user_gom", "pass", getApplication()};
		return args;
	}
	/*
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizedUser_Teacher()
	 */
	protected String[] getAuthorizedUserTeacher()
	{
		String[] args = { "user_t", "pass", getApplication()};
		return args;
	}
	/*
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getUnauthorizedUser()
	 */
	protected String[] getUnauthorizedUser()
	{
		String[] args = { "julia", "pass", getApplication()};
		return args;
	}
	/*
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizeArguments_GrantOwnerManager()
	 */
	protected Object[] getAuthorizeArgumentsGrantOwnerManager()
	{
		//A Grant Owner qualification
		InfoQualification info = new InfoQualification();
		info.setIdInternal(new Integer(1));
		info.setPersonInfo(getInfoPersonGO());

		Integer infoManagerPersonCode = new Integer(17);

		Object[] args = { infoManagerPersonCode, info };
		return args;
	}
	/*
	 * @see ServidorAplicacao.Servicos.person.QualificationServiceNeedsAuthenticationTestCase#getAuthorizeArguments_Teacher()
	 */
	protected Object[] getAuthorizeArgumentsTeacher()
	{
		//A teacher qualification
		InfoQualification info = new InfoQualification();
		info.setIdInternal(new Integer(4));
		info.setPersonInfo(getInfoPersonT());

		Integer infoManagerPersonCode = new Integer(18);

		Object[] args = { infoManagerPersonCode, info };
		return args;
	}

	/**
	 * 
	 * Start of the tests
	 *  
	 */

	/**
	 * A Grant Owner Manager reads a qualification of a Grant Owner
	 */
	public void testReadQualificationGOSuccessfull()
	{
		try
		{
			String[] args = getAuthorizedUserGrantOwnerManager();
			IUserView user = authenticateUser(args);
			Object[] argserv = getAuthorizeArgumentsGrantOwnerManager();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedReadQualificationDataSet.xml");

			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (FenixServiceException e)
		{
			fail("Reading a Qualification for GrantOwner: " + e);
		} catch (Exception e)
		{
			fail("Reading a Qualification for GrantOwner: " + e);
		}
	}

	/**
	 * A Teacher reads a qualification of his own
	 */
	public void testReadQualificationTSuccessfull()
	{
		try
		{
			String[] args = getAuthorizedUserTeacher();
			IUserView user = authenticateUser(args);
			Object[] argserv = getAuthorizeArgumentsTeacher();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedReadQualificationDataSet.xml");

			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (FenixServiceException e)
		{
			fail("Reading a Qualification for Teacher: " + e);
		} catch (Exception e)
		{
			fail("Reading a Qualification for Teacher: " + e);
		}
	}

	/**
	 * Valid user(teacher), but wrong arguments(Grant Owner)
	 */
	public void testReadQualificationUnsuccessfull1()
	{
		try
		{
			String[] args = getAuthorizedUserTeacher();
			IUserView user = authenticateUser(args);
			Object[] argserv = getAuthorizeArgumentsGrantOwnerManager();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			fail("ReadQualificationUnsuccessfull.");

		} catch (NotAuthorizedException e)
		{
			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedReadQualificationDataSet.xml");
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (FenixServiceException e)
		{
			fail("ReadQualificationUnsuccessfull: " + e);
		} catch (Exception e)
		{
			fail("ReadQualificationUnsuccessfull: " + e);
		}
	}

	/**
	 * Valid user(Grant Owner), but wrong arguments(Teacher)
	 */
	public void testReadQualificationUnsuccessfull2()
	{
		try
		{
			String[] args = getAuthorizedUserGrantOwnerManager();
			IUserView user = authenticateUser(args);
			Object[] argserv = getAuthorizeArgumentsTeacher();

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			fail("ReadQualificationUnsuccessfull.");

		} catch (NotAuthorizedException e)
		{
			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedReadQualificationDataSet.xml");
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (FenixServiceException e)
		{
			fail("ReadQualificationUnsuccessfull: " + e);
		} catch (Exception e)
		{
			fail("ReadQualificationUnsuccessfull: " + e);
		}
	}

	/**
	 * Valid user, but wrong arguments (editing a qualification that does't exists)
	 */
	public void testReadQualificationUnsuccessfull3()
	{
		try
		{
			String[] args = getAuthorizedUserGrantOwnerManager();
			IUserView user = authenticateUser(args);
			Object[] argserv = getAuthorizeArgumentsGrantOwnerManager();

			//Invalid qualification
			 ((InfoQualification) argserv[1]).setIdInternal(new Integer(1220));

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			fail("ReadQualificationUnsuccessfull.");

		} catch (NotAuthorizedException e)
		{
			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedReadQualificationDataSet.xml");
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (FenixServiceException e)
		{
			fail("ReadQualificationUnsuccessfull: " + e);
		} catch (Exception e)
		{
			fail("ReadQualificationUnsuccessfull: " + e);
		}
	}

	/**
	 * Valid user, but wrong arguments (qualification id is null)
	 */
	public void testReadQualificationUnsuccessfull4()
	{
		try
		{
			String[] args = getAuthorizedUserGrantOwnerManager();
			IUserView user = authenticateUser(args);
			Object[] argserv = getAuthorizeArgumentsGrantOwnerManager();

			//Invalid qualification
			 ((InfoQualification) argserv[1]).setIdInternal(null);

			gestor.executar(user, getNameOfServiceToBeTested(), argserv);

			fail("Read Qualification Unsuccessfull.");

		} catch (NotAuthorizedException e)
		{
			fail("ReadQualificationUnsuccessfull: " + e);
		} catch (FenixServiceException e)
		{
			compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/person/qualification/testExpectedReadQualificationDataSet.xml");
			System.out.println(
				getNameOfServiceToBeTested()
					+ " was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (Exception e)
		{
			fail("ReadQualificationUnsuccessfull: " + e);
		}

	}

	/**
	 * 
	 * End of the tests 
	 * 
	 */
	
	//Return a valid GrantOwner Manager user
	protected InfoPerson getInfoPersonGO()
	{
		InfoPerson info = new InfoPerson();
		info.setIdInternal(new Integer(14));
		info.setUsername("user_gom");
		return info;
	}
	//Return a valid Teacher user
	protected InfoPerson getInfoPersonT()
	{
		InfoPerson info = new InfoPerson();
		info.setIdInternal(new Integer(18));
		info.setUsername("user_t");
		return info;
	}

}