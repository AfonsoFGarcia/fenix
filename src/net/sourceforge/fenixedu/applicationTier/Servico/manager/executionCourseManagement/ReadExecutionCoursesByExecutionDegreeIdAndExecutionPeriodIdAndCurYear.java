package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/*
 * 
 * @author Fernanda Quit�rio 22/Dez/2003
 */

public class ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear implements IService {

	public Object run(Integer executionDegreeId, Integer executionPeriodId, Integer curricularYear)
			throws FenixServiceException, ExcepcaoPersistencia {

		List infoExecutionCourseList = new ArrayList();

		List executionCourseList = null;
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
		IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
		IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();

		if (executionPeriodId == null) {
			throw new FenixServiceException("nullExecutionPeriodId");
		}

		IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(
				ExecutionPeriod.class, executionPeriodId);

		if (executionDegreeId == null && curricularYear == null) {
			executionCourseList = executionCourseDAO
					.readByExecutionPeriodWithNoCurricularCourses(executionPeriod.getIdInternal());

		} else {
			IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
					ExecutionDegree.class, executionDegreeId);

			executionCourseList = executionCourseDAO
					.readByCurricularYearAndExecutionPeriodAndExecutionDegree(curricularYear,
							executionPeriod.getSemester(), executionDegree.getDegreeCurricularPlan()
									.getName(), executionDegree.getDegreeCurricularPlan().getDegree()
									.getSigla(), executionPeriod.getIdInternal());
		}

		CollectionUtils.collect(executionCourseList, new Transformer() {
			public Object transform(Object input) {
				IExecutionCourse executionCourse = (IExecutionCourse) input;
				return InfoExecutionCourse.newInfoFromDomain(executionCourse);
			}
		}, infoExecutionCourseList);

		return infoExecutionCourseList;
	}
}