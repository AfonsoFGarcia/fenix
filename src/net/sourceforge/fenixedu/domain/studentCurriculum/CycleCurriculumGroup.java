package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.math.BigDecimal;
import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.curriculum.CycleConclusionProcess;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CycleCurriculumGroup extends CycleCurriculumGroup_Base {

    static final private Comparator<CycleCurriculumGroup> COMPARATOR_BY_CYCLE_TYPE = new Comparator<CycleCurriculumGroup>() {
	final public int compare(final CycleCurriculumGroup o1, final CycleCurriculumGroup o2) {
	    return CycleType.COMPARATOR_BY_LESS_WEIGHT.compare(o1.getCycleType(), o2.getCycleType());
	}
    };

    static final public Comparator<CycleCurriculumGroup> COMPARATOR_BY_CYCLE_TYPE_AND_ID = new Comparator<CycleCurriculumGroup>() {
	final public int compare(final CycleCurriculumGroup o1, final CycleCurriculumGroup o2) {
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE);
	    comparatorChain.addComparator(CycleCurriculumGroup.COMPARATOR_BY_ID);

	    return comparatorChain.compare(o1, o2);
	}
    };

    protected CycleCurriculumGroup() {
	super();
    }

    public CycleCurriculumGroup(RootCurriculumGroup rootCurriculumGroup, CycleCourseGroup cycleCourseGroup,
	    ExecutionSemester executionSemester) {
	this();
	init(rootCurriculumGroup, cycleCourseGroup, executionSemester);
    }

    public CycleCurriculumGroup(RootCurriculumGroup rootCurriculumGroup, CycleCourseGroup cycleCourseGroup) {
	this();
	init(rootCurriculumGroup, cycleCourseGroup);
    }

    @Override
    protected void init(CurriculumGroup curriculumGroup, CourseGroup courseGroup) {
	checkInitConstraints((RootCurriculumGroup) curriculumGroup, (CycleCourseGroup) courseGroup);
	super.init(curriculumGroup, courseGroup);
    }

    @Override
    protected void init(CurriculumGroup curriculumGroup, CourseGroup courseGroup, ExecutionSemester executionSemester) {
	checkInitConstraints((RootCurriculumGroup) curriculumGroup, (CycleCourseGroup) courseGroup);
	super.init(curriculumGroup, courseGroup, executionSemester);
    }

    private void checkInitConstraints(final RootCurriculumGroup rootCurriculumGroup, final CycleCourseGroup cycleCourseGroup) {
	if (rootCurriculumGroup.getCycleCurriculumGroup(cycleCourseGroup.getCycleType()) != null) {
	    throw new DomainException(
		    "error.studentCurriculum.RootCurriculumGroup.cycle.course.group.already.exists.in.curriculum",
		    cycleCourseGroup.getName());
	}
    }

    @Override
    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	if (curriculumGroup != null && !(curriculumGroup instanceof RootCurriculumGroup)) {
	    throw new DomainException("error.curriculumGroup.CycleParentCanOnlyBeRootCurriculumGroup");
	}
	super.setCurriculumGroup(curriculumGroup);
    }

    @Override
    public void setDegreeModule(DegreeModule degreeModule) {
	if (degreeModule != null && !(degreeModule instanceof CycleCourseGroup)) {
	    throw new DomainException("error.curriculumGroup.CycleParentDegreeModuleCanOnlyBeCycleCourseGroup");
	}
	super.setDegreeModule(degreeModule);
    }

    @Override
    public CycleCourseGroup getDegreeModule() {
	return (CycleCourseGroup) super.getDegreeModule();
    }

    @Override
    public boolean isCycleCurriculumGroup() {
	return true;
    }

    public boolean isCycle(final CycleType cycleType) {
	return getCycleType() == cycleType;
    }

    public boolean isFirstCycle() {
	return isCycle(CycleType.FIRST_CYCLE);
    }

    public CycleCourseGroup getCycleCourseGroup() {
	return (CycleCourseGroup) getDegreeModule();
    }

    public CycleType getCycleType() {
	return getCycleCourseGroup().getCycleType();
    }

    @Override
    public RootCurriculumGroup getCurriculumGroup() {
	return (RootCurriculumGroup) super.getCurriculumGroup();
    }

    @Override
    public void delete() {
	checkRulesToDelete();

	super.delete();
    }

    @Override
    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void deleteRecursive() {
	for (final CurriculumModule child : getCurriculumModules()) {
	    child.deleteRecursive();
	}

	super.delete();
    }

    private void checkRulesToDelete() {
	if (!getCurriculumGroup().getDegreeType().canRemoveEnrolmentIn(getCycleType())) {
	    throw new DomainException("error.studentCurriculum.CycleCurriculumGroup.degree.type.requires.this.cycle.to.exist",
		    getName().getContent());
	}

    }

    public boolean isExternal() {
	return false;
    }

    @Checked("RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    public void conclude() {
	if (isConclusionProcessed()) {
	    throw new DomainException("error.CycleCurriculumGroup.cycle.is.already.concluded", getCycleCourseGroup().getName());
	}
	if (!isConcluded()) {
	    throw new DomainException("error.CycleCurriculumGroup.cycle.is.not.concluded");
	}

	CycleConclusionProcess.conclude(new RegistrationConclusionBean(getRegistration(), this));
    }

    @Override
    public boolean isConcluded() {
	return isConclusionProcessed() || super.isConcluded();
    }

    @Override
    public ConclusionValue isConcluded(final ExecutionYear executionYear) {
	return isConclusionProcessed() && !executionYear.getBeginDateYearMonthDay().isBefore(getConclusionDate()) ? ConclusionValue.CONCLUDED
		: super.isConcluded(executionYear);
    }

    final public BigDecimal getAverage() {
	return isConclusionProcessed() ? getConclusionProcess().getAverage() : getAverage((ExecutionYear) null);
    }

    final public BigDecimal getAverage(final ExecutionYear executionYear) {
	return executionYear == null && isConcluded() && isConclusionProcessed() ? BigDecimal.valueOf(getFinalAverage())
		: getCurriculum(new DateTime(), executionYear).getAverage();
    }

    final public ExecutionYear getConclusionYear() {
	return isConclusionProcessed() ? getConclusionProcess().getConclusionYear() : null;
    }

    final public ExecutionYear calculateConclusionYear() {
	return getLastApprovementExecutionYear();
    }

    final public Integer getFinalAverage() {
	return isConclusionProcessed() ? getConclusionProcess().getFinalAverage() : null;
    }

    @Override
    final public Double getCreditsConcluded() {
	return isConclusionProcessed() ? getConclusionProcess().getCredits().doubleValue() : calculateCreditsConcluded();
    }

    final public Double calculateCreditsConcluded() {
	return super.getCreditsConcluded();
    }

    final public ExecutionYear getIngressionYear() {
	return isConclusionProcessed() ? getConclusionProcess().getIngressionYear() : calculateIngressionYear();
    }

    final public ExecutionYear calculateIngressionYear() {
	return getRegistration().calculateIngressionYear();
    }

    public boolean isConclusionProcessed() {
	return hasConclusionProcess();
    }

    final public YearMonthDay getConclusionDate() {
	return isConclusionProcessed() ? getConclusionProcess().getConclusionYearMonthDay() : null;
    }

    @Override
    public YearMonthDay calculateConclusionDate() {
	YearMonthDay result = super.calculateConclusionDate();

	if (getRegistration().getWasTransition()) {
	    final ExecutionSemester firstBolonhaTransitionExecutionPeriod = ExecutionSemester
		    .readFirstBolonhaTransitionExecutionPeriod();
	    final YearMonthDay begin = firstBolonhaTransitionExecutionPeriod.getBeginDateYearMonthDay();

	    if (result == null || result.isBefore(begin)) {
		result = begin;
	    }
	}

	return result;
    }

    final public Person getConclusionProcessResponsible() {
	return isConclusionProcessed() ? getConclusionProcess().getResponsible() : null;
    }

    final public Person getConclusionProcessLastResponsible() {
	return isConclusionProcessed() ? getConclusionProcess().getLastResponsible() : null;
    }

    final public String getConclusionProcessNotes() {
	return isConclusionProcessed() ? getConclusionProcess().getNotes() : null;
    }

    final public DateTime getConclusionProcessCreationDateTime() {
	return isConclusionProcessed() ? getConclusionProcess().getCreationDateTime() : null;
    }

    final public DateTime getConclusionProcessLastModificationDateTime() {
	return isConclusionProcessed() ? getConclusionProcess().getLastModificationDateTime() : null;
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void editConclusionInformation(final Integer finalAverage, final YearMonthDay conclusion, final String notes) {
	editConclusionInformation(AccessControl.getPerson(), finalAverage, conclusion, notes);
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void editConclusionInformation(final Person editor, final Integer finalAverage, final YearMonthDay conclusion,
	    final String notes) {
	if (!isConclusionProcessed()) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup.its.only.possible.to.edit.after.conclusion.process.has.been.performed");
	}

	check(finalAverage, "error.CycleCurriculumGroup.argument.must.not.be.null");
	check(conclusion, "error.CycleCurriculumGroup.argument.must.not.be.null");

	getConclusionProcess().update(editor, finalAverage, conclusion.toLocalDate(), notes);
    }

    public Double getCurrentDefaultEcts() {
	return getDegreeModule().getCurrentDefaultEcts();
    }

    public Double getDefaultEcts(final ExecutionYear executionYear) {
	return getDegreeModule().getDefaultEcts(executionYear);
    }

    @Override
    public CycleCurriculumGroup getParentCycleCurriculumGroup() {
        return this;
    }
}
