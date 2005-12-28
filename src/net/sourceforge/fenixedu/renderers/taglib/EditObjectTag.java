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
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.LifeCycleConstants;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.components.state.ViewState;
import net.sourceforge.fenixedu.renderers.contexts.InputContext;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.taglib.BaseRenderObjectTag;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.TagUtils;

public class EditObjectTag extends BaseRenderObjectTag {

    private String action;
    
    private Map<String, ViewDestination> destinations;
    
    public EditObjectTag() {
        super();
        
        this.destinations = new Hashtable<String, ViewDestination>();
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

        this.destinations = new Hashtable<String, ViewDestination>();
    }

    protected boolean isPostBack() {
        IViewState viewState = getViewState();

        if (viewState == null) {
            return false;
        }

        if (getId() == null) {
            return viewState.getId() == null;
        }

        if (viewState.getId() == null) {
            return false;
        } else {
            return viewState.getId().equals(getId());
        }
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
        InputContext context = new InputContext();

        context.setLayout(layout);
        context.setProperties(properties);

        IViewState viewState = createViewState(object, context);
        context.setViewState(viewState);
        
        return context;
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
        HtmlForm form = inputContext.getForm();
        
        form.setId(getId());
        form.setAction(getActionPath());
        form.setMethod(HtmlForm.POST);
        form.setEncoding(HtmlForm.URL_ENCODED);

        form.setBody(component);
        
        IViewState viewState = inputContext.getViewState();
        HtmlHiddenField htmlHiddenField = new HtmlHiddenField(LifeCycleConstants.VIEWSTATE_PARAM_NAME, ViewState.encodeToBase64(viewState));
        form.addHiddenField(htmlHiddenField);
        
        form.draw(pageContext);
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

    protected String getSubmitPath() {
        // TODO: make this configurable 
        return TagUtils.getInstance().getActionMappingURL("editObject", pageContext);
    }

    protected IViewState getViewState() {
        return (IViewState) pageContext.findAttribute(LifeCycleConstants.VIEWSTATE_PARAM_NAME);
    }
    
    protected IViewState createViewState(Object targetObject, PresentationContext context) {
        IViewState viewState;

        if (isPostBack()) {
            viewState = getViewState();
        }
        else {
            viewState = new ViewState(getId());

            viewState.setLayout(getLayout());
            viewState.setProperties(getRenderProperties());
            viewState.setContextClass(context.getClass());
            viewState.setRequest((HttpServletRequest) pageContext.getRequest());
            
            Schema schema = RenderKit.getInstance().findSchema(getSchema());
            MetaObject metaObject = MetaObjectFactory.createObject(targetObject, schema);
            viewState.setMetaObject(metaObject);
            
            String currentPath = getCurrentPath();
    
            ModuleConfig module = TagUtils.getInstance().getModuleConfig(pageContext);
            viewState.setInputDestination(new ViewDestination(currentPath, module.getPrefix(), false));
    
            for (String name : this.destinations.keySet()) {
                ViewDestination destination = this.destinations.get(name);
                
                viewState.addDestination(name, normalizeDestination(destination, currentPath, module.getPrefix()));
            }
        }
        
        return viewState;
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
