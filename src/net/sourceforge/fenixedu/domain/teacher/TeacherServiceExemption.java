/*
 * Created on Nov 8, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class TeacherServiceExemption extends TeacherServiceExemption_Base {

    public TeacherServiceExemption(Teacher teacher, Date beginDate, Date endDate,
	    ServiceExemptionType type, String institution) {
	super();
	checkParameters(teacher, beginDate, endDate, type);
	setRootDomainObject(RootDomainObject.getInstance());
	setEnd(endDate);
	setStart(beginDate);
	setTeacher(teacher);
	setType(type);
	setInstitution(institution);
    }

    private void checkParameters(Teacher teacher, Date beginDate, Date endDate, ServiceExemptionType type) {
	if (teacher == null) {
	    throw new DomainException("error.serviceExemption.no.teacher");
	}
	if (type == null) {
	    throw new DomainException("error.serviceExemption.no.type");
	}
	if (beginDate == null) {
	    throw new DomainException("error.serviceExemption.no.beginDate");
	}
	if (endDate != null && endDate.before(beginDate)) {
	    throw new DomainException("error.serviceExemption.endDateBeforeBeginDate");
	}
	// checkTeacherServiceExemptionsDatesIntersection(teacher, beginDate,
        // endDate, type);
    }

    public void delete() {
	removeTeacher();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    // private void checkTeacherServiceExemptionsDatesIntersection(Teacher
        // teacher, Date begin, Date end, ServiceExemptionType type) {
    // for (TeacherServiceExemption serviceExemption :
        // teacher.getServiceExemptionSituations()) {
    // if (serviceExemption.getType().equals(type) &&
        // serviceExemption.checkDatesIntersections(begin, end)) {
    // System.out.println("Teacher Number: " + teacher.getTeacherNumber());
    // throw new
        // DomainException("error.teacherLegalRegimen.dates.intersection");
    // }
    // }
    // }
    //
    // private boolean checkDatesIntersections(Date begin, Date end) {
    // return ((end == null || this.getStart().before(end))
    // && (this.getEnd() == null || this.getEnd().after(begin)));
    // }

    public boolean belongsToPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
	return ((endDate == null || !this.getStartYearMonthDay().isAfter(endDate)) && (this
		.getEndYearMonthDay() == null || !this.getEndYearMonthDay().isBefore(beginDate)));
    }

    public boolean isMedicalSituation() {
	return (this.getType().equals(ServiceExemptionType.MEDICAL_SITUATION)
		|| this.getType().equals(ServiceExemptionType.MATERNAL_LICENSE_WITH_SALARY_80PERCENT)
		|| this.getType().equals(ServiceExemptionType.MATERNAL_LICENSE)
		|| this.getType().equals(ServiceExemptionType.DANGER_MATERNAL_LICENSE) || this.getType()
		.equals(ServiceExemptionType.CHILDBIRTH_LICENSE));
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
		|| this.getType().equals(ServiceExemptionType.SPECIAL_LICENSE)
		|| this.getType().equals(ServiceExemptionType.DETACHED_TO)
		|| this.getType().equals(ServiceExemptionType.INCAPACITY_FOR_TOGETHER_DOCTOR_OF_THE_CGA)
		|| this.getType().equals(ServiceExemptionType.FUNCTIONS_MANAGEMENT_SERVICE_EXEMPTION) || this
		.getType().equals(ServiceExemptionType.PUBLIC_MANAGER));
    }

    public boolean isServiceExemptionToCountInCredits() {
	return (this.getType().equals(ServiceExemptionType.SABBATICAL)
		|| this.getType().equals(ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITHOUT_SALARY)
		|| this.getType().equals(ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY)
		|| this.getType().equals(
			ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY_SABBATICAL)
		|| this.getType().equals(
			ServiceExemptionType.GRANT_OWNER_EQUIVALENCE_WITH_SALARY_WITH_DEBITS)
		|| this.getType().equals(ServiceExemptionType.TEACHER_SERVICE_EXEMPTION_E_C_D_U) || this
		.getType().equals(
			ServiceExemptionType.TEACHER_SERVICE_EXEMPTION_DL24_84_ART51_N6_EST_DISC));
    }
}
