package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;

public class ProgramCertificateRequest extends ProgramCertificateRequest_Base {

    static public final List<RegistrationAgreement> FREE_PAYMENT_AGREEMENTS = Arrays.asList(RegistrationAgreement.AFA,
	    RegistrationAgreement.MA);

    protected ProgramCertificateRequest() {
	super();
	setNumberOfPages(0);
    }

    public ProgramCertificateRequest(final DocumentRequestCreateBean bean) {
	this();
	super.init(bean);

	checkParameters(bean);
	super.getEnrolments().addAll(bean.getEnrolments());
	super.setRequestedCycle(bean.getRequestedCycle());
    }

    @Override
    protected void checkParameters(final DocumentRequestCreateBean bean) {
	if (bean.getEnrolments().isEmpty()) {
	    throw new DomainException("error.CourseLoadRequest.invalid.number.of.enrolments");
	}

	for (final Enrolment enrolment : bean.getEnrolments()) {
	    if (!enrolment.isApproved()) {
		throw new DomainException("error.ProgramCertificateRequest.cannot.add.not.approved.enrolments");
	    }
	    if (!getStudent().hasEnrolments(enrolment)) {
		throw new DomainException("error.ProgramCertificateRequest.enrolment.doesnot.belong.to.student");
	    }
	}
    }

    @Override
    public Integer getNumberOfUnits() {
	return Integer.valueOf(0);
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.PROGRAM_CERTIFICATE;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    public EventType getEventType() {
	return EventType.PROGRAM_CERTIFICATE_REQUEST;
    }

    @Override
    public void delete() {
	getEnrolments().clear();
	super.delete();
    }

    @Override
    public boolean isFree() {
	if (FREE_PAYMENT_AGREEMENTS.contains(getRegistration().getRegistrationAgreement())) {
	    return true;
	}
	return super.isFree();
    }
}
