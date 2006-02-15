package net.sourceforge.fenixedu.renderers.components.state;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.UserIdentity;

import org.apache.commons.codec.binary.Base64;

public class ViewState implements IViewState {

    private String id;

    private String layout;

    private Properties properties;

    private transient PresentationContext context;
    
    private Class contextClass;

    private Map<String, Object> attributes;
    
    // Hidden slots, filled from context
    
    private List<HiddenSlot> hiddenSlots;
    
    // Destinations available 
    
    private ViewDestination inputDestination;

    private Map<String, ViewDestination> destinations;

    private ViewDestination currentDestination;
    
    // Properties set after each deserialization 
    
    transient private UserIdentity user;

    transient private HtmlComponent component;

    transient private HttpServletRequest request;

    // Viewed object 
    
    private MetaObject metaObject;
    
    // Lifecycle properties
    
    private boolean valid;

    private boolean skipUpdate;

    private boolean updateComponentTree;
    
    private boolean postBack;

    public ViewState(String id) {
        super();

        this.id = id;

        this.valid = true;
        this.skipUpdate = false;
        this.updateComponentTree = true;
        this.postBack = false;
    }

    public String getId() {
        return id;
    }
    public boolean isPostBack() {
        return postBack;
    }

    public void setPostBack(boolean isPostBack) {
        this.postBack = isPostBack;
    }

    public HtmlComponent getComponent() {
        return this.component;
    }

    public void setComponent(HtmlComponent component) {
        this.component = component;
    }

    public void setValid(boolean isValid) {
        this.valid = isValid;
    }

    public boolean skipUpdate() {
        return this.skipUpdate;
    }

    public void setSkipUpdate(boolean skipUpdate) {
        this.skipUpdate = skipUpdate;
    }

    public boolean isValid() {
        return this.valid;
    }

    public void setUpdateComponentTree(boolean updateTree) {
        this.updateComponentTree = updateTree;
    }

    public boolean getUpdateComponentTree() {
        return this.updateComponentTree;
    }

    public void addDestination(String name, ViewDestination destination) {
        if (this.destinations == null) {
            this.destinations = new Hashtable<String, ViewDestination>();
        }
        
        this.destinations.put(name, destination);
    }

    public ViewDestination getDestination(String name) {
        if (this.destinations == null) {
            this.destinations = new Hashtable<String, ViewDestination>();
        }

        return this.destinations.get(name);
    }

    public void setInputDestination(ViewDestination destination) {
        this.inputDestination = destination;
    }

    public ViewDestination getInputDestination() {
        return this.inputDestination;
    }

    public void setCurrentDestination(String name) {
        this.currentDestination = getDestination(name);
    }

    public void setCurrentDestination(ViewDestination destination) {
        this.currentDestination = destination;
    }

    public ViewDestination getCurrentDestination() {
        return this.currentDestination;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
    
    public void setMetaObject(MetaObject object) {
        this.metaObject = object;
    }

    public MetaObject getMetaObject() {
        return this.metaObject;
    }

    public UserIdentity getUser() {
        return user;
    }

    public void setUser(UserIdentity user) {
        this.user = user;
        
        if (getMetaObject() != null) {
            getMetaObject().setUser(user);
        }
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Class getContextClass() {
        return contextClass;
    }

    public void setContextClass(Class contextClass) {
        this.contextClass = contextClass;
    }

    public void setLocalAttribute(String name, Object value) {
        setAttribute(name, value);
    }

    public Object getLocalAttribute(String name) {
        return getAttribute(name);
    }

    public void removeLocalAttribute(String name) {
        removeAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        if (this.attributes == null) {
            this.attributes = new Hashtable<String, Object>();
        }
        
        this.attributes.put(name, value);
    }
    
    public Object getAttribute(String name) {
        if (this.attributes == null) {
            this.attributes = new Hashtable<String, Object>();
        }
        
        return this.attributes.get(name);
    }
    
    public void removeAttribute(String name) {
        if (this.attributes == null) {
            this.attributes = new Hashtable<String, Object>();
        }
        
        this.attributes.remove(name);
    }
    
    //
    // Serialization utils
    //

    public static String encodeToBase64(IViewState state) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(byteOutputStream);

        stream.writeObject(state);
        stream.close();

        return new String(Base64.encodeBase64(byteOutputStream.toByteArray()));
    }

    public static IViewState decodeFromBase64(String encodedState) throws IOException,
            ClassNotFoundException {
        byte[] decodedForm = Base64.decodeBase64(encodedState.getBytes());

        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(decodedForm);
        ObjectInputStream stream = new ObjectInputStream(byteInputStream);

        return (IViewState) stream.readObject();
    }

    public void setContext(PresentationContext context) {
        this.context = context;
        
        if (this.context != null) {
            setContextClass(this.context.getClass());
        }
    }

    public PresentationContext getContext() {
        return this.context;
    }

    public void addHiddenSlot(HiddenSlot slot) {
        if (this.hiddenSlots == null) {
            this.hiddenSlots = new ArrayList<HiddenSlot>();
        }

        this.hiddenSlots.add(slot);
    }

    public List<HiddenSlot> getHiddenSlots() {
        if (this.hiddenSlots == null) {
            this.hiddenSlots = new ArrayList<HiddenSlot>();
        }
        
        return this.hiddenSlots;
    }
}
