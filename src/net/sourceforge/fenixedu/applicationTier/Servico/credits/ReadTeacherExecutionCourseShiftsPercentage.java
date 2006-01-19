/*
 * Created on 14/Mai/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonWithInfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftPercentage;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorshipAndTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.TeacherExecutionCourseProfessorshipShiftsDTO;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author T�nia & Alexandra
 */
public class ReadTeacherExecutionCourseShiftsPercentage extends Service {

    public TeacherExecutionCourseProfessorshipShiftsDTO run(InfoTeacher infoTeacher,
            InfoExecutionCourse infoExecutionCourse) throws FenixServiceException, ExcepcaoPersistencia {

        TeacherExecutionCourseProfessorshipShiftsDTO result = new TeacherExecutionCourseProfessorshipShiftsDTO();

        List infoShiftPercentageList = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        ExecutionCourse executionCourse = readExecutionCourse(infoExecutionCourse, sp);
        Teacher teacher = readTeacher(infoTeacher, sp);

        result.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        result.setInfoTeacher(InfoTeacher.newInfoFromDomain(teacher));

        ITurnoPersistente shiftDAO = sp.getITurnoPersistente();

        List executionCourseShiftsList = null;

        executionCourseShiftsList = shiftDAO.readByExecutionCourse(executionCourse.getIdInternal());

        Iterator iterator = executionCourseShiftsList.iterator();
        while (iterator.hasNext()) {
            Shift shift = (Shift) iterator.next();

            InfoShiftPercentage infoShiftPercentage = new InfoShiftPercentage();
            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            infoShiftPercentage.setShift(infoShift);
            double availablePercentage = 100;
            InfoShiftProfessorship infoShiftProfessorship = null;

            Iterator iter = shift.getAssociatedShiftProfessorship().iterator();
            while (iter.hasNext()) {
                ShiftProfessorship shiftProfessorship = (ShiftProfessorship) iter.next();
                /**
                 * if shift's type is LABORATORIAL the shift professorship
                 * percentage can exceed 100%
                 */
                if ((shift.getTipo() != net.sourceforge.fenixedu.domain.ShiftType.LABORATORIAL)
                        && (shiftProfessorship.getProfessorship().getTeacher() != teacher)) {
                    availablePercentage -= shiftProfessorship.getPercentage().doubleValue();
                }
                infoShiftProfessorship = InfoShiftProfessorshipAndTeacher
                        .newInfoFromDomain(shiftProfessorship);
                infoShiftPercentage.addInfoShiftProfessorship(infoShiftProfessorship);
            }

            List infoLessons = (List) CollectionUtils.collect(shift.getAssociatedLessons(),
                    new Transformer() {
                        public Object transform(Object input) {
                            Lesson lesson = (Lesson) input;
                            return InfoLessonWithInfoRoom.newInfoFromDomain(lesson);
                        }
                    });
            infoShiftPercentage.setInfoLessons(infoLessons);

            infoShiftPercentage.setAvailablePercentage(Double.valueOf(availablePercentage));

            infoShiftPercentageList.add(infoShiftPercentage);
        }

        result.setInfoShiftPercentageList(infoShiftPercentageList);
        return result;
    }

    private Teacher readTeacher(InfoTeacher infoTeacher, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
        return (Teacher) teacherDAO.readByOID(Teacher.class, infoTeacher.getIdInternal());
    }

    private ExecutionCourse readExecutionCourse(InfoExecutionCourse infoExecutionCourse,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

        return (ExecutionCourse) executionCourseDAO.readByOID(
                ExecutionCourse.class, infoExecutionCourse.getIdInternal());
    }
}