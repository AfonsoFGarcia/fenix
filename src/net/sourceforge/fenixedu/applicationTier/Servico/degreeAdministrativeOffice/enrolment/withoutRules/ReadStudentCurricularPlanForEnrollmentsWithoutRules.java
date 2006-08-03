package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public class ReadStudentCurricularPlanForEnrollmentsWithoutRules extends Service {

	public StudentCurricularPlan run(Student student, DegreeType degreeType,
			ExecutionPeriod executionPeriod) throws FenixServiceException {

		final StudentCurricularPlan studentCurricularPlan = (student == null) ? null : student.getActiveOrConcludedStudentCurricularPlan();
		if (studentCurricularPlan == null) {
			throw new FenixServiceException("error.student.curriculum.noCurricularPlans");
		}

		if (isStudentCurricularPlanFromChosenExecutionYear(studentCurricularPlan, executionPeriod.getExecutionYear())) {
			return studentCurricularPlan;
			
		} else {
			throw new FenixServiceException("error.student.curriculum.not.from.chosen.execution.year");
		}
	}

	private boolean isStudentCurricularPlanFromChosenExecutionYear(StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
		return studentCurricularPlan.getDegreeCurricularPlan().hasExecutionDegreeFor(executionYear);
	}
}