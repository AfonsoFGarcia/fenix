package net.sourceforge.fenixedu.dataTransferObject.alumni;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.BusinessArea;
import net.sourceforge.fenixedu.domain.ContractType;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.JobApplicationType;
import net.sourceforge.fenixedu.domain.SalaryType;

import org.joda.time.LocalDate;

public class AlumniJobBean implements Serializable {

    private Alumni alumni;
    private String employerName;
    private String city;
    private Country country;
    private BusinessArea parentBusinessArea;
    private BusinessArea childBusinessArea;
    private String position;
    private Date beginDate;
    private Date endDate;
    private Integer jobId;
    private String schema;
    private JobApplicationType applicationType;
    private ContractType contractType;
    private SalaryType salaryType;

    private AlumniJobBean(Alumni alumni, String schema) {
	setAlumni(alumni);
	setSchema(schema);
    }

    public AlumniJobBean(Alumni alumni) {
	this(alumni, "alumni.public.access.jobContact");
    }

    public AlumniJobBean(Alumni alumni, Job job) {
	this(alumni, "alumni.public.access.jobContact.full");
	setEmployerName(job.getEmployerName());
	setCity(job.getCity());
	setCountry(job.getCountry());
	setParentBusinessArea(job.getBusinessArea().getParentArea());
	setChildBusinessArea(job.getBusinessArea());
	setPosition(job.getPosition());
	setBeginDateAsDate(job.getBeginDate());
	setEndDateAsDate(job.getEndDate());
	setApplicationType(job.getJobApplicationType());
	setContractType(job.getContractType());
	setSalaryType(job.getSalaryType());
	setJobId(job.getIdInternal());
    }

    public void setAlumni(Alumni alumni) {
	this.alumni = alumni;
    }

    public Alumni getAlumni() {
	return this.alumni;
    }

    public void setCountry(Country country) {
	this.country = country;
    }

    public Country getCountry() {
	return this.country;
    }

    public String getEmployerName() {
	return employerName;
    }

    public void setEmployerName(String employerName) {
	this.employerName = employerName;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getPosition() {
	return position;
    }

    public void setPosition(String jobPosition) {
	this.position = jobPosition;
    }

    public void setParentBusinessArea(BusinessArea businessArea) {
	this.parentBusinessArea = businessArea;
    }

    public BusinessArea getParentBusinessArea() {
	return this.parentBusinessArea;
    }

    public void setChildBusinessArea(BusinessArea businessArea) {
	this.childBusinessArea = businessArea;
    }

    public BusinessArea getChildBusinessArea() {
	return this.childBusinessArea;
    }

    public Date getBeginDate() {
	return beginDate;
    }

    public void setBeginDate(Date beginDate) {
	this.beginDate = beginDate;
    }

    public Date getEndDate() {
	return endDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    public Integer getJobId() {
	return jobId;
    }

    public void setJobId(Integer jobId) {
	this.jobId = jobId;
    }

    public String getSchema() {
	return schema;
    }

    public void setSchema(String schema) {
	this.schema = schema;
    }

    public void updateSchema() {
	if (getParentBusinessArea() == null) {
	    setSchema("alumni.public.access.jobContact");
	} else {
	    setSchema("alumni.public.access.jobContact.full");
	}
    }

    private void setBeginDateAsDate(LocalDate beginDate) {
	if (beginDate != null) {
	    final Calendar date = Calendar.getInstance();
	    date.set(Calendar.YEAR, beginDate.getYear());
	    date.set(Calendar.MONTH, beginDate.getMonthOfYear() - 1);
	    date.set(Calendar.DAY_OF_MONTH, beginDate.getDayOfMonth());
	    setBeginDate(date.getTime());
	}
    }

    private void setEndDateAsDate(LocalDate endDate) {
	if (endDate != null) {
	    final Calendar date = Calendar.getInstance();
	    date.set(Calendar.YEAR, endDate.getYear());
	    date.set(Calendar.MONTH, endDate.getMonthOfYear() - 1);
	    date.set(Calendar.DAY_OF_MONTH, endDate.getDayOfMonth());
	    setEndDate(date.getTime());
	}
    }

    public LocalDate getBeginDateAsLocalDate() {
	return beginDate != null ? LocalDate.fromDateFields(beginDate) : null;
    }

    public LocalDate getEndDateAsLocalDate() {
	return endDate != null ? LocalDate.fromDateFields(endDate) : null;
    }

    public ContractType getContractType() {
	return contractType;
    }

    public void setContractType(ContractType contractType) {
	this.contractType = contractType;
    }

    public JobApplicationType getApplicationType() {
	return applicationType;
    }

    public void setApplicationType(JobApplicationType applicationType) {
	this.applicationType = applicationType;
    }

    public SalaryType getSalaryType() {
	return salaryType;
    }

    public void setSalaryType(SalaryType salaryType) {
	this.salaryType = salaryType;
    }

}
