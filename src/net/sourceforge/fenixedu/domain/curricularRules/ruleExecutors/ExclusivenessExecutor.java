package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.Exclusiveness;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class ExclusivenessExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeWithRules(CurricularRule curricularRule, EnrolmentContext enrolmentContext) {
	
	final Exclusiveness rule = (Exclusiveness) curricularRule;
	final DegreeModuleToEnrol moduleToEnrol = searchDegreeModuleToEnrol(enrolmentContext, rule.getDegreeModuleToApplyRule());
	
	if (!rule.appliesToContext(moduleToEnrol.getContext())) {
	    return RuleResult.createNA();
	}
	
	final DegreeModule degreeModule = rule.getExclusiveDegreeModule();
	
	if (isToEnrol(enrolmentContext, degreeModule) || isEnroled(enrolmentContext, degreeModule)) {
	    return RuleResult
		.createFalse(
		   "curricularRules.ruleExecutors.ExclusivenessExecutor.exclusive.degreeModule",
		   rule.getDegreeModuleToApplyRule().getName(), rule.getExclusiveDegreeModule().getName());
	}
	
	if (degreeModule.isLeaf() && isApproved(enrolmentContext, (CurricularCourse) degreeModule)) {
	    return RuleResult
		.createFalse(
		   "curricularRules.ruleExecutors.ExclusivenessExecutor.exclusive.degreeModule",
		   rule.getDegreeModuleToApplyRule().getName(), rule.getExclusiveDegreeModule().getName());
	}
	
	return RuleResult.createTrue();
    }
    
    @Override
    protected RuleResult executeWithRulesAndTemporaryEnrolment(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return executeWithRules(curricularRule, enrolmentContext);
    }
    
    @Override
    protected RuleResult executeNoRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }

}
