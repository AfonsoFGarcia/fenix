package DataBeans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Fernanda Quit�rio
 * 10/Jan/2004
 *
 */
public class InfoGratuityValues extends InfoObject implements Serializable
{
	private Double anualValue;
	private Double scholarShipValue;
	private Double finalProofValue;
	private Double courseValue;
	private Double creditValue;
	private Boolean proofRequestPayment;
	private Date startPayment;
	private Date endPayment;
	
	private InfoExecutionDegree infoExecutionDegree;
	
	private InfoEmployee infoEmployee;
	
	private List infoPaymentPhases;
	
	/**
	 * @return Returns the anualValue.
	 */
	public Double getAnualValue()
	{
		return anualValue;
	}

	/**
	 * @param anualValue The anualValue to set.
	 */
	public void setAnualValue(Double anualValue)
	{
		this.anualValue = anualValue;
	}

	/**
	 * @return Returns the courseValue.
	 */
	public Double getCourseValue()
	{
		return courseValue;
	}

	/**
	 * @param courseValue The courseValue to set.
	 */
	public void setCourseValue(Double courseValue)
	{
		this.courseValue = courseValue;
	}

	/**
	 * @return Returns the creditValue.
	 */
	public Double getCreditValue()
	{
		return creditValue;
	}

	/**
	 * @param creditValue The creditValue to set.
	 */
	public void setCreditValue(Double creditValue)
	{
		this.creditValue = creditValue;
	}

	/**
	 * @return Returns the endPayment.
	 */
	public Date getEndPayment()
	{
		return endPayment;
	}

	/**
	 * @param endPayment The endPayment to set.
	 */
	public void setEndPayment(Date endPayment)
	{
		this.endPayment = endPayment;
	}

	/**
	 * @return Returns the finalProofValue.
	 */
	public Double getFinalProofValue()
	{
		return finalProofValue;
	}

	/**
	 * @param finalProofValue The finalProofValue to set.
	 */
	public void setFinalProofValue(Double finalProofValue)
	{
		this.finalProofValue = finalProofValue;
	}

	/**
	 * @return Returns the infoEmployee.
	 */
	public InfoEmployee getInfoEmployee()
	{
		return infoEmployee;
	}

	/**
	 * @param infoEmployee The infoEmployee to set.
	 */
	public void setInfoEmployee(InfoEmployee infoEmployee)
	{
		this.infoEmployee = infoEmployee;
	}

	/**
	 * @return Returns the infoExecutionDegree.
	 */
	public InfoExecutionDegree getInfoExecutionDegree()
	{
		return infoExecutionDegree;
	}

	/**
	 * @param infoExecutionDegree The infoExecutionDegree to set.
	 */
	public void setInfoExecutionDegree(InfoExecutionDegree infoExecutionDegree)
	{
		this.infoExecutionDegree = infoExecutionDegree;
	}

	/**
	 * @return Returns the infoPaymentPhases.
	 */
	public List getInfoPaymentPhases()
	{
		return infoPaymentPhases;
	}

	/**
	 * @param infoPaymentPhases The infoPaymentPhases to set.
	 */
	public void setInfoPaymentPhases(List infoPaymentPhases)
	{
		this.infoPaymentPhases = infoPaymentPhases;
	}

	/**
	 * @return Returns the proofRequestPayment.
	 */
	public Boolean getProofRequestPayment()
	{
		return proofRequestPayment;
	}

	/**
	 * @param proofRequestPayment The proofRequestPayment to set.
	 */
	public void setProofRequestPayment(Boolean proofRequestPayment)
	{
		this.proofRequestPayment = proofRequestPayment;
	}

	/**
	 * @return Returns the scholarShipValue.
	 */
	public Double getScholarShipValue()
	{
		return scholarShipValue;
	}

	/**
	 * @param scholarShipValue The scholarShipValue to set.
	 */
	public void setScholarShipValue(Double scholarShipValue)
	{
		this.scholarShipValue = scholarShipValue;
	}

	/**
	 * @return Returns the startPayment.
	 */
	public Date getStartPayment()
	{
		return startPayment;
	}

	/**
	 * @param startPayment The startPayment to set.
	 */
	public void setStartPayment(Date startPayment)
	{
		this.startPayment = startPayment;
	}

}
