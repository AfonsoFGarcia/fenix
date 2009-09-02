package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class PendingRequest extends PendingRequest_Base {

    public PendingRequest(HttpServletRequest request) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setGenerationDate(new DateTime());
	if (request.getMethod().equalsIgnoreCase("GET")){
	    setPost(false);
	}else{
	    setPost(true);
	}
	setUrl(request.getContextPath() + request.getServletPath());
	
	for (Object object : request.getParameterMap().keySet()) {
	    String key = (String) object;
	    addPendingRequestParameter(new PendingRequestParameter(key, request.getParameter(key)));
	}
	
	for (Enumeration<String> e = request.getAttributeNames(); e.hasMoreElements() ;){
	    String key = e.nextElement();
	    Object object = request.getAttribute(key);
	    if (object.getClass().isArray()){
		for(Object value : java.util.Arrays.asList(object)){
		    PendingRequestParameter pendingRequestParameter = new PendingRequestParameter(key, (String) value);
		    pendingRequestParameter.setAttribute(true);
		    addPendingRequestParameter(pendingRequestParameter);
		}
	    }else{
		addPendingRequestParameter(new PendingRequestParameter(key, (String) object));
	    }
	    
	}

    }

    private boolean shouldBeAdded(Object attribute) {

	if (attribute instanceof Collection) {
	    Iterator iterator = ((Collection) attribute).iterator();
	    while (iterator.hasNext()) {
		if (!(iterator.next() instanceof Serializable)) {
		    return false;
		}
	    }
	}
	return attribute instanceof Serializable;
    }

    @Service
    public void delete() {
	for (PendingRequestParameter pendingRequestParameter : getPendingRequestParameter()) {
	    pendingRequestParameter.delete();
	}
	removeRootDomainObject();
	deleteDomainObject();
    }

}
