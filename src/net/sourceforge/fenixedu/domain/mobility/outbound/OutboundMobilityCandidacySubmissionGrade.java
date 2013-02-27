package net.sourceforge.fenixedu.domain.mobility.outbound;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class OutboundMobilityCandidacySubmissionGrade extends OutboundMobilityCandidacySubmissionGrade_Base {

    public OutboundMobilityCandidacySubmissionGrade(
            final OutboundMobilityCandidacySubmission submission,
            final OutboundMobilityCandidacyContestGroup mobilityGroup, final BigDecimal grade) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOutboundMobilityCandidacySubmission(submission);
        setOutboundMobilityCandidacyContestGroup(mobilityGroup);
        edit(grade);
    }

    public void edit(final BigDecimal grade) {
        setGrade(grade);
    }

}
