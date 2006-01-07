/*
 * Created on 5/Ago/2003, 15:46:40 By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT]
 * utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudyChoice;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudy;
import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudy;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudyChoice;
import net.sourceforge.fenixedu.domain.Seminaries.Modality;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import net.sourceforge.fenixedu.domain.Seminaries.Modality;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminary;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudy;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryModality;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryTheme;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt Created at
 *         5/Ago/2003, 15:46:40
 */
public class WriteCandidacy implements IService {

    public void run(InfoCandidacy infoCandidacy) throws ExcepcaoPersistencia {
        final ISuportePersistente persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        Candidacy candidacy = DomainFactory.makeCandidacy();
        candidacy.setApproved(infoCandidacy.getApproved());
        candidacy.setMotivation(infoCandidacy.getMotivation());
        
        // Modality
        final IPersistentSeminaryModality persistentModality = persistenceSupport.getIPersistentSeminaryModality();
        final Modality modality = (Modality) persistentModality.readByOID(Modality.class,infoCandidacy.getInfoModality().getIdInternal());        
        candidacy.setModality(modality);

        // Student
        final IPersistentStudent persistentStudent = persistenceSupport.getIPersistentStudent();
        final Student readStudent = (Student) persistentStudent.readByOID(Student.class, infoCandidacy.getInfoStudent().getIdInternal());
        candidacy.setStudent(readStudent);

        // Seminary
        final IPersistentSeminary persistentSeminary = persistenceSupport.getIPersistentSeminary();
        final Seminary readSeminary = (Seminary) persistentSeminary.readByOID(Seminary.class, infoCandidacy.getInfoSeminary().getIdInternal());
        candidacy.setSeminary(readSeminary);
        
        // Curricular Course
        final IPersistentCurricularCourse persistentCurricularCourse = persistenceSupport.getIPersistentCurricularCourse();
        final CurricularCourse readCurricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(CurricularCourse.class, infoCandidacy.getCurricularCourse().getIdInternal());
        candidacy.setCurricularCourse(readCurricularCourse);

        // Theme
        if (modality.getIdInternal().equals(infoCandidacy.getInfoModality().getIdInternal())) {
            candidacy.setTheme(null);
        } else {
            final IPersistentSeminaryTheme persistentTheme = persistenceSupport.getIPersistentSeminaryTheme();
            final Theme readTheme = (Theme) persistentTheme.readByOID(Theme.class, infoCandidacy.getTheme().getIdInternal());
            candidacy.setTheme(readTheme);
        }
        if (!infoCandidacy.getInfoSeminary().getHasThemes().booleanValue()) {
            candidacy.setTheme(null);
        }

        // Seminary Case Study Choices
        final IPersistentSeminaryCaseStudy persistentSeminaryCaseStudy = persistenceSupport.getIPersistentSeminaryCaseStudy();
        for (InfoCaseStudyChoice infoCaseStudyChoice : (List<InfoCaseStudyChoice>) infoCandidacy.getCaseStudyChoices()) {
            final CaseStudyChoice caseStudyChoice = DomainFactory.makeCaseStudyChoice();
            
            caseStudyChoice.setOrder(infoCaseStudyChoice.getOrder());

            final CaseStudy caseStudy = (CaseStudy) persistentSeminaryCaseStudy.readByOID(CaseStudy.class, infoCaseStudyChoice.getCaseStudy().getIdInternal());
            caseStudyChoice.setCaseStudy(caseStudy);

            caseStudyChoice.setCandidacy(candidacy);
            candidacy.addCaseStudyChoices(caseStudyChoice);
        }
    }

}
