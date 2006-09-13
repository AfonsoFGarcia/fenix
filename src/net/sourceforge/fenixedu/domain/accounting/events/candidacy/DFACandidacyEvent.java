package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.util.StateMachine;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

public class DFACandidacyEvent extends DFACandidacyEvent_Base {

    private DFACandidacyEvent() {
        super();
    }

    public DFACandidacyEvent(AdministrativeOffice administrativeOffice, Person person,
            DFACandidacy candidacy) {
        this();
        init(administrativeOffice, person, candidacy);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, DFACandidacy candidacy) {
        init(administrativeOffice, EventType.CANDIDACY_ENROLMENT, person);
        checkParameters(candidacy);
        super.setCandidacy(candidacy);
    }

    private void checkParameters(Candidacy candidacy) {
        if (candidacy == null) {
            throw new DomainException("error.candidacy.dfaCandidacyEvent.invalid.candidacy");
        }
    }

    @Override
    public Account getToAccount() {
        return getUnit().getAccountBy(AccountType.INTERNAL);
    }

    private Unit getUnit() {
        return getCandidacy().getExecutionDegree().getDegreeCurricularPlan().getDegree().getUnit();
    }

    @Override
    public void setCandidacy(DFACandidacy candidacy) {
        throw new DomainException("error.candidacy.dfaCandidacyEvent.cannot.modify.candidacy");
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" (").appendLabel(
                getDegree().getDegreeType().name(), "enum").appendLabel(" - ").appendLabel(
                getDegree().getName()).appendLabel(" - ").appendLabel(
                getExecutionDegree().getExecutionYear().getYear()).appendLabel(")");

        return labelFormatter;

    }

    private ExecutionDegree getExecutionDegree() {
        return getCandidacy().getExecutionDegree();

    }

    private Degree getDegree() {
        return getExecutionDegree().getDegreeCurricularPlan().getDegree();

    }

    @Override
    public void closeEvent() {
        StateMachine.execute(getCandidacy().getActiveCandidacySituation());

        super.closeEvent();
    }

    @Override
    protected PostingRule getPostingRule(DateTime whenRegistered) {
        return getExecutionDegree().getDegreeCurricularPlan().getServiceAgreementTemplate()
                .findPostingRuleByEventTypeAndDate(getEventType(), whenRegistered);
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, List<EntryDTO> entryDTOs,
            PaymentMode paymentMode, DateTime whenRegistered) {
        return getPostingRule(whenRegistered).process(responsibleUser, entryDTOs, paymentMode,
                whenRegistered, this, getPerson().getAccountBy(AccountType.EXTERNAL), getToAccount());
    }

}
