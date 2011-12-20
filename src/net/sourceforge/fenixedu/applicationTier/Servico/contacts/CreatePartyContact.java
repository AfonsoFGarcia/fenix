package net.sourceforge.fenixedu.applicationTier.Servico.contacts;

import net.sourceforge.fenixedu.dataTransferObject.contacts.PartyContactBean;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import pt.ist.fenixWebFramework.services.Service;

public class CreatePartyContact {

    @Service
    public static PartyContact run(PartyContactBean contactBean, final boolean toBeValidated) {
	if (contactBean.hasPartyContact()) {
	    return null;
	}
	final PartyContact createNewContact = contactBean.createNewContact();
	if (toBeValidated || createNewContact instanceof EmailAddress) {
	    createNewContact.triggerValidationProcess();
	} else {
	    if (createNewContact instanceof PhysicalAddress) {
		((PhysicalAddress) createNewContact).setValid();
	    }
	}
	contactBean.setContact(createNewContact);
	return createNewContact;
    }
}
