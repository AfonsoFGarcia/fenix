/*
 * ReadShiftLessonsTest.java JUnit based test
 * 
 * Created on January 4th, 2003, 23:35
 */

package ServidorAplicacao.Servicos.student;

/**
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadShiftLessonsTest extends TestCaseReadServices {

    public ReadShiftLessonsTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadShiftLessonsTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadShiftLessons";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        IExecutionCourse executionCourse = null;
        try {
            SuportePersistenteOJB.getInstance().iniciarTransaccao();
            executionCourse = SuportePersistenteOJB.getInstance().getIPersistentExecutionCourse()
                    .readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
            assertNotNull(executionCourse);
            SuportePersistenteOJB.getInstance().confirmarTransaccao();
            Object[] result = { new InfoShift("turnoINEX", null, null, (InfoExecutionCourse) Cloner
                    .get(executionCourse)) };
            return result;
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        IExecutionCourse executionCourse = null;
        try {
            SuportePersistenteOJB.getInstance().iniciarTransaccao();
            executionCourse = SuportePersistenteOJB.getInstance().getIPersistentExecutionCourse()
                    .readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
            assertNotNull(executionCourse);
            SuportePersistenteOJB.getInstance().confirmarTransaccao();
            Object[] result = { new InfoShift("turno4", null, null, (InfoExecutionCourse) Cloner
                    .get(executionCourse)) };
            return result;
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 1;
    }

    protected Object getObjectToCompare() {
        return null;
    }

}