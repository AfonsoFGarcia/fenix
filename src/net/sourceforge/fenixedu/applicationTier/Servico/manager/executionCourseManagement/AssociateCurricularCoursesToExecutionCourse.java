package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/*
 * 
 * @author Fernanda Quit�rio 29/Dez/2003
 */
public class AssociateCurricularCoursesToExecutionCourse extends Service {

	public void run(Integer executionCourseId, List curricularCourseIds) throws FenixServiceException, ExcepcaoPersistencia {
		if (executionCourseId == null) {
			throw new FenixServiceException("nullExecutionCourseId");
		}

		if (curricularCourseIds != null) {
			ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);

			if (executionCourse == null) {
				throw new NonExistingServiceException("noExecutionCourse");
			}

			Iterator iter = curricularCourseIds.iterator();
			while (iter.hasNext()) {
				Integer curricularCourseId = (Integer) iter.next();

				CurricularCourse curricularCourse = (CurricularCourse) persistentObject
						.readByOID(CurricularCourse.class, curricularCourseId);
				if (curricularCourse == null) {
					throw new NonExistingServiceException("noCurricularCourse");
				}
				if (!curricularCourse.hasAssociatedExecutionCourses(executionCourse)) {
					curricularCourse.addAssociatedExecutionCourses(executionCourse);
				}
			}
		}
	}
}
