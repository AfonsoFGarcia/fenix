/*
 * Created on Aug 2, 2004
 *
 */
package Dominio.transactions;

import Dominio.IExecutionYear;
import Dominio.IStudent;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IInsuranceTransaction extends IPaymentTransaction {

    /**
     * @return Returns the executionYear.
     */
    public abstract IExecutionYear getExecutionYear();

    /**
     * @param executionYear
     *            The executionYear to set.
     */
    public abstract void setExecutionYear(IExecutionYear executionYear);

    /**
     * @return Returns the student.
     */
    public abstract IStudent getStudent();

    /**
     * @param student
     *            The student to set.
     */
    public abstract void setStudent(IStudent student);
}