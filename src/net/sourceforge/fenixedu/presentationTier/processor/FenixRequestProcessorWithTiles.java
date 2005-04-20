/*
 * Created on 23/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.presentationTier.processor;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.tiles.TilesRequestProcessor;
import org.apache.struts.util.RequestUtils;

/**
 * @author jpvl
 */
public class FenixRequestProcessorWithTiles extends TilesRequestProcessor {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.RequestProcessor#processPreprocess(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {

        HttpSession httpSession = request.getSession(false);
        Locale locale = (Locale) httpSession.getAttribute(Action.LOCALE_KEY);
        if (locale == null) {
            httpSession.setAttribute(Action.LOCALE_KEY, Locale.getDefault());
        }

        String uri = request.getRequestURI();
        if (((uri.indexOf("login.do") == -1) && (uri.indexOf("showErrorPage.do") == -1) && (uri
                .indexOf("/publico/index.do") == -1)) && (uri.indexOf("/manager/manageCache.do") == -1)
                && (uri.indexOf("/isAlive.do") == -1)) {
            if (request.getRemoteUser() == null) {
                ActionErrors errors = new ActionErrors();

                errors.add("error.invalid.session", new ActionError("error.invalid.session"));
                request.setAttribute(Globals.ERROR_KEY, errors);

                String uri2Forward = "/loginPage.jsp";
                String applicationPrefix = "";
                if (uri.indexOf("publico") != -1) {
                    applicationPrefix = "/publico";
                    uri2Forward = "/publico/index.jsp";
                }

                RequestUtils.selectModule(applicationPrefix, request, this.getServletContext());

                try {
                    doForward(uri2Forward, request, response);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (ServletException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                return false;
            }
        }
        return true;
    }

}