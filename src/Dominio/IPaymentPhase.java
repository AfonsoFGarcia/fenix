/*
 * Created on 6/Jan/2004
 *
 */
package Dominio;

import java.util.Date;

/**
 * @author T�nia Pous�o
 *
 */
public interface IPaymentPhase extends IDomainObject
{
	public Date getStartDate();
	public Date getEndDate();
	public Double getValue();
	public String getDescription();
	public IGratuityValues getGratuityValues();	

	public void setValue(Double value);
	public void setStartDate(Date startDate);
	public void setEndDate(Date endDate);
	public void setDescription(String description);
	public void setGratuityValues(IGratuityValues gratuity);
	
}
