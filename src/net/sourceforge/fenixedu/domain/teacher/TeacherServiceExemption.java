/*
 * Created on Nov 8, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class TeacherServiceExemption extends TeacherServiceExemption_Base {

    public TeacherServiceExemption() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        this.removeTeacher();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    public boolean belongsToPeriod(Date beginDate, Date endDate) {
        return (!this.getStart().after(endDate)
                && (this.getEnd() == null || !this.getEnd().before(beginDate)));
    }

    public boolean isMedicalSituation() {
       return (this.getType().equals(ServiceExemptionType.MEDICAL_SITUATION)
                || this.getType().equals(ServiceExemptionType.MATERNAL_LICENSE_WITH_SALARY_80PERCENT)
                || this.getType().equals(ServiceExemptionType.MATERNAL_LICENSE)
                || this.getType().equals(ServiceExemptionType.DANGER_MATERNAL_LICENSE)
                || this.getType().equals(ServiceExemptionType.CHILDBIRTH_LICENSE));            
    }

    public boolean isServiceExemptionToCountZeroInCredits() {
        return (this.getType().equals(ServiceExemptionType.CONTRACT_SUSPEND_ART_73_ECDU)
                || this.getType().equals(ServiceExemptionType.CONTRACT_SUSPEND)
                || this.getType().equals(ServiceExemptionType.GOVERNMENT_MEMBER)
                || this.getType().equals(ServiceExemptionType.LICENSE_WITHOUT_SALARY_FOR_ACCOMPANIMENT)
                || this.getType().equals(
                        ServiceExemptionType.LICENSE_WITHOUT_SALARY_FOR_INTERNATIONAL_EXERCISE)
                || this.getType().equals(ServiceExemptionType.LICENSE_WITHOUT_SALARY_LONG)
                || this.getType().equals(ServiceExemptionType.LICENSE_WITHOUT_SALARY_UNTIL_NINETY_DAYS)
                || this.getType().equals(ServiceExemptionType.LICENSE_WITHOUT_SALARY_YEAR)
                || this.getType().equals(ServiceExemptionType.MILITAR_SITUATION)
                || this.getType().equals(ServiceExemptionType.REQUESTED_FOR)
                || this.getType().equals(ServiceExemptionType.SERVICE_COMMISSION)
                || this.getType().equals(ServiceExemptionType.SERVICE_COMMISSION_IST_OUT)
                || this.getType().equals(ServiceExemptionType.SPECIAL_LICENSE));           
    }

    public boolean isServiceExemptionToCountInCredits() {
        return (this.getType().equals(ServiceExemptionType.SABBATICAL)
                || this.getType().equals(
                        ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITHOUT_SALARY)
                || this.getType().equals(
                        ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY)
                || this.getType().equals(
                        ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY_SABBATICAL)
                || this.getType().equals(
                        ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY_WITH_DEBITS)
                || this.getType().equals(
                        ServiceExemptionType.TEACHER_SERVICE_EXEMPTION_E_C_D_U));            
    }
}
