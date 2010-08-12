package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
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

public class StudentCurricularPlanEnrolmentManager extends StudentCurricularPlanEnrolment {

    public StudentCurricularPlanEnrolmentManager(final EnrolmentContext enrolmentContext) {
	super(enrolmentContext);
    }

    @Override
    protected void assertEnrolmentPreConditions() {
	if (!isResponsiblePersonManager() && !getRegistration().isRegistered(getExecutionSemester())) {
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
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : getStudentCurricularPlan().getDegreeModulesToEvaluate(
		getExecutionSemester())) {
	    enrolmentContext.addDegreeModuleToEvaluate(degreeModuleToEvaluate);
	}
    }

    @Override
    protected Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate() {
	final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> result = new HashMap<IDegreeModuleToEvaluate, Set<ICurricularRule>>();

	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {

	    if (degreeModuleToEvaluate.canCollectRules()) {

		final Set<ICurricularRule> curricularRules = new HashSet<ICurricularRule>();
		curricularRules.addAll(degreeModuleToEvaluate.getCurricularRulesFromDegreeModule(getExecutionSemester()));
		curricularRules.addAll(degreeModuleToEvaluate.getCurricularRulesFromCurriculumGroup(getExecutionSemester()));

		if (degreeModuleToEvaluate.isLeaf()) {
		    final CurricularCourse curricularCourse = (CurricularCourse) degreeModuleToEvaluate.getDegreeModule();
		    addRuntimeRules(curricularRules, curricularCourse);
		}
		result.put(degreeModuleToEvaluate, curricularRules);
	    }
	}

	return result;
    }

    protected void addRuntimeRules(final Set<ICurricularRule> curricularRules, final CurricularCourse curricularCourse) {
	curricularRules.add(new AssertUniqueApprovalInCurricularCourseContexts(curricularCourse));
    }

    @Override
    protected void performEnrolments(final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesEnrolMap) {
	final String createdBy = getResponsiblePerson().getIstUsername();
	for (final Entry<EnrolmentResultType, List<IDegreeModuleToEvaluate>> entry : degreeModulesEnrolMap.entrySet()) {

	    if (entry.getKey() == EnrolmentResultType.NULL) {
		continue;
	    }

	    for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : entry.getValue()) {

		if (degreeModuleToEvaluate.isEnroled()) {

		    final EnroledCurriculumModuleWrapper moduleEnroledWrapper = (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;

		    if (moduleEnroledWrapper.getCurriculumModule() instanceof Enrolment) {
			final Enrolment enrolment = (Enrolment) moduleEnroledWrapper.getCurriculumModule();
			enrolment.setEnrolmentCondition(getEnrolmentCondition(enrolment, entry.getKey()));
		    }
		} else {

		    final DegreeModule degreeModule = degreeModuleToEvaluate.getDegreeModule();
		    final CurriculumGroup curriculumGroup = degreeModuleToEvaluate.getCurriculumGroup();

		    if (degreeModule.isLeaf()) {
			if (degreeModuleToEvaluate.isOptional()) {
			    createOptionalEnrolmentFor(getEnrolmentCondition(null, entry.getKey()), degreeModuleToEvaluate,
				    curriculumGroup);

			} else {
			    new Enrolment(getStudentCurricularPlan(), curriculumGroup, (CurricularCourse) degreeModule,
				    getExecutionSemester(), getEnrolmentCondition(null, entry.getKey()), createdBy);
			}

		    } else {
			CurriculumGroupFactory.createGroup(degreeModuleToEvaluate.getCurriculumGroup(),
				(CourseGroup) degreeModule, getExecutionSemester());
		    }
		}
	    }
	}
    }

    protected EnrollmentCondition getEnrolmentCondition(final Enrolment enrolment, final EnrolmentResultType enrolmentResultType) {
	return enrolmentResultType.getEnrollmentCondition();
    }

    private void createOptionalEnrolmentFor(final EnrollmentCondition enrollmentCondition,
	    final IDegreeModuleToEvaluate degreeModuleToEvaluate, final CurriculumGroup curriculumGroup) {

	final OptionalDegreeModuleToEnrol optionalDegreeModuleToEnrol = (OptionalDegreeModuleToEnrol) degreeModuleToEvaluate;
	final OptionalCurricularCourse optionalCurricularCourse = (OptionalCurricularCourse) optionalDegreeModuleToEnrol
		.getDegreeModule();
	final CurricularCourse curricularCourse = optionalDegreeModuleToEnrol.getCurricularCourse();

	getStudentCurricularPlan().createOptionalEnrolment(curriculumGroup, getExecutionSemester(), optionalCurricularCourse,
		curricularCourse, enrollmentCondition);
    }

    @Override
    protected RuleResult evaluateExtraRules(RuleResult actualResult) {
	if (actualResult.isFalse() || !getDegreeCurricularPlan().isToApplyPreviousYearsEnrolmentRule()) {
	    return actualResult;
	}

	RuleResult finalResult = RuleResult.createInitialTrue();
	if (!getRoot().hasExternalCycles()) {
	    final PreviousYearsEnrolmentCurricularRule previousYearsEnrolmentCurricularRule = new PreviousYearsEnrolmentCurricularRule(
		    getRoot().getDegreeModule());
	    finalResult = finalResult.and(previousYearsEnrolmentCurricularRule.evaluate(new EnroledCurriculumModuleWrapper(
		    getRoot(), getExecutionSemester()), this.enrolmentContext));

	} else {
	    for (final CycleCurriculumGroup cycleCurriculumGroup : getRoot().getCycleCurriculumGroups()) {
		final PreviousYearsEnrolmentCurricularRule previousYearsEnrolmentCurricularRule = new PreviousYearsEnrolmentCurricularRule(
			cycleCurriculumGroup.getDegreeModule());
		finalResult = finalResult.and(previousYearsEnrolmentCurricularRule.evaluate(new EnroledCurriculumModuleWrapper(
			cycleCurriculumGroup, getExecutionSemester()), this.enrolmentContext));
	    }
	}

	return finalResult.and(actualResult);
    }

}
