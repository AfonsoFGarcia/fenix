package Dominio.precedences;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Dominio.IEnrollment;
import Util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionNotDoneCurricularCourse extends RestrictionByCurricularCourse implements
        IRestrictionByCurricularCourse {
    public RestrictionNotDoneCurricularCourse() {
        super();
    }

    public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext) {

        if (!precedenceContext.getStudentCurricularPlan().isCurricularCourseApproved(
                this.getPrecedentCurricularCourse())) {
            return CurricularCourseEnrollmentType.DEFINITIVE;
        }

        //        CurricularCourseEnrollmentType type =
        // precedenceContext.getStudentCurricularPlan()
        //                .getCurricularCourseEnrollmentType(this.getPrecedentCurricularCourse(),
        //                        precedenceContext.getExecutionPeriod());
        //        if (type.equals(CurricularCourseEnrollmentType.TEMPORARY)) {
        //            return CurricularCourseEnrollmentType.TEMPORARY;
        //        }

        List enrollmentsWithEnrolledStateInPreviousExecutionPeriod = precedenceContext
                .getStudentCurricularPlan().getAllStudentEnrolledEnrollmentsInExecutionPeriod(
                        precedenceContext.getExecutionPeriod().getPreviousExecutionPeriod());

        List result = (List) CollectionUtils.collect(
                enrollmentsWithEnrolledStateInPreviousExecutionPeriod, new Transformer() {
                    public Object transform(Object obj) {
                        IEnrollment enrollment = (IEnrollment) obj;
                        return enrollment.getCurricularCourse();
                    }
                });

        if (result.contains(this.getPrecedentCurricularCourse())) {
            return CurricularCourseEnrollmentType.TEMPORARY;
        }

        return CurricularCourseEnrollmentType.NOT_ALLOWED;
    }
}