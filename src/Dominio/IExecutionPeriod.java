package Dominio;

import Util.PeriodState;
import fileSuport.INode;


/**
 * Created on 11/Fev/2003
 * @author Jo�o Mota
 * ciapl 
 * Dominio
 * 
 */
public interface IExecutionPeriod extends IDomainObject,INode {
	public String getName();
	public IExecutionYear getExecutionYear();
	public void setExecutionYear(IExecutionYear executionYear);
	public void setName(String name);
	
	void setState(PeriodState newState);
	PeriodState getState();
	
	Integer getSemester();
	void setSemester (Integer semester);
}
