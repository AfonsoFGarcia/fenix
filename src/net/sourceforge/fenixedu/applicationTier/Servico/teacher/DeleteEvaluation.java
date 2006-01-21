/*
 * Created on Nov 8, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteEvaluation extends Service {

    /**
     * @param Integer executionCourseID used in filtering
     *        (ExecutionCourseLecturingTeacherAuthorizationFilter)
     */
    public void run(Integer executionCourseID, Integer evaluationID) throws ExcepcaoPersistencia,
            FenixServiceException {
        final Evaluation evaluation = (Evaluation) persistentObject
                .readByOID(Evaluation.class, evaluationID);
        if (evaluation == null) {
            throw new FenixServiceException("error.noEvaluation");
        }
        evaluation.delete();
    }
}
