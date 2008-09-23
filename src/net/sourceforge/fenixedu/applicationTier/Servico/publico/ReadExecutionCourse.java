package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Jo�o Mota
 */
public class ReadExecutionCourse extends FenixService {

    public Object run(InfoExecutionPeriod infoExecutionPeriod, String code) {
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(infoExecutionPeriod
		.getIdInternal());
	ExecutionCourse iExecCourse = executionSemester.getExecutionCourseByInitials(code);

	if (iExecCourse != null) {
	    return InfoExecutionCourse.newInfoFromDomain(iExecCourse);
	}
	return null;
    }

}
