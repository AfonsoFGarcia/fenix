package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.fileSuport.INode;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;

/**
 * Created on 11/Fev/2003
 * 
 * @author Jo�o Mota
 * @author jpvl
 * 
 */
public class ExecutionPeriod extends ExecutionPeriod_Base implements INode, Comparable {

    public String toString() {
        String result = "[EXECUTION_PERIOD";
        result += ", internalCode=" + getIdInternal();
        result += ", name=" + getName();
        result += ", executionYear=" + getExecutionYear();
        result += ", begin Date=" + getBeginDate();
        result += ", end Date=" + getEndDate();
        result += "]\n";
        return result;
    }

    public String getSlideName() {
        String result = getParentNode().getSlideName() + "/EP" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        ExecutionYear executionYear = getExecutionYear();
        return executionYear;
    }

    public int compareTo(Object object) {
        final ExecutionPeriod executionPeriod = (ExecutionPeriod) object; 
        final ExecutionYear executionYear = executionPeriod.getExecutionYear();

        if (getExecutionYear() == executionYear) {
            return getSemester().compareTo(executionPeriod.getSemester());
        } else {
            return getExecutionYear().compareTo(executionYear);
        }
    }
    

	public String getQualifiedName()
	{
		return new StringBuilder().append(this.getName()).append(" ").append(this.getExecutionYear().getYear()).toString();
	}

    public boolean containsDay(Date day) {
        return !(this.getBeginDate().after(day) || this.getEndDate().before(day));
    }

    public DateMidnight getThisMonday() {
   		final DateTime beginningOfSemester = new DateTime(this.getBeginDate());
   		final DateTime endOfSemester = new DateTime(this.getEndDate());

   		if (beginningOfSemester.isAfterNow() || endOfSemester.isBeforeNow()) {
   			return null;
   		}

   		final DateMidnight now = new DateMidnight();
   		return now.withField(DateTimeFieldType.dayOfWeek(), 1);
    }

    public Interval getCurrentWeek() {
   		final DateMidnight thisMonday = getThisMonday();
   		return thisMonday == null ? null : new Interval(thisMonday, thisMonday.plusWeeks(1));
    }

    public Interval getPreviousWeek() {
   		final DateMidnight thisMonday = getThisMonday();
   		return thisMonday == null ? null : new Interval(thisMonday.minusWeeks(1), thisMonday);
    }
    
    public boolean isAfter(ExecutionPeriod executionPeriod) {
    	return this.compareTo(executionPeriod) > 0;
    }

    public boolean isAfterOrEquals(ExecutionPeriod executionPeriod) {
    	return this.compareTo(executionPeriod) >= 0;
    }

    public boolean isBefore(ExecutionPeriod executionPeriod) {
    	return this.compareTo(executionPeriod) < 0;
    }

    public boolean isBeforeOrEquals(ExecutionPeriod executionPeriod) {
    	return this.compareTo(executionPeriod) <= 0;
    }

}
