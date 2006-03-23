package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author T�nia Pous�o Created on 10/Out/2003
 */
public class ReadCurricularCourseScopeListByDegreeCurricularPlan extends Service {

    public List run(Integer idDegreeCurricularPlan) throws FenixServiceException, ExcepcaoPersistencia {
        List allCurricularCourses = null;
        List allCurricularCourseScope = new ArrayList();

        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentObject.readByOID(DegreeCurricularPlan.class,
                        idDegreeCurricularPlan);

        String name = degreeCurricularPlan.getName();
        String degreeName = degreeCurricularPlan.getDegree().getNome();
        String degreeSigla = degreeCurricularPlan.getDegree().getSigla();

        allCurricularCourses = persistentSupport.getIPersistentCurricularCourse()
                .readCurricularCoursesByDegreeCurricularPlan(name, degreeName, degreeSigla);

        if (allCurricularCourses == null || allCurricularCourses.isEmpty())
            return allCurricularCourses;

        // build the result of this service, ie, curricular course scope's
        // list
        // for each curricular course, add it scopes in the result list
        Iterator iterator = allCurricularCourses.iterator();

        CurricularCourse curricularCourse = null;
        ListIterator iteratorScopes = null;
        while (iterator.hasNext()) {
            curricularCourse = (CurricularCourse) iterator.next();

            List<CurricularCourseScope> curricularCourseScopes = curricularCourse.getActiveScopes();

            if (curricularCourseScopes != null) {
                iteratorScopes = curricularCourseScopes.listIterator();
                while (iteratorScopes.hasNext()) {
                    allCurricularCourseScope
                            .add(InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear
                                    .newInfoFromDomain((CurricularCourseScope) iteratorScopes.next()));
                }
            }
        }

        return allCurricularCourseScope;
    }
}