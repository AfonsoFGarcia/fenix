/*
 * Created on 5/Ago/2003, 15:46:40 By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT]
 * utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacy;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudyChoice;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudy;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudyChoice;
import net.sourceforge.fenixedu.domain.Seminaries.Modality;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.domain.Seminaries.Theme;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt Created at
 *         5/Ago/2003, 15:46:40
 */
public class WriteCandidacy extends Service {

    public void run(InfoCandidacy infoCandidacy) throws ExcepcaoPersistencia {
        Candidacy candidacy = DomainFactory.makeCandidacy();
        candidacy.setApproved(infoCandidacy.getApproved());
        candidacy.setMotivation(infoCandidacy.getMotivation());
        
        // Modality
        final Modality modality = (Modality) persistentObject.readByOID(Modality.class,infoCandidacy.getInfoModality().getIdInternal());        
        candidacy.setModality(modality);

        // Student
        final Student readStudent = rootDomainObject.readStudentByOID(infoCandidacy.getInfoStudent().getIdInternal());
        candidacy.setStudent(readStudent);

        // Seminary
        final Seminary readSeminary = (Seminary) persistentObject.readByOID(Seminary.class, infoCandidacy.getInfoSeminary().getIdInternal());
        candidacy.setSeminary(readSeminary);
        
        // Curricular Course
        final CurricularCourse readCurricularCourse = (CurricularCourse) persistentObject.readByOID(CurricularCourse.class, infoCandidacy.getCurricularCourse().getIdInternal());
        candidacy.setCurricularCourse(readCurricularCourse);

        // Theme
        if (modality.getIdInternal().equals(infoCandidacy.getInfoModality().getIdInternal())) {
            candidacy.setTheme(null);
        } else {
            final Theme readTheme = rootDomainObject.readThemeByOID(infoCandidacy.getTheme().getIdInternal());
            candidacy.setTheme(readTheme);
        }
        if (!infoCandidacy.getInfoSeminary().getHasThemes().booleanValue()) {
            candidacy.setTheme(null);
        }

        // Seminary Case Study Choices
        for (InfoCaseStudyChoice infoCaseStudyChoice : (List<InfoCaseStudyChoice>) infoCandidacy.getCaseStudyChoices()) {
            final CaseStudyChoice caseStudyChoice = DomainFactory.makeCaseStudyChoice();
            
            caseStudyChoice.setOrder(infoCaseStudyChoice.getOrder());

            final CaseStudy caseStudy = rootDomainObject.readCaseStudyByOID(infoCaseStudyChoice.getCaseStudy().getIdInternal());
            caseStudyChoice.setCaseStudy(caseStudy);

            caseStudyChoice.setCandidacy(candidacy);
            candidacy.addCaseStudyChoices(caseStudyChoice);
        }
    }

}
