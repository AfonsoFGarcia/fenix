package net.sourceforge.fenixedu.applicationTier.Servico.manager.teachersManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Fernanda Quit�rio 5/Dez/2003
 * 
 */
public class ReadInfoTeacherByTeacherNumber extends FenixService {

    public InfoTeacher run(Integer teacherNumber) throws FenixServiceException {
	if (teacherNumber == null) {
	    throw new FenixServiceException("error.readInfoTeacherByTeacherNumber.nullTeacherNumber");
	}

	final Teacher teacher = Teacher.readByNumber(teacherNumber);
	if (teacher == null) {
	    throw new NonExistingServiceException("error.readInfoTeacherByTeacherNumber.noTeacher");
	}

	if (teacher.getProfessorshipsCount() == 0) {
	    throw new NonExistingServiceException("error.readInfoTeacherByTeacherNumber.noProfessorshipsOrNoResp");
	}

	return InfoTeacher.newInfoFromDomain(teacher);
    }

}