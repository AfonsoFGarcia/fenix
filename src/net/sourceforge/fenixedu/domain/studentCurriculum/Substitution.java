package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
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
    final public String getGivenGrade() {
	if (super.getGivenGrade() == null) {
	    BigDecimal result = BigDecimal.ZERO;
	    for (final IEnrolment enrolment : getIEnrolments()) {
		if (StringUtils.isNumeric(enrolment.getGradeValue())) {
		    result = result.add(new BigDecimal(enrolment.getGradeValue()));
		}
	    }
	    // FIXME: This is a temporary solution
	    if (result == BigDecimal.ZERO) {
		return null;
	    }

	    final BigDecimal grade = result.divide(new BigDecimal(getIEnrolments().size()), 2,
		    RoundingMode.HALF_UP);
	    
	    return grade.compareTo(BigDecimal.TEN) < 0 ? null : grade.toPlainString();
	    
	} else {
	    return super.getGivenGrade();
	}
    }

}
