package Dominio.precedences;

import Util.enrollment.CurricularCourseEnrollmentType;
import Dominio.ICurricularCourse;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse extends
	RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse implements IRestrictionByCurricularCourse
{
	public RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse()
	{
		super();
	}

//	public boolean evaluate(PrecedenceContext precedenceContext)
//	{
//		ICurricularCourse curricularCourse = super.getPrecedentCurricularCourse();
//		return (precedenceContext.getStudentCurricularPlan().isCurricularCourseEnrolled(curricularCourse) || super
//			.evaluate(precedenceContext));
//	}

	public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext)
	{
		ICurricularCourse curricularCourse = this.getPrecedentCurricularCourse();
		CurricularCourseEnrollmentType result1 = null;
		CurricularCourseEnrollmentType result2 = null;

        if (precedenceContext.getStudentCurricularPlan().isCurricularCourseEnrolled(curricularCourse)) {
            result1 = CurricularCourseEnrollmentType.DEFINITIVE;
        } else {
            result1 = CurricularCourseEnrollmentType.NOT_ALLOWED;
        }

        result2 = super.evaluate(precedenceContext);
        
        return result1.or(result2);
	}
}