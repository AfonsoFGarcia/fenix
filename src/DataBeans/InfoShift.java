/*
 * InfoShift.java
 *
 * Created on 31 de Outubro de 2002, 12:35
 */

package DataBeans;

/**
 *
 * @author  tfc130
 */
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import Util.TipoAula;

public class InfoShift extends InfoObject {
	protected String _nome;
	protected TipoAula _tipo;
	protected Integer _lotacao;
	protected Integer availabilityFinal;
	protected InfoExecutionCourse _infoDisciplinaExecucao;
	protected List infoLessons;

	public InfoShift() {
	}

	public InfoShift(
		String nome,
		TipoAula tipo,
		Integer lotacao,
		InfoExecutionCourse infoDisciplinaExecucao) {
		setNome(nome);
		setTipo(tipo);
		setLotacao(lotacao);
		setInfoDisciplinaExecucao(infoDisciplinaExecucao);
	}

	public String getNome() {
		return _nome;
	}

	public void setNome(String nome) {
		_nome = nome;
	}

	public InfoExecutionCourse getInfoDisciplinaExecucao() {
		return _infoDisciplinaExecucao;
	}

	public void setInfoDisciplinaExecucao(InfoExecutionCourse infoDisciplinaExecucao) {
		_infoDisciplinaExecucao = infoDisciplinaExecucao;
	}

	public TipoAula getTipo() {
		return _tipo;
	}

	public void setTipo(TipoAula tipo) {
		_tipo = tipo;
	}

	public Integer getLotacao() {
		return _lotacao;
	}

	public void setLotacao(Integer lotacao) {
		_lotacao = lotacao;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoShift) {
			InfoShift infoTurno = (InfoShift) obj;
			resultado =
				(getNome().equals(infoTurno.getNome()))
					&& (getInfoDisciplinaExecucao()
						.equals(infoTurno.getInfoDisciplinaExecucao()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[TURNO";
		result += ", nome=" + _nome;
		result += ", tipo=" + _tipo;
		result += ", lotacao=" + _lotacao;
		result += ", infoDisciplinaExecucao=" + _infoDisciplinaExecucao;
		result += "]";
		return result;

	}

	public String getLessons() {
		List infoLessonsList = getInfoLessons();
		Iterator itLesson = infoLessonsList.iterator();
		String result = new String();
		int index = 0;
		
		while (itLesson.hasNext()) {
			index = index+1;
			InfoLesson lesson = (InfoLesson) itLesson.next();
			result += lesson.getDiaSemana().toString();
			result += " (";
			result += ((Calendar) lesson.getInicio()).get(Calendar.HOUR_OF_DAY);
			result += ":";
			result += minutesFormatter(((Calendar) lesson.getInicio()).get(Calendar.MINUTE));
			result += "-";
			result += ((Calendar) lesson.getFim()).get(Calendar.HOUR_OF_DAY);
			result += ":";
			result += minutesFormatter(((Calendar) lesson.getFim()).get(Calendar.MINUTE));
			result += ") ";
			result += lesson.getInfoSala().getNome().toString();
			int last = (infoLessonsList.size());
			if ( index != last || (index != 1 && index != last)) {
				result += " , ";
			} else {
				result += " ";
			}

		}

		return result;
	}

	private String minutesFormatter(int minute){
		String result="";
		if (minute<10){
			result+="0";
		}
		result+=minute;
		
		return result;
	}

	/**
	 * @return
	 */
	public Integer getAvailabilityFinal() {
		return availabilityFinal;
	}

	/**
	 * @param integer
	 */
	public void setAvailabilityFinal(Integer integer) {
		availabilityFinal = integer;
	}

	/**
	 * @return
	 */
	public List getInfoLessons() {
		return infoLessons;
	}

	/**
	 * @param list
	 */
	public void setInfoLessons(List list) {
		infoLessons = list;
	}

}
