package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Employee;

/**
 * @author Fernanda Quit�rio Created on 6/Set/2004
 *  
 */
public class InfoEmployeeWithAll extends InfoEmployee {

    public void copyFromDomain(Employee employee) {
        super.copyFromDomain(employee);
        if (employee != null) {
            setPerson(InfoPerson.newInfoFromDomain(employee.getPerson()));
            setWorkingUnit(InfoUnit.newInfoFromDomain(employee.getCurrentContract().getWorkingUnit()));
            setMailingUnit(InfoUnit.newInfoFromDomain(employee.getCurrentContract().getMailingUnit()));
        }
    }

    public static InfoEmployee newInfoFromDomain(Employee employee) {
        InfoEmployeeWithAll infoEmployeeWithAll = null;
        if (employee != null) {
            infoEmployeeWithAll = new InfoEmployeeWithAll();
            infoEmployeeWithAll.copyFromDomain(employee);
        }
        return infoEmployeeWithAll;
    }
}