/*
 * Created on 23/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.EvaluationMethod;
import Dominio.IExecutionCourse;
import Dominio.IEvaluationMethod;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Jo�o Mota
 *
 * 
 */
public class EvaluationMethodOJB extends ObjectFenixOJB implements IPersistentEvaluationMethod {

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentEvaluationMethod#readByExecutionCourse(Dominio.IDisciplinaExecucao)
	 */
	public IEvaluationMethod readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
		try {
			IEvaluationMethod evaluation = null;
			String oqlQuery = "select all from " + EvaluationMethod.class.getName();
			oqlQuery += " where executionCourse.sigla = $1 ";
			oqlQuery += " and executionCourse.executionPeriod.name = $2 ";
			oqlQuery += " and executionCourse.executionPeriod.executionYear.year = $3 ";
			query.create(oqlQuery);
			query.bind(executionCourse.getSigla());
			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
			List result = (List) query.execute();

			lockRead(result);
			if (result.size() != 0) {
				evaluation = (IEvaluationMethod) result.get(0);
			}
			return evaluation;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public IEvaluationMethod readByIdExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExecutionCourse", new Integer(String.valueOf(executionCourse.getIdInternal())));
		return (IEvaluationMethod) queryObject(EvaluationMethod.class, criteria);
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentEvaluationMethod#readAll()
	 */
	public List readAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + EvaluationMethod.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();
			lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentEvaluationMethod#delete(Dominio.IEvaluationMethod)
	 */
	public void delete(IEvaluationMethod evaluation) throws ExcepcaoPersistencia {
		super.delete(evaluation);
		//TODO: add to the execution Course delete methods to delete all evaluations, all curriculums and all sites
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentEvaluationMethod#deleteAll()
	 */
	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + EvaluationMethod.class.getName();
		super.deleteAll(oqlQuery);

	}

	public void lockWrite(IEvaluationMethod evaluation) throws ExcepcaoPersistencia {
		IEvaluationMethod evaluationFromDB = null;

		// If there is nothing to write, simply return.
		if (evaluation == null) {
			return;
		}

		// Read evaluation method from database.
		evaluationFromDB = this.readByIdExecutionCourse(evaluation.getExecutionCourse());

		// If evaluation methodp is not in database, then write it.
		if (evaluationFromDB == null) {
			super.lockWrite(evaluation);
		} else {
			
			if (evaluation.getIdInternal() == null) {
				evaluation.setIdInternal(evaluationFromDB.getIdInternal());				
			}

			if (evaluationFromDB.getIdInternal().equals(evaluation.getIdInternal())) {
				// else If the evaluation method is mapped to the database, then write any existing changes.
				super.lockWrite(evaluation);
				// else Throw an already existing exception
			} else {
				throw new ExistingPersistentException();
			}
		}
	}
}
