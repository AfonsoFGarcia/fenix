/*
 * Created on 23/Jan/2004
 *
 */
package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.gratuity;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;

/**
 * @author T�nia Pous�o
 *
 */
public class ReadGratuitySituationListByExecutionDegreeAndSpecializationTest
	extends AdministrativeOfficeBaseTest
{

	/**
	 * @param name
	 */
	public ReadGratuitySituationListByExecutionDegreeAndSpecializationTest(String name)
	{
		super(name);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest#getServiceArgumentsForNotAuthenticatedUser()
	 */
	protected Object[] getServiceArgumentsForNotAuthenticatedUser() throws FenixServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest#getServiceArgumentsForNotAuthorizedUser()
	 */
	protected Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
