package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.Iterator;

import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import Util.CurricularCourseType;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */

public class EnrolmentFilterLEQTrainingCourseRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {
		ICurricularCourseScope curricularCourseScopeToRemove = null;
		Iterator iterator = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
		while(iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			if(curricularCourseScope.getCurricularCourse().getType().equals(CurricularCourseType.TRAINING_COURSE_OBJ)) {
				curricularCourseScopeToRemove = curricularCourseScope;
				break;
			}
		}
		if(curricularCourseScopeToRemove != null) {
			enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().remove(curricularCourseScopeToRemove);
		}
		return enrolmentContext;
	}
}