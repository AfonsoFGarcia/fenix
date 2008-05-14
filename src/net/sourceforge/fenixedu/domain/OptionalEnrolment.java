package net.sourceforge.fenixedu.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.enrolment.EnroledOptionalEnrolment;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.EnrolmentAction;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class OptionalEnrolment extends OptionalEnrolment_Base {

    protected OptionalEnrolment() {
	super();
    }

    public OptionalEnrolment(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    CurricularCourse curricularCourse, ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition,
	    String createdBy, OptionalCurricularCourse optionalCurricularCourse) {
	if (studentCurricularPlan == null || curriculumGroup == null || curricularCourse == null || executionSemester == null
		|| enrolmentCondition == null || createdBy == null || optionalCurricularCourse == null) {
	    throw new DomainException("invalid arguments");
	}
	checkInitConstraints(studentCurricularPlan, curricularCourse, executionSemester, optionalCurricularCourse);
	// TODO: check this
	// validateDegreeModuleLink(curriculumGroup, curricularCourse);
	initializeAsNew(studentCurricularPlan, curriculumGroup, curricularCourse, executionSemester, enrolmentCondition, createdBy);
	setOptionalCurricularCourse(optionalCurricularCourse);
    }

    protected void checkInitConstraints(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
	    ExecutionSemester executionSemester, OptionalCurricularCourse optionalCurricularCourse) {
	super.checkInitConstraints(studentCurricularPlan, curricularCourse, executionSemester);

	final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) studentCurricularPlan.findEnrolmentFor(
		optionalCurricularCourse, executionSemester);
	if (optionalEnrolment != null && optionalEnrolment.isValid(executionSemester)) {
	    throw new DomainException("error.OptionalEnrolment.duplicate.enrolment", optionalCurricularCourse.getName());

	}
    }

    @Override
    final public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
	if (executionSemester == null || getExecutionPeriod().isBeforeOrEquals(executionSemester)) {
	    return isApproved() && hasCurricularCourseOrOptionalCurricularCourse(curricularCourse, executionSemester);
	} else {
	    return false;
	}
    }

    private boolean hasCurricularCourseOrOptionalCurricularCourse(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
	return hasCurricularCourse(getCurricularCourse(), curricularCourse, executionSemester) 
		|| hasCurricularCourse(getOptionalCurricularCourse(), curricularCourse, executionSemester);
    }

    @Override
    final public boolean isEnroledInExecutionPeriod(CurricularCourse curricularCourse, ExecutionSemester executionSemester) {
	return this.getExecutionPeriod().equals(executionSemester)
		&& (this.getCurricularCourse().equals(curricularCourse) || this.getOptionalCurricularCourse().equals(
			curricularCourse));
    }

    @Override
    public boolean isOptional() {
	return true;
    }

    @Override
    public MultiLanguageString getName() {
	ExecutionSemester executionSemester = getExecutionPeriod();
	return MultiLanguageString.i18n().add("pt", this.getOptionalCurricularCourse().getName(executionSemester)).add("en",
		this.getOptionalCurricularCourse().getNameEn(executionSemester)).finish();
    }

    @Override
    public boolean hasDegreeModule(final DegreeModule degreeModule) {
	return super.hasDegreeModule(degreeModule) || hasOptionalCurricularCourse(degreeModule);
    }

    private boolean hasOptionalCurricularCourse(final DegreeModule degreeModule) {
	return getOptionalCurricularCourse() == degreeModule;
    }

    @Override
    final public void delete() {
	removeOptionalCurricularCourse();
	super.delete();
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(ExecutionSemester executionSemester) {
	if (isValid(executionSemester) && isEnroled()) {
	    final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>(1);
	    result.add(new EnroledOptionalEnrolment(this, getOptionalCurricularCourse(), executionSemester));
	    return result;
	}
	return Collections.emptySet();

    }

    /**
     * 
     * After create new OptionalEnrolment, must delete Enrolment
     * (to delete Enrolment remove: ProgramCertificateRequests, CourseLoadRequests, ExamDateCertificateRequests)
     * @param enrolment 
     * @param curriculumGroup: new CurriculumGroup for OptionalEnrolment
     * @param optionalCurricularCourse: choosed OptionalCurricularCourse
     * @return OptionalEnrolment
     */
    static OptionalEnrolment createBasedOn(final Enrolment enrolment, final CurriculumGroup curriculumGroup,
	    final OptionalCurricularCourse optionalCurricularCourse) {
	checkParameters(enrolment, curriculumGroup, optionalCurricularCourse);

	final OptionalEnrolment optionalEnrolment = new OptionalEnrolment();
	optionalEnrolment.setCurricularCourse(enrolment.getCurricularCourse());
	optionalEnrolment.setWeigth(enrolment.getWeigth());
	optionalEnrolment.setEnrollmentState(enrolment.getEnrollmentState());
	optionalEnrolment.setExecutionPeriod(enrolment.getExecutionPeriod());
	optionalEnrolment.setEnrolmentEvaluationType(enrolment.getEnrolmentEvaluationType());
	optionalEnrolment.setCreatedBy(AccessControl.getUserView().getUtilizador());
	optionalEnrolment.setCreationDateDateTime(enrolment.getCreationDateDateTime());
	optionalEnrolment.setEnrolmentCondition(enrolment.getEnrolmentCondition());
	optionalEnrolment.setCurriculumGroup(curriculumGroup);
	optionalEnrolment.setOptionalCurricularCourse(optionalCurricularCourse);

	optionalEnrolment.getEvaluations().addAll(enrolment.getEvaluations());
	optionalEnrolment.getProgramCertificateRequests().addAll(enrolment.getProgramCertificateRequests());
	optionalEnrolment.getCourseLoadRequests().addAll(enrolment.getCourseLoadRequests());
	optionalEnrolment.getExtraExamRequests().addAll(enrolment.getExtraExamRequests());
	optionalEnrolment.getEnrolmentWrappers().addAll(enrolment.getEnrolmentWrappers());
	optionalEnrolment.getTheses().addAll(enrolment.getTheses());
	optionalEnrolment.getExamDateCertificateRequests().addAll(enrolment.getExamDateCertificateRequests());
	optionalEnrolment.getAttends().addAll(enrolment.getAttends());
	optionalEnrolment.createEnrolmentLog(EnrolmentAction.ENROL);

	return optionalEnrolment;
    }

    private static void checkParameters(final Enrolment enrolment, final CurriculumGroup curriculumGroup,
	    final OptionalCurricularCourse optionalCurricularCourse) {
	if (enrolment == null || enrolment.isOptional()) {
	    throw new DomainException("error.OptionalEnrolment.invalid.enrolment");
	}
	if (curriculumGroup == null) {
	    throw new DomainException("error.OptionalEnrolment.invalid.curriculumGroup");
	}
	if (optionalCurricularCourse == null) {
	    throw new DomainException("error.OptionalEnrolment.invalid.optional.curricularCourse");
	}
    }
}
