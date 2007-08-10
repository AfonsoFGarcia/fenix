package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Substitution extends Substitution_Base {

    public Substitution() {
	super();
    }

    public Substitution(final StudentCurricularPlan studentCurricularPlan,
	    final Collection<SelectedCurricularCourse> dismissals,
	    final Collection<IEnrolment> enrolments, ExecutionPeriod executionPeriod) {

	init(studentCurricularPlan, dismissals, enrolments, executionPeriod);
    }

    @Override
    final protected void init(final StudentCurricularPlan studentCurricularPlan,
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
	return super.getGrade() != null ? super.getGrade() : getEnrolmentsAverageGrade();
    }

    private Grade getEnrolmentsAverageGrade() {
	return null;
    }
}
