package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InterceptingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidTimeIntervalServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonServiceResult;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftServiceResult;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.applicationTier.Service;

public class EditLesson extends Service {

    public Object run(InfoLesson aulaAntiga, InfoLesson aulaNova, InfoShift infoShift)
            throws FenixServiceException, ExcepcaoPersistencia {
        InfoLessonServiceResult result = null;

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IAulaPersistente aulaPersistente = persistentSupport.getIAulaPersistente();

        Room salaNova = persistentSupport.getISalaPersistente().readByName(aulaNova.getInfoSala().getNome());
        Lesson aula = (Lesson) aulaPersistente.readByOID(Lesson.class, aulaAntiga.getIdInternal());
        Shift shift = aula.getShift();

        RoomOccupation roomOccupation = aula.getRoomOccupation();

        if (aula != null) {
            result = valid(aulaNova.getInicio(), aulaNova.getFim());
            if (result.getMessageType() == 1) {
                throw new InvalidTimeIntervalServiceException();
            }
            boolean resultB = validNoInterceptingLesson(
                    aulaNova.getInfoRoomOccupation().getStartTime(),
                    aulaNova.getInfoRoomOccupation().getEndTime(), 
                    aulaNova.getInfoRoomOccupation().getDayOfWeek(), 
                    aulaNova.getInfoRoomOccupation().getFrequency(), 
                    aulaNova.getInfoRoomOccupation().getWeekOfQuinzenalStart(),
                    salaNova,
                    roomOccupation);

            InfoShiftServiceResult infoShiftServiceResult = valid(shift, aula.getIdInternal(), aulaNova.getInicio(), aulaNova.getFim());
            if (result.isSUCESS() && resultB && infoShiftServiceResult.isSUCESS()) {
                aula.getShift();

                aula.setDiaSemana(aulaNova.getDiaSemana());
                aula.setInicio(aulaNova.getInicio());
                aula.setFim(aulaNova.getFim());
                aula.setTipo(aulaNova.getTipo());
                aula.setSala(salaNova);
                
                roomOccupation.setDayOfWeek(aulaNova.getDiaSemana());
                roomOccupation.setStartTime(aulaNova.getInicio());
                roomOccupation.setEndTime(aulaNova.getFim());
                roomOccupation.setRoom(salaNova);

                roomOccupation.setFrequency(aulaNova.getInfoRoomOccupation().getFrequency());
                roomOccupation.setWeekOfQuinzenalStart(aulaNova.getInfoRoomOccupation()
                        .getWeekOfQuinzenalStart());

            } else if (!infoShiftServiceResult.isSUCESS()) {
                throw new InvalidLoadException(infoShiftServiceResult.toString());
            }
        }

        return result;
    }

    private InfoLessonServiceResult valid(Calendar start, Calendar end) {
        InfoLessonServiceResult result = new InfoLessonServiceResult();
        if (start.getTime().getTime() >= end.getTime().getTime()) {
            result.setMessageType(InfoLessonServiceResult.INVALID_TIME_INTERVAL);
        }
        return result;
    }


    private boolean validNoInterceptingLesson(Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, Integer frequency, Integer week, Room room, RoomOccupation oldroomOccupation)
            throws FenixServiceException, ExcepcaoPersistencia {
        final List<RoomOccupation> roomOccupations = room.getRoomOccupations();

        for (final RoomOccupation roomOccupation : roomOccupations) {
            if (roomOccupation != oldroomOccupation && 
                    roomOccupation.roomOccupationForDateAndTime(
                    oldroomOccupation.getPeriod().getStartDate(),
                    oldroomOccupation.getPeriod().getEndDate(),
                    startTime, endTime, dayOfWeek, frequency, week)) {
                throw new InterceptingServiceException();
            }
        }
        return true;
    }

    private InfoShiftServiceResult valid(Shift shift, Integer lessonId, Calendar start, Calendar end) throws ExcepcaoPersistencia {
        InfoShiftServiceResult result = new InfoShiftServiceResult();
        result.setMessageType(InfoShiftServiceResult.SUCESS);
        double hours = getTotalHoursOfShiftType(shift, lessonId, start, end);
        double lessonDuration = (getLessonDurationInMinutes(start.getTime(), end.getTime()).doubleValue()) / 60;

        if (shift.getTipo().equals(ShiftType.TEORICA)) {
            if (hours == shift.getDisciplinaExecucao().getTheoreticalHours().doubleValue()) {
                result.setMessageType(InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_REACHED);
            } else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getTheoreticalHours()
                    .doubleValue()) {
                result.setMessageType(InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_EXCEEDED);
            }
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

    private double getTotalHoursOfShiftType(Shift shift, Integer lessonId, Calendar start, Calendar end)
            throws ExcepcaoPersistencia {
        double duration = 0;
        final List<Lesson> associatedLessons = shift.getAssociatedLessons();
        for (final Lesson lesson : associatedLessons) {
            if (!lesson.getIdInternal().equals(lessonId)) {
                duration += (getLessonDurationInMinutes(lesson.getBegin(), lesson.getEnd()).doubleValue() / 60);
            }
        }
        return duration;
    }

    private Integer getLessonDurationInMinutes(Date start, Date end) {
        int beginHour = start.getHours();
        int beginMinutes = start.getMinutes();
        int endHour = end.getHours();
        int endMinutes = end.getMinutes();
        int duration = (endHour - beginHour) * 60 + (endMinutes - beginMinutes);
        return Integer.valueOf(duration);
    }

    /**
     * To change the template for this generated type comment go to
     * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
     */
    public class InvalidLoadException extends FenixServiceException {
        /**
         * 
         */
        private InvalidLoadException() {
            super();
        }

        /**
         * @param s
         */
        InvalidLoadException(String s) {
            super(s);
        }
    }
}
