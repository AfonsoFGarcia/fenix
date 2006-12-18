package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituation;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.DateTime;

public abstract class DocumentRequest extends DocumentRequest_Base {

    protected DocumentRequest() {
	super();
    }

    @Override
    public String getDescription() {
	return getDescription("AcademicServiceRequestType.DOCUMENT", getDocumentRequestType()
		.getQualifiedName());
    }

    abstract public DocumentRequestType getDocumentRequestType();

    abstract public String getDocumentTemplateKey();

    public boolean isCertificate() {
	return this instanceof CertificateRequest;
    }

    public boolean isDeclaration() {
	return this instanceof DeclarationRequest;
    }

    public boolean isDegreeDiploma() {
	return this instanceof DegreeDiploma;
    }

    public boolean isPagedDocument() {
	return isCertificate() || isDeclaration();
    }

    public static class DocumentRequestCreator extends DocumentRequestCreateBean implements
	    FactoryExecutor {

	public DocumentRequestCreator(Registration registration) {
	    super(registration);
	}

	public Object execute() {

	    if (getChosenDocumentRequestType().isCertificate()) {
		return CertificateRequest.create(getRegistration(), getChosenDocumentRequestType(),
			getChosenDocumentPurposeType(), getOtherPurpose(), getNotes(),
			getUrgentRequest(), getAverage(), getDetailed(), getExecutionYear());

	    } else if (getChosenDocumentRequestType().isDeclaration()) {
		return DeclarationRequest.create(getRegistration(), getChosenDocumentRequestType(),
			getChosenDocumentPurposeType(), getOtherPurpose(),
			getNotes(), getAverage(), getDetailed(), getYear());

	    }
	    return null;

	}

    }

    public DateTime getCreationDate() {
	AcademicServiceRequestSituation situation = getSituationByType(AcademicServiceRequestSituationType.NEW);
	return situation != null ? situation.getCreationDate() : null;
    }

    @Override
    protected void assertProcessingStatePreConditions() throws DomainException {
	if (getRegistration().hasAnyNotPayedGratuityEvents()) {
	    throw new DomainException("DocumentRequest.registration.has.not.payed.gratuities");
	}
    }

}
