/*
 * Created on 13/Nov/2003
 *
 */
package Dominio.reimbursementGuide;

import java.util.Calendar;

import Dominio.IDomainObject;
import Dominio.IEmployee;
import Util.ReimbursementGuideState;
import Util.State;

/**
 * @author Jo�o Mota
 *13/Nov/2003
 *
 */
public interface IReimbursementGuideSituation extends IDomainObject{
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
}