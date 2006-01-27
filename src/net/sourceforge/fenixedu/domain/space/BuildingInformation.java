package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class BuildingInformation extends BuildingInformation_Base {

    protected BuildingInformation(final Building building, final String buildingName) {
        super();
        setOjbConcreteClass(this.getClass().getName());
        setSpace(building);
        setName(buildingName);
    }

    @Override
    public void setName(final String name) {
        if (name == null) {
            throw new NullPointerException("error.building.name.cannot.be.null");
        }
        super.setName(name);
    }

    @Override
    public void setSpace(final Space space) {
        if (space == null) {
            setSpace(null);
        }
        throw new DomainException("error.incompatible.space");
    }

    public void setSpace(final Building building) {
        if (building == null) {
            setSpace(null);
            //throw new NullPointerException("error.building.cannot.be.null");
        } else if (getSpace() != null) {
            throw new DomainException("error.cannot.change.building");
        }
        super.setSpace(building);
    }

}
