/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteLessons implements IService {

    public Boolean run(final List lessonOIDs) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        for (int j = 0; j < lessonOIDs.size(); j++) {
            final Integer lessonOID = (Integer) lessonOIDs.get(j);
            deleteLesson(persistentSupport, lessonOID);
        }

        return new Boolean(true);
    }

    public static void deleteLesson(final ISuportePersistente persistentSupport, final Integer lessonOID)
            throws ExcepcaoPersistencia {
        final IAulaPersistente persistenetLesson = persistentSupport.getIAulaPersistente();
        final ILesson lesson = (ILesson) persistenetLesson.readByOID(Lesson.class, lessonOID);
        deleteLesson(persistentSupport, lesson);
    }

    public static void deleteLesson(final ISuportePersistente persistentSupport, final ILesson lesson)
            throws ExcepcaoPersistencia {
        final IAulaPersistente persistenetLesson = persistentSupport.getIAulaPersistente();
        final IPersistentRoomOccupation persistentRoomOccupation = persistentSupport
                .getIPersistentRoomOccupation();

        final IRoomOccupation roomOccupation = lesson.getRoomOccupation();

        roomOccupation.setPeriod(null);
        roomOccupation.setRoom(null);

        lesson.setShift(null);
        lesson.setRoomOccupation(null);
        lesson.setSala(null);
        lesson.setExecutionPeriod(null);

        persistentRoomOccupation.deleteByOID(RoomOccupation.class, roomOccupation.getIdInternal());
        persistenetLesson.deleteByOID(Lesson.class, lesson.getIdInternal());
    }

}