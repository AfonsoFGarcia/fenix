package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

public class CheckUserViewFilter implements Filter {

    private static final String REDIRECT = PropertiesManager.getProperty("login.page");

    private static final int APP_CONTEXT_LENGTH = PropertiesManager.getProperty("app.context").length() + 1;

    public void init(FilterConfig config) {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String uri = request.getRequestURI();
        final IUserView userView = getUserView(request);
        if (isPrivateURI(uri.substring(APP_CONTEXT_LENGTH)) && !validUserView(userView)) {
        	System.out.println("uri: " + uri);
//        	for (final Entry<String, String[]> entry : ((Map<String, String[]>) request.getParameterMap()).entrySet()) {
//        		System.out.println("   parameter: " + entry.getKey());
//        		for (final String string : entry.getValue()) {
//        			System.out.println("      |-> " + string);
//        		}
//        	}
//        	final Enumeration enumeration = request.getAttributeNames();
//        	while (enumeration.hasMoreElements()) {
//        		final Object object = enumeration.nextElement();
//        		System.out.println("   attribute: " + object + " = " + request.getAttribute((String) object));
//        	}

        	final HttpSession httpSession = request.getSession(true);
        	httpSession.setAttribute("ORIGINAL_REQUEST", request);

            final StringBuilder originalURI = new StringBuilder(request.getRequestURI());
            boolean isFirst = true;
        	for (final Entry<String, String[]> entry : ((Map<String, String[]>) request.getParameterMap()).entrySet()) {
        		for (final String parameterValue : entry.getValue()) {
            		if (isFirst) {
            			isFirst = false;
            			originalURI.append('?');
            		} else {
            			originalURI.append('&');
            		}
            		originalURI.append(entry.getKey());
            		originalURI.append('=');
            		originalURI.append(parameterValue);

                }
        	}
        	httpSession.setAttribute("ORIGINAL_URI", originalURI.toString());

            final Map<String, Object> parameterMap = new HashMap<String, Object>();
            for (final Entry<String, Object> entry : ((Map<String, Object>) request.getParameterMap()).entrySet()) {
                parameterMap.put(entry.getKey(), entry.getValue());
            }
            httpSession.setAttribute("ORIGINAL_PARAMETER_MAP", parameterMap);

            final Map<String, Object> attributeMap = new HashMap<String, Object>();
        	final Enumeration enumeration = request.getAttributeNames();
        	while (enumeration.hasMoreElements()) {
        		final String attributeName = (String) enumeration.nextElement();
        		attributeMap.put(attributeName, request.getAttribute(attributeName));
        	}
        	httpSession.setAttribute("ORIGINAL_ATTRIBUTE_MAP", attributeMap);

            response.sendRedirect(REDIRECT);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private IUserView getUserView(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        return (IUserView) ((session != null) ? session.getAttribute(SessionConstants.U_VIEW) : null);
    }

    private boolean isPrivateURI(final String uri) {
        return (uri.length() > 1
        		&& (uri.indexOf("CSS/") == -1)
        		&& (uri.indexOf("images/") == -1)
                && (uri.indexOf("download/") == -1)
                && (uri.indexOf("external/") == -1)
        		&& (uri.indexOf("index.jsp") == -1)
                && (uri.indexOf("index.html") == -1)
                && (uri.indexOf("login.do") == -1)
                && (uri.indexOf("privado") == -1)
                && (uri.indexOf("loginPage.jsp") == -1)
                && (uri.indexOf("logoff.do") == -1)
                && (uri.indexOf("publico/") == -1)
                && (uri.indexOf("showErrorPage.do") == -1)
                && (uri.indexOf("manager/manageCache.do") == -1)
                && (uri.indexOf("siteMap.do") == -1)
                && (uri.indexOf("changeLocaleTo.do") == -1)
                && (uri.indexOf("isAlive.do") == -1));
    }

    private boolean validUserView(final IUserView userView) {
        return userView != null;
    }

}