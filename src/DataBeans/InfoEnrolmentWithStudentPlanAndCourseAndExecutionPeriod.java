package DataBeans;

import Dominio.IEnrollment;

/**
 * @author Fernanda Quit�rio
 * 20/Jul/2004
 */
public class InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod extends InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear {

	public void copyFromDomain(IEnrollment enrolment) {
		super.copyFromDomain(enrolment);
		if(enrolment != null) {
		    setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree
                    .newInfoFromDomain(enrolment.getStudentCurricularPlan()));
		}
	}
	
	public static InfoEnrolment newInfoFromDomain(IEnrollment enrolment) {
	    InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod infoEnrolment = null;
		if(enrolment != null) {
			infoEnrolment = new InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod();
			infoEnrolment.copyFromDomain(enrolment);
		}				
		return infoEnrolment;
	}
}
