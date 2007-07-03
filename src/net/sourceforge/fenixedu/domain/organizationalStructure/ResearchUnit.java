package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.ResearchUnitElementGroup;
import net.sourceforge.fenixedu.domain.accessControl.ResearchUnitMembersGroup;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class ResearchUnit extends ResearchUnit_Base {

    private ResearchUnit() {
	super();
	super.setType(PartyTypeEnum.RESEARCH_UNIT);
    }

    public static ResearchUnit createNewResearchUnit(String name, Integer costCenterCode,
	    String acronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
	    AccountabilityType accountabilityType, String webAddress, UnitClassification classification,
	    Boolean canBeResponsibleOfSpaces, Campus campus) {

	ResearchUnit researchUnit = new ResearchUnit();
	researchUnit.init(name, costCenterCode, acronym, beginDate, endDate, webAddress, classification, canBeResponsibleOfSpaces, campus);
	researchUnit.addParentUnit(parentUnit, accountabilityType);

	checkIfAlreadyExistsOneResearchUnitWithSameNameOrAcronym(researchUnit);

	return researchUnit;
    }

    @Override
    public void setAcronym(String acronym) {
	if (StringUtils.isEmpty(acronym)) {
	    throw new DomainException("acronym.cannot.be.null");
	}
	super.setAcronym(acronym);
    }

    @Override
    public void edit(String unitName, Integer unitCostCenter, String acronym, YearMonthDay beginDate,
	    YearMonthDay endDate, String webAddress, UnitClassification classification,
	    Department department, Degree degree, AdministrativeOffice administrativeOffice,
	    Boolean canBeResponsibleOfSpaces, Campus campus) {

	super.edit(unitName, unitCostCenter, acronym, beginDate, endDate, webAddress, classification, department, degree, administrativeOffice, canBeResponsibleOfSpaces, campus);

	checkIfAlreadyExistsOneResearchUnitWithSameNameOrAcronym(this);
    }

    @Override
    public boolean isResearchUnit() {
	return true;
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
	throw new DomainException("unit.impossible.set.type");
    }

    private static void checkIfAlreadyExistsOneResearchUnitWithSameNameOrAcronym(
	    ResearchUnit researchUnit) {
	PartyType type = PartyType.readPartyTypeByType(PartyTypeEnum.RESEARCH_UNIT);
	for (Party party : type.getParties()) {
	    ResearchUnit unit = (ResearchUnit) party;
	    if (!unit.equals(researchUnit) && unit.isResearchUnit()
		    && researchUnit.getName().equalsIgnoreCase(unit.getName())
		    && researchUnit.getAcronym().equalsIgnoreCase(unit.getAcronym())) {
		throw new DomainException("error.unit.already.exists.unit.with.same.name.or.acronym");
	    }
	}

    }

    public Collection<Accountability> getResearchContracts() {
	return (Collection<Accountability>) getChildAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT);
    }

    public Collection<Accountability> getActiveResearchContracts(ResearchFunctionType type) {
	AccountabilityType accountabilityType = ResearchFunction
	.readAccountabilityTypeByResearchFunctionType(type);
	YearMonthDay today = new YearMonthDay();
	List<Accountability> accountabilities = new ArrayList<Accountability>();
	for (Accountability accountability : getChildsSet()) {
	    if (accountability.getAccountabilityType().equals(accountabilityType)
		    && (accountability.getEndDate() == null || accountability.getEndDate()
			    .isAfter(today))) {
		accountabilities.add(accountability);
	    }
	}
	return accountabilities;
    }

    @Override
    public Collection<Person> getPossibleGroupMembers() {
	List<Person> people = new ArrayList<Person> ();
	YearMonthDay today = new YearMonthDay();
	for (Accountability accountability : getChildsSet()) {
	    if (accountability instanceof ResearchContract && (accountability.getEndDate() == null || accountability.getEndDate()
		    .isAfter(today))) {
		people.add(((ResearchContract)accountability).getPerson());
	    }
	}
	return people;
    }

    public Collection<Unit> getAllCurrentActiveSubUnits() {
	return this.getAllActiveSubUnits(new YearMonthDay(), AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE);
    }

    public Collection<Accountability> getPermanentResearchContracts() {
	return getActiveResearchContracts(ResearchFunctionType.PERMANENT_RESEARCHER);
    }

    public Collection<Accountability> getInvitedResearchContracts() {
	return getActiveResearchContracts(ResearchFunctionType.INVITED_RESEARCHER);
    }

    public Collection<Accountability> getOtherStaffContracts() {
	return getActiveResearchContracts(ResearchFunctionType.OTHER_STAFF);
    }

    public Collection<Accountability> getTechnicalStaffContracts() {
	return getActiveResearchContracts(ResearchFunctionType.TECHNICAL_STAFF);
    }

    public Collection<Accountability> getCollaboratorsContracts() {
	return getActiveResearchContracts(ResearchFunctionType.COLLABORATORS);
    }

    public Collection<Accountability> getMsCStudentsContracts() {
	return getActiveResearchContracts(ResearchFunctionType.MSC_STUDENT);
    }

    public Collection<Accountability> getPhDStudentsContracts() {
	return getActiveResearchContracts(ResearchFunctionType.PHD_STUDENT);
    }

    @Override
    public List<IGroup> getDefaultGroups() {
	List<IGroup> groups = super.getDefaultGroups();

	groups.add(new ResearchUnitMembersGroup(this, ResearchFunctionType.COLLABORATORS));
	groups.add(new ResearchUnitMembersGroup(this, ResearchFunctionType.INVITED_RESEARCHER));
	groups.add(new ResearchUnitMembersGroup(this, ResearchFunctionType.MSC_STUDENT));
	groups.add(new ResearchUnitMembersGroup(this, ResearchFunctionType.OTHER_STAFF));
	groups.add(new ResearchUnitMembersGroup(this, ResearchFunctionType.PERMANENT_RESEARCHER));
	groups.add(new ResearchUnitMembersGroup(this, ResearchFunctionType.PHD_STUDENT));
	groups.add(new ResearchUnitMembersGroup(this, ResearchFunctionType.POST_DOC_STUDENT));
	groups.add(new ResearchUnitMembersGroup(this, ResearchFunctionType.TECHNICAL_STAFF));

	groups.add(new ResearchUnitElementGroup(this));

	return groups;
    }

    public boolean isUserAbleToInsertOthersPublications(Person person) {
	return getPublicationCollaborators().contains(person);
    }

    public boolean isCurrentUserAbleToInsertOthersPublications() {
	return isUserAbleToInsertOthersPublications(AccessControl.getPerson());
    }

    public void setPublicationCollaborators(List<Person> collaborators) {
	getPublicationCollaborators().clear();
	getPublicationCollaborators().addAll(collaborators);
    }
}
