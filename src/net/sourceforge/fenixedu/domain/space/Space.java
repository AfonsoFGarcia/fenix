package net.sourceforge.fenixedu.domain.space;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.YearMonthDay;

import dml.runtime.RelationAdapter;

public abstract class Space extends Space_Base {

    static {
        SpaceSpaceInformation.addListener(new SpaceSpaceInformationListener());
    }

    protected Space() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(this.getClass().getName());
    }

    public SpaceInformation getSpaceInformation() {
        return getSpaceInformation(null);
    }
    
    public SpaceInformation getSpaceInformation(final YearMonthDay when) {
        SpaceInformation selectedSpaceInformation = null;

        for (final SpaceInformation spaceInformation : getSpaceInformations()) {
            final YearMonthDay validUntil = spaceInformation.getValidUntil();

            if (validUntil == null) {
                if (selectedSpaceInformation == null) {
                    selectedSpaceInformation = spaceInformation;
                }
            } else {
                if (validUntil.isAfter((when != null) ? when : new YearMonthDay())) {
                    if (selectedSpaceInformation == null
                            || selectedSpaceInformation.getValidUntil() == null
                            || selectedSpaceInformation.getValidUntil().isAfter(validUntil)) {
                        selectedSpaceInformation = spaceInformation;
                    }
                }
            }
        }

        return selectedSpaceInformation;
    }

    public SortedSet<SpaceInformation> getOrderedSpaceInformations() {
    	final TreeSet<SpaceInformation> spaceInformations = new TreeSet<SpaceInformation>(getSpaceInformations());
    	YearMonthDay previousValidUntil = null;
    	for (final SpaceInformation spaceInformation : spaceInformations) {
    		spaceInformation.setValidFrom(previousValidUntil);
    		previousValidUntil = spaceInformation.getValidUntil();
    	}
    	return spaceInformations;
    }

    public void delete() {
        for (final List<Space> containedSpaces = getContainedSpaces();
                !containedSpaces.isEmpty(); containedSpaces.get(0).delete());
        for (final SpaceInformation spaceInformation : getSpaceInformations()) {
            spaceInformation.deleteMaintainingReferenceToSpace();
        }
        setSuroundingSpace(null);
        deleteDomainObject();
    }

    public static class SpaceSpaceInformationListener extends RelationAdapter<Space, SpaceInformation> {

        @Override
        public void beforeAdd(Space space, SpaceInformation spaceInformation) {
        	if (space != null) {
        		for (final SpaceInformation otherSpaceInformation : space.getSpaceInformations()) {
        			if (otherSpaceInformation.getValidUntil() == null) {
        				otherSpaceInformation.setValidUntil(new YearMonthDay());
        			}
        		}
            }
        }

        @Override
        public void afterRemove(Space space, SpaceInformation spaceInformation) {
            if (space != null) {
                if (spaceInformation.getValidUntil() == null) {
                    final SpaceInformation nextMostRecentSpaceInformation = findMostRecentSpaceInformation(space.getSpaceInformations());
                    if (nextMostRecentSpaceInformation != null) {
                        nextMostRecentSpaceInformation.setValidUntil(null);
                    }
                }
            }
        }

        private static SpaceInformation findMostRecentSpaceInformation(final List<SpaceInformation> spaceInformations) {
            SpaceInformation selectedSpaceInformation = null;

            for (final SpaceInformation spaceInformation : spaceInformations) {
                final YearMonthDay validUntil = spaceInformation.getValidUntil();

                if (selectedSpaceInformation == null || validUntil.isAfter(selectedSpaceInformation.getValidUntil())) {
                    selectedSpaceInformation = spaceInformation;
                }
            }

            return selectedSpaceInformation;
        }

    }

}
