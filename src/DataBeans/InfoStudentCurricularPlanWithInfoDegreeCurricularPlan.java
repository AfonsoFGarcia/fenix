/*
 * Created on 18/Jun/2004
 *
 */
package DataBeans;

import Dominio.IStudentCurricularPlan;

/**
 * @author T�nia Pous�o
 * 18/Jun/2004
 */
public class InfoStudentCurricularPlanWithInfoDegreeCurricularPlan
		extends
			InfoStudentCurricularPlan {
	
	public void copyFromDomain(IStudentCurricularPlan studentCurricularPlan) {
		super.copyFromDomain(studentCurricularPlan);
		if(studentCurricularPlan != null) {
			setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan.newInfoFromDomain(studentCurricularPlan.getDegreeCurricularPlan()));
		}
	}
	
	public static InfoStudentCurricularPlan newInfoFromDomain(IStudentCurricularPlan studentCurricularPlan) {
		InfoStudentCurricularPlanWithInfoDegreeCurricularPlan infoStudentCurricularPlan = null;
		if(studentCurricularPlan != null) {
			infoStudentCurricularPlan = new InfoStudentCurricularPlanWithInfoDegreeCurricularPlan();
			infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
		}
		return infoStudentCurricularPlan;
	}
}
