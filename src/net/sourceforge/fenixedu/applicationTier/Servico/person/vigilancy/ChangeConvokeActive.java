package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.Collections;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.accessControl.VigilancyGroup;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.PersonSender;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.services.Service;

public class ChangeConvokeActive extends FenixService {

    @Service
    public static void run(Vigilancy convoke, Boolean bool, Person person) {

	convoke.setActive(bool);
	sendEmailNotification(bool, person, convoke);
    }

    private static void sendEmailNotification(Boolean bool, Person person, Vigilancy convoke) {

	String replyTo = person.getEmail();

	VigilantGroup group = convoke.getAssociatedVigilantGroup();
	String groupEmail = group.getContactEmail();

	if (groupEmail != null) {
	    if (person.isExamCoordinatorForVigilantGroup(group)) {
		replyTo = groupEmail;
	    }
	}

	WrittenEvaluation writtenEvaluation = convoke.getWrittenEvaluation();

	String emailMessage = generateMessage(bool, convoke);
	DateTime date = writtenEvaluation.getBeginningDateTime();
	String time = writtenEvaluation.getBeginningDateHourMinuteSecond().toString();
	String beginDateString = date.getDayOfMonth() + "-" + date.getMonthOfYear() + "-" + date.getYear();

	String subject = RenderUtils.getResourceString("VIGILANCY_RESOURCES", "email.convoke.subject", new Object[] {
		writtenEvaluation.getName(), group.getName(), beginDateString, time });

	new Message(PersonSender.newInstance(person), new ConcreteReplyTo(replyTo).asCollection(), new Recipient(
		new VigilancyGroup(convoke)).asCollection(), Collections.EMPTY_LIST, Collections.EMPTY_LIST, subject,
		emailMessage, convoke.getSitesAndGroupEmails());

    }

    private static String generateMessage(Boolean bool, Vigilancy convoke) {

	WrittenEvaluation writtenEvaluation = convoke.getWrittenEvaluation();
	DateTime beginDate = writtenEvaluation.getBeginningDateTime();
	String date = beginDate.getDayOfMonth() + "-" + beginDate.getMonthOfYear() + "-" + beginDate.getYear();

	return RenderUtils.getResourceString("VIGILANCY_RESOURCES", "email.convoke.active.body", new Object[] {
		convoke.getVigilantWrapper().getPerson().getName(),
		(bool) ? RenderUtils.getResourceString("VIGILANCY_RESOURCES", "email.convoke.convokedAgain") : RenderUtils
			.getResourceString("VIGILANCY_RESOURCES", "email.convoke.uncovoked"), writtenEvaluation.getFullName(),
		date, writtenEvaluation.getBeginningDateHourMinuteSecond().toString() });
    }
}