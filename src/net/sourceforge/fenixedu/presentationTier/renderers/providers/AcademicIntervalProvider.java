package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.AcademicIntervalConverter;

import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AcademicIntervalProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return new AcademicIntervalConverter();
	}

	@Override
	public Object provide(Object source, Object current) {
		List<AcademicInterval> result = AcademicInterval.readAcademicIntervals(AcademicPeriod.SEMESTER);
		Collections.sort(result, new ReverseComparator(AcademicInterval.COMPARATOR_BY_BEGIN_DATE));
		return result;
	}
}
