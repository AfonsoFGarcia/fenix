/*
 * ExistingActionException.java
 *
 * Febuary 28th, 2003, Sometime in the morning
 */
 
package ServidorApresentacao.Action.sop.exceptions;

/**
 *
 * @author  Luis Cruz & Nuno Nunes & Jo�o Mota
 */
import ServidorApresentacao.Action.exceptions.FenixActionException;

public class ExistingActionException extends FenixActionException {

	public static String key = "error.exception.existing";
	

	public ExistingActionException(Throwable cause) {
		super(key, cause);
	}
	
	public ExistingActionException(Object value, Throwable cause) {
		super(key, value, cause);
	}	
	
	public ExistingActionException(Object[] values, Throwable cause) {
		super(key, values, cause);
	}
    

	/**
	 * @return String
	 */
	public static String getKey() {
		return key;
	}

	/**
	 * Sets the key.
	 * @param key The key to set
	 */
	public static void setKey(String key) {
		ExistingActionException.key = key;
	}

}