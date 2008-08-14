package net.sourceforge.fenixedu.presentationTier.renderers.converters;

import net.sourceforge.fenixedu.domain.material.Extension;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExtensionNumberConverter extends Converter {

    @Override
    public Object convert(Class type, Object value) {

	String numberText = ((String) value).trim();

	if (numberText.length() == 0) {
	    return null;
	}

	return Extension.getMaterialByTypeAndIdentification(Extension.class, numberText);
    }
}
