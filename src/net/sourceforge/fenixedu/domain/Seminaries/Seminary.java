/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at Jul 23, 2003, 9:49:19 AM
 * 
 */
public class Seminary extends Seminary_Base {

    public Seminary() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginDate() {
	if (this.getEnrollmentBegin() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getEnrollmentBegin());
	    return result;
	}
	return null;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginDate(Calendar calendar) {
	if (calendar != null) {
	    this.setEnrollmentBegin(calendar.getTime());
	} else {
	    this.setEnrollmentBegin(null);
	}
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginTime() {
	if (this.getEnrollmentTimeBegin() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getEnrollmentTimeBegin());
	    return result;
	}
	return null;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginTime(Calendar calendar) {
	if (calendar != null) {
	    this.setEnrollmentTimeBegin(calendar.getTime());
	} else {
	    this.setEnrollmentTimeBegin(null);
	}

    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndDate() {
	if (this.getEnrollmentEnd() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getEnrollmentEnd());
	    return result;
	}
	return null;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndDate(Calendar calendar) {
	if (calendar != null) {
	    this.setEnrollmentEnd(calendar.getTime());
	} else {
	    this.setEnrollmentEnd(null);
	}
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndTime() {
	if (this.getEnrollmentTimeEnd() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getEnrollmentTimeEnd());
	    return result;
	}
	return null;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndTime(Calendar calendar) {
	if (calendar != null) {
	    this.setEnrollmentTimeEnd(calendar.getTime());
	} else {
	    this.setEnrollmentTimeEnd(null);
	}
    }

}
