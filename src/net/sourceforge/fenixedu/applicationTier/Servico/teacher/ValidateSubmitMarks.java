package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSubmitMarks;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMark;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author T�nia Pous�o
 *  
 */
public class ValidateSubmitMarks implements IService {

    public InfoSiteSubmitMarks run(Integer executionCourseCode, Integer evaluationCode,
            IUserView userView) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        //execution course and execution course's site
        IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        IPersistentEnrolmentEvaluation enrolmentEvaluationDAO = sp.getIPersistentEnrolmentEvaluation();

        final ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseCode);

        //evaluation
        Evaluation evaluation = (Evaluation) sp.getIPersistentObject().readByOID(Evaluation.class,
                evaluationCode);

        //attend list
        IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
        List attendList = persistentAttend.readByExecutionCourse(executionCourse.getIdInternal());

        //verifySubmitMarks(attendList);

        List enrolmentListIds = (List) CollectionUtils.collect(attendList, new Transformer() {

            public Object transform(Object input) {
                Attends attend = (Attends) input;
                Enrolment enrolment = attend.getEnrolment();
                if (enrolment != null) {
                    if (enrolment.getExecutionPeriod().equals(executionCourse.getExecutionPeriod()))
                        return enrolment.getIdInternal();
                }
                return null;
            }
        });

        enrolmentListIds = (List) CollectionUtils.select(enrolmentListIds, new Predicate() {
            public boolean evaluate(Object arg0) {
                return arg0 != null;
            }
        });

        List alreadySubmiteMarks = new ArrayList();
        if (!enrolmentListIds.isEmpty()) {
            alreadySubmiteMarks = enrolmentEvaluationDAO.readAlreadySubmitedMarks(enrolmentListIds);
        }

        if (!alreadySubmiteMarks.isEmpty()) {
            throw new FenixServiceException("errors.submitMarks.yetSubmited");
        }

        //marks list
        IPersistentMark persistentMark = sp.getIPersistentMark();
        List markList = persistentMark.readBy(evaluation);

        //Check if there is any mark. If not, we can not submit
        if (markList.isEmpty()) {
            throw new FenixServiceException("errors.submitMarks.noMarks");
        }

        InfoSiteSubmitMarks infoSiteSubmitMarks = new InfoSiteSubmitMarks();
        infoSiteSubmitMarks.setInfoEvaluation(InfoEvaluation.newInfoFromDomain(evaluation));

        return infoSiteSubmitMarks;
    }
}