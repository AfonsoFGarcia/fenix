package ServidorAplicacao.Servicos.publico;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            sp = SuportePersistenteOJB.getInstance();
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