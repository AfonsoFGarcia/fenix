/*
 * Created on 19/Ago/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author Susana Fernandes
 */
public class CorrectionAvailability2EnumCorrectionAvailabilityFieldConversion implements FieldConversion {

    /**
     *  
     */

    public Object javaToSql(Object arg0) throws ConversionException {
	if (arg0 instanceof CorrectionAvailability) {
	    CorrectionAvailability ca = (CorrectionAvailability) arg0;
	    return ca.getAvailability();
	}
	return arg0;

    }

    /**
     *  
     */

    public Object sqlToJava(Object arg0) throws ConversionException {
	if (arg0 instanceof Integer) {
	    Integer availability = (Integer) arg0;
	    return new CorrectionAvailability(availability);
	}
	return arg0;

    }

}