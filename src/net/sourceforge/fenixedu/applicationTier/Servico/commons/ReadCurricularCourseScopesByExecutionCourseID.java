package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class ReadCurricularCourseScopesByExecutionCourseID extends Service {

    public List run(Integer executionCourseID) throws FenixServiceException, ExcepcaoPersistencia {

        List infoCurricularCourses = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        // Read The ExecutionCourse

        ExecutionCourse executionCourse = (ExecutionCourse) sp.getIPersistentExecutionCourse()
                .readByOID(ExecutionCourse.class, executionCourseID);

        // For all associated Curricular Courses read the Scopes

        infoCurricularCourses = new ArrayList();
        Iterator iterator = executionCourse.getAssociatedCurricularCourses().iterator();
        while (iterator.hasNext()) {
            CurricularCourse curricularCourse = (CurricularCourse) iterator.next();

            ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
            List curricularCourseScopes = sp.getIPersistentCurricularCourseScope()
                    .readCurricularCourseScopesByCurricularCourseInExecutionPeriod(
                            curricularCourse.getIdInternal(), executionPeriod.getBeginDate(),
                            executionPeriod.getEndDate());

            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                    .newInfoFromDomain(curricularCourse);
            infoCurricularCourse.setInfoScopes(new ArrayList());

            Iterator scopeIterator = curricularCourseScopes.iterator();
            while (scopeIterator.hasNext()) {

                infoCurricularCourse.getInfoScopes().add(
                        InfoCurricularCourseScopeWithBranchAndSemesterAndYear
                                .newInfoFromDomain((CurricularCourseScope) scopeIterator.next()));
            }
            infoCurricularCourses.add(infoCurricularCourse);
        }

        return infoCurricularCourses;
    }
}