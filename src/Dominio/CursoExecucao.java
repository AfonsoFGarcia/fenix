/*
 * CursoExecucao.java
 *
 * Created on 2 de Novembro de 2002, 20:53
 */

package Dominio;

import java.util.List;

/**
 *
 * @author  rpfi
 */

public class CursoExecucao extends DomainObject implements ICursoExecucao {	
	private Integer keyCurricularPlan;
	private Integer keyCoordinator;

	private IExecutionYear executionYear;

	private Integer academicYear;

	private IDegreeCurricularPlan degreeCurricularPlan;
	
	//added by T�nia Pous�o
	private List coordinatorsList;
	
	private Boolean temporaryExamMap;
	
	//added by T�nia Pous�o
	private Integer keyCampus;
	private ICampus campus;
	

	/** Construtor sem argumentos publico requerido pela moldura de objectos OJB */
	public CursoExecucao() {
	}

	public CursoExecucao(
		IExecutionYear executionYear,
		IDegreeCurricularPlan curricularPlan) {
		setExecutionYear(executionYear);
		setCurricularPlan(curricularPlan);
	}

	/**
	 * @param executionDegreeId
	 */
	public CursoExecucao(Integer executionDegreeId) {
		setIdInternal(executionDegreeId);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof ICursoExecucao) {
			ICursoExecucao cursoExecucao = (ICursoExecucao) obj;
			resultado = getExecutionYear().equals(cursoExecucao.getExecutionYear())
				&& getCurricularPlan().equals(cursoExecucao.getCurricularPlan());
					
		}
		return resultado;
	}

	public String toString() {
		String result = "[CURSO_EXECUCAO";
		result += ", codInt=" + getIdInternal();
		result += ", executionYear=" + executionYear;
		result += ", keyExecutionYear=" + academicYear;
		result += ", degreeCurricularPlan=" + degreeCurricularPlan;
		if(coordinatorsList != null) {
			result += ", coordinatorsList=" + coordinatorsList.size();
		} else {
			result += ", coordinatorsList is NULL";
		}
		result += ", campus=" + campus;		
		result += "]";
		return result;
	}


	

	/**
	 * 
	 * @see Dominio.ICursoExecucao#getExecutionYear()
	 */
	public IExecutionYear getExecutionYear() {
		return executionYear;
	}

	/**
	 * 
	 * @see Dominio.ICursoExecucao#setExecutionYear(IExecutionYear)
	 */
	public void setExecutionYear(IExecutionYear executionYear) {
		this.executionYear = executionYear;
	}

	/**
	 * Returns the academicYear.
	 * @return Integer
	 */
	public Integer getAcademicYear() {
		return academicYear;
	}

	/**
	 * Sets the academicYear.
	 * @param academicYear The academicYear to set
	 */
	public void setAcademicYear(Integer academicYear) {
		this.academicYear = academicYear;
	}

	/**
	 * Returns the curricularPlan.
	 * @return IDegreeCurricularPlan
	 */
	public IDegreeCurricularPlan getCurricularPlan() {
		return degreeCurricularPlan;
	}

	/**
	 * Returns the keyCurricularPlan.
	 * @return Integer
	 */
	public Integer getKeyCurricularPlan() {
		return keyCurricularPlan;
	}

	/**
	 * Sets the curricularPlan.
	 * @param curricularPlan The curricularPlan to set
	 */
	public void setCurricularPlan(IDegreeCurricularPlan curricularPlan) {
		this.degreeCurricularPlan = curricularPlan;
	}

	/**
	 * Sets the keyCurricularPlan.
	 * @param keyCurricularPlan The keyCurricularPlan to set
	 */
	public void setKeyCurricularPlan(Integer keyCurricularPlan) {
		this.keyCurricularPlan = keyCurricularPlan;
	}

	/**
	 * @return
	 */
	public IDegreeCurricularPlan getDegreeCurricularPlan() {
		return degreeCurricularPlan;
	}

	/**
	 * @return
	 */
	public Integer getKeyCoordinator() {
		return keyCoordinator;
	}

	/**
	 * @param plan
	 */
	public void setDegreeCurricularPlan(IDegreeCurricularPlan plan) {
		degreeCurricularPlan = plan;
	}

	/**
	 * @param integer
	 */
	public void setKeyCoordinator(Integer integer) {
		keyCoordinator = integer;
	}

	/**
	 * @return
	 */
	public Boolean getTemporaryExamMap() {
		return temporaryExamMap;
	}

	/**
	 * @param boolean1
	 */
	public void setTemporaryExamMap(Boolean temporary) {
		temporaryExamMap = temporary;
	}

	/**
	 * @return Returns the campus.
	 */
	public ICampus getCampus() {
		return campus;
	}

	/**
	 * @param campus The campus to set.
	 */
	public void setCampus(ICampus campus) {
		this.campus = campus;
	}

	/**
	 * @return Returns the keyCampus.
	 */
	public Integer getKeyCampus() {
		return keyCampus;
	}

	/**
	 * @param keyCampus The keyCampus to set.
	 */
	public void setKeyCampus(Integer keyCampus) {
		this.keyCampus = keyCampus;
	}

	/**
	 * @return Returns the coordinatorsList.
	 */
	public List getCoordinatorsList()
	{
		return coordinatorsList;
	}

	/**
	 * @param coordinatorsList The coordinatorsList to set.
	 */
	public void setCoordinatorsList(List coordinatorsList)
	{
		this.coordinatorsList = coordinatorsList;
	}	
}
