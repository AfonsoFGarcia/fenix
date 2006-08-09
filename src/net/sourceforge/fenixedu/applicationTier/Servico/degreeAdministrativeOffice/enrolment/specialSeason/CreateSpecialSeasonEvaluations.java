package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.specialSeason;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.EnrolmentException;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.SpecialSeasonCode;
import net.sourceforge.fenixedu.domain.Student;

public class CreateSpecialSeasonEvaluations extends Service {
	
	public void run(Student student, ExecutionYear executionYear, Collection<Enrolment> enrolments) throws EnrolmentException {

		SpecialSeasonCode specialSeasonCode = student.getSpecialSeasonCodeByExecutionYear(executionYear);
		
    	if(specialSeasonCode == null) {
    		throw new EnrolmentException("error.no.specialSeason.code");
    	} 
    	
    	if(specialSeasonCode.getMaxEnrolments() < 
    			(student.getActiveStudentCurricularPlan().getSpecialSeasonEnrolments(executionYear).size() + enrolments.size())) {
    		throw new EnrolmentException("error.too.many.specialSeason.enrolments", new String[] {specialSeasonCode.getMaxEnrolments().toString()});
    	}

		for (Enrolment enrolment : enrolments) {
			enrolment.createSpecialSeasonEvaluation();
		}
	}

}
