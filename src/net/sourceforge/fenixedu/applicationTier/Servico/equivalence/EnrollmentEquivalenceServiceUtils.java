package net.sourceforge.fenixedu.applicationTier.Servico.equivalence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithInfoCurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCreditsInAnySecundaryArea;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author David Santos 9/Jul/2003
 */

public abstract class EnrollmentEquivalenceServiceUtils extends Service {
    /**
     * @param enrollment
     * @return true/false
     */
    protected boolean isAnAprovedEnrollment(IEnrolment enrollment) {
        return enrollment.getEnrollmentState().equals(EnrollmentState.APROVED);
    }

    /**
     * @param enrollment
     * @return true/false
     */
    protected boolean isAnEnroledEnrollment(IEnrolment enrollment) {
        return (enrollment.getEnrollmentState().equals(EnrollmentState.ENROLLED) || enrollment
                .getEnrollmentState().equals(EnrollmentState.TEMPORARILY_ENROLLED));
    }

    /**
     * @param enrollment
     * @param degreeCurricularPlan
     * @param studentCurricularPlan
     * @return true/false
     */
    protected boolean isAnEnrollmentWithNoEquivalences(IEnrolment enrollment,
            Integer degreeCurricularPlanKey, IStudentCurricularPlan studentCurricularPlan) {
        List result1 = null;
        List result2 = null;
        List result3 = null;
        List result4 = null;

        try {
            ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentEquivalentEnrolmentForEnrolmentEquivalence equivalentEnrolmentForEnrolmentEquivalenceDAO = persistentSupport
                    .getIPersistentEquivalentEnrolmentForEnrolmentEquivalence();

            IPersistentCurricularCourse persistentCurricularCourse = persistentSupport
                    .getIPersistentCurricularCourse();

            IPersistentCreditsInAnySecundaryArea creditsInAnySecundaryAreaDAO = persistentSupport
                    .getIPersistentCreditsInAnySecundaryArea();

            result1 = equivalentEnrolmentForEnrolmentEquivalenceDAO
                    .readByEquivalentEnrolment(enrollment);

            result2 = persistentCurricularCourse.readbyCourseNameAndDegreeCurricularPlan(enrollment
                    .getCurricularCourse().getName(), degreeCurricularPlanKey);

            result3 = studentCurricularPlan.getCreditsInScientificAreas();

            result4 = creditsInAnySecundaryAreaDAO.readAllByStudentCurricularPlan(studentCurricularPlan);

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        //		if ( (result1 == null) && (result2 == null) )
        //		{
        //			return true;
        //		} else if ( (result1 != null) && (result2 != null) )
        //		{
        //			return (result1.isEmpty() && result2.isEmpty());
        //		} else if (result1 != null)
        //		{
        //			return result1.isEmpty();
        //		} else
        //		{
        //			return result2.isEmpty();
        //		}

        //		return (result1.isEmpty() && result2.isEmpty());

        return (result1.isEmpty() && result2.isEmpty() && result3.isEmpty() && result4.isEmpty());
    }

    /**
     * @param studentNumber
     * @param degreeType
     * @return IStudentCurricularPlan
     * @throws ExcepcaoPersistencia
     */
    protected IStudentCurricularPlan getActiveStudentCurricularPlan(Integer studentNumber,
            DegreeType degreeType) throws ExcepcaoPersistencia {
        ISuportePersistente persistenceDAO = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentStudentCurricularPlan studentCurricularPlanDAO = persistenceDAO
                .getIStudentCurricularPlanPersistente();

        IStudentCurricularPlan activeStudentCurricularPlan = studentCurricularPlanDAO
                .readActiveStudentCurricularPlan(studentNumber, degreeType);

        return activeStudentCurricularPlan;
    }

    /**
     * @param curricularCourses
     * @return List
     */
    protected List cloneCurricularCoursesToInfoCurricularCourses(List curricularCourses) {
        List infoCurricularCourses = new ArrayList();
        for (int i = 0; i < curricularCourses.size(); i++) {
            ICurricularCourse curricularCourse = (ICurricularCourse) curricularCourses.get(i);

            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                    .newInfoFromDomain(curricularCourse);
            infoCurricularCourses.add(infoCurricularCourse);
        }
        return infoCurricularCourses;
    }

    /**
     * @param enrolments
     * @return List
     */
    protected List cloneEnrolmentsToInfoEnrolments(List enrollments) {
        List infoEnrolments = new ArrayList();
        for (int i = 0; i < enrollments.size(); i++) {
            IEnrolment enrollment = (IEnrolment) enrollments.get(i);

            InfoEnrolment infoEnrolment = InfoEnrolmentWithInfoCurricularCourse
                    .newInfoFromDomain(enrollment);
            infoEnrolments.add(infoEnrolment);
        }
        return infoEnrolments;
    }

    /**
     * @param infoEnrollment
     * @return String
     */
    protected String getEnrollmentGrade(IEnrolment enrollment) {
        // This sorts the list ascendingly so we need to reverse it to get the
        // first object.
        Collections.sort(enrollment.getEvaluations());
        Collections.reverse(enrollment.getEvaluations());
        return ((IEnrolmentEvaluation) enrollment.getEvaluations().get(0)).getGrade();
    }
}