package net.sourceforge.fenixedu.renderers.plugin;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.renderers.components.state.ComponentLifeCycle;
import net.sourceforge.fenixedu.renderers.components.state.LifeCycleConstants;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.TilesRequestProcessor;

public class RenderersRequestProcessor extends TilesRequestProcessor {

    private static ThreadLocal<HttpServletRequest> currentRequest = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<ServletContext>     currentContext = new ThreadLocal<ServletContext>();
    
    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        currentRequest.set(request);
        currentContext.set(getServletContext());
        
        super.process(request, response);
    }

    public static HttpServletRequest getCurrentRequest() {
        return currentRequest.get();
    }

    public static ServletContext getCurrentContext() {
        return currentContext.get();
    }

    @Override
    protected Action processActionCreate(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException {
        Action action = super.processActionCreate(request, response, mapping);
        
        if (action == null) {
            return new VoidAction();
        }
        
        return action;
    }

    @Override
    protected ActionForward processActionPerform(HttpServletRequest request, HttpServletResponse response, Action action, ActionForm form, ActionMapping mapping) throws IOException, ServletException {
        if (request.getParameter(LifeCycleConstants.VIEWSTATE_PARAM_NAME) != null && request.getAttribute(LifeCycleConstants.PROCESSED_PARAM_NAME) == null) {
 
            // TODO: check if we should do this only after forwarding
            request.setAttribute(LifeCycleConstants.PROCESSED_PARAM_NAME, true);
            
            try {
                ActionForward forward = ComponentLifeCycle.execute(request);
                if (forward != null) {
                    return forward;
                }
            } catch (Exception e) {
                throw new ServletException("exception during the component lifecyle processing", e);
            }
        }
        
        return super.processActionPerform(request, response, action, form, mapping);
    }

}

class VoidAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return null;

    }

}