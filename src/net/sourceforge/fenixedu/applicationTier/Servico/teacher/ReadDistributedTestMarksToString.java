/*
 * Created on Nov 3, 2003
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 *  
 */
public class ReadDistributedTestMarksToString implements IService {

    public String run(Integer executionCourseId, Integer distributedTestId)
            throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSuport;

        persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IDistributedTest distributedTest = (IDistributedTest) persistentSuport
                .getIPersistentDistributedTest().readByOID(DistributedTest.class, distributedTestId);
        if (distributedTest == null)
            throw new InvalidArgumentsServiceException();

        String result = new String("N�mero\tNome\t");
        for (int i = 1; i <= distributedTest.getNumberOfQuestions().intValue(); i++) {
            result = result.concat("P" + i + "\t");
        }
        result = result.concat("Nota");
        List studentTestQuestionList = persistentSuport.getIPersistentStudentTestQuestion()
                .readByDistributedTest(distributedTest);
        if (studentTestQuestionList == null || studentTestQuestionList.size() == 0)
            throw new FenixServiceException();
        Iterator it = studentTestQuestionList.iterator();
        int questionIndex = 0;
        Double maximumMark = persistentSuport.getIPersistentStudentTestQuestion()
                .getMaximumDistributedTestMark(distributedTest);
        if (maximumMark.doubleValue() > 0)
            result = result.concat("\tNota (%)\n");
        else
            result = result.concat("\n");
        Double finalMark = new Double(0);
        DecimalFormat df = new DecimalFormat("#0.##");
        DecimalFormat percentageFormat = new DecimalFormat("#%");
        while (it.hasNext()) {
            IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
            if (questionIndex == 0) {
                result = result.concat(studentTestQuestion.getStudent().getNumber() + "\t"
                        + studentTestQuestion.getStudent().getPerson().getNome() + "\t");
            }
            result = result.concat(df.format(studentTestQuestion.getTestQuestionMark()) + "\t");
            finalMark = new Double(finalMark.doubleValue()
                    + studentTestQuestion.getTestQuestionMark().doubleValue());
            questionIndex++;
            if (questionIndex == distributedTest.getNumberOfQuestions().intValue()) {
                if (finalMark.doubleValue() < 0)
                    result = result.concat("0\t");
                else
                    result = result.concat(df.format(finalMark.doubleValue()) + "\t");
                if (maximumMark.doubleValue() > 0) {
                    double finalMarkPercentage = finalMark.doubleValue()
                            * java.lang.Math.pow(maximumMark.doubleValue(), -1);
                    if (finalMarkPercentage < 0) {
                        result = result.concat("0%");
                    } else
                        result = result.concat(percentageFormat.format(finalMarkPercentage));
                }
                result = result.concat("\n");
                finalMark = new Double(0);
                questionIndex = 0;
            }
        }
        return result;
    }

    public String run(Integer executionCourseId, String[] distributedTestCodes)
            throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSuport;
        String result = new String("N�mero\tNome\t");
        persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport
                .getIPersistentStudentTestQuestion();
        IPersistentDistributedTest persistentDistributedTest = persistentSuport
                .getIPersistentDistributedTest();

        List studentsFromAttendsList = (List) CollectionUtils.collect(persistentSuport
                .getIFrequentaPersistente().readByExecutionCourse(executionCourseId), new Transformer() {

            public Object transform(Object input) {
                return ((IAttends) input).getAluno();
            }
        });
        List distributedTestIdsList = new ArrayList();
        CollectionUtils.addAll(distributedTestIdsList, distributedTestCodes);
        List studentsFromTestsList = persistentStudentTestQuestion
                .readStudentsByDistributedTests(distributedTestIdsList);
        List studentList = concatStudentsLists(studentsFromAttendsList, studentsFromTestsList);
        Double[] maxValues = new Double[distributedTestCodes.length];

        for (int i = 0; i < distributedTestCodes.length; i++) {
            IDistributedTest distributedTest = (IDistributedTest) persistentDistributedTest.readByOID(
                    DistributedTest.class, new Integer(distributedTestCodes[i]));
            if (distributedTest == null)
                throw new InvalidArgumentsServiceException();
            maxValues[i] = persistentStudentTestQuestion.getMaximumDistributedTestMark(new Integer(
                    distributedTestCodes[i]));
            result = result.concat(distributedTest.getTitle() + "\t");
            if (maxValues[i].doubleValue() > 0)
                result = result.concat("%\t");
        }

        Iterator it = studentList.iterator();

        while (it.hasNext()) {
            result = result.concat("\n");
            IStudent student = (IStudent) it.next();
            result = result.concat(student.getNumber() + "\t" + student.getPerson().getNome() + "\t");

            for (int i = 0; i < distributedTestCodes.length; i++) {

                Double finalMark = new Double(0);
                DecimalFormat df = new DecimalFormat("#0.##");
                DecimalFormat percentageFormat = new DecimalFormat("#%");

                finalMark = persistentStudentTestQuestion.readStudentTestFinalMark(new Integer(
                        distributedTestCodes[i]), student.getIdInternal());

                if (finalMark == null) {
                    result = result.concat("NA\t");
                    if (maxValues[i].doubleValue() > 0)
                        result = result.concat("NA\t");
                } else {
                    if (finalMark.doubleValue() < 0)
                        result = result.concat("0\t");
                    else {
                        result = result.concat(df.format(finalMark.doubleValue()) + "\t");
                    }
                    if (maxValues[i].doubleValue() > 0) {
                        double finalMarkPercentage = finalMark.doubleValue()
                                * java.lang.Math.pow(maxValues[i].doubleValue(), -1);
                        if (finalMarkPercentage < 0)
                            result = result.concat("0%\t");
                        else
                            result = result.concat(percentageFormat.format(finalMarkPercentage) + "\t");
                    }
                }
            }
        }
        return result;
    }

    private List concatStudentsLists(List list1, List list2) {
        Iterator it = list2.iterator();
        while (it.hasNext()) {
            IStudent student = (IStudent) it.next();
            if (!list1.contains(student))
                list1.add(student);
        }
        Collections.sort(list1, new BeanComparator("number"));
        return list1;
    }
}