package ServidorAplicacao.strategy.degreeCurricularPlan;

import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.*;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt) <br>
 *
 * Factory for Degree Curricular Plan related operations
 * 
 */

public interface IDegreeCurricularPlanStrategyFactory {
	
	/**
	 * 
	 * @param The Degree Curricular Plan
	 * @return The Degree Curricular Plan Strategy
	 */
	public IDegreeCurricularPlanStrategy getDegreeCurricularPlanStrategy(IDegreeCurricularPlan degreeCurricularPlan);
}