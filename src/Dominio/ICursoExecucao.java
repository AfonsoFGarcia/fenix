/*
 * ICursoExecucao.java
 * 
 * Created on 2 de Novembro de 2002, 20:50
 */

package Dominio;

import java.io.Serializable;
import java.util.List;

/**
 * @author rpfi
 */
public interface ICursoExecucao extends Serializable, IDomainObject {

	IExecutionYear getExecutionYear();
	IDegreeCurricularPlan getCurricularPlan();
	ITeacher getCoordinator(); //to remove
	List getCoordinatorsList();
	Boolean getTemporaryExamMap();
	ICampus getCampus();

	void setCurricularPlan(IDegreeCurricularPlan curricularPlan);
	void setExecutionYear(IExecutionYear newExecutionYear);
	void setCoordinator(ITeacher coordinator); //to remove
	void setCoordinatorsList(List coordinatorsList);
	void setTemporaryExamMap(Boolean bool);
	void setCampus(ICampus campus);
}
