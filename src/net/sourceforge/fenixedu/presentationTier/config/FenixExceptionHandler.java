/*
 * Created on 25/Fev/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.config;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.log.requests.RequestLog;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSessionActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.stm.Transaction;
import net.sourceforge.fenixedu.util.ArrayUtils;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

/**
 * @author Jo�o Mota
 */
public class FenixExceptionHandler extends ExceptionHandler {

    /**
     * Handle the exception. Return the <code>ActionForward</code> instance
     * (if any) returned by the called <code>ExceptionHandler</code>.
     * 
     * @param ex
     *                The exception to handle
     * @param ae
     *                The ExceptionConfig corresponding to the exception
     * @param mapping
     *                The ActionMapping we are processing
     * @param formInstance
     *                The ActionForm we are processing
     * @param request
     *                The servlet request we are processing
     * @param response
     *                The servlet response we are creating
     * 
     * @exception ServletException
     *                    if a servlet exception occurs
     * 
     * @since Struts 1.1
     */
    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping, ActionForm formInstance,
	    HttpServletRequest request, HttpServletResponse response) throws ServletException {
	ActionForward forward = null;

	ActionError error = null;

	if (ex instanceof InvalidSessionActionException) {

	    ActionErrors errors = new ActionErrors();
	    error = new ActionError("error.invalid.session");
	    errors.add("error.invalid.session", error);
	    request.setAttribute(Globals.ERROR_KEY, errors);
	    return mapping.findForward("firstPage");

	}

	request.setAttribute(SessionConstants.ORIGINAL_MAPPING_KEY, mapping);

	request.setAttribute(SessionConstants.EXCEPTION_STACK_TRACE, ex.getStackTrace());

	final String requestContext = requestContextGetter(request);
	request.setAttribute(SessionConstants.REQUEST_CONTEXT, requestContext);

	final StringBuilder exceptionInfo = new StringBuilder("Error Origin: \n");
	exceptionInfo.append("Exception: \n" + ex + "\n\n");

	IUserView userView = SessionUtils.getUserView(request);
	if (userView != null) {
	    exceptionInfo.append("UserLogedIn: " + userView.getUtilizador() + "\n");
	} else {
	    exceptionInfo.append("No user logged in, or session was lost.\n");
	}

	exceptionInfo.append("RequestURI: " + request.getRequestURI() + "\n");
	exceptionInfo.append("RequestURL: " + request.getRequestURL() + "\n");
	exceptionInfo.append("QueryString: " + request.getQueryString() + "\n");

	exceptionInfo.append("RequestContext: \n" + requestContext + "\n\n\n");
	final String sessionContext = sessionContextGetter(request);
	exceptionInfo.append("SessionContext: \n" + sessionContext + "\n\n\n");

	final ActionForward actionForward;
	if (mapping != null) {
	    exceptionInfo.append("Path: " + mapping.getPath() + "\n");
	    exceptionInfo.append("Name: " + mapping.getName() + "\n");
	}

	exceptionInfo.append("\n\n");
	final String stackTrace = stackTrace2String(ex.getStackTrace());
	exceptionInfo.append(stackTrace);

	request.setAttribute("exceptionInfo", exceptionInfo.toString());

	if (ae.getScope() != "request") {
	    ae.setScope("session");
	}

	String property = null;

	// Figure out the error
	if (ex instanceof FenixActionException) {
	    error = ((FenixActionException) ex).getError();
	    property = ((FenixActionException) ex).getProperty();
	} else {
	    error = new ActionError(ae.getKey(), ex.getMessage());
	    property = error.getKey();
	}

	// Store the exception
	request.setAttribute(Globals.EXCEPTION_KEY, ex);
	super.storeException(request, property, error, forward, ae.getScope());

	String[] parameters = ArrayUtils.toStringArray(request.getParameterNames(), "_request_checksum_", "jsessionid");
	ErrorLogger errorLogger = new ErrorLogger(request.getRequestURI(), parameters, request.getQueryString(), userView
		.getUtilizador(), requestContext, sessionContext, stackTrace, ex.getClass().getName());
	errorLogger.start();
	try {
	    errorLogger.join();
	} catch (InterruptedException e) {
	}

	return super.execute(ex, ae, mapping, formInstance, request, response);
    }

    private String requestContextGetter(HttpServletRequest request) {

	Enumeration requestContents = request.getAttributeNames();
	String context = "";
	while (requestContents.hasMoreElements()) {
	    String requestElement = requestContents.nextElement().toString();
	    context += "RequestElement:" + requestElement + "\n";
	    context += "RequestElement Value:" + request.getAttribute(requestElement) + "\n";
	}

	return context;
    }

    private String sessionContextGetter(HttpServletRequest request) {
	HttpSession session = request.getSession(false);
	Enumeration sessionContents = session.getAttributeNames();
	String context = "";
	while (sessionContents.hasMoreElements()) {
	    String sessionElement = sessionContents.nextElement().toString();
	    context += "Element:" + sessionElement + "\n";
	    context += "Element Value:" + session.getAttribute(sessionElement) + "\n";
	}

	return context;
    }

    private String stackTrace2String(StackTraceElement[] stackTrace) {
	String result = "StackTrace: \n ";
	int i = 0;
	if (stackTrace != null) {
	    while (i < stackTrace.length) {
		result += stackTrace[i] + "\n";
		i++;
	    }
	}
	return result;
    }

    private static class ErrorLogger extends Thread {

	private String path;

	private String[] parameters;

	private String queryString;

	private String user;

	private String requestAttributes;

	private String sessionAttributes;

	private String stackTrace;

	private String exceptionType;

	public ErrorLogger(String path, String[] parameters, String queryString, String user, String requestAttributes,
		String sessionAttributes, String stackTrace, String exceptionType) {
	    super();
	    this.path = path;
	    this.parameters = parameters;
	    this.queryString = queryString;
	    this.user = user;
	    this.requestAttributes = requestAttributes;
	    this.sessionAttributes = sessionAttributes;
	    this.stackTrace = stackTrace;
	    this.exceptionType = exceptionType;
	}

	@Override
	public void run() {
	    Transaction.withTransaction(new jvstm.TransactionalCommand() {
		public void doIt() {
		    try {
			RequestLog.registerError(path, parameters, queryString, user, requestAttributes, sessionAttributes,
				stackTrace, exceptionType);
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
	    });
	}

    }

}