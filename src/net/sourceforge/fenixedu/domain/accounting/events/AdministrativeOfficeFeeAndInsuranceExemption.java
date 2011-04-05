package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.AdministrativeOfficeFeeAndInsuranceExemptionJustificationFactory;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import dml.runtime.RelationAdapter;

public class AdministrativeOfficeFeeAndInsuranceExemption extends AdministrativeOfficeFeeAndInsuranceExemption_Base {

    static {
	ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {
	    @Override
	    public void beforeAdd(Exemption exemption, Event event) {

		if (exemption instanceof AdministrativeOfficeFeeAndInsuranceExemption && event != null) {
		    final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent = (AdministrativeOfficeFeeAndInsuranceEvent) event;
		    if (administrativeOfficeFeeAndInsuranceEvent.hasAdministrativeOfficeFeeAndInsuranceExemption()) {
			throw new DomainException(
				"error.net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemption.event.already.has.exemption");

		    }
		}
	    }
	});
    }

    protected AdministrativeOfficeFeeAndInsuranceExemption() {
	super();
    }

    public AdministrativeOfficeFeeAndInsuranceExemption(Employee employee,
	    AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent,
	    AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType, String reason,
	    YearMonthDay dispatchDate) {
	this();

	super.init(employee, administrativeOfficeFeeAndInsuranceEvent,
		AdministrativeOfficeFeeAndInsuranceExemptionJustificationFactory.create(this, justificationType, reason,
			dispatchDate));

	administrativeOfficeFeeAndInsuranceEvent.recalculateState(new DateTime());
    }

    public boolean isAdministrativeOfficeFeeAndInsuranceExemption() {
	return true;
    }
}
