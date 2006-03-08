package net.sourceforge.fenixedu.renderers;

import java.util.Collection;

import net.sourceforge.fenixedu.renderers.components.HtmlCheckBox;
import net.sourceforge.fenixedu.renderers.components.HtmlCheckBoxList;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaObjectKey;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;

/**
 * This renderer can be used as the input for a list of objects. The list of
 * objects the user can choose will be presented as an html list were each
 * list item will contain the presentation of the object and a checkbox that
 * allows to choose that particular object. When submiting, all the checked objects
 * will be added to a list and that list will be the value passed to the slot.
 * 
 * <p> 
 * Example:
 *  <ul>
 *      <li><input type="checkbox"/><em>&lt;object A presentation&gt;</em></li>
 *      <li><input type="checkbox" checked="checked"/><em>&lt;object B presentation&gt;</em></li>
 *      <li><input type="checkbox"/><em>&lt;object C presentation&gt;</em></li>
 *  </ul>
 * 
 * @author cfgi
 */
public class CheckBoxOptionListRenderer extends InputRenderer {
    private String eachClasses;

    private String eachStyle;

    private String eachSchema;

    private String eachLayout;

    private String providerClass;

    private DataProvider provider;
    
    /**
     * This property allows you to configure the class attribute for each
     * object's presentation.
     * 
     * @property
     */
    public void setEachClasses(String classes) {
        this.eachClasses = classes;
    }

    public String getEachClasses() {
        return this.eachClasses;
    }

    /**
     * Allows yout to configure the style attribute for each object's
     * presentation.
     * 
     * @property
     */
    public void setEachStyle(String style) {
        this.eachStyle = style;
    }

    public String getEachStyle() {
        return this.eachStyle;
    }

    public String getEachLayout() {
        return eachLayout;
    }

    /**
     * Allows you to choose the layout in wich each object is to be presented.
     * 
     * @property
     */
    public void setEachLayout(String eachLayout) {
        this.eachLayout = eachLayout;
    }

    public String getEachSchema() {
        return eachSchema;
    }

    /**
     * Allows you to specify the schema that should be used when presenting
     * each individual object.
     * 
     * @property
     */
    public void setEachSchema(String eachSchema) {
        this.eachSchema = eachSchema;
    }

    public String getProviderClass() {
        return this.providerClass;
    }

    /**
     * The class name of a {@link DataProvider data provider}. The data
     * provider is responsible for providing a collection of objects. This
     * collection represents all the possible options for the slot beeing
     * edited. Additionally the data provider can also provide a custom
     * converter for the object encoded values.
     * 
     * <p>
     * Those objects that are already part of the slot's value will make
     * the associated checkbox to be checked. In the example above, the
     * slot contained as value a collection with at least object B but didn't
     * contained object A or C.
     * 
     * @property
     */
    public void setProviderClass(String providerClass) {
        this.providerClass = providerClass;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new CheckBoxListLayout();
    }

    protected DataProvider getProvider() {
        if (this.provider == null) {
            String className = getProviderClass();
            
            try {
                Class providerCass = (Class<DataProvider>) Class.forName(className);
                this.provider = (DataProvider) providerCass.newInstance();  
            } catch (Exception e) {
                throw new RuntimeException("could not get a data provider instance", e);
            }
        }
        
        return this.provider;
    }
    
    protected Converter getConverter() {
        DataProvider provider = getProvider();
        
        return provider.getConverter();
    }

    protected Collection getPossibleObjects() {
        Object object = getInputContext().getParentContext().getMetaObject().getObject();

        if (getProviderClass() != null) {
            try {
                DataProvider provider = getProvider();
                return (Collection) provider.provide(object);
            }
            catch (Exception e) {
                throw new RuntimeException("exception while executing data provider", e);
            }
        }
        else {
            throw new RuntimeException("a data provider must be supplied");
        }
    }
    
    class CheckBoxListLayout extends Layout {
        
        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            Collection collection = (Collection) object;
            
            HtmlCheckBoxList listComponent = new HtmlCheckBoxList();
            
            Collection possibleObjects = getPossibleObjects();
            for (Object obj : possibleObjects) {
                Schema schema = RenderKit.getInstance().findSchema(getEachSchema());
                String layout = getEachLayout();
                
                MetaObject metaObject = MetaObjectFactory.createObject(obj, schema);
                MetaObjectKey key = metaObject.getKey();
                
                PresentationContext newContext = getContext().createSubContext(metaObject);
                newContext.setLayout(layout);
                newContext.setRenderMode(RenderMode.getMode("output"));
                
                RenderKit kit = RenderKit.getInstance();
                HtmlComponent component = kit.render(newContext, obj);
                HtmlCheckBox checkBox = listComponent.addOption(component, key.toString());
                
                if (collection != null && collection.contains(obj)) {
                    checkBox.setChecked(true);
                }
            }

            if (collection != null) {
                for (Object obj : collection) {
                    if (! possibleObjects.contains(obj)) {
                        Schema schema = RenderKit.getInstance().findSchema(getEachSchema());
                        
                        MetaObject metaObject = MetaObjectFactory.createObject(obj, schema);
                        MetaObjectKey key = metaObject.getKey();
                        
                        listComponent.addHiddenOption(key.toString());
                    }
                }
            }
            
            Converter converter = getConverter();
            if (converter != null) {
                listComponent.setConverter(converter);
            }
            
            listComponent.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
            return listComponent;
        }

    }
}
