package net.sourceforge.fenixedu.domain.contacts;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.predicates.Predicate;

public abstract class PartyContactValidation extends PartyContactValidation_Base {
    private final int MAX_TRIES = 3;

    public static class PartyContactValidationPredicate extends Predicate<PartyContactValidation> {

	private final PartyContactValidationState state;

	public PartyContactValidationPredicate(PartyContactValidationState state) {
	    this.state = state;
	}

	@Override
	public boolean eval(PartyContactValidation t) {
	    return t.getState().equals(state);
	}

    }

    public static final PartyContactValidationPredicate PREDICATE_INVALID = new PartyContactValidationPredicate(
	    PartyContactValidationState.INVALID);
    public static final PartyContactValidationPredicate PREDICATE_VALID = new PartyContactValidationPredicate(
	    PartyContactValidationState.VALID);
    public static final PartyContactValidationPredicate PREDICATE_REFUSED = new PartyContactValidationPredicate(
	    PartyContactValidationState.REFUSED);

    public PartyContactValidation() {
	super();
	setInvalid();
	setRequestDate(new DateTime());
	setTries(MAX_TRIES);
    }

    public void init(PartyContact contact) {
	setPartyContact(contact);
    }

    public PartyContactValidation(PartyContact contact) {
	this();
	init(contact);
    }

    private boolean validate(boolean result) {
	if (isInvalid()) {
	    if (result) {
		setValid();
	    } else {
		setInvalid();
	    }
	}
	return isValid();
    }

    @Service
    public Boolean processValidation(String token) {

	if (isRefused()) {
	    return false;
	}

	if (!StringUtils.isEmpty(getToken()) && !StringUtils.isEmpty(token)) {
	    validate(getToken().equals(token));
	    if (isInvalid()) {
		setTries(getTries() - 1);
		if (getTries() <= 0) {
		    setRefused();
		}
	    }
	}

	return isValid();
    }

    protected void setValid() {
	if (!isInvalid()) {
	    return;
	}
	if (hasRootDomainObject()) {
	    setRootDomainObject(null);
	}
	super.setState(PartyContactValidationState.VALID);
	setLastChangeDate(new DateTime());
	final PartyContact partyContact = getPartyContact();
	if (partyContact.hasPrevPartyContact()) {
	    partyContact.getPrevPartyContact().deleteWithoutCheckRules();
	}
    }

    private void setNotValidState(PartyContactValidationState state) {
	if (!hasRootDomainObject()) {
	    setRootDomainObject(RootDomainObject.getInstance());
	}
	super.setState(state);
	setLastChangeDate(new DateTime());
    }

    public Integer getAvailableTries() {
	return getTries() > 0 ? getTries() : 0;
    }

    protected void setInvalid() {
	setNotValidState(PartyContactValidationState.INVALID);
    }

    protected void setRefused() {
	if (!isRefused()) {
	    setNotValidState(PartyContactValidationState.REFUSED);
	    getPartyContact().deleteWithoutCheckRules();
	}
    }

    private boolean isState(PartyContactValidationState state) {
	return state.equals(getState());
    }

    public boolean isValid() {
	return isState(PartyContactValidationState.VALID);
    }

    public boolean isInvalid() {
	return isState(PartyContactValidationState.INVALID);
    }

    public boolean isRefused() {
	return isState(PartyContactValidationState.REFUSED);
    }

    @Override
    public void setState(PartyContactValidationState state) {
	switch (state) {
	case INVALID:
	    setInvalid();
	    break;
	case REFUSED:
	    setRefused();
	    break;
	case VALID:
	    setValid();
	    break;
	}
    }

    public void triggerValidationProcess() {
	// TODO Auto-generated method stub

    }

}
