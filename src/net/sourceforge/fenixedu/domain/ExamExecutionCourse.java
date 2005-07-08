/*
 * Created on 29/Mar/2003
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ExamExecutionCourse extends ExamExecutionCourse_Base {

    public ExamExecutionCourse() {
    }

    public ExamExecutionCourse(IExam exam, IExecutionCourse executionCourse) {
        this.setExam(exam);
        this.setExecutionCourse(executionCourse);
    }

    public String toString() {
        return "[EXAM_EXECUTIONCOURSE:" + " exam= '" + this.getExam() + "'" + " execution_course= '"
                + this.getExecutionCourse() + "'" + "]";
    }

}
