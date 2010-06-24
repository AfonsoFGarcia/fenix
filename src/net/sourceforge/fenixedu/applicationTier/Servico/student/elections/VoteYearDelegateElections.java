package net.sourceforge.fenixedu.applicationTier.Servico.student.elections;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionBlankVote;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionVote;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class VoteYearDelegateElections extends FenixService {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static void run(YearDelegateElection yearDelegateElection, Student student, Student votedStudent)
	    throws FenixServiceException {

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.DelegateResources", Language.getLocale());
	try {
	    if (!yearDelegateElection.getVotingStudents().contains(student)) {
		final String fromName = bundle.getString("VoteYearDelegateElections.email.fromName");
		final String fromAddress = bundle.getString("VoteYearDelegateElections.email.fromAddress");
		final String subject = fromName + "-" + bundle.getString("VoteYearDelegateElections.email.subject");
		final String msg = bundle.getString("VoteYearDelegateElections.email.message");
		final Person person = student.getPerson();
		DelegateElectionVote vote = createDelegateElectionVote(yearDelegateElection, votedStudent);
		yearDelegateElection.addVotingStudents(student);
		yearDelegateElection.addVotes(vote);
		new Message(rootDomainObject.getSystemSender(), new ConcreteReplyTo(fromAddress).asCollection(), new Recipient(
			new PersonGroup(person)).asCollection(), subject, msg, "");
	    } else {
		throw new FenixServiceException("error.student.elections.voting.studentAlreadyVoted");
	    }
	} catch (DomainException ex) {
	    throw new FenixServiceException(ex.getMessage(), ex.getArgs());
	}
    }

    public static DelegateElectionVote createDelegateElectionVote(YearDelegateElection yearDelegateElection, Student votedStudent) {
	if (DelegateElectionBlankVote.isBlankVote(votedStudent)) {
	    return new DelegateElectionBlankVote(yearDelegateElection);
	}
	return new DelegateElectionVote(yearDelegateElection, votedStudent);
    }

}