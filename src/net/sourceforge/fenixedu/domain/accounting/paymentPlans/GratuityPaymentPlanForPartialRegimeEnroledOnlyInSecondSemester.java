package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.HasEnrolmentsOnlyInSecondSemesterPaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.IsPartialRegimePaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRuleFactory;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;

public class GratuityPaymentPlanForPartialRegimeEnroledOnlyInSecondSemester extends
	GratuityPaymentPlanForPartialRegimeEnroledOnlyInSecondSemester_Base {

    protected GratuityPaymentPlanForPartialRegimeEnroledOnlyInSecondSemester() {
	super();
    }

    public GratuityPaymentPlanForPartialRegimeEnroledOnlyInSecondSemester(final ExecutionYear executionYear,
	    final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate, final Boolean defaultPaymentPlan) {
	this();
	init(executionYear, serviceAgreementTemplate, defaultPaymentPlan);
    }

    @Override
    protected Collection<PaymentPlanRule> getSpecificPaymentPlanRules() {
	return Arrays.asList(

	PaymentPlanRuleFactory.create(IsPartialRegimePaymentPlanRule.class),

	PaymentPlanRuleFactory.create(HasEnrolmentsOnlyInSecondSemesterPaymentPlanRule.class)

	);
    }

    @Override
    public boolean isForPartialRegime() {
	return true;
    }
}
