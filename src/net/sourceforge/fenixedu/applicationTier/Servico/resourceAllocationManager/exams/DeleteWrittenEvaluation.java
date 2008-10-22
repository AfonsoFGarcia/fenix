package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.smtp.EmailSender;

public class DeleteWrittenEvaluation extends FenixService {

    /**
     * @param Integer
     *            executionCourseOID used in filtering
     *            (ExecutionCourseLecturingTeacherAuthorizationFilter)
     */
    public void run(Integer executionCourseOID, Integer writtenEvaluationOID) throws FenixServiceException {
	final WrittenEvaluation writtenEvaluationToDelete = (WrittenEvaluation) rootDomainObject
		.readEvaluationByOID(writtenEvaluationOID);
	if (writtenEvaluationToDelete == null) {
	    throw new FenixServiceException("error.noWrittenEvaluation");
	}
	if (writtenEvaluationToDelete.hasAnyVigilancies()) {
	    notifyVigilants(writtenEvaluationToDelete);
	}
	writtenEvaluationToDelete.delete();
    }

    private void notifyVigilants(WrittenEvaluation writtenEvaluation) {

	final ArrayList<String> tos = new ArrayList<String>();

	VigilantGroup group = writtenEvaluation.getAssociatedVigilantGroups().iterator().next();
	DateTime date = writtenEvaluation.getBeginningDateTime();
	String time = writtenEvaluation.getBeginningDateHourMinuteSecond().toString();
	String beginDateString = date.getDayOfMonth() + "-" + date.getMonthOfYear() + "-" + date.getYear();

	String subject = RenderUtils.getResourceString("VIGILANCY_RESOURCES", "email.convoke.subject", new Object[] {
		writtenEvaluation.getName(), group.getName(), beginDateString, time });

	for (Vigilancy vigilancy : writtenEvaluation.getVigilancies()) {
	    Person person = vigilancy.getVigilant().getPerson();
	    tos.add(person.getEmail());
	}

	EmailSender.send(group.getName(), group.getContactEmail(), new String[] { group.getContactEmail() }, tos, null, null,
		subject, RenderUtils.getResourceString("VIGILANCY_RESOURCES", "label.writtenEvaluationDeletedMessage",
			new Object[] { writtenEvaluation.getName(), beginDateString, time }));
    }

}
