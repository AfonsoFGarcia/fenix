package DataBeans;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Fernanda Quit�rio 10/Jan/2004
 *  
 */
public class InfoPaymentPhase extends InfoObject implements Serializable
{
	private Date startDate;
	private Date endDate;
	private Float value;
	private String description;

	private InfoGratuityValues infoGratuityValues;

	public String toString()
	{
		StringBuffer object = new StringBuffer();
		object =
			object
				.append("\n[InfoPaymentPhase: ")
				.append("idInternal= ")
				.append(getIdInternal())
				.append(" starDate= ")
				.append(startDate)
				.append("; endDate= ")
				.append(endDate)
				.append("; value= ")
				.append(value)
				.append("; description= ")
				.append(description)
				.append("\n");

		return object.toString();
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *            The description to set.
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
	 * @param endDate
	 *            The endDate to set.
	 */
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @return Returns the infoGratuityValues.
	 */
	public InfoGratuityValues getInfoGratuityValues()
	{
		return infoGratuityValues;
	}

	/**
	 * @param infoGratuityValues
	 *            The infoGratuityValues to set.
	 */
	public void setInfoGratuityValues(InfoGratuityValues infoGratuityValues)
	{
		this.infoGratuityValues = infoGratuityValues;
	}

	/**
	 * @return Returns the startDate.
	 */
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate
	 *            The startDate to set.
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
	 * @param value
	 *            The value to set.
	 */
	public void setValue(Float value)
	{
		this.value = value;
	}

}
