/*
 * Created on Mar 7, 2004
 *
 */
package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.Calendar;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;

/**
 * @author Luis Cruz
 * 
 */
public class Scheduleing extends Scheduleing_Base {

    public Date getEndOfProposalPeriod() {
        if (this.getEndOfProposalPeriodDate() != null && this.getEndOfProposalPeriodTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.getEndOfProposalPeriodDate().getTime());
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(this.getEndOfProposalPeriodTime().getTime());
            calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
            return calendar.getTime();
        }
        return null;
    }

    public void setEndOfProposalPeriod(Date endOfProposalPeriod) {
        this.setEndOfProposalPeriodDate(endOfProposalPeriod);
        this.setEndOfProposalPeriodTime(endOfProposalPeriod);
    }

    public Date getStartOfProposalPeriod() {
        if (this.getStartOfProposalPeriodDate() != null && this.getStartOfProposalPeriodTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.getStartOfProposalPeriodDate().getTime());
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(this.getStartOfProposalPeriodTime().getTime());
            calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
            return calendar.getTime();
        }
        return null;
    }

    public void setStartOfProposalPeriod(Date startOfProposalPeriod) {
        this.setStartOfProposalPeriodDate(startOfProposalPeriod);
        this.setStartOfProposalPeriodTime(startOfProposalPeriod);
    }

    public Date getStartOfCandidacyPeriod() {
        if (this.getStartOfCandidacyPeriodDate() != null && this.getStartOfCandidacyPeriodTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.getStartOfCandidacyPeriodDate().getTime());
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(this.getStartOfCandidacyPeriodTime().getTime());
            calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
            return calendar.getTime();
        }
        return null;
    }

    public void setStartOfCandidacyPeriod(Date startOfCandidacyPeriod) {
        this.setStartOfCandidacyPeriodDate(startOfCandidacyPeriod);
        this.setStartOfCandidacyPeriodTime(startOfCandidacyPeriod);
    }

    public Date getEndOfCandidacyPeriod() {
        if (this.getEndOfCandidacyPeriodDate() != null && this.getEndOfCandidacyPeriodTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.getEndOfCandidacyPeriodDate().getTime());
            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(this.getEndOfCandidacyPeriodTime().getTime());
            calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
            return calendar.getTime();
        }
        return null;
    }

    public void setEndOfCandidacyPeriod(Date endOfCandidacyPeriod) {
        this.setEndOfCandidacyPeriodDate(endOfCandidacyPeriod);
        this.setEndOfCandidacyPeriodTime(endOfCandidacyPeriod);
    }

    public SortedSet<ExecutionDegree> getExecutionDegreesSortedByDegreeName() {
    	final SortedSet<ExecutionDegree> executionDegrees =
    			new TreeSet<ExecutionDegree>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
    	executionDegrees.addAll(getExecutionDegrees());
    	return executionDegrees;
    }

}
