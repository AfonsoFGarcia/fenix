/*
 * ReadShiftEnrolment.java
 *
 * Created on December 20th, 2002, 03:39
 */

package ServidorAplicacao.Servico.student;

/**
 * Service ReadShiftSignup
 * 
 * @author tfc130
 */
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.InfoRoomOccupation;
import DataBeans.InfoShift;
import Dominio.ILesson;
import Dominio.IRoomOccupation;
import Dominio.IRoom;
import Dominio.IShift;
import Dominio.Shift;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadShiftLessons implements IService {

    public Object run(final InfoShift infoShift) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        final ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();

        final IShift shift = (IShift) persistentShift.readByOID(Shift.class, infoShift.getIdInternal());
        return CollectionUtils.collect(shift.getAssociatedLessons(), new Transformer() {

            public Object transform(Object arg0) {
                final ILesson lesson = (ILesson) arg0;
                final IRoomOccupation roomOccupation = lesson.getRoomOccupation();
                final IRoom room = roomOccupation.getRoom();

                final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
                infoLesson.setInfoShift(infoShift);
                final InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(roomOccupation);
                infoLesson.setInfoRoomOccupation(infoRoomOccupation);
                final InfoRoom infoRoom = InfoRoom.newInfoFromDomain(room);
                infoRoomOccupation.setInfoRoom(infoRoom);

                return infoLesson;
            }
            
        });
    }

}