package net.sourceforge.fenixedu.domain;

import java.io.Serializable;

import org.joda.time.Partial;

public class QualificationBean implements Serializable {

    static private final long serialVersionUID = 3778404795808964270L;

    private QualificationType type;
    private String school;
    private String degree;
    private Partial attendedBegin;
    private Partial attendedEnd;
    private String year;
    private String mark;

    public QualificationBean() {
    }

    public QualificationType getType() {
	return type;
    }

    public void setType(QualificationType type) {
	this.type = type;
    }

    public String getSchool() {
	return school;
    }

    public void setSchool(String school) {
	this.school = school;
    }

    public Partial getAttendedBegin() {
	return attendedBegin;
    }

    public void setAttendedBegin(Partial attendedBegin) {
	this.attendedBegin = attendedBegin;
    }

    public Partial getAttendedEnd() {
	return attendedEnd;
    }

    public void setAttendedEnd(Partial attendedEnd) {
	this.attendedEnd = attendedEnd;
    }

    public String getYear() {
	return year;
    }

    public void setYear(String year) {
	this.year = year;
    }

    public String getMark() {
	return mark;
    }

    public void setMark(String mark) {
	this.mark = mark;
    }

    public String getDegree() {
	return degree;
    }

    public void setDegree(String degree) {
	this.degree = degree;
    }

}
