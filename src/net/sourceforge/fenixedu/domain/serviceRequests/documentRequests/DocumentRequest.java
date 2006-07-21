package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class DocumentRequest extends DocumentRequest_Base {

	protected DocumentRequest() {
		super();
	}

	public DocumentRequest(StudentCurricularPlan studentCurricularPlan,
			AdministrativeOffice administrativeOffice, DocumentRequestType documentRequestType,
			DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
			Integer numberOfPages) {
		this();
		init(studentCurricularPlan, administrativeOffice, documentRequestType, documentPurposeType,
				otherDocumentPurposeTypeDescription, numberOfPages);
	}

	private void checkParameters(DocumentRequestType documentRequestType,
			DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
			Integer numberOfPages) {

		if (documentRequestType == null) {
			throw new DomainException(
					"error.serviceRequests.documentRequests.DocumentRequest.documentRequestType.cannot.be.null");
		}

		if (documentPurposeType == null) {
			throw new DomainException(
					"error.serviceRequests.documentRequests.DocumentRequest.documentPurposeType.cannot.be.null");
		}

		if (documentPurposeType == DocumentPurposeType.OTHER
				&& otherDocumentPurposeTypeDescription == null) {
			throw new DomainException(
					"error.serviceRequests.documentRequests.DocumentRequest.otherDocumentPurposeTypeDescription.cannot.be.null.for.other.purpose.type");
		}

		if (numberOfPages == null) {
			throw new DomainException(
					"error.serviceRequests.documentRequests.DocumentRequest.numberOfPages.cannot.be.null");
		}

	}

	protected void init(StudentCurricularPlan studentCurricularPlan,
			AdministrativeOffice administrativeOffice, DocumentRequestType documentRequestType,
			DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
			Integer numberOfPages) {

		init(studentCurricularPlan, administrativeOffice);

		checkParameters(documentRequestType, documentPurposeType, otherDocumentPurposeTypeDescription,
				numberOfPages);

		super.setDocumentRequestType(documentRequestType);
		super.setDocumentPurposeType(documentPurposeType);
		super.setOtherDocumentPurposeTypeDescription(otherDocumentPurposeTypeDescription);
		super.setNumberOfPages(numberOfPages);
	}

	@Override
	public void setDocumentRequestType(DocumentRequestType documentRequestType) {
		throw new DomainException(
				"error.serviceRequests.documentRequests.DocumentRequest.cannot.modify.documentRequestType");
	}

	@Override
	public void setDocumentPurposeType(DocumentPurposeType documentPurposeType) {
		throw new DomainException(
				"error.serviceRequests.documentRequests.DocumentRequest.cannot.modify.documentPurposeType");
	}

	@Override
	public void setOtherDocumentPurposeTypeDescription(String otherDocumentTypeDescription) {
		throw new DomainException(
				"error.serviceRequests.documentRequests.DocumentRequest.cannot.modify.otherDocumentTypeDescription");
	}

	@Override
	public void setNumberOfPages(Integer numberOfPages) {
		throw new DomainException(
				"error.serviceRequests.documentRequests.enclosing_type.cannot.modify.numberOfPages");
	}

}
