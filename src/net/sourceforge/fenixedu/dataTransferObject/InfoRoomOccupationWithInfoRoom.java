/*
 * Created on 8/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IRoomOccupation;

/**
 * @author T�nia Pous�o
 *  
 */
public class InfoRoomOccupationWithInfoRoom extends InfoRoomOccupation {

    @Override
    public void copyFromDomain(IRoomOccupation roomOccupation) {
        super.copyFromDomain(roomOccupation);
        if (roomOccupation != null) {
            setInfoRoom(InfoRoom.newInfoFromDomain(roomOccupation.getRoom()));
        }
    }

    public static InfoRoomOccupation newInfoFromDomain(IRoomOccupation roomOccupation) {
        InfoRoomOccupationWithInfoRoom infoRoomOccupation = null;
        if (roomOccupation != null) {
            infoRoomOccupation = new InfoRoomOccupationWithInfoRoom();
            infoRoomOccupation.copyFromDomain(roomOccupation);
        }
        return infoRoomOccupation;
    }
}