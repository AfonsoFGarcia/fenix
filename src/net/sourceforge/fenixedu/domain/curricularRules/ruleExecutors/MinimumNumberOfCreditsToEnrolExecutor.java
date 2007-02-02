package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.MinimumNumberOfCreditsToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class MinimumNumberOfCreditsToEnrolExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeWithRules(CurricularRule curricularRule, EnrolmentContext enrolmentContext) {
	
	final MinimumNumberOfCreditsToEnrol rule = (MinimumNumberOfCreditsToEnrol) curricularRule;
	final DegreeModuleToEnrol moduleToEnrol = getDegreeModuleToEnrol(enrolmentContext, rule.getDegreeModuleToApplyRule());

	if (!rule.appliesToContext(moduleToEnrol.getContext())) {
	    return RuleResult.createNA();
	}
	
	final StudentCurricularPlan studentCurricularPlan = enrolmentContext.getStudentCurricularPlan();
	final Double ectsCredits = studentCurricularPlan.getRoot().getEctsCredits();
	
	if (rule.allowCredits(ectsCredits)) {
	    return RuleResult.createTrue();
	}
	
	return RuleResult.createFalse(
		"curricularRules.ruleExecutors.MinimumNumberOfCreditsToEnrolExecutor.student.has.not.minimum.number.of.credits", 
			ectsCredits.toString(), rule.getMinimumCredits().toString(), rule.getDegreeModuleToApplyRule().getName());
    }

    @Override
    protected RuleResult executeWithRulesAndTemporaryEnrolment(CurricularRule curricularRule, EnrolmentContext enrolmentContext) {
	return executeWithRules(curricularRule, enrolmentContext);
    }

    @Override
    protected RuleResult executeNoRules(CurricularRule curricularRule, EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }
    
}
