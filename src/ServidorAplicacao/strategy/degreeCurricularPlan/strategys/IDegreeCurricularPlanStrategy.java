package ServidorAplicacao.strategy.degreeCurricularPlan.strategys;

import Dominio.IDegreeCurricularPlan;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public interface IDegreeCurricularPlanStrategy {

	/**
	 * Gets the Degree Curricular Plan 
	 * @return the DegreeCurricular Plan
	 */
	public IDegreeCurricularPlan getDegreeCurricularPlan();
	
	/**
	 * Sets the Degree Curricular Plan
	 * @param degreeCurricularPlan
	 */
	public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan);


	/**
	 * Checks if the mark is Valid for this Degree Curricular Plan
	 * @param The String with the mark to test
	 */
	public boolean checkMark(String mark);
	
	
	/**
	 * Calculate's the Student's average 
	 * @param The student's Curricular Plan
	 * @param The list of the students enrolment
	 * @return The Student's Average
	 */
	public Double calculateStudentAverage(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;

}