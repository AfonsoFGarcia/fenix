package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import Dominio.degree.enrollment.CurricularCourse2Enroll;
import Dominio.precedences.IPrecedence;
import Dominio.precedences.PrecedenceContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPrecedence;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author David Santos in Jun 9, 2004
 */

public class PrecedencesEnrollmentRule implements IEnrollmentRule {
    protected PrecedenceContext precedenceContext;

    public PrecedencesEnrollmentRule(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {
        this.precedenceContext = new PrecedenceContext();
        this.precedenceContext.setStudentCurricularPlan(studentCurricularPlan);
        this.precedenceContext.setExecutionPeriod(executionPeriod);
    }

    public List apply(List curricularCoursesWhereToApply) {
        precedenceContext.setCurricularCourses2Enroll(curricularCoursesWhereToApply);

        List curricularCourses2Enroll = new ArrayList();

        for (int i = 0; i < curricularCoursesWhereToApply.size(); i++) {
            CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) curricularCoursesWhereToApply
                    .get(i);

            List precedenceList = null;

            try {
                ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
                IPersistentPrecedence precedenceDAO = persistentSuport.getIPersistentPrecedence();
                precedenceList = precedenceDAO.readByCurricularCourse(curricularCourse2Enroll
                        .getCurricularCourse());
            } catch (ExcepcaoPersistencia e) {
                e.printStackTrace(System.out);
                throw new RuntimeException(e);
            }

            if (precedenceList == null || precedenceList.isEmpty()) {
                if (!curricularCourses2Enroll.contains(curricularCourse2Enroll)) {
                    curricularCourses2Enroll.add(curricularCourse2Enroll);
                }
            } else {
                int size = precedenceList.size();
                CurricularCourseEnrollmentType evaluate = ((IPrecedence) precedenceList.get(0))
                        .evaluate(precedenceContext);

                for (int j = 1; j < size; j++) {
                    IPrecedence precedence = (IPrecedence) precedenceList.get(j);
                    evaluate = evaluate.or(precedence.evaluate(precedenceContext));
                }

                curricularCourse2Enroll.setEnrollmentType(evaluate.and(curricularCourse2Enroll
                        .getEnrollmentType()));
                curricularCourses2Enroll.add(curricularCourse2Enroll);
            }
        }

        List elementsToRemove = (List) CollectionUtils.select(curricularCourses2Enroll, new Predicate() {
            public boolean evaluate(Object obj) {
                CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
                return curricularCourse2Enroll.getEnrollmentType().equals(
                        CurricularCourseEnrollmentType.NOT_ALLOWED);
            }
        });

        curricularCourses2Enroll.removeAll(elementsToRemove);

        curricularCoursesWhereToApply.clear();
        curricularCoursesWhereToApply.addAll(curricularCourses2Enroll);

        return curricularCoursesWhereToApply;
    }

}