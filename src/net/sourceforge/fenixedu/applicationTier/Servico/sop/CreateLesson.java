/*
 * CreateLesson.java
 *
 * Created on 2003/08/12
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o CreateLesson.
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidTimeIntervalServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonServiceResult;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftServiceResult;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;

public class CreateLesson extends Service {

    public InfoLessonServiceResult run(InfoLesson infoLesson, InfoShift infoShift)
            throws FenixServiceException, ExcepcaoPersistencia {
        final ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentObject
                .readByOID(
                        ExecutionPeriod.class,
                        infoLesson.getInfoShift().getInfoDisciplinaExecucao().getInfoExecutionPeriod()
                                .getIdInternal());

        final Shift shift = (Shift) persistentObject.readByOID(Shift.class,
                infoShift.getIdInternal());

        InfoLessonServiceResult result = validTimeInterval(infoLesson);
        if (result.getMessageType() == 1) {
            throw new InvalidTimeIntervalServiceException();
        }
        boolean resultB = validNoInterceptingLesson(infoLesson.getInfoRoomOccupation());
        if (result.isSUCESS() && resultB) {
            try {
                InfoShiftServiceResult infoShiftServiceResult = valid(shift, infoLesson);
                if (infoShiftServiceResult.isSUCESS()) {
                    RoomOccupation roomOccupation = DomainFactory.makeRoomOccupation();
                    roomOccupation.setDayOfWeek(infoLesson.getInfoRoomOccupation().getDayOfWeek());
                    roomOccupation.setStartTime(infoLesson.getInfoRoomOccupation().getStartTime());
                    roomOccupation.setEndTime(infoLesson.getInfoRoomOccupation().getEndTime());
                    roomOccupation.setFrequency(infoLesson.getInfoRoomOccupation().getFrequency());
                    roomOccupation.setWeekOfQuinzenalStart(infoLesson.getInfoRoomOccupation()
                            .getWeekOfQuinzenalStart());

                    final OldRoom sala = OldRoom.findOldRoomByName(infoLesson.getInfoSala().getNome());
                    roomOccupation.setRoom(sala);

                    final OccupationPeriod period = (OccupationPeriod) persistentObject.readByOID(OccupationPeriod.class,
                            infoLesson.getInfoRoomOccupation().getInfoPeriod().getIdInternal());
                    roomOccupation.setPeriod(period);

                    Lesson aula2 = DomainFactory.makeLesson(infoLesson.getDiaSemana(), infoLesson
                            .getInicio(), infoLesson.getFim(), infoLesson.getTipo(), sala,
                            roomOccupation, shift);

                    aula2.setExecutionPeriod(executionPeriod);
                } else {
                    throw new InvalidLoadException(infoShiftServiceResult.toString());
                }
            } catch (ExistingPersistentException ex) {
                throw new ExistingServiceException(ex);
            }
        } else {
            if (!resultB) {
                throw new InterceptingLessonException();
            } else {
                result.setMessageType(2);                
            }
        }

        return result;
    }

    private boolean validNoInterceptingLesson(InfoRoomOccupation infoRoomOccupation)
            throws FenixServiceException, ExcepcaoPersistencia {
        final OldRoom room = OldRoom.findOldRoomByName(infoRoomOccupation.getInfoRoom().getNome());
        final List<RoomOccupation> roomOccupations = room.getRoomOccupations();

        for (final RoomOccupation roomOccupation : roomOccupations) {
            if (roomOccupation.roomOccupationForDateAndTime(
                    infoRoomOccupation.getInfoPeriod().getStartDate(),
                    infoRoomOccupation.getInfoPeriod().getEndDate(),
                    infoRoomOccupation.getStartTime(),
                    infoRoomOccupation.getEndTime(),
                    infoRoomOccupation.getDayOfWeek(),
                    infoRoomOccupation.getFrequency(),
                    infoRoomOccupation.getWeekOfQuinzenalStart())) {
                return false;
            }
        }
        return true;
    }

    private InfoLessonServiceResult validTimeInterval(InfoLesson infoLesson) {
        InfoLessonServiceResult result = new InfoLessonServiceResult();
        if (infoLesson.getInicio().getTime().getTime() >= infoLesson.getFim().getTime().getTime()) {
            result.setMessageType(InfoLessonServiceResult.INVALID_TIME_INTERVAL);
        }
        return result;
    }

    private InfoShiftServiceResult valid(Shift shift, InfoLesson infoLesson)
            throws ExcepcaoPersistencia {
        InfoShiftServiceResult result = new InfoShiftServiceResult();
        result.setMessageType(InfoShiftServiceResult.SUCESS);
        double hours = getTotalHoursOfShiftType(shift);
        double lessonDuration = (getLessonDurationInMinutes(infoLesson).doubleValue()) / 60;
        if (shift.getTipo().equals(ShiftType.TEORICA)) {
            if (hours == shift.getDisciplinaExecucao().getTheoreticalHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getTheoreticalHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_EXCEEDED);
        } else if (shift.getTipo().equals(ShiftType.PRATICA)) {
            if (hours == shift.getDisciplinaExecucao().getPraticalHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getPraticalHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_EXCEEDED);
        } else if (shift.getTipo().equals(ShiftType.TEORICO_PRATICA)) {
            if (hours == shift.getDisciplinaExecucao().getTheoPratHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getTheoPratHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_EXCEEDED);
        } else if (shift.getTipo().equals(ShiftType.LABORATORIAL)) {
            if (hours == shift.getDisciplinaExecucao().getLabHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.LAB_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getLabHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.LAB_HOURS_LIMIT_EXCEEDED);
        }
        return result;
    }

    private double getTotalHoursOfShiftType(Shift shift) throws ExcepcaoPersistencia {
        Lesson lesson = null;
        double duration = 0;
        List associatedLessons = shift.getAssociatedLessons();
        for (int i = 0; i < associatedLessons.size(); i++) {
            lesson = (Lesson) associatedLessons.get(i);
            try {
                lesson.getIdInternal();
                duration += (getLessonDurationInMinutes(lesson).doubleValue() / 60);
            } catch (Exception ex) {
                // all is ok
                // the lesson contained a proxy to null.
            }
        }
        return duration;
    }

    private Integer getLessonDurationInMinutes(Lesson lesson) {
        int beginHour = lesson.getInicio().get(Calendar.HOUR_OF_DAY);
        int beginMinutes = lesson.getInicio().get(Calendar.MINUTE);
        int endHour = lesson.getFim().get(Calendar.HOUR_OF_DAY);
        int endMinutes = lesson.getFim().get(Calendar.MINUTE);
        int duration = 0;
        duration = (endHour - beginHour) * 60 + (endMinutes - beginMinutes);
        return new Integer(duration);
    }

    private Integer getLessonDurationInMinutes(InfoLesson infoLesson) {
        int beginHour = infoLesson.getInicio().get(Calendar.HOUR_OF_DAY);
        int beginMinutes = infoLesson.getInicio().get(Calendar.MINUTE);
        int endHour = infoLesson.getFim().get(Calendar.HOUR_OF_DAY);
        int endMinutes = infoLesson.getFim().get(Calendar.MINUTE);
        int duration = 0;
        duration = (endHour - beginHour) * 60 + (endMinutes - beginMinutes);
        return new Integer(duration);
    }

    public class InvalidLoadException extends FenixServiceException {
        private InvalidLoadException() {
            super();
        }

        InvalidLoadException(String s) {
            super(s);
        }
    }

    public class InterceptingLessonException extends FenixServiceException {
        private InterceptingLessonException() {
            super();
        }

        InterceptingLessonException(String s) {
            super(s);
        }
    }

}
