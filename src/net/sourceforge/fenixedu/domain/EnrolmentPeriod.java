/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

/**
 * @author jpvl
 */
public abstract class EnrolmentPeriod extends EnrolmentPeriod_Base {

    public static final Comparator<EnrolmentPeriod> COMPARATOR_BY_DEGREE_NAME = new Comparator<EnrolmentPeriod>() {

	@Override
	public int compare(EnrolmentPeriod o1, EnrolmentPeriod o2) {
	    final DegreeCurricularPlan dcp1 = o1.getDegreeCurricularPlan();
	    final DegreeCurricularPlan dcp2 = o2.getDegreeCurricularPlan();
	    final int dcp = DegreeCurricularPlan.COMPARATOR_BY_PRESENTATION_NAME.compare(dcp1, dcp2);
	    return dcp == 0 ? o2.hashCode() - o1.hashCode() : dcp;
	}
	
    };

    public EnrolmentPeriod() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected void init(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionSemester executionSemester,
	    final Date startDate, final Date endDate) {
	init(degreeCurricularPlan, executionSemester, new DateTime(startDate), new DateTime(endDate));
    }

    protected void init(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionSemester executionSemester,
	    final DateTime startDate, final DateTime endDate) {

	if (!endDate.isAfter(startDate)) {
	    throw new DomainException("EnrolmentPeriod.end.date.must.be.after.start.date");
	}

	setDegreeCurricularPlan(degreeCurricularPlan);
	setExecutionPeriod(executionSemester);
	setStartDateDateTime(startDate);
	setEndDateDateTime(endDate);
    }

    public boolean isValid() {
	return containsDate(new DateTime());
    }

    public boolean isValid(final Date date) {
	return containsDate(new DateTime(date));
    }

    public boolean containsDate(DateTime date) {
	return !(getStartDateDateTime().isAfter(date) || getEndDateDateTime().isBefore(date));
    }

    public boolean isFor(final ExecutionSemester executionSemester) {
	return getExecutionPeriod() == executionSemester;
    }

    public void delete() {
	removeDegreeCurricularPlan();
	removeExecutionPeriod();
	removeRootDomainObject();
	deleteDomainObject();
    }

}