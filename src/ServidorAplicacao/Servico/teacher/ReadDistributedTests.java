/*
 * Created on 20/Ago/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoDistributedTest;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteDistributedTests;
import DataBeans.SiteView;
import Dominio.ExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTests implements IService {

    public ReadDistributedTests() {
    }

    public SiteView run(Integer executionCourseId) throws FenixServiceException {

        ISuportePersistente persistentSuport;
        try {
            persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentDistributedTest persistentDistrubutedTest = persistentSuport
                    .getIPersistentDistributedTest();
            List distributedTests = persistentDistrubutedTest.readByTestScopeObject(executionCourse);
            List result = new ArrayList();
            Iterator iter = distributedTests.iterator();
            while (iter.hasNext()) {
                IDistributedTest distributedTest = (IDistributedTest) iter.next();
                InfoDistributedTest infoDistributedTest = InfoDistributedTest
                        .newInfoFromDomain(distributedTest);
                result.add(infoDistributedTest);
            }
            InfoSiteDistributedTests bodyComponent = new InfoSiteDistributedTests();
            bodyComponent.setInfoDistributedTests(result);
            bodyComponent.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}