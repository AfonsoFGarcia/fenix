/*
 * ReadExamsByDayAndBeginning.java
 *
 * Created on 2003/03/19
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;

import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadRoomsWithNoExamsInDayAndBeginning extends Service {

    public List run(Calendar day, Calendar beginning) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List exams = persistentSupport.getIPersistentExam().readBy(day, beginning);
        List allRooms = persistentSupport.getISalaPersistente().readAll();

        List occupiedRooms = new ArrayList();
        for (int i = 0; i < exams.size(); i++) {
            List examRooms = ((Exam) exams.get(i)).getAssociatedRooms();
            if (examRooms != null && examRooms.size() > 0) {
                for (int r = 0; r < examRooms.size(); r++) {
                    occupiedRooms.add(examRooms.get(r));
                }
            }
        }

        List availableInfoRooms = new ArrayList();
        List availableRooms = (ArrayList) CollectionUtils.subtract(allRooms, occupiedRooms);
        for (int i = 0; i < availableRooms.size(); i++) {
            Room room = (Room) availableRooms.get(i);
            InfoRoom infoRoom = InfoRoom.newInfoFromDomain(room);
            availableInfoRooms.add(infoRoom);
        }

        return availableInfoRooms;
    }
}