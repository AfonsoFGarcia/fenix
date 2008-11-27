package net.sourceforge.fenixedu.applicationTier.Servico.student;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.struts.util.MessageResources;

import pt.utl.ist.fenix.tools.util.EMail;

/**
 * @author asnr and scpo
 * 
 */

public class UnEnrollStudentInGroup extends FenixService {

    public static String mailServer() {
	final String server = PropertiesManager.getProperty("mail.smtp.host");
	return (server != null) ? server : "mail.adm";
    }

    private static final MessageResources messages = MessageResources.getMessageResources("resources/GlobalResources");

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static Boolean run(String userName, Integer studentGroupCode) throws FenixServiceException {

	StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupCode);

	final List<String> emails = new ArrayList<String>();
	for (final Attends attends : studentGroup.getAttends()) {
	    emails.add(attends.getRegistration().getPerson().getEmail());
	}

	if (studentGroup == null) {
	    throw new InvalidSituationServiceException();
	}

	Registration registration = Registration.readByUsername(userName);

	Grouping groupProperties = studentGroup.getGrouping();

	Attends attend = groupProperties.getStudentAttend(registration);

	if (attend == null) {
	    throw new NotAuthorizedException();
	}

	IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();

	IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

	boolean resultEmpty = strategy.checkIfStudentGroupIsEmpty(attend, studentGroup);

	studentGroup.removeAttends(attend);

	if (resultEmpty) {
	    studentGroup.delete();
	    return Boolean.FALSE;
	}

	final StringBuilder executionCourseNames = new StringBuilder();
	for (final ExecutionCourse executionCourse : groupProperties.getExecutionCourses()) {
	    if (executionCourseNames.length() > 0) {
		executionCourseNames.append(", ");
	    }
	    executionCourseNames.append(executionCourse.getNome());
	}
	EMail.send(mailServer(), "Fenix System", messages.getMessage("suporte.mail"), messages
		.getMessage("message.subject.grouping.change"), emails, new ArrayList(), new ArrayList(), messages.getMessage(
		"message.body.grouping.change.unenrolment", registration.getNumber().toString(), studentGroup.getGroupNumber()
			.toString(), attend.getExecutionCourse().getNome()));

	return Boolean.TRUE;
    }

}