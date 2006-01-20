/*
 * Created on Feb 19, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.delegate;

import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.SearchService;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class ReadDelegateCurricularCourses extends SearchService {

    @Override
    protected InfoObject newInfoFromDomain(DomainObject object) {
        CurricularCourse curricularCourse = (CurricularCourse) object;
        InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree.newInfoFromDomain(curricularCourse);

        List infoScopes = (List) CollectionUtils.collect(curricularCourse.getScopes(),
                new Transformer() {
                    public Object transform(Object arg0) {
                        CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;
                        return InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear.newInfoFromDomain(curricularCourseScope);
                    }

                });
        infoCurricularCourse.setInfoScopes(infoScopes);

        return infoCurricularCourse;
    }

    @Override
    protected List doSearch(HashMap searchParameters) throws ExcepcaoPersistencia {

        final String user = (String) searchParameters.get("user");
        final Student student = persistentSupport.getIPersistentStudent().readByUsername(user);
        final Delegate delegate = persistentSupport.getIPersistentDelegate().readByStudent(student);

        // if he's a degree delegate then he can read all curricular courses
        // report
        final IPersistentCurricularCourse persistentCurricularCourse = persistentSupport
                .getIPersistentCurricularCourse();
        List curricularCourses = null;
        if (delegate.getType().booleanValue()) {
            curricularCourses = persistentCurricularCourse
                    .readExecutedCurricularCoursesByDegreeAndExecutionYear(delegate.getDegree()
                            .getIdInternal(), delegate.getExecutionYear().getIdInternal());
        } else {
            Integer year = new Integer(delegate.getYearType().getValue());
            curricularCourses = persistentCurricularCourse
                    .readExecutedCurricularCoursesByDegreeAndYearAndExecutionYear(delegate.getDegree()
                            .getIdInternal(), year, delegate.getExecutionYear().getIdInternal());
        }
        return curricularCourses;
    }

}
