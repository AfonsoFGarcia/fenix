package net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.multiple;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.assiduousness.util.DayType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumArrayConverter;

public class DayTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
	return Arrays.asList(DayType.values());
    }

    @Override
    public Converter getConverter() {
	return new EnumArrayConverter(DayType.class);
    }

}
