package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionCourseSite {

    @Checked("RolePredicates.TEACHER_PREDICATE")
    @Service
    public static InfoSite run(Integer executionCourseId) throws FenixServiceException {

        InfoSite infoSite = null;

        final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseId);
        final ExecutionCourseSite site = executionCourse.getSite();

        if (site != null) {
            infoSite = InfoSite.newInfoFromDomain(site);
            infoSite.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        }

        return infoSite;
    }
}