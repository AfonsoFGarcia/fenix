package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.applicationTier.FactoryExecutor;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.space.Floor.FloorFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class Floor extends Floor_Base {

	public static Comparator<Floor> FLOOR_COMPARATOR_BY_LEVEL = new BeanComparator("spaceInformation.level");

	public static abstract class FloorFactory implements Serializable, FactoryExecutor {
		private Integer level;

		public Integer getLevel() {
			return level;
		}

		public void setLevel(Integer level) {
			this.level = level;
		}
	}

	public static class FloorFactoryCreator extends FloorFactory {

		private DomainReference<Space> surroundingSpaceReference;

		public Space getSurroundingSpace() {
			return surroundingSpaceReference == null ? null : surroundingSpaceReference.getObject();
		}
		public void setSurroundingSpace(Space surroundingSpace) {
			if (surroundingSpace != null) {
				this.surroundingSpaceReference = new DomainReference<Space>(surroundingSpace);
			}
		}

		public Floor execute() {
			return new Floor(this);
		}
	}

	public static class FloorFactoryEditor extends FloorFactory {

		private DomainReference<Floor> floorReference;

		public Floor getFloor() {
			return floorReference == null ? null : floorReference.getObject();
		}
		public void setFloor(Floor floor) {
			if (floor != null) {
				this.floorReference = new DomainReference<Floor>(floor);
			}
		}

		public FloorInformation execute() {
			return new FloorInformation(getFloor(), this);
		}

	}

    protected Floor() {
    	super();
    }

    public Floor(final FloorFactoryCreator floorFactoryCreator) {
        this();

        final Space suroundingSpace = floorFactoryCreator.getSurroundingSpace();
        if (suroundingSpace == null) {
            throw new NullPointerException("error.surrounding.space");
        }
        setSuroundingSpace(suroundingSpace);

        new FloorInformation(this, floorFactoryCreator);
    }

    @Override
    public FloorInformation getSpaceInformation() {
        return (FloorInformation) super.getSpaceInformation();
    }

    @Override
    public FloorInformation getSpaceInformation(final YearMonthDay when) {
        return (FloorInformation) super.getSpaceInformation(when);
    }

}
