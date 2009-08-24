/*
 * @(#)RequestUtils.java Created on Oct 24, 2004
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.utils;

import java.io.IOException;
import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.PendingRequest;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.util.HostRedirector;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Luis Cruz
 * @version 1.1, Oct 24, 2004
 * @since 1.1
 * 
 */
public class RequestUtils {

    public static final int APP_CONTEXT_LENGTH = PropertiesManager.getProperty("app.context").length() + 1;

    public static String getAndSetStringToRequest(HttpServletRequest request, String name) {
	String parameter = request.getParameter(name);
	if (parameter == null) {
	    parameter = (String) request.getAttribute(name);
	}
	request.setAttribute(name, parameter);
	return parameter;
    }

    public static Collection buildExecutionDegreeLabelValueBean(Collection executionDegrees) {
	final Map duplicateDegreesMap = new HashMap();
	for (Iterator iterator = executionDegrees.iterator(); iterator.hasNext();) {
	    InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
	    InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
	    String degreeName = infoDegree.getNome();

	    if (duplicateDegreesMap.get(degreeName) == null) {
		duplicateDegreesMap.put(degreeName, new Boolean(false));
	    } else {
		duplicateDegreesMap.put(degreeName, new Boolean(true));
	    }
	}

	Collection lableValueList = CollectionUtils.collect(executionDegrees, new Transformer() {

	    public Object transform(Object arg0) {
		InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arg0;
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = infoExecutionDegree.getInfoDegreeCurricularPlan();
		InfoDegree infoDegree = infoDegreeCurricularPlan.getInfoDegree();

		StringBuilder label = new StringBuilder();
		label.append(infoDegree.getDegreeType().toString());
		label.append(" em ");
		label.append(infoDegree.getNome());
		if (((Boolean) duplicateDegreesMap.get(infoDegree.getNome())).booleanValue()) {
		    label.append(" - ");
		    label.append(infoDegreeCurricularPlan.getName());
		}

		String value = infoExecutionDegree.getIdInternal().toString();

		return new LabelValueBean(label.toString(), value);
	    }

	});

	Comparator comparator = new BeanComparator("label", Collator.getInstance());
	Collections.sort((List) lableValueList, comparator);

	return lableValueList;
    }

    public static final List buildCurricularYearLabelValueBean() {
	final List curricularYears = new ArrayList();
	curricularYears.add(new LabelValueBean("escolher", ""));
	curricularYears.add(new LabelValueBean("1 �", "1"));
	curricularYears.add(new LabelValueBean("2 �", "2"));
	curricularYears.add(new LabelValueBean("3 �", "3"));
	curricularYears.add(new LabelValueBean("4 �", "4"));
	curricularYears.add(new LabelValueBean("5 �", "5"));
	return curricularYears;
    }

    /**
     * Redirects the user to the login page saving the request state. This can
     * be used to force the user to login before a certain request is fulfilled.
     * 
     * @param request
     *            the current request
     * @param response
     *            the reponse were the redirect will be sent
     * 
     * @throws IOException
     *             when it's not possible to send the redirect to the client
     */
    public static void sendLoginRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
	request.getSession(true);
	PendingRequest pendingRequest = storeRequest(request);
	response.sendRedirect(generateRedirectLink(HostRedirector.getRedirectPageLogin(request.getRequestURL().toString()),pendingRequest));
    }
    
    private static String generateRedirectLink(String url, PendingRequest pendingRequest) {
	String param = "pendingRequest=" +  pendingRequest.getExternalId();
	if (url.contains("?")){
	    if (url.contains("&")){
		return url + "&" + param;
	    }else if (url.charAt(url.length() - 1) != '?'){
		return url + "&" + param;
	    }else{
		return url + param;
	    }
	}else{
	    return url + "?" + param;
	}
    }

    @Service
    public static PendingRequest storeRequest(HttpServletRequest request) {
	return new PendingRequest(request);
    }

    public static boolean isPrivateURI(HttpServletRequest request) {
	final String uri = request.getRequestURI().substring(APP_CONTEXT_LENGTH);

	return (uri.length() > 1 && (uri.indexOf("CSS/") == -1) && (uri.indexOf("ajax/") == -1) && (uri.indexOf("images/") == -1)
		&& (uri.indexOf("img/") == -1) && (uri.indexOf("download/") == -1) && (uri.indexOf("external/") == -1)
		&& (uri.indexOf("services/") == -1) && (uri.indexOf("index.jsp") == -1) && (uri.indexOf("index.html") == -1)
		&& (uri.indexOf("login.do") == -1) && (uri.indexOf("loginCAS.do") == -1) && (uri.indexOf("privado") == -1)
		&& (uri.indexOf("loginPage.jsp") == -1) && (uri.indexOf("loginExpired.jsp") == -1)
		&& (uri.indexOf("loginExpired.do") == -1) && (uri.indexOf("logoff.do") == -1) && (uri.indexOf("publico/") == -1)
		&& (uri.indexOf("showErrorPage.do") == -1) && (uri.indexOf("showErrorPageRegistered.do") == -1)
		&& (uri.indexOf("exceptionHandlingAction.do") == -1) && (uri.indexOf("manager/manageCache.do") == -1)
		&& (uri.indexOf("checkPasswordKerberos.do") == -1) && (uri.indexOf("siteMap.do") == -1)
		&& (uri.indexOf("changeLocaleTo.do") == -1) && (uri.indexOf("cms/forwardEmailAction.do") == -1) && (uri
		.indexOf("isAlive.do") == -1));
    }
}
