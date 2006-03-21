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

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;

public class ReadRoomsWithNoExamsInDayAndBeginning extends Service {

    public List run(Calendar day, Calendar beginning) throws ExcepcaoPersistencia {
        List exams = persistentSupport.getIPersistentExam().readBy(day, beginning);
        List allRooms = RootDomainObject.getInstance().getOldRooms();

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
            OldRoom room = (OldRoom) availableRooms.get(i);
            InfoRoom infoRoom = InfoRoom.newInfoFromDomain(room);
            availableInfoRooms.add(infoRoom);
        }

        return availableInfoRooms;
    }
}