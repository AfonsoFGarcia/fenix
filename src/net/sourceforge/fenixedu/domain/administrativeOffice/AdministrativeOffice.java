package net.sourceforge.fenixedu.domain.administrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class AdministrativeOffice extends AdministrativeOffice_Base {

    private AdministrativeOffice() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public AdministrativeOffice(AdministrativeOfficeType administrativeOfficeType, Unit unit) {
        this();
        init(administrativeOfficeType, unit);
    }

    private void checkParameters(AdministrativeOfficeType administrativeOfficeType, Unit unit) {
        if (administrativeOfficeType == null) {
            throw new DomainException(
                    "error.administrativeOffice.AdministrativeOffice.administrativeOfficeType.cannot.be.null");
        }
        if (unit == null) {
            throw new DomainException(
                    "error.administrativeOffice.AdministrativeOffice.unit.cannot.be.null");
        }

        checkIfExistsAdministrativeOfficeForType(administrativeOfficeType);
    }

    private void checkIfExistsAdministrativeOfficeForType(
            AdministrativeOfficeType administrativeOfficeType) {

        for (final AdministrativeOffice administrativeOffice : RootDomainObject.getInstance()
                .getAdministrativeOffices()) {
            if (administrativeOffice.getAdministrativeOfficeType() == administrativeOfficeType) {
                throw new DomainException(
                        "error.administrativeOffice.AdministrativeOffice.already.exists.with.administrativeOfficeType");
            }
        }
    }
    
    protected void init(AdministrativeOfficeType administrativeOfficeType, Unit unit) {
        checkParameters(administrativeOfficeType, unit);
        super.setAdministrativeOfficeType(administrativeOfficeType);
        super.setUnit(unit);

    }

    @Override
    public void setAdministrativeOfficeType(AdministrativeOfficeType administrativeOfficeType) {
        throw new DomainException(
                "error.administrativeOffice.AdministrativeOffice.cannot.modify.administrativeOfficeType");
    }

    @Override
    public void setUnit(Unit unit) {
        throw new DomainException("error.administrativeOffice.AdministrativeOffice.cannot.modify.unit");
    }
    
    public List<DocumentRequest> searchDocumentsBy(DocumentRequestType documentRequestType,
            AcademicServiceRequestSituationType requestSituationType, Boolean isUrgent, Registration registration) {

        final List<DocumentRequest> result = new ArrayList<DocumentRequest>();

        for (final AcademicServiceRequest serviceRequest : getAcademicServiceRequestsSet()) {

            if (serviceRequest.isNewRequest()) {
                continue;
            }

            if (serviceRequest instanceof DocumentRequest) {

                final DocumentRequest documentRequest = (DocumentRequest) serviceRequest;

                if (documentRequestType != null
                        && documentRequest.getDocumentRequestType() != documentRequestType) {
                    continue;
                }

                if (requestSituationType != null
                        && documentRequest.getAcademicServiceRequestSituationType() != requestSituationType) {
                    continue;
                }

                if (isUrgent != null && documentRequest.isUrgentRequest() != isUrgent.booleanValue()) {
                    continue;
                }

                if (registration != null && documentRequest.getStudent() != registration) {
                    continue;
                }

                result.add(documentRequest);
            }
        }

        return result;
    }

    public List<AcademicServiceRequest> getNewAcademicServiceRequests() {
    	
        final List<AcademicServiceRequest> result = new ArrayList<AcademicServiceRequest>();
        for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequests()) {
            if (academicServiceRequest.isNewRequest()) {
                result.add(academicServiceRequest);
            }
        }
        return result;
    }

    // static methods
    public static AdministrativeOffice readByAdministrativeOfficeType(
            AdministrativeOfficeType administrativeOfficeType) {

        for (final AdministrativeOffice administrativeOffice : RootDomainObject.getInstance()
                .getAdministrativeOffices()) {

            if (administrativeOffice.getAdministrativeOfficeType() == administrativeOfficeType) {
                return administrativeOffice;
            }

        }
        return null;

    }

    public static AdministrativeOffice getResponsibleAdministrativeOffice(DegreeType degreeType) {

        switch (degreeType) {
        case DEGREE:
            return readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);

        case MASTER_DEGREE:
        case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
            return readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE);
        }

        return null;
    }

}
