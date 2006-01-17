/*
 * Created on Apr 18, 2005
 */
package net.sourceforge.fenixedu.presentationTier.TagLib;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.Globals;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

public class EnumTagLib extends BodyTagSupport {

    private final static Map<String, Map<String, Collection<LabelValueBean>>> labelValueBeansMap = new HashMap<String, Map<String, Collection<LabelValueBean>>>();

    private String id;

    private String enumeration;

    private String locale = Globals.LOCALE_KEY;

    private String bundle;

    private String excludedFields;

    private String includedFields;

    protected static MessageResources messages = MessageResources
            .getMessageResources("org.apache.struts.taglib.bean.LocalStrings");

    public int doStartTag() throws JspException {
        Collection<LabelValueBean> labelValueBeans = getLabelValues();
        pageContext.getRequest().setAttribute(id, labelValueBeans);
        // pageContext.getRequest().setAttribute(id, new ArrayList());

        return super.doStartTag();
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setExcludedFields(String excludedFields) {
        this.excludedFields = excludedFields;
    }

    public void setIncludedFields(String includedFields) {
        this.includedFields = includedFields; 
    }

    public Collection<LabelValueBean> getLabelValues() {

        return getLabelValuesLookup();
    }

    public Collection<LabelValueBean> getLabelValuesLookup() {
        try {
            Class clazz = Class.forName(this.enumeration);
            if (!clazz.isEnum()) {
                throw new IllegalArgumentException("Expected an enum type, got: " + this.enumeration);
            }

            if (this.excludedFields != null && this.includedFields != null) {
                throw new IllegalArgumentException(
                        "includedFields and excludedFields are both not empty");
            }

            final Method method = clazz.getMethod("values", (Class[]) null);
            final Object[] objects = (Object[]) method.invoke(clazz, (Object[]) null);
            final Collection<LabelValueBean> labelValueBeans = new ArrayList<LabelValueBean>(
                    objects.length);

            final Set<String> excludedFieldsNames = new HashSet();
            fillSetWithSplittedString(excludedFieldsNames, this.excludedFields);
            final Set<String> includedFieldsNames = new HashSet();
            fillSetWithSplittedString(includedFieldsNames, this.includedFields);

            for (int i = 0; i < objects.length; i++) {
                String value = objects[i].toString();

                if ((!excludedFieldsNames.isEmpty() && !excludedFieldsNames.contains(value))
                        || (!includedFieldsNames.isEmpty() && includedFieldsNames.contains(value)) || (includedFieldsNames
                                .isEmpty() && excludedFieldsNames.isEmpty())) {
                    String message = RequestUtils.message(pageContext, this.bundle, this.locale, value,
                            null);
                    labelValueBeans.add(new LabelValueBean(message, value));
                }
            }

            return labelValueBeans;
        } catch (Exception e) {
            throw new IllegalArgumentException("Expected an enum type, got: " + this.enumeration);
        }

    }

    private void fillSetWithSplittedString(final Set<String> fieldsNamesSet, String fieldNamesString) {
        if (fieldNamesString != null && fieldNamesString.length() > 0) {
            String[] fieldsNamesArray = fieldNamesString.split(",");
            for (String fieldName : fieldsNamesArray) {
                fieldsNamesSet.add(fieldName);
            }
        }
    }

}
