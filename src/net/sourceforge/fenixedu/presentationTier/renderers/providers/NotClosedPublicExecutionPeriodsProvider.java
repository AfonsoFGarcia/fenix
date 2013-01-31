package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class NotClosedPublicExecutionPeriodsProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		return ExecutionSemester.readNotClosedPublicExecutionPeriods();
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}
}
