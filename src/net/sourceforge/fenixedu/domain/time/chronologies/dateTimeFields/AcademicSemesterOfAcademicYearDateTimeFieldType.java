package net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields;

import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.chronologies.durationFields.AcademicSemestersDurationFieldType;
import net.sourceforge.fenixedu.domain.time.chronologies.durationFields.AcademicYearsDurationFieldType;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;

public class AcademicSemesterOfAcademicYearDateTimeFieldType extends DateTimeFieldType {

    private static final AcademicSemesterOfAcademicYearDateTimeFieldType ACADEMIC_SEMESTER_OF_ACADEMIC_YEAR;
    static {
	ACADEMIC_SEMESTER_OF_ACADEMIC_YEAR = new AcademicSemesterOfAcademicYearDateTimeFieldType(
		"academicSemesterOfAcademicYear",
		AcademicSemestersDurationFieldType.ACADEMIC_SEMESTERS_TYPE,
		AcademicYearsDurationFieldType.ACADEMIC_YEARS_TYPE);
    }

    private AcademicSemesterOfAcademicYearDateTimeFieldType(String name, DurationFieldType unitType, DurationFieldType rangeType) {
	super(name);
    }

    public static DateTimeFieldType academicSemesterOfAcademicYear() {
	return ACADEMIC_SEMESTER_OF_ACADEMIC_YEAR;
    }
    
    @Override
    public DateTimeField getField(Chronology chronology) {
	if(chronology instanceof AcademicChronology) {
	    return ((AcademicChronology)chronology).academicSemesterOfAcademicYear();
	}	
	throw unsupported();
    }  
    
    @Override
    public DurationFieldType getDurationType() {
	return AcademicSemestersDurationFieldType.academicSemesters();
    }
    
    @Override
    public DurationFieldType getRangeDurationType() {
	return AcademicYearsDurationFieldType.academicYears();
    }
    
    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(ACADEMIC_SEMESTER_OF_ACADEMIC_YEAR + " field is unsupported");
    }
}
