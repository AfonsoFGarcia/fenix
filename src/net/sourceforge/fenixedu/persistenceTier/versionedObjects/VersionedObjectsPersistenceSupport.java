package net.sourceforge.fenixedu.persistenceTier.versionedObjects;

import net.sourceforge.fenixedu.persistenceTier.*;
import net.sourceforge.fenixedu.persistenceTier.OJB.gaugingTests.physics.IPersistentGaugingTestResult;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminary;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudy;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudyChoice;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCurricularCourseEquivalency;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryModality;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryTheme;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentOtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentServiceExemptionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseHistoric;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentStudentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPart;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantType;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFile;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFileEntry;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuide;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuideEntry;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesCourse;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRoom;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesTeacher;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesSummary;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentCostCenter;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWork;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkCompensation;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkHistoric;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkRequests;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentMoneyCostCenter;
import net.sourceforge.fenixedu.persistenceTier.places.campus.IPersistentCampus;
import net.sourceforge.fenixedu.persistenceTier.projectsManagement.IPersistentProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthor;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationAttribute;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationFormat;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentDelegate;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentSenior;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCareer;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCategory;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOldPublication;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOrientation;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentWeeklyOcupation;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentNonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;
import net.sourceforge.fenixedu.persistenceTier.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentGratuityTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentPaymentTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentReimbursementTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentSmsTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentTransaction;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.AdvisoryVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.AnnouncementVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.AttendInAttendsSetVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.AttendsSetVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.AulaVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.AuthorVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.BibliographicReferenceVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.BuildingVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CandidateEnrolmentVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CandidateSituationVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ContributorVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CoordinatorVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CountryVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CourseReportVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CurricularCourseEquivalenceVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CurriculumVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CursoExecucaoVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CursoVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.DegreeCurricularPlanVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.DegreeInfoVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.DepartmentVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.EnrollmentVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.EnrolmentEquivalenceVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.EquivalentEnrolmentForEnrolmentEquivalenceVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.EvaluationExecutionCourseVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.EvaluationMethodVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.EvaluationVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ExamExecutionCourseVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ExamStudentRoomVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ExamVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ExecutionPeriodVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ExecutionYearVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.GratuitySituationVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.GuideSituationVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.GuideVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.InsuranceValueVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ItemVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.MasterDegreeProofVersionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.MasterDegreeThesisDataVersionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.MasterDegreeThesisVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PaymentPhaseVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PeriodVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PersonAccountVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PrecedenceVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ProfessorshipVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PublicationAuthorVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PublicationTeacherVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PublicationTypeVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PublicationVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.PublicationsNumberVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ResponsibleForVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.RestrictionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.ScientificAreaVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SecretaryEnrolmentStudentVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SectionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SibsPaymentFileEntryVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SibsPaymentFileVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SiteVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.StudentKindVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SummaryVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.SupportLessonVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.TeacherVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.TurmaTurnoVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.TurmaVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.TurnoAlunoVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.UniversityVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.WrittenEvaluationCurricularCourseScopeVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.WrittenTestVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.credits.ManagementPositionCreditLineVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.credits.OtherTypeCreditLineVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.credits.ServiceExemptionCreditLineVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.degree.finalProject.TeacherDegreeFinalProjectStudentVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantContractMovementVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantContractRegimeVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantContractVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantCostCenterVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantInsuranceVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantOrientationTeacherVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantPartVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantPaymentEntityVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantSubsidyVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantTypeVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.owner.GrantOwnerVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.places.campus.CampusVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries.CandidacyVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries.CaseStudyChoiceVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries.CaseStudyVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries.EquivalencyVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries.ModalityVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries.SeminaryVO;

public class VersionedObjectsPersistenceSupport implements ISuportePersistente {

    private static final VersionedObjectsPersistenceSupport instance = new VersionedObjectsPersistenceSupport();

    static {
    }

    private VersionedObjectsPersistenceSupport() {
    }

    public static VersionedObjectsPersistenceSupport getInstance() {
        return instance;
    }

    public IPersistentBibliographicReference getIPersistentBibliographicReference() {
        return new BibliographicReferenceVO();
    }

    public IPersistentStudentGroup getIPersistentStudentGroup() {
        return null;
    }

    public IPersistentGratuityTransaction getIPersistentGratuityTransaction() {
        return null;
    }

    public IPersistentTestScope getIPersistentTestScope() {
        return null;
    }

    public IPersistentCareer getIPersistentCareer() {
        return null;
    }

    public IPessoaPersistente getIPessoaPersistente() {
        return null;
    }

    public IPersistentStudentGroupAttend getIPersistentStudentGroupAttend() {
        return null;
    }

    public IPersistentPrice getIPersistentPrice() {
        return null;
    }

    public IPersistentOtherTypeCreditLine getIPersistentOtherTypeCreditLine() {
        return new OtherTypeCreditLineVO();
    }

    public IPersistentScientificArea getIPersistentScientificArea() {
        return new ScientificAreaVO();
    }

    public IPersistentStudentTestLog getIPersistentStudentTestLog() {
        return null;
    }

    public IPersistentPublicationsNumber getIPersistentPublicationsNumber() {
        return new PublicationsNumberVO();
    }

    public IPersistentEvaluationMethod getIPersistentEvaluationMethod() {
        return new EvaluationMethodVO();
    }

    public IPersistentResponsibleFor getIPersistentResponsibleFor() {
        return new ResponsibleForVO();
    }

    public IPersistentWrittenEvaluationCurricularCourseScope getIPersistentWrittenEvaluationCurricularCourseScope() {
        return new WrittenEvaluationCurricularCourseScopeVO();
    }

    public IPersistentPublicationAuthor getIPersistentPublicationAuthor() {
        return new PublicationAuthorVO();
    }

    public IPersistentExam getIPersistentExam() {
        return new ExamVO();
    }

    public IPersistentServiceExemptionCreditLine getIPersistentServiceExemptionCreditLine() {
        return new ServiceExemptionCreditLineVO();
    }

    public IPersistentExamExecutionCourse getIPersistentExamExecutionCourse() {
        return new ExamExecutionCourseVO();
    }

    public IPersistentReimbursementTransaction getIPersistentReimbursementTransaction() {
        return null;
    }

    public IPersistentGrantOrientationTeacher getIPersistentGrantOrientationTeacher() {
        return new GrantOrientationTeacherVO();
    }

    public IPersistentReimbursementGuide getIPersistentReimbursementGuide() {
        return null;
    }

    public IPersistentExamStudentRoom getIPersistentExamStudentRoom() {
        return new ExamStudentRoomVO();
    }

    public IPersistentStudentTestQuestion getIPersistentStudentTestQuestion() {
        return null;
    }

    public IPersistentPublication getIPersistentPublication() {
        return new PublicationVO();
    }

    public IPersistentMasterDegreeThesis getIPersistentMasterDegreeThesis() {
        return new MasterDegreeThesisVO();
    }

    public IPersistentStudentCurricularPlan getIStudentCurricularPlanPersistente() {
        return null;
    }

    public IPersistentBranch getIPersistentBranch() {
        return null;
    }

    public IPersistentCountry getIPersistentCountry() {
        return new CountryVO();
    }

    public IPersistentOrientation getIPersistentOrientation() {
        return null;
    }

    public IPersistentGrantContractMovement getIPersistentGrantContractMovement() {
        return new GrantContractMovementVO();
    }

    public IPersistentWrittenTest getIPersistentWrittenTest() {
        return new WrittenTestVO();
    }

    public IPersistentPublicationType getIPersistentPublicationType() {
        return new PublicationTypeVO();
    }

    public IPersistentSeminaryCaseStudyChoice getIPersistentSeminaryCaseStudyChoice() {
        return new CaseStudyChoiceVO();
    }

    public IPersistentAdvisory getIPersistentAdvisory() {
        return new AdvisoryVO();
    }

    public IPersistentMasterDegreeCandidate getIPersistentMasterDegreeCandidate() {
        return null;
    }

    public IPersistentSeminaryCaseStudy getIPersistentSeminaryCaseStudy() {
        return new CaseStudyVO();
    }

    public IPersistentEvaluation getIPersistentEvaluation() {
        return new EvaluationVO();
    }

    public IPersistentSmsTransaction getIPersistentSmsTransaction() {
        return null;
    }

    public IPersistentTutor getIPersistentTutor() {
        return null;
    }

    public IPersistentWebSite getIPersistentWebSite() {
        return null;
    }

    public IDisciplinaDepartamentoPersistente getIDisciplinaDepartamentoPersistente() {
        return null;
    }

    public IPersistentSeminaryModality getIPersistentSeminaryModality() {
        return new ModalityVO();
    }

    public IPersistentAttendsSet getIPersistentAttendsSet() {
        return new AttendsSetVO();
    }

    public IPersistentRole getIPersistentRole() {
        return null;
    }

    public IPersistentCreditsInAnySecundaryArea getIPersistentCreditsInAnySecundaryArea() {
        return null;
    }

    public IPersistentGrantInsurance getIPersistentGrantInsurance() {
        return new GrantInsuranceVO();
    }

    public ITurnoPersistente getITurnoPersistente() {
        return null;
    }

    public IPersistentDegreeCurricularPlan getIPersistentDegreeCurricularPlan() {
        return new DegreeCurricularPlanVO();
    }

    public IPersistentDelegate getIPersistentDelegate() {
        return null;
    }

    public IPersistentCurricularCourseGroup getIPersistentCurricularCourseGroup() {
        return null;
    }

    public ITurmaTurnoPersistente getITurmaTurnoPersistente() {
        return new TurmaTurnoVO();
    }

    public IPersistentWorkLocation getIPersistentWorkLocation() {
        return null;
    }

    public IPersistentUniversity getIPersistentUniversity() {
        return new UniversityVO();
    }

    public IPersistentCourseHistoric getIPersistentCourseHistoric() {
        return null;
    }

    public IPersistentPaymentTransaction getIPersistentPaymentTransaction() {
        return null;
    }

    public IPersistentCostCenter getIPersistentCostCenter() {
        return null;
    }

    public IPersistentGrantCostCenter getIPersistentGrantCostCenter() {
        return new GrantCostCenterVO();
    }

    public IPersistentInsuranceValue getIPersistentInsuranceValue() {
        return new InsuranceValueVO();
    }

    public IPersistentGlossaryEntries getIPersistentGlossaryEntries() {
        return null;
    }

    public IPersistentCurricularSemester getIPersistentCurricularSemester() {
        return null;
    }

    public IFrequentaPersistente getIFrequentaPersistente() {
        return null;
    }

    public ISalaPersistente getISalaPersistente() {
        return null;
    }

    public IPersistentEnrolmentEvaluation getIPersistentEnrolmentEvaluation() {
        return null;
    }

    public void confirmarTransaccao() throws ExcepcaoPersistencia {
        return;
    }

    public void iniciarTransaccao() throws ExcepcaoPersistencia {
        return;
    }

    public IPersistentPeriod getIPersistentPeriod() {
        return new PeriodVO();
    }

    public IPersistentCreditsInSpecificScientificArea getIPersistentCreditsInSpecificScientificArea() {
        return null;
    }

    public IPersistentExtraWorkHistoric getIPersistentExtraWorkHistoric() {
        return null;
    }

    public ITurnoAlunoPersistente getITurnoAlunoPersistente() {
        return new TurnoAlunoVO();
    }

    public IPersistentExternalPerson getIPersistentExternalPerson() {
        return null;
    }

    public IPersistentPaymentPhase getIPersistentPaymentPhase() {
        return new PaymentPhaseVO();
    }

    public IPersistentStudent getIPersistentStudent() {
        return null;
    }

    public IPersistentResidenceCandidacies getIPersistentResidenceCandidacies() {
        return null;
    }

    public IPersistentSibsPaymentFile getIPersistentSibsPaymentFile() {
        return new SibsPaymentFileVO();
    }

    public IPersistentGrantSubsidy getIPersistentGrantSubsidy() {
        return new GrantSubsidyVO();
    }

    public IPersistentTeacherInstitutionWorkingTime getIPersistentTeacherInstitutionWorkingTime() {
        return null;
    }

    public IPersistentProjectAccess getIPersistentProjectAccess() {
        return null;
    }

    public IPersistentManagementPositionCreditLine getIPersistentManagementPositionCreditLine() {
        return new ManagementPositionCreditLineVO();
    }

    public IPersistentShiftProfessorship getIPersistentShiftProfessorship() {
        return null;
    }

    public IPersistentMetadata getIPersistentMetadata() {
        return null;
    }

    public IPersistentBuilding getIPersistentBuilding() {
        return new BuildingVO();
    }

    public IPersistentSentSms getIPersistentSentSms() {
        return null;
    }

    public IPersistentGaugingTestResult getIPersistentGaugingTestResult() {
        return null;
    }

    public IPersistentOldInquiriesSummary getIPersistentOldInquiriesSummary() {
        return null;
    }

    public IPersistentGrantOwner getIPersistentGrantOwner() {
        return new GrantOwnerVO();
    }

    public IPersistentServiceProviderRegime getIPersistentServiceProviderRegime() {
        return null;
    }

    public IPersistentGuide getIPersistentGuide() {
        return new GuideVO();
    }

    public IPersistentSeminaryTheme getIPersistentSeminaryTheme() {
        return null;
    }

    public IPersistentGuideSituation getIPersistentGuideSituation() {
        return new GuideSituationVO();
    }

    public IPersistentGratuityValues getIPersistentGratuityValues() {
        return null;
    }

    public IPersistentDistributedTestAdvisory getIPersistentDistributedTestAdvisory() {
        return null;
    }

    public IPersistentTest getIPersistentTest() {
        return null;
    }

    public IPersistentGrantPaymentEntity getIPersistentGrantPaymentEntity() {
        return new GrantPaymentEntityVO();
    }

    public IPersistentPersonalDataUseInquiryAnswers getIPersistentPersonalDataUseInquiryAnswers() {
        return null;
    }

    public IPersistentPublicationAttribute getIPersistentPublicationAttribute() {
        return null;
    }

    public IPersistentPublicationTeacher getIPersistentPublicationTeacher() {
        return new PublicationTeacherVO();
    }

    public IPersistentCampus getIPersistentCampus() {
        return new CampusVO();
    }

    public IPersistentCurricularCourse getIPersistentCurricularCourse() {
        return null;
    }

    public IPersistentGuideEntry getIPersistentGuideEntry() {
        return null;
    }

    public IPersistentGrantContract getIPersistentGrantContract() {
        return new GrantContractVO();
    }

    public IPersistentTeacher getIPersistentTeacher() {
        return new TeacherVO();
    }

    public IPersistentEnrolmentEquivalence getIPersistentEnrolmentEquivalence() {
        return new EnrolmentEquivalenceVO();
    }

    public ITurmaPersistente getITurmaPersistente() {
        return new TurmaVO();
    }

    public IPersistentSummary getIPersistentSummary() {
        return new SummaryVO();
    }

    public IPersistentSite getIPersistentSite() {
        return new SiteVO();
    }

    public IPersistentFAQSection getIPersistentFAQSection() {
        return null;
    }

    public IPersistentWebSiteItem getIPersistentWebSiteItem() {
        return null;
    }

    public IPersistentQualification getIPersistentQualification() {
        return null;
    }

    public IPersistentGratuitySituation getIPersistentGratuitySituation() {
        return new GratuitySituationVO();
    }

    public IPersistentOnlineTest getIPersistentOnlineTest() {
        return null;
    }

    public IPersistentWeeklyOcupation getIPersistentWeeklyOcupation() {
        return null;
    }

    public IPersistentEnrolmentPeriod getIPersistentEnrolmentPeriod() {
        return null;
    }

    public IPersistentExtraWorkCompensation getIPersistentExtraWorkCompensation() {
        return null;
    }

    public IPersistentGrantContractRegime getIPersistentGrantContractRegime() {
        return new GrantContractRegimeVO();
    }

    public IPersistentPersonRole getIPersistentPersonRole() {
        return null;
    }

    public IPersistentItem getIPersistentItem() {
        return new ItemVO();
    }

    public IPersistentRestriction getIPersistentRestriction() {
        return new RestrictionVO();
    }

    public IPersistentQuestion getIPersistentQuestion() {
        return null;
    }

    public IPersistentCourseReport getIPersistentCourseReport() {
        return new CourseReportVO();
    }

    public IPersistentCandidateSituation getIPersistentCandidateSituation() {
        return new CandidateSituationVO();
    }

    public Integer getNumberCachedItems() {
        return null;
    }

    public IPersistentStudentKind getIPersistentStudentKind() {
        return new StudentKindVO();
    }

    public IPersistentSeminary getIPersistentSeminary() {
        return new SeminaryVO();
    }

    public IPersistentGrantType getIPersistentGrantType() {
        return new GrantTypeVO();
    }

    public IPersistentGroupPropertiesExecutionCourse getIPersistentGroupPropertiesExecutionCourse() {
        return null;
    }

    public IAulaPersistente getIAulaPersistente() {
        return new AulaVO();
    }

    public IPersistentAttendInAttendsSet getIPersistentAttendInAttendsSet() {
        return new AttendInAttendsSetVO();
    }

    public IPersistentSibsPaymentFileEntry getIPersistentSibsPaymentFileEntry() {
        return new SibsPaymentFileEntryVO();
    }

    public IPersistentFinalDegreeWork getIPersistentFinalDegreeWork() {
        return null;
    }

    public IPersistentInsuranceTransaction getIPersistentInsuranceTransaction() {
        return null;
    }

    public IPersistentEquivalentEnrolmentForEnrolmentEquivalence getIPersistentEquivalentEnrolmentForEnrolmentEquivalence() {
        return new EquivalentEnrolmentForEnrolmentEquivalenceVO();
    }

    public IPersistentGroupProperties getIPersistentGroupProperties() {
        return null;
    }

    public IPersistentMasterDegreeProofVersion getIPersistentMasterDegreeProofVersion() {
        return new MasterDegreeProofVersionVO();
    }

    public IPersistentCurriculum getIPersistentCurriculum() {
        return new CurriculumVO();
    }

    public IPersistentPublicationFormat getIPersistentPublicationFormat() {
        return null;
    }

    public void cancelarTransaccao() throws ExcepcaoPersistencia {
        return;
    }

    public IPersistentMasterDegreeThesisDataVersion getIPersistentMasterDegreeThesisDataVersion() {
        return new MasterDegreeThesisDataVersionVO();
    }

    public IPersistentEnrollment getIPersistentEnrolment() {
        return new EnrollmentVO();
    }

    public IPersistentSection getIPersistentSection() {
        return new SectionVO();
    }

    public void clearCache() {
        return;
    }

    public IPersistentDegreeCurricularPlanEnrolmentInfo getIPersistentDegreeEnrolmentInfo() {
        return null;
    }

    public IPersistentExternalActivity getIPersistentExternalActivity() {
        return null;
    }

    public IPersistentReimbursementGuideEntry getIPersistentReimbursementGuideEntry() {
        return null;
    }

    public IPersistentGrantPart getIPersistentGrantPart() {
        return new GrantPartVO();
    }

    public IPersistentSeminaryCandidacy getIPersistentSeminaryCandidacy() {
        return new CandidacyVO();
    }

    public IPersistentSeminaryCurricularCourseEquivalency getIPersistentSeminaryCurricularCourseEquivalency() {
        return new EquivalencyVO();
    }

    public IPersistentStudentCourseReport getIPersistentStudentCourseReport() {
        return null;
    }

    public IPersistentMoneyCostCenter getIPersistentMoneyCostCenter() {
        return null;
    }

    public IPersistentSupportLesson getIPersistentSupportLesson() {
        return new SupportLessonVO();
    }

    public IPersistentTransaction getIPersistentTransaction() {
        return null;
    }

    public IPersistentCoordinator getIPersistentCoordinator() {
        return new CoordinatorVO();
    }

    public ICursoPersistente getICursoPersistente() {
        return new CursoVO();
    }

    public IPersistentCurricularYear getIPersistentCurricularYear() {
        return null;
    }

    public IPersistentWebSiteSection getIPersistentWebSiteSection() {
        return null;
    }

    public IPersistentPersonAccount getIPersistentPersonAccount() {
        return new PersonAccountVO();
    }

    public IPersistentExecutionYear getIPersistentExecutionYear() {
        return new ExecutionYearVO();
    }

    public IPersistentOldInquiriesTeachersRes getIPersistentOldInquiriesTeachersRes() {
        return null;
    }

    public IPersistentFAQEntries getIPersistentFAQEntries() {
        return null;
    }

    public IPersistentObject getIPersistentObject() {
        return null;
    }

    public IPersistentProfessorship getIPersistentProfessorship() {
        return new ProfessorshipVO();
    }

    public IPersistentSenior getIPersistentSenior() {
        return null;
    }

    public IPersistentEvaluationExecutionCourse getIPersistentEvaluationExecutionCourse() {
        return new EvaluationExecutionCourseVO();
    }

    public IPersistentExtraWork getIPersistentExtraWork() {
        return null;
    }

    public IPersistentAnnouncement getIPersistentAnnouncement() {
        return new AnnouncementVO();
    }

    public IPersistentExtraWorkRequests getIPersistentExtraWorkRequests() {
        return null;
    }

    public IPersistentTeacherDegreeFinalProjectStudent getIPersistentTeacherDegreeFinalProjectStudent() {
        return new TeacherDegreeFinalProjectStudentVO();
    }

    public IPersistentContributor getIPersistentContributor() {
        return new ContributorVO();
    }

    public IPersistentSecretaryEnrolmentStudent getIPersistentSecretaryEnrolmentStudent() {
        return new SecretaryEnrolmentStudentVO();
    }

    public IPersistentDepartment getIDepartamentoPersistente() {
        return new DepartmentVO();
    }

    public IPersistentMark getIPersistentMark() {
        return null;
    }

    public IPersistentExecutionPeriod getIPersistentExecutionPeriod() {
        return new ExecutionPeriodVO();
    }

    public IPersistentExecutionCourse getIPersistentExecutionCourse() {
        return null;
    }

    public IPersistentExecutionDegree getIPersistentExecutionDegree() {
        return new CursoExecucaoVO();
    }

    public IPersistentPrecedence getIPersistentPrecedence() {
        return new PrecedenceVO();
    }

    public IPersistentAuthor getIPersistentAuthor() {
        return new AuthorVO();
    }

    public IPersistentShiftProfessorship getIPersistentTeacherShiftPercentage() {
        return null;
    }

    public IPersistentDistributedTest getIPersistentDistributedTest() {
        return null;
    }

    public IPersistentOldPublication getIPersistentOldPublication() {
        return null;
    }

    public IPersistentCandidateEnrolment getIPersistentCandidateEnrolment() {
        return new CandidateEnrolmentVO();
    }

    public IPersistentDegreeInfo getIPersistentDegreeInfo() {
        return new DegreeInfoVO();
    }

    public IPersistentEmployee getIPersistentEmployee() {
        return null;
    }

    public IPersistentTestQuestion getIPersistentTestQuestion() {
        return null;
    }

    public IPersistentCurricularCourseEquivalence getIPersistentCurricularCourseEquivalence() {
        return new CurricularCourseEquivalenceVO();
    }

    public IPersistentRoomOccupation getIPersistentRoomOccupation() {
        return null;
    }

    public IPersistentCurricularCourseScope getIPersistentCurricularCourseScope() {
        return null;
    }

    public IPersistentCategory getIPersistentCategory() {
        return null;
    }

    public IPersistentOldInquiriesCoursesRes getIPersistentOldInquiriesCoursesRes() {
        return null;
    }

    public IPersistentInquiriesCourse getIPersistentInquiriesCourse() {
        return null;
    }

    public IPersistentInquiriesRegistry getIPersistentInquiriesRegistry() {
        return null;
    }

    public IPersistentInquiriesRoom getIPersistentInquiriesRoom() {
        return null;
    }

    public IPersistentInquiriesTeacher getIPersistentInquiriesTeacher() {
        return null;
    }

    public IPersistentInstitution getIPersistentInstitution() {
        return null;
    }

    public IPersistentNonAffiliatedTeacher getIPersistentNonAffiliatedTeacher() {
        return null;
    }

}
