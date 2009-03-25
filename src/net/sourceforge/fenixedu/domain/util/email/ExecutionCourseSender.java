package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsibleTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.services.Service;

public class ExecutionCourseSender extends ExecutionCourseSender_Base {

    public ExecutionCourseSender(ExecutionCourse executionCourse) {
	super();
	setCourse(executionCourse);
	setFromName(executionCourse.getNome());
	setFromAddress("noreply@ist.utl.pt");
	addReplyTos(new ExecutionCourseReplyTo());
	// why not create PersonReplyTo ?
	addReplyTos(new CurrentUserReplyTo());
	setMembers(new ExecutionCourseTeachersGroup(executionCourse));
	final String labelECTeachers = RenderUtils.getResourceString("SITE_RESOURCES",
		"label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroupWithName",
		new Object[] { executionCourse.getNome() });
	final String labelECStudents = RenderUtils.getResourceString("SITE_RESOURCES",
		"label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroupWithName",
		new Object[] { executionCourse.getNome() });
	final String labelECResponsibleTeachers = RenderUtils.getResourceString("SITE_RESOURCES",
		"label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsibleTeachersGroupWithName",
		new Object[] { executionCourse.getNome() });
	addRecipients(new Recipient(labelECTeachers, new ExecutionCourseTeachersGroup(executionCourse)));
	addRecipients(new Recipient(labelECStudents, new ExecutionCourseStudentsGroup(executionCourse)));
	addRecipients(new Recipient(labelECResponsibleTeachers, new ExecutionCourseResponsibleTeachersGroup(executionCourse)));
    }

    @Service
    public static ExecutionCourseSender newInstance(ExecutionCourse ec) {
	ExecutionCourseSender sender = (ExecutionCourseSender) ec.getSender();
	return sender == null ? new ExecutionCourseSender(ec) : sender;
    }

}
