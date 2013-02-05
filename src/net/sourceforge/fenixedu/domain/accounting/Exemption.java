package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.events.ExemptionJustification;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import dml.runtime.RelationAdapter;

public abstract class Exemption extends Exemption_Base {

    static {
        ExemptionEvent.addListener(new RelationAdapter<Exemption, Event>() {

            @Override
            public void beforeAdd(Exemption exemption, Event event) {
                if (event != null && !event.isExemptionAppliable()) {
                    throw new DomainException("error.accounting.Exemption.event.does.not.support.exemption");
                }
            }
        });

    }

    protected Exemption() {
        super();
        super.setRootDomainObject(RootDomainObject.getInstance());
        super.setWhenCreated(new DateTime());
    }

    protected void init(final Person responsible, final Event event, final ExemptionJustification exemptionJustification) {
        checkParameters(event, exemptionJustification);
        super.setResponsible(responsible);
        super.setEvent(event);
        super.setExemptionJustification(exemptionJustification);
    }

    private void checkParameters(final Event event, final ExemptionJustification exemptionJustification) {
        if (event == null) {
            throw new DomainException("error.accounting.Exemption.event.cannot.be.null");
        }
        if (exemptionJustification == null) {
            throw new DomainException("error.accounting.Exemption.exemptionJustification.cannot.be.null");
        }
    }

    @Override
    public void setEvent(Event event) {
        throw new DomainException("error.domain.accounting.Exemption.cannot.modify.event");
    }

    @Override
    public void setResponsible(Person responsible) {
        throw new DomainException("error.accounting.Exemption.cannot.modify.responsible");
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
        throw new DomainException("error.accounting.Exemption.cannot.modify.whenCreated");
    }

    @Override
    public void setExemptionJustification(ExemptionJustification exemptionJustification) {
        throw new DomainException("error.accounting.Exemption.cannot.modify.exemptionJustification");
    }

    public void delete() {
        delete(true);
    }

    public void delete(final boolean recalculateEventState) {
        removeRootDomainObject();
        removeResponsible();
        getExemptionJustification().delete();
        final Event event = getEvent();
        removeEvent();
        if (recalculateEventState) {
            event.recalculateState(new DateTime());
        }

        super.deleteDomainObject();
    }

    @Override
    public void removeResponsible() {
        super.setResponsible(null);
    }

    @Override
    public void removeEvent() {
        super.setEvent(null);
    }

    public LabelFormatter getDescription() {
        return getExemptionJustification().getDescription();
    }

    public String getReason() {
        return getExemptionJustification().getReason();
    }

    public boolean isAdministrativeOfficeFeeAndInsuranceExemption() {
        return false;
    }

    public boolean isAdministrativeOfficeFeeExemption() {
        return false;
    }

    public boolean isForAdministrativeOfficeFee() {
        return false;
    }

    public boolean isInsuranceExemption() {
        return false;
    }

    public boolean isForInsurance() {
        return false;
    }

    public boolean isAcademicEventExemption() {
        return false;
    }

    public boolean isGratuityExemption() {
        return false;
    }

    public boolean isPenaltyExemption() {
        return false;
    }

    public boolean isPhdEventExemption() {
        return false;
    }

    public boolean isSecondCycleIndividualCandidacyExemption() {
        return false;
    }
}
