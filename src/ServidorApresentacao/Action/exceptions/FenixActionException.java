/*
 * ExistingPersistentException.java
 *
 * Febuary 28th, 2003, Sometime in the morning
 */

package ServidorApresentacao.Action.exceptions;

import org.apache.struts.action.ActionError;

/**
 *
 * @author Luis Cruz & Nuno Nunes & Jo�o Mota
 */

// Note: When upgrading to struts 1.1 rc1 or above change
// extends declaration to ModuleException and remove already
// implemented methods and constructors.

public class FenixActionException extends Exception {

	protected String property = "error.default";
	protected ActionError error = null;

	// --- End of Variable Declarations ----------------------------------

	public FenixActionException() {
		super("error.default");
		error = new ActionError("error.default");}

	public FenixActionException(String key) {
		super(key);
		error = new ActionError(key);
	}

	public FenixActionException(String key, Object value) {
		super(key);
		error = new ActionError(key, value);
	}

	public FenixActionException(String key, Object value0, Object value1) {
		super(key);
		error = new ActionError(key, value0, value1);
	}

	public FenixActionException(
		String key,
		Object value0,
		Object value1,
		Object value2) {
		super(key);
		error = new ActionError(key, value0, value1, value2);
	}

	public FenixActionException(
		String key,
		Object value0,
		Object value1,
		Object value2,
		Object value3) {
		super(key);
		error = new ActionError(key, value0, value1, value2, value3);
	}

	public FenixActionException(String key, Object[] values) {
		super(key);
		error = new ActionError(key, values);
	}

	public FenixActionException(String key, Throwable cause) {
		super(key, cause);
		error = new ActionError(key);
	}

	public FenixActionException(String key, Object value, Throwable cause) {
		super(key, cause);
		error = new ActionError(key, value);
	}

	public FenixActionException(
		String key,
		Object value0,
		Object value1,
		Throwable cause) {
		super(key, cause);
		error = new ActionError(key, value0, value1);
	}

	public FenixActionException(
		String key,
		Object value0,
		Object value1,
		Object value2,
		Throwable cause) {
		super(key, cause);
		error = new ActionError(key, value0, value1, value2);
	}

	public FenixActionException(
		String key,
		Object value0,
		Object value1,
		Object value2,
		Object value3,
		Throwable cause) {
		super(key, cause);
		error = new ActionError(key, value0, value1, value2, value3);
	}

	public FenixActionException(String key, Object[] values, Throwable cause) {
		super(key, cause);
		error = new ActionError(key, values);
	}
	
	public FenixActionException(Throwable cause) {
			super(cause);
		}

	// --- End of Constructores ------------------------------------------
	public String toString() {
		String result = "[FenixActionException\n";
		result += "property" +this.getProperty()+ "\n";
		result += "error" +this.getError()+ "\n";
		result += "cause" +this.getCause()+ "\n";
		result += "]";
		return result;
	}

	public String getProperty() {
		return (property != null) ? property : error.getKey();
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public ActionError getError() {
		return error;
	}

}