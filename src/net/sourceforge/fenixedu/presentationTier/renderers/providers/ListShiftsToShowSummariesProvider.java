package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.ShowSummariesBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ListShiftsToShowSummariesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	ShowSummariesBean bean = (ShowSummariesBean) source;
	ExecutionCourse executionCourse = bean.getExecutionCourse();
	Set<Shift> shifts = new TreeSet<Shift>(Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
	if (executionCourse != null) {
	    shifts.addAll(executionCourse.getAssociatedShifts());
	}
	return shifts;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
