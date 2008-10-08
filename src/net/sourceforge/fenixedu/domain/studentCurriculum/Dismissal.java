package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.enrolment.DismissalCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;

import org.joda.time.YearMonthDay;

public class Dismissal extends Dismissal_Base implements ICurriculumEntry {

    public Dismissal() {
	super();
    }

    public Dismissal(Credits credits, CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
	init(credits, curriculumGroup, curricularCourse);
    }

    protected void init(Credits credits, CurriculumGroup curriculumGroup) {
	if (credits == null || curriculumGroup == null) {
	    throw new DomainException("error.dismissal.wrong.arguments");
	}
	setCredits(credits);
	setCurriculumGroup(curriculumGroup);
    }

    protected void init(final Credits credits, final CurriculumGroup curriculumGroup, final CurricularCourse curricularCourse) {
	if (curricularCourse == null) {
	    throw new DomainException("error.dismissal.wrong.arguments");
	}
	checkCurriculumGroupCurricularCourse(credits, curriculumGroup, curricularCourse);
	init(credits, curriculumGroup);
	setCurricularCourse(curricularCourse);
    }

    private void checkCurriculumGroupCurricularCourse(final Credits credits, final CurriculumGroup curriculumGroup,
	    final CurricularCourse curricularCourse) {
	if (!(curriculumGroup instanceof NoCourseGroupCurriculumGroup)) {
	    if (!curriculumGroup.getCurricularCoursesToDismissal(credits.getExecutionPeriod()).contains(curricularCourse)) {
		throw new DomainException("error.dismissal.invalid.curricular.course.to.dismissal");
	    }
	}
    }

    static protected Dismissal createNewDismissal(final Credits credits, final StudentCurricularPlan studentCurricularPlan,
	    final CourseGroup courseGroup, final Collection<CurricularCourse> noEnrolCurricularCourses) {
	return new CreditsDismissal(credits, findCurriculumGroupForCourseGroup(studentCurricularPlan, courseGroup),
		noEnrolCurricularCourses);
    }

    static protected Dismissal createNewDismissal(final Credits credits, final StudentCurricularPlan studentCurricularPlan,
	    final CurriculumGroup curriculumGroup, final Collection<CurricularCourse> noEnrolCurricularCourses) {
	return new CreditsDismissal(credits, curriculumGroup, noEnrolCurricularCourses);
    }

    static private CurriculumGroup findCurriculumGroupForCourseGroup(final StudentCurricularPlan studentCurricularPlan,
	    final CourseGroup courseGroup) {
	final CurriculumGroup curriculumGroup = studentCurricularPlan.findCurriculumGroupFor(courseGroup);
	if (curriculumGroup != null) {
	    return curriculumGroup;
	}
	return getOrCreateExtraCurricularCurriculumGroup(studentCurricularPlan);
    }

    static private NoCourseGroupCurriculumGroup getOrCreateExtraCurricularCurriculumGroup(
	    final StudentCurricularPlan studentCurricularPlan) {
	final NoCourseGroupCurriculumGroup result = studentCurricularPlan
		.getNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR);
	return (result == null) ? studentCurricularPlan
		.createNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR) : result;
    }

    static protected Dismissal createNewDismissal(final Credits credits, final StudentCurricularPlan studentCurricularPlan,
	    CurriculumGroup curriculumGroup, final CurricularCourse curricularCourse) {
	return new Dismissal(credits, curriculumGroup, curricularCourse);
    }

    static protected Dismissal createNewOptionalDismissal(final Credits credits,
	    final StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    final OptionalCurricularCourse optionalCurricularCourse, final Double ectsCredits) {
	return new OptionalDismissal(credits, curriculumGroup, optionalCurricularCourse, ectsCredits);
    }

    static protected OptionalDismissal createNewOptionalDismissal(final Credits credits,
	    final StudentCurricularPlan studentCurricularPlan, final OptionalCurricularCourse optionalCurricularCourse,
	    final Double ectsCredits) {
	return new OptionalDismissal(credits, findCurriculumGroupForCurricularCourse(studentCurricularPlan,
		optionalCurricularCourse), optionalCurricularCourse, ectsCredits);
    }

    static private CurriculumGroup findCurriculumGroupForCurricularCourse(final StudentCurricularPlan studentCurricularPlan,
	    final CurricularCourse curricularCourse) {
	final Set<CurriculumGroup> curriculumGroups = new HashSet<CurriculumGroup>(curricularCourse.getParentContextsCount());
	for (final Context context : curricularCourse.getParentContexts()) {
	    final CurriculumGroup curriculumGroup = studentCurricularPlan.findCurriculumGroupFor(context.getParentCourseGroup());
	    if (curriculumGroup != null && !curriculumGroup.parentCurriculumGroupIsNoCourseGroupCurriculumGroup()) {
		curriculumGroups.add(curriculumGroup);
	    }
	}
	return curriculumGroups.size() == 1 ? curriculumGroups.iterator().next()
		: getOrCreateExtraCurricularCurriculumGroup(studentCurricularPlan);
    }

    @Override
    public StringBuilder print(String tabs) {
	final StringBuilder builder = new StringBuilder();
	builder.append(tabs);
	builder.append("[D ").append(hasDegreeModule() ? getDegreeModule().getName() : "").append(" ");
	builder.append(getEctsCredits()).append(" ects ]\n");
	return builder;
    }

    @Override
    public boolean isApproved() {
	return true;
    }

    @Override
    final public ExecutionYear getIEnrolmentsLastExecutionYear() {
	ExecutionYear result = null;

	for (final IEnrolment iEnrolment : this.getSourceIEnrolments()) {
	    final ExecutionYear executionYear = iEnrolment.getExecutionYear();
	    if (result == null || result.isBefore(executionYear)) {
		result = executionYear;
	    }
	}

	return result;
    }

    public Collection<IEnrolment> getSourceIEnrolments() {
	return getCredits().getIEnrolments();
    }

    final public boolean hasSourceIEnrolments(final IEnrolment iEnrolment) {
	return getCredits().hasIEnrolments(iEnrolment);
    }

    @Override
    public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
	return (executionSemester == null || !hasExecutionPeriod() || getExecutionPeriod().isBeforeOrEquals(executionSemester))
		&& hasCurricularCourse(getCurricularCourse(), curricularCourse, executionSemester);
    }

    @Override
    public Double getEctsCredits() {
	// FIXME must migrate Dismissal with optional curricular courses to
	// OptionalDismissal
	return getCurricularCourse().isOptionalCurricularCourse() ? getEnrolmentsEcts() : getCurricularCourse().getEctsCredits(
		getExecutionPeriod());
    }

    final public BigDecimal getEctsCreditsForCurriculum() {
	return BigDecimal.valueOf(getEctsCredits());
    }

    protected Double getEnrolmentsEcts() {
	return getCredits().getEnrolmentsEcts();
    }

    @Override
    public Double getAprovedEctsCredits() {
	return isExtraCurricular() ? Double.valueOf(0d) : getEctsCredits();
    }

    @Override
    public Double getEnroledEctsCredits(final ExecutionSemester executionSemester) {
	return Double.valueOf(0d);
    }

    @Override
    public void collectDismissals(final List<Dismissal> result) {
	result.add(this);
    }

    @Override
    public boolean isDismissal() {
	return true;
    }

    public boolean isSimilar(final Dismissal dismissal) {
	return hasSameDegreeModules(dismissal) && hasSameSourceIEnrolments(getSourceIEnrolments(), dismissal);
    }

    protected boolean hasSameDegreeModules(final Dismissal dismissal) {
	return (getDegreeModule() == dismissal.getDegreeModule() || getCurricularCourse().isEquivalent(
		dismissal.getCurricularCourse()));
    }

    protected boolean hasSameSourceIEnrolments(final Collection<IEnrolment> ienrolments, final Dismissal dismissal) {
	return ienrolments.containsAll(dismissal.getSourceIEnrolments())
		&& ienrolments.size() == dismissal.getSourceIEnrolments().size();
    }

    @Override
    public Dismissal getDismissal(final CurricularCourse curricularCourse) {
	return (getCurricularCourse() == curricularCourse) ? this : null;
    }

    @Override
    public void delete() {
	final Credits credits = getCredits();
	removeCredits();
	if (credits != null && !credits.hasAnyDismissals()) {
	    credits.delete();
	}
	super.delete();
    }

    @Override
    public ConclusionValue isConcluded(final ExecutionYear executionYear) {
	return ConclusionValue.create(executionYear == null || !hasExecutionPeriod()
		|| getExecutionPeriod().getExecutionYear().isBeforeOrEquals(executionYear));
    }

    @Override
    public Double getCreditsConcluded(ExecutionYear executionYear) {
	return isConcluded(executionYear).value() && !getCredits().isTemporary() ? getEctsCredits() : Double.valueOf(0d);
    }

    @Override
    public YearMonthDay calculateConclusionDate() {
	final SortedSet<IEnrolment> iEnrolments = new TreeSet<IEnrolment>(IEnrolment.COMPARATOR_BY_APPROVEMENT_DATE);
	iEnrolments.addAll(getSourceIEnrolments());

	final YearMonthDay beginDate = getExecutionPeriod().getBeginDateYearMonthDay();
	if (!iEnrolments.isEmpty()) {
	    final IEnrolment enrolment = iEnrolments.last();
	    final YearMonthDay approvementDate = enrolment.getApprovementDate();
	    return approvementDate != null ? approvementDate : beginDate;
	} else {
	    return beginDate;
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public Curriculum getCurriculum(final ExecutionYear year) {
	if ((year == null || !hasExecutionPeriod() || getExecutionYear().isBeforeOrEquals(year))) {

	    final Collection<ICurriculumEntry> averageEntries = getAverageEntries(year);
	    if (!averageEntries.isEmpty()) {
		return new Curriculum(this, year, Collections.EMPTY_SET, averageEntries, Collections
			.singleton((ICurriculumEntry) this));
	    }

	}

	return Curriculum.createEmpty(this, year);
    }

    private Collection<ICurriculumEntry> getAverageEntries(final ExecutionYear year) {
	if (getCredits().isEquivalence() && (year == null || !hasExecutionPeriod() || getExecutionYear().isBefore(year))) {
	    return Collections.singleton((ICurriculumEntry) this);
	}

	return getCredits().getAverageEntries(year);
    }

    public Grade getGrade() {
	return getCredits().isEquivalence() ? getCredits().getGrade() : Grade.createEmptyGrade();
    }

    public String getGradeValue() {
	return getGrade().getValue();
    }

    public Double getWeigth() {
	return getCredits().isEquivalence() ? getEctsCredits() : null;
    }

    final public BigDecimal getWeigthForCurriculum() {
	return BigDecimal.valueOf(getWeigth());
    }

    public BigDecimal getWeigthTimesGrade() {
	return getCredits().isEquivalence() && getGrade().isNumeric() ? getWeigthForCurriculum().multiply(
		getGrade().getNumericValue()) : BigDecimal.ZERO;
    }

    public String getCode() {
	return hasCurricularCourse() ? getCurricularCourse().getCode() : null;
    }

    @Override
    public ExecutionSemester getExecutionPeriod() {
	return getCredits().getExecutionPeriod();
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(ExecutionSemester executionSemester) {
	if (executionSemester != null && executionSemester != getExecutionPeriod()) {
	    return Collections.EMPTY_SET;
	}

	final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>(1);
	result.add(new DismissalCurriculumModuleWrapper(this, executionSemester));
	return result;
    }

}
