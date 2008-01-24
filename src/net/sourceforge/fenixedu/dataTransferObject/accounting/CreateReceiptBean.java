package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;

public class CreateReceiptBean implements Serializable {

    private DomainReference<Person> person;

    private DomainReference<Party> contributorParty;

    private List<SelectableEntryBean> entries;

    private String contributorNumber;

    public CreateReceiptBean() {

    }

    public CreateReceiptBean(Person person, Party contributor, List<SelectableEntryBean> entries) {
        setPerson(person);
        setContributorParty(contributor);
        setEntries(entries);
    }

    public Party getContributorParty() {
        return (this.contributorParty != null) ? this.contributorParty.getObject() : null;
    }

    public void setContributorParty(Party contributorParty) {
        this.contributorParty = (contributorParty != null) ? new DomainReference<Party>(contributorParty) : null;
    }

    public void setPerson(Person person) {
        this.person = (person != null) ? new DomainReference<Person>(person) : null;
    }

    public Person getPerson() {
        return (this.person != null) ? this.person.getObject() : null;
    }

    public List<SelectableEntryBean> getEntries() {
        return entries;
    }

    public void setEntries(List<SelectableEntryBean> entries) {
        this.entries = entries;
    }

    public String getContributorNumber() {
        return (StringUtils.isEmpty(contributorNumber) && person != null) ? person.getObject().getSocialSecurityNumber() : contributorNumber;
    }

    public void setContributorNumber(String contributorNumber) {
        this.contributorNumber = contributorNumber;
    }

    public List<Entry> getSelectedEntries() {
        final List<Entry> result = new ArrayList<Entry>();

        for (final SelectableEntryBean selectableEntryBean : getEntries()) {
            if (selectableEntryBean.isSelected()) {
                result.add(selectableEntryBean.getEntry());
            }
        }

        return result;
    }

    public Money getTotalAmount() {
        Money result = Money.ZERO;
        for (final Entry entry : getSelectedEntries()) {
            result = result.add(entry.getAmountWithAdjustment());
        }
        return result;
    }

}
