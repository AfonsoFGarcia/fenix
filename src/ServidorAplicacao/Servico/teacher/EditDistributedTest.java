/*
 * Created on 1/Ago/2003
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.comparators.CalendarDateComparator;
import DataBeans.comparators.CalendarHourComparator;
import Dominio.Advisory;
import Dominio.DistributedTest;
import Dominio.DistributedTestAdvisory;
import Dominio.ExecutionCourse;
import Dominio.Frequenta;
import Dominio.IAdvisory;
import Dominio.IDistributedTest;
import Dominio.IDistributedTestAdvisory;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.IOnlineTest;
import Dominio.IQuestion;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.ITurno;
import Dominio.Mark;
import Dominio.OnlineTest;
import Dominio.Student;
import Dominio.StudentTestQuestion;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.CorrectionAvailability;
import Util.tests.TestType;

/**
 * @author Susana Fernandes
 */
public class EditDistributedTest implements IService {

    private String contextPath = new String();

    public EditDistributedTest() {
    }

    public boolean run(Integer executionCourseId, Integer distributedTestId,
            String testInformation, Calendar beginDate, Calendar beginHour,
            Calendar endDate, Calendar endHour, TestType testType,
            CorrectionAvailability correctionAvailability,Boolean imsFeedback, String[] selected,
            Boolean insertByShifts, String contextPath)
            throws FenixServiceException {
        this.contextPath = contextPath.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();
            IExecutionCourse executionCourse = new ExecutionCourse(
                    executionCourseId);
            executionCourse = (IExecutionCourse) persistentSuport
                    .getIPersistentExecutionCourse().readByOId(executionCourse,
                            false);
            if (executionCourse == null)
                    throw new InvalidArgumentsServiceException();
            IPersistentDistributedTest persistentDistributedTest = persistentSuport
                    .getIPersistentDistributedTest();
            IDistributedTest distributedTest = new DistributedTest(
                    distributedTestId);
            distributedTest = (IDistributedTest) persistentDistributedTest
                    .readByOId(distributedTest, true);
            if (distributedTest == null)
                    throw new InvalidArgumentsServiceException();
            persistentDistributedTest.simpleLockWrite(distributedTest);
            distributedTest.setTestInformation(testInformation);
            boolean change2EvaluationType = false, change2OtherType = false;
            if (distributedTest.getTestType().equals(
                    new TestType(TestType.EVALUATION))
                    && (!testType.equals(new TestType(TestType.EVALUATION))))
                change2OtherType = true;
            else if ((!distributedTest.getTestType().equals(
                    new TestType(TestType.EVALUATION)))
                    && testType.equals(new TestType(TestType.EVALUATION)))
                    change2EvaluationType = true;
            distributedTest.setTestType(testType);
            distributedTest.setCorrectionAvailability(correctionAvailability);
            distributedTest.setImsFeedback(imsFeedback);
            //create advisory for students that already have the test to
            // anounce the date changes

            CalendarDateComparator dateComparator = new CalendarDateComparator();
            CalendarHourComparator hourComparator = new CalendarHourComparator();
            if (dateComparator.compare(distributedTest.getBeginDate(),
                    beginDate) != 0
                    || hourComparator.compare(distributedTest.getBeginHour(),
                            beginHour) != 0
                    || dateComparator.compare(distributedTest.getEndDate(),
                            endDate) != 0
                    || hourComparator.compare(distributedTest.getEndHour(),
                            endHour) != 0) {

                List students = persistentSuport
                        .getIPersistentStudentTestQuestion()
                        .readStudentsByDistributedTest(distributedTest);

                IAdvisory advisory = createTestAdvisory(distributedTest,
                        beginDate, beginHour, endDate, endHour, true);
                persistentSuport.getIPersistentAdvisory().write(advisory,
                        getPersonListFromStudentList(students));
                distributedTest.setBeginDate(beginDate);
                distributedTest.setBeginHour(beginHour);
                distributedTest.setEndDate(endDate);
                distributedTest.setEndHour(endHour);

                persistentSuport.getIPersistentDistributedTestAdvisory()
                        .updateDistributedTestAdvisoryDates(distributedTest,
                                endDate.getTime());

                persistentSuport.getIPersistentDistributedTestAdvisory()
                        .simpleLockWrite(
                                createDistributedTestAdvisory(distributedTest,
                                        advisory));

            }
            // distribution for new students
            List group = new ArrayList();
            if (selected != null) {
                List studentList = null;
                if (insertByShifts.booleanValue())
                    studentList = returnStudentsFromShiftsArray(
                            persistentSuport, selected);
                else
                    studentList = returnStudentsFromStudentsArray(
                            persistentSuport, selected, executionCourseId);
                List studentTestQuestionList = persistentSuport
                        .getIPersistentStudentTestQuestion()
                        .readStudentTestQuestionsByDistributedTest(
                                distributedTest);
                Iterator studentTestQuestionIt = studentTestQuestionList
                        .iterator();
                while (studentTestQuestionIt.hasNext()) {
                    IStudentTestQuestion studentTestQuestionExample = (IStudentTestQuestion) studentTestQuestionIt
                            .next();

                    List questionList = new ArrayList();
                    questionList.addAll(studentTestQuestionExample
                            .getQuestion().getMetadata().getVisibleQuestions());

                    Iterator studentIt = studentList.iterator();
                    while (studentIt.hasNext()) {
                        IStudent student = (IStudent) studentIt.next();
                        if (persistentSuport
                                .getIPersistentStudentTestQuestion()
                                .readByStudentAndDistributedTest(student,
                                        distributedTest).isEmpty()) {
                            if (!group.contains(student.getPerson()))
                                    group.add(student.getPerson());
                            IStudentTestQuestion studentTestQuestion = new StudentTestQuestion();
                            persistentSuport
                                    .getIPersistentStudentTestQuestion()
                                    .lockWrite(studentTestQuestion);
                            studentTestQuestion.setStudent(student);
                            studentTestQuestion
                                    .setDistributedTest(distributedTest);
                            studentTestQuestion
                                    .setTestQuestionOrder(studentTestQuestionExample
                                            .getTestQuestionOrder());
                            studentTestQuestion
                                    .setTestQuestionValue(studentTestQuestionExample
                                            .getTestQuestionValue());
                            studentTestQuestion.setOldResponse(new Integer(0));
                            studentTestQuestion
                                    .setCorrectionFormula(studentTestQuestionExample
                                            .getCorrectionFormula());
                            studentTestQuestion.setTestQuestionMark(new Double(
                                    0));
                            studentTestQuestion.setResponse(null);

                            if (questionList.size() == 0)
                                    questionList
                                            .addAll(studentTestQuestionExample
                                                    .getQuestion()
                                                    .getMetadata()
                                                    .getVisibleQuestions());
                            IQuestion question = getStudentQuestion(questionList);
                            if (question == null) { throw new InvalidArgumentsServiceException(); }
                            studentTestQuestion.setQuestion(question);
                            questionList.remove(question);

                        }
                    }
                }
                //create advisory for new students
                IAdvisory advisory = createTestAdvisory(distributedTest, false);
                persistentSuport.getIPersistentAdvisory()
                        .write(advisory, group);
                persistentSuport.getIPersistentDistributedTestAdvisory()
                        .simpleLockWrite(
                                createDistributedTestAdvisory(distributedTest,
                                        advisory));
            }
            if (change2OtherType) {
                //Change evaluation test to study/inquiry test
                //delete evaluation and marks
                IOnlineTest onlineTest = (IOnlineTest) persistentSuport
                        .getIPersistentOnlineTest().readByDistributedTest(
                                distributedTest);
                persistentSuport.getIPersistentMark().deleteByEvaluation(
                        onlineTest);
                persistentSuport.getIPersistentOnlineTest().delete(onlineTest);
            } else if (change2EvaluationType) {
                //Change to evaluation test
                //Create evaluation (onlineTest) and marks
                IOnlineTest onlineTest = new OnlineTest();
                persistentSuport.getIPersistentEvaluation().simpleLockWrite(
                        onlineTest);
                onlineTest.setDistributedTest(distributedTest);
                List executionCourseList = new ArrayList();
                executionCourseList.add(executionCourse);
                onlineTest.setAssociatedExecutionCourses(executionCourseList);
                List studentList = persistentSuport
                        .getIPersistentStudentTestQuestion()
                        .readStudentsByDistributedTest(distributedTest);
                Iterator studentIt = studentList.iterator();
                while (studentIt.hasNext()) {
                    IStudent student = (IStudent) studentIt.next();
                    List studentTestQuestionList = persistentSuport
                            .getIPersistentStudentTestQuestion()
                            .readByStudentAndDistributedTest(student,
                                    distributedTest);
                    Iterator studentTestQuestionIt = studentTestQuestionList
                            .iterator();
                    double studentMark = 0;
                    while (studentTestQuestionIt.hasNext()) {
                        IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) studentTestQuestionIt
                                .next();
                        studentMark += studentTestQuestion
                                .getTestQuestionMark().doubleValue();
                    }
                    IFrequenta attend = persistentSuport
                            .getIFrequentaPersistente()
                            .readByAlunoAndDisciplinaExecucao(student,
                                    executionCourse);
                    if (attend != null) {
                        IMark mark = new Mark();
                        persistentSuport.getIPersistentMark().simpleLockWrite(
                                mark);
                        mark.setAttend(attend);
                        mark.setEvaluation(onlineTest);
                        mark.setMark(new java.text.DecimalFormat("#0.##")
                                .format(studentMark));
                    }
                }
            }
            return true;
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
    }

    private IQuestion getStudentQuestion(List questions)
            throws ExcepcaoPersistencia {
        IQuestion question = null;
        if (questions.size() != 0) {
            Random r = new Random();
            int questionIndex = r.nextInt(questions.size());
            question = (IQuestion) questions.get(questionIndex);
        }
        return question;
    }

    private List returnStudentsFromShiftsArray(
            ISuportePersistente persistentSuport, String[] shifts)
            throws FenixServiceException {
        List studentsList = new ArrayList();
        try {
            ITurnoPersistente persistentShift = persistentSuport
                    .getITurnoPersistente();
            for (int i = 0; i < shifts.length; i++) {
                if (shifts[i].equals("Todos os Turnos")) {
                    continue;
                } else {
                    ITurno shift = new Turno(new Integer(shifts[i]));
                    shift = (ITurno) persistentShift.readByOId(shift, false);
                    Iterator studentIt = persistentSuport
                            .getITurnoAlunoPersistente().readByShift(shift)
                            .iterator();
                    while (studentIt.hasNext()) {
                        IStudent student = (IStudent) studentIt.next();
                        if (!studentsList.contains(student))
                                studentsList.add(student);
                    }
                }
            }
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
        return studentsList;
    }

    private List returnStudentsFromStudentsArray(
            ISuportePersistente persistentSuport, String[] students,
            Integer executionCourseId) throws FenixServiceException {
        List studentsList = new ArrayList();
        try {
            for (int i = 0; i < students.length; i++) {
                if (students[i].equals("Todos os Alunos")) {
                    IExecutionCourse executionCourse = new ExecutionCourse(
                            executionCourseId);
                    executionCourse = (IExecutionCourse) persistentSuport
                            .getIPersistentExecutionCourse().readByOId(
                                    executionCourse, false);
                    List attendList = persistentSuport
                            .getIFrequentaPersistente().readByExecutionCourse(
                                    executionCourse);
                    Iterator iterStudent = attendList.listIterator();
                    while (iterStudent.hasNext()) {
                        IFrequenta attend = (Frequenta) iterStudent.next();
                        IStudent student = attend.getAluno();
                        studentsList.add(student);
                    }
                    break;
                } else {
                    IStudent student = new Student(new Integer(students[i]));
                    student = (IStudent) persistentSuport
                            .getIPersistentStudent().readByOId(student, false);
                    if (!studentsList.contains(student))
                            studentsList.add(student);
                }
            }
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
        return studentsList;
    }

    private List getPersonListFromStudentList(List students) {
        List personList = new ArrayList();
        Iterator it = students.iterator();
        while (it.hasNext())
            personList.add(((IStudent) it.next()).getPerson());
        return personList;
    }

    private String getDateFormatted(Calendar date) {
        String result = new String();
        result += date.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += date.get(Calendar.MONTH) + 1;
        result += "/";
        result += date.get(Calendar.YEAR);
        return result;
    }

    private String getHourFormatted(Calendar hour) {
        String result = new String();
        result += hour.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (hour.get(Calendar.MINUTE) < 10) result += "0";
        result += hour.get(Calendar.MINUTE);
        return result;
    }

    private IAdvisory createTestAdvisory(IDistributedTest distributedTest,
            boolean dateChanges) {
        return createTestAdvisory(distributedTest, distributedTest
                .getBeginDate(), distributedTest.getBeginHour(),
                distributedTest.getEndDate(), distributedTest.getEndHour(),
                dateChanges);
    }

    private IAdvisory createTestAdvisory(IDistributedTest distributedTest,
            Calendar beginDate, Calendar beginHour, Calendar endDate,
            Calendar endHour, boolean dateChanges) {
        IAdvisory advisory = new Advisory();
        advisory.setCreated(null);
        advisory.setExpires(endDate.getTime());
        advisory.setSender("Docente da disciplina "
                + ((IExecutionCourse) distributedTest.getTestScope()
                        .getDomainObject()).getNome());
        String msgBeginning;
        if (dateChanges) {
            advisory.setSubject(distributedTest.getTitle()
                    + ": Altera��o de datas");
            if (distributedTest.getTestType().equals(
                    new TestType(TestType.INQUIRY)))
                msgBeginning = new String(
                        "As datas para responder ao <a href='"
                                + this.contextPath
                                + "/student/studentTests.do?method=prepareToDoTest&testCode="
                                + distributedTest.getIdInternal()
                                + "'>question�rio</a> foram alteradas. Dever� responder ao question�rio entre ");
            else
                msgBeginning = new String(
                        "As datas de realiza��o da <a href='"
                                + this.contextPath
                                + "/student/studentTests.do?method=prepareToDoTest&testCode="
                                + distributedTest.getIdInternal()
                                + "'>Ficha de Trabalho</a> foram alteradas. Dever� realizar a ficha entre ");
        } else {
            advisory.setSubject(distributedTest.getTitle());
            if (distributedTest.getTestType().equals(
                    new TestType(TestType.INQUIRY)))
                msgBeginning = new String(
                        "Tem um <a href='"
                                + this.contextPath
                                + "/student/studentTests.do?method=prepareToDoTest&testCode="
                                + distributedTest.getIdInternal()
                                + "'>question�rio</a> para responder entre ");
            else
                msgBeginning = new String(
                        "Tem uma <a href='"
                                + this.contextPath
                                + "/student/studentTests.do?method=prepareToDoTest&testCode="
                                + distributedTest.getIdInternal()
                                + "'>Ficha de Trabalho</a> a realizar entre ");
        }
        advisory.setMessage(msgBeginning + " as " + getHourFormatted(beginHour)
                + " de " + getDateFormatted(beginDate) + " e as "
                + getHourFormatted(endHour) + " de "
                + getDateFormatted(endDate));
        advisory.setOnlyShowOnce(new Boolean(false));
        return advisory;
    }

    private IDistributedTestAdvisory createDistributedTestAdvisory(
            IDistributedTest distributedTest, IAdvisory advisory) {
        IDistributedTestAdvisory distributedTestAdvisory = new DistributedTestAdvisory();
        distributedTestAdvisory.setAdvisory(advisory);
        distributedTestAdvisory.setDistributedTest(distributedTest);
        return distributedTestAdvisory;
    }
}