/*
 * Created on 18/Dec/2003
 *  
 */

package ServidorAplicacao.Servicos.grant.contract;

import java.util.List;

import DataBeans.grant.contract.InfoGrantContract;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.UtilsTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class ReadAllContractsByGrantOwnerTest
    extends ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase
{

    /**
	 * @param testName
	 */
    public ReadAllContractsByGrantOwnerTest(java.lang.String testName)
    {
        super(testName);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServiceNeedsAuthenticationTestCase#getApplication()
	 */
    protected String getApplication()
    {
        return Autenticacao.INTRANET;
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadAllContractByGrantOwner";
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/grant/owner/testReadAllContractsByGrantOwnerDataSet.xml";
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
	 */
    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "16", "pass", getApplication()};
        return args;
    }
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
	 */
    protected String[] getAuthenticatedAndUnauthorizedUser()
    {
        String[] args = { "julia", "pass", getApplication()};
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServiceNeedsAuthenticationTestCase#getNonAuthenticatedUser()
	 */
    protected String[] getNotAuthenticatedUser()
    {
        String[] args = { "fiado", "pass", getApplication()};
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
    protected Object[] getAuthorizeArguments()
    {
        Integer idInternal = new Integer(2);
        Object[] args = { idInternal };
        return args;
    }

    protected Object[] getUnauthorizeArguments()
    {
        Integer idInternal = new Integer(69);
        Object[] args = { idInternal };
        return args;
    }
    /** ********** Inicio dos testes ao servi�o************* */

    /*
	 * Read all GrantContracts of a GrantOwner Successfull
	 */
    public void testReadAllContractsByGrantOwnerSuccessfull()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            List result =
                (List) ServiceManagerServiceFactory.executeService(
                    id,
                    getNameOfServiceToBeTested(),
                    args2);

            //Check that service returns some result
            if (result.size() <= 1)
                fail("Reading all contracts by grantOwner: NO results!!");

            //Check the search result
            Object[] values = { new Integer(1), new Integer(2)};
            UtilsTestCase.readTestList(result, values, "idInternal", InfoGrantContract.class);

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println(
                "testReadAllContractsByGrantOwnerSuccessfull was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());
        } catch (FenixServiceException e)
        {
            fail("Reading all contracts by grantOwner Successfull " + e);
        } catch (Exception e)
        {
            fail("Reading all contracts by grantOwner Successfull " + e);
        }
    }

    /*
	 * Read all GrantContracts of a GrantOwner Unsuccessfull
	 */
    public void testReadAllContractsByGrantOwnerUnsuccessfull()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments();

            List result =
                (List) ServiceManagerServiceFactory.executeService(
                    id,
                    getNameOfServiceToBeTested(),
                    args2);

            //Check that service returns some result
            if (result.size() > 0)
                fail("Reading all contracts by grantOwner: should have NO results!!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println(
                "testReadAllContractsByGrantOwnerUnsuccessfull was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());
        } catch (FenixServiceException e)
        {
            fail("Reading all contracts by grantOwner Unsuccessfull " + e);
        } catch (Exception e)
        {
            fail("Reading all contracts by grantOwner Unsuccessfull " + e);
        }
    }
}