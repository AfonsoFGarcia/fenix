package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.tiles.TilesUtilImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * Reloads the Struts configuration files and associated plugins by
 * reinitializing the Struts' ActionServlet.
 * 
 * @author cfgi
 */
@Mapping(module = "manager", path = "/reloadStruts", scope = "session")
@Forwards(value = { @Forward(name = "firstPage", path = "/index") })
public class ReloadStrutsAction extends Action {

    private static final Logger logger = LoggerFactory.getLogger(ReloadStrutsAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionServlet servlet = getServlet();

        if (servlet == null) {
            logger.warn("action servlet is null, could not reload configuration");
            return null;
        }

        servlet.destroy();

        // clear struts and tiles state
        ServletContext context = servlet.getServletContext();
        Enumeration names = context.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();

            if (name.startsWith(Globals.REQUEST_PROCESSOR_KEY)) {
                context.removeAttribute(name);
            }

            if (name.startsWith(TilesUtilImpl.DEFINITIONS_FACTORY)) {
                context.removeAttribute(name);
            }
        }

        try {
            servlet.init();
        } catch (ServletException e) {
            logger.error("could not reload configuration, please redeploy the application");
            e.printStackTrace();
        }

        logger.info("reloaded configuration");
        return mapping.findForward("df.page.main-page");
    }
}
