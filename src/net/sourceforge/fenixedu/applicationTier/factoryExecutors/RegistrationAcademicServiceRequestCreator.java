package net.sourceforge.fenixedu.applicationTier.factoryExecutors;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.serviceRequests.CourseGroupChangeRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRevisionRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.ExtraExamRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.FreeSolicitationAcademicRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.StudentReingressionRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.PhotocopyRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import pt.ist.fenixWebFramework.security.accessControl.Checked;

import org.joda.time.DateTime;

public class RegistrationAcademicServiceRequestCreator extends RegistrationAcademicServiceRequestCreateBean implements
	FactoryExecutor {

    public RegistrationAcademicServiceRequestCreator(final Registration registration) {
	super(registration);
    }

    @Checked("RolePredicates.MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    public Object execute() {
	final Object result;
	switch (getAcademicServiceRequestType()) {
	case REINGRESSION:
	    result = new StudentReingressionRequest(getRegistration(), ExecutionYear.readCurrentExecutionYear(),
		    getFinalRequestDate());
	    break;

	case EQUIVALENCE_PLAN:
	    result = new EquivalencePlanRequest(getRegistration(), getExecutionYear(), getFinalRequestDate());
	    break;

	case REVISION_EQUIVALENCE_PLAN:
	    result = new EquivalencePlanRevisionRequest(getEquivalencePlanRequest(), getExecutionYear(), getFinalRequestDate());
	    break;

	case COURSE_GROUP_CHANGE_REQUEST:
	    result = new CourseGroupChangeRequest(getRegistration(), getCurriculumGroup(), getCourseGroup(), ExecutionYear
		    .readCurrentExecutionYear(), getFinalRequestDate());
	    break;

	case EXTRA_EXAM_REQUEST:
	    result = new ExtraExamRequest(getRegistration(), getEnrolment(), ExecutionYear.readCurrentExecutionYear(),
		    getFinalRequestDate());
	    break;

	case FREE_SOLICITATION_ACADEMIC_REQUEST:
	    result = new FreeSolicitationAcademicRequest(getRegistration(), ExecutionYear.readCurrentExecutionYear(),
		    getFinalRequestDate(), getSubject(), getPurpose());
	    break;

	case PHOTOCOPY_REQUEST:
	    result = new PhotocopyRequest(getRegistration(), ExecutionYear.readCurrentExecutionYear(), getFinalRequestDate());
	    break;

	default:
	    result = null;
	}

	return result;
    }

    private DateTime getFinalRequestDate() {
	return getRequestDate().toDateTimeAtCurrentTime();
    }
}
