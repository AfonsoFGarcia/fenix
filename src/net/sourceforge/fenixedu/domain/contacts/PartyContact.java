package net.sourceforge.fenixedu.domain.contacts;

import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;

public abstract class PartyContact extends PartyContact_Base {

    public static Comparator<PartyContact> COMPARATOR_BY_TYPE = new Comparator<PartyContact>() {
	public int compare(PartyContact contact, PartyContact otherContact) {
	    int result = contact.getType().compareTo(otherContact.getType());
	    return (result == 0) ? DomainObject.COMPARATOR_BY_ID.compare(contact, otherContact) : result;
	}
    };

    protected PartyContact() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	setVisibleToPublic(Boolean.FALSE);
	setVisibleToStudents(Boolean.FALSE);
	setVisibleToTeachers(Boolean.FALSE);
	setVisibleToEmployees(Boolean.FALSE);
	setVisibleToAlumni(Boolean.FALSE);
    }

    protected void init(final Party party, final PartyContactType type, final boolean defaultContact) {

	checkParameters(party, type);

	super.setParty(party);
	super.setType(type);

	if (type.equals(PartyContactType.INSTITUTIONAL)) {
	    setVisibleToPublic();
	} else if (party.isPerson() && !((Person) party).hasRole(RoleType.TEACHER)
		&& !((Person) party).hasRole(RoleType.EMPLOYEE)) {
	    setVisibleToStudents(type == PartyContactType.WORK);
	    setVisibleToAlumni(type == PartyContactType.WORK);
	    setVisibleToTeachers(type == PartyContactType.WORK);
	    setVisibleToEmployees(type == PartyContactType.WORK);
	} else {
	    setVisibleToTeachers(type == PartyContactType.WORK);
	    setVisibleToEmployees(type == PartyContactType.WORK);
	}
	setDefaultContactInformation(defaultContact);
    }

    protected void init(final Party party, final PartyContactType type, final boolean visibleToPublic,
	    final boolean visibleToStudents, final boolean visibleToTeachers, final boolean visibleToEmployees,
	    final boolean visibleToAlumni, final boolean defaultContact) {
	checkParameters(party, type);
	super.setParty(party);
	super.setType(type);
	setVisibleToPublic(new Boolean(visibleToPublic));
	setVisibleToStudents(new Boolean(visibleToStudents));
	setVisibleToTeachers(new Boolean(visibleToTeachers));
	setVisibleToEmployees(new Boolean(visibleToEmployees));
	setVisibleToAlumni(new Boolean(visibleToAlumni));
	setDefaultContactInformation(defaultContact);
    }

    private void setVisibleToPublic() {
	super.setVisibleToPublic(Boolean.TRUE);
	super.setVisibleToStudents(Boolean.TRUE);
	super.setVisibleToTeachers(Boolean.TRUE);
	super.setVisibleToEmployees(Boolean.TRUE);
	super.setVisibleToAlumni(Boolean.TRUE);
    }

    @Override
    public void setVisibleToPublic(Boolean visibleToPublic) {
	super.setVisibleToPublic(visibleToPublic);
	if (visibleToPublic.booleanValue()) {
	    super.setVisibleToStudents(Boolean.TRUE);
	    super.setVisibleToTeachers(Boolean.TRUE);
	    super.setVisibleToEmployees(Boolean.TRUE);
	    super.setVisibleToAlumni(Boolean.TRUE);
	}
    }

    @Override
    public void setVisibleToStudents(Boolean visibleToStudents) {
	super.setVisibleToStudents(visibleToStudents);
	if (!visibleToStudents.booleanValue())
	    super.setVisibleToPublic(Boolean.FALSE);
    }

    @Override
    public void setVisibleToTeachers(Boolean visibleToTeachers) {
	super.setVisibleToTeachers(visibleToTeachers);
	if (!visibleToTeachers.booleanValue())
	    super.setVisibleToPublic(Boolean.FALSE);
    }

    @Override
    public void setVisibleToEmployees(Boolean visibleToEmployees) {
	super.setVisibleToEmployees(visibleToEmployees);
	if (!visibleToEmployees.booleanValue())
	    super.setVisibleToPublic(Boolean.FALSE);
    }

    private void setDefaultContactInformation(final boolean defaultContact) {
	if (defaultContact) {
	    changeToDefault();
	} else {
	    final List<PartyContact> partyContacts = (List<PartyContact>) getParty().getPartyContacts(getClass());
	    if (partyContacts.isEmpty() || partyContacts.size() == 1) {
		super.setDefaultContact(Boolean.TRUE);
	    } else {
		setAnotherContactAsDefault();
		super.setDefaultContact(Boolean.FALSE);
	    }
	}
    }

    public void changeToDefault() {
	final PartyContact defaultPartyContact = getParty().getDefaultPartyContact(getClass());
	if (defaultPartyContact != null && defaultPartyContact != this) {
	    defaultPartyContact.setDefaultContact(Boolean.FALSE);
	}
	super.setDefaultContact(Boolean.TRUE);
    }

    private void checkParameters(final Party party, final PartyContactType type) {
	if (party == null) {
	    throw new DomainException("error.contacts.PartyContact.party.cannot.be.null");
	}
	if (type == null) {
	    throw new DomainException("error.contacts.PartyContact.contactType.cannot.be.null");
	}
    }

    public void edit(final PartyContactType type, final boolean defaultContact) {
	checkParameters(getParty(), type);
	super.setType(type);
	setDefaultContactInformation(defaultContact);
    }

    public abstract String getPresentationValue();

    public boolean isDefault() {
	return hasDefaultContactValue() && getDefaultContact().booleanValue();
    }

    private boolean hasDefaultContactValue() {
	return getDefaultContact() != null;
    }

    public boolean isInstitutionalType() {
	return getType() == PartyContactType.INSTITUTIONAL;
    }

    public boolean isWorkType() {
	return getType() == PartyContactType.WORK;
    }

    public boolean isPersonalType() {
	return getType() == PartyContactType.PERSONAL;
    }

    public boolean isWebAddress() {
	return false;
    }

    public boolean isPhysicalAddress() {
	return false;
    }

    public boolean isEmailAddress() {
	return false;
    }

    public boolean isPhone() {
	return false;
    }

    public boolean isMobile() {
	return false;
    }

    public void deleteWithoutCheckRules() {
	processDelete();
    }

    public void delete() {
	checkRulesToDelete();
	processDelete();
    }

    private void processDelete() {
	setAnotherContactAsDefault();
	removeParty();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    protected void checkRulesToDelete() {
	// nothing to check
    }

    private void setAnotherContactAsDefault() {
	if (isDefault()) {
	    final List<PartyContact> contacts = (List<PartyContact>) getParty().getPartyContacts(getClass());
	    if (!contacts.isEmpty() && contacts.size() > 1) {
		contacts.remove(this);
		contacts.get(0).setDefaultContact(Boolean.TRUE);
	    }
	}
    }
}
