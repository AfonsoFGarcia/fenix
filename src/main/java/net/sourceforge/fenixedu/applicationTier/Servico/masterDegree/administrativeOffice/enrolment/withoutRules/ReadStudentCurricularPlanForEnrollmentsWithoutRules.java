package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.enrolment.withoutRules;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ReadStudentCurricularPlanForEnrollmentsWithoutRules extends FenixService {

    public StudentCurricularPlan run(Registration registration, DegreeType degreeType, ExecutionSemester executionSemester)
            throws FenixServiceException {

        final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
        if (studentCurricularPlan == null) {
            throw new FenixServiceException("error.student.curriculum.noCurricularPlans");
        }

        if (isStudentCurricularPlanFromChosenExecutionYear(studentCurricularPlan, executionSemester.getExecutionYear())) {
            return studentCurricularPlan;

        } else {
            throw new FenixServiceException("error.student.curriculum.not.from.chosen.execution.year");
        }
    }

    private boolean isStudentCurricularPlanFromChosenExecutionYear(StudentCurricularPlan studentCurricularPlan,
            ExecutionYear executionYear) {
        return studentCurricularPlan.getDegreeCurricularPlan().hasExecutionDegreeFor(executionYear);
    }
}