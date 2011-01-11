/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.EmployeeContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.teacher.Category;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * 
 * @author T�nia Pous�o
 */
public class Employee extends Employee_Base {

    public Employee(Person person, Integer employeeNumber, Boolean active) {
	super();
	setEmployeeNumber(employeeNumber);
	setCreationDate(new DateTime());
	setPerson(person);
	setActive(active);
	setWorkingHours(0);
	setAssiduousness(null);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	checkRulesToDelete();

	super.setPerson(null);
	removeRootDomainObject();
	deleteDomainObject();
    }

    private void checkRulesToDelete() {
	if (hasAnyAcademicServiceRequestSituations()) {
	    throw new DomainException("error.Employee.cannot.delete");
	}
    }

    @Override
    public void setEmployeeNumber(Integer employeeNumber) {
	if (employeeNumber == null) {
	    throw new DomainException("error.employee.no.employeeNumber");
	}
	checkEmployeeNumber(employeeNumber);
	super.setEmployeeNumber(employeeNumber);
    }

    @Override
    public void setPerson(Person person) {
	if (person == null) {
	    throw new DomainException("error.employee.no.person");
	}
	if (person.hasEmployee()) {
	    throw new DomainException("error.employee.person.already.has.employee");
	}
	super.setPerson(person);
    }

    private void checkEmployeeNumber(Integer employeeNumber) {
	Employee employee = readByNumber(employeeNumber);
	if (employee != null && !employee.equals(this)) {
	    throw new DomainException("error.employee.already.exists.one.employee.with.same.number");
	}
    }

    public List<EmployeeProfessionalSituation> getEmployeeProfessionalSituations() {
	List<EmployeeProfessionalSituation> result = new ArrayList<EmployeeProfessionalSituation>();
	for (ProfessionalSituation professionalSituation : getProfessionalSituations()) {
	    if (professionalSituation.isEmployeeProfessionalSituation()) {
		result.add((EmployeeProfessionalSituation) professionalSituation);
	    }
	}
	return result;
    }

    public EmployeeProfessionalSituation getCurrentEmployeeProfessionalSituation() {
	YearMonthDay currentDate = new YearMonthDay();
	for (ProfessionalSituation professionalSituation : getProfessionalSituations()) {
	    if (professionalSituation.isEmployeeProfessionalSituation() && professionalSituation.isActive(currentDate)) {
		return (EmployeeProfessionalSituation) professionalSituation;
	    }
	}
	return null;
    }

    public EmployeeProfessionalSituation getLastEmployeeProfessionalSituation() {
	YearMonthDay date = null, current = new YearMonthDay();
	EmployeeProfessionalSituation regimenToReturn = null;
	for (EmployeeProfessionalSituation regimen : getEmployeeProfessionalSituations()) {
	    if (!regimen.getBeginDateYearMonthDay().isAfter(current)) {
		if (regimen.isActive(current)) {
		    return regimen;
		} else if (date == null || regimen.getBeginDateYearMonthDay().isAfter(date)) {
		    date = regimen.getBeginDateYearMonthDay();
		    regimenToReturn = regimen;
		}
	    }
	}
	return regimenToReturn;
    }

    public Collection<Contract> getContractsByContractType(AccountabilityTypeEnum contractType) {
	return (Collection<Contract>) getPerson().getParentAccountabilities(contractType, EmployeeContract.class);
    }

    public List<Contract> getContractsByContractType(AccountabilityTypeEnum contractType, YearMonthDay begin, YearMonthDay end) {
	final List<Contract> contracts = new ArrayList<Contract>();
	for (final Contract accountability : getContractsByContractType(contractType)) {
	    if (accountability.belongsToPeriod(begin, end)) {
		contracts.add(accountability);
	    }
	}
	return contracts;
    }

    public Contract getCurrentContractByContractType(AccountabilityTypeEnum contractType) {
	YearMonthDay current = new YearMonthDay();
	for (final Contract accountability : getContractsByContractType(contractType)) {
	    if (accountability.isActive(current)) {
		return accountability;
	    }
	}
	return null;
    }

    public Contract getLastContractByContractType(AccountabilityTypeEnum contractType) {
	YearMonthDay date = null, current = new YearMonthDay();
	Contract contractToReturn = null;
	for (Contract contract : getContractsByContractType(contractType)) {
	    if (!contract.getBeginDate().isAfter(current)) {
		if (contract.isActive(current)) {
		    return contract;
		} else if (date == null || contract.getBeginDate().isAfter(date)) {
		    date = contract.getBeginDate();
		    contractToReturn = contract;
		}
	    }
	}
	return contractToReturn;

    }

    public Contract getLastContractByContractType(AccountabilityTypeEnum contractType, YearMonthDay begin, YearMonthDay end) {
	YearMonthDay date = null, current = new YearMonthDay();
	Contract contractToReturn = null;
	for (Contract contract : getContractsByContractType(contractType, begin, end)) {
	    if (!contract.getBeginDate().isAfter(current)) {
		if (contract.isActive(current)) {
		    return contract;
		} else if (date == null || contract.getBeginDate().isAfter(date)) {
		    date = contract.getBeginDate();
		    contractToReturn = contract;
		}
	    }
	}
	return contractToReturn;
    }

    public List<Contract> getWorkingContracts() {
	List<Contract> workingContracts = new ArrayList<Contract>();
	workingContracts.addAll(getContractsByContractType(AccountabilityTypeEnum.WORKING_CONTRACT));
	return workingContracts;
    }

    public List<Contract> getMailingContracts() {
	List<Contract> mailingContracts = new ArrayList<Contract>();
	mailingContracts.addAll(getContractsByContractType(AccountabilityTypeEnum.MAILING_CONTRACT));
	return mailingContracts;
    }

    public List<Contract> getWorkingContracts(YearMonthDay begin, YearMonthDay end) {
	final List<Contract> contracts = new ArrayList<Contract>();
	for (final Contract accountability : getContractsByContractType(AccountabilityTypeEnum.WORKING_CONTRACT)) {
	    if (accountability.belongsToPeriod(begin, end)) {
		contracts.add(accountability);
	    }
	}
	return contracts;
    }

    public Contract getCurrentWorkingContract() {
	return getCurrentContractByContractType(AccountabilityTypeEnum.WORKING_CONTRACT);
    }

    public Contract getLastWorkingContract() {
	return getLastContractByContractType(AccountabilityTypeEnum.WORKING_CONTRACT);
    }

    public Unit getCurrentWorkingPlace() {
	Contract contract = getCurrentWorkingContract();
	return (contract != null) ? contract.getWorkingUnit() : null;
    }

    public Unit getLastWorkingPlace() {
	Contract contract = getLastWorkingContract();
	return (contract != null) ? contract.getWorkingUnit() : null;
    }

    public Unit getCurrentMailingPlace() {
	Contract contract = getCurrentContractByContractType(AccountabilityTypeEnum.MAILING_CONTRACT);
	return (contract != null) ? contract.getMailingUnit() : null;
    }

    public Unit getLastWorkingPlace(YearMonthDay beginDate, YearMonthDay endDate) {
	Contract lastContract = getLastContractByContractType(AccountabilityTypeEnum.WORKING_CONTRACT, beginDate, endDate);
	return lastContract != null ? lastContract.getWorkingUnit() : null;
    }

    public List<Unit> getWorkingPlaces(YearMonthDay beginDate, YearMonthDay endDate) {
	List<Unit> units = new ArrayList<Unit>();
	for (final Contract contract : getContractsByContractType(AccountabilityTypeEnum.WORKING_CONTRACT)) {
	    if (contract.belongsToPeriod(beginDate, endDate)) {
		units.add(contract.getWorkingUnit());
	    }
	}
	return units;
    }

    public Department getCurrentDepartmentWorkingPlace() {
	Contract contract = getCurrentWorkingContract();
	return (contract != null && contract.getWorkingUnit() != null) ? getEmployeeDepartmentUnit(contract.getWorkingUnit(),
		true) : null;
    }

    public Department getLastDepartmentWorkingPlace() {
	Contract contract = getLastContractByContractType(AccountabilityTypeEnum.WORKING_CONTRACT);
	return (contract != null && contract.getWorkingUnit() != null) ? getEmployeeDepartmentUnit(contract.getWorkingUnit(),
		false) : null;
    }

    public Department getLastDepartmentWorkingPlace(YearMonthDay begin, YearMonthDay end) {
	Unit unit = getLastWorkingPlace(begin, end);
	DepartmentUnit departmentUnit = (unit != null) ? unit.getDepartmentUnit() : null;
	return (departmentUnit != null) ? departmentUnit.getDepartment() : null;
    }

    private Department getEmployeeDepartmentUnit(Unit unit, boolean onlyActiveEmployees) {
	Collection<Unit> parentUnits = unit.getParentUnits();
	if (unitDepartment(unit, onlyActiveEmployees)) {
	    return ((DepartmentUnit) unit).getDepartment();
	} else if (!parentUnits.isEmpty()) {
	    for (Unit parentUnit : parentUnits) {
		if (unitDepartment(parentUnit, onlyActiveEmployees)) {
		    return ((DepartmentUnit) parentUnit).getDepartment();
		} else if (parentUnit.hasAnyParentUnits()) {
		    Department department = getEmployeeDepartmentUnit(parentUnit, onlyActiveEmployees);
		    if (department != null) {
			return department;
		    }
		}
	    }
	}
	return null;
    }

    private boolean unitDepartment(Unit unit, boolean onlyActiveEmployees) {
	return (unit.isDepartmentUnit() && ((DepartmentUnit) unit).getDepartment() != null && (!onlyActiveEmployees || ((DepartmentUnit) unit)
		.getDepartment().hasCurrentActiveWorkingEmployee(this)));
    }

    public static Employee readByNumber(final Integer employeeNumber) {
	for (final Employee employee : RootDomainObject.getInstance().getEmployees()) {
	    if (employee.getEmployeeNumber().equals(employeeNumber)) {
		return employee;
	    }
	}
	return null;
    }

    public Campus getCurrentCampus() {
	final Unit unit = getCurrentWorkingPlace();
	return (unit != null) ? unit.getCampus() : null;
    }

    public AdministrativeOffice getAdministrativeOffice() {
	AdministrativeOffice administrativeOffice = getCurrentWorkingPlace() == null ? null : getCurrentWorkingPlace()
		.getAdministrativeOffice();
	if (administrativeOffice == null) {
	    for (PersonFunction personFunction : getPerson().getPersonFunctions(AccountabilityTypeEnum.ASSIDUOUSNESS_STRUCTURE)) {
		if (personFunction.getFunction().getFunctionType().equals(FunctionType.ASSIDUOUSNESS_RESPONSIBLE)
			&& personFunction.getUnit() != null && personFunction.getUnit().getAdministrativeOffice() != null) {
		    administrativeOffice = personFunction.getUnit().getAdministrativeOffice();
		}
	    }
	}
	return administrativeOffice;
    }

    public boolean isAdministrativeOfficeEmployee() {
	return getAdministrativeOffice() != null;
    }

    private RoleType getRoleType() {
	return RoleType.EMPLOYEE;
    }

    public String getRoleLoginAlias() {
	final List<LoginAlias> roleLoginAlias = getPerson().getLoginIdentification().getRoleLoginAlias(getRoleType());
	if (roleLoginAlias.isEmpty() || roleLoginAlias.size() > 1) {
	    return "F" + getEmployeeNumber();
	} else {
	    return roleLoginAlias.get(0).getAlias();
	}
    }

    public Unit getCurrentSectionOrScientificArea() {
	return getSectionOrScientificArea(getCurrentWorkingPlace());
    }

    private Unit getSectionOrScientificArea(Unit unit) {
	if (unit == null) {
	    return null;
	}

	if (unit.isScientificAreaUnit() || unit.isSectionUnit()) {
	    return unit;
	}

	for (Unit parent : unit.getParentUnits()) {
	    Unit parentUnit = getSectionOrScientificArea(parent);

	    if (parentUnit != null) {
		return parentUnit;
	    }
	}

	return null;
    }

    public Category getCategory() {
	EmployeeProfessionalSituation regimen = getLastEmployeeProfessionalSituation();
	return (regimen != null) ? regimen.getCategory() : null;
    }

    public boolean isActive() {
	return getCurrentEmployeeProfessionalSituation() != null;
    }

    public boolean worksAt(final Campus campus) {
	final Assiduousness assiduousness = getAssiduousness();
	return assiduousness == null ? false : assiduousness.worksAt(campus);
    }

    public boolean isUnitCoordinator() {
	return getCurrentWorkingPlace().getActiveUnitCoordinator() == getPerson();
    }

    public static Integer getNextEmployeeNumber() {
	final int max = findMaxEmployeeNumber();
	return new Integer(max + 1);
    }

    private static int findMaxEmployeeNumber() {
	int max = 0;
	for (final Employee employee : RootDomainObject.getInstance().getEmployeesSet()) {
	    max = Math.max(max, employee.getEmployeeNumber().intValue());
	}
	return max;
    }

    public boolean hasMultipleDepartments() {
	List<Department> departments = RootDomainObject.getInstance().getDepartments();
	int count = 0;
	final int several = 2;
	for (Department department : departments) {
	    if (department.hasAssociatedPersons(getPerson())) {
		count++;
	    }
	}
	return count >= several ? true : false;
    }

}
