package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class WrittenEvaluationRoomDistribution implements IService {

    public void run(Integer executionCourseID, Integer evaluationID, List<Integer> roomIDs,
            Boolean sendSMS, Boolean distributeOnlyEnroledStudents) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) persistentSupport
                .getIPersistentObject().readByOID(Exam.class, evaluationID);
        if (writtenEvaluation == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }
        List<Student> studentsToDistribute;
        if (distributeOnlyEnroledStudents) {
            studentsToDistribute = readEnroledStudentsInWrittenEvaluation(writtenEvaluation);
        } else {
            studentsToDistribute = readAllStudentsAttendingExecutionCourses(writtenEvaluation);
        }
        final List<Room> selectedRooms = readRooms(writtenEvaluation, roomIDs);
        if (!selectedRooms.containsAll(writtenEvaluation.getAssociatedRooms())) {
            // if the selected rooms are different of the evaluation rooms
            // then the user probably selected repeated rooms
            throw new FenixServiceException("error.repeated.rooms");
        }
        writtenEvaluation.distributeStudentsByRooms(studentsToDistribute, selectedRooms);
        if (sendSMS) {
            sendSMSToStudents(writtenEvaluation);
        }
    }
    
    private List<Room> readRooms(final WrittenEvaluation writtenEvaluation,
            final List<Integer> roomIDs) throws ExcepcaoPersistencia {
        
        List<Integer> selectedRoomIDs = removeDuplicatedEntries(roomIDs);
        final List<Room> writtenEvaluationRooms = writtenEvaluation.getAssociatedRooms();        
        final List<Room> selectedRooms = new ArrayList<Room>(selectedRoomIDs.size());
        
        for (final Integer roomID : selectedRoomIDs) {
            for (final Room room : writtenEvaluationRooms) {
                if (room.getIdInternal().equals(roomID)) {
                    selectedRooms.add(room);
                    break;
                }
            }
        }
        return selectedRooms;
    }

    private List<Integer> removeDuplicatedEntries(List<Integer> roomIDs) {
        List<Integer> result = new ArrayList<Integer>();
        for (final Integer roomID : roomIDs) {
            if (!result.contains(roomID)) {
                result.add(roomID);
            }
        }
        return result;
    }

    private List<Student> readEnroledStudentsInWrittenEvaluation(WrittenEvaluation writtenEvaluation) {
        final List<Student> result = new ArrayList<Student>(writtenEvaluation
                .getWrittenEvaluationEnrolmentsCount());
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : writtenEvaluation
                .getWrittenEvaluationEnrolments()) {
            result.add(writtenEvaluationEnrolment.getStudent());
        }
        return result;
    }

    private List<Student> readAllStudentsAttendingExecutionCourses(WrittenEvaluation writtenEvaluation) {
        final List<Student> result = new ArrayList<Student>();
        for (final ExecutionCourse executionCourse : writtenEvaluation.getAssociatedExecutionCourses()) {
            for (final Attends attend : executionCourse.getAttends()) {
                if (!result.contains(attend.getAluno())) {
                    result.add(attend.getAluno());
                }
            }
        }
        return result;
    }

    private void sendSMSToStudents(WrittenEvaluation writtenEvaluation) {
        // TODO: Send SMS method: fill this method when we have sms
    }
}