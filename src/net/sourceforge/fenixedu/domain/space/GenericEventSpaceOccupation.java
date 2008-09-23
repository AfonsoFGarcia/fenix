package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class GenericEventSpaceOccupation extends GenericEventSpaceOccupation_Base {

    @Checked("SpacePredicates.checkPermissionsToManageGenericEventSpaceOccupations")
    public GenericEventSpaceOccupation(AllocatableSpace allocatableSpace, GenericEvent genericEvent) {

	super();

	setGenericEvent(genericEvent);

	if (allocatableSpace != null && !allocatableSpace.isFree(this)) {
	    throw new DomainException("error.roomOccupation.room.is.not.free");
	}

	setResource(allocatableSpace);
    }

    @Checked("SpacePredicates.checkPermissionsToManageGenericEventSpaceOccupations")
    public void delete() {
	super.setGenericEvent(null);
	super.delete();
    }

    @Checked("SpacePredicates.checkPermissionsToManageGenericEventSpaceOccupations")
    public void verifyIfIsPossibleCloseGenericEvent() {
    }

    @Override
    public void setGenericEvent(GenericEvent genericEvent) {
	if (genericEvent == null) {
	    throw new DomainException("error.GenericEventSpaceOccupation.empty.genericEvent");
	}
	super.setGenericEvent(genericEvent);
    }

    @Override
    public boolean isGenericEventSpaceOccupation() {
	return true;
    }

    @Override
    public Group getAccessGroup() {
	return getSpace().getGenericEventOccupationsAccessGroupWithChainOfResponsibility();
    }

    @Override
    public FrequencyType getFrequency() {
	return getGenericEvent().getFrequency();
    }

    @Override
    public YearMonthDay getBeginDate() {
	return getGenericEvent().getBeginDate();
    }

    @Override
    public YearMonthDay getEndDate() {
	return getGenericEvent().getEndDate();
    }

    @Override
    public Boolean getDailyFrequencyMarkSaturday() {
	return getGenericEvent().getDailyFrequencyMarkSaturday();
    }

    @Override
    public Boolean getDailyFrequencyMarkSunday() {
	return getGenericEvent().getDailyFrequencyMarkSunday();
    }

    @Override
    public HourMinuteSecond getStartTimeDateHourMinuteSecond() {
	return getGenericEvent().getStartTimeDateHourMinuteSecond();
    }

    @Override
    public HourMinuteSecond getEndTimeDateHourMinuteSecond() {
	return getGenericEvent().getEndTimeDateHourMinuteSecond();
    }

    @Override
    public DiaSemana getDayOfWeek() {
	return null;
    }

    @Override
    public boolean isOccupiedByExecutionCourse(ExecutionCourse executionCourse, DateTime start, DateTime end) {
	return false;
    }
}
