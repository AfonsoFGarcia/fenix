/*
 * Created on 25/Fev/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorApresentacao.config;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author jmota
 */
public class FenixExceptionHandler extends ExceptionHandler {

	/**
		 * Handle the exception.
		 * Return the <code>ActionForward</code> instance (if any) returned by
		 * the called <code>ExceptionHandler</code>.
		 *
		 * @param ex The exception to handle
		 * @param ae The ExceptionConfig corresponding to the exception
		 * @param mapping The ActionMapping we are processing
		 * @param formInstance The ActionForm we are processing
		 * @param request The servlet request we are processing
		 * @param response The servlet response we are creating
		 *
		 * @exception ServletException if a servlet exception occurs
		 *
		 * @since Struts 1.1
		 */
	public ActionForward execute(
		Exception ex,
		ExceptionConfig ae,
		ActionMapping mapping,
		ActionForm formInstance,
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException {
		ActionForward forward = null;

		request.getSession().setAttribute(
			SessionConstants.ORIGINAL_MAPPING_KEY,
			mapping);

		request.getSession().setAttribute(
			SessionConstants.EXCEPTION_STACK_TRACE,
			ex.getStackTrace());

		request.getSession().setAttribute(
			SessionConstants.REQUEST_CONTEXT,
			requestContextGetter(request));

		if (ae.getScope() != "request") {
			ae.setScope("session");
		}

		return super.execute(ex, ae, mapping, formInstance, request, response);
	}

	private String requestContextGetter(HttpServletRequest request) {

		Enumeration requestContents = request.getAttributeNames();
		String context = "";
		while (requestContents.hasMoreElements()) {
			String requestElement = requestContents.nextElement().toString();
			context += "RequestElement:" + requestElement + "\n";
			context += "RequestElement Value:"
				+ request.getAttribute(requestElement)
				+ "\n";
		}

		return context;
	}

}
