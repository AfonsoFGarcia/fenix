package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ImprovementOfApprovedEnrolment;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

public class ImprovementOfApprovedEnrolmentExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
	return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	final ImprovementOfApprovedEnrolment improvementOfApprovedEnrolment = (ImprovementOfApprovedEnrolment) curricularRule;
	final Enrolment enrolment = improvementOfApprovedEnrolment.getEnrolment();
	final DegreeModule degreeModule = enrolment.getDegreeModule();

	if (enrolment.hasImprovement()) {
	    return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.ImprovementOfApprovedEnrolmentExecutor.already.enroled.in.improvement",
		    degreeModule.getName());
	}

	if (!enrolment.isApproved()) {
	    return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
		    "curricularRules.ruleExecutors.ImprovementOfApprovedEnrolmentExecutor.degree.module.hasnt.been.approved",
		    degreeModule.getName());
	}

	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();

	if (!executionPeriod.isOneYearAfter(enrolment.getExecutionPeriod())) {
	    if (!degreeModule.hasAnyParentContexts(executionPeriod)) {
		return RuleResult
			.createFalse(
				sourceDegreeModuleToEvaluate.getDegreeModule(),
				"curricularRules.ruleExecutors.ImprovementOfApprovedEnrolmentExecutor.degree.module.has.no.context.in.present.execution.period",
				degreeModule.getName(), executionPeriod.getQualifiedName());
	    }

	    if (!enrolment.isImprovingInExecutionPeriodFollowingApproval(executionPeriod)) {
		return RuleResult
			.createFalse(
				sourceDegreeModuleToEvaluate.getDegreeModule(),
				"curricularRules.ruleExecutors.ImprovementOfApprovedEnrolmentExecutor.is.not.improving.in.execution.period.following.approval",
				degreeModule.getName());
	    }
	}

	return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

}
