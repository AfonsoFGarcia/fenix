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
public class PaymentPhase extends DomainObject implements IPaymentPhase
{
	private Date startDate;
	private Date endDate;
	private Float value;
	private String description;

	private IGratuityValues gratuityValues;
	private Integer keyGratuity;
	
	
	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return Returns the endDate.
	 */
	public Date getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @return Returns the startDate.
	 */
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * @return Returns the value.
	 */
	public Float getValue()
	{
		return value;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(Float value)
	{
		this.value = value;
	}
	
	/**
	 * @return Returns the gratuity.
	 */
	public IGratuityValues getGratuityValues()
	{
		return gratuityValues;
	}

	/**
	 * @param gratuity The gratuity to set.
	 */
	public void setGratuityValues(IGratuityValues gratuityValues)
	{
		this.gratuityValues = gratuityValues;
	}

	/**
	 * @return Returns the keyGratuity.
	 */
	public Integer getKeyGratuity()
	{
		return keyGratuity;
	}

	/**
	 * @param keyGratuity The keyGratuity to set.
	 */
	public void setKeyGratuity(Integer keyGratuity)
	{
		this.keyGratuity = keyGratuity;
	}

	public String toString()
	{
		//TODO: to make
		return null;
	}

	public boolean equals(Object object)
	{
		//TODO: to make
		return true;
	}	
}
