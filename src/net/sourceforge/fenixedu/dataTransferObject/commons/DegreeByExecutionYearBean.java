package net.sourceforge.fenixedu.dataTransferObject.commons;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public class DegreeByExecutionYearBean implements Serializable, Comparable<DegreeByExecutionYearBean> {

    private Degree degree;
    private DegreeType degreeType;
    private ExecutionYear executionYear;
    private Set<Degree> administratedDegrees;
    private Set<DegreeType> administratedDegreeTypes;

    public DegreeByExecutionYearBean() {
    }

    public DegreeByExecutionYearBean(Set<DegreeType> administratedDegreeTypes) {
	this(administratedDegreeTypes, new TreeSet<Degree>(Degree.readAllByDegreeTypes(administratedDegreeTypes)));
    }

    public DegreeByExecutionYearBean(Set<DegreeType> administratedDegreeTypes, Set<Degree> administratedDegrees) {
	this.administratedDegrees = administratedDegrees;
	this.administratedDegreeTypes = administratedDegreeTypes;
    }

    public DegreeByExecutionYearBean(final Degree degree, final ExecutionYear executionYear) {
	setDegree(degree);
	setDegreeType(degree.getDegreeType());
	setExecutionYear(executionYear);
    }

    public DegreeByExecutionYearBean(final Degree degree, final ExecutionYear executionYear,
	    Set<DegreeType> administratedDegreeTypes) {
	this(degree, executionYear, administratedDegreeTypes, new TreeSet<Degree>(Degree
		.readAllByDegreeTypes(administratedDegreeTypes)));
    }

    public DegreeByExecutionYearBean(Degree degree, ExecutionYear executionYear, Set<DegreeType> administratedDegreeTypes,
	    Set<Degree> administratedDegrees) {
	setDegree(degree);
	setDegreeType(degree.getDegreeType());
	setExecutionYear(executionYear);
	this.administratedDegrees = administratedDegrees;
	this.administratedDegreeTypes = administratedDegreeTypes;
    }

    public Set<DegreeType> getAdministratedDegreeTypes() {
	return administratedDegreeTypes;
    }

    public void setAdministratedDegreeTypes(Set<DegreeType> administratedDegreeTypes) {
	this.administratedDegreeTypes = administratedDegreeTypes;
    }

    public Set<Degree> getAdministratedDegrees() {
	return administratedDegrees;
    }

    public void setAdministratedDegrees(Set<Degree> administratedDegrees) {
	this.administratedDegrees = administratedDegrees;
    }

    public Degree getDegree() {
	return degree;
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
	return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = executionYear;
    }

    public String getDegreeName() {
	return getDegree().getNameFor(getExecutionYear()).getContent();
    }

    public String getDegreePresentationName() {
	return getDegree().getPresentationName(getExecutionYear());
    }

    public String getKey() {
	return getDegree().getOID() + ":" + getExecutionYear().getOID();
    }

    @Override
    public boolean equals(Object obj) {
	return (obj instanceof DegreeByExecutionYearBean) ? equals((DegreeByExecutionYearBean) obj) : false;
    }

    public boolean equals(DegreeByExecutionYearBean bean) {
	return getDegree() == bean.getDegree();
    }

    @Override
    public int hashCode() {
	Degree degree = getDegree();
	if (degree != null) {
	    return getDegree().hashCode();
	} else {
	    return 0;
	}
    }

    public List<DegreeCurricularPlan> getDegreeCurricularPlans() {
	return getDegree().getDegreeCurricularPlans();
    }

    @Override
    public int compareTo(final DegreeByExecutionYearBean other) {
	return (other == null) ? 1 : Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID.compare(getDegree(), other.getDegree());
    }

    static public DegreeByExecutionYearBean buildFrom(final String key) {
	if (key == null || key.isEmpty()) {
	    return null;
	}
	final String[] values = key.split(":");
	final Degree degree = (Degree) DomainObject.fromOID(Long.valueOf(values[0]).longValue());
	final ExecutionYear year = (ExecutionYear) DomainObject.fromOID(Long.valueOf(values[1]).longValue());
	return new DegreeByExecutionYearBean(degree, year);
    }
}
