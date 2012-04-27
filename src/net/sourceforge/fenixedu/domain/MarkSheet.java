package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentNotPayedException;
import net.sourceforge.fenixedu.domain.exceptions.InDebtEnrolmentsException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.predicates.MarkSheetPredicates;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.FenixDigestUtils;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class MarkSheet extends MarkSheet_Base {

    static final private Comparator<MarkSheet> COMPARATOR_BY_EVALUATION_DATE = new Comparator<MarkSheet>() {
	public int compare(MarkSheet o1, MarkSheet o2) {
	    if (o1.getEvaluationDateDateTime() == null && o2.getEvaluationDateDateTime() == null) {
		return 0;
	    }
	    if (o1.getEvaluationDateDateTime() == null) {
		return -1;
	    }
	    if (o2.getEvaluationDateDateTime() == null) {
		return 1;
	    }

	    return o1.getEvaluationDateDateTime().compareTo(o2.getEvaluationDateDateTime());
	}
    };

    static final private Comparator<MarkSheet> COMPARATOR_BY_CREATION_DATE = new Comparator<MarkSheet>() {
	public int compare(MarkSheet o1, MarkSheet o2) {
	    if (o1.getCreationDateDateTime() == null && o2.getCreationDateDateTime() == null) {
		return 0;
	    }
	    if (o1.getCreationDateDateTime() == null) {
		return -1;
	    }
	    if (o2.getCreationDateDateTime() == null) {
		return 1;
	    }

	    return o1.getCreationDateDateTime().compareTo(o2.getCreationDateDateTime());
	}
    };

    static final public Comparator<MarkSheet> COMPARATOR_BY_EVALUATION_DATE_AND_ID = new Comparator<MarkSheet>() {
	final public int compare(MarkSheet o1, MarkSheet o2) {
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(MarkSheet.COMPARATOR_BY_EVALUATION_DATE);
	    comparatorChain.addComparator(MarkSheet.COMPARATOR_BY_ID);

	    return comparatorChain.compare(o1, o2);
	}
    };

    static final public Comparator<MarkSheet> COMPARATOR_BY_EVALUATION_DATE_AND_CREATION_DATE_AND_ID = new Comparator<MarkSheet>() {
	final public int compare(MarkSheet o1, MarkSheet o2) {
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(MarkSheet.COMPARATOR_BY_EVALUATION_DATE);
	    comparatorChain.addComparator(MarkSheet.COMPARATOR_BY_CREATION_DATE);
	    comparatorChain.addComparator(MarkSheet.COMPARATOR_BY_ID);

	    return comparatorChain.compare(o1, o2);
	}
    };

    protected MarkSheet() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCreationDateDateTime(new DateTime());
	setPrinted(Boolean.FALSE);
    }

    private MarkSheet(CurricularCourse curricularCourse, ExecutionSemester executionSemester, Teacher responsibleTeacher,
	    Date evaluationDate, MarkSheetType markSheetType, MarkSheetState markSheetState, Boolean submittedByTeacher,
	    Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Employee employee) {

	this();
	checkParameters(curricularCourse, executionSemester, responsibleTeacher, evaluationDate, markSheetType, markSheetState,
		evaluationBeans, employee);
	init(curricularCourse, executionSemester, responsibleTeacher, evaluationDate, markSheetType, markSheetState,
		submittedByTeacher, employee);

	if (hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED)) {
	    addEnrolmentEvaluationsWithoutResctrictions(responsibleTeacher, evaluationBeans,
		    EnrolmentEvaluationState.TEMPORARY_OBJ);
	} else {
	    addEnrolmentEvaluationsWithResctrictions(responsibleTeacher, evaluationBeans, EnrolmentEvaluationState.TEMPORARY_OBJ);
	}
	generateCheckSum();
    }

    public static MarkSheet createNormal(CurricularCourse curricularCourse, ExecutionSemester executionSemester,
	    Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType, Boolean submittedByTeacher,
	    Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Employee employee) {

	return new MarkSheet(curricularCourse, executionSemester, responsibleTeacher, evaluationDate, markSheetType,
		MarkSheetState.NOT_CONFIRMED, submittedByTeacher, evaluationBeans, employee);
    }

    public static MarkSheet createOldNormal(CurricularCourse curricularCourse, ExecutionSemester executionSemester,
	    Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType,
	    Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Employee employee) {

	return new OldMarkSheet(curricularCourse, executionSemester, responsibleTeacher, evaluationDate, markSheetType,
		MarkSheetState.NOT_CONFIRMED, evaluationBeans, employee);
    }

    public static MarkSheet createRectification(CurricularCourse curricularCourse, ExecutionSemester executionSemester,
	    Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType, String reason,
	    MarkSheetEnrolmentEvaluationBean evaluationBean, Employee employee) {

	MarkSheet markSheet = new MarkSheet(curricularCourse, executionSemester, responsibleTeacher, evaluationDate,
		markSheetType, MarkSheetState.RECTIFICATION_NOT_CONFIRMED, Boolean.FALSE, (evaluationBean != null) ? Collections
			.singletonList(evaluationBean) : null, employee);
	markSheet.setReason(reason);
	return markSheet;
    }

    public static MarkSheet createOldRectification(CurricularCourse curricularCourse, ExecutionSemester executionSemester,
	    Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType, String reason,
	    MarkSheetEnrolmentEvaluationBean evaluationBean, Employee employee) {

	Collection<MarkSheetEnrolmentEvaluationBean> beans = (evaluationBean != null) ? Collections.singletonList(evaluationBean)
		: null;

	MarkSheet markSheet = new OldMarkSheet(curricularCourse, executionSemester, responsibleTeacher, evaluationDate,
		markSheetType, MarkSheetState.RECTIFICATION_NOT_CONFIRMED, beans, employee);
	markSheet.setReason(reason);
	return markSheet;
    }

    private void checkParameters(CurricularCourse curricularCourse, ExecutionSemester executionSemester,
	    Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType, MarkSheetState markSheetState,
	    Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Employee employee) {

	if (curricularCourse == null || executionSemester == null || responsibleTeacher == null || evaluationDate == null
		|| markSheetType == null || markSheetState == null || employee == null) {
	    throw new DomainException("error.markSheet.invalid.arguments");
	}
	if (evaluationBeans == null || evaluationBeans.size() == 0) {
	    throw new DomainException("error.markSheet.create.with.invalid.enrolmentEvaluations.number");
	}
	checkIfTeacherIsResponsibleOrCoordinator(curricularCourse, executionSemester, responsibleTeacher, markSheetType);
	checkIfEvaluationDateIsInExamsPeriod(curricularCourse, getExecutionDegree(curricularCourse, executionSemester),
		executionSemester, evaluationDate, markSheetType);
    }

    protected void checkIfTeacherIsResponsibleOrCoordinator(CurricularCourse curricularCourse,
	    ExecutionSemester executionSemester, Teacher responsibleTeacher, MarkSheetType markSheetType) throws DomainException {

	if (curricularCourse.isDissertation()) {
	    if (responsibleTeacher.getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
		return;
	    }
	    for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
		if (executionCourse.getExecutionPeriod().getExecutionYear() == executionSemester.getExecutionYear()) {
		    for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
			if (professorship.isResponsibleFor() && professorship.getTeacher() == responsibleTeacher) {
			    return;
			}
		    }
		}
	    }
	}

	if (markSheetType == MarkSheetType.IMPROVEMENT
		&& curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester).isEmpty()) {

	    if (!responsibleTeacher.getPerson().isResponsibleOrCoordinatorFor(curricularCourse,
		    executionSemester.getPreviousExecutionPeriod())
		    && !responsibleTeacher.getPerson().isResponsibleOrCoordinatorFor(curricularCourse,
			    executionSemester.getPreviousExecutionPeriod().getPreviousExecutionPeriod())
		    && !responsibleTeacher.getPerson().isResponsibleOrCoordinatorFor(
			    curricularCourse,
			    executionSemester.getPreviousExecutionPeriod().getPreviousExecutionPeriod()
				    .getPreviousExecutionPeriod())) {
		throw new DomainException("error.teacherNotResponsibleOrNotCoordinator");
	    }

	} else if (!responsibleTeacher.getPerson().isResponsibleOrCoordinatorFor(curricularCourse, executionSemester)) {
	    throw new DomainException("error.teacherNotResponsibleOrNotCoordinator");
	}

    }

    protected void checkIfEvaluationDateIsInExamsPeriod(CurricularCourse curricularCourse, ExecutionDegree executionDegree,
	    ExecutionSemester executionSemester, Date evaluationDate, MarkSheetType markSheetType) throws DomainException {

	if (executionDegree == null) {
	    if (!markSheetType.equals(MarkSheetType.IMPROVEMENT)
		    || !curricularCourse.getDegreeCurricularPlan().canSubmitImprovementMarkSheets(
			    executionSemester.getExecutionYear())) {
		throw new DomainException("error.evaluationDateNotInExamsPeriod");
	    }

	} else if (!executionDegree.isEvaluationDateInExamPeriod(evaluationDate, executionSemester, markSheetType)) {

	    OccupationPeriod occupationPeriod = executionDegree.getOccupationPeriodFor(executionSemester, markSheetType);
	    if (occupationPeriod == null) {
		throw new DomainException("error.evaluationDateNotInExamsPeriod");
	    } else {
		throw new DomainException("error.evaluationDateNotInExamsPeriod.withEvaluationDateAndPeriodDates", DateFormatUtil
			.format("dd/MM/yyyy", evaluationDate), occupationPeriod.getStartYearMonthDay().toString("dd/MM/yyyy"),
			occupationPeriod.getEndYearMonthDay().toString("dd/MM/yyyy"));
	    }
	}
    }

    private ExecutionDegree getExecutionDegree(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
	return curricularCourse.getDegreeCurricularPlan().getExecutionDegreeByYear(executionSemester.getExecutionYear());
    }

    protected void init(CurricularCourse curricularCourse, ExecutionSemester executionSemester, Teacher responsibleTeacher,
	    Date evaluationDate, MarkSheetType markSheetType, MarkSheetState markSheetState, Boolean submittedByTeacher,
	    Employee employee) {

	setMarkSheetState(markSheetState);
	setCurricularCourse(curricularCourse);
	setExecutionPeriod(executionSemester);
	setResponsibleTeacher(responsibleTeacher);
	setEvaluationDate(evaluationDate);
	setMarkSheetType(markSheetType);
	setSubmittedByTeacher(submittedByTeacher);
	setCreationEmployee(employee);
    }

    @Checked("MarkSheetPredicates.editPredicate")
    private void addEnrolmentEvaluationsWithoutResctrictions(Teacher responsibleTeacher,
	    Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, EnrolmentEvaluationState enrolmentEvaluationState) {

	final ExecutionDegree executionDegree = getExecutionDegree(getCurricularCourse(), getExecutionPeriod());

	for (final MarkSheetEnrolmentEvaluationBean evaluationBean : evaluationBeans) {

	    checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(), executionDegree, getExecutionPeriod(), evaluationBean
		    .getEvaluationDate(), getMarkSheetType());

	    final EnrolmentEvaluation enrolmentEvaluation = evaluationBean.getEnrolment().addNewEnrolmentEvaluation(
		    enrolmentEvaluationState, getMarkSheetType().getEnrolmentEvaluationType(), responsibleTeacher.getPerson(),
		    evaluationBean.getGradeValue(), getCreationDate(), evaluationBean.getEvaluationDate(), getExecutionPeriod(),
		    null);

	    addEnrolmentEvaluations(enrolmentEvaluation);
	}
    }

    @Checked("MarkSheetPredicates.editPredicate")
    private void addEnrolmentEvaluationsWithResctrictions(Teacher responsibleTeacher,
	    Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, EnrolmentEvaluationState enrolmentEvaluationState) {

	final ExecutionDegree executionDegree = getExecutionDegree(getCurricularCourse(), getExecutionPeriod());
	final Set<Enrolment> enrolmentsNotInAnyMarkSheet = getCurricularCourse().getEnrolmentsNotInAnyMarkSheet(
		getMarkSheetType(), getExecutionPeriod());

	Set<Enrolment> notPayedEnrolments = new HashSet<Enrolment>();
	for (final MarkSheetEnrolmentEvaluationBean evaluationBean : evaluationBeans) {

	    if (enrolmentsNotInAnyMarkSheet.contains(evaluationBean.getEnrolment())) {
		try {
		    addEnrolmentEvaluationToMarkSheet(responsibleTeacher, enrolmentEvaluationState, evaluationBean,
			    executionDegree);
		} catch (EnrolmentNotPayedException e) {
		    notPayedEnrolments.add(e.getEnrolment());
		}
	    } else {
		// TODO:
		throw new DomainException("error.markSheet");
	    }
	}
    }

    @Checked("MarkSheetPredicates.editPredicate")
    private void addEnrolmentEvaluationToMarkSheet(Teacher responsibleTeacher, EnrolmentEvaluationState enrolmentEvaluationState,
	    final MarkSheetEnrolmentEvaluationBean evaluationBean, ExecutionDegree executionDegree) {

	checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(), executionDegree, getExecutionPeriod(), evaluationBean
		.getEvaluationDate(), getMarkSheetType());

	EnrolmentEvaluation enrolmentEvaluation = evaluationBean.getEnrolment()
		.getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(enrolmentEvaluationState,
			getMarkSheetType().getEnrolmentEvaluationType());

	if (enrolmentEvaluation == null) {
	    enrolmentEvaluation = evaluationBean.getEnrolment().addNewEnrolmentEvaluation(enrolmentEvaluationState,
		    getMarkSheetType().getEnrolmentEvaluationType(), responsibleTeacher.getPerson(),
		    evaluationBean.getGradeValue(), getCreationDate(), evaluationBean.getEvaluationDate(), getExecutionPeriod(),
		    null);
	} else {
	    enrolmentEvaluation.edit(responsibleTeacher.getPerson(), evaluationBean.getGradeValue(), getCreationDate(),
		    evaluationBean.getEvaluationDate());
	}
	addEnrolmentEvaluations(enrolmentEvaluation);
    }

    protected boolean hasMarkSheetState(MarkSheetState markSheetState) {
	return (getMarkSheetState() == markSheetState);
    }

    public boolean isNotConfirmed() {
	return hasMarkSheetState(MarkSheetState.NOT_CONFIRMED) || hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED);
    }

    public boolean isConfirmed() {
	return hasMarkSheetState(MarkSheetState.CONFIRMED) || hasMarkSheetState(MarkSheetState.RECTIFICATION);
    }

    public boolean isRectification() {
	return hasMarkSheetState(MarkSheetState.RECTIFICATION) || hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED);
    }

    @Checked("MarkSheetPredicates.editPredicate")
    public void editNormal(Teacher responsibleTeacher, Date newEvaluationDate) {

	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");

	} else if (hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED)) {
	    throw new DomainException("error.markSheet.wrong.edit.method");

	} else {
	    checkIfTeacherIsResponsibleOrCoordinator(getCurricularCourse(), getExecutionPeriod(), responsibleTeacher,
		    getMarkSheetType());
	    checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(), getExecutionDegree(getCurricularCourse(),
		    getExecutionPeriod()), getExecutionPeriod(), newEvaluationDate, getMarkSheetType());

	    Date oldEvaluationDate = getEvaluationDateDateTime().toDate();
	    setResponsibleTeacher(responsibleTeacher);
	    setEvaluationDate(newEvaluationDate);

	    editMarkSheetEnrolmentEvaluationsWithSameEvaluationDate(responsibleTeacher, oldEvaluationDate, newEvaluationDate,
		    getEnrolmentEvaluationsSet());

	    generateCheckSum();
	}
    }

    @Checked("MarkSheetPredicates.editPredicate")
    public void editRectification(MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean) {

	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");

	} else if (hasMarkSheetState(MarkSheetState.NOT_CONFIRMED)) {
	    throw new DomainException("error.markSheet.wrong.edit.method");

	} else if (enrolmentEvaluationBean == null) {
	    throw new DomainException("error.markSheet.edit.with.invalid.enrolmentEvaluations.number");

	} else {
	    editEnrolmentEvaluations(Collections.singletonList(enrolmentEvaluationBean));
	    generateCheckSum();
	}
    }

    @Checked("MarkSheetPredicates.editPredicate")
    private void editMarkSheetEnrolmentEvaluationsWithSameEvaluationDate(Teacher responsibleTeacher, Date oldEvaluationDate,
	    Date newEvaluationDate, Set<EnrolmentEvaluation> enrolmentEvaluationsToEdit) {

	String dateFormat = "dd/MM/yyyy";
	final ExecutionDegree executionDegree = getExecutionDegree(getCurricularCourse(), getExecutionPeriod());
	for (EnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluationsToEdit) {
	    if (DateFormatUtil.compareDates(dateFormat, enrolmentEvaluation.getExamDate(), oldEvaluationDate) == 0) {
		checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(), executionDegree, getExecutionPeriod(),
			newEvaluationDate, getMarkSheetType());
		enrolmentEvaluation.edit(responsibleTeacher.getPerson(), newEvaluationDate);
	    }
	}
    }

    @Checked("MarkSheetPredicates.editPredicate")
    public void editNormal(Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToEdit,
	    Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToAppend,
	    Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToRemove) {

	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");

	} else if (hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED)) {
	    throw new DomainException("error.markSheet.wrong.edit.method");

	} else {
	    checkIfEnrolmentEvaluationsNumberIsValid(evaluationBeansToAppend, evaluationBeansToRemove);

	    editEnrolmentEvaluations(evaluationBeansToEdit);
	    removeEnrolmentEvaluations(evaluationBeansToRemove);
	    appendEnrolmentEvaluations(evaluationBeansToAppend);

	    generateCheckSum();
	}
    }

    private void checkIfEnrolmentEvaluationsNumberIsValid(Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToAppend,
	    Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToRemove) {

	if (evaluationBeansToAppend.size() == 0 && evaluationBeansToRemove.size() == getEnrolmentEvaluationsCount()) {
	    throw new DomainException("error.markSheet.edit.with.invalid.enrolmentEvaluations.number");
	}
    }

    @Checked("MarkSheetPredicates.editPredicate")
    protected void editEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToEdit) {

	final ExecutionDegree executionDegree = getExecutionDegree(getCurricularCourse(), getExecutionPeriod());

	for (final MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean : evaluationBeansToEdit) {

	    if (this.getEnrolmentEvaluationsSet().contains(enrolmentEvaluationBean.getEnrolmentEvaluation())) {

		checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(), executionDegree, getExecutionPeriod(),
			enrolmentEvaluationBean.getEvaluationDate(), getMarkSheetType());

		final EnrolmentEvaluation enrolmentEvaluation = enrolmentEvaluationBean.getEnrolmentEvaluation();
		enrolmentEvaluation.edit(getResponsibleTeacher().getPerson(), enrolmentEvaluationBean.getGradeValue(),
			new Date(), enrolmentEvaluationBean.getEvaluationDate());
	    } else {
		// TODO:
		throw new DomainException("error.markSheet");
	    }
	}
    }

    @Checked("MarkSheetPredicates.editPredicate")
    private void removeEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToRemove) {
	for (MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean : enrolmentEvaluationBeansToRemove) {
	    enrolmentEvaluationBean.getEnrolmentEvaluation().removeFromMarkSheet();
	    enrolmentEvaluationBean.getEnrolmentEvaluation().setGrade(Grade.createEmptyGrade());
	}
    }

    protected void appendEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToAppend) {
	addEnrolmentEvaluationsWithResctrictions(getResponsibleTeacher(), evaluationBeansToAppend,
		EnrolmentEvaluationState.TEMPORARY_OBJ);
    }

    @Checked("MarkSheetPredicates.confirmPredicate")
    public void confirm(Employee employee) {
	if (employee == null) {
	    throw new DomainException("error.markSheet.invalid.arguments");
	}
	if (isNotConfirmed()) {
	    setConfirmationEmployee(employee);

	    Set<Enrolment> inDebtEnrolments = new HashSet<Enrolment>();
	    for (final EnrolmentEvaluation enrolmentEvaluation : this.getEnrolmentEvaluationsSet()) {
		try {
		    enrolmentEvaluation.confirmSubmission(getEnrolmentEvaluationStateToConfirm(), employee, "");
		} catch (EnrolmentNotPayedException e) {
		    inDebtEnrolments.add(e.getEnrolment());
		}
	    }

	    if (!inDebtEnrolments.isEmpty()) {
		throw new InDebtEnrolmentsException("EnrolmentEvaluation.cannot.set.grade.on.not.payed.enrolment.evaluation",
			inDebtEnrolments);
	    }

	    setConfirmationDateDateTime(new DateTime());
	    setMarkSheetState(getMarkSheetStateToConfirm());

	} else {
	    throw new DomainException("error.markSheet.already.confirmed");
	}
    }

    protected MarkSheetState getMarkSheetStateToConfirm() {
	if (this.getMarkSheetState() == MarkSheetState.NOT_CONFIRMED) {
	    return MarkSheetState.CONFIRMED;
	} else {
	    return MarkSheetState.RECTIFICATION;
	}
    }

    protected EnrolmentEvaluationState getEnrolmentEvaluationStateToConfirm() {
	if (this.getMarkSheetState() == MarkSheetState.NOT_CONFIRMED) {
	    return EnrolmentEvaluationState.FINAL_OBJ;
	} else {
	    return EnrolmentEvaluationState.RECTIFICATION_OBJ;
	}
    }

    public boolean getCanBeDeleted() {
	return isNotConfirmed();
    }

    public void delete() {
	if (!getCanBeDeleted()) {
	    throw new DomainException("error.markSheet.cannot.be.deleted");
	}

	removeExecutionPeriod();
	removeCurricularCourse();
	removeResponsibleTeacher();
	removeConfirmationEmployee();
	removeCreationEmployee();

	if (hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED)) {
	    changeRectifiedEnrolmentEvaluationToPreviowsState();
	    for (; !getEnrolmentEvaluations().isEmpty(); getEnrolmentEvaluations().get(0).delete())
		;
	} else {
	    for (; !getEnrolmentEvaluations().isEmpty(); getEnrolmentEvaluations().get(0).removeFromMarkSheet())
		;
	}

	removeRootDomainObject();
	deleteDomainObject();
    }

    private void changeRectifiedEnrolmentEvaluationToPreviowsState() {

	/*
	 * This is not common, but if by any reason the rectified marksheet was
	 * removed from system and rectified enrolment evaluation was removed
	 * too, no enrolment evaluation will be available
	 */
	if (!hasAnyEnrolmentEvaluations()) {
	    return;
	}

	EnrolmentEvaluation enrolmentEvaluation = this.getEnrolmentEvaluations().get(0).getRectified();
	enrolmentEvaluation
		.setEnrolmentEvaluationState((enrolmentEvaluation.getMarkSheet().getMarkSheetState() == MarkSheetState.RECTIFICATION) ? EnrolmentEvaluationState.RECTIFICATION_OBJ
			: EnrolmentEvaluationState.FINAL_OBJ);
    }

    protected void generateCheckSum() {
	if (isNotConfirmed()) {
	    StringBuilder stringBuilder = new StringBuilder();
	    stringBuilder.append(getExecutionPeriod().getExecutionYear().getYear()).append(getExecutionPeriod().getSemester());
	    stringBuilder.append(getResponsibleTeacher().getPerson().getIstUsername()).append(
		    getEvaluationDateDateTime().toString("yyyy/MM/dd"));
	    stringBuilder.append(getMarkSheetType().getName());
	    for (EnrolmentEvaluation enrolmentEvaluation : getEnrolmentEvaluationsSortedByStudentNumber()) {
		stringBuilder.append(enrolmentEvaluation.getCheckSum());
	    }
	    setCheckSum(FenixDigestUtils.createDigest(stringBuilder.toString()));
	}
    }

    public Set<EnrolmentEvaluation> getEnrolmentEvaluationsSortedByStudentNumber() {
	final Set<EnrolmentEvaluation> enrolmentEvaluations = new TreeSet<EnrolmentEvaluation>(
		EnrolmentEvaluation.SORT_BY_STUDENT_NUMBER);
	enrolmentEvaluations.addAll(getEnrolmentEvaluationsSet());
	return enrolmentEvaluations;
    }

    public EnrolmentEvaluation getEnrolmentEvaluationByStudent(Student student) {
	for (EnrolmentEvaluation enrolmentEvaluation : this.getEnrolmentEvaluationsSet()) {
	    if (enrolmentEvaluation.getEnrolment().getStudentCurricularPlan().getRegistration().getStudent().equals(student)) {
		return enrolmentEvaluation;
	    }
	}
	return null;
    }

    /*
     * Override: Getters and Setters
     */

    @Override
    public void addEnrolmentEvaluations(EnrolmentEvaluation enrolmentEvaluations) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.addEnrolmentEvaluations(enrolmentEvaluations);
	}
    }

    @Override
    public void removeCurricularCourse() {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.removeCurricularCourse();
	}
    }

    @Override
    public void removeConfirmationEmployee() {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.removeConfirmationEmployee();
	}
    }

    @Override
    public void removeCreationEmployee() {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.removeCreationEmployee();
	}
    }

    @Override
    public void removeEnrolmentEvaluations(EnrolmentEvaluation enrolmentEvaluations) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.removeEnrolmentEvaluations(enrolmentEvaluations);
	}
    }

    @Override
    public void removeExecutionPeriod() {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.removeExecutionPeriod();
	}
    }

    @Override
    public void removeResponsibleTeacher() {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.removeResponsibleTeacher();
	}
    }

    @Override
    public void removeRootDomainObject() {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.removeRootDomainObject();
	}
    }

    @Override
    public void setCheckSum(String checkSum) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.setCheckSum(checkSum);
	}
    }

    public void setConfirmationDate(Date confirmationDate) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    if (confirmationDate == null)
		setConfirmationDateDateTime(null);
	    else
		setConfirmationDateDateTime(new org.joda.time.DateTime(confirmationDate.getTime()));
	}
    }
    
    public void setCreationDate(Date creationDate) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    if (creationDate == null)
		setCreationDateDateTime(null);
	    else
		setCreationDateDateTime(new org.joda.time.DateTime(creationDate.getTime()));
	}
    }

    @Override
    public void setCurricularCourse(CurricularCourse curricularCourse) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.setCurricularCourse(curricularCourse);
	}
    }

    @Override
    public void setConfirmationEmployee(Employee confirmationEmployee) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.setConfirmationEmployee(confirmationEmployee);
	}
    }

    @Override
    public void setCreationEmployee(Employee creationEmployee) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.setCreationEmployee(creationEmployee);
	}
    }

    public void setEvaluationDate(Date evaluationDate) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    if (evaluationDate == null)
		setEvaluationDateDateTime(null);
	    else
		setEvaluationDateDateTime(new org.joda.time.DateTime(evaluationDate.getTime()));
	}
    }
    
    @Override
    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.setExecutionPeriod(executionSemester);
	}
    }

    @Override
    public void setMarkSheetState(MarkSheetState markSheetState) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.setMarkSheetState(markSheetState);
	}
    }

    @Override
    public void setMarkSheetType(MarkSheetType markSheetType) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.setMarkSheetType(markSheetType);
	}
    }

    @Override
    public void setResponsibleTeacher(Teacher responsibleTeacher) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.setResponsibleTeacher(responsibleTeacher);
	}
    }

    @Override
    public void setConfirmationDateDateTime(DateTime confirmationDateDateTime) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.setConfirmationDateDateTime(confirmationDateDateTime);
	}
    }

    @Override
    public void setCreationDateDateTime(DateTime creationDateDateTime) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.setCreationDateDateTime(creationDateDateTime);
	}
    }

    @Override
    public void setEvaluationDateDateTime(DateTime evaluationDateDateTime) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.setEvaluationDateDateTime(evaluationDateDateTime);
	}
    }

    @Override
    public void setReason(String reason) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.setReason(reason);
	}
    }

    @Override
    public void setSubmittedByTeacher(Boolean submittedByTeacher) {
	if (isConfirmed()) {
	    throw new DomainException("error.markSheet.already.confirmed");
	} else {
	    super.setSubmittedByTeacher(submittedByTeacher);
	}
    }

    public String getPrettyCheckSum() {
	return FenixDigestUtils.getPrettyCheckSum(getCheckSum());
    }

    public boolean canManage(final Employee employee) {
	final DegreeCurricularPlan degreeCurricularPlan = getCurricularCourse().getDegreeCurricularPlan();
	return degreeCurricularPlan.canManageMarkSheetsForExecutionPeriod(employee, getExecutionPeriod());
    }

    public AdministrativeOfficeType getAdministrativeOfficeType() {
	return getCurricularCourse().getDegreeType().getAdministrativeOfficeType();
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void removeGrades(Collection<EnrolmentEvaluation> enrolmentEvaluations) {
	if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
	    if (getMarkSheetState() == MarkSheetState.CONFIRMED) {
		super.setMarkSheetState(MarkSheetState.NOT_CONFIRMED);
	    }
	    if (getMarkSheetState() == MarkSheetState.RECTIFICATION) {
		super.setMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED);
	    }

	    setConfirmationEmployee(null);
	    setConfirmationDateDateTime(null);

	    for (EnrolmentEvaluation enrolmentEvaluation : getEnrolmentEvaluationsSet()) {
		if (enrolmentEvaluations.contains(enrolmentEvaluation)) {
		    if (enrolmentEvaluation.hasRectification()) {
			throw new DomainException("error.enrolment.evaluation.has.rectification");
		    }
		    removeEvaluationFromMarkSheet(enrolmentEvaluation);
		} else {
		    changeEvaluationStateToTemporaryState(enrolmentEvaluation);
		}
	    }
	    generateCheckSum();
	}
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    private void removeEvaluationFromMarkSheet(EnrolmentEvaluation enrolmentEvaluation) {
	changeEvaluationStateToTemporaryState(enrolmentEvaluation);
	enrolmentEvaluation.removeMarkSheet();
	enrolmentEvaluation.setGrade(Grade.createEmptyGrade());
	enrolmentEvaluation.setCheckSum(null);
	enrolmentEvaluation.setExamDateYearMonthDay(null);
	enrolmentEvaluation.removeEmployee();
	enrolmentEvaluation.setGradeAvailableDateYearMonthDay(null);
	enrolmentEvaluation.removePersonResponsibleForGrade();
	enrolmentEvaluation.removeRectified();
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    private void changeEvaluationStateToTemporaryState(final EnrolmentEvaluation enrolmentEvaluation) {
	enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);

	Enrolment enrolment = enrolmentEvaluation.getEnrolment();

	if (enrolment.getAllFinalEnrolmentEvaluations().isEmpty()) {
	    enrolment.setEnrollmentState(EnrollmentState.ENROLLED);
	} else {
	    enrolment.setEnrollmentState(enrolment.getLatestEnrolmentEvaluation().getEnrollmentStateByGrade());
	}
    }

    public boolean getCanRectify() {
	return isConfirmed() && MarkSheetPredicates.rectifyPredicate.evaluate(this);
    }

    public boolean getCanConfirm() {
	return isNotConfirmed() && MarkSheetPredicates.confirmPredicate.evaluate(this);
    }

    public boolean getCanEdit() {
	return isNotConfirmed() && MarkSheetPredicates.editPredicate.evaluate(this);
    }

    public boolean isDissertation() {
	return getCurricularCourse().isDissertation();
    }

    public String getStateDiscription() {
	StringBuilder stringBuilder = new StringBuilder();
	final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources", Language
		.getLocale());
	stringBuilder.append(enumerationResources.getString(getMarkSheetState().getName()).trim());
	if (getSubmittedByTeacher()) {
	    final ResourceBundle academicResources = ResourceBundle.getBundle("resources.AcademicAdminOffice", Language
		    .getLocale());
	    stringBuilder.append(" (").append(academicResources.getString("label.markSheet.submittedByTeacher").trim()).append(
		    ")");
	}
	return stringBuilder.toString();
    }

    public String getDegreeName() {
	return getCurricularCourse().getDegree().getNameFor(getExecutionPeriod()).getContent();
    }

    public String getDegreeCurricularPlanName() {
	return getCurricularCourse().getDegreeCurricularPlan().getName();
    }

    public String getCurricularCourseName() {
	return getCurricularCourse().getName(getExecutionPeriod());
    }

    public String getCurricularCourseAcronym() {
	return getCurricularCourse().getAcronym(getExecutionPeriod());
    }

    public boolean isFor(AdministrativeOffice administrativeOffice) {
	return getAdministrativeOfficeType().equals(administrativeOffice.getAdministrativeOfficeType());
    }

    public boolean isFor(DegreeCurricularPlan dcp) {
	return getCurricularCourse().getDegreeCurricularPlan().equals(dcp);
    }


	@Deprecated
	public java.util.Date getConfirmationDate(){
		org.joda.time.DateTime dt = getConfirmationDateDateTime();
		return (dt == null) ? null : new java.util.Date(dt.getMillis());
	}

	@Deprecated
	public java.util.Date getCreationDate(){
		org.joda.time.DateTime dt = getCreationDateDateTime();
		return (dt == null) ? null : new java.util.Date(dt.getMillis());
	}

	@Deprecated
	public java.util.Date getEvaluationDate(){
		org.joda.time.DateTime dt = getEvaluationDateDateTime();
		return (dt == null) ? null : new java.util.Date(dt.getMillis());
	}


}
