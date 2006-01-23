/*
 * Created on 19/Dez/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico.teachersBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorshipWithAll;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.DetailedProfessorship;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a> 19/Dez/2003
 * 
 */
public class ReadProfessorshipsAndResponsibilitiesByExecutionDegree extends Service {

    public List run(Integer executionDegreeId) throws FenixServiceException, ExcepcaoPersistencia {
        IPersistentProfessorship persistentProfessorship = persistentSupport.getIPersistentProfessorship();

        ExecutionDegree executionDegree = (ExecutionDegree) persistentObject.readByOID(
                ExecutionDegree.class, executionDegreeId);

        List professorships = persistentProfessorship.readByDegreeCurricularPlanAndExecutionYear(
                executionDegree.getDegreeCurricularPlan().getIdInternal(), executionDegree
                        .getExecutionYear().getIdInternal());

        List responsibleFors = getResponsibleForsByDegree(executionDegree);

        List detailedProfessorships = getDetailedProfessorships(professorships, responsibleFors, persistentSupport);

        Collections.sort(detailedProfessorships, new Comparator() {

            public int compare(Object o1, Object o2) {
                DetailedProfessorship detailedProfessorship1 = (DetailedProfessorship) o1;
                DetailedProfessorship detailedProfessorship2 = (DetailedProfessorship) o2;
                int result = detailedProfessorship1.getInfoProfessorship().getInfoExecutionCourse()
                        .getIdInternal().intValue()
                        - detailedProfessorship2.getInfoProfessorship().getInfoExecutionCourse()
                                .getIdInternal().intValue();
                if (result == 0
                        && (detailedProfessorship1.getResponsibleFor().booleanValue() || detailedProfessorship2
                                .getResponsibleFor().booleanValue())) {
                    if (detailedProfessorship1.getResponsibleFor().booleanValue()) {
                        return -1;
                    }
                    if (detailedProfessorship2.getResponsibleFor().booleanValue()) {
                        return 1;
                    }
                }

                return result;
            }

        });

        List result = new ArrayList();
        Iterator iter = detailedProfessorships.iterator();
        List temp = new ArrayList();
        while (iter.hasNext()) {
            DetailedProfessorship detailedProfessorship = (DetailedProfessorship) iter.next();
            if (temp.isEmpty()
                    || ((DetailedProfessorship) temp.get(temp.size() - 1)).getInfoProfessorship()
                            .getInfoExecutionCourse().equals(
                                    detailedProfessorship.getInfoProfessorship()
                                            .getInfoExecutionCourse())) {
                temp.add(detailedProfessorship);
            } else {
                result.add(temp);
                temp = new ArrayList();
                temp.add(detailedProfessorship);
            }
        }
        if (!temp.isEmpty()) {
            result.add(temp);
        }
        return result;
    }

    private List getResponsibleForsByDegree(ExecutionDegree executionDegree) {
        List responsibleFors = new ArrayList();

        List<ExecutionCourse> executionCourses = new ArrayList();
        List<ExecutionPeriod> executionPeriods = executionDegree.getExecutionYear()
                .getExecutionPeriods();

        for (ExecutionPeriod executionPeriod : executionPeriods) {
            executionCourses = executionPeriod.getAssociatedExecutionCourses();
            for (ExecutionCourse executionCourse : executionCourses) {
                responsibleFors.add(executionCourse.responsibleFors());
            }
        }
        return responsibleFors;
    }

    protected List getDetailedProfessorships(List professorships, final List responsibleFors,
            ISuportePersistente persistentSupport) {
        List detailedProfessorshipList = (List) CollectionUtils.collect(professorships,
                new Transformer() {

                    public Object transform(Object input) {
                        Professorship professorship = (Professorship) input;

                        InfoProfessorship infoProfessorShip = InfoProfessorshipWithAll
                                .newInfoFromDomain(professorship);

                        List executionCourseCurricularCoursesList = getInfoCurricularCourses(professorship
                                .getExecutionCourse());

                        DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

                        detailedProfessorship.setResponsibleFor(professorship.getResponsibleFor());

                        detailedProfessorship.setInfoProfessorship(infoProfessorShip);
                        detailedProfessorship
                                .setExecutionCourseCurricularCoursesList(executionCourseCurricularCoursesList);

                        return detailedProfessorship;
                    }

                    private List getInfoCurricularCourses(ExecutionCourse executionCourse) {

                        List infoCurricularCourses = (List) CollectionUtils.collect(executionCourse
                                .getAssociatedCurricularCourses(), new Transformer() {

                            public Object transform(Object input) {
                                CurricularCourse curricularCourse = (CurricularCourse) input;

                                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                                        .newInfoFromDomain(curricularCourse);
                                return infoCurricularCourse;
                            }
                        });
                        return infoCurricularCourses;
                    }
                });

        return detailedProfessorshipList;
    }

}