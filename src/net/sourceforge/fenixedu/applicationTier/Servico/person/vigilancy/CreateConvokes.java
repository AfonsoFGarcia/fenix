package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.services.Service;

public class CreateConvokes extends FenixService {

    @Service
    public static void run(List<Vigilant> vigilants, WrittenEvaluation writtenEvaluation, VigilantGroup group,
	    ExamCoordinator coordinator, String emailMessage) {
	group.convokeVigilants(vigilants, writtenEvaluation);
	if (emailMessage.length() != 0) {
	    Person person = coordinator.getPerson();
	    final Set<String> tos = new HashSet<String>();
	    for (Vigilant vigilant : vigilants) {
		String emailTo = vigilant.getEmail();
		tos.add(emailTo);
	    }

	    String groupEmail = group.getContactEmail();
	    String[] replyTo;

	    tos.addAll(Vigilancy.getEmailsThatShouldBeContactedFor(writtenEvaluation));

	    if (groupEmail != null) {
		tos.add(groupEmail);
		replyTo = new String[] { groupEmail };
	    } else {
		replyTo = new String[] { person.getEmail() };
	    }

	    DateTime date = writtenEvaluation.getBeginningDateTime();
	    String beginDateString = date.getDayOfMonth() + "/" + date.getMonthOfYear() + "/" + date.getYear();

	    String subject = RenderUtils.getResourceString("VIGILANCY_RESOURCES", "email.convoke.subject", new Object[] {
		    group.getEmailSubjectPrefix(), writtenEvaluation.getName(), group.getName(), beginDateString });

	    new Email(person.getName(), (groupEmail == null) ? person.getEmail() : groupEmail, replyTo, tos, null, null, subject,
		    emailMessage);
	}
    }

}