/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.util.HourMinuteSecond;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

/**
 * @author Jo�o Mota
 * @author Susana Fernandes
 * 
 * 21/Jul/2003 fenix-head Dominio
 * 
 */
public class Summary extends Summary_Base {

    public static final Comparator<Summary> COMPARATOR_BY_DATE_AND_HOUR = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_DATE_AND_HOUR).addComparator(new BeanComparator(
		"summaryDateYearMonthDay"), true);
	((ComparatorChain) COMPARATOR_BY_DATE_AND_HOUR).addComparator(new BeanComparator(
		"summaryHourHourMinuteSecond"), true);
	((ComparatorChain) COMPARATOR_BY_DATE_AND_HOUR).addComparator(new BeanComparator("idInternal"));
    }

    private Summary() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Summary(MultiLanguageString title, MultiLanguageString summaryText, Integer studentsNumber,
	    Boolean isExtraLesson, Professorship professorship, String teacherName, Teacher teacher,
	    Shift shift, Lesson lesson, YearMonthDay date, OldRoom room, Partial hour) {

	this();
	setInfoToSummary(title, summaryText, studentsNumber, isExtraLesson, professorship, teacherName,
		teacher, shift, lesson, date, room, hour);
    }

    public void edit(MultiLanguageString title, MultiLanguageString summaryText, Integer studentsNumber,
	    Boolean isExtraLesson, Professorship professorship, String teacherName, Teacher teacher,
	    Shift shift, Lesson lesson, YearMonthDay date, OldRoom room, Partial hour) {

	setInfoToSummary(title, summaryText, studentsNumber, isExtraLesson, professorship, teacherName,
		teacher, shift, lesson, date, room, hour);
    }

    private void setInfoToSummary(MultiLanguageString title, MultiLanguageString summaryText,
	    Integer studentsNumber, Boolean isExtraLesson, Professorship professorship,
	    String teacherName, Teacher teacher, Shift shift, Lesson lesson, YearMonthDay date,
	    OldRoom room, Partial hour) {

	checkParameters(title, summaryText, isExtraLesson, professorship, teacherName, teacher, shift,
		lesson, date, room, hour);
	checkTeacher(teacher, shift.getDisciplinaExecucao());
	checkDate(date, shift.getDisciplinaExecucao().getExecutionPeriod(), lesson);
	setExecutionCourse(shift.getDisciplinaExecucao());
	setTitle(title);
	setSummaryText(summaryText);
	setStudentsNumber(studentsNumber);
	setIsExtraLesson(isExtraLesson);
	setProfessorship(professorship);
	setTeacherName(teacherName);
	setTeacher(teacher);
	setShift(shift);
	setSummaryDateYearMonthDay(date);
	setLastModifiedDateDateTime(new DateTime());
	setSummaryType(shift.getTipo());
	if (isExtraLesson) {
	    HourMinuteSecond hourMinuteSecond = new HourMinuteSecond(hour.get(DateTimeFieldType
		    .hourOfDay()), hour.get(DateTimeFieldType.minuteOfHour()), 0);
	    setSummaryHourHourMinuteSecond(hourMinuteSecond);
	    setRoom(room);
	    removeLesson();
	} else {
	    setLesson(lesson);
	    setRoom(lesson.getSala());
	    setSummaryHourHourMinuteSecond(lesson.getBeginHourMinuteSecond());
	}
    }

    private void checkDate(YearMonthDay date, ExecutionPeriod period, Lesson lesson) {
	if (lesson != null) {	 
	    Summary summaryByDate = lesson.getSummaryByDate(date);
	    if (summaryByDate != null && !summaryByDate.equals(this)) {
		throw new DomainException("error.summary.already.exists");
	    }
	    if (!lesson.isDateValidToInsertSummary(date)) {
		throw new DomainException("error.summary.no.valid.date.to.lesson");
	    }
	    if (!lesson.isTimeValidToInsertSummary(new HourMinuteSecond())) {
		throw new DomainException("error.summary.no.valid.time.to.lesson");
	    }	    
	} else if (date.isAfter(new YearMonthDay()) || date.isBefore(period.getBeginDateYearMonthDay())) {
	    throw new DomainException("error.summary.no.valid.date");
	}
    }

    private void checkTeacher(Teacher teacher, ExecutionCourse executionCourse) {
	if (teacher != null && teacher.getProfessorshipByExecutionCourse(executionCourse) != null) {
	    throw new DomainException("error.summary.teacher.is.executionCourse.professorship");
	}
    }

    private void checkParameters(MultiLanguageString title, MultiLanguageString summaryText,
	    Boolean isExtraLesson, Professorship professorship, String teacherName, Teacher teacher,
	    Shift shift, Lesson lesson, YearMonthDay date, OldRoom room, Partial hour) {

	if (title == null || title.getAllContents().isEmpty()) {
	    throw new DomainException("error.summary.no.title");
	}
	if (summaryText == null || summaryText.getAllContents().isEmpty()) {
	    throw new DomainException("error.summary.no.summaryText");
	}
	if (shift == null) {
	    throw new DomainException("error.summary.no.shift");
	}
	if (date == null) {
	    throw new DomainException("error.summary.no.date");
	}
	if (professorship == null && StringUtils.isEmpty(teacherName) && teacher == null) {
	    throw new DomainException("error.summary.no.teacher");
	}
	if (isExtraLesson) {
	    if (room == null) {
		throw new DomainException("error.summary.no.room");
	    }
	    if (hour == null) {
		throw new DomainException("error.summary.no.hour");
	    }
	} else {
	    if (lesson == null) {
		throw new DomainException("error.summary.no.lesson");
	    }
	}
    }

    public void delete() {
	removeExecutionCourse();
	removeProfessorship();
	removeRoom();
	removeShift();
	removeTeacher();
	removeLesson();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public String getOrder() {
	StringBuilder stringBuilder = new StringBuilder();
	Lesson lesson = getLesson();
	if (lesson != null) {
	    List<YearMonthDay> allLessonDates = lesson.getAllLessonDates();
	    if (!allLessonDates.isEmpty()) {
		int index = allLessonDates.indexOf(getSummaryDateYearMonthDay());
		if (index != -1) {
		    return stringBuilder.append("(").append(index + 1).append("/").append(
			    allLessonDates.size()).append(")").toString();
		}
	    }
	}
	return "";
    }

    @Override
    public OldRoom getRoom() {
	if (getLesson() != null) {
	    return getLesson().getSala();
	}
	return super.getRoom();
    }

    @Override
    public HourMinuteSecond getSummaryHourHourMinuteSecond() {
	if (getLesson() != null) {
	    return getLesson().getBeginHourMinuteSecond();
	}
	return super.getSummaryHourHourMinuteSecond();
    }
}
