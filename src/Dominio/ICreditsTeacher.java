package Dominio;

import java.io.Serializable;

/**
 * @author Fernanda & T�nia
 *
 */
public interface ICreditsTeacher extends Serializable {
	ITeacher getTeacher();
	void setTeacher(ITeacher teacher);
	 
	ITurno getShift();
	void setShift(ITurno shift);
	
	public Double getPercentage();
	public void setPercentage(Double percentage);
}
