/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;

public class PaymentsManagementDTO implements Serializable {

    private DomainReference<Person> person;
    private List<EntryDTO> entryDTOs;

    public PaymentsManagementDTO(Person person) {
        setPerson(person);
        setEntryDTOs(new ArrayList<EntryDTO>());
    }

    public Person getPerson() {
        return (this.person != null) ? this.person.getObject() : null;
    }

    public void setPerson(Person person) {
        this.person = (person != null) ? new DomainReference<Person>(person) : null;
    }

    public void addEntryDTOs(List<EntryDTO> entryDTOs) {
        this.entryDTOs.addAll(entryDTOs);
    }

    public List<EntryDTO> getEntryDTOs() {
        return entryDTOs;
    }

    private void setEntryDTOs(List<EntryDTO> entryDTOs) {
        this.entryDTOs = entryDTOs;
    }

    public List<EntryDTO> getSelectedEntries() {
        final List<EntryDTO> result = new ArrayList<EntryDTO>();
        for (final EntryDTO each : getEntryDTOs()) {
            if (each.isSelected()) {
                result.add(each);
            }
        }
        return result;
    }

}
