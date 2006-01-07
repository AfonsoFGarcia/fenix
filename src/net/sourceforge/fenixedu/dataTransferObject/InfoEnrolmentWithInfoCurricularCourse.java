package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Enrolment;

/**
 * @author Fernanda Quit�rio Created on 12/Jul/2004
 */
public class InfoEnrolmentWithInfoCurricularCourse extends InfoEnrolment {

    public void copyFromDomain(Enrolment enrolment) {
        super.copyFromDomain(enrolment);
        if (enrolment != null) {
            setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(enrolment
                    .getCurricularCourse()));
        }
    }

    public static InfoEnrolment newInfoFromDomain(Enrolment enrolment) {
        InfoEnrolmentWithInfoCurricularCourse infoEnrolment = null;
        if (enrolment != null) {
            infoEnrolment = new InfoEnrolmentWithInfoCurricularCourse();
            infoEnrolment.copyFromDomain(enrolment);
        }
        return infoEnrolment;
    }
}