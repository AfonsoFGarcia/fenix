package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class PendingRequest extends PendingRequest_Base {

    public PendingRequest(HttpServletRequest request) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setGenerationDate(new DateTime());
	if (request.getMethod().equalsIgnoreCase("GET")) {
	    setPost(false);
	} else {
	    setPost(true);
	}
	setUrl(request.getContextPath() + request.getServletPath());

	for (Object object : request.getParameterMap().keySet()) {
	    String key = (String) object;
	    addPendingRequestParameter(new PendingRequestParameter(key, request.getParameter(key), false));
	}

	for (Enumeration<String> e = request.getAttributeNames(); e.hasMoreElements();) {
	    String key = e.nextElement();
	    Object object = request.getAttribute(key);
	    if (object.getClass().isArray()) {
		for (Object value : java.util.Arrays.asList(object)) {
		    PendingRequestParameter pendingRequestParameter = new PendingRequestParameter(key, (String) value, true);
		    addPendingRequestParameter(pendingRequestParameter);
		}
	    } else if (object instanceof String) {
		addPendingRequestParameter(new PendingRequestParameter(key, (String) object, true));
	    } else {
		 // Not sure how to procede here...
		
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
