package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class DegreeModulesSelectionLimitExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule,
	    final EnrolmentContext enrolmentContext) {

	final DegreeModulesSelectionLimit rule = (DegreeModulesSelectionLimit) curricularRule;

	if (ruleWasSelectedFromAnyModuleToEnrol(enrolmentContext, curricularRule)) {
	    return RuleResult.createNA();
	}

	final CourseGroup courseGroup = rule.getDegreeModuleToApplyRule();
	final CurriculumGroup curriculumGroup = (CurriculumGroup) searchCurriculumModule(enrolmentContext, rule);
	final CourseGroup parentCourseGroup = curriculumGroup.getCurriculumGroup().getDegreeModule();
	
	if (rule.appliesToCourseGroup(parentCourseGroup)) {
	    int total = countTotalDegreeModules(enrolmentContext, courseGroup, curriculumGroup);
	    return rule.numberOfDegreeModulesExceedMaximum(total) ? createFalseRuleResult(rule) : RuleResult.createTrue();
	}

	return RuleResult.createNA();

    }
    
    private int countTotalDegreeModules(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup, final CurriculumGroup curriculumGroup) {
	
	int numberOfDegreeModulesToEnrol = countNumberOfDegreeModulesToEnrol(enrolmentContext, courseGroup);
	int numberOfApprovedEnrolments = curriculumGroup.getNumberOfApprovedEnrolments();
	int numberOfEnrolments = curriculumGroup.getNumberOfEnrolments(enrolmentContext.getExecutionPeriod());
	int numberOfChildCurriculumGroups = curriculumGroup.getNumberOfChildCurriculumGroupsWithCourseGroup();
	
	return numberOfApprovedEnrolments + numberOfEnrolments + numberOfDegreeModulesToEnrol + numberOfChildCurriculumGroups;
    }

    private RuleResult createFalseRuleResult(final DegreeModulesSelectionLimit rule) {
	if (rule.getMinimumLimit().equals(rule.getMaximumLimit())) {
	    return RuleResult.createFalse(
		    "curricularRules.ruleExecutors.DegreeModulesSelectionLimitExecutor.limit.exceded", 
		    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumLimit().toString());
	} else {
	    return RuleResult.createFalse(
		    "curricularRules.ruleExecutors.DegreeModulesSelectionLimitExecutor.limits.exceded", 
		    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumLimit().toString(), rule.getMaximumLimit().toString());
	}
    }

    private int countNumberOfDegreeModulesToEnrol(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup) {
	int result = 0;
	for (final Context context : courseGroup.getChildContexts(enrolmentContext.getExecutionPeriod())) {
	    if (isEnrolling(enrolmentContext, context.getChildDegreeModule())) {
		result++;
	    }
	}
	return result;
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final DegreeModulesSelectionLimit rule = (DegreeModulesSelectionLimit) curricularRule;
	
	if (ruleWasSelectedFromAnyModuleToEnrol(enrolmentContext, curricularRule)) {
	    return RuleResult.createNA();
	}
	
	if (appliesToCourseGroup(enrolmentContext,rule)) {
	    
	    final CourseGroup courseGroup = rule.getDegreeModuleToApplyRule();
	    final CurriculumGroup curriculumGroup = (CurriculumGroup) searchCurriculumModule(enrolmentContext, rule);

	    int total = countTotalDegreeModules(enrolmentContext, courseGroup, curriculumGroup);

	    if (rule.numberOfDegreeModulesExceedMaximum(total)) {
		return createFalseRuleResult(rule);
	    }
	    
	    final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	    total += curriculumGroup.getNumberOfEnrolments(executionPeriod.getPreviousExecutionPeriod());
	    return rule.numberOfDegreeModulesExceedMaximum(total) ? RuleResult.createTrue(EnrolmentResultType.TEMPORARY) : RuleResult.createTrue();
	}
	
	return RuleResult.createNA();
    }

    @Override
    protected RuleResult executeEnrolmentWithNoRules(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }
}
