package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithCoordinators;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author T�nia Pous�o Create on 13/Nov/2003
 */
public class ReadExecutionDegreesByDegreeAndExecutionPeriod extends Service {

	public List run(Integer executionPeriodId, Integer degreeId) throws FenixServiceException, ExcepcaoPersistencia {
		List infoExecutionDegreeList = null;

		if (degreeId == null) {
			throw new FenixServiceException("error.impossibleDegreeSite");
		}

		// Execution OccupationPeriod
		ExecutionPeriod executionPeriod;
		if (executionPeriodId == null) {
			executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
		} else {
			executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
		}

		if (executionPeriod == null) {
			throw new FenixServiceException("error.impossibleDegreeSite");
		}

		ExecutionYear executionYear = executionPeriod.getExecutionYear();
		if (executionYear == null) {
			throw new FenixServiceException("error.impossibleDegreeSite");
		}

		// Degree
		Degree degree = rootDomainObject.readDegreeByOID(degreeId);
		if (degree == null) {
			throw new FenixServiceException("error.impossibleDegreeSite");
		}

		// Execution degrees
		List executionDegreeList = ExecutionDegree.getAllByDegreeAndExecutionYear(degree, executionYear.getYear(), CurricularStage.OLD);
		if (executionDegreeList == null || executionDegreeList.size() <= 0) {
			throw new FenixServiceException("error.impossibleDegreeSite");
		}

		infoExecutionDegreeList = new ArrayList();
		ListIterator listIterator = executionDegreeList.listIterator();
		while (listIterator.hasNext()) {
			ExecutionDegree executionDegree = (ExecutionDegree) listIterator.next();

			InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithCoordinators
					.newInfoFromDomain(executionDegree);
			InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionDegree
					.getExecutionYear());
			infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);

			InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
					.newInfoFromDomain(executionDegree.getDegreeCurricularPlan());
			InfoDegree infoDegree = InfoDegree.newInfoFromDomain(executionDegree
					.getDegreeCurricularPlan().getDegree());
			infoDegreeCurricularPlan.setInfoDegree(infoDegree);
			infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

			infoExecutionDegreeList.add(infoExecutionDegree);
		}

		return infoExecutionDegreeList;
	}
}