package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Set;

import net.sourceforge.fenixedu.domain.vigilancy.strategies.StrategyFactory;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class StrategyProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {

		StrategyFactory factory = StrategyFactory.getInstance();
		Set<String> availableStrategies = factory.getAvailableStrategies();

		return new ArrayList<String>(factory.getAvailableStrategies());

	}

	@Override
	public Converter getConverter() {
		return new Converter() {

			@Override
			public Object convert(Class type, Object value) {

				return value;
			}
		};
	}

}