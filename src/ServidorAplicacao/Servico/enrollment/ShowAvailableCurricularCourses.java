package ServidorAplicacao.Servico.enrollment;

import Dominio.IEnrolmentPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrollmentContext;
import ServidorPersistente.ExcepcaoPersistencia;

/*
 * 
 * @author Fernanda Quit�rio 12/Fev/2004
 *  
 */
public class ShowAvailableCurricularCourses extends
        ShowAvailableCurricularCoursesWithoutEnrollmentPeriod {
    public ShowAvailableCurricularCourses() {
    }

    // some of these arguments may be null. they are only needed for filter
    public InfoStudentEnrollmentContext run(Integer executionDegreeId,
            Integer studentCurricularPlanId, Integer studentNumber)
            throws FenixServiceException {
        try {

            IStudent student = getStudent(studentNumber);

            if (student != null) {
                IStudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(student);

                if (studentCurricularPlan != null) {
                    IEnrolmentPeriod enrolmentPeriod = getEnrolmentPeriod(studentCurricularPlan);
                    IExecutionPeriod executionPeriod = getCurrentExecutionPeriod();
                    if (executionPeriod.equals(enrolmentPeriod
                            .getExecutionPeriod())) {
                        try {
                            return super.run(executionDegreeId,
                                    studentCurricularPlanId, studentNumber);
                        } catch (IllegalArgumentException e) {
                            throw new FenixServiceException("degree");
                        }
                    }
                    throw new FenixServiceException("enrolmentPeriod");

                }
                throw new ExistingServiceException("studentCurricularPlan");

            }
            throw new ExistingServiceException("student");

        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        }
    }

}