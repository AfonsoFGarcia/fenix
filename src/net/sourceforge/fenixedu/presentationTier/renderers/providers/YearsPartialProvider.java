package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class YearsPartialProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {

		List<Partial> result = new ArrayList<Partial>();
		ExecutionYear firstExecutionYear = ExecutionYear.readFirstExecutionYear();

		if (firstExecutionYear != null) {
			ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
			int firstYear = firstExecutionYear.getBeginDateYearMonthDay().getYear();
			int lastYear = currentExecutionYear.getEndDateYearMonthDay().getYear();
			while (firstYear <= lastYear) {
				result.add(new Partial(DateTimeFieldType.year(), firstYear));
				firstYear++;
			}
		}

		return result;
	}

	@Override
	public Converter getConverter() {
		return null;
	}
}