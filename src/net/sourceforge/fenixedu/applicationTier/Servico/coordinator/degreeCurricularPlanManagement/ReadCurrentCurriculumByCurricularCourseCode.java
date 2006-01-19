package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Fernanda Quit�rio 13/Nov/2003
 */
public class ReadCurrentCurriculumByCurricularCourseCode implements IService {

    public InfoCurriculum run(Integer executionDegreeCode, Integer curricularCourseCode)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
        IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
        IPersistentCurricularCourseScope persistentCurricularCourseScope = sp
                .getIPersistentCurricularCourseScope();
        IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourse");
        }

        CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(
                CurricularCourse.class, curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }
        //selects active curricular course scopes
        List activeCurricularCourseScopes = persistentCurricularCourseScope
                .readActiveCurricularCourseScopesByCurricularCourse(curricularCourse.getIdInternal());

        activeCurricularCourseScopes = (List) CollectionUtils.select(activeCurricularCourseScopes,
                new Predicate() {
                    public boolean evaluate(Object arg0) {
                        CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;
                        if (curricularCourseScope.isActive().booleanValue()) {
                            return true;
                        }
                        return false;
                    }
                });

        //selects execution courses for current execution period
        final ExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
       
        List<ExecutionCourse> associatedExecutionCourses = new ArrayList<ExecutionCourse>();
        List<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();
        for(ExecutionCourse executionCourse : executionCourses){
            if(executionCourse.getExecutionPeriod().equals(executionPeriod))
                associatedExecutionCourses.add(executionCourse);
        }
        

        Curriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse
                .getIdInternal());
        InfoCurriculum infoCurriculum = null; 
        if (curriculum != null) {
            infoCurriculum = InfoCurriculumWithInfoCurricularCourse.newInfoFromDomain(curriculum);            
        } else {
            infoCurriculum = new InfoCurriculumWithInfoCurricularCourse();
            infoCurriculum.setIdInternal(new Integer(0));
            infoCurriculum.setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }

        infoCurriculum = createInfoCurriculum(infoCurriculum, persistentExecutionCourse,
                activeCurricularCourseScopes, associatedExecutionCourses);
        return infoCurriculum;
    }

    private InfoCurriculum createInfoCurriculum(InfoCurriculum infoCurriculum,
            IPersistentExecutionCourse persistentExecutionCourse, List activeCurricularCourseScopes,
            List associatedExecutionCourses) throws ExcepcaoPersistencia {

        List scopes = new ArrayList();

        CollectionUtils.collect(activeCurricularCourseScopes, new Transformer() {
            public Object transform(Object arg0) {
                CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;

                return InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear
                        .newInfoFromDomain(curricularCourseScope);
            }
        }, scopes);
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(scopes);

        List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        Iterator iterExecutionCourses = associatedExecutionCourses.iterator();
        while (iterExecutionCourses.hasNext()) {
            ExecutionCourse executionCourse = (ExecutionCourse) iterExecutionCourses.next();

            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                    .newInfoFromDomain(executionCourse);

            Boolean hasSite;
            if(executionCourse.getSite() != null)
                hasSite = Boolean.TRUE;
            else
                hasSite = Boolean.FALSE;
            
            infoExecutionCourse.setHasSite(hasSite);
            infoExecutionCourses.add(infoExecutionCourse);
        }
        infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);
        return infoCurriculum;
    }
}