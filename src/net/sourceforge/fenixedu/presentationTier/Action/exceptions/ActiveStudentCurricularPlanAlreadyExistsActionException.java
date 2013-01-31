package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ActiveStudentCurricularPlanAlreadyExistsActionException extends FenixActionException {

	public static String key = "error.exception.existingActiveStudentCurricularPlan";

	public ActiveStudentCurricularPlanAlreadyExistsActionException(Throwable cause) {
		super(key, cause);
	}

	public ActiveStudentCurricularPlanAlreadyExistsActionException(Object value, Throwable cause) {
		super(key, value, cause);
	}

	public ActiveStudentCurricularPlanAlreadyExistsActionException(String key, Throwable cause) {
		super(key, cause);
	}

	public ActiveStudentCurricularPlanAlreadyExistsActionException(Object[] values, Throwable cause) {
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
	 * 
	 * @param key
	 *            The key to set
	 */
	public static void setKey(String key) {
		ActiveStudentCurricularPlanAlreadyExistsActionException.key = key;
	}

	@Override
	public String toString() {
		String result = "[ExistingActionException\n";
		result += "property" + this.getProperty() + "\n";
		result += "error" + this.getError() + "\n";
		result += "cause" + this.getCause() + "\n";
		result += "]";
		return result;
	}
	// TODO find a way of internationalizing the message passed as argument to
	// the exception error message of the resource bundle
}