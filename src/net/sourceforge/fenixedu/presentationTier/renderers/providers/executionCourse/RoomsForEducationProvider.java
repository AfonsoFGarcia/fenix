package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class RoomsForEducationProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        Set<AllocatableSpace> rooms = new TreeSet<AllocatableSpace>(AllocatableSpace.ROOM_COMPARATOR_BY_NAME);
        rooms.addAll(AllocatableSpace.getAllActiveAllocatableSpacesForEducation());
        return rooms;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
