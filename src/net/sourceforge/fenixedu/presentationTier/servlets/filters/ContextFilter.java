package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.contents.InvalidContentPathException;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;

public class ContextFilter implements Filter {

    public static String INVALID_CONTENT_PATH_EXCEPTION = "INVALID_CONTENT_PATH_EXCEPTION";

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
	    throws IOException, ServletException {
	final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
	final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
	doFilter(httpServletRequest, httpServletResponse, filterChain);
    }

    public void doFilter(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse,
	    final FilterChain filterChain) throws IOException, ServletException {
	final FunctionalityContext functionalityContext = getContextAttibute(httpServletRequest);

	if (!shouldBeSkipped(httpServletRequest.getServletPath())) {
	    if (hasNoSelectedFunctionality(functionalityContext)) {
		try {
		    final FunctionalityContext context = createContext(httpServletRequest);
		    setContextAttibute(httpServletRequest, context);
		} catch (InvalidContentPathException ex) {
		    httpServletRequest.setAttribute(INVALID_CONTENT_PATH_EXCEPTION, ex);
		}
	    }
	}

	filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private boolean shouldBeSkipped(String path) {
	return path.contains(".css") || path.contains(".gif") || path.contains(".jpg") || path.contains(".js")
		|| path.contains("/ajax/") || path.contains("/checkPasswordKerberos.do") || path.contains("/loginCAS.do")
		|| path.contains("/home.do") || path.contains("/logoff.do") || path.contains("/siteMap.do")
		|| path.contains("/login.do") || path.startsWith("/external/") || path.startsWith("/isAlive.do")
		|| path.endsWith(".html") || path.endsWith(".htm") || path.startsWith("/exceptionHandlingAction.do")
		|| path.startsWith("/showErrorPageRegistered.do") || path.startsWith("/services") 
		|| path.contains("/loginExpired.do");
    }

    private FunctionalityContext getContextAttibute(final HttpServletRequest httpServletRequest) {
	return (FunctionalityContext) httpServletRequest.getAttribute(FunctionalityContext.CONTEXT_KEY);
    }

    private boolean hasNoSelectedFunctionality(FunctionalityContext functionalityContext) {
	return functionalityContext == null || functionalityContext.getSelectedContents() == null
		|| functionalityContext.getSelectedContents().isEmpty();
    }

    private FunctionalityContext createContext(final HttpServletRequest httpServletRequest) {
	return new FilterFunctionalityContext(httpServletRequest, "ISO-8859-1");
    }

    private void setContextAttibute(final HttpServletRequest servletRequest, final FunctionalityContext context) {
	servletRequest.setAttribute(FunctionalityContext.CONTEXT_KEY, context);
    }

}
