/*
 * Created on 1/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoTestScope;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Susana Fernandes
 */
public class GenetareStudentTestForSimulation extends Service {
    public List run(Integer executionCourseId, Integer testId, String path, TestType testType, CorrectionAvailability correctionAvailability,
            Boolean imsfeedback, String testInformation) throws FenixServiceException, ExcepcaoPersistencia {
        List<InfoStudentTestQuestion> infoStudentTestQuestionList = new ArrayList<InfoStudentTestQuestion>();
        path = path.replace('\\', '/');
        final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final Test test = (Test) persistentSuport.getIPersistentTest().readByOID(Test.class, testId);
        if (test == null)
            throw new InvalidArgumentsServiceException();

        TestScope testScope = persistentSuport.getIPersistentTestScope().readByDomainObject(ExecutionCourse.class.getName(), executionCourseId);

        if (testScope == null) {
            final ExecutionCourse executionCourse = (ExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null)
                throw new InvalidArgumentsServiceException();
            testScope = DomainFactory.makeTestScope(executionCourse);
        }

        InfoDistributedTest infoDistributedTest = new InfoDistributedTest();
        infoDistributedTest.setIdInternal(testId);
        infoDistributedTest.setInfoTestScope(InfoTestScope.newInfoFromDomain(testScope));
        infoDistributedTest.setTestType(testType);
        infoDistributedTest.setCorrectionAvailability(correctionAvailability);
        infoDistributedTest.setImsFeedback(imsfeedback);
        infoDistributedTest.setTitle(test.getTitle());
        infoDistributedTest.setTestInformation(testInformation);
        infoDistributedTest.setNumberOfQuestions(test.getTestQuestionsCount());

        List<TestQuestion> testQuestionList = persistentSuport.getIPersistentTestQuestion().readByTest(testId);
        for (TestQuestion testQuestionExample : testQuestionList) {

            List<Question> questionList = new ArrayList<Question>();
            questionList.addAll(testQuestionExample.getQuestion().getMetadata().getVisibleQuestions());

            InfoStudentTestQuestion infoStudentTestQuestion = new InfoStudentTestQuestion();
            infoStudentTestQuestion.setDistributedTest(infoDistributedTest);
            infoStudentTestQuestion.setTestQuestionOrder(testQuestionExample.getTestQuestionOrder());
            infoStudentTestQuestion.setTestQuestionValue(testQuestionExample.getTestQuestionValue());
            infoStudentTestQuestion.setOldResponse(new Integer(0));
            infoStudentTestQuestion.setCorrectionFormula(testQuestionExample.getCorrectionFormula());
            infoStudentTestQuestion.setTestQuestionMark(new Double(0));
            infoStudentTestQuestion.setResponse(null);

            if (questionList.size() == 0)
                questionList.addAll(testQuestionExample.getQuestion().getMetadata().getVisibleQuestions());
            Question question = getStudentQuestion(questionList);
            if (question == null) {
                throw new InvalidArgumentsServiceException();
            }
            infoStudentTestQuestion.setQuestion(InfoQuestion.newInfoFromDomain(question));
            ParseQuestion parse = new ParseQuestion();
            try {
                infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion, path);
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            infoStudentTestQuestionList.add(infoStudentTestQuestion);
            questionList.remove(question);
        }

        return infoStudentTestQuestionList;
    }

    private Question getStudentQuestion(List<Question> questions) {
        Question question = null;
        if (questions.size() != 0) {
            Random r = new Random();
            int questionIndex = r.nextInt(questions.size());
            question = questions.get(questionIndex);
        }
        return question;
    }

}