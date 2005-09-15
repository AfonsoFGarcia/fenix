/*
 * Created on 5/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.sourceforge.fenixedu.applicationTier.Factory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRoomTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.CalendarUtil;

/**
 * @author Jo�o Mota
 * 
 *  
 */
public class RoomSiteComponentBuilder {

    private static RoomSiteComponentBuilder instance = null;

    public RoomSiteComponentBuilder() {
    }

    public static RoomSiteComponentBuilder getInstance() {
        if (instance == null) {
            instance = new RoomSiteComponentBuilder();
        }
        return instance;
    }

    public ISiteComponent getComponent(ISiteComponent component, Calendar day, IRoom room)  {

        if (component instanceof InfoSiteRoomTimeTable) {
            return getInfoSiteRoomTimeTable((InfoSiteRoomTimeTable) component, day, room);
        }

        return null;

    }

    // TODO (rspl): alterar as aulas a ler e o dia da semana
    // FIXME duplicated code: this method is (almost?) identical to ReadLessonsAndExamsInWeekAndRoom.run
    private ISiteComponent getInfoSiteRoomTimeTable(InfoSiteRoomTimeTable component, Calendar day,
            IRoom room) {

        List<InfoObject> infoShowOccupations = new ArrayList<InfoObject>();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IAulaPersistente lessonDAO = sp.getIAulaPersistente();
            IPersistentExam examDAO = sp.getIPersistentExam();

            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();

            Calendar startDay = Calendar.getInstance();
            startDay.setTimeInMillis(day.getTimeInMillis());
            startDay.add(Calendar.DATE, Calendar.MONDAY - day.get(Calendar.DAY_OF_WEEK));
            Calendar endDay = Calendar.getInstance();
            endDay.setTimeInMillis(startDay.getTimeInMillis());
            endDay.add(Calendar.DATE, 6);

            InfoPeriod lessonsInfoPeriod = calculateLessonsSeason(executionPeriod);
            if (this.intersectPeriods(day, endDay,lessonsInfoPeriod)) {
                //adicionar as aulas
                List lessonList = lessonDAO.readByRoomAndExecutionPeriod(room.getIdInternal(), executionPeriod.getIdInternal());
                Iterator iterator = lessonList.iterator();

                while (iterator.hasNext()) {
                    ILesson aula = (ILesson) iterator.next();
                    IRoomOccupation roomOccupation = aula.getRoomOccupation();
                    
                    IPeriod period = roomOccupation.getPeriod();
                    InfoPeriod infoPeriod = InfoPeriod.newInfoFromDomain(period);
                    
                    if (this.intersectPeriods(day, endDay, infoPeriod)) {

                        boolean add = true;
                        if ((roomOccupation.getFrequency().intValue() == RoomOccupation.QUINZENAL)
                                && (!RoomOccupation.periodQuinzenalContainsWeekPeriod(period.getStartDate(), period.getEndDate(),
                                        roomOccupation.getWeekOfQuinzenalStart().intValue(),
                                        roomOccupation.getDayOfWeek(), day, endDay, null))) {
                            add = false;
                        }
                        if (roomOccupation.getFrequency().intValue() == RoomOccupation.SEMANAL) {
                            Calendar dayOfLesson = Calendar.getInstance();
                            dayOfLesson.setTimeInMillis(day.getTimeInMillis());
                            dayOfLesson.add(Calendar.DATE, roomOccupation.getDayOfWeek().getDiaSemana()
                                    .intValue()
                                    - Calendar.MONDAY);
                            if (!this.intersectPeriods(dayOfLesson, dayOfLesson, infoPeriod)) {
                                add = false;
                            }
                        }
                        if (add) {
                            InfoLesson infoLesson = InfoLesson.newInfoFromDomain(aula);
                            IShift shift = aula.getShift();
                            if (shift == null) {
                                continue;
                            }
							InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                            infoLesson.setInfoShift(infoShift);

                            InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(aula.getRoomOccupation());
                            InfoRoom infoRoom = InfoRoom.newInfoFromDomain(aula
                                    .getRoomOccupation().getRoom());
                            infoRoomOccupation.setInfoRoom(infoRoom);
                            infoLesson.setInfoRoomOccupation(infoRoomOccupation);

                            IExecutionCourse executionCourse = shift.getDisciplinaExecucao();
                            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
                            infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

                            InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionPeriod);
                            infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

                            infoShowOccupations.add(infoLesson);
                        }
                    }
                }
            }

            //adicionar os exames
            List examList = examDAO.readByRoomAndWeek(room.getNome(), day);
            Iterator iteratorExams = examList.iterator();

            while (iteratorExams.hasNext()) {
                IExam elem = (IExam) iteratorExams.next();
                InfoExam infoExam = Cloner.copyIExam2InfoExam(elem);

                infoShowOccupations.add(infoExam);
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        component.setInfoShowOccupation(infoShowOccupations);
        component.setInfoRoom(Cloner.copyRoom2InfoRoom(room));

        return component;
    }
    
    private boolean intersectPeriods(Calendar startDate, Calendar endDate, InfoPeriod secondPeriod) {
        while (secondPeriod != null) {
            if (CalendarUtil.intersectDates(startDate, endDate, secondPeriod
                    .getStartDate(), secondPeriod.getEndDate())) {
                return true;
            }
            secondPeriod = secondPeriod.getNextPeriod();
        }
        return false;
    }

    private InfoPeriod calculateLessonsSeason(IExecutionPeriod executionPeriod) throws Exception {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            int semester = executionPeriod.getSemester().intValue();

            List executionDegreesList = sp.getIPersistentExecutionDegree().readByExecutionYear(
                    executionPeriod.getExecutionYear().getYear());
            IExecutionDegree executionDegree = (IExecutionDegree) executionDegreesList.get(0);

            Calendar startSeason1 = null;
            Calendar endSeason2 = null;
            if (semester == 1) {
                startSeason1 = executionDegree.getPeriodLessonsFirstSemester().getStartDate();
                endSeason2 = executionDegree.getPeriodLessonsFirstSemester().getEndDateOfComposite();
            } else {
                startSeason1 = executionDegree.getPeriodLessonsSecondSemester().getStartDate();
                endSeason2 = executionDegree.getPeriodLessonsSecondSemester().getEndDateOfComposite();
            }
            for (int i = 1; i < executionDegreesList.size(); i++) {
                executionDegree = (IExecutionDegree) executionDegreesList.get(i);
                Calendar startLessons;
                Calendar endLessons;
                if (semester == 1) {
                    startLessons = executionDegree.getPeriodLessonsFirstSemester().getStartDate();
                    endLessons = executionDegree.getPeriodLessonsFirstSemester().getEndDateOfComposite();
                } else {
                    startLessons = executionDegree.getPeriodLessonsSecondSemester().getStartDate();
                    endLessons = executionDegree.getPeriodLessonsSecondSemester()
                            .getEndDateOfComposite();
                }
                if (startLessons.before(startSeason1)) {
                    startSeason1 = startLessons;
                }
                if (endLessons.after(endSeason2)) {
                    endSeason2 = endLessons;
                }

            }
            return new InfoPeriod(startSeason1, endSeason2);
        } catch (Exception e) {
            throw new FenixServiceException("Error calculating exams season");
        }
    }
}