package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionPeriodsReverseOrderProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		final List<ExecutionSemester> executionSemesters =
				new ArrayList<ExecutionSemester>(RootDomainObject.getInstance().getExecutionPeriodsSet());
		Collections.sort(executionSemesters, ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR);
		Collections.reverse(executionSemesters);
		return executionSemesters;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
