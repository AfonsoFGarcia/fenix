package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * 
 * @author João Mota
 */
public class FileNameTooLongServiceException extends FenixServiceException {

	public FileNameTooLongServiceException() {
	}

	public FileNameTooLongServiceException(Throwable cause) {
		super(cause);
	}

	public FileNameTooLongServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String toString() {
		String result = "[FileNameTooLongServiceException\n";
		result += "message" + this.getMessage() + "\n";
		result += "cause" + this.getCause() + "\n";
		result += "]";
		return result;
	}

}