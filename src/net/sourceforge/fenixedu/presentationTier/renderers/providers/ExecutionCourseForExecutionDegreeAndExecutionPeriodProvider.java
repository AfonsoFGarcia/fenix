package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionDegree;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ExecutionCourseForExecutionDegreeAndExecutionPeriodProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();

	final HasExecutionPeriod hasExecutionPeriod = (HasExecutionPeriod) source;
	final ExecutionPeriod executionPeriod = hasExecutionPeriod.getExecutionPeriod();

	final HasExecutionDegree hasExecutionDegree = (HasExecutionDegree) source;
	final ExecutionDegree executionDegree = hasExecutionDegree.getExecutionDegree();
	final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

	if (executionPeriod != null && executionDegree != null) {
	    for (final ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCoursesSet()) {
		if (matches(executionCourse, degreeCurricularPlan)) {
		    executionCourses.add(executionCourse);
		}
	    }
	}

	Collections.sort(executionCourses, ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);

	return executionCourses;
    }

    private boolean matches(final ExecutionCourse executionCourse, final DegreeCurricularPlan degreeCurricularPlan) {
	for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
	    if (curricularCourse.getDegreeCurricularPlan() == degreeCurricularPlan) {
		return true;
	    }
	}
	return false;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
