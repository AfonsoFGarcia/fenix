package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.resource.Resource;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class OldBuilding extends OldBuilding_Base {

    public final static Comparator<Building> BUILDING_COMPARATOR_BY_NAME = new ComparatorChain();
    static {
	((ComparatorChain) BUILDING_COMPARATOR_BY_NAME).addComparator(new BeanComparator("name", Collator.getInstance()));
	((ComparatorChain) BUILDING_COMPARATOR_BY_NAME).addComparator(DomainObject.COMPARATOR_BY_ID);
    }
    
    public OldBuilding() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	if (!hasAnyRooms()) {
	    removeCampus();
	    removeRootDomainObject();
	    deleteDomainObject();
	} else {
	    throw new DomainException("");
	}
    }
    
    public static Set<OldBuilding> getOldBuildings() {
	final Set<OldBuilding> oldBuildings = new HashSet<OldBuilding>();
	for (Resource resource : RootDomainObject.getInstance().getResources()) {
	    if (resource instanceof OldBuilding) {
		OldBuilding oldBuilding = (OldBuilding) resource;
		oldBuildings.add(oldBuilding);
	    }
	}
	return oldBuildings;
    }

}
