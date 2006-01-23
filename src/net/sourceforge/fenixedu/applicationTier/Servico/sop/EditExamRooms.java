package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class EditExamRooms extends Service {

    public InfoExam run(InfoExam infoExam, final List<Integer> roomsForExam) throws ExcepcaoPersistencia,
            FenixServiceException {

        final List<Room> finalRoomList = new ArrayList<Room>();
        for (final Integer id : roomsForExam) {
        	finalRoomList.add((Room) persistentObject.readByOID(Room.class, (Integer) id));
        }

        final Exam exam = (Exam) persistentObject.readByOID(Exam.class, infoExam.getIdInternal());
        if (exam == null) {
            throw new NonExistingServiceException();
        }
 
        // Remove all elements
        // TODO : Do this more intelegently.
        exam.getAssociatedRooms().clear();

        // Add all elements
        exam.getAssociatedRooms().addAll(finalRoomList);

        return InfoExam.newInfoFromDomain(exam);
    }

}