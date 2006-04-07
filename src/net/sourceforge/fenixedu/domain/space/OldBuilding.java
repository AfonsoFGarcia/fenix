package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;


public class OldBuilding extends OldBuilding_Base {

    public OldBuilding() {
    	super();
    }
    
    public void delete() {
        if (!hasAnyRooms()) {
            deleteDomainObject();    
        } else {
            throw new DomainException("");
        }
    }

}
