package ServidorAplicacao.Servico.enrollment;

import Dominio.IEnrolmentPeriodInCurricularCourses;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrollmentContext;
import ServidorPersistente.ExcepcaoPersistencia;

/*
 * 
 * @author Fernanda Quit�rio 12/Fev/2004
 *  
 */
public class ShowAvailableCurricularCourses extends
        ShowAvailableCurricularCoursesWithoutEnrollmentPeriod {

    // some of these arguments may be null. they are only needed for filter
    public InfoStudentEnrollmentContext run(Integer executionDegreeId, Integer studentCurricularPlanId,
            Integer studentNumber) throws FenixServiceException, ExcepcaoPersistencia {

        IStudent student = getStudent(studentNumber);

            if (student != null) {
                IStudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(student);

                if (studentCurricularPlan != null) {
                    IEnrolmentPeriodInCurricularCourses enrolmentPeriod = getEnrolmentPeriod(studentCurricularPlan);
                    IExecutionPeriod executionPeriod = getExecutionPeriod(null);
                    if (executionPeriod.equals(enrolmentPeriod.getExecutionPeriod())) {
                        try {
                            return super.run(executionDegreeId, studentCurricularPlanId, studentNumber);
                        } catch (IllegalArgumentException e) {
                            throw new FenixServiceException("degree");
                        }
                    }
                    throw new OutOfCurricularCourseEnrolmentPeriod(enrolmentPeriod.getStartDate(),
                            enrolmentPeriod.getEndDate());

                }
                throw new ExistingServiceException("studentCurricularPlan");

            }
            throw new ExistingServiceException("student");

    }

}