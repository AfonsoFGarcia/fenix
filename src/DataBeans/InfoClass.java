/*
 * InfoClass.java
 *
 * Created on 31 de Outubro de 2002, 12:27
 */

package DataBeans;

import java.io.Serializable;

/**
 *
 * @author  tfc130
 */

public class InfoClass extends InfoObject implements Serializable {
	protected String _name;
	
	protected Integer _anoCurricular;
	

	private InfoExecutionDegree infoExecutionDegree;
	private InfoExecutionPeriod infoExecutionPeriod;

	public InfoClass() {
	}

	public InfoClass(
		String name,
		Integer anoCurricular,
		InfoExecutionDegree infoExecutionDegree,
		InfoExecutionPeriod infoExecutionPeriod) {
		setNome(name);
		setAnoCurricular(anoCurricular);
		setInfoExecutionDegree(infoExecutionDegree);
		setInfoExecutionPeriod(infoExecutionPeriod);
	}

	

	public String getNome() {
		return _name;
	}

	public void setNome(String nome) {
		_name = nome;
	}

	

	public Integer getAnoCurricular() {
		return _anoCurricular;
	}

	public void setAnoCurricular(Integer anoCurricular) {
		_anoCurricular = anoCurricular;
	}

	

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoClass) {
			InfoClass infoTurma = (InfoClass) obj;
			resultado =
				getNome().equals(infoTurma.getNome())
					&& getInfoExecutionPeriod().equals(infoTurma.getInfoExecutionPeriod())
					&& getInfoExecutionDegree().equals(infoTurma.getInfoExecutionDegree());
		}
		return resultado;
	}

	public String toString() {
		String result = "[INFOTURMA";
		result += ", nome=" + _name;
		result += ", infoExecutionPeriod=" + infoExecutionPeriod;
		result += ", infoExecutionDegree=" + infoExecutionDegree;
		result += "]";
		return result;
	}

	/**
	 * Returns the infoExecutionDegree.
	 * @return InfoExecutionDegree
	 */
	public InfoExecutionDegree getInfoExecutionDegree() {
		return infoExecutionDegree;
	}

	/**
	 * Sets the infoExecutionDegree.
	 * @param infoExecutionDegree The infoExecutionDegree to set
	 */
	public void setInfoExecutionDegree(InfoExecutionDegree infoExecutionDegree) {
		this.infoExecutionDegree = infoExecutionDegree;
	}

	/**
	 * Returns the infoExecutionPeriod.
	 * @return InfoExecutionPeriod
	 */
	public InfoExecutionPeriod getInfoExecutionPeriod() {
		return infoExecutionPeriod;
	}

	/**
	 * Sets the infoExecutionPeriod.
	 * @param infoExecutionPeriod The infoExecutionPeriod to set
	 */
	public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
		this.infoExecutionPeriod = infoExecutionPeriod;
	}

}
