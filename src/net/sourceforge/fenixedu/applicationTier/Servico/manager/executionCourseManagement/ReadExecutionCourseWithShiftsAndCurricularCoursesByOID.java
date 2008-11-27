/*
 * Created on 2004/11/17
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class ReadExecutionCourseWithShiftsAndCurricularCoursesByOID extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static InfoExecutionCourse run(final Integer oid) {
	InfoExecutionCourse infoExecutionCourse = null;

	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(oid);
	if (executionCourse != null) {
	    infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
	}

	return infoExecutionCourse;
    }

}