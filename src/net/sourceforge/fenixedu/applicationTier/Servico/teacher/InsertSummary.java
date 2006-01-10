/*
 * Created on 21/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.utils.summary.SummaryUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Jo�o Mota
 * @author Susana Fernandes modified by T�nia Pous�o 5/Abr/2004 21/Jul/2003
 *         fenix-head ServidorAplicacao.Servico.teacher
 * 
 * @author jdnf and mrsp 21/Jul/2005
 * 
 */
public class InsertSummary implements IService {

    public Boolean run(Integer executionCourseID, InfoSummary infoSummary) throws FenixServiceException,
            ExcepcaoPersistencia {

        if (infoSummary == null || infoSummary.getInfoShift() == null
                || infoSummary.getInfoShift().getIdInternal() == null
                || infoSummary.getIsExtraLesson() == null || infoSummary.getSummaryDate() == null) {
            throw new FenixServiceException("error.summary.impossible.insert");
        }

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();

        final ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseID);
        if (executionCourse == null)
            throw new InvalidArgumentsServiceException();

        final Shift shift = SummaryUtils.getShift(persistentSupport, null, infoSummary);
        final Room room = SummaryUtils.getRoom(persistentSupport, null, shift, infoSummary);

        final Professorship professorship = SummaryUtils.getProfessorship(persistentSupport,
                infoSummary);
        if (professorship != null) {
            final Summary summary = executionCourse.createSummary(infoSummary.getTitle(), infoSummary
                    .getSummaryText(), infoSummary.getStudentsNumber(), infoSummary.getIsExtraLesson(),
                    professorship);
            shift.transferSummary(summary, infoSummary.getSummaryDate().getTime(), infoSummary
                    .getSummaryHour().getTime(), room, true);
            return true;
        }

        final Teacher teacher = SummaryUtils.getTeacher(persistentSupport, infoSummary);
        if (teacher != null) {
            if (!executionCourse.teacherLecturesExecutionCourse(teacher)) {
                final Summary summary = executionCourse.createSummary(infoSummary.getTitle(),
                        infoSummary.getSummaryText(), infoSummary.getStudentsNumber(), infoSummary
                                .getIsExtraLesson(), teacher);
                shift.transferSummary(summary, infoSummary.getSummaryDate().getTime(), infoSummary
                        .getSummaryHour().getTime(), room, true);
                return true;
            }
            else{
                throw new FenixServiceException("error.summary.teacher.invalid");
            }
        }

        String teacherName = infoSummary.getTeacherName();
        if (teacherName != null) {
            final Summary summary = executionCourse.createSummary(infoSummary.getTitle(), infoSummary
                    .getSummaryText(), infoSummary.getStudentsNumber(), infoSummary.getIsExtraLesson(),
                    teacherName);
            shift.transferSummary(summary, infoSummary.getSummaryDate().getTime(), infoSummary
                    .getSummaryHour().getTime(), room, true);
            return true;
        }

        throw new FenixServiceException("error.summary.no.teacher");
    }
}