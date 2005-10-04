package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITeacher;

public class TeacherPersonalExpectation extends TeacherPersonalExpectation_Base {

    public TeacherPersonalExpectation() {
        super();
    }

    public TeacherPersonalExpectation(InfoTeacherPersonalExpectation infoTeacherPersonalExpectation,
            ITeacher teacher, IExecutionYear executionYear) {
        setProperties(infoTeacherPersonalExpectation);
        setTeacher(teacher);
        setExecutionYear(executionYear);
    }

    public void edit(InfoTeacherPersonalExpectation infoTeacherPersonalExpectation) {
        setProperties(infoTeacherPersonalExpectation);
    }

    private void setProperties(InfoTeacherPersonalExpectation infoTeacherPersonalExpectation) {
        setEducationMainFocus(infoTeacherPersonalExpectation.getEducationMainFocus());
        setGraduations(infoTeacherPersonalExpectation.getGraduations());
        setGraduationsDescription(infoTeacherPersonalExpectation.getGraduationsDescription());
        setCientificPosGraduations(infoTeacherPersonalExpectation.getCientificPosGraduations());
        setCientificPosGraduationsDescription(infoTeacherPersonalExpectation.getCientificPosGraduationsDescription());
        setProfessionalPosGraduations(infoTeacherPersonalExpectation.getProfessionalPosGraduations());
        setProfessionalPosGraduationsDescription(infoTeacherPersonalExpectation.getProfessionalPosGraduationsDescription());
        setSeminaries(infoTeacherPersonalExpectation.getSeminaries());
        setSeminariesDescription(infoTeacherPersonalExpectation.getSeminariesDescription());
        setResearchAndDevProjects(infoTeacherPersonalExpectation.getResearchAndDevProjects());
        setJornalArticlePublications(infoTeacherPersonalExpectation.getJornalArticlePublications());
        setBookPublications(infoTeacherPersonalExpectation.getBookPublications());
        setConferencePublications(infoTeacherPersonalExpectation.getConferencePublications());
        setTechnicalReportPublications(infoTeacherPersonalExpectation.getTechnicalReportPublications());
        setPatentPublications(infoTeacherPersonalExpectation.getPatentPublications());
        setOtherPublications(infoTeacherPersonalExpectation.getOtherPublications());
        setOtherPublicationsDescription(infoTeacherPersonalExpectation.getOtherPublicationsDescription());
        setResearchAndDevMainFocus(infoTeacherPersonalExpectation.getResearchAndDevMainFocus());
        setPhdOrientations(infoTeacherPersonalExpectation.getPhdOrientations());
        setMasterDegreeOrientations(infoTeacherPersonalExpectation.getMasterDegreeOrientations());
        setFinalDegreeWorkOrientations(infoTeacherPersonalExpectation.getFinalDegreeWorkOrientations());
        setOrientationsMainFocus(infoTeacherPersonalExpectation.getOrientationsMainFocus());
        setUniversityServiceMainFocus(infoTeacherPersonalExpectation.getUniversityServiceMainFocus());
        setDepartmentOrgans(infoTeacherPersonalExpectation.getDepartmentOrgans());
        setIstOrgans(infoTeacherPersonalExpectation.getIstOrgans());
        setUtlOrgans(infoTeacherPersonalExpectation.getUtlOrgans());
        setProfessionalActivityMainFocus(infoTeacherPersonalExpectation
                .getProfessionalActivityMainFocus());
        setCientificComunityService(infoTeacherPersonalExpectation.getCientificComunityService());
        setSocietyService(infoTeacherPersonalExpectation.getSocietyService());
        setConsulting(infoTeacherPersonalExpectation.getConsulting());
        setCompanySocialOrgans(infoTeacherPersonalExpectation.getCompanySocialOrgans());
        setCompanyPositions(infoTeacherPersonalExpectation.getCompanyPositions());

    }

}
