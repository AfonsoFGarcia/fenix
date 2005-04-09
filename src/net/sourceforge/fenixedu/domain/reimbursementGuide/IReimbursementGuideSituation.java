/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.domain.reimbursementGuide;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.util.State;

/**
 * @author Jo�o Mota 13/Nov/2003
 *  
 */
public interface IReimbursementGuideSituation extends IDomainObject {
    /**
     * @return
     */
    public IEmployee getEmployee();

    /**
     * @param employee
     */
    public void setEmployee(IEmployee employee);

    /**
     * @return
     */
    public Calendar getModificationDate();

    /**
     * @param modificationDate
     */
    public void setModificationDate(Calendar modificationDate);

    /**
     * @return
     */
    public IReimbursementGuide getReimbursementGuide();

    /**
     * @param reimbursementGuide
     */
    public void setReimbursementGuide(IReimbursementGuide reimbursementGuide);

    /**
     * @return
     */
    public ReimbursementGuideState getReimbursementGuideState();

    /**
     * @param reimbursementGuideState
     */
    public void setReimbursementGuideState(ReimbursementGuideState reimbursementGuideState);

    /**
     * @return
     */
    public String getRemarks();

    /**
     * @param remarks
     */
    public void setRemarks(String remarks);

    /**
     * @return
     */
    public State getState();

    /**
     * @param state
     */
    public void setState(State state);

    /**
     * @return Returns the officialDate.
     */
    public abstract Calendar getOfficialDate();

    /**
     * @param officialDate
     *            The officialDate to set.
     */
    public abstract void setOfficialDate(Calendar officialDate);
}