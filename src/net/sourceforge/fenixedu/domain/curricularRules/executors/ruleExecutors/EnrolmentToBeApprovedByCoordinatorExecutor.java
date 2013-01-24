package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class EnrolmentToBeApprovedByCoordinatorExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {

	final Person responsiblePerson = enrolmentContext.getResponsiblePerson();
	if (AcademicAuthorizationGroup.getProgramsForOperation(responsiblePerson, AcademicOperationType.STUDENT_ENROLMENTS)
		.contains(enrolmentContext.getStudentCurricularPlan().getDegree()) || responsiblePerson.hasRole(RoleType.MANAGER)) {
	    return RuleResult
		    .createWarning(
			    sourceDegreeModuleToEvaluate.getDegreeModule(),
			    "curricularRules.ruleExecutors.EnrolmentToBeApprovedByCoordinatorExecutor.degree.module.needs.aproval.by.coordinator",
			    sourceDegreeModuleToEvaluate.getName());
	}

	if (sourceDegreeModuleToEvaluate.isEnroled()) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	return RuleResult
		.createFalse(
			sourceDegreeModuleToEvaluate.getDegreeModule(),
			"curricularRules.ruleExecutors.EnrolmentToBeApprovedByCoordinatorExecutor.degree.module.needs.aproval.by.coordinator",
			sourceDegreeModuleToEvaluate.getName());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
	    EnrolmentContext enrolmentContext) {
	return true;
    }

}
