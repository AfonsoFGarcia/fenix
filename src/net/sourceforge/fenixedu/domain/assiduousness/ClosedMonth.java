package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class ClosedMonth extends ClosedMonth_Base {

    static final public Comparator<ClosedMonth> COMPARATOR_BY_DATE = new Comparator<ClosedMonth>() {
        public int compare(final ClosedMonth o1, final ClosedMonth o2) {
            return o1.getClosedYearMonth().compareTo(o2.getClosedYearMonth());
        }
    };
    
    public static final int dayOfMonthToCloseLastMonth = 4;

    public ClosedMonth(Partial closedYearMonth) {
	setRootDomainObject(RootDomainObject.getInstance());
	setClosedForBalance(true);
	setClosedForExtraWork(false);
	setClosedYearMonth(closedYearMonth);
    }

    public ClosedMonth(YearMonthDay day) {
	setRootDomainObject(RootDomainObject.getInstance());
	setClosedForBalance(true);
	setClosedForExtraWork(false);
	setClosedYearMonth(new Partial().with(DateTimeFieldType.monthOfYear(), day.getMonthOfYear())
		.with(DateTimeFieldType.year(), day.getYear()));
    }

    public static boolean isMonthClosed(Partial yearMonth) {
	for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
	    if (closedMonth.getClosedYearMonth().equals(yearMonth)) {
		return true;
	    }
	}
	return false;
    }

    public static boolean isMonthClosedForExtraWork(Partial yearMonth) {
	for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
	    if (closedMonth.getClosedYearMonth().equals(yearMonth)
		    && closedMonth.getClosedForExtraWork()) {
		return true;
	    }
	}
	return false;
    }

    public static boolean isMonthClosed(YearMonthDay day) {
	Partial yearMonth = new Partial().with(DateTimeFieldType.monthOfYear(), day.getMonthOfYear())
		.with(DateTimeFieldType.year(), day.getYear());
	return isMonthClosed(yearMonth);
    }

    public static ClosedMonth getLastMonthClosed() {
	return getLastMonthClosed(false);
    }

    public static YearMonthDay getLastClosedYearMonthDay() {
	Partial lastClosedYearMonth = getLastMonthClosed(false).getClosedYearMonth();
	YearMonthDay firstDay = new YearMonthDay(lastClosedYearMonth.get(DateTimeFieldType.year()),
		lastClosedYearMonth.get(DateTimeFieldType.monthOfYear()), 1);
	return new YearMonthDay(lastClosedYearMonth.get(DateTimeFieldType.year()), lastClosedYearMonth
		.get(DateTimeFieldType.monthOfYear()), firstDay.dayOfMonth().getMaximumValue());
    }

    public static ClosedMonth getLastMonthClosed(boolean extraWork) {
	ClosedMonth resultClosedMonth = null;
	for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
	    if (resultClosedMonth == null
		    || (closedMonth.getClosedYearMonth().isAfter(resultClosedMonth.getClosedYearMonth()) && (!extraWork || closedMonth
			    .getClosedForExtraWork()))) {
		resultClosedMonth = closedMonth;
	    }
	}
	return resultClosedMonth;
    }

    public boolean sameClosedMonth(ClosedMonth closedMonth) {
	return closedMonth.getClosedYearMonth().isEqual(getClosedYearMonth());
    }

    public static boolean getCanCloseMonth(Partial yearMonth) {
	YearMonthDay yearMonthBefore = new YearMonthDay(yearMonth.get(DateTimeFieldType.year()),
		yearMonth.get(DateTimeFieldType.monthOfYear()), 1).minusMonths(1);
	if (isMonthClosed(yearMonthBefore) || RootDomainObject.getInstance().getClosedMonths().isEmpty()) {
	    YearMonthDay yearMonthAfter = new YearMonthDay(yearMonth.get(DateTimeFieldType.year()),
		    yearMonth.get(DateTimeFieldType.monthOfYear()), dayOfMonthToCloseLastMonth)
		    .plusMonths(1);
	    YearMonthDay now = new YearMonthDay();
	    if (!now.isBefore(yearMonthAfter)) {
		return true;
	    }
	}
	return false;
    }

    public static ClosedMonth getClosedMonth(YearMonth yearMonth) {
	for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
	    if (closedMonth.getClosedYearMonth().get(DateTimeFieldType.year()) == yearMonth.getYear()
		    && closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()) == yearMonth
			    .getNumberOfMonth()) {
		return closedMonth;
	    }
	}
	return null;
    }

    public void delete() {
	removeRootDomainObject();
	List<AssiduousnessClosedMonth> assiduousnessClosedMonths = new ArrayList<AssiduousnessClosedMonth>(
		getAssiduousnessClosedMonths());
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : assiduousnessClosedMonths) {
	    getAssiduousnessClosedMonths().remove(assiduousnessClosedMonth);
	    assiduousnessClosedMonth.delete();
	}
	deleteDomainObject();
    }

}
