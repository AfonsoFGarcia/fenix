/*
 * Created on Oct 14, 2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Employee;

/**
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class InfoEmployee extends InfoObject {
    private InfoPerson person = null;

    private Integer employeeNumber = null;

    private InfoUnit workingUnit = null;

    private InfoUnit mailingUnit = null;

    public void setPerson(InfoPerson person) {
        this.person = person;
    }

    public InfoPerson getPerson() {
        return person;
    }

    public Integer getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Integer employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": \n";
        result += "idInternal = " + getIdInternal() + "; \n";
        result += "number = " + this.employeeNumber + "; \n";
        result += "person = " + this.person.getIdInternal() + "; \n";
        result += "] \n";

        return result;
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof InfoEmployee) {
            InfoEmployee infoEmployee = (InfoEmployee) obj;
            result = this.person.equals(infoEmployee.getPerson());
        }
        return result;
    }

    public void copyFromDomain(Employee employee) {
        super.copyFromDomain(employee);
        if (employee != null) {
            setEmployeeNumber(employee.getEmployeeNumber());
            if (employee.getCurrentContract() != null) {
                setWorkingUnit(InfoUnit
                        .newInfoFromDomain(employee.getCurrentContract().getWorkingUnit()));
                setMailingUnit(InfoUnit
                        .newInfoFromDomain(employee.getCurrentContract().getMailingUnit()));
            }
        }
    }

    public static InfoEmployee newInfoFromDomain(Employee employee) {
        InfoEmployee infoEmployee = null;
        if (employee != null) {
            infoEmployee = new InfoEmployee();
            infoEmployee.copyFromDomain(employee);
        }
        return infoEmployee;
    }

    public InfoUnit getWorkingUnit() {
        return workingUnit;
    }

    public void setWorkingUnit(InfoUnit workingUnit) {
        this.workingUnit = workingUnit;
    }

    public InfoUnit getMailingUnit() {
        return mailingUnit;
    }

    public void setMailingUnit(InfoUnit mailingUnit) {
        this.mailingUnit = mailingUnit;
    }
}