package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.serviceRequests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class AcademicServiceRequestsManagementDispatchAction extends FenixDispatchAction {

    private AcademicServiceRequest getAndSetAcademicServiceRequest(final HttpServletRequest request) {
	Integer academicServiceRequestId = getRequestParameterAsInteger(request,
		"academicServiceRequestId");
	if (academicServiceRequestId == null) {
	    academicServiceRequestId = (Integer) request.getAttribute("academicServiceRequestId");
	}
	final AcademicServiceRequest academicServiceRequest = rootDomainObject
		.readAcademicServiceRequestByOID(academicServiceRequestId);
	request.setAttribute("academicServiceRequest", academicServiceRequest);
	return academicServiceRequest;
    }

    private Registration getAndSetRegistration(final HttpServletRequest request) {
	final Registration registration = rootDomainObject
		.readRegistrationByOID(getRequestParameterAsInteger(request, "registrationID"));
	request.setAttribute("registration", registration);
	return registration;
    }

    private String getAndSetUrl(ActionForm actionForm, HttpServletRequest request) {
	final StringBuilder result = new StringBuilder();

	if (!StringUtils.isEmpty(request.getParameter("backAction"))) {
	    result.append("/").append(request.getParameter("backAction")).append(".do?");

	    if (!StringUtils.isEmpty(request.getParameter("backMethod"))) {
		result.append("method=").append(request.getParameter("backMethod"));
	    }
	}

	request.setAttribute("url", result.toString());
	return result.toString();
    }

    public ActionForward viewRegistrationAcademicServiceRequestsHistoric(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("registration", getAndSetRegistration(request));
	return mapping.findForward("viewRegistrationAcademicServiceRequestsHistoric");
    }

    public ActionForward viewAcademicServiceRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	getAndSetAcademicServiceRequest(request);
	getAndSetUrl(form, request);
	return mapping.findForward("viewAcademicServiceRequest");
    }

    public ActionForward processNewAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);

	try {
	    executeService("ProcessNewAcademicServiceRequests", academicServiceRequest);
	    addActionMessage(request, "academic.service.request.processed.with.success");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    request.setAttribute("failingCondition", ex.getKey());
	    return mapping.findForward("prepareRejectAcademicServiceRequest");
	}
	
	if (academicServiceRequest.isDocumentRequest() && ((DocumentRequest) academicServiceRequest).getDocumentRequestType().isAllowedToQuickDeliver()) {
	    return prepareConcludeAcademicServiceRequest(mapping, actionForm, request, response);
	} else if (request.getParameter("academicSituationType") != null) {
	    return search(mapping, actionForm, request, response);
	} else {
	    request.setAttribute("registration", academicServiceRequest.getRegistration());
	    return mapping.findForward("viewRegistrationDetails");
	}
    }

    public ActionForward prepareRejectAcademicServiceRequest(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	    throws FenixFilterException, FenixServiceException {

	getAndSetAcademicServiceRequest(request);
	return mapping.findForward("prepareRejectAcademicServiceRequest");
    }

    public ActionForward rejectAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
	final String justification = ((DynaActionForm) actionForm).getString("justification");

	try {
	    ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request),
		    "RejectAcademicServiceRequest",
		    new Object[] { academicServiceRequest, justification });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex
		    .getLabelFormatterArgs()));
	    return mapping.findForward("prepareRejectAcademicServiceRequest");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    return mapping.findForward("prepareRejectAcademicServiceRequest");
	}

	request.setAttribute("registration", academicServiceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareCancelAcademicServiceRequest(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	    throws FenixFilterException, FenixServiceException {

	getAndSetAcademicServiceRequest(request);
	return mapping.findForward("prepareCancelAcademicServiceRequest");
    }

    public ActionForward cancelAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
	final String justification = ((DynaActionForm) actionForm).getString("justification");

	try {
	    ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request),
		    "CancelAcademicServiceRequest",
		    new Object[] { academicServiceRequest, justification });
	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex
		    .getLabelFormatterArgs()));
	    return mapping.findForward("prepareCancelAcademicServiceRequest");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    return mapping.findForward("prepareCancelAcademicServiceRequest");
	}

	request.setAttribute("registration", academicServiceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward prepareConcludeAcademicServiceRequest(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	    throws FenixFilterException, FenixServiceException {

	final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);

	if (academicServiceRequest.isDocumentRequest()) {
	    return mapping.findForward("prepareConcludeDocumentRequest");
	} else {
	    request.setAttribute("registration", academicServiceRequest.getRegistration());
	    return mapping.findForward("viewRegistrationDetails");
	}
    }

    public ActionForward concludeAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);

	try {
	    ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request),
		    "ConcludeAcademicServiceRequest", new Object[] { academicServiceRequest });
	    addActionMessage(request, "academic.service.request.concluded.with.success");
	    
	    if (academicServiceRequest.isDocumentRequest() && ((DocumentRequest) academicServiceRequest).getDocumentRequestType().isAllowedToQuickDeliver()) {
		return deliveredAcademicServiceRequest(mapping, actionForm, request, response);
	    }
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	}
	
	request.setAttribute("registration", academicServiceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward deliveredAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
	
	try {
	    ServiceManagerServiceFactory.executeService(SessionUtils.getUserView(request),
		    "DeliveredAcademicServiceRequest", new Object[] { academicServiceRequest });
	    addActionMessage(request, "academic.service.request.delivered.with.success");
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	}

	request.setAttribute("registration", academicServiceRequest.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final AcademicServiceRequestSituationType academicServiceRequestSituationType = AcademicServiceRequestSituationType.valueOf(request.getParameter("academicSituationType"));
	request.setAttribute("academicSituationType", academicServiceRequestSituationType);
	
	final Collection<AcademicServiceRequest> academicServiceRequests = getAdministrativeOffice().searchAcademicServiceRequests(
		(Employee) null,
		academicServiceRequestSituationType,
		(Registration) null,
		(Boolean) null,
		(DocumentRequestType) null,
		getEmployee().getCurrentCampus());
	
	final Collection<AcademicServiceRequest> employeeRequests = new ArrayList<AcademicServiceRequest>();
	if (academicServiceRequestSituationType != AcademicServiceRequestSituationType.NEW) {
	    for (Iterator iter = academicServiceRequests.iterator(); iter.hasNext();) {
		AcademicServiceRequest academicServiceRequest = (AcademicServiceRequest) iter.next();
		if (academicServiceRequest.getActiveSituation().getEmployee() == getEmployee()) {
		    iter.remove();
		    employeeRequests.add(academicServiceRequest);
		}
	    }
	}

	request.setAttribute("academicServiceRequests", academicServiceRequests);
	request.setAttribute("employeeRequests", employeeRequests);
	request.setAttribute("employee", getEmployee());
	return mapping.findForward("searchResults");
    }

    private Employee getEmployee() {
	return AccessControl.getPerson().getEmployee();
    }   

    private AdministrativeOffice getAdministrativeOffice() {
	return getEmployee().getAdministrativeOffice();
    }

}
