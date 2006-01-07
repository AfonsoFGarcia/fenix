/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitType;

/**
 * 
 * @author T�nia Pous�o
 */
public class Employee extends Employee_Base {

    public String toString() {
        String result = "[Dominio.Employee ";
        result += ", employeeNumber=" + getEmployeeNumber();
        result += ", person=" + getPerson();
        result += "]";
        return result;
    }

    public Department getCurrentDepartmentWorkingPlace() {

        Contract contract = getCurrentContract();
        if (contract != null && contract.getWorkingUnit() != null) {
            return getEmployeeUnitDepartment(contract.getWorkingUnit(), true);
        }
        return null;
    }

    public Department getLastDepartmentWorkingPlace() {
        Contract contract = getLastContract();
        if (contract != null && contract.getWorkingUnit() != null) {
            return getEmployeeUnitDepartment(contract.getWorkingUnit(), false);
        }
        return null;
    }

    public Contract getCurrentContract() {        
        List<Contract> contracts = this.getContracts();
        for (Contract contract : contracts) {
            if (contract.isActive(Calendar.getInstance().getTime()))
                return contract;
        }
        return null;
    }

    public Contract getLastContract() {
        Date date = null;
        Contract contractToReturn = null;
        for (Contract contract : this.getContracts()) {
            if (contract.isActive(Calendar.getInstance().getTime())) {
                contractToReturn = contract;
                break;
            } else if (date == null || date.before(contract.getEndDate())) {
                date = contract.getEndDate();
                contractToReturn = contract;
            }
        }
        return contractToReturn;
    }

    private Department getEmployeeUnitDepartment(Unit unit, boolean onlyActiveEmployees) {

        List<Unit> allTopUnits = unit.getTopUnits();
        if (!allTopUnits.isEmpty()) {
            for (Unit topUnit : allTopUnits) {
                if (topUnit.getType() != null
                        && topUnit.getType().equals(UnitType.DEPARTMENT)
                        && topUnit.getDepartment() != null
                        && (!onlyActiveEmployees || topUnit.getDepartment()
                                .getCurrentActiveWorkingEmployees().contains(this))) {
                    return topUnit.getDepartment();
                }
            }
        } else if (unit.getType() != null && unit.getType().equals(UnitType.DEPARTMENT)) {
            return unit.getDepartment();
        }
        return null;
    }
}
