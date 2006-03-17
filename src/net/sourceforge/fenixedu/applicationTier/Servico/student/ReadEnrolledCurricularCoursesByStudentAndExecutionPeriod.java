/*
 * Created on 4/Mai/2005 - 15:38:02
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Jo�o Fialho & Rita Ferreira
 *
 */
public class ReadEnrolledCurricularCoursesByStudentAndExecutionPeriod extends
		Service {

    
	public List<InfoCurricularCourse> run(Integer studentCurricularPlanId, Integer executionPeriodId)
	throws ExcepcaoPersistencia {
		StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) persistentObject.readByOID(StudentCurricularPlan.class, studentCurricularPlanId);
		ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentObject.readByOID(ExecutionPeriod.class, executionPeriodId);
		List<Enrolment> enrollments = studentCurricularPlan.getEnrolmentsByExecutionPeriod(executionPeriod);
		
		List<InfoCurricularCourse> enrolledCurricularCourses = new ArrayList<InfoCurricularCourse>();
		
		for(Enrolment enrollment : enrollments) {
			enrolledCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(enrollment.getCurricularCourse()));
		}
		
		return enrolledCurricularCourses;

		
	}
}
