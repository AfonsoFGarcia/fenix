/*
 * Created on Dec 22, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.credits.calcutation;

import java.util.Iterator;
import java.util.List;

import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.ISupportLesson;
import Dominio.ITeacher;
import Dominio.credits.IOtherTypeCreditLine;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.credits.IPersistentOtherTypeCreditLine;
import ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;
import ServidorPersistente.teacher.professorship.IPersistentSupportLesson;
import ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import Util.date.TimePeriod;

/**
 * @author jpvl
 */
public class CreditsCalculator
{
    private static CreditsCalculator calculator = new CreditsCalculator();

    public static CreditsCalculator getInstance()
    {
        return calculator;
    }

    /**
     *  
     */
    private CreditsCalculator()
    {
        super();
    }

    /**
     * @param professorship
     * @param shiftProfessorshipAdded
     * @param shiftProfessorshipDeleted
     * @param sp
     * @return
     */
    public Double calculateLessons(
        IProfessorship professorship,
        List shiftProfessorshipAdded,
        List shiftProfessorshipDeleted,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {

//        IPersistentShiftProfessorship shiftProfessorshipDAO = sp.getIPersistentShiftProfessorship();
//
//        List shiftProfessorshipList = null;
//        if (shiftProfessorshipDeleted.isEmpty())
//        {
//            shiftProfessorshipList =
//                shiftProfessorshipDAO.readByTeacherAndExecutionPeriod(
//                    professorship.getTeacher(),
//                    professorship.getExecutionCourse().getExecutionPeriod());
//        } else
//        {
//            List shiftProfessorshipsIds = TransformationUtils.transformToIds(shiftProfessorshipDeleted);
//            shiftProfessorshipList =
//                shiftProfessorshipDAO.readByTeacherAndExecutionPeriodWithDifferentIds(
//                    professorship.getTeacher(),
//                    professorship.getExecutionCourse().getExecutionPeriod(),
//                    shiftProfessorshipsIds);
//        }
//
//        shiftProfessorshipList.removeAll(shiftProfessorshipAdded);
//        shiftProfessorshipList.addAll(shiftProfessorshipAdded);
//
//        Iterator iterator = shiftProfessorshipList.iterator();
//
//        double hours = 0;
//
//        while (iterator.hasNext())
//        {
//            IShiftProfessorship shiftProfessorship = (IShiftProfessorship) iterator.next();
//            double shiftHours = calcuteShiftHours(shiftProfessorship.getShift());
//            hours += shiftHours * shiftProfessorship.getPercentage().doubleValue() / 100;
//        }
//
        //return new Double(hours);
        return new Double(0);
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param supportLesson
     * @param sp
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public Double calculateSupportLessonsAfterDelete(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        ISupportLesson supportLesson,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();

        List supportLessonList =
            supportLessonDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);

        Iterator iterator = supportLessonList.iterator();

        double hours = 0;
        while (iterator.hasNext())
        {
            ISupportLesson sl = (ISupportLesson) iterator.next();
            if (!sl.getIdInternal().equals(supportLesson.getIdInternal()))
            {
                TimePeriod timePeriod = new TimePeriod(sl.getStartTime(), sl.getEndTime());
                hours += timePeriod.hours().doubleValue();
            }
        }
        return new Double(hours);
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param supportLesson
     * @param sp
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public Double calculateSupportLessonsAfterInsert(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        ISupportLesson supportLesson,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();

        List supportLessonList =
            supportLessonDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);

        Iterator iterator = supportLessonList.iterator();

        TimePeriod timePeriod = new TimePeriod(supportLesson.getStartTime(), supportLesson.getEndTime());
        double hours = timePeriod.hours().doubleValue();

        while (iterator.hasNext())
        {
            ISupportLesson sl = (ISupportLesson) iterator.next();
            if (!sl.getIdInternal().equals(supportLesson.getIdInternal()))
            {
                TimePeriod timePeriod2 = new TimePeriod(sl.getStartTime(), sl.getEndTime());
                hours += timePeriod2.hours().doubleValue();
            }
        }

        return new Double(hours);
    }

    /**
     * @param degreeFinalProjectStudent
     * @param iterator
     * @param numberOfStudents
     * @return
     */
    private double calculeDFPSCredits(
        ITeacherDegreeFinalProjectStudent degreeFinalProjectStudent,
        Iterator iterator,
        double numberOfStudents)
    {
        while (iterator.hasNext())
        {
            ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent =
                (ITeacherDegreeFinalProjectStudent) iterator.next();

            if (!teacherDegreeFinalProjectStudent
                .getIdInternal()
                .equals(degreeFinalProjectStudent.getIdInternal()))
            {
                numberOfStudents += teacherDegreeFinalProjectStudent.getPercentage().doubleValue() / 100;
            }
        }
        return numberOfStudents;
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param degreeFinalProjectStudent
     * @param sp
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public Double calcuteDegreeFinalProjectStudentAfterDelete(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        ITeacherDegreeFinalProjectStudent degreeFinalProjectStudent,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentTeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudentDAO =
            sp.getIPersistentTeacherDegreeFinalProjectStudent();
        List teacherDegreeFinalProjectStudents =
            teacherDegreeFinalProjectStudentDAO.readByTeacherAndExecutionPeriod(
                teacher,
                executionPeriod);

        Iterator iterator = teacherDegreeFinalProjectStudents.iterator();

        double numberOfStudents = 0;

        numberOfStudents = calculeDFPSCredits(degreeFinalProjectStudent, iterator, numberOfStudents);

        return new Double(numberOfStudents);
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param degreeFinalProjectStudent
     * @param sp
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public Double calcuteDegreeFinalProjectStudentAfterInsert(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        ITeacherDegreeFinalProjectStudent degreeFinalProjectStudent,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {

        IPersistentTeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudentDAO =
            sp.getIPersistentTeacherDegreeFinalProjectStudent();
        List teacherDegreeFinalProjectStudents =
            teacherDegreeFinalProjectStudentDAO.readByTeacherAndExecutionPeriod(
                teacher,
                executionPeriod);

        Iterator iterator = teacherDegreeFinalProjectStudents.iterator();

        double numberOfStudents = degreeFinalProjectStudent.getPercentage().doubleValue() / 100;

        numberOfStudents = calculeDFPSCredits(degreeFinalProjectStudent, iterator, numberOfStudents);

        return new Double(numberOfStudents);
    }

//    /**
//     * @param turno
//     * @return
//     */
//    private double calcuteShiftHours(ITurno shift)
//    {
//        Iterator lessonsIterator = shift.getAssociatedLessons().iterator();
//        double hours = 0;
//        while (lessonsIterator.hasNext())
//        {
//            IAula lesson = (IAula) lessonsIterator.next();
//            TimePeriod timePeriod = new TimePeriod(lesson.getInicio(), lesson.getFim());
//            hours += timePeriod.hours().doubleValue();
//        }
//        return hours;
//    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param teacherInstitutionWorkTime
     * @param sp
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public Double calcuteTeacherInstitutionWorkingTimeAfterDelete(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        ITeacherInstitutionWorkTime teacherInstitutionWorkTime,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkingTimeDAO =
            sp.getIPersistentTeacherInstitutionWorkingTime();

        List teacherInstitutionWorkTimes =
            teacherInstitutionWorkingTimeDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);

        teacherInstitutionWorkTimes.remove(teacherInstitutionWorkTime);

        Iterator iterator = teacherInstitutionWorkTimes.iterator();
        double hours = 0;
        while (iterator.hasNext())
        {
            ITeacherInstitutionWorkTime tiwt = (ITeacherInstitutionWorkTime) iterator.next();
            if (!tiwt.getIdInternal().equals(teacherInstitutionWorkTime.getIdInternal()))
            {
                TimePeriod timePeriod = new TimePeriod(tiwt.getStartTime(), tiwt.getEndTime());
                hours += timePeriod.hours().doubleValue();
            }
        }

        return new Double(hours);
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param teacherInstitutionWorkTime
     * @param sp
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public Double calcuteTeacherInstitutionWorkingTimeAfterInsert(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        ITeacherInstitutionWorkTime teacherInstitutionWorkTime,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentTeacherInstitutionWorkingTime teacherInstitutionWorkTimeDAO =
            sp.getIPersistentTeacherInstitutionWorkingTime();

        List teacherInstitutionWorkTimes =
            teacherInstitutionWorkTimeDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);

        Iterator iterator = teacherInstitutionWorkTimes.iterator();
        TimePeriod timePeriod =
            new TimePeriod(
                teacherInstitutionWorkTime.getStartTime(),
                teacherInstitutionWorkTime.getEndTime());
        double hours = timePeriod.hours().doubleValue();
        while (iterator.hasNext())
        {
            ITeacherInstitutionWorkTime tiwt = (ITeacherInstitutionWorkTime) iterator.next();
            if (!tiwt.getIdInternal().equals(teacherInstitutionWorkTime.getIdInternal()))
            {
                TimePeriod timePeriod2 = new TimePeriod(tiwt.getStartTime(), tiwt.getEndTime());
                hours += timePeriod2.hours().doubleValue();
            }
        }

        return new Double(hours);
    }

    /**
     * @param teacher
     * @param executionPeriod
     * @param otherTypeCreditLine
     * @param sp
     * @return
     */
    public Double calcuteOtherTypeCreditLineAfterInsert(
        ITeacher teacher,
        IExecutionPeriod executionPeriod,
        IOtherTypeCreditLine otherTypeCreditLine,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentOtherTypeCreditLine otherTypeCreditLineDAO = sp.getIPersistentOtherTypeCreditLine();

        List creditLines =
            otherTypeCreditLineDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);

        double credits = otherTypeCreditLine.getCredits().doubleValue();
        for (int i = 0; i < creditLines.size(); i++)
        {
            IOtherTypeCreditLine creditLine = (IOtherTypeCreditLine) creditLines.get(i);
            if (!creditLine.getIdInternal().equals(otherTypeCreditLine.getIdInternal()))
            {
               credits += creditLine.getCredits().doubleValue(); 	
            }
        }

        return new Double(credits);
    }
    
    public Double calcuteOtherTypeCreditLineAfterDelete(
            ITeacher teacher,
            IExecutionPeriod executionPeriod,
            IOtherTypeCreditLine otherTypeCreditLine,
            ISuportePersistente sp)
    throws ExcepcaoPersistencia
    {
        IPersistentOtherTypeCreditLine otherTypeCreditLineDAO = sp.getIPersistentOtherTypeCreditLine();

        List creditLines =
            otherTypeCreditLineDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);

        double credits = 0;
        for (int i = 0; i < creditLines.size(); i++)
        {
            IOtherTypeCreditLine creditLine = (IOtherTypeCreditLine) creditLines.get(i);
            if (!creditLine.getIdInternal().equals(otherTypeCreditLine.getIdInternal()))
            {
                credits += creditLine.getCredits().doubleValue(); 	
            }
        }

        return new Double(credits);
    }
    
}
