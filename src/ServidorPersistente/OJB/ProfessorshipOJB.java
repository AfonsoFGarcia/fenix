/*
 * Created on 26/Mar/2003
 *
 * 
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.IDisciplinaExecucao;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.Professorship;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Jo�o Mota
 *
 * 
 */
public class ProfessorshipOJB extends ObjectFenixOJB implements IPersistentProfessorship {

	/**
	 * 
	 */
	public ProfessorshipOJB() {
		super();
	}

	public List readAll() throws ExcepcaoPersistencia {

		try {
			String oqlQuery = "select all from " + Professorship.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public IProfessorship readByTeacherAndExecutionCourse(ITeacher teacher, IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia {
		try {
			IProfessorship professorship = null;
			String oqlQuery =
				"select professorship from "
					+ Professorship.class.getName()
					+ " where teacher.teacherNumber = $1"
					+ " and executionCourse.sigla = $2"
					+ " and executionCourse.executionPeriod.name = $3"
					+ " and executionCourse.executionPeriod.executionYear.year = $4";

			query.create(oqlQuery);
			query.bind(teacher.getTeacherNumber());
			query.bind(executionCourse.getSigla());
			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());

			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				professorship = (IProfessorship) result.get(0);
			return professorship;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public IProfessorship readByTeacherAndExecutionCoursePB(ITeacher teacher, IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		
		criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
		criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
		return (IProfessorship) queryObject(Professorship.class, criteria);

	}

	public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia {
		try {

			String oqlQuery = "select professorship from " + Professorship.class.getName() + " where teacher.teacherNumber = $1";

			query.create(oqlQuery);
			query.bind(teacher.getTeacherNumber());

			List result = (List) query.execute();
			lockRead(result);

			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readByExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia {
		try {
			String oqlQuery =
				"select professorship from "
					+ Professorship.class.getName()
					+ " where executionCourse.sigla = $1"
					+ " and executionCourse.executionPeriod.name = $2"
					+ " and executionCourse.executionPeriod.executionYear.year = $3";

			query.create(oqlQuery);
			query.bind(executionCourse.getSigla());
			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());

			List result = (List) query.execute();
			lockRead(result);

			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void delete(IProfessorship professorship) throws ExcepcaoPersistencia {
		super.delete(professorship);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Professorship.class.getName();
		super.deleteAll(oqlQuery);
	}

	public void lockWrite(IProfessorship professorship) throws ExcepcaoPersistencia, ExistingPersistentException {
		IProfessorship professorshipFromDB = null;

		// If there is nothing to write, simply return.
		if (professorship == null)
			return;

		// Read professorship from database.
		professorshipFromDB = this.readByTeacherAndExecutionCourse(professorship.getTeacher(), professorship.getExecutionCourse());

		// If professorship is not in database, then write it.
		if (professorshipFromDB == null)
			super.lockWrite(professorship);

		// else If the professorship is mapped to the database, then write any existing changes.
		else if ((professorship instanceof Professorship) && ((Professorship) professorshipFromDB).getIdInternal().equals(((Professorship) professorship).getIdInternal())) {
			super.lockWrite(professorship);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

}
