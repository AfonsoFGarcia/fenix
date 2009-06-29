package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnroledEnrolmentWrapper;
import net.sourceforge.fenixedu.domain.enrolment.ExternalDegreeEnrolmentWrapper;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.log.EnrolmentLog;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.InternalEnrolmentWrapper;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.EnrolmentAction;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.EntryPhase;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author dcs-rjao
 * 
 *         24/Mar/2003
 */

public class Enrolment extends Enrolment_Base implements IEnrolment {

    static final public Comparator<Enrolment> REVERSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_ID = new Comparator<Enrolment>() {
	public int compare(Enrolment o1, Enrolment o2) {
	    return -COMPARATOR_BY_EXECUTION_PERIOD_AND_ID.compare(o1, o2);
	}
    };

    static final private Comparator<Enrolment> COMPARATOR_BY_LATEST_ENROLMENT_EVALUATION = new Comparator<Enrolment>() {
	final public int compare(Enrolment o1, Enrolment o2) {
	    return EnrolmentEvaluation.COMPARATOR_BY_EXAM_DATE.compare(o1.getLatestEnrolmentEvaluation(), o2
		    .getLatestEnrolmentEvaluation());
	}
    };

    static final public Comparator<Enrolment> COMPARATOR_BY_LATEST_ENROLMENT_EVALUATION_AND_ID = new Comparator<Enrolment>() {
	final public int compare(Enrolment o1, Enrolment o2) {
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(Enrolment.COMPARATOR_BY_LATEST_ENROLMENT_EVALUATION);
	    comparatorChain.addComparator(CurriculumLine.COMPARATOR_BY_ID);

	    return comparatorChain.compare(o1, o2);
	}
    };

    private Integer accumulatedWeight;

    private Double accumulatedEctsCredits;

    public Enrolment() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	super.setIsExtraCurricular(Boolean.FALSE);
    }

    public Enrolment(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
	    ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition, String createdBy) {
	this();
	initializeAsNew(studentCurricularPlan, curricularCourse, executionSemester, enrolmentCondition, createdBy);
	createCurriculumLineLog(EnrolmentAction.ENROL);
    }

    @Override
    final public boolean isEnrolment() {
	return true;
    }

    public boolean isOptional() {
	return false;
    }

    final public boolean isExternalEnrolment() {
	return false;
    }

    @Override
    final public boolean isPropaedeutic() {
	return (isBoxStructure() && super.isPropaedeutic()) || (!isBolonhaDegree() && getCurricularCourse().isPropaedeutic());
    }

    @Override
    public boolean isExtraCurricular() {
	if (!isBoxStructure()) {
	    return super.getIsExtraCurricular() != null && super.getIsExtraCurricular();
	}

	return super.isExtraCurricular();
    }

    @Override
    @Deprecated
    public Boolean getIsExtraCurricular() {
	return isExtraCurricular();
    }

    @Override
    @Deprecated
    public void setIsExtraCurricular(Boolean isExtraCurricular) {
	if (isBoxStructure()) {
	    throw new DomainException("error.net.sourceforge.fenixedu.domain.Enrolment.use.markAsExtraCurricular.method.instead");
	}

	super.setIsExtraCurricular(isExtraCurricular);
    }

    public void markAsExtraCurricular() {
	if (isBoxStructure()) {
	    setCurriculumGroup(getStudentCurricularPlan().getExtraCurriculumGroup());
	    super.setIsExtraCurricular(null);
	} else {
	    super.setIsExtraCurricular(true);
	}
    }

    final public boolean isFinal() {
	return getEnrolmentCondition() == EnrollmentCondition.FINAL;
    }

    final public boolean isInvisible() {
	return getEnrolmentCondition() == EnrollmentCondition.INVISIBLE;
    }

    final public boolean isTemporary() {
	return getEnrolmentCondition() == EnrollmentCondition.TEMPORARY;
    }

    final public boolean isImpossible() {
	return getEnrolmentCondition() == EnrollmentCondition.IMPOSSIBLE;
    }

    final public boolean isSpecialSeason() {
	return hasSpecialSeason();
    }

    // new student structure methods
    public Enrolment(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    CurricularCourse curricularCourse, ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition,
	    String createdBy) {
	this();
	if (studentCurricularPlan == null || curriculumGroup == null || curricularCourse == null || executionSemester == null
		|| enrolmentCondition == null || createdBy == null) {
	    throw new DomainException("invalid arguments");
	}
	checkInitConstraints(studentCurricularPlan, curricularCourse, executionSemester);
	// TODO: check this
	// validateDegreeModuleLink(curriculumGroup, curricularCourse);
	initializeAsNew(studentCurricularPlan, curriculumGroup, curricularCourse, executionSemester, enrolmentCondition,
		createdBy);
	createCurriculumLineLog(EnrolmentAction.ENROL);
    }

    protected void checkInitConstraints(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
	    ExecutionSemester executionSemester) {
	if (studentCurricularPlan.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse, executionSemester) != null) {
	    throw new DomainException("error.Enrolment.duplicate.enrolment", curricularCourse.getName());
	}
    }

    protected void initializeAsNew(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    CurricularCourse curricularCourse, ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition,
	    String createdBy) {
	initializeAsNewWithoutEnrolmentEvaluation(studentCurricularPlan, curriculumGroup, curricularCourse, executionSemester,
		enrolmentCondition, createdBy);
	createEnrolmentEvaluationWithoutGrade();
    }

    protected void initializeAsNewWithoutEnrolmentEvaluation(StudentCurricularPlan studentCurricularPlan,
	    CurriculumGroup curriculumGroup, CurricularCourse curricularCourse, ExecutionSemester executionSemester,
	    EnrollmentCondition enrolmentCondition, String createdBy) {
	initializeCommon(studentCurricularPlan, curricularCourse, executionSemester, enrolmentCondition, createdBy);
	setCurriculumGroup(curriculumGroup);
    }

    // end

    final public Integer getAccumulatedWeight() {
	return accumulatedWeight;
    }

    final public void setAccumulatedWeight(Integer accumulatedWeight) {
	this.accumulatedWeight = accumulatedWeight;
    }

    protected void initializeAsNew(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
	    ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition, String createdBy) {
	initializeAsNewWithoutEnrolmentEvaluation(studentCurricularPlan, curricularCourse, executionSemester, enrolmentCondition,
		createdBy);
	createEnrolmentEvaluationWithoutGrade();
    }

    private void initializeCommon(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
	    ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition, String createdBy) {
	setCurricularCourse(curricularCourse);
	setWeigth(studentCurricularPlan.isBolonhaDegree() ? curricularCourse.getEctsCredits(executionSemester) : curricularCourse
		.getWeigth());
	setEnrollmentState(EnrollmentState.ENROLLED);
	setExecutionPeriod(executionSemester);
	setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
	setCreatedBy(createdBy);
	setCreationDateDateTime(new DateTime());
	setEnrolmentCondition(enrolmentCondition);
	createAttend(studentCurricularPlan.getRegistration(), curricularCourse, executionSemester);
	super.setIsExtraCurricular(Boolean.FALSE);
    }

    protected void initializeAsNewWithoutEnrolmentEvaluation(StudentCurricularPlan studentCurricularPlan,
	    CurricularCourse curricularCourse, ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition,
	    String createdBy) {
	initializeCommon(studentCurricularPlan, curricularCourse, executionSemester, enrolmentCondition, createdBy);
	setStudentCurricularPlan(studentCurricularPlan);
    }

    final public void unEnroll() throws DomainException {

	for (EnrolmentEvaluation eval : getEvaluationsSet()) {

	    if (eval.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.NORMAL)
		    && eval.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ)
		    && eval.getGrade().isEmpty()) {
		continue;
	    } else {
		throw new DomainException("error.enrolment.cant.unenroll");
	    }
	}

	delete();
    }

    @Override
    public void delete() {
	checkRulesToDelete();
	createCurriculumLineLog(EnrolmentAction.UNENROL);
	deleteInformation();
	super.delete();
    }

    protected void deleteInformation() {

	final Iterator<Thesis> theses = getThesesIterator();
	while (theses.hasNext()) {
	    theses.next().delete();
	}

	final Registration registration = getRegistration();

	getStudentCurricularPlan().setIsFirstTimeToNull();
	removeExecutionPeriod();
	removeStudentCurricularPlan();
	removeDegreeModule();
	removeCurriculumGroup();
	getNotNeedToEnrollCurricularCourses().clear();

	Iterator<Attends> attendsIter = getAttendsIterator();
	while (attendsIter.hasNext()) {
	    Attends attends = attendsIter.next();

	    attendsIter.remove();
	    attends.removeEnrolment();

	    if (!attends.hasAnyAssociatedMarks() && !attends.hasAnyStudentGroups()) {
		boolean hasShiftEnrolment = false;
		for (Shift shift : attends.getExecutionCourse().getAssociatedShifts()) {
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
    }

    protected void checkRulesToDelete() {
	if (hasAnyExtraExamRequests()) {
	    throw new DomainException("error.Enrolment.has.ExtraExamRequests");
	}
	if (hasAnyEnrolmentWrappers()) {
	    throw new DomainException("error.Enrolment.is.origin.in.some.Equivalence");
	}
	if (hasAnyCourseLoadRequests()) {
	    throw new DomainException("error.Enrolment.has.CourseLoadRequests");
	}
	if (hasAnyProgramCertificateRequests()) {
	    throw new DomainException("error.Enrolment.has.ProgramCertificateRequests");
	}
    }

    final public Collection<Enrolment> getBrothers() {
	final Collection<Enrolment> result = new HashSet<Enrolment>();

	result.addAll(getStudentCurricularPlan().getEnrolments(getCurricularCourse()));
	result.remove(this);

	return result;
    }

    final public EnrolmentEvaluation getEnrolmentEvaluationByEnrolmentEvaluationTypeAndGrade(
	    final EnrolmentEvaluationType evaluationType, final Grade grade) {

	return (EnrolmentEvaluation) CollectionUtils.find(getEvaluationsSet(), new Predicate() {

	    final public boolean evaluate(Object o) {
		EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) o;
		Grade evaluationGrade = enrolmentEvaluation.getGrade();

		return enrolmentEvaluation.getEnrolmentEvaluationType().equals(evaluationType) && evaluationGrade.equals(grade);
	    }

	});
    }

    final public EnrolmentEvaluation getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(
	    final EnrolmentEvaluationState state, final EnrolmentEvaluationType type) {
	return (EnrolmentEvaluation) CollectionUtils.find(getEvaluationsSet(), new Predicate() {

	    final public boolean evaluate(Object o) {
		EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) o;
		return (enrolmentEvaluation.getEnrolmentEvaluationState().equals(state) && enrolmentEvaluation
			.getEnrolmentEvaluationType().equals(type));
	    }

	});
    }

    public EnrolmentEvaluation getEnrolmentEvaluation(pt.utl.ist.fenix.tools.predicates.Predicate<EnrolmentEvaluation> predicate) {
	for (EnrolmentEvaluation enrolmentEvaluation : getEvaluations()) {
	    if (predicate.eval(enrolmentEvaluation)) {
		return enrolmentEvaluation;
	    }
	}
	return null;
    }

    final public List<EnrolmentEvaluation> getEnrolmentEvaluationsByEnrolmentEvaluationState(
	    final EnrolmentEvaluationState evaluationState) {
	List<EnrolmentEvaluation> result = new ArrayList<EnrolmentEvaluation>();
	for (EnrolmentEvaluation evaluation : getEvaluationsSet()) {
	    if (evaluation.getEnrolmentEvaluationState().equals(evaluationState)) {
		result.add(evaluation);
	    }
	}
	return result;
    }

    final public List<EnrolmentEvaluation> getEnrolmentEvaluationsByEnrolmentEvaluationType(
	    final EnrolmentEvaluationType evaluationType) {
	List<EnrolmentEvaluation> result = new ArrayList<EnrolmentEvaluation>();
	for (EnrolmentEvaluation evaluation : getEvaluationsSet()) {
	    if (evaluation.getEnrolmentEvaluationType().equals(evaluationType)) {
		result.add(evaluation);
	    }
	}
	return result;
    }

    final public EnrolmentEvaluation submitEnrolmentEvaluation(EnrolmentEvaluationType enrolmentEvaluationType,
	    Mark publishedMark, Employee employee, Person personResponsibleForGrade, Date evaluationDate, String observation) {

	EnrolmentEvaluation enrolmentEvaluation = getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(
		EnrolmentEvaluationState.TEMPORARY_OBJ, enrolmentEvaluationType);

	// There can be only one enrolmentEvaluation with Temporary State
	if (enrolmentEvaluation == null) {
	    enrolmentEvaluation = new EnrolmentEvaluation(this, enrolmentEvaluationType, EnrolmentEvaluationState.TEMPORARY_OBJ,
		    employee);
	} else {
	    enrolmentEvaluation.setEnrolmentEvaluationType(enrolmentEvaluationType);
	    enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
	    enrolmentEvaluation.setEmployee(employee);
	}

	// teacher responsible for execution course
	Grade grade = null;
	if ((publishedMark == null) || (publishedMark.getMark().length() == 0))
	    grade = Grade.createGrade(GradeScale.NA, getGradeScale());
	else
	    grade = Grade.createGrade(publishedMark.getMark(), getGradeScale());

	enrolmentEvaluation.setGrade(grade);

	enrolmentEvaluation.setObservation(observation);
	enrolmentEvaluation.setPersonResponsibleForGrade(personResponsibleForGrade);

	final YearMonthDay yearMonthDay = new YearMonthDay();
	enrolmentEvaluation.setGradeAvailableDateYearMonthDay(yearMonthDay);
	if (evaluationDate != null) {
	    enrolmentEvaluation.setExamDateYearMonthDay(new YearMonthDay(evaluationDate));
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
	    enrolmentEvaluation = new EnrolmentEvaluation(this, EnrolmentEvaluationType.NORMAL,
		    EnrolmentEvaluationState.TEMPORARY_OBJ);
	    enrolmentEvaluation.setWhenDateTime(new DateTime());

	    addEvaluations(enrolmentEvaluation);
	}
    }

    private void createAttend(Registration registration, CurricularCourse curricularCourse, ExecutionSemester executionSemester) {

	final List<ExecutionCourse> executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester);

	ExecutionCourse executionCourse = null;
	if (executionCourses.size() > 1) {
	    final Iterator<ExecutionCourse> iterator = executionCourses.iterator();
	    while (iterator.hasNext()) {
		final ExecutionCourse each = iterator.next();
		if (!each.hasAnyExecutionCourseProperties()) {
		    executionCourse = each;
		}
	    }
	} else if (executionCourses.size() == 1) {
	    executionCourse = executionCourses.get(0);
	}

	if (executionCourse != null) {
	    final Attends attend = executionCourse.getAttendsByStudent(registration.getStudent());
	    if (attend == null) {
		addAttends(new Attends(registration, executionCourse));
	    } else if (!attend.hasEnrolment()) {
		attend.setRegistration(registration);
		addAttends(attend);
	    } else {
		throw new DomainException("error.cannot.create.multiple.enrolments.for.student.in.execution.course",
			executionCourse.getNome(), executionCourse.getExecutionPeriod().getQualifiedName());
	    }
	}
    }

    final public void createAttends(final Registration registration, final ExecutionCourse executionCourse) {
	final Attends attendsFor = this.getAttendsFor(executionCourse.getExecutionPeriod());
	if (attendsFor != null) {
	    try {
		attendsFor.delete();
	    } catch (DomainException e) {
		throw new DomainException("error.attends.cant.change.attends");
	    }
	}

	this.addAttends(new Attends(registration, executionCourse));
    }

    final public EnrolmentEvaluation createEnrolmentEvaluationForImprovement(final Employee employee,
	    final ExecutionSemester executionSemester) {
	final EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(this, EnrolmentEvaluationType.IMPROVEMENT,
		EnrolmentEvaluationState.TEMPORARY_OBJ, employee, executionSemester);
	createAttendForImprovement(executionSemester);
	return enrolmentEvaluation;
    }

    private void createAttendForImprovement(final ExecutionSemester executionSemester) {
	final Registration registration = getRegistration();

	// FIXME this is completly wrong, there may be more than one execution
	// course for this curricular course
	ExecutionCourse currentExecutionCourse = (ExecutionCourse) CollectionUtils.find(getCurricularCourse()
		.getAssociatedExecutionCourses(), new Predicate() {

	    final public boolean evaluate(Object arg0) {
		ExecutionCourse executionCourse = (ExecutionCourse) arg0;
		if (executionCourse.getExecutionPeriod().equals(executionSemester)
			&& executionCourse.getEntryPhase().equals(EntryPhase.FIRST_PHASE_OBJ)) {
		    return true;
		}
		return false;
	    }

	});

	if (currentExecutionCourse != null) {
	    List attends = currentExecutionCourse.getAttends();
	    Attends attend = (Attends) CollectionUtils.find(attends, new Predicate() {

		public boolean evaluate(Object arg0) {
		    Attends attend = (Attends) arg0;
		    if (attend.getRegistration().equals(registration))
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

    final public void unEnrollImprovement(final ExecutionSemester executionSemester) throws DomainException {
	final EnrolmentEvaluation temporaryImprovement = getImprovementEvaluation();
	if (temporaryImprovement == null) {
	    throw new DomainException("error.enrolment.cant.unenroll.improvement");
	}

	temporaryImprovement.delete();
	Attends attends = getAttendsFor(executionSemester);
	if (attends != null) {
	    attends.delete();
	}
    }

    final public boolean isImprovementForExecutionCourse(ExecutionCourse executionCourse) {
	return getCurricularCourse().hasAssociatedExecutionCourses(executionCourse)
		&& getExecutionPeriod() != executionCourse.getExecutionPeriod();
    }

    final public boolean isImprovingInExecutionPeriodFollowingApproval(final ExecutionSemester improvementExecutionPeriod) {
	final DegreeModule degreeModule = getDegreeModule();
	if (hasImprovement() || !isApproved() || !degreeModule.hasAnyParentContexts(improvementExecutionPeriod)) {
	    throw new DomainException("Enrolment.is.not.in.improvement.conditions");
	}

	final ExecutionSemester enrolmentExecutionPeriod = getExecutionPeriod();
	if (improvementExecutionPeriod.isBeforeOrEquals(enrolmentExecutionPeriod)) {
	    throw new DomainException("Enrolment.cannot.improve.enrolment.prior.to.its.execution.period");
	}

	ExecutionSemester enrolmentNextExecutionPeriod = enrolmentExecutionPeriod.getNextExecutionPeriod();
	if (improvementExecutionPeriod == enrolmentNextExecutionPeriod) {
	    return true;
	}

	for (ExecutionSemester executionSemester = enrolmentNextExecutionPeriod; executionSemester != null
		&& executionSemester != improvementExecutionPeriod; executionSemester = executionSemester
		.getNextExecutionPeriod()) {
	    if (degreeModule.hasAnyParentContexts(executionSemester)) {
		return false;
	    }
	}

	return true;
    }

    final public EnrolmentEvaluation createSpecialSeasonEvaluation(final Employee employee) {
	if (getEnrolmentEvaluationType() != EnrolmentEvaluationType.SPECIAL_SEASON && !isApproved()) {
	    setEnrolmentEvaluationType(EnrolmentEvaluationType.SPECIAL_SEASON);
	    setEnrollmentState(EnrollmentState.ENROLLED);

	    return new EnrolmentEvaluation(this, EnrolmentEvaluationType.SPECIAL_SEASON, EnrolmentEvaluationState.TEMPORARY_OBJ,
		    employee);
	} else {
	    throw new DomainException("error.invalid.enrolment.state");
	}
    }

    final public void deleteSpecialSeasonEvaluation() {
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

    final public List<EnrolmentEvaluation> getAllFinalEnrolmentEvaluations() {
	final List<EnrolmentEvaluation> result = new ArrayList<EnrolmentEvaluation>();

	for (final EnrolmentEvaluation enrolmentEvaluation : getEvaluationsSet()) {
	    if (enrolmentEvaluation.isFinal()) {
		result.add(enrolmentEvaluation);
	    }
	}

	return result;
    }

    private boolean hasEnrolmentEvaluationByType(final EnrolmentEvaluationType enrolmentEvaluationType) {
	for (final EnrolmentEvaluation enrolmentEvaluation : getEvaluationsSet()) {
	    if (enrolmentEvaluation.getEnrolmentEvaluationType().equals(enrolmentEvaluationType)) {
		return true;
	    }
	}
	return false;
    }

    final public boolean hasImprovement() {
	return hasEnrolmentEvaluationByType(EnrolmentEvaluationType.IMPROVEMENT);
    }

    final public boolean hasImprovementFor(ExecutionSemester executionSemester) {
	for (EnrolmentEvaluation enrolmentEvaluation : this.getEvaluationsSet()) {
	    if (enrolmentEvaluation.isImprovment() && enrolmentEvaluation.hasExecutionPeriod()
		    && enrolmentEvaluation.getExecutionPeriod().equals(executionSemester)) {
		return true;
	    }
	}
	return false;
    }

    final public boolean hasSpecialSeason() {
	return hasEnrolmentEvaluationByType(EnrolmentEvaluationType.SPECIAL_SEASON);
    }

    final public boolean hasSpecialSeasonInExecutionYear() {
	for (final Enrolment enrolment : getBrothers()) {
	    if (enrolment.getExecutionYear() == getExecutionYear() && enrolment.hasSpecialSeason()) {
		return true;
	    }
	}

	return hasSpecialSeason();
    }

    final public boolean isNotEvaluated() {
	final EnrolmentEvaluation latestEnrolmentEvaluation = getLatestEnrolmentEvaluation();
	return latestEnrolmentEvaluation == null || latestEnrolmentEvaluation.isNotEvaluated();
    }

    final public boolean isFlunked() {
	final EnrolmentEvaluation latestEnrolmentEvaluation = getLatestEnrolmentEvaluation();
	return latestEnrolmentEvaluation != null && latestEnrolmentEvaluation.isFlunked();
    }

    @Override
    final public boolean isApproved() {
	if (isAnnulled()) {
	    return false;
	}

	final EnrolmentEvaluation latestEnrolmentEvaluation = getLatestEnrolmentEvaluation();
	return latestEnrolmentEvaluation != null && latestEnrolmentEvaluation.isApproved();
    }

    final public boolean isAproved(final ExecutionYear executionYear) {
	return (executionYear == null || getExecutionYear().isBeforeOrEquals(executionYear)) && isApproved();
    }

    @Override
    public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
	if (executionSemester == null || getExecutionPeriod().isBeforeOrEquals(executionSemester)) {
	    return isApproved() && hasCurricularCourse(getCurricularCourse(), curricularCourse, executionSemester);
	} else {
	    return false;
	}
    }

    @Override
    public final ConclusionValue isConcluded(ExecutionYear executionYear) {
	return ConclusionValue.create(isAproved(executionYear));
    }

    @Override
    public YearMonthDay calculateConclusionDate() {
	if (!isApproved()) {
	    throw new DomainException("error.Enrolment.not.approved");
	}

	EnrolmentEvaluation exceptImprovements = getLatestEnrolmentEvaluationExceptImprovements();
	if (exceptImprovements == null || exceptImprovements.getExamDateYearMonthDay() == null) {
	    return getLatestEnrolmentEvaluation().getExamDateYearMonthDay();
	} else {
	    return exceptImprovements.getExamDateYearMonthDay();
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    final public Curriculum getCurriculum(final DateTime when, final ExecutionYear year) {
	if (wasCreated(when) && (year == null || getExecutionYear().isBefore(year)) && isApproved() && !isPropaedeutic()
		&& !isExtraCurricular()) {
	    return new Curriculum(this, year, Collections.singleton((ICurriculumEntry) this), Collections.EMPTY_SET, Collections
		    .singleton((ICurriculumEntry) this));
	}

	return Curriculum.createEmpty(this, year);
    }

    final public Grade getGrade() {
	final EnrolmentEvaluation enrolmentEvaluation = getLatestEnrolmentEvaluation();
	return enrolmentEvaluation == null ? Grade.createEmptyGrade() : enrolmentEvaluation.getGrade();
    }

    final public String getGradeValue() {
	return getGrade().getValue();
    }

    final public Integer getFinalGrade() {
	return getGrade().getIntegerValue();
    }

    public GradeScale getGradeScale() {
	return getCurricularCourse().getGradeScaleChain();
    }

    public BigDecimal getWeigthTimesGrade() {
	return getGrade().isNumeric() ? getWeigthForCurriculum().multiply(getGrade().getNumericValue()) : null;
    }

    final public boolean isEnroled() {
	return this.getEnrollmentState() == EnrollmentState.ENROLLED;
    }

    @Deprecated
    final public boolean isEnrolmentStateApproved() {
	return this.getEnrollmentState() == EnrollmentState.APROVED;
    }

    @Deprecated
    final public boolean isEnrolmentStateNotApproved() {
	return this.getEnrollmentState() == EnrollmentState.NOT_APROVED;
    }

    @Deprecated
    final public boolean isEnrolmentStateNotEvaluated() {
	return this.getEnrollmentState() == EnrollmentState.NOT_EVALUATED;
    }

    final public boolean isAnnulled() {
	return this.getEnrollmentState() == EnrollmentState.ANNULED;
    }

    final public boolean isTemporarilyEnroled() {
	return this.getEnrollmentState() == EnrollmentState.TEMPORARILY_ENROLLED;
    }

    final public boolean isEvaluated() {
	return isEnrolmentStateApproved() || isEnrolmentStateNotApproved();
    }

    final public boolean isActive() {
	return !isAnnulled() && !isTemporarilyEnroled();
    }

    final public Boolean isFirstTime() {
	if (getIsFirstTime() == null) {
	    resetIsFirstTimeEnrolment();
	}
	return getIsFirstTime();
    }

    final public void resetIsFirstTimeEnrolment() {
	if (getStudentCurricularPlan() != null && getCurricularCourse() != null && getExecutionPeriod() != null
		&& getEnrollmentState() != null) {
	    getStudentCurricularPlan().resetIsFirstTimeEnrolmentForCurricularCourse(getCurricularCourse());
	} else {
	    setIsFirstTime(Boolean.FALSE);
	}
    }

    @Override
    final public void setDegreeModule(DegreeModule degreeModule) {
	super.setDegreeModule(degreeModule);
	resetIsFirstTimeEnrolment();
    }

    @Override
    final public void setEnrollmentState(EnrollmentState enrollmentState) {
	super.setEnrollmentState(enrollmentState);
	resetIsFirstTimeEnrolment();
    }

    @Override
    final public void setExecutionPeriod(ExecutionSemester executionSemester) {
	super.setExecutionPeriod(executionSemester);
	resetIsFirstTimeEnrolment();
    }

    @Override
    final public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
	super.setStudentCurricularPlan(studentCurricularPlan);
	resetIsFirstTimeEnrolment();
    }

    final public int getNumberOfTotalEnrolmentsInThisCourse() {
	return this.getStudentCurricularPlan().countEnrolmentsByCurricularCourse(this.getCurricularCourse());
    }

    final public int getNumberOfTotalEnrolmentsInThisCourse(ExecutionSemester untilExecutionPeriod) {
	return this.getStudentCurricularPlan()
		.countEnrolmentsByCurricularCourse(this.getCurricularCourse(), untilExecutionPeriod);
    }

    @Override
    protected void createCurriculumLineLog(final EnrolmentAction action) {
	new EnrolmentLog(action, getRegistration(), getCurricularCourse(), getExecutionPeriod(), getCurrentUser());
    }

    @Override
    public StringBuilder print(String tabs) {
	final StringBuilder builder = new StringBuilder();
	builder.append(tabs);
	builder.append("[E ").append(getDegreeModule().getName()).append(" ").append(isApproved()).append(" ]\n");
	return builder;
    }

    final public EnrolmentEvaluation addNewEnrolmentEvaluation(EnrolmentEvaluationState enrolmentEvaluationState,
	    EnrolmentEvaluationType enrolmentEvaluationType, Person responsibleFor, String gradeValue, Date availableDate,
	    Date examDate, ExecutionSemester executionSemester) {

	final Grade grade = Grade.createGrade(gradeValue, getGradeScale());

	final EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(this, enrolmentEvaluationState,
		enrolmentEvaluationType, responsibleFor, grade, availableDate, examDate, new DateTime());
	if (enrolmentEvaluationType == EnrolmentEvaluationType.IMPROVEMENT) {
	    enrolmentEvaluation.setExecutionPeriod(executionSemester);
	}
	return enrolmentEvaluation;
    }

    final public boolean hasAssociatedMarkSheet(MarkSheetType markSheetType) {
	for (final EnrolmentEvaluation enrolmentEvaluation : this.getEvaluationsSet()) {
	    if (enrolmentEvaluation.hasMarkSheet()
		    && enrolmentEvaluation.getEnrolmentEvaluationType() == markSheetType.getEnrolmentEvaluationType()) {
		return true;
	    }
	}
	return false;
    }

    final public boolean hasAssociatedMarkSheetOrFinalGrade() {
	for (final EnrolmentEvaluation enrolmentEvaluation : getEvaluationsSet()) {
	    if (enrolmentEvaluation.hasMarkSheet() || enrolmentEvaluation.isFinal()) {
		return true;
	    }
	}
	return false;
    }

    final public boolean hasAssociatedMarkSheetOrFinalGrade(MarkSheetType markSheetType) {
	for (final EnrolmentEvaluation enrolmentEvaluation : this.getEvaluationsSet()) {
	    if (enrolmentEvaluation.getEnrolmentEvaluationType() == markSheetType.getEnrolmentEvaluationType()
		    && (enrolmentEvaluation.hasMarkSheet() || enrolmentEvaluation.isFinal())) {
		return true;
	    }
	}
	return false;
    }

    final public List<EnrolmentEvaluation> getConfirmedEvaluations(MarkSheetType markSheetType) {
	List<EnrolmentEvaluation> evaluations = new ArrayList<EnrolmentEvaluation>();
	for (EnrolmentEvaluation evaluation : this.getEvaluationsSet()) {
	    if (evaluation.hasMarkSheet() && evaluation.getMarkSheet().getMarkSheetType() == markSheetType
		    && evaluation.getMarkSheet().isConfirmed()) {

		evaluations.add(evaluation);
	    }
	}
	Collections.sort(evaluations, EnrolmentEvaluation.COMPARATORY_BY_WHEN);
	return evaluations;
    }

    final public Attends getAttendsByExecutionCourse(ExecutionCourse executionCourse) {
	for (final Attends attends : this.getAttendsSet()) {
	    if (attends.isFor(executionCourse)) {
		return attends;
	    }
	}
	return null;
    }

    final public boolean hasAttendsFor(ExecutionSemester executionSemester) {
	for (final Attends attends : this.getAttendsSet()) {
	    if (attends.isFor(executionSemester)) {
		return true;
	    }
	}
	return false;
    }

    final public Attends getAttendsFor(final ExecutionSemester executionSemester) {
	Attends result = null;

	for (final Attends attends : getAttendsSet()) {
	    if (attends.isFor(executionSemester)) {
		if (result == null) {
		    result = attends;
		} else {
		    throw new DomainException("Enrolment.found.two.attends.for.same.execution.period");
		}
	    }
	}

	return result;
    }

    final public ExecutionCourse getExecutionCourseFor(final ExecutionSemester executionSemester) {
	for (final Attends attend : getAttends()) {
	    if (attend.getExecutionCourse().getExecutionPeriod() == executionSemester) {
		return attend.getExecutionCourse();
	    }
	}

	return null;
    }

    @Deprecated
    final public EnrolmentEvaluation getFinalEnrolmentEvaluation() {
	return getLatestEnrolmentEvaluation();
    }

    final public EnrolmentEvaluation getLatestEnrolmentEvaluation() {
	return (getStudentCurricularPlan().getDegreeType().getAdministrativeOfficeType() == AdministrativeOfficeType.DEGREE ? getLatestEnrolmentEvalution(getAllFinalEnrolmentEvaluations())
		: getLatestEnrolmentEvalution(getEvaluationsSet()));
    }

    final public EnrolmentEvaluation getLatestEnrolmentEvaluationExceptImprovements() {
	final Collection<EnrolmentEvaluation> toInspect = new HashSet<EnrolmentEvaluation>();

	for (final EnrolmentEvaluation enrolmentEvaluation : getEvaluationsSet()) {
	    if (!enrolmentEvaluation.isImprovment()) {
		toInspect.add(enrolmentEvaluation);
	    }
	}

	return getLatestEnrolmentEvalution(toInspect);
    }

    @SuppressWarnings("unchecked")
    private EnrolmentEvaluation getLatestEnrolmentEvalution(Collection<EnrolmentEvaluation> enrolmentEvaluations) {
	return ((enrolmentEvaluations == null || enrolmentEvaluations.isEmpty()) ? null : Collections
		.<EnrolmentEvaluation> max(enrolmentEvaluations));
    }

    final public EnrolmentEvaluation getLatestEnrolmentEvaluationBy(EnrolmentEvaluationType evaluationType) {
	return getLatestEnrolmentEvalution(getEnrolmentEvaluationsByEnrolmentEvaluationType(evaluationType));
    }

    final public EnrolmentEvaluation getLatestNormalEnrolmentEvaluation() {
	return getLatestEnrolmentEvaluationBy(EnrolmentEvaluationType.NORMAL);
    }

    final public EnrolmentEvaluation getLatestSpecialSeasonEnrolmentEvaluation() {
	return getLatestEnrolmentEvaluationBy(EnrolmentEvaluationType.SPECIAL_SEASON);
    }

    final public EnrolmentEvaluation getLatestImprovementEnrolmentEvaluation() {
	return getLatestEnrolmentEvaluationBy(EnrolmentEvaluationType.IMPROVEMENT);
    }

    final public EnrolmentEvaluation getImprovementEvaluation() {
	final EnrolmentEvaluation latestImprovementEnrolmentEvaluation = getLatestImprovementEnrolmentEvaluation();

	if (latestImprovementEnrolmentEvaluation != null
		&& latestImprovementEnrolmentEvaluation.getEnrolmentEvaluationState().equals(
			EnrolmentEvaluationState.TEMPORARY_OBJ)) {
	    return latestImprovementEnrolmentEvaluation;
	}

	return null;
    }

    final public EnrolmentEvaluation getLatestEquivalenceEnrolmentEvaluation() {
	return getLatestEnrolmentEvaluationBy(EnrolmentEvaluationType.EQUIVALENCE);
    }

    final public Double getAccumulatedEctsCredits() {
	return accumulatedEctsCredits;
    }

    final public void setAccumulatedEctsCredits(Double ectsCredits) {
	this.accumulatedEctsCredits = ectsCredits;
    }

    @Override
    final public List<Enrolment> getEnrolments() {
	return Collections.singletonList(this);
    }

    @Override
    final public boolean hasAnyEnrolments() {
	return true;
    }

    @Override
    final public StudentCurricularPlan getStudentCurricularPlan() {
	return hasCurriculumGroup() ? getCurriculumGroup().getStudentCurricularPlan() : super.getStudentCurricularPlan();
    }

    @Override
    public boolean isEnroledInExecutionPeriod(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
	return isValid(executionSemester) && this.getCurricularCourse().equals(curricularCourse);
    }

    public boolean isValid(final ExecutionSemester executionSemester) {
	return getExecutionPeriod() == executionSemester
		|| (getCurricularCourse().isAnual() && getExecutionPeriod().getExecutionYear() == executionSemester
			.getExecutionYear());
    }

    public boolean isValid(final ExecutionYear executionYear) {
	for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
	    if (isValid(executionSemester)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    final public boolean hasEnrolmentWithEnroledState(final CurricularCourse curricularCourse,
	    final ExecutionSemester executionSemester) {
	return isEnroledInExecutionPeriod(curricularCourse, executionSemester) && isEnroled();
    }

    final public List<ExecutionCourse> getExecutionCourses() {
	return this.getCurricularCourse().getAssociatedExecutionCourses();
    }

    final public boolean isEnrolmentTypeNormal() {
	return getCurricularCourse().getType() == CurricularCourseType.NORMAL_COURSE && !isExtraCurricular() && !isOptional();
    }

    final public String getEnrolmentTypeName() {
	if (isExtraCurricular()) {
	    return "EXTRA_CURRICULAR_ENROLMENT";
	} else if (isOptional()) {
	    return "ENROLMENT_IN_OPTIONAL_DEGREE_MODULE";
	} else {
	    return getCurricularCourse().getType().name();
	}
    }

    final public static class DeleteEnrolmentExecutor implements FactoryExecutor {

	private final DomainReference<Enrolment> enrolment;

	public DeleteEnrolmentExecutor(Enrolment enrolment) {
	    super();
	    this.enrolment = new DomainReference<Enrolment>(enrolment);
	}

	public Object execute() {
	    enrolment.getObject().delete();
	    return null;
	}

    }

    static public int countEvaluated(final List<Enrolment> enrolments) {
	int result = 0;

	for (final Enrolment enrolment : enrolments) {
	    if (enrolment.isEvaluated()) {
		result++;
	    }
	}

	return result;
    }

    static public int countApproved(final List<Enrolment> enrolments) {
	int result = 0;

	for (final Enrolment enrolment : enrolments) {
	    if (enrolment.isEnrolmentStateApproved()) {
		result++;
	    }
	}

	return result;
    }

    static final public BigDecimal LEIC_WEIGHT_BEFORE_0607_EXCEPT_TFC = BigDecimal.valueOf(4.0d);

    static final BigDecimal LMAC_AND_LCI_WEIGHT_FACTOR = BigDecimal.valueOf(0.25d);

    static final BigDecimal LMAC_WEIGHT_BEFORE_0607_EXCEPT_LMAC_AND_LCI_DEGREE_MODULES = BigDecimal.valueOf(7.5d);

    @Override
    final public Double getWeigth() {
	return isExtraCurricular() || isPropaedeutic() ? Double.valueOf(0) : getWeigthForCurriculum().doubleValue();
    }

    final public BigDecimal getWeigthForCurriculum() {
	if (!isBolonhaDegree()) {

	    final DegreeCurricularPlan dcpOfStudent = getDegreeCurricularPlanOfStudent();
	    if (dcpOfStudent.getDegreeType().isDegree()) {
		if (isExecutionYearEnrolmentAfterOrEqualsExecutionYear0607()) {
		    return getEctsCreditsForCurriculum();
		}

		final Degree leicPb = Degree.readBySigla("LEIC-pB");
		if (isStudentFromDegree(leicPb, dcpOfStudent)) {
		    return getBaseWeigth();
		}

		final Degree lmacPb = Degree.readBySigla("LMAC-pB");
		final DegreeCurricularPlan dcpOfDegreeModule = getDegreeCurricularPlanOfDegreeModule();
		if (isDegreeModuleFromDegree(lmacPb, dcpOfDegreeModule)) {
		    return getBaseWeigth().multiply(LMAC_AND_LCI_WEIGHT_FACTOR);
		}

		final Degree lciPb = Degree.readBySigla("LCI-pB");
		if (isDegreeModuleFromDegree(lciPb, dcpOfDegreeModule)) {
		    return getBaseWeigth().multiply(LMAC_AND_LCI_WEIGHT_FACTOR);
		}

		if (isStudentFromDegree(lmacPb, dcpOfStudent)) {
		    return LMAC_WEIGHT_BEFORE_0607_EXCEPT_LMAC_AND_LCI_DEGREE_MODULES;
		}
	    }
	}

	return getBaseWeigth();
    }

    private BigDecimal getBaseWeigth() {
	return BigDecimal.valueOf((super.getWeigth() == null || super.getWeigth() == 0d) ? getCurricularCourse().getWeigth()
		: super.getWeigth());
    }

    private boolean isExecutionYearEnrolmentAfterOrEqualsExecutionYear0607() {
	final ExecutionYear executionYear = getExecutionPeriod().getExecutionYear();
	final ExecutionYear executionYear0607 = ExecutionYear.readExecutionYearByName("2006/2007");
	return executionYear.isAfterOrEquals(executionYear0607);
    }

    private boolean isStudentFromDegree(final Degree degree, final DegreeCurricularPlan degreeCurricularPlanOfStudent) {
	return degree.hasDegreeCurricularPlans(degreeCurricularPlanOfStudent);
    }

    private boolean isDegreeModuleFromDegree(final Degree degree, DegreeCurricularPlan degreeCurricularPlanOfDegreeModule) {
	return degree.hasDegreeCurricularPlans(degreeCurricularPlanOfDegreeModule);
    }

    /**
     * Just for Master Degrees legacy code
     * 
     * @return
     */
    @Deprecated
    final public double getCredits() {
	return getEctsCredits();
    }

    @Override
    final public Double getEctsCredits() {
	return isExtraCurricular() || isPropaedeutic() ? Double.valueOf(0d) : getEctsCreditsForCurriculum().doubleValue();
    }

    final public BigDecimal getEctsCreditsForCurriculum() {
	return BigDecimal.valueOf(getCurricularCourse().getEctsCredits(getExecutionPeriod()));
    }

    @Override
    final public Double getAprovedEctsCredits() {
	return isApproved() ? getEctsCredits() : Double.valueOf(0d);
    }

    @Override
    final public Double getCreditsConcluded(ExecutionYear executionYear) {
	return getAprovedEctsCredits();
    }

    @Override
    final public Double getEnroledEctsCredits(final ExecutionSemester executionSemester) {
	return isValid(executionSemester) && isEnroled() ? getEctsCredits() : Double.valueOf(0d);
    }

    @Override
    final public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
	return isEnroledInExecutionPeriod(curricularCourse, executionSemester) ? this : null;
    }

    @Override
    final public Enrolment getApprovedEnrolment(final CurricularCourse curricularCourse) {
	return isApproved(curricularCourse) ? this : null;
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(final ExecutionSemester executionSemester) {
	if (isValid(executionSemester) && isEnroled()) {
	    if (isFromExternalDegree()) {
		return Collections
			.<IDegreeModuleToEvaluate> singleton(new ExternalDegreeEnrolmentWrapper(this, executionSemester));
	    } else {
		return Collections.<IDegreeModuleToEvaluate> singleton(new EnroledEnrolmentWrapper(this, executionSemester));
	    }
	}
	return Collections.emptySet();
    }

    private boolean isFromExternalDegree() {
	return getDegreeModule().getParentDegreeCurricularPlan() != getDegreeCurricularPlanOfDegreeModule();
    }

    final public double getAccumulatedEctsCredits(final ExecutionSemester executionSemester) {
	if (!isBolonhaDegree()) {
	    return accumulatedEctsCredits;
	}
	if (!parentAllowAccumulatedEctsCredits()) {
	    return 0d;
	}

	return getStudentCurricularPlan().getAccumulatedEctsCredits(executionSemester, getCurricularCourse());
    }

    final public boolean isImprovementEnroled() {
	return isEnrolmentStateApproved() && getImprovementEvaluation() != null;
    }

    final public boolean canBeImproved() {
	return isEnrolmentStateApproved() && !hasImprovement();
    }

    final public boolean isSpecialSeasonEnroled(final ExecutionYear executionYear) {
	return isSpecialSeason() && getExecutionPeriod().getExecutionYear() == executionYear
		&& getTempSpecialSeasonEvaluation() != null;
    }

    private EnrolmentEvaluation getTempSpecialSeasonEvaluation() {
	final EnrolmentEvaluation latestSpecialSeasonEnrolmentEvaluation = getLatestSpecialSeasonEnrolmentEvaluation();

	if (latestSpecialSeasonEnrolmentEvaluation != null
		&& latestSpecialSeasonEnrolmentEvaluation.getEnrolmentEvaluationState().equals(
			EnrolmentEvaluationState.TEMPORARY_OBJ)) {
	    return latestSpecialSeasonEnrolmentEvaluation;
	}

	return null;
    }

    final public boolean canBeSpecialSeasonEnroled(ExecutionYear executionYear) {
	return getEnrolmentEvaluationType() != EnrolmentEvaluationType.SPECIAL_SEASON
		&& getExecutionPeriod().getExecutionYear() == executionYear && !isApproved();
    }

    @Override
    final public Collection<Enrolment> getSpecialSeasonEnrolments(final ExecutionYear executionYear) {
	if (isSpecialSeason() && getExecutionPeriod().getExecutionYear().equals(executionYear)) {
	    return Collections.singleton(this);
	}
	return Collections.emptySet();
    }

    final public String getDescription() {
	return getStudentCurricularPlan().getDegree().getPresentationName(getExecutionYear()) + " > " + getName().getContent();
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * This method assumes that each Student has at most one non evaluated
     * Thesis and no more that two Thesis.
     */
    final public Thesis getThesis() {
	List<Thesis> theses = getTheses();

	switch (theses.size()) {
	case 0:
	    return null;
	case 1:
	    return theses.iterator().next();
	default:
	    SortedSet<Thesis> sortedTheses = new TreeSet<Thesis>(new Comparator<Thesis>() {
		public int compare(Thesis o1, Thesis o2) {
		    return o2.getCreation().compareTo(o1.getCreation());
		}
	    });

	    sortedTheses.addAll(theses);
	    return sortedTheses.iterator().next();
	}
    }

    final public boolean isBefore(final Enrolment enrolment) {
	return getExecutionPeriod().isBefore(enrolment.getExecutionPeriod());
    }

    final public Proposal getDissertationProposal() {
	final ExecutionYear previousExecutionYear = getExecutionYear().getPreviousExecutionYear();
	if (previousExecutionYear == null) {
	    return null;
	}
	return getRegistration().getDissertationProposal(previousExecutionYear);
    }

    public Thesis getPreviousYearThesis() {
	ExecutionYear executionYear = getExecutionYear().getPreviousExecutionYear();
	Enrolment enrolment = getStudent().getDissertationEnrolment(null, executionYear);
	if (enrolment != null && enrolment.getThesis() != null) {
	    return enrolment.getThesis();
	}
	return null;
    }

    public Thesis getPossibleThesis() {
	Thesis thesis = getThesis();
	return (thesis == null && getDissertationProposal() == null) ? getPreviousYearThesis() : thesis;
    }

    // 
    public MultiLanguageString getPossibleDissertationTitle() {
	Thesis thesis = getThesis();
	if (thesis == null) {
	    if (getDissertationProposal() == null) {
		thesis = getPreviousYearThesis();
	    } else {
		return new MultiLanguageString(getDissertationProposal().getTitle());
	    }
	}
	return thesis == null ? new MultiLanguageString("-") : thesis.getTitle();
    }

    final public Unit getAcademicUnit() {
	return RootDomainObject.getInstance().getInstitutionUnit();
    }

    final public String getCode() {
	if (hasDegreeModule()) {
	    return getDegreeModule().getCode();
	}
	return null;
    }

    public boolean hasAnyAssociatedMarkSheetOrFinalGrade() {
	for (final EnrolmentEvaluation enrolmentEvaluation : getEvaluationsSet()) {
	    if (enrolmentEvaluation.hasMarkSheet() || enrolmentEvaluation.isFinal()) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public boolean hasEnrolment(ExecutionSemester executionSemester) {
	return isValid(executionSemester);
    }

    @Override
    public boolean hasEnrolment(ExecutionYear executionYear) {
	return isValid(executionYear);
    }

    @Override
    public boolean isEnroledInSpecialSeason(final ExecutionSemester executionSemester) {
	return isValid(executionSemester) && hasSpecialSeason();
    }

    @Override
    public boolean isEnroledInSpecialSeason(ExecutionYear executionYear) {
	return isValid(executionYear) && hasSpecialSeason();
    }

    @Override
    public int getNumberOfAllApprovedEnrolments(ExecutionSemester executionSemester) {
	return isValid(executionSemester) && isApproved() ? 1 : 0;
    }

    public boolean canBeSubmittedForOldMarkSheet(EnrolmentEvaluationType enrolmentEvaluationType) {
	if (enrolmentEvaluationType == EnrolmentEvaluationType.NORMAL && !hasAnyEvaluations()) {
	    return true;
	}

	for (EnrolmentEvaluation enrolmentEvaluation : getEvaluations()) {
	    if (enrolmentEvaluation.getEnrolmentEvaluationType() == enrolmentEvaluationType
		    && !enrolmentEvaluation.hasMarkSheet()
		    && (enrolmentEvaluation.isTemporary() || (enrolmentEvaluation.isNotEvaluated() && enrolmentEvaluation
			    .getExamDateYearMonthDay() == null))) {
		return true;
	    }
	}

	return false;
    }

    public boolean isSourceOfAnyCreditsInCurriculum() {
	for (final InternalEnrolmentWrapper enrolmentWrapper : this.getEnrolmentWrappers()) {
	    if (enrolmentWrapper.getCredits().hasAnyDismissalInCurriculum()) {
		return true;
	    }
	}
	return false;
    }

    static public Enrolment getEnrolmentWithLastExecutionPeriod(List<Enrolment> enrolments) {
	Collections.sort(enrolments, Enrolment.REVERSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_ID);
	return enrolments.get(0);
    }

    /**
     * 
     * After create new Enrolment, must delete OptionalEnrolment (to delete
     * OptionalEnrolment disconnect at least: ProgramCertificateRequests,
     * CourseLoadRequests, ExamDateCertificateRequests)
     * 
     * @param optionalEnrolment
     * @param curriculumGroup
     *            : new CurriculumGroup for Enrolment
     * @return Enrolment
     */
    static Enrolment createBasedOn(final OptionalEnrolment optionalEnrolment, final CurriculumGroup curriculumGroup) {
	checkParameters(optionalEnrolment, curriculumGroup);

	final Enrolment enrolment = new Enrolment();
	enrolment.setCurricularCourse(optionalEnrolment.getCurricularCourse());
	enrolment.setWeigth(optionalEnrolment.getWeigth());
	enrolment.setEnrollmentState(optionalEnrolment.getEnrollmentState());
	enrolment.setExecutionPeriod(optionalEnrolment.getExecutionPeriod());
	enrolment.setEnrolmentEvaluationType(optionalEnrolment.getEnrolmentEvaluationType());
	enrolment.setCreatedBy(AccessControl.getUserView().getUtilizador());
	enrolment.setCreationDateDateTime(optionalEnrolment.getCreationDateDateTime());
	enrolment.setEnrolmentCondition(optionalEnrolment.getEnrolmentCondition());
	enrolment.setCurriculumGroup(curriculumGroup);

	enrolment.getEvaluations().addAll(optionalEnrolment.getEvaluations());
	enrolment.getProgramCertificateRequests().addAll(optionalEnrolment.getProgramCertificateRequests());
	enrolment.getCourseLoadRequests().addAll(optionalEnrolment.getCourseLoadRequests());
	enrolment.getExtraExamRequests().addAll(optionalEnrolment.getExtraExamRequests());
	enrolment.getEnrolmentWrappers().addAll(optionalEnrolment.getEnrolmentWrappers());
	enrolment.getTheses().addAll(optionalEnrolment.getTheses());
	enrolment.getExamDateCertificateRequests().addAll(optionalEnrolment.getExamDateCertificateRequests());
	changeAttends(optionalEnrolment, enrolment);
	enrolment.createCurriculumLineLog(EnrolmentAction.ENROL);

	return enrolment;
    }

    @Override
    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	super.setCurriculumGroup(curriculumGroup);
	// Enrolment "isFirstTime" optimization needs to be re-computed, for all
	// sibling enrolments.
	if (curriculumGroup != null) {
	    curriculumGroup.getStudentCurricularPlan().setIsFirstTimeToNull();
	}
    }

    static protected void changeAttends(final Enrolment from, final Enrolment to) {
	final Registration oldRegistration = from.getRegistration();
	final Registration newRegistration = to.getRegistration();

	if (oldRegistration != newRegistration) {
	    for (final Attends attend : from.getAttends()) {
		oldRegistration.changeShifts(attend, newRegistration);
		attend.setRegistration(newRegistration);
	    }
	}
	to.getAttends().addAll(from.getAttends());
    }

    static private void checkParameters(final OptionalEnrolment optionalEnrolment, final CurriculumGroup curriculumGroup) {
	if (optionalEnrolment == null) {
	    throw new DomainException("error.Enrolment.invalid.optionalEnrolment");
	}
	if (curriculumGroup == null) {
	    throw new DomainException("error.Enrolment.invalid.curriculumGroup");
	}
    }

    public boolean isAnual() {
	final CurricularCourse curricularCourse = getCurricularCourse();
	return curricularCourse != null && curricularCourse.isAnual();
    }

}