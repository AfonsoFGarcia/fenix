/*
 * InfoExecutionCourse.java
 *
 * Created on 28 de Novembro de 2002, 3:41
 */

package DataBeans;

import java.io.Serializable;

/**
 *
 * @author  tfc130
 */
public class InfoExecutionCourse implements Serializable {
	protected String _nome;
	protected String _sigla;
	protected String _programa;
	private Double _theoreticalHours;
	private Double _praticalHours;
	private Double _theoPratHours;
	private Double _labHours;

	
	// A chave do responsavel falta ainda porque ainda nao existe a respeciva ligacao
	// na base de dados.

	private InfoExecutionPeriod infoExecutionPeriod;
	

	public InfoExecutionCourse() {
	}

/**
 * 
 * @param nome
 * @param sigla
 * @param programa
 * @param infoLicenciaturaExecucao
 * @param theoreticalHours
 * @param praticalHours
 * @param theoPratHours
 * @param labHours
 * @deprecated
 */
	public InfoExecutionCourse(
			String nome,
			String sigla,
			String programa,
			InfoExecutionDegree infoLicenciaturaExecucao,
			Double theoreticalHours,
			Double praticalHours,
			Double theoPratHours,
			Double labHours) {
			setNome(nome);
			setSigla(sigla);
			setPrograma(programa);
	//		setInfoLicenciaturaExecucao(infoLicenciaturaExecucao);
			setTheoreticalHours(theoreticalHours);
			setPraticalHours(praticalHours);
			setTheoPratHours(theoPratHours);
			setLabHours(labHours);
			
		}


	public InfoExecutionCourse(
		String nome,
		String sigla,
		String programa,
		Double theoreticalHours,
		Double praticalHours,
		Double theoPratHours,
		Double labHours,
		InfoExecutionPeriod infoExecutionPeriod) {
		setNome(nome);
		setSigla(sigla);
		setPrograma(programa);
		setTheoreticalHours(theoreticalHours);
		setPraticalHours(praticalHours);
		setTheoPratHours(theoPratHours);
		setLabHours(labHours);
		setInfoExecutionPeriod(infoExecutionPeriod);
	}


/**
 * @deprecated
 * @param nome
 * @param sigla
 * @param programa
 * @param infoLicenciaturaExecucao
 * @param theoreticalHours
 * @param praticalHours
 * @param theoPratHours
 * @param labHours
 * @param semester
 */
	public InfoExecutionCourse(
		String nome,
		String sigla,
		String programa,
		InfoExecutionDegree infoLicenciaturaExecucao,
		Double theoreticalHours,
		Double praticalHours,
		Double theoPratHours,
		Double labHours,
		Integer semester) {
		setNome(nome);
		setSigla(sigla);
		setPrograma(programa);
	//	setInfoLicenciaturaExecucao(infoLicenciaturaExecucao);
		setTheoreticalHours(theoreticalHours);
		setPraticalHours(praticalHours);
		setTheoPratHours(theoPratHours);
		setLabHours(labHours);
	}
	public String getNome() {
		return _nome;
	}

	public void setNome(String nome) {
		_nome = nome;
	}

	

	public String getSigla() {
		return _sigla;
	}

	public void setSigla(String sigla) {
		_sigla = sigla;
	}

	public String getPrograma() {
		return _programa;
	}

	public void setPrograma(String programa) {
		_programa = programa;
	}

	public Double getTheoreticalHours() {
		return _theoreticalHours;
	}

	public void setTheoreticalHours(Double theoreticalHours) {
		_theoreticalHours = theoreticalHours;
	}

	public Double getPraticalHours() {
		return _praticalHours;
	}

	public void setPraticalHours(Double praticalHours) {
		_praticalHours = praticalHours;
	}

	public Double getTheoPratHours() {
		return _theoPratHours;
	}

	public void setTheoPratHours(Double theoPratHours) {
		_theoPratHours = theoPratHours;
	}

	public Double getLabHours() {
		return _labHours;
	}

	public void setLabHours(Double labHours) {
		_labHours = labHours;
	}

	

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoExecutionCourse) {
			InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) obj;
			resultado =
				(getSigla().equals(infoExecutionCourse.getSigla())
					&& getInfoExecutionPeriod().equals(infoExecutionCourse.getInfoExecutionPeriod()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[INFODISCIPLINAEXECUCAO";
		result += ", nome=" + _nome;
		result += ", sigla=" + _sigla;
		result += ", programa=" + _programa;
		result += ", theoreticalHours=" + _theoreticalHours;
		result += ", praticalHours=" + _praticalHours;
		result += ", theoPratHours=" + _theoPratHours;
		result += ", labHours=" + _labHours;
		result += ", infoExecutionPeriod=" + infoExecutionPeriod;
		result += "]";
		return result;
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
