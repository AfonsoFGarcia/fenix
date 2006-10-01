package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.VigilancyWithCredits;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.smtp.EmailSender;

public class ChangeConvokeActive extends Service {

    public void run(Integer convokeOID, Boolean bool, ExamCoordinator coordinator)
            throws ExcepcaoPersistencia {

        VigilancyWithCredits convoke = (VigilancyWithCredits) RootDomainObject.readDomainObjectByOID(VigilancyWithCredits.class, convokeOID);
        convoke.setActive(bool);

        Vigilant vigilant = convoke.getVigilant();
        String emailTo = vigilant.getEmail();
        final ArrayList<String> tos = new ArrayList<String>();
        tos.add(emailTo);

        VigilantGroup group = convoke.getWrittenEvaluation().getAssociatedExecutionCourses().get(0).getVigilantGroup();
        
        String groupEmail = group.getContactEmail(); 
        Person person = coordinator.getPerson();
        String[] replyTo;
        if(groupEmail!=null) {
        	tos.add(groupEmail);
        	replyTo = new String[] { groupEmail }; 
        }
        else {
        	replyTo = new String[] { person.getEmail() };
        }
        
        tos.addAll(getEmailsFromTeachers(convoke.getWrittenEvaluation()));
        
        String emailMessage = generateMessage(bool, convoke);
        EmailSender.send(person.getName(), (groupEmail!=null) ? groupEmail : person.getEmail(), replyTo, tos, null, null, RenderUtils.getResourceString("VIGILANCY_RESOURCES", "email.convoke.subject"),
                emailMessage);
    }

    private String generateMessage(Boolean bool, VigilancyWithCredits convoke) {
     
    	String message = "";
    	WrittenEvaluation writtenEvaluation = convoke.getWrittenEvaluation();
        DateTime beginDate = writtenEvaluation.getBeginningDateTime();
    	String date = beginDate.getDayOfMonth() + "/" + beginDate.getMonthOfYear() + "/" + beginDate.getYear();
    	
    	 // "Martelada" big time. Tomorrow with time will read documentation
  	  //  and see the correct formating for MessageFormat
  	  
  	  String minutes = String.valueOf(beginDate.getMinuteOfHour());
  	  if(minutes.length()==1) {
  		  minutes = "0" + minutes;
  	  }
    	message = "Caro docente,\n\n";
  	    message += (bool) ? RenderUtils.getResourceString("VIGILANCY_RESOURCES", "email.convoke.convokedAgain") : RenderUtils.getResourceString("VIGILANCY_RESOURCES", "email.convoke.uncovoked");
        message += "\n\nProva de avaliacao: " + writtenEvaluation.getFullName();
        message +="\nData: " + date + " (" + beginDate.getHourOfDay() + ":" + minutes +"h)";
        return message;
    }
    
    private List<String> getEmailsFromTeachers(WrittenEvaluation writtenEvaluation) {
    	Set<String> emails = new HashSet<String>();
    	for(ExecutionCourse course : writtenEvaluation.getAssociatedExecutionCourses()) {
    		for(Professorship professorship : course.getProfessorships()) {
    			emails.add(professorship.getTeacher().getPerson().getEmail());
    		}
    	}
    	return new ArrayList<String>(emails);
    }

}