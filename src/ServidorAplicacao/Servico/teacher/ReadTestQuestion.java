/*
 * Created on 6/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoQuestion;
import DataBeans.InfoSiteTestQuestion;
import DataBeans.InfoTestQuestion;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Question;
import Dominio.Test;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadTestQuestion implements IServico {

	private static ReadTestQuestion service = new ReadTestQuestion();

	public static ReadTestQuestion getService() {
		return service;
	}

	public String getNome() {
		return "ReadTestQuestion";
	}

	public SiteView run(
		Integer executionCourseId,
		Integer testId,
		Integer questionId)
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
			if (executionCourse == null)
				throw new InvalidArgumentsServiceException();

			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Test(testId);
			test = (ITest) persistentTest.readByOId(test, false);
			if (test == null)
				throw new InvalidArgumentsServiceException();

			IPersistentQuestion persistentQuestion =
				persistentSuport.getIPersistentQuestion();
			IQuestion question = new Question(questionId);
			question =
				(IQuestion) persistentQuestion.readByOId(question, false);
			if (question == null)
				throw new InvalidArgumentsServiceException();

			InfoQuestion infoQuestion =
				Cloner.copyIQuestion2InfoQuestion(question);
			ParseQuestion parse = new ParseQuestion();
			try {
				infoQuestion =
					parse.parseQuestion(
						infoQuestion.getXmlFile(),
						infoQuestion);
			} catch (Exception e) {
				throw new FenixServiceException(e);
			}
			IPersistentTestQuestion persistentTestQuestion =
				persistentSuport.getIPersistentTestQuestion();
			ITestQuestion testQuestion =
				(ITestQuestion) persistentTestQuestion.readByTestAndQuestion(
					test,
					question);
InfoTestQuestion infoTestQuestion = Cloner.copyITestQuestion2InfoTestQuestion(testQuestion);
infoTestQuestion.setQuestion(infoQuestion);
			InfoSiteTestQuestion bodyComponent = new InfoSiteTestQuestion();
			bodyComponent.setInfoTestQuestion(infoTestQuestion);
			bodyComponent.setInfoQuestion(infoQuestion);
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