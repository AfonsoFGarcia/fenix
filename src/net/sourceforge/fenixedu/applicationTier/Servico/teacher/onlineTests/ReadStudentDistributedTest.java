/*
 * Created on 8/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionWithAll;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadStudentDistributedTest extends Service {
    public List run(Integer executionCourseId, Integer distributedTestId, Integer studentId, String path) throws FenixServiceException,
            ExcepcaoPersistencia {
        path = path.replace('\\', '/');
        List<InfoStudentTestQuestion> infoStudentTestQuestionList = new ArrayList<InfoStudentTestQuestion>();
        Student student = (Student) persistentSupport.getIPersistentStudent().readByOID(Student.class, studentId);
        if (student == null)
            throw new FenixServiceException();
        DistributedTest distributedTest = (DistributedTest) persistentSupport.getIPersistentDistributedTest().readByOID(DistributedTest.class,
                distributedTestId);
        if (distributedTest == null)
            throw new FenixServiceException();
        List<StudentTestQuestion> studentTestQuestionList = persistentSupport.getIPersistentStudentTestQuestion().readByStudentAndDistributedTest(
                student.getIdInternal(), distributedTest.getIdInternal());
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            InfoStudentTestQuestion infoStudentTestQuestion;
            ParseQuestion parse = new ParseQuestion();
            try {
                infoStudentTestQuestion = InfoStudentTestQuestionWithAll.newInfoFromDomain(studentTestQuestion);
                infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion, path);
                if (studentTestQuestion.getOptionShuffle() == null) {
                    studentTestQuestion.setOptionShuffle(infoStudentTestQuestion.getOptionShuffle());
                }
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }

            infoStudentTestQuestionList.add(infoStudentTestQuestion);
        }
        return infoStudentTestQuestionList;
    }
}