/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.domain.candidacy;

import java.math.BigDecimal;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class DFACandidacyPaymentEvent extends Event {
    
    public DFACandidacyPaymentEvent(DFACandidacyEvent candidacyEvent, DateTime whenOccured) {
        super();
        checkParameters(candidacyEvent);
        init(EventType.CANDIDACY_ENROLMENT_PAYMENT, whenOccured, candidacyEvent.getCandidacy().getPerson());
        initPayedEvent(candidacyEvent);
    }

    private void checkParameters(DFACandidacyEvent candidacyEvent) {
        if (candidacyEvent == null) {
            throw new DomainException("error.candidacy.dfaCandidacyPaymentEvent.invalid.candidacyEvent");
        }
    }

    @Override
    protected boolean checkIfIsProcessed() {
        return (hasAnyEntries() && isTotalPayed());
    }

    @Override
    protected void internalProcess(List<EntryDTO> entryDTOs) {
        
        final Account personExternalAccount = getPersonAccountBy(AccountType.EXTERNAL);
        final Account personInternalAccount = getPersonAccountBy(AccountType.INTERNAL);
        final Account degreeInternalAccount = getDegreeAccountBy(AccountType.INTERNAL);
        
        for (final EntryDTO entry : entryDTOs) {
            checkAmount(entry.getAmountToPay());
            makeAccountingTransaction(personExternalAccount, personInternalAccount, EntryType.TRANSFER, entry.getAmountToPay());
            makeAccountingTransaction(personInternalAccount, degreeInternalAccount, entry.getEntryType(), entry.getAmountToPay());
        }
        
        //TODO: modify according to penalties
        if (isTotalPayed()) {
            closeEvent();
        }
    }
    
    private void checkAmount(BigDecimal amountToPay) {
        if (amountToPay.add(calculateTotalPayedAmount()).compareTo(calculateAmount()) > 1) {
            throw new DomainException("error.candidacy.dfaCandidacy.invalid.amountToPay");
        }
    }
    
    //TODO: modify according to penalties
    private boolean isTotalPayed() {
        return calculateAmount().equals(calculateTotalPayedAmount());
    }
    
    //TODO: remove after posting rules? (get from PayedEvent) must include penalties
    private BigDecimal calculateAmount() {
        return getDFACandidacyEvent().calculateAmount();
    }
    
    //TODO: remove after posting rules? (get from PayedEvent)
    public BigDecimal calculateTotalPayedAmount() {
        return getDFACandidacyEvent().calculateTotalPayedAmount();
    }
    
    private DFACandidacyEvent getDFACandidacyEvent() {
        return (DFACandidacyEvent) getPayedEvent();
    }
    
    private Account getDegreeAccountBy(AccountType accountType) {
        return getDFACandidacyEvent().getCandidacy().getExecutionDegree().getDegreeCurricularPlan().getDegree().getUnit().getAccountBy(accountType);
    }
    
    private Account getPersonAccountBy(AccountType accountType) {
        return getDFACandidacyEvent().getCandidacy().getPerson().getAccountBy(accountType);
    }

    @Override
    public List<EntryDTO> calculateEntries() {
        // TODO Auto-generated method stub
        return null;
    }

}
