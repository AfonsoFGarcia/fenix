/*
 * Created on 6/Jan/2004
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.GratuitySituation;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IGratuitySituation;
import Dominio.IGratuityValues;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuitySituation;
import Util.GratuitySituationType;
import Util.Specialization;
import Util.StudentCurricularPlanState;

/**
 * @author T�nia Pous�o
 *
 */
public class GratuitySituationOJB extends ObjectFenixOJB implements IPersistentGratuitySituation
{

	public IGratuitySituation readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(IStudentCurricularPlan studentCurricularPlan, IGratuityValues gratuityValues)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());
		criteria.addEqualTo("gratuityValues.idInternal", gratuityValues.getIdInternal());
		
		return (IGratuitySituation) queryObject(GratuitySituation.class, criteria);
	}
	
	public List readGratuitySituationsByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"studentCurricularPlan.degreeCurricularPlan.idInternal",
			degreeCurricularPlan.getIdInternal());
		
		return queryList(GratuitySituation.class, criteria);
	}

	public List readGratuitySituationListByExecutionDegreeAndSpecialization(
		ICursoExecucao executionDegree,
		Specialization specialization)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		if (executionDegree != null)
		{
			if (executionDegree.getIdInternal() != null)
			{
				criteria.addEqualTo(
						"gratuityValues.executionDegree.idInternal",
						executionDegree.getIdInternal());
			}
			else if (
					executionDegree.getExecutionYear() != null
					&& executionDegree.getExecutionYear().getYear() != null)
			{
				criteria.addEqualTo(
						"gratuityValues.executionDegree.executionYear.year",
						executionDegree.getExecutionYear().getYear());
			}
		}

		if (specialization != null && specialization.getSpecialization() != null)
		{
			criteria.addEqualTo(
					"studentCurricularPlan.specialization",
					specialization.getSpecialization());
		}
		else //all specialization required, but not records with  specialization null
		{
			criteria.addNotNull("studentCurricularPlan.specialization");
		}
		
		//student curricular plan actives or with school part conclued
		criteria.addEqualTo(
				"studentCurricularPlan.currentState",
				StudentCurricularPlanState.ACTIVE_OBJ);
		
		Criteria criteriaState = new Criteria();
		criteriaState.addEqualTo(
				"studentCurricularPlan.currentState",
				StudentCurricularPlanState.SCHOOLPARTCONCLUDED_OBJ);
		criteria.addOrCriteria(criteriaState);
		
		return queryList(GratuitySituation.class, criteria);
	}

	public List readGratuitySituationListByExecutionDegreeAndSpecializationAndSituation(
		ICursoExecucao executionDegree,
		Specialization specialization,
		GratuitySituationType situation)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		if (executionDegree != null)
		{
			if (executionDegree.getIdInternal() != null)
			{
				criteria.addEqualTo(
					"gratuityValues.executionDegree.idInternal",
					executionDegree.getIdInternal());
			}
			else if (
				executionDegree.getExecutionYear() != null
					&& executionDegree.getExecutionYear().getYear() != null)
			{
				criteria.addEqualTo(
					"gratuityValues.executionDegree.executionYear.year",
					executionDegree.getExecutionYear().getYear());
			}
		}

		if (specialization != null && specialization.getSpecialization() != null)
		{
			criteria.addEqualTo(
				"studentCurricularPlan.specialization",
				specialization.getSpecialization());
		}
		else //all specialization required, but not records with  specialization null
		{
			criteria.addNotNull("studentCurricularPlan.specialization");
		}

 		if (situation != null)
		{
			switch (situation.getValue())
			{
				case GratuitySituationType.CREDITOR_TYPE :
					//CREDITOR situation: remainingValue < 0
					criteria.addLessThan("remainingValue", new Double(0));					
					break;
				case GratuitySituationType.DEBTOR_TYPE :
					//DEBTOR situation: remainingValue > 0
					criteria.addGreaterThan("remainingValue", new Double(0));
					break;
				case GratuitySituationType.REGULARIZED_TYPE :
					//REGULARIZED situation: remainingValue == 0 			
					criteria.addEqualTo("remainingValue", new Double(0));
					break;
				default :
					break;
			}
		}
		
		return queryList(GratuitySituation.class, criteria);
	}
}
