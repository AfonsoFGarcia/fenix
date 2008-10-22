/*
 * Created on 23/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;

/**
 * @author lmac1
 */

public class ReadExecutionCourseResponsiblesIds extends FenixService {

    public List run(Integer executionCourseId) throws FenixServiceException {
	ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);

	List<Professorship> responsibles = executionCourse.responsibleFors();

	List<Integer> responsibleIDs = new ArrayList<Integer>();
	if (responsibles != null) {
	    for (Professorship responsibleFor : responsibles) {
		responsibleIDs.add(responsibleFor.getTeacher().getIdInternal());
	    }
	}
	return responsibleIDs;
    }
}