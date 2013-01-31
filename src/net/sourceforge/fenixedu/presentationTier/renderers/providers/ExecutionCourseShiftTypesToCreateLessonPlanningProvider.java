package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.HashSet;

import net.sourceforge.fenixedu.dataTransferObject.gesdis.CreateLessonPlanningBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ShiftType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class ExecutionCourseShiftTypesToCreateLessonPlanningProvider implements DataProvider {

	public ExecutionCourse getExecutionCourse(Object source) {
		return ((CreateLessonPlanningBean) source).getExecutionCourse();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
		ExecutionCourse executionCourse = getExecutionCourse(source);
		return (getExecutionCourse(source) != null) ? executionCourse.getShiftTypes() : new HashSet<ShiftType>();
	}

	@Override
	public Converter getConverter() {
		return new EnumConverter();
	}
}
