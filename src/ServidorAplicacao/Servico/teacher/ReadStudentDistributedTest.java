/*
 * Created on 8/Set/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoStudentTestQuestion;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadStudentDistributedTest implements IServico {

	private static ReadStudentDistributedTest service = new ReadStudentDistributedTest();

	public static ReadStudentDistributedTest getService() {
		return service;
	}

	public String getNome() {
		return "ReadStudentDistributedTest";
	}

	public List run(
		Integer executionCourseId,
		Integer distributedTestId,
		Integer studentId)
		throws FenixServiceException {

		ISuportePersistente persistentSuport;
		List infoStudentTestQuestionList = new ArrayList();
		try {
			persistentSuport = SuportePersistenteOJB.getInstance();
			IStudent student = new Student(studentId);
			student =
				(IStudent) persistentSuport.getIPersistentStudent().readByOId(
					student,
					false);
			if (student == null)
				throw new FenixServiceException();
			IDistributedTest distributedTest =
				new DistributedTest(distributedTestId);
			if (distributedTest == null)
				throw new FenixServiceException();
			distributedTest =
				(IDistributedTest) persistentSuport
					.getIPersistentDistributedTest()
					.readByOId(
					distributedTest,
					false);

			List studentTestQuestionList =
				persistentSuport
					.getIPersistentStudentTestQuestion()
					.readByStudentAndDistributedTest(student, distributedTest);
			Iterator it = studentTestQuestionList.iterator();
			while (it.hasNext()) {
				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
				InfoStudentTestQuestion infoStudentTestQuestion =
					Cloner.copyIStudentTestQuestion2InfoStudentTestQuestion(
						studentTestQuestion);
				ParseQuestion parse = new ParseQuestion();
				try {
					infoStudentTestQuestion.setQuestion(
						parse.parseQuestion(
							infoStudentTestQuestion.getQuestion().getXmlFile(),
							infoStudentTestQuestion.getQuestion()));
					infoStudentTestQuestion =
						parse.getOptionsShuffle(infoStudentTestQuestion);
				} catch (Exception e) {
					throw new FenixServiceException(e);
				}

				infoStudentTestQuestionList.add(infoStudentTestQuestion);
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return infoStudentTestQuestionList;
	}
}