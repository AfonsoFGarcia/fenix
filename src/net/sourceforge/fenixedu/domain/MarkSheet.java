package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.FenixDigestUtils;

public class MarkSheet extends MarkSheet_Base {

    private MarkSheet() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCreationDate(new Date());
    }

    public MarkSheet(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
            Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType,
            MarkSheetState markSheetState,
            Collection<MarkSheetEnrolmentEvaluationBean> markSheetEnrolmentEvaluationBeans) {

        this();
        checkParameters(curricularCourse, executionPeriod, responsibleTeacher, evaluationDate, markSheetType, markSheetState);
        init(curricularCourse, executionPeriod, responsibleTeacher, evaluationDate, markSheetType, markSheetState);

        if (getMarkSheetState() == MarkSheetState.RECTIFICATION) {
            addEnrolmentEvaluationsWithoutResctrictions(responsibleTeacher,
                    markSheetEnrolmentEvaluationBeans, EnrolmentEvaluationState.RECTIFICATION_OBJ);
        } else {
            addEnrolmentEvaluationsWithResctrictions(responsibleTeacher,
                    markSheetEnrolmentEvaluationBeans, EnrolmentEvaluationState.TEMPORARY_OBJ);
        }
        generateCheckSum();
    }

    private void checkParameters(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
            Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType,
            MarkSheetState markSheetState) {

        if (curricularCourse == null || executionPeriod == null || responsibleTeacher == null
                || evaluationDate == null || markSheetType == null || markSheetState == null) {
            throw new DomainException("error.markSheet.invalid.arguments");
        } 
        checkIfTeacherIsResponsibleOrCoordinator(curricularCourse, executionPeriod, responsibleTeacher); 
        checkIfEvaluationDateIsInExamsPeriod(curricularCourse, executionPeriod, evaluationDate, markSheetType);
    }

    private void checkIfTeacherIsResponsibleOrCoordinator(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod, Teacher responsibleTeacher) throws DomainException {
        if (! responsibleTeacher.isResponsibleOrCoordinatorFor(curricularCourse
                .getDegreeCurricularPlan(), curricularCourse, executionPeriod)) {
            throw new DomainException("error.markSheet.teacher.is.not.responsible.or.coordinator");
        }
    }
    
    private void checkIfEvaluationDateIsInExamsPeriod(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod, Date evaluationDate, MarkSheetType markSheetType) throws DomainException {
        ExecutionDegree executionDegree = curricularCourse.getDegreeCurricularPlan().getExecutionDegreeByYear(executionPeriod.getExecutionYear());
        if (executionDegree == null || !executionDegree.isEvaluationDateInExamPeriod(evaluationDate, executionPeriod, markSheetType)) {
            throw new DomainException("error.evaluationDateNotInExamsPeriod");
        }
    }

    private void init(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
            Teacher responsibleTeacher, Date evaluationDate, MarkSheetType markSheetType,
            MarkSheetState markSheetState) {
        
        setMarkSheetState(markSheetState);
        setCurricularCourse(curricularCourse);
        setExecutionPeriod(executionPeriod);
        setResponsibleTeacher(responsibleTeacher);
        setEvaluationDate(evaluationDate);
        setMarkSheetType(markSheetType);
    }

    private void addEnrolmentEvaluationsWithoutResctrictions(Teacher responsibleTeacher,
            Collection<MarkSheetEnrolmentEvaluationBean> markSheetEnrolmentEvaluationBeans,
            EnrolmentEvaluationState enrolmentEvaluationState) {

        for (final MarkSheetEnrolmentEvaluationBean markSheetEnrolmentEvaluationBean : markSheetEnrolmentEvaluationBeans) {
            addEnrolmentEvaluationToMarkSheet(responsibleTeacher, enrolmentEvaluationState,
                    markSheetEnrolmentEvaluationBean);
        }
    }

    private void addEnrolmentEvaluationsWithResctrictions(Teacher responsibleTeacher,
            Collection<MarkSheetEnrolmentEvaluationBean> markSheetEnrolmentEvaluationBeans,
            EnrolmentEvaluationState enrolmentEvaluationState) {

        final Set<Enrolment> enrolmentsNotInAnyMarkSheet = getCurricularCourse()
                .getEnrolmentsNotInAnyMarkSheet(getMarkSheetType(), getExecutionPeriod());

        for (final MarkSheetEnrolmentEvaluationBean markSheetEnrolmentEvaluationBean : markSheetEnrolmentEvaluationBeans) {
            if (enrolmentsNotInAnyMarkSheet.contains(markSheetEnrolmentEvaluationBean.getEnrolment())) {
                addEnrolmentEvaluationToMarkSheet(responsibleTeacher, enrolmentEvaluationState,
                        markSheetEnrolmentEvaluationBean);
            } else {
                // TODO:
            }
        }
    }

    private void addEnrolmentEvaluationToMarkSheet(Teacher responsibleTeacher,
            EnrolmentEvaluationState enrolmentEvaluationState,
            final MarkSheetEnrolmentEvaluationBean markSheetEnrolmentEvaluationBean) {

        EnrolmentEvaluation enrolmentEvaluation = markSheetEnrolmentEvaluationBean.getEnrolment()
                .getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(enrolmentEvaluationState,
                        getMarkSheetType().getEnrolmentEvaluationType());

        if (enrolmentEvaluation == null) {
            enrolmentEvaluation = markSheetEnrolmentEvaluationBean.getEnrolment()
                    .addNewEnrolmentEvaluation(enrolmentEvaluationState,
                            getMarkSheetType().getEnrolmentEvaluationType(),
                            responsibleTeacher.getPerson(), markSheetEnrolmentEvaluationBean.getGrade(),
                            getCreationDate(), markSheetEnrolmentEvaluationBean.getEvaluationDate());
        } else {
            enrolmentEvaluation
                    .edit(responsibleTeacher.getPerson(), markSheetEnrolmentEvaluationBean.getGrade(),
                            getCreationDate(), markSheetEnrolmentEvaluationBean.getEvaluationDate());
        }
        addEnrolmentEvaluations(enrolmentEvaluation);
    }

    private boolean hasMarkSheetState(MarkSheetState markSheetState) {
        return (getMarkSheetState() == markSheetState);
    }

    public boolean isNotConfirmed() {
        return hasMarkSheetState(MarkSheetState.NOT_CONFIRMED) || hasMarkSheetState(MarkSheetState.RECTIFICATION_NOT_CONFIRMED);
    }

    public boolean isConfirmed() {
        return hasMarkSheetState(MarkSheetState.CONFIRMED) || hasMarkSheetState(MarkSheetState.RECTIFICATION);
    }
    
    public void edit(Teacher responsibleTeacher, Date newEvaluationDate) {
        if (isNotConfirmed()) {

            checkIfTeacherIsResponsibleOrCoordinator(getCurricularCourse(), getExecutionPeriod(), responsibleTeacher);
            checkIfEvaluationDateIsInExamsPeriod(getCurricularCourse(), getExecutionPeriod(), newEvaluationDate, getMarkSheetType());
            
            Date oldEvaluationDate = getEvaluationDate();
            setResponsibleTeacher(responsibleTeacher);
            setEvaluationDate(newEvaluationDate);
            
            editEnrolmentEvaluationsWithSameMarkSheetEvaluationDate(responsibleTeacher, oldEvaluationDate, newEvaluationDate, getEnrolmentEvaluationsSet());
            generateCheckSum();
            
        } else {
            throw new DomainException("error.markSheet.already.confirmed");
        }
    }

    private void editEnrolmentEvaluationsWithSameMarkSheetEvaluationDate(Teacher responsibleTeacher,
            Date oldEvaluationDate, Date newEvaluationDate,
            Set<EnrolmentEvaluation> enrolmentEvaluationsToEdit) {
        
        String dateFormat = "dd/MM/yyyy";
        for (EnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluationsToEdit) {
            if (DateFormatUtil.compareDates(dateFormat, enrolmentEvaluation.getExamDate(), oldEvaluationDate) == 0) {
                enrolmentEvaluation.edit(responsibleTeacher.getPerson(), newEvaluationDate);
            }
        }
    }
    
    public void edit(Collection<MarkSheetEnrolmentEvaluationBean> markSheetEnrolmentEvaluationBeansToEdit,
            Collection<MarkSheetEnrolmentEvaluationBean> markSheetEnrolmentEvaluationBeansToAppend, Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToRemove) {
        
        if (isNotConfirmed()) {

            editEnrolmentEvaluations(markSheetEnrolmentEvaluationBeansToEdit);
            removeEnrolmentEvaluations(enrolmentEvaluationBeansToRemove);
            appendEnrolmentEvaluations(markSheetEnrolmentEvaluationBeansToAppend);
            
            generateCheckSum();
            
        } else {
            throw new DomainException("error.markSheet.already.confirmed");
        }
    }
    
    private void editEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> markSheetEnrolmentEvaluationBeansToEdit) {
        
        for (final MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean : markSheetEnrolmentEvaluationBeansToEdit) {

            if (this.getEnrolmentEvaluationsSet().contains(enrolmentEvaluationBean.getEnrolmentEvaluation())) {
                final EnrolmentEvaluation enrolmentEvaluation = enrolmentEvaluationBean.getEnrolmentEvaluation();
                enrolmentEvaluation.edit(getResponsibleTeacher().getPerson(),
                        enrolmentEvaluationBean.getGrade(), new Date(),
                        enrolmentEvaluationBean.getEvaluationDate());
            } else {
                // TODO:
            }
        }
    }
    
    private void removeEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToRemove) {
        for (MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean : enrolmentEvaluationBeansToRemove) {
            enrolmentEvaluationBean.getEnrolmentEvaluation().delete();
        }
    }

    private void appendEnrolmentEvaluations(Collection<MarkSheetEnrolmentEvaluationBean> markSheetEnrolmentEvaluationBeansToAppend) {
        addEnrolmentEvaluationsWithResctrictions(getResponsibleTeacher(),
                markSheetEnrolmentEvaluationBeansToAppend, EnrolmentEvaluationState.TEMPORARY_OBJ);
    }

    public void confirm(Employee employee) {
        if (isNotConfirmed()) {
            setEmployee(employee);
            for (final EnrolmentEvaluation enrolmentEvaluation : this.getEnrolmentEvaluationsSet()) {
                enrolmentEvaluation.confirmSubmission(employee, "");
            }
            setConfirmationDate(new Date());
            setMarkSheetState(MarkSheetState.CONFIRMED);
        }
    }

    public boolean getCanBeDeleted() {
        return isNotConfirmed();
    }

    public void delete() {
        if (!getCanBeDeleted()) {
            throw new DomainException("error.markSheet.cannot.be.deleted");
        }
        removeCurricularCourse();
        removeResponsibleTeacher();
        removeEmployee();
        for (; ! getEnrolmentEvaluations().isEmpty(); getEnrolmentEvaluations().get(0).delete());
        removeRootDomainObject();
        deleteDomainObject();
    }

    protected void generateCheckSum() {
        if (isNotConfirmed()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getExecutionPeriod().getExecutionYear().getYear()).append(
                    getExecutionPeriod().getSemester());
            stringBuilder.append(getResponsibleTeacher().getTeacherNumber()).append(
                    DateFormatUtil.format("yyyy/MM/dd", getEvaluationDate()));
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
    public void removeEmployee() {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.removeEmployee();
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

    @Override
    public void setConfirmationDate(Date confirmationDate) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setConfirmationDate(confirmationDate);
        }
    }

    @Override
    public void setCreationDate(Date creationDate) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setCreationDate(creationDate);
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
    public void setEmployee(Employee employee) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setEmployee(employee);
        }
    }

    @Override
    public void setEvaluationDate(Date evaluationDate) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setEvaluationDate(evaluationDate);
        }
    }

    @Override
    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
        if (isConfirmed()) {
            throw new DomainException("error.markSheet.already.confirmed");
        } else {
            super.setExecutionPeriod(executionPeriod);
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

}
