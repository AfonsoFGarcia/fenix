package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadCurriculumByCurricularCourseCode implements IService {

    public InfoCurriculum run(final Integer curricularCourseCode)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourse");
        }

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

        final ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                CurricularCourse.class, curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        final ICurriculum curriculum = curricularCourse.findLatestCurriculum();
        final InfoCurriculum infoCurriculum = (curriculum != null) ? InfoCurriculum.newInfoFromDomain(curriculum) : new InfoCurriculum();
        infoCurriculum.setInfoCurricularCourse(InfoCurricularCourseWithInfoDegree.newInfoFromDomain(curricularCourse));

        List infoExecutionCourses = buildExecutionCourses(curricularCourse);
        infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);

        List activeInfoScopes = buildActiveScopes(curricularCourse);
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(activeInfoScopes);

        return infoCurriculum;
    }

    private List buildExecutionCourses(final ICurricularCourse curricularCourse) {
        final List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        for (final IExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
            final IExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
            if (executionPeriod.getState().equals(PeriodState.OPEN)
                    || executionPeriod.getState().equals(PeriodState.CURRENT)) {
                final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
                infoExecutionCourse.setHasSite(executionCourse.getSite() != null);
                infoExecutionCourses.add(infoExecutionCourse);
            }
        }
        return infoExecutionCourses;
    }

    private List<InfoCurricularCourseScope> buildActiveScopes(final ICurricularCourse curricularCourse) {
        final List<InfoCurricularCourseScope> activeInfoCurricularCourseScopes = new ArrayList<InfoCurricularCourseScope>();
        for (final ICurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
            if (curricularCourseScope.isActive()) {
                activeInfoCurricularCourseScopes.add(InfoCurricularCourseScopeWithBranchAndSemesterAndYear.newInfoFromDomain(curricularCourseScope));
            }
        }
        return activeInfoCurricularCourseScopes;
    }
}