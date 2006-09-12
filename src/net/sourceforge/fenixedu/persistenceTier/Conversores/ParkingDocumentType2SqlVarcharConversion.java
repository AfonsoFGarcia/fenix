package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.parking.ParkingDocumentType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ParkingDocumentType2SqlVarcharConversion implements FieldConversion {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof ParkingDocumentType) {
            final ParkingDocumentType parkingDocumentType = (ParkingDocumentType) obj;
            return parkingDocumentType.toString();
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException {
        ParkingDocumentType parkingDocumentType = null;
        if (obj instanceof String) {
            final String parkingDocumentTypeString = (String) obj;
            return ParkingDocumentType.valueOf(parkingDocumentTypeString);
        }
        return parkingDocumentType;
    }
}
