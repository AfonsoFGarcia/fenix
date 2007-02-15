package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleEnroledWrapper;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.log.EnrolmentLog;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.EnrolmentAction;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class Enrolment extends Enrolment_Base implements IEnrolment {

    public static final Comparator<Enrolment> COMPARATOR_BY_EXECUTION_PERIOD = new BeanComparator(
	    "executionPeriod");

    public static final Comparator<Enrolment> REVERSE_COMPARATOR_BY_EXECUTION_PERIOD = new ComparatorChain();
    static {
	((ComparatorChain) REVERSE_COMPARATOR_BY_EXECUTION_PERIOD).addComparator(new BeanComparator(
		"executionPeriod"), true);
	((ComparatorChain) REVERSE_COMPARATOR_BY_EXECUTION_PERIOD).addComparator(new BeanComparator(
		"idInternal"));
    }

    private Integer accumulatedWeight;

    private Double accumulatedEctsCredits;

    /*
         * static {
         * EnrolmentEvaluation.EnrolmentEnrolmentEvaluation.addListener(new
         * EnrolmentEnrolmentEvaluationListener()); }
         */

    public Enrolment() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Enrolment(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod, EnrollmentCondition enrolmentCondition, String createdBy) {
	this();
	initializeAsNew(studentCurricularPlan, curricularCourse, executionPeriod, enrolmentCondition,
		createdBy);
	createEnrolmentLog(studentCurricularPlan.getRegistration(), EnrolmentAction.ENROL);
    }

    public Enrolment(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod, EnrollmentCondition enrolmentCondition, String createdBy,
	    Boolean isExtraCurricular) {
	this(studentCurricularPlan, curricularCourse, executionPeriod, enrolmentCondition, createdBy);
	this.setIsExtraCurricular(isExtraCurricular);
    }

    @Override
    public boolean isEnrolment() {
	return true;
    }

    public boolean isBolonha() {
	return !(getDegreeModule() instanceof CurricularCourse) || getCurricularCourse().isBolonha();
    }
    
    @Deprecated
    public EnrollmentCondition getCondition() {
	return getEnrolmentCondition();
    }

    @Deprecated
    public void setCondition(EnrollmentCondition enrollmentCondition) {
	setEnrolmentCondition(enrollmentCondition);
    }

    public boolean isFinal() {
	return getEnrolmentCondition() == EnrollmentCondition.FINAL;
    }

    public boolean isInvisible() {
	return getEnrolmentCondition() == EnrollmentCondition.INVISIBLE;
    }

    public boolean isTemporary() {
	return getEnrolmentCondition() == EnrollmentCondition.TEMPORARY;
    }

    public boolean isSpecialSeason() {
	boolean result = false;
	for (EnrolmentEvaluation enrolmentEvaluation : this.getEvaluations()) {
	    result |= enrolmentEvaluation.getEnrolmentEvaluationType().equals(
		    EnrolmentEvaluationType.SPECIAL_SEASON);
	}

	return result;
    }

    // new student structure methods
    public Enrolment(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
	    EnrollmentCondition enrolmentCondition, String createdBy) {
	this();
	if (studentCurricularPlan == null || curriculumGroup == null || curricularCourse == null
		|| executionPeriod == null || enrolmentCondition == null || createdBy == null) {
	    throw new DomainException("invalid arguments");
	}
	checkInitConstraints(studentCurricularPlan, curricularCourse, executionPeriod);
	// TODO: check this
	// validateDegreeModuleLink(curriculumGroup, curricularCourse);
	initializeAsNew(studentCurricularPlan, curriculumGroup, curricularCourse, executionPeriod,
		enrolmentCondition, createdBy);
    }

    public Enrolment(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
	    EnrollmentCondition enrolmentCondition, String createdBy, Boolean isExtraCurricular) {

	this(studentCurricularPlan, curriculumGroup, curricularCourse, executionPeriod,
		enrolmentCondition, createdBy);
	setIsExtraCurricular(isExtraCurricular);
    }

    protected void checkInitConstraints(StudentCurricularPlan studentCurricularPlan,
	    CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
	if (studentCurricularPlan.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse,
		executionPeriod) != null) {
	    throw new DomainException("error.Enrolment.duplicate.enrolment", curricularCourse.getName());
	}
    }

    protected void initializeAsNew(StudentCurricularPlan studentCurricularPlan,
	    CurriculumGroup curriculumGroup, CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod, EnrollmentCondition enrolmentCondition, String createdBy) {
	initializeAsNewWithoutEnrolmentEvaluation(studentCurricularPlan, curriculumGroup,
		curricularCourse, executionPeriod, enrolmentCondition, createdBy);
	createEnrolmentEvaluationWithoutGrade();
    }

    protected void initializeAsNewWithoutEnrolmentEvaluation(
	    StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
	    EnrollmentCondition enrolmentCondition, String createdBy) {
	initializeCommon(studentCurricularPlan, curricularCourse, executionPeriod, enrolmentCondition,
		createdBy);
	setCurriculumGroup(curriculumGroup);
    }

    // end

    public Integer getAccumulatedWeight() {
	return accumulatedWeight;
    }

    public void setAccumulatedWeight(Integer accumulatedWeight) {
	this.accumulatedWeight = accumulatedWeight;
    }

    protected void initializeAsNew(StudentCurricularPlan studentCurricularPlan,
	    CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
	    EnrollmentCondition enrolmentCondition, String createdBy) {
	initializeAsNewWithoutEnrolmentEvaluation(studentCurricularPlan, curricularCourse,
		executionPeriod, enrolmentCondition, createdBy);
	createEnrolmentEvaluationWithoutGrade();
    }

    private void initializeCommon(StudentCurricularPlan studentCurricularPlan,
	    CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
	    EnrollmentCondition enrolmentCondition, String createdBy) {
	setCurricularCourse(curricularCourse);
	setWeigth(curricularCourse.getWeigth());
	setEnrollmentState(EnrollmentState.ENROLLED);
	setExecutionPeriod(executionPeriod);
	setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
	setCreatedBy(createdBy);
	setCreationDateDateTime(new DateTime());
	setEnrolmentCondition(enrolmentCondition);
	createAttend(studentCurricularPlan.getRegistration(), curricularCourse, executionPeriod);
	setIsExtraCurricular(Boolean.FALSE);
    }

    protected void initializeAsNewWithoutEnrolmentEvaluation(
	    StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod, EnrollmentCondition enrolmentCondition, String createdBy) {
	initializeCommon(studentCurricularPlan, curricularCourse, executionPeriod, enrolmentCondition,
		createdBy);
	setStudentCurricularPlan(studentCurricularPlan);
    }

    public void unEnroll() throws DomainException {

	for (EnrolmentEvaluation eval : getEvaluations()) {

	    if (eval.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.NORMAL)
		    && eval.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ)
		    && (eval.getGrade() == null || eval.getGrade().equals("")))
		;
	    else
		throw new DomainException("error.enrolment.cant.unenroll");
	}

	delete();
    }

    public void delete() {
	createEnrolmentLog(EnrolmentAction.UNENROL);
	// TODO: falta ver se � dos antigos enrolments ou dos novos
	final Registration registration = getStudentCurricularPlan().getRegistration();

	removeExecutionPeriod();
	removeStudentCurricularPlan();
	removeDegreeModule();
	removeCurriculumGroup();

	Iterator<Attends> attendsIter = getAttendsIterator();
	while (attendsIter.hasNext()) {
	    Attends attends = attendsIter.next();

	    attendsIter.remove();
	    attends.removeEnrolment();

	    if (!attends.hasAnyAssociatedMarks() && !attends.hasAnyStudentGroups()) {
		boolean hasShiftEnrolment = false;
		for (Shift shift : attends.getDisciplinaExecucao().getAssociatedShifts()) {
		    if (shift.hasStudents(registration)) {
			hasShiftEnrolment = true;
			break;
		    }
		}

		if (!hasShiftEnrolment) {
		    attends.delete();
		}
	    }
	}

	Iterator<EnrolmentEvaluation> evalsIter = getEvaluationsIterator();
	while (evalsIter.hasNext()) {
	    EnrolmentEvaluation eval = evalsIter.next();
	    evalsIter.remove();
	    eval.delete();
	}

	Iterator<CreditsInAnySecundaryArea> creditsInAnysecundaryAreaIterator = getCreditsInAnySecundaryAreasIterator();

	while (creditsInAnysecundaryAreaIterator.hasNext()) {
	    CreditsInAnySecundaryArea credits = creditsInAnysecundaryAreaIterator.next();
	    creditsInAnysecundaryAreaIterator.remove();
	    credits.delete();
	}

	Iterator<CreditsInScientificArea> creditsInScientificAreaIterator = getCreditsInScientificAreasIterator();

	while (creditsInScientificAreaIterator.hasNext()) {
	    CreditsInScientificArea credits = creditsInScientificAreaIterator.next();
	    creditsInScientificAreaIterator.remove();
	    credits.delete();
	}

	Iterator<EquivalentEnrolmentForEnrolmentEquivalence> equivalentEnrolmentIterator = getEquivalentEnrolmentForEnrolmentEquivalencesIterator();

	while (equivalentEnrolmentIterator.hasNext()) {
	    EquivalentEnrolmentForEnrolmentEquivalence equivalentEnrolment = equivalentEnrolmentIterator
		    .next();
	    equivalentEnrolmentIterator.remove();
	    equivalentEnrolment.removeEquivalentEnrolment();

	    EnrolmentEquivalence equivalence = equivalentEnrolment.getEnrolmentEquivalence();
	    Enrolment enrolment = equivalence.getEnrolment();

	    equivalence.removeEnrolment();
	    enrolment.delete();
	    equivalentEnrolment.removeEnrolmentEquivalence();

	    equivalentEnrolment.delete();
	    equivalence.delete();
	}

	Iterator<EnrolmentEquivalence> equivalenceIterator = getEnrolmentEquivalencesIterator();

	while (equivalenceIterator.hasNext()) {
	    EnrolmentEquivalence equivalence = equivalenceIterator.next();
	    equivalenceIterator.remove();
	    equivalence.removeEnrolment();

	    Iterator<EquivalentEnrolmentForEnrolmentEquivalence> equivalentRestrictionIterator = equivalence
		    .getEquivalenceRestrictionsIterator();

	    while (equivalentRestrictionIterator.hasNext()) {
		EquivalentEnrolmentForEnrolmentEquivalence equivalentRestriction = equivalentRestrictionIterator
			.next();
		equivalentRestriction.removeEquivalentEnrolment();
		equivalentRestrictionIterator.remove();
		equivalentRestriction.removeEnrolmentEquivalence();

		equivalentRestriction.delete();
	    }
	    equivalence.delete();
	}

	super.delete();

    }

    public EnrolmentEvaluation getImprovementEvaluation() {

	for (EnrolmentEvaluation evaluation : getEvaluations()) {
	    if (evaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT)
		    && evaluation.getEnrolmentEvaluationState().equals(
			    EnrolmentEvaluationState.TEMPORARY_OBJ))

		return evaluation;
	}

	return null;
    }

    public EnrolmentEvaluation getEnrolmentEvaluationByEnrolmentEvaluationTypeAndGrade(
	    final EnrolmentEvaluationType evaluationType, final String grade) {

	return (EnrolmentEvaluation) CollectionUtils.find(getEvaluations(), new Predicate() {

	    public boolean evaluate(Object o) {
		EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) o;
		String evaluationGrade = enrolmentEvaluation.getGrade();

		return enrolmentEvaluation.getEnrolmentEvaluationType().equals(evaluationType)
			&& ((grade == null && evaluationGrade == null) || (evaluationGrade != null && evaluationGrade
				.equals(grade)));
	    }

	});
    }

    public EnrolmentEvaluation getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(
	    final EnrolmentEvaluationState state, final EnrolmentEvaluationType type) {
	return (EnrolmentEvaluation) CollectionUtils.find(getEvaluations(), new Predicate() {

	    public boolean evaluate(Object o) {
		EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) o;
		return (enrolmentEvaluation.getEnrolmentEvaluationState().equals(state) && enrolmentEvaluation
			.getEnrolmentEvaluationType().equals(type));
	    }

	});
    }

    public List<EnrolmentEvaluation> getEnrolmentEvaluationsByEnrolmentEvaluationState(
	    final EnrolmentEvaluationState evaluationState) {
	List<EnrolmentEvaluation> result = new ArrayList<EnrolmentEvaluation>();
	for (EnrolmentEvaluation evaluation : getEvaluationsSet()) {
	    if (evaluation.getEnrolmentEvaluationState().equals(evaluationState)) {
		result.add(evaluation);
	    }
	}
	return result;
    }

    public List<EnrolmentEvaluation> getEnrolmentEvaluationsByEnrolmentEvaluationType(
	    final EnrolmentEvaluationType evaluationType) {
	List<EnrolmentEvaluation> result = new ArrayList<EnrolmentEvaluation>();
	for (EnrolmentEvaluation evaluation : getEvaluationsSet()) {
	    if (evaluation.getEnrolmentEvaluationType().equals(evaluationType)) {
		result.add(evaluation);
	    }
	}
	return result;
    }

    public EnrolmentEvaluation submitEnrolmentEvaluation(
	    EnrolmentEvaluationType enrolmentEvaluationType, Mark publishedMark, Employee employee,
	    Person personResponsibleForGrade, Date evaluationDate, String observation) {

	EnrolmentEvaluation enrolmentEvaluation = getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(
		EnrolmentEvaluationState.TEMPORARY_OBJ, enrolmentEvaluationType);

	// There can be only one enrolmentEvaluation with Temporary State
	if (enrolmentEvaluation == null) {
	    enrolmentEvaluation = new EnrolmentEvaluation();
	    enrolmentEvaluation.setEnrolment(this);
	}

	// teacher responsible for execution course
	String grade = null;
	if ((publishedMark == null) || (publishedMark.getMark().length() == 0))
	    grade = "NA";
	else
	    grade = publishedMark.getMark().toUpperCase();

	enrolmentEvaluation.setGrade(grade);

	enrolmentEvaluation.setEnrolmentEvaluationType(enrolmentEvaluationType);
	enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
	enrolmentEvaluation.setObservation(observation);
	enrolmentEvaluation.setPersonResponsibleForGrade(personResponsibleForGrade);

	enrolmentEvaluation.setEmployee(employee);

	final YearMonthDay yearMonthDay = new YearMonthDay();
	enrolmentEvaluation.setGradeAvailableDateYearMonthDay(yearMonthDay);
	if (evaluationDate != null) {
	    enrolmentEvaluation.setExamDate(evaluationDate);
	} else {
	    enrolmentEvaluation.setExamDateYearMonthDay(yearMonthDay);
	}

	enrolmentEvaluation.setCheckSum("");

	return enrolmentEvaluation;
    }

    protected void createEnrolmentEvaluationWithoutGrade() {

	EnrolmentEvaluation enrolmentEvaluation = getEnrolmentEvaluationByEnrolmentEvaluationTypeAndGrade(
		EnrolmentEvaluationType.NORMAL, null);

	if (enrolmentEvaluation == null) {
	    enrolmentEvaluation = new EnrolmentEvaluation();
	    enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
	    enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
	    enrolmentEvaluation.setWhenDateTime(new DateTime());

	    addEvaluations(enrolmentEvaluation);
	}
    }

    private void createAttend(Registration registration, CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod) {

	List executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(executionPeriod);

	ExecutionCourse executionCourse = null;
	if (executionCourses.size() > 1) {
	    Iterator iterator = executionCourses.iterator();
	    while (iterator.hasNext()) {
		ExecutionCourse executionCourse2 = (ExecutionCourse) iterator.next();
		if (executionCourse2.getExecutionCourseProperties() == null
			|| executionCourse2.getExecutionCourseProperties().isEmpty()) {
		    executionCourse = executionCourse2;
		}
	    }
	} else if (executionCourses.size() == 1) {
	    executionCourse = (ExecutionCourse) executionCourses.get(0);
	}

	if (executionCourse != null) {
	    Attends attend = executionCourse.getAttendsByStudent(registration);

	    if (attend != null) {
		addAttends(attend);
	    } else {
		Attends attendToWrite = new Attends(registration, executionCourse);
		addAttends(attendToWrite);
	    }
	}
    }

    public void createAttends(final Registration registration, final ExecutionCourse executionCourse) {
	final Attends attendsFor = this.getAttendsFor(executionCourse.getExecutionPeriod());
	if (attendsFor != null) {
	    try {
		attendsFor.delete();
	    } catch (DomainException e) {
		throw new DomainException("error.attends.cant.change.attends");
	    }
	}

	Attends attends = new Attends(registration, executionCourse);
	this.addAttends(attends);
    }

    public void createEnrolmentEvaluationForImprovement(Employee employee,
	    ExecutionPeriod currentExecutionPeriod, Registration registration) {

	EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();

	enrolmentEvaluation.setEmployee(employee);
	enrolmentEvaluation.setWhenDateTime(new DateTime());
	enrolmentEvaluation.setEnrolment(this);
	enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
	enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.IMPROVEMENT);

	createAttendForImprovment(currentExecutionPeriod, registration);
    }

    private void createAttendForImprovment(final ExecutionPeriod currentExecutionPeriod,
	    final Registration registration) {

	List executionCourses = getCurricularCourse().getAssociatedExecutionCourses();
	ExecutionCourse currentExecutionCourse = (ExecutionCourse) CollectionUtils.find(
		executionCourses, new Predicate() {

		    public boolean evaluate(Object arg0) {
			ExecutionCourse executionCourse = (ExecutionCourse) arg0;
			if (executionCourse.getExecutionPeriod().equals(currentExecutionPeriod))
			    return true;
			return false;
		    }

		});

	if (currentExecutionCourse != null) {
	    List attends = currentExecutionCourse.getAttends();
	    Attends attend = (Attends) CollectionUtils.find(attends, new Predicate() {

		public boolean evaluate(Object arg0) {
		    Attends frequenta = (Attends) arg0;
		    if (frequenta.getAluno().equals(registration))
			return true;
		    return false;
		}

	    });

	    if (attend != null) {
		attend.setEnrolment(this);
	    } else {
		attend = new Attends(registration, currentExecutionCourse);
		attend.setEnrolment(this);
	    }
	}
    }

    public boolean isImprovementForExecutionCourse(ExecutionCourse executionCourse) {
	return !getExecutionPeriod().equals(executionCourse.getExecutionPeriod());
    }

    public void unEnrollImprovement(final ExecutionPeriod executionPeriod) throws DomainException {
	EnrolmentEvaluation improvmentEnrolmentEvaluation = getImprovementEvaluation();
	if (improvmentEnrolmentEvaluation != null) {

	    improvmentEnrolmentEvaluation.delete();

	    final Registration registration = getStudentCurricularPlan().getRegistration();
	    List<ExecutionCourse> executionCourses = getCurricularCourse()
		    .getAssociatedExecutionCourses();

	    ExecutionCourse currentExecutionCourse = (ExecutionCourse) CollectionUtils.find(
		    executionCourses, new Predicate() {

			public boolean evaluate(Object arg0) {
			    ExecutionCourse executionCourse = (ExecutionCourse) arg0;
			    if (executionCourse.getExecutionPeriod().equals(executionPeriod))
				return true;
			    return false;
			}
		    });

	    if (currentExecutionCourse != null) {
		List attends = currentExecutionCourse.getAttends();
		Attends attend = (Attends) CollectionUtils.find(attends, new Predicate() {

		    public boolean evaluate(Object arg0) {
			Attends frequenta = (Attends) arg0;
			if (frequenta.getAluno().equals(registration))
			    return true;
			return false;
		    }
		});

		if (attend != null) {
		    attend.delete();
		}
	    }
	} else {
	    throw new DomainException("error.enrolment.cant.unenroll.improvement");
	}
    }

    public List<EnrolmentEvaluation> getAllFinalEnrolmentEvaluations() {
	final List<EnrolmentEvaluation> result = new ArrayList<EnrolmentEvaluation>();

	for (final EnrolmentEvaluation enrolmentEvaluation : getEvaluationsSet()) {
	    if (enrolmentEvaluation.isFinal()) {
		result.add(enrolmentEvaluation);
	    }
	}

	return result;
    }

    private boolean hasEnrolmentEvaluationByType(EnrolmentEvaluationType enrolmentEvaluationType) {
	for (EnrolmentEvaluation enrolmentEvaluation : getEvaluations()) {
	    if (enrolmentEvaluation.getEnrolmentEvaluationType().equals(enrolmentEvaluationType)) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasImprovement() {
	return hasEnrolmentEvaluationByType(EnrolmentEvaluationType.IMPROVEMENT);
    }

    public boolean hasSpecialSeason() {
	return hasEnrolmentEvaluationByType(EnrolmentEvaluationType.SPECIAL_SEASON);
    }

    public boolean isNotEvaluated() {
	final EnrolmentEvaluation finalEnrolmentEvaluation = getLatestEnrolmentEvaluation();
	return finalEnrolmentEvaluation == null || finalEnrolmentEvaluation.isNotEvaluated();
    }

    public boolean isFlunked() {
	final EnrolmentEvaluation finalEnrolmentEvaluation = getLatestEnrolmentEvaluation();
	return finalEnrolmentEvaluation != null && finalEnrolmentEvaluation.isFlunked();
    }

    public boolean isApproved() {
	final EnrolmentEvaluation finalEnrolmentEvaluation = getLatestEnrolmentEvaluation();
	return finalEnrolmentEvaluation != null && finalEnrolmentEvaluation.isApproved();
    }

    public boolean isEnroled() {
	return this.getEnrollmentState() == EnrollmentState.ENROLLED;
    }

    public boolean isEnrolmentStateApproved() {
	return this.getEnrollmentState() == EnrollmentState.APROVED;
    }

    public boolean isAnnulled() {
	return this.getEnrollmentState() == EnrollmentState.ANNULED;
    }

    public boolean isTemporarilyEnroled() {
	return this.getEnrollmentState() == EnrollmentState.TEMPORARILY_ENROLLED;
    }

    public Boolean isFirstTime() {
	List<Enrolment> enrollments = getStudentCurricularPlan().getActiveEnrolments(
		getCurricularCourse());
	for (Enrolment enrollment : enrollments) {
	    if (enrollment.getExecutionPeriod().isBefore(this.getExecutionPeriod())) {
		return Boolean.FALSE;
	    }
	}
	return Boolean.TRUE;
    }

    public int getNumberOfTotalEnrolmentsInThisCourse() {
	return this.getStudentCurricularPlan().countEnrolmentsByCurricularCourse(
		this.getCurricularCourse());
    }

    protected void createEnrolmentLog(Registration registration, EnrolmentAction action) {
	new EnrolmentLog(action, registration, this.getCurricularCourse(), this.getExecutionPeriod(),
		getCurrentUser());
    }

    protected void createEnrolmentLog(EnrolmentAction action) {
	new EnrolmentLog(action, this.getStudentCurricularPlan().getRegistration(), this
		.getCurricularCourse(), this.getExecutionPeriod(), getCurrentUser());
    }

    @Override
    public StringBuilder print(String tabs) {
	final StringBuilder builder = new StringBuilder();
	builder.append(tabs);
	builder.append("[E ").append(getDegreeModule().getName()).append(" ]\n");

	return builder;
    }

    public EnrolmentEvaluation addNewEnrolmentEvaluation(
	    EnrolmentEvaluationState enrolmentEvaluationState,
	    EnrolmentEvaluationType enrolmentEvaluationType, Person responsibleFor, String grade,
	    Date availableDate, Date examDate) {
	return new EnrolmentEvaluation(this, enrolmentEvaluationState, enrolmentEvaluationType,
		responsibleFor, grade, availableDate, examDate, new DateTime());
    }

    public boolean hasAssociatedMarkSheet(MarkSheetType markSheetType) {
	for (final EnrolmentEvaluation enrolmentEvaluation : this.getEvaluationsSet()) {
	    if (enrolmentEvaluation.hasMarkSheet()
		    && enrolmentEvaluation.getEnrolmentEvaluationType() == markSheetType
			    .getEnrolmentEvaluationType()) {
		return true;
	    }
	}
	return false;
    }

    public List<EnrolmentEvaluation> getConfirmedEvaluations(MarkSheetType markSheetType) {
	List<EnrolmentEvaluation> evaluations = new ArrayList<EnrolmentEvaluation>();
	for (EnrolmentEvaluation evaluation : this.getEvaluationsSet()) {
	    if (evaluation.hasMarkSheet()
		    && evaluation.getMarkSheet().getMarkSheetType() == markSheetType
		    && evaluation.getMarkSheet().isConfirmed()) {

		evaluations.add(evaluation);
	    }
	}
	Collections.sort(evaluations, new BeanComparator("when"));
	return evaluations;
    }

    public Attends getAttendsByExecutionCourse(ExecutionCourse executionCourse) {
	for (Attends attends : this.getAttendsSet()) {
	    if (attends.getDisciplinaExecucao() == executionCourse) {
		return attends;
	    }
	}
	return null;
    }

    public boolean hasAttendsFor(ExecutionPeriod executionPeriod) {
	for (final Attends attends : this.getAttendsSet()) {
	    if (attends.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
		return true;
	    }
	}
	return false;
    }

    public void createSpecialSeasonEvaluation() {
	if (getEnrolmentEvaluationType() != EnrolmentEvaluationType.SPECIAL_SEASON && !isApproved()) {
	    setEnrolmentEvaluationType(EnrolmentEvaluationType.SPECIAL_SEASON);
	    if (getEnrollmentState() == EnrollmentState.ENROLLED) {
		setEnrolmentCondition(EnrollmentCondition.TEMPORARY);
	    } else {
		setEnrollmentState(EnrollmentState.ENROLLED);
		setEnrolmentCondition(EnrollmentCondition.FINAL);
	    }
	    EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(this,
		    EnrolmentEvaluationType.SPECIAL_SEASON, EnrolmentEvaluationState.TEMPORARY_OBJ);
	    enrolmentEvaluation.setWhenDateTime(new DateTime());
	} else {
	    throw new DomainException("error.invalid.enrolment.state");
	}
    }

    public void deleteSpecialSeasonEvaluation() {
	if (getEnrolmentEvaluationType() == EnrolmentEvaluationType.SPECIAL_SEASON && hasSpecialSeason()) {
	    setEnrolmentCondition(EnrollmentCondition.FINAL);
	    setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
	    EnrolmentEvaluation enrolmentEvaluation = getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(
		    EnrolmentEvaluationState.TEMPORARY_OBJ, EnrolmentEvaluationType.SPECIAL_SEASON);
	    if (enrolmentEvaluation != null) {
		enrolmentEvaluation.delete();
	    }

	    EnrolmentEvaluation normalEnrolmentEvaluation = getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(
		    EnrolmentEvaluationState.FINAL_OBJ, EnrolmentEvaluationType.NORMAL);
	    if (normalEnrolmentEvaluation != null) {
		setEnrollmentState(normalEnrolmentEvaluation.getEnrollmentStateByGrade());
	    }
	} else {
	    throw new DomainException("error.invalid.enrolment.state");
	}
    }

    @Deprecated
    public EnrolmentEvaluation getFinalEnrolmentEvaluation() {
	return getLatestEnrolmentEvaluation();
    }

    public EnrolmentEvaluation getLatestEnrolmentEvaluation() {
	return (getStudentCurricularPlan().getDegreeType().getAdministrativeOfficeType() == AdministrativeOfficeType.DEGREE) ? getLatestEnrolmentEvalution(this
		.getAllFinalEnrolmentEvaluations())
		: getLatestEnrolmentEvalution(this.getEvaluations());
    }

    private EnrolmentEvaluation getLatestEnrolmentEvalution(
	    List<EnrolmentEvaluation> enrolmentEvaluations) {
	return (enrolmentEvaluations == null || enrolmentEvaluations.isEmpty()) ? null : Collections
		.max(enrolmentEvaluations);
    }

    public EnrolmentEvaluation getLatestEnrolmentEvaluationBy(EnrolmentEvaluationType evaluationType) {
	return getLatestEnrolmentEvalution(getEnrolmentEvaluationsByEnrolmentEvaluationType(evaluationType));
    }

    public String getGrade() {
	final EnrolmentEvaluation enrolmentEvaluation = getLatestEnrolmentEvaluation();
	return (enrolmentEvaluation == null) ? null : enrolmentEvaluation.getGrade();
    }

    public Integer getFinalGrade() {
	final String grade = getGrade();
	return (grade == null || StringUtils.isEmpty(grade) || !StringUtils.isNumeric(grade)) ? null
		: Integer.valueOf(grade);
    }

    public Double getAccumulatedEctsCredits() {
	return accumulatedEctsCredits;
    }

    public void setAccumulatedEctsCredits(Double ectsCredits) {
	this.accumulatedEctsCredits = ectsCredits;
    }

    public ExecutionCourse getExecutionCourseFor(final ExecutionPeriod executionPeriod) {
	for (final Attends attend : getAttends()) {
	    if (attend.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
		return attend.getDisciplinaExecucao();
	    }
	}

	return null;
    }

    @Override
    public List<Enrolment> getEnrolments() {
	return Collections.singletonList(this);
    }

    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
	return hasCurriculumGroup() ? getCurriculumGroup().getStudentCurricularPlan() : super
		.getStudentCurricularPlan();
    }

    @Override
    public boolean isAproved(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
	if (executionPeriod == null || this.getExecutionPeriod().isBeforeOrEquals(executionPeriod)) {
	    return this.isApproved() && this.getCurricularCourse().isEquivalent(curricularCourse);
	} else {
	    return false;
	}
    }

    @Override
    public boolean isEnroledInExecutionPeriod(CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod) {
	return isValid(executionPeriod)
		&& this.getCurricularCourse().equals(curricularCourse);
    }

    public boolean isValid(final ExecutionPeriod executionPeriod) {
	return getExecutionPeriod() == executionPeriod
		|| (getCurricularCourse().isAnual() && getExecutionPeriod().getExecutionYear() == executionPeriod
			.getExecutionYear());
    }
    
    @Override
    public boolean hasEnrolmentWithEnroledState(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
        return isEnroledInExecutionPeriod(curricularCourse, executionPeriod) && isEnroled();
    }
    
    public List<ExecutionCourse> getExecutionCourses() {
	return this.getCurricularCourse().getAssociatedExecutionCourses();
    }

    public boolean isEnrolmentTypeNormal() {
	return getCurricularCourse().getType() == CurricularCourseType.NORMAL_COURSE
		&& !isExtraCurricular() && !isOptional();
    }

    public String getEnrolmentTypeName() {
	if (isExtraCurricular()) {
	    return "EXTRA_CURRICULAR_ENROLMENT";
	} else if (isOptional()) {
	    return "ENROLMENT_IN_OPTIONAL_DEGREE_MODULE";
	} else {
	    return getCurricularCourse().getType().name();
	}
    }

    public Attends getAttendsFor(final ExecutionPeriod executionPeriod) {
	for (final Attends attends : getAttendsSet()) {
	    if (attends.isFor(executionPeriod)) {
		return attends;
	    }
	}
	return null;
    }

    public boolean isOptional() {
	return false;
    }

    public static class DeleteEnrolmentExecutor implements FactoryExecutor {

	private DomainReference<Enrolment> enrolment;

	public DeleteEnrolmentExecutor(Enrolment enrolment) {
	    super();
	    this.enrolment = new DomainReference<Enrolment>(enrolment);
	}

	public Object execute() {
	    enrolment.getObject().delete();
	    return null;
	}

    }

    @Override
    public Double getWeigth() {
	if (isExtraCurricular()) {
	    return Double.valueOf(0);
	}

	final Double weigth = super.getWeigth();
	return (weigth == null || weigth == 0) ? getCurricularCourse().getWeigth() : weigth;
    }

    @Override
    public Double getEctsCredits() {
	if (isExtraCurricular()) {
	    return Double.valueOf(0);
	}

	return getCurricularCourse().getEctsCredits(getExecutionPeriod());
    }

    @Override
    public Double getAprovedEctsCredits() {
	if (isApproved()) {
	    return getEctsCredits();
	}
	return Double.valueOf(0);
    }

    public boolean isExtraCurricular() {
	return getIsExtraCurricular() != null && getIsExtraCurricular();
    }

    @Override
    public Double getEnroledEctsCredits(final ExecutionPeriod executionPeriod) {
	return isValid(executionPeriod) && isEnroled() ? getEctsCredits() : Double.valueOf(0d);
    }
    
    @Override
    public CurriculumLine findCurriculumLineFor(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
        return isEnroledInExecutionPeriod(curricularCourse, executionPeriod) ? this : null;
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(ExecutionPeriod executionPeriod) {
	if (!isValid(executionPeriod)) {
	    return Collections.EMPTY_SET;
	}
	final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>(1);
	result.add(new CurriculumModuleEnroledWrapper(this, executionPeriod));
	return result;
    }
}