package net.sourceforge.fenixedu.domain.degreeStructure;

import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.injectionCode.Checked;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import dml.runtime.RelationAdapter;

public class Context extends Context_Base implements Comparable<Context> {

    public static final Comparator<Context> COMPARATOR_BY_DEGREE_MODULE_NAME = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_DEGREE_MODULE_NAME).addComparator(new BeanComparator("childDegreeModule.name", Collator
		.getInstance()));
	((ComparatorChain) COMPARATOR_BY_DEGREE_MODULE_NAME).addComparator(new BeanComparator("childDegreeModule.idInternal"));
    }

    public static Comparator<Context> COMPARATOR_BY_CURRICULAR_YEAR = new Comparator<Context>() {
	public int compare(Context leftContext, Context rightContext) {
	    int comparationResult = leftContext.getCurricularYear().compareTo(rightContext.getCurricularYear());
	    return (comparationResult == 0) ? leftContext.getIdInternal().compareTo(rightContext.getIdInternal())
		    : comparationResult;
	}
    };

    static {
	CourseGroupContext.addListener(new RelationAdapter<Context, CourseGroup>() {

	    @Override
	    public void beforeAdd(Context context, CourseGroup courseGroup) {
		if (context != null && courseGroup != null) {
		    if (context.getChildDegreeModule() != null && context.getChildDegreeModule().isCycleCourseGroup()) {
			CycleCourseGroup cycleCourseGroup = (CycleCourseGroup) context.getChildDegreeModule();
			if (cycleCourseGroup.getParentContexts().size() > 1) {
			    throw new DomainException("error.degreeStructure.CycleCourseGroup.can.only.have.one.parent");
			}
			if (!courseGroup.isRoot()) {
			    throw new DomainException("error.degreeStructure.CycleCourseGroup.parent.must.be.RootCourseGroup");
			}
		    }
		}
	    }

	});

	DegreeModuleContext.addListener(new RelationAdapter<Context, DegreeModule>() {
	    @Override
	    public void beforeAdd(Context context, DegreeModule degreeModule) {
		if (context != null && degreeModule != null) {
		    if (degreeModule.isRoot()) {
			throw new DomainException("error.degreeStructure.RootCourseGroup.cannot.have.parent.contexts");
		    }
		    if (degreeModule.isCycleCourseGroup()) {
			CycleCourseGroup cycleCourseGroup = (CycleCourseGroup) degreeModule;
			if (cycleCourseGroup.getParentContexts().size() > 0) {
			    throw new DomainException("error.degreeStructure.CycleCourseGroup.can.only.have.one.parent");
			}
			if (context.getParentCourseGroup() != null && !context.getParentCourseGroup().isRoot()) {
			    throw new DomainException("error.degreeStructure.CycleCourseGroup.parent.must.be.RootCourseGroup");
			}
		    }
		}
	    }
	});

    }

    protected Context() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	this.setChildOrder(0);
    }

    public Context(final CourseGroup courseGroup, final DegreeModule degreeModule, final CurricularPeriod curricularPeriod,
	    final ExecutionSemester begin, final ExecutionSemester end) {

	this();

	checkParameters(courseGroup, degreeModule, begin);
	checkExecutionPeriods(begin, end);
	checkIfCanAddDegreeModuleToCourseGroup(courseGroup, degreeModule, curricularPeriod, begin.getExecutionYear());
	checkExistingCourseGroupContexts(courseGroup, degreeModule, curricularPeriod, begin, end);

	super.setParentCourseGroup(courseGroup);
	super.setChildDegreeModule(degreeModule);
	super.setCurricularPeriod(curricularPeriod);
	super.setBeginExecutionPeriod(begin);
	super.setEndExecutionPeriod(end);
    }

    private void checkIfCanAddDegreeModuleToCourseGroup(final CourseGroup courseGroup, final DegreeModule degreeModule,
	    final CurricularPeriod curricularPeriod, final ExecutionYear executionYear) {
	if (degreeModule.isLeaf()) {
	    checkIfCanAddCurricularCourseToCourseGroup(courseGroup, (CurricularCourse) degreeModule, curricularPeriod,
		    executionYear);
	} else {
	    checkIfCanAddCourseGroupToCourseGroup(courseGroup, (CourseGroup) degreeModule);
	}
    }

    private void checkIfCanAddCurricularCourseToCourseGroup(final CourseGroup parent, final CurricularCourse curricularCourse,
	    final CurricularPeriod curricularPeriod, final ExecutionYear executionYear) {
	if (curricularCourse.hasCompetenceCourse() && curricularCourse.getCompetenceCourse().isAnual(executionYear)
		&& !curricularPeriod.hasChildOrderValue(1)) {
	    throw new DomainException("competenceCourse.anual.but.trying.to.associate.curricular.course.not.to.first.period");
	}
    }

    private void checkIfCanAddCourseGroupToCourseGroup(final CourseGroup parent, final CourseGroup courseGroup) {
	parent.checkDuplicateChildNames(courseGroup.getName(), courseGroup.getNameEn());
    }

    private void checkParameters(CourseGroup courseGroup, DegreeModule degreeModule, ExecutionSemester beginExecutionPeriod) {
	if (courseGroup == null || degreeModule == null || beginExecutionPeriod == null) {
	    throw new DomainException("error.incorrectContextValues");
	}
    }

    private void checkExistingCourseGroupContexts(final CourseGroup courseGroup, final DegreeModule degreeModule,
	    final CurricularPeriod curricularPeriod, final ExecutionSemester begin, final ExecutionSemester end) {

	for (final Context context : courseGroup.getChildContexts()) {
	    if (context != this && context.hasChildDegreeModule(degreeModule) && context.hasCurricularPeriod(curricularPeriod)
		    && context.contains(begin, end)) {
		throw new DomainException("courseGroup.contextAlreadyExistForCourseGroup");
	    }
	}
    }

    public void edit(final CourseGroup parent, final CurricularPeriod curricularPeriod, final ExecutionSemester begin,
	    final ExecutionSemester end) {
	setParentCourseGroup(parent);
	setCurricularPeriod(curricularPeriod);
	edit(begin, end);
    }

    protected void edit(final ExecutionSemester begin, final ExecutionSemester end) {
	checkExecutionPeriods(begin, end);
	checkExistingCourseGroupContexts(getParentCourseGroup(), getChildDegreeModule(), getCurricularPeriod(), begin, end);
	setBeginExecutionPeriod(begin);
	setEndExecutionPeriod(end);
	checkCurriculumLines();
    }

    private void checkCurriculumLines() {
	for (final CurriculumModule curriculumModule : getChildDegreeModule().getCurriculumModules()) {
	    if (curriculumModule.isCurriculumLine()) {
		final CurriculumLine curriculumLine = (CurriculumLine) curriculumModule;
		if (curriculumLine.hasExecutionPeriod()
			&& !getChildDegreeModule().hasAnyParentContexts(curriculumLine.getExecutionPeriod())) {
		    throw new DomainException("error.Context.cannot.modify.begin.and.end.because.of.enroled.curriculumLines");
		}
	    }
	}
    }

    public void delete() {
	removeCurricularPeriod();
	removeChildDegreeModule();
	removeParentCourseGroup();
	removeBeginExecutionPeriod();
	removeEndExecutionPeriod();
	removeRootDomainObject();
	getAssociatedWrittenEvaluations().clear();
	super.deleteDomainObject();
    }

    public int compareTo(Context o) {
	int orderCompare = this.getChildOrder().compareTo(o.getChildOrder());
	if (this.getParentCourseGroup().equals(o.getParentCourseGroup()) && orderCompare != 0) {
	    return orderCompare;
	} else {
	    if (this.getChildDegreeModule() instanceof CurricularCourse) {
		int periodsCompare = this.getCurricularPeriod().compareTo(o.getCurricularPeriod());
		if (periodsCompare != 0) {
		    return periodsCompare;
		}
		return Collator.getInstance().compare(this.getChildDegreeModule().getName(), o.getChildDegreeModule().getName());
	    } else {
		return Collator.getInstance().compare(this.getChildDegreeModule().getName(), o.getChildDegreeModule().getName());
	    }
	}
    }

    @Override
    @Checked("ContextPredicates.curricularPlanMemberWritePredicate")
    public void setParentCourseGroup(CourseGroup courseGroup) {
	super.setParentCourseGroup(courseGroup);
    }

    @Override
    @Checked("ContextPredicates.curricularPlanMemberWritePredicate")
    public void setCurricularPeriod(CurricularPeriod curricularPeriod) {
	super.setCurricularPeriod(curricularPeriod);
    }

    @Override
    @Checked("ContextPredicates.curricularPlanMemberWritePredicate")
    public void setChildDegreeModule(DegreeModule degreeModule) {
	super.setChildDegreeModule(degreeModule);
    }

    public boolean isValid(final ExecutionSemester executionSemester) {
	if (isOpen(executionSemester)) {
	    if (getChildDegreeModule().isCurricularCourse()) {
		CurricularCourse curricularCourse = (CurricularCourse) getChildDegreeModule();
		if (!curricularCourse.isAnual(executionSemester.getExecutionYear())) {
		    return containsSemester(executionSemester.getSemester());
		}
	    }
	    return true;
	}
	return false;
    }

    public boolean isValid(final ExecutionYear executionYear) {
	for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
	    if (isValid(executionSemester)) {
		return true;
	    }
	}
	return false;
    }

    protected void checkExecutionPeriods(ExecutionSemester beginExecutionPeriod, ExecutionSemester endExecutionPeriod) {
	if (beginExecutionPeriod == null) {
	    throw new DomainException("context.begin.execution.period.cannot.be.null");
	}
	if (endExecutionPeriod != null && beginExecutionPeriod.isAfter(endExecutionPeriod)) {
	    throw new DomainException("context.begin.is.after.end.execution.period");
	}
    }

    public boolean isOpen(final ExecutionSemester executionSemester) {
	return getBeginExecutionPeriod().isBeforeOrEquals(executionSemester)
		&& (!hasEndExecutionPeriod() || getEndExecutionPeriod().isAfterOrEquals(executionSemester));
    }

    public boolean isOpen(final ExecutionYear executionYear) {
	for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
	    if (isOpen(executionSemester)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isOpen() {
	return isOpen(ExecutionSemester.readActualExecutionSemester());
    }

    public boolean contains(final ExecutionSemester begin, final ExecutionSemester end) {
	if (end != null && begin.isAfter(end)) {
	    throw new DomainException("context.begin.is.after.end.execution.period");
	}

	if (begin.isAfterOrEquals(getBeginExecutionPeriod())) {
	    return !hasEndExecutionPeriod() || begin.isBeforeOrEquals(getEndExecutionPeriod());
	} else {
	    return end == null || end.isAfterOrEquals(getBeginExecutionPeriod());
	}
    }

    @Deprecated
    public Integer getOrder() {
	return super.getChildOrder();
    }

    @Deprecated
    public void setOrder(Integer order) {
	super.setChildOrder(order);
    }

    public boolean containsCurricularYear(final Integer curricularYear) {
	final CurricularPeriod firstCurricularPeriod = getCurricularPeriod().getParent();
	final int firstCurricularPeriodOrder = firstCurricularPeriod.getAbsoluteOrderOfChild();
	return curricularYear.intValue() == firstCurricularPeriodOrder;
    }

    public boolean containsSemester(final Integer semester) {
	final CurricularPeriod firstCurricularPeriod = getCurricularPeriod();
	final int firstCurricularPeriodOrder = firstCurricularPeriod.getChildOrder();
	return semester.intValue() == firstCurricularPeriodOrder;
    }

    public boolean containsSemesterAndCurricularYear(final Integer semester, final Integer curricularYear,
	    final RegimeType regimeType) {

	final int argumentOrder = (curricularYear - 1) * 2 + semester.intValue();
	final CurricularPeriod firstCurricularPeriod = getCurricularPeriod();
	final int firstCurricularPeriodOrder = firstCurricularPeriod.getAbsoluteOrderOfChild();
	final int duration;
	if (regimeType == RegimeType.ANUAL) {
	    duration = 2;
	} else if (regimeType == RegimeType.SEMESTRIAL) {
	    duration = 1;
	} else {
	    throw new IllegalArgumentException("Unknown regimeType: " + regimeType);
	}
	final int lastCurricularPeriodOrder = firstCurricularPeriodOrder + duration - 1;
	return firstCurricularPeriodOrder <= argumentOrder && argumentOrder <= lastCurricularPeriodOrder;
    }

    private DegreeModuleScopeContext degreeModuleScopeContext = null;

    private synchronized void initDegreeModuleScopeContext() {
	if (degreeModuleScopeContext == null) {
	    degreeModuleScopeContext = new DegreeModuleScopeContext(this);
	}
    }

    public DegreeModuleScopeContext getDegreeModuleScopeContext() {
	if (degreeModuleScopeContext == null) {
	    initDegreeModuleScopeContext();
	}
	return degreeModuleScopeContext;
    }

    @Override
    public void setBeginExecutionPeriod(ExecutionSemester beginExecutionPeriod) {
	if (beginExecutionPeriod == null) {
	    throw new DomainException("curricular.rule.begin.execution.period.cannot.be.null");
	}
	super.setBeginExecutionPeriod(beginExecutionPeriod);
    }

    public void removeBeginExecutionPeriod() {
	super.setBeginExecutionPeriod(null);
    }

    public Integer getCurricularYear() {
	return getCurricularPeriod().getParent().getAbsoluteOrderOfChild();
    }

    public void addAllCourseGroups(Set<CourseGroup> courseGroups) {
	final DegreeModule degreeModule = getChildDegreeModule();
	if (!degreeModule.isLeaf()) {
	    final CourseGroup courseGroup = (CourseGroup) degreeModule;
	    courseGroups.add(courseGroup);
	    courseGroup.getAllCoursesGroupse(courseGroups);
	}
    }

    public void getAllDegreeModules(final Collection<DegreeModule> degreeModules) {
	final DegreeModule degreeModule = getChildDegreeModule();
	degreeModule.getAllDegreeModules(degreeModules);
    }

    public boolean hasChildDegreeModule(final DegreeModule degreeModule) {
	return hasChildDegreeModule() && getChildDegreeModule().equals(degreeModule);
    }

    public boolean hasCurricularPeriod(final CurricularPeriod curricularPeriod) {
	return hasCurricularPeriod() && getCurricularPeriod().equals(curricularPeriod);
    }

    public class DegreeModuleScopeContext extends DegreeModuleScope {

	private final Context context;

	private DegreeModuleScopeContext(Context context) {
	    this.context = context;
	}

	@Override
	public Integer getIdInternal() {
	    return context.getIdInternal();
	}

	@Override
	public Integer getCurricularSemester() {
	    return context.getCurricularPeriod().getChildOrder();
	}

	@Override
	public Integer getCurricularYear() {
	    return context.getCurricularYear();
	}

	@Override
	public String getBranch() {
	    return "";
	}

	public Context getContext() {
	    return context;
	}

	@Override
	public boolean isActiveForExecutionPeriod(final ExecutionSemester executionSemester) {
	    final ExecutionYear executionYear = executionSemester.getExecutionYear();
	    return getCurricularCourse().isAnual(executionYear) ? getContext().isValid(executionYear) : getContext().isValid(
		    executionSemester);
	}

	@Override
	public CurricularCourse getCurricularCourse() {
	    return (CurricularCourse) context.getChildDegreeModule();
	}

	@Override
	public String getAnotation() {
	    return null;
	}

	@Override
	public String getClassName() {
	    return context.getClass().getName();
	}

	@Override
	public boolean equals(Object obj) {
	    if (!(obj instanceof DegreeModuleScopeContext)) {
		return false;
	    }
	    return context.equals(((DegreeModuleScopeContext) obj).getContext());
	}

	@Override
	public int hashCode() {
	    return context.hashCode();
	}
    }

}