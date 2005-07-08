/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ListIterator;

/**
 * 
 * @author T�nia Pous�o
 */
public class Employee extends Employee_Base {
    private IEmployeeHistoric employeeHistoric = null;

    public IEmployeeHistoric getEmployeeHistoric() {
        return employeeHistoric;
    }

    public void setEmployeeHistoric(IEmployeeHistoric employeeHistoric) {
        this.employeeHistoric = employeeHistoric;
    }

    public String toString() {
        String result = "[Dominio.Employee ";
        result += ", employeeNumber=" + getEmployeeNumber();
        result += ", person=" + getPerson();
        result += "]";
        return result;
    }

    public void fillEmployeeHistoric() {
        if (getHistoricList() != null) {
            ListIterator iterator = getHistoricList().listIterator();
            IEmployeeHistoric historic = null;

            setEmployeeHistoric(new EmployeeHistoric());
            while (iterator.hasNext()) {
                historic = (IEmployeeHistoric) iterator.next();

                if (historic.getKeyResponsableEmployee() != null
                        && historic.getKeyResponsableEmployee().intValue() > 0) {
                    employeeHistoric.setKeyResponsableEmployee(historic.getKeyResponsableEmployee());
                    employeeHistoric.setResponsableEmployee(historic.getResponsableEmployee());
                }
                if (historic.getKeyMailingCostCenter() != null
                        && historic.getKeyMailingCostCenter().intValue() > 0) {
                    employeeHistoric.setKeyMailingCostCenter(historic.getKeyMailingCostCenter());
                    employeeHistoric.setMailingCostCenter(historic.getMailingCostCenter());
                }
                if (historic.getKeyWorkingPlaceCostCenter() != null
                        && historic.getKeyWorkingPlaceCostCenter().intValue() > 0) {
                    employeeHistoric.setKeyWorkingPlaceCostCenter(historic
                            .getKeyWorkingPlaceCostCenter());
                    employeeHistoric.setWorkingPlaceCostCenter(historic.getWorkingPlaceCostCenter());
                }
                if (historic.getKeySalaryCostCenter() != null
                        && historic.getKeySalaryCostCenter().intValue() > 0) {
                    employeeHistoric.setKeySalaryCostCenter(historic.getKeySalaryCostCenter());
                    employeeHistoric.setSalaryCostCenter(historic.getSalaryCostCenter());
                }
                if (historic.getCalendar() != null) {
                    employeeHistoric.setCalendar(historic.getCalendar());
                }
                if (historic.getKeyStatus() != null && historic.getKeyStatus().intValue() > 0) {
                    employeeHistoric.setKeyStatus(historic.getKeyStatus());
                    employeeHistoric.setStatus(historic.getStatus());
                }
            }
        }
    }

}
