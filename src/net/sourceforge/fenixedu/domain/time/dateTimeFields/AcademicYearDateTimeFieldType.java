package net.sourceforge.fenixedu.domain.time.dateTimeFields;

import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.durationFields.AcademicYearsDurationFieldType;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;

public class AcademicYearDateTimeFieldType extends DateTimeFieldType {
    
    public static final AcademicYearDateTimeFieldType ACADEMIC_YEAR_TYPE;
    
    static {
	ACADEMIC_YEAR_TYPE = new AcademicYearDateTimeFieldType("academicYear");
    }
    
    private AcademicYearDateTimeFieldType(String name) {
	super(name);	
    }

    @Override
    public DateTimeField getField(Chronology chronology) {
	if(chronology instanceof AcademicChronology) {
	    return ((AcademicChronology)chronology).academicYear();
	}
	return null;
    }
    
    @Override
    public DurationFieldType getDurationType() {	
	return AcademicYearsDurationFieldType.ACADEMIC_YEARS_TYPE;
    }   

    @Override
    public DurationFieldType getRangeDurationType() {	
	return null;
    }
}
