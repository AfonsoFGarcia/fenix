/*
 * Created on 11/Nov/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author joaosa & rmalo
 * 
 */

public class EnrollStudentGroupShift extends Service {

	public Boolean run(Integer executionCourseCode, Integer studentGroupCode,
			Integer groupPropertiesCode, Integer newShiftCode) throws FenixServiceException,
			ExcepcaoPersistencia {

		Grouping grouping = (Grouping) persistentObject.readByOID(Grouping.class,
				groupPropertiesCode);

		if (grouping == null) {
			throw new ExistingServiceException();
		}

		Shift shift = (Shift) persistentObject.readByOID(Shift.class, newShiftCode);

		if (shift == null) {
			throw new InvalidSituationServiceException();
		}

		StudentGroup studentGroup = (StudentGroup) persistentObject.readByOID(
				StudentGroup.class, studentGroupCode);

		if (studentGroup == null) {
			throw new InvalidArgumentsServiceException();
		}

		if (grouping.getShiftType() == null || studentGroup.getShift() != null
				|| (!grouping.getShiftType().equals(shift.getTipo()))) {
			throw new InvalidChangeServiceException();
		}

		studentGroup.setShift(shift);

		return new Boolean(true);
	}
}