package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.ImportContentBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionDegreesToImportLessonPlanningsProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		ImportContentBean bean = (ImportContentBean) source;
		ExecutionSemester executionSemester = bean.getExecutionPeriod();
		if (executionSemester != null) {
			List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();
			executionDegrees.addAll(executionSemester.getExecutionYear().getExecutionDegrees());
			Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
			return executionDegrees;
		}
		return new ArrayList<ExecutionDegree>();
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}
}
