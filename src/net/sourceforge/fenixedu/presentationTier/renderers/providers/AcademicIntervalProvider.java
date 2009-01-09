package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AcademicIntervalProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object current) {
	List<AcademicInterval> result = AcademicInterval.readAcademicIntervals(AcademicPeriod.SEMESTER);
	Collections.sort(result, AcademicInterval.COMPARATOR_BY_BEGIN_DATE);
	return result;
    }
}
