/*
 * Created on 5/Jan/2004
 *  
 */
package Dominio;

import java.util.Date;
import java.util.List;

import Util.ExemptionGratuityType;

/**
 * @author T�nia Pous�o
 *  
 */
public interface IGratuitySituation extends IDomainObject
{
	public Double getPayedValue();
	public Double getRemainingValue();
	public Integer getExemptionPercentage();
	public ExemptionGratuityType getExemptionType();
	public String getExemptionDescription();
	public IGratuityValues getGratuityValues();
	public IStudentCurricularPlan getStudentCurricularPlan();
	public List getTransactionList();
	public IEmployee getEmployee();
	public Date getWhen();
	
	public void setPayedValue(Double payedValue);
	public void setRemainingValue(Double remainingValue);
	public void setExemptionPercentage(Integer exemptionPercentage);
	public void setExemptionType(ExemptionGratuityType exemptionType);
	public void setExemptionDescription(String exemptionDescription);
	public void setGratuityValues(IGratuityValues gratuity);
	public void setStudentCurricularPlan(IStudentCurricularPlan student);
	public void setTransactionList(List transactionList);
	public void setEmployee(IEmployee funcionario);
	public void setWhen(Date date);
}
