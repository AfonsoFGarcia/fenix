package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public abstract class Event extends Event_Base {

    protected Event() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
    }

    protected void init(EventType eventType, DateTime whenOccured) {
        checkParameters(eventType, whenOccured);
        super.setEventType(eventType);
        super.setWhenOccured(whenOccured);
        super.setClosed(Boolean.FALSE);
    }

    private void checkParameters(EventType eventType, DateTime whenOccured) throws DomainException {
        if (eventType == null) {
            throw new DomainException("error.accounting.event.invalid.eventType");
        }
        if (whenOccured == null) {
            throw new DomainException("error.accounting.event.invalid.dateTime");
        }
    }

    // TODO: to remove after create agreement and posting rules
    protected Entry makeEntry(EntryType entryType, BigDecimal amount, Account account) {
        return new Entry(entryType, amount, account, this);
    }

    // TODO: to remove after create agreement and posting rules
    protected void makeAccountingTransaction(Entry debit, Entry credit) {
        new AccountingTransaction(debit, credit);
    }

    // TODO: to remove after create agreement and posting rules
    protected void makeAccountingTransaction(Account from, Account to, EntryType entryType,
            BigDecimal amount) {
        new AccountingTransaction(makeEntry(entryType, amount.negate(), from), makeEntry(entryType,
                amount, to));
    }

    public boolean isClosed() {
        return getClosed().booleanValue();
    }

    public final void process(List<EntryDTO> entryDTOs) {
        if (!isClosed()) {
            internalProcess(entryDTOs);
        }
    }

    public void closeEvent() {
        super.setClosed(Boolean.TRUE);
    }

    protected abstract void internalProcess(List<EntryDTO> entryDTOs);

    @Override
    public void addEntries(Entry entries) {
        throw new DomainException("error.accounting.event.cannot.add.entries");
    }

    @Override
    public List<Entry> getEntries() {
        return Collections.unmodifiableList(super.getEntries());
    }

    @Override
    public Iterator<Entry> getEntriesIterator() {
        return getEntriesSet().iterator();
    }

    @Override
    public Set<Entry> getEntriesSet() {
        return Collections.unmodifiableSet(super.getEntriesSet());
    }

    @Override
    public void removeEntries(Entry entries) {
        throw new DomainException("error.accounting.event.cannot.remove.entries");
    }

    @Override
    public void setEventType(EventType eventType) {
        throw new DomainException("error.accounting.event.cannot.modify.eventType");
    }

    @Override
    public void setClosed(Boolean closed) {
        throw new DomainException("error.accounting.event.cannot.modify.processed.value");
    }

    @Override
    public void setWhenOccured(DateTime whenOccured) {
        throw new DomainException("error.accounting.event.cannot.modify.occuredDateTime");
    }

}
