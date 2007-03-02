package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.Exclusiveness;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

public class ExclusivenessExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final Exclusiveness rule = (Exclusiveness) curricularRule;

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA();
	}

	final DegreeModule degreeModule = rule.getExclusiveDegreeModule();

	if (degreeModule.isLeaf()) {
	    final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
	    final ExecutionPeriod previousExecutionPeriod = enrolmentContext.getExecutionPeriod().getPreviousExecutionPeriod();

	    if (isApproved(enrolmentContext, curricularCourse)
		    || hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, previousExecutionPeriod)) {

		if (isEnroled(enrolmentContext, (CurricularCourse) rule.getDegreeModuleToApplyRule(),
			enrolmentContext.getExecutionPeriod())) {
		    return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE);
		}
		return createFalseRuleResult(rule);
	    }
	}

	if (isEnroled(enrolmentContext, degreeModule) || isEnrolling(enrolmentContext, degreeModule)) {
	    return createFalseRuleResult(rule);
	}

	return RuleResult.createTrue();
    }

    private RuleResult createFalseRuleResult(final Exclusiveness rule) {
	return RuleResult.createFalse(
		"curricularRules.ruleExecutors.ExclusivenessExecutor.exclusive.degreeModule", rule
			.getDegreeModuleToApplyRule().getName(), rule.getExclusiveDegreeModule()
			.getName());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	final Exclusiveness rule = (Exclusiveness) curricularRule;

	if (!canApplyRule(enrolmentContext, rule)) {
	    return RuleResult.createNA();
	}

	final DegreeModule degreeModule = rule.getExclusiveDegreeModule();
	if (degreeModule.isLeaf()) {
	    final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;

	    if (isApproved(enrolmentContext, curricularCourse)) {
		if (isEnroled(enrolmentContext, (CurricularCourse) rule.getDegreeModuleToApplyRule(), enrolmentContext.getExecutionPeriod())) {
		    return RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE);
		}
		return createFalseRuleResult(rule);
	    }

	    final ExecutionPeriod previousExecutionPeriod = enrolmentContext.getExecutionPeriod().getPreviousExecutionPeriod();
	    if (hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, previousExecutionPeriod)) {
		return RuleResult.createTrue(EnrolmentResultType.TEMPORARY);
	    }
	}

	if (isEnroled(enrolmentContext, degreeModule) || isEnrolling(enrolmentContext, degreeModule)) {
	    return createFalseRuleResult(rule);
	}

	return RuleResult.createTrue();
    }

}
