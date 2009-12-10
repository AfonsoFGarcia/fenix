package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.curriculum.CurriculumValidationEvaluationPhase;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationContext;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.curriculum.GradeFactory;
import net.sourceforge.fenixedu.domain.curriculum.IGrade;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentNotPayedException;
import net.sourceforge.fenixedu.domain.log.EnrolmentEvaluationLog;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.FenixDigestUtils;
import net.sourceforge.fenixedu.util.MarkType;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.services.Service;

public class EnrolmentEvaluation extends EnrolmentEvaluation_Base implements Comparable {

    public static final Comparator<EnrolmentEvaluation> COMPARATORY_BY_WHEN = new Comparator<EnrolmentEvaluation>() {

	@Override
	public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {
	    return o1.getWhenDateTime().compareTo(o2.getWhenDateTime());
	}

    };

    public static final Comparator<EnrolmentEvaluation> SORT_BY_STUDENT_NUMBER = new Comparator<EnrolmentEvaluation>() {

	@Override
	public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {
	    final Student s1 = o1.getRegistration().getStudent();
	    final Student s2 = o2.getRegistration().getStudent();
	    return s1.getNumber().compareTo(s2.getNumber());
	}

    };

    public static final Comparator<EnrolmentEvaluation> SORT_SAME_TYPE_GRADE = new Comparator<EnrolmentEvaluation>() {
	public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {
	    if (o1.getEnrolmentEvaluationType() != o2.getEnrolmentEvaluationType()) {
		throw new RuntimeException("error.enrolmentEvaluation.different.types");
	    }
	    if (o1.getEnrolmentEvaluationState().getWeight() == o2.getEnrolmentEvaluationState().getWeight()) {
		return o1.compareMyWhenAlteredDateToAnotherWhenAlteredDate(o2.getWhen());
	    }
	    return o1.getEnrolmentEvaluationState().getWeight() - o2.getEnrolmentEvaluationState().getWeight();
	}
    };

    public static final Comparator<EnrolmentEvaluation> SORT_BY_GRADE = new Comparator<EnrolmentEvaluation>() {
	public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {
	    return o1.getGradeWrapper().compareTo(o2.getGradeWrapper());
	}
    };

    static final public Comparator<EnrolmentEvaluation> COMPARATOR_BY_EXAM_DATE = new Comparator<EnrolmentEvaluation>() {
	public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {
	    if (o1.getExamDateYearMonthDay() == null && o2.getExamDateYearMonthDay() == null) {
		return 0;
	    }
	    if (o1.getExamDateYearMonthDay() == null) {
		return -1;
	    }
	    if (o2.getExamDateYearMonthDay() == null) {
		return 1;
	    }
	    return o1.getExamDateYearMonthDay().compareTo(o2.getExamDateYearMonthDay());
	}
    };

    private static final String RECTIFICATION = "RECTIFICA��O";

    private static final String RECTIFIED = "RECTIFICADO";

    public EnrolmentEvaluation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
	setGrade(Grade.createEmptyGrade());
	setContext(EnrolmentEvaluationContext.MARK_SHEET_EVALUATION);
    }

    public EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationType type) {
	this();
	if (enrolment == null || type == null) {
	    throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
	}
	setEnrolment(enrolment);
	setEnrolmentEvaluationType(type);
    }

    private EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationState enrolmentEvaluationState,
	    EnrolmentEvaluationType type, Person responsibleFor, Grade grade, Date availableDate, Date examDate) {

	this(enrolment, type);

	if (enrolmentEvaluationState == null || responsibleFor == null) {
	    throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
	}
	setEnrolmentEvaluationState(enrolmentEvaluationState);
	setPersonResponsibleForGrade(responsibleFor);
	setGrade(grade);
	setGradeAvailableDate(availableDate);
	setExamDate(examDate);

	generateCheckSum();
    }

    @Override
    public void setExamDateYearMonthDay(YearMonthDay evaluationDateYearMonthDay) {
	if (evaluationDateYearMonthDay != null) {
	    final Enrolment enrolment = getEnrolment();
	    final Thesis thesis = enrolment.getThesis();
	    if (thesis != null) {
		DateTime newDateTime = evaluationDateYearMonthDay.toDateTimeAtMidnight();
		final DateTime dateTime = thesis.getDiscussed();
		if (dateTime != null) {
		    newDateTime = newDateTime.withHourOfDay(dateTime.getHourOfDay());
		    newDateTime = newDateTime.withMinuteOfHour(dateTime.getMinuteOfHour());
		}
		thesis.setDiscussed(newDateTime);
	    }
	}
	super.setExamDateYearMonthDay(evaluationDateYearMonthDay);
    }

    protected EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationState enrolmentEvaluationState,
	    EnrolmentEvaluationType type, Person responsibleFor, Grade grade, Date availableDate, Date examDate, DateTime when) {
	this(enrolment, enrolmentEvaluationState, type, responsibleFor, grade, availableDate, examDate);
	setWhenDateTime(when);
    }

    protected EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationType enrolmentEvaluationType,
	    EnrolmentEvaluationState evaluationState) {
	this(enrolment, enrolmentEvaluationType);
	if (evaluationState == null) {
	    throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
	}
	setEnrolmentEvaluationState(evaluationState);
	setWhenDateTime(new DateTime());
    }

    protected EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationType enrolmentEvaluationType,
	    EnrolmentEvaluationState evaluationState, Employee employee) {
	this(enrolment, enrolmentEvaluationType, evaluationState);
	if (employee == null) {
	    throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
	}
	setEmployee(employee);
    }

    protected EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationType enrolmentEvaluationType,
	    EnrolmentEvaluationState evaluationState, Employee employee, ExecutionSemester executionSemester) {
	this(enrolment, enrolmentEvaluationType, evaluationState, employee);
	if (executionSemester == null) {
	    throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
	}
	setExecutionPeriod(executionSemester);
    }

    public int compareTo(Object o) {
	EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) o;
	if (this.getEnrolment().getStudentCurricularPlan().getDegreeType().equals(DegreeType.MASTER_DEGREE)) {
	    return compareMyWhenAlteredDateToAnotherWhenAlteredDate(enrolmentEvaluation.getWhen());
	}

	if (this.isInCurriculumValidationContextAndIsFinal() && !enrolmentEvaluation.isInCurriculumValidationContextAndIsFinal()) {
	    return 1;
	} else if (!this.isInCurriculumValidationContextAndIsFinal()
		&& enrolmentEvaluation.isInCurriculumValidationContextAndIsFinal()) {
	    return -1;
	} else if (this.isInCurriculumValidationContextAndIsFinal()
		&& enrolmentEvaluation.isInCurriculumValidationContextAndIsFinal()) {
	    return compareMyWhenAlteredDateToAnotherWhenAlteredDate(enrolmentEvaluation.getWhen());
	} else if (this.getEnrolmentEvaluationType() == enrolmentEvaluation.getEnrolmentEvaluationType()) {
	    if ((this.isRectification() && enrolmentEvaluation.isRectification())
		    || (this.isRectified() && enrolmentEvaluation.isRectified())) {
		return compareMyWhenAlteredDateToAnotherWhenAlteredDate(enrolmentEvaluation.getWhen());
	    }
	    if (this.isRectification()) {
		return 1;
	    }
	    if (enrolmentEvaluation.isRectification()) {
		return -1;
	    }
	    return compareByGrade(this.getGrade(), enrolmentEvaluation.getGrade());

	} else {
	    return compareByGrade(this.getGrade(), enrolmentEvaluation.getGrade());
	}
    }

    private int compareByGrade(final Grade grade, final Grade otherGrade) {
	EnrollmentState gradeEnrolmentState = getEnrollmentStateByGrade();
	EnrollmentState otherGradeEnrolmentState = getEnrollmentStateByGrade();
	if (gradeEnrolmentState == EnrollmentState.APROVED && otherGradeEnrolmentState == EnrollmentState.APROVED) {
	    return grade.compareTo(otherGrade);
	}

	return compareByGradeState(gradeEnrolmentState, otherGradeEnrolmentState);
    }

    private int compareByGradeState(EnrollmentState gradeEnrolmentState, EnrollmentState otherGradeEnrolmentState) {
	if (gradeEnrolmentState == EnrollmentState.APROVED) {
	    return 1;
	}
	if (otherGradeEnrolmentState == EnrollmentState.APROVED) {
	    return -1;
	}
	if (gradeEnrolmentState == EnrollmentState.NOT_APROVED && otherGradeEnrolmentState == EnrollmentState.NOT_EVALUATED) {
	    return 1;
	}
	if (gradeEnrolmentState == EnrollmentState.NOT_EVALUATED && otherGradeEnrolmentState == EnrollmentState.NOT_APROVED) {
	    return -1;
	}

	return 0;
    }

    private int compareMyExamDateToAnotherExamDate(Date examDate) {
	if (this.getExamDate() == null) {
	    return -1;
	}
	if (examDate == null) {
	    return 1;
	}

	return this.getExamDate().compareTo(examDate);

    }

    private int compareMyWhenAlteredDateToAnotherWhenAlteredDate(Date whenAltered) {
	if (this.getWhen() == null) {
	    return -1;
	}
	if (whenAltered == null) {
	    return 1;
	}

	return this.getWhen().compareTo(whenAltered);

    }

    private int compareMyGradeToAnotherGrade(final Grade otherGrade) {
	return this.getGrade().compareTo(otherGrade);
    }

    private int compareForEqualStates(EnrollmentState myEnrolmentState, EnrollmentState otherEnrolmentState, Grade otherGrade,
	    Date otherExamDate) {
	if (myEnrolmentState.equals(EnrollmentState.APROVED)) {
	    return compareMyGradeToAnotherGrade(otherGrade);
	}
	return compareMyExamDateToAnotherExamDate(otherExamDate);
    }

    private int compareForNotEqualStates(EnrollmentState myEnrolmentState, EnrollmentState otherEnrolmentState) {
	if (myEnrolmentState.equals(EnrollmentState.APROVED)) {
	    return 1;
	} else if (myEnrolmentState.equals(EnrollmentState.NOT_APROVED) && otherEnrolmentState.equals(EnrollmentState.APROVED)) {
	    return -1;
	} else if (myEnrolmentState.equals(EnrollmentState.NOT_APROVED)) {
	    return 1;
	} else if (myEnrolmentState.equals(EnrollmentState.NOT_EVALUATED)) {
	    return -1;
	} else {
	    return 0;
	}
    }

    public EnrollmentState getEnrollmentStateByGrade() {
	return getGrade().getEnrolmentState();
    }

    public GradeScale getGradeScale() {
	return getGradeScaleChain();
    }

    public GradeScale getGradeScaleChain() {
	return super.getGradeScale() != null ? super.getGradeScale() : getEnrolment().getGradeScaleChain();
    }

    public GradeScale getAssociatedGradeScale() {
	return super.getGradeScale();
    }

    public boolean isNormal() {
	return getEnrolmentEvaluationType() == EnrolmentEvaluationType.NORMAL;
    }

    public boolean isImprovment() {
	return getEnrolmentEvaluationType() == EnrolmentEvaluationType.IMPROVEMENT;
    }

    public boolean isSpecialSeason() {
	return getEnrolmentEvaluationType() == EnrolmentEvaluationType.SPECIAL_SEASON;
    }

    public boolean isNotEvaluated() {
	return getEnrollmentStateByGrade() == EnrollmentState.NOT_EVALUATED;
    }

    public boolean isFlunked() {
	return getEnrollmentStateByGrade() == EnrollmentState.NOT_APROVED;
    }

    public boolean isApproved() {
	return getEnrollmentStateByGrade() == EnrollmentState.APROVED;
    }

    public void edit(Person responsibleFor, Date evaluationDate) {
	if (responsibleFor == null) {
	    throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
	}
	setPersonResponsibleForGrade(responsibleFor);
	setExamDate(evaluationDate);
	generateCheckSum();
    }

    public void edit(Person responsibleFor, String gradeValue, Date availableDate, Date examDate, String bookReference,
	    String page, String examReference) {
	edit(responsibleFor, gradeValue, availableDate, examDate);
	setBookReference(bookReference);
	setPage(page);
    }

    public void edit(Person responsibleFor, String gradeValue, Date availableDate, Date examDate) {
	edit(responsibleFor, Grade.createGrade(gradeValue, getGradeScale()), availableDate, examDate);
    }

    public void edit(Person responsibleFor, Grade grade, Date availableDate, Date examDate) {
	if (responsibleFor == null) {
	    throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
	}

	if (examDate != null) {
	    if (!grade.isNotEvaluated()) {
		final RegistrationState state = getRegistration().getLastRegistrationState(
			getExecutionPeriod().getExecutionYear());
		if (state == null
			|| !(getRegistration().hasAnyActiveState(getExecutionPeriod().getExecutionYear()) || state.getStateType() == RegistrationStateType.TRANSITED)) {
		    final Enrolment enrolment = getEnrolment();
		    final StudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();
		    final Registration registration = studentCurricularPlan.getRegistration();
		    throw new DomainException("error.enrolmentEvaluation.examDateNotInRegistrationActiveState", registration
			    .getNumber().toString());
		}
	    }
	    setExamDateYearMonthDay(YearMonthDay.fromDateFields(examDate));
	} else if (grade.isEmpty()) {
	    setExamDateYearMonthDay(null);
	} else {
	    setExamDateYearMonthDay(YearMonthDay.fromDateFields(availableDate));
	}

	setGrade(grade);
	setGradeAvailableDateYearMonthDay(YearMonthDay.fromDateFields(availableDate));
	setPersonResponsibleForGrade(responsibleFor);

	generateCheckSum();
    }

    public void confirmSubmission(Employee employee, String observation) {
	confirmSubmission(EnrolmentEvaluationState.FINAL_OBJ, employee, observation);
    }

    public void confirmSubmission(EnrolmentEvaluationState enrolmentEvaluationState, Employee employee, String observation) {

	if (!isTemporary()) {
	    throw new DomainException("EnrolmentEvaluation.cannot.submit.not.temporary");
	}

	if (!hasGrade()) {
	    throw new DomainException("EnrolmentEvaluation.cannot.submit.with.empty.grade");
	}

	/*
	 * 
	 * Due to curriculum validation the exam date is not required
	 * 
	 * if (!hasExamDateYearMonthDay()) { throw new
	 * DomainException("EnrolmentEvaluation.cannot.submit.without.exam.date"
	 * ); }
	 */

	if (isPayable() && !isPayed()) {
	    throw new EnrolmentNotPayedException("EnrolmentEvaluation.cannot.set.grade.on.not.payed.enrolment.evaluation",
		    getEnrolment());
	}

	if (enrolmentEvaluationState == EnrolmentEvaluationState.RECTIFICATION_OBJ && hasRectified()) {
	    getRectified().setEnrolmentEvaluationState(EnrolmentEvaluationState.RECTIFIED_OBJ);
	}

	setEnrolmentEvaluationState(enrolmentEvaluationState);
	setEmployee(employee);
	setObservation(observation);
	setWhenDateTime(new DateTime());

	EnrollmentState newEnrolmentState = EnrollmentState.APROVED;
	if (!this.isImprovment()) {
	    if (MarkType.getRepMarks().contains(getGradeValue())) {
		newEnrolmentState = EnrollmentState.NOT_APROVED;
	    } else if (MarkType.getNaMarks().contains(getGradeValue())) {
		newEnrolmentState = EnrollmentState.NOT_EVALUATED;
	    }
	}

	this.getEnrolment().setEnrollmentState(newEnrolmentState);
    }

    public boolean getCanBeDeleted() {
	return isTemporary() && !hasConfirmedMarkSheet()
		&& (!hasImprovementOfApprovedEnrolmentEvent() || !getImprovementOfApprovedEnrolmentEvent().isPayed());
    }

    public boolean hasConfirmedMarkSheet() {
	return hasMarkSheet() && getMarkSheet().isConfirmed();
    }

    public boolean isTemporary() {
	return getEnrolmentEvaluationState() != null
		&& getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ);
    }

    public boolean isFinal() {
	return getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.FINAL_OBJ)
		|| getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFICATION_OBJ)
		|| getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFIED_OBJ);
    }

    public boolean isRectification() {
	return (this.getObservation() != null && this.getObservation().equals(RECTIFICATION))
		|| (this.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFICATION_OBJ));
    }

    public boolean isRectified() {
	return (this.getObservation() != null && this.getObservation().equals(RECTIFIED))
		|| (this.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFIED_OBJ));
    }

    public void delete() {
	if (!getCanBeDeleted()) {
	    throw new DomainException("error.enrolmentEvaluation.cannot.be.deleted");
	}

	deleteObject();

    }

    public void deleteObject() {

	if (hasImprovementOfApprovedEnrolmentEvent() && getImprovementOfApprovedEnrolmentEvent().isPayed()) {
	    throw new DomainException("error.enrolmentEvaluation.has.been.payed");
	}

	removePersonResponsibleForGrade();
	removeEmployee();
	removeEnrolment();
	removeMarkSheet();
	removeRectification();
	removeRectified();
	if (hasImprovementOfApprovedEnrolmentEvent()) {
	    getImprovementOfApprovedEnrolmentEvent().removeImprovementEnrolmentEvaluations(this);
	}
	removeExecutionPeriod();

	removeRootDomainObject();

	super.deleteDomainObject();
    }

    public void removeFromMarkSheet() {
	if (hasConfirmedMarkSheet()) {
	    throw new DomainException("error.enrolmentEvaluation.cannot.be.removed.from.markSheet");
	}

	setCheckSum(null);
	setExamDateYearMonthDay(null);
	setGradeAvailableDateYearMonthDay(null);

	removeMarkSheet();
    }

    public void insertStudentFinalEvaluationForMasterDegree(String gradeValue, Person responsibleFor, Date examDate)
	    throws DomainException {

	DegreeCurricularPlan degreeCurricularPlan = getEnrolment().getStudentCurricularPlan().getDegreeCurricularPlan();

	final Grade grade = Grade.createGrade(gradeValue, getGradeScale());
	if (!grade.isEmpty() && degreeCurricularPlan.isGradeValid(grade)) {
	    edit(responsibleFor, gradeValue, Calendar.getInstance().getTime(), examDate);
	} else {
	    throw new DomainException("error.invalid.grade");
	}
    }

    public void alterStudentEnrolmentEvaluationForMasterDegree(String gradeValue, Employee employee, Person responsibleFor,
	    EnrolmentEvaluationType evaluationType, Date evaluationAvailableDate, Date examDate, String observation)
	    throws DomainException {

	Enrolment enrolment = getEnrolment();
	DegreeCurricularPlan degreeCurricularPlan = getEnrolment().getStudentCurricularPlan().getDegreeCurricularPlan();

	final Grade grade = Grade.createGrade(gradeValue, getGradeScale());
	if (grade.isEmpty()) {
	    EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(enrolment, getEnrolmentEvaluationType());
	    enrolmentEvaluation.confirmSubmission(EnrolmentEvaluationState.FINAL_OBJ, employee, observation);
	    enrolment.setEnrollmentState(EnrollmentState.ENROLLED);
	} else {
	    if (degreeCurricularPlan.isGradeValid(grade)) {
		EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(enrolment, evaluationType);
		enrolmentEvaluation.edit(responsibleFor, grade, evaluationAvailableDate, examDate);
		enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ); // temporary
		// hack
		enrolmentEvaluation.confirmSubmission(EnrolmentEvaluationState.FINAL_OBJ, employee, observation);
	    } else {
		throw new DomainException("error.invalid.grade");
	    }
	}
    }

    protected void generateCheckSum() {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(getExamDateYearMonthDay() != null ? getExamDateYearMonthDay().toString() : "").append(
		getGradeValue());
	stringBuilder.append(getEnrolmentEvaluationType());
	stringBuilder.append(getEnrolment().getStudentCurricularPlan().getRegistration().getNumber());
	setCheckSum(FenixDigestUtils.createDigest(stringBuilder.toString()));
    }

    public IGrade getGradeWrapper() {
	return GradeFactory.getInstance().getGrade(getGradeValue());
    }

    @Override
    public String getGradeValue() {
	return getGrade().getValue();
    }

    @Override
    @Deprecated
    public void setGradeValue(final String grade) {
	setGrade(grade);
    }

    public void setGrade(final String grade) {
	setGrade(Grade.createGrade(grade, getGradeScale()));
    }

    @Override
    public void setGrade(final Grade grade) {

	if (isFinal()) {
	    throw new DomainException("EnrolmentEvaluation.cannot.set.grade.final");
	}

	super.setGrade(grade);

	final Enrolment enrolment = getEnrolment();
	if (enrolment != null && enrolment.getCurricularCourse().isDissertation()) {
	    final Thesis thesis = enrolment.getThesis();
	    if (thesis != null) {
		thesis.setMark(Integer.valueOf(grade.getValue()));
	    }
	}

	// TODO remove this once we're sure migration to Grade went OK
	super.setGradeValue(grade.getValue());
    }

    @Deprecated
    public Registration getStudent() {
	return this.getRegistration();
    }

    public Registration getRegistration() {
	return getStudentCurricularPlan().getRegistration();
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
	return getStudentCurricularPlan().getDegreeCurricularPlan();
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
	return getEnrolment().getStudentCurricularPlan();
    }

    public MarkSheet getRectificationMarkSheet() {
	if (this.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFIED_OBJ) && hasRectification()) {
	    return getRectification().getMarkSheet();
	} else {
	    return null;
	}
    }

    public boolean hasGrade() {
	return !getGrade().isEmpty();
    }

    final public boolean hasExamDateYearMonthDay() {
	return getExamDateYearMonthDay() != null;
    }

    public boolean isPayable() {
	return hasImprovementOfApprovedEnrolmentEvent();
    }

    public boolean isPayed() {
	return getImprovementOfApprovedEnrolmentEvent().isPayed();
    }

    @Override
    public ExecutionSemester getExecutionPeriod() {
	if (getEnrolmentEvaluationType() == EnrolmentEvaluationType.IMPROVEMENT) {
	    return super.getExecutionPeriod();
	}

	if (getEnrolment() != null) {
	    return getEnrolment().getExecutionPeriod();
	}

	return null;
    }

    public boolean isInCurriculumValidationContext() {
	return this.getContext() != null && this.getContext().equals(EnrolmentEvaluationContext.CURRICULUM_VALIDATION_EVALUATION);
    }

    public boolean isInCurriculumValidationContextAndIsFinal() {
	return this.isInCurriculumValidationContext() && this.isFinal();
    }

    private static final String NORMAL_TYPE_FIRST_SEASON_DESCRIPTION = "label.curriculum.validation.normal.type.first.season.description";
    private static final String NORMAL_TYPE_SECOND_SEASON_DESCRIPTION = "label.curriculum.validation.normal.type.second.season.description";

    private static final ResourceBundle academicResources = ResourceBundle.getBundle("resources.AcademicAdminOffice");

    public String getEnrolmentEvaluationTypeDescription() {
	if (EnrolmentEvaluationType.NORMAL.equals(this.getEnrolmentEvaluationType())
		&& CurriculumValidationEvaluationPhase.FIRST_SEASON.equals(this.getCurriculumValidationEvaluationPhase())) {
	    return academicResources.getString(NORMAL_TYPE_FIRST_SEASON_DESCRIPTION);
	} else if (EnrolmentEvaluationType.NORMAL.equals(this.getEnrolmentEvaluationType())
		&& CurriculumValidationEvaluationPhase.SECOND_SEASON.equals(this.getCurriculumValidationEvaluationPhase())) {
	    return academicResources.getString(NORMAL_TYPE_SECOND_SEASON_DESCRIPTION);
	}

	return this.getEnrolmentEvaluationType().getDescription();
    }

    @Service
    public void deleteEnrolmentEvaluationCurriculumValidationContext() {
	if (!getEnrolment().getStudentCurricularPlan().getEvaluationForCurriculumValidationAllowed()) {
	    throw new DomainException("error.curriculum.validation.enrolment.evaluatiom.removal.not.allowed");
	}

	Enrolment enrolment = getEnrolment();

	EnrolmentEvaluationLog.logEnrolmentEvaluationDeletion(this);
	deleteObject();

	enrolment.changeStateIfAprovedAndEvaluationsIsEmpty();
    }

}
