package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;

public class AssiduousnessClosedMonth extends AssiduousnessClosedMonth_Base {

    public AssiduousnessClosedMonth(Assiduousness assiduousness, ClosedMonth closedMonth,
	    Duration balance, Duration totalComplementaryWeeklyRestBalance,
	    Duration totalWeeklyRestBalance, Duration holidayRest, Duration balanceToDiscount,
	    double vacations, double tolerance, double article17, double article66) {
	setRootDomainObject(RootDomainObject.getInstance());

	setBalance(balance);
	setBalanceToDiscount(balanceToDiscount);
	setAssiduousness(assiduousness);
	setClosedMonth(closedMonth);
	setSaturdayBalance(totalComplementaryWeeklyRestBalance);
	setSundayBalance(totalWeeklyRestBalance);
	setHolidayBalance(holidayRest);
	setVacations(vacations);
	setTolerance(tolerance);
	setArticle17(article17);
	setArticle66(article66);
    }

    public Duration getBalanceWithoutBalanceDiscount() {
	return getBalance().plus(getBalanceToDiscount());
    }

    public HashMap<JustificationMotive, Duration> getPastJustificationsDurations() {
	HashMap<JustificationMotive, Duration> pastJustificationsDurations = new HashMap<JustificationMotive, Duration>();
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : getAssiduousness()
		.getAssiduousnessClosedMonths()) {
	    if (assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth().get(
		    DateTimeFieldType.year()) == getClosedMonth().getClosedYearMonth().get(
		    DateTimeFieldType.year())) {
		for (ClosedMonthJustification closedMonthJustification : assiduousnessClosedMonth
			.getClosedMonthJustifications()) {
		    Duration duration = pastJustificationsDurations.get(closedMonthJustification);
		    if (duration == null) {
			duration = Duration.ZERO;
		    }
		    duration = duration.plus(closedMonthJustification.getJustificationDuration());
		    pastJustificationsDurations.put(closedMonthJustification.getJustificationMotive(),
			    duration);
		}
	    }
	}
	return pastJustificationsDurations;
    }
}
