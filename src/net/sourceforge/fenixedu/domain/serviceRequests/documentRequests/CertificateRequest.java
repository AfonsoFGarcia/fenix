package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class CertificateRequest extends CertificateRequest_Base {

    protected CertificateRequest() {
        super();
    }

    protected CertificateRequest(StudentCurricularPlan studentCurricularPlan,
            AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
            String otherDocumentPurposeTypeDescription, Integer numberOfPages, Boolean urgentRequest) {
        this();

        init(studentCurricularPlan, administrativeOffice, documentPurposeType,
                otherDocumentPurposeTypeDescription, numberOfPages, urgentRequest);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan,
            AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
            String otherDocumentPurposeTypeDescription, Integer numberOfPages, Boolean urgentRequest) {

        init(studentCurricularPlan, administrativeOffice, documentPurposeType,
                otherDocumentPurposeTypeDescription, numberOfPages);

        checkParameters(urgentRequest);

        super.setUrgentRequest(urgentRequest);

    }

    private void checkParameters(Boolean urgentRequest) {
        if (urgentRequest == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.CertificateRequest.urgentRequest.cannot.be.null");
        }

    }

    @Override
    public void setUrgentRequest(Boolean urgentRequest) {
        throw new DomainException(
                "error.serviceRequests.documentRequests.CertificateRequest.cannot.modify.urgentRequest");
    }

    @Override
    public void setCertificateRequestEvent(CertificateRequestEvent certificateRequestEvent) {
        throw new DomainException(
                "error.serviceRequests.documentRequests.CertificateRequest.cannot.modify.certificateRequestEvent");
    }

    public Integer getNumberOfUnits() {
        return 0;
    }

    public boolean isUrgent() {
        return getUrgentRequest().booleanValue();
    }

}
