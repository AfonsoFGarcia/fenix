/*
 * Created on 2004/03/09
 *  
 */

package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.finalDegreeWork.IScheduleing;
import Dominio.finalDegreeWork.Proposal;
import Dominio.finalDegreeWork.Scheduleing;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFinalDegreeWork;

/**
 * @author Luis Cruz
 */

public class FinalDegreeWorkOJB
	extends ObjectFenixOJB
	implements IPersistentFinalDegreeWork {

	public List readFinalDegreeWorkProposalsByExecutionDegree(Integer executionDegreeOID)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionDegree.idInternal", executionDegreeOID);
		return queryList(Proposal.class, criteria);
	}

	public IScheduleing readFinalDegreeWorkScheduleing(Integer executionDegreeOID)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionDegree.idInternal", executionDegreeOID);
		return (IScheduleing) queryObject(Scheduleing.class, criteria);
	}

	public List readFinalDegreeWorkProposalsByTeacher(Integer teacherOID)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("orientator.idInternal", teacherOID);
		Criteria criteriaCorientator = new Criteria();
		criteriaCorientator.addEqualTo("coorientator.idInternal", teacherOID);
		criteria.addOrCriteria(criteriaCorientator);
		return queryList(Proposal.class, criteria);
	}

}
