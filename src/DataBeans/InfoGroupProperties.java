/*
 * Created on 22/Jul/2003
 *
 */
package DataBeans;

import java.util.Calendar;
import java.util.List;

import Dominio.IGroupProperties;
import Util.EnrolmentGroupPolicyType;
import Util.TipoAula;

/**
 * @author asnr and scpo
 * @author joaosa & rmalo 12/Aug/2004
 */
public class InfoGroupProperties extends InfoObject
{

	private Integer maximumCapacity;

	private Integer minimumCapacity;

	private Integer idealCapacity;

	private EnrolmentGroupPolicyType enrolmentPolicy;

	private Integer groupMaximumNumber;

	private String name;
	
	private InfoAttendsSet infoAttendsSet;

	private List infoGroupPropertiesExecutionCourse;

	private TipoAula shiftType;

	private Calendar enrolmentBeginDay;

	private Calendar enrolmentEndDay;

	private String projectDescription;

	/**
	 * Construtor
	 */
	public InfoGroupProperties()
	{

	}

	/**
	 * Construtor
	 */
	public InfoGroupProperties(List infoGroupPropertiesExecutionCourse)
	{

		this.infoGroupPropertiesExecutionCourse = infoGroupPropertiesExecutionCourse;
	}

	/**
	 * Construtor
	 */
	public InfoGroupProperties(Integer maximumCapacity, Integer minimumCapacity, Integer idealCapacity,
			EnrolmentGroupPolicyType enrolmentPolicy, Integer groupMaximumNumber,
			List infoGroupPropertiesExecutionCourse, String name, TipoAula shiftType,
			Calendar enrolmentBeginDay, Calendar enrolmentEndDay, String projectDescription)
	{

		this.maximumCapacity = maximumCapacity;
		this.minimumCapacity = minimumCapacity;
		this.idealCapacity = idealCapacity;
		this.enrolmentPolicy = enrolmentPolicy;
		this.groupMaximumNumber = groupMaximumNumber;
		this.infoGroupPropertiesExecutionCourse = infoGroupPropertiesExecutionCourse;
		this.name = name;
		this.shiftType = shiftType;
		this.enrolmentBeginDay = enrolmentBeginDay;
		this.enrolmentEndDay = enrolmentEndDay;
		this.projectDescription = projectDescription;

	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0)
	{

		boolean result = false;
		if (arg0 instanceof InfoGroupProperties)
		{
			result = (getInfoGroupPropertiesExecutionCourse().equals(((InfoGroupProperties) arg0)
					.getInfoGroupPropertiesExecutionCourse()))
					&& (getName().equals(((InfoGroupProperties) arg0).getName()));
		}
		return result;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{

		String result = "[INFO_GROUP_PROPERTIES";
		result += ", maximumCapacity=" + getMaximumCapacity();
		result += ", minimumCapacity=" + getMinimumCapacity();
		result += ", idealCapacity=" + getIdealCapacity();
		result += ", enrolmentPolicy=" + getEnrolmentPolicy();
		result += ", groupMaximumNumber=" + getGroupMaximumNumber();
		result += ", name=" + getName();
		result += ", shiftType=" + getShiftType();
		//result += ", enrolmentBeginDay=" + getEnrolmentBeginDay();
		//result += ", enrolmentEndDay=" + getEnrolmentEndDay();
		result += ", projectDescription=" + getProjectDescription();
		result += "]";
		return result;
	}

	/**
	 * @return Integer
	 */
	public Integer getMaximumCapacity()
	{

		return maximumCapacity;
	}

	/**
	 * @return Integer
	 */
	public Integer getMinimumCapacity()
	{

		return minimumCapacity;
	}

	/**
	 * @return Integer
	 */
	public Integer getIdealCapacity()
	{

		return idealCapacity;
	}

	/**
	 * @return EnrolmentPolicy
	 */
	public EnrolmentGroupPolicyType getEnrolmentPolicy()
	{

		return enrolmentPolicy;
	}

	/**
	 * @return Integer
	 */
	public Integer getGroupMaximumNumber()
	{

		return groupMaximumNumber;
	}

	/**
	 * @return String
	 */
	public String getName()
	{

		return name;
	}

	/**
	 * @return String
	 */
	public String getProjectDescription()
	{

		return projectDescription;
	}

	/**
	 * @return InfoGroupPropertiesExecutionCourse
	 */
	public List getInfoGroupPropertiesExecutionCourse()
	{

		return infoGroupPropertiesExecutionCourse;
	}

	/**
	 * @return Tipo Lesson
	 */
	public TipoAula getShiftType()
	{

		return shiftType;
	}

	/**
	 * @return Calendar
	 */
	public Calendar getEnrolmentBeginDay()
	{

		return enrolmentBeginDay;
	}

	/**
	 * @return Calendar
	 */
	public Calendar getEnrolmentEndDay()
	{

		return enrolmentEndDay;
	}

	/**
	 * Sets the maximumCapacity.
	 * 
	 * @param maximumCapacity
	 *            The maximumCapacity to set
	 */
	public void setMaximumCapacity(Integer maximumCapacity)
	{

		this.maximumCapacity = maximumCapacity;
	}

	/**
	 * Sets the minimumCapacity.
	 * 
	 * @param minimumCapacity
	 *            The minimumCapacity to set
	 */
	public void setMinimumCapacity(Integer minimumCapacity)
	{

		this.minimumCapacity = minimumCapacity;
	}

	/**
	 * Sets the idealCapacity.
	 * 
	 * @param idealCapacity
	 *            The idealCapacity to set
	 */
	public void setIdealCapacity(Integer idealCapacity)
	{

		this.idealCapacity = idealCapacity;
	}

	/**
	 * Sets the enrolmentPolicy.
	 * 
	 * @param enrolmentPolicy
	 *            The enrolmentPolicy to set
	 */
	public void setEnrolmentPolicy(EnrolmentGroupPolicyType enrolmentPolicy)
	{

		this.enrolmentPolicy = enrolmentPolicy;
	}

	/**
	 * Sets the groupMaximumNumber.
	 * 
	 * @param groupMaximumNumber
	 *            The groupMaximumNumber to set
	 */
	public void setGroupMaximumNumber(Integer groupMaximumNumber)
	{

		this.groupMaximumNumber = groupMaximumNumber;
	}

	/**
	 * Sets the projectName.
	 * 
	 * @param projectName
	 *            The projectName to set
	 */
	public void setName(String name)
	{

		this.name = name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            The name to set
	 */
	public void setProjectDescription(String projectDescription)
	{

		this.projectDescription = projectDescription;
	}

	/**
	 * Sets the infoGroupPropertiesExecutionCourse.
	 * 
	 * @param infoGroupPropertiesExecutionCourse
	 *            The infoGroupPropertiesExecutionCourse to set
	 */
	public void setInfoGroupPropertiesExecutionCourse(List infoGroupPropertiesExecutionCourse)
	{
		this.infoGroupPropertiesExecutionCourse = infoGroupPropertiesExecutionCourse;
	}

	/**
	 * Sets the shiftType.
	 * 
	 * @param shiftType
	 *            The shiftType to set
	 */
	public void setShiftType(TipoAula shiftType)
	{

		this.shiftType = shiftType;
	}

	/**
	 * Sets the enrolmentBeginDay.
	 * 
	 * @param enrolmentBeginDay
	 *            The enrolmentBeginDay to set
	 */
	public void setEnrolmentBeginDay(Calendar enrolmentBeginDay)
	{

		this.enrolmentBeginDay = enrolmentBeginDay;
	}

	/**
	 * Sets the enrolmentEndDay.
	 * 
	 * @param enrolmentEndDay
	 *            The enrolmentEndDay to set
	 */
	public void setEnrolmentEndDay(Calendar enrolmentEndDay)
	{

		this.enrolmentEndDay = enrolmentEndDay;
	}

	public String getShiftTypeFormatted()
	{

		String result = "";
		int value = getShiftType().getTipo().intValue();
		switch (value)
		{
			case 1 :
				return "Te�rica";
			case 2 :
				return "Pr�tica";
			case 3 :
				return "Te�rico-Pr�tica";
			case 4 :
				return "Laboratorial";
		}
		return result;
	}

	public String getEnrolmentBeginDayFormatted()
	{

		String result = "";
		Calendar date = getEnrolmentBeginDay();
		if (date == null)
			return result;
		if (date.get(Calendar.DAY_OF_MONTH) < 10) {
			result = result.concat("0");
		}
		result += date.get(Calendar.DAY_OF_MONTH);
		result += "/";
		if ((date.get(Calendar.MONTH)+1) < 10) {
			result = result.concat("0");
		}
		result += date.get(Calendar.MONTH) + 1;
		result += "/";
		result += date.get(Calendar.YEAR);
		return result;
	}
					 
	public String getEnrolmentBeginHourFormatted()
	{

		String result = "";
		Calendar date = getEnrolmentBeginDay();
		if (date == null)
			return result;
		if (date.get(Calendar.HOUR_OF_DAY) < 10) {
			result = result.concat("0");
		}
		result += date.get(Calendar.HOUR_OF_DAY);
		result += ":";
		if (date.get(Calendar.MINUTE) < 10) {
			result = result.concat("0");
		}
		result += date.get(Calendar.MINUTE);
		return result;
	}

	public String getEnrolmentEndDayFormatted()
	{

		String result = "";
		Calendar date = getEnrolmentEndDay();
		if (date == null)
			return result;
		if (date.get(Calendar.DAY_OF_MONTH) < 10) {
			result = result.concat("0");
		}
		result += date.get(Calendar.DAY_OF_MONTH);
		result += "/";
		if ((date.get(Calendar.MONTH)+1) < 10) {
			result = result.concat("0");
		}
		result += date.get(Calendar.MONTH) + 1;
		result += "/";
		result += date.get(Calendar.YEAR);
		return result;
	}

	public String getEnrolmentEndHourFormatted()
	{

		String result = "";
		Calendar date = getEnrolmentEndDay();
		if (date == null)
			return result;
		if (date.get(Calendar.HOUR_OF_DAY) < 10) {
			result = result.concat("0");
		}
		result += date.get(Calendar.HOUR_OF_DAY);
		result += ":";
		if (date.get(Calendar.MINUTE) < 10) {
			result = result.concat("0");
		}
		result += date.get(Calendar.MINUTE);
		return result;
	}
	
	
	public void setInfoAttendsSet (InfoAttendsSet infoAttendsSet) {
		this.infoAttendsSet=infoAttendsSet;
	}
	
	public InfoAttendsSet getInfoAttendsSet () {
		return infoAttendsSet;
	}
	
		
    public void copyFromDomain(IGroupProperties groupProperties) {
        super.copyFromDomain(groupProperties);
        if(groupProperties != null) {
            setName(groupProperties.getName());
            //setInfoAttendsSet(__________);
            setProjectDescription(groupProperties.getProjectDescription());
            setShiftType(groupProperties.getShiftType());
            setEnrolmentPolicy(groupProperties.getEnrolmentPolicy());
            setGroupMaximumNumber(groupProperties.getGroupMaximumNumber());
            setIdealCapacity(groupProperties.getIdealCapacity());
            setMaximumCapacity(groupProperties.getMaximumCapacity());
            setMinimumCapacity(groupProperties.getMinimumCapacity());
            setEnrolmentBeginDay(groupProperties.getEnrolmentBeginDay());
            setEnrolmentEndDay(groupProperties.getEnrolmentEndDay());
        }
    }
    
    public static InfoGroupProperties newInfoFromDomain(IGroupProperties groupProperties){
        InfoGroupProperties infoGroupProperties = null;
        if(groupProperties != null) {
            infoGroupProperties = new InfoGroupProperties();
            infoGroupProperties.copyFromDomain(groupProperties);
        }
        
        return infoGroupProperties;
    }
}
