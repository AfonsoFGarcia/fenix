/*
 * Created on 26/Mar/2003
 *
 * 
 */
package ServidorApresentacao.Action.exceptions;

/**
 * @author Jo�o Mota
 *
 * 
 */
public class InvalidArgumentsActionException extends FenixActionException{

	public static String key = "error.exception.nonExisting";
	/**
	 * 
	 */
	public InvalidArgumentsActionException(Throwable cause) {
		super(key, cause);
	}
	
	public InvalidArgumentsActionException(Object value, Throwable cause) {
		super(key, value, cause);
	}	
	
	public InvalidArgumentsActionException(Object[] values, Throwable cause) {
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
	
	public String toString() {
			String result = "[InvalidArgumentsException\n";
			result += "property" +this.getProperty()+ "\n";
			result += "error" +this.getError()+ "\n";
			result += "cause" +this.getCause()+ "\n";
			result += "]";
			return result;
		}

}
