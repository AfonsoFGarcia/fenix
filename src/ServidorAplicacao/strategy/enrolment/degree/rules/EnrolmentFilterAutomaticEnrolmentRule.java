package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import Util.ScopeType;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */

public class EnrolmentFilterAutomaticEnrolmentRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {

		//	Anual curricular course filter
		List finalListToRemove = new ArrayList();

		Iterator iteratorAnualScopes = enrolmentContext.getCurricularCoursesScopesEnroledByStudent().iterator();
		List anualCurricularCourseScopes = new ArrayList();
		while (iteratorAnualScopes.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorAnualScopes.next();
			if ((curricularCourseScope.getBranch().equals(enrolmentContext.getStudentActiveCurricularPlan().getBranch()))
				|| (curricularCourseScope.getBranch().getName().equals(""))) {
				if (curricularCourseScope.getCurricularSemester().getSemester().equals(enrolmentContext.getSemester())) {
					anualCurricularCourseScopes.add(curricularCourseScope);
				}
			}
		}
		enrolmentContext.getCurricularCoursesScopesEnroledByStudent().clear();
		enrolmentContext.getCurricularCoursesScopesEnroledByStudent().addAll(anualCurricularCourseScopes);

		while(!anualCurricularCourseScopes.isEmpty()) {
			final ICurricularCourseScope curricularCourseScope1 = (ICurricularCourseScope) anualCurricularCourseScopes.get(0);
			List aux = (List) CollectionUtils.select(anualCurricularCourseScopes, new Predicate() {
				public boolean evaluate(Object obj) {
					ICurricularCourseScope curricularCourseScope2 = (ICurricularCourseScope) obj;
					return curricularCourseScope2.getCurricularCourse().equals(curricularCourseScope1.getCurricularCourse());
				}
			});

			anualCurricularCourseScopes.removeAll(aux);

			int year = 10; // este valor importa que seja maior que o numero maximo de anos curriculares dos cursos.
			int index = 0;
			Iterator iterator = aux.iterator();
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
				if (curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue() < year) {
					year = curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue();
					index = aux.indexOf(curricularCourseScope);
				}
			}
			aux.remove(index);
			finalListToRemove.addAll(aux);
			aux.clear();
		}
		enrolmentContext.getCurricularCoursesScopesEnroledByStudent().removeAll(finalListToRemove);
				
		// ---------------------------------------------------------------------------- //

final Integer semester = enrolmentContext.getSemester();
		//	Alternative curricular course filter
		List alternativeSemesterScopes =
			(List) CollectionUtils.select(enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled(), new Predicate() {
			public boolean evaluate(Object obj) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) obj;
				return( (curricularCourseScope.getScopeType().equals(new ScopeType(ScopeType.MANDATORY))) &&
								(curricularCourseScope.getCurricularSemester().getSemester().equals(semester)));
			}
		});

//		if (enrolmentContext.getSemester().intValue() == 1) {
//			enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().removeAll(alternativeSemesterScopes);
//		} else {
			enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().removeAll(alternativeSemesterScopes);

			Iterator iteratorAlternative = alternativeSemesterScopes.iterator();
			while (iteratorAlternative.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorAlternative.next();
				if ((curricularCourseScope.getBranch().equals(enrolmentContext.getStudentActiveCurricularPlan().getBranch()))
					|| (curricularCourseScope.getBranch().getName().equals(""))) {
					enrolmentContext.getCurricularCoursesScopesEnroledByStudent().add(curricularCourseScope);
				}
			}
//		}

		return enrolmentContext;
	}
}