package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class ExternalEnrolment extends ExternalEnrolment_Base implements IEnrolment {
    
    static public final Comparator<ExternalEnrolment> COMPARATOR_BY_NAME = new Comparator<ExternalEnrolment>() {
	public int compare(ExternalEnrolment o1, ExternalEnrolment o2) {
	    int result = o1.getName().compareTo(o2.getName());
	    return (result != 0) ? result : o1.getIdInternal().compareTo(o2.getIdInternal());
	}
    };
    
    protected ExternalEnrolment() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCreationDateDateTime(new DateTime());
	if(AccessControl.getPerson() != null){
	    setCreatedBy(AccessControl.getPerson().getUsername());
	}
	setGrade(Grade.createEmptyGrade());
    }
    
    public ExternalEnrolment(final Student student, final ExternalCurricularCourse externalCurricularCourse, final String grade, final ExecutionPeriod executionPeriod, final YearMonthDay evaluationDate) {
        this();
        if(student == null) {
            throw new DomainException("error.externalEnrolment.student.cannot.be.null");
        }
        if(externalCurricularCourse == null) {
            throw new DomainException("error.externalEnrolment.externalCurricularCourse.cannot.be.null");
        }
        if (StringUtils.isEmpty(grade)) {
            throw new DomainException("error.externalEnrolment.invalid.grade");
        }
        checkIfCanCreateExternalEnrolment(student, externalCurricularCourse);
        setStudent(student);
        setExternalCurricularCourse(externalCurricularCourse);
        setGradeValue(grade);
        setExecutionPeriod(executionPeriod);
        setEvaluationDate(evaluationDate);
    }

    private void checkIfCanCreateExternalEnrolment(final Student student, final ExternalCurricularCourse externalCurricularCourse) {
	for (final ExternalEnrolment externalEnrolment : student.getExternalEnrolmentsSet()) {
	    if (externalEnrolment.getExternalCurricularCourse() == externalCurricularCourse) {
		throw new DomainException("error.studentCurriculum.ExternalEnrolment.already.exists.externalEnrolment.for.externalCurricularCourse", externalCurricularCourse.getName());
	    }
	}
    }
    
    public void edit(final Student student, final String gradeValue, final ExecutionPeriod executionPeriod, final YearMonthDay evaluationDate) {
	
	if (student != getStudent()) {
	    checkIfCanCreateExternalEnrolment(student, getExternalCurricularCourse());
	}
	
	setStudent(student);
        setGradeValue(gradeValue);
        setExecutionPeriod(executionPeriod);
        setEvaluationDate(evaluationDate);
    }

    public MultiLanguageString getName() {
	MultiLanguageString multiLanguageString = new MultiLanguageString();
	multiLanguageString.setContent(getExternalCurricularCourse().getName());
	return multiLanguageString;
    }
    
    public String getCode() {
        return getExternalCurricularCourse().getCode();
    }
    
    public String getFullPathName() {
	return getExternalCurricularCourse().getFullPathName();
    }
    
    public String getDescription() {
        return getFullPathName();
    }
    
    public void delete() {
	removeExecutionPeriod();
	removeExternalCurricularCourse();
	removeStudent();
	removeRootDomainObject();
	getNotNeedToEnrollCurricularCourses().clear();
	super.deleteDomainObject();
    }

    final public boolean isExternalEnrolment() {
	return true;
    }

    public Double getEctsCredits() {
	return Double.valueOf(0d);
    }

    public Integer getFinalGrade() {
	final String grade = getGradeValue();
	return (StringUtils.isEmpty(grade) || !StringUtils.isNumeric(grade)) ? null : Integer.valueOf(grade);
    }

    public Double getWeigth() {
	return getEctsCredits();
    }

    final public ExecutionYear getExecutionYear() {
	return getExecutionPeriod().getExecutionYear();
    }

    public Unit getAcademicUnit() {
	return getExternalCurricularCourse().getAcademicUnit();
    }

}
