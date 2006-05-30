package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;

import org.apache.commons.beanutils.ConvertUtils;

public abstract class HtmlSimpleValueComponent extends HtmlFormComponent {

    private String value;
    
    public HtmlSimpleValueComponent() {
        super();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String[] getValues() {
        if (getValue() == null) {
            return null;
        }
        
        return new String[] { getValue() };
    }
    
    @Override
    public Object getConvertedValue(MetaSlot slot) {
        if (hasConverter()) {
            return getConverter().convert(slot.getType(), getValue());
        }
        
        if (slot.hasConverter()) {
            try {
                return slot.getConverter().newInstance().convert(slot.getType(), getValue());
            } catch (Exception e) {
                throw new RuntimeException("converter specified in meta slot generated an exception: " + e, e);
            }
        }
        
        return ConvertUtils.convert(getValue(), slot.getType());
    }
    
    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setAttribute("value", HtmlText.escape(this.value));
        
        return tag;
    }
}
