package Dominio;

import Util.PeriodState;

/**
 * Created on 11/Fev/2003
 * @author Jo�o Mota
 * ciapl 
 * Dominio
 * 
 */
public interface IExecutionYear {
	public String getYear();
	public void setYear(String year);
	void setState (PeriodState state);
	PeriodState getState();

}
