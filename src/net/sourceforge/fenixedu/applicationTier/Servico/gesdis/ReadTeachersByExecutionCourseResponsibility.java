package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import pt.ist.fenixWebFramework.services.Service;

public class ReadTeachersByExecutionCourseResponsibility extends FenixService {

    @Service
    public static List run(InfoExecutionCourse infoExecutionCourse) {
        return run(infoExecutionCourse.getIdInternal());
    }

    @Service
    public static List run(Integer executionCourseID) {
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);

        final List<InfoTeacher> result = new ArrayList<InfoTeacher>();
        for (final Professorship professorship : executionCourse.responsibleFors()) {
            result.add(InfoTeacher.newInfoFromDomain(professorship.getTeacher()));
        }
        return result;
    }

}