package Dominio;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.degree.enrollment.rules.MaximumNumberOfAcumulatedEnrollmentsRule;
import Dominio.degree.enrollment.rules.MaximumNumberOfCurricularCoursesEnrollmentRule;
import Dominio.degree.enrollment.rules.PrecedencesEnrollmentRule;
import Dominio.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRule;
import Dominio.degree.enrollment.rules.SecretaryEnrollmentRule;
import Dominio.degree.enrollment.rules.SpecificLEICEnrollmentRule;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AreaType;

/**
 * @author David Santos in Jun 25, 2004
 */

public class DegreeCurricularPlanLEIC extends DegreeCurricularPlan implements IDegreeCurricularPlan {

    public DegreeCurricularPlanLEIC() {
        ojbConcreteClass = getClass().getName();
    }

    public List getSpecialListOfCurricularCourses() {

        List allDegreeCurricularPlans = this.getDegree().getDegreeCurricularPlans();

        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) CollectionUtils.find(
                allDegreeCurricularPlans, new Predicate() {
                    public boolean evaluate(Object obj) {
                        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) obj;
                        return degreeCurricularPlan.getName().equals("LEIC - Curr�culo Antigo");
                    }
                });

        List curricularCourses = new ArrayList();

        int curricularCoursesSize = degreeCurricularPlan.getCurricularCourses().size();
        for (int i = 0; i < curricularCoursesSize; i++) {
            ICurricularCourse curricularCourse = (ICurricularCourse) degreeCurricularPlan
                    .getCurricularCourses().get(i);

            int curricularCourseScopesSize = curricularCourse.getScopes().size();
            for (int j = 0; j < curricularCourseScopesSize; j++) {
                ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) curricularCourse
                        .getScopes().get(j);
                if ((curricularCourseScope.getCurricularSemester().getCurricularYear().getYear()
                        .intValue() == 5)
                        && (!curricularCourses.contains(curricularCourse))) {
                    curricularCourses.add(curricularCourse);
                }
            }
        }

        return curricularCourses;
    }
    
    public List getListOfEnrollmentRules(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {

        List result = new ArrayList();

        result.add(new SecretaryEnrollmentRule(studentCurricularPlan));
        result.add(new MaximumNumberOfAcumulatedEnrollmentsRule(studentCurricularPlan, executionPeriod));
        result.add(new MaximumNumberOfCurricularCoursesEnrollmentRule(studentCurricularPlan,
                executionPeriod));
        result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new PreviousYearsCurricularCourseEnrollmentRule(studentCurricularPlan,
                executionPeriod));
        result.add(new SpecificLEICEnrollmentRule(studentCurricularPlan, executionPeriod));

        return result;
    }
    
    public List getCurricularCoursesFromArea(IBranch area, AreaType areaType) {

        List curricularCourses = new ArrayList();

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                    .getIPersistentCurricularCourseGroup();

            List groups = curricularCourseGroupDAO.readByBranchAndAreaType(area, areaType);

            int groupsSize = groups.size();

            for (int i = 0; i < groupsSize; i++) {
                ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) groups.get(i);

                List courses = curricularCourseGroup.getCurricularCourses();

                int coursesSize = courses.size();
                for (int j = 0; j < coursesSize; j++) {
                    ICurricularCourse curricularCourse = (ICurricularCourse) courses.get(j);
                    if (!curricularCourses.contains(curricularCourse)) {
                        curricularCourses.add(curricularCourse);
                    }
                }
            }

        } catch (ExcepcaoPersistencia e) {
            throw new RuntimeException(e);
        }

        return curricularCourses;
    }
    
    public List getSecundaryAreas() {
        List sec = super.getSecundaryAreas();
        sec.addAll(getSpecializationAreas());
        return sec;
    }
}