/*
 * Sala.java
 *
 * Created on 17 de Outubro de 2002, 22:56
 */

package Dominio;

/**
 *
 * @author  tfc130
 */
import java.util.List;

import Util.TipoSala;

public class Sala implements ISala, IDomainObject {
	protected String _nome;
	protected String _edificio;
	protected Integer _piso;
	protected Integer _capacidadeNormal;
	protected Integer _capacidadeExame;
	protected TipoSala _tipo;

	// c�digos internos da base de dados
	private Integer idInternal;

	/** Construtor sem argumentos p�blico requerido pela moldura de objectos OJB */
	public Sala() {
	}

	public Sala(
		String nome,
		String edificio,
		Integer piso,
		TipoSala tipo,
		Integer capacidadeNormal,
		Integer capacidadeExame) {
		setNome(nome);
		setEdificio(edificio);
		setPiso(piso);
		setTipo(tipo);
		setCapacidadeNormal(capacidadeNormal);
		setCapacidadeExame(capacidadeExame);
	}



	public String getNome() {
		return _nome;
	}

	public void setNome(String nome) {
		_nome = nome;
	}

	public String getEdificio() {
		return _edificio;
	}

	public void setEdificio(String edificio) {
		_edificio = edificio;
	}

	public Integer getPiso() {
		return _piso;
	}

	public void setPiso(Integer piso) {
		_piso = piso;
	}

	public TipoSala getTipo() {
		return _tipo;
	}

	public void setTipo(TipoSala tipo) {
		_tipo = tipo;
	}

	public Integer getCapacidadeNormal() {
		return _capacidadeNormal;
	}

	public void setCapacidadeNormal(Integer capacidadeNormal) {
		_capacidadeNormal = capacidadeNormal;
	}

	public Integer getCapacidadeExame() {
		return _capacidadeExame;
	}

	public void setCapacidadeExame(Integer capacidadeExame) {
		_capacidadeExame = capacidadeExame;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof ISala) {
			ISala sala = (ISala) obj;
			resultado =
				(getNome().equals(sala.getNome()))
					&& (getEdificio().equals(sala.getEdificio()))
					&& (getPiso().equals(sala.getPiso()))
					&& (getTipo().equals(sala.getTipo()))
					&& (getCapacidadeNormal().equals(sala.getCapacidadeNormal()))
					&& (getCapacidadeExame().equals(sala.getCapacidadeExame()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[SALA";
		result += ", codInt=" + idInternal;
		result += ", nome=" + _nome;
		result += ", edificio=" + _edificio;
		result += ", piso=" + _piso;
		result += ", tipo=" + _tipo;
		result += ", capacidadeNormal=" + _capacidadeNormal;
		result += ", capacidadeExame=" + _capacidadeExame;
		result += "]";
		return result;
	}

	/**
	 * @return
	 */
	public Integer getIdInternal() {
		return idInternal;
	}

	/**
	 * @param internal
	 */
	public void setIdInternal(Integer internal) {
		idInternal = internal;
	}

}
