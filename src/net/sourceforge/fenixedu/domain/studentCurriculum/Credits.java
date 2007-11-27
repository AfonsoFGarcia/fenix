package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedOptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;

public class Credits extends Credits_Base {

    public Credits() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Credits(StudentCurricularPlan studentCurricularPlan,
	    Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments,
	    ExecutionPeriod executionPeriod) {
	this();
	init(studentCurricularPlan, dismissals, enrolments, executionPeriod);
    }

    public Credits(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, Collection<IEnrolment> enrolments,
	    Collection<CurricularCourse> noEnrolCurricularCourses, Double credits, ExecutionPeriod executionPeriod) {
	this();
	init(studentCurricularPlan, courseGroup, enrolments, noEnrolCurricularCourses, credits, executionPeriod);
    }
    
    public Credits(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup, Collection<IEnrolment> enrolments,
	    Double credits, ExecutionPeriod executionPeriod) {
	this();
	init(studentCurricularPlan, curriculumGroup, enrolments, new HashSet<CurricularCourse>(0), credits, executionPeriod);
    }

    final protected void initExecutionPeriod(ExecutionPeriod executionPeriod) {
	if (executionPeriod == null) {
	    throw new DomainException("error.credits.wrong.arguments");
	}
	setExecutionPeriod(executionPeriod);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, Collection<IEnrolment> enrolments,
	    Collection<CurricularCourse> noEnrolCurricularCourses, Double credits, ExecutionPeriod executionPeriod) {
	if (studentCurricularPlan == null || courseGroup == null || credits == null) {
	    throw new DomainException("error.credits.wrong.arguments");
	}

	checkGivenCredits(studentCurricularPlan, courseGroup, credits);
	initExecutionPeriod(executionPeriod);

	setStudentCurricularPlan(studentCurricularPlan);
	setGivenCredits(credits);
	addEnrolments(enrolments);

	Dismissal.createNewDismissal(this, studentCurricularPlan, courseGroup, noEnrolCurricularCourses);
    }
    
    protected void init(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup, Collection<IEnrolment> enrolments,
	    Collection<CurricularCourse> noEnrolCurricularCourses, Double credits, ExecutionPeriod executionPeriod) {
	if (studentCurricularPlan == null || curriculumGroup == null || credits == null) {
	    throw new DomainException("error.credits.wrong.arguments");
	}

	initExecutionPeriod(executionPeriod);

	setStudentCurricularPlan(studentCurricularPlan);
	setGivenCredits(credits);
	addEnrolments(enrolments);

	Dismissal.createNewDismissal(this, studentCurricularPlan, curriculumGroup, noEnrolCurricularCourses);
    }


    private void checkGivenCredits(final StudentCurricularPlan studentCurricularPlan,
	    final CourseGroup courseGroup, final Double credits) {
	if (!allowsEctsCredits(studentCurricularPlan, courseGroup, ExecutionPeriod
		.readActualExecutionPeriod(), credits.doubleValue())) {
	    throw new DomainException("error.credits.invalid.credits", credits.toString());
	}
    }

    private boolean allowsEctsCredits(final StudentCurricularPlan studentCurricularPlan, final CourseGroup courseGroup,
	    final ExecutionPeriod executionPeriod, final double ectsCredits) {
	final double ectsCreditsForCourseGroup = studentCurricularPlan.getEctsCreditsForCourseGroup(courseGroup).doubleValue();
	if (ectsCredits + ectsCreditsForCourseGroup > courseGroup.getMaxEctsCredits(executionPeriod).doubleValue()) {
	    return false;
	}
	if (courseGroup.isRoot()) {
	    return true;
	}
	for (final Context context : courseGroup.getParentContexts()) {
	    if (context.isOpen(executionPeriod)) {
		if (allowsEctsCredits(studentCurricularPlan, context.getParentCourseGroup(), executionPeriod, ectsCredits)) {
		    return true;
		}
	    }
	}
	return false;
    }

    protected void init(StudentCurricularPlan studentCurricularPlan,
	    Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments,
	    ExecutionPeriod executionPeriod) {
	if (studentCurricularPlan == null || dismissals == null || dismissals.isEmpty()) {
	    throw new DomainException("error.credits.wrong.arguments");
	}

	initExecutionPeriod(executionPeriod);
	setStudentCurricularPlan(studentCurricularPlan);
	addEnrolments(enrolments);

	for (final SelectedCurricularCourse selectedCurricularCourse : dismissals) {
	    if (selectedCurricularCourse.isOptional()) {
		final SelectedOptionalCurricularCourse selectedOptionalCurricularCourse = (SelectedOptionalCurricularCourse) selectedCurricularCourse;
		Dismissal.createNewOptionalDismissal(this, studentCurricularPlan, selectedOptionalCurricularCourse
			.getCurriculumGroup(), selectedOptionalCurricularCourse.getCurricularCourse(),
			selectedOptionalCurricularCourse.getCredits());
	    } else {
		Dismissal.createNewDismissal(this, studentCurricularPlan, selectedCurricularCourse.getCurriculumGroup(),
			selectedCurricularCourse.getCurricularCourse());
	    }
	}
    }

    private void addEnrolments(final Collection<IEnrolment> enrolments) {
	if (enrolments != null) {
	    for (final IEnrolment enrolment : enrolments) {
		EnrolmentWrapper.create(this, enrolment);
	    }
	}
    }

    final public Collection<IEnrolment> getIEnrolments() {
	final Set<IEnrolment> result = new HashSet<IEnrolment>();
	for (final EnrolmentWrapper enrolmentWrapper : this.getEnrolmentsSet()) {
	    IEnrolment enrolment = enrolmentWrapper.getIEnrolment();
	    if(enrolment != null) {
		result.add(enrolmentWrapper.getIEnrolment());
	    }
	}
	return result;
    }
    
    final public boolean hasIEnrolments(final IEnrolment iEnrolment) {
	for (final EnrolmentWrapper enrolmentWrapper : this.getEnrolmentsSet()) {
	    if (enrolmentWrapper.getIEnrolment() == iEnrolment) {
		return true;
	    }
	}
	
	return false;
    }

    final public boolean hasAnyIEnrolments() {
	return hasAnyEnrolments();
    }

    @Override
    final public Double getGivenCredits() {
	if (super.getGivenCredits() == null) {
	    BigDecimal bigDecimal = BigDecimal.ZERO;
	    for (Dismissal dismissal : getDismissalsSet()) {
		bigDecimal = bigDecimal.add(new BigDecimal(dismissal.getEctsCredits()));
	    }
	    return Double.valueOf(bigDecimal.doubleValue());
	}
	return super.getGivenCredits();
    }

    public String getGivenGrade() {
	return null;
    }

    public Grade getGrade() {
	return null;
    }

    final public void delete() {
	removeStudentCurricularPlan();
	removeRootDomainObject();
	removeExecutionPeriod();

	for (; hasAnyDismissals(); getDismissals().get(0).delete())
	    ;

	for (; hasAnyEnrolments(); getEnrolments().get(0).delete())
	    ;

	super.deleteDomainObject();
    }

    final public Double getEnrolmentsEcts() {
	Double result = 0d;
	for (final IEnrolment enrolment : getIEnrolments()) {
	    result = result + enrolment.getEctsCredits();
	}
	return result;
    }

    final public boolean hasGivenCredits() {
	return getGivenCredits() != null;
    }

    final public boolean hasGivenCredits(final Double ectsCredits) {
	return hasGivenCredits() && getGivenCredits().equals(ectsCredits);
    }

    public boolean isTemporary() {
	return false;
    }

    public boolean isSubstitution() {
	return false;
    }

    public boolean isEquivalence() {
	return false;
    }

    @SuppressWarnings("unchecked")
    public Collection<ICurriculumEntry> getAverageEntries() {
	return Collections.EMPTY_SET;
    }

}
