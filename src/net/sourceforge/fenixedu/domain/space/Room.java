package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.YearMonthDay;

public class Room extends Room_Base {

	public class RoomFactory implements Serializable {
		private Integer blueprintNumber;
		private String identification;
		private String description;
		private String classification;
		private BigDecimal area;
		private Boolean heightQuality;
		private Boolean illuminationQuality;
		private Boolean distanceFromSanitaryInstalationsQuality;
		private Boolean securityQuality;
		private Boolean ageQuality;
		private String observations;

		public Boolean getAgeQuality() {
			return ageQuality;
		}
		public void setAgeQuality(Boolean ageQuality) {
			this.ageQuality = ageQuality;
		}
		public BigDecimal getArea() {
			return area;
		}
		public void setArea(BigDecimal area) {
			this.area = area;
		}
		public Integer getBlueprintNumber() {
			return blueprintNumber;
		}
		public void setBlueprintNumber(Integer blueprintNumber) {
			this.blueprintNumber = blueprintNumber;
		}
		public String getClassification() {
			return classification;
		}
		public void setClassification(String classification) {
			this.classification = classification;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Boolean getDistanceFromSanitaryInstalationsQuality() {
			return distanceFromSanitaryInstalationsQuality;
		}
		public void setDistanceFromSanitaryInstalationsQuality(Boolean distanceFromSanitaryInstalationsQuality) {
			this.distanceFromSanitaryInstalationsQuality = distanceFromSanitaryInstalationsQuality;
		}
		public Boolean getHeightQuality() {
			return heightQuality;
		}
		public void setHeightQuality(Boolean heightQuality) {
			this.heightQuality = heightQuality;
		}
		public String getIdentification() {
			return identification;
		}
		public void setIdentification(String identification) {
			this.identification = identification;
		}
		public Boolean getIlluminationQuality() {
			return illuminationQuality;
		}
		public void setIlluminationQuality(Boolean illuminationQuality) {
			this.illuminationQuality = illuminationQuality;
		}
		public String getObservations() {
			return observations;
		}
		public void setObservations(String observations) {
			this.observations = observations;
		}
		public Boolean getSecurityQuality() {
			return securityQuality;
		}
		public void setSecurityQuality(Boolean securityQuality) {
			this.securityQuality = securityQuality;
		}
	}

	public class RoomFactoryCreator extends RoomFactory {
        private DomainReference<Space> suroundingSpaceReference;

		public Space getSuroundingSpace() {
			return suroundingSpaceReference == null ? null : suroundingSpaceReference.getObject();
		}
		public void setSuroundingSpaceReference(Space suroundingSpace) {
			if (suroundingSpace != null) {
				this.suroundingSpaceReference = new DomainReference<Space>(suroundingSpace);
			}
		}

		public Room execute() {
			return new Room(this);
		}
	}

	public class RoomFactoryEditor extends RoomFactory {
        private DomainReference<Room> roomReference;

		public Room getRoom() {
			return roomReference == null ? null : roomReference.getObject();
		}
		public void setRoom(Room room) {
			if (room != null) {
				this.roomReference = new DomainReference<Room>(room);
			}
		}

		public RoomInformation execute() {
			return new RoomInformation(getRoom(), this);
		}
	}

	public Room() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(this.getClass().getName());
    }

    public Room(RoomFactoryCreator roomFactoryCreator) {
    	this();

    	final Space suroundingSpace = roomFactoryCreator.getSuroundingSpace();
        if (suroundingSpace == null) {
            throw new NullPointerException("error.surrounding.space");
        }
        setSuroundingSpace(suroundingSpace);

        new RoomInformation(this, roomFactoryCreator);
    }

    @Override
    public RoomInformation getSpaceInformation() {
        return (RoomInformation) super.getSpaceInformation();
    }

    @Override
    public RoomInformation getSpaceInformation(final YearMonthDay when) {
        return (RoomInformation) super.getSpaceInformation(when);
    }

}
