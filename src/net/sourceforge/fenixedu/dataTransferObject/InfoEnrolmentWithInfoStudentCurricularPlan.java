/*
 * Created on 18/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IEnrollment;

/**
 * @author T�nia Pous�o 18/Jun/2004
 */
public class InfoEnrolmentWithInfoStudentCurricularPlan extends InfoEnrolment {

    public void copyFromDomain(IEnrollment enrolment) {
        super.copyFromDomain(enrolment);
        if (enrolment != null) {
            setInfoStudentCurricularPlan(InfoStudentCurricularPlan.newInfoFromDomain(enrolment
                    .getStudentCurricularPlan()));
        }
    }

    public static InfoEnrolment newInfoFromDomain(IEnrollment enrolment) {
        InfoEnrolmentWithInfoStudentCurricularPlan infoEnrolment = null;
        if (enrolment != null) {
            infoEnrolment = new InfoEnrolmentWithInfoStudentCurricularPlan();
            infoEnrolment.copyFromDomain(enrolment);
        }
        return infoEnrolment;
    }
}