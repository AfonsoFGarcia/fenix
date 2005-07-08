/*
 * ReadExamsMapByRoom.java
 * 
 * Created on 2004/02/19
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

/**
 * @author Ana e Ricardo
 *  
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.Period;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExamsMapByRooms implements IService {

    public List run(InfoExecutionPeriod infoExecutionPeriod, List infoRooms) throws Exception {

        // Object to be returned
        List infoRoomExamMapList = new ArrayList();

        
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            //List rooms = sp.getISalaPersistente().readForRoomReservation();
            InfoRoom room = null;
            InfoRoomExamsMap infoExamsMap = null;
            String executionPeriodName = infoExecutionPeriod.getName();
            String year = infoExecutionPeriod.getInfoExecutionYear().getYear();

            

            IPeriod period = calculateExamsSeason(year, infoExecutionPeriod.getSemester().intValue());
            Calendar startSeason1 = period.getStartDate();
            Calendar endSeason2 = period.getEndDate();
            // The calendar must start at a monday
            if (startSeason1.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                int shiftDays = Calendar.MONDAY - startSeason1.get(Calendar.DAY_OF_WEEK);
                startSeason1.add(Calendar.DATE, shiftDays);
            }

            for (int i = 0; i < infoRooms.size(); i++) {
                room = (InfoRoom) infoRooms.get(i);
                infoExamsMap = new InfoRoomExamsMap();
                // Set Exam Season info
                infoExamsMap.setInfoRoom(room);
                infoExamsMap.setStartSeason1(startSeason1);
                infoExamsMap.setEndSeason1(null);
                infoExamsMap.setStartSeason2(null);
                infoExamsMap.setEndSeason2(endSeason2);
                
                String nameRoom = room.getNome();
                
                
                List exams = sp.getIPersistentExam().readByRoomAndExecutionPeriod(nameRoom, executionPeriodName, year);
                infoExamsMap.setExams((List) CollectionUtils.collect(exams, TRANSFORM_EXAM_TO_INFOEXAM));
                infoRoomExamMapList.add(infoExamsMap);
            }

        

        return infoRoomExamMapList;
    }

    private Transformer TRANSFORM_EXAM_TO_INFOEXAM = new Transformer() {
        public Object transform(Object exam) {
            InfoExam infoExam = Cloner.copyIExam2InfoExam((IExam) exam);
            infoExam.setInfoExecutionCourse((InfoExecutionCourse) Cloner
                    .get(((IExam) exam).getAssociatedExecutionCourses().get(0)));
            return infoExam;
        }
    };

    private Period calculateExamsSeason(String year, int semester) throws Exception {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            List executionDegreesList = sp.getIPersistentExecutionDegree().readByExecutionYear(year);
            IExecutionDegree executionDegree = (IExecutionDegree) executionDegreesList.get(0);
            
            Calendar startSeason1 = null;
            Calendar endSeason2 = null;
            if (semester == 1) {
                startSeason1 = executionDegree.getPeriodExamsFirstSemester().getStartDate();
                endSeason2 = executionDegree.getPeriodExamsFirstSemester().getEndDateOfComposite();
            } else {
                startSeason1 = executionDegree.getPeriodExamsSecondSemester().getStartDate();
                endSeason2 = executionDegree.getPeriodExamsSecondSemester().getEndDateOfComposite();
            }

            for (int i = 1; i < executionDegreesList.size(); i++) {
                executionDegree = (IExecutionDegree) executionDegreesList.get(i);
                Calendar startExams;
                Calendar endExams;
                if (semester == 1) {
                    startExams = executionDegree.getPeriodExamsFirstSemester().getStartDate();
                    endExams = executionDegree.getPeriodExamsFirstSemester().getEndDateOfComposite();
                } else {
                    startExams = executionDegree.getPeriodExamsSecondSemester().getStartDate();
                    endExams = executionDegree.getPeriodExamsSecondSemester().getEndDateOfComposite();
                }
                if (startExams.before(startSeason1)) {
                    startSeason1 = startExams;
                }
                if (endExams.after(endSeason2)) {
                    endSeason2 = endExams;
                }

            }
            return new Period(startSeason1, endSeason2);
        } catch (Exception e) {
            throw new FenixServiceException("Error calculating exams season", e);
        }
    }
}