package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.PaymentType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class JavaPaymentType2SqlPaymentTypeFieldConversion implements FieldConversion {

	public Object javaToSql(Object source) {
		if (source instanceof PaymentType) {
			PaymentType s = (PaymentType) source;
			return s.getType();
		} else {
			return source;
		}
	}

	public Object sqlToJava(Object source) {
		if (source instanceof Integer) {
			Integer src = (Integer) source;
			return new PaymentType(src);
		} else {
			return source;
		}
	}

}