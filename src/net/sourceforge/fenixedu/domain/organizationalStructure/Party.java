/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PartyClassification;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressData;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.Prize;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.CooperationParticipation;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.EventEditionParticipation;
import net.sourceforge.fenixedu.domain.research.activity.EventParticipation;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssueParticipation;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournalParticipation;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.Unstructured;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.StringNormalizer;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class Party extends Party_Base implements Comparable<Party> {

    static final public Comparator<Party> COMPARATOR_BY_NAME = new Comparator<Party>() {
	public int compare(final Party o1, final Party o2) {
	    return Collator.getInstance().compare(o1.getName(), o2.getName());
	}
    };

    static final public Comparator<Party> COMPARATOR_BY_NAME_AND_ID = new Comparator<Party>() {
	public int compare(final Party o1, final Party o2) {
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(Party.COMPARATOR_BY_NAME);
	    comparatorChain.addComparator(Party.COMPARATOR_BY_ID);

	    return comparatorChain.compare(o1, o2);
	}
    };

    public abstract String getPartyPresentationName();

    public abstract void setName(String name);

    public abstract String getName();

    public Party() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	createAccount(AccountType.INTERNAL);
	createAccount(AccountType.EXTERNAL);
    }

    @Override
    public void setPartyName(MultiLanguageString partyName) {
	if (partyName == null || partyName.isEmpty()) {
	    throw new DomainException("error.Party.empty.partyName");
	}
	super.setPartyName(partyName);
    }

    @Deprecated
    @Override
    final public Country getNationality() {
	return getCountry();
    }

    @Deprecated
    @Override
    public void setNationality(final Country country) {
	setCountry(country);
    }

    public Country getCountry() {
	return super.getNationality();
    }

    final public void setCountry(final Country country) {
	super.setNationality(country);
    }

    public Account createAccount(AccountType accountType) {
	checkAccountsFor(accountType);
	return new Account(accountType, this);
    }

    private void checkAccountsFor(AccountType accountType) {
	if (getAccountBy(accountType) != null) {
	    throw new DomainException("error.party.accounts.accountType.already.exist");
	}
    }

    public Account getAccountBy(AccountType accountType) {
	for (final Account account : getAccountsSet()) {
	    if (account.getAccountType() == accountType) {
		return account;
	    }
	}
	return null;
    }

    public Account getInternalAccount() {
	return getAccountBy(AccountType.INTERNAL);
    }

    public Account getExternalAccount() {
	return getAccountBy(AccountType.EXTERNAL);
    }

    public PartyTypeEnum getType() {
	return getPartyType() != null ? getPartyType().getType() : null;
    }

    public void setType(PartyTypeEnum partyTypeEnum) {
	if (partyTypeEnum != null) {
	    PartyType partyType = PartyType.readPartyTypeByType(partyTypeEnum);
	    if (partyType == null) {
		throw new DomainException("error.Party.unknown.partyType");
	    }
	    setPartyType(partyType);
	} else {
	    setPartyType(null);
	}
    }

    public Collection<? extends Party> getParentParties(AccountabilityTypeEnum accountabilityTypeEnum,
	    Class<? extends Party> parentPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && parentPartyClass.isAssignableFrom(accountability.getParentParty().getClass())) {
		result.add(accountability.getParentParty());
	    }
	}
	return result;
    }

    public Collection<? extends Party> getParentParties(Class<? extends Party> parentPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getParentsSet()) {
	    if (parentPartyClass.isAssignableFrom(accountability.getParentParty().getClass())) {
		result.add(accountability.getParentParty());
	    }
	}
	return result;
    }

    public Collection<? extends Party> getParentParties(List<AccountabilityTypeEnum> accountabilityTypeEnums,
	    Class<? extends Party> parentPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountabilityTypeEnums.contains(accountability.getAccountabilityType().getType())
		    && parentPartyClass.isAssignableFrom(accountability.getParentParty().getClass())) {
		result.add(accountability.getParentParty());
	    }
	}
	return result;
    }

    public Collection<? extends Party> getChildParties(Class<? extends Party> childPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (childPartyClass.isAssignableFrom(accountability.getChildParty().getClass())) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }

    public Collection<? extends Party> getChildParties(AccountabilityTypeEnum accountabilityTypeEnum,
	    Class<? extends Party> childPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && childPartyClass.isAssignableFrom(accountability.getChildParty().getClass())) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }

    public Collection<? extends Party> getChildParties(List<AccountabilityTypeEnum> accountabilityTypeEnums,
	    Class<? extends Party> childPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountabilityTypeEnums.contains(accountability.getAccountabilityType().getType())
		    && childPartyClass.isAssignableFrom(accountability.getChildParty().getClass())) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }

    protected Collection<? extends Party> getChildParties(PartyTypeEnum type, Class<? extends Party> childPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getChildParty().getType() == type
		    && childPartyClass.isAssignableFrom(accountability.getChildParty().getClass())) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getParentAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getChildAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getParentAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum,
	    Class<? extends Accountability> accountabilityClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountabilityClass.isAssignableFrom(accountability.getClass())) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getChildAccountabilities(Class<? extends Accountability> accountabilityClass,
	    AccountabilityTypeEnum... types) {
	final Set<Accountability> result = new HashSet<Accountability>();

	for (final Accountability accountability : getChildsSet()) {
	    AccountabilityTypeEnum accountabilityType = accountability.getAccountabilityType().getType();

	    if (!isOneOfTypes(accountabilityType, types)) {
		continue;
	    }

	    if (!accountabilityClass.isAssignableFrom(accountability.getClass())) {
		continue;
	    }

	    result.add(accountability);
	}

	return result;
    }

    private boolean isOneOfTypes(AccountabilityTypeEnum type, AccountabilityTypeEnum[] possibilities) {
	for (AccountabilityTypeEnum t : possibilities) {
	    if (t == type) {
		return true;
	    }
	}

	return false;
    }

    public Collection<? extends Accountability> getParentAccountabilitiesByParentClass(Class<? extends Party> parentClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (parentClass.isAssignableFrom(accountability.getParentParty().getClass())) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getChildAccountabilitiesByChildClass(Class<? extends Party> childClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (childClass.isAssignableFrom(accountability.getChildParty().getClass())) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getChildAccountabilitiesByAccountabilityClass(
	    Class<? extends Accountability> accountabilityClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountabilityClass.isAssignableFrom(accountability.getClass())) {
		result.add(accountability);
	    }
	}
	return result;
    }

    protected void delete() {
	if (!canBeDeleted()) {
	    throw new DomainException("error.party.cannot.be.deleted");
	}

	for (; !getAccounts().isEmpty(); getAccounts().get(0).delete())
	    ;
	for (; hasAnyPartyContacts(); getPartyContacts().get(0).deleteWithoutCheckRules())
	    ;

	if (hasPartySocialSecurityNumber()) {
	    getPartySocialSecurityNumber().delete();
	}

	removeNationality();
	removePartyType();
	removeRootDomainObject();

	deleteDomainObject();
    }

    private boolean canBeDeleted() {
	return !hasAnyResourceResponsibility() && !hasAnyVehicleAllocations() && !hasAnyPayedReceipts();
    }

    public static Party readByContributorNumber(String contributorNumber) {
	return PartySocialSecurityNumber.readPartyBySocialSecurityNumber(contributorNumber);
    }

    public String getSocialSecurityNumber() {
	return hasPartySocialSecurityNumber() ? getPartySocialSecurityNumber().getSocialSecurityNumber() : null;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {

	if (socialSecurityNumber != null && socialSecurityNumber.length() != 0) {
	    if (hasPartySocialSecurityNumber()
		    && socialSecurityNumber.equals(getPartySocialSecurityNumber().getSocialSecurityNumber())) {
		return;
	    }

	    final Party party = PartySocialSecurityNumber.readPartyBySocialSecurityNumber(socialSecurityNumber);
	    if (party != null && party != this) {
		throw new DomainException("error.party.existing.contributor.number");
	    } else {
		if (hasPartySocialSecurityNumber()) {
		    getPartySocialSecurityNumber().setSocialSecurityNumber(socialSecurityNumber);
		} else {
		    new PartySocialSecurityNumber(this, socialSecurityNumber);
		}
	    }
	}
    }

    public void editContributor(String contributorName, String contributorNumber, String contributorAddress, String areaCode,
	    String areaOfAreaCode, String area, String parishOfResidence, String districtSubdivisionOfResidence,
	    String districtOfResidence) {

	setName(contributorName);
	setSocialSecurityNumber(contributorNumber);
	setAddress(contributorAddress);
	setAddress(contributorAddress);
	setAreaCode(areaCode);
	setAreaOfAreaCode(areaOfAreaCode);
	setArea(area);
	setParishOfResidence(parishOfResidence);
	setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
	setDistrictOfResidence(districtOfResidence);
    }

    public boolean isPerson() {
	return false;
    }

    public boolean isUnit() {
	return false;
    }

    public boolean isDepartmentUnit() {
	return false;
    }

    public boolean isCompetenceCourseGroupUnit() {
	return false;
    }

    public boolean isScientificAreaUnit() {
	return false;
    }

    public boolean isAdministrativeOfficeUnit() {
	return false;
    }

    public boolean isDegreeUnit() {
	return false;
    }

    public boolean isAcademicalUnit() {
	return false;
    }

    public boolean isSchoolUnit() {
	return false;
    }

    public boolean isUniversityUnit() {
	return false;
    }

    public boolean isPlanetUnit() {
	return false;
    }

    public boolean isCountryUnit() {
	return false;
    }

    public boolean isSectionUnit() {
	return false;
    }

    public boolean isAggregateUnit() {
	return false;
    }

    public boolean isResearchUnit() {
	return false;
    }

    public boolean hasCompetenceCourses(final CompetenceCourse competenceCourse) {
	return false;
    }

    public boolean hasAdministrativeOffice() {
	return false;
    }

    public boolean hasDegree() {
	return false;
    }

    public boolean hasDepartment() {
	return false;
    }

    public abstract PartyClassification getPartyClassification();

    public boolean verifyNameEquality(String[] nameWords) {
	if (nameWords == null) {
	    return true;
	}
	if (getName() != null) {
	    String[] personNameWords = getName().trim().split(" ");
	    StringNormalizer.normalize(personNameWords);
	    int j, i;
	    for (i = 0; i < nameWords.length; i++) {
		if (!nameWords[i].equals("")) {
		    for (j = 0; j < personNameWords.length; j++) {
			if (personNameWords[j].equals(nameWords[i])) {
			    break;
			}
		    }
		    if (j == personNameWords.length) {
			return false;
		    }
		}
	    }
	    if (i == nameWords.length) {
		return true;
	    }
	}
	return false;
    }

    private List<? extends Participation> filterParticipationsByYear(List<? extends Participation> participations,
	    ExecutionYear begin, ExecutionYear end) {
	List<Participation> participationsForInterval = new ArrayList<Participation>();
	for (Participation participation : participations) {
	    Integer year = participation.getCivilYear();
	    if (year == null || (begin == null || begin.isBeforeCivilYear(year) || begin.belongsToCivilYear(year))
		    && (end == null || end.isAfterCivilYear(year) || end.belongsToCivilYear(year))) {
		participationsForInterval.add(participation);
	    }
	}
	return participationsForInterval;
    }

    private List<? extends Participation> filterParticipationsByType(Class<? extends Participation> clazz, ScopeType scopeType) {
	List<Participation> participations = new ArrayList<Participation>();
	for (Participation participation : getParticipations()) {
	    if (participation.getClass().equals(clazz) && (scopeType == null || participation.scopeMatches(scopeType))) {
		participations.add(participation);
	    }
	}
	return participations;
    }

    public List<EventEditionParticipation> getEventEditionParticipations(ScopeType type, ExecutionYear begin, ExecutionYear end) {
	return (List<EventEditionParticipation>) filterParticipationsByYear(getEventEditionParticipations(type), begin, end);
    }

    public List<EventEditionParticipation> getEventEditionParticipations(ExecutionYear begin, ExecutionYear end) {
	return (List<EventEditionParticipation>) filterParticipationsByYear(getEventEditionParticipations(), begin, end);
    }

    public List<EventEditionParticipation> getEventEditionParticipations(ScopeType type) {
	return (List<EventEditionParticipation>) filterParticipationsByType(EventEditionParticipation.class, type);
    }

    public List<EventEditionParticipation> getEventEditionParticipations() {
	return getEventEditionParticipations(null);
    }

    public List<EventParticipation> getEventParticipations(ScopeType type) {
	return (List<EventParticipation>) filterParticipationsByType(EventParticipation.class, type);
    }

    public Set<EventEdition> getAssociatedEventEditions(ScopeType type, ExecutionYear begin, ExecutionYear end) {
	Set<EventEdition> eventEditions = new HashSet<EventEdition>();
	for (EventEditionParticipation participation : getEventEditionParticipations(type, begin, end)) {
	    eventEditions.add(participation.getEventEdition());
	}
	return eventEditions;
    }

    public Set<EventEdition> getAssociatedEventEditions(ExecutionYear begin, ExecutionYear end) {
	return getAssociatedEventEditions(null, begin, end);
    }

    public Set<EventEdition> getAssociatedEventEditions() {
	return getAssociatedEventEditions(null);
    }

    public Set<EventEdition> getAssociatedEventEditions(ScopeType type) {
	return getAssociatedEventEditions(type, null, null);
    }

    public List<EventParticipation> getEventParticipations(ScopeType type, ExecutionYear begin, ExecutionYear end) {
	return (List<EventParticipation>) filterParticipationsByYear(getEventParticipations(type), begin, end);
    }

    public List<EventParticipation> getEventParticipation(ExecutionYear begin, ExecutionYear end) {
	return (List<EventParticipation>) filterParticipationsByYear(getEventParticipations(), begin, end);
    }

    public List<EventParticipation> getEventParticipations() {
	return getEventParticipations(null);
    }

    public Set<ResearchEvent> getAssociatedEvents(ScopeType type, ExecutionYear begin, ExecutionYear end) {
	Set<ResearchEvent> events = new HashSet<ResearchEvent>();
	for (EventParticipation participation : getEventParticipations(type, begin, end)) {
	    events.add(participation.getEvent());
	}
	return events;
    }

    public Set<ResearchEvent> getAssociatedEvents(ExecutionYear begin, ExecutionYear end) {
	return getAssociatedEvents(null, begin, end);
    }

    public Set<ResearchEvent> getAssociatedEvents(ScopeType type) {
	return getAssociatedEvents(type, null, null);
    }

    public Set<ResearchEvent> getAssociatedEvents() {
	return getAssociatedEvents(null);
    }

    public List<ScientificJournalParticipation> getScientificJournalParticipations(ScopeType type, ExecutionYear begin,
	    ExecutionYear end) {
	return (List<ScientificJournalParticipation>) filterParticipationsByYear(getScientificJournalParticipations(type), begin,
		end);
    }

    public List<ScientificJournalParticipation> getScientificJournalParticipations(ExecutionYear begin, ExecutionYear end) {
	return (List<ScientificJournalParticipation>) filterParticipationsByYear(getScientificJournalParticipations(), begin, end);
    }

    public List<ScientificJournalParticipation> getScientificJournalParticipations(ScopeType type) {
	return (List<ScientificJournalParticipation>) filterParticipationsByType(ScientificJournalParticipation.class, type);
    }

    public List<ScientificJournalParticipation> getScientificJournalParticipations() {
	return getScientificJournalParticipations(null);
    }

    public Set<ScientificJournal> getAssociatedScientificJournals(ScopeType type, ExecutionYear begin, ExecutionYear end) {
	Set<ScientificJournal> journals = new HashSet<ScientificJournal>();
	for (ScientificJournalParticipation participation : getScientificJournalParticipations(type, begin, end)) {
	    journals.add(participation.getScientificJournal());
	}
	return journals;
    }

    public Set<ScientificJournal> getAssociatedScientificJournals(ExecutionYear begin, ExecutionYear end) {
	return getAssociatedScientificJournals(null, begin, end);
    }

    public Set<ScientificJournal> getAssociatedScientificJournals(ScopeType type) {
	return getAssociatedScientificJournals(type, null, null);
    }

    public Set<ScientificJournal> getAssociatedScientificJournals() {
	return getAssociatedScientificJournals(null);
    }

    public List<JournalIssueParticipation> getJournalIssueParticipations(ScopeType type, ExecutionYear begin, ExecutionYear end) {
	return (List<JournalIssueParticipation>) filterParticipationsByYear(getJournalIssueParticipations(type), begin, end);
    }

    public List<JournalIssueParticipation> getJournalIssueParticipations(ExecutionYear begin, ExecutionYear end) {
	return (List<JournalIssueParticipation>) filterParticipationsByYear(getJournalIssueParticipations(), begin, end);
    }

    public List<JournalIssueParticipation> getJournalIssueParticipations(ScopeType type) {
	return (List<JournalIssueParticipation>) filterParticipationsByType(JournalIssueParticipation.class, type);
    }

    public List<JournalIssueParticipation> getJournalIssueParticipations() {
	return getJournalIssueParticipations(null);
    }

    public Set<JournalIssue> getAssociatedJournalIssues(ScopeType type, ExecutionYear begin, ExecutionYear end) {
	Set<JournalIssue> issues = new HashSet<JournalIssue>();
	for (JournalIssueParticipation participation : this.getJournalIssueParticipations(type, begin, end)) {
	    issues.add(participation.getJournalIssue());
	}
	return issues;
    }

    public Set<JournalIssue> getAssociatedJournalIssues(ExecutionYear begin, ExecutionYear end) {
	return getAssociatedJournalIssues(null, begin, end);
    }

    public Set<JournalIssue> getAssociatedJournalIssues(ScopeType locationType) {
	return getAssociatedJournalIssues(locationType, null, null);
    }

    public Set<JournalIssue> getAssociatedJournalIssues() {
	return getAssociatedJournalIssues(null);
    }

    public List<CooperationParticipation> getCooperationParticipations(ExecutionYear begin, ExecutionYear end) {
	return (List<CooperationParticipation>) filterParticipationsByYear(getCooperationParticipations(), begin, end);
    }

    public List<CooperationParticipation> getCooperationParticipations() {
	List<CooperationParticipation> cooperationParticipations = new ArrayList<CooperationParticipation>();
	for (Participation participation : this.getParticipations()) {
	    if (participation.isCooperationParticipation()) {
		cooperationParticipations.add((CooperationParticipation) participation);
	    }
	}
	return cooperationParticipations;
    }

    public Set<Cooperation> getAssociatedCooperations(ExecutionYear begin, ExecutionYear end) {
	Set<Cooperation> cooperations = new HashSet<Cooperation>();
	for (CooperationParticipation participation : getCooperationParticipations(begin, end)) {
	    cooperations.add(participation.getCooperation());
	}
	return cooperations;
    }

    public Set<Cooperation> getAssociatedCooperations() {
	return getAssociatedCooperations(null, null);
    }

    public List<? extends PartyContact> getPartyContacts(final Class<? extends PartyContact> clazz, final PartyContactType type) {
	final List<PartyContact> result = new ArrayList<PartyContact>();
	for (final PartyContact contact : getPartyContactsSet()) {
	    if (clazz.isAssignableFrom(contact.getClass()) && (type == null || contact.getType() == type)) {
		result.add(contact);
	    }
	}
	return result;
    }

    public List<? extends PartyContact> getPartyContacts(final Class<? extends PartyContact> clazz) {
	return getPartyContacts(clazz, null);
    }

    public boolean hasAnyPartyContact(final Class<? extends PartyContact> clazz, final PartyContactType type) {
	for (final PartyContact contact : getPartyContactsSet()) {
	    if (clazz.isAssignableFrom(contact.getClass()) && (type == null || contact.getType() == type)) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasAnyPartyContact(final Class<? extends PartyContact> clazz) {
	return hasAnyPartyContact(clazz, null);
    }

    public PartyContact getDefaultPartyContact(final Class<? extends PartyContact> clazz) {
	for (final PartyContact contact : getPartyContactsSet()) {
	    if (clazz.isAssignableFrom(contact.getClass()) && contact.isDefault()) {
		return contact;
	    }
	}
	return null;
    }

    public boolean hasDefaultPartyContact(final Class<? extends PartyContact> clazz) {
	return getDefaultPartyContact(clazz) != null;
    }

    public PartyContact getInstitutionalPartyContact(final Class<? extends PartyContact> clazz) {
	List<EmailAddress> institutionals = (List<EmailAddress>) getPartyContacts(EmailAddress.class,
		PartyContactType.INSTITUTIONAL);
	return institutionals.isEmpty() ? null : institutionals.get(0);
    }

    public boolean hasInstitutionalPartyContact(final Class<? extends PartyContact> clazz) {
	return getInstitutionalPartyContact(clazz) != null;
    }

    /*
     * WebAddress
     */
    public WebAddress getDefaultWebAddress() {
	return (WebAddress) getDefaultPartyContact(WebAddress.class);
    }

    public boolean hasDefaultWebAddress() {
	return hasDefaultPartyContact(WebAddress.class);
    }

    public List<WebAddress> getWebAddresses() {
	return (List<WebAddress>) getPartyContacts(WebAddress.class);
    }

    @Deprecated
    private WebAddress getOrCreateDefaultWebAddress() {
	final WebAddress webAddress = getDefaultWebAddress();
	return webAddress != null ? webAddress : PartyContact.createDefaultPersonalWebAddress(this, null);
    }

    protected WebAddress createDefaultWebAddress(final String url) {
	return (!StringUtils.isEmpty(url)) ? PartyContact.createDefaultPersonalWebAddress(this, url) : null;
    }

    public void updateDefaultWebAddress(final String url) {
	if (hasDefaultWebAddress()) {
	    getDefaultWebAddress().edit(url);
	} else {
	    createDefaultWebAddress(url);
	}
    }

    @Deprecated
    public String getWebAddress() {
	final WebAddress webAddress = getDefaultWebAddress();
	return webAddress != null ? webAddress.getUrl() : StringUtils.EMPTY;
    }

    @Deprecated
    public void setWebAddress(String webAddress) {
	updateDefaultWebAddress(webAddress);
    }

    /*
     * Phone
     */
    public Phone getDefaultPhone() {
	return (Phone) getDefaultPartyContact(Phone.class);
    }

    public boolean hasDefaultPhone() {
	return hasDefaultPartyContact(Phone.class);
    }

    public List<Phone> getPhones() {
	return (List<Phone>) getPartyContacts(Phone.class);
    }

    @Deprecated
    private Phone getOrCreateDefaultPhone() {
	final Phone phone = getDefaultPhone();
	return phone != null ? phone : (Phone) PartyContact.createDefaultPersonalPhone(this, null);
    }

    protected Phone createDefaultPhone(final String number) {
	return (!StringUtils.isEmpty(number)) ? PartyContact.createDefaultPersonalPhone(this, number) : null;
    }

    protected void updateDefaultPhone(final String number) {
	if (hasDefaultPhone()) {
	    getDefaultPhone().edit(number);
	} else {
	    createDefaultPhone(number);
	}
    }

    @Deprecated
    public String getPhone() {
	final Phone phone = getDefaultPhone();
	return phone != null ? phone.getNumber() : StringUtils.EMPTY;
    }

    @Deprecated
    public void setPhone(String phone) {
	updateDefaultPhone(phone);
    }

    /*
     * MobilePhone
     */
    public MobilePhone getDefaultMobilePhone() {
	return (MobilePhone) getDefaultPartyContact(MobilePhone.class);
    }

    public boolean hasDefaultMobilePhone() {
	return hasDefaultPartyContact(MobilePhone.class);
    }

    public List<MobilePhone> getMobilePhones() {
	return (List<MobilePhone>) getPartyContacts(MobilePhone.class);
    }

    @Deprecated
    private MobilePhone getOrCreateDefaultMobilePhone() {
	final MobilePhone mobilePhone = getDefaultMobilePhone();
	return mobilePhone != null ? mobilePhone : (MobilePhone) PartyContact.createDefaultPersonalMobilePhone(this, null);
    }

    protected MobilePhone createDefaultMobilePhone(final String number) {
	return (!StringUtils.isEmpty(number)) ? PartyContact.createDefaultPersonalMobilePhone(this, number) : null;
    }

    public void updateDefaultMobilePhone(final String number) {
	if (hasDefaultMobilePhone()) {
	    getDefaultMobilePhone().edit(number);
	} else {
	    createDefaultMobilePhone(number);
	}
    }

    @Deprecated
    public String getMobile() {
	final MobilePhone phone = getDefaultMobilePhone();
	return phone != null ? phone.getNumber() : StringUtils.EMPTY;
    }

    @Deprecated
    public void setMobile(String mobile) {
	updateDefaultMobilePhone(mobile);
    }

    /*
     * EmailAddress
     */

    public boolean hasDefaultEmailAddress() {
	return hasDefaultPartyContact(EmailAddress.class);
    }

    protected EmailAddress createDefaultEmailAddress(final String value) {
	return (!StringUtils.isEmpty(value)) ? PartyContact.createDefaultPersonalEmailAddress(this, value) : null;
    }

    @Deprecated
    private EmailAddress getOrCreateDefaultEmailAddress() {
	final EmailAddress emailAddress = getDefaultEmailAddress();
	return emailAddress != null ? emailAddress : PartyContact.createDefaultPersonalEmailAddress(this, null);
    }

    public void updateDefaultEmailAddress(final String email) {
	if (hasDefaultEmailAddress()) {
	    getDefaultEmailAddress().edit(email);
	} else {
	    createDefaultEmailAddress(email);
	}
    }

    public EmailAddress getDefaultEmailAddress() {
	return (EmailAddress) getDefaultPartyContact(EmailAddress.class);
    }

    public EmailAddress getInstitutionalEmailAddress() {
	return (EmailAddress) getInstitutionalPartyContact(EmailAddress.class);
    }

    public EmailAddress getInstitutionalOrDefaultEmailAddress() {
	return hasInstitutionalEmailAddress() ? getInstitutionalEmailAddress() : getDefaultEmailAddress();
    }

    public String getInstitutionalOrDefaultEmailAddressValue() {
	EmailAddress email = getInstitutionalOrDefaultEmailAddress();
	return (email != null ? email.getValue() : "");
    }

    public boolean hasInstitutionalEmailAddress() {
	return hasInstitutionalPartyContact(EmailAddress.class);
    }

    public List<EmailAddress> getEmailAddresses() {
	return (List<EmailAddress>) getPartyContacts(EmailAddress.class);
    }

    /**
     * @deprecated Use {@link getDefaultEmailAddress}
     */
    @Deprecated
    public String getEmail() {
	final EmailAddress emailAddress = getDefaultEmailAddress();
	return emailAddress != null ? emailAddress.getValue() : StringUtils.EMPTY;
    }

    @Deprecated
    public void setEmail(String email) {
	updateDefaultEmailAddress(email);
    }

    /*
     * PhysicalAddress
     */
    protected PhysicalAddress createDefaultPhysicalAddress(final PhysicalAddressData data) {
	return (data != null) ? PartyContact.createDefaultPersonalPhysicalAddress(this, data) : null;
    }

    protected void updateDefaultPhysicalAddress(final PhysicalAddressData data) {
	if (hasDefaultPhysicalAddress())
	    getDefaultPhysicalAddress().edit(data);
	else
	    createDefaultPhysicalAddress(data);
    }

    @Deprecated
    private PhysicalAddress getOrCreateDefaultPhysicalAddress() {
	final PhysicalAddress physicalAdress = getDefaultPhysicalAddress();
	return physicalAdress != null ? physicalAdress : PartyContact.createDefaultPersonalPhysicalAddress(this, null);
    }

    public PhysicalAddress getDefaultPhysicalAddress() {
	return (PhysicalAddress) getDefaultPartyContact(PhysicalAddress.class);
    }

    public boolean hasDefaultPhysicalAddress() {
	return hasDefaultPartyContact(PhysicalAddress.class);
    }

    public List<PhysicalAddress> getPhysicalAddresses() {
	return (List<PhysicalAddress>) getPartyContacts(PhysicalAddress.class);
    }

    @Deprecated
    public String getAddress() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getAddress() : StringUtils.EMPTY;
    }

    @Deprecated
    public void setAddress(String address) {
	getOrCreateDefaultPhysicalAddress().setAddress(address);
    }

    public String getAreaCode() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getAreaCode() : StringUtils.EMPTY;
    }

    public void setAreaCode(String areaCode) {
	getOrCreateDefaultPhysicalAddress().setAreaCode(areaCode);
    }

    public String getAreaOfAreaCode() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getAreaOfAreaCode() : StringUtils.EMPTY;
    }

    public void setAreaOfAreaCode(String areaOfAreaCode) {
	getOrCreateDefaultPhysicalAddress().setAreaOfAreaCode(areaOfAreaCode);
    }

    public String getPostalCode() {
	final StringBuilder result = new StringBuilder();
	if (getDefaultPhysicalAddress() != null) {
	    result.append(getDefaultPhysicalAddress().getAreaCode());
	    result.append(" ");
	    result.append(getDefaultPhysicalAddress().getAreaOfAreaCode());
	}
	return result.toString();
    }

    public String getArea() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getArea() : StringUtils.EMPTY;
    }

    public void setArea(String area) {
	getOrCreateDefaultPhysicalAddress().setArea(area);
    }

    public String getParishOfResidence() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getParishOfResidence() : StringUtils.EMPTY;
    }

    public void setParishOfResidence(String parishOfResidence) {
	getOrCreateDefaultPhysicalAddress().setParishOfResidence(parishOfResidence);
    }

    public String getDistrictSubdivisionOfResidence() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getDistrictSubdivisionOfResidence() : StringUtils.EMPTY;
    }

    public void setDistrictSubdivisionOfResidence(String districtSubdivisionOfResidence) {
	getOrCreateDefaultPhysicalAddress().setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
    }

    public String getDistrictOfResidence() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getDistrictOfResidence() : StringUtils.EMPTY;
    }

    public void setDistrictOfResidence(String districtOfResidence) {
	getOrCreateDefaultPhysicalAddress().setDistrictOfResidence(districtOfResidence);
    }

    public Country getCountryOfResidence() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getCountryOfResidence() : null;
    }

    public void setCountryOfResidence(Country countryOfResidence) {
	getOrCreateDefaultPhysicalAddress().setCountryOfResidence(countryOfResidence);
    }

    protected List<ResearchResultPublication> filterArticlesWithType(List<ResearchResultPublication> publications,
	    ScopeType locationType) {
	List<ResearchResultPublication> publicationsOfType = new ArrayList<ResearchResultPublication>();
	for (ResearchResultPublication publication : publications) {
	    Article article = (Article) publication;
	    if (article.getScope().equals(locationType)) {
		publicationsOfType.add(publication);
	    }
	}
	return publicationsOfType;
    }

    protected List<ResearchResultPublication> filterInproceedingsWithType(List<ResearchResultPublication> publications,
	    ScopeType locationType) {
	List<ResearchResultPublication> publicationsOfType = new ArrayList<ResearchResultPublication>();
	for (ResearchResultPublication publication : publications) {
	    Inproceedings inproceedings = (Inproceedings) publication;
	    if (inproceedings.getScope().equals(locationType)) {
		publicationsOfType.add(publication);
	    }
	}
	return publicationsOfType;
    }

    protected List<ResearchResultPublication> filterResultPublicationsByType(
	    final Class<? extends ResearchResultPublication> clazz, List<ResearchResultPublication> publications) {
	return (List<ResearchResultPublication>) CollectionUtils.select(publications, new Predicate() {
	    public boolean evaluate(Object arg0) {
		return clazz.equals(arg0.getClass());
	    }
	});
    }

    protected List<ResearchResultPublication> getResearchResultPublicationsByType(
	    final Class<? extends ResearchResultPublication> clazz) {
	return filterResultPublicationsByType(clazz, getResearchResultPublications());
    }

    protected List<ResearchResultPublication> getResearchResultPublicationsByType(
	    final Class<? extends ResearchResultPublication> clazz, ExecutionYear executionYear) {
	return filterResultPublicationsByType(clazz, getResearchResultPublicationsByExecutionYear(executionYear));
    }

    protected List<ResearchResultPublication> getResearchResultPublicationsByType(
	    final Class<? extends ResearchResultPublication> clazz, ExecutionYear firstExecutionYear,
	    ExecutionYear lastExecutionYear) {
	return filterResultPublicationsByType(clazz, getResearchResultPublicationsByExecutionYear(firstExecutionYear,
		lastExecutionYear));
    }

    public List<ResearchResultPublication> getResearchResultPublicationsByExecutionYear(ExecutionYear executionYear) {

	List<ResearchResultPublication> publicationsForExecutionYear = new ArrayList<ResearchResultPublication>();
	for (ResearchResultPublication publication : getResearchResultPublications()) {
	    if (publication.getYear() == null || executionYear.belongsToCivilYear(publication.getYear())) {
		publicationsForExecutionYear.add(publication);
	    }
	}
	return publicationsForExecutionYear;
    }

    protected List<ResearchResultPublication> getResearchResultPublicationsByExecutionYear(ExecutionYear firstExecutionYear,
	    ExecutionYear lastExecutionYear) {

	List<ResearchResultPublication> publicationsForExecutionYear = new ArrayList<ResearchResultPublication>();

	if (firstExecutionYear == null) {
	    firstExecutionYear = ExecutionYear.readFirstExecutionYear();
	}
	if (lastExecutionYear == null || lastExecutionYear.isBefore(firstExecutionYear)) {
	    lastExecutionYear = ExecutionYear.readLastExecutionYear();
	}

	for (ResearchResultPublication publication : getResearchResultPublications()) {
	    if (publication.getYear() == null
		    || ((firstExecutionYear.isBeforeCivilYear(publication.getYear()) || firstExecutionYear
			    .belongsToCivilYear(publication.getYear())) && (lastExecutionYear.isAfterCivilYear(publication
			    .getYear()) || lastExecutionYear.belongsToCivilYear(publication.getYear())))) {
		publicationsForExecutionYear.add(publication);
	    }

	}

	return publicationsForExecutionYear;
    }

    public List<Prize> getPrizes(ExecutionYear executionYear) {
	List<Prize> prizes = new ArrayList<Prize>();
	for (Prize prize : this.getPrizes()) {
	    if (executionYear.belongsToCivilYear(prize.getYear())) {
		prizes.add(prize);
	    }
	}
	return prizes;
    }

    public abstract List<ResearchResultPublication> getResearchResultPublications();

    public List<ResearchResultPublication> getBooks() {
	return this.getResearchResultPublicationsByType(Book.class);
    }

    public List<ResearchResultPublication> getBooks(ExecutionYear executionYear) {
	return this.getResearchResultPublicationsByType(Book.class, executionYear);
    }

    public List<ResearchResultPublication> getBooks(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
	return this.getResearchResultPublicationsByType(Book.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getInbooks() {
	return this.getResearchResultPublicationsByType(BookPart.class);
    }

    public List<ResearchResultPublication> getInbooks(ExecutionYear executionYear) {
	return this.getResearchResultPublicationsByType(BookPart.class, executionYear);
    }

    public List<ResearchResultPublication> getInbooks(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
	return this.getResearchResultPublicationsByType(BookPart.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getArticles(ScopeType locationType) {
	return filterArticlesWithType(this.getResearchResultPublicationsByType(Article.class), locationType);
    }

    public List<ResearchResultPublication> getArticles(ScopeType locationType, ExecutionYear executionYear) {
	return filterArticlesWithType(this.getResearchResultPublicationsByType(Article.class, executionYear), locationType);
    }

    public List<ResearchResultPublication> getArticles(ScopeType locationType, ExecutionYear firstExecutionYear,
	    ExecutionYear lastExecutionYear) {
	return filterArticlesWithType(this.getResearchResultPublicationsByType(Article.class, firstExecutionYear,
		lastExecutionYear), locationType);
    }

    public List<ResearchResultPublication> getArticles() {
	return this.getResearchResultPublicationsByType(Article.class);
    }

    public List<ResearchResultPublication> getArticles(ExecutionYear executionYear) {
	return this.getResearchResultPublicationsByType(Article.class, executionYear);
    }

    public List<ResearchResultPublication> getArticles(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
	return this.getResearchResultPublicationsByType(Article.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getInproceedings(ScopeType locationType) {
	return filterInproceedingsWithType(this.getResearchResultPublicationsByType(Inproceedings.class), locationType);
    }

    public List<ResearchResultPublication> getInproceedings(ScopeType locationType, ExecutionYear executionYear) {
	return filterInproceedingsWithType(this.getResearchResultPublicationsByType(Inproceedings.class, executionYear),
		locationType);
    }

    public List<ResearchResultPublication> getInproceedings(ScopeType locationType, ExecutionYear firstExecutionYear,
	    ExecutionYear lastExecutionYear) {
	return filterInproceedingsWithType(this.getResearchResultPublicationsByType(Inproceedings.class, firstExecutionYear,
		lastExecutionYear), locationType);
    }

    public List<ResearchResultPublication> getInproceedings() {
	return this.getResearchResultPublicationsByType(Inproceedings.class);
    }

    public List<ResearchResultPublication> getInproceedings(ExecutionYear executionYear) {
	return this.getResearchResultPublicationsByType(Inproceedings.class, executionYear);
    }

    public List<ResearchResultPublication> getInproceedings(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
	return this.getResearchResultPublicationsByType(Inproceedings.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getProceedings() {
	return this.getResearchResultPublicationsByType(Proceedings.class);
    }

    public List<ResearchResultPublication> getProceedings(ExecutionYear executionYear) {
	return this.getResearchResultPublicationsByType(Proceedings.class, executionYear);
    }

    public List<ResearchResultPublication> getProceedings(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
	return this.getResearchResultPublicationsByType(Proceedings.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getTheses() {
	return this.getResearchResultPublicationsByType(Thesis.class);
    }

    public List<ResearchResultPublication> getTheses(ExecutionYear executionYear) {
	return this.getResearchResultPublicationsByType(Thesis.class, executionYear);
    }

    public List<ResearchResultPublication> getTheses(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
	return this.getResearchResultPublicationsByType(Thesis.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getManuals() {
	return this.getResearchResultPublicationsByType(Manual.class);
    }

    public List<ResearchResultPublication> getManuals(ExecutionYear executionYear) {
	return this.getResearchResultPublicationsByType(Manual.class, executionYear);
    }

    public List<ResearchResultPublication> getManuals(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
	return this.getResearchResultPublicationsByType(Manual.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getTechnicalReports() {
	return ResearchResultPublication.sort(this.getResearchResultPublicationsByType(TechnicalReport.class));
    }

    public List<ResearchResultPublication> getTechnicalReports(ExecutionYear executionYear) {
	return this.getResearchResultPublicationsByType(TechnicalReport.class, executionYear);
    }

    public List<ResearchResultPublication> getTechnicalReports(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
	return this.getResearchResultPublicationsByType(TechnicalReport.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getOtherPublications() {
	return this.getResearchResultPublicationsByType(OtherPublication.class);
    }

    public List<ResearchResultPublication> getOtherPublications(ExecutionYear executionYear) {
	return this.getResearchResultPublicationsByType(OtherPublication.class, executionYear);
    }

    public List<ResearchResultPublication> getOtherPublications(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
	return this.getResearchResultPublicationsByType(OtherPublication.class, firstExecutionYear, lastExecutionYear);
    }

    public List<ResearchResultPublication> getUnstructureds() {
	return this.getResearchResultPublicationsByType(Unstructured.class);
    }

    public List<ResearchResultPublication> getUnstructureds(ExecutionYear executionYear) {
	return this.getResearchResultPublicationsByType(Unstructured.class, executionYear);
    }

    public List<ResearchResultPublication> getUnstructureds(ExecutionYear firstExecutionYear, ExecutionYear lastExecutionYear) {
	return this.getResearchResultPublicationsByType(Unstructured.class, firstExecutionYear, lastExecutionYear);
    }

    //
    // Site
    //

    public abstract Site getSite();

    protected abstract Site createSite();

    /**
     * Initializes the party's site. This method ensures that if the party has a
     * site then no other is created and that site is returned. Nevertheless if
     * the party does not have a site, it is asked to create one by calling
     * {@link #createSite()}. This allows each specific party to create the
     * appropriate site.
     * 
     * @return the newly created site or, if this party already contains a site,
     *         the currently existing one(publication.getYear())
     */
    public Site initializeSite() {
	Site site = getSite();

	if (site != null) {
	    return site;
	} else {
	    return createSite();
	}
    }

    @Override
    public int compareTo(Party party) {
	return COMPARATOR_BY_NAME.compare(this, party);
    }
}
