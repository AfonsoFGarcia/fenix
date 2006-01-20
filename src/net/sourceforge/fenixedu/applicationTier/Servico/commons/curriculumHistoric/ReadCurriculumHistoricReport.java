/*
 * Created on Oct 11, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons.curriculumHistoric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.GetEnrolmentGrade;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlanAndInfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.commons.curriculumHistoric.InfoCurriculumHistoricReport;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadCurriculumHistoricReport extends Service {

    public InfoCurriculumHistoricReport run(Integer curricularCourseID, Integer semester,
            Integer executionYearID) throws FenixServiceException, ExcepcaoPersistencia {

        // read ExecutionYear
        IPersistentExecutionYear persistentExecutionYear = persistentSupport
                .getIPersistentExecutionYear();
        ExecutionYear executionYear = (ExecutionYear) persistentExecutionYear.readByOID(
                ExecutionYear.class, executionYearID);

        // read ExecutionPeriod
        IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport
                .getIPersistentExecutionPeriod();
        ExecutionPeriod executionPeriod = persistentExecutionPeriod.readBySemesterAndExecutionYear(
                semester, executionYear.getYear());

        // read CurricularCourse
        IPersistentCurricularCourse persistentCurricularCourse = persistentSupport
                .getIPersistentCurricularCourse();
        CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(
                CurricularCourse.class, curricularCourseID);
        // read all enrollments
        IPersistentEnrollment persistentEnrollment = persistentSupport.getIPersistentEnrolment();
        List enrollments = persistentEnrollment.readByCurricularCourseAndExecutionPeriod(
                curricularCourse.getIdInternal(), executionPeriod.getIdInternal());

        InfoCurriculumHistoricReport infoCurriculumHistoricReport = createInfoCurriculumHistoricReport(enrollments);

        infoCurriculumHistoricReport.setSemester(semester);

        InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                .newInfoFromDomain(curricularCourse);
        infoCurriculumHistoricReport.setInfoCurricularCourse(infoCurricularCourse);

        InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionYear);
        infoCurriculumHistoricReport.setInfoExecutionYear(infoExecutionYear);

        return infoCurriculumHistoricReport;
    }

    /**
     * @param enrollments
     * @return
     * @throws ExcepcaoPersistencia
     */
    private InfoCurriculumHistoricReport createInfoCurriculumHistoricReport(List enrollments)
            throws FenixServiceException, ExcepcaoPersistencia {

        List notAnulledEnrollments = (List) CollectionUtils.select(enrollments, new Predicate() {

            public boolean evaluate(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                if (!enrollment.getEnrollmentState().equals(EnrollmentState.ANNULED)) {
                    return true;
                }
                return false;
            }
        });

        List evaluatedEnrollments = (List) CollectionUtils.select(notAnulledEnrollments,
                new Predicate() {

                    public boolean evaluate(Object obj) {
                        Enrolment enrollment = (Enrolment) obj;
                        if (enrollment.getEnrollmentState().equals(EnrollmentState.APROVED)
                                || enrollment.getEnrollmentState().equals(EnrollmentState.NOT_APROVED)) {
                            return true;
                        }
                        return false;
                    }
                });

        List aprovedEnrollments = (List) CollectionUtils.select(evaluatedEnrollments, new Predicate() {

            public boolean evaluate(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                if (enrollment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
                    return true;
                }
                return false;
            }
        });

        InfoCurriculumHistoricReport infoCurriculumHistoricReport = new InfoCurriculumHistoricReport();
        infoCurriculumHistoricReport.setEnrolled(new Integer(notAnulledEnrollments.size()));
        infoCurriculumHistoricReport.setEvaluated(new Integer(evaluatedEnrollments.size()));
        infoCurriculumHistoricReport.setAproved(new Integer(aprovedEnrollments.size()));

        GetEnrolmentGrade getEnrollmentGrade = new GetEnrolmentGrade();
        Iterator iterator = notAnulledEnrollments.iterator();
        List infoEnrollments = new ArrayList();
        while (iterator.hasNext()) {
            Enrolment enrolmentTemp = (Enrolment) iterator.next();

            InfoEnrolmentEvaluation infoEnrolmentEvaluation = getEnrollmentGrade.run(enrolmentTemp);

            InfoEnrolment infoEnrolment = InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlanAndInfoStudent
                    .newInfoFromDomain(enrolmentTemp);

            infoEnrolment.setInfoEnrolmentEvaluation(infoEnrolmentEvaluation);

            setInfoEnrolmentByEnrolmentEvaluationType(infoEnrolment, enrolmentTemp.getEvaluations());

            infoEnrollments.add(infoEnrolment);
        }

        infoCurriculumHistoricReport.setEnrollments(infoEnrollments);

        return infoCurriculumHistoricReport;
    }

    /**
     * @param infoEnrolment
     * @param evaluations
     */
    private void setInfoEnrolmentByEnrolmentEvaluationType(InfoEnrolment infoEnrolment, List evaluations) {
        List normalEnrolmentEvaluations = new ArrayList();
        List specialSeasonEnrolmentEvaluations = new ArrayList();
        List improvmentEnrolmentEvaluations = new ArrayList();
        List equivalenceEnrolmentEvaluations = new ArrayList();

        Iterator iterator = evaluations.iterator();
        while (iterator.hasNext()) {
            EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) iterator.next();
            if (enrolmentEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.NORMAL))
                normalEnrolmentEvaluations.add(enrolmentEvaluation);

            if (enrolmentEvaluation.getEnrolmentEvaluationType().equals(
                    EnrolmentEvaluationType.IMPROVEMENT))
                improvmentEnrolmentEvaluations.add(enrolmentEvaluation);

            if (enrolmentEvaluation.getEnrolmentEvaluationType().equals(
                    EnrolmentEvaluationType.SPECIAL_SEASON))
                specialSeasonEnrolmentEvaluations.add(enrolmentEvaluation);

            if (enrolmentEvaluation.getEnrolmentEvaluationType().equals(
                    EnrolmentEvaluationType.EQUIVALENCE))
                equivalenceEnrolmentEvaluations.add(enrolmentEvaluation);
        }

        infoEnrolment
                .setInfoNormalEnrolmentEvaluation(getLatestInfoEnrolmentEvaluation(normalEnrolmentEvaluations));
        infoEnrolment
                .setInfoImprovmentEnrolmentEvaluation(getLatestInfoEnrolmentEvaluation(improvmentEnrolmentEvaluations));
        infoEnrolment
                .setInfoSpecialSeasonEnrolmentEvaluation(getLatestInfoEnrolmentEvaluation(specialSeasonEnrolmentEvaluations));
        infoEnrolment
                .setInfoEquivalenceEnrolmentEvaluation(getLatestInfoEnrolmentEvaluation(equivalenceEnrolmentEvaluations));
    }

    /**
     * @param normalEnrolmentEvaluations
     * @return
     */
    private InfoEnrolmentEvaluation getLatestInfoEnrolmentEvaluation(List enrolmentEvaluations) {
        return (enrolmentEvaluations.isEmpty()) ? null : InfoEnrolmentEvaluation
                .newInfoFromDomain((EnrolmentEvaluation) Collections.max(enrolmentEvaluations));
    }

}
