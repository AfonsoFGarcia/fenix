package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.odmg.QueryException;

import Dominio.CurricularCourse;
import Dominio.CursoExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.DegreeCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class DegreeCurricularPlanOJB extends ObjectFenixOJB implements IPersistentDegreeCurricularPlan {

	public DegreeCurricularPlanOJB() {
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + DegreeCurricularPlan.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			Iterator iter = result.iterator();
			while (iter.hasNext()) {
				IDegreeCurricularPlan curricularPlan = (IDegreeCurricularPlan) iter.next();
				delete(curricularPlan);

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcepcaoPersistencia();
		}
	}

	public void write(IDegreeCurricularPlan degreeCurricularPlanToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

		IDegreeCurricularPlan degreeCurricularPlanFromDB = null;

		// If there is nothing to write, simply return.
		if (degreeCurricularPlanToWrite == null)
			return;

		// Read degreeCurricularPlan from database.
		degreeCurricularPlanFromDB = this.readByNameAndDegree(degreeCurricularPlanToWrite.getName(), degreeCurricularPlanToWrite.getDegree());

		// If degreeCurricularPlan is not in database, then write it.
		if (degreeCurricularPlanFromDB == null)
			super.lockWrite(degreeCurricularPlanToWrite);
		// else If the degreeCurricularPlan is mapped to the database, then write any existing changes.
		else if (
			(degreeCurricularPlanToWrite instanceof DegreeCurricularPlan)
				&& ((DegreeCurricularPlan) degreeCurricularPlanFromDB).getIdInternal().equals(
					((DegreeCurricularPlan) degreeCurricularPlanToWrite).getIdInternal())) {
			super.lockWrite(degreeCurricularPlanToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public ArrayList readAll() throws ExcepcaoPersistencia {
		try {
			ArrayList listade = new ArrayList();
			String oqlQuery = "select all from " + DegreeCurricularPlan.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			super.lockRead(result);
			if (result.size() != 0) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					listade.add((IDegreeCurricularPlan) iterator.next());
			}
			return listade;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void deleteDegreeCurricularPlan(IDegreeCurricularPlan planoCurricularCurso) throws ExcepcaoPersistencia {
		try {
			// Delete all Execution Degree
			CursoExecucaoOJB executionDegreeOJB = new CursoExecucaoOJB();
			String oqlQuery = "select all from " + CursoExecucao.class.getName();
			oqlQuery += " where curricularPlan.name = $1 ";
			oqlQuery += " and curricularPlan.degree.sigla = $2 ";
			query.create(oqlQuery);
			query.bind(planoCurricularCurso.getName());
			query.bind(planoCurricularCurso.getDegree().getSigla());
			List result = (List) query.execute();
			Iterator iter = result.iterator();
			while (iter.hasNext()) {
				ICursoExecucao executionDegree = (ICursoExecucao) iter.next();
				executionDegreeOJB.delete(executionDegree);
			}

			// Delete All Curricular Courses
			CurricularCourseOJB curricularCourseOJB = new CurricularCourseOJB();
			oqlQuery = "select all from " + CurricularCourse.class.getName();
			oqlQuery += " where degreeCurricularPlan.name = $1 ";
			oqlQuery += " and degreeCurricularPlan.degree.sigla = $2 ";
			query.create(oqlQuery);
			query.bind(planoCurricularCurso.getName());
			query.bind(planoCurricularCurso.getDegree().getSigla());
			result = (List) query.execute();
			iter = result.iterator();
			while (iter.hasNext()) {
				ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
				curricularCourseOJB.delete(curricularCourse);
			}

			super.delete(planoCurricularCurso);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcepcaoPersistencia();
		}
	}

	public IDegreeCurricularPlan readByNameAndDegree(String name, ICurso degree) throws ExcepcaoPersistencia {
		try {
			IDegreeCurricularPlan de = null;

			String oqlQuery = "select all from " + DegreeCurricularPlan.class.getName();
			oqlQuery += " where name = $1 " + " and degree.sigla = $2 ";

			query.create(oqlQuery);
			query.bind(name);
			query.bind(degree.getSigla());

			List result = (List) query.execute();
			super.lockRead(result);
			if (result.size() != 0)
				de = (IDegreeCurricularPlan) result.get(0);
			return de;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}

	}
}
