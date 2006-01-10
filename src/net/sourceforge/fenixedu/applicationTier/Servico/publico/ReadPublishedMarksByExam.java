package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Factory.ExecutionCourseSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequentaWithInfoStudentAndPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoMark;
import net.sourceforge.fenixedu.dataTransferObject.InfoMarkWithInfoAttendAndInfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteMarks;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMark;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quit�rio
 *  
 */
public class ReadPublishedMarksByExam implements IService {

    public Object run(Integer siteCode, Integer evaluationCode) throws ExcepcaoInexistente,
            FenixServiceException, ExcepcaoPersistencia {
        List marksList = null;
        List infoMarksList = null;

        Site site = null;
        Evaluation evaluation = null;
        InfoEvaluation infoEvaluation = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        //Site

        IPersistentSite siteDAO = sp.getIPersistentSite();
        site = (Site) siteDAO.readByOID(Site.class, siteCode);

        //Execution Course
        ExecutionCourse executionCourse = site.getExecutionCourse();

        // Evaluation

        IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();
        evaluation = (Evaluation) persistentEvaluation.readByOID(Evaluation.class, evaluationCode);

        infoEvaluation = InfoEvaluation.newInfoFromDomain(evaluation);

        //Attends
        IFrequentaPersistente attendDAO = sp.getIFrequentaPersistente();
        List attendList = attendDAO.readByExecutionCourse(executionCourse.getIdInternal());

        //Marks
        IPersistentMark markDAO = sp.getIPersistentMark();
        marksList = markDAO.readBy(evaluation);

        List infoAttendList = (List) CollectionUtils.collect(attendList, new Transformer() {
            public Object transform(Object input) {
                Attends attend = (Attends) input;

                InfoFrequenta infoAttend = InfoFrequentaWithInfoStudentAndPerson
                        .newInfoFromDomain(attend);
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
        infoSiteMarks.setMarksList(infoMarksList);
        infoSiteMarks.setInfoEvaluation(infoEvaluation);
        infoSiteMarks.setHashMarks(hashMarks);
        infoSiteMarks.setInfoAttends(infoAttendList);

        ExecutionCourseSiteComponentBuilder componentBuilder = new ExecutionCourseSiteComponentBuilder();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);

        ExecutionCourseSiteView siteView = new ExecutionCourseSiteView(commonComponent, infoSiteMarks);

        return siteView;
    }
}