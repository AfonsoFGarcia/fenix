/*
 * Created on 8/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.student;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentDistributedTests;
import net.sourceforge.fenixedu.dataTransferObject.util.CopyUtils;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTestsTest extends TestCaseReadServices {

    public ReadStudentTestsTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadStudentTestsTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadStudentTests";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        String userName = new String("L48283");
        Integer executionCourseId = new Integer(34882);
        Object[] args = { userName, executionCourseId };
        return args;

    }

    protected int getNumberOfItemsToRetrieve() {
        return 1;
    }

    protected Object getObjectToCompare() {
        InfoSiteStudentDistributedTests infoSiteDistributedTests = new InfoSiteStudentDistributedTests();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();
            IDistributedTest distributedTest = (IDistributedTest) sp.getIPersistentDistributedTest()
                    .readByOID(DistributedTest.class, new Integer(2));
            sp.confirmarTransaccao();
            if (distributedTest == null)
                fail("ReadStudentTestsTest " + "no distributedTest");

            List distributedTestList = new ArrayList();
            distributedTestList.add(copyIDistributedTest2InfoDistributedTest(distributedTest));
            infoSiteDistributedTests.setInfoDistributedTestsToDo(distributedTestList);
            infoSiteDistributedTests.setInfoDoneDistributedTests(new ArrayList());
        } catch (Exception ex) {
            fail("ReadStudentTestsTest " + ex);
        }
        return infoSiteDistributedTests;
    }

    protected boolean needsAuthorization() {
        return true;
    }

    protected String[] getArgsForAuthorizedUser() {
        String argsAutenticacao3[] = { "l48283", "pass", getApplication() };
        return argsAutenticacao3;
    }

    protected String[] getArgsForNotAuthorizedUser() {
        String argsAutenticacao4[] = { "d2543", "pass", getApplication() };
        return argsAutenticacao4;
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/student/testReadStudentTestsToDoDataSet.xml";
    }

    public String getApplication() {
        return Autenticacao.EXTRANET;
    }

    private static InfoDistributedTest copyIDistributedTest2InfoDistributedTest(
            IDistributedTest distributedTest) {
        InfoDistributedTest infoDistributedTest = new InfoDistributedTest();
        try {
            CopyUtils.copyProperties(infoDistributedTest, distributedTest);
        } catch (Exception e) {
            fail("ReadStudentTestsTest " + "cloner");
        }
        return infoDistributedTest;
    }

}