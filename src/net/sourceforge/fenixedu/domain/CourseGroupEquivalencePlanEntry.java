package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import pt.utl.ist.fenix.tools.util.StringAppender;

public class CourseGroupEquivalencePlanEntry extends CourseGroupEquivalencePlanEntry_Base {

    public static class CourseGroupEquivalencePlanEntryCreator implements FactoryExecutor, Serializable {

	private DomainReference<EquivalencePlan> equivalencePlan;

	private DomainReference<CourseGroup> originCourseGroup;

	private DomainReference<CourseGroup> destinationCourseGroup;

	public CourseGroupEquivalencePlanEntryCreator(final EquivalencePlan equivalencePlan, final CourseGroup destinationCourseGroup) {
	    setEquivalencePlan(equivalencePlan);
	    setDestinationCourseGroup(destinationCourseGroup);
	}

	public Object execute() {
	    return new CourseGroupEquivalencePlanEntry(getEquivalencePlan(), getOriginCourseGroup(), getDestinationCourseGroup());
	}

	public EquivalencePlan getEquivalencePlan() {
	    return equivalencePlan == null ? null : equivalencePlan.getObject();
	}

	public void setEquivalencePlan(EquivalencePlan equivalencePlan) {
	    this.equivalencePlan = equivalencePlan == null ? null : new DomainReference<EquivalencePlan>(equivalencePlan);
	}

	public CourseGroup getOriginCourseGroup() {
	    return originCourseGroup == null ? null : originCourseGroup.getObject();
	}

	public void setOriginCourseGroup(CourseGroup originCourseGroup) {
	    this.originCourseGroup = originCourseGroup == null ? null : new DomainReference<CourseGroup>(originCourseGroup);
	}

	public CourseGroup getDestinationCourseGroup() {
	    return destinationCourseGroup == null ? null : destinationCourseGroup.getObject();
	}

	public void setDestinationCourseGroup(CourseGroup destinationCourseGroup) {
	    this.destinationCourseGroup = destinationCourseGroup == null ? null : new DomainReference<CourseGroup>(destinationCourseGroup);
	}

    }

    public static final Comparator<CourseGroupEquivalencePlanEntry> COMPARATOR_BY_OLD_COURSE_GROUP_NAME_AND_NEW_COURSE_GROUP_NAME = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_OLD_COURSE_GROUP_NAME_AND_NEW_COURSE_GROUP_NAME).addComparator(new BeanComparator("oldCourseGroup.name", Collator.getInstance()));
	((ComparatorChain) COMPARATOR_BY_OLD_COURSE_GROUP_NAME_AND_NEW_COURSE_GROUP_NAME).addComparator(new BeanComparator("newCourseGroup.name", Collator.getInstance()));
	((ComparatorChain) COMPARATOR_BY_OLD_COURSE_GROUP_NAME_AND_NEW_COURSE_GROUP_NAME).addComparator(COMPARATOR_BY_ID);
    }

    protected CourseGroupEquivalencePlanEntry() {
	super();
    }

    public CourseGroupEquivalencePlanEntry(final EquivalencePlan equivalencePlan,
	    final CourseGroup oldCourseGroup, final CourseGroup newCourseGroup) {

	init(equivalencePlan, oldCourseGroup, newCourseGroup);

    }

    private void init(EquivalencePlan equivalencePlan, CourseGroup oldCourseGroup,
	    CourseGroup newCourseGroup) {

	super.init(equivalencePlan);

	checkParameters(oldCourseGroup, newCourseGroup);

	super.setOldCourseGroup(oldCourseGroup);
	super.setNewCourseGroup(newCourseGroup);

    }

    private void checkParameters(CourseGroup oldCourseGroup, CourseGroup newCourseGroup) {
	if (oldCourseGroup == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.CourseGroupEquivalencePlanEntry.oldCourseGroup.cannot.be.null");
	}

	if (newCourseGroup == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.enclosing_type.newCourseGroup.cannot.be.null");
	}
    }

    @Override
    public boolean isCourseGroupEntry() {
	return true;
    }

    @Override
    public void delete() {
	removeOldCourseGroup();
	removeNewCourseGroup();
	super.delete();
    }

    @Override
    public boolean hasAnyDestinationDegreeModuleFor(final DegreeCurricularPlan degreeCurricularPlan) {
	return degreeCurricularPlan.hasDegreeModule(getNewCourseGroup());
    }

    @Override
    public boolean isFor(DegreeCurricularPlan degreeCurricularPlan) {
	return getNewCourseGroup().getParentDegreeCurricularPlan() == degreeCurricularPlan;
    }

    @Override
    public boolean isFor(final DegreeModule degreeModule) {
	return getNewCourseGroup() == degreeModule || getOldCourseGroup() == degreeModule;
    }

    @Override
    protected String getCompareString() {
	return StringAppender.append(getOldCourseGroup().getName(), getNewCourseGroup().getName(), getIdInternal().toString()); 
    }
}
