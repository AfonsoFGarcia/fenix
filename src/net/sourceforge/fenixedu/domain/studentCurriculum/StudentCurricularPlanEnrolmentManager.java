package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.AssertUniqueApprovalInCurricularCourseContexts;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.PreviousYearsEnrolmentCurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.enrolment.EnroledCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class StudentCurricularPlanEnrolmentManager extends StudentCurricularPlanEnrolment {

    public StudentCurricularPlanEnrolmentManager(final StudentCurricularPlan plan, final EnrolmentContext enrolmentContext) {
	super(plan, enrolmentContext);
    }

    @Override
    protected void assertEnrolmentPreConditions() {
	final Registration registration = studentCurricularPlan.getRegistration();

	if (!responsiblePerson.hasRole(RoleType.MANAGER) && !registration.isInRegisteredState(executionSemester)) {
	    throw new DomainException("error.StudentCurricularPlan.cannot.enrol.with.registration.inactive");
	}

	super.assertEnrolmentPreConditions();
    }

    @Override
    protected void unEnrol() {
	// First remove Enrolments
	for (final CurriculumModule curriculumModule : enrolmentContext.getToRemove()) {
	    if (curriculumModule.isLeaf()) {
		curriculumModule.delete();
	    }
	}

	// After, remove CurriculumGroups and evaluate rules
	for (final CurriculumModule curriculumModule : enrolmentContext.getToRemove()) {
	    if (!curriculumModule.isLeaf()) {
		curriculumModule.delete();
	    }
	}
    }

    @Override
    protected void addEnroled() {
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : studentCurricularPlan
		.getDegreeModulesToEvaluate(executionSemester)) {
	    enrolmentContext.addDegreeModuleToEvaluate(degreeModuleToEvaluate);
	}
    }

    @Override
    protected Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate() {
	final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> result = new HashMap<IDegreeModuleToEvaluate, Set<ICurricularRule>>();

	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {

	    if (degreeModuleToEvaluate.canCollectRules()) {

		final Set<ICurricularRule> curricularRules = new HashSet<ICurricularRule>();
		curricularRules.addAll(degreeModuleToEvaluate.getCurricularRulesFromDegreeModule(executionSemester));
		curricularRules.addAll(degreeModuleToEvaluate.getCurricularRulesFromCurriculumGroup(executionSemester));

		if (degreeModuleToEvaluate.isLeaf()) {
		    final CurricularCourse curricularCourse = (CurricularCourse) degreeModuleToEvaluate.getDegreeModule();
		    curricularRules.add(new AssertUniqueApprovalInCurricularCourseContexts(curricularCourse));
		}
		result.put(degreeModuleToEvaluate, curricularRules);
	    }
	}

	return result;
    }

    @Override
    protected void performEnrolments(final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesEnrolMap) {
	final String createdBy = responsiblePerson.getIstUsername();
	for (final Entry<EnrolmentResultType, List<IDegreeModuleToEvaluate>> entry : degreeModulesEnrolMap.entrySet()) {

	    if (entry.getKey() == EnrolmentResultType.NULL) {
		continue;
	    }

	    final EnrollmentCondition enrollmentCondition = entry.getKey().getEnrollmentCondition();
	    for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : entry.getValue()) {

		if (degreeModuleToEvaluate.isEnroled()) {

		    final EnroledCurriculumModuleWrapper moduleEnroledWrapper = (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;

		    if (moduleEnroledWrapper.getCurriculumModule() instanceof Enrolment) {
			final Enrolment enrolment = (Enrolment) moduleEnroledWrapper.getCurriculumModule();
			enrolment.setEnrolmentCondition(enrollmentCondition);
		    }
		} else {

		    final DegreeModule degreeModule = degreeModuleToEvaluate.getDegreeModule();
		    final CurriculumGroup curriculumGroup = degreeModuleToEvaluate.getCurriculumGroup();

		    if (degreeModule.isLeaf()) {
			if (degreeModuleToEvaluate.isOptional()) {
			    createOptionalEnrolmentFor(enrollmentCondition, degreeModuleToEvaluate, curriculumGroup);

			} else {
			    new Enrolment(studentCurricularPlan, curriculumGroup, (CurricularCourse) degreeModule,
				    executionSemester, enrollmentCondition, createdBy);
			}

		    } else if (degreeModule instanceof CycleCourseGroup) {
			new CycleCurriculumGroup((RootCurriculumGroup) degreeModuleToEvaluate.getCurriculumGroup(),
				(CycleCourseGroup) degreeModule, executionSemester);
		    } else {
			new CurriculumGroup(degreeModuleToEvaluate.getCurriculumGroup(), (CourseGroup) degreeModule,
				executionSemester);
		    }
		}
	    }
	}
    }

    private void createOptionalEnrolmentFor(final EnrollmentCondition enrollmentCondition,
	    final IDegreeModuleToEvaluate degreeModuleToEvaluate, final CurriculumGroup curriculumGroup) {

	final OptionalDegreeModuleToEnrol optionalDegreeModuleToEnrol = (OptionalDegreeModuleToEnrol) degreeModuleToEvaluate;
	final OptionalCurricularCourse optionalCurricularCourse = (OptionalCurricularCourse) optionalDegreeModuleToEnrol
		.getDegreeModule();
	final CurricularCourse curricularCourse = optionalDegreeModuleToEnrol.getCurricularCourse();

	enrolmentContext.getStudentCurricularPlan().createOptionalEnrolment(curriculumGroup, executionSemester,
		optionalCurricularCourse, curricularCourse, enrollmentCondition);
    }

    @Override
    protected RuleResult evaluateExtraRules(RuleResult actualResult) {
	if (actualResult.isFalse() || !this.studentCurricularPlan.getDegreeCurricularPlan().isToApplyPreviousYearsEnrolmentRule()) {
	    return actualResult;
	}

	RuleResult finalResult = RuleResult.createInitialTrue();
	if (!this.studentCurricularPlan.getRoot().hasExternalCycles()) {
	    final PreviousYearsEnrolmentCurricularRule previousYearsEnrolmentCurricularRule = new PreviousYearsEnrolmentCurricularRule(
		    this.studentCurricularPlan.getRoot().getDegreeModule());
	    finalResult = finalResult.and(previousYearsEnrolmentCurricularRule.evaluate(new EnroledCurriculumModuleWrapper(
		    this.studentCurricularPlan.getRoot(), this.enrolmentContext.getExecutionPeriod()), this.enrolmentContext));

	} else {
	    for (final CycleCurriculumGroup cycleCurriculumGroup : this.studentCurricularPlan.getRoot()
		    .getCycleCurriculumGroups()) {
		final PreviousYearsEnrolmentCurricularRule previousYearsEnrolmentCurricularRule = new PreviousYearsEnrolmentCurricularRule(
			cycleCurriculumGroup.getDegreeModule());
		finalResult = finalResult.and(previousYearsEnrolmentCurricularRule.evaluate(new EnroledCurriculumModuleWrapper(
			cycleCurriculumGroup, this.enrolmentContext.getExecutionPeriod()), this.enrolmentContext));
	    }
	}

	return finalResult.and(actualResult);
    }

}
