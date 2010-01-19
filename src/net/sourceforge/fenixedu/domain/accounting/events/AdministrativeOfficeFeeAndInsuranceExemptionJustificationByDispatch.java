package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class AdministrativeOfficeFeeAndInsuranceExemptionJustificationByDispatch extends
	AdministrativeOfficeFeeAndInsuranceExemptionJustificationByDispatch_Base {

    protected AdministrativeOfficeFeeAndInsuranceExemptionJustificationByDispatch() {
	super();
    }

    public AdministrativeOfficeFeeAndInsuranceExemptionJustificationByDispatch(
	    final AdministrativeOfficeFeeAndInsuranceExemption exemption,
	    final AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType, final String reason,
	    final YearMonthDay dispatchDate) {
	this();
	init(exemption, justificationType, reason, dispatchDate);

    }

    private void init(AdministrativeOfficeFeeAndInsuranceExemption exemption,
	    AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType, String reason,
	    YearMonthDay dispatchDate) {
	checkParameters(justificationType, reason, dispatchDate);

	super.init(exemption, justificationType, reason);

	super.setDispatchDate(dispatchDate);

    }

    private void checkParameters(AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType, String reason,
	    YearMonthDay dispatchDate) {
	if (dispatchDate == null || StringUtils.isEmpty(reason)) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustificationByDispatch.dispatchDate.and.reason.are.required",
		    new LabelFormatter(justificationType.getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES));
	}
    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(getJustificationType().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" (").appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES).appendLabel(" ")
		.appendLabel(getDispatchDate().toString(DateFormatUtil.DEFAULT_DATE_FORMAT)).appendLabel(")");

	return labelFormatter;
    }

}
