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
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.space.IRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadRoomsWithNoExamsInDayAndBeginning implements IService {

    public List run(Calendar day, Calendar beginning) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List exams = sp.getIPersistentExam().readBy(day, beginning);
        List allRooms = sp.getISalaPersistente().readAll();

        List occupiedRooms = new ArrayList();
        for (int i = 0; i < exams.size(); i++) {
            List examRooms = ((IExam) exams.get(i)).getAssociatedRooms();
            if (examRooms != null && examRooms.size() > 0) {
                for (int r = 0; r < examRooms.size(); r++) {
                    occupiedRooms.add(examRooms.get(r));
                }
            }
        }

        List availableInfoRooms = new ArrayList();
        List availableRooms = (ArrayList) CollectionUtils.subtract(allRooms, occupiedRooms);
        for (int i = 0; i < availableRooms.size(); i++) {
            IRoom room = (IRoom) availableRooms.get(i);
            InfoRoom infoRoom = InfoRoom.newInfoFromDomain(room);
            availableInfoRooms.add(infoRoom);
        }

        return availableInfoRooms;
    }
}