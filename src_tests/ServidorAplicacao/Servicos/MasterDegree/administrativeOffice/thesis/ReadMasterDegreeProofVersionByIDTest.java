package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.thesis;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoMasterDegreeProofVersion;
import DataBeans.InfoTeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import Util.MasterDegreeClassification;
import Util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *               (naat@mega.ist.utl.pt)
 */
public class ReadMasterDegreeProofVersionByIDTest extends AdministrativeOfficeBaseTest
{

    /**
     * @param testName
     */
    public ReadMasterDegreeProofVersionByIDTest(String testName)
    {
        super(testName);
        this.dataSetFilePath =
            "etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testReadMasterDegreeProofVersionByIDDataSet.xml";
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadMasterDegreeProofVersionByID";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() throws FenixServiceException
    {
        Object[] argsReadMasterDegreeProofVersion = { null };

        return argsReadMasterDegreeProofVersion;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException
    {
        Object[] argsReadMasterDegreeProofVersion = { new Integer(1)};

        return argsReadMasterDegreeProofVersion;
    }

    public void testReadExistingMasterDegreeProofVersion()
    {
        try
        {
            Object[] argsReadMasterDegreeProofVersion = { new Integer(1)};
            InfoMasterDegreeProofVersion infoMasterDegreeProofVersion =
                (InfoMasterDegreeProofVersion) ServiceManagerServiceFactory.executeService(
                    userView,
                    getNameOfServiceToBeTested(),
                    argsReadMasterDegreeProofVersion);

            assertEquals(infoMasterDegreeProofVersion.getIdInternal(), new Integer(1));
            assertEquals(
                infoMasterDegreeProofVersion.getInfoMasterDegreeThesis().getIdInternal(),
                new Integer(1));
            assertEquals(
                infoMasterDegreeProofVersion.getInfoResponsibleEmployee().getIdInternal(),
                new Integer(1194));
            assertEquals(infoMasterDegreeProofVersion.getCurrentState(), new State(State.ACTIVE));
            assertEquals(infoMasterDegreeProofVersion.getAttachedCopiesNumber(), new Integer(5));
            assertEquals(
                infoMasterDegreeProofVersion.getFinalResult(),
                MasterDegreeClassification.APPROVED);
            Date proofDate = new GregorianCalendar(2003, Calendar.OCTOBER, 10).getTime();
            assertEquals(infoMasterDegreeProofVersion.getProofDate(), proofDate);
            Date thesisDeliveryDate = new GregorianCalendar(2003, Calendar.NOVEMBER, 11).getTime();
            assertEquals(infoMasterDegreeProofVersion.getThesisDeliveryDate(), thesisDeliveryDate);
            assertEquals(
                ((InfoTeacher) infoMasterDegreeProofVersion.getInfoJuries().get(0)).getIdInternal(),
                new Integer(954));

            //ok

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("testReadExistingMasterDegreeProofVersion " + ex.getMessage());
        }

    }

    public void testReadNonExistingMasterDegreeProofVersion()
    {
        try
        {
            Object[] argsReadMasterDegreeProofVersion = { new Integer(50)};

            ServiceManagerServiceFactory.executeService(
                userView,
                getNameOfServiceToBeTested(),
                argsReadMasterDegreeProofVersion);

            fail("testReadNonExistingMasterDegreeProofVersion did not throw NonExistingServiceException");

        }
        catch (NonExistingServiceException ex)
        {
            //ok

        }
        catch (FenixServiceException e)
        {
            e.printStackTrace();
            fail("testReadNonExistingMasterDegreeProofVersion " + e.getMessage());
        }

    }

}
