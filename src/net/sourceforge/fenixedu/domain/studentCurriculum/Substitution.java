package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;

public class Substitution extends Substitution_Base {

    public Substitution() {
	super();
    }

    public Substitution(final StudentCurricularPlan studentCurricularPlan,
	    final Collection<SelectedCurricularCourse> dismissals,
	    final Collection<IEnrolment> enrolments, ExecutionPeriod executionPeriod) {
	this();
	init(studentCurricularPlan, dismissals, enrolments, executionPeriod);
    }
    
    public Substitution(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
	    Collection<IEnrolment> enrolments, Collection<CurricularCourse> noEnrolCurricularCourses, 
	    Double credits, ExecutionPeriod executionPeriod) {
	this();
	init(studentCurricularPlan, courseGroup, enrolments, noEnrolCurricularCourses, credits, executionPeriod);
    }

    public Substitution(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    Collection<IEnrolment> enrolments, Double credits, ExecutionPeriod executionPeriod) {
	this();
	init(studentCurricularPlan, curriculumGroup, enrolments, new HashSet<CurricularCourse>(0), credits, executionPeriod);
    }

    @Override
    protected void init(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, Collection<IEnrolment> enrolments,
	    Collection<CurricularCourse> noEnrolCurricularCourses, Double credits, ExecutionPeriod executionPeriod) {
	if (enrolments == null || enrolments.isEmpty()) {
	    throw new DomainException("error.substitution.wrong.arguments");
	}
        super.init(studentCurricularPlan, courseGroup, enrolments, noEnrolCurricularCourses, credits, executionPeriod);
    }

    @Override
    protected void init(final StudentCurricularPlan studentCurricularPlan,
	    final Collection<SelectedCurricularCourse> dismissals,
	    final Collection<IEnrolment> enrolments, ExecutionPeriod executionPeriod) {

	if (enrolments == null || enrolments.isEmpty()) {
	    throw new DomainException("error.substitution.wrong.arguments");
	}
	super.init(studentCurricularPlan, dismissals, enrolments, executionPeriod);
    }

    @Override
    final public boolean isSubstitution() {
	return true;
    }

    @Override
    public boolean isEquivalence() {
        return false;
    }
    
    @Override
    public Grade getGrade() {
	return getEnrolmentsAverageGrade();
    }

    private Grade getEnrolmentsAverageGrade() {
	return null;
    }
    
    @Override
    @jvstm.cps.ConsistencyPredicate
    protected boolean checkGrade() {
        return true;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Collection<ICurriculumEntry> getAverageEntries() {
	return new HashSet<ICurriculumEntry>(getIEnrolments());
    }

}
