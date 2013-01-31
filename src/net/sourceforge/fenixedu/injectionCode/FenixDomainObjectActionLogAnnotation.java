package net.sourceforge.fenixedu.injectionCode;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author <a href="mailto:manuel.pinto@ist.utl.pt">Manuel Pinto</a> <br/>
 * <br/>
 * <br/>
 *         Created on 9:11:59,24/Nov/2005
 * @version $Id$
 */

@Retention(RUNTIME)
@Target({ METHOD, CONSTRUCTOR })
public @interface FenixDomainObjectActionLogAnnotation {
	String actionName();

	String[] parameters();
}
