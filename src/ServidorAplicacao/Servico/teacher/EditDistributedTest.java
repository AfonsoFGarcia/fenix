/*
 * Created on 1/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import Dominio.Advisory;
import Dominio.DistributedTest;
import Dominio.ExecutionCourse;
import Dominio.Frequenta;
import Dominio.IAdvisory;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.ITurno;
import Dominio.Student;
import Dominio.StudentTestQuestion;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CorrectionAvailability;
import Util.TestType;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class EditDistributedTest implements IServico
{
	private static EditDistributedTest service = new EditDistributedTest();
	private String path = new String();

	public static EditDistributedTest getService()
	{
		return service;
	}

	public EditDistributedTest()
	{
	}

	public String getNome()
	{
		return "EditDistributedTest";
	}

	public boolean run(
		Integer executionCourseId,
		Integer distributedTestId,
		String testInformation,
		Calendar beginDate,
		Calendar beginHour,
		Calendar endDate,
		Calendar endHour,
		TestType testType,
		CorrectionAvailability correctionAvailability,
		Boolean studentFeedback,
		String[] selected,
		Boolean insertByShifts,
		String path)
		throws FenixServiceException
	{
		this.path = path.replace('\\', '/');
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

			IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
			executionCourse =
				(IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOId(
					executionCourse,
					false);
			if (executionCourse == null)
				throw new InvalidArgumentsServiceException();

			IPersistentDistributedTest persistentDistributedTest =
				persistentSuport.getIPersistentDistributedTest();

			IDistributedTest distributedTest = new DistributedTest(distributedTestId);
			distributedTest =
				(IDistributedTest) persistentDistributedTest.readByOId(distributedTest, true);
			if (distributedTest == null)
				throw new InvalidArgumentsServiceException();

			if (distributedTest.getBeginDate() != beginDate
				|| distributedTest.getBeginHour() != beginHour
				|| distributedTest.getEndDate() != endDate
				|| distributedTest.getEndHour() != endHour)
			{
				IAdvisory advisory = new Advisory();
				advisory.setCreated(null);
				advisory.setExpires(endDate.getTime());
				advisory.setSender("Docente da disciplina " + executionCourse.getNome());
				advisory.setSubject(distributedTest.getTitle()+": Altera��o de datas");
				advisory.setMessage(
					"As datas da Ficha de Trabalho foram alteradas. Dever� realizar a ficha entre "
						+ getDateFormatted(beginDate)
						+ " �s "
						+ getHourFormatted(beginHour)
						+ " at� "
						+ getDateFormatted(endDate)
						+ " �s "
						+ getHourFormatted(endHour));
				advisory.setOnlyShowOnce(new Boolean(false));
				List students =
					persistentSuport.getIPersistentStudentTestQuestion().readStudentsByDistributedTest(
						distributedTest);
				persistentSuport.getIPersistentAdvisory().write(
					advisory,
					getPersonListFromStudentList(students));
			}
			persistentDistributedTest.simpleLockWrite(distributedTest);
			distributedTest.setTestInformation(testInformation);
			distributedTest.setBeginDate(beginDate);
			distributedTest.setBeginHour(beginHour);
			distributedTest.setEndDate(endDate);
			distributedTest.setEndHour(endHour);
			distributedTest.setTestType(testType);
			distributedTest.setCorrectionAvailability(correctionAvailability);
			distributedTest.setStudentFeedback(studentFeedback);

			List group = new ArrayList();
			if (selected != null)
			{
				List studentList = null;
				if (insertByShifts.booleanValue())
				{
					studentList = returnStudentsFromShiftsArray(persistentSuport, selected);
				}
				else
				{
					studentList =
						returnStudentsFromStudentsArray(persistentSuport, selected, executionCourseId);
				}

				List studentTestQuestionList =
					persistentSuport
						.getIPersistentStudentTestQuestion()
						.readStudentTestQuestionsByDistributedTest(
						distributedTest);

				Iterator studentTestQuestionIt = studentTestQuestionList.iterator();
				while (studentTestQuestionIt.hasNext())
				{
					IStudentTestQuestion studentTestQuestionExample =
						(StudentTestQuestion) studentTestQuestionIt.next();

					Iterator studentIt = studentList.iterator();
					while (studentIt.hasNext())
					{
						IStudent student = (IStudent) studentIt.next();
						if (persistentSuport
							.getIPersistentStudentTestQuestion()
							.readByStudentAndDistributedTest(student, distributedTest)
							.isEmpty())
						{
							if (!group.contains(student.getPerson()))
								group.add(student.getPerson());

							IStudentTestQuestion studentTestQuestion = new StudentTestQuestion();
							persistentSuport.getIPersistentStudentTestQuestion().lockWrite(
								studentTestQuestion);
							studentTestQuestion.setStudent(student);
							studentTestQuestion.setDistributedTest(distributedTest);
							studentTestQuestion.setTestQuestionOrder(
								studentTestQuestionExample.getTestQuestionOrder());
							studentTestQuestion.setTestQuestionValue(
								studentTestQuestionExample.getTestQuestionValue());
							studentTestQuestion.setResponse(new Integer(0));
							studentTestQuestion.setTestQuestionMark(new Double(0));
							IQuestion question =
								getStudentQuestion(
									persistentSuport.getIPersistentQuestion(),
									studentTestQuestionExample.getQuestion().getMetadata());
							if (question == null)
							{
								throw new InvalidArgumentsServiceException();
							}

							studentTestQuestion.setQuestion(question);

							ParseQuestion p = new ParseQuestion();
							try
							{
								studentTestQuestion.setOptionShuffle(
									p.shuffleQuestionOptions(
										studentTestQuestion.getQuestion().getXmlFile(),
										this.path));
							}
							catch (Exception e)
							{
								throw new FenixServiceException(e);
							}
						}
					}
				}
				// Create Advisory
				IAdvisory advisory = new Advisory();
				advisory.setCreated(null);
				advisory.setExpires(endDate.getTime());
				advisory.setSender("Docente da disciplina " + executionCourse.getNome());
				advisory.setSubject(distributedTest.getTitle());
				advisory.setMessage(
					"Tem uma Ficha de Trabalho a realizar entre "
						+ getDateFormatted(beginDate)
						+ " �s "
						+ getHourFormatted(beginHour)
						+ " at� "
						+ getDateFormatted(endDate)
						+ " �s "
						+ getHourFormatted(endHour));
				advisory.setOnlyShowOnce(new Boolean(false));

				advisory.setOnlyShowOnce(new Boolean(false));
				persistentSuport.getIPersistentAdvisory().write(advisory, group);
			}

			return true;
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
	}

	private IQuestion getStudentQuestion(IPersistentQuestion persistentQuestion, IMetadata metadata)
		throws ExcepcaoPersistencia
	{
		List questions = metadata.getVisibleQuestions();
		IQuestion question = null;
		if (questions.size() != 0)
		{
			Random r = new Random();
			int questionIndex = r.nextInt(questions.size());
			question = (IQuestion) questions.get(questionIndex);
		}
		return question;
	}

	private List returnStudentsFromShiftsArray(ISuportePersistente persistentSuport, String[] shifts)
		throws FenixServiceException
	{
		List studentsList = new ArrayList();
		try
		{

			ITurnoPersistente persistentShift = persistentSuport.getITurnoPersistente();
			for (int i = 0; i < shifts.length; i++)
			{
				if (shifts[i].equals("Todos os Turnos"))
				{
					continue;
				}
				else
				{
					ITurno shift = new Turno(new Integer(shifts[i]));
					shift = (ITurno) persistentShift.readByOId(shift, false);
					Iterator studentIt =
						persistentSuport.getITurnoAlunoPersistente().readByShift(shift).iterator();
					while (studentIt.hasNext())
					{
						IStudent student = (IStudent) studentIt.next();
						if (!studentsList.contains(student))
							studentsList.add(student);
					}
				}
			}
		}
		catch (Exception e)
		{
			throw new FenixServiceException(e);
		}
		return studentsList;
	}

	private List returnStudentsFromStudentsArray(
		ISuportePersistente persistentSuport,
		String[] students,
		Integer executionCourseId)
		throws FenixServiceException
	{
		List studentsList = new ArrayList();
		try
		{

			for (int i = 0; i < students.length; i++)
			{
				if (students[i].equals("Todos os Alunos"))
				{
					IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
					executionCourse =
						(IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOId(
							executionCourse,
							false);
					List attendList =
						persistentSuport.getIFrequentaPersistente().readByExecutionCourse(
							executionCourse);

					Iterator iterStudent = attendList.listIterator();
					while (iterStudent.hasNext())
					{
						IFrequenta attend = (Frequenta) iterStudent.next();
						IStudent student = attend.getAluno();
						studentsList.add(student);
					}
					break;
				}
				else
				{
					IStudent student = new Student(new Integer(students[i]));
					student =
						(IStudent) persistentSuport.getIPersistentStudent().readByOId(student, false);

					if (!studentsList.contains(student))
						studentsList.add(student);
				}
			}
		}
		catch (Exception e)
		{
			throw new FenixServiceException(e);
		}
		return studentsList;
	}

	private List getPersonListFromStudentList(List students)
	{
		List personList = new ArrayList();
		Iterator it = students.iterator();
		while (it.hasNext())
			personList.add(((IStudent) it.next()).getPerson());
		return personList;
	}

	private String getDateFormatted(Calendar date)
	{
		String result = new String();
		result += date.get(Calendar.DAY_OF_MONTH);
		result += "/";
		result += date.get(Calendar.MONTH) + 1;
		result += "/";
		result += date.get(Calendar.YEAR);
		return result;
	}
	private String getHourFormatted(Calendar hour)
	{
		String result = new String();
		result += hour.get(Calendar.HOUR_OF_DAY);
		result += ":";
		if (hour.get(Calendar.MINUTE) < 10)
			result += "0";
		result += hour.get(Calendar.MINUTE);
		return result;
	}

}
