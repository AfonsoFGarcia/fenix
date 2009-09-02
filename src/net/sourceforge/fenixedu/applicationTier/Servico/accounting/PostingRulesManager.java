package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateDFAGratuityPostingRuleBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateGratuityPostingRuleBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateStandaloneEnrolmentGratuityPRBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByNumberOfEnrolmentsPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.GratuityWithPaymentPlanPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class PostingRulesManager {

    @Service
    @Checked("RolePredicates.MANAGER_PREDICATE")
    static public void createGraduationGratuityPostingRule(final CreateGratuityPostingRuleBean bean) {

	if (bean.getRule() == GratuityWithPaymentPlanPR.class) {
	    for (final DegreeCurricularPlan degreeCurricularPlan : bean.getDegreeCurricularPlans()) {
		final ServiceAgreementTemplate serviceAgreementTemplate = degreeCurricularPlan.getServiceAgreementTemplate();
		deactivateExistingPostingRule(EventType.GRATUITY, bean.getStartDate(), serviceAgreementTemplate);
		new GratuityWithPaymentPlanPR(bean.getStartDate(), null, serviceAgreementTemplate);
	    }
	} else {
	    throw new RuntimeException("Unexpected rule type for gratuity posting rule");
	}

    }

    @Service
    @Checked("RolePredicates.MANAGER_PREDICATE")
    static public void createStandaloneGraduationGratuityPostingRule(final CreateStandaloneEnrolmentGratuityPRBean bean) {

	if (bean.getRule() == StandaloneEnrolmentGratuityPR.class) {
	    for (final DegreeCurricularPlan degreeCurricularPlan : bean.getDegreeCurricularPlans()) {
		final ServiceAgreementTemplate serviceAgreementTemplate = degreeCurricularPlan.getServiceAgreementTemplate();
		deactivateExistingPostingRule(EventType.STANDALONE_ENROLMENT_GRATUITY, bean.getStartDate(),
			serviceAgreementTemplate);
		new StandaloneEnrolmentGratuityPR(bean.getStartDate(), null, serviceAgreementTemplate, bean.getEctsForYear(),
			bean.getGratuityFactor(), bean.getEctsFactor());
	    }
	} else {
	    throw new RuntimeException("Unexpected rule type for gratuity posting rule");
	}

    }

    private static void deactivateExistingPostingRule(final EventType eventType, final DateTime when,
	    final ServiceAgreementTemplate serviceAgreementTemplate) {
	if (!serviceAgreementTemplate.hasPostingRuleFor(eventType, when)) {
	    return;
	}

	final PostingRule existingPostingRule = serviceAgreementTemplate.findPostingRuleByEventTypeAndDate(eventType, when);

	if (existingPostingRule != null) {
	    existingPostingRule.deactivate(when);
	}
    }

    @Service
    @Checked("RolePredicates.MANAGER_PREDICATE")
    static public void createDFAGratuityPostingRule(final CreateDFAGratuityPostingRuleBean bean) {
	if (bean.getRule() == DFAGratuityByAmountPerEctsPR.class) {
	    new DFAGratuityByAmountPerEctsPR(bean.getStartDate(), null, bean.getServiceAgreementTemplate(),
		    bean.getTotalAmount(), bean.getPartialAcceptedPercentage(), bean.getAmountPerEctsCredit());
	} else if (bean.getRule() == DFAGratuityByNumberOfEnrolmentsPR.class) {
	    new DFAGratuityByNumberOfEnrolmentsPR(bean.getStartDate(), null, bean.getServiceAgreementTemplate(), bean
		    .getTotalAmount(), bean.getPartialAcceptedPercentage());
	} else {
	    throw new RuntimeException("Unexpected rule type for DFA gratuity posting rule");
	}
    }

    @Service
    @Checked("RolePredicates.MANAGER_PREDICATE")
    static public void deletePostingRule(final PostingRule postingRule) {
	postingRule.delete();
    }

}
