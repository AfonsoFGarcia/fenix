/*
 * Created on 20/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoDistributedTest;
import DataBeans.InfoSiteDistributedTests;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IDistributedTest;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTests implements IServico {

	private static ReadDistributedTests service = new ReadDistributedTests();

	public static ReadDistributedTests getService() {
		return service;
	}

	public String getNome() {
		return "ReadDistributedTests";
	}
	public SiteView run(Integer executionCourseId)
		throws FenixServiceException {

		ISuportePersistente persistentSuport;
		try {
			persistentSuport = SuportePersistenteOJB.getInstance();

			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				persistentSuport.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse =
				new DisciplinaExecucao(executionCourseId);
			executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			if (executionCourse == null) {
				throw new InvalidArgumentsServiceException();
			}
			IPersistentDistributedTest persistentDistrubutedTest =
				(IPersistentDistributedTest) persistentSuport
					.getIPersistentDistributedTest();
			List distributedTests =
				persistentDistrubutedTest.readByExecutionCourse(
					executionCourse);
			List result = new ArrayList();
			Iterator iter = distributedTests.iterator();
			while (iter.hasNext()) {
				IDistributedTest distributedTest =
					(IDistributedTest) iter.next();
				InfoDistributedTest infoDistributedTest =
					Cloner.copyIDistributedTest2InfoDistributedTest(
						distributedTest);
				result.add(infoDistributedTest);
			}
			InfoSiteDistributedTests bodyComponent =
				new InfoSiteDistributedTests();
			bodyComponent.setInfoDistributedTests(result);
			bodyComponent.setExecutionCourse(
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse));
			SiteView siteView =
				new ExecutionCourseSiteView(bodyComponent, bodyComponent);
			return siteView;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}
