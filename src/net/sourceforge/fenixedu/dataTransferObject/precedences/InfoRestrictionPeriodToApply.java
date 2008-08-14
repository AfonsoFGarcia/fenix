package net.sourceforge.fenixedu.dataTransferObject.precedences;

import net.sourceforge.fenixedu.domain.precedences.RestrictionPeriodToApply;
import net.sourceforge.fenixedu.util.PeriodToApplyRestriction;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoRestrictionPeriodToApply extends InfoRestriction {

    protected PeriodToApplyRestriction periodToApplyRestriction;

    public InfoRestrictionPeriodToApply() {
    }

    public PeriodToApplyRestriction getPeriodToApplyRestriction() {
	return periodToApplyRestriction;
    }

    public void setPeriodToApplyRestriction(PeriodToApplyRestriction periodToApplyRestriction) {
	this.periodToApplyRestriction = periodToApplyRestriction;
    }

    public void copyFromDomain(RestrictionPeriodToApply restriction) {
	super.copyFromDomain(restriction);
	this.setPeriodToApplyRestriction(restriction.getPeriodToApplyRestriction());
	this.setRestrictionKindResourceKey("label.manager.restrictionPeriodToApply");
    }

    public static InfoRestrictionPeriodToApply newInfoFromDomain(RestrictionPeriodToApply restriction) {

	InfoRestrictionPeriodToApply infoRestriction = null;

	if (restriction != null) {
	    infoRestriction = new InfoRestrictionPeriodToApply();
	    infoRestriction.copyFromDomain(restriction);
	}

	return infoRestriction;
    }

    public String getArg() {
	return String.valueOf(periodToApplyRestriction.getValue());
    }
}