package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class RectorateSubmissionBatch extends RectorateSubmissionBatch_Base {
    public RectorateSubmissionBatch(InstitutionRegistryCodeGenerator registryCodeGenerator) {
	super();
	setCreation(new DateTime());
	setState(RectorateSubmissionState.UNSENT);
	setRegistryCodeGenerator(registryCodeGenerator);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Set<DocumentRequest> getDocumentRequests() {
	Set<DocumentRequest> requests = new HashSet<DocumentRequest>();
	for (RegistryCode code : getRegistryCodeSet()) {
	    requests.addAll(code.getDocumentRequestSet());
	}
	return requests;
    }

    public Integer getDocumentRequestCount() {
	return getDocumentRequests().size();
    }

    public String getRange() {
	String first = null;
	String last = null;
	for (RegistryCode code : getRegistryCodeSet()) {
	    if (first == null || code.getCode().compareTo(first) < 0) {
		first = code.getCode();
	    }
	    if (last == null || code.getCode().compareTo(last) > 0) {
		last = code.getCode();
	    }
	}
	if (first != null && last != null) {
	    return first + "-" + last;
	} else {
	    return "-";
	}
    }

    @Service
    public void closeBag() {
	if (!getState().equals(RectorateSubmissionState.UNSENT))
	    throw new DomainException("error.rectorateSubmission.attemptingToCloseABatchNotInUnsentState");
	setState(RectorateSubmissionState.CLOSED);
	new RectorateSubmissionBatch(getRegistryCodeGenerator());
    }

    @Service
    public void markAsSent() {
	if (!getState().equals(RectorateSubmissionState.CLOSED))
	    throw new DomainException("error.rectorateSubmission.attemptingToSendABatchNotInClosedState");
	setState(RectorateSubmissionState.SENT);
	setSubmission(new DateTime());
	Employee employee = AccessControl.getPerson().getEmployee();
	for (RegistryCode code : getRegistryCodeSet()) {
	    for (DocumentRequest document : code.getDocumentRequestSet()) {
		document.edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.CONCLUDED, employee,
			"Insert Template Text Here"));
		document.edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY,
			employee, "Insert Template Text Here"));
	    }
	}
    }

    @Service
    public void markAsReceived() {
	if (!getState().equals(RectorateSubmissionState.SENT))
	    throw new DomainException("error.rectorateSubmission.attemptingToReceiveABatchNotInSentState");
	setState(RectorateSubmissionState.RECEIVED);
	setReception(new DateTime());
	Employee employee = AccessControl.getPerson().getEmployee();
	for (RegistryCode code : getRegistryCodeSet()) {
	    for (DocumentRequest document : code.getDocumentRequestSet()) {
		document.edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY,
			employee));
	    }
	}
    }
}
