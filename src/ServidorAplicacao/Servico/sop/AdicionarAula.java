/*
 * AdicionarAula.java
 *
 * Created on 26 de Outubro de 2002, 19:26
 */

package ServidorAplicacao.Servico.sop;

/**
 * Service AdicionarAula.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoShift;
import DataBeans.InfoShiftServiceResult;
import DataBeans.util.Cloner;
import Dominio.Lesson;
import Dominio.ILesson;
import Dominio.IExecutionCourse;
import Dominio.IShift;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

public class AdicionarAula implements IService {

    /**
     * The actor of this class.
     */
    public AdicionarAula() {
    }

    public List run(InfoShift infoShift, String[] classesList) throws FenixServiceException {

        //ITurnoAula turnoAula = null;
        InfoShiftServiceResult result = null;
        List serviceResult = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionCourse executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoShift
                    .getInfoDisciplinaExecucao());

            IShift turno1 = sp.getITurnoPersistente().readByNameAndExecutionCourse(infoShift.getNome(),
                    executionCourse);

            int i = 0;
            while (i < classesList.length) {
                Integer lessonId = new Integer(classesList[i]);
                ILesson lesson = (ILesson) sp.getIAulaPersistente().readByOID(Lesson.class, lessonId);
                if (lesson != null) {
                    //turnoAula = new TurnoAula(turno1, lesson);
                    result = valid(turno1, lesson);
                    serviceResult.add(result);
                    if (result.isSUCESS()) {
                        /*
                         * try { sp.getITurnoAulaPersistente().simpleLockWrite(
                         * turnoAula); } catch (ExistingPersistentException ex) {
                         * throw new ExistingServiceException(ex); }
                         */
                        try {
                            sp.getIAulaPersistente().simpleLockWrite(lesson);
                            lesson.setShift(turno1);
                        } catch (ExcepcaoPersistencia ex) {

                        }
                    }
                }
                i++;
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex.getMessage());
        }

        return serviceResult;
    }

    private InfoShiftServiceResult valid(IShift shift, ILesson lesson) throws ExcepcaoPersistencia {
        InfoShiftServiceResult result = new InfoShiftServiceResult();
        result.setMessageType(InfoShiftServiceResult.SUCESS);

        double hours = getTotalHoursOfShiftType(shift);
        double lessonDuration = (getLessonDurationInMinutes(lesson).doubleValue()) / 60;

        if (shift.getTipo().equals(new TipoAula(TipoAula.TEORICA))) {
            if (hours == shift.getDisciplinaExecucao().getTheoreticalHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getTheoreticalHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_EXCEEDED);
        } else if (shift.getTipo().equals(new TipoAula(TipoAula.PRATICA))) {
            if (hours == shift.getDisciplinaExecucao().getPraticalHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getPraticalHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_EXCEEDED);
        } else if (shift.getTipo().equals(new TipoAula(TipoAula.TEORICO_PRATICA))) {
            if (hours == shift.getDisciplinaExecucao().getTheoPratHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getTheoPratHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_EXCEEDED);
        } else if (shift.getTipo().equals(new TipoAula(TipoAula.LABORATORIAL))) {
            if (hours == shift.getDisciplinaExecucao().getLabHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.LAB_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getLabHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.LAB_HOURS_LIMIT_EXCEEDED);
        }

        return result;
    }

    private double getTotalHoursOfShiftType(IShift shift) throws ExcepcaoPersistencia {
        /*
         * IShift shiftCriteria = new Shift();
         * shiftCriteria.setNome(shift.getNome());
         * shiftCriteria.setDisciplinaExecucao(shift.getDisciplinaExecucao());
         * 
         * List lessonsOfShiftType = SuportePersistenteOJB.getInstance()
         * .getITurnoAulaPersistente().readLessonsByShift(shiftCriteria);
         * 
         * ILesson lesson = null; double duration = 0; for (int i = 0; i <
         * lessonsOfShiftType.size(); i++) { lesson = ((ITurnoAula)
         * lessonsOfShiftType.get(i)).getAula(); duration +=
         * (getLessonDurationInMinutes(lesson).doubleValue() / 60); } return
         * duration;
         */

        ILesson lesson = null;
        double duration = 0;
        List associatedLessons = shift.getAssociatedLessons();

        for (int i = 0; i < associatedLessons.size(); i++) {
            lesson = (ILesson) associatedLessons.get(i);
            duration += (getLessonDurationInMinutes(lesson).doubleValue() / 60);
        }

        return duration;
    }

    private Integer getLessonDurationInMinutes(ILesson lesson) {
        int beginHour = lesson.getInicio().get(Calendar.HOUR_OF_DAY);
        int beginMinutes = lesson.getInicio().get(Calendar.MINUTE);
        int endHour = lesson.getFim().get(Calendar.HOUR_OF_DAY);
        int endMinutes = lesson.getFim().get(Calendar.MINUTE);
        int duration = 0;

        duration = (endHour - beginHour) * 60 + (endMinutes - beginMinutes);
        return new Integer(duration);
    }

}