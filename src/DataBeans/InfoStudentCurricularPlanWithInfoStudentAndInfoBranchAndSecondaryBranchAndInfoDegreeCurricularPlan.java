/*
 * Created on Jul 5, 2004
 *
 */
package DataBeans;

import Dominio.IStudentCurricularPlan;

/**
 * @author Jo�o Mota
 *  
 */
public class InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranchAndInfoDegreeCurricularPlan
        extends InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranch {

    public void copyFromDomain(IStudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
        if (studentCurricularPlan != null) {
            setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan.newInfoFromDomain(studentCurricularPlan
                    .getDegreeCurricularPlan()));

        }
    }

    public static InfoStudentCurricularPlan newInfoFromDomain(
            IStudentCurricularPlan studentCurricularPlan) {
        InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranchAndInfoDegreeCurricularPlan infoStudentCurricularPlan = null;
        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = new InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranchAndInfoDegreeCurricularPlan();
            infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
        }
        return infoStudentCurricularPlan;
    }
}