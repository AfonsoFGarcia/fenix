package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.dataTransferObject.InfoEmployee;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValues;
import net.sourceforge.fenixedu.dataTransferObject.InfoPaymentPhase;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.CursoExecucao;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.ICursoExecucao;
import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.domain.IPaymentPhase;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuityValues;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * 
 * @author Fernanda Quit�rio 19/Jan/2004
 *  
 */
public class InsertGratuityDataTest extends AdministrativeOfficeBaseTest {

    /**
     * @param name
     */
    public InsertGratuityDataTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return new String("InsertGratuityData");
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/MasterDegree/administrativeOffice/gratuity/testInsertGratuityDataDataSet.xml";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest#getServiceArgumentsForNotAuthenticatedUser()
     */
    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Calendar now = Calendar.getInstance();

        InfoPerson infoPerson = new InfoPerson();
        infoPerson.setUsername("naoAutenticado");

        InfoEmployee infoEmployee = new InfoEmployee();
        infoEmployee.setPerson(infoPerson);

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
        infoExecutionDegree.setIdInternal(new Integer(30));

        InfoGratuityValues infoGratuityValues = new InfoGratuityValues();
        infoGratuityValues.setIdInternal(new Integer(111));
        infoGratuityValues.setAnualValue(new Double(650));
        infoGratuityValues.setCreditValue(new Double(50));
        infoGratuityValues.setRegistrationPayment(Boolean.TRUE);
        infoGratuityValues.setWhen(now.getTime());
        infoGratuityValues.setInfoEmployee(infoEmployee);
        infoGratuityValues.setInfoExecutionDegree(infoExecutionDegree);

        Object args[] = { infoGratuityValues };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest#getServiceArgumentsForNotAuthorizedUser()
     */
    protected Object[] getServiceArgumentsForNotAuthorizedUser() {
        Calendar now = Calendar.getInstance();

        InfoPerson infoPerson = new InfoPerson();
        infoPerson.setUsername(getAuthenticatedAndNotAuthorizedUser()[0]);

        InfoEmployee infoEmployee = new InfoEmployee();
        infoEmployee.setPerson(infoPerson);

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
        infoExecutionDegree.setIdInternal(new Integer(30));

        InfoGratuityValues infoGratuityValues = new InfoGratuityValues();
        infoGratuityValues.setIdInternal(new Integer(111));
        infoGratuityValues.setAnualValue(new Double(650));
        infoGratuityValues.setCreditValue(new Double(50));
        infoGratuityValues.setRegistrationPayment(Boolean.TRUE);
        infoGratuityValues.setWhen(now.getTime());
        infoGratuityValues.setInfoEmployee(infoEmployee);
        infoGratuityValues.setInfoExecutionDegree(infoExecutionDegree);

        Object args[] = { infoGratuityValues };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest#getServiceArgumentsForNotAuthorizedUser()
     */
    protected Object[] getServiceArgumentForInsertSuccess() {
        Calendar now = Calendar.getInstance();

        InfoPerson infoPerson = new InfoPerson();
        infoPerson.setUsername(getAuthenticatedAndAuthorizedUser()[0]);

        InfoEmployee infoEmployee = new InfoEmployee();
        infoEmployee.setPerson(infoPerson);

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
        infoExecutionDegree.setIdInternal(new Integer(30));

        List infoPaymentPhases = new ArrayList();
        InfoPaymentPhase infoPaymentPhase = new InfoPaymentPhase();
        infoPaymentPhase.setDescription(SessionConstants.REGISTRATION_PAYMENT);
        now.set(2004, 1, 20);
        infoPaymentPhase.setEndDate(now.getTime());
        infoPaymentPhase.setValue(new Double(150));

        infoPaymentPhases.add(infoPaymentPhase);

        infoPaymentPhase = new InfoPaymentPhase();
        now.set(2004, 5, 21);
        infoPaymentPhase.setEndDate(now.getTime());
        now.set(2004, 1, 21);
        infoPaymentPhase.setStartDate(now.getTime());
        infoPaymentPhase.setValue(new Double(200));

        infoPaymentPhases.add(infoPaymentPhase);

        infoPaymentPhase = new InfoPaymentPhase();
        now.set(2004, 7, 22);
        infoPaymentPhase.setEndDate(now.getTime());
        now.set(2004, 5, 22);
        infoPaymentPhase.setStartDate(now.getTime());
        infoPaymentPhase.setValue(new Double(300));

        infoPaymentPhases.add(infoPaymentPhase);

        InfoGratuityValues infoGratuityValues = new InfoGratuityValues();
        infoGratuityValues.setIdInternal(null);
        infoGratuityValues.setAnualValue(new Double(650));
        infoGratuityValues.setCreditValue(new Double(50));
        infoGratuityValues.setRegistrationPayment(Boolean.TRUE);
        infoGratuityValues.setWhen(now.getTime());
        infoGratuityValues.setInfoEmployee(infoEmployee);
        infoGratuityValues.setInfoExecutionDegree(infoExecutionDegree);
        infoGratuityValues.setInfoPaymentPhases(infoPaymentPhases);

        Object args[] = { infoGratuityValues };
        return args;
    }

    protected Object[] getServiceArgumentForEditSuccess() {
        Calendar now = Calendar.getInstance();

        InfoPerson infoPerson = new InfoPerson();
        infoPerson.setUsername(getAuthenticatedAndAuthorizedUser()[0]);

        InfoEmployee infoEmployee = new InfoEmployee();
        infoEmployee.setPerson(infoPerson);

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
        infoExecutionDegree.setIdInternal(new Integer(29));

        List infoPaymentPhases = new ArrayList();
        InfoPaymentPhase infoPaymentPhase = new InfoPaymentPhase();
        infoPaymentPhase.setDescription(SessionConstants.REGISTRATION_PAYMENT);
        now.set(2004, 1, 20);
        infoPaymentPhase.setEndDate(now.getTime());
        infoPaymentPhase.setValue(new Double(150));

        infoPaymentPhases.add(infoPaymentPhase);

        infoPaymentPhase = new InfoPaymentPhase();
        now.set(2004, 5, 21);
        infoPaymentPhase.setEndDate(now.getTime());
        now.set(2004, 1, 21);
        infoPaymentPhase.setStartDate(now.getTime());
        infoPaymentPhase.setValue(new Double(200));

        infoPaymentPhases.add(infoPaymentPhase);

        infoPaymentPhase = new InfoPaymentPhase();
        now.set(2004, 7, 22);
        infoPaymentPhase.setEndDate(now.getTime());
        now.set(2004, 5, 22);
        infoPaymentPhase.setStartDate(now.getTime());
        infoPaymentPhase.setValue(new Double(300));

        infoPaymentPhases.add(infoPaymentPhase);

        InfoGratuityValues infoGratuityValues = new InfoGratuityValues();
        infoGratuityValues.setIdInternal(new Integer(4));
        infoGratuityValues.setAnualValue(new Double(650));
        infoGratuityValues.setCreditValue(new Double(50));
        infoGratuityValues.setRegistrationPayment(Boolean.TRUE);
        infoGratuityValues.setWhen(now.getTime());
        infoGratuityValues.setInfoEmployee(infoEmployee);
        infoGratuityValues.setInfoExecutionDegree(infoExecutionDegree);
        infoGratuityValues.setInfoPaymentPhases(infoPaymentPhases);

        Object args[] = { infoGratuityValues };
        return args;
    }

    public void testInsertGratuityDataInsertSuccessfull() {
        try {

            Object[] args = getServiceArgumentForInsertSuccess();

            //Valid user
            String[] argsUser = getAuthenticatedAndAuthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao",
                    argsUser);

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);

            SuportePersistenteOJB sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentGratuityValues persistentGratuityValues = sp.getIPersistentGratuityValues();

            ICursoExecucao executionDegree = new CursoExecucao();
            executionDegree.setIdInternal(new Integer(30));

            IGratuityValues gratuityValues = persistentGratuityValues
                    .readGratuityValuesByExecutionDegree(executionDegree);

            InfoGratuityValues infoGratuityValuesDB = cloneGratuityValues(gratuityValues);

            InfoGratuityValues infoGratuityValues = (InfoGratuityValues) getServiceArgumentForInsertSuccess()[0];

            //Check information read is correct
            assertEquals(infoGratuityValues.getAnualValue(), infoGratuityValuesDB.getAnualValue());
            assertEquals(infoGratuityValues.getCourseValue(), infoGratuityValuesDB.getCourseValue());
            assertEquals(infoGratuityValues.getCreditValue(), infoGratuityValuesDB.getCreditValue());
            assertEquals(infoGratuityValues.getEndPayment(), infoGratuityValuesDB.getEndPayment());
            assertEquals(infoGratuityValues.getFinalProofValue(), infoGratuityValuesDB
                    .getFinalProofValue());
            assertEquals(infoGratuityValues.getInfoEmployee().getPerson().getUsername(),
                    infoGratuityValuesDB.getInfoEmployee().getPerson().getUsername());
            assertEquals(infoGratuityValues.getInfoExecutionDegree().getIdInternal(),
                    infoGratuityValuesDB.getInfoExecutionDegree().getIdInternal());
            assertEquals(infoGratuityValues.getInfoPaymentPhases().size(), infoGratuityValuesDB
                    .getInfoPaymentPhases().size());
            assertEquals(infoGratuityValues.getProofRequestPayment(), infoGratuityValuesDB
                    .getProofRequestPayment());
            assertEquals(infoGratuityValues.getRegistrationPayment(), infoGratuityValuesDB
                    .getRegistrationPayment());
            assertEquals(infoGratuityValues.getScholarShipValue(), infoGratuityValuesDB
                    .getScholarShipValue());
            assertEquals(infoGratuityValues.getStartPayment(), infoGratuityValuesDB.getStartPayment());
            assertEquals(infoGratuityValues.getWhen(), infoGratuityValuesDB.getWhen());

            //			IPersistentGratuitySituation persistentGratuitySituation =
            // sp.getIPersistentGratuitySituation();
            //			IGratuitySituation gratuitySituation = new GratuitySituation();
            System.out
                    .println("testInsertGratuityDataInsertSuccessfull was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());

        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("Inserting gratuity values into database " + e);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Inserting gratuity values into database " + e);
        }
    }

    private InfoGratuityValues cloneGratuityValues(IGratuityValues gratuityValues) {
        InfoGratuityValues infoGratuityValuesDB = Cloner
                .copyIGratuityValues2InfoGratuityValues(gratuityValues);
        List infoPaymentPhases = new ArrayList();
        CollectionUtils.collect(gratuityValues.getPaymentPhaseList(), new Transformer() {
            public Object transform(Object input) {
                IPaymentPhase paymentPhase = (IPaymentPhase) input;
                return Cloner.copyIPaymentPhase2InfoPaymentPhase(paymentPhase);
            }
        }, infoPaymentPhases);

        InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) CollectionUtils.find(infoPaymentPhases,
                new Predicate() {
                    public boolean evaluate(Object input) {
                        InfoPaymentPhase aInfoPaymentPhase = (InfoPaymentPhase) input;
                        if (aInfoPaymentPhase.getDescription().equals(
                                SessionConstants.REGISTRATION_PAYMENT)) {
                            return true;
                        }
                        return false;
                    }
                });

        if (infoPaymentPhase != null) {
            infoGratuityValuesDB.setRegistrationPayment(Boolean.TRUE);
        }

        infoGratuityValuesDB.setInfoPaymentPhases(infoPaymentPhases);
        return infoGratuityValuesDB;
    }

    public void testInsertGratuityDataEditSuccessfull() {
        try {

            Object[] args = getServiceArgumentForEditSuccess();

            //Valid user
            String[] argsUser = getAuthenticatedAndAuthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao",
                    argsUser);

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);

            SuportePersistenteOJB sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentGratuityValues persistentGratuityValues = sp.getIPersistentGratuityValues();

            IGratuityValues gratuityValues;

            gratuityValues = (IGratuityValues) persistentGratuityValues.readByOID(GratuityValues.class,
                    new Integer(222));

            InfoGratuityValues infoGratuityValuesDB = cloneGratuityValues(gratuityValues);

            InfoGratuityValues infoGratuityValues = (InfoGratuityValues) getServiceArgumentForEditSuccess()[0];

            //Check information read is correct
            assertEquals(infoGratuityValues.getIdInternal(), infoGratuityValuesDB.getIdInternal());
            assertEquals(infoGratuityValues.getAnualValue(), infoGratuityValuesDB.getAnualValue());
            assertEquals(infoGratuityValues.getCourseValue(), infoGratuityValuesDB.getCourseValue());
            assertEquals(infoGratuityValues.getCreditValue(), infoGratuityValuesDB.getCreditValue());
            assertEquals(infoGratuityValues.getEndPayment(), infoGratuityValuesDB.getEndPayment());
            assertEquals(infoGratuityValues.getFinalProofValue(), infoGratuityValuesDB
                    .getFinalProofValue());
            assertEquals(infoGratuityValues.getInfoEmployee().getPerson().getUsername(),
                    infoGratuityValuesDB.getInfoEmployee().getPerson().getUsername());
            assertEquals(infoGratuityValues.getInfoExecutionDegree().getIdInternal(),
                    infoGratuityValuesDB.getInfoExecutionDegree().getIdInternal());
            assertEquals(infoGratuityValues.getInfoPaymentPhases().size(), infoGratuityValuesDB
                    .getInfoPaymentPhases().size());
            assertEquals(infoGratuityValues.getProofRequestPayment(), infoGratuityValuesDB
                    .getProofRequestPayment());
            assertEquals(infoGratuityValues.getRegistrationPayment(), infoGratuityValuesDB
                    .getRegistrationPayment());
            assertEquals(infoGratuityValues.getScholarShipValue(), infoGratuityValuesDB
                    .getScholarShipValue());
            assertEquals(infoGratuityValues.getStartPayment(), infoGratuityValuesDB.getStartPayment());
            assertEquals(infoGratuityValues.getWhen(), infoGratuityValuesDB.getWhen());

            System.out
                    .println("testInsertGratuityDataEditSuccessfull was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());

        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("Inserting gratuity values into database " + e);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Inserting gratuity values into database " + e);
        }
    }

}