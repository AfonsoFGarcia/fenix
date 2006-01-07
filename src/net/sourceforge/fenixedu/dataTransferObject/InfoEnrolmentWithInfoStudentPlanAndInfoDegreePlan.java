/*
 * Created on 18/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Enrolment;

/**
 * @author T�nia Pous�o 18/Jun/2004
 */
public class InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlan extends InfoEnrolment {

    public void copyFromDomain(Enrolment enrolment) {
        super.copyFromDomain(enrolment);
        if (enrolment != null) {
            setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoDegreeCurricularPlan
                    .newInfoFromDomain(enrolment.getStudentCurricularPlan()));
        }
    }

    public static InfoEnrolment newInfoFromDomain(Enrolment enrolment) {
        InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlan infoEnrolment = null;
        if (enrolment != null) {
            infoEnrolment = new InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlan();
            infoEnrolment.copyFromDomain(enrolment);
        }
        return infoEnrolment;
    }
}