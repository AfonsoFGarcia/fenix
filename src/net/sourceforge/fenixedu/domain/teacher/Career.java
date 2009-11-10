/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.LocalDateTime;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.PossiblyNullEndedInterval;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
//
public abstract class Career extends Career_Base {

    public static Comparator<Career> CAREER_DATE_COMPARATOR = new Comparator<Career>() {
	@Override
	public int compare(Career o1, Career o2) {
	    if (!o1.getBeginYear().equals(o2.getBeginYear())) {
		return -(o1.getBeginYear().compareTo(o2.getBeginYear()));
	    } else {
		return o1.getExternalId().compareTo(o2.getExternalId());
	    }
	}
    };

    public Career() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    @Service
    public void delete() {
	removePerson();
	removeTeacher();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public PossiblyNullEndedInterval getInterval() {
	if (getEndYear() != null) {
	    return new PossiblyNullEndedInterval(new LocalDateTime(getBeginYear(), 1, 1, 0, 0, 0).toDateTime().getMillis(),
		    new LocalDateTime(getEndYear(), 1, 1, 0, 0, 0).toDateTime().getMillis());
	} else {
	    return new PossiblyNullEndedInterval(new LocalDateTime(getBeginYear(), 1, 1, 0, 0, 0).toDateTime().getMillis());
	}
    }

    @Override
    public void setBeginYear(Integer beginYear) {
	if (getEndYear() != null && beginYear != null && beginYear > getEndYear()) {
	    throw new DomainException("error.professionalcareer.endYearBeforeStart");
	}
	super.setBeginYear(beginYear);
    }

    @Override
    public void setEndYear(Integer endYear) {
	if (getBeginYear() != null && endYear != null && getBeginYear() > endYear) {
	    throw new DomainException("error.professionalcareer.endYearBeforeStart");
	}
	super.setEndYear(endYear);
    }

    public static List<Career> readAllByTeacherIdAndCareerType(Person person, CareerType careerType) {
	if (careerType == null) {
	    return person.getAssociatedCareers();
	}
	List<Career> allTeacherCareers = new ArrayList<Career>();

	if (careerType.equals(CareerType.PROFESSIONAL)) {
	    readCareersByClass(person, allTeacherCareers, ProfessionalCareer.class.getName());
	} else if (careerType.equals(CareerType.TEACHING)) {
	    readCareersByClass(person, allTeacherCareers, TeachingCareer.class.getName());
	}
	return allTeacherCareers;
    }

    private static void readCareersByClass(Person person, List<Career> allTeacherCareers, String className) {
	for (Career career : person.getAssociatedCareers()) {
	    if (career.getClass().getName().equals(className)) {
		allTeacherCareers.add(career);
	    }
	}
    }
}
