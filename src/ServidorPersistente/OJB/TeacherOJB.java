/*
 * TeacherOJB.java
 */
package ServidorPersistente.OJB;
import java.util.List;

import org.odmg.QueryException;

import Dominio.IDisciplinaExecucao;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
/**
 *
 * @author  EP 15
 * @author Ivo Brand�o
 */
public class TeacherOJB extends ObjectFenixOJB implements IPersistentTeacher {

	public ITeacher readTeacherByUsername(String user)
		throws ExcepcaoPersistencia {
		try {
			ITeacher teacher = null;

			String oqlQuery = "select teacher from " + Teacher.class.getName();
			oqlQuery += " where person.username = $1";

			query.create(oqlQuery);
			query.bind(user);

			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0) {
				teacher = (ITeacher) result.get(0);
			}

			return teacher;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	public ITeacher readTeacherByNumber(Integer teacherNumber)
		throws ExcepcaoPersistencia {
		try {
			ITeacher teacher = null;

			String oqlQuery = "select teacher from " + Teacher.class.getName();
			oqlQuery += " where teacherNumber = $1";

			query.create(oqlQuery);
			query.bind(teacherNumber);

			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0) {
				teacher = (ITeacher) result.get(0);
			}

			return teacher;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	public void lockWrite(ITeacher teacher) throws ExcepcaoPersistencia {
		super.lockWrite(teacher);
	}
	public void delete(ITeacher teacher) throws ExcepcaoPersistencia {
		super.delete(teacher);
	}
	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Teacher.class.getName();
		super.deleteAll(oqlQuery);
	}
	
	
	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Teacher.class.getName();

			query.create(oqlQuery);

			List result = (List) query.execute();
			lockRead(result);

			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentTeacher#readTeacherByExecutionCourse()
	 */
	public List readTeacherByExecutionCourseProfessorship(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select teacher from " + Teacher.class.getName();			
			oqlQuery += " where professorShipsExecutionCourses.executionPeriod.executionYear.year = $1";
			oqlQuery += " and professorShipsExecutionCourses.sigla = $2";
			oqlQuery += " and professorShipsExecutionCourses.executionPeriod.name = $3";
			
			query.create(oqlQuery);
			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
			query.bind(executionCourse.getSigla());
			query.bind(executionCourse.getExecutionPeriod().getName());
			
			
			List result = (List) query.execute();
			lockRead(result);
			
			return result;
		} catch (Exception ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentTeacher#readTeacherByExecutionCourseResponsibility(Dominio.IDisciplinaExecucao)
	 */
	public List readTeacherByExecutionCourseResponsibility(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia {
		try {
				String oqlQuery = "select teacher from " + Teacher.class.getName();
				oqlQuery += " where responsibleForExecutionCourses.executionPeriod.executionYear.year = $1";
				oqlQuery += " and responsibleForExecutionCourses.sigla = $2";
				oqlQuery += " and responsibleForExecutionCourses.executionPeriod.name = $3";
				query.create(oqlQuery);
				query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());				
				query.bind(executionCourse.getSigla());
				query.bind(executionCourse.getExecutionPeriod().getName());
				List result = (List) query.execute();
				lockRead(result);
							
				return result;
			} catch (Exception ex) {
				throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
			}
	}

}
