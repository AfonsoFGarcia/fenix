package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Coordinator;
import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCoordinator;

/**
 *fenix-head
 *ServidorPersistente.OJB
 * @author Jo�o Mota
 *28/Out/2003
 *
 */

public class CoordinatorOJB
	extends ObjectFenixOJB
	implements IPersistentCoordinator {

	public List readExecutionDegreesByTeacher(ITeacher teacher)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
		List coordinators = queryList(Coordinator.class, criteria);
		if (coordinators == null) {
			return null;
		}
		Iterator iter = coordinators.iterator();
		List executionDegrees = new ArrayList();
		while (iter.hasNext()) {
			ICoordinator coordinator = (ICoordinator) iter.next();
			ICursoExecucao executionDegree = coordinator.getExecutionDegree();
			if (!executionDegrees.contains(executionDegree)) {
				executionDegrees.add(executionDegree);
			}
		}
		return executionDegrees;
	}

	public List readCoordinatorsByExecutionDegree(ICursoExecucao executionDegree)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"executionDegree.idInternal",
			executionDegree.getIdInternal());
		return queryList(Coordinator.class, criteria);

	}

	public ICoordinator readCoordinatorByTeacherAndExecutionDegreeId(
		ITeacher teacher,
		Integer executionDegreeId)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionDegree.idInternal", executionDegreeId);
		criteria.addEqualTo("teacher.idInternal",teacher.getIdInternal());
		return (ICoordinator) queryObject(Coordinator.class,criteria);
	}
}
