package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.utl.ist.fenix.tools.predicates.InlinePredicate;

public class OldMarkSheet extends OldMarkSheet_Base {

    protected OldMarkSheet() {
	super();
    }

    public OldMarkSheet(CurricularCourse curricularCourse, ExecutionSemester executionSemester, Teacher responsibleTeacher,
	    Date evaluationDate, MarkSheetType markSheetType, MarkSheetState markSheetState,
	    Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Employee employee) {
	this();
	checkParameters(curricularCourse, executionSemester, responsibleTeacher, evaluationDate, markSheetType, markSheetState,
		evaluationBeans, employee);
	init(curricularCourse, executionSemester, responsibleTeacher, evaluationDate, markSheetType, markSheetState,
		Boolean.FALSE, employee);

	for (MarkSheetEnrolmentEvaluationBean evaluationBean : evaluationBeans) {
	    addEnrolmentEvaluationToMarkSheet(responsibleTeacher, evaluationBean);
	}

	generateCheckSum();

    }

    @Checked("MarkSheetPredicates.editPredicate")
    private void addEnrolmentEvaluationToMarkSheet(Teacher responsibleTeacher,
	    final MarkSheetEnrolmentEvaluationBean evaluationBean) {

	EnrolmentEvaluation enrolmentEvaluation = evaluationBean.getEnrolment()
		.getEnrolmentEvaluation(
			new InlinePredicate<EnrolmentEvaluation, EnrolmentEvaluationType>(getMarkSheetType()
				.getEnrolmentEvaluationType()) {

			    @Override
			    public boolean eval(EnrolmentEvaluation ee) {
				return ee.getEnrolmentEvaluationType() == getValue()
					&& (ee.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ) || ee
						.isNotEvaluated());
			    }

			});

	if (enrolmentEvaluation == null) {
	    enrolmentEvaluation = evaluationBean.getEnrolment().addNewEnrolmentEvaluation(EnrolmentEvaluationState.TEMPORARY_OBJ,
		    getMarkSheetType().getEnrolmentEvaluationType(), responsibleTeacher.getPerson(),
		    evaluationBean.getGradeValue(), getCreationDate(), evaluationBean.getEvaluationDate(), getExecutionPeriod(),
		    null);
	} else {
	    enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
	    enrolmentEvaluation.edit(responsibleTeacher.getPerson(), evaluationBean.getGradeValue(), getCreationDate(),
		    evaluationBean.getEvaluationDate());
	}
	addEnrolmentEvaluations(enrolmentEvaluation);
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
    }

    @Override
    @Checked("MarkSheetPredicates.confirmPredicate")
    public void confirm(Employee employee) {
	if (employee == null) {
	    throw new DomainException("error.markSheet.invalid.arguments");
	}
	if (isNotConfirmed()) {
	    setConfirmationEmployee(employee);

	    for (final EnrolmentEvaluation enrolmentEvaluation : this.getEnrolmentEvaluationsSet()) {
		enrolmentEvaluation.confirmSubmission(getEnrolmentEvaluationStateToConfirm(), employee, "");
	    }

	    setConfirmationDateDateTime(new DateTime());
	    setMarkSheetState(getMarkSheetStateToConfirm());

	} else {
	    throw new DomainException("error.markSheet.already.confirmed");
	}
    }

    @Override
    protected void appendEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans) {
	for (MarkSheetEnrolmentEvaluationBean evaluationBean : evaluationBeans) {
	    addEnrolmentEvaluationToMarkSheet(getResponsibleTeacher(), evaluationBean);
	}
    }

    @Checked("MarkSheetPredicates.editPredicate")
    protected void editEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeansToEdit) {

	for (final MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean : evaluationBeansToEdit) {

	    if (this.getEnrolmentEvaluationsSet().contains(enrolmentEvaluationBean.getEnrolmentEvaluation())) {

		final EnrolmentEvaluation enrolmentEvaluation = enrolmentEvaluationBean.getEnrolmentEvaluation();
		enrolmentEvaluation.edit(getResponsibleTeacher().getPerson(), enrolmentEvaluationBean.getGradeValue(),
			new Date(), enrolmentEvaluationBean.getEvaluationDate());
	    } else {
		// TODO:
		throw new DomainException("error.markSheet");
	    }
	}
    }

    @Override
    protected void checkIfEvaluationDateIsInExamsPeriod(CurricularCourse curricularCourse, ExecutionDegree executionDegree,
	    ExecutionSemester executionSemester, Date evaluationDate, MarkSheetType markSheetType) throws DomainException {
    }

    @Override
    protected void checkIfTeacherIsResponsibleOrCoordinator(CurricularCourse curricularCourse,
	    ExecutionSemester executionSemester, Teacher responsibleTeacher, MarkSheetType markSheetType) throws DomainException {
    }
}
