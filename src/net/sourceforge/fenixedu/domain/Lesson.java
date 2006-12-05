package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;
import net.sourceforge.fenixedu.util.date.TimePeriod;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.YearMonthDay;

public class Lesson extends Lesson_Base {

    public static final Comparator LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME = new ComparatorChain();
    static {
	((ComparatorChain) LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME).addComparator(new BeanComparator(
		"diaSemana.diaSemana"));
	((ComparatorChain) LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME).addComparator(new BeanComparator(
		"beginHourMinuteSecond"));
	((ComparatorChain) LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME).addComparator(new BeanComparator(
		"idInternal"));
    }

    public Lesson() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Lesson(DiaSemana diaSemana, Calendar inicio, Calendar fim, ShiftType tipo, OldRoom sala,
	    RoomOccupation roomOccupation, Shift shift, Integer weekOfQuinzenalStart, Integer frequency) {

	this();
	setDiaSemana(diaSemana);
	setInicio(inicio);
	setFim(fim);
	setTipo(tipo);
	setSala(sala);
	setRoomOccupation(roomOccupation);
	setShift(shift);
	setFrequency(frequency);
	setWeekOfQuinzenalStart(weekOfQuinzenalStart);
    }

    public void edit(DiaSemana diaSemana, Calendar inicio, Calendar fim, ShiftType tipo,
	    OldRoom salaNova, Integer frequency, Integer weekOfQuinzenalStart) {

	setDiaSemana(diaSemana);
	setInicio(inicio);
	setFim(fim);
	setTipo(tipo);
	setSala(salaNova);
	setFrequency(frequency);
	setWeekOfQuinzenalStart(weekOfQuinzenalStart);
    }

    public void delete() {
	if (getShift().hasAnyAssociatedStudentGroups()) {
	    throw new DomainException("error.deleteLesson.with.Shift.with.studentGroups", prettyPrint());
	}

	if (getShift().hasAnyStudents()) {
	    throw new DomainException("error.deleteLesson.with.Shift.with.students", prettyPrint());
	}

	if (hasAnyAssociatedSummaries()) {
	    throw new DomainException("error.deleteLesson.with.summaries", prettyPrint());
	}

	removeExecutionPeriod();
	removeSala();
	removeShift();
	getRoomOccupation().delete();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public Calendar getInicio() {
	if (this.getBegin() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getBegin());
	    return result;
	}
	return null;
    }

    public void setInicio(Calendar inicio) {
	if (inicio != null) {
	    this.setBegin(inicio.getTime());
	} else {
	    this.setBegin(null);
	}
    }

    public Calendar getFim() {
	if (this.getEnd() != null) {
	    Calendar result = Calendar.getInstance();
	    result.setTime(this.getEnd());
	    return result;
	}
	return null;
    }

    public void setFim(Calendar fim) {
	if (fim != null) {
	    this.setEnd(fim.getTime());
	} else {
	    this.setEnd(null);
	}
    }

    public double hours() {
	TimePeriod timePeriod = new TimePeriod(this.getInicio(), this.getFim());
	return timePeriod.hours().doubleValue();
    }

    public String getInicioString() {
	return String.valueOf(getInicio().get(Calendar.HOUR_OF_DAY));
    }

    public double hoursAfter(int hour) {

	final Calendar start = this.getInicio();
	final Calendar end = this.getFim();

	final Calendar specifiedHour = Calendar.getInstance();
	specifiedHour.setTime(this.getEnd());
	specifiedHour.set(Calendar.HOUR_OF_DAY, hour);
	specifiedHour.set(Calendar.MINUTE, 0);
	specifiedHour.set(Calendar.SECOND, 0);
	specifiedHour.set(Calendar.MILLISECOND, 0);

	if (!start.before(specifiedHour)) {
	    TimePeriod timePeriod = new TimePeriod(start, end);
	    return timePeriod.hours().doubleValue();

	} else if (end.after(specifiedHour)) {
	    TimePeriod timePeriod = new TimePeriod(specifiedHour, end);
	    return timePeriod.hours().doubleValue();
	}

	return 0.0;
    }

    public Summary getSummaryByDate(YearMonthDay date) {
	for (Summary summary : getAssociatedSummaries()) {
	    if (summary.getSummaryDateYearMonthDay().isEqual(date)) {
		return summary;
	    }
	}
	return null;
    }

    public SortedSet<Summary> getSummariesSortedByDate() {
	return getSummaries(new ReverseComparator(Summary.COMPARATOR_BY_DATE_AND_HOUR));
    }

    public SortedSet<Summary> getSummariesSortedByNewestDate() {
	return getSummaries(Summary.COMPARATOR_BY_DATE_AND_HOUR);
    }

    public boolean isTimeValidToInsertSummary(HourMinuteSecond time, YearMonthDay summaryDate) {
	YearMonthDay currentDate = new YearMonthDay();
	return ((summaryDate.isAfter(currentDate) || summaryDate == null) ? false : summaryDate
		.isBefore(currentDate) ? true : !getEndHourMinuteSecond().isAfter(time));
    }

    public boolean isDateValidToInsertSummary(YearMonthDay date) {
	List<YearMonthDay> allLessonDatesEvenToday = getAllLessonDatesEvenToday();
	return (allLessonDatesEvenToday.isEmpty() || date == null) ? false : allLessonDatesEvenToday
		.contains(date);
    }

    public YearMonthDay getLessonStartDay() {
	YearMonthDay periodStart = getRoomOccupation().getPeriod().getStartYearMonthDay();
	int weekOfQuinzenalStart = (getWeekOfQuinzenalStart() != null) ? getWeekOfQuinzenalStart()
		.intValue() : 0;
	YearMonthDay lessonStart = periodStart.plusDays(7 * weekOfQuinzenalStart);
	int lessonStartDayOfWeek = lessonStart.toDateTimeAtMidnight().getDayOfWeek();
	return lessonStart.plusDays(getLessonWeekDayToYearMonthDayFormat() - lessonStartDayOfWeek);
    }

    public YearMonthDay getLessonEndDay() {
	YearMonthDay periodEnd = getRoomOccupation().getPeriod().getEndYearMonthDay();
	int lessonEndDayOfWeek = periodEnd.toDateTimeAtMidnight().getDayOfWeek();
	return periodEnd.minusDays(lessonEndDayOfWeek - getLessonWeekDayToYearMonthDayFormat());
    }

    public int getLessonWeekDayToYearMonthDayFormat() {
	return (getDiaSemana().getDiaSemana().intValue() == 1) ? 7 : (getDiaSemana().getDiaSemana()
		.intValue() - 1);
    }

    private Campus getLessonCampus() {
	net.sourceforge.fenixedu.domain.Campus oldCampus = getSala().getBuilding().getCampus();
	if (oldCampus != null) {
	    for (Campus campus : Space.getAllCampus()) {
		if (campus.getSpaceInformation().getName().equalsIgnoreCase(oldCampus.getName())) {
		    return campus;
		}
	    }
	}
	return null;
    }

    public YearMonthDay getNextPossibleSummaryDate() {
	YearMonthDay currentDate = new YearMonthDay();
	YearMonthDay lessonEndDay = getLessonEndDay();
	YearMonthDay endDateToSearch = (lessonEndDay.isAfter(currentDate)) ? currentDate : lessonEndDay;

	HourMinuteSecond now = new HourMinuteSecond();
	Campus lessonCampus = getLessonCampus();
	Summary lastSummary = getLastSummary();

	if (lastSummary != null) {
	    YearMonthDay summaryDateYearMonthDay = lastSummary.getSummaryDateYearMonthDay();
	    YearMonthDay nextSummaryDate = summaryDateYearMonthDay.plusDays(7);
	    while (true) {
		nextSummaryDate = getPossibleDateInValidLessonWeekDay(nextSummaryDate);
		if (nextSummaryDate.isAfter(endDateToSearch)) {
		    break;
		}
		if (!Holiday.isHoliday(nextSummaryDate, lessonCampus)
			&& isTimeValidToInsertSummary(now, nextSummaryDate)) {
		    return nextSummaryDate;
		}
		nextSummaryDate = nextSummaryDate.plusDays(7);
	    }
	} else {
	    YearMonthDay lessonStartDay = getLessonStartDay();
	    return (!lessonStartDay.isAfter(endDateToSearch)) ? lessonStartDay : null;
	}

	return null;
    }

    public List<YearMonthDay> getPossibleDatesToInsertSummary() {
	SortedSet<Summary> summaries = getSummariesSortedByDate();
	List<YearMonthDay> datesToInsert = new ArrayList<YearMonthDay>();
	List<YearMonthDay> datesSearched = new ArrayList<YearMonthDay>();

	YearMonthDay lessonEndDay = getLessonEndDay();
	YearMonthDay currentDate = new YearMonthDay();

	YearMonthDay startDateToSearch = getLessonStartDay();
	YearMonthDay endDateToSearch = (lessonEndDay.isAfter(currentDate)) ? currentDate : lessonEndDay;

	HourMinuteSecond now = new HourMinuteSecond();
	Campus lessonCampus = getLessonCampus();

	if (!startDateToSearch.isAfter(endDateToSearch)) {
	    if (summaries.isEmpty()) {
		while (true) {
		    startDateToSearch = addPossibleDate(datesToInsert, startDateToSearch,
			    startDateToSearch, now, lessonCampus);
		    if (startDateToSearch.isAfter(endDateToSearch)) {
			break;
		    }
		}
	    } else {
		for (Summary summary : summaries) {
		    if (!datesSearched.contains(summary.getSummaryDateYearMonthDay())) {
			YearMonthDay summaryDate = summary.getSummaryDateYearMonthDay();
			while (true) {
			    if (!hasLessonWeekDayChanges(summaryDate)) {
				if (startDateToSearch.isBefore(summaryDate)) {
				    startDateToSearch = addPossibleDate(datesToInsert,
					    startDateToSearch, startDateToSearch, now, lessonCampus);
				} else if (startDateToSearch.isEqual(summaryDate)) {
				    startDateToSearch = startDateToSearch.plusDays(7);
				    break;
				} else {
				    // ERROR: I hope that never enter here
				    System.out.println("3 2 1 ... BOOOOOOMMMMMMMMM!!!");
				    startDateToSearch = startDateToSearch.plusDays(7);
				}

			    } else if (isInSameWeek(startDateToSearch, summaryDate)) {
				startDateToSearch = startDateToSearch.plusDays(7);
				break;

			    } else if (startDateToSearch.isBefore(summaryDate)) {
				YearMonthDay day = getPossibleDateInValidSummaryDateWeekDay(
					startDateToSearch, summaryDate);
				startDateToSearch = addPossibleDate(datesToInsert, day,
					startDateToSearch, now, lessonCampus);

			    } else {
				// ERROR: I hope that never enter here
				System.out.println("3 2 1 ... BOOOOOOMMMMMMMMM!!!");
				startDateToSearch = startDateToSearch.plusDays(7);
			    }

			    if (startDateToSearch.isAfter(endDateToSearch)) {
				break;
			    }
			}
			datesSearched.add(summaryDate);
		    }
		}
		if (!startDateToSearch.isAfter(endDateToSearch)) {
		    while (true) {
			startDateToSearch = addPossibleDate(datesToInsert, startDateToSearch,
				startDateToSearch, now, lessonCampus);
			if (startDateToSearch.isAfter(endDateToSearch)) {
			    break;
			}
		    }
		}
	    }
	}
	return datesToInsert;
    }

    private YearMonthDay addPossibleDate(List<YearMonthDay> datesToInsert, YearMonthDay dayToInsert,
	    YearMonthDay startDateToSearch, HourMinuteSecond now, Campus lessonCampus) {

	if (!Holiday.isHoliday(dayToInsert, lessonCampus)
		&& isTimeValidToInsertSummary(now, dayToInsert)) {
	    datesToInsert.add(dayToInsert);
	}
	return startDateToSearch.plusDays(7);
    }

    public List<YearMonthDay> getAllLessonDatesEvenToday() {
	YearMonthDay startDateToSearch = getLessonStartDay();
	YearMonthDay lessonEndDay = getLessonEndDay();
	YearMonthDay currentDate = new YearMonthDay();
	YearMonthDay endDateToSearch = (lessonEndDay.isAfter(currentDate)) ? currentDate : lessonEndDay;
	return getLessonDates(startDateToSearch, endDateToSearch);
    }

    public List<YearMonthDay> getAllLessonDates() {
	YearMonthDay startDateToSearch = getLessonStartDay();
	YearMonthDay endDateToSearch = getLessonEndDay();
	return getLessonDates(startDateToSearch, endDateToSearch);
    }

    private boolean hasLessonWeekDayChanges(YearMonthDay date) {
	int dateWeekDay = date.toDateTimeAtMidnight().getDayOfWeek();
	int lessonWeekDayToYearMonthDayFormat = getLessonWeekDayToYearMonthDayFormat();
	return lessonWeekDayToYearMonthDayFormat != dateWeekDay;
    }

    private boolean isInSameWeek(YearMonthDay possibleDate, YearMonthDay summaryDate) {
	int dateWeek = possibleDate.toDateTimeAtMidnight().getWeekOfWeekyear();
	int summaryWeek = summaryDate.toDateTimeAtMidnight().getWeekOfWeekyear();
	return dateWeek == summaryWeek;
    }

    private List<YearMonthDay> getLessonDates(YearMonthDay startDateToSearch,
	    YearMonthDay endDateToSearch) {

	List<YearMonthDay> dates = new ArrayList<YearMonthDay>();
	if (!startDateToSearch.isAfter(endDateToSearch)) {
	    Campus lessonCampus = getLessonCampus();
	    while (true) {
		if (!Holiday.isHoliday(startDateToSearch, lessonCampus)) {
		    dates.add(startDateToSearch);
		}
		startDateToSearch = startDateToSearch.plusDays(7);
		if (startDateToSearch.isAfter(endDateToSearch)) {
		    break;
		}
	    }
	}
	return dates;
    }

    private Summary getLastSummary() {
	SortedSet<Summary> summaries = getSummariesSortedByNewestDate();
	return (summaries.isEmpty()) ? null : summaries.first();
    }

    private YearMonthDay getPossibleDateInValidLessonWeekDay(YearMonthDay possibleDate) {
	int lessonWeekDay = getLessonWeekDayToYearMonthDayFormat();
	int possibleDateWeekDay = possibleDate.toDateTimeAtMidnight().getDayOfWeek();
	int weekDayDiff = lessonWeekDay - possibleDateWeekDay;
	return getWeekDayValidDate(weekDayDiff, possibleDate);
    }

    private YearMonthDay getPossibleDateInValidSummaryDateWeekDay(YearMonthDay possibleDate,
	    YearMonthDay summaryDate) {
	int summaryDateWeekDay = summaryDate.toDateTimeAtMidnight().getDayOfWeek();
	int possibleDateWeekDay = possibleDate.toDateTimeAtMidnight().getDayOfWeek();
	int weekDayDiff = summaryDateWeekDay - possibleDateWeekDay;
	return getWeekDayValidDate(weekDayDiff, possibleDate);
    }

    private YearMonthDay getWeekDayValidDate(int weekDayDiff, YearMonthDay possibleDate) {
	if (weekDayDiff != 0 && weekDayDiff > 0) {
	    return possibleDate.plusDays(weekDayDiff);
	} else if (weekDayDiff != 0 && weekDayDiff < 0) {
	    return possibleDate.minusDays((-1) * weekDayDiff);
	}
	return possibleDate;
    }

    private SortedSet<Summary> getSummaries(Comparator comparator) {
	SortedSet<Summary> lessonSummaries = new TreeSet<Summary>(comparator);
	lessonSummaries.addAll(getAssociatedSummariesSet());
	return lessonSummaries;
    }

    public String prettyPrint() {
	final StringBuilder result = new StringBuilder();

	result.append(getDiaSemana().getDiaSemanaString());
	result.append(", ");
	result.append(getInicio().get(Calendar.HOUR_OF_DAY));
	result.append(":");
	result.append(getInicio().get(Calendar.MINUTE));
	result.append(", ");
	result.append(getSala().getName());

	return result.toString();
    }
}
