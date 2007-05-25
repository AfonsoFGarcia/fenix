package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DegreeCurricularPlanEquivalencePlan extends DegreeCurricularPlanEquivalencePlan_Base {

    public DegreeCurricularPlanEquivalencePlan(final DegreeCurricularPlan degreeCurricularPlan,
	    final DegreeCurricularPlan sourceDegreeCurricularPlan) {
	super();
	init(degreeCurricularPlan, sourceDegreeCurricularPlan);
    }

    protected void init(DegreeCurricularPlan degreeCurricularPlan,
	    DegreeCurricularPlan sourceDegreeCurricularPlan) {
	checkParameters(degreeCurricularPlan, sourceDegreeCurricularPlan);

	super.setDegreeCurricularPlan(degreeCurricularPlan);
	super.setSourceDegreeCurricularPlan(sourceDegreeCurricularPlan);

    }

    private void checkParameters(DegreeCurricularPlan degreeCurricularPlan,
	    DegreeCurricularPlan sourceDegreeCurricularPlan) {
	if (degreeCurricularPlan == null) {
	    throw new DomainException(
		    "error.DegreeCurricularPlanEquivalencePlan.degreeCurricularPlan.cannot.be.null");
	}

	if (sourceDegreeCurricularPlan == null) {
	    throw new DomainException(
		    "error.DegreeCurricularPlanEquivalencePlan.sourceDegreeCurricularPlan.cannot.be.null");
	}

    }

}
