/*
 * Created on Sep 16, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.parking.ParkingPartyClassification;
import net.sourceforge.fenixedu.domain.teacher.TeacherLegalRegimen;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.util.ContractType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class Unit extends Unit_Base {

    public final static Comparator<Unit> UNIT_COMPARATOR_BY_NAME = new ComparatorChain();
    static {
	((ComparatorChain) UNIT_COMPARATOR_BY_NAME).addComparator(new BeanComparator("name", Collator
		.getInstance()));
	((ComparatorChain) UNIT_COMPARATOR_BY_NAME).addComparator(new BeanComparator("idInternal"));
    }

    private Unit() {
	super();
    }

    public void edit(String unitName, Integer unitCostCenter, String acronym, Date beginDate,
	    Date endDate, PartyTypeEnum type, String webAddress) {

	checkUnitDates(beginDate, endDate);
	setCostCenterCode(unitCostCenter);
	checkAcronym(acronym, this.getType());
	setName(unitName);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setType(type);
	setWebAddress(webAddress);
	setAcronym(acronym);
    }

    private void checkCostCenterCode(Integer costCenterCode) {
	Unit unit = readByCostCenterCode(costCenterCode);
	if (unit != null && !unit.equals(this)) {
	    throw new DomainException("error.costCenter.alreadyExists");
	}
    }

    private void checkUnitDates(Date beginDate, Date endDate) {
	if (beginDate == null) {
	    throw new DomainException("error.unit.no.beginDate");
	}
	if (endDate != null && endDate.before(beginDate)) {
	    throw new DomainException("error.unit.endDateBeforeBeginDate");
	}
    }

    private void checkAcronym(String acronym, PartyTypeEnum partyTypeEnum) {
	Unit unit = readUnitByAcronymAndType(acronym, partyTypeEnum);
	if (unit != null && !unit.equals(this)) {
	    throw new DomainException("error.existent.acronym");
	}
    }

    public void delete() {
	if (!canBeDeleted()) {
	    throw new DomainException("error.unit.cannot.be.deleted");
	}

	if (hasAnyParentUnits()) {
	    this.getParents().get(0).delete();
	}

	for (; !getParticipatingAnyCurricularCourseCurricularRules().isEmpty(); getParticipatingAnyCurricularCourseCurricularRules()
		.get(0).delete())
	    ;
	removeDepartment();
	removeDegree();
	super.delete();
    }

    private boolean canBeDeleted() {
	return (!hasAnyParents() || (this.getParentUnits().size() == 1 && this.getParents().size() == 1))
		&& !hasAnyFunctions()
		&& !hasAnyChilds()
		&& !hasAnySpaceResponsibility()
		&& !hasAnyMaterials()
		&& !hasAnyVigilantGroups()
		&& !hasAnyCompetenceCourses()
		&& !hasAnyAssociatedNonAffiliatedTeachers()
		&& !hasAnyPayedGuides()
		&& !hasAnyPayedReceipts()
		&& !hasAdministrativeOffice()
		&& !hasUnitServiceAgreementTemplate();
    }

    public boolean isActive(YearMonthDay currentDate) {
	return (!this.getBeginDateYearMonthDay().isAfter(currentDate) && (this.getEndDateYearMonthDay() == null || !this
		.getEndDateYearMonthDay().isBefore(currentDate)));
    }

    public List<Unit> getTopUnits() {
	Unit unit = this;
	List<Unit> allTopUnits = new ArrayList<Unit>();
	if (unit.hasAnyParentUnits()) {
	    for (Unit parentUnit : this.getParentUnits()) {
		if (!parentUnit.hasAnyParentUnits() && !allTopUnits.contains(parentUnit)) {
		    allTopUnits.add(parentUnit);
		} else if (parentUnit.hasAnyParentUnits()) {
		    for (Unit parentUnit2 : parentUnit.getTopUnits()) {
			if (!allTopUnits.contains(parentUnit2)) {
			    allTopUnits.add(parentUnit2);
			}
		    }
		}
	    }
	}
	return allTopUnits;
    }

    public Unit getDepartmentUnit() {
	Collection<Unit> parentUnits = this.getParentUnits();
	if (isUnitDepartment(this)) {
	    return this;
	} else if (!parentUnits.isEmpty()) {
	    for (Unit parentUnit : parentUnits) {
		if (isUnitDepartment(parentUnit)) {
		    return parentUnit;
		} else if (parentUnit.hasAnyParentUnits()) {
		    Unit departmentUnit = parentUnit.getDepartmentUnit();
		    if (departmentUnit == null) {
			continue;
		    } else {
			return departmentUnit;
		    }
		}
	    }
	}
	return null;
    }

    private boolean isUnitDepartment(Unit unit) {
	return (unit.getType() != null && unit.getType().equals(PartyTypeEnum.DEPARTMENT) && unit
		.getDepartment() != null);
    }

    public List<Unit> getInactiveSubUnits(YearMonthDay currentDate) {
	return getSubUnitsByState(currentDate, false);
    }

    public List<Unit> getActiveSubUnits(YearMonthDay currentDate) {
	return getSubUnitsByState(currentDate, true);
    }

    private List<Unit> getSubUnitsByState(YearMonthDay currentDate, boolean state) {
	List<Unit> allSubUnits = new ArrayList<Unit>();
	for (Unit subUnit : this.getSubUnits()) {
	    if (subUnit.isActive(currentDate) == state) {
		allSubUnits.add(subUnit);
	    }
	}
	return allSubUnits;
    }

    public List<Unit> getInactiveParentUnits(YearMonthDay currentDate) {
	return getParentUnitsByState(currentDate, false);
    }

    public List<Unit> getActiveParentUnits(YearMonthDay currentDate) {
	return getParentUnitsByState(currentDate, true);
    }

    private List<Unit> getParentUnitsByState(YearMonthDay currentDate, boolean state) {
	List<Unit> allParentUnits = new ArrayList<Unit>();
	for (Unit subUnit : this.getParentUnits()) {
	    if (subUnit.isActive(currentDate) == state) {
		allParentUnits.add(subUnit);
	    }
	}
	return allParentUnits;
    }

    public List<Unit> getInactiveSubUnits(YearMonthDay currentDate,
	    AccountabilityTypeEnum accountabilityTypeEnum) {
	return getSubUnitsByState(currentDate, accountabilityTypeEnum, false);
    }

    public List<Unit> getActiveSubUnits(YearMonthDay currentDate,
	    AccountabilityTypeEnum accountabilityTypeEnum) {
	return getSubUnitsByState(currentDate, accountabilityTypeEnum, true);
    }

    private List<Unit> getSubUnitsByState(YearMonthDay currentDate,
	    AccountabilityTypeEnum accountabilityTypeEnum, boolean state) {
	List<Unit> allSubUnits = new ArrayList<Unit>();
	for (Unit subUnit : getSubUnits(accountabilityTypeEnum)) {
	    if (subUnit.isActive(currentDate) == state) {
		allSubUnits.add(subUnit);
	    }
	}
	return allSubUnits;
    }

    public List<Unit> getActiveSubUnits(YearMonthDay currentDate,
	    List<AccountabilityTypeEnum> accountabilityTypeEnums) {
	return getSubUnitsByState(currentDate, accountabilityTypeEnums, true);
    }

    public List<Unit> getInactiveSubUnits(YearMonthDay currentDate,
	    List<AccountabilityTypeEnum> accountabilityTypeEnums) {
	return getSubUnitsByState(currentDate, accountabilityTypeEnums, false);
    }

    private List<Unit> getSubUnitsByState(YearMonthDay currentDate,
	    List<AccountabilityTypeEnum> accountabilityTypeEnums, boolean state) {
	List<Unit> allSubUnits = new ArrayList<Unit>();
	for (Unit subUnit : this.getSubUnits(accountabilityTypeEnums)) {
	    if (subUnit.isActive(currentDate) == state) {
		allSubUnits.add(subUnit);
	    }
	}
	return allSubUnits;
    }

    public List<Unit> getAllInactiveParentUnits(YearMonthDay currentDate) {
	Set<Unit> allInactiveParentUnits = new HashSet<Unit>();
	List<Unit> inactiveParentUnits = getInactiveParentUnits(currentDate);
	allInactiveParentUnits.addAll(inactiveParentUnits);
	for (Unit subUnit : inactiveParentUnits) {
	    allInactiveParentUnits.addAll(subUnit.getAllInactiveParentUnits(currentDate));
	}
	return new ArrayList<Unit>(allInactiveParentUnits);
    }

    public List<Unit> getAllActiveParentUnits(YearMonthDay currentDate) {
	Set<Unit> allActiveParentUnits = new HashSet<Unit>();
	List<Unit> activeParentUnits = getActiveParentUnits(currentDate);
	allActiveParentUnits.addAll(activeParentUnits);
	for (Unit subUnit : activeParentUnits) {
	    allActiveParentUnits.addAll(subUnit.getAllActiveParentUnits(currentDate));
	}
	return new ArrayList<Unit>(allActiveParentUnits);
    }

    public List<Unit> getAllInactiveSubUnits(YearMonthDay currentDate) {
	Set<Unit> allInactiveSubUnits = new HashSet<Unit>();
	List<Unit> inactiveSubUnits = getInactiveSubUnits(currentDate);
	allInactiveSubUnits.addAll(inactiveSubUnits);
	for (Unit subUnit : inactiveSubUnits) {
	    allInactiveSubUnits.addAll(subUnit.getAllInactiveSubUnits(currentDate));
	}
	return new ArrayList<Unit>(allInactiveSubUnits);
    }

    public List<Unit> getAllActiveSubUnits(YearMonthDay currentDate) {
	Set<Unit> allActiveSubUnits = new HashSet<Unit>();
	List<Unit> activeSubUnits = getActiveSubUnits(currentDate);
	allActiveSubUnits.addAll(activeSubUnits);
	for (Unit subUnit : activeSubUnits) {
	    allActiveSubUnits.addAll(subUnit.getAllActiveSubUnits(currentDate));
	}
	return new ArrayList<Unit>(allActiveSubUnits);
    }

    public List<Unit> getAllActiveSubUnits(YearMonthDay currentDate,
	    AccountabilityTypeEnum accountabilityTypeEnum) {
	Set<Unit> allActiveSubUnits = new HashSet<Unit>();
	List<Unit> activeSubUnits = getActiveSubUnits(currentDate, accountabilityTypeEnum);
	allActiveSubUnits.addAll(activeSubUnits);
	for (Unit subUnit : activeSubUnits) {
	    allActiveSubUnits.addAll(subUnit.getAllActiveSubUnits(currentDate));
	}
	return new ArrayList<Unit>(allActiveSubUnits);
    }

    public List<Unit> getAllInactiveSubUnits(YearMonthDay currentDate,
	    AccountabilityTypeEnum accountabilityTypeEnum) {
	Set<Unit> allInactiveSubUnits = new HashSet<Unit>();
	List<Unit> inactiveSubUnits = getInactiveSubUnits(currentDate, accountabilityTypeEnum);
	allInactiveSubUnits.addAll(inactiveSubUnits);
	for (Unit subUnit : inactiveSubUnits) {
	    allInactiveSubUnits.addAll(subUnit.getAllInactiveSubUnits(currentDate));
	}
	return new ArrayList<Unit>(allInactiveSubUnits);
    }

    public Collection<Unit> getAllSubUnits() {
	Set<Unit> allSubUnits = new HashSet<Unit>();
	Collection<Unit> subUnits = getSubUnits();
	allSubUnits.addAll(subUnits);
	for (Unit subUnit : subUnits) {
	    allSubUnits.addAll(subUnit.getAllSubUnits());
	}
	return allSubUnits;
    }

    public Collection<Unit> getAllParentUnits() {
	Set<Unit> allParentUnits = new HashSet<Unit>();
	Collection<Unit> parentUnits = getSubUnits();
	allParentUnits.addAll(parentUnits);
	for (Unit subUnit : parentUnits) {
	    allParentUnits.addAll(subUnit.getAllParentUnits());
	}
	return allParentUnits;
    }

    public Collection<Contract> getContracts() {
	return (Collection<Contract>) getChildAccountabilities(AccountabilityTypeEnum.EMPLOYEE_CONTRACT,
		Contract.class);
    }

    public List<Contract> getWorkingContracts(YearMonthDay begin, YearMonthDay end) {
	List<Contract> contracts = new ArrayList<Contract>();
	for (Contract contract : (Collection<Contract>) getChildAccountabilities(
		AccountabilityTypeEnum.EMPLOYEE_CONTRACT, Contract.class)) {
	    if (contract.getContractType().equals(ContractType.WORKING)
		    && contract.belongsToPeriod(begin, end)) {
		contracts.add(contract);
	    }
	}
	return contracts;
    }

    public List<Contract> getContractsByContractType(ContractType contractType) {
	List<Contract> contracts = new ArrayList<Contract>();
	for (Contract contract : (Collection<Contract>) getChildAccountabilities(
		AccountabilityTypeEnum.EMPLOYEE_CONTRACT, Contract.class)) {
	    if (contract.getContractType().equals(contractType)) {
		contracts.add(contract);
	    }
	}
	return contracts;
    }

    public List<Contract> getWorkingContracts() {
	return getContractsByContractType(ContractType.WORKING);
    }

    // begin SCIENTIFIC AREA UNITS, COMPETENCE COURSE GROUP UNITS AND
    // RELATED
    public List<Unit> getScientificAreaUnits() {
	final SortedSet<Unit> result = new TreeSet<Unit>(Unit.UNIT_COMPARATOR_BY_NAME);

	for (Unit unit : this.getSubUnits()) {
	    if (unit.getType() != null && unit.getType().equals(PartyTypeEnum.SCIENTIFIC_AREA)) {
		result.add(unit);
	    }
	}

	return new ArrayList<Unit>(result);
    }

    public Double getScientificAreaUnitEctsCredits() {
	double result = 0.0;
	for (Unit competenceCourseGroupUnit : getCompetenceCourseGroupUnits()) {
	    for (CompetenceCourse competenceCourse : competenceCourseGroupUnit.getCompetenceCourses()) {
		result += competenceCourse.getEctsCredits();
	    }
	}
	return result;
    }

    public Double getScientificAreaUnitEctsCredits(List<Context> contexts) {
	double result = 0.0;
	for (Context context : contexts) {
	    if (context.getChildDegreeModule().isLeaf()) {
		CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();

		if (!curricularCourse.isOptional()
			&& curricularCourse.getCompetenceCourse().getScientificAreaUnit().equals(this)) {
		    result += curricularCourse.getCompetenceCourse().getEctsCredits();
		}
	    }
	}
	return result;
    }

    public List<Unit> getCompetenceCourseGroupUnits() {
	final SortedSet<Unit> result = new TreeSet<Unit>(Unit.UNIT_COMPARATOR_BY_NAME);

	for (Unit unit : this.getSubUnits()) {
	    if (unit.getType() != null && unit.getType().equals(PartyTypeEnum.COMPETENCE_COURSE_GROUP)) {
		result.add(unit);
	    }
	}

	return new ArrayList<Unit>(result);
    }

    @Override
    public List<CompetenceCourse> getCompetenceCourses() {
	final SortedSet<CompetenceCourse> result = new TreeSet<CompetenceCourse>(
		CompetenceCourse.COMPETENCE_COURSE_COMPARATOR_BY_NAME);
	result.addAll(super.getCompetenceCourses());
	return new ArrayList<CompetenceCourse>(result);
    }

    // end SCIENTIFIC AREA UNITS, COMPETENCE COURSE GROUP UNITS AND RELATED

    public List<Teacher> getAllTeachers() {
	List<Teacher> teachers = new ArrayList<Teacher>();
	List<Employee> employees = getAllWorkingEmployees();
	for (Employee employee : employees) {
	    Teacher teacher = employee.getPerson().getTeacher();
	    if (teacher != null && !teacher.getAllLegalRegimensWithoutEndSituations().isEmpty()) {
		teachers.add(teacher);
	    }
	}
	return teachers;
    }

    public List<Teacher> getAllTeachers(YearMonthDay begin, YearMonthDay end) {
	List<Teacher> teachers = new ArrayList<Teacher>();
	List<Employee> employees = getAllWorkingEmployees(begin, end);
	for (Employee employee : employees) {
	    Teacher teacher = employee.getPerson().getTeacher();
	    if (teacher != null
		    && !teacher.getAllLegalRegimensWithoutEndSituations(begin, end).isEmpty()) {
		teachers.add(teacher);
	    }
	}
	return teachers;
    }

    public List<Teacher> getAllCurrentTeachers() {
	List<Teacher> teachers = new ArrayList<Teacher>();
	List<Employee> employees = getAllCurrentActiveWorkingEmployees();
	for (Employee employee : employees) {
	    Teacher teacher = employee.getPerson().getTeacher();
	    if (teacher != null) {
		TeacherLegalRegimen legalRegimen = teacher.getCurrentLegalRegimenWithoutEndSitutions();
		if (legalRegimen != null) {
		    teachers.add(teacher);
		}
	    }
	}
	return teachers;
    }

    public Teacher getTeacherByPeriod(Integer teacherNumber, YearMonthDay begin, YearMonthDay end) {
	for (Employee employee : getAllWorkingEmployees(begin, end)) {
	    Teacher teacher = employee.getPerson().getTeacher();
	    if (teacher != null && teacher.getTeacherNumber().equals(teacherNumber)
		    && !teacher.getAllLegalRegimensWithoutEndSituations(begin, end).isEmpty()) {
		return teacher;
	    }
	}
	return null;
    }

    public List<Employee> getAllWorkingEmployees() {
	Set<Employee> employees = new HashSet<Employee>();
	for (Contract contract : getWorkingContracts()) {
	    employees.add(contract.getEmployee());
	}
	for (Unit subUnit : getSubUnits()) {
	    employees.addAll(subUnit.getAllWorkingEmployees());
	}
	return new ArrayList<Employee>(employees);
    }

    public List<Employee> getAllWorkingEmployees(YearMonthDay begin, YearMonthDay end) {
	Set<Employee> employees = new HashSet<Employee>();
	for (Contract contract : getWorkingContracts(begin, end)) {
	    employees.add(contract.getEmployee());
	}
	for (Unit subUnit : getSubUnits()) {
	    employees.addAll(subUnit.getAllWorkingEmployees(begin, end));
	}
	return new ArrayList<Employee>(employees);
    }

    public List<Employee> getAllCurrentActiveWorkingEmployees() {
	Set<Employee> employees = new HashSet<Employee>();
	YearMonthDay currentDate = new YearMonthDay();
	for (Contract contract : getWorkingContracts()) {
	    Employee employee = contract.getEmployee();
	    if (employee.getActive().booleanValue() && contract.isActive(currentDate)) {
		employees.add(employee);
	    }
	}
	for (Unit subUnit : getSubUnits()) {
	    employees.addAll(subUnit.getAllCurrentActiveWorkingEmployees());
	}
	return new ArrayList<Employee>(employees);
    }

    public Collection<Unit> getParentUnits() {
	return (Collection<Unit>) getParentParties(getClass());
    }

    public Collection<Unit> getParentUnits(AccountabilityTypeEnum accountabilityTypeEnum) {
	return (Collection<Unit>) getParentParties(accountabilityTypeEnum, getClass());
    }

    public Collection<Unit> getParentUnits(List<AccountabilityTypeEnum> accountabilityTypeEnums) {
	return (Collection<Unit>) getParentParties(accountabilityTypeEnums, getClass());
    }

    public Collection<Unit> getSubUnits() {
	return (Collection<Unit>) getChildParties(getClass());
    }

    public Collection<Unit> getSubUnits(AccountabilityTypeEnum accountabilityTypeEnum) {
	return (Collection<Unit>) getChildParties(accountabilityTypeEnum, getClass());
    }

    public Collection<Unit> getSubUnits(List<AccountabilityTypeEnum> accountabilityTypeEnums) {
	return (Collection<Unit>) getChildParties(accountabilityTypeEnums, getClass());
    }

    public boolean hasAnyParentUnits() {
	return !getParentUnits().isEmpty();
    }

    public boolean hasAnySubUnits() {
	return !getSubUnits().isEmpty();
    }

    public Accountability addParentUnit(Unit parentUnit, AccountabilityType accountabilityType) {
	if (this.equals(parentUnit)) {
	    throw new DomainException("error.unit.equals.parentUnit");
	}
	if (getParentUnits(accountabilityType.getType()).contains(parentUnit)) {
	    throw new DomainException("error.unit.parentUnit.is.already.parentUnit");
	}
	YearMonthDay currentDate = new YearMonthDay();
	List<Unit> subUnits = (parentUnit.isActive(currentDate)) ? getAllActiveSubUnits(currentDate)
		: getAllInactiveSubUnits(currentDate);
	if (subUnits.contains(parentUnit)) {
	    throw new DomainException("error.unit.parentUnit.is.already.subUnit");
	}
	return new Accountability(parentUnit, this, accountabilityType);
    }

    public Accountability addSubUnit(Unit childUnit, AccountabilityType accountabilityType) {
	if (this.equals(childUnit)) {
	    throw new DomainException("error.unit.equals.subUnit");
	}
	if (this.getSubUnits(accountabilityType.getType()).contains(childUnit)) {
	    throw new DomainException("error.unit.subUnit.is.already.subUnit");
	}
	YearMonthDay currentDate = new YearMonthDay();
	List<Unit> parentUnits = (childUnit.isActive(currentDate)) ? getAllActiveParentUnits(currentDate)
		: getAllInactiveParentUnits(currentDate);
	if (parentUnits.contains(childUnit)) {
	    throw new DomainException("error.unit.childUnit.is.already.parentUnit");
	}
	return new Accountability(this, childUnit, accountabilityType);
    }

    public NonAffiliatedTeacher findNonAffiliatedTeacherByName(final String name) {
	for (final NonAffiliatedTeacher nonAffiliatedTeacher : getAssociatedNonAffiliatedTeachersSet()) {
	    if (nonAffiliatedTeacher.getName().equalsIgnoreCase(name)) {
		return nonAffiliatedTeacher;
	    }
	}
	return null;
    }

    public Unit getChildUnitByAcronym(String acronym) {
	for (Unit subUnit : getSubUnits()) {
	    if ((subUnit.getAcronym() != null) && (subUnit.getAcronym().equals(acronym))) {
		return subUnit;
	    }
	}
	return null;
    }

    public static List<Unit> readAllUnits() {
	List<Unit> allUnits = new ArrayList<Unit>();
	for (Party party : RootDomainObject.getInstance().getPartys()) {
	    if (party instanceof Unit) {
		allUnits.add((Unit) party);
	    }
	}
	return allUnits;
    }

    /**
         * This method should be used only for Unit types where acronyms are
         * unique.
         */
    public static Unit readUnitByAcronymAndType(String acronym, PartyTypeEnum partyTypeEnum) {
	if (acronym != null
		&& !acronym.equals("")
		&& partyTypeEnum != null
		&& (partyTypeEnum.equals(PartyTypeEnum.DEGREE_UNIT)
			|| partyTypeEnum.equals(PartyTypeEnum.DEPARTMENT) || partyTypeEnum
			.equals(PartyTypeEnum.ACADEMIC_SERVICES_SUPERVISION))) {

	    for (Unit unit : readAllUnits()) {
		if (unit.getAcronym() != null && unit.getAcronym().equals(acronym)
			&& unit.getType() != null && unit.getType().equals(partyTypeEnum)) {
		    return unit;
		}
	    }
	}
	return null;
    }

    public static List<Unit> readUnitsByAcronym(String acronym) {
	List<Unit> result = new ArrayList<Unit>();
	if (!StringUtils.isEmpty(acronym.trim())) {
	    for (Party party : RootDomainObject.getInstance().getPartys()) {
		if (party instanceof Unit && ((Unit) party).getAcronym() != null
			&& ((Unit) party).getAcronym().equals(acronym)) {
		    result.add((Unit) party);
		}
	    }
	}
	return result;
    }

    public static Unit readByCostCenterCode(Integer costCenterCode) {
	if (costCenterCode != null) {
	    for (Party party : RootDomainObject.getInstance().getPartys()) {
		if (party instanceof Unit && ((Unit) party).getCostCenterCode() != null
			&& ((Unit) party).getCostCenterCode().equals(costCenterCode)) {
		    return (Unit) party;
		}
	    }
	}
	return null;
    }

    public List<CompetenceCourse> getDepartmentUnitCompetenceCourses(CurricularStage curricularStage) {
	List<CompetenceCourse> result = new ArrayList<CompetenceCourse>();
	if (isUnitDepartment(this)) {
	    for (Unit scientificAreaUnit : this.getScientificAreaUnits()) {
		for (Unit competenceCourseGroupUnit : scientificAreaUnit.getCompetenceCourseGroupUnits()) {
		    for (CompetenceCourse competenceCourse : competenceCourseGroupUnit
			    .getCompetenceCourses()) {
			if (competenceCourse.getCurricularStage().equals(curricularStage)) {
			    result.add(competenceCourse);
			}
		    }
		}
	    }
	}
	return result;
    }

    @Override
    public void setCostCenterCode(Integer costCenterCode) {
	checkCostCenterCode(costCenterCode);
	super.setCostCenterCode(costCenterCode);
    }

    public Collection<Unit> getParentByOrganizationalStructureAccountabilityType() {
	return (Collection<Unit>) getParentParties(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE,
		getClass());
    }

    public static Unit createNewUnit(String unitName, Integer costCenterCode, String acronym,
	    Date beginDate, Date endDate, PartyTypeEnum type, Unit parentUnit,
	    AccountabilityType accountabilityType, String webAddress) throws FenixFilterException,
	    FenixServiceException {

	if (unitName == null || StringUtils.isEmpty(unitName.trim())) {
	    throw new DomainException("error.unit.empty.name");
	}
	Unit unit = new Unit();
	unit.checkUnitDates(beginDate, endDate);
	unit.checkAcronym(acronym, type);
	unit.setCostCenterCode(costCenterCode);
	unit.setName(unitName);
	unit.setBeginDate(beginDate);
	unit.setEndDate(endDate);
	unit.setType(type);
	unit.setWebAddress(webAddress);
	unit.setAcronym(acronym);
	if (parentUnit != null && accountabilityType != null) {
	    unit.addParentUnit(parentUnit, accountabilityType);
	}
	return unit;
    }

    public static Unit createNewExternalInstitution(String unitName) {

	if (unitName == null || StringUtils.isEmpty(unitName.trim())) {
	    throw new DomainException("error.unit.empty.name");
	}

	Unit externalInstitutionUnit = UnitUtils.readExternalInstitutionUnit();
	if (externalInstitutionUnit == null) {
	    throw new DomainException("error.exception.commons.institution.rootInstitutionNotFound");
	}

	Unit institutionUnit = new Unit();
	institutionUnit.setName(unitName);
	institutionUnit.setBeginDate(Calendar.getInstance().getTime());
	institutionUnit.setType(PartyTypeEnum.EXTERNAL_INSTITUTION);
	institutionUnit.addParentUnit(externalInstitutionUnit, AccountabilityType
		.readAccountabilityTypeByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));

	return institutionUnit;
    }

    public static Party createContributor(String contributorName, String contributorNumber,
	    String contributorAddress, String areaCode, String areaOfAreaCode, String area,
	    String parishOfResidence, String districtSubdivisionOfResidence, String districtOfResidence) {

	if (Party.readByContributorNumber(contributorNumber) != null) {
	    throw new DomainException(
		    "EXTERNAL_INSTITUTION_UNIT.createContributor.existing.contributor.number");
	}

	Unit contributor = Unit.createNewExternalInstitution(contributorName);

	contributor.setSocialSecurityNumber(contributorNumber);
	contributor.setAddress(contributorAddress);
	contributor.setAreaCode(areaCode);
	contributor.setAreaOfAreaCode(areaOfAreaCode);
	contributor.setArea(area);
	contributor.setParishOfResidence(parishOfResidence);
	contributor.setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
	contributor.setDistrictOfResidence(districtOfResidence);

	return contributor;
    }

    public boolean isDepartmentUnit() {
	return isUnitDepartment(this);
    }

    public List<CurricularCourse> getCurricularCourses() {
	List<CompetenceCourse> competenceCourses = this.getCompetenceCourses();
	List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();

	for (CompetenceCourse competenceCourse : competenceCourses) {
	    curricularCourses.addAll(competenceCourse.getAssociatedCurricularCourses());
	}

	return curricularCourses;
    }

    public List<VigilantGroup> getVigilantGroupsForGivenExecutionYear(ExecutionYear executionYear) {
	List<VigilantGroup> vigilantGroups = this.getVigilantGroups();
	List<VigilantGroup> vigilantGroupsInExecutionYear = new ArrayList<VigilantGroup>();

	for (VigilantGroup group : vigilantGroups) {
	    if (group.getExecutionYear().equals(executionYear)) {
		vigilantGroupsInExecutionYear.add(group);
	    }
	}

	return vigilantGroupsInExecutionYear;
    }

    public List<CompetenceCourse> getCompetenceCoursesByExecutionYear(ExecutionYear executionYear) {
	List<CompetenceCourse> competenceCourses = this.getCompetenceCourses();
	List<CompetenceCourse> competenceCoursesByExecutionYear = new ArrayList<CompetenceCourse>();
	for (CompetenceCourse competenceCourse : competenceCourses) {
	    if (competenceCourse.hasActiveScopesInExecutionYear(executionYear)) {
		competenceCoursesByExecutionYear.add(competenceCourse);
	    }

	}
	return competenceCoursesByExecutionYear;
    }

    public List<ExamCoordinator> getExamCoordinatorsForGivenYear(ExecutionYear executionYear) {
	List<ExamCoordinator> examCoordinators = new ArrayList<ExamCoordinator>();
	for (ExamCoordinator coordinator : this.getExamCoordinators()) {
	    if (coordinator.getExecutionYear().equals(executionYear)) {
		examCoordinators.add(coordinator);
	    }
	}
	return examCoordinators;
    }

    @Override
    public ParkingPartyClassification getPartyClassification() {
	return ParkingPartyClassification.UNIT;
    }

    public Collection<ExternalContract> getExternalPersons() {
	return (Collection<ExternalContract>) getChildAccountabilities(
		AccountabilityTypeEnum.EMPLOYEE_CONTRACT, ExternalContract.class);
    }

    public static Unit findFirstExternalUnitByName(final String unitName) {
	if (unitName == null || unitName.length() == 0) {
	    return null;
	}
	for (final Party party : RootDomainObject.getInstance().getExternalInstitutionUnit()
		.getSubUnits()) {
	    if (!party.isPerson() && unitName.equalsIgnoreCase(party.getName())) {
		final Unit unit = (Unit) party;
		return unit;
	    }
	}
	return null;
    }

    public String getNameWithAcronym() {
	String name = super.getName().trim();
	return (getAcronym() == null || StringUtils.isEmpty(getAcronym().trim())) ? name : name + " ("
		+ getAcronym().trim() + ")";
    }

    public String getPresentationName() {
	StringBuilder builder = new StringBuilder();
	builder.append(getNameWithAcronym());
	if (getCostCenterCode() != null) {
	    builder.append(" [c.c. ").append(getCostCenterCode()).append("]");
	}
	return builder.toString();
    }

    public String getPresentationNameWithParents() {
	String parentUnits = getParentUnitsPresentationName();
	return (!StringUtils.isEmpty(parentUnits.trim())) ? getParentUnitsPresentationName() + " - "
		+ getPresentationName() : getPresentationName();
    }

    public String getPresentationNameWithParentsAndBreakLine() {
	String parentUnits = getParentUnitsPresentationNameWithBreakLine();
	return (!StringUtils.isEmpty(parentUnits.trim())) ? getParentUnitsPresentationNameWithBreakLine()
		+ " <br/> " + getPresentationName()
		: getPresentationName();
    }

    public String getParentUnitsPresentationNameWithBreakLine() {
	return getParentUnitsPresentationName("<br/>");
    }

    public String getParentUnitsPresentationName() {
	return getParentUnitsPresentationName(" - ");
    }

    private String getParentUnitsPresentationName(String separator) {
	StringBuilder builder = new StringBuilder();
	Unit externalInstitutionUnit = UnitUtils.readExternalInstitutionUnit();
	Unit institutionUnit = UnitUtils.readInstitutionUnit();
	List<Unit> parentUnits = new ArrayList<Unit>();
	Unit searchedUnit = this;

	while (searchedUnit.getParentUnits().size() == 1) {
	    Iterator<Unit> iter = searchedUnit.getParentUnits().iterator();
	    Unit parentUnit = iter.hasNext() ? iter.next() : null;
	    if (parentUnit != institutionUnit && parentUnit != externalInstitutionUnit) {
		if (parentUnit.getType() == null
			|| !parentUnit.getType().equals(PartyTypeEnum.AGGREGATE_UNIT)) {
		    parentUnits.add(0, parentUnit);
		}
		searchedUnit = parentUnit;
	    } else {
		parentUnits.add(0, parentUnit);
		break;
	    }
	}

	if (searchedUnit.getParentUnits().size() > 1) {
	    if (searchedUnit.getType() != null
		    && searchedUnit.getType().equals(PartyTypeEnum.EXTERNAL_INSTITUTION)) {
		parentUnits.add(0, externalInstitutionUnit);
	    } else {
		parentUnits.add(0, institutionUnit);
	    }
	}

	int index = 1;
	for (Unit unit : parentUnits) {
	    if (index == parentUnits.size()) {
		builder.append(unit.getNameWithAcronym());
	    } else {
		builder.append(unit.getNameWithAcronym() + separator);
	    }
	    index++;
	}

	return builder.toString();
    }
}
