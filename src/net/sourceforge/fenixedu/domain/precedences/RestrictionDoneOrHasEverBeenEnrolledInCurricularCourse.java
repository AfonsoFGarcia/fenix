package net.sourceforge.fenixedu.domain.precedences;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse extends RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse_Base {
    public RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse() {
        super();
    }
	
	public RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse(Integer number, IPrecedence precedence, ICurricularCourse precedentCurricularCourse) {
		super();
		
		setPrecedence(precedence);
        setPrecedentCurricularCourse(precedentCurricularCourse);
	}
	

    public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext) {
        ICurricularCourse curricularCourse = this.getPrecedentCurricularCourse();
        CurricularCourseEnrollmentType result1 = null;
        CurricularCourseEnrollmentType result2 = null;

        if (precedenceContext.getStudentCurricularPlan().getCurricularCourseAcumulatedEnrollments(
                curricularCourse).intValue() > curricularCourse
                .getMinimumValueForAcumulatedEnrollments().intValue()) {
            result1 = CurricularCourseEnrollmentType.DEFINITIVE;
        } else {
            result1 = CurricularCourseEnrollmentType.NOT_ALLOWED;
        }

        result2 = super.evaluate(precedenceContext);

        return result2.or(result1);
    }
}