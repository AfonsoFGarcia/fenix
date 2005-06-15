/*
 * Created on 8/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

/**
 * @author Ana e Ricardo
 */
public class WrittenEvaluation extends WrittenEvaluation_Base {
    protected Calendar day;

    protected Calendar beginning;

    protected Calendar end;

    protected Calendar enrollmentBeginDay;

    protected Calendar enrollmentBeginTime;

    protected Calendar enrollmentEndDay;

    protected Calendar enrollmentEndTime;

    /**
     * @return
     */
    public Calendar getBeginning() {
        return beginning;
    }

    /**
     * @return
     */
    public Calendar getDay() {
        return day;
    }

    /**
     * @return
     */
    public Calendar getEnd() {
        return end;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginDay() {
        return enrollmentBeginDay;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginTime() {
        return enrollmentBeginTime;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndDay() {
        return enrollmentEndDay;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndTime() {
        return enrollmentEndTime;
    }

    /**
     * @param calendar
     */
    public void setBeginning(Calendar calendar) {
        beginning = calendar;
    }

    /**
     * @param calendar
     */
    public void setDay(Calendar calendar) {
        day = calendar;
    }

    /**
     * @param calendar
     */
    public void setEnd(Calendar calendar) {
        end = calendar;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginDay(Calendar calendar) {
        enrollmentBeginDay = calendar;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginTime(Calendar calendar) {
        enrollmentBeginTime = calendar;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndDay(Calendar calendar) {
        enrollmentEndDay = calendar;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndTime(Calendar calendar) {
        enrollmentEndTime = calendar;
    }

    public String toString() {
        return "[WRITTEN EVALUATION:" + " id= '" + this.getIdInternal() + "'\n" + " day= '"
                + this.getDay() + "'\n" + " beginning= '" + this.getBeginning() + "'\n" + " end= '"
                + this.getEnd() + "'\n" + "";
    }

    public boolean equals(Object obj) {
        if (obj instanceof IWrittenEvaluation) {
            IWrittenEvaluation writtenEvaluationObj = (IWrittenEvaluation) obj;
            return this.getIdInternal().equals(writtenEvaluationObj.getIdInternal());
        }

        return false;
    }

}
