package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;
import dml.runtime.RelationAdapter;

public class MeasurementTestRoom extends MeasurementTestRoom_Base {

    static {
        MeasurementTestShiftMeasurementTestRoom.addListener(new RelationAdapter<MeasurementTestRoom, MeasurementTestShift>() {
            @Override
            public void beforeAdd(MeasurementTestRoom toAdd, MeasurementTestShift shift) {

                if (toAdd != null && shift != null) {
                    if (shift.getRoomByName(toAdd.getName()) != null) {
                        throw new DomainException(
                                "error.net.sourceforge.fenixedu.domain.candidacy.MeasurementTestShift.already.contains.room.with.same.name");
                    }

                }
            }

        });
    }

    public static Comparator<MeasurementTestRoom> COMPARATOR_BY_ROOM_ORDER = new Comparator<MeasurementTestRoom>() {
        @Override
        public int compare(MeasurementTestRoom leftMeasurementTestRoom, MeasurementTestRoom rightMeasurementTestRoom) {
            return leftMeasurementTestRoom.getRoomOrder().compareTo(rightMeasurementTestRoom.getRoomOrder());
        }
    };

    protected MeasurementTestRoom() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public MeasurementTestRoom(String name, Integer capacity, MeasurementTestShift shift) {

        this();

        check(shift, "error.net.sourceforge.fenixedu.domain.candidacy.MeasurementTestRoom.shift.cannot.be.null");
        check(name, "error.net.sourceforge.fenixedu.domain.candidacy.MeasurementTestRoom.name.cannot.be.null");
        check(capacity, "error.net.sourceforge.fenixedu.domain.candidacy.MeasurementTestRoom.capacity.cannot.be.null");

        setRoomOrder(shift.getRoomsCount() + 1);
        setShift(shift);
        setName(name);
        setCapacity(capacity);

    }

    public boolean isAvailable() {
        return getCapacity().intValue() - getRegistrationsCount() > 0;
    }

    public Campus getCampus() {
        return getShift().getTest().getCampus();
    }

}
