/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.List;

/**
 * @author T�nia Pous�o
 */
public interface IEmployee extends IDomainObject {
    public Integer getEmployeeNumber();

    public Integer getWorkingHours();

    public Date getAntiquity();

    public IPerson getPerson();

    public EmployeeHistoric getEmployeeHistoric();

    public List getHistoricList();

    public void setEmployeeNumber(Integer number);

    public void setWorkingHours(Integer workingHours);

    public void setAntiquity(Date antiquity);

    public void setPerson(IPerson person);

    public void setEmployeeHistoric(EmployeeHistoric employeeHistoric);

    public void setHistoricList(List historicList);

    public Boolean getActive();

    public void setActive(Boolean active);

    public void fillEmployeeHistoric();
}