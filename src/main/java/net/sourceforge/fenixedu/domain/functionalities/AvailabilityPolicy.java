package net.sourceforge.fenixedu.domain.functionalities;

import java.util.UUID;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;

/**
 * An <code>AvailabilityPolicy</code> allows a functionality to indicate whom
 * may access to the funcitonality.
 * 
 * @author cfgi
 */
public abstract class AvailabilityPolicy extends AvailabilityPolicy_Base {

    protected AvailabilityPolicy() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setContentId(UUID.randomUUID().toString());
    }

    /**
     * Determines if this policy allows the functionality to be available in the
     * given context.
     * 
     * @param context
     *            the context of the functionality
     * 
     * @return <code>true</code> if the functionality is available
     * 
     * @see Functionality#isAvailable(FunctionalityContext)
     */
    public abstract boolean isAvailable(FunctionalityContext context);

    /**
     * Deletes this object from persistent storage.
     */
    public void delete() {
        removeContent();
        removeRootDomainObject();
        deleteDomainObject();
    }

    public abstract Group getTargetGroup();
}
