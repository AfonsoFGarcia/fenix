package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * This class represents an exception thrown when an invalid service is
 * persistentSupportecified.
 * 
 * @author Joao Pereira
 * @version
 */

public class InvalidServiceException extends RuntimeException {

    public InvalidServiceException() {
    }

    public InvalidServiceException(String s) {
        super(s);
    }
}