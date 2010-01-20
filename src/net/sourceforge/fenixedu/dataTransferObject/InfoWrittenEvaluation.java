/*
 * Created on 29/Out/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;

/**
 * @author Ana e Ricardo
 * 
 */
public class InfoWrittenEvaluation extends InfoEvaluation {

    protected WrittenEvaluation writtenEvaluation;

    protected Calendar day;

    protected Calendar beginning;

    protected Calendar end;

    protected Calendar enrollmentBeginDay;

    protected Calendar enrollmentEndDay;

    protected Calendar enrollmentBeginTime;

    protected Calendar enrollmentEndTime;

    protected List<InfoCurricularCourseScope> associatedCurricularCourseScope;

    protected List<InfoRoomOccupation> associatedRoomOccupation;

    protected List<InfoExecutionCourse> associatedExecutionCourse;

    protected Integer enrolledStudents;

    public List<InfoCurricularCourseScope> getAssociatedCurricularCourseScope() {
	return associatedCurricularCourseScope;
    }

    public List<InfoRoomOccupation> getWrittenEvaluationSpaceOccupations() {
	return associatedRoomOccupation;
    }

    public Calendar getBeginning() {
	return beginning;
    }

    public Calendar getDay() {
	return day;
    }

    public Calendar getEnd() {
	return end;
    }

    public Calendar getEnrollmentBeginDay() {
	return enrollmentBeginDay;
    }

    public Calendar getEnrollmentBeginTime() {
	return enrollmentBeginTime;
    }

    public Calendar getEnrollmentEndDay() {
	return enrollmentEndDay;
    }

    public Calendar getEnrollmentEndTime() {
	return enrollmentEndTime;
    }

    public void setAssociatedCurricularCourseScope(List<InfoCurricularCourseScope> list) {
	associatedCurricularCourseScope = list;
    }

    public void setWrittenEvaluationSpaceOccupations(List<InfoRoomOccupation> list) {
	associatedRoomOccupation = list;
    }

    public void setBeginning(Calendar calendar) {
	beginning = calendar;
    }

    public void setDay(Calendar calendar) {
	day = calendar;
    }

    public void setEnd(Calendar calendar) {
	end = calendar;
    }

    public void setEnrollmentBeginDay(Calendar calendar) {
	enrollmentBeginDay = calendar;
    }

    public void setEnrollmentBeginTime(Calendar calendar) {
	enrollmentBeginTime = calendar;
    }

    public void setEnrollmentEndDay(Calendar calendar) {
	enrollmentEndDay = calendar;
    }

    public void setEnrollmentEndTime(Calendar calendar) {
	enrollmentEndTime = calendar;
    }

    public List<InfoExecutionCourse> getAssociatedExecutionCourse() {
	return associatedExecutionCourse;
    }

    public void setAssociatedExecutionCourse(List<InfoExecutionCourse> list) {
	associatedExecutionCourse = list;
    }

    public Integer getEnrolledStudents() {
	return enrolledStudents;
    }

    public void setEnrolledStudents(Integer enrolledStudents) {
	this.enrolledStudents = enrolledStudents;
    }

    public Calendar getInicio() {
	return getBeginning();
    }

    public Calendar getFim() {
	return getEnd();
    }

    public void copyFromDomain(WrittenEvaluation writtenEvaluation) {
	super.copyFromDomain(writtenEvaluation);
	if (writtenEvaluation != null) {
	    setWrittenEvaluation(writtenEvaluation);
	    associatedExecutionCourse = new ArrayList<InfoExecutionCourse>();
	    associatedRoomOccupation = new ArrayList<InfoRoomOccupation>();
	    associatedCurricularCourseScope = new ArrayList<InfoCurricularCourseScope>();
	    for (ExecutionCourse executionCourse : writtenEvaluation.getAssociatedExecutionCourses()) {
		associatedExecutionCourse.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
	    }
	    for (WrittenEvaluationSpaceOccupation roomOccupation : writtenEvaluation.getWrittenEvaluationSpaceOccupations()) {
		associatedRoomOccupation.add(InfoRoomOccupation.newInfoFromDomain(roomOccupation));
	    }
	    for (CurricularCourseScope curricularCourseScope : writtenEvaluation.getAssociatedCurricularCourseScope()) {
		associatedCurricularCourseScope.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
	    }
	    setBeginning(writtenEvaluation.getBeginning());
	    setDay(writtenEvaluation.getDay());
	    setEnd(writtenEvaluation.getEnd());
	    setEnrollmentBeginDay(writtenEvaluation.getEnrollmentBeginDay());
	    setEnrollmentBeginTime(writtenEvaluation.getEnrollmentBeginTime());
	    setEnrollmentEndDay(writtenEvaluation.getEnrollmentEndDay());
	    setEnrollmentEndTime(writtenEvaluation.getEnrollmentEndTime());
	}
    }

    public static InfoWrittenEvaluation newInfoFromDomain(WrittenEvaluation writtenEvaluation) {
	InfoWrittenEvaluation infoWrittenEvaluation = null;
	if (writtenEvaluation != null) {
	    if (writtenEvaluation instanceof Exam) {
		infoWrittenEvaluation = InfoExam.newInfoFromDomain((Exam) writtenEvaluation);
	    } else if (writtenEvaluation instanceof WrittenTest) {
		infoWrittenEvaluation = InfoWrittenTest.newInfoFromDomain((WrittenTest) writtenEvaluation);
	    } else {
		infoWrittenEvaluation = new InfoWrittenEvaluation();
		infoWrittenEvaluation.copyFromDomain(writtenEvaluation);
	    }
	}
	return infoWrittenEvaluation;
    }

    public WrittenEvaluation getWrittenEvaluation() {
	return writtenEvaluation;
    }

    public void setWrittenEvaluation(WrittenEvaluation writtenEvaluation) {
	this.writtenEvaluation = writtenEvaluation;
    }
}