package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

/**
 * 
 * @author EP 15
 */

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;

public class ReadExecutionCourseSite extends FenixService {

    public InfoSite run(Integer executionCourseId) throws FenixServiceException {

	InfoSite infoSite = null;

	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
	final ExecutionCourseSite site = executionCourse.getSite();

	if (site != null) {
	    infoSite = InfoSite.newInfoFromDomain(site);
	    infoSite.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
	}

	return infoSite;
    }
}