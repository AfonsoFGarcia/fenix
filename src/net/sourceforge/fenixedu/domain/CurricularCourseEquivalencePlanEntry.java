package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.domain.util.LogicOperator;
import net.sourceforge.fenixedu.injectionCode.Checked;

public class CurricularCourseEquivalencePlanEntry extends CurricularCourseEquivalencePlanEntry_Base {

    public static Comparator<CurricularCourseEquivalencePlanEntry> COMPARATOR_BY_OLD_CURRICULAR_COURSE_NAMES = new Comparator<CurricularCourseEquivalencePlanEntry>() {

	public int compare(final CurricularCourseEquivalencePlanEntry o1,
		final CurricularCourseEquivalencePlanEntry o2) {
	    final String o1String = getCompareString(o1);
	    final String o2String = getCompareString(o2);
	    return Collator.getInstance().compare(o1String, o2String);
	}

	private String getCompareString(
		final CurricularCourseEquivalencePlanEntry curricularCourseEquivalencePlanEntry) {
	    final StringBuilder stringBuilder = new StringBuilder();
	    for (final CurricularCourse curricularCourse : curricularCourseEquivalencePlanEntry
		    .getOldCurricularCoursesSet()) {
		stringBuilder.append(curricularCourse.getName());
	    }
	    stringBuilder.append(curricularCourseEquivalencePlanEntry.getIdInternal());
	    return stringBuilder.toString();
	}
    };

    public static Comparator<CurricularCourseEquivalencePlanEntry> COMPARATOR_BY_OLD_CURRICULAR_COURSE_NAMES_AND_NEW_CURRICULAR_COURSE_NAMES = new Comparator<CurricularCourseEquivalencePlanEntry>() {

	public int compare(final CurricularCourseEquivalencePlanEntry o1, final CurricularCourseEquivalencePlanEntry o2) {
	    final String o1String = getCompareString(o1);
	    final String o2String = getCompareString(o2);
	    return Collator.getInstance().compare(o1String, o2String);
	}

	private String getCompareString(final CurricularCourseEquivalencePlanEntry curricularCourseEquivalencePlanEntry) {
	    final StringBuilder stringBuilder = new StringBuilder();
	    getCompareString(stringBuilder, curricularCourseEquivalencePlanEntry.getOldCurricularCoursesSet());
	    getCompareString(stringBuilder, curricularCourseEquivalencePlanEntry.getNewCurricularCoursesSet());
	    stringBuilder.append(curricularCourseEquivalencePlanEntry.getIdInternal());
	    return stringBuilder.toString();
	}

	private void getCompareString(final StringBuilder stringBuilder, final Set<CurricularCourse> curricularCourses) {
	    for (final CurricularCourse curricularCourse : curricularCourses) {
		stringBuilder.append(curricularCourse.getName());
	    }
	}
    };

    public static class CurricularCourseEquivalencePlanEntryCreator implements FactoryExecutor,
	    Serializable {
	private DomainReference<EquivalencePlan> equivalencePlan;

	private Set<DomainReference<CurricularCourse>> originCurricularCourses = new HashSet<DomainReference<CurricularCourse>>();

	private Set<DomainReference<CurricularCourse>> destinationCurricularCourses = new HashSet<DomainReference<CurricularCourse>>();

	private DomainReference<CurricularCourse> originCurricularCourseToAdd;

	private DomainReference<CurricularCourse> destinationCurricularCourseToAdd;

	private LogicOperator logicOperator;

	public CurricularCourseEquivalencePlanEntryCreator(final EquivalencePlan equivalencePlan,
		final CurricularCourse curricularCourse) {
	    setEquivalencePlan(equivalencePlan);
	    addDestination(curricularCourse);
	    setLogicOperator(LogicOperator.AND);
	}

	public Object execute() {
	    return new CurricularCourseEquivalencePlanEntry(getEquivalencePlan(), getOriginCurricularCourses(),
		    getDestinationCurricularCourses(), null, getLogicOperator());
	}

	public EquivalencePlan getEquivalencePlan() {
	    return equivalencePlan == null ? null : equivalencePlan.getObject();
	}

	public void setEquivalencePlan(EquivalencePlan equivalencePlan) {
	    this.equivalencePlan = equivalencePlan == null ? null
		    : new DomainReference<EquivalencePlan>(equivalencePlan);
	}

	public Set<CurricularCourse> getOriginCurricularCourses() {
	    final Set<CurricularCourse> curricularCourses = new TreeSet<CurricularCourse>(CurricularCourse.COMPARATOR_BY_NAME);
	    for (final DomainReference<CurricularCourse> curricularCourse : this.originCurricularCourses) {
		curricularCourses.add(curricularCourse.getObject());
	    }
	    return curricularCourses;
	}

	public Set<CurricularCourse> getDestinationCurricularCourses() {
	    final Set<CurricularCourse> curricularCourses = new TreeSet<CurricularCourse>(CurricularCourse.COMPARATOR_BY_NAME);
	    for (final DomainReference<CurricularCourse> curricularCourse : this.destinationCurricularCourses) {
		curricularCourses.add(curricularCourse.getObject());
	    }
	    return curricularCourses;
	}

	public CurricularCourse getOriginCurricularCourseToAdd() {
	    return originCurricularCourseToAdd == null ? null : originCurricularCourseToAdd.getObject();
	}

	public void setOriginCurricularCourseToAdd(CurricularCourse curricularCourseToAdd) {
	    this.originCurricularCourseToAdd = curricularCourseToAdd == null ? null : new DomainReference<CurricularCourse>(curricularCourseToAdd);
	}

	public CurricularCourse getDestinationCurricularCourseToAdd() {
	    return destinationCurricularCourseToAdd == null ? null : destinationCurricularCourseToAdd.getObject();
	}

	public void setDestinationCurricularCourseToAdd(CurricularCourse curricularCourseToAdd) {
	    this.destinationCurricularCourseToAdd = curricularCourseToAdd == null ? null : new DomainReference<CurricularCourse>(curricularCourseToAdd);
	}

	public void addOrigin(CurricularCourse curricularCourseToAdd) {
	    if (curricularCourseToAdd != null) {
		originCurricularCourses.add(new DomainReference<CurricularCourse>(curricularCourseToAdd));
	    }
	}

	public void addDestination(CurricularCourse curricularCourseToAdd) {
	    if (curricularCourseToAdd != null) {
		destinationCurricularCourses.add(new DomainReference<CurricularCourse>(curricularCourseToAdd));
	    }
	}

	public LogicOperator getLogicOperator() {
	    return logicOperator;
	}

	public void setLogicOperator(LogicOperator logicOperator) {
	    this.logicOperator = logicOperator;
	}

    }

    protected CurricularCourseEquivalencePlanEntry() {
	super();
    }

    public CurricularCourseEquivalencePlanEntry(final EquivalencePlan equivalencePlan,
	    final Collection<CurricularCourse> oldCurricularCourses,
	    final Collection<CurricularCourse> newCurricularCourses,
	    final CourseGroup previousCourseGroup, final LogicOperator newCurricularCoursesOperator) {
	this();
	init(equivalencePlan, oldCurricularCourses, newCurricularCourses, previousCourseGroup,
		newCurricularCoursesOperator);
	checkPermisionsForConstructor();
    }

    @Checked("EquivalencePlanPredicates.isCoordinator")
    private void checkPermisionsForConstructor() {
	// The annotation cannot be called before constructor because the sets
	// have to be done first.
    }

    private void init(final EquivalencePlan equivalencePlan,
	    final Collection<CurricularCourse> oldCurricularCourses,
	    final Collection<CurricularCourse> newCurricularCourses,
	    final CourseGroup previousCourseGroup, final LogicOperator newCurricularCoursesOperator) {
	super.init(equivalencePlan);
	checkParameters(oldCurricularCourses, newCurricularCourses, newCurricularCoursesOperator);
	super.getOldCurricularCourses().addAll(oldCurricularCourses);
	super.getNewCurricularCourses().addAll(newCurricularCourses);
	super.setPreviousCourseGroupForNewCurricularCourses(previousCourseGroup);
	super.setNewCurricularCoursesOperator(newCurricularCoursesOperator);
    }

    public void checkParameters(Collection<CurricularCourse> oldCurricularCourses,
	    Collection<CurricularCourse> newCurricularCourses,
	    LogicOperator newCurricularCoursesOperator) {
	if (oldCurricularCourses.isEmpty()) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry.oldCurricularCourses.cannot.be.empty");
	}

	if (newCurricularCourses.isEmpty()) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry.newCurricularCourse.cannot.be.empty");
	}

	if (newCurricularCoursesOperator == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry.newCurricularCoursesOperator.cannot.be.null");
	}

    }

    public Set<CurricularCourse> getOldCurricularCoursesSortedByName() {
	final Set<CurricularCourse> curricularCourses = new TreeSet<CurricularCourse>(
		CurricularCourse.COMPARATOR_BY_NAME);
	curricularCourses.addAll(getOldCurricularCourses());
	return curricularCourses;
    }

    @Checked("EquivalencePlanPredicates.isCoordinator")
    public void delete() {
	removeCourseGroupEquivalencePlanEntry();
	removeEquivalencePlan();
	getEquivalencePlansSet().clear();
	getNewCurricularCoursesSet().clear();
	getOldCurricularCoursesSet().clear();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public void checkPermissions(final Person person) {
	final DegreeCurricularPlanEquivalencePlan equivalencePlan = (DegreeCurricularPlanEquivalencePlan) getEquivalencePlan();
	final DegreeCurricularPlan degreeCurricularPlan = equivalencePlan.getDegreeCurricularPlan();
	for (final Coordinator coordinator : person.getCoordinatorsSet()) {
	    final ExecutionDegree executionDegree = coordinator.getExecutionDegree();
	    if (executionDegree.getDegreeCurricularPlan() == degreeCurricularPlan) {
		return;
	    }
	}
	throw new DomainException("error.logged.person.not.authorized.to.make.operation");
    }

    @Override
    public boolean isCurricularCourseEntry() {
	return true;
    }
}
