/*
 * Created on Nov 22, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.improvment;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ImprovmentUnEnrollService extends Service {

    public void run(Registration registration, List<Integer> enrolmentsIds)
			throws FenixServiceException, ExcepcaoPersistencia, DomainException {

        if (registration == null) {
            throw new InvalidArgumentsServiceException();
        }
    	
    	for (final Integer enrolmentId : enrolmentsIds) {
            final Enrolment enrolment = registration.findEnrolmentByEnrolmentID(enrolmentId);
            if (enrolment == null) {
                throw new InvalidArgumentsServiceException();
            }
			enrolment.unEnrollImprovement(ExecutionSemester.readActualExecutionPeriod());
        }
    }
}
