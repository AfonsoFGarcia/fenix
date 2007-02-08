package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionBetweenDegreeModules;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class RestrictionBetweenDegreeModulesExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final RestrictionBetweenDegreeModules rule = (RestrictionBetweenDegreeModules) curricularRule;
	final DegreeModuleToEnrol moduleToEnrol = searchDegreeModuleToEnrol(enrolmentContext, rule.getDegreeModuleToApplyRule());
	
	if (!rule.appliesToContext(moduleToEnrol.getContext())) {
	    return RuleResult.createNA();
	}
	
	final DegreeModule degreeModule = rule.getPrecedenceDegreeModule();
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	if (isEnrolling(enrolmentContext, degreeModule) || isEnroled(enrolmentContext, degreeModule)) {

	    if (!rule.hasMinimumCredits() || rule.allowCredits(degreeModule.getEctsCredits(executionPeriod))) {
		return RuleResult.createTrue();

	    } else {
		return RuleResult.createFalse(
			"curricularRules.ruleExecutors.RestrictionBetweenDegreeModulesExecutor.invalid.ects.credits.in.precedence.degreeModule",
			rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName(), rule.getMinimumCredits().toString());
	    }
	}
	
	return RuleResult.createFalse(
		"curricularRules.ruleExecutors.RestrictionBetweenDegreeModulesExecutor.student.has.not.precedence.degreeModule", rule
			.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return executeEnrolmentWithRules(curricularRule, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentWithNoRules(CurricularRule curricularRule, EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }
}
