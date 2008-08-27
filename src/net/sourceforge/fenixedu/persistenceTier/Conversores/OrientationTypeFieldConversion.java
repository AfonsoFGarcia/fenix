/*
 * Created on 16/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.OrientationType;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class OrientationTypeFieldConversion implements FieldConversion {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql
     * (java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException {
	if (obj instanceof OrientationType) {
	    OrientationType orientationType = (OrientationType) obj;
	    return Integer.valueOf(orientationType.getValue());
	}
	return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava
     * (java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException {
	OrientationType orientationType = null;
	if (obj instanceof Integer) {
	    Integer orientationTypeId = (Integer) obj;

	    orientationType = OrientationType.getEnum(orientationTypeId.intValue());

	    if (orientationType == null) {
		throw new IllegalArgumentException(this.getClass().getName() + ": Illegal provider regime type!(" + obj + ")");
	    }
	} else {
	    throw new IllegalArgumentException("Illegal provider regime type!(" + obj + ")");
	}
	return orientationType;
    }
}