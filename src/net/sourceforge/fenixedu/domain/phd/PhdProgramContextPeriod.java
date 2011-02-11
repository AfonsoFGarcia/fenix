package net.sourceforge.fenixedu.domain.phd;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixWebFramework.services.Service;

public class PhdProgramContextPeriod extends PhdProgramContextPeriod_Base {

    public static final Comparator<PhdProgramContextPeriod> COMPARATOR_BY_BEGIN_DATE = new Comparator<PhdProgramContextPeriod>() {

	@Override
	public int compare(PhdProgramContextPeriod o1, PhdProgramContextPeriod o2) {
	    return o1.getBeginDate().compareTo(o2.getBeginDate());
	}

    };
    
    protected PhdProgramContextPeriod() {
        super();
	setRootDomainObject(getRootDomainObject());
    }
    
    protected PhdProgramContextPeriod(PhdProgram phdProgram, DateTime beginPeriod, DateTime endPeriod) {
	super();
	init(phdProgram, beginPeriod, endPeriod);
    }

    protected void init(PhdProgram phdProgram, DateTime beginPeriod, DateTime endPeriod) {
	checkParameters(phdProgram, beginPeriod, endPeriod);

	setPhdProgram(phdProgram);
	setBeginDate(beginPeriod);
	setEndDate(endPeriod);
    }

    protected void checkParameters(PhdProgram phdProgram, DateTime beginPeriod, DateTime endPeriod) {
	if (phdProgram == null) {
	    throw new DomainException("phd.PhdProgramContextPeriod.phdProgram.cannot.be.null");
	}

	if (beginPeriod == null) {
	    throw new DomainException("phd.PhdProgramContextPeriod.beginPeriod.cannot.be.null");
	}

	if (endPeriod != null && !endPeriod.isAfter(beginPeriod)) {
	    throw new DomainException("phd.PhdProgramContextPeriod.endPeriod.is.after.of.beginPeriod");
	}

	checkOverlaps(phdProgram, beginPeriod, endPeriod);
    }

    private void checkOverlaps(PhdProgram phdProgram, DateTime beginPeriod, DateTime endPeriod) {
	for (PhdProgramContextPeriod period : phdProgram.getPhdProgramContextPeriods()) {
	    if (period == this) {
		continue;
	    }

	    if (period.overlaps(beginPeriod, endPeriod)) {
		throw new DomainException("phd.PhdProgramContextPeriod.period.is.overlaping.another");
	    }
	}
    }

    private boolean overlaps(DateTime beginPeriod, DateTime endPeriod) {
	return getOwnInterval().overlaps(new Interval(beginPeriod, endPeriod));
    }

    public boolean contains(DateTime dateTime) {
	return getOwnInterval().contains(dateTime);
    }

    private Interval getOwnInterval() {
	return new Interval(getBeginDate(), getEndDate());
    }

    @Service
    public void edit(DateTime beginDate, DateTime endDate) {
	checkParameters(getPhdProgram(), beginDate, endDate);
    }

    @Service
    public void closePeriod(DateTime endPeriod) {
	if (endPeriod == null) {
	    throw new DomainException("phd.PhdProgramContextPeriod.endPeriod.cannot.be.null");
	}

	setEndDate(endPeriod);
    }

    @Service
    public static PhdProgramContextPeriod create(final PhdProgram phdProgram, DateTime beginPeriod, DateTime endPeriod) {
	return new PhdProgramContextPeriod(phdProgram, beginPeriod, endPeriod);
    }
    
    @Service
    public static PhdProgramContextPeriod create(PhdProgramContextPeriodBean bean) {
	return create(bean.getPhdProgram(), bean.getBeginDateAtMidnight(), bean.getEndDateBeforeMidnight());
    }

    @Service
    public void deletePeriod() {
	delete();
    }

    private void delete() {
	removePhdProgram();
	removeRootDomainObject();

	deleteDomainObject();
    }
}
