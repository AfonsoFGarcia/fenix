package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;

public class EnrolStudentInWrittenEvaluation extends Service {

	public void run(String username, Integer writtenEvaluationOID) throws FenixServiceException,
			ExcepcaoPersistencia {
		final IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
		final Student student = persistentStudent.readByUsername(username);

		final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) persistentObject.readByOID(
				WrittenEvaluation.class, writtenEvaluationOID);
		if (writtenEvaluation == null || student == null) {
			throw new InvalidArgumentsServiceException();
		}

		writtenEvaluation.enrolStudent(student);
	}

}
