/*
 * AulaOJB.java
 *
 * Created on 18 de Outubro de 2002, 00:34
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.odmg.QueryException;

import Dominio.Aula;
import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import Dominio.ITurnoAula;
import Dominio.TurnoAula;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DiaSemana;
import Util.TipoAula;

public class AulaOJB extends ObjectFenixOJB implements IAulaPersistente {

	public IAula readByDiaSemanaAndInicioAndFimAndSala(
		DiaSemana diaSemana,
		Calendar inicio,
		Calendar fim,
		ISala sala)
		throws ExcepcaoPersistencia {
		try {
			IAula aula = null;
			String oqlQuery =
				"select diasemanainiciofimsala from " + Aula.class.getName();
			oqlQuery += " where diaSemana = $1";
			oqlQuery += " and inicio = $2";
			oqlQuery += " and fim = $3";
			oqlQuery += " and sala.nome = $4";
			query.create(oqlQuery);
			query.bind(diaSemana);
			query.bind(inicio);
			query.bind(fim);
			query.bind(sala.getNome());
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				aula = (IAula) result.get(0);
			return aula;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(IAula lessonToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {
		IAula lessonFromDB = null;
		if (lessonToWrite == null)
			// Should we throw an exception saying nothing to write or
			// something of the sort?
			// By default, if OJB received a null object it would complain.
			return;

		// read lesson		
		lessonFromDB =
			this.readByDiaSemanaAndInicioAndFimAndSala(
				lessonToWrite.getDiaSemana(),
				lessonToWrite.getInicio(),
				lessonToWrite.getFim(),
				lessonToWrite.getSala());

		// if (lesson not in database) then write it
		if (lessonFromDB == null)
			super.lockWrite(lessonToWrite);
		// else if (lesson is mapped to the database then write any existing changes)
		else if ((lessonToWrite instanceof Aula) &&
				 ((Aula) lessonFromDB).getCodigoInterno().equals(
		          ((Aula) lessonToWrite).getCodigoInterno())) {

			lessonFromDB.setDisciplinaExecucao(
				lessonToWrite.getDisciplinaExecucao());
			lessonFromDB.setTipo(lessonToWrite.getTipo());
			// No need to werite it because it is already mapped.
			//super.lockWrite(lessonToWrite);
			// else throw an AlreadyExists exception.
		} else
			throw new ExistingPersistentException();
	}

	public void delete(IAula aula) throws ExcepcaoPersistencia {
		try {
			ITurnoAula turnoAula = null;
			TurnoAulaOJB turnoAulaOJB = new TurnoAulaOJB();
			String oqlQuery = "select all from " + TurnoAula.class.getName();
			oqlQuery += " where aula.diaSemana = $1"
				+ " and  aula.inicio = $2"
				+ " and aula.fim = $3"
				+ " and aula.sala.nome= $4";
			query.create(oqlQuery);
			query.bind(aula.getDiaSemana());
			query.bind(aula.getInicio());
			query.bind(aula.getFim());
			query.bind(aula.getSala().getNome());
			List result = (List) query.execute();
			lockRead(result);
			Iterator iterador = result.iterator();
			while (iterador.hasNext()) {
				turnoAula = (ITurnoAula) iterador.next();
				turnoAulaOJB.delete(turnoAula);
			}
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
		super.delete(aula);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Aula.class.getName();
		super.deleteAll(oqlQuery);
	}

	public List readByExecutionCourse(IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia {
		if (executionCourse == null)
			return new ArrayList();
		try {
			String oqlQuery = "select aulas from " + Aula.class.getName();
			oqlQuery += " where disciplinaExecucao.sigla = $1";
			oqlQuery += " and disciplinaExecucao.executionPeriod.name = $2";
			oqlQuery
				+= " and disciplinaExecucao.executionPeriod.executionYear.year = $3";

			query.create(oqlQuery);

			query.bind(executionCourse.getSigla());
			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(
				executionCourse
					.getExecutionPeriod()
					.getExecutionYear()
					.getYear());

			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readByExecutionCourseAndLessonType(
		IDisciplinaExecucao executionCourse,
		TipoAula lessonType)
		throws ExcepcaoPersistencia {
		try {
			//			List aulas = new ArrayList();
			String oqlQuery =
				"select aulas from "
					+ Aula.class.getName()
					+ " where tipo = $1"
					+ " and disciplinaExecucao.sigla = $2"
					+ " and disciplinaExecucao.executionPeriod.name = $3"
					+ " and disciplinaExecucao.executionPeriod.executionYear.year = $4";
			query.create(oqlQuery);

			query.bind(lessonType);

			query.bind(executionCourse.getSigla());

			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(
				executionCourse
					.getExecutionPeriod()
					.getExecutionYear()
					.getYear());

			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readByRoomAndExecutionPeriod(
		ISala room,
		IExecutionPeriod executionPeriod)
		throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select aulas from " + Aula.class.getName();
			oqlQuery += " where sala.nome = $1"
				+ " and disciplinaExecucao.executionPeriod.name = $2"
				+ " and disciplinaExecucao.executionPeriod.executionYear.year = $3";
			query.create(oqlQuery);
			query.bind(room.getNome());
			query.bind(executionPeriod.getName());
			query.bind(executionPeriod.getExecutionYear().getYear());

			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readLessonsInPeriod(IAula lesson) throws ExcepcaoPersistencia {
		try {
			List lessonList = null;
			String oqlQuery = "select aulas from " + Aula.class.getName();
			oqlQuery += " where inicio >= $1 "
				+ "and fim <= $2 "
				+ "and diaSemana = $3";
			// 
			//oqlQuery += " and disciplinaExecucao.semestre = $2";
			query.create(oqlQuery);
			query.bind(lesson.getInicio());
			query.bind(lesson.getFim());
			query.bind(lesson.getDiaSemana());
			//query.bind(semestre);
			lessonList = (List) query.execute();
			lockRead(lessonList);
			return lessonList;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readLessonsInBroadPeriod(IAula newLesson, IAula oldLesson)
		throws ExcepcaoPersistencia {
		try {
			List lessonList = null;
			String oqlQuery = "select aulas from " + Aula.class.getName();
			oqlQuery += " where ( inicio >$1 "
				+ "and inicio <$2 "
				+ "and diaSemana = $3 "
				+ "and sala.nome = $4 ) "
				+ "or ( fim > $5 "
				+ "and fim < $6 "
				+ "and diaSemana = $7 "
				+ "and sala.nome = $8 )"
				+ "or ( inicio = $9 "
				+ "and fim = $10 "
				+ "and diaSemana = $11 "
				+ "and sala.nome = $12 )"
				+ "or ( inicio <= $13 "
				+ "and fim >= $14"
				+ "and diaSemana = $15 "
				+ "and sala.nome = $16 )"
				+ "or ( inicio = $17 "
				+ "and fim <= $18 "
				+ "and diaSemana = $19 "
				+ "and sala.nome = $20 )";
			
			query.create(oqlQuery);
			query.bind(newLesson.getInicio());
			query.bind(newLesson.getFim());
			query.bind(newLesson.getDiaSemana());
			query.bind(newLesson.getSala().getNome());

			query.bind(newLesson.getInicio());
			query.bind(newLesson.getFim());
			query.bind(newLesson.getDiaSemana());
			query.bind(newLesson.getSala().getNome());

			query.bind(newLesson.getInicio());
			query.bind(newLesson.getFim());
			query.bind(newLesson.getDiaSemana());
			query.bind(newLesson.getSala().getNome());

			query.bind(newLesson.getInicio());
			query.bind(newLesson.getFim());
			query.bind(newLesson.getDiaSemana());
			query.bind(newLesson.getSala().getNome());

			query.bind(newLesson.getInicio());
			query.bind(newLesson.getFim());
			query.bind(newLesson.getDiaSemana());
			query.bind(newLesson.getSala().getNome());

			lessonList = (List) query.execute();
			lockRead(lessonList);

			// Remove the Lesson that is being edited from the list
			// of intercepting lessons.
			if (oldLesson != null && oldLesson instanceof Aula)
				for (int ipto = 0; ipto < lessonList.size(); ipto++)
					if (((Aula) lessonList.get(ipto))
						.getCodigoInterno()
						.equals(((Aula) oldLesson).getCodigoInterno()))
						lessonList.remove(ipto);
			
			return lessonList;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}
