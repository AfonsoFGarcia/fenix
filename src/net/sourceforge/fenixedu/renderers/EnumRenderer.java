package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * The <code>EnumRenderer</code> provides a simple presentation for 
 * enumeration values. An enum value will be displayed in one of two forms.
 * First a bundle named <code>ENUMERATION_RESOURCES</code> is used. Using 
 * as key the enum name a localized message is searched. If the bundle is 
 * not defined or the key does not exist in the bundle then the programmatic
 * name of the enum is presented.
 * 
 * @author cfgi
 */
public class EnumRenderer extends OutputRenderer {

    // NOTE: duplicate code with EnumInputRenderer
    protected String getEnumDescription(Enum enumerate) {
        String description = RenderUtils.getResourceString("ENUMERATION_RESOURCES", enumerate.toString()); 
            
        if (description == null) {
            description = RenderUtils.getResourceString(enumerate.toString());
        }
        
        if (description == null) {
        	description = enumerate.toString();
        }
        
        return description;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                Enum enumerate = (Enum) object;
                
                if (enumerate == null) {
                    return new HtmlText();
                }
                
                String description = getEnumDescription(enumerate);
                
                return new HtmlText(description);
            }
            
        };
    }
}
