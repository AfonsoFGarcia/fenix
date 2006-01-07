/*
 * Created on 23/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * @author T�nia Pous�o 23/Jun/2004
 */
public class InfoStudentCurricularPlanWithInfoStudentAndInfoBranch extends
        InfoStudentCurricularPlanWithInfoStudentAndDegree {
    public void copyFromDomain(StudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
        if (studentCurricularPlan != null) {

            setInfoBranch(InfoBranch.newInfoFromDomain(studentCurricularPlan.getBranch()));
        }
    }

    public static InfoStudentCurricularPlan newInfoFromDomain(
            StudentCurricularPlan studentCurricularPlan) {
        InfoStudentCurricularPlanWithInfoStudentAndInfoBranch infoStudentCurricularPlan = null;
        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = new InfoStudentCurricularPlanWithInfoStudentAndInfoBranch();
            infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
        }
        return infoStudentCurricularPlan;
    }
}