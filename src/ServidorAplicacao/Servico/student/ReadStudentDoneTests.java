/*
 * Created on 29/Ago/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoDistributedTest;
import DataBeans.InfoSiteDistributedTests;
import DataBeans.comparators.CalendarDateComparator;
import DataBeans.comparators.CalendarHourComparator;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.StudentTestQuestion;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadStudentDoneTests implements IServico {

	private static ReadStudentDoneTests service = new ReadStudentDoneTests();

	public static ReadStudentDoneTests getService() {
		return service;
	}

	public String getNome() {
		return "ReadStudentDoneTests";
	}

	public Object run(String userName, Integer executionCourseId)
		throws FenixServiceException {
		InfoSiteDistributedTests infoSite = new InfoSiteDistributedTests();
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			IStudent student =
				persistentSuport.getIPersistentStudent().readByUsername(
					userName);
			List StudentTestQuestionList =
				persistentSuport
					.getIPersistentStudentTestQuestion()
					.readByStudent(
					student);
			Iterator studentTestQuestionIt = StudentTestQuestionList.iterator();
			IPersistentDistributedTest persistentDistributedTest =
				persistentSuport.getIPersistentDistributedTest();

			List distributedTestList = new ArrayList();
			while (studentTestQuestionIt.hasNext()) {
				IStudentTestQuestion studentTestQuestion =
					(StudentTestQuestion) studentTestQuestionIt.next();
				IDistributedTest distributedTest =
					new DistributedTest(
						studentTestQuestion.getKeyDistributedTest());
				distributedTest =
					(IDistributedTest) persistentDistributedTest.readByOId(
						distributedTest,
						false);
				if (!(compareDates(distributedTest.getEndDate(),
					distributedTest.getEndHour()))
					&& distributedTest.getKeyExecutionCourse().equals(
						executionCourseId)) {
					InfoDistributedTest infoDistributedTest =
						Cloner.copyIDistributedTest2InfoDistributedTest(
							distributedTest);
					if (!distributedTestList.contains(infoDistributedTest))
						distributedTestList.add(infoDistributedTest);

				}

			}
			infoSite.setInfoDistributedTests(distributedTestList);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return infoSite;
	}

	private boolean compareDates(Calendar date, Calendar hour) {
		Calendar calendar = Calendar.getInstance();
		CalendarDateComparator dateComparator = new CalendarDateComparator();
		CalendarHourComparator hourComparator = new CalendarHourComparator();
		if (dateComparator.compare(calendar, date) <= 0) {
			if (dateComparator.compare(calendar, date) == 0)
				if (hourComparator.compare(calendar, hour) <= 0)
					return true;
				else
					return false;
			return true;
		}
		return false;
	}
}