/**
* Aug 7, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadClassTimeTableByStudent extends Service {
    
    public List run(final String username, final Integer classID, final Integer executionCourseID) throws ExcepcaoPersistencia {
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
        
        Student student = persistentStudent.readByUsername(username);
        SchoolClass schoolClass = (SchoolClass) persistentObject.readByOID(SchoolClass.class, classID);
        
        List studentAttends = student.getAssociatedAttends();
        
        List classShifts = new ArrayList();
        for (Iterator iter = schoolClass.getAssociatedShifts().iterator(); iter.hasNext();) {
            final Shift shift = (Shift) iter.next();
            if(CollectionUtils.exists(studentAttends,new Predicate(){
                public boolean evaluate(Object arg0) {
                    Attends attends = (Attends) arg0;
                    boolean result = shift.getDisciplinaExecucao().equals(attends.getDisciplinaExecucao());
                    if(executionCourseID != null){
                        result = result & shift.getDisciplinaExecucao().getIdInternal().equals(executionCourseID);
                    }
                    return result;
                }})){
                classShifts.add(shift);
            }
        }

        List lessons = new ArrayList();
        Iterator shiftIter = classShifts.iterator();
        while (shiftIter.hasNext()) {
            Shift shift = (Shift) shiftIter.next();
            lessons.addAll(shift.getAssociatedLessons());
        }

        Iterator iter = lessons.iterator();
        List infoLessons = new ArrayList();
        while (iter.hasNext()) {
            Lesson lesson = (Lesson) iter.next();
            InfoLesson infolesson = copyILesson2InfoLesson(lesson);
            Shift shift = lesson.getShift();
            InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(shift
                    .getDisciplinaExecucao());
            infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
            infolesson.setInfoShift(infoShift);
            infoLessons.add(infolesson);
        }

        return infoLessons;
    }

    private InfoLesson copyILesson2InfoLesson(Lesson lesson) {
        InfoLesson infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLesson();
            infoLesson.setIdInternal(lesson.getIdInternal());
            infoLesson.setDiaSemana(lesson.getDiaSemana());
            infoLesson.setFim(lesson.getFim());
            infoLesson.setInicio(lesson.getInicio());
            infoLesson.setTipo(lesson.getTipo());
            infoLesson.setInfoSala(copyISala2InfoRoom(lesson.getSala()));

            RoomOccupation roomOccupation = lesson.getRoomOccupation();
            InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(roomOccupation);
            infoLesson.setInfoRoomOccupation(infoRoomOccupation);

            Room room = roomOccupation.getRoom();
            InfoRoom infoRoom = InfoRoom.newInfoFromDomain(room);
            infoRoomOccupation.setInfoRoom(infoRoom);

            OccupationPeriod period = roomOccupation.getPeriod();
            InfoPeriod infoPeriod = InfoPeriod.newInfoFromDomain(period);
            infoRoomOccupation.setInfoPeriod(infoPeriod);
        }
        return infoLesson;
    }

    /**
     * @param sala
     * @return
     */
    private InfoRoom copyISala2InfoRoom(Room sala) {
        InfoRoom infoRoom = null;
        if (sala != null) {
            infoRoom = new InfoRoom();
            infoRoom.setIdInternal(sala.getIdInternal());
            infoRoom.setNome(sala.getNome());
        }
        return infoRoom;
    }

}


