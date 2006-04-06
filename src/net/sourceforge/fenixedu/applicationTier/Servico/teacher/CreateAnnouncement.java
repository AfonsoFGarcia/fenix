package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Fernanda Quit�rio
 * 
 */
public class CreateAnnouncement extends Service {

    public boolean run(Integer infoExecutionCourseCode, String announcementTitle,
            String announcementInformation) throws ExcepcaoPersistencia, InvalidArgumentsServiceException {

    	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID( infoExecutionCourseCode);
        final Site site = executionCourse.getSite();

        if (site == null)
            throw new InvalidArgumentsServiceException();

        site.createAnnouncement(announcementTitle, announcementInformation);
        
        return true;
    }
}