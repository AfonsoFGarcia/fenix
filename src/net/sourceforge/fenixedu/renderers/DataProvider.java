package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public interface DataProvider {
    public Object provide(Object source);
    public Converter getConverter();
}
