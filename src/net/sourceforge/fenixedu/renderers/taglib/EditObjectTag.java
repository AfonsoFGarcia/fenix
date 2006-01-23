package net.sourceforge.fenixedu.renderers.taglib;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlForm;
import net.sourceforge.fenixedu.renderers.components.HtmlHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.LifeCycleConstants;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.components.state.ViewState;
import net.sourceforge.fenixedu.renderers.contexts.InputContext;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.TagUtils;

public class EditObjectTag extends BaseRenderObjectTag {

    private boolean nested;
    
    private String action;
    
    private Map<String, ViewDestination> destinations;
    
    public EditObjectTag() {
        super();

        this.nested = false;
        this.destinations = new Hashtable<String, ViewDestination>();
    }

    public boolean isNested() {
        return this.nested;
    }

    public void setNested(boolean nested) {
        this.nested = nested;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public void release() {
        super.release();

        this.nested = false;
        this.action = null;
        this.destinations = new Hashtable<String, ViewDestination>();
    }

    protected boolean isPostBack() {
        IViewState viewState = getViewState();

        if (viewState == null) {
            return false;
        }

        if (getId() == null) {
            if (viewState.getId() != null) {
                return false;
            }
        }
        else if (! getId().equals(viewState.getId())) {
            return false;
        }
        
        return getInputDestination().equals(viewState.getInputDestination());
    }

    @Override
    protected Object getTargetObject() throws JspException {
        if (!isPostBack()) {
            return super.getTargetObject();
        } else {
            IViewState viewState = getViewState();
            return viewState.getMetaObject().getObject();
        }
    }

    @Override
    protected PresentationContext createPresentationContext(Object object, String layout, String schema, Properties properties) {
        IViewState viewState = createViewState(object);

        if (viewState.getContext() != null) {
            return viewState.getContext();
        }
        else {
            InputContext context = new InputContext();
    
            context.setLayout(layout);
            context.setProperties(properties);
    
            viewState.setContextClass(context.getClass());
            viewState.setContext(context);
            
            context.setViewState(viewState);
            
            return context;
        }
    }

    @Override
    protected HtmlComponent renderObject(PresentationContext context, Object object) {
        if (isPostBack()) {
            return retrieveComponent();
        } else {
            return RenderKit.getInstance().render(context, object);
        }
    }

    protected HtmlComponent retrieveComponent() {
        return getViewState().getComponent();
    }

    @Override
    protected void drawComponent(PresentationContext context, HtmlComponent component) throws JspException, IOException {
        InputContext inputContext = (InputContext) context;
        IViewState viewState = inputContext.getViewState();

        HtmlComponent componentToDraw;
        HtmlHiddenField htmlHiddenField = new HtmlHiddenField(LifeCycleConstants.VIEWSTATE_PARAM_NAME, ViewState.encodeToBase64(viewState));
        
        if (isNested()) {
            HtmlInlineContainer container = new HtmlInlineContainer();
            
            container.addChild(htmlHiddenField);
            container.addChild(component);
            
            componentToDraw = container;
        }
        else {
            HtmlForm form = inputContext.getForm();
            
            form.setId(getId());
            form.setAction(getActionPath());
            form.setMethod(HtmlForm.POST);
            form.setEncoding(HtmlForm.URL_ENCODED);

            form.setBody(component);
            form.addHiddenField(htmlHiddenField);
            
            componentToDraw = form;
        }
        
        componentToDraw.draw(pageContext);
    }

    protected String getActionPath() {
        HttpServletResponse response = (HttpServletResponse) this.pageContext.getResponse();
        
        String action = getAction();
        if (action == null) {
            action = getCurrentPath();
        }

        String actionMappingURL = TagUtils.getInstance().getActionMappingURL(action, this.pageContext);
        return response.encodeURL(actionMappingURL);
    }

    protected IViewState getViewState() {
        return (IViewState) pageContext.findAttribute(LifeCycleConstants.VIEWSTATE_PARAM_NAME);
    }
    
    protected IViewState createViewState(Object targetObject) {
        IViewState viewState;

        if (isPostBack()) {
            viewState = getViewState();
        }
        else {
            viewState = new ViewState(getId());

            viewState.setLayout(getLayout());
            viewState.setProperties(getRenderProperties());
            viewState.setRequest((HttpServletRequest) pageContext.getRequest());
            
            Schema schema = RenderKit.getInstance().findSchema(getSchema());
            MetaObject metaObject = MetaObjectFactory.createObject(targetObject, schema);
            viewState.setMetaObject(metaObject);
            
            viewState.setInputDestination(getInputDestination());

            String currentPath = getCurrentPath();
            ModuleConfig module = TagUtils.getInstance().getModuleConfig(pageContext);
    
            for (String name : this.destinations.keySet()) {
                ViewDestination destination = this.destinations.get(name);
                
                viewState.addDestination(name, normalizeDestination(destination, currentPath, module.getPrefix()));
            }
        }
        
        return viewState;
    }

    protected ViewDestination getInputDestination() {
        String currentPath = getCurrentPath();
        ModuleConfig module = TagUtils.getInstance().getModuleConfig(pageContext);
        
        return new ViewDestination(currentPath, module.getPrefix(), false);       
    }
    
    protected String getCurrentPath() {
        ActionMapping mapping = (ActionMapping) pageContext.findAttribute(Globals.MAPPING_KEY);
        String currentPath = TagUtils.getInstance().getActionMappingURL(mapping.getPath(), pageContext);
        String contextPath = ((HttpServletRequest) pageContext.getRequest()).getContextPath();

        ModuleConfig module = TagUtils.getInstance().getModuleConfig(pageContext);

        if (currentPath.startsWith(contextPath)) {
            currentPath = currentPath.substring(contextPath.length());
        }

        if (module != null && currentPath.startsWith(module.getPrefix())) {
            currentPath = currentPath.substring(module.getPrefix().length());
        }
        
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        if (request.getQueryString() != null) {
            currentPath = currentPath + "?" + request.getQueryString();
        }
        
        return currentPath;
    }

    private ViewDestination normalizeDestination(ViewDestination destination, String currentPath, String module) {
        if (destination.getModule() == null) {
            destination.setModule(module);
        }

        if (destination.getPath() == null) {
            destination.setPath(currentPath);
        }

        return destination;
    }

    public void addDestination(String name, String path, String module, boolean redirect) {
        this.destinations.put(name, new ViewDestination(path, module, redirect));
    }
}
