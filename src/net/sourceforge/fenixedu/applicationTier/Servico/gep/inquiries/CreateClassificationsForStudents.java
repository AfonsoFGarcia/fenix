/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateClassificationsForStudents implements IService {

    private static Transformer getEntryGradeTransformer = new Transformer() {
        public Object transform(Object input) {
            return ((IStudent) input).getEntryGrade();
        }
    };

    private static Transformer getApprovationRatioTransformer = new Transformer() {
        public Object transform(Object input) {
            return ((IStudent) input).getApprovationRatio();
        }
    };

    private static Transformer getArithmeticMeanTransformer = new Transformer() {
        public Object transform(Object input) {
            return ((IStudent) input).getArithmeticMean();
        }
    };

    private static Closure setEntryGradeClosure = new Closure() {
        public void execute(Object input) {
            if (input instanceof GenericPair<?, ?>) {
                GenericPair<IStudent, Character> pairStudentClassification = (GenericPair<IStudent, Character>) input;
                pairStudentClassification.getLeft().setEntryGradeClassification(
                        pairStudentClassification.getRight());
            }
        }
    };

    private static Closure setApprovationRatioClosure = new Closure() {
        public void execute(Object input) {
            if (input instanceof GenericPair<?, ?>) {
                GenericPair<IStudent, Character> pairStudentClassification = (GenericPair<IStudent, Character>) input;
                pairStudentClassification.getLeft().setApprovationRatioClassification(
                        pairStudentClassification.getRight());
            }
        }
    };

    private static Closure setArithmeticMeanClosure = new Closure() {
        public void execute(Object input) {
            if (input instanceof GenericPair<?, ?>) {
                GenericPair<IStudent, Character> pairStudentClassification = (GenericPair<IStudent, Character>) input;
                pairStudentClassification.getLeft().setArithmeticMeanClassification(
                        pairStudentClassification.getRight());
            }
        }
    };

    public ByteArrayOutputStream run(Integer[] entryGradeLimits, Integer[] approvationRatioLimits,
            Integer[] arithmeticMeanLimits, Integer degreeCurricularPlanID) throws ExcepcaoPersistencia,
            FileNotFoundException {

        ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IExecutionYear currentExecutionYear = ps.getIPersistentExecutionYear()
                .readCurrentExecutionYear();

        List<IStudent> otherYearsStudents = new ArrayList<IStudent>();
        List<IStudent> firstYearStudents = new ArrayList<IStudent>();

        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) ps
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        degreeCurricularPlanID);
        List<IStudentCurricularPlan> studentCurricularPlans = degreeCurricularPlan
                .getStudentCurricularPlans();

        for (IStudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
            if (studentCurricularPlan.getCurrentState() == StudentCurricularPlanState.ACTIVE) {
                IStudent student = studentCurricularPlan.getStudent();
                if (student.getRegistrationYear() == currentExecutionYear) {
                    firstYearStudents.add(student);
                } else {
                    student.calculateApprovationRatioAndArithmeticMeanIfActive(currentExecutionYear);
                    otherYearsStudents.add(student);
                }
            }
        }

        Arrays.sort(entryGradeLimits);
        Arrays.sort(approvationRatioLimits);
        Arrays.sort(arithmeticMeanLimits);

        HashMap<Character, List<IStudent>> splitedStudentsByEntryGrade = splitStudentsByClassification(
                entryGradeLimits, firstYearStudents, "entryGrade", getEntryGradeTransformer,
                setEntryGradeClosure);
        ByteArrayOutputStream entryGradeStream = studentsRenderer(splitedStudentsByEntryGrade,
                getEntryGradeTransformer);

        HashMap<Character, List<IStudent>> splitedStudentsByApprovationRatio = splitStudentsByClassification(
                approvationRatioLimits, otherYearsStudents, "approvationRatio",
                getApprovationRatioTransformer, setApprovationRatioClosure);
        ByteArrayOutputStream approvationRatioStream = studentsRenderer(
                splitedStudentsByApprovationRatio, getApprovationRatioTransformer);

        HashMap<Character, List<IStudent>> splitedStudentsByArithmeticMean = splitStudentsByClassification(
                arithmeticMeanLimits, otherYearsStudents, "arithmeticMean",
                getArithmeticMeanTransformer, setArithmeticMeanClosure);
        ByteArrayOutputStream arithmeticMeanStream = studentsRenderer(splitedStudentsByArithmeticMean,
                getArithmeticMeanTransformer);

        return zipResults(entryGradeStream, approvationRatioStream, arithmeticMeanStream);

    }

    private HashMap<Character, List<IStudent>> splitStudentsByClassification(Integer[] limits,
            List<IStudent> students, String field, Transformer fieldGetter, Closure fieldSetter) {
        HashMap<Character, List<IStudent>> studentsClassifications = new HashMap<Character, List<IStudent>>(
                limits.length);

        Collections.sort(students, new BeanComparator(field));

        char classification = 'A';
        ListIterator<IStudent> studentsIter = students.listIterator(students.size());
        for (int i = limits.length - 1; i > 0; i--) {

            int groundLimitIndex = (int) Math.ceil(students.size() * (limits[i - 1] / 100.0));
            Double groundLimitValue = (Double) fieldGetter.transform(students.get(groundLimitIndex));

            int limitIndex = studentsIter.previousIndex() + 1;
            for (IStudent student = studentsIter.previous(); (Double) fieldGetter.transform(student) > groundLimitValue; student = studentsIter
                    .previous()) {
                fieldSetter.execute(new GenericPair<IStudent, Character>(student, classification));
            }
            studentsIter.next();
            groundLimitIndex = studentsIter.nextIndex();

            studentsClassifications.put(classification, students.subList(groundLimitIndex, limitIndex));

            classification++;
        }

        List<IStudent> weakStudents = students.subList((int) Math.ceil(students.size()
                * (limits[0] / 100.0)), studentsIter.nextIndex());
        for (IStudent weakStudent : weakStudents) {
            fieldSetter.execute(new GenericPair<IStudent, Character>(weakStudent, classification));
        }
        studentsClassifications.put(classification, weakStudents);
        return studentsClassifications;
    }

    private ByteArrayOutputStream studentsRenderer(
            HashMap<Character, List<IStudent>> studentsClassifications, Transformer transformer) {

        List<Character> keys = new ArrayList<Character>(studentsClassifications.keySet());
        Collections.sort(keys);

        OutputStream outputStream = new ByteArrayOutputStream();
        Formatter fmt = new Formatter(outputStream);

        for (Character classification : keys) {
            for (ListIterator<IStudent> studentIter = studentsClassifications.get(classification)
                    .listIterator(studentsClassifications.get(classification).size()); studentIter.hasPrevious();) {
                IStudent student = (IStudent) studentIter.previous();
                fmt.format("%d\t%s\t%f\t%c\n", student.getNumber(), student.getPerson().getNome(),
                        transformer.transform(student), classification);
            }
        }

        fmt.flush();

        return (ByteArrayOutputStream) outputStream;

    }

    private ByteArrayOutputStream zipResults(ByteArrayOutputStream entryGradeStream,
            ByteArrayOutputStream approvationRatioStream, ByteArrayOutputStream arithmeticMeanStream) {

        String[] filenames = new String[] { "entryGrade", "approvationRatio", "arithmeticMean" };
        ByteArrayOutputStream[] outputStreams = new ByteArrayOutputStream[] { entryGradeStream,
                approvationRatioStream, arithmeticMeanStream };

        try {

            ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
            ZipOutputStream out = new ZipOutputStream(resultStream);

            // Compress the files
            for (int i = 0; i < filenames.length; i++) {

                out.putNextEntry(new ZipEntry(filenames[i]));
                out.write(outputStreams[i].toByteArray());
                out.closeEntry();
            }

            // Complete the ZIP file
            out.close();

            return resultStream;

        } catch (IOException e) {
        }

        return null;

    }

}
