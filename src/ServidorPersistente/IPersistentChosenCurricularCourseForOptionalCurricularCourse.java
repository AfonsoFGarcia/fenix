/*
 * Created on 7/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IDegreeCurricularPlan;


/**
 * @author dcs-rjao
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IPersistentChosenCurricularCourseForOptionalCurricularCourse extends IPersistentObject {
	public List readAllByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;

}