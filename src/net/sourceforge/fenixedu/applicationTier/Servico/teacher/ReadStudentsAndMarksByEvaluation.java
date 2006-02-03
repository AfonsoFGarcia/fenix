package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequentaWithAll;
import net.sourceforge.fenixedu.dataTransferObject.InfoMark;
import net.sourceforge.fenixedu.dataTransferObject.InfoMarkWithInfoAttendAndInfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteMarks;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMark;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author T�nia Pous�o
 *  
 */
public class ReadStudentsAndMarksByEvaluation extends Service {

    public Object run(Integer executionCourseCode, Integer evaluationCode) throws FenixServiceException,
            ExcepcaoPersistencia {

        InfoEvaluation infoEvaluation = new InfoEvaluation();

        //Execution Course

        final ExecutionCourse executionCourse = (ExecutionCourse) persistentObject.readByOID(
                ExecutionCourse.class, executionCourseCode);

        //Site
        final Site site = executionCourse.getSite();

        //Evaluation

        Evaluation evaluation = (Evaluation) persistentObject.readByOID(Evaluation.class, evaluationCode);

        infoEvaluation = InfoEvaluation.newInfoFromDomain(evaluation);

        //Attends
        IFrequentaPersistente frequentaPersistente = persistentSupport.getIFrequentaPersistente();
        List attendList = frequentaPersistente.readByExecutionCourse(executionCourseCode);

        //Marks
        IPersistentMark persistentMark = persistentSupport.getIPersistentMark();
        List marksList = persistentMark.readBy(evaluation);

        List infoAttendList = (List) CollectionUtils.collect(attendList, new Transformer() {
            public Object transform(Object input) {
                Attends attend = (Attends) input;
                InfoFrequenta infoAttend = InfoFrequentaWithAll.newInfoFromDomain(attend);
                //Melhoria Alterar isto depois: isto est� feio assim
                if (attend.getEnrolment() != null) {
                    if (!attend.getEnrolment().getExecutionPeriod().equals(
                            executionCourse.getExecutionPeriod())) {
                        infoAttend.getInfoEnrolment().setEnrolmentEvaluationType(
                                EnrolmentEvaluationType.IMPROVEMENT);
                    }
                }

                return infoAttend;
            }
        });

        List infoMarkList = (List) CollectionUtils.collect(marksList, new Transformer() {
            public Object transform(Object input) {
                Mark mark = (Mark) input;

                InfoMark infoMark = InfoMarkWithInfoAttendAndInfoStudent.newInfoFromDomain(mark);
                return infoMark;
            }
        });

        HashMap hashMarks = new HashMap();
        Iterator iter = infoMarkList.iterator();
        while (iter.hasNext()) {
            InfoMark infoMark = (InfoMark) iter.next();
            hashMarks.put(infoMark.getInfoFrequenta().getAluno().getNumber().toString(), infoMark
                    .getMark());
        }
        InfoSiteMarks infoSiteMarks = new InfoSiteMarks();
        infoSiteMarks.setMarksList(infoMarkList);
        infoSiteMarks.setInfoEvaluation(infoEvaluation);
        infoSiteMarks.setInfoAttends(infoAttendList);
        infoSiteMarks.setHashMarks(hashMarks);

        TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent,
                infoSiteMarks);

        return siteView;

    }
}
