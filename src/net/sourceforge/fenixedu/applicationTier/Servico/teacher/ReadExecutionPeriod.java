package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionPeriod extends Service {

	public InfoExecutionPeriod run(Integer executionCourseCode) throws FenixServiceException, ExcepcaoPersistencia {
		ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);
		ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
		return InfoExecutionPeriod.newInfoFromDomain(executionPeriod);
	}
    
}
