package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

import dml.runtime.RelationAdapter;

public class AdministrativeOfficeFeeAndInsurancePenaltyExemption extends AdministrativeOfficeFeeAndInsurancePenaltyExemption_Base {

    static {
	ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {
	    @Override
	    public void beforeAdd(Exemption exemption, Event event) {
		if (exemption != null && event != null) {
		    if (exemption instanceof AdministrativeOfficeFeeAndInsurancePenaltyExemption) {
			final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent = ((AdministrativeOfficeFeeAndInsuranceEvent) event);
			if (administrativeOfficeFeeAndInsuranceEvent.hasAdministrativeOfficeFeeAndInsuranceExemption()) {
			    throw new DomainException(
				    "error.accounting.events.AdministrativeOfficeFeeAndInsuranceExemption.event.already.has.exemption.for.fee.and.insurance");
			}

		    }
		}
	    }
	});
    }

    protected AdministrativeOfficeFeeAndInsurancePenaltyExemption() {
	super();
    }

    public AdministrativeOfficeFeeAndInsurancePenaltyExemption(final PenaltyExemptionJustificationType penaltyExemptionType,
	    final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent, final Employee employee,
	    final String reason, final YearMonthDay directiveCouncilDispatchDate) {
	this();
	super
		.init(penaltyExemptionType, administrativeOfficeFeeAndInsuranceEvent, employee, reason,
			directiveCouncilDispatchDate);
    }

}
