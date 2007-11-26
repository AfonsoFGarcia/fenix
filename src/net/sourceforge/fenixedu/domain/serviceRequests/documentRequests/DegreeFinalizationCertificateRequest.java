package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.MobilityProgram;
import net.sourceforge.fenixedu.domain.student.Registration;

public class DegreeFinalizationCertificateRequest extends DegreeFinalizationCertificateRequest_Base {

    private DegreeFinalizationCertificateRequest() {
	super();
    }

    public DegreeFinalizationCertificateRequest(final Registration registration,
	    final DocumentPurposeType documentPurposeType, final String otherDocumentPurposeTypeDescription,
	    final Boolean urgentRequest, final Boolean average, final Boolean detailed, MobilityProgram mobilityProgram) {
	this();

	this.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest,
		average, detailed, mobilityProgram);
    }

    final protected void init(final Registration registration, final DocumentPurposeType documentPurposeType,
	    final String otherDocumentPurposeTypeDescription, final Boolean urgentRequest, final Boolean average,
	    final Boolean detailed, final MobilityProgram mobilityProgram) {
	
	super.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);

	this.checkParameters(average, detailed, mobilityProgram);
	super.setAverage(average);
	super.setDetailed(detailed);
	super.setMobilityProgram(mobilityProgram);
    }

    final private void checkParameters(final Boolean average, final Boolean detailed, final MobilityProgram mobilityProgram) {
	if (average == null) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.average.cannot.be.null");
	}
	if (detailed == null) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.detailed.cannot.be.null");
	}
	if (mobilityProgram == null && getRegistration().hasAnyExternalEnrolments()) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.mobility.program.cannot.be.null.for.registration.with.external.enrolments");
	}
    }

    @Override
    final protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	super.internalChangeState(academicServiceRequestBean);

	if (academicServiceRequestBean.isToProcess()) {
	    if (!getRegistration().isConcluded()) {
		throw new DomainException("DegreeFinalizationCertificateRequest.registration.is.not.concluded");
	    }
	    
	    if (!getRegistration().hasDiplomaRequest()) {
		throw new DomainException("DegreeFinalizationCertificateRequest.registration.withoutDiplomaRequest");
	    }
	    
	    if (NOT_AVAILABLE.contains(getRegistration().getDegreeType())) {
		throw new DomainException("DegreeFinalizationCertificateRequest.not.available");
	    }
	}
    }
    
    static final private List<DegreeType> NOT_AVAILABLE = Arrays.asList(new DegreeType[] {
	    DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE});
    
    @Override
    final public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE;
    }

    @Override
    final public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    final public void setAverage(final Boolean average) {
	throw new DomainException("DegreeFinalizationCertificateRequest.cannot.modify.average");
    }

    @Override
    final public void setDetailed(final Boolean detailed) {
	throw new DomainException("DegreeFinalizationCertificateRequest.cannot.modify.detailed");
    }

    @Override
    final public EventType getEventType() {
	return EventType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST;
    }

    @Override
    final public Integer getNumberOfUnits() {
	return getDetailed() ? getRegistration().getCurriculum().getCurriculumEntries().size() : 0;
    }

}
