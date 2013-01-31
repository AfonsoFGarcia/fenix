package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisContextBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionYearsProviderForThesisContextBean implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		ThesisContextBean bean = (ThesisContextBean) source;
		return bean.getExecutionYearPossibilities();
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
