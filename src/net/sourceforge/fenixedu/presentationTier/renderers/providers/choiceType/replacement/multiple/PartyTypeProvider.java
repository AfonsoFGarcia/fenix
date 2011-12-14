package net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.multiple;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PartyTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
	return new ArrayList(RootDomainObject.getInstance().getPartyTypes());
    }

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }

}
