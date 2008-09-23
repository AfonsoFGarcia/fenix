package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.OtherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateOtherService extends FenixService {

    public void run(Integer teacherID, Integer executionPeriodID, Double credits, String reason) {

	Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);

	TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
	if (teacherService == null) {
	    teacherService = new TeacherService(teacher, executionSemester);
	}

	new OtherService(teacherService, credits, reason);
    }

}
