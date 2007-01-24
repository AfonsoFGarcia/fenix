package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class SchoolRegistrationDeclarationRequest extends SchoolRegistrationDeclarationRequest_Base {

    public SchoolRegistrationDeclarationRequest() {
	super();
    }

    public SchoolRegistrationDeclarationRequest(Registration registration,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription) {

	this();
	this.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription);
    }

    protected void init(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription) {

	super.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription);

        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        if (!getRegistration().isInRegisteredState(currentExecutionYear)) {
            throw new DomainException("SchoolRegistrationDeclarationRequest.registration.not.in.registered.state.in.current.executionYear");
        }
        super.setExecutionYear(currentExecutionYear);
    }

    @Override
    public Set<AdministrativeOfficeType> getPossibleAdministrativeOffices() {
	final Set<AdministrativeOfficeType> result = new HashSet<AdministrativeOfficeType>();

	result.add(AdministrativeOfficeType.DEGREE);

	return result;
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.SchoolRegistrationDeclarationRequest.cannot.modify.executionYear");
    }
    
    @Override
    public EventType getEventType() {
	return EventType.SCHOOL_REGISTRATION_DECLARATION_REQUEST;
    }

}
