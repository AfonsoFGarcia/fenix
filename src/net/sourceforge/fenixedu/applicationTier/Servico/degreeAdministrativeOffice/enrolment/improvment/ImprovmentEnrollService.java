package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.improvment;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author nmgo
 */
public class ImprovmentEnrollService extends Service {

    public void run(Registration registration, ExecutionSemester executionSemester, String employeeUserName, List<Integer> enrolmentsIds)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (executionSemester == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Person person = Person.readPersonByUsername(employeeUserName);

        if (person == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Employee employee = person.getEmployee();
        if (employee == null) {
            throw new InvalidArgumentsServiceException();
        }

        for (final Integer enrolmentID : enrolmentsIds) {
            final Enrolment enrollment = registration.findEnrolmentByEnrolmentID(enrolmentID);
            if (enrollment == null) {
                throw new InvalidArgumentsServiceException();
            }

            enrollment.createEnrolmentEvaluationForImprovement(employee, executionSemester);
        }
    }

}
