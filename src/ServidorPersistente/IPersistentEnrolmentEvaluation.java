package ServidorPersistente;

import java.util.Date;
import java.util.List;

import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;

/**
 * @author �ngela
 */

public interface IPersistentEnrolmentEvaluation extends IPersistentObject {

    public void delete(IEnrolmentEvaluation enrolmentEvaluation) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
            IEnrollment enrolment, EnrolmentEvaluationType evaluationType, String grade)
            throws ExcepcaoPersistencia;

    public List readEnrolmentEvaluationByEnrolmentEvaluationState(IEnrollment enrolment,
            EnrolmentEvaluationState evaluationState) throws ExcepcaoPersistencia;

    public List readEnrolmentEvaluationByEnrolment(IEnrollment enrolment) throws ExcepcaoPersistencia;

    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGradeAndWhenAlteredDate(
            IEnrollment enrolment, EnrolmentEvaluationType evaluationType, String grade, Date whenAltered)
            throws ExcepcaoPersistencia;

    public List readAlreadySubmitedMarks(List enrolmentIds) throws ExcepcaoPersistencia;

    /**
     * @param enrolment
     * @param temporary_obj
     * @param enrolmentEvaluationType
     * @return
     */
    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentEvaluationStateAndType(IEnrollment enrolment, EnrolmentEvaluationState temporary_obj, EnrolmentEvaluationType enrolmentEvaluationType) throws ExcepcaoPersistencia;
}