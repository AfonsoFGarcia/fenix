package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/*
 * 
 * @author Fernanda Quit�rio 23/Dez/2003
 */

public class DissociateCurricularCourseByExecutionCourseId implements IService {

	public Object run(Integer executionCourseId, Integer curricularCourseId)
			throws FenixServiceException, ExcepcaoPersistencia {

		// List executionCourseList = null;
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
		IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

		if (executionCourseId == null) {
			throw new FenixServiceException("nullExecutionCourseId");
		}
		if (curricularCourseId == null) {
			throw new FenixServiceException("nullCurricularCourseId");
		}

		ExecutionCourse executionCourse = (ExecutionCourse) executionCourseDAO.readByOID(
				ExecutionCourse.class, executionCourseId);
		if (executionCourse == null) {
			throw new NonExistingServiceException("noExecutionCourse");
		}

		CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(
				CurricularCourse.class, curricularCourseId);
		if (curricularCourse == null) {
			throw new NonExistingServiceException("noCurricularCourse");
		}

		List curricularCourses = executionCourse.getAssociatedCurricularCourses();
		List executionCourses = curricularCourse.getAssociatedExecutionCourses();

		if (!executionCourses.isEmpty() && !curricularCourses.isEmpty()) {
			executionCourses.remove(executionCourse);
			curricularCourses.remove(curricularCourse);
		}

		return Boolean.TRUE;
	}
}
