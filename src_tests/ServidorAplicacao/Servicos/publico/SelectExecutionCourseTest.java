package net.sourceforge.fenixedu.applicationTier.Servicos.publico;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICurso;
import net.sourceforge.fenixedu.domain.ICursoExecucao;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServicos;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 *  
 */
public class SelectExecutionCourseTest extends TestCaseServicos {

    private InfoExecutionDegree infoExecutionDegree = null;

    private InfoExecutionPeriod infoExecutionPeriod = null;

    private Integer curricularYear = null;

    /**
     * Constructor for SelectClassesTest.
     */
    public SelectExecutionCourseTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SelectExecutionCourseTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testReadAll() {

        Object argsSelectExecutionCourses[] = new Object[3];

        Object result = null;

        // n > 0 executionCourses in database
        prepareTestCase(true);
        argsSelectExecutionCourses[0] = infoExecutionDegree;
        argsSelectExecutionCourses[1] = infoExecutionPeriod;
        argsSelectExecutionCourses[2] = curricularYear;
        try {
            result = ServiceManagerServiceFactory.executeService(null, "SelectExecutionCourse",
                    argsSelectExecutionCourses);
            assertNotNull("test reading executionCourses", result);
            assertTrue("test reading executionCourses", ((List) result).size() > 0);
        } catch (Exception e) {
            fail("test reading execution courses" + e);
            e.printStackTrace();
        }

        // no executionCourses in database
        prepareTestCase(false);

        argsSelectExecutionCourses[0] = infoExecutionDegree;
        argsSelectExecutionCourses[1] = infoExecutionPeriod;
        argsSelectExecutionCourses[2] = curricularYear;

        try {
            result = ServiceManagerServiceFactory.executeService(null, "SelectExecutionCourse",
                    argsSelectExecutionCourses);
            assertTrue("test reading executionCourses", ((List) result).size() == 0);
        } catch (Exception e) {
            fail("test reading execution courses" + e);
            e.printStackTrace();
        }

    }

    private void prepareTestCase(boolean hasExecutionCourses) {

        ISuportePersistente sp = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
            IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
            assertNotNull(executionYear);

            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(
                    "2� Semestre", executionYear);
            assertNotNull(executionPeriod);

            ICursoPersistente cursoPersistente = sp.getICursoPersistente();
            ICurso degree = cursoPersistente.readBySigla("LEIC");
            assertNotNull(degree);

            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp
                    .getIPersistentDegreeCurricularPlan();
            IDegreeCurricularPlan degreeCurricularPlan = persistentDegreeCurricularPlan
                    .readByNameAndDegree("plano1", degree);
            assertNotNull(degreeCurricularPlan);

            IPersistentExecutionDegree cursoExecucaoPersistente = sp.getIPersistentExecutionDegree();
            ICursoExecucao executionDegree = cursoExecucaoPersistente
                    .readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
            assertNotNull(executionDegree);

            this.infoExecutionDegree = (InfoExecutionDegree) Cloner.get(executionDegree);
            this.infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(executionPeriod);
            this.curricularYear = new Integer(1);

            if (!hasExecutionCourses) {
                //                sp.getIPersistentExecutionCourse().apagarTodasAsDisciplinasExecucao();
                // no longer supported- too dangerous
                PersistenceBroker pb = PersistenceBrokerFactory.defaultPersistenceBroker();
                pb.clearCache();
            }
            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao: " + ex);
            }
            fail("ligarSuportePersistente: confirmarTransaccao: " + excepcao);
        }

    }

}