/*
 * Created on 7/Abr/2003 by jpvl
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;

/**
 * @author jpvl
 */
public interface IPersistentPrecedence {


	/**
	 * @param plan
	 * @return
	 */
	List readByDegreeCurricularPlan(IDegreeCurricularPlan plan) throws ExcepcaoPersistencia;

	/**
	 * @param curricularCourse
	 * @return
	 */
	List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;

}
