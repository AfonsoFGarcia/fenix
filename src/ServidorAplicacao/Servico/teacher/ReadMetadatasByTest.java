/*
 * Created on 23/Jul/2003
 *
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoMetadata;
import DataBeans.InfoSiteMetadatas;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IMetadata;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Test;
import Dominio.TestQuestion;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseMetadata;

/**
 * @author Susana Fernandes
 */

public class ReadMetadatasByTest implements IServico {

	private static ReadMetadatasByTest service =
		new ReadMetadatasByTest();

	public static ReadMetadatasByTest getService() {
		return service;
	}

	public String getNome() {
		return "ReadMetadatasByTest";
	}
	public SiteView run(Integer executionCourseId, Integer testId)
		throws FenixServiceException {

		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
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
			IPersistentMetadata persistentMetadata =
				(IPersistentMetadata) persistentSuport.getIPersistentMetadata();
			List metadatas =
				persistentMetadata.readByExecutionCourse(executionCourse);
			List result = new ArrayList();
			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Test(testId);
			test = (ITest) persistentTest.readByOId(test, false);
			if (test == null) {
				throw new InvalidArgumentsServiceException();
			}
			IPersistentTestQuestion persistentTestQuestion =
				persistentSuport.getIPersistentTestQuestion();
			List questions = persistentTestQuestion.readByTest(test);
			Iterator iter = metadatas.iterator();
			while (iter.hasNext()) {
				IMetadata metadata = (IMetadata) iter.next();
				boolean contains = false;
				Iterator iterQuestion = questions.iterator();
				while (iterQuestion.hasNext()) {
					ITestQuestion testQuestion =
						(TestQuestion) iterQuestion.next();
					if (testQuestion
						.getQuestion()
						.getKeyMetadata()
						.equals(metadata.getIdInternal())) {
						contains = true;
						break;
					}
				}
				if (contains == false) {
					InfoMetadata infoMetadata =
						Cloner.copyIMetadata2InfoMetadata(metadata);
					ParseMetadata p = new ParseMetadata();
					try {
						infoMetadata =
							p.parseMetadata(
								metadata.getMetadataFile(),
								infoMetadata);
					} catch (Exception e) {
						throw new FenixServiceException(e);
					}
					result.add(infoMetadata);
				}
			}
			InfoSiteMetadatas bodyComponent = new InfoSiteMetadatas();
			bodyComponent.setInfoMetadatas(result);
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