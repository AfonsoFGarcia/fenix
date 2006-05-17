package net.sourceforge.fenixedu.presentationTier.renderers;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlScript;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTextInput;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * This renderer allows you to search for a domain object by providing a list
 * of possible completions, for the text typed, using a javascript technique.
 * 
 * <p>
 * This renderer can be used to do the input of any domain object because you
 * can configure the concrete service that searches the objects and all objects
 * are referred to by their internal id. It's recommended that a specialized
 * service is created for most cases so that it's as efficient as possible.
 * 
 * <p>
 * Example: <br>
 * <input type="text" value="po" style="width: 20em;"/>
 * <div style="margin-top: -10px; border: 1px solid #eee; width: 20em;">
 *  <ul>
 *      <li>French Polynesia</li>
 *      <li>Poland</li>
 *      <li>Portugal</li>
 *      <li>Singapore</li>
 *  </ul>
 * </div>
 * 
 * @author cfgi
 */
public class AutoCompleteInputRenderer extends InputRenderer {

    public static final String SERVLET_URI  = "/ajax/AutoCompleteServlet";
    public static final String TYPING_VALUE  = "custom";
    
    private static final String SCRIPT_FLAG_KEY = AutoCompleteInputRenderer.class.getName() + "/scripts"; 
    
    private String rawSlotName;
    
    private String labelField;
    private String format;

    private String className;
    private String serviceName;
    private String serviceArgs;

    private Integer maxCount;
    
    private String size;
    private int minChars;
    
    private String autoCompleteStyleClass;
    private String autoCompleteItemsStyleClass;
    private String textFieldStyleClass;
    
    public AutoCompleteInputRenderer() {
        super();
        
        setMinChars(3);
    }

    public String getRawSlotName() {
        return this.rawSlotName;
    }

    /**
     * This property allows you to specify a slot that will hold the text that
     * was present in the input field when it was submited.
     *
     * <p>
     * If the renderer is used to edit the slot <code>slotA</code> of an object A
     * and this property is set the value <code>slotB</code> then when the field
     * is submited the renderer will set the value of the text field in the 
     * <code>slotB</code> of the object A.
     * 
     * <p>
     * When you type in the text field an auto completion list is presented.
     * Nevertheless an object is only selected when the user selects one element
     * from the sugested completions. This means that if the user does not
     * select one element after typing some text the value of the slot beeing
     * edited will be set to <code>null</code>.
     *  
     * @property
     */
    public void setRawSlotName(String rawSlotName) {
        this.rawSlotName = rawSlotName;
    }

    public String getLabelField() {
        return this.labelField;
    }

    /**
     * This property allows you tho choose the name of the slot that will be used
     * as the presentation of the object. If this proprty has the value <code>slotL</code>
     * then the list of suggestions will be a list of values obtained by invoking
     * <code>getSlotL</code> in each object. 
     * 
     * @property
     */
    public void setLabelField(String labelField) {
        this.labelField = labelField;
    }

    public String getFormat() {
        return this.format;
    }

    /**
     * Allows you select the presentation format. If not set the value of the field given
     * by {@link #setLabelField(String) labelField} is used. See {@link net.sourceforge.fenixedu.renderers.utils.RenderUtils#getFormatedProperties(String, Object)}
     * to see the accepted format syntax. 
     * 
     * @property
     */
    public void setFormat(String format) {
        this.format = format;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    /**
     * Configures the service that should be used to do the search. That service must
     * implement the interface {@link net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService}.
     * 
     * @property
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceArgs() {
        return this.serviceArgs;
    }

    /**
     * Allows you to pass extra arguments to the service in the form 
     * <code>paramA=value1,paramB=value2</code>. This arguments will
     * be available in the arguments map passed to the service.
     * 
     * @property
     */
    public void setServiceArgs(String serviceArgs) {
        this.serviceArgs = serviceArgs;
    }

    public Integer getMaxCount() {
        return this.maxCount;
    }

    /**
     * Limits the number of results that the servlet returns thus the
     * number of suggestions given to the user.
     * 
     * @property
     */
    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public String getClassName() {
        if (this.className != null) {
            return this.className;            
        }
        else {
            return getContext().getMetaObject().getType().getName();
        }
    }

    /**
     * The name of the type of objects we want to search. This should be
     * the the same type or a subtype of the type of the slot this 
     * rendering is editing.
     * 
     * @property
     */
    public void setClassName(String className) {
        this.className = className;
    }

    public String getAutoCompleteItemsStyleClass() {
        return this.autoCompleteItemsStyleClass;
    }

    /**
     * The html class of results shown.
     * 
     * @property
     */
    public void setAutoCompleteItemsStyleClass(String autoCompleteItemsStyleClass) {
        this.autoCompleteItemsStyleClass = autoCompleteItemsStyleClass;
    }

    public String getAutoCompleteStyleClass() {
        return this.autoCompleteStyleClass;
    }

    /**
     * The html class of the container of the results shown.
     * 
     * @property
     */
    public void setAutoCompleteStyleClass(String autoCompleteStyleClass) {
        this.autoCompleteStyleClass = autoCompleteStyleClass;
    }

    public String getTextFieldStyleClass() {
        return this.textFieldStyleClass;
    }

    /**
     * The html class of the text field.
     * 
     * @property
     */
    public void setTextFieldStyleClass(String textFieldStyleClass) {
        this.textFieldStyleClass = textFieldStyleClass;
    }

    public String getSize() {
        return this.size;
    }

    /**
     * The size of the text field.
     * 
     * @property
     */
    public void setSize(String size) {
        this.size = size;
    }

    public int getMinChars() {
        return this.minChars;
    }

    /**
     * The number of characters the user is required to type before any suggestion is offered.
     * 
     * @property
     */
    public void setMinChars(int minChars) {
        this.minChars = minChars;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                
                HtmlInlineContainer container = new HtmlInlineContainer();

                addScripts(container);
                
                MetaSlotKey key = (MetaSlotKey) getContext().getMetaObject().getKey();
                
                HtmlHiddenField valueField = new HtmlHiddenField();
                valueField.setTargetSlot(key);
                valueField.setId(key.toString() + "_AutoComplete");
                valueField.setName(valueField.getId());
                container.addChild(valueField);

                valueField.setConverter(new AutoCompleteConverter(getClassName()));
                
                HtmlTextInput textField = new HtmlTextInput();
                textField.setId(key.toString());
                textField.setName(textField.getId());
                textField.setClasses(getTextFieldStyleClass());
                textField.setSize(getSize());
                textField.setOnKeyUp("javascript:autoCompleteUpdateValueField('" + textField.getId() + "', '" + TYPING_VALUE + "');");
                container.addChild(textField);

                if (getRawSlotName() != null) {
                    textField.setController(new UpdateRawNameController(getRawSlotName()));
                }
                
                HtmlBlockContainer resultsContainer = new HtmlBlockContainer();
                resultsContainer.setId(key.toString() + "_div");
                resultsContainer.setClasses(getAutoCompleteStyleClass());
                container.addChild(resultsContainer);
                
                addFinalScript(container, textField.getId(), resultsContainer.getId());
                
                return container;
            }

            private void addScripts(HtmlInlineContainer container) {
                HttpServletRequest request = getContext().getViewState().getRequest();
                
                if (request.getAttribute(SCRIPT_FLAG_KEY) != null) {
                    return;
                }
                
                request.setAttribute(SCRIPT_FLAG_KEY, true);
                
                HtmlLink link = new HtmlLink();
                link.setModuleRelative(false);
                link.setContextRelative(true);
                
                String[] scriptNames = new String[] { 
                        "prototype.js", 
                        "effects.js", 
                        "dragdrop.js", 
                        "controls.js", 
                        "fenixScript.js" 
                };
                
                for (String script : scriptNames) {
                    addSingleScript(container, link, script);
                }
            }

            private void addSingleScript(HtmlInlineContainer container, HtmlLink link, String scriptName) {
                link.setUrl("/javaScript/" + scriptName);
                HtmlScript script = new HtmlScript("text/javascript", link.calculateUrl(), true);
                container.addChild(script);
            }

            private void addFinalScript(HtmlInlineContainer container, String textFieldId, String divId) {
                HtmlLink link = new HtmlLink();
                link.setModuleRelative(false);
                link.setContextRelative(true);
                
                link.setUrl(SERVLET_URI);
                link.setParameter("serviceName", getServiceName());
                link.setParameter("serviceArgs", getServiceArgs());
                link.setParameter("labelField", getLabelField());
                link.setParameter("valueField", "idInternal"); // TODO: allow configuration, needs also converter
                link.setParameter("styleClass", getAutoCompleteItemsStyleClass() == null ? "" : getAutoCompleteItemsStyleClass());
                link.setParameter("class", getClassName());
                
                if (getFormat() != null) {
                    link.setParameter("format", getFormat());
                }
                
                if (getMaxCount() != null) {
                    link.setParameter("maxCount", String.valueOf(getMaxCount()));
                }
                
                String finalUri = link.calculateUrl();
                String scriptText = "new Ajax.Autocompleter('" + textFieldId + "','" + divId + "','" + finalUri +
                        "', {paramName: 'value', afterUpdateElement: autoCompleteUpdate, minChars: " + getMinChars() + "});";
                
                HtmlScript script = new HtmlScript();
                script.setContentType("text/javascript");
                script.setScript(scriptText);
                
                container.addChild(script);
            }

        };
    }

    private static class UpdateRawNameController extends HtmlController {

        private String rawSlotName;

        public UpdateRawNameController(String rawSlotName) {
            super();
            
            this.rawSlotName = rawSlotName;
        }

        @Override
        public void execute(IViewState viewState) {
            HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getControlledComponent();
            
            updatRawSlot(viewState, component.getValue()); 
        }

        private void updatRawSlot(IViewState viewState, String value) {
            Object object = viewState.getMetaObject().getObject();
            
            try {
                PropertyUtils.setProperty(object, this.rawSlotName, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    private static class AutoCompleteConverter extends Converter {

        private String className;

        public AutoCompleteConverter(String className) {
            super();
            
            this.className = className;
        }

        @Override
        public Object convert(Class type, Object value) {
            if (value == null || "".equals(value)) {
                return null;
            }
            
            String text = (String) value;
            
            if (text.equals(TYPING_VALUE)) {
                return null;
            }
            
            String key = DomainObjectKeyConverter.code(this.className, text);
            return new DomainObjectKeyConverter().convert(type, key);
        }
        
    }

}
