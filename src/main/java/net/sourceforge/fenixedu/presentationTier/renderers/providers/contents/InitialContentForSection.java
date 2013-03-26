package net.sourceforge.fenixedu.presentationTier.renderers.providers.contents;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.contents.Element;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class InitialContentForSection implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        Section section = (Section) source;
        return section.getChildren(Element.class);
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
