/*
 * Created on Dec 18, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author jpvl
 */
public class UpdateTeacherExecutionYearResponsabilities extends FenixService {

    public void run(Integer teacherId, Integer executionYearId, final List executionCourseResponsabilities)
	    throws FenixServiceException, DomainException {
	final Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);
	if (teacher == null)
	    throw new FenixServiceException("message.teacher-not-found");

	teacher.updateResponsabilitiesFor(executionYearId, executionCourseResponsabilities);
    }
}