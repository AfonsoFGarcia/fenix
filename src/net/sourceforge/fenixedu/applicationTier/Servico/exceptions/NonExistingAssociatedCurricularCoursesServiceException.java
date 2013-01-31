package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class NonExistingAssociatedCurricularCoursesServiceException extends FenixServiceException {

	public NonExistingAssociatedCurricularCoursesServiceException() {
		super();
	}

	public NonExistingAssociatedCurricularCoursesServiceException(Throwable cause) {
		super(cause);
	}

	public NonExistingAssociatedCurricularCoursesServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String toString() {
		String result = "[NonExistingAssociatedCurricularCoursesServiceException\n";
		result += "message" + this.getMessage() + "\n";
		result += "cause" + this.getCause() + "\n";
		result += "]";
		return result;
	}

}