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
public class ExecutionYear extends DomainObject implements IExecutionYear {

	private PeriodState state;
	protected String year;
	
	/**
	 * Constructor for ExecutionYear.
	 */
	public ExecutionYear() {
	}
	/**
	 * 
	 * @param year
	 */
	public ExecutionYear(String year) {
			setYear(year);
		}

	/**
	 * Returns the year.
	 * @return String
	 */
	public String getYear() {
		return year;
	}

	/**
	 * Sets the year.
	 * @param year The year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}


	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj instanceof IExecutionYear){
			IExecutionYear executionYear = (IExecutionYear) obj;
			return getYear().equals(executionYear.getYear());
		}
		return false;
	}

	public String toString() {
		String result = "[EXECUTION_YEAR";
		result += ", internalCode=" + getIdInternal();
		result += ", year=" + year;
		result += "]";
		return result;
	}
	/* (non-Javadoc)
	 * @see Dominio.IExecutionYear#setState(Util.PeriodState)
	 */
	public void setState(PeriodState state) {
		this.state = state;		
	}
	/* (non-Javadoc)
	 * @see Dominio.IExecutionYear#getState()
	 */
	public PeriodState getState() {
		return this.state;
	}
	/* (non-Javadoc)
	 * @see fileSuport.INode#getSlideName()
	 */
	public String getSlideName() {
		String result="/"+getYear().trim().replaceAll("/","-").replaceAll("\\","-");	
		return result;
	}
	/* (non-Javadoc)
	 * @see fileSuport.INode#getParentNode()
	 */
	public INode getParentNode() {
		return null;
	}

	

}
