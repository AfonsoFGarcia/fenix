package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import org.joda.time.YearMonthDay;

abstract public class CurriculumLine extends CurriculumLine_Base {

    static final public Comparator<CurriculumLine> COMPARATOR_BY_APPROVEMENT_DATE_AND_ID = new Comparator<CurriculumLine>() {
	public int compare(CurriculumLine o1, CurriculumLine o2) {
	    int result = o1.getApprovementDate().compareTo(o2.getApprovementDate());
	    return result == 0 ? COMPARATOR_BY_ID.compare(o1, o2) : result;
	}
    };

    public CurriculumLine() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCreatedBy(getCurrentUser());
    }

    final public ExecutionYear getExecutionYear() {
	final ExecutionSemester executionSemester = getExecutionPeriod();
	return executionSemester == null ? null : executionSemester.getExecutionYear();
    }

    public YearMonthDay getApprovementDate() {
	return isApproved() ? calculateConclusionDate() : null;
    }

    @Override
    final public boolean isLeaf() {
	return true;
    }

    @Override
    final public boolean isRoot() {
	return false;
    }

    @Override
    public boolean isApproved(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
	return false;
    }

    @Override
    public boolean isEnroledInExecutionPeriod(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
	return false;
    }

    @Override
    public boolean isPropaedeutic() {
	return getCurriculumGroup().isPropaedeutic();
    }

    public boolean isExtraCurricular() {
	return getCurriculumGroup().isExtraCurriculum();
    }

    final protected void validateDegreeModuleLink(CurriculumGroup curriculumGroup, CurricularCourse curricularCourse) {
	if (!curriculumGroup.getDegreeModule().validate(curricularCourse)) {
	    throw new DomainException("error.studentCurriculum.curriculumLine.invalid.curriculum.group");
	}
    }

    @Override
    public List<Enrolment> getEnrolments() {
	return Collections.emptyList();
    }

    @Override
    public boolean hasAnyEnrolments() {
	return false;
    }

    @Override
    final public void addApprovedCurriculumLines(final Collection<CurriculumLine> result) {
	if (isApproved()) {
	    result.add(this);
	}
    }

    @Override
    final public boolean hasAnyApprovedCurriculumLines() {
	return isApproved();
    }

    @Override
    public void collectDismissals(final List<Dismissal> result) {
	// nothing to do
    }

    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
	return hasCurriculumGroup() ? getCurriculumGroup().getStudentCurricularPlan() : null;
    }

    @Override
    public boolean hasEnrolmentWithEnroledState(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
	return false;
    }

    @Override
    public ExecutionYear getIEnrolmentsLastExecutionYear() {
	return getExecutionYear();
    }

    final public CurricularCourse getCurricularCourse() {
	return (CurricularCourse) getDegreeModule();
    }

    final public void setCurricularCourse(CurricularCourse curricularCourse) {
	setDegreeModule(curricularCourse);
    }

    @Override
    public void setDegreeModule(final DegreeModule degreeModule) {
	if (degreeModule != null && !(degreeModule instanceof CurricularCourse)) {
	    throw new DomainException("error.curriculumLine.DegreeModuleCanOnlyBeCurricularCourse");
	}
	super.setDegreeModule(degreeModule);
    }

    final public boolean hasCurricularCourse() {
	return hasDegreeModule();
    }

    @Override
    public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
	return null;
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(ExecutionSemester executionSemester) {
	return Collections.emptySet();
    }

    @Override
    public Enrolment getApprovedEnrolment(final CurricularCourse curricularCourse) {
	return null;
    }

    @Override
    public Dismissal getDismissal(final CurricularCourse curricularCourse) {
	return null;
    }

    @Override
    public Collection<Enrolment> getSpecialSeasonEnrolments(ExecutionYear executionYear) {
	return Collections.emptySet();
    }

    @Override
    final public void getAllDegreeModules(final Collection<DegreeModule> degreeModules) {
	degreeModules.add(getDegreeModule());
    }

    @Override
    public Set<CurriculumLine> getAllCurriculumLines() {
	return Collections.singleton(this);
    }

    @Override
    public MultiLanguageString getName() {
	ExecutionSemester period = getExecutionPeriod();
	CurricularCourse course = getCurricularCourse();
	return MultiLanguageString.i18n().nadd("pt", course.getName(period)).nadd("en", course.getNameEn(period)).finish();
    }

    public boolean hasExecutionPeriod() {
	return getExecutionPeriod() != null;
    }

    protected boolean hasCurricularCourse(final CurricularCourse own, final CurricularCourse other,
	    final ExecutionSemester executionSemester) {
	return own.isEquivalent(other) || hasCurricularCourseEquivalence(own, other, executionSemester);
    }

    private boolean hasCurricularCourseEquivalence(final CurricularCourse sourceCurricularCourse,
	    final CurricularCourse equivalentCurricularCourse, final ExecutionSemester executionSemester) {
	for (final CurricularCourseEquivalence curricularCourseEquivalence : sourceCurricularCourse
		.getCurricularCourseEquivalencesFor(equivalentCurricularCourse)) {
	    if (oldCurricularCoursesAreApproved(curricularCourseEquivalence, executionSemester)) {
		return true;
	    }
	}
	return false;
    }

    private boolean oldCurricularCoursesAreApproved(final CurricularCourseEquivalence curricularCourseEquivalence,
	    final ExecutionSemester executionSemester) {
	for (final CurricularCourse curricularCourse : curricularCourseEquivalence.getOldCurricularCourses()) {
	    if (!getStudentCurricularPlan().isApproved(curricularCourse, executionSemester)) {
		return false;
	    }
	}
	return true;
    }

    @Override
    public boolean hasConcluded(final DegreeModule degreeModule, final ExecutionYear executionYear) {
	return getDegreeModule() == degreeModule && isConcluded(executionYear).value();
    }

    @Override
    public CurriculumLine getApprovedCurriculumLine(CurricularCourse curricularCourse) {
	return isApproved(curricularCourse) ? this : null;
    }

    @Override
    protected String getCurrentUser() {
	return AccessControl.getUserView() != null ? AccessControl.getUserView().getUtilizador() : null;
    }

    abstract public boolean isApproved();

    abstract public ExecutionSemester getExecutionPeriod();

}
