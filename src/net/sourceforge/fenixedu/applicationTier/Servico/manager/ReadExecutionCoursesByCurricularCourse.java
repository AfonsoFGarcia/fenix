package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionCoursesByCurricularCourse extends FenixService {

    public List run(Integer curricularCourseId) throws FenixServiceException {
	CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseId);
	if (curricularCourse == null) {
	    throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
	}

	List<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();

	List<InfoExecutionCourse> result = new ArrayList<InfoExecutionCourse>(executionCourses.size());
	for (ExecutionCourse executionCourse : executionCourses) {
	    result.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
	}
	return result;
    }

}
