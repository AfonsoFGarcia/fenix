package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class RootCourseGroup extends RootCourseGroup_Base {

    protected RootCourseGroup() {
	super();
    }

    public RootCourseGroup(final DegreeCurricularPlan degreeCurricularPlan, final String name,
	    final String nameEn) {
	if (degreeCurricularPlan == null) {
	    throw new DomainException(
		    "error.degreeStructure.CourseGroup.degreeCurricularPlan.cannot.be.null");
	}
	init(name, nameEn);
	setParentDegreeCurricularPlan(degreeCurricularPlan);
	createCycleCourseGroups(degreeCurricularPlan.getDegreeType());
    }

    private void createCycleCourseGroups(DegreeType courseGroupType) {
	if (courseGroupType.isBolonhaType()) {
	    ExecutionPeriod executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
	    if (courseGroupType.isFirstCycle()) {
		new CycleCourseGroup(this, "1� Ciclo", "First Cycle", CycleType.FIRST_CYCLE,
			executionPeriod, null);
	    }
	    if (courseGroupType.isSecondCycle()) {
		new CycleCourseGroup(this, "2� Ciclo", "Second Cycle", CycleType.SECOND_CYCLE,
			executionPeriod, null);
	    }
	    if (courseGroupType.isThirdCycle()) {
		new CycleCourseGroup(this, "3� Ciclo", "Third Cycle", CycleType.THIRD_CYCLE,
			executionPeriod, null);
	    }
	}
    }

    @Override
    public boolean isRoot() {
	return true;
    }

    public void delete() {
	removeParentDegreeCurricularPlan();
	super.delete();
    }

    static public RootCourseGroup createRoot(final DegreeCurricularPlan degreeCurricularPlan,
	    final String name, final String nameEn) {
	return new RootCourseGroup(degreeCurricularPlan, name, nameEn);
    }

    @Override
    public void addParentContexts(Context parentContexts) {
	throw new DomainException("error.degreeStructure.RootCourseGroup.cannot.have.parent.contexts");
    }

    public CycleCourseGroup getFirstCycleCourseGroup() {
	return getCycleCourseGroup(CycleType.FIRST_CYCLE);
    }

    public CycleCourseGroup getSecondCycleCourseGroup() {
	return getCycleCourseGroup(CycleType.SECOND_CYCLE);
    }

    public CycleCourseGroup getThirdCycleCourseGroup() {
	return getCycleCourseGroup(CycleType.THIRD_CYCLE);
    }

    public CycleCourseGroup getCycleCourseGroup(CycleType cycleType) {
	for (CycleCourseGroup cycleCourseGroup : getCycleCourseGroups()) {
	    if (cycleCourseGroup.getCycleType() == cycleType) {
		return cycleCourseGroup;
	    }
	}
	return null;
    }

    public Collection<CycleCourseGroup> getCycleCourseGroups(CycleType cycle) {

	Collection<CycleCourseGroup> result = new HashSet<CycleCourseGroup>();

	if (cycle == null) {

	    CycleType firstCycleType = getDegree().getDegreeType().getFirstCycleType();
	    if (firstCycleType != null) {
		return getCycleCourseGroups(firstCycleType);
	    }

	} else {

	    for (Context context : getChildContextsSet()) {
		if (context.getChildDegreeModule().isCycleCourseGroup()) {

		    CycleCourseGroup cycleCourseGroup = (CycleCourseGroup) context
			    .getChildDegreeModule();
		    if (cycle == null || cycle == cycleCourseGroup.getCycleType()) {
			result.add((CycleCourseGroup) context.getChildDegreeModule());
		    }
		}
	    }

	}

	return result;

    }

    private Collection<CycleCourseGroup> getCycleCourseGroups() {
	return getCycleCourseGroups(null);
    }

    public boolean hasCycleGroups() {
	return !getCycleCourseGroups().isEmpty();
    }

}
