package net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.delegates;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.student.Student;

public class DelegateBean implements Serializable {
    private DegreeType degreeType;

    private ExecutionYear executionYear;

    private Degree degree;

    private CurricularYear curricularYear;

    private Student delegate;

    private Person ggaeDelegate;

    private FunctionType delegateType;

    private Integer studentNumber;

    private String personUsername;

    private Function ggaeDelegateFunction;

    private DelegateElection delegateElection;

    public DelegateBean() {
	setDegreeType(null);
	setDegree(null);
	setCurricularYear(null);
	setDelegate(null);
	setGgaeDelegate(null);
	setGgaeDelegateFunction(null);
    }

    public CurricularYear getCurricularYear() {
	return (curricularYear);
    }

    public void setCurricularYear(CurricularYear curricularYear) {
	this.curricularYear = curricularYear;
    }

    public Degree getDegree() {
	return (degree);
    }

    public void setDegree(Degree degree) {
	this.degree = degree;
    }

    public DegreeType getDegreeType() {
	return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
	this.degreeType = degreeType;
    }

    public ExecutionYear getExecutionYear() {
	return (executionYear);
    }

    public void setDelegateElection(DelegateElection election) {
	this.delegateElection = election;
    }

    public DelegateElection getDelegateElection() {
	return (delegateElection);
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = executionYear;
    }

    public Student getDelegate() {
	return (delegate);
    }

    public void setDelegate(Student delegate) {
	this.delegate = delegate;
    }

    public Person getGgaeDelegate() {
	return (ggaeDelegate);
    }

    public void setGgaeDelegate(Person ggaeDelegate) {
	this.ggaeDelegate = ggaeDelegate;
    }

    public Integer getStudentNumber() {
	return ((getDelegate() == null) || (getDelegate().getLastActiveRegistration() == null)) ? studentNumber : getDelegate()
		.getLastActiveRegistration().getNumber();
    }

    public void setStudentNumber(Integer studentNumber) {
	this.studentNumber = studentNumber;
    }

    public String getPersonUsername() {
	return (getGgaeDelegate() == null ? personUsername : getGgaeDelegate().getUsername());
    }

    public void setPersonUsername(String personUsername) {
	this.personUsername = personUsername;
    }

    public String getPersonName() {
	return (getGgaeDelegate() != null ? getGgaeDelegate().getName() : null);
    }

    public String getStudentName() {
	return (getDelegate() != null ? getDelegate().getPerson().getName() : null);
    }

    public String getStudentEmail() {
	return (getDelegate() != null ? getDelegate().getPerson().getEmail() : null);
    }

    public FunctionType getDelegateType() {
	return delegateType;
    }

    public void setDelegateType(FunctionType delegateType) {
	this.delegateType = delegateType;
    }

    public Function getGgaeDelegateFunction() {
	return (ggaeDelegateFunction);
    }

    public void setGgaeDelegateFunction(Function delegateFunction) {
	this.ggaeDelegateFunction = delegateFunction;
    }

    /*
     * THE FOLLOWING METHODS ARE PRESENTATION LOGIC
     */
    public boolean getHasDelegate() {
	return hasDelegate();
    }

    public boolean getHasGgaeDelegate() {
	return hasGgaeDelegate();
    }

    public boolean isEmptyDelegateBean() {
	return (getDelegate() == null && !isYearDelegateTypeBean() ? true : false);
    }

    public boolean isEmptyYearDelegateBean() {
	return (getDelegate() == null && isYearDelegateTypeBean() ? true : false);
    }

    public boolean isEmptyYearDelegateBeanWithElection() {
	return (hasDelegateElection() && getDelegateElection().hasLastVotingPeriod() && !hasYearDelegate() ? true : false);
    }

    public boolean isYearDelegateBeanWithElectedElection() {
	return (hasDelegateElection() && hasYearDelegate() && getDelegateElection().hasElectedStudent()
		&& getDelegateElection().getElectedStudent().equals(getDelegate()) ? true : false);
    }

    public boolean hasDelegate() {
	return (getDelegate() != null ? true : false);
    }

    public boolean hasGgaeDelegate() {
	return (getGgaeDelegate() != null ? true : false);
    }

    public boolean isYearDelegateTypeBean() {
	return (getDelegateType().equals(FunctionType.DELEGATE_OF_YEAR) ? true : false);
    }

    public boolean hasYearDelegate() {
	return (isYearDelegateTypeBean() && hasDelegate() ? true : false);
    }

    public boolean hasDelegateElection() {
	return (getDelegateElection() != null ? true : false);
    }
}