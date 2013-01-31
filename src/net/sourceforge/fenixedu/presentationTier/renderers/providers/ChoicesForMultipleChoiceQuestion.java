package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ChoicesForMultipleChoiceQuestion implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		NewMultipleChoiceQuestion multipleChoiceQuestion = (NewMultipleChoiceQuestion) source;
		return multipleChoiceQuestion.getOrderedChoices(true);
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyArrayConverter();
	}

}
