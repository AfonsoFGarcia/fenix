package net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class SpecialSeasonStatusTrackerBean implements Serializable{

    private static final long serialVersionUID = 7601169267648955212L;
    
    private ExecutionSemester executionSemester;
    private Department department;
    private CompetenceCourse competenceCourse;
    private List<Enrolment> enrolments;
    private List<SpecialSeasonStatusTrackerRegisterBean> entries;
    
    public SpecialSeasonStatusTrackerBean() {
	super();
	entries = new ArrayList<SpecialSeasonStatusTrackerRegisterBean>();
    }
    
    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }
    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }
    public Department getDepartment() {
        return department;
    }
    public void setDepartment(Department department) {
        this.department = department;
    }
    public CompetenceCourse getCompetenceCourse() {
        return competenceCourse;
    }
    public void setCompetenceCourse(CompetenceCourse competenceCourse) {
        this.competenceCourse = competenceCourse;
    }
    public List<Enrolment> getEnrolments() {
        return enrolments;
    }
    public void setEnrolments(List<Enrolment> enrolments) {
        this.enrolments = enrolments;
    }
    public List<SpecialSeasonStatusTrackerRegisterBean> getEntries() {
	return entries;
    }
    public void addEntry(Integer studentNumber, String studentName, String degreeSigla, String courseName) {
	SpecialSeasonStatusTrackerRegisterBean newEntry = new SpecialSeasonStatusTrackerRegisterBean(studentNumber, studentName, degreeSigla, courseName);
	entries.add(newEntry);
    }
    public void clearEntries() {
	entries.clear();
    }
}
