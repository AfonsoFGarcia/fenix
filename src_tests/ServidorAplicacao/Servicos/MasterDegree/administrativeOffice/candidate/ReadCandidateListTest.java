/*
 * CriarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Outubro de 2002, 12:00
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.candidate;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

public class ReadCandidateListTest extends TestCaseReadServices {

    public ReadCandidateListTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadCandidateListTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadCandidateList";
    }

    protected int getNumberOfItemsToRetrieve() {
        return 2;
    }

    protected Object getObjectToCompare() {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        ISuportePersistente sp = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();
            //TODO: delete all no longer exists
            //sp.getIPersistentMasterDegreeCandidate().deleteAll();
            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia excepcao) {
            fail("Exception when setUp");
        }
        return null;
    }
}