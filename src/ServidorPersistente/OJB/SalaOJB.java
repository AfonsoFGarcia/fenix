/*
 * SalaOJB.java
 *
 * Created on 21 de Agosto de 2002, 16:36
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  ars
 */

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ojb.odmg.HasBroker;
import org.odmg.QueryException;

import Dominio.Aula;
import Dominio.Exam;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.ISala;
import Dominio.Sala;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import ServidorPersistente.exceptions.notAuthorizedPersistentDeleteException;
import Util.TipoSala;

public class SalaOJB extends ObjectFenixOJB implements ISalaPersistente {

	public ISala readByName(String nome) throws ExcepcaoPersistencia {
		try {
			ISala sala = null;
			String oqlQuery = "select salanome from " + Sala.class.getName();
			oqlQuery += " where nome = $1";
			query.create(oqlQuery);
			query.bind(nome);
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				sala = (ISala) result.get(0);
			return sala;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(ISala roomToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException {

		ISala roomFromDB = null;

		// If there is nothing to write, simply return.
		if (roomToWrite == null)
			return;

		// Read room from database.
		roomFromDB = this.readByName(roomToWrite.getNome());

		// If room is not in database, then write it.
		if (roomFromDB == null)
			super.lockWrite(roomToWrite);
		// else If the room is mapped to the database, then write any existing changes.
		else if (
			(roomToWrite instanceof Sala)
				&& ((Sala) roomFromDB).getIdInternal().equals(
					((Sala) roomToWrite).getIdInternal())) {
			super.lockWrite(roomToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(ISala sala) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Aula.class.getName();
			oqlQuery += " where sala.nome = $1";
			query.create(oqlQuery);
			query.bind(sala.getNome());
			List result = (List) query.execute();
			if (result.size() != 0) {
				throw new notAuthorizedPersistentDeleteException("Cannot delete rooms with classes");
			} else {
				super.delete(sala);
			}

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}

	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Sala.class.getName();
		super.deleteAll(oqlQuery);
	}

	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Sala.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/**
	 * Reads all salas that with certains properties. The properties are
	 * specified by the arguments of this method. If an argument is
	 * null, then the sala can have any value concerning that
	 * argument. In what concerns the capacidadeNormal and
	 * capacidadeExame, this two arguments specify the minimal value that a sala
	 * can have in order to be selected.
	 *
	 * @return a list with all salas that satisfy the conditions specified by the 
	 * non-null arguments.
	 **/
	public List readSalas(
		String nome,
		String edificio,
		Integer piso,
		Integer tipo,
		Integer capacidadeNormal,
		Integer capacidadeExame)
		throws ExcepcaoPersistencia {
		if (nome == null
			&& edificio == null
			&& piso == null
			&& tipo == null
			&& capacidadeExame == null
			&& capacidadeNormal == null)
			return readAll();

		try {
			StringBuffer oqlQuery = new StringBuffer("select sala from ");
			boolean hasPrevious = false;

			oqlQuery.append(Sala.class.getName()).append(" where ");
			if (nome != null) {
				hasPrevious = true;
				oqlQuery.append("nome = \"").append(nome).append("\"");
			}

			if (edificio != null) {
				if (hasPrevious)
					oqlQuery.append(" and ");
				else
					hasPrevious = true;

				oqlQuery.append(" edificio = \"").append(edificio).append("\"");
			}

			if (piso != null) {
				if (hasPrevious)
					oqlQuery.append(" and ");
				else
					hasPrevious = true;

				oqlQuery.append(" piso = \"").append(piso).append("\"");
			}

			if (tipo != null) {
				if (hasPrevious)
					oqlQuery.append(" and ");
				else
					hasPrevious = true;

				oqlQuery.append(" tipo = \"").append(tipo).append("\"");
			}

			if (capacidadeNormal != null) {
				if (hasPrevious)
					oqlQuery.append(" and ");
				else
					hasPrevious = true;

				oqlQuery
					.append(" capacidadeNormal > \"")
					.append(capacidadeNormal.intValue() - 1)
					.append("\"");
			}

			if (capacidadeExame != null) {
				if (hasPrevious)
					oqlQuery.append(" and ");
				else
					hasPrevious = true;

				oqlQuery
					.append(" capacidadeExame > \"")
					.append(capacidadeExame.intValue() - 1)
					.append("\"");
			}

			query.create(oqlQuery.toString());
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readAvailableRooms(IExam exam) throws ExcepcaoPersistencia {
		List availableRooms = null;

		try {
			String oqlQuery =
				"select exam from " + Exam.class.getName();
			oqlQuery += " where idInternal = $1";
			query.create(oqlQuery);
			query.bind(((Exam) exam).getIdInternal());
			List examList = (List) query.execute();
			lockRead(examList);
			exam = (IExam) examList.get(0);

			oqlQuery =
				"select occupiedrooms from " + Sala.class.getName();
			oqlQuery += " where associatedExams.day = $1";
			oqlQuery += " and associatedExams.beginning = $2";
			oqlQuery += " and associatedExams.idInternal != $3";
			oqlQuery
				+= " and associatedExams.associatedExecutionCourses.executionPeriod.name = $4";
			oqlQuery
				+= " and associatedExams.associatedExecutionCourses.executionPeriod.executionYear.year = $5";
			query.create(oqlQuery);
			query.bind(exam.getDay());
			query.bind(exam.getBeginning());
			query.bind(((Exam) exam).getIdInternal());
			query.bind(
				((IDisciplinaExecucao) exam
					.getAssociatedExecutionCourses()
					.get(0))
					.getExecutionPeriod()
					.getName());
			query.bind(
				((IDisciplinaExecucao) exam
					.getAssociatedExecutionCourses()
					.get(0))
					.getExecutionPeriod()
					.getExecutionYear()
					.getYear());
			List occupiedRooms = (List) query.execute();
			lockRead(occupiedRooms);
			System.out.println("## Salas ocupadas="+occupiedRooms.size());

			oqlQuery = "select allExamRooms from " + Sala.class.getName();
			oqlQuery += " where tipo != $1";
			query.create(oqlQuery);
			query.bind(new TipoSala(TipoSala.LABORATORIO));
			List allExamRooms = (List) query.execute();
			lockRead(allExamRooms);
			System.out.println("## Todas as Salas="+allExamRooms.size());
			availableRooms =
				(List) CollectionUtils.subtract(allExamRooms, occupiedRooms);

			System.out.println("availableRooms.size= " + availableRooms.size());
			for (int i = 0; i < availableRooms.size(); i++) {
				ISala sala = (ISala) availableRooms.get(i);
				System.out.println("sala= " + sala.getNome());
			}

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}

		return availableRooms;
	}

}
