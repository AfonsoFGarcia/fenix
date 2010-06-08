package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import java.util.Collections;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.permissions.PhdPermissionType;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.util.phd.PhdProperties;

abstract public class PhdThesisActivity extends Activity<PhdThesisProcess> {

    protected PhdPermissionType getThesisProcessPermission() {
	return PhdPermissionType.THESIS_PROCESS_MANAGEMENT;
    }

    @Override
    final public void checkPreConditions(final PhdThesisProcess process, final IUserView userView) {
	processPreConditions(process, userView);
	activityPreConditions(process, userView);
    }

    protected void processPreConditions(final PhdThesisProcess process, final IUserView userView) {
    }

    abstract protected void activityPreConditions(final PhdThesisProcess process, final IUserView userView);

    protected String getAccessInformation(PhdIndividualProgramProcess process, PhdParticipant participant,
	    String coordinatorMessage, String teacherMessage) {

	if (!participant.isInternal()) {
	    return AlertMessage.get("message.phd.external.access", PhdProperties.getPhdExternalAccessLink(), participant
		    .getAccessHashCode(), participant.getPassword());

	} else {
	    final Person person = ((InternalPhdParticipant) participant).getPerson();

	    if (process.isCoordinatorForPhdProgram(person)) {
		return AlertMessage.get(coordinatorMessage);

	    } else if (process.isGuiderOrAssistentGuider(person) || person.hasTeacher()) {
		return AlertMessage.get(teacherMessage);
	    }
	}

	throw new DomainException("error.PhdThesisProcess.unexpected.participant.type");
    }

    protected void email(String email, String subject, String body) {
	final SystemSender sender = RootDomainObject.getInstance().getSystemSender();
	new Message(sender, sender.getConcreteReplyTos(), null, null, null, subject, body, Collections.singleton(email));
    }

}