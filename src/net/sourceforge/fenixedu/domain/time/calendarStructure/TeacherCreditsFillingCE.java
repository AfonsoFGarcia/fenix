package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class TeacherCreditsFillingCE extends TeacherCreditsFillingCE_Base {

    protected TeacherCreditsFillingCE() {
	super();
    }

    @Override
    public AcademicCalendarEntry edit(MultiLanguageString title, MultiLanguageString description, DateTime begin, DateTime end,
	    AcademicCalendarRootEntry rootEntry, AcademicCalendarEntry templateEntry) {

	throw new DomainException("error.unsupported.operation");
    }

    @Override
    public void delete(AcademicCalendarRootEntry rootEntry) {
	throw new DomainException("error.unsupported.operation");
    }

    @Override
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
	throw new DomainException("error.unsupported.operation");
    }

    public void edit(DateTime begin, DateTime end) {
	setTimeInterval(begin, end);
    }

    @Override
    protected boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd) {
	return false;
    }

    @Override
    protected boolean isPossibleToChangeTimeInterval() {
	return false;
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {
	return true;
    }

    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
	return !parentEntry.isAcademicSemester();
    }

    @Override
    public boolean isOfType(AcademicPeriod period) {
	return false;
    }

    @Override
    public boolean isTeacherCreditsFilling() {
	return true;
    }

    public boolean containsNow() {
	return !getBegin().isAfterNow() && !getEnd().isBeforeNow();
    }

    @Override
    protected boolean associatedWithDomainEntities() {
	return false;
    }
}
