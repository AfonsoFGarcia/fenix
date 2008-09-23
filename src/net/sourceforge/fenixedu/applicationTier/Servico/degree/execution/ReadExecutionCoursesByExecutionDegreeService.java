package net.sourceforge.fenixedu.applicationTier.Servico.degree.execution;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadExecutionCoursesByExecutionDegreeService extends FenixService {

    public class NonExistingExecutionDegree extends FenixServiceException {
	public NonExistingExecutionDegree() {
	    super();
	}
    }

    public List run(Integer executionDegreeId, Integer executionPeriodId) throws FenixServiceException {

	final ExecutionSemester executionSemester;
	if (executionPeriodId == null) {
	    executionSemester = ExecutionSemester.readActualExecutionSemester();
	} else {
	    executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);
	}

	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);
	if (executionDegree == null) {
	    throw new NonExistingExecutionDegree();
	}

	Set<ExecutionCourse> executionCourseList = executionDegree.getDegreeCurricularPlan()
		.getExecutionCoursesByExecutionPeriod(executionSemester);

	List infoExecutionCourseList = (List) CollectionUtils.collect(executionCourseList, new Transformer() {

	    public Object transform(Object input) {
		ExecutionCourse executionCourse = (ExecutionCourse) input;
		InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
		return infoExecutionCourse;
	    }
	});

	return infoExecutionCourseList;

    }
}