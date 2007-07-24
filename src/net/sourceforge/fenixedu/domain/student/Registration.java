package net.sourceforge.fenixedu.domain.student;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.SpecialSeasonCode;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.YearStudentSpecialSeasonCode;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.curriculum.CurriculumEntry;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegisteredState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.domain.studentCurriculum.Substitution;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.tests.NewTestGroup;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class Registration extends Registration_Base {

    private transient Double approvationRatio;

    private transient Double arithmeticMean;

    private transient Integer approvedEnrollmentsNumber = 0;

    static final public Comparator<Registration> NUMBER_COMPARATOR = new Comparator<Registration>() {
	public int compare(Registration o1, Registration o2) {
	    return o1.getNumber().compareTo(o2.getNumber());
	}
    };

    private Registration() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setStartDate(new YearMonthDay());
	new RegisteredState(this, AccessControl.getUserView() != null ? AccessControl.getUserView()
		.getPerson() : null, new DateTime());
    }

    public Registration(Person person, StudentCandidacy studentCandidacy) {
	this(person, null, RegistrationAgreement.NORMAL, studentCandidacy);
    }

    public Registration(Person person, Integer studentNumber) {
	this(person, studentNumber, RegistrationAgreement.NORMAL, null);
    }

    public Registration(Person person, DegreeCurricularPlan degreeCurricularPlan,
	    StudentCandidacy studentCandidacy, RegistrationAgreement agreement, CycleType cycleType) {
	this(person, null, agreement, studentCandidacy, degreeCurricularPlan);

	// create scp
	StudentCurricularPlan.createBolonhaStudentCurricularPlan(this, degreeCurricularPlan,
		StudentCurricularPlanState.ACTIVE, new YearMonthDay(), ExecutionPeriod
			.readActualExecutionPeriod(), cycleType);
    }

    public Registration(Person person, DegreeCurricularPlan degreeCurricularPlan) {
	this(person, degreeCurricularPlan, null, RegistrationAgreement.NORMAL, null);
    }

    private Registration(Person person, Integer studentNumber, RegistrationAgreement agreement,
	    StudentCandidacy studentCandidacy, DegreeCurricularPlan degreeCurricularPlan) {
	this(person, studentNumber, agreement, studentCandidacy);
	if (degreeCurricularPlan != null) {
	    setDegree(degreeCurricularPlan.getDegree());
	}
    }

    private Registration(Person person, Integer registrationNumber, RegistrationAgreement agreement,
	    StudentCandidacy studentCandidacy) {
	this();
	if (person.hasStudent()) {
	    setStudent(person.getStudent());
	} else {
	    setStudent(new Student(person, registrationNumber));
	}
	setNumber(registrationNumber);
	setPayedTuition(true);
	setStudentCandidacy(studentCandidacy);
	setRegistrationYear(ExecutionYear.readCurrentExecutionYear());
	setRequestedChangeDegree(false);
	setRequestedChangeBranch(false);
	setRegistrationAgreement(agreement == null ? RegistrationAgreement.NORMAL : agreement);
    }

    @Override
    final public void setNumber(Integer number) {
	super.setNumber(number);
	if (number == null && hasRegistrationNumber()) {
	    getRegistrationNumber().delete();
	} else if (number != null) {
	    if (hasRegistrationNumber()) {
		getRegistrationNumber().setNumber(number);
	    } else {
		new RegistrationNumber(this);
	    }
	}
    }

    @Deprecated
    final public void delete() {

	checkRulesToDelete();

	for (; !getStudentCurricularPlans().isEmpty(); getStudentCurricularPlans().get(0).delete())
	    ;

	for (; !getRegistrationStates().isEmpty(); getRegistrationStates().get(0).delete())
	    ;

	if (hasRegistrationNumber()) {
	    getRegistrationNumber().delete();
	}

	if (hasExternalRegistrationData()) {
	    getExternalRegistrationData().delete();
	}
	
	for (; hasAnyExternalEnrolments(); getExternalEnrolments().get(0).delete())
	    ;

	removeRegistrationYear();
	removeDegree();
	removeStudent();
	removeRootDomainObject();
	getShiftsSet().clear();
	super.deleteDomainObject();
    }

    private void checkRulesToDelete() {
	if (hasDfaRegistrationEvent()) {
	    throw new DomainException(
		    "error.student.Registration.cannot.delete.because.is.associated.to.dfa.registration.event");
	}
    }

    final public StudentCurricularPlan getActiveStudentCurricularPlan() {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlans()) {
	    if (studentCurricularPlan.getCurrentState() == StudentCurricularPlanState.ACTIVE
		    || studentCurricularPlan.getCurrentState() == StudentCurricularPlanState.SCHOOLPARTCONCLUDED) {
		return studentCurricularPlan;
	    }
	}
	return null;
    }

    final public StudentCurricularPlan getConcludedStudentCurricularPlan() {
	if (isConcluded()) {
	    return getLastStudentCurricularPlanExceptPast();
	}
	return null;
    }

    final public StudentCurricularPlan getLastStudentCurricularPlan() {

	if (getStudentCurricularPlans().size() == 0) {
	    return null;
	}
	return (StudentCurricularPlan) Collections.max(getStudentCurricularPlans(),
		StudentCurricularPlan.STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE);
    }

    final public StudentCurricularPlan getLastStudentCurricularPlanExceptPast() {

	if (getStudentCurricularPlansExceptPast().size() == 0) {
	    return null;
	}
	return (StudentCurricularPlan) Collections.max(getStudentCurricularPlansExceptPast(),
		new BeanComparator("startDateYearMonthDay"));
    }

    final public StudentCurricularPlan getCurrentStudentCurricularPlan() {
	return getLastStudentCurricularPlanExceptPast();
    }

    final public StudentCurricularPlan getFirstStudentCurricularPlan() {
	return (StudentCurricularPlan) Collections.min(getStudentCurricularPlans(), new BeanComparator(
		"startDateYearMonthDay"));
    }

    final public List<StudentCurricularPlan> getStudentCurricularPlansExceptPast() {
	List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
	for (StudentCurricularPlan studentCurricularPlan : super.getStudentCurricularPlans()) {
	    if (!studentCurricularPlan.isPast()) {
		result.add(studentCurricularPlan);
	    }
	}
	return result;
    }

    final public StudentCurricularPlan getActiveOrConcludedStudentCurricularPlan() {
	StudentCurricularPlan concludedStudentCurricularPlan = null;
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlans()) {
	    if (studentCurricularPlan.getCurrentState() == StudentCurricularPlanState.ACTIVE) {
		return studentCurricularPlan;
	    }
	    if (concludedStudentCurricularPlan == null
		    && studentCurricularPlan.getCurrentState() == StudentCurricularPlanState.SCHOOLPARTCONCLUDED) {
		concludedStudentCurricularPlan = studentCurricularPlan;
	    }
	}
	return concludedStudentCurricularPlan;
    }

    final public StudentCurricularPlan getActiveOrConcludedOrLastStudentCurricularPlan() {
	StudentCurricularPlan studentCurricularPlan = getActiveOrConcludedStudentCurricularPlan();
	return studentCurricularPlan == null ? studentCurricularPlan = getLastStudentCurricularPlan()
		: studentCurricularPlan;
    }

    final public boolean attends(final ExecutionCourse executionCourse) {
	for (final Attends attends : getAssociatedAttends()) {
	    if (attends.getExecutionCourse() == executionCourse) {
		return true;
	    }
	}
	return false;
    }

    final public List<WrittenEvaluation> getWrittenEvaluations(final ExecutionPeriod executionPeriod) {
	final List<WrittenEvaluation> result = new ArrayList<WrittenEvaluation>();
	for (final Attends attend : this.getAssociatedAttends()) {
	    if (attend.getExecutionCourse().getExecutionPeriod() == executionPeriod) {
		for (final Evaluation evaluation : attend.getExecutionCourse()
			.getAssociatedEvaluations()) {
		    if (evaluation instanceof WrittenEvaluation && !result.contains(evaluation)) {
			result.add((WrittenEvaluation) evaluation);
		    }
		}
	    }
	}
	return result;
    }

    final public List<Exam> getEnroledExams(final ExecutionPeriod executionPeriod) {
	final List<Exam> result = new ArrayList<Exam>();
	for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this
		.getWrittenEvaluationEnrolments()) {
	    if (writtenEvaluationEnrolment.getWrittenEvaluation() instanceof Exam
		    && writtenEvaluationEnrolment.isForExecutionPeriod(executionPeriod)) {
		result.add((Exam) writtenEvaluationEnrolment.getWrittenEvaluation());
	    }
	}
	return result;
    }

    final public List<Exam> getUnenroledExams(final ExecutionPeriod executionPeriod) {
	final List<Exam> result = new ArrayList<Exam>();
	for (final Attends attend : this.getAssociatedAttends()) {
	    if (attend.getExecutionCourse().getExecutionPeriod() == executionPeriod) {
		for (final Evaluation evaluation : attend.getExecutionCourse()
			.getAssociatedEvaluations()) {
		    if (evaluation instanceof Exam && !this.isEnroledIn(evaluation)) {
			result.add((Exam) evaluation);
		    }
		}
	    }
	}
	return result;
    }

    final public List<WrittenTest> getEnroledWrittenTests(final ExecutionPeriod executionPeriod) {
	final List<WrittenTest> result = new ArrayList<WrittenTest>();
	for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this
		.getWrittenEvaluationEnrolments()) {
	    if (writtenEvaluationEnrolment.getWrittenEvaluation() instanceof WrittenTest
		    && writtenEvaluationEnrolment.isForExecutionPeriod(executionPeriod)) {
		result.add((WrittenTest) writtenEvaluationEnrolment.getWrittenEvaluation());
	    }
	}
	return result;
    }

    final public List<WrittenTest> getUnenroledWrittenTests(final ExecutionPeriod executionPeriod) {
	final List<WrittenTest> result = new ArrayList<WrittenTest>();
	for (final Attends attend : this.getAssociatedAttends()) {
	    if (attend.getExecutionCourse().getExecutionPeriod() == executionPeriod) {
		for (final Evaluation evaluation : attend.getExecutionCourse()
			.getAssociatedEvaluations()) {
		    if (evaluation instanceof WrittenTest && !this.isEnroledIn(evaluation)) {
			result.add((WrittenTest) evaluation);
		    }
		}
	    }
	}
	return result;
    }

    final public List<Project> getProjects(final ExecutionPeriod executionPeriod) {
	final List<Project> result = new ArrayList<Project>();
	for (final Attends attend : this.getAssociatedAttends()) {
	    if (attend.getExecutionCourse().getExecutionPeriod() == executionPeriod) {
		for (final Evaluation evaluation : attend.getExecutionCourse()
			.getAssociatedEvaluations()) {
		    if (evaluation instanceof Project) {
			result.add((Project) evaluation);
		    }
		}
	    }
	}
	return result;
    }

    final public boolean isEnroledIn(final Evaluation evaluation) {
	for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this
		.getWrittenEvaluationEnrolments()) {
	    if (writtenEvaluationEnrolment.getWrittenEvaluation() == evaluation) {
		return true;
	    }
	}
	return false;
    }

    final public AllocatableSpace getRoomFor(final WrittenEvaluation writtenEvaluation) {
	for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this
		.getWrittenEvaluationEnrolments()) {
	    if (writtenEvaluationEnrolment.getWrittenEvaluation() == writtenEvaluation) {
		return (AllocatableSpace) writtenEvaluationEnrolment.getRoom();
	    }
	}
	return null;
    }

    final public Double getApprovationRatio() {
	if (this.approvationRatio == null) {
	    calculateApprovationRatioAndArithmeticMeanIfActive(true);
	}
	return this.approvationRatio;
    }

    final public Double getArithmeticMean() {
	if (this.arithmeticMean == null) {
	    calculateApprovationRatioAndArithmeticMeanIfActive(true);
	}
	return Double.valueOf(Math.round(this.arithmeticMean * 100) / 100.0);
    }

    final public int getNumberOfCurriculumEntries() {
	return getCurriculumEntries().size();
    }

    final public int getNumberOfCurriculumEntries(final ExecutionYear executionYear) {
	return getCurriculumEntries(executionYear).size();
    }

    final public Collection<CurriculumEntry> getCurriculumEntries() {
	return getCurriculumEntries((ExecutionYear) null);
    }

    final public Collection<CurriculumEntry> getCurriculumEntries(final ExecutionYear executionYear) {
	return new StudentCurriculum(this).getCurriculumEntries(executionYear);
    }

    final public double getAverage() {
	return isConcluded() ? getFinalAverage() : getAverage(null);
    }

    final public double getAverage(final ExecutionYear executionYear) {
	return new StudentCurriculum(this).getRoundedAverage(executionYear, true);
    }

    @Override
    final public Integer getFinalAverage() {
	if (!isConcluded()) {
	    // throw new DomainException(
	    // "Registration.getting.final.average.mean.from.non.concluded.registration");
	    return null;
	}

	return super.getFinalAverage() == null ? new StudentCurriculum(this).getRoundedAverage(null, false).intValue() : super.getFinalAverage();
    }

    final public String getFinalAverageDescription() {
	return ResourceBundle.getBundle("resources.EnumerationResources").getString(
		getFinalAverage().toString());
    }

    final public String getFinalAverageQualified() {
	return getDegreeType().getGradeScale().getQualifiedName(getFinalAverage().toString());
    }

    final public boolean isInFinalDegreeYear() {
	return getCurricularYear() == getDegreeType().getYears();
    }

    final public boolean isQualifiedForSeniority() {
	return isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree()
		&& (isConcluded() || (isActive() && isInFinalDegreeYear()));
    }

    final public void calculateApprovationRatioAndArithmeticMeanIfActive(boolean onlyPreviousExecutionYear) {

	int enrollmentsNumber = 0;
	int approvedEnrollmentsNumber = 0;
	int actualApprovedEnrollmentsNumber = 0;
	int totalGrade = 0;

	ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	ExecutionYear previousExecutionYear = currentExecutionYear.getPreviousExecutionYear();

	for (StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlans()) {
	    for (Enrolment enrolment : studentCurricularPlan.getEnrolments()) {
		if (enrolment.getEnrolmentCondition() == EnrollmentCondition.INVISIBLE) {
		    continue;
		}

		ExecutionYear enrolmentExecutionYear = enrolment.getExecutionPeriod().getExecutionYear();
		if (onlyPreviousExecutionYear && (previousExecutionYear != enrolmentExecutionYear)) {
		    continue;
		}

		if (enrolmentExecutionYear != currentExecutionYear) {

		    enrollmentsNumber++;
		    if (enrolment.isEnrolmentStateApproved()) {
			actualApprovedEnrollmentsNumber++;

			Integer finalGrade = enrolment.getFinalGrade();
			if (finalGrade != null) {
			    approvedEnrollmentsNumber++;
			    totalGrade += finalGrade;
			} else {
			    enrollmentsNumber--;
			}
		    }
		}
	    }
	}

	setApprovedEnrollmentsNumber(Integer.valueOf(actualApprovedEnrollmentsNumber));

	setApprovationRatio((enrollmentsNumber == 0) ? 0 : (double) approvedEnrollmentsNumber
		/ enrollmentsNumber);
	setArithmeticMean((approvedEnrollmentsNumber == 0) ? 0 : (double) totalGrade
		/ approvedEnrollmentsNumber);

    }

    private void setApprovationRatio(Double approvationRatio) {
	this.approvationRatio = approvationRatio;
    }

    private void setArithmeticMean(Double arithmeticMean) {
	this.arithmeticMean = arithmeticMean;
    }

    final public Integer getApprovedEnrollmentsNumber() {
	if (this.approvedEnrollmentsNumber == null) {
	    calculateApprovationRatioAndArithmeticMeanIfActive(true);
	}
	return approvedEnrollmentsNumber;
    }

    private void setApprovedEnrollmentsNumber(Integer approvedEnrollmentsNumber) {
	this.approvedEnrollmentsNumber = approvedEnrollmentsNumber;
    }

    final public Collection<Enrolment> getLatestCurricularCoursesEnrolments(final ExecutionYear executionYear) {
	return getStudentCurricularPlan(executionYear).getLatestCurricularCoursesEnrolments(
		executionYear);
    }

    final public Collection<CurricularCourse> getCurricularCoursesApprovedByEnrolment() {
	final Collection<CurricularCourse> result = new HashSet<CurricularCourse>();

	for (final Enrolment enrolment : getApprovedEnrolments()) {
	    result.add(enrolment.getCurricularCourse());
	}

	return result;
    }

    final public boolean hasEnrolments(final Enrolment enrolment) {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    if (studentCurricularPlan.hasEnrolments(enrolment)) {
		return true;
	    }
	}

	return false;
    }

    final public Collection<Enrolment> getEnrolments(final ExecutionYear executionYear) {
	return getStudentCurricularPlan(executionYear).getEnrolmentsByExecutionYear(executionYear);
    }

    final public void addApprovedEnrolments(final Collection<Enrolment> enrolments) {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    studentCurricularPlan.addApprovedEnrolments(enrolments);
	}
    }

    final public Collection<Enrolment> getApprovedEnrolments() {
	final Collection<Enrolment> result = new HashSet<Enrolment>();

	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    result.addAll(studentCurricularPlan.getAprovedEnrolments());
	}

	return result;
    }

    final public Collection<CurriculumLine> getApprovedCurriculumLines() {
	return getLastStudentCurricularPlan().getApprovedCurriculumLines();
    }

    final public boolean hasAnyApprovedCurriculumLines() {
	return getLastStudentCurricularPlan().hasAnyApprovedCurriculumLines();
    }

    final public Collection<IEnrolment> getApprovedIEnrolments() {
	final Collection<IEnrolment> result = new HashSet<IEnrolment>();
	
	for (final CurriculumLine curriculumLine : getApprovedCurriculumLines()) {
	    if (curriculumLine.isEnrolment()) {
		result.add((Enrolment) curriculumLine);
	    } else if (curriculumLine.isDismissal()) {
		result.addAll(((Dismissal) curriculumLine).getSourceIEnrolments());
	    }
	}
	
	result.addAll(getExternalEnrolments());

	return result;
    }

    final public boolean hasAnyApprovedEnrolment() {
	return getLastStudentCurricularPlan().hasAnyApprovedEnrolment();
    }

    final public boolean hasAnyEnrolmentsIn(final ExecutionYear executionYear) {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
		if (enrolment.getExecutionPeriod().getExecutionYear() == executionYear) {
		    return true;
		}
	    }
	}
	return false;

    }

    final public Substitution getSubstitution(final IEnrolment iEnrolment) {
	return getLastStudentCurricularPlan().getSubstitution(iEnrolment);
    }
    
    final public Double getDismissalsEctsCredits() {
	return getLastStudentCurricularPlan().getDismissalsEctsCredits();
    }
    
    final public Double getDismissalsEctsCreditsExceptApproved() {
	return getLastStudentCurricularPlan().getDismissalsEctsCreditsExceptApproved();
    }
    
    final public boolean getHasExternalEnrolments() {
	return hasAnyExternalEnrolments();
    }

    final public Collection<ExecutionYear> getEnrolmentsExecutionYears() {
	final Collection<ExecutionYear> result = new ArrayList<ExecutionYear>();

	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
		result.add(enrolment.getExecutionPeriod().getExecutionYear());
	    }
	}

	return result;
    }

    final public SortedSet<ExecutionYear> getSortedEnrolmentsExecutionYears() {
	final SortedSet<ExecutionYear> result = new TreeSet<ExecutionYear>(
		ExecutionYear.COMPARATOR_BY_YEAR);
	result.addAll(getEnrolmentsExecutionYears());

	return result;
    }

    final public ExecutionYear getFirstEnrolmentExecutionYear() {
	final SortedSet<ExecutionYear> sortedEnrolmentsExecutionYears = getSortedEnrolmentsExecutionYears();
	return sortedEnrolmentsExecutionYears.isEmpty() ? null : sortedEnrolmentsExecutionYears.first();
    }

    final public ExecutionYear getLastEnrolmentExecutionYear() {
	return getSortedEnrolmentsExecutionYears().last();
    }

    final public Collection<ExecutionPeriod> getEnrolmentsExecutionPeriods() {
	final Collection<ExecutionPeriod> result = new ArrayList<ExecutionPeriod>();

	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
		result.add(enrolment.getExecutionPeriod());
	    }
	}

	return result;
    }

    final public SortedSet<ExecutionPeriod> getSortedEnrolmentsExecutionPeriods() {
	final SortedSet<ExecutionPeriod> result = new TreeSet<ExecutionPeriod>(
		ExecutionPeriod.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR);
	result.addAll(getEnrolmentsExecutionPeriods());

	return result;
    }

    final public YearMonthDay getLastApprovedEnrolmentEvaluationDate() {
	final SortedSet<Enrolment> enrolments = new TreeSet<Enrolment>(
		Enrolment.COMPARATOR_BY_LATEST_ENROLMENT_EVALUATION_AND_ID);
	enrolments.addAll(getApprovedEnrolments());

	if (enrolments.isEmpty()) {
	    throw new DomainException("Registration.no.approved.enrolments");
	}

	return enrolments.last().getLatestEnrolmentEvaluation().getExamDateYearMonthDay();
    }

    final public List<Advise> getAdvisesByTeacher(final Teacher teacher) {
	return (List<Advise>) CollectionUtils.select(getAdvises(), new Predicate() {

	    final public boolean evaluate(Object arg0) {
		Advise advise = (Advise) arg0;
		return advise.getTeacher() == teacher;
	    }
	});
    }

    final public List<Advise> getAdvisesByType(final AdviseType adviseType) {
	return (List<Advise>) CollectionUtils.select(getAdvises(), new Predicate() {
	    final public boolean evaluate(Object arg0) {
		Advise advise = (Advise) arg0;
		return advise.getAdviseType().equals(adviseType);
	    }
	});
    }

    final public Set<Attends> getOrderedAttends() {
	final Set<Attends> result = new TreeSet<Attends>(Attends.ATTENDS_COMPARATOR);
	result.addAll(getAssociatedAttends());
	return result;
    }

    final public int countCompletedCoursesForActiveUndergraduateCurricularPlan() {
	return getActiveStudentCurricularPlan().getAprovedEnrolments().size();
    }

    final public List<StudentCurricularPlan> getStudentCurricularPlansByStateAndType(
	    StudentCurricularPlanState state, DegreeType type) {
	List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
	for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
	    if (studentCurricularPlan.getCurrentState().equals(state)
		    && studentCurricularPlan.getDegreeCurricularPlan().getDegree().getDegreeType()
			    .equals(type)) {
		result.add(studentCurricularPlan);
	    }
	}
	return result;
    }

    final public List<StudentCurricularPlan> getStudentCurricularPlansBySpecialization(
	    Specialization specialization) {
	List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
	for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
	    if (studentCurricularPlan.getSpecialization() != null
		    && studentCurricularPlan.getSpecialization().equals(specialization)) {
		result.add(studentCurricularPlan);
	    }
	}
	return result;
    }

    final public List<StudentCurricularPlan> getStudentCurricularPlansByDegree(Degree degree) {
	List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
	for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
	    if (studentCurricularPlan.getDegree() == degree) {
		result.add(studentCurricularPlan);
	    }
	}
	return result;
    }

    final public List<StudentCurricularPlan> getStudentCurricularPlansBySpecializationAndState(
	    Specialization specialization, StudentCurricularPlanState state) {
	List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
	for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
	    if (studentCurricularPlan.getSpecialization() != null
		    && studentCurricularPlan.getSpecialization().equals(specialization)
		    && studentCurricularPlan.getCurrentState() != null
		    && studentCurricularPlan.getCurrentState().equals(state)) {
		result.add(studentCurricularPlan);
	    }
	}
	return result;
    }

    final public List<Attends> readAttendsInCurrentExecutionPeriod() {
	final List<Attends> attends = new ArrayList<Attends>();
	for (final Attends attend : this.getAssociatedAttendsSet()) {
	    if (attend.getExecutionCourse().getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
		attends.add(attend);
	    }
	}
	return attends;
    }

    final public List<Attends> readAttendsByExecutionPeriod(ExecutionPeriod executionPeriod) {
	List<Attends> attends = new ArrayList<Attends>();
	for (Attends attend : this.getAssociatedAttends()) {
	    if (attend.getExecutionCourse().getExecutionPeriod().equals(executionPeriod)) {
		attends.add(attend);
	    }
	}
	return attends;
    }

    @Deprecated
    final public static Registration readByUsername(String username) {
	final Person person = Person.readPersonByUsername(username);
	if (person != null) {
	    for (final Registration registration : person.getStudentsSet()) {
		return registration;
	    }
	}
	return null;
    }

    @Deprecated
    final public static Registration readStudentByNumberAndDegreeType(Integer number, DegreeType degreeType) {
	Registration nonActiveRegistration = null;
	for (Registration registration : RootDomainObject.getInstance().getRegistrations()) {
	    if (registration.getNumber().equals(number)
		    && registration.getDegreeType().equals(degreeType)) {
		if (registration.isActive()) {
		    return registration;
		}
		nonActiveRegistration = registration;
	    }
	}
	return nonActiveRegistration;
    }

    final public static Registration readRegisteredRegistrationByNumberAndDegreeType(Integer number,
	    DegreeType degreeType) {
	for (Registration registration : RootDomainObject.getInstance().getRegistrations()) {
	    if (registration.getNumber().equals(number)
		    && registration.getDegreeType().equals(degreeType)
		    && registration.isInRegisteredState()) {
		return registration;
	    }
	}
	return null;
    }

    @Deprecated
    final public static Registration readRegistrationByNumberAndDegreeTypes(Integer number,
	    DegreeType... degreeTypes) {
	final List<DegreeType> degreeTypesList = Arrays.asList(degreeTypes);
	for (Registration registration : RootDomainObject.getInstance().getRegistrations()) {
	    if (registration.getNumber().equals(number)
		    && degreeTypesList.contains(registration.getDegreeType())) {
		return registration;
	    }
	}
	return null;
    }

    final public static Collection<Registration> readRegistrationsByNumberAndDegreeTypes(Integer number,
	    DegreeType... degreeTypes) {
	List<Registration> result = new ArrayList<Registration>();
	final List<DegreeType> degreeTypesList = Arrays.asList(degreeTypes);
	for (Registration registration : RootDomainObject.getInstance().getRegistrations()) {
	    if (registration.getNumber().equals(number)
		    && degreeTypesList.contains(registration.getDegreeType())) {
		result.add(registration);
	    }
	}
	return result;
    }

    final public static List<Registration> readByNumber(Integer number) {
	final List<Registration> registrations = new ArrayList<Registration>();
	for (RegistrationNumber registrationNumber : RootDomainObject.getInstance()
		.getRegistrationNumbersSet()) {
	    if (registrationNumber.getNumber().equals(number)) {
		registrations.add(registrationNumber.getRegistration());
	    }
	}
	return registrations;
    }

    final public static List<Registration> readByNumberAndDegreeType(Integer number, DegreeType degreeType) {
	final List<Registration> registrations = new ArrayList<Registration>();
	for (RegistrationNumber registrationNumber : RootDomainObject.getInstance()
		.getRegistrationNumbersSet()) {
	    if (registrationNumber.getNumber().equals(number)
		    && registrationNumber.getRegistration().getDegreeType() == degreeType) {
		registrations.add(registrationNumber.getRegistration());
	    }
	}
	return registrations;
    }

    final public static List<Registration> readByNumberAndDegreeTypeAndAgreement(Integer number,
	    DegreeType degreeType, boolean normalAgreement) {
	final List<Registration> registrations = new ArrayList<Registration>();
	for (RegistrationNumber registrationNumber : RootDomainObject.getInstance()
		.getRegistrationNumbersSet()) {
	    if (registrationNumber.getNumber().equals(number)
		    && registrationNumber.getRegistration().getDegreeType() == degreeType
		    && registrationNumber.getRegistration().getRegistrationAgreement().isNormal() == normalAgreement) {
		registrations.add(registrationNumber.getRegistration());
	    }
	}
	return registrations;
    }

    final public static List<Registration> readMasterDegreeStudentsByNameDocIDNumberIDTypeAndStudentNumber(
	    String studentName, String docIdNumber, IDDocumentType idType, Integer studentNumber) {

	final List<Registration> registrations = new ArrayList<Registration>();

	if (studentNumber != null && studentNumber > 0) {
	    registrations.addAll(Registration.readRegistrationsByNumberAndDegreeTypes(studentNumber,
		    DegreeType.MASTER_DEGREE, DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA));
	}

	if (!StringUtils.isEmpty(studentName)) {
	    for (Person person : Person.readPersonsByName(studentName)) {
		if (person.hasStudent()) {
		    registrations.addAll(person.getStudent().getRegistrationsByDegreeTypes(
			    DegreeType.MASTER_DEGREE, DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA));
		}
	    }
	}

	if (!StringUtils.isEmpty(docIdNumber)) {
	    for (Person person : Person.readByDocumentIdNumber(docIdNumber)) {
		if (person.hasStudent()) {
		    registrations.addAll(person.getStudent().getRegistrationsByDegreeTypes(
			    DegreeType.MASTER_DEGREE, DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA));
		}
	    }
	}

	return registrations;
    }

    final public static List<Registration> readAllStudentsBetweenNumbers(Integer fromNumber, Integer toNumber) {
	int fromNumberInt = fromNumber.intValue();
	int toNumberInt = toNumber.intValue();

	final List<Registration> students = new ArrayList<Registration>();
	for (final Registration registration : RootDomainObject.getInstance().getRegistrationsSet()) {
	    int studentNumberInt = registration.getNumber().intValue();
	    if (studentNumberInt >= fromNumberInt && studentNumberInt <= toNumberInt) {
		students.add(registration);
	    }
	}
	return students;
    }

    @Deprecated
    final public static List<Registration> readStudentsByDegreeType(DegreeType degreeType) {
	return readRegistrationsByDegreeType(degreeType);
    }

    final public static List<Registration> readRegistrationsByDegreeType(DegreeType degreeType) {
	final List<Registration> students = new ArrayList<Registration>();
	for (final Registration registration : RootDomainObject.getInstance().getRegistrationsSet()) {
	    if (registration.getDegreeType().equals(degreeType)) {
		students.add(registration);
	    }
	}
	return students;
    }

    final public GratuitySituation readGratuitySituationByExecutionDegree(ExecutionDegree executionDegree) {
	GratuityValues gratuityValues = executionDegree.getGratuityValues();
	for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
	    GratuitySituation gratuitySituation = studentCurricularPlan
		    .getGratuitySituationByGratuityValues(gratuityValues);
	    if (gratuitySituation != null) {
		return gratuitySituation;
	    }
	}
	return null;
    }

    final public FinalDegreeWorkGroup findFinalDegreeWorkGroupForCurrentExecutionYear() {
	for (final GroupStudent groupStudent : getAssociatedGroupStudents()) {
	    final FinalDegreeWorkGroup group = groupStudent.getFinalDegreeDegreeWorkGroup();
	    final ExecutionDegree executionDegree = group.getExecutionDegree();
	    final ExecutionYear executionYear = executionDegree.getExecutionYear();
	    if (executionYear != null && executionYear.getState().equals(PeriodState.CURRENT)) {
		return group;
	    }
	}
	return null;
    }

    final public List readAllInsuranceTransactionByExecutionYear(ExecutionYear executionYear) {
	List<InsuranceTransaction> insuranceTransactions = new ArrayList<InsuranceTransaction>();
	for (InsuranceTransaction insuranceTransaction : this.getInsuranceTransactions()) {
	    if (insuranceTransaction.getExecutionYear().equals(executionYear)) {
		insuranceTransactions.add(insuranceTransaction);
	    }
	}
	return insuranceTransactions;
    }

    final public List<InsuranceTransaction> readAllNonReimbursedInsuranceTransactionsByExecutionYear(
	    ExecutionYear executionYear) {
	List<InsuranceTransaction> nonReimbursedInsuranceTransactions = new ArrayList<InsuranceTransaction>();
	for (InsuranceTransaction insuranceTransaction : this.getInsuranceTransactions()) {
	    if (insuranceTransaction.getExecutionYear().equals(executionYear)) {
		GuideEntry guideEntry = insuranceTransaction.getGuideEntry();
		if (guideEntry == null || guideEntry.getReimbursementGuideEntries().isEmpty()) {
		    nonReimbursedInsuranceTransactions.add(insuranceTransaction);
		} else {
		    boolean isReimbursed = false;
		    for (ReimbursementGuideEntry reimbursementGuideEntry : guideEntry
			    .getReimbursementGuideEntries()) {
			if (reimbursementGuideEntry.getReimbursementGuide()
				.getActiveReimbursementGuideSituation().getReimbursementGuideState()
				.equals(ReimbursementGuideState.PAYED)) {
			    isReimbursed = true;
			    break;
			}
		    }
		    if (!isReimbursed) {
			nonReimbursedInsuranceTransactions.add(insuranceTransaction);
		    }
		}
	    }
	}
	return nonReimbursedInsuranceTransactions;
    }

    // TODO: to remove as soon as possible
    boolean hasToPayMasterDegreeInsurance(final ExecutionYear executionYear) {
	final StudentCurricularPlan activeStudentCurricularPlan = getActiveStudentCurricularPlan();
	final boolean isSpecialization = (activeStudentCurricularPlan.getSpecialization() == Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION);

	if (isSpecialization) {

	    if (!getEnrolments(executionYear).isEmpty()) {
		return true;
	    }

	    final ExecutionDegree executionDegreeByYear = getActiveDegreeCurricularPlan()
		    .getExecutionDegreeByYear(executionYear);
	    if (executionDegreeByYear != null && executionDegreeByYear.isFirstYear()) {
		return true;
	    }

	    return false;
	}

	return readAllNonReimbursedInsuranceTransactionsByExecutionYear(executionYear).isEmpty();
    }

    final public Enrolment findEnrolmentByEnrolmentID(final Integer enrolmentID) {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    final Enrolment enrolment = studentCurricularPlan.findEnrolmentByEnrolmentID(enrolmentID);
	    if (enrolment != null) {
		return enrolment;
	    }
	}
	return null;
    }

    final public Collection<DocumentRequest> getDocumentRequests() {
	final Set<DocumentRequest> result = new HashSet<DocumentRequest>();
	for (AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
	    if (academicServiceRequest.isDocumentRequest()) {
		result.add((DocumentRequest) academicServiceRequest);
	    }
	}
	return result;
    }

    final public boolean hasDiplomaRequest() {
	for (final DocumentRequest documentRequest : getDocumentRequests()) {
	    if (documentRequest.isDiploma()) {
		return true;
	    }
	}

	return false;
    }

    // Special Season

    final public SpecialSeasonCode getSpecialSeasonCodeByExecutionYear(ExecutionYear executionYear) {
	for (YearStudentSpecialSeasonCode yearStudentSpecialSeasonCode : getYearStudentSpecialSeasonCodesSet()) {
	    if (yearStudentSpecialSeasonCode.getExecutionYear() == executionYear) {
		return yearStudentSpecialSeasonCode.getSpecialSeasonCode();
	    }
	}
	return null;
    }

    final public void setSpecialSeasonCode(ExecutionYear executionYear, SpecialSeasonCode specialSeasonCode) {
	if (specialSeasonCode == null) {
	    if (!getActiveStudentCurricularPlan().getSpecialSeasonEnrolments(executionYear).isEmpty()) {
		throw new DomainException("error.cannot.change.specialSeasonCode");
	    } else {
		deleteYearSpecialSeasonCode(executionYear);
	    }
	} else {
	    if (specialSeasonCode.getMaxEnrolments() < getActiveStudentCurricularPlan()
		    .getSpecialSeasonEnrolments(executionYear).size()) {
		throw new DomainException("error.cannot.change.specialSeasonCode");
	    } else {
		changeYearSpecialSeasonCode(executionYear, specialSeasonCode);
	    }
	}
    }

    private void changeYearSpecialSeasonCode(ExecutionYear executionYear,
	    SpecialSeasonCode specialSeasonCode) {
	YearStudentSpecialSeasonCode yearStudentSpecialSeasonCode = getYearStudentSpecialSeasonCodeByExecutionYear(executionYear);
	if (yearStudentSpecialSeasonCode != null) {
	    yearStudentSpecialSeasonCode.setSpecialSeasonCode(specialSeasonCode);
	} else {
	    new YearStudentSpecialSeasonCode(this, executionYear, specialSeasonCode);
	}
    }

    private YearStudentSpecialSeasonCode getYearStudentSpecialSeasonCodeByExecutionYear(
	    ExecutionYear executionYear) {
	for (YearStudentSpecialSeasonCode yearStudentSpecialSeasonCode : getYearStudentSpecialSeasonCodesSet()) {
	    if (yearStudentSpecialSeasonCode.getExecutionYear() == executionYear) {
		return yearStudentSpecialSeasonCode;
	    }
	}
	return null;
    }

    private void deleteYearSpecialSeasonCode(ExecutionYear executionYear) {
	for (YearStudentSpecialSeasonCode yearStudentSpecialSeasonCode : getYearStudentSpecialSeasonCodesSet()) {
	    if (yearStudentSpecialSeasonCode.getExecutionYear() == executionYear) {
		yearStudentSpecialSeasonCode.delete();
	    }
	}
    }

    // end Special Season

    // Improvement

    final public List<Enrolment> getEnrolmentsToImprov(ExecutionPeriod executionPeriod) {

	if (executionPeriod == null) {
	    throw new DomainException("error.executionPeriod.notExist");
	}

	final List<Enrolment> enrolmentsToImprov = new ArrayList<Enrolment>();
	for (final StudentCurricularPlan scp : getStudentCurricularPlans()) {
	    if (!scp.isBoxStructure()
		    && scp.getDegreeCurricularPlan().getDegree().getDegreeType().equals(
			    DegreeType.DEGREE)) {
		enrolmentsToImprov.addAll(scp.getEnrolmentsToImprov(executionPeriod));
	    }
	}
	return enrolmentsToImprov;
    }

    final public List<Enrolment> getEnroledImprovements() {
	final List<Enrolment> enroledImprovements = new ArrayList<Enrolment>();
	for (final StudentCurricularPlan scp : getStudentCurricularPlans()) {
	    if (!scp.isBoxStructure()
		    && scp.getDegreeCurricularPlan().getDegree().getDegreeType().equals(
			    DegreeType.DEGREE)) {
		enroledImprovements.addAll(scp.getEnroledImprovements());
	    }
	}
	return enroledImprovements;
    }

    final public List<ExecutionCourse> getAttendingExecutionCoursesForCurrentExecutionPeriod() {
	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	for (final Attends attends : getAssociatedAttendsSet()) {
	    if (attends.getExecutionCourse().getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
		result.add(attends.getExecutionCourse());
	    }
	}
	return result;
    }

    final public List<ExecutionCourse> getAttendingExecutionCoursesFor(final ExecutionPeriod executionPeriod) {
	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	for (final Attends attends : getAssociatedAttendsSet()) {
	    if (attends.getExecutionCourse().getExecutionPeriod() == executionPeriod) {
		result.add(attends.getExecutionCourse());
	    }
	}
	return result;
    }

    final public List<ExecutionCourse> getAttendingExecutionCoursesFor(final ExecutionYear executionYear) {
	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	for (final Attends attends : getAssociatedAttendsSet()) {
	    if (attends.isFor(executionYear)) {
		result.add(attends.getExecutionCourse());
	    }
	}

	return result;
    }

    final public List<Attends> getAttendsForExecutionPeriod(final ExecutionPeriod executionPeriod) {
	final List<Attends> result = new ArrayList<Attends>();
	for (final Attends attends : getAssociatedAttendsSet()) {
	    if (attends.getExecutionCourse().getExecutionPeriod() == executionPeriod) {
		result.add(attends);
	    }
	}
	return result;
    }

    final public List<Shift> getShiftsForCurrentExecutionPeriod() {
	final List<Shift> result = new ArrayList<Shift>();
	for (final Shift shift : getShiftsSet()) {
	    if (shift.getDisciplinaExecucao().getExecutionPeriod().getState()
		    .equals(PeriodState.CURRENT)) {
		result.add(shift);
	    }
	}
	return result;
    }

    final public List<Shift> getShiftsFor(final ExecutionPeriod executionPeriod) {
	final List<Shift> result = new ArrayList<Shift>();
	for (final Shift shift : getShiftsSet()) {
	    if (shift.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
		result.add(shift);
	    }
	}
	return result;
    }

    final public List<Shift> getShiftsFor(final ExecutionCourse executionCourse) {
	final List<Shift> result = new ArrayList<Shift>();
	for (final Shift shift : getShiftsSet()) {
	    if (shift.getDisciplinaExecucao() == executionCourse) {
		result.add(shift);
	    }
	}
	return result;
    }

    private int countNumberOfDistinctExecutionCoursesOfShiftsFor(final ExecutionPeriod executionPeriod) {
	final Set<ExecutionCourse> result = new HashSet<ExecutionCourse>();
	for (final Shift shift : getShiftsSet()) {
	    if (shift.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
		result.add(shift.getDisciplinaExecucao());
	    }
	}
	return result.size();
    }

    final public Integer getNumberOfExecutionCoursesWithEnroledShiftsFor(final ExecutionPeriod executionPeriod) {
	return getAttendingExecutionCoursesFor(executionPeriod).size()
		- countNumberOfDistinctExecutionCoursesOfShiftsFor(executionPeriod);
    }

    final public Integer getNumberOfExecutionCoursesHavingNotEnroledShiftsFor(
	    final ExecutionPeriod executionPeriod) {
	int result = 0;
	final List<Shift> enroledShifts = getShiftsFor(executionPeriod);
	for (final ExecutionCourse executionCourse : getAttendingExecutionCoursesFor(executionPeriod)) {
	    for (final ShiftType shiftType : executionCourse.getOldShiftTypesToEnrol()) {
		if (!enroledShiftsContainsShiftWithSameExecutionCourseAndShiftType(enroledShifts,
			executionCourse, shiftType)) {
		    result++;
		    break;
		}
	    }
	}
	return Integer.valueOf(result);
    }

    private boolean enroledShiftsContainsShiftWithSameExecutionCourseAndShiftType(
	    final List<Shift> enroledShifts, final ExecutionCourse executionCourse,
	    final ShiftType shiftType) {

	return CollectionUtils.exists(enroledShifts, new Predicate() {
	    final public boolean evaluate(Object object) {
		Shift enroledShift = (Shift) object;
		return enroledShift.getDisciplinaExecucao() == executionCourse
			&& enroledShift.getTipo() == shiftType;
	    }
	});
    }

    final public Set<SchoolClass> getSchoolClassesToEnrol() {
	final Set<SchoolClass> result = new HashSet<SchoolClass>();
	for (final Attends attends : getAssociatedAttendsSet()) {
	    final ExecutionCourse executionCourse = attends.getExecutionCourse();

	    if (executionCourse.getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
		result.addAll(getSchoolClassesToEnrolBy(executionCourse));
	    }
	}
	return result;
    }

    final public Set<SchoolClass> getSchoolClassesToEnrolBy(final ExecutionCourse executionCourse) {
	Set<SchoolClass> schoolClasses = executionCourse
		.getSchoolClassesBy(getActiveStudentCurricularPlan().getDegreeCurricularPlan());
	return schoolClasses.isEmpty() ? executionCourse.getSchoolClasses() : schoolClasses;
    }

    final public void addAttendsTo(final ExecutionCourse executionCourse) {

	checkIfReachedAttendsLimit();

	if (getStudent().readAttendByExecutionCourse(executionCourse) == null) {
	    final Enrolment enrolment = findEnrolment(getActiveStudentCurricularPlan(), executionCourse,
		    executionCourse.getExecutionPeriod());
	    if (enrolment != null) {
		enrolment.createAttends(this, executionCourse);
	    } else {
		Attends attends = getAttendsForExecutionCourse(executionCourse);
		if (attends != null) {
		    attends.delete();
		}
		new Attends(this, executionCourse);
	    }
	}
    }

    private Attends getAttendsForExecutionCourse(ExecutionCourse executionCourse) {
	List<Attends> attendsInExecutionPeriod = getAttendsForExecutionPeriod(executionCourse
		.getExecutionPeriod());
	for (Attends attends : attendsInExecutionPeriod) {
	    for (CurricularCourse curricularCourse : attends.getExecutionCourse()
		    .getAssociatedCurricularCoursesSet()) {
		if (executionCourse.getAssociatedCurricularCoursesSet().contains(curricularCourse)) {
		    return attends;
		}
	    }
	}
	return null;
    }

    private Enrolment findEnrolment(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionCourse executionCourse, final ExecutionPeriod executionPeriod) {
	for (final CurricularCourse curricularCourse : executionCourse
		.getAssociatedCurricularCoursesSet()) {
	    final Enrolment enrolment = studentCurricularPlan
		    .getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse, executionPeriod);
	    if (enrolment != null) {
		return enrolment;
	    }
	}
	return null;
    }

    private static final int MAXIMUM_STUDENT_ATTENDS_PER_EXECUTION_PERIOD = 10;

    private void checkIfReachedAttendsLimit() {
	if (readAttendsInCurrentExecutionPeriod().size() >= MAXIMUM_STUDENT_ATTENDS_PER_EXECUTION_PERIOD) {
	    throw new DomainException("error.student.reached.attends.limit", String
		    .valueOf(MAXIMUM_STUDENT_ATTENDS_PER_EXECUTION_PERIOD));
	}
    }

    final public void removeAttendFor(final ExecutionCourse executionCourse) {
	final Attends attend = getStudent().readAttendByExecutionCourse(executionCourse);
	if (attend != null) {
	    checkIfHasEnrolmentFor(attend);
	    checkIfHasShiftsFor(executionCourse);
	    attend.delete();
	}
    }

    private void checkIfHasEnrolmentFor(final Attends attend) {
	if (attend.hasEnrolment()) {
	    throw new DomainException("errors.student.already.enroled");
	}
    }

    private void checkIfHasShiftsFor(final ExecutionCourse executionCourse) {
	if (!getShiftsFor(executionCourse).isEmpty()) {
	    throw new DomainException("errors.student.already.enroled.in.shift");
	}
    }

    final public Tutorship getActiveTutorship() {

	StudentCurricularPlan activeStudentCurricularPlan = this
		.getActiveOrConcludedStudentCurricularPlan();
	if (activeStudentCurricularPlan == null) {
	    activeStudentCurricularPlan = this.getLastStudentCurricularPlan();
	}

	return activeStudentCurricularPlan.getActiveTutorship();
    }

    @Override
    final public Integer getNumber() {
	return (super.getNumber() != null) ? super.getNumber() : getStudent().getNumber();
    }

    final public Person getPerson() {
	return getStudent().getPerson();
    }

    final public String getEmail() {
	return getPerson().getEmail();
    }

    final public EntryPhase getEntryPhase() {
	if (hasStudentCandidacy()) {
	    return getStudentCandidacy().getEntryPhase();
	}
	return null;
    }

    final public void setEntryPhase(EntryPhase entryPhase) {
	if (hasStudentCandidacy()) {
	    getStudentCandidacy().setEntryPhase(entryPhase);
	} else {
	    throw new DomainException("error.registration.withou.student.candidacy");
	}
    }

    final public Double getEntryGrade() {
	return hasStudentCandidacy() ? getStudentCandidacy().getEntryGrade() : null;
    }

    final public void setEntryGrade(Double entryGrade) {
	if (hasStudentCandidacy()) {
	    getStudentCandidacy().setEntryGrade(entryGrade);
	} else {
	    throw new DomainException("error.registration.withou.student.candidacy");
	}
    }

    final public String getIngression() {
	return hasStudentCandidacy() ? getStudentCandidacy().getIngression() : null;
    }

    final public Ingression getIngressionEnum() {
	return getIngression() != null ? Ingression.valueOf(getIngression()) : null;
    }

    final public void setIngression(String ingression) {
	if (hasStudentCandidacy()) {
	    getStudentCandidacy().setIngression(ingression);
	} else {
	    throw new DomainException("error.registration.withou.student.candidacy");
	}

    }

    final public String getContigent() {
	return hasStudentCandidacy() ? getStudentCandidacy().getContigent() : null;
    }

    final public void setContigent(String contigent) {
	if (hasStudentCandidacy()) {
	    getStudentCandidacy().setContigent(contigent);
	} else {
	    throw new DomainException("error.registration.withou.student.candidacy");
	}

    }

    @Override
    final public Degree getDegree() {
	return super.getDegree() != null ? super.getDegree()
		: (hasAnyStudentCurricularPlans() ? getLastStudentCurricularPlan().getDegree() : null);
    }

    final public DegreeType getDegreeType() {
	return getDegree().getDegreeType();
    }

    final public boolean isBolonha() {
	return getDegreeType().isBolonhaType();
    }

    final public boolean isActiveForOffice(Unit office) {
	return isActive() && isForOffice(office.getAdministrativeOffice());
    }

    final public boolean isForOffice(final AdministrativeOffice administrativeOffice) {
	return getDegreeType().getAdministrativeOfficeType().equals(
		administrativeOffice.getAdministrativeOfficeType());
    }

    final public boolean getIsForOffice() {
	final AdministrativeOffice administrativeOffice = AccessControl.getPerson().getEmployee()
		.getAdministrativeOffice();
	return isForOffice(administrativeOffice);
    }

    final public List<NewTestGroup> getPublishedTestGroups() {
	List<NewTestGroup> testGroups = new ArrayList<NewTestGroup>();

	for (ExecutionCourse executionCourse : this
		.getAttendingExecutionCoursesForCurrentExecutionPeriod()) {
	    testGroups.addAll(executionCourse.getPublishedTestGroups());
	}

	return testGroups;
    }

    final public List<NewTestGroup> getFinishedTestGroups() {
	List<NewTestGroup> testGroups = new ArrayList<NewTestGroup>();

	for (ExecutionCourse executionCourse : this
		.getAttendingExecutionCoursesForCurrentExecutionPeriod()) {
	    testGroups.addAll(executionCourse.getFinishedTestGroups());
	}

	return testGroups;
    }

    final public DegreeCurricularPlan getActiveOrConcludedOrLastDegreeCurricularPlan() {
	final StudentCurricularPlan studentCurricularPlan = getActiveOrConcludedOrLastStudentCurricularPlan();
	return studentCurricularPlan == null ? null : studentCurricularPlan.getDegreeCurricularPlan();

    }

    final public boolean isCurricularCourseApproved(final CurricularCourse curricularCourse) {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    if (studentCurricularPlan.isCurricularCourseApproved(curricularCourse)) {
		return true;
	    }
	}
	return false;
    }

    final public Set<RegistrationStateType> getRegistrationStatesTypes(final ExecutionYear executionYear) {
	final Set<RegistrationStateType> result = new HashSet<RegistrationStateType>();

	for (final RegistrationState registrationState : getRegistrationStates(executionYear)) {
	    result.add(registrationState.getStateType());
	}

	return result;
    }

    final public boolean isInRegisteredState(ExecutionYear executionYear) {
	final Set<RegistrationStateType> registrationStatesTypes = getRegistrationStatesTypes(executionYear);

	return (registrationStatesTypes.contains(RegistrationStateType.REGISTERED)
		|| hasAnyEnrolmentsIn(executionYear) || registrationStatesTypes
		.contains(RegistrationStateType.MOBILITY))
		&& (!registrationStatesTypes.contains(RegistrationStateType.CANCELED)
			&& !registrationStatesTypes.contains(RegistrationStateType.INTERRUPTED)
			&& !registrationStatesTypes.contains(RegistrationStateType.INTERNAL_ABANDON) && !registrationStatesTypes
			.contains(RegistrationStateType.EXTERNAL_ABANDON));
    }

    final public RegistrationState getActiveState() {
	return hasAnyRegistrationStates() ? Collections.max(getRegistrationStates(),
		RegistrationState.DATE_COMPARATOR) : null;
    }

    final public RegistrationStateType getActiveStateType() {
	final RegistrationState activeState = getActiveState();
	return activeState != null ? activeState.getStateType() : RegistrationStateType.REGISTERED;
    }

    final public boolean isActive() {
	return getActiveStateType().isActive();
    }

    final public boolean isInRegisteredState() {
	return getActiveStateType() == RegistrationStateType.REGISTERED;
    }

    final public boolean getInterruptedStudies() {
	return getActiveStateType() == RegistrationStateType.INTERRUPTED;
    }

    final public boolean getFlunked() {
	return getActiveStateType() == RegistrationStateType.FLUNKED;
    }

    final public boolean isInMobilityState() {
	return getActiveStateType() == RegistrationStateType.MOBILITY;
    }

    final public RegistrationState getStateInDate(DateTime dateTime) {

	List<RegistrationState> sortedRegistrationStates = new ArrayList<RegistrationState>(
		getRegistrationStates());
	Collections.sort(sortedRegistrationStates, RegistrationState.DATE_COMPARATOR);

	for (ListIterator<RegistrationState> iterator = sortedRegistrationStates
		.listIterator(sortedRegistrationStates.size()); iterator.hasPrevious();) {

	    RegistrationState registrationState = iterator.previous();
	    if (!dateTime.isBefore(registrationState.getStateDate())) {
		return registrationState;
	    }
	}

	return null;
    }

    final public Set<RegistrationState> getRegistrationStates(final ExecutionYear executionYear) {
	final Set<RegistrationState> result = new HashSet<RegistrationState>();

	List<RegistrationState> sortedRegistrationsStates = new ArrayList<RegistrationState>(
		getRegistrationStates());
	Collections.sort(sortedRegistrationsStates, RegistrationState.DATE_COMPARATOR);

	for (ListIterator<RegistrationState> iter = sortedRegistrationsStates
		.listIterator(sortedRegistrationsStates.size()); iter.hasPrevious();) {
	    RegistrationState state = (RegistrationState) iter.previous();

	    if (state.getStateDate().isAfter(
		    executionYear.getEndDateYearMonthDay().toDateTimeAtMidnight())) {
		continue;
	    }

	    result.add(state);

	    if (state.getStateDate().isBefore(
		    executionYear.getBeginDateYearMonthDay().toDateTimeAtMidnight())) {
		break;
	    }

	}

	return result;
    }

    final public RegistrationState getLastRegistrationState(final ExecutionYear executionYear) {
	List<RegistrationState> sortedRegistrationsStates = new ArrayList<RegistrationState>(
		getRegistrationStates());
	Collections.sort(sortedRegistrationsStates, RegistrationState.DATE_COMPARATOR);

	for (ListIterator<RegistrationState> iter = sortedRegistrationsStates
		.listIterator(sortedRegistrationsStates.size()); iter.hasPrevious();) {
	    RegistrationState state = (RegistrationState) iter.previous();
	    if (state.getStateDate().isAfter(
		    executionYear.getEndDateYearMonthDay().toDateTimeAtMidnight())) {
		continue;
	    }
	    return state;
	}

	return null;
    }

    final public boolean hasState(final RegistrationStateType stateType) {
	for (final RegistrationState registrationState : getRegistrationStates()) {
	    if (registrationState.getStateType() == stateType) {
		return true;
	    }
	}

	return false;
    }

    final public Collection<RegistrationState> getRegistrationStates(
	    final RegistrationStateType registrationStateType) {
	final Collection<RegistrationState> result = new HashSet<RegistrationState>();

	for (final RegistrationState registrationState : getRegistrationStates()) {
	    if (registrationState.getStateType() == registrationStateType) {
		result.add(registrationState);
	    }
	}

	return result;
    }

    final public double getEctsCredits() {
	return getTotalEctsCredits((ExecutionYear) null).doubleValue();
    }

    final public BigDecimal getTotalEctsCredits(final ExecutionYear executionYear) {
	return BigDecimal.valueOf(new StudentCurriculum(this).getTotalEctsCredits(executionYear));
    }

    final public int getCurricularYear() {
	return getCurricularYear((ExecutionYear) null);
    }

    final public int getCurricularYear(ExecutionYear executionYear) {
	return new StudentCurriculum(this).calculateCurricularYear(executionYear);
    }

    final public boolean isConcluded() {
	return getActiveStateType() == RegistrationStateType.CONCLUDED;
    }

    final public boolean isTransition() {
	return getActiveStateType() == RegistrationStateType.TRANSITION;
    }

    final public YearMonthDay getConclusionDate() {
	if (isConcluded()) {
	    return getActiveState().getStateDate().toYearMonthDay();
    	}

	throw new DomainException("Registration.is.not.concluded");
    }

    final public YearMonthDay getConclusionDate(final CycleType cycleType) {
	if (!getDegreeType().hasAnyCycleTypes()) {
	    return getConclusionDate();
	}
	
	if (!hasConcludedCycle(cycleType, (ExecutionYear) null)) {
	    throw new DomainException("Registration.hasnt.finished.given.cycle");
	}

	final StudentCurricularPlan lastStudentCurricularPlan = getLastStudentCurricularPlan();
	if (lastStudentCurricularPlan == null) {
	    throw new DomainException("Registration.has.no.student.curricular.plan");
    	}
    
	return lastStudentCurricularPlan.getConclusionDate(cycleType);
    }

    final public String getGraduateTitle() {
	if (isConcluded()) {
	    return getLastDegreeCurricularPlan().getGraduateTitle();
	}

	throw new DomainException("Registration.is.not.concluded");
    }

    final public String getGraduateTitle(final CycleType cycleType) {
	if (cycleType == null) {
	    return getGraduateTitle();
	}

	if (getDegreeType().getCycleTypes().isEmpty()) {
	    throw new DomainException("Registration.has.no.cycle.type");
	}

	if (!getDegreeType().hasCycleTypes(cycleType)) {
	    throw new DomainException("Registration.doesnt.have.such.cycle.type");
	}

	if (hasConcludedCycle(cycleType, null)) {
	    return getLastDegreeCurricularPlan().getGraduateTitle(cycleType);
	}

	throw new DomainException("Registration.hasnt.concluded.requested.cycle");
    }

    final public boolean hasConcludedFirstCycle() {
	return hasConcludedCycle(CycleType.FIRST_CYCLE, null);
    }

    final public boolean hasConcludedSecondCycle() {
	return hasConcludedCycle(CycleType.SECOND_CYCLE, null);
    }

    final public boolean hasConcludedCycle(final CycleType cycleType, ExecutionYear executionYear) {

	StudentCurricularPlan lastStudentCurricularPlan = getLastStudentCurricularPlan();

	if (lastStudentCurricularPlan == null) {
	    throw new DomainException("error.registration.has.no.student.curricular.plan");
	}

	return lastStudentCurricularPlan.hasConcludedCycle(cycleType, executionYear);
    }

    final public Collection<CycleType> getConcludedCycles() {
	return getConcludedCycles(null);
    }
    
    /**
     * Retrieve concluded cycles before or equal to the given cycle
     */
    final public Collection<CycleType> getConcludedCycles(final CycleType lastCycleTypeToInspect) {
	final Collection<CycleType> result = new TreeSet<CycleType>(CycleType.CYCLE_TYPE_COMPARATOR);

	for (final CycleType cycleType : CycleType.getSortedValues()) {
	    if ((lastCycleTypeToInspect == null || cycleType.isBeforeOrEquals(lastCycleTypeToInspect)) && hasConcludedCycle(cycleType, null)) {
		result.add(cycleType);
	    }
	}

	return result;
    }

    final public CycleType getLastConcludedCycleType() {
	final SortedSet<CycleType> concludedCycles = new TreeSet<CycleType>(getConcludedCycles());
	return concludedCycles.isEmpty() ? null : concludedCycles.last();
    }
    
    final public boolean hasApprovement(ExecutionYear executionYear) {
	int curricularYearInTheBegin = getCurricularYear(executionYear);
	int curricularYearAtTheEnd = getCurricularYear(executionYear.getNextExecutionYear());

	if (curricularYearInTheBegin > curricularYearAtTheEnd) {
	    throw new DomainException("Registration.curricular.year.has.decreased");
	}

	return curricularYearAtTheEnd > curricularYearInTheBegin;
    }

    final public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
	return getDegreeType().isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree();
    }

    final public boolean isMasterDegreeOrBolonhaMasterDegree() {
	final DegreeType degreeType = getDegreeType();
	return (degreeType == DegreeType.MASTER_DEGREE || degreeType == DegreeType.BOLONHA_MASTER_DEGREE);
    }

    final public EnrolmentModel getEnrolmentModelForCurrentExecutionYear() {
	return getEnrolmentModelForExecutionYear(ExecutionYear.readCurrentExecutionYear());
    }

    final public EnrolmentModel getEnrolmentModelForExecutionYear(ExecutionYear year) {
	RegistrationDataByExecutionYear registrationData = getRegistrationDataByExecutionYear(year);
	return registrationData != null ? registrationData.getEnrolmentModel() : null;
    }

    final public void setEnrolmentModelForCurrentExecutionYear(EnrolmentModel model) {
	setEnrolmentModelForExecutionYear(ExecutionYear.readCurrentExecutionYear(), model);
    }

    final public void setEnrolmentModelForExecutionYear(ExecutionYear year, EnrolmentModel model) {
	RegistrationDataByExecutionYear registrationData = getRegistrationDataByExecutionYear(year);
	if (registrationData == null) {
	    registrationData = new RegistrationDataByExecutionYear(this, year);
	}
	registrationData.setEnrolmentModel(model);
    }

    private RegistrationDataByExecutionYear getRegistrationDataByExecutionYear(ExecutionYear year) {
	for (RegistrationDataByExecutionYear registrationData : getRegistrationDataByExecutionYearSet()) {
	    if (registrationData.getExecutionYear().equals(year)) {
		return registrationData;
	    }
	}
	return null;
    }

    @Override
    final public ExecutionYear getRegistrationYear() {
	return super.getRegistrationYear() == null ? getFirstEnrolmentExecutionYear() : super
		.getRegistrationYear();
    }

    final public boolean isFirstTime(ExecutionYear executionYear) {
	return getRegistrationYear() == executionYear;
    }

    final public boolean isFirstTime() {
	return isFirstTime(ExecutionYear.readCurrentExecutionYear());
    }

    final public StudentCurricularPlan getStudentCurricularPlan(final ExecutionYear executionYear) {
	return executionYear == null ? getStudentCurricularPlan(new YearMonthDay())
		: getStudentCurricularPlan(executionYear.getEndDateYearMonthDay());
    }

    final public StudentCurricularPlan getStudentCurricularPlan(final ExecutionPeriod executionPeriod) {
	return executionPeriod == null ? getStudentCurricularPlan(new YearMonthDay())
		: getStudentCurricularPlan(executionPeriod.getEndDateYearMonthDay());
    }

    final public StudentCurricularPlan getStudentCurricularPlan(final YearMonthDay date) {
	StudentCurricularPlan result = null;
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    final YearMonthDay startDate = studentCurricularPlan.getStartDateYearMonthDay();
	    if (!startDate.isAfter(date)
		    && (result == null || startDate.isAfter(result.getStartDateYearMonthDay()))) {
		result = studentCurricularPlan;
	    }
	}
	return result;
    }

    final public StudentCurricularPlan getStudentCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan) {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    if (studentCurricularPlan.getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
		return studentCurricularPlan;
	    }
	}
	return null;
    }

    final public Set<DegreeCurricularPlan> getDegreeCurricularPlans() {
	Set<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    result.add(studentCurricularPlan.getDegreeCurricularPlan());
	}
	return result;
    }

    @Override
    final public YearMonthDay getStartDate() {

	if (super.getStartDate() != null) {
	    return super.getStartDate();
	}

	if (hasStudentCandidacy()) {
	    return getStudentCandidacy().getActiveCandidacySituation().getSituationDate()
		    .toYearMonthDay();
	}

	if (getRegistrationYear() != null) {
	    return getRegistrationYear().getBeginDateYearMonthDay();
	}

	return null;
    }

    final public ExecutionYear getStartExecutionYear() {
	return ExecutionYear.readByDateTime(getStartDate().toDateTimeAtMidnight());
    }

    final public boolean hasStartedBeforeFirstBolonhaExecutionYear() {
	return getStartExecutionYear().isBefore(ExecutionYear.readFirstBolonhaExecutionYear());
    }

    final public boolean hasStudentCurricularPlanInExecutionPeriod(ExecutionPeriod executionPeriod) {
	return getStudentCurricularPlan(executionPeriod) != null;
    }

    final public boolean isCustomEnrolmentModel(final ExecutionYear executionYear) {
	return getEnrolmentModelForExecutionYear(executionYear) == EnrolmentModel.CUSTOM;
    }

    final public boolean isCustomEnrolmentModel() {
	return isCustomEnrolmentModel(ExecutionYear.readCurrentExecutionYear());
    }

    final public boolean isCompleteEnrolmentModel(final ExecutionYear executionYear) {
	return getEnrolmentModelForExecutionYear(executionYear) == EnrolmentModel.COMPLETE;
    }

    final public boolean isCompleteEnrolmentModel() {
	return isCompleteEnrolmentModel(ExecutionYear.readCurrentExecutionYear());
    }

    final public DegreeCurricularPlan getActiveDegreeCurricularPlan() {
	return getActiveStudentCurricularPlan().getDegreeCurricularPlan();
    }

    final public DegreeCurricularPlan getLastDegreeCurricularPlan() {
	return getLastStudentCurricularPlan().getDegreeCurricularPlan();
    }

    private boolean hasAnyNotPayedGratuityEvents() {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    if (studentCurricularPlan.hasAnyNotPayedGratuityEvents()) {
		return true;
	    }
	}

	return false;
    }

    private boolean hasAnyNotPayedGratuityEventsForPreviousYears() {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlansSet()) {
	    if (studentCurricularPlan.hasAnyNotPayedGratuityEventsForPreviousYears()) {
		return true;
	    }
	}

	return false;
    }

    final public boolean hasToPayGratuityOrInsurance() {
	return getInterruptedStudies() ? false
		: getRegistrationAgreement() == RegistrationAgreement.NORMAL;
    }

    final public Set<DocumentRequest> getSucessfullyFinishedDocumentRequestsBy(ExecutionYear executionYear,
	    DocumentRequestType documentRequestType, boolean collectDocumentsMarkedAsFreeProcessed) {

	final Set<DocumentRequest> result = new HashSet<DocumentRequest>();

	for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
	    if (academicServiceRequest instanceof DocumentRequest) {
		final DocumentRequest documentRequest = (DocumentRequest) academicServiceRequest;
		if (documentRequest.getDocumentRequestType() == documentRequestType
			&& documentRequest.finishedSuccessfully()
			&& executionYear.containsDate(documentRequest.getCreationDate())
			&& (!collectDocumentsMarkedAsFreeProcessed || documentRequest.isFreeProcessed())) {

		    result.add((DocumentRequest) academicServiceRequest);
		}
	    }
	}
	return result;
    }

    final public Collection<? extends AcademicServiceRequest> getAcademicServiceRequests(
	    final Class<? extends AcademicServiceRequest> clazz) {
	final Set<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();

	for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
	    if (clazz != null && academicServiceRequest.getClass().equals(clazz)) {
		result.add(academicServiceRequest);
	    }
	}

	return result;
    }

    final public Collection<? extends AcademicServiceRequest> getAcademicServiceRequests(
	    final AcademicServiceRequestSituationType academicServiceRequestSituationType) {

	final Set<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();

	for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
	    if ((academicServiceRequestSituationType == null && academicServiceRequest.isNewRequest())
		    || academicServiceRequest.getAcademicServiceRequestSituationType() == academicServiceRequestSituationType) {

		result.add(academicServiceRequest);
	    }
	}

	return result;
    }

    final public Collection<AcademicServiceRequest> getNewAcademicServiceRequests() {
	return (Collection<AcademicServiceRequest>) getAcademicServiceRequests(AcademicServiceRequestSituationType.NEW);
    }

    final public Collection<AcademicServiceRequest> getProcessingAcademicServiceRequests() {
	return (Collection<AcademicServiceRequest>) getAcademicServiceRequests(AcademicServiceRequestSituationType.PROCESSING);
    }

    final public Collection<AcademicServiceRequest> getConcludedAcademicServiceRequests() {
	return (Collection<AcademicServiceRequest>) getAcademicServiceRequests(AcademicServiceRequestSituationType.CONCLUDED);
    }

    final public Collection<AcademicServiceRequest> getHistoricalAcademicServiceRequests() {
	final Set<AcademicServiceRequest> result = new HashSet<AcademicServiceRequest>();

	for (final AcademicServiceRequest academicServiceRequest : getAcademicServiceRequestsSet()) {
	    if (academicServiceRequest.isHistorical()) {
		result.add(academicServiceRequest);
	    }
	}

	return result;
    }

    final public boolean isInactive() {
	return getActiveStateType().isInactive();
    }

    final public Campus getCampus() {
	return getLastStudentCurricularPlanExceptPast().getLastCampus();
    }

    final public String getIstUniversity() {
	return getCampus().getName();
    }

    @Override
    final public void setStudentCandidacy(StudentCandidacy studentCandidacy) {
	if (hasStudentCandidacy()) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.student.Registration.studentCandidacy.cannot.be.modified");
	}

	super.setStudentCandidacy(studentCandidacy);
    }

    @Override
    final public void removeStudentCandidacy() {
	super.removeStudentCandidacy();
    }

    @Override
    final public Boolean getPayedTuition() {
	return super.getPayedTuition() != null && super.getPayedTuition()
		&& !hasAnyNotPayedGratuityEventsForPreviousYears();
    }

    final public Boolean getPayedOldTuition() {
	return super.getPayedTuition() != null && super.getPayedTuition();
    }

    final public boolean getHasGratuityDebtsCurrently() {
	return hasGratuityDebtsCurrently();
    }

    final public boolean hasGratuityDebtsCurrently() {
	return !super.getPayedTuition() || hasAnyNotPayedGratuityEvents();
    }

    final public Attends readAttendByExecutionCourse(ExecutionCourse executionCourse) {
	return getStudent().readAttendByExecutionCourse(executionCourse);
    }

    final public Attends readRegistrationAttendByExecutionCourse(ExecutionCourse executionCourse) {
	for (Attends attend : this.getAssociatedAttends()) {
	    if (attend.getExecutionCourse().equals(executionCourse)) {
		return attend;
	    }
	}
	return null;
    }

    @Override
    final public void setRegistrationAgreement(RegistrationAgreement registrationAgreement) {
	super.setRegistrationAgreement(registrationAgreement == null ? RegistrationAgreement.NORMAL
		: registrationAgreement);
	if (registrationAgreement != null && !registrationAgreement.isNormal()
		&& !hasExternalRegistrationData()) {
	    new ExternalRegistrationData(this);
	}
    }

    final public boolean hasGratuityEvent(final ExecutionYear executionYear) {
	for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlans()) {
	    if (studentCurricularPlan.hasGratuityEvent(executionYear)) {
		return true;
	    }
	}
	return false;
    }

    final public boolean hasDissertationThesis() {
	return getDissertationEnrolment() != null && getDissertationEnrolment().getThesis() != null;
    }

    final public Enrolment getDissertationEnrolment() {
	return getDissertationEnrolment(null);
    }

    final public Enrolment getDissertationEnrolment(final DegreeCurricularPlan degreeCurricularPlan) {
	final StudentCurricularPlan lastStudentCurricularPlan = getLastStudentCurricularPlan();
	if (lastStudentCurricularPlan == null) {
	    return null;
	}

	if (degreeCurricularPlan != null
		&& lastStudentCurricularPlan.getDegreeCurricularPlan() != degreeCurricularPlan) {
	    return null;
	}

	return lastStudentCurricularPlan.getLatestDissertationEnrolment();
    }

    final public Proposal getDissertationProposal(final ExecutionYear executionYear) {
	for (final GroupStudent groupStudent : getAssociatedGroupStudents()) {
	    final FinalDegreeWorkGroup group = groupStudent.getFinalDegreeDegreeWorkGroup();
	    final Proposal proposalAttributedByCoordinator = group.getProposalAttributed();
	    if (proposalAttributedByCoordinator != null
		    && isProposalForExecutionYear(proposalAttributedByCoordinator, executionYear)) {
		return proposalAttributedByCoordinator;
	    }
	    final Proposal proposalAttributedByTeacher = group.getProposalAttributedByTeacher();
	    if (proposalAttributedByTeacher != null
		    && isProposalForExecutionYear(proposalAttributedByTeacher, executionYear)) {
		if (proposalAttributedByTeacher.isProposalConfirmedByTeacherAndStudents(group)) {
		    return proposalAttributedByTeacher;
		}
	    }
	}
	return null;
    }

    private boolean isProposalForExecutionYear(final Proposal proposal, final ExecutionYear executionYear) {
	final Scheduleing scheduleing = proposal.getScheduleing();
	for (final ExecutionDegree executionDegree : scheduleing.getExecutionDegreesSet()) {
	    if (executionDegree.getExecutionYear() == executionYear) {
		return true;
	    }
	}
	return false;
    }

    final public boolean isAvailableDegreeTypeForInquiries() {
	final DegreeType degreeType = getDegreeType();
	return degreeType == DegreeType.DEGREE || degreeType == DegreeType.BOLONHA_DEGREE
		|| degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE;
    }

    final public boolean hasInquiriesToRespond() {
	for (final Attends attends : getAssociatedAttendsSet()) {
	    final ExecutionCourse executionCourse = attends.getExecutionCourse();
	    final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
	    if (executionCourse.getAvailableForInquiries().booleanValue()
		    && executionPeriod.getState().equals(PeriodState.CURRENT)
		    && !hasInquiryResponseFor(executionCourse)) {
		return true;
	    }
	}
	return false;
    }

    final public boolean hasInquiryResponseFor(final ExecutionCourse executionCourse) {
	for (final InquiriesRegistry inquiriesRegistry : getAssociatedInquiriesRegistries()) {
	    if (inquiriesRegistry.getExecutionCourse() == executionCourse) {
		return true;
	    }
	}
	return false;
    }

    final public StudentCurricularPlan getLastStudentDegreeCurricularPlansByDegree(Degree degree) {
	final SortedSet<StudentCurricularPlan> result = new TreeSet<StudentCurricularPlan>(
		StudentCurricularPlan.DATE_COMPARATOR);
	for (DegreeCurricularPlan degreeCurricularPlan : this.getDegreeCurricularPlans()) {
	    if (degreeCurricularPlan.getDegree() == degree) {
		result.add(this.getStudentCurricularPlan(degreeCurricularPlan));
	    }
	}
	return result.last();

    }

    final public ExternalEnrolment findExternalEnrolment(Unit university, ExecutionPeriod period, String code) {
	for (final ExternalEnrolment externalEnrolment : this.getExternalEnrolments()) {
	    if (externalEnrolment.getExecutionPeriod() == period
		    && externalEnrolment.getExternalCurricularCourse().getCode().equals(code)
		    && externalEnrolment.getExternalCurricularCourse().getUnit() == university) {
		return externalEnrolment;
	    }
	}
	return null;
    }

    final public SortedSet<ExternalEnrolment> getSortedExternalEnrolments() {
	final SortedSet<ExternalEnrolment> result = new TreeSet<ExternalEnrolment>(
		ExternalEnrolment.COMPARATOR_BY_NAME);
	result.addAll(getExternalEnrolmentsSet());
	return result;
    }
    
    @Override
    public RegistrationAgreement getRegistrationAgreement() {
        return super.getRegistrationAgreement() == null ? RegistrationAgreement.NORMAL : super
		.getRegistrationAgreement();
}
}
