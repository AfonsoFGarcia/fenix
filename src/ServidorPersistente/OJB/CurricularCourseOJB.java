package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.CurricularCourse;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */

public class CurricularCourseOJB extends ObjectFenixOJB implements IPersistentCurricularCourse {

	public CurricularCourseOJB() {
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			super.deleteAll(oqlQuery);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public ICurricularCourse readCurricularCourseByNameAndCode(String name, String code) throws ExcepcaoPersistencia {
		try {
			ICurricularCourse curricularCourse = null;
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where name = $1";
			oqlQuery += " and code = $2";
			query.create(oqlQuery);
			query.bind(name);
			query.bind(code);

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				curricularCourse = (ICurricularCourse) result.get(0);
			}
			return curricularCourse;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void lockWrite(ICurricularCourse curricularCourseToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

		ICurricularCourse curricularCourseFromBD = null;

		// If there is nothing to write, simply return.
		if (curricularCourseToWrite == null) {
			return;
		}

		// Read branch from database.
		curricularCourseFromBD = this.readCurricularCourseByNameAndCode(curricularCourseToWrite.getName(), curricularCourseToWrite.getCode());

		// If branch is not in database, then write it.
		if (curricularCourseFromBD == null) {
			super.lockWrite(curricularCourseToWrite);
			// else If the branch is mapped to the database, then write any existing changes.
		} else if (
			(curricularCourseToWrite instanceof CurricularCourse)
				&& ((CurricularCourse) curricularCourseFromBD).getInternalCode().equals(
					((CurricularCourse) curricularCourseToWrite).getInternalCode())) {
			super.lockWrite(curricularCourseToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
		try {
			super.delete(curricularCourse);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public List readAll() throws ExcepcaoPersistencia {

		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add((ICurricularCourse) iterator.next());
			}
			return list;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readCurricularCoursesByCurricularYear(Integer year) throws ExcepcaoPersistencia {
		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where scopes.curricularSemester.curricularYear.year = $1";
			query.create(oqlQuery);
			query.bind(year);
			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add((ICurricularCourse) iterator.next());
			}
			return list;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readCurricularCoursesByCurricularSemester(Integer semester) throws ExcepcaoPersistencia {
		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where scopes.curricularSemester.semester = $1";
			query.create(oqlQuery);
			query.bind(semester);
			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add((ICurricularCourse) iterator.next());
			}
			return list;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	//	public ArrayList readCurricularCoursesByCurricularSemesterAndCurricularYear(Integer semester, Integer year) throws ExcepcaoPersistencia {
	//		try {
	//			ArrayList list = new ArrayList();
	//			String oqlQuery = "select all from " + CurricularCourse.class.getName();
	//			oqlQuery += " where associatedCurricularSemesters.semester = $1";
	//			oqlQuery += " and associatedCurricularSemesters.curricularYear.year = $2";
	//			query.create(oqlQuery);
	//			query.bind(semester);
	//			query.bind(year);
	//			List result = (List) query.execute();
	//
	//			try {
	//				lockRead(result);
	//			} catch (ExcepcaoPersistencia ex) {
	//				throw ex;
	//			}
	//
	//			if ((result != null) && (result.size() != 0)) {
	//				ListIterator iterator = result.listIterator();
	//				while (iterator.hasNext())
	//					list.add((ICurricularCourse) iterator.next());
	//			}
	//			return list;
	//
	//		} catch (QueryException ex) {
	//			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
	//		}
	//	}

	public List readCurricularCoursesByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {

		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where degreeCurricularPlan.name = $1";
			oqlQuery += " and degreeCurricularPlan.degree.nome = $2";
			oqlQuery += " and degreeCurricularPlan.degree.sigla = $3";
			query.create(oqlQuery);

			query.bind(degreeCurricularPlan.getName());
			query.bind(degreeCurricularPlan.getDegree().getNome());
			query.bind(degreeCurricularPlan.getDegree().getSigla());

			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add((ICurricularCourse) iterator.next());
			}
			return list;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readAllCurricularCoursesByBranch(IBranch branch) throws ExcepcaoPersistencia {
		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where scopes.branch.name = $1";
			oqlQuery += " and scopes.branch.code = $2";

			query.create(oqlQuery);

			query.bind(branch.getName());
			query.bind(branch.getCode());

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			ICurricularCourse curricularCourse = null;

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext()) {
					curricularCourse = (ICurricularCourse) iterator.next();
					list.add(curricularCourse);
				}
			}
			return list;

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public List readAllCurricularCoursesBySemester(Integer semester/*, IStudentCurricularPlan studentCurricularPlan*/) throws ExcepcaoPersistencia {
		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where scopes.curricularSemester.semester = $1";
//			oqlQuery += " and curricularCourse.degreeCurricularPlan.name = $2";
//			oqlQuery += " and curricularCourse.degreeCurricularPlan.degree.name = $3";

			query.create(oqlQuery);

			query.bind(semester);
//			query.bind(studentCurricularPlan.getDegreeCurricularPlan().getName());
//			query.bind(studentCurricularPlan.getDegreeCurricularPlan().getDegree().getNome());

			List result = (List) query.execute();
			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			ICurricularCourse curricularCourse = null;

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext()) {
					curricularCourse = (ICurricularCourse) iterator.next();
					list.add(curricularCourse);
				}
			}
			return list;
	

		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
}