package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCandidateSituation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExportGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExternalPerson;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuide;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuideEntry;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMark;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeProofVersion;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPrice;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRestriction;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTutor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.CandidacyOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.CaseStudyOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.EquivalencyOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.ModalityOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.SeminaryOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.ThemeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.cms.PersistentCMSOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.cms.PersistentMailAddressAliasOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.cms.PersistentMailingListOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.credits.ManagementPositionCreditLineOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.credits.OtherTypeCreditLineOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.degree.finalProject.TeacherDegreeFinalProjectStudentOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.gaugingTests.physics.GaugingTestResultOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.gaugingTests.physics.IPersistentGaugingTestResult;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantContractMovementOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantContractOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantContractRegimeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantCostCenterOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantInsuranceOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantOrientationTeacherOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantPartOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantPaymentEntityOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantSubsidyOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantTypeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.owner.GrantOwnerOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.gratuity.masterDegree.SibsPaymentFileEntryOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.gratuity.masterDegree.SibsPaymentFileOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.guide.ReimbursementGuideOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.inquiries.InquiriesRegistryOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.inquiries.OldInquiriesCoursesResOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.inquiries.OldInquiriesSummaryOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.inquiries.OldInquiriesTeachersResOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness.CostCenterOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness.ExtraWorkResquestsOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness.MoneyCostCenterOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.DistributedTestAdvisoryOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.DistributedTestOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.MetadataOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.OnlineTestOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.QuestionOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.StudentTestLogOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.StudentTestQuestionOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.TestOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.TestQuestionOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.TestScopeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.projectsManagement.ProjectAccessOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.AuthorshipOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.PublicationAttributeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.PublicationFormatOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.PublicationTeacherOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.PublicationTypeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.sms.SentSmsOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.student.DelegateOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.ExternalActivityOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.OldPublicationOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.OrientationOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.PublicationsNumberOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.ServiceProviderRegimeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.professorship.NonAffiliatedTeacherOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.professorship.ShiftProfessorshipOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.professorship.SupportLessonOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.workingTime.TeacherInstitutionWorkingTimeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.transactions.GratuityTransactionOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.transactions.InsuranceTransactionOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.transactions.PaymentTransactionOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.transactions.ReimbursementTransactionOJB;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminary;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudy;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCurricularCourseEquivalency;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryModality;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryTheme;
import net.sourceforge.fenixedu.persistenceTier.cache.FenixCache;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentCMS;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailAddressAlias;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailingList;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentOtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;
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
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesSummary;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentCostCenter;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkRequests;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentMoneyCostCenter;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTestAdvisory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentOnlineTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestScope;
import net.sourceforge.fenixedu.persistenceTier.projectsManagement.IPersistentProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthorship;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationAttribute;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationFormat;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentDelegate;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOldPublication;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOrientation;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentNonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;
import net.sourceforge.fenixedu.persistenceTier.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentGratuityTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentPaymentTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentReimbursementTransaction;
import net.sourceforge.fenixedu.stm.OJBFunctionalSetWrapper;
import net.sourceforge.fenixedu.stm.Transaction;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.apache.ojb.broker.metadata.ObjectReferenceDescriptor;

import pt.utl.ist.berserk.storage.ITransactionBroker;
import pt.utl.ist.berserk.storage.exceptions.StorageException;

public class SuportePersistenteOJB implements ISuportePersistente, ITransactionBroker
{
	private static SuportePersistenteOJB _instance = null;

	private static HashMap<String,DescriptorRepository> descriptorMap = null;

	public void setDescriptor(DescriptorRepository descriptorRepository, String hashName)
	{
		descriptorMap.put(hashName, descriptorRepository);
	}

	public DescriptorRepository getDescriptor(String hashName)
	{

		return (DescriptorRepository) descriptorMap.get(hashName);
	}

	public static PersistenceBroker getCurrentPersistenceBroker()
	{
		return Transaction.getOJBBroker();
	}

	public void clearCache()
	{
		getCurrentPersistenceBroker().clearCache();
	}

	public Integer getNumberCachedItems()
	{
		return new Integer(FenixCache.getNumberOfCachedItems());
	}

	public static synchronized SuportePersistenteOJB getInstance() throws ExcepcaoPersistencia
	{
		if (_instance == null)
		{
			_instance = new SuportePersistenteOJB();
		}
		if (descriptorMap == null)
		{
			descriptorMap = new HashMap<String,DescriptorRepository>();

		}
		return _instance;
	}

	public static synchronized void resetInstance()
	{
		if (_instance != null)
		{
			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			broker.clearCache();
			_instance = null;
		}
	}

	/** Creates a new instance of SuportePersistenteOJB */
	private SuportePersistenteOJB()
	{
	}

	protected void finalize() throws Throwable
	{
	}

	public void iniciarTransaccao()
	{
		// commit any current transaction
		if (Transaction.current() != null)
		{
			Transaction.commit();
		}
		Transaction.begin();
	}

	public void confirmarTransaccao()
	{
		Transaction.checkpoint();
		Transaction.currentFenixTransaction().setReadOnly();
	}

	public void cancelarTransaccao()
	{
		Transaction.abort();
	}

	public IFrequentaPersistente getIFrequentaPersistente()
	{
		return new FrequentaOJB();
	}

	public IPersistentCurricularCourse getIPersistentCurricularCourse()
	{
		return new CurricularCourseOJB();
	}

	public IPersistentExecutionCourse getIPersistentExecutionCourse()
	{
		return new ExecutionCourseOJB();
	}

	public IPessoaPersistente getIPessoaPersistente()
	{
		return new PessoaOJB();
	}

	public IPersistentExecutionDegree getIPersistentExecutionDegree()
	{
		return new CursoExecucaoOJB();
	}

	public IPersistentStudent getIPersistentStudent()
	{
		return new StudentOJB();
	}

	public IPersistentStudentCurricularPlan getIStudentCurricularPlanPersistente()
	{
		return new StudentCurricularPlanOJB();
	}

	public IPersistentMasterDegreeCandidate getIPersistentMasterDegreeCandidate()
	{
		return new MasterDegreeCandidateOJB();
	}

	public IPersistentCandidateSituation getIPersistentCandidateSituation()
	{
		return new CandidateSituationOJB();
	}

	/**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentExecutionPeriod()
	 */
	public IPersistentExecutionPeriod getIPersistentExecutionPeriod()
	{
		return new ExecutionPeriodOJB();
	}

	/**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentExecutionYear()
	 */

	public IPersistentExecutionYear getIPersistentExecutionYear()
	{
		return new ExecutionYearOJB();
	}

	/**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentCurriculum()
	 */
	public IPersistentCurriculum getIPersistentCurriculum()
	{
		return new CurriculumOJB();
	}

	public IPersistentExam getIPersistentExam()
	{
		return new ExamOJB();
	}

	public IPersistentPrice getIPersistentPrice()
	{
		return new PriceOJB();
	}

	public IPersistentGuideEntry getIPersistentGuideEntry()
	{
		return new GuideEntryOJB();
	}

	public IPersistentGuide getIPersistentGuide()
	{
		return new GuideOJB();
	}

	public IPersistentCurricularCourseScope getIPersistentCurricularCourseScope()
	{
		return new CurricularCourseScopeOJB();
	}

	public IPersistentRestriction getIPersistentRestriction()
	{
		return new RestrictionOJB();
	}

	public IPersistentEnrolmentPeriod getIPersistentEnrolmentPeriod()
	{
		return new PersistentEnrolmentPeriod();
	}

	public IPersistentShiftProfessorship getIPersistentTeacherShiftPercentage()
	{
		return new ShiftProfessorshipOJB();
	}

	public IPersistentMark getIPersistentMark()
	{
		return new MarkOJB();
	}

	public IPersistentSummary getIPersistentSummary()
	{
		return new SummaryOJB();
	}

	public IPersistentSeminaryModality getIPersistentSeminaryModality()
	{
		return new ModalityOJB();
	}

	public IPersistentGrantOwner getIPersistentGrantOwner()
	{
		return new GrantOwnerOJB();
	}

	public IPersistentGrantContract getIPersistentGrantContract()
	{
		return new GrantContractOJB();
	}

	public IPersistentGrantType getIPersistentGrantType()
	{
		return new GrantTypeOJB();
	}

	public IPersistentGrantOrientationTeacher getIPersistentGrantOrientationTeacher()
	{
		return new GrantOrientationTeacherOJB();
	}

	public IPersistentMasterDegreeThesisDataVersion getIPersistentMasterDegreeThesisDataVersion()
	{
		return new MasterDegreeThesisDataVersionOJB();
	}

	public IPersistentMasterDegreeProofVersion getIPersistentMasterDegreeProofVersion()
	{
		return new MasterDegreeProofVersionOJB();
	}

	public IPersistentExternalPerson getIPersistentExternalPerson()
	{
		return new ExternalPersonOJB();
	}

	public IPersistentCoordinator getIPersistentCoordinator()
	{
		return new CoordinatorOJB();
	}

	public IPersistentExternalActivity getIPersistentExternalActivity()
	{
		return new ExternalActivityOJB();
	}

	public IPersistentServiceProviderRegime getIPersistentServiceProviderRegime()
	{
		return new ServiceProviderRegimeOJB();
	}

	public IPersistentShiftProfessorship getIPersistentShiftProfessorship()
	{
		return new ShiftProfessorshipOJB();
	}

	public IPersistentReimbursementGuide getIPersistentReimbursementGuide()
	{
		return new ReimbursementGuideOJB();
	}

	public IPersistentOrientation getIPersistentOrientation()
	{
		return new OrientationOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentPublicationsNumber()
	 */
	public IPersistentPublicationsNumber getIPersistentPublicationsNumber()
	{
		return new PublicationsNumberOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentOldPublication()
	 */
	public IPersistentOldPublication getIPersistentOldPublication()
	{
		return new OldPublicationOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentGaugingTestResult()
	 */
	public IPersistentGaugingTestResult getIPersistentGaugingTestResult()
	{
		return new GaugingTestResultOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentSupportLesson()
	 */
	public IPersistentSupportLesson getIPersistentSupportLesson()
	{
		return new SupportLessonOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentTeacherDegreeFinalProjectStudent()
	 */
	public IPersistentTeacherDegreeFinalProjectStudent getIPersistentTeacherDegreeFinalProjectStudent()
	{
		return new TeacherDegreeFinalProjectStudentOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentTeacherInstitutionWorkingTime()
	 */
	public IPersistentTeacherInstitutionWorkingTime getIPersistentTeacherInstitutionWorkingTime()
	{
		return new TeacherInstitutionWorkingTimeOJB();
	}

	// Nuno Correia & Ricardo Rodrigues
	public IPersistentCurricularCourseGroup getIPersistentCurricularCourseGroup()
	{
		return new CurricularCourseGroupOJB();
	}

	// by gedl AT rnl DOT ist DOT utl DOT pt (July the 28th, 2003)
	public IPersistentSeminaryTheme getIPersistentSeminaryTheme()
	{
		return new ThemeOJB();
	}

	// by gedl AT rnl DOT ist DOT utl DOT pt (July the 28th, 2003)
	public IPersistentSeminary getIPersistentSeminary()
	{
		return new SeminaryOJB();
	}

	// by gedl AT rnl DOT ist DOT utl DOT pt (July the 28th, 2003)
	public IPersistentSeminaryCaseStudy getIPersistentSeminaryCaseStudy()
	{
		return new CaseStudyOJB();
	}

	// by gedl AT rnl DOT ist DOT utl DOT pt (July the 29th, 2003)
	public IPersistentSeminaryCandidacy getIPersistentSeminaryCandidacy()
	{
		return new CandidacyOJB();
	}

	public IPersistentSeminaryCurricularCourseEquivalency getIPersistentSeminaryCurricularCourseEquivalency()
	{
		return new EquivalencyOJB();
	}

	public IPersistentMetadata getIPersistentMetadata()
	{
		return new MetadataOJB();
	}

	public IPersistentQuestion getIPersistentQuestion()
	{
		return new QuestionOJB();
	}

	public IPersistentTest getIPersistentTest()
	{
		return new TestOJB();
	}

	public IPersistentTestQuestion getIPersistentTestQuestion()
	{
		return new TestQuestionOJB();
	}

	public IPersistentDistributedTest getIPersistentDistributedTest()
	{
		return new DistributedTestOJB();
	}

	public IPersistentStudentTestQuestion getIPersistentStudentTestQuestion()
	{
		return new StudentTestQuestionOJB();
	}

	public IPersistentStudentTestLog getIPersistentStudentTestLog()
	{
		return new StudentTestLogOJB();
	}

	public IPersistentOnlineTest getIPersistentOnlineTest()
	{
		return new OnlineTestOJB();
	}

	public IPersistentTestScope getIPersistentTestScope()
	{
		return new TestScopeOJB();
	}

	public IPersistentDistributedTestAdvisory getIPersistentDistributedTestAdvisory()
	{
		return new DistributedTestAdvisoryOJB();
	}

	public IPersistentWebSiteSection getIPersistentWebSiteSection()
	{
		return new WebSiteSectionOJB();
	}

	public IPersistentWebSiteItem getIPersistentWebSiteItem()
	{
		return new WebSiteItemOJB();
	}

	public void beginTransaction()
	{
		this.iniciarTransaccao();
	}

	// by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	public void commitTransaction()
	{
		this.confirmarTransaccao();
	}

	// by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	public void abortTransaction() throws StorageException
	{
		this.cancelarTransaccao();
	}

	// by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	public void lockRead(List list) throws StorageException
	{
	}

	// by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	public void lockRead(Object obj) throws StorageException
	{
	}

	// by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	public void lockWrite(Object obj) throws StorageException
	{
	}

	// by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	public PersistenceBroker currentBroker()
	{
		return getCurrentPersistenceBroker();
	}

	public IPersistentGratuitySituation getIPersistentGratuitySituation()
	{
		return new GratuitySituationOJB();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantCostCenter()
	 */
	public IPersistentGrantCostCenter getIPersistentGrantCostCenter()
	{
		return new GrantCostCenterOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantPart()
	 */
	public IPersistentGrantPart getIPersistentGrantPart()
	{
		return new GrantPartOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantPaymentEntity()
	 */
	public IPersistentGrantPaymentEntity getIPersistentGrantPaymentEntity()
	{
		return new GrantPaymentEntityOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantSubsidy()
	 */
	public IPersistentGrantSubsidy getIPersistentGrantSubsidy()
	{
		return new GrantSubsidyOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantContractRegime()
	 */
	public IPersistentGrantContractRegime getIPersistentGrantContractRegime()
	{
		return new GrantContractRegimeOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantContractRegime()
	 */
	public IPersistentGrantInsurance getIPersistentGrantInsurance()
	{
		return new GrantInsuranceOJB();
	}

	public IPersistentGrantContractMovement getIPersistentGrantContractMovement()
	{
		return new GrantContractMovementOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentTutor()
	 */
	public IPersistentTutor getIPersistentTutor()
	{
		return new TutorOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentDelegate()
	 */
	public IPersistentDelegate getIPersistentDelegate()
	{
		return new DelegateOJB();
	}

	public IPersistentOtherTypeCreditLine getIPersistentOtherTypeCreditLine()
	{
		return new OtherTypeCreditLineOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentManagementPosistionCreditLine()
	 */
	public IPersistentManagementPositionCreditLine getIPersistentManagementPositionCreditLine()
	{
		return new ManagementPositionCreditLineOJB();
	}

	public IPersistentFinalDegreeWork getIPersistentFinalDegreeWork()
	{
		return new FinalDegreeWorkOJB();
	}

	public IPersistentPeriod getIPersistentPeriod()
	{
		return new PeriodOJB();
	}

	public IPersistentSentSms getIPersistentSentSms()
	{
		return new SentSmsOJB();
	}

	public IPersistentPublicationType getIPersistentPublicationType()
	{
		return new PublicationTypeOJB();
	}

	public IPersistentPublicationAttribute getIPersistentPublicationAttribute()
	{
		return new PublicationAttributeOJB();
	}

	public IPersistentPublicationFormat getIPersistentPublicationFormat()
	{
		return new PublicationFormatOJB();
	}

	public IPersistentSibsPaymentFile getIPersistentSibsPaymentFile()
	{
		return new SibsPaymentFileOJB();
	}

	public IPersistentSibsPaymentFileEntry getIPersistentSibsPaymentFileEntry()
	{
		return new SibsPaymentFileEntryOJB();
	}

	public IPersistentObject getIPersistentObject()
	{
		return new PersistentObjectOJB();
	}

	public IPersistentGratuityTransaction getIPersistentGratuityTransaction()
	{
		return new GratuityTransactionOJB();
	}

	public IPersistentReimbursementTransaction getIPersistentReimbursementTransaction()
	{
		return new ReimbursementTransactionOJB();
	}

	public IPersistentInsuranceTransaction getIPersistentInsuranceTransaction()
	{
		return new InsuranceTransactionOJB();
	}

	public IPersistentExportGrouping getIPersistentExportGrouping()
	{
		return new ExportGroupingOJB();
	}

	public IPersistentOldInquiriesSummary getIPersistentOldInquiriesSummary()
	{
		return new OldInquiriesSummaryOJB();
	}

	public IPersistentOldInquiriesTeachersRes getIPersistentOldInquiriesTeachersRes()
	{
		return new OldInquiriesTeachersResOJB();
	}

	public IPersistentOldInquiriesCoursesRes getIPersistentOldInquiriesCoursesRes()
	{
		return new OldInquiriesCoursesResOJB();
	}

	public IPersistentInquiriesRegistry getIPersistentInquiriesRegistry()
	{
		return new InquiriesRegistryOJB();
	}

	public IPersistentPublicationTeacher getIPersistentPublicationTeacher()
	{
		return new PublicationTeacherOJB();
	}

	public IPersistentCostCenter getIPersistentCostCenter()
	{
		return new CostCenterOJB();
	}

	public IPersistentMoneyCostCenter getIPersistentMoneyCostCenter()
	{
		return new MoneyCostCenterOJB();
	}

	public IPersistentExtraWorkRequests getIPersistentExtraWorkRequests()
	{
		return new ExtraWorkResquestsOJB();
	}

	public IPersistentPaymentTransaction getIPersistentPaymentTransaction()
	{
		return new PaymentTransactionOJB();
	}

	public IPersistentProjectAccess getIPersistentProjectAccess()
	{
		return new ProjectAccessOJB();
	}

	public IPersistentNonAffiliatedTeacher getIPersistentNonAffiliatedTeacher()
	{
		return new NonAffiliatedTeacherOJB();
	}

	public IPersistentAuthorship getIPersistentAuthorship()
	{
		return new AuthorshipOJB();
	}

	public static void fixDescriptors()
	{
		final MetadataManager metadataManager = MetadataManager.getInstance();
		final Collection<ClassDescriptor> classDescriptors = (Collection<ClassDescriptor>) metadataManager.getGlobalRepository().getDescriptorTable().values();

		for (ClassDescriptor classDescriptor : classDescriptors)
		{
			for (ObjectReferenceDescriptor rd : (Collection<ObjectReferenceDescriptor>) classDescriptor.getObjectReferenceDescriptors())
			{
				rd.setCascadingStore(ObjectReferenceDescriptor.CASCADE_LINK);
				rd.setCascadeRetrieve(false);
				rd.setLazy(false);
			}

			for (CollectionDescriptor cod : (Collection<CollectionDescriptor>) classDescriptor.getCollectionDescriptors())
			{
				cod.setCascadingStore(ObjectReferenceDescriptor.CASCADE_NONE);
				cod.setCollectionClass(OJBFunctionalSetWrapper.class);
				cod.setCascadeRetrieve(false);
				cod.setLazy(false);
			}
		}
	}

	public IPersistentCMS getIPersistentCms()
	{
		return new PersistentCMSOJB();
	}

	public IPersistentMailAddressAlias getIPersistentMailAdressAlias()
	{
		return new PersistentMailAddressAliasOJB();
	}

	public IPersistentMailingList getIPersistentMailingList()
	{
		return new PersistentMailingListOJB();
	}   
    
}
