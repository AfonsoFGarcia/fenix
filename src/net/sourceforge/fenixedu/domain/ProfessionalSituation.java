package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.util.RegimeType;

import org.joda.time.YearMonthDay;

public abstract class ProfessionalSituation extends ProfessionalSituation_Base {

    protected ProfessionalSituation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void init(YearMonthDay beginDate, YearMonthDay endDate, ProfessionalSituationType type, RegimeType regimenType,
	    Employee employee, Category category) {

	setBeginDateYearMonthDay(beginDate);
	setEndDateYearMonthDay(endDate);
	setSituationType(type);
	setRegimeType(regimenType);
	setEmployee(employee);
	setCategory(category);
    }

    public boolean belongsToPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
	return ((endDate == null || !getBeginDateYearMonthDay().isAfter(endDate)) && (getEndDateYearMonthDay() == null || !getEndDateYearMonthDay()
		.isBefore(beginDate)));
    }

    public boolean isActive(YearMonthDay currentDate) {
	return belongsToPeriod(currentDate, currentDate);
    }

    @Override
    public void setCategory(Category category) {
	if (category == null && !isTeacherServiceExemption()) {
	    throw new DomainException("error.ProfessionalSituation.no.category");
	}
	super.setCategory(category);
    }

    @Override
    public void setRegimeType(RegimeType regimeType) {
	if (regimeType == null && !isTeacherServiceExemption()) {
	    throw new DomainException("error.ProfessionalSituation.no.regimeType");
	}
	super.setRegimeType(regimeType);
    }

    @Override
    public void setSituationType(ProfessionalSituationType situationType) {
	if (situationType == null) {
	    throw new DomainException("error.ProfessionalSituation.empty.situationType");
	}
	super.setSituationType(situationType);
    }

    @Override
    public void setEmployee(Employee employee) {
	if (employee == null) {
	    throw new DomainException("error.ProfessionalSituation.empty.employee");
	}
	super.setEmployee(employee);
    }

    public void delete() {
	super.setEmployee(null);
	super.setCategory(null);
	removeProfessionalCategory();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public boolean isTeacherProfessionalSituation() {
	return false;
    }

    public boolean isTeacherServiceExemption() {
	return false;
    }

    public boolean isEmployeeProfessionalSituation() {
	return false;
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
	final YearMonthDay start = getBeginDateYearMonthDay();
	final YearMonthDay end = getEndDateYearMonthDay();
	return start != null && (end == null || !start.isAfter(end));
    }
}
