package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IEmployee;

/**
 * @author Fernanda Quit�rio Created on 6/Set/2004
 *  
 */
public class InfoEmployeeWithAll extends InfoEmployee {

    public void copyFromDomain(IEmployee employee) {
        super.copyFromDomain(employee);
        if (employee != null) {
            setPerson(InfoPerson.newInfoFromDomain(employee.getPerson()));
            setWorkingPlaceInfoCostCenter(InfoCostCenter.newInfoFromDomain(employee.getEmployeeHistoric().getWorkingPlaceCostCenter()));
        }
    }

    public static InfoEmployee newInfoFromDomain(IEmployee employee) {
        InfoEmployeeWithAll infoEmployeeWithAll = null;
        if (employee != null) {
            infoEmployeeWithAll = new InfoEmployeeWithAll();
            infoEmployeeWithAll.copyFromDomain(employee);
        }
        return infoEmployeeWithAll;
    }
}