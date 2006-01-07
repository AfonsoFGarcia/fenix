/*
 * Created on 2005/05/19
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * 
 * @author Luis Cruz
 */
public class CandidacyVO extends VersionedObjectsBase implements IPersistentSeminaryCandidacy {

    public List readByStudentID(final Integer studentID) throws ExcepcaoPersistencia {
        final Student student = (Student) readByOID(Student.class, studentID);
        return student.getAssociatedCandidancies();
    }

    public List readByStudentIDAndSeminaryID(final Integer studentID, final Integer seminaryID)
            throws ExcepcaoPersistencia {
        final Student student = (Student) readByOID(Student.class, studentID);
        final List<Candidacy> candidacies = student.getAssociatedCandidancies();
        final List<Candidacy> result = new ArrayList<Candidacy>();
        for (final Candidacy candidacy : candidacies) {
            if (candidacy.getSeminary().getIdInternal().equals(seminaryID)) {
                result.add(candidacy);
            }
        }
        return result;
    }

    public List readByUserInput(final Integer modalityID, final Integer seminaryID, final Integer themeID,
            final Integer case1Id, final Integer case2Id, final Integer case3Id, final Integer case4Id, final Integer case5Id,
            final Integer curricularCourseID, final Integer degreeID, final Boolean approved) throws ExcepcaoPersistencia {

        final List<Candidacy> candidacies = (List<Candidacy>) readAll(Candidacy.class);
        final List<Candidacy> result = new ArrayList<Candidacy>();
        for (final Candidacy candidacy : candidacies) {
            boolean addCandidacy = true;

            if (modalityID.intValue() != -1 && !candidacy.getModality().getIdInternal().equals(modalityID)) {
                addCandidacy = false;
            }
            if (seminaryID.intValue() != -1 && !candidacy.getSeminary().getIdInternal().equals(seminaryID)) {
                addCandidacy = false;
            }
            if (curricularCourseID.intValue() != -1
                    && !candidacy.getCurricularCourse().getIdInternal().equals(curricularCourseID)) {
                addCandidacy = false;
            }
            if (themeID.intValue() != -1
                    && !candidacy.getTheme().getIdInternal().equals(themeID)
                            && !candidacy.getModality().getIdInternal().equals(1)) {
                addCandidacy = false;
            }
            if (approved != null && !candidacy.getApproved().equals(approved)) {
                addCandidacy = false;
            }
            if (degreeID.intValue() != -1 && !candidacy.getCurricularCourse().getDegreeCurricularPlan().getIdInternal().equals(degreeID)) {
                addCandidacy = false;
            }

            if (case1Id.intValue() != -1 && candidacy.getCaseStudyChoices().size() > 0 &&
                candidacy.getCaseStudyChoices().get(0).getCaseStudy()
                        .getIdInternal().intValue() != case1Id.intValue()) {
                addCandidacy = false;
            }
            if (case2Id.intValue() != -1 && candidacy.getCaseStudyChoices().size() > 1 &&
                candidacy.getCaseStudyChoices().get(1).getCaseStudy()
                        .getIdInternal().intValue() != case2Id.intValue()) {
                addCandidacy = false;
            }
            if (case3Id.intValue() != -1 && candidacy.getCaseStudyChoices().size() > 2 &&
                candidacy.getCaseStudyChoices().get(2).getCaseStudy()
                        .getIdInternal().intValue() != case3Id.intValue()) {
                addCandidacy = false;
            }
            if (case4Id.intValue() != -1 && candidacy.getCaseStudyChoices().size() > 3 &&
                candidacy.getCaseStudyChoices().get(3).getCaseStudy()
                        .getIdInternal().intValue() == case4Id.intValue()) {
                addCandidacy = false;
            }
            if (case5Id.intValue() != -1 && candidacy.getCaseStudyChoices().size() > 4 &&
                candidacy.getCaseStudyChoices().get(4).getCaseStudy()
                        .getIdInternal().intValue() == case5Id.intValue()) {
                addCandidacy = false;
            }

            if (addCandidacy) {
            	result.add(candidacy);
            }
        }

       return result;
    }

}