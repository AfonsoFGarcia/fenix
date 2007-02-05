package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionDoneDegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class RestrictionDoneDegreeModuleExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeWithRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {

	final RestrictionDoneDegreeModule rule = (RestrictionDoneDegreeModule) curricularRule;
	final DegreeModuleToEnrol moduleToEnrol = searchDegreeModuleToEnrol(enrolmentContext, rule.getDegreeModuleToApplyRule());
	
	if (!rule.appliesToContext(moduleToEnrol.getContext())) {
	    return RuleResult.createNA();
	}

	if (!isApproved(enrolmentContext, (CurricularCourse) rule.getPrecedenceDegreeModule())) {
	    return RuleResult
		    .createFalse(
			    "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.student.is.not.approved.to.precendenceDegreeModule",
			    rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
	}

	return RuleResult.createTrue();
    }

    @Override
    protected RuleResult executeWithRulesAndTemporaryEnrolment(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final RestrictionDoneDegreeModule rule = (RestrictionDoneDegreeModule) curricularRule;
	final DegreeModuleToEnrol moduleToEnrol = searchDegreeModuleToEnrol(enrolmentContext, rule.getDegreeModuleToApplyRule());
	
	if (!rule.appliesToContext(moduleToEnrol.getContext())) {
	    return RuleResult.createNA();
	}

	final CurricularCourse curricularCourse = (CurricularCourse) rule.getPrecedenceDegreeModule();
	if (!isApproved(enrolmentContext, curricularCourse)) {
	    
	    final ExecutionPeriod previous = enrolmentContext.getExecutionPeriod().getPreviousExecutionPeriod();
	    if (isEnroled(enrolmentContext, curricularCourse, previous)) {
		return RuleResult.createTrue(EnrolmentResultType.TEMPORARY);
	    }
	    return RuleResult
		    .createFalse(
			    "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.student.is.not.approved.to.precendenceDegreeModule",
			    rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
	}

	return RuleResult.createTrue();
    }
    
    @Override
    protected RuleResult executeNoRules(CurricularRule curricularRule, EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }
}
