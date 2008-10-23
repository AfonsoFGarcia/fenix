package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.DeclarationRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

abstract public class DeclarationRequest extends DeclarationRequest_Base {

    private static final int MAX_FREE_DECLARATIONS_PER_EXECUTION_YEAR = 4;

    protected DeclarationRequest() {
	super();
	super.setNumberOfPages(0);
    }

    @Override
    final protected void init(final DocumentRequestCreateBean bean) {
	bean.setRequestDate(new DateTime());
	bean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	super.init(bean);

	super.checkParameters(bean);
	super.setDocumentPurposeType(bean.getChosenDocumentPurposeType());
	super.setOtherDocumentPurposeTypeDescription(bean.getOtherPurpose());
    }

    static final protected DeclarationRequest create(final DocumentRequestCreateBean bean) {
	switch (bean.getChosenDocumentRequestType()) {
	case SCHOOL_REGISTRATION_DECLARATION:
	    return new SchoolRegistrationDeclarationRequest(bean);
	case ENROLMENT_DECLARATION:
	    return new EnrolmentDeclarationRequest(bean);
	case IRS_DECLARATION:
	    return new IRSDeclarationRequest(bean);
	}

	return null;
    }

    @Override
    final public void setDocumentPurposeType(DocumentPurposeType documentPurposeType) {
	throw new DomainException("error.serviceRequests.documentRequests.DeclarationRequest.cannot.modify.documentPurposeType");
    }

    @Override
    final public void setOtherDocumentPurposeTypeDescription(String otherDocumentTypeDescription) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.DeclarationRequest.cannot.modify.otherDocumentTypeDescription");
    }

    @Override
    final public Boolean getUrgentRequest() {
	return Boolean.FALSE;
    }

    final public void edit(final DocumentRequestBean documentRequestBean) {

	if (isPayable() && isPayed() && !getNumberOfPages().equals(documentRequestBean.getNumberOfPages())) {
	    throw new DomainException("error.serviceRequests.documentRequests.cannot.change.numberOfPages.on.payed.documents");
	}

	super.edit(documentRequestBean);
	super.setNumberOfPages(documentRequestBean.getNumberOfPages());
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	super.internalChangeState(academicServiceRequestBean);

	if (academicServiceRequestBean.isToConclude()) {
	    if (getNumberOfPages() == null || getNumberOfPages().intValue() == 0) {
		throw new DomainException("error.serviceRequests.documentRequests.numberOfPages.must.be.set");
	    }

	    if (!isFree()) {
		new DeclarationRequestEvent(getAdministrativeOffice(), getEventType(), getRegistration().getPerson(), this);
	    }
	}
    }

    /**
     * Important: Notice that this method's return value may not be the same
     * before and after conclusion of the academic service request.
     */
    @Override
    final public boolean isFree() {
	if (getDocumentPurposeType() == DocumentPurposeType.PPRE) {
	    return false;
	}

	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	final Set<DocumentRequest> schoolRegistrationDeclarations = getRegistration().getSucessfullyFinishedDocumentRequestsBy(
		currentExecutionYear, DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION, false);

	final Set<DocumentRequest> enrolmentDeclarations = getRegistration().getSucessfullyFinishedDocumentRequestsBy(
		currentExecutionYear, DocumentRequestType.ENROLMENT_DECLARATION, false);

	return super.isFree()
		|| ((schoolRegistrationDeclarations.size() + enrolmentDeclarations.size()) < MAX_FREE_DECLARATIONS_PER_EXECUTION_YEAR);
    }

    @Override
    public boolean isPayedUponCreation() {
	return false;
    }

    @Override
    public boolean isPagedDocument() {
	return true;
    }

    @Override
    public boolean isToPrint() {
	return true;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return false;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return false;
    }

    @Override
    public boolean hasPersonalInfo() {
	return false;
    }

}
