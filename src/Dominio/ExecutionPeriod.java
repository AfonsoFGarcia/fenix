package Dominio;

import java.util.Date;

import Util.PeriodState;
import fileSuport.INode;

/**
 * Created on 11/Fev/2003
 * @author Jo�o Mota
 * @author jpvl
 * ciapl 
 * Dominio
 * 
 */
public class ExecutionPeriod extends DomainObject implements IExecutionPeriod {

	private Integer semester;
	private PeriodState state;
	protected IExecutionYear executionYear;
	protected String name;
	private Integer keyExecutionYear;
	private Date beginDate ;
	private Date endDate ;
	
	/**
	 * Constructor for ExecutionPeriod.
	 */
	public ExecutionPeriod() {
	}

	public ExecutionPeriod(Integer idInternal) {
		setIdInternal(idInternal);
		}
		
	public ExecutionPeriod(String name, IExecutionYear executionYear) {
		setName(name);
		setExecutionYear(executionYear);

	}

	/**
	 * Returns the executionYear.
	 * @return ExecutionYear
	 */
	public IExecutionYear getExecutionYear() {
		return executionYear;
	}

	/**
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Returns the keyExecutionYear.
	 * @return Integer
	 */
	public Integer getKeyExecutionYear() {
		return keyExecutionYear;
	}

	/**
	 * Sets the keyExecutionYear.
	 * @param keyExecutionYear The keyExecutionYear to set
	 */
	public void setKeyExecutionYear(Integer keyExecutionYear) {
		this.keyExecutionYear = keyExecutionYear;
	}

	/**
	 * Sets the executionYear.
	 * @param executionYear The executionYear to set
	 */
	public void setExecutionYear(IExecutionYear executionYear) {
		this.executionYear = executionYear;
	}

	public String toString() {
		String result = "[EXECUTION_PERIOD";
		result += ", internalCode=" + getIdInternal();
		result += ", name=" + name;
		result += ", executionYear=" + getExecutionYear();
		result += ", begin Date=" + getBeginDate();
		result += ", end Date=" + getEndDate();
		result += "]\n";
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof IExecutionPeriod) {
			IExecutionPeriod executionPeriod = (IExecutionPeriod) obj;
			return getName().equals(executionPeriod.getName())
				&& getExecutionYear().equals(executionPeriod.getExecutionYear());
		}
		return super.equals(obj);
	}
	/* (non-Javadoc)
	 * @see Dominio.IExecutionPeriod#setState(Util.PeriodState)
	 */
	public void setState(PeriodState newState) {
		this.state = newState;
	}
	/* (non-Javadoc)
	 * @see Dominio.IExecutionPeriod#getState()
	 */
	public PeriodState getState() {
		return this.state;
	}

	/* (non-Javadoc)
	 * @see Dominio.IExecutionPeriod#getSemester()
	 */
	public Integer getSemester() {
		return this.semester;
	}

	/* (non-Javadoc)
	 * @see Dominio.IExecutionPeriod#setSemester(java.lang.Integer)
	 */
	public void setSemester(Integer semester) {
		this.semester = semester;		
	}

	/* (non-Javadoc)
	 * @see fileSuport.INode#getSlideName()
	 */
	public String getSlideName() {
		String result = getParentNode().getSlideName()+"/EP"+getIdInternal();
		return result;
	}

	/* (non-Javadoc)
	 * @see fileSuport.INode#getParentNode()
	 */
	public INode getParentNode() {	
		IExecutionYear executionYear = getExecutionYear();	
		return executionYear;
	}

	/**
	 * @return
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param begin_date
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param end_date
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
