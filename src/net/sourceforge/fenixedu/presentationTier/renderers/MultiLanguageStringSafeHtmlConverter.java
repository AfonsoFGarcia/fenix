package net.sourceforge.fenixedu.presentationTier.renderers;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.presentationTier.renderers.MultiLanguageStringInputRenderer.MultiLanguageStringConverter;
import net.sourceforge.fenixedu.presentationTier.renderers.htmlEditor.SafeHtmlConverter;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class MultiLanguageStringSafeHtmlConverter extends Converter {

    @Override
    public Object convert(Class type, Object value) {
        SafeHtmlConverter safeConverter = new SafeHtmlConverter();
        MultiLanguageStringConverter mlsConverter = new MultiLanguageStringConverter();
        
        MultiLanguageString mls = (MultiLanguageString) mlsConverter.convert(type, value);
        
        if (mls == null) {
            return null;
        }

        if (mls.getAllLanguages().isEmpty()) {
            return null;
        }
        
        for (Language language : mls.getAllLanguages()) {
            String text = (String) safeConverter.convert(String.class, mls.getContent(language));
            
            if (text == null) {
                mls.removeContent(language);
            }
            else {
                mls.setContent(language, text);
            }
        }
        
        return mls;
    }

}
