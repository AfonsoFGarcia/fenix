/*
 * ICursoExecucaoPersistente.java
 *
 * Created on 2 de Novembro de 2002, 21:14
 */

package ServidorPersistente;

/**
 *
 * @author  rpfi
 */
import java.util.List;

import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IDegreeCurricularPlan;

public interface ICursoExecucaoPersistente extends IPersistentObject {
	/**
	 * 
	 * @param cursoExecucao
	 * @throws ExcepcaoPersistencia
	 */
	public void lockWrite(ICursoExecucao cursoExecucao) throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @param cursoExecucao
	 * @throws ExcepcaoPersistencia
	 */
	public void delete(ICursoExecucao cursoExecucao) throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @throws ExcepcaoPersistencia
	 */
	public void deleteAll() throws ExcepcaoPersistencia;
	
	/**
	 * Method readByExecutionYear.
	 * @param executionYear
	 * @return List
	 */
	public List readByExecutionYear(IExecutionYear executionYear)
		throws ExcepcaoPersistencia;

	/**
	* 
	* @param degree
	* @param executionYear
	* @return ICursoExecucao
	*/
	public ICursoExecucao readByDegreeCurricularPlanAndExecutionYear(
		IDegreeCurricularPlan degreeCurricularPlan,
		IExecutionYear executionYear)
		throws ExcepcaoPersistencia;

	/**
	 * 
	 * @param degreeInitials
	 * @param nameDegreeCurricularPlan
	 * @param executionYear
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public ICursoExecucao readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
		String degreeInitials,
		String nameDegreeCurricularPlan,
		IExecutionYear executionYear)
		throws ExcepcaoPersistencia;		
	
	/**
	 * 
	 * @param executionPeriod
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readMasterDegrees(String executionYear) throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @param degreeName
	 * @param executionYear
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public ICursoExecucao readByDegreeNameAndExecutionYear(String degreeName, IExecutionYear executionYear) throws ExcepcaoPersistencia;
}
