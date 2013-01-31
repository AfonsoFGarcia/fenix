package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;

public class UnEnrollStudentFromShift extends FenixService {

	public void run(final Registration registration, final Integer shiftId) throws StudentNotFoundServiceException,
			ShiftNotFoundServiceException, ShiftEnrolmentNotFoundServiceException, FenixServiceException {

		if (registration == null) {
			throw new StudentNotFoundServiceException();
		}
		if (registration.getPayedTuition() == null || registration.getPayedTuition().equals(Boolean.FALSE)) {
			throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
		}

		final Shift shift = rootDomainObject.readShiftByOID(shiftId);
		if (shift == null) {
			throw new ShiftNotFoundServiceException();
		}

		shift.removeStudents(registration);
	}

	public class StudentNotFoundServiceException extends FenixServiceException {
	}

	public class ShiftNotFoundServiceException extends FenixServiceException {
	}

	public class ShiftEnrolmentNotFoundServiceException extends FenixServiceException {
	}

}
