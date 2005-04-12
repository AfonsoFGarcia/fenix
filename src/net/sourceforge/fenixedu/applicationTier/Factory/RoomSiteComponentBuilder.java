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
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRoomTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.Period;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

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

    /* TODO (rspl): alterar as aulas a ler e o dia da semana */
    private ISiteComponent getInfoSiteRoomTimeTable(InfoSiteRoomTimeTable component, Calendar day,
            IRoom room) {

        List infoShowOccupations = new ArrayList();
        /*
         * try { ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
         * 
         * //adicionar as aulas IAulaPersistente lessonDAO =
         * sp.getIAulaPersistente(); IPersistentExam examDAO =
         * sp.getIPersistentExam(); List lessonList =
         * lessonDAO.readByRoomAndExecutionPeriod(room, executionPeriod);
         * 
         * Iterator iterator = lessonList.iterator();
         * 
         * while (iterator.hasNext()) { ILesson elem = (ILesson) iterator.next();
         * InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem); IShift
         * shift = elem.getShift(); if(shift == null) { continue; } InfoShift
         * infoShift = Cloner.copyShift2InfoShift(shift);
         * infoLesson.setInfoShift(infoShift);
         * 
         * infoShowOccupations.add(infoLesson); }
         * 
         * //adicionar os exames Calendar week = Calendar.getInstance();
         * 
         * List examList = examDAO.readByRoomAndWeek(room, week); iterator =
         * examList.iterator();
         * 
         * while (iterator.hasNext()) { IExam elem = (IExam) iterator.next();
         * InfoExam infoExam = Cloner.copyIExam2InfoExam(elem);
         * 
         * infoShowOccupations.add(infoExam); } } catch (ExcepcaoPersistencia
         * ex) { ex.printStackTrace(); }
         */
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
            Period weekPeriod = new Period(day, endDay);

            Period lessonsPeriod = calculateLessonsSeason(executionPeriod);
            if (lessonsPeriod.intersectPeriods(weekPeriod)) {
                //adicionar as aulas
                List lessonList = lessonDAO.readByRoomAndExecutionPeriod(room, executionPeriod);
                Iterator iterator = lessonList.iterator();

                while (iterator.hasNext()) {
                    ILesson aula = (ILesson) iterator.next();
                    IRoomOccupation roomOccupation = aula.getRoomOccupation();
                    IPeriod period = roomOccupation.getPeriod();
                    if (period.intersectPeriods(weekPeriod)) {

                        boolean add = true;
                        if ((roomOccupation.getFrequency().intValue() == RoomOccupation.QUINZENAL)
                                && (!RoomOccupation.periodQuinzenalContainsWeekPeriod(period,
                                        roomOccupation.getWeekOfQuinzenalStart().intValue(),
                                        roomOccupation.getDayOfWeek(), weekPeriod))) {
                            add = false;
                        }
                        if (roomOccupation.getFrequency().intValue() == RoomOccupation.SEMANAL) {
                            Calendar dayOfLesson = Calendar.getInstance();
                            dayOfLesson.setTimeInMillis(day.getTimeInMillis());
                            dayOfLesson.add(Calendar.DATE, roomOccupation.getDayOfWeek().getDiaSemana()
                                    .intValue()
                                    - Calendar.MONDAY);
                            if (!period.intersectPeriods(new Period(dayOfLesson, dayOfLesson))) {
                                add = false;
                            }
                        }
                        if (add) {
                            InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(aula);
                            IShift shift = aula.getShift();
                            if (shift == null) {
                                continue;
                            }
							InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                            infoLesson.setInfoShift(infoShift);

                            infoShowOccupations.add(infoLesson);
                        }
                    }
                }
            }

            //adicionar os exames
            List examList = examDAO.readByRoomAndWeek(room, day);
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

    private Period calculateLessonsSeason(IExecutionPeriod executionPeriod) throws Exception {
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
            return new Period(startSeason1, endSeason2);
        } catch (Exception e) {
            throw new FenixServiceException("Error calculating exams season");
        }
    }
}