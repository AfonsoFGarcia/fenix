/*
 * Funcionario.java
 *
 */

package Dominio;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author  Ivo Brand�o
 */
public class Funcionario {

	private int codigoInterno = 0;
	private int chavePessoa = 0;
	private int numeroMecanografico = 0;
	private int chaveHorarioActual = 0;
	private Date antiguidade = null;
	private int chaveFuncResponsavel = 0;
	private int chaveCCLocalTrabalho = 0;
	private int chaveCCCorrespondencia = 0;
	private int chaveCCVencimento = 0;
	private String calendario = null;
	private int chaveStatus = 0;
	private Date dataInicio = null;
	private Date dataFim = null;
	private int quem = 0;
	private Timestamp quando = null;

	/** Construtor por omissao */
	public Funcionario() {
		this.codigoInterno = 0;
		this.chavePessoa = 0;
		this.numeroMecanografico = 0;
		this.chaveHorarioActual = 0;
		this.antiguidade = new Date(0);
		this.chaveFuncResponsavel = 0;
		this.chaveCCLocalTrabalho = 0;
		this.chaveCCCorrespondencia = 0;
		this.chaveCCVencimento = 0;
		this.calendario = new String("LISBOA");
		this.chaveStatus = 0;
		this.dataInicio = new Date(0);
		this.dataFim = new Date(0);
		this.quem = 0;
		this.quando = new Timestamp(0);
	}

	/** Construtor */
	public Funcionario(int codigoInterno, int chavePessoa, int numeroMecanografico, int chaveHorarioActual) {

		this.codigoInterno = codigoInterno;
		this.chavePessoa = chavePessoa;
		this.numeroMecanografico = numeroMecanografico;
		this.chaveHorarioActual = chaveHorarioActual;
		this.antiguidade = new Date(0);
		this.chaveFuncResponsavel = 0;
		this.chaveCCLocalTrabalho = 0;
		this.chaveCCCorrespondencia = 0;
		this.chaveCCVencimento = 0;
		this.calendario = new String("LISBOA");
		this.chaveStatus = 0;
		this.dataInicio = new Date(0);
		this.dataFim = new Date(0);
		this.quem = 0;
		this.quando = new Timestamp(0);

	}

	public Funcionario(
		int codigoInterno,
		int chavePessoa,
		int numeroMecanografico,
		int chaveHorarioActual,
		int chaveCCLocalTrabalho,
		int chaveCCCorrespondencia,
		int chaveCCVencimento) {

		this.codigoInterno = codigoInterno;
		this.chavePessoa = chavePessoa;
		this.numeroMecanografico = numeroMecanografico;
		this.chaveHorarioActual = chaveHorarioActual;
		this.antiguidade = new Date(0);
		this.chaveFuncResponsavel = 0;
		this.chaveCCLocalTrabalho = chaveCCLocalTrabalho;
		this.chaveCCCorrespondencia = chaveCCCorrespondencia;
		this.chaveCCVencimento = chaveCCVencimento;
		this.calendario = new String("LISBOA");
		this.chaveStatus = 0;
		this.dataInicio = new Date(0);
		this.dataFim = new Date(0);
		this.quem = 0;
		this.quando = new Timestamp(0);
	}

	public Funcionario(
		int codigoInterno,
		int chavePessoa,
		int numeroMecanografico,
		int chaveHorarioActual,
		Date antiguidade,
		int chaveFuncResponsavel,
		int chaveCCLocalTrabalho,
		int chaveCCCorrespondencia,
		int chaveCCVencimento,
		String calendario,
		int chaveStatus,
		Date dataInicio,
		Date dataFim,
		int quem,
		Timestamp quando) {

		this.codigoInterno = codigoInterno;
		this.chavePessoa = chavePessoa;
		this.numeroMecanografico = numeroMecanografico;
		this.chaveHorarioActual = chaveHorarioActual;
		this.antiguidade = antiguidade;
		this.chaveFuncResponsavel = chaveFuncResponsavel;
		this.chaveCCLocalTrabalho = chaveCCLocalTrabalho;
		this.chaveCCCorrespondencia = chaveCCCorrespondencia;
		this.chaveCCVencimento = chaveCCVencimento;
		this.calendario = calendario;
		this.chaveStatus = chaveStatus;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.quem = quem;
		this.quando = quando;
	}

	/** Verifica se outro objecto e identico a este */
	public boolean equals(Object obj) {

		return (
			(obj instanceof Funcionario)
				&& (codigoInterno == ((Funcionario) obj).getCodigoInterno())
				&& (chavePessoa == ((Funcionario) obj).getChavePessoa())
				&& (numeroMecanografico == ((Funcionario) obj).getNumeroMecanografico())
				&& (chaveHorarioActual == ((Funcionario) obj).getChaveHorarioActual())
				&& (antiguidade == ((Funcionario) obj).getAntiguidade())
				&& (chaveFuncResponsavel == ((Funcionario) obj).getChaveFuncResponsavel())
				&& (chaveCCLocalTrabalho == ((Funcionario) obj).getChaveCCLocalTrabalho())
				&& (chaveCCCorrespondencia == ((Funcionario) obj).getChaveCCCorrespondencia())
				&& (chaveCCVencimento == ((Funcionario) obj).getChaveCCVencimento())
				&& (calendario == ((Funcionario) obj).getCalendario())
		&& (chaveStatus == ((Funcionario) obj).getChaveStatus())
				&& (dataInicio == ((Funcionario) obj).getDataInicio())
				&& (dataFim == ((Funcionario) obj).getDataFim())
				&& (quem == ((Funcionario) obj).getQuem())
				&& (quando == ((Funcionario) obj).getQuando()));
	}

	/** Getter for property codigoInterno.
	 * @return Value of property codigoInterno.
	 *
	 */
	public int getCodigoInterno() {
		return codigoInterno;
	}

	/** Setter for property codigoInterno.
	 * @param codigoInterno New value of property codigoInterno.
	 *
	 */
	public void setCodigoInterno(int codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	/** Getter for property chavePessoa.
	 * @return Value of property chavePessoa.
	 *
	 */
	public int getChavePessoa() {
		return chavePessoa;
	}

	/** Setter for property chavePessoa.
	 * @param chavePessoa New value of property chavePessoa.
	 *
	 */
	public void setChavePessoa(int chavePessoa) {
		this.chavePessoa = chavePessoa;
	}

	/** Getter for property numeroMecanografico.
	 * @return Value of property numeroMecanografico.
	 *
	 */
	public int getNumeroMecanografico() {
		return numeroMecanografico;
	}

	/** Setter for property numeroMecanografico.
	 * @param numeroMecanografico New value of property numeroMecanografico.
	 *
	 */
	public void setNumeroMecanografico(int numeroMecanografico) {
		this.numeroMecanografico = numeroMecanografico;
	}

	/** Getter for property chaveHorarioActual.
	 * @return Value of property chaveHorarioActual.
	
	 */
	public int getChaveHorarioActual() {
		return chaveHorarioActual;
	}

	/** Setter for property chaveHorarioActual.
	 * @param chaveHorarioActual New value of property chaveHorarioActual.
	 *
	 */
	public void setChaveHorarioActual(int chaveHorarioActual) {
		this.chaveHorarioActual = chaveHorarioActual;
	}

	/**
	 * @return Date
	 */
	public Date getAntiguidade() {
		return antiguidade;
	}

	/**
	 * @return String
	 */
	public String getCalendario() {
		return calendario;
	}

	/**
	 * @return int
	 */
	public int getChaveCCCorrespondencia() {
		return chaveCCCorrespondencia;
	}

	/**
	 * @return int
	 */
	public int getChaveCCLocalTrabalho() {
		return chaveCCLocalTrabalho;
	}

	/**
	 * @return int
	 */
	public int getChaveCCVencimento() {
		return chaveCCVencimento;
	}

	/**
	 * @return int
	 */
	public int getChaveFuncResponsavel() {
		return chaveFuncResponsavel;
	}

	/** Getter for property chaveStatus.
	 * @return Value of property chaveStatus.
	 *
	 */
	public int getChaveStatus() {
		return chaveStatus;
	}

	/** Setter for property chaveStatus.
	 * @param chaveStatus New value of property chaveStatus.
	 *
	 */
	public void setChaveStatus(int chaveStatus) {
		this.chaveStatus = chaveStatus;
	}

	/**
	 * @return Date
	 */
	public Date getDataFim() {
		return dataFim;
	}

	/**
	 * @return Date
	 */
	public Date getDataInicio() {
		return dataInicio;
	}

	/**
	 * Sets the antiguidade.
	 * @param antiguidade The antiguidade to set
	 */
	public void setAntiguidade(Date antiguidade) {
		this.antiguidade = antiguidade;
	}

	/**
	 * Sets the calendario.
	 * @param calendario The calendario to set
	 */
	public void setCalendario(String calendario) {
		this.calendario = calendario;
	}

	/**
	 * Sets the chaveCCCorrespondencia.
	 * @param chaveCCCorrespondencia The chaveCCCorrespondencia to set
	 */
	public void setChaveCCCorrespondencia(int chaveCCCorrespondencia) {
		this.chaveCCCorrespondencia = chaveCCCorrespondencia;
	}

	/**
	 * Sets the chaveCCLocalTrabalho.
	 * @param chaveCCLocalTrabalho The chaveCCLocalTrabalho to set
	 */
	public void setChaveCCLocalTrabalho(int chaveCCLocalTrabalho) {
		this.chaveCCLocalTrabalho = chaveCCLocalTrabalho;
	}

	/**
	 * Sets the chaveCCVencimento.
	 * @param chaveCCVencimento The chaveCCVencimento to set
	 */
	public void setChaveCCVencimento(int chaveCCVencimento) {
		this.chaveCCVencimento = chaveCCVencimento;
	}

	/**
	 * Sets the chaveFuncResponsavel.
	 * @param chaveFuncResponsavel The chaveFuncResponsavel to set
	 */
	public void setChaveFuncResponsavel(int chaveFuncResponsavel) {
		this.chaveFuncResponsavel = chaveFuncResponsavel;
	}

	/**
	 * Sets the dataFim.
	 * @param dataFim The dataFim to set
	 */
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	/**
	 * Sets the dataInicio.
	 * @param dataInicio The dataInicio to set
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return Timestamp
	 */
	public Timestamp getQuando() {
		return quando;
	}

	/**
	 * @return int
	 */
	public int getQuem() {
		return quem;
	}

	/**
	 * Sets the quando.
	 * @param quando The quando to set
	 */
	public void setQuando(Timestamp quando) {
		this.quando = quando;
	}

	/**
	 * Sets the quem.
	 * @param quem The quem to set
	 */
	public void setQuem(int quem) {
		this.quem = quem;
	}

}
