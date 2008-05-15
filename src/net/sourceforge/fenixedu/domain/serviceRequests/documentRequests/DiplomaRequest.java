package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.DiplomaRequestEvent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.joda.time.DateTime;

public class DiplomaRequest extends DiplomaRequest_Base {

    private DiplomaRequest() {
	super();
    }

    public DiplomaRequest(final Registration registration, final CycleType requestedCycle) {
	this(registration, new DateTime(), requestedCycle);
    }

    public DiplomaRequest(final Registration registration, final DateTime requestDate, final CycleType requestedCycle) {
	this();

	this.init(registration, requestDate, requestedCycle);
    }

    final private void init(final Registration registration, DateTime requestDate, final CycleType requestedCycle) {
	super.init(registration, requestDate, Boolean.FALSE, Boolean.FALSE);

	this.checkParameters(requestedCycle);

	if (isPayedUponCreation() && !isFree()) {
	    DiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
	}
    }

    final private void checkParameters(final CycleType requestedCycle) {
	if (getDegreeType().isComposite()) {
	    if (requestedCycle == null) {
		throw new DomainException("DiplomaRequest.diploma.requested.cycle.must.be.given");
	    } else if (!getDegreeType().getCycleTypes().contains(requestedCycle)) {
		throw new DomainException(
			"DiplomaRequest.diploma.requested.degree.type.is.not.allowed.for.given.student.curricular.plan");
	    }

	    super.setRequestedCycle(requestedCycle);
	}

	checkForDuplicate(requestedCycle);
    }

    private void checkForDuplicate(final CycleType requestedCycle) {
	final DiplomaRequest diplomaRequest = getRegistration().getDiplomaRequest(requestedCycle);
	if (diplomaRequest != null && diplomaRequest != this) {
	    throw new DomainException("DiplomaRequest.diploma.already.requested");
	}
    }

    @Override
    final public void setRequestedCycle(final CycleType requestedCycle) {
	throw new DomainException("DiplomaRequest.cannot.modify.requestedCycle");
    }

    @Override
    final public String getDescription() {
	final DegreeType degreeType = getDegreeType();
	final CycleType requestedCycle = getRequestedCycle();

	return getDescription(getAcademicServiceRequestType(), getDocumentRequestType().getQualifiedName() + "."
		+ degreeType.name() + (degreeType.isComposite() ? "." + requestedCycle.name() : ""));
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.DIPLOMA_REQUEST;
    }

    @Override
    final public String getDocumentTemplateKey() {
	String result = getClass().getName() + "." + getDegreeType().getName();
	if (getRequestedCycle() != null) {
	    result += "." + getRequestedCycle().name();
	}

	return result;
    }

    @Override
    final public EventType getEventType() {
	switch (getDegreeType()) {
	case DEGREE:
	case BOLONHA_DEGREE:
	    return EventType.BOLONHA_DEGREE_DIPLOMA_REQUEST;
	case MASTER_DEGREE:
	case BOLONHA_MASTER_DEGREE:
	    return EventType.BOLONHA_MASTER_DEGREE_DIPLOMA_REQUEST;
	case BOLONHA_INTEGRATED_MASTER_DEGREE:
	    return (getRequestedCycle() == CycleType.FIRST_CYCLE) ? EventType.BOLONHA_DEGREE_DIPLOMA_REQUEST
		    : EventType.BOLONHA_MASTER_DEGREE_DIPLOMA_REQUEST;
	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
	    return EventType.BOLONHA_ADVANCED_FORMATION_DIPLOMA_REQUEST;
	case BOLONHA_PHD_PROGRAM:
	    return EventType.BOLONHA_PHD_PROGRAM_DIPLOMA_REQUEST;
	default:
	    throw new DomainException("DiplomaRequest.not.available.for.given.degree.type");
	}
    }

    @Override
    final protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	if (academicServiceRequestBean.isToProcess()) {
	    if (NOT_AVAILABLE.contains(getRegistration().getDegreeType())) {
		throw new DomainException("DiplomaRequest.diploma.not.available");
	    }

	    checkForDuplicate(getRequestedCycle());

	    if (!getRegistration().isRegistrationConclusionProcessed(getRequestedCycle())) {
		throw new DomainException("DiplomaRequest.registration.not.submited.to.conclusion.process");
	    }

	    if (hasDissertationTitle() && !getRegistration().hasDissertationThesis()) {
		throw new DomainException("DiplomaRequest.registration.doesnt.have.dissertation.thesis");
	    }

	    if (!getFreeProcessed()) {
		if (hasCycleCurriculumGroup()) {
		    assertPayedEvents(getCycleCurriculumGroup().getIEnrolmentsLastExecutionYear());
		} else {
		    assertPayedEvents();
		}
	    }

	    if (isPayable() && !isPayed()) {
		throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
	    }
	}

	if (academicServiceRequestBean.isToConclude() && !isFree() && !isPayedUponCreation()) {
	    DiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
	}

	if (academicServiceRequestBean.isToCancelOrReject() && hasEvent()) {
	    getEvent().cancel(academicServiceRequestBean.getEmployee());
	}
    }

    static final private List<DegreeType> NOT_AVAILABLE = Arrays.asList(new DegreeType[] { DegreeType.BOLONHA_PHD_PROGRAM,
	    DegreeType.BOLONHA_SPECIALIZATION_DEGREE });

    final public boolean hasFinalAverageDescription() {
	return !hasDissertationTitle();
    }

    final public boolean hasDissertationTitle() {
	return getDegreeType() == DegreeType.MASTER_DEGREE;
    }

    /* TODO refactor, always set requested cycle type in document creation */

    public CycleType getWhatShouldBeRequestedCycle() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getCycleType() : null;
    }

    public CycleCurriculumGroup getCycleCurriculumGroup() {
	final CycleType requestedCycle = getRequestedCycle();
	final Registration registration = getRegistration();

	if (requestedCycle == null) {
	    if (registration.getDegreeType().hasExactlyOneCycleType()) {
		return registration.getLastStudentCurricularPlan().getLastOrderedCycleCurriculumGroup();
	    } else {
		return null;
	    }
	} else {
	    return registration.getLastStudentCurricularPlan().getCycle(requestedCycle);
	}
    }

    public boolean hasCycleCurriculumGroup() {
	return getCycleCurriculumGroup() != null;
    }

    @Override
    public boolean hasPersonalInfo() {
	return true;
    }

    @Override
    public boolean isPagedDocument() {
	return false;
    }

    @Override
    public boolean isToPrint() {
	return !isDelivered();
    }

    @Override
    protected List<AcademicServiceRequestSituationType> getProcessingSituationAcceptedSituationsTypes() {
	return Collections.unmodifiableList(Arrays.asList(AcademicServiceRequestSituationType.CANCELLED,
		AcademicServiceRequestSituationType.REJECTED, AcademicServiceRequestSituationType.CONCLUDED));
    }

    @Override
    protected List<AcademicServiceRequestSituationType> getReceivedFromExternalEntitySituationAcceptedSituationsTypes() {
	return Collections.unmodifiableList(Arrays.asList(AcademicServiceRequestSituationType.DELIVERED));
    }

    @Override
    protected List<AcademicServiceRequestSituationType> getConcludedSituationAcceptedSituationsTypes() {
	return Collections.unmodifiableList(Arrays.asList(AcademicServiceRequestSituationType.DELIVERED,
		AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY));
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return true;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return false;
    }

    @Override
    public boolean isPayedUponCreation() {
	return getDegreeType() != DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA;
    }

}
