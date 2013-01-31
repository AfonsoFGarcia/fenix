package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularCourseFunctor;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionYear;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionDegreesWithDissertationByExecutionYearProvider implements DataProvider {

	private static class HasDissertationPredicate implements CurricularCourseFunctor {

		boolean hasDissertation = false;

		@Override
		public void doWith(final CurricularCourse curricularCourse) {
			if (curricularCourse.isDissertation()) {
				hasDissertation = true;
			}
		}

		@Override
		public boolean keepDoing() {
			return !hasDissertation;
		}

	}

	@Override
	public Object provide(Object source, Object currentValue) {
		final HasExecutionYear hasExecutionYear = (HasExecutionYear) source;
		final Set<ExecutionDegree> executionDegrees =
				new TreeSet<ExecutionDegree>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
		final ExecutionYear executionYear = hasExecutionYear.getExecutionYear();
		if (executionYear != null) {
			for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
				if (!executionDegree.getDegreeCurricularPlan().isEmpty() && hasDissertation(executionDegree)) {
					executionDegrees.add(executionDegree);
				}
			}
		}
		return executionDegrees;
	}

	private boolean hasDissertation(final ExecutionDegree executionDegree) {
		final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
		final HasDissertationPredicate hasDissertationPredicate = new HasDissertationPredicate();
		degreeCurricularPlan.doForAllCurricularCourses(hasDissertationPredicate);
		return hasDissertationPredicate.hasDissertation;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
