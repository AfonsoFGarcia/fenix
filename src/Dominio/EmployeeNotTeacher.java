/*
 * Created on 1/Mar/2004
 *
 */
package Dominio;

/**
 * @author T�nia Pous�o
 *  
 */
public class EmployeeNotTeacher extends DomainObject implements IEmployeeNotTeacher {
    private IEmployee employee;

    private Integer keyEmployee;

    /**
     * @return Returns the keyEmployee.
     */
    public Integer getKeyEmployee() {
        return keyEmployee;
    }

    /**
     * @param keyEmployee
     *            The keyEmployee to set.
     */
    public void setKeyEmployee(Integer keyEmployee) {
        this.keyEmployee = keyEmployee;
    }

    /**
     * @return Returns the employee.
     */
    public IEmployee getEmployee() {
        return employee;
    }

    /**
     * @param employee
     *            The employee to set.
     */
    public void setEmployee(IEmployee employee) {
        this.employee = employee;
    }

}