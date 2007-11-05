package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.InternalDegreeChangeRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class InternalDegreeChangeRequest extends InternalDegreeChangeRequest_Base {
    
    protected InternalDegreeChangeRequest() {
	super();
    }
    
    public InternalDegreeChangeRequest(final Registration registration, final DegreeCurricularPlan destinationDegreeCurricularPlan, final Boolean urgentRequest, final Boolean freeProcessed) {
	this();
	init(registration, destinationDegreeCurricularPlan, urgentRequest, freeProcessed);
    }
    
    protected void init(final Registration registration, final DegreeCurricularPlan destination, final Boolean urgentRequest, final Boolean freeProcessed) {
	super.init(registration, urgentRequest, freeProcessed);
	checkParameters(destination);
	super.setDestination(destination);
    }
    
    private void checkParameters(final DegreeCurricularPlan sourceDegreeCurricularPlan) {
	if (sourceDegreeCurricularPlan == null) {
	    throw new DomainException("error.serviceRequests.PersonAcademicServiceRequest.sourceDegreeCurricularPlan.cannot.be.null");
	}
    }

    @Override
    public void setDestination(DegreeCurricularPlan destination) {
        throw new DomainException("error.serviceRequests.ExternalDegreeChangeRequest.cannot.modify.destinationDegreeCurricularPlan");
    }
    
    @Override
    public EventType getEventType() {
	return EventType.INTERNAL_DEGREE_CHANGE_REQUEST;
    }
    
    @Override
    public String getDescription() {
	return getDescription("AcademicServiceRequestType.DEGREE_CHANGE");
    }

    @Override
    public ExecutionYear getExecutionYear() {
	return null;
    }

    @Override
    protected void internalChangeState(final AcademicServiceRequestSituationType academicServiceRequestSituationType, final Employee employee) {
	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (isToProcessing(academicServiceRequestSituationType)) {
	    if (hasEvent()) {
		throw new DomainException("error.InternalDegreeChangeRequest.already.has.event");
	    }
	    new InternalDegreeChangeRequestEvent(getAdministrativeOffice(), getPerson(), this);
	}
    }

    @Override
    public void delete() {
        super.setDestination(null);
        super.delete();
    }
}
