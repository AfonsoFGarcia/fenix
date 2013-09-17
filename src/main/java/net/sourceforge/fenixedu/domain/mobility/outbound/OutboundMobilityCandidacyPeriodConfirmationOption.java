package net.sourceforge.fenixedu.domain.mobility.outbound;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixframework.Atomic;

public class OutboundMobilityCandidacyPeriodConfirmationOption extends OutboundMobilityCandidacyPeriodConfirmationOption_Base
        implements Comparable<OutboundMobilityCandidacyPeriodConfirmationOption> {

    public OutboundMobilityCandidacyPeriodConfirmationOption(final OutboundMobilityCandidacyPeriod period,
            final String optionValue, final Boolean availableForCandidates) {
        setRootDomainObject(RootDomainObject.getInstance());
        setOptionValue(optionValue);
        setAvailableForCandidates(availableForCandidates);
        setOutboundMobilityCandidacyPeriod(period);
    }

    @Atomic
    public void delete() {
        setOutboundMobilityCandidacyPeriod(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Override
    public int compareTo(final OutboundMobilityCandidacyPeriodConfirmationOption o) {
        return getExternalId().compareTo(o.getExternalId());
    }

}
