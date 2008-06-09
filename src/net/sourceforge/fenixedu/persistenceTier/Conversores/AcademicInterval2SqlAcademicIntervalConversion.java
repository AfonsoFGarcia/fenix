/*
 * Created on Jan 26, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class AcademicInterval2SqlAcademicIntervalConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
	if (source instanceof AcademicInterval) {
	    AcademicInterval academicInterval = (AcademicInterval) source;	    
	    return academicInterval.getRepresentationInStringFormat();
	}
	return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
	if (source == null || source.equals("")) {
	    return null;
	}
	if (source instanceof String) {	    	    
	    return AcademicInterval.getAcademicIntervalFromString((String) source);	    	   
	}
	return source;
    }
}
