package net.sourceforge.fenixedu.domain.executionCourse;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class ExecutionCourseSearchBean implements Serializable {

	private ExecutionSemester executionPeriodDomainReference;
	private ExecutionDegree executionDegreeDomainReference;

	public ExecutionCourseSearchBean() {
		super();
	}

	public ExecutionDegree getExecutionDegree() {
		return executionDegreeDomainReference;
	}

	public void setExecutionDegree(ExecutionDegree executionDegree) {
		this.executionDegreeDomainReference = executionDegree;
	}

	public ExecutionSemester getExecutionPeriod() {
		return executionPeriodDomainReference;
	}

	public void setExecutionPeriod(ExecutionSemester executionSemester) {
		this.executionPeriodDomainReference = executionSemester;
	}

	public Collection<ExecutionCourse> search(final Collection<ExecutionCourse> result) {
		final ExecutionSemester executionSemester = getExecutionPeriod();
		final ExecutionDegree executionDegree = getExecutionDegree();
		if (executionSemester == null || executionDegree == null) {
			return null;
		}
		for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
			if (matchesCriteria(executionSemester, executionDegree, executionCourse)) {
				result.add(executionCourse);
			}
		}
		return result;
	}

	public Collection<ExecutionCourse> search() {
		final Set<ExecutionCourse> executionCourses =
				new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
		return search(executionCourses);

	}

	protected boolean matchesCriteria(final ExecutionSemester executionSemester, final ExecutionDegree executionDegree,
			final ExecutionCourse executionCourse) {
		return matchExecutionPeriod(executionSemester, executionCourse) && matchExecutionDegree(executionDegree, executionCourse);
	}

	private boolean matchExecutionPeriod(final ExecutionSemester executionSemester, final ExecutionCourse executionCourse) {
		return executionSemester == executionCourse.getExecutionPeriod();
	}

	private boolean matchExecutionDegree(final ExecutionDegree executionDegree, final ExecutionCourse executionCourse) {
		for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
			final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
			if (degreeCurricularPlan == executionDegree.getDegreeCurricularPlan()) {
				return true;
			}
		}
		return false;
	}

}
